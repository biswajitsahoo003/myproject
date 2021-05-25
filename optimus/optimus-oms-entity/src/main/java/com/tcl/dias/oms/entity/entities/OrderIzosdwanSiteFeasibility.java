package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * This file contains the OrderIzosdwanSiteFeasibility.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_izosdwan_site_feasibility")
@NamedQuery(name="OrderIzosdwanSiteFeasibility.findAll", query="SELECT o FROM OrderIzosdwanSiteFeasibility o")
public class OrderIzosdwanSiteFeasibility implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_time")
	private Timestamp createdTime;

	@Column(name="feasibility_check")
	private String feasibilityCheck;

	@Column(name="feasibility_code")
	private String feasibilityCode;

	@Column(name="feasibility_mode")
	private String feasibilityMode;

	@Column(name="feasibility_type")
	private String feasibilityType;

	@Column(name="is_selected")
	private byte isSelected;

	private String provider;

	private Integer rank;

	@Lob
	@Column(name="response_json")
	private String responseJson;

	@Column(name="sfdc_feasibility_id")
	private String sfdcFeasibilityId;

	@Lob
	@Column(name="site_document")
	private byte[] siteDocument;

	@Column(name="site_document_name")
	private String siteDocumentName;

	@OneToOne
	@JoinColumn(name = "site_id")
	private OrderIzosdwanSite orderIzosdwanSite;

	private String type;

	public OrderIzosdwanSiteFeasibility() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityCheck() {
		return this.feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getFeasibilityCode() {
		return this.feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public String getFeasibilityMode() {
		return this.feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public String getFeasibilityType() {
		return this.feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public byte getIsSelected() {
		return this.isSelected;
	}

	public void setIsSelected(byte isSelected) {
		this.isSelected = isSelected;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getResponseJson() {
		return this.responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getSfdcFeasibilityId() {
		return this.sfdcFeasibilityId;
	}

	public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
		this.sfdcFeasibilityId = sfdcFeasibilityId;
	}

	public byte[] getSiteDocument() {
		return this.siteDocument;
	}

	public void setSiteDocument(byte[] siteDocument) {
		this.siteDocument = siteDocument;
	}

	public String getSiteDocumentName() {
		return this.siteDocumentName;
	}

	public void setSiteDocumentName(String siteDocumentName) {
		this.siteDocumentName = siteDocumentName;
	}

	public OrderIzosdwanSite getOrderIzosdwanSite() {
		return orderIzosdwanSite;
	}

	public void setOrderIzosdwanSite(OrderIzosdwanSite orderIzosdwanSite) {
		this.orderIzosdwanSite = orderIzosdwanSite;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}