package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * This class is used for Create Planned Event.
 *
 *
 * @author Vishesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CreatePlannedEventBean {

    private Integer serviceId;
    private String processInstanceId;
    private String serviceCode;
    private String siteType;

    public CreatePlannedEventBean() {
    }

    public CreatePlannedEventBean(Integer serviceId, String processInstanceId,String siteType,String serviceCode) {
        this.serviceId = serviceId;
        this.processInstanceId = processInstanceId;
        this.siteType = siteType;
        this.serviceCode=serviceCode;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
    
    
}
