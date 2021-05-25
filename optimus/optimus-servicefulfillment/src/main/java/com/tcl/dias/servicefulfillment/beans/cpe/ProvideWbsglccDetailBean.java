package com.tcl.dias.servicefulfillment.beans.cpe;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ProvideWbsglccDetailBean extends TaskDetailsBaseBean {
    private String level5Wbs;
    private String demandIdNo;
    private String glCode;
    private String costCenter;
    private String supportDemandIdNo;
    private String supportGlCode;
    private String supportCostCenter;
    private String licenceDemandIdNo;
    private String licenceGlCode;
    private String licenceCostCenter;
    private String cpeDeliveryRequired;
    private String cpeDeliveryRequiredRejectionReason;
    private String cpeRequiredDate;
    private String vendorPORaisedDate;
    
       
    public String getCpeDeliveryRequiredRejectionReason() {
		return cpeDeliveryRequiredRejectionReason;
	}

	public void setCpeDeliveryRequiredRejectionReason(String cpeDeliveryRequiredRejectionReason) {
		this.cpeDeliveryRequiredRejectionReason = cpeDeliveryRequiredRejectionReason;
	}

	public String getCpeDeliveryRequired() {
		return cpeDeliveryRequired;
	}

	public void setCpeDeliveryRequired(String cpeDeliveryRequired) {
		this.cpeDeliveryRequired = cpeDeliveryRequired;
	}

	public String getLevel5Wbs() {
        return level5Wbs;
    }

    public void setLevel5Wbs(String level5Wbs) {
        this.level5Wbs = level5Wbs;
    }

    public String getDemandIdNo() {
        return demandIdNo;
    }

    public void setDemandIdNo(String demandIdNo) {
        this.demandIdNo = demandIdNo;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getSupportDemandIdNo() {
		return supportDemandIdNo;
	}

	public void setSupportDemandIdNo(String supportDemandIdNo) {
		this.supportDemandIdNo = supportDemandIdNo;
	}

	public String getSupportGlCode() {
		return supportGlCode;
	}

	public void setSupportGlCode(String supportGlCode) {
		this.supportGlCode = supportGlCode;
	}

	public String getSupportCostCenter() {
		return supportCostCenter;
	}

	public void setSupportCostCenter(String supportCostCenter) {
		this.supportCostCenter = supportCostCenter;
	}

    public String getLicenceDemandIdNo() {
        return licenceDemandIdNo;
    }

    public void setLicenceDemandIdNo(String licenceDemandIdNo) {
        this.licenceDemandIdNo = licenceDemandIdNo;
    }

    public String getLicenceGlCode() {
        return licenceGlCode;
    }

    public void setLicenceGlCode(String licenceGlCode) {
        this.licenceGlCode = licenceGlCode;
    }

    public String getLicenceCostCenter() {
        return licenceCostCenter;
    }

    public void setLicenceCostCenter(String licenceCostCenter) {
        this.licenceCostCenter = licenceCostCenter;
    }

    public String getCpeRequiredDate() {
		return cpeRequiredDate;
	}

	public void setCpeRequiredDate(String cpeRequiredDate) {
		this.cpeRequiredDate = cpeRequiredDate;
	}

	public String getVendorPORaisedDate() {
		return vendorPORaisedDate;
	}

	public void setVendorPORaisedDate(String vendorPORaisedDate) {
		this.vendorPORaisedDate = vendorPORaisedDate;
	}

	@Override
    public String toString() {
        return "ProvideWbsglccDetailBean{" +
                "level5Wbs='" + level5Wbs + '\'' +
                ", demandIdNo='" + demandIdNo + '\'' +
                ", glCode='" + glCode + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", supportDemandIdNo='" + supportDemandIdNo + '\'' +
                ", supportGlCode='" + supportGlCode + '\'' +
                ", supportCostCenter='" + supportCostCenter + '\'' +
                ", licenceDemandIdNo='" + licenceDemandIdNo + '\'' +
                ", licenceGlCode='" + licenceGlCode + '\'' +
                ", licenceCostCenter='" + licenceCostCenter + '\'' +
                ", cpeRequiredDate='" + cpeRequiredDate + '\'' +
                ", vendorPORaisedDate='" + vendorPORaisedDate + '\'' +
                '}';
    }
}
