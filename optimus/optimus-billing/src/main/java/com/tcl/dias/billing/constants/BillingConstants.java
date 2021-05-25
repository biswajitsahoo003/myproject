package com.tcl.dias.billing.constants;

/**
 * Billing & Invoices related constants
 * 
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class BillingConstants {

	private BillingConstants() {
	}
	
	public static final String INVOICE_DOWNLOAD_SUCCESSFUL = "Requested Invoice(s) is downloaded successfully";
	public static final String FETCH_PAYMENT_DATA = "Fetched Payment datasuccessfully";

	public static final String TICKET_AUTH_MESSAGE = "Ticket number: %s cannot be verified against authorized SAP codes";
	public static final String SELECT_INVOICES = "select invoice(s)";
	public static final String NO_SAP_CODES_FOUND = "No associated SAP code not found";
	public static final String NO_SAP_CODE_FOUND_FOR_INVOICENO =  "Invoice Number cannot be verified - No associated SAP code";
	
	
	// misc constants
	
	
	public static final String FILEPATH = "filePath";
	public static final String FILENAME = "fileName";
	public static final String SOURCE =  "source";
	public static final String CBF= "CBF";
	public static final String GBS = "GBS";
	public static final String SMB_PREFIX ="smb://";

	public static final String PARTNER = "Partner";
	public static final String GENEVA = "Geneva";

	
}
