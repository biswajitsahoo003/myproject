package com.tcl.dias.common.utils;

/**
 * This class is used to initiate Sourcce interms of async calls like queue
 * etc..
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SourceAdapter {

	private static final InheritableThreadLocal<String> initaiteSource = new InheritableThreadLocal<String>() {
		@Override
		protected String childValue(String parentValue) {
			if (parentValue == null) {
				return null;
			}
			return new String(parentValue);
		}
	};

	/**
	 * 
	 * setSource - This will set the source
	 * 
	 * @param username
	 */
	public static void setSource(String username) {
		initaiteSource.set(username);
	}

	/**
	 * 
	 * remove - Clean up
	 */
	public static void remove() {
		initaiteSource.remove();
	}

	/**
	 * 
	 * getSource - this will return the source if available
	 * 
	 * @return
	 */
	public static String getSource() {
		return initaiteSource.get();
	}

}
