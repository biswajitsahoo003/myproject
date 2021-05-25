package com.tcl.dias.performance.swagger.constants;

/**
 * 
 * This file contains the SwaggerConstants.java class. This class conatins
 * swagger constants
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class ApiOperations {
		private ApiOperations() {

		}

		public static class Quotes {
			private Quotes() {
				// DO NOTHING
			}

		}

		public static class FileUpload {
			private FileUpload() {

			}

			public static final String UPLOAD_FILE = "Uploading the File";
		}

		public static class FileDownload {
			private FileDownload() {

			}

			public static final String DOWNLOAD_FILE = "File download with attachment id";
		}

		public static class UserManagement {
			private UserManagement() {

			}

			public static final String CREATE_ROLE = "This api is user for creating role";
			public static final String CREATE_USER = "This api is user for creating user";
			public static final String DELETE_USER = "This api is user for deleting user";
			public static final String ADD_ROLE = "This api is user for adding role";
			public static final String DELETE_USER_ROLE = "This api is user for deleting role";
			public static final String GET_ROLES = "This api is user for getting roles";
			public static final String UPDATE_USER = "This api is used to update user";
			public static final String DELETE_USER_AUTH = "Delete user in keycloak";
			public static final String GET_USER = "Get user by user id";
			public static final String GET_ALL_USER = "Get all user";
			public static final String CHANGE_PASSWORD_USER = "Changes the password for the user";
			public static final String CREATE_USERGROUP = "Creates user group";
			public static final String UPDATE_USERGROUP = "Update user group";
			public static final String DELETE_USERGROUP = "Delete user group";
			public static final String GET_ALL_USERGROUP = "Get all user group";
		}
		
		public static class Report {
			private Report() {

			}

			public static final String GET_PERFORMANCE_REPORT_DETAILS = "Getting Performance report details like MOS,Jitter and Packet loss";
			public static final String GET_USAGE_REPORT_DETAILS = "Getting Usage report details like MOU,ARS/NER and PDT/ACD";
			public static final String GET_CONCURRENT_BANDWIDTH_REPORT_DETAILS = "Getting concurrent and bandwidth report details";
			public static final String SOURCE_COUNTRY_LIST = "Getting source country list";
			public static final String DST_COUNTRY_LIST = "Getting destination country list";
			public static final String GET_ORIG_LOCATION_LIST = "Getting origin location list";
			public static final String GET_DST_LOCATION_LIST = "Getting destination location list";
			public static final String GET_CONCURRENT_LOCATION_LIST = "Getting concurrent location list";
			public static final String GET_CONFERENCE_USAGE_REPORT = "Getting Conference usage report ";
			public static final String GET_TELCHEMY_QOS_STATS_SUMMARY = "Getting TelchemyQosStatsSummary";

		}
		
		public static class NetworkPerformance{
			private NetworkPerformance() { 

			}
			public static final String NETWORK_PERFORMANCE = "getting network performance";
			
		}

		public static class Performance {
			private Performance() {
			}
			
			public static final String GET_RFO_OUTAGES_REPORT = "This api is fetching all the RFO reports for a gvien month";
			public static final String SEVERITY_TICKETS__MONTHLY_REPORT = "This api is to get all the Severity 1 and 2 tickets for a given month.";
			public static final String SEVERITY_TICKETS_REPORT = "This api is to get all the Severity 1,2,3 tickets for last 6 months.";
			public static final String UPTIME_REPORT = "This api is to get the uptime records of last 6 months";
			public static final String MTTR_REPORTS = "This api is to get the monthly, severity wise MTTR reports.";
			public static final String GET_PERFORMANCE_PDF = "This api is to fetch all the performance reports in a PDF file.";
		}

	}

}
