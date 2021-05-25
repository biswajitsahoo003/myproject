package com.tcl.dias.common.beans;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the CustomerDetailBean.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(value=Include.NON_NULL)
public class CustomerDetailBean {
	
	private Integer customerId;

	private String customerName;

	private String sFDCAccountId;

	private String customercode;

	private Integer status;

	private String isVerified;

	private String type;
	
	private Set<String> cuid;

	private  String tpsSFDCaccId;

	private  String fySegmentation;

	public String getSFDCAccountId() {
		return sFDCAccountId;
	}

	public void setSFDCAccountId(String sFDCAccountId) {
		this.sFDCAccountId = sFDCAccountId;
	}




	public Set<String> getCuid() {
		return cuid;
	}

	public void setCuid(Set<String> cuid) {
		this.cuid = cuid;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}


	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}


	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getTpsSFDCaccId() {
		return tpsSFDCaccId;
	}

	public void setTpsSFDCaccId(String tpsSFDCaccId) {
		this.tpsSFDCaccId = tpsSFDCaccId;
	}


	public String getFySegmentation() {
		return fySegmentation;
	}

	public void setFySegmentation(String fySegmentation) {
		this.fySegmentation = fySegmentation;
	}


	@Override
	public String toString() {
		return "CustomerDetailBean{" +
				"customerId=" + customerId +
				", customerName='" + customerName + '\'' +
				", sFDCAccountId='" + sFDCAccountId + '\'' +
				", customercode='" + customercode + '\'' +
				", status=" + status +
				'}';
	    }

	}
