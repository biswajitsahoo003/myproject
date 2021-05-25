package com.tcl.dias.nso.swagger.constants;

/**
 * 
 * Swagger constants
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class Customer {
		private Customer() {
		}
	}
	
	public static class Feedback {
		private Feedback() {
		}
		public static final String GET_FEEDBACK_DETAILS = "Get all Feedbacks";
		public static final String GET_PAGE_FEEDBACK_DETAILS = "Get Feedbacks per page";
		public static final String SAVE_FEEDBACK_DETAILS= "Save Feedback";
	}
	
	public static class ApiOperations {

		private ApiOperations() {

		}

		public static class Quotes {
			private Quotes() {
				// DO NOTHING
			}

			public static final String GET_EXCEL_TEMPLATE = "download the template for macd ill";
			public static final String GET_BY_PINCODE = "Get the Details by the given pincode";
			public static final String ADD_LOCATION = "Add location details";
			public static final String UPDATE_LOCATION = "Update location details by id";
			public static final String GET_LOCATION = "Get location details by given id";
			public static final String GET_LOCATIONS = "Get location details by given list of id's";
			public static final String UPDATE_ILLSITES = "Update the illsites based on the quote id";
			public static final String GET_SITE_INFORMATION = "Get site information based on the quote id";
			public static final String GET_BOM_DETAILS_BY_QUOTE_ID = "Create quote based on user input";
			public static final String GET_TOTAL_CHARGES_QUOTE_ID = "Get total charges by quote Id";
			public static final String CREATE_DOCUMENT = "Create Document";
			public static final String GET_USER_DETAILS = "Get user details";
			public static final String GET_USER_SITE_DETAILS = "Get user site details";
			public static final String GET_TAX_EXCEMPTED_SITES = "Get only taxExcempted site";
			public static final String GET_SITE_INFO_BY_ID = "Get site info by site id";
			public static final String DELETE_SITE_INFO = "Delete site info";
			public static final String CREATE_QUOTE = "create the quote";
			public static final String UPDATE_SITE = "update the site information to the given quote";
			public static final String SHARE_QUOTE = "Share quote to the input email";
			public static final String UPDATE_LINK = "update the link information to the given quote";
			public static final String GET_QUOTE = "get the quote configuration";
			public static final String DELETE_OR_DISABLE_SITE = "delete or disable the site information for the given quote";
			public static final String GET_TERMS_AND_CONDITIONS = "Get the terms and conditions for the given product name";
			public static final String GET_ALL_USER_DETAILS = "Get all user details based on the customer id";
			public static final String TRIGGER_EMAIL = "Trigger Email to the delegated user";
			public static final String GET_DELEGATED_USERS = "Get the user from quote delegation based on the status";
			public static final String GET_QUOTE_SUMMARY = "Get all the quote summay details";
			public static final String GET_PRODUCT_SOLUTION = "Get Product solution details";
			public static final String EDIT_SITES = "Edit Sites";
			public static final String APPROVE_QUOTES = "Approve Sites";
			public static final String QUOTES_DOCUSIGN = "Docusign for quotes";
			public static final String SFDC_GET = "Get Sfdc Audit";
			public static final String SFDC_TRIGGER = "Trigger Sfdc";
			public static final String FEASIBILITY_CHECK = "Feasibility check";
			public static final String DASH_BOARD_DETAILS = "Get dashboard details";
			public static final String GET_QUOTES_BASED_ON_CUSTOMER = "Get quotes based on customer";
			public static final String SAVE_SITE_PROPERTIES = "Save site properties";
			public static final String GET_SITE_PROPERTIES_ONLY = "Get site properties only";
			public static final String SAVE_LEGALENTITY_ATTRIBUTE = "Save legal entity attribute";
			public static final String GET_ALL_DETAILS_BY_QUOTETOLEID = "Get all attribute details by quote to le id";
			public static final String UPDATE_CURRENCY_IN_QUOTE_TO_LE = "Update the currency code in quote to le";

			public static final String TRIGGER_FEASIBILITY = "Trigger components for feasibility engine";
			public static final String SFDC_UPDATE_STAGE = "Update the sfdc stage";
			public static final String SAVE_FEASIBILITY = "Save the feasible sites";
			public static final String UPDATE_BILLING_INFO_FOR_SFDC = "used to update billing info into oms for sfdc process";
			public static final String TRIGGER_PRICING = "Trigger components for pricing engine";

			public static final String DELETE_OR_DISABLE_LINK = "API to delete or disable link";
			public static final String GET_LINK_INFO_BY_ID = "Get link info by link id";

			public static final String GET_CONTACT_ATTRIBUTE = "used to get contact attribute";
			public static final String UPDATE_SITE_PROPERTIES = "used to update site list of properties ";
			public static final String CURRENCY_CONVERSION = "Convert payment currency into required currency format";
			public static final String EDIT_LINKS = "Edit Links";
			public static final String GET_USER_LINK_DETAILS = "Get user link details";
			public static final String DELETE_NPL_QUOTE = "Delete NPL quote and order details";

			public static final String CREATE_UPDATE_DELETE_ATTRIBUTES = "used to create/update/delete product component attributes";

			public static final String GET_QUOTE_PRICE_AUDIT = "Get Quote Price audit component wise";
			public static final String DELETE_OR_DISABLE_SOLUTION_AND_SITE = "API to delete or disable solution and sites";
			public static final String GET_SLA_DETAILS = "Get sla details";
			public static final String GET_OUTBOUND_DETAILS = "Get outbound details";
			public static final String GET_OUTBOUNG_SURCHARGE_PRICES = "Get outbound surcharge prices";
			public static final String TRIGGER_MAIL_GSC = "Trigger Mail Notification";
			public static final String CHECK_MSA_SSA_DOCUMENTS = "Check MSA and SSA Documents";
			public static final String UPDATE_COF_UPLOADED_DETAILS = "Update the details related to the cof uploaded to the storage container";
			public static final String DOWNLOAD_COF_STORAGE = "Download the cof document from storage container";

			public static final String GET_QUOTE_PAGE_CURRENCY = "Get quote page currency";
			public static final String GET_FEASIBLITY_PRICING_DETAILS = "Get feasibility and pricing details based on the given site id input";
			public static final String GET_MACD_ORDER_SUMMARY = "Get order summary of the macd quote based on the quote id and service id input";
			public static final String GET_COMPARED_QUOTES = "Get the NRC and ARC and their components' prices";

			public static final String MOVE_FILES_OBJECT_STORAGE = "Move files from file system to Object storage";

			public static final String UPDATE_USER_DETAILS = "Update the User details";
			public static final String SITE_NOT_FEASIBLE = "Site is not Feasible";
			public static final String REMOVE_UNSELECTED_SOLUTION = "Remove Unselected Solution from Quote";
			public static final String UPDATE_UPLOAD_LINK = "update the upload links information to the given quote";
			public static final String UPADTE_PARTNER_ENTITY_LOCATION = "update the partner legal entity location";

			public static final String SAVE_ASK_PRICE = "Save Ask price into site properties";
			public static final String GET_DISCOUNT_DETAILS = "Get discount details for edited price.";
			public static final String DELETE_OR_DISABLE_QUOTE_CLOUD = "delete or disable the cloud product information for the given quote";
			public static final String ADD_QUOTE_CLOUD = "add new cloud product for the given quote";
			public static final String ADD_QUOTE_COMPONENT = "Add new Quote Component";
			public static final String DELETE_QUOTE_COMPONENT = "Delete new Quote Component";
			public static final String ADD_QUOTE_COMPONENT_ATTR = "Add or Update new Quote Component Attribute";
			public static final String DELETE_QUOTE_COMPONENT_ATTR = "Delete new Quote Component Attibute";
			public static final String UPDATE_SOLUTION_NAME = "Update Solution Name";
			public static final String VALIDATE_ORDERFORM = "Validate Order Form";
			public static final String TRIGGER_WORKFLOW = "Trigger Workflow";
			public static final String UPLOAD_SOLUTION_DOCUMENT = "Upload solution document";
			public static final String DOWNLOAD_SOLUTION_DOCUMENT = "Download solution document";
			public static final String GET_SITE_DOCUMENTS_QUOTE_LE = "Get Site document namse for the quoteToLe";

			public static final String GET_OPPORTUNITY_DETAILS = "Get Opportunity Details";
			public static final String CREDITCHECK_RETRIGGER = "Credit Check Retrigger";
			public static final String GET_SERVICE_REQUEST = "Get Service Request";
			public static final String UPLOAD_UPGRADE_EXCEL = "Upload Bulk Upgrade Details";
			public static final String SET_BILLING_PARAMETERS = "Set Billing Parameters";


		}

}
}
