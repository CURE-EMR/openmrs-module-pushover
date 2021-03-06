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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PushoverConstants {
	
	public static final String MODULE_ID = "pushover";
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String HEADER_ACCEPT = "Accept";
	
	public static final String HEADER_AUTH = "Authorization";
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	public static final String PARAMS_ID_SCHEMES = "orgUnitIdScheme=CODE";
	
	public static final String CONTENT_TYPE_JSON = "application/json";
	
	public static final String CONTENT_TYPE_XML = "application/xml";

    public static final String CODE_SYSTEM_CIEL = "2.16.840.1.113883.3.7201";
	
	public static final String CODE_SYSTEM_NAME_CIEL = "CIEL";
	
	public static final String DHIS2_CODE_ATTRIB_TYPE_UUID = "66bfca6e-75af-11e8-a395-ec86dab4f7b9";
	
	//TODO should probably be a GP
	public static final String CIEL_CODE_NEW_HIV_CASE = "138571";
	
	public static final String PERSON_ATTRIBUTE_TYPE_UUID = "ab1e48f4-5528-11e8-9ed7-c05155b794c3";
	
	public static final String TRIGGER_CONCEPT_SOURCE = "SNOMED CT";
	
	public static final String TRIGGER_CONCEPT_CODE = "410658008";
	
	public static final String GP_CR_ENCOUNTER_TYPE_NAME = MODULE_ID + ".caseReportEncounterTypeName";
	
	public static final String GP_URL = MODULE_ID + ".dhis2Url";
	
	public static final String GP_USERNAME = MODULE_ID + ".username";
	
	public static final String GP_PASSWORD = MODULE_ID + ".password";
	
	public static final String GP_TRACKED_ENTITY_TYPE_UID = MODULE_ID + ".trackedEntityTypeUID";
	
	public static final String GP_PROGRAM_UID = MODULE_ID + ".programUID";
	
	public static final String GP_ATTRIB_FIRSTNAME_UID = MODULE_ID + ".firstnameUID";
	
	public static final String GP_ATTRIB_MIDDLENAME_UID = MODULE_ID + ".middlenameUID";
	
	public static final String GP_ATTRIB_LASTNAME_UID = MODULE_ID + ".lastnameUID";
	
	public static final String GP_ATTRIB_GENDER_UID = MODULE_ID + ".genderUID";
	
	public static final String GP_ATTRIB_BIRTHDATE_UID = MODULE_ID + ".birthdateUID";
	
	public static final String GP_ATTRIB_PERSON_ID_UID = MODULE_ID + ".personIdUID";
	
	public static final String GP_ATTRIB_DATE_OF_HIV_DIAGNOSIS_UID = MODULE_ID + ".dateOfHivDiagnosisUID";
	
	public static final String GP_OPTION_FEMALE_UID = MODULE_ID + ".maleOptionCode";
	
	public static final String GP_OPTION_MALE_UID = MODULE_ID + ".femaleOptionCode";
	
}
