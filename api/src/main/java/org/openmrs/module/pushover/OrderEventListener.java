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

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.pushover.util.PushoverUtils;

public class OrderEventListener implements EventListener {
	
	protected Log log = LogFactory.getLog(getClass());
	
	private DaemonToken daemonToken;
	
	public OrderEventListener(DaemonToken daemonToken) {
		this.daemonToken = daemonToken;
	}
	
	/**
	 * @see EventListener#onMessage(Message)
	 */
	@Override
	public void onMessage(final Message message) {
		log.debug("Received order created event");
		
		Daemon.runInDaemonThread(new Runnable() {
			
			/**
			 * @see Runnable#run()
			 */
			@Override
			public void run() {
				
				try {
					//TODO in theory we want to save this event to the DB
					//before we submit it for broadcasting purposes and also just in case the
					//submission fails we can try again later.
					//saveOrderEvent();
					ProcessorResult result = processMessage(message);
					log.info("Order creation processor result: " + result);
				}
				catch (Exception e) {
					log.error("An error occurred while processing order creation", e);
				}
				
			}
			
		}, daemonToken);
		
	}
	
	/**
	 * Processes the specified JMS message
	 *
	 * @param message the message to process
	 * @return a {@link ProcessorResult}
	 * @throws JMSException
	 */
	public ProcessorResult processMessage(Message message) throws JMSException {
		log.debug("Processing JMS message");
		MapMessage mm = (MapMessage) message;
		String encUuid = mm.getString("uuid");
		Order order = Context.getOrderService().getOrderByUuid(encUuid);
		String caseReportEncType = PushoverUtils.getCaseReportEncounterTypeName();
		String orderTypeName = order.getOrderType().getName();
		if (!caseReportEncType.equals(orderTypeName)) {
			log.debug("Ignoring non tracked order with order type: " + orderTypeName);
			return ProcessorResult.IGNORED;
		}
		log.debug("Processing case report encounter with encounter type: " + orderTypeName);
		
		return EncounterProcessor.newInstance().process(order);
	}
	
}
