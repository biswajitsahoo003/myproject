package com.tcl.dias.oms.gsc.tiger.beans;

/**
 * Bean class to store organisation data from Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class Organisation {
	private String orgId;
	private String orgType;
	private String orgAbbrName;
	private String orgActvDate;
	private String orgLegalName;
	private String orgBusType;
	private String cuId;
	private String customerType;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgAbbrName() {
		return orgAbbrName;
	}

	public void setOrgAbbrName(String orgAbbrName) {
		this.orgAbbrName = orgAbbrName;
	}

	public String getOrgActvDate() {
		return orgActvDate;
	}

	public void setOrgActvDate(String orgActvDate) {
		this.orgActvDate = orgActvDate;
	}

	public String getOrgLegalName() {
		return orgLegalName;
	}

	public void setOrgLegalName(String orgLegalName) {
		this.orgLegalName = orgLegalName;
	}

	public String getOrgBusType() {
		return orgBusType;
	}

	public void setOrgBusType(String orgBusType) {
		this.orgBusType = orgBusType;
	}

	public String getCuId() {
		return cuId;
	}

	public void setCuId(String cuId) {
		this.cuId = cuId;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
}
