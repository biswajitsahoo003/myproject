package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

import java.util.List;

public class TerminationRequestBean extends BaseRequest {

    private Integer serviceId;
    private String terminationEffectiveDate;
    private String etcValue;
    private String etcWaiver;
    private List<AttachmentIdBean> documentIds;
    private String fromTime="00:00";
    private String customerRequestorDate;
    
    private String contractEndDate;
    
    private String approvalMailAvailable;
    private String backdatedTermination;
    
    
    
    

    public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getTerminationEffectiveDate() {
        return terminationEffectiveDate;
    }

    public void setTerminationEffectiveDate(String terminationEffectiveDate) {
        this.terminationEffectiveDate = terminationEffectiveDate;
    }

    public String getEtcValue() {
        return etcValue;
    }

    public void setEtcValue(String etcValue) {
        this.etcValue = etcValue;
    }

    public String getEtcWaiver() {
        return etcWaiver;
    }

    public void setEtcWaiver(String etcWaiver) {
        this.etcWaiver = etcWaiver;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

	public String getCustomerRequestorDate() {
		return customerRequestorDate;
	}

	public void setCustomerRequestorDate(String customerRequestorDate) {
		this.customerRequestorDate = customerRequestorDate;
	}

	public String getApprovalMailAvailable() {
		return approvalMailAvailable;
	}

	public void setApprovalMailAvailable(String approvalMailAvailable) {
		this.approvalMailAvailable = approvalMailAvailable;
	}

	public String getBackdatedTermination() {
		return backdatedTermination;
	}

	public void setBackdatedTermination(String backdatedTermination) {
		this.backdatedTermination = backdatedTermination;
	}
 
	
    

}
