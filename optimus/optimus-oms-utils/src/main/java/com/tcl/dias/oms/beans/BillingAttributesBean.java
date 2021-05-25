package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the BillingAttributesBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class BillingAttributesBean {
	

		private String attributeName;

		private String attributeValue;

		private String type;

		/**
		 * @return the attributeName
		 */
		public String getAttributeName() {
			return attributeName;
		}

		/**
		 * @param attributeName the attributeName to set
		 */
		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		/**
		 * @return the attributeValue
		 */
		public String getAttributeValue() {
			return attributeValue;
		}

		/**
		 * @param attributeValue the attributeValue to set
		 */
		public void setAttributeValue(String attributeValue) {
			this.attributeValue = attributeValue;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		
		
		
		

}
