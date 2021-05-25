package com.tcl.dias.servicefulfillmentutils.constants;

/**
 * IPCServiceFulfillmentConstant class is to define IPC service acceptance related constants.
 * 
 *
 * @author Danish
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class IPCServiceFulfillmentConstant {

	public static final String IPC = "IPC";

	public static final String SERVICE_ACCEPTED = "ipcServiceAccepted";	
	public static final String SERVICE_ACCEPTANCE_TIMEOUT = "ipcServiceAcceptanceTimeout";
	
	public static final String SERVICE_ISSUE_ITERATION = "serviceIssueIteration";
	public static final String SERVICE_ISSUE = "serviceIssue";
	public static final String SERVICE_ISSUES_LOG = "serviceIssueLog";

	public static final String SERVICE_ISSUE_ORDER = "order";
	public static final String SERVICE_ISSUE_LOGIN = "login";
	public static final String SERVICE_ISSUE_VM_CONFIG = "vmConfig";
	public static final String SERVICE_ISSUE_INTERNET = "internet";
	public static final String SERVICE_ISSUE_VPN = "vpn";
	public static final String SERVICE_ISSUE_BACKUP = "backup";
	public static final String SERVICE_ISSUE_OTHER = "other";

	public static final String IS_RESOLVED = "isResolved";
	public static final String COMMENT = "comment";
	public static final String TIMESTAMP = "timestamp";

	public static final String SERVICE_RESOLUTION = "serviceResolution";
	public static final String SERVICE_RESOLUTION_LOG = "serviceResolutionLog";

	public static final String ORDER_STAGE_SERVICE_ACCEPTANCE = "SERVICE_ACCEPTANCE";
	
	public static final String INTIAL_ORDER_ACCEPTANCE_DURATION = "P5D"; // 5 DAYS
	//public static final String INTIAL_ORDER_ACCEPTANCE_DURATION = "PT30M";
	public static final String ISSUE_FLOW_ORDER_ACCEPTANCE_DURATION = "P2D"; // 2 DAYS
	//public static final String ISSUE_FLOW_ORDER_ACCEPTANCE_DURATION = "PT30M";
	
	public static final String DC = "DC";
	public static final String DR = "DR";
	public static final String DC_ORDER_ID = "DC_ORDER_ID";
	public static final String DC_SERVICE_ID = "DC_SERVICE_ID";
	
	private IPCServiceFulfillmentConstant() {}

}
