package com.tcl.dias.oms.entity.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This file contains the OrderLinkFeasibility.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "order_link_feasibility")
public class OrderLinkFeasibility {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "link_id")
	private OrderNplLink orderNplLink;

	@Column(name = "feasibility_check")
	private String feasibilityCheck;

	@Column(name = "feasibility_mode")
	private String feasibilityMode;

	@Column(name = "feasibility_code")
	private String feasibilityCode;

	@Column(name = "provider")
	private String provider;

	private Integer rank;

	@Column(name = "response_json")
	private String responseJson;

	@Column(name = "is_selected")
	private Byte isSelected;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "type")
	private String type;
	
	
	@Column(name = "feasibility_mode_b")
	private String feasibilityModeB;
	
	@Column(name = "provider_b")
	private String providerB;

	@Column(name = "feasibility_type")
	private String feasibilityType;
	
	@Column(name = "feasibility_type_b")
	private String feasibilityTypeB;
	
	@Column(name = "site_document")
	private byte[] siteDocument;
	
	@Column(name ="site_document_name")
	private String siteDocumentName;
	
	@Column(name="cd_distance")
	private Integer cdDistance;
	
	
	
	

	

	public String getFeasibilityModeB() {
		return feasibilityModeB;
	}

	public void setFeasibilityModeB(String feasibilityModeB) {
		this.feasibilityModeB = feasibilityModeB;
	}

	public String getProviderB() {
		return providerB;
	}

	public void setProviderB(String providerB) {
		this.providerB = providerB;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public String getFeasibilityTypeB() {
		return feasibilityTypeB;
	}

	public void setFeasibilityTypeB(String feasibilityTypeB) {
		this.feasibilityTypeB = feasibilityTypeB;
	}

	public byte[] getSiteDocument() {
		return siteDocument;
	}

	public void setSiteDocument(byte[] siteDocument) {
		this.siteDocument = siteDocument;
	}

	public String getSiteDocumentName() {
		return siteDocumentName;
	}

	public void setSiteDocumentName(String siteDocumentName) {
		this.siteDocumentName = siteDocumentName;
	}

	public Integer getCdDistance() {
		return cdDistance;
	}

	public void setCdDistance(Integer cdDistance) {
		this.cdDistance = cdDistance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public Byte getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public OrderLinkFeasibility() {
		
	}

	public OrderLinkFeasibility (LinkFeasibility linkFeas,OrderNplLink orderLink) {
		if (linkFeas!=null) {
			this.createdTime=Timestamp.valueOf(LocalDateTime.now());
			this.feasibilityCheck=linkFeas.getFeasibilityCheck();
			this.feasibilityCode=linkFeas.getFeasibilityCode();
			this.feasibilityMode=linkFeas.getFeasibilityMode();
			this.isSelected=linkFeas.getIsSelected();
			this.orderNplLink=orderLink;
			this.provider=linkFeas.getProvider();
			this.rank=linkFeas.getRank();
			this.responseJson= linkFeas.getResponseJson();
			this.type=linkFeas.getType();
			this.feasibilityModeB=linkFeas.getFeasibilityModeB();
			this.providerB=linkFeas.getProviderB();
			this.feasibilityType=linkFeas.getFeasibilityType();
			this.feasibilityTypeB=linkFeas.getFeasibilityTypeB();
			this.siteDocument=linkFeas.getSiteDocument();
			this.siteDocumentName=linkFeas.getSiteDocumentName();
			this.cdDistance=linkFeas.getCdDistance();
			
		}
		
	}

	public OrderNplLink getOrderNplLink() {
		return orderNplLink;
	}

	public void setOrderNplLink(OrderNplLink orderNplLink) {
		this.orderNplLink = orderNplLink;
	}
}
