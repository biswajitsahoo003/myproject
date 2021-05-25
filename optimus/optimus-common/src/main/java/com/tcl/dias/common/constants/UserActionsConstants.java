package com.tcl.dias.common.constants;

/**
 * This file contains the UserActionsConstants.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class UserActionsConstants {

	public static final String OPT_L2O_ILL_CUSTOM_PROFILE = "OPT_L2O_ILL_CUSTOM_PROFILE";
	public static final String OPT_L2O_ORDER_CREATION = "OPT_L2O_ORDER_CREATION";
	public static final String OPT_L2O_CUSTOMER_ORDER_CREATE = "OPT_L2O_CUSTOMER_ORDER_CREATE";
	public static final String OPT_L2O_VIEW_ALL_FEASIBILITIES = "OPT_L2O_VIEW_ALL_FEASIBILITIES";
	public static final String OPT_OMS_VIEW_QUOTES = "OPT_OMS_VIEW_QUOTES";
	public static final String OPT_OMS_VIEW_ORDERS = "OPT_OMS_VIEW_ORDERS";
	public static final String OPT_OMS_USER_VIEW = "OPT_OMS_USER_VIEW";
	public static final String USER_IMP_RO = "USER_IMP_RO";
	public static final String OPT_MYACC_PROFILE_VIEW = "OPT_MYACC_PROFILE_VIEW";
	public static final String OPT_MYACC_MYSITES = "OPT_MYACC_MYSITES";
	public static final String OPT_COMMERCIAL_TASK="OPT_COMMERCIAL_TASK";
	public static final String OB_ACTION="OB_ACTION";
	public static final String MF_ACTION = "OPT_MF_TASK";
	public static final String OPT_O2C_TD_ASSIGN = "OPT_O2C_TD_ASSIGN";
	public static final String OPT_O2C_TASK_DASHBOARD = "OPT_O2C_TASK_DASHBOARD";
	public static final String OPT_O2C_AT_ASSIGN = "OPT_O2C_AT_ASSIGN";
	public static final String OPT_GROUP_CONFIGURE="OPT_GROUP_CONFIGURE";
	public static final String OPT_NS_ORDER_CREATE="OPT_NS_ORDER_CREATE";
	public static final String OPT_MULTI_CUS_VIEW="OPT_MULTI_CUS_VIEW";

	public static final String[] CUSTOMER_ACCESS_GROUP = { OPT_L2O_CUSTOMER_ORDER_CREATE, OPT_L2O_ILL_CUSTOM_PROFILE,
			OPT_L2O_ORDER_CREATION, OPT_L2O_VIEW_ALL_FEASIBILITIES, OPT_MYACC_MYSITES, OPT_MYACC_PROFILE_VIEW, MF_ACTION,OPT_NS_ORDER_CREATE }; // added mf_action for temp testing
	public static final String[] MISC_ACCESS_GROUP = {OPT_MULTI_CUS_VIEW };
	public static final String[] INTERNAL_ACCESS_GROUP = { OPT_OMS_USER_VIEW, OPT_OMS_VIEW_ORDERS,
			OPT_OMS_VIEW_QUOTES, OPT_O2C_TD_ASSIGN, OPT_O2C_TASK_DASHBOARD, OPT_O2C_AT_ASSIGN, OPT_L2O_ORDER_CREATION ,OPT_GROUP_CONFIGURE};
	public static final String[] OB_GROUP = { OB_ACTION };
	
	public static final String[] COMMERCIAL_ACCESS_GROUP = { OPT_COMMERCIAL_TASK,MF_ACTION }; // added mf_action for temp testing
	
	public static final String[] MANUAL_FEASIBILTIY_GROUP = {MF_ACTION};


	public static final String USR_MODULE_DELETE = "USR_MODULE_DELETE";
	public static final String USR_ACTION_READ = "USR_ACTION_READ";
	public static final String USR_ROLE_DELETE = "USR_ROLE_DELETE";
	public static final String USR_MODULE_READ = "USR_MODULE_READ";
	public static final String USR_USER_READ = "USR_USER_READ";
	public static final String USR_USER_CREATE = "USR_USER_CREATE";
	public static final String USR_ROLE_READ = "USR_ROLE_READ";
	public static final String USR_ROLE_CREATE = "USR_ROLE_CREATE";
	public static final String USR_MODULE_CREATE = "USR_MODULE_CREATE";
	public static final String USR_APPLICATION_DELETE = "USR_APPLICATION_DELETE";
	public static final String USR_ACTION_CREATE = "USR_ACTION_CREATE";
	public static final String USR_USER_DELETE = "USR_USER_DELETE";
	public static final String USR_ACTION_DELETE = "USR_ACTION_DELETE";

	public static final String[] USER_ACCESS_GROUP = { USR_MODULE_DELETE, USR_ACTION_READ, USR_ROLE_DELETE,
			USR_MODULE_READ, USR_USER_CREATE, USR_USER_READ, USR_ROLE_READ, USR_ROLE_CREATE, USR_MODULE_CREATE,
			USR_APPLICATION_DELETE, USR_ACTION_CREATE, USR_USER_DELETE, USR_USER_DELETE, USR_ACTION_DELETE

	};

    public static final String OPT_PARTNER_DASHBOARD_WRITE = "OPT_PARTNER_DASHBOARD_WRITE";
    public static final String OPT_PARTNER_COMMISSIONS_READ = "OPT_PARTNER_COMMISSIONS_READ";
    public static final String OPT_PARTNER_COMMISSIONS_NO_ACCESS = "OPT_PARTNER_COMMISSIONS_NO_ACCESS";
    public static final String OPT_PARTNER_LMS_READ = "OPT_PARTNER_LMS_READ";
    public static final String OPT_PARTNER_BILLING_WRITE = "OPT_PARTNER_BILLING_WRITE";
    public static final String OPT_PARTNER_BILLING_READ = "OPT_PARTNER_BILLING_READ";
    public static final String OPT_PARTNER_BILLING_NO_ACCESS = "OPT_PARTNER_BILLING_NO_ACCESS";
    public static final String OPT_PARTNER_TICKETING_WRITE = "OPT_PARTNER_TICKETING_WRITE";
    public static final String OPT_PARTNER_TICKETING_READ = "OPT_PARTNER_TICKETING_READ";
    public static final String OPT_PARTNER_TICKETING_NO_ACCESS = "OPT_PARTNER_TICKETING_NO_ACCESS";
    
    public static final String USER_IMP_ALL="USER_IMP_ALL";
    public static final String USER_IMP_BASIC="USER_IMP_BASIC";
    public static final String USER_IMP_SALES="USER_IMP_SALES";

    public static final String[] PARTNER_ACCESS_GROUP = {
            OPT_PARTNER_DASHBOARD_WRITE,
            OPT_PARTNER_COMMISSIONS_READ,
            OPT_PARTNER_COMMISSIONS_NO_ACCESS,
            OPT_PARTNER_LMS_READ,
            OPT_PARTNER_BILLING_WRITE};
    public static final String[] CUSTOMER_SUPPORT_ACCESS_GROUP = {"L2O_CUSTOMER_PORTAL_SUPPORT"};
    public static final String[] CUSTOMER_REPORT_ACCESS_GROUP = {"L2O_CUSTOMER_PORTAL_REPORTS"};
    public static final String[] CUSTOMER_BILLING_ACCESS_GROUP = {"L2O_CUSTOMER_PORTAL_BILLING"};
}
