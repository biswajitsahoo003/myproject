package com.tcl.dias.oms.validator.beans;

public class ValidatorAttributeBean {

	private String nodeName;
	private String type;
	private String xPath;
	private String productName;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "ValidatorAttributeBean [nodeName=" + nodeName + ", type=" + type + ", xPath=" + xPath + ", productName="
				+ productName + "]";
	}

}
