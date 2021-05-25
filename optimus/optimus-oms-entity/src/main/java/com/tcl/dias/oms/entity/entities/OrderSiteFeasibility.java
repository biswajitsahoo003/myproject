package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This file contains the OrderSiteFeasibility.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_site_feasibility")
@NamedQuery(name = "OrderSiteFeasibility.findAll", query = "SELECT o FROM OrderSiteFeasibility o")
public class OrderSiteFeasibility implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1414917588070971086L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "site_id")
	private OrderIllSite orderIllSite;

	@Column(name = "feasibility_check")
	private String feasibilityCheck;

	@Column(name = "feasibility_mode")
	private String feasibilityMode;

	@Column(name = "feasibility_code")
	private String feasibilityCode;

	@Column(name = "rank")
	private Integer rank;

	@Column(name = "provider")
	private String provider;

	@Column(name = "response_json")
	private String responseJson;

	@Column(name = "is_selected")
	private Byte isSelected;

	@Column(name = "type")
	private String type;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "sfdc_feasibility_id")
	private String sfdcFeasibilityId;

	@Column(name = "feasibility_type")
	private String feasibilityType;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the orderIllSite
	 */
	public OrderIllSite getOrderIllSite() {
		return orderIllSite;
	}

	/**
	 * @param orderIllSite
	 *            the orderIllSite to set
	 */
	public void setOrderIllSite(OrderIllSite orderIllSite) {
		this.orderIllSite = orderIllSite;
	}

	/**
	 * @return the feasibilityCheck
	 */
	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	/**
	 * @param feasibilityCheck
	 *            the feasibilityCheck to set
	 */
	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	/**
	 * @return the feasibilityMode
	 */
	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	/**
	 * @param feasibilityMode
	 *            the feasibilityMode to set
	 */
	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	/**
	 * @return the rank
	 */
	public Integer getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * @return the responseJson
	 */
	public String getResponseJson() {
		return responseJson;
	}

	/**
	 * @param responseJson
	 *            the responseJson to set
	 */
	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	/**
	 * @return the isSelected
	 */
	public Byte getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
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

	public String getSfdcFeasibilityId() {
		return sfdcFeasibilityId;
	}

	public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
		this.sfdcFeasibilityId = sfdcFeasibilityId;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

}
