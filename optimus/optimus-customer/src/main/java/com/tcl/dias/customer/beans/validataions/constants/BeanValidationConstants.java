package com.tcl.dias.customer.beans.validataions.constants;

/**
 * Constant messages related to Bean validations
 * @author Naveen G
 *
 */
public class BeanValidationConstants {
	private BeanValidationConstants() {
	}

	public static class BillingConstants {
		private BillingConstants() {
		}
		public static final String ATTRIBUTE_NAME_NOT_NULL = "Attribute name should not be null";
		public static final String ATTRIBUTE_VALUE_NOT_NULL = "Attribute Value should not be null";
		public static final String TYPE_NOT_NULL = "Type should not be null";
		public static final String STATUS_NOT_NULL = "Status should not be null";
	}
}
