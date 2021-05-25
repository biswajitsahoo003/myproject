package com.tcl.dias.customer.swagger.constants;

/**
 * 
 * This file contains the SwaggerConstants details.
 * 
 *
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class Customer {
		private Customer() {
		}

		public static final String GET_CUSTOMER_LEGAL_ENTITY_BY_ID = "Get the Customer Legal Entities based on Customer Id";
		public static final String GET_CUSTOMER_ID_TRIGGER_EMAIL = "Get the customer email id to trigger email";

		public static final String GET_BILLING_DETAILS = "Get the billing details";
		public static final String GET_CUSTOMER_CONATCT_INFO = "Get Customer contact Info";
		public static final String GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID = "Get the Customer Legal Entities based on Customer Legal Entity Id";
		public static final String GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_ID_AND_SEVICE = "Get the Customer Legal Entities based on Customer Legal Entity Id And Service";
		public static final String GET_CUSTOMER_LEGAL_ENTITY_DETAILS_BY_LEGAL_ENTITY_ID = "Get the Customer Legal Entity Details based on Customer Legal Entity Id";
		public static final String ATTACHMENT_DEATILS = "attachment details based on attachment id";
		public static final String GET_OMS_BILING_DETAILS = "used to get oms billing details for Oms";
		public static final String GET_MSA_DOCUMENT = "used to get Msa document";
		public static final String UPLOAD_MSA_DOC = "used to upload Msa Document";
		public static final String DOWNLOAD_FILE_DOC = "used to download Document";
		public static final String UPLOAD_FILE_DOC = "used to upload Document";
		public static final String GET_TEMP_URL_UPLOAD = "get temporary url for object storage";
		public static final String GET_LE_STATE_GST = "used to get gst number for state";
		public static final String ADD_LE_STATE_GST = "used to add gst number for state";
		public static final String GET_ALL_CUSTOMER = "Used to get all active customer";
		public static final String GET_ALL_LE_FOR_CUSTOMER = "Used to get all le for customer";
		public static final String CUSTOMER_LEGAL_ENTITY = "used to get customer legal entity based on country";
		public static final String CUSTOMER_LEGAL_DATA_CENTER = "used to get customer legal entity data center";
		public static final String EDIT_BILLING_DETAILS = "Edit the billing details";
		public static final String GET_CURRENCY_DETAILS = "Get Currency details";		
		public static final String GET_TEMP_DOWNLOAD_URL = "Get the temporary download url to donwload documents stored in storage container";
		public static final String UPDATE_DOCUMENT_UPLOADED_DETAILS = "Update the document related details after the document is stored in storage container";
		public static final String GENERATE_PASSCODE = "Generate the passcode to create the path in storage container to upload documents related to the legal entity";
		public static final String GET_GST_FOR_CUSTOMER_LEGAL_ENTITY = "Get GST number for a customer legal entity";
		public static final String SAVE_OR_UPDATE_LE_GST = "Used to save or update GST number for the customer legal entity";
		public static final String MOVE_FILES_TO_OBJECT_STORAGE = "Moving Files from file system to object storage";
		
		public static final String DOWNLOAD_GSC_OUTBOUND_PRICE_DOCUMENT = "Download outboung price document for GSC";
		public static final String UPDATE_CUSTOMER_LEGAL_ENTITY_CONTACT = "Used to update the contact information of the customer legal entity";
		public static final String SEARCH_CUSTOMER = "Search Customer";
		public static final String GET_CUSTOMER_LIST = "Get customers for list of customer id";
		public static final String GET_PARTNER_LEGAL_ENTITIES = "Get the partner legal entities";
		public static final String GET_CUSTOMER_DETAILS = "Get customerDetails";
		public static final String GET_ALL_CUSTOMER_LE="Get all customer legal entity";
		public static final String GET_ALL_CUSTOMER_PAGE="Get all customer";
		public static final String GET_ALL_SPLE_IZOSDWAN="Get all supplier details based on countries mapped to the quote";
		public static final String GET_CUSTOMER_CONTRACTING_ENTITY_DETAILS_BY_lE_ID = "Get the Customer Contracting Entities based on Customer Legal Entity Id";
		public static final String GET_CUSTOMER_LE_VERIFICATION="To check whether customer legal entity is verified";
		public static final String GET_PARTNER_END_CUSTOMER_NAME = "Get Partner's end customer name list";
		
		public static final String UPLOAD_SUPPORT_MATRIX = "API to upload support matrix document";
		public static final String GET_CUSTOMER_TEAM_MEMBER="Set all customer team members based on Customer ID and Team Role";

		
	}

	public static class Partner {
		private Partner() {
		}
		public static final String GET_CALLIDUS_INFO = "Get Callidus Info";
		public static final String UPLOAD_PARTNER_DOCUMENTS = "Upload required partner onboarding documents";
		public static final String DOWNLOAD_PARTNER_DOCUMENTS = "Download required partner onboarding documents";
		public static final String GET_PARTNER_NNI_ID = "Get Partner NNI ID";
		public static final String GET_PARTNER_PRODUCT_CLASSIFICATION = "Get Partner product classification by ID";
		public static final String GET_PARTNER_PROFILE = "Get Partner profile by Partner ID";
		public static final String GET_SUPPLIER_LE_BY_PARTNER_LE = "Get Supplier le by Partner Le ID";
		}
}
