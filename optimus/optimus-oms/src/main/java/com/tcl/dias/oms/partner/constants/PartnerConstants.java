package com.tcl.dias.oms.partner.constants;


/**
 * Constants Related to Partner
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerConstants {

    public static final String GSC_PRODUCT = "GSC";
    public static final String GVPN_PRODUCT = "GVPN";
    public static final String ILL_PRODUCT = "IAS";
    public static final String ALL = "ALL";
    public static final String GSIP_PRODUCT = "GSIP";


    public static final String PARTNER_OPPORTUNITY = "Partner_Opportunity";
    public static final String PARTNER_REFERENCE_NAME = "Opportunity";
    public static final String PARTNER_ATTACHMENT_TYPE = "Partner";

    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_UPDATE = "UPDATE";

    public static final String SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE = "Opportunity_Name__r.Partner_Name__r.Customer_Code__c in ";
    public static final String SFDC_SALES_REQUEST_OPTY_ID_WHERE_CLAUSE = "Opportunity_Name__r.Opportunity_ID__c in ";
    public static final String SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE=" Opportunity_Name__r.createddate ";
    public static final String SFDC_SALES_REQUEST_PARTNER_CUSTOMER_CODE_WHERE_CLAUSE = "Opportunity_Name__r.Customer_Contracting_Entity__r.Customer_Code__c in ";
    public static final String SFDC_SALES_REQUEST_CLASSIFICATION_WHERE_CLAUSE = "Opportunity_Name__r.Opportunity_Classification__c = ";
    public static final String SFDC_SALES_REQUEST_OBJECT_NAME = "Products_Services__c";
    public static final String SFDC_SALES_REQUEST_FIELDS = "Opportunity_Name__r.Opportunity_Classification__c, Opportunity_Name__r.Opportunity_ID__c, " +
            "Opportunity_Name__r.Stagename,recordType.Name, Differential_Product_ACV__c, CurrencyISOCode, Opportunity_Name__r.Customer_Contracting_Entity__r.Customer_Code__c, " +
            "Opportunity_Name__r.Customer_Contracting_Entity__r.Name, Opportunity_Name__r.Partner_Name__r.Customer_Code__c," +
            "Opportunity_Name__r.Customer_Contracting_Entity__r.is_Partner_account__c";
    public static final String SFDC_SALES_DOWNLOAD_REQUEST_FIELDS = "Opportunity_Name__r.Opportunity_Classification__c, Opportunity_Name__r.Partner_Name__r.Name, " +
            "Opportunity_Name__r.Portal_Transaction_Id__c, Opportunity_Name__r.Type, Opportunity_Name__r.createddate,Opportunity_Name__r.closedate, Opportunity_Name__r.Term_of_Months__c," +
            " Opportunity_Name__r.Deal_Registration_Status__c, Opportunity_Name__r.Opportunity_ID__c, Opportunity_Name__r.account.Name, " +
            "Opportunity_Name__r.Stagename,recordType.Name, Differential_Product_ACV__c, CurrencyISOCode, Opportunity_Name__r.Customer_Contracting_Entity__r.Customer_Code__c, " +
            "Opportunity_Name__r.Customer_Contracting_Entity__r.Name, Opportunity_Name__r.Partner_Name__r.Customer_Code__c," +
            "Opportunity_Name__r.Customer_Contracting_Entity__r.is_Partner_account__c";
    public static final String SFDC_SALES_REQUEST_SOURCE_SYSTEM = "Catalyst";
    public static final String SFDC_SALES_REQUEST_TRANSACTON_ID = "1000000";

    public static final String SFDC_SALES_RESPONSE_STAGE_ORDER_ACCEPTED = "Order Accepted";
    public static final String SFDC_SALES_RESPONSE_STAGE_ORDER_PROCESSING = "Order Processing";
    public static final String SFDC_SALES_RESPONSE_STAGE_ORDER_DROPPED = "Dropped";
    public static final String SFDC_SALES_RESPONSE_STAGE_IDENTIFIED_OPPORTUNITY = "Identified Opportunity";
    public static final String SFDC_SALES_RESPONSE_STAGE_PROPOSAL_SENT = "Proposal Sent";
    public static final String SFDC_SALES_RESPONSE_STAGE_SHORTLISTED = "Short listed";
    public static final String SFDC_SALES_RESPONSE_STAGE_VERBAL_AGREEMENT = "Verbal Agreement";


    public static final String SFDC_SALES_RESPONSE_TYPE_IAS = "Internet Access Service";
    public static final String SFDC_SALES_RESPONSE_TYPE_GVPN = "Global VPN";
    public static final String SFDC_SALES_RESPONSE_TYPE_GSIP = "Global SIP Connect(GSC)";

    public static final String SFDC_SALES_FUNNEL_RESPONSE_STAGE_ACCEPTED = "orderAccepted";
    public static final String SFDC_SALES_FUNNEL_RESPONSE_STAGE_PROCESSING = "orderProcessing";
    public static final String SFDC_SALES_FUNNEL_RESPONSE_STAGE_DROPPED = "orderDropped";
    public static final String SFDC_SALES_FUNNEL_RESPONSE_STAGE_SELECT_CONFIGURATION = "selectConfiguration";
    public static final String SFDC_SALES_FUNNEL_RESPONSE_STAGE_GET_QUOTE = "getQuote";
    public static final String SFDC_SALES_FUNNEL_RESPONSE_STAGE_ORDER_FORM = "orderForm";

    public static final String SFDC_CAMPAIGN_DETAIL_WHERE_CLAUSE ="IsActive = True AND Business_Units_Involved__c includes ('Partner Campaign')";
    public static final String SFDC_CAMPAIGN_DETAIL_OBJECT_NAME="Campaign";
    public static final String SFDC_CAMPAIGN_DETAIL_REQUEST_FIELDS="Id,Name,IsActive,CreatedDate,CreatedById,LastModifiedDate,LastModifiedById";

    public static final byte STATUS_ACTIVE = (byte) 1;
    public static final byte STATUS_INACTIVE = (byte) 0;
    public static final Byte STATUS_ACTIVE_BYTE = Byte.valueOf(STATUS_ACTIVE);
    public static final Byte STATUS_INACTIVE_BYTE = Byte.valueOf(STATUS_INACTIVE);

    public static final String RELAY_WARE_AUTHENTICATION_USERNAME = "username";
    public static final String RELAY_WARE_AUTHENTICATION_PASSWORD = "password";
    public static final String RELAY_WARE_SESSIONID_HEADER = "sessionid";

    public static final String DNB_AUTHENTICATION_USERNAME = "x-dnb-user";
    public static final String DNB_AUTHENTICATION_PASSWORD = "x-dnb-pwd";
    public static final String DNB_BASIC_AUTHENTICATION = "Basic ";

    public static final String SELL_WITH_CLASSIFICATION = "Sell With";
    public static final String SELL_THROUGH_CLASSIFICATION = "Sell Through";

    public static final String PARTNER = "Partner";

    public static final String SFDC_DEAL_REGISTRATION_OPTY_CODE = "Opportunity_Id__c in ";
    public static final String SFDC_DEAL_REGISTRATION_FIELDS = "Id, Opportunity_ID__c, Deal_Registration_Status__c";
    public static final String SFDC_OPTY_STAGE_FIELDS = "Id, Opportunity_ID__c, Deal_Registration_Status__c, Stagename, Probability";

    public static final String APPROVED = "Approved";
    public static final String PENDING = "Pending";
    public static final String REJECTED = "Rejected";

    public static final String BEARER="Bearer";
    public static final String CLIENT_ID="client_id";
    public static final String CLIENT_SECRET="client_secret";
    public static final String GRANT_TYPE="grant_type";
    public static final String PASSWORD="password";
    public static final String USERNAME="username";
    public static final String AUTHORIZATION="Authorization";
    public static final String SELL_TO ="Sell To";

    public static final String GREATER_THAN =" > ";
    public static final String LESSER_THAN =" < ";

    public  static final String SFDC_TIME_FORMAT="T00:00:00Z";
    public static final String DEAL_NOT_AVAILABLE ="Deal Registration Not Available";
    public static final String OPTY_NOT_AVAILABLE ="Pending";
    public static final String QUOTE_NOT_AVAILABLE ="Quote not Available";

    public static final String OPTIMUS_SOURCE_SYSTEM = "OPTIMUS";

    public  static final String CUSTOMER_ACCOUNT_CREATION_REQEST="Customer Account Creation Request";
    public  static final String DEFAULT_ACCOUNT_RTM="Direct - Ent";

    public  static final String CUSTOMER_ALREADY_EXIST= "Customer Name Already Exists. Please contact partnersupport@tatacommunications.com for further action.";
    public  static final String ACCOUNT_CREATED_SUCCESS= "Your Account/Entity creation request has been created. Please contact partnersupport@tatacommunications.com for further action.";
    public  static final String ACCOUNT_CREATED_ERROR= "Error in Account/Entity creation request";

    public  static final String DEFAULT_FY_SEGMENT="FY21 Unarchived";
    public  static final String PARTNER_ACCOUNT_RTM="Partner";


}
