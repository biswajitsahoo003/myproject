package com.tcl.dias.auth.constants;

/**
 * This class contains all the exception constants of token generation.
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ExceptionConstants {

	private ExceptionConstants() {

	}

	public static final String AUTH_USER_NOT_FOUND = "auth.usernotfound.error";
	public static final String COMMON_ERROR = "common.exception";
	public static final String TOKEN_GENERATOR_ERROR = "token.generator.error";
	public static final String TOKEN_DESTROY_ERROR = "token.destroy.error";
	public static final String TOKEN_EXPIRED_ERROR = "token.expired.error";

	public static final String LDAP_USER_NOT_FOUND = "ldap.user.notfound";
	public static final String LDAP_SAVE_ERROR = "ldap.save.error";
	public static final String LDAP_PASSWRDSET_ERROR = "ldap.passwordset.error";
	public static final String LDAP_INVALID_CREDENTIALS = "ldap.invalid.credentials";
	public static final String LDAP_USER_INITIALIZE_ERROR = "ldap.user.initialized.error";
	public static final String FORGOT_PASWD_VALIDATION_ERROR = "forgot.password.validation.error";
	public static final String RESET_PASWD_VALIDATION_ERROR = "reset.password.validation.error";
	public static final String RESET_PASWD_TOKEN_ERROR = "reset.password.token.error";
	
	public static final String ERROR_CREATING_ROLE = "error.create.role";
	public static final String ERROR_CREATING_USER = "error.create.user";
	public static final String ERROR_DELETING_USER = "error.delete.user";
	public static final String ERROR_ASSIGN_ROLE = "error.role.assign";
	public static final String ERROR_REMOVE_ROLE = "error.remove.role";
	public static final String ERROR_NO_ROLE = "error.no.role";
	public static final String ERROR_GET_ROLE = "error.get.role";
	public static final String ERROR_NO_USER = "error.no.user";
	public static final String ERROR_NO_IMP_IMP = "error.no.imp_imp";
	public static final String ERROR_CREATE_PD = "error.create.password";
	public static final String INCORRECT_OLD_PD = "error.wrong.old.password";
	public static final String USER_ALREADY_PRESENT = "error.user.already.present";
	public static final String ROLE_ALREADY_PRESENT = "error.role.already.present";
	
	public static final String ERROR_FETCHING_LE_DETAILS = "error.le.details";
	public static final String ERROR_PROCESS_GET_USERINFO = "error.get.userinfo";
	public static final String ERROR_PROCESS_USERINFO = "error.process.userinfo";
	public static final String USER_EXISTS_ERROR = "user.already.exists.error";
	public static final String USER_VALIDATION_ERROR = "user.validation.error";
	public static final String INVALID_INPUT = "invalid.input";
	public static final String INVALID_REQUEST = "invalid.request";
	public static final String INVALID_GROUP_TYPE = "invalid.group.type";
	public static final String DUPLICATE_GROUP_NAME = "duplicate.group.name";

	public static final String PARTNER_LE_MQ_ERROR = "partner.le.mq.error";
	public static final String PARTNER_LE_MQ_EMPTY = "partner.le.mq.empty";

	public static final String CUSTOMER_LE_MQ_ERROR = "customer.le.mq.error";
	public static final String CUSTOMER_LE_MQ_EMPTY = "customer.le.mq.empty";

	

}
