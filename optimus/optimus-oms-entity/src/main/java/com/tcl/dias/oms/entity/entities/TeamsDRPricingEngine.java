package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity for storing teamsdr pricing request and response
 * @author Syed Ali.
 * @createdAt 23/03/2021, Tuesday, 15:31
 */
@Entity
@Table(name = "teamsdr_pricing_engine")
@NamedQuery(name = "TeamsDRPricingEngine.findAll", query = "SELECT p FROM TeamsDRPricingEngine p")
public class TeamsDRPricingEngine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "date_time")
	private Timestamp dateTime;

	@Column(name = "price_mode")
	private String priceMode;

	@Column(name = "pricing_type")
	private String pricingType;

	@Lob
	@Column(name = "request_data")
	private String requestData;

	@Lob
	@Column(name = "response_data")
	private String responseData;

	@Column(name = "site_code")
	private String siteCode;

	public TeamsDRPricingEngine() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getPriceMode() {
		return priceMode;
	}

	public void setPriceMode(String priceMode) {
		this.priceMode = priceMode;
	}

	public String getPricingType() {
		return pricingType;
	}

	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
}

