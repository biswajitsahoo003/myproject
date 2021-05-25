package com.tcl.dias.service.constants;

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

	public static final String COMMON_ERROR = "common.exception";
	public static final String FILE_NAME_CONATINS_INVALID_CHARECTERS = "file.name.conatins.invalid.charectrs";
	public static final String FAILED_TO_UPLOAD = "failed.to.upload";
	public static final String FILE_EMPTY = "file.empty";
	public static final String MISSING_INPUT_DATA = "missing.input.data";
	public static final String INVALID_EMAIL = "invalid.email";

	public static final String FAILED_TO_DOWNLOAD = "failed.to.download";
	public static final String MALFORMED_URL_EXCEPTION = "malformed.url.exception";
	public static final String RESOURCE_NOT_EXIST = "resource.not.exist";
	public static final String ATTACHMENT_ID_MISSING = "attachment.id.missing";
	public static final String REFERENCE_NAME_EMPTY = "reference.name.empty";
	public static final String CUSTOMER_ID_EMPTY = "customer.id.empty";
	public static final String CREATEROLES = "roles.error";

	public static final String GELALLROLESERROR = "roles.get.error";
	public static final String ADD_USER_OMS_ERROR = "user.add.error.oms";
	public static final String ADD_USER_KEYCLOAK_ERROR = "user.add.error.keycloak";
	public static final String GET_USER_OMS_ERROR = "user.get.oms.error";

}
