package com.tcl.dias.ticketing.swagger.constants;

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

	

		public static class Ticketing {
			private Ticketing() {

			}

			public static final String GET_TICKET_DETAILS = "This api is used to get the ticket details";
			public static final String CREATE_TICKETS = "This api is used to create Tickets";

			public static final String GET_PARTICULAR_TICKET_DETAILS = "This api is used to get the ticket details of particular details";
			public static final String UPDATE_TICKET = "This api is used to update ticket";

			
		}
		
		public static class Attachments {
			private Attachments() {

			}

			public static final String GET_ATTACHMENTS = "This api is used to get all the Attachment details";
			public static final String GET_ATTACHMENTS_DETAILS = "This api is used to get the particular Attachment Details";

			public static final String GET_PARTICULAR_TICKET_DETAILS = "This api is used to get the ticket details of particular details";
			public static final String UPDATE_TICKET = "This api is used to update ticket";
			public static final String UPDATE_ATTACHMENTS = "This api is used to update the attachment details";
			public static final String CREATE_ATTACHMENTS = "This Api is used to create Attachments";

			
		}
		
		public static class Service {
			private Service() {

			}

			public static final String GET_SERVICE_DETAILS = "This api is used to get the Service details";
			
		}
		
		public static class Category {
			private Category() {

			}

			public static final String GET_CATEGORY_DETAILS = "This api is used to get the Category details";
			
		}
		
		public static class PlannedOutage {
			private PlannedOutage() {

			}

			public static final String GET_PLANNED_OUTAGE_TICKETS = "This api is used to get all planned outage tickets";

			
		}
		
		public static class ServiceRequestManagement {
			private ServiceRequestManagement() {

			}

			public static final String GET_TICKETS = "This api is used to get all service request tickets";
			public static final String GET_TICKETS_STATUSWISE = "This api is used to get all service request tickets";
			public static final String CREATE_TICKETS = "This api is used to create tickets in Service Request";
			public static final String GET_TICKET_BY_ID = "This api is used to get ticket by the requested ticket id";
			public static final String UPDATE_TICKET_BY_ID = "This api is used to update a ticket by the requested ticket id";
			public static final String FILTERS = "This API is used to filter the data";
		}
		public static class ServiceCatalog {
			private ServiceCatalog() {

			}

			public static final String GET_SERVICE_CATALOG_INFO = "This api is used to get service catalog details";
			public static final String GET_SERVICE_CATALOG_ATTRIBUTE = "This api is used to get service catalog attribute details";
			
		}
		
	}

}
