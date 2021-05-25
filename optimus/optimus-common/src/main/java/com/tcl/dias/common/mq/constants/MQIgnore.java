package com.tcl.dias.common.mq.constants;

/**
 * This file contains the MQIgnore.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MQIgnore {

	public static final String[] IGNORE_QUEUES = new String[] { "${mq.docusign.notification.queue}",
			"${mq.sap.minstatus.queue}","${mq.sap.postatus.queue}","${wps.async.account.queue}","${wps.async.order.queue}","${wps.async.invoice.queue}","${sap.grn.response}"};

}
