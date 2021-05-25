package com.tcl.dias.products.validation.constants;

/**
 * Constant messages related to Bean validations
 * @author Naveen G
 *
 */
public class BeanValidationConstants {
	private BeanValidationConstants() {
	}

	public static class ProductFamilyConstants {
		private ProductFamilyConstants() {
		}

		public static final String ISACTIVE_SIZE = "Is Active should be only 1 character";
		public static final String ISACTIVE_PATTERN = "Is Active should be Y/N only";
		public static final String NAME_SIZE = "Product Family Name should be more than 2 characters!";
		public static final String NAME_NOT_NULL = "Product family name is mandatory!";
		public static final String NAME_NOT_EMPTY = "Product family name should not be empty!";
		public static final String ISACTIVE_PATTERN_REGEX = "(?:Y|N)";
	}
}
