package com.tcl.dias.l2oworkflow.swagger.constants;

/**
 * Swagger Constants related information
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SwaggerConstants {

    interface ApiOperations {
        interface Filter{
        	String GET_CUSTOM_FILTER_DETAILS="Lists all the custom filter save by user";
			String SAVE_CUSTOM_FILTER_DETAILS = "Saves custom filter details";
			String DELETE_CUSTOM_FILTER_DETAILS = "Delete custom filter details";
			String FILTER_TASK_BY_ASSIGNED_GROUP = "List all the tasks for given assigned groups";
		}

        interface DASHBOARD {
            public static String GET_SERVICE_INFO = "Get Service Info for given sc Order Id";
            public static String GET_SERVICEDETAIL_INFO = "Get Service Info for given Service Id";
            public static String GET_ORDER_DASHBOARD_COUNT = "Get the Stage count by given scOrderId";
            public static String GET_PROCESS_TASK_LOGS = "Get the task log informations from given scOrderId";
        }
        
        interface Task {
			public static final String GET_TASK_COUNT = "Used to get the task count";
			public static final String GET_USER_DETAILS = "User to get all the user";
			public static final String UPLOAD_VMI = "upload vmi template";

			public static final String GET_LATEST_ACTIVITY = "Used to get the latest activity";
			public static final String GET_TASK_DETAILS = "Used to get the task Details";
			public static final String GET_TASK_DETAILS_BASED_ON_FILTER = "Used to get the task Details based on filter";

			public static final String ASSIGN_TASK = "used to assign the task";
			public static final String GET_TASK_DETAILS_BASED_ON_STATUS = "Used to get the task details fetched for a groupname and group them by status";
			public static final String GET_TASK_BASED_ON_ID = "Used to get the task based on a provided id";
			public static final String DELAY_TASK_DETAILS = "used to get delay task";
			public static final String INITIATE_DAILY_TRACKING = "initiate daily tracking changes";
			public static final String INITIATE_CUSTOMER_DELAY_TRACKING = "initiate customer delay tracking changes";
			
			public static final String GET_MF_SUPPORT_DETAILS = "Get Details for Mf Support Group";
			public static final String CREATE_TASK = "Create Task For A Team";
			
			public static final String GET_ASSIGNED_TASK = "Get Assigned Task for the siteCode";
			public static final String EDIT_TASK_STATUS = "Used to edit manual feasibility task status";
			public static final String GET_TASK_SUMMARY = "Get Task Summary";
			public static final String CREATE_RESPONSE_SAVE = "Crete response - Save";
			public static final String GET_SUPPLIER_IOR = "Get Supplier IOR List";
			public static final String GET_MF_TASK_DETAILS = "Get Manual feasibility task details";
			public static final String SELECT_MF_RESPONSE = "Select best mf response using priority matrix";
			public static final String DELETE_MF_TASK = "Delete a Manual Feasibility task";
			public static final String GET_MF_POP_DATA = "Get Mf Pop Data";
			public static final String CREATE_RESPONSE_DELETE = "Create response - Delete";
			public static final String GET_MF_BTS_DATA = "Get Manual feasibility Bts Data";
			public static final String GET_MF_VENDOR_DATA = "Get Mf Vendor Data";
			public static final String GET_MF_PROVIDER_DATA = "Get Mf Provider Data";
			public static final String GET_MF_HAND_HOLD_DATA = "Get Mf hand hold data";
			public static final String GET_FEASIBILITY_CATEGORY = "Get Feasibility Category";
            public static final String DOWNLOAD_EXCEL = "Download Excel for group";
            public static final String GET_GROUP_LIST = "Get group name list";
            public static final String REASSIGN_TASK = "API to reassign task";
            public static final String ASSIGN_TASK_TRAIL = "API to get assign task trail";
            public static final String SAVE_CUSTOMER_LAT_LONG = "API to save customer latitude and longitute";
            public static final String GET_TASK_DETAIL_FOR_OMS = "Get Task Detail For Oms View";

            public static final String MF_UPLOAD_REPORT="Upload l2o report for MF response";
			public static final String DOWNLOAD_FILE_DOC = "used to download document";
			public static final String MF_DELETE_REPORT = "Delete MF - L2O Report";
			public static final String GET_TASK_DETAIL_FETCH = "Get Task Details for feasibility workbench update";



		}
    }

}
