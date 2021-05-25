package com.tcl.dias.audit.swagger.constants;

/**
 * 
 * Swagger constants
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class Customer {
		private Customer() {
		}
	}
	
	public static class Feedback {
		private Feedback() {
		}
		public static final String GET_FEEDBACK_DETAILS = "Get all Feedbacks";
		public static final String GET_PAGE_FEEDBACK_DETAILS = "Get Feedbacks per page";
		public static final String SAVE_FEEDBACK_DETAILS= "Save Feedback";
		public static final String UPDATE_FEEDBACK_DETAILS= "update Feedback";
	}

}
