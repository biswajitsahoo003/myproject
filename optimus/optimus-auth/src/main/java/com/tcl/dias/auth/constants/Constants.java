package com.tcl.dias.auth.constants;

/**
 * used to get final string values 
 * 
 *
 * @author RSriramo
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class Constants {
	private Constants() {
	}

	public static final String PD_NOT_MATCHED = "Passwords are not matching";
	public static final String OLD_PD_IS_WRONG = "Old password is wrong";
	public static final String USER_IS_INACTIVE ="User is inactive";
	public static final String USER_IS_NOT_FOUND ="User is not found";
	
	public static final String KEYCLOAK_TOKEN_URL = "/realms/master/protocol/openid-connect/token";
	public static final String ROLES_URL = "/roles";
	public static final String ROLES_URL_WITH_SLASH = "/roles";
	public static final String USER_URL = "/users";
	public static final String USERS_URL = "/users/";
	public static final String ROLES_MAPPING_URL = "/role-mappings/realm";
	public static final String DEFAULT_PD="welcome123";
	public static final String IMPERSONATE_URL="/impersonation";
	public static final String DISABLE_CREDENTIALS_TYPE="/disable-credential-types";
	public static final String CONFIGURE_TOTP="CONFIGURE_TOTP";
	public static final String INTERNAL="internal";
	public static final String CUSTOMER="customer";
	public static final String SALES="sales";
	public static final String TATA_COM_DOMAIN="tatacommunications.com";
	public static final String IDENTITY_CREATE_URL_SAML="/federated-identity/saml-nexus";
	public static final String SAML="saml-nexus";
	
	public static final String ENABLE = "ENABLE";
	public static final String DISABLE = "DISABLE";
	public static final String RECONFIGURE = "RECONFIGURE";
	public static final String DISABLE_OTP = "DISABLE_OTP";
	
	public static final String CLEAR_USER_CACHE="/clear-user-cache";

	public static final String PARTNER = "Partner";
	
	
}
