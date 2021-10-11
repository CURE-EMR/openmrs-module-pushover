/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pushover.util;



import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pushover.PushoverConstants;

public class PushoverUtils {
	
	public static String getUrl() {
		return getGlobalProperty(PushoverConstants.GP_URL);
	}
	
	public static String getUsername() {
		return getGlobalProperty(PushoverConstants.GP_USERNAME);
	}
	
	public static String getPassword() {
		return getGlobalProperty(PushoverConstants.GP_PASSWORD);
	}
		
	public static String getCaseReportEncounterTypeName() {
		String mapping = getGlobalProperty(PushoverConstants.GP_CR_ENCOUNTER_TYPE_NAME);
		if (StringUtils.isBlank(mapping)) {
			throw new APIException(PushoverConstants.GP_CR_ENCOUNTER_TYPE_NAME + " global property value is required");
		}
		
		return mapping;
	}
	
	/**
	 * Convenience method that gets the value of the specified global property name
	 *
	 * @param propertyName the global property name
	 * @return the global property value
	 */
	private static String getGlobalProperty(String propertyName) {
		return Context.getAdministrationService().getGlobalProperty(propertyName);
	}
	
}
