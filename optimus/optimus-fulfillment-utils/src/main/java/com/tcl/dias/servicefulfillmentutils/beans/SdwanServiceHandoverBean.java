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
public class SdwanServiceHandoverBean extends BaseRequest {

    private String serviceHandoverAction;
    private String remark;
    private List<DependencyDetails> dependencyDetails;
    private List<CpeInstallationBean> cpeDetailBeans;
    private List<UnderlayBean> underlayBeans;
    private List<AttachmentIdBean> documentIds;

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
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

	public List<CpeInstallationBean> getCpeDetailBeans() {
		return cpeDetailBeans;
	}

	public void setCpeDetailBeans(List<CpeInstallationBean> cpeDetailBeans) {
		this.cpeDetailBeans = cpeDetailBeans;
	}

	public List<UnderlayBean> getUnderlayBeans() {
		return underlayBeans;
	}

	public void setUnderlayBeans(List<UnderlayBean> underlayBeans) {
		this.underlayBeans = underlayBeans;
	}

	@Override
	public String toString() {
		return "SdwanServiceHandoverBean [serviceHandoverAction=" + serviceHandoverAction + ", remark=" + remark
				+ ", dependencyDetails=" + dependencyDetails + ", cpeDetailBeans=" + cpeDetailBeans + ", underlayBeans="
				+ underlayBeans + ", documentIds=" + documentIds + "]";
	}
}
