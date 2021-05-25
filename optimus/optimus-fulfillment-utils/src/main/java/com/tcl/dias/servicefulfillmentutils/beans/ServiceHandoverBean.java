package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *  
 * ServiceHandoverBean for Service Handover details
 */
public class ServiceHandoverBean extends BaseRequest {

    private String serviceHandoverAction;
    private String remark;
    private List<DependencyDetails> dependencyDetails;
    
    private String demarcationBuildingName;
    private String demarcationFloor;
    private String demarcationRoom;
    private String demarcationWing;
    private String cpeAmcEndDate;
    private String cpeSerialNumber;
    private String cpeAmcStartDate;
    private String localItContactName;
    private String localItContactMobile;
    private String localItContactEmailId;
    private List<AttachmentIdBean> documentIds;

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public String getDemarcationBuildingName() {
		return demarcationBuildingName;
	}

	public void setDemarcationBuildingName(String demarcationBuildingName) {
		this.demarcationBuildingName = demarcationBuildingName;
	}

	public String getDemarcationFloor() {
		return demarcationFloor;
	}

	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	public String getDemarcationRoom() {
		return demarcationRoom;
	}

	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	public String getDemarcationWing() {
		return demarcationWing;
	}

	public void setDemarcationWing(String demarcationWing) {
		this.demarcationWing = demarcationWing;
	}

	public String getServiceHandoverAction() {
        return serviceHandoverAction;
    }

    public void setServiceHandoverAction(String serviceHandoverAction) {
        this.serviceHandoverAction = serviceHandoverAction;
    }

    public List<DependencyDetails> getDependencyDetails() {
        return dependencyDetails;
    }

    public void setDependencyDetails(List<DependencyDetails> dependencyDetails) {
        this.dependencyDetails = dependencyDetails;
    }

    public String getRemark() { return remark; }

    public void setRemark(String remark) { this.remark = remark; }

    public String getCpeAmcEndDate() {
        return cpeAmcEndDate;
    }

    public void setCpeAmcEndDate(String cpeAmcEndDate) {
        this.cpeAmcEndDate = cpeAmcEndDate;
    }

    public String getCpeSerialNumber() {
        return cpeSerialNumber;
    }

    public void setCpeSerialNumber(String cpeSerialNumber) {
        this.cpeSerialNumber = cpeSerialNumber;
    }

    public String getCpeAmcStartDate() {
        return cpeAmcStartDate;
    }

    public void setCpeAmcStartDate(String cpeAmcStartDate) {
        this.cpeAmcStartDate = cpeAmcStartDate;
    }

    public String getLocalItContactName() {
        return localItContactName;
    }

    public void setLocalItContactName(String localItContactName) {
        this.localItContactName = localItContactName;
    }

    public String getLocalItContactMobile() {
        return localItContactMobile;
    }

    public void setLocalItContactMobile(String localItContactMobile) {
        this.localItContactMobile = localItContactMobile;
    }

    public String getLocalItContactEmailId() {
        return localItContactEmailId;
    }

    public void setLocalItContactEmailId(String localItContactEmailId) {
        this.localItContactEmailId = localItContactEmailId;
    }

    @Override
    public String toString() {
        return "ServiceHandoverBean{" +
                "serviceHandoverAction='" + serviceHandoverAction + '\'' +
                ", remark='" + remark + '\'' +
                ", dependencyDetails=" + dependencyDetails +
                '}';
    }
}
