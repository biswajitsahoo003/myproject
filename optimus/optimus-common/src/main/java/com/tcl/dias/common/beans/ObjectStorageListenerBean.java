package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the bean class used for queue calls for object storage
 *
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class ObjectStorageListenerBean {

    private Integer customerId;


    private Integer customerLeId;


    private Integer attachmentId;

    private String customerCode;

    private String customerLeCode;

    private Integer omsAttachmentId;

    private List<Integer> attachmentIds;

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
     * @return the customerLeId
     */
    public Integer getCustomerLeId() {
        return customerLeId;
    }


    /**
     * @param customerLeId the customerLeId to set
     */
    public void setCustomerLeId(Integer customerLeId) {
        this.customerLeId = customerLeId;
    }


//	/**
//	 * @return the attachmentIdsList
//	 */
//	public List<Integer> getAttachmentIdsList() {
//		return attachmentIdsList;
//	}
//
//
//	/**
//	 * @param attachmentIdsList the attachmentIdsList to set
//	 */
//	public void setAttachmentIdsList(List<Integer> attachmentIdsList) {
//		this.attachmentIdsList = attachmentIdsList;
//	}




    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }




    public Integer getAttachmentId() {
        return attachmentId;
    }


    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }


    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }


    /**
     * @return the customerLeCode
     */
    public String getCustomerLeCode() {
        return customerLeCode;
    }


    /**
     * @param customerLeCode the customerLeCode to set
     */
    public void setCustomerLeCode(String customerLeCode) {
        this.customerLeCode = customerLeCode;
    }


    public Integer getOmsAttachmentId() {
        return omsAttachmentId;
    }


    public void setOmsAttachmentId(Integer omsAttachmentId) {
        this.omsAttachmentId = omsAttachmentId;
    }


    public List<Integer> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<Integer> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }
}
