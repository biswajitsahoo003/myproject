package com.tcl.dias.service.swagger.constants;

/**
 * 
 * This file contains the SwaggerConstants.java class. This class conatins
 * swagger constants
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

		public static class Quotes {
			private Quotes() {
				// DO NOTHING
			}

		}

		public static class FileUpload {
			private FileUpload() {

			}

			public static final String UPLOAD_FILE = "Uploading the File";
		}

		public static class FileDownload {
			private FileDownload() {

			}

			public static final String DOWNLOAD_FILE = "File download with attachment id";
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
			public static final String CHANGE_PASSWORD_USER = "Changes the password for the user";
			public static final String CREATE_USERGROUP = "Creates user group";
			public static final String UPDATE_USERGROUP = "Update user group";
			public static final String DELETE_USERGROUP = "Delete user group";
			public static final String GET_ALL_USERGROUP = "Get all user group";
		}
		
		

	}

}
