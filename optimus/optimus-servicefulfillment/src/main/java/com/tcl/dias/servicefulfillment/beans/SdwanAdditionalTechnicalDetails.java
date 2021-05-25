package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class SdwanAdditionalTechnicalDetails extends TaskDetailsBaseBean {

    private String vpnName;
    private String directorInstanceMapping;
    private String noOfCpe;
    private String noOfWanLinks;
    private List<CpeDetailsBean> cpeDetails;
    private List<AttachmentIdBean> documentIds;

    private String isSecurityAddonApplicable;
    private String securityAddonData;


    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getDirectorInstanceMapping() {
        return directorInstanceMapping;
    }

    public void setDirectorInstanceMapping(String directorInstanceMapping) {
        this.directorInstanceMapping = directorInstanceMapping;
    }

    public String getNoOfCpe() {
        return noOfCpe;
    }

    public void setNoOfCpe(String noOfCpe) {
        this.noOfCpe = noOfCpe;
    }

    public String getNoOfWanLinks() {
        return noOfWanLinks;
    }

    public void setNoOfWanLinks(String noOfWanLinks) {
        this.noOfWanLinks = noOfWanLinks;
    }

    public List<CpeDetailsBean> getCpeDetails() {
        return cpeDetails;
    }

    public void setCpeDetails(List<CpeDetailsBean> cpeDetails) {
        this.cpeDetails = cpeDetails;
    }

    public String getIsSecurityAddonApplicable() {
        return isSecurityAddonApplicable;
    }

    public void setIsSecurityAddonApplicable(String isSecurityAddonApplicable) {
        this.isSecurityAddonApplicable = isSecurityAddonApplicable;
    }

    public String getSecurityAddonData() {
        return securityAddonData;
    }

    public void setSecurityAddonData(String securityAddonData) {
        this.securityAddonData = securityAddonData;
    }

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
    
}
