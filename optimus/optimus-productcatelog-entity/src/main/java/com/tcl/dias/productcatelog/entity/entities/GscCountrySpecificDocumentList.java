package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "vw_ctry_specific_doc_list_GSC")
@NamedQuery(name = "GscCountrySpecificDocumentList.findAll", query = "SELECT c FROM GscCountrySpecificDocumentList c")
public class GscCountrySpecificDocumentList implements Serializable {

	@Id
	private String UID;

	@Column(name = "Pdt_Name")
	private String productName;

	@Column(name = "Iso_3_Ctry_Cd")
	private String countryCode;

	@Column(name = "Iso_Ctry_Name")
	private String countryName;

	@Column(name = "doc_name")
	private String documentName;

	@Column(name = "category_txt")
	private String category;

	@Column(name = "template_nm")
	private String template;

	@Column(name = "remarks_txt")
	private String remarks;

	@Column(name = "url")
	private String url;

	@Column(name ="type")
	private String type;
	
	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
