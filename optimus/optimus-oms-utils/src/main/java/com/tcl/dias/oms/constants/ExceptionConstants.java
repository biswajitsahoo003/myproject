package com.tcl.dias.oms.constants;

/**
 * 
 * This file contains the ExceptionConstants.java class. This constant file have
 * all the constants
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ExceptionConstants {
	private ExceptionConstants() {
		// DO NOTHING

	}

	public static final String QUOTE_VALIDATION_ERROR = "quote.validation.error";
	public static final String QUOTE_CLOUD_VALIDATION_ERROR = "quote.cloud.validation.error";
	public static final String QUOTE_SOLUTION_VALIDATION_ERROR = "quote.solution.validation.error";
	public static final String USER_VALIDATION_ERROR = "user.validation.error";
	public static final String DOCUMENT_VALIDATION_ERROR = "document.validation.error";
	public static final String CUSTOMER_EMPTY = "customer.empty";
	public static final String REQUEST_VALIDATION = "request.validation.error";
	public static final String MST_PRODUCT_EMPTY = "mst.product.empty";
	public static final String PRODUCT_SOLUTION_EMPTY = "product.solution.empty";
	public static final String MST_PRODUCT_OFFERING_EMPTY = "mst.product.offering.empty";
	public static final String ILL_SITE_EMPTY = "ill.site.empty";
	public static final String QUOTE_EMPTY = "quote.empty";
	public static final String PRODUCT_EMPTY = "product.empty";
	public static final String COMPONENT_EMPTY = "component.empty";
	public static final String ATTRIBUTE_EMPTY = "attribute.empty";
	public static final String PRODUCT_OFFERING_EMPTY = "productoffering.empty";
	public static final String COMMON_ERROR = "common.exception";
	public static final String ILL_VALIDATION_ERROR = "illsites.validation.error";
	public static final String SITE_INFORMATION_ERROR = "siteinformation.validation.error";
	public static final String MST_PRODUCT_NAME_EMPTY = "mstproductfamily.name.empty.validation.error";
	public static final String INVALID_ATTRIBUTE_ERROR = "quote.invalid.attribute";
	public static final String INVALID_COMPONENT_ERROR = "quote.invalid.component";
	public static final String UNAUTHORIZED_REQUEST = "request.unauthorized";
	public static final String ATTACHMENT_SAVE_FAILED="aatachment.save.failed";
	public static final String ACTION_VALIDATION_ERROR = "action.validation.error";
	public static final String ATTACHMENT_VALIDATION_ERROR = "attachment.validation.error";
	public static final String ORDER_EMPTY = "order.empty";
	public static final String ORDER_ALREADY_EXISTS = "order already exists for the quote";
	public static final String ORDER_TO_LE_VALIDATION_ERROR = "order.to.le.validation.error";
	public static final String QUOTE_TO_LE_VALIDATION_ERROR = "quote.to.le.validation.error";
	public static final String ORDER_ILLSITE_VALIDATION_ERROR = "order.illsite.validation.error";
	public static final String INVALID_INPUT = "invalid.input";
	public static final String LE_PRODUCT_SLA = "le.product.sla.error";
	public static final String ORDER_SITE_STATUS_VALIDATION_ERROR= "order.site.status.validation.error";
	public static final String ORDER_SITE_STAGE_VALIDATION_ERROR= "order.site.stage.validation.error";
	public static final String RESOURCE_NOT_EXIST = "resource.not.exist";
	public static final String CUSTOMER_EMAIL_EMPTY = "customer.email.empty";
	public static final String USER_EMPTY = "user.empty";
	public static final String PRICING_VALIDATION_ERROR = "pricing.validation.error";
	public static final String ONLY_ONE_HUB_AVAILABLE = "only.one.hub.is.there";
	public static final String ALL_ARE_HUB_AVAILABLE = "all.are.hub";
	public static final String MST_ORDER_SITE_STAGE_EMPTY="stage.empty";
	public static final String MST_ORDER_SITE_STATUS_EMPTY="status.empty";
	public static final String MULTIPLE_ORDER_LE_ATTR_VALUES = "multiple.orderleattr.error";
	public static final String CUSTOM_FEASIBILITY_VALIDATIION = "custom.feasibility.validation";
	public static final String RESULTANT_CURRENCY_VALIDATION_ERROR = "output.currency.validation.error";
	public static final String EXISTING_CURRENCY_NULL = "currency.null.error";
	public static final String EXCEPTION_STREAM_LOOP = "exception.stream.loop";
	public static final String PRICING_FAILURE = "Pricing.failed.with.exception";
	public static final String USER_EXISTS_ERROR = "user.already.exists.error";
	public static final String BLACKLISTED_ACCOUNT_ERROR = "blacklisted.account.error";
	public static final String CREDITCHECK_RETRIGGER_ERROR = "credit.check.retrigger.error";
	public static final String CREDITCHECK_LE_PREAPPROVED_FLAG_ERROR = "credit.check.le.preapprovedflag.error";
	public static final String CREDITCHECK_DATA_INVALID = "credit.check.data.invalid";

	public static final String TEXT_SEARCH="TEXT WAS EMPTY";
	public static final String COMMON_GET_QUOTE_ERROR = "common.quote.get.exception";
	public static final String GET_QUOTE_VALIDATION_ERROR = "get.quote.validation.error";
	public static final String DETAILS_NOT_FOUND="Details was not found";

	public static final String GET_ATTRIBUTES_ERROR = "get.attributes.error";
	public static final String BANDWIDTH_UNIT_ERROR = "bandwidth.unit.error";
	public static final String SAVE_SLA_ERROR = "save.sla.error";
	public static final String GET_ILLSITE_ERROR = "get.illsite.error";
	public static final String PRICING_FAILURE_EXCEPTION = "pricing.failed.exception";
	public static final String GET_LINK_ERROR = "get.link.error";
	public static final String JOBS_EMPTY = "jobs.empty.error";

	public static final String SI_ORDER_RETRIEVAL_ERROR="si.order.retrival.error";

	public static final String LINKID_NOT_FOUND = "linkid.not.found";
	
	public static final String STATE_EMPTY = "state.empty";

	// NPL related constants - start
	public static final String NPL_LINK_UNAVAILABLE = "npl.link.uavailable";
	public static final String NPL_SITE_EMPTY = "npl.site.empty";
	public static final String NPL_LINK_EMPTY = "npl.link.empty";
	public static final String ORDER_LINK_VALIDATION_ERROR = "order.nlplink.validation.error";
	public static final String ORDER_LINK_STATUS_VALIDATION_ERROR= "order.link.status.validation.error";
	public static final String ORDER_LINK_STAGE_VALIDATION_ERROR= "order.link.stage.validation.error";
	public static final String NPL_VALIDATION_ERROR= "npl.validation.error";
	public static final String NPL_FEASIBILITY_RESPONSE_EMPTY = "npl.feasibility.response.empty";
	public static final String INVALID_LINKID = "invalid.linkid";

	// NPL related constants - end


	//Gsc related constants - start
		public static final String GSC_SUMMERY_EMPTY = "gsc.summery.empty";
		public static final String GSC_ORDER_EMPTY = "gsc.order.empty";
		public static final String QUOTE_GSC_EMPTY = "quote.gsc.empty";
		public static final String PRICING_INPUT_EMPTY = "pricing.input.empty";
		public static final String QUOTE_GSC_VALIDATION_ERROR = "quote.gsc.validation.error";
		public static final String PRICING_RESPONSE_EMPTY = "pricing.response.empty";
		public static final String ORDER_PRODUCT_EMPTY = "order.product.empty";
		public static final String GSC_QUOTE_VALIDATION_ERROR = "gsc.quote.validation.error";
		public static final String CUSTOMER_DETAILS_EMPTY="customer.details.queue.empty";
		public static final String CUSTOMER_LE_DETAILS_EMPTY="customer.le.details.queue.empty";
		public static final String SFDC_VALIDATION_ERROR="sfdc.validation.error";
		public static final String CUSTOMER_NOT_FOUND = "customer.not.found";
		public static final String ASSET_NOT_FOUND = "si.asset.not.found";
		public static final String MACD_ASSET_ID_EMPTY = "macd.asset.id.empty";
		public static final String MACD_DOWNSTREAM_ORDER_ID_EMPTY = "macd.downstream.order.id.empty";
		public static final String MACD_OUTPULSE_EMPTY = "macd.outpulse.empty";
		public static final String MACD_BUSINESS_RULE_REQUEST_TYPE_NOT_ALLOWED = "macd.business.rule.requesttype.not.allowed";
		public static final String COUNTRY_NOT_FOUND = "country.not.found";
	//Gsc related constants - end


		//Macd related constants
		public static final String QUOTE_TYPE_VALIDATION_ERROR="requestType.or.downStreamOrderId.validation.error";
		public static final String MACD_REQUEST_VALIDATION_ERROR="macd.request.validation.error";
		public static final String QUOTE_ID_ERROR = "Quote Id cannot be empty";
		public static final String SERVICE_ID_ERROR = "Service Order Id cannot be empty";
		public static final String TPS_ID_ERROR = "TPS Id cannot be empty";
		public static final String LOCATION_ID_MISMATCH = "location.id.mismatch.error";

	//Patner Opportunity constants - start
	    public static final String PARTNER_OPPORTUNITY_EMPTY = "partner.opportunity.empty";
	    public static final String PARTNER_DOCUMENT_EMPTY = "partner.document.empty";
	    public static final String QUEUE_ERROR = "queue.error";
		public static final String FILE_UPLOAD_ERROR = "file.upload.error";
		public static final String PARTNER_NULL = "partner.null";
		public static final String PARTNER_DOCUMENT_MQ_ERROR= "partner.document.mq.error";
		public static final String PARTNER_DOCUMENT_MQ_EMPTY= "partner.document.mq.empty";
		public static final String PARTNER_LEGAL_ENTITY_MQ_ERROR= "partner.legal.entity.mq.error";
		public static final String PARTNER_LEGAL_ENTITY_MQ_EMPTY= "partner.legal.entity.mq.empty";
		public static final String PARTNER_MQ_ERROR= "partner.mq.error";
		public static final String PARTNER_MQ_EMPTY= "partner.mq.empty";
		public static final String INVALID_PARTNER_ID= "invalid.partner.id";
	//Patner Opportunity constants - end

	//Partner Relay Ware constants - start
		public static final String PARTNER_RELAY_WARE_TRAININGS= "partner.relay.ware.trainings.error";
		public static final String PARTNER_RELAY_WARE_SESSIONID= "partner.relay.ware.sessionId.empty";
	//Partner Relay Ware constants - end

	//Customer Le Details Queue constants - start
		public static final String CUSTOMER_LE_DETAILS_MQ_ERROR= "customer.le.details.mq.error";
		public static final String CUSTOMER_LE_DETAILS_MQ_EMPTY= "customer.le.details.mq.empty";
	//Customer Le Details Queue constants - end

	//DNB constants - start
	public static final String DNB_AUTHENTICATION_ERROR= "dnb.authentication.error";
	public static final String DNB_GET_ENTITY_LIST_ERROR= "dnb.entity.list.error";
	public static final String DNB_GET_ENTITY_DETAILS_ERROR= "dnb.entity.details.error";
	//DNB constants - end

	//Callidus - exception constants - start
	public static final String INVALID_CALLIDUS_COMMISSION_TYPE= "invalid.callidus.commission.type";
	//Callidus - exception constants - end

	public static final String PARTNER_PROFILE_MQ_ERROR= "partner.profile.mq.error";
	public static final String PARTNER_PROFILE_MQ_EMPTY= "partner.profile.mq.empty";

	public static final String CUSTOMER_DETAILS_MQ_ERROR= "customer.details.mq.error";
	public static final String CUSTOMER_DETAILS_MQ_EMPTY= "customer.details.mq.empty";
	public static final String MALFORMED_URL_EXCEPTION = "malformed.url.exception";


	public static final String TASKS_PENDING_FOR_QUOTE = "tasks.pending.for.quote";
	public static final String OPTY_DETAILS_NOT_AVAILABLE = "oppurtunity.details.not.available";
	
	//UCaaS - WEBEX related exceptions - start
	public static final String WEBEX_AUTH_TOKEN_EXCEPTION = "webex.auth.token.error";
	public static final String WEBEX_FORBIDDEN_EXCEPTION = "webex.forbidden.error";
	public static final String WEBEX_URL_NOT_FOUND_EXCEPTION = "webex.url.not.found";
	public static final String WEBEX_NOT_AUTHORIZED_EXCEPTION = "webex.not.authorized.error";
	public static final String WEBEX_CLIENT_TIMEOUT_EXCEPTION = "webex.client.timeout.error";
	public static final String WEBEX_SERVER_UNAVAILABLE_EXCEPTION = "webex.server.unavailable.error";
	public static final String WEBEX_SERVER_TIMEOUT_EXCEPTION = "webex.server.timeout.error";
	public static final String WEBEX_TRY_AFTER_SOMETIME_MESSAGE = "webex.try.after.sometime.message";
	public static final String ORDER_UCAAS_SITE_DETAILS_EMPTY ="order.ucaas.site.details.validation.error";
	//UCaaS - WEBEX related exceptions - end

	//GDE- Schedule
	public static final String MDSO_CREATE_BOD_FAILED = "mdso.create.bod.failed";
	public static final String MDSO_AUTH_TOKEN_GENERATION_FALIED = "mdso.auth.token.generation.failed";
	public static final String MDSO_FEASIBILITY_CHECK_FALIED = "mdso.feasibility.check.failed";
	public static final String MDSO_FEASIBILITY_CHECK_POLL_FAILED = "mdso.feasibility.check.poll.failed";
	public static final String MDSO_OPTIMUS_NOTIFICATION = "mdso.optimus.notification.invalid.operation.id";
	public static final String GDE_ORDER_CANCELLATION_INVALID_INPUT = "gde.order.cancellation.invalid.input";
	public static final String MSDO_RESOURCE_ID_NULL = "mdso.resource.id.null";
	public static final String MDSO_CANCEL_BOD_FAILED = "mdso.cancel.bod.failed";
	public static final String GDE_SERVICE_ID_IS_NULL = "gde.service.id.null";
	public static final String MDSO_RESOURCE_ID_IS_NULL = "mdso.resource.id.null";
	public static final String GDE_BILLING_CURRENCY_NOT_AVAILABLE = "billing.currency.not.available";
	public static final String GDE_ONNET_ARC_NOT_AVAILABLE = "gde.onnet.arc.not.available";

	public static final String GSC_SERVICE_DETAILS_MQ_ERROR= "gsc.service.details.mq.error";
	public static final String GSC_SERVICE_DETAILS_MQ_EMPTY= "gsc.service.details.mq.empty";

	public static final String INVALID_QUOTE_TYPE = "invalid.quote.type";
	public static final String INVALID_QUOTE_CATEGORY = "invalid.quote.category";

	public static final String REQUEST_INVALID = "Request.invalid";
	public static final String CUSTOMER_LE_ID_EMPTY="customer.le.id.empty";


	public static final String VRF_SITE_EMPTY = "vrf.site.empty";

	public static final String EXCEL_VALIDATION_ERROR="excel.validation.error";
	public static final String FEASIBILITY_FAILURE_EXCEPTION = "feasibility.failed.exception";
	
	// For teamsdr
	public static final String QUOTE_TEAMSDR_DETAILS_NOT_FOUND = "quote.teamsdr.details.not.found";
	public static final String QUOTE_TEAMSDR_NOT_FOUND = "quote.teamsdr.not.found";
	public static final String QUOTE_DIRECT_ROUTING_NOT_FOUND = "quote.direct.routing.not.found";
	public static final String ORDER_TEAMSDR_NOT_FOUND = "order.teamsdr.not.found";
	public static final String ORDER_DIRECT_ROUTING_CITY_NOT_FOUND =  "order.direct.routing.city.not.found";
	public static final String ERROR_IN_ZIP_GENERATION = "zip.file.generation.exception";

	//Cancellation
	public static final String INVALID_CUSTOMER_ID_MDM = "invalid.customer.id.mdm";
	public static final String INVALID_CUSTOMER_LEGAL_ENTITY_ID_MDM = "invalid.customer.le.id.mdm";

	public static final String COUNTRY_CURRENCY_ID_BY_LE_MQ_ERROR= "country.currency.id.by.le.mq.error";
	public static final String COUNTRY_CURRENCY_ID_BY_LE_MQ_EMPTY= "country.currency.id.by.le.mq.empty";
	
	public static final String DUPLICATE_CANCELLATION_INITIATED= "duplicate.cancellation.request";
	public static final String POS_VALIDATION_FAILED = "pos.validation.failed";


	//Termination
	public static final String TERMINATION_REQUEST_VALIDATION_ERROR="termination.request.validation.error";
	public static final String O2C_TEN_PERCENT_CALL_ERROR = "o2c.ten.percent.call.error";
	public static final String DUPLICATE_TERMINATION_INITIATED="duplicate.termination.request";

	public static final String REFERENCE_NAME_EMPTY = "reference.name.empty";
	public static final String FILE_EMPTY="file.empty";

	
	public static final String EXCEL_MANDATORY_EMPTY="excel.mandatory.empty.field";
	public static final String EXCEL_MANDATORY_SITE_LEVEL_ACTION_OR_COMMERCIAL_MANAGER_COMMENTS_EMPTY="excel.mandatory.empty.site.level.action.or.commercial.manager.comments";
	public static final String EXCEL_MANDATORY_COMMERCIAL_MANAGER_COMMENTS_EMPTY="excel.mandatory.empty.commercial.manager.comments";

	public static final String INVALID_LM_TYPE_DATA_SERVICE_INVENTORY_ERROR = "invalid.lm.type.data.service.inventory.error";
	public static final String INVALID_LM_PROVIDER_DATA_SERVICE_INVENTORY_ERROR = "invalid.lm.provider.data.service.inventory.error";



	public static final String INVALID_CUSTOMER_ID= "renewals.invalid.customerid";
	public static final String INVALID_LE_ID= "renewals.invalid.leid";
	
    public static final String MF_TASK_TRIGGERED = "mf.task.triggered";
    public static final String ETC_PRICING_ERROR = "etc.pricing.error";

    public static final String MILESTONE_QUOTE_VALIDATION_ERROR = "total.order.value.more.than.parent.cap";
   	public static final String MF_TASK_TRIGGERED_MQ_ERROR= "mf.task.triggered.mq.error";

    public static final String DROP_TERMINATION_QUOTE_ERROR = "drop.termination.quote.error";
    
    public static final String SITE_FEASIBILITY_UNAVAILABLE = "site.feasibility.unavailable";
    public static final String SITE_FEASIBILITY_ROW_UNAVAILABLE= "site.feasibility.row.unavailable";
    
    public static final String QUOTE_NOT_ELIGIBLE_FOR_AMENDMENT= "quote.not.eligible.amendment";
    
}
