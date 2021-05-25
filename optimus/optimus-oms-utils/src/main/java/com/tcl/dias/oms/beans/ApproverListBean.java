package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.CCEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * ApproverListBean class to get list of approvers
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApproverListBean {

    List<Approver> approvers=new ArrayList<>();

    List<CCEmail> ccEmails=new ArrayList<>();

    List<Approver> customerSigners = new ArrayList<>();

    List<Approver> commercialSigners = new ArrayList<>();
    
    private String supplierStatus;

    public List<Approver> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Approver> approvers) {
        this.approvers = approvers;
    }

    public List<CCEmail> getCcEmails() {
        return ccEmails;
    }

    public void setCcEmails(List<CCEmail> ccEmails) {
        this.ccEmails = ccEmails;
    }

    public List<Approver> getCustomerSigners() {
        return customerSigners;
    }

    public void setCustomerSigners(List<Approver> customerSigners) {
        this.customerSigners = customerSigners;
    }

    public String getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }
    
    

    public List<Approver> getCommercialSigners() {
		return commercialSigners;
	}

	public void setCommercialSigners(List<Approver> commercialSigners) {
		this.commercialSigners = commercialSigners;
	}

	@Override
    public String toString() {
        return "ApproverListBean{" +
                "approvers=" + approvers +
                ", ccEmails=" + ccEmails +
                ", customerSigners=" + customerSigners +
                ", supplierStatus='" + supplierStatus + '\'' +
                '}';
    }
}
