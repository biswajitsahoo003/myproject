package com.tcl.dias.serviceinventory.constants;

/**
 * This class contains all the exception constants of service inventory project.
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ExceptionConstants {

	private ExceptionConstants() {

	}

	public static final String INVALID_INPUT = "invalid.input";
	public static final String INVALID_SERVICEID = "invalid.serviceid";
	public static final String COMMON_ERROR = "common.error";
	public static final String IP_ATTRIBUTE_NOT_FOUND = "ip.attribute.error";
	public static final String INVALID_RELATED_SERVICEID = "invalid.related.serviceid";
	public static final String TOLLFREE_SERVICE_ERROR = "tollfree.service.error";
	public static final String PARTNER_LEGAL_ENTITY_MQ_ERROR= "partner.legal.entity.mq.error";
	public static final String PARTNER_LEGAL_ENTITY_MQ_EMPTY= "partner.legal.entity.mq.empty";

	// versa API related (SDWANSPP)
	public static final String VERSA_CPE_STATUS_API_ERROR = "versa.cpe.status.api.error";
	public static final String VERSA_USER_APPL_ERROR = "versa.user.appl.error";
	public static final String VERSA_FAILED_TO_UPDATE_POLICY = "versa.policy.update.error";
	public static final String VERSA_DIRECTOR_MAPPING_NOT_AVAILABLE = "versa.director.mapping.not.available";
	public static final String VERSA_SLA_PROFILE_CREATION_FAILED = "versa.sla.profile.create.failed";
	public static final String VERSA_FP_PROFILE_CREATION_FAILED = "versa.fp.profile.create.failed";
	public static final String VERSA_SLA_PROFILE_EDIT_FAILED = "versa.sla.profile.edit.failed";
	
	public static final String CISCO_APPLICATION_EDIT_API_ERROR = "cisco.application.edit.api.error";
	public static final String CISCO_FAILED_TO_UPDATE_POLICY = "cisco.policy.update.error";
	public static final String CISCO_CONCURRENT_UPDATE_ERROR = "cisco.concurrent.update.error";
}
