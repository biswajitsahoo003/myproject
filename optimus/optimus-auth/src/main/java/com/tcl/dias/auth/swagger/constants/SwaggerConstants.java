package com.tcl.dias.auth.swagger.constants;

/**
 * useful information for the end user used by swagger api
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class ApiOperations {
		private ApiOperations() {
		}

		public static class Auth {
			private Auth() {
			}

			public static final String FORGOT_PASSWRD = "process the forgot password by sending the reset url by email";
			public static final String RESET_PASSWRD = "process the reset password by getting from the reset password page";
			public static final String VALIDATE_RESET_TOKEN = "validate the reset token and return the status";
		}

		public static class UserManagement {
			private UserManagement() {

			}

			public static final String CREATE_ROLE = "This api is user for creating role";
			public static final String CREATE_USER = "This api is user for creating user";
			public static final String DELETE_USER = "This api is user for deleting user";
			public static final String ADD_ROLE = "This api is user for adding role";
			public static final String DELETE_USER_ROLE = "This api is user for deleting role";
			public static final String GET_ROLES = "This api is user for getting roles";
			public static final String UPDATE_USER = "This api is used to update user";
			public static final String DELETE_USER_AUTH = "Delete user in keycloak";
			public static final String GET_USER = "Get user by user id";
			public static final String GET_ALL_USER = "Get all user";
			public static final String CHANGE_PASSWRD_USER = "Changes the password for the user";
			public static final String CREATE_USERGROUP = "Creates user group";
			public static final String UPDATE_USERGROUP = "Update user group";
			public static final String DELETE_USERGROUP = "Delete user group";
			public static final String GET_ALL_USERGROUP = "Get all user group";
			public static final String SEARCH_CUSTOMERS = "This api is used to search given string against customer name";
			public static final String SEARCH_USER_GROUP = "This api is used to search given string against usergroup name";

			public static final String BULK_USER_CREATION = "This API is user to create users as a bulk";
			public static final String BULK_USER_CREATION_TEMPLATE_DOWNLOAD = "This API is user to downloak template to create users as a bulk";
			public static final String PUSH_USER_DETAILS = "User to push the user details";
			public static final String USER_LIST = "Gets the list of users in a user group";
			public static final String GET_USER_GROUP = "Gets the list of user groups";
			public static final String CREATE_APP_USER = "create app users";
			public static final String DELETE_APP_USER = "delete the app users";
			public static final String SAVE_USER_MAPPING_DETAILS = "Used to save the user and le mapping details";
			public static final String SAVE_PARTNER_CUSTOMER_DETAILS = "Save partner customer details";
			public static final String SYNC_USERS = "This api is to sync Optimus users and usermanagement users";
			public static final String ADD_MODIFY_USER_GROUP_TYPE="This api is used to add or modify the user group type";
			public static final String ADD_MODIFY_USER_GROUP="This api is used to add or modify the user group";
			public static final String GET_USER_GROUP_BY_ID = "Gets user group";
			public static final String MODIFY_USER_STATUS="This api is used to change the status status of user whether he is active or deactive";
			public static final String GET_USER_LES_MAPPING="This api is used to get customerLes and partnerLes for the given user id";
			public static final String SAVE_USER_LE_DETAILS="This api is used to save customerLes  and parnerLes for the given user id";

			public static final String GET_USER_ACCESS_DETAILS = "Used to get user access details";
			
		}
	}

}
