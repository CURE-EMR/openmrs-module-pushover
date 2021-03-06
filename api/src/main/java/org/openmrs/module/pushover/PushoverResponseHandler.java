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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.pushover.model.PushoverResponse;

public class PushoverResponseHandler implements ResponseHandler<PushoverResponse> {
	
	protected static final Log log = LogFactory.getLog(PushoverResponseHandler.class);
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public PushoverResponse handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
		final StatusLine statusLine = response.getStatusLine();
		final HttpEntity entity = response.getEntity();
		if (statusLine.getStatusCode() != 200) {
			log.warn("Response received with error code: " + statusLine.getStatusCode() + " and reason: "
			        + statusLine.getReasonPhrase());
		}
		
		return entity == null ? null : handleEntity(entity);
	}
	
	private PushoverResponse handleEntity(HttpEntity httpEntity) throws IOException {
		log.debug("Unmarshalling the response contents");
		String json = EntityUtils.toString(httpEntity);
		
		log.debug("\nResponse: " + json);
		return mapper.readValue(json, PushoverResponse.class);
	}
	
}
