package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 
 * This is the entity class for the Manual feasiblity report view Table
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_manual_feasibility_report")
@NamedQuery(name="VwManualFeasibilityReport.findAll", query="SELECT v FROM VwManualFeasibilityReport v")
public class VwManualFeasibilityReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	private String address;

	@Column(name="customer_name")
	private String customerName;

	@Column(name="email_id")
	private String emailId;
	
	@Column(name="local_Loop_speed_in_mbps")
	private String localLoopSpeedInMbps;
	
	@Column(name="port_speed_in_mbps")
	private String portSpeedInMbps;

	@Column(name="product_family")
	private String productFamily;

	@Column(name="product_name")
	private String productName;

	@Column(name="quote_code")
	private String quoteCode;
	
	
	@Column(name="quote_id")
	private Integer quoteId;
	
	@Id
	@Column(name="site_code")
	private String siteCode;
	
	@Column(name="lcon_name")
	private String lconName;

	@Column(name="lcon_contact_number")
	private String lconContactNumber;

	private String username;

	public String getLconName() {
		return lconName;
	}

	public void setLconName(String lconName) {
		this.lconName = lconName;
	}

	public String getLconContactNumber() {
		return lconContactNumber;
	}

	public void setLconContactNumber(String lconContactNumber) {
		this.lconContactNumber = lconContactNumber;
	}
	
	
	public VwManualFeasibilityReport() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLocalLoopSpeedInMbps() {
		return localLoopSpeedInMbps;
	}

	public void setLocalLoopSpeedInMbps(String localLoopSpeedInMbps) {
		this.localLoopSpeedInMbps = localLoopSpeedInMbps;
	}

	public String getPortSpeedInMbps() {
		return portSpeedInMbps;
	}

	public void setPortSpeedInMbps(String portSpeedInMbps) {
		this.portSpeedInMbps = portSpeedInMbps;
	}

	public String getProductFamily() {
		return this.productFamily;
	}

	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getQuoteCode() {
		return this.quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteId() {
		return this.quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getSiteCode() {
		return this.siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}