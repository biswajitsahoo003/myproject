package com.tcl.dias.common.gsc.beans;

public class GscCountrySpecificDocumentBean {

	private String uID;
	private String productName;
	private String countryCode;
	private String category;
	private String countryName;
	private String documentName;
	private String template;
	private String remarks;
	private String type;

	public String getuID() {
		return uID;
	}

	public void setuID(String uID) {
		this.uID = uID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "GscCountrySpecificDocumentBean{" +
				"uID='" + uID + '\'' +
				", productName='" + productName + '\'' +
				", countryCode='" + countryCode + '\'' +
				", category='" + category + '\'' +
				", countryName='" + countryName + '\'' +
				", documentName='" + documentName + '\'' +
				", template='" + template + '\'' +
				", remarks='" + remarks + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
