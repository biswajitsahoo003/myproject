package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Objects;
/**
 * To Store VProxy Related Primary IDs
 * @author vpachava
 *
 */
public class VwVproxyProductComponentId implements Serializable{

	private String productName;
	
	private String attributeCode;


	private String attributeName;

	
	private String attributeDesc;

	
	private String attributeValue;

	   @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        VwVproxyProductComponentId dataClass = (VwVproxyProductComponentId) o;
	        return productName.equals(dataClass.productName) &&
	              //  licenseName.equals(dataClass.licenseName) &&
	            //    addonName.equals(dataClass.addonName) &&
	                attributeCode.equals(dataClass.attributeCode) &&
	                attributeName.equals(dataClass.attributeName) &&
	                attributeDesc.equals(dataClass.attributeDesc) &&
	                attributeValue.equals(dataClass.attributeValue);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(productName,   attributeCode, attributeName, attributeDesc, attributeValue);
	    }
	    public VwVproxyProductComponentId() {
	    	
	    }

		public VwVproxyProductComponentId(String productName, String licenseName, String addonName,
				String attributeCode, String attributeName, String attributeDesc, String attributeValue) {
			super();
			this.productName = productName;
			this.attributeCode = attributeCode;
			this.attributeName = attributeName;
			this.attributeDesc = attributeDesc;
			this.attributeValue = attributeValue;
		}
	    
}

