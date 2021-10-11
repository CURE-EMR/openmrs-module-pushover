/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pushover;

import static org.apache.http.HttpStatus.SC_OK;
import static org.openmrs.module.pushover.PushoverConstants.CONTENT_TYPE_JSON;
import static org.openmrs.module.pushover.PushoverConstants.CONTENT_TYPE_XML;
import static org.openmrs.module.pushover.PushoverConstants.HEADER_ACCEPT;
import static org.openmrs.module.pushover.PushoverConstants.HEADER_AUTH;
import static org.openmrs.module.pushover.PushoverConstants.HEADER_CONTENT_TYPE;
import static org.openmrs.module.pushover.PushoverConstants.PARAMS_ID_SCHEMES;
import static org.openmrs.module.pushover.PushoverHttpClient.ResponseResult.FAILURE;
import static org.openmrs.module.pushover.PushoverHttpClient.ResponseResult.SUCCESS;
import static org.openmrs.module.pushover.PushoverHttpClient.ResponseResult.SUCCESS_CONFLICTS;
import static org.openmrs.module.pushover.PushoverHttpClient.ResponseResult.SUCCESS_FAILED_ENROLL;
import static org.openmrs.module.pushover.model.ImportSummary.STATUS_ERROR;
import static org.openmrs.module.pushover.model.ImportSummary.STATUS_SUCCESS;

import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openmrs.api.APIException;
import org.openmrs.module.pushover.model.Conflict;
import org.openmrs.module.pushover.model.ImportSummary;
import org.openmrs.module.pushover.model.PushoverResponse;
import org.openmrs.module.pushover.util.PushoverUtils;

public class PushoverHttpClient {
	
	protected static final Log log = LogFactory.getLog(PushoverHttpClient.class);
	
	public static final String SUB_PATH_API = "api/";
	
	public static final String RESOURCE_TRACKED_ENTITY_INSTANCES = SUB_PATH_API + "trackedEntityInstances";
	
	public static final String RESOURCE_EVENTS = SUB_PATH_API + "events";
	
	enum ResponseResult {
		SUCCESS, FAILURE, SUCCESS_CONFLICTS, SUCCESS_FAILED_ENROLL
	}
	
	private PushoverHttpClient() {
	}
	
	public static PushoverHttpClient newInstance() {
		return new PushoverHttpClient();
	}
	
	/**
	 * Sends the specified events to Pushover
	 *
	 * @param data the json data to post
	 * @return true if the event are successfully sent otherwise false;
	 */
	public boolean sendEvents(String data) throws IOException {
		PushoverResponse response = post(RESOURCE_EVENTS, data, false);
		return getResult(response) != SUCCESS;
	}
	
	public PushoverResponse post(String resource, String jsonContent, boolean isRegistration) throws IOException {
		log.debug("Posting data to Pushover");
		
		String url = PushoverUtils.getUrl();
		String username = PushoverUtils.getUsername();
		String password = PushoverUtils.getPassword();
		url = url.endsWith("/") ? url : url + "/";
		HttpPost post = new HttpPost(url + resource + "?" + PARAMS_ID_SCHEMES);
		post.setEntity(new StringEntity(jsonContent));
		post.addHeader(HEADER_ACCEPT, CONTENT_TYPE_JSON);
		post.addHeader(HEADER_CONTENT_TYPE, isRegistration ? CONTENT_TYPE_JSON : CONTENT_TYPE_XML);
		String authToken = Base64.encodeBase64String((username + ":" + password).getBytes());
		post.addHeader(HEADER_AUTH, "Basic " + authToken);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			return httpclient.execute(post, new PushoverResponseHandler());
		}
		catch (IOException e) {
			log.error("An error occurred while submitting data to Pushover", e);
			throw e;
		}
		finally {
			try {
				httpclient.close();
			}
			catch (IOException e) {
				log.error("An error occurred while closing http client", e);
			}
		}
	}
	
	private ResponseResult getResult(PushoverResponse response) {
		ImportSummary importResp = response.getResponse();
		if (SC_OK != response.getHttpStatusCode() || STATUS_ERROR.equals(importResp.getStatus())) {
			List<Conflict> conflicts = response.getResponse().getImportSummaries().get(0).getConflicts();
			if (CollectionUtils.isNotEmpty(conflicts)) {
				log.error(StringUtils.join(conflicts, ", "));
			}
			return FAILURE;
		} else if (SC_OK == response.getHttpStatusCode() || STATUS_SUCCESS.equals(importResp.getStatus())) {
			List<Conflict> conflicts = response.getResponse().getImportSummaries().get(0).getConflicts();
			if (CollectionUtils.isNotEmpty(conflicts)) {
				log.error(StringUtils.join(conflicts, ", "));
				return SUCCESS_CONFLICTS;
			}
			
			ImportSummary enrollments = importResp.getImportSummaries().get(0).getEnrollments();
			ImportSummary enrollSummary = enrollments.getImportSummaries().get(0);
			if (STATUS_ERROR.equals(enrollments.getStatus()) || STATUS_ERROR.equals(enrollSummary.getStatus())) {
				log.warn("Enrollment failed even though the patient might have been registered in DHIS2");
				List<Conflict> enrollConflicts = enrollSummary.getConflicts();
				if (CollectionUtils.isNotEmpty(enrollConflicts)) {
					log.error(StringUtils.join(enrollConflicts, ", "));
				}
				return SUCCESS_FAILED_ENROLL;
			}
			return SUCCESS;
		}
		
		throw new APIException("There was an unknown problem in DHIS2 when registering and enrolling the patient");
	}
	
}
