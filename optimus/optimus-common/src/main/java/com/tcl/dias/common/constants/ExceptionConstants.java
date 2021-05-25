package com.tcl.dias.common.constants;

/**
 * Exception constants
 * 
 * @author Manojkumar R
 *
 */
public class ExceptionConstants {
	private ExceptionConstants() {
	}

	public static final String TOKEN_EXPIRED = "Token Expired";
	public static final String FILE_EMPTY = "file.empty";
	public static final String FAILED_TO_UPLOAD = "failed.to.upload";
	public static final String COMMON_ERROR = "common.exception";
	public static final String OBJECT_ALREADY_EXISTS = "object.already.exists";

	public static final String CUSTOMER_LE_ERROR = "customer.le.exception";
	public static final String DATE_ERROR = " date.exception";
	public static final String ERROR_FECTHING_CUSTOMERS_FOR_USER = "customer.exception";
	public static final String ERROR_FETCHING_USERINFO = "userinfo.exception";
	public static final String ERROR_FETCHING_CUSTOMER_LEIDS = "error.fetching.customer.LeIds";
	public static final String REQUEST_INVALID = "Request.invalid";
	
	public static final String INACTIVE_SFDC_RECORD = "inactive.sfdc.record";
	
	public static final String SI_ORDER_RETRIEVAL_ERROR = "si.order.retrieval.error";
	public static final String SI_SERVICEDETAIL_RETRIEVAL_ERROR = "si.servicedetail.retrieval.error";
	//Exception messages for sfdc failure
	public static final String ERROR_FETCHING_SFDCFESIBILITY_RESPONSE = "Error while fetching sfdc create feasibility response from db";
	public static final String ERROR_PROCESSING_SFDC_REQUEST = "Error While Processing SFDC request";
	public static final String ERROR_PROCESSING_OMS_SFDC_FEAS_REQ = "oms.sfdc.feasibility.request.failure";
	public static final String ERROR_TRIGGER_CREATE_FEASIBLITY="Runtime exception occured in Create Feasiblity SFDC call";
	public static final String RUNTIME_EXCEPTION_ON_CMD_CRON="Runtime exception occured on CMD Cron job";
	

	//Partner SFDC sales funnel constants - start
	public static final String SFDC_SALES_FUNNEL_MQ_ERROR= "sfdc.sales.funnel.mq.error";
	public static final String SFDC_SALES_FUNNEL_MQ_EMPTY= "sfdc.sales.funnel.mq.empty";
	
    public static final String RESOURCE_RELEASE_INITIATED_NOT_COMPLETE ="resource.release.initiated.not.complete";
	public static final String PRODUCT_LOCATION_MQ_ERROR= "product.location.mq.error";
	public static final String PRODUCT_LOCATION_MQ_EMPTY= "product.location.mq.empty";
	//Partner SFDC sales funnel constants - end

}
