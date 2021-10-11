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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.event.Event;
import org.openmrs.event.EventListener;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class PushoverActivator extends BaseModuleActivator implements DaemonTokenAware{
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private EventListener eventListener;
	
	private static final Class<Encounter> CLAZZ = Encounter.class;
	
	private static final Event.Action ACTION = Event.Action.CREATED;
	
	private DaemonToken daemonToken;
	
	/**
	 * @see DaemonTokenAware#setDaemonToken(DaemonToken)
	 */
	@Override
	public void setDaemonToken(DaemonToken daemonToken) {
		this.daemonToken = daemonToken;
	}
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Started Pushover");
		
		eventListener = new OrderEventListener(daemonToken);
		Event.subscribe(CLAZZ, ACTION.name(), eventListener);
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown Pushover");
		
		Event.unsubscribe(CLAZZ, ACTION, eventListener);
	}
	
}
