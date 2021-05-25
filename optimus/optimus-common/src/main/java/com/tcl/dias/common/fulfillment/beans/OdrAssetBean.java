package com.tcl.dias.common.fulfillment.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entity Class
 *
 *
 * @author Thamizhselvi perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class OdrAssetBean {

    private Integer id;
    private String createdBy;
    private String createdDate;
    private String fqdn;
    private String name;
    private String publicIp;
    private String type;
    private String updatedBy;
    private String updatedDate;
    private String isActive;
    private Integer odrServiceDetailId;
    private String originnetwork;
    private String isSharedInd;
    private String supplierActivationDate;

    private List<OdrAssetAttributeBean> odrAssetAttributeBean;
    private List<OdrAssetRelationBean> odrAssetRelationBeans;
    
    private String customerOutpulse;
    private String supplierName;
    private String supplierId;
    private String billingDate;
    private String cdrLog;

    private String serviceAcceptenceStatus;
    
    public OdrAssetBean() {
    }

    public Integer getId() {
        return this.id;
    }


    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


    public String getFqdn() {
        return this.fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicIp() {
        return this.publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getOriginnetwork() {
        return originnetwork;
    }

    public void setOriginnetwork(String originnetwork) {
        this.originnetwork = originnetwork;
    }

    public String getIsSharedInd() {
        return isSharedInd;
    }

    public void setIsSharedInd(String isSharedInd) {
        this.isSharedInd = isSharedInd;
    }

    public Integer getOdrServiceDetailId() {
        return odrServiceDetailId;
    }
    
    

    /**
	 * @return the supplierActivationDate
	 */
	public String getSupplierActivationDate() {
		return supplierActivationDate;
	}

	/**
	 * @param supplierActivationDate the supplierActivationDate to set
	 */
	public void setSupplierActivationDate(String supplierActivationDate) {
		this.supplierActivationDate = supplierActivationDate;
	}

	public void setOdrServiceDetailId(Integer odrServiceDetailId) {
        this.odrServiceDetailId = odrServiceDetailId;
    }

    public List<OdrAssetAttributeBean> getOdrAssetAttributeBean() {
		return odrAssetAttributeBean;
	}

	public void setOdrAssetAttributeBean(List<OdrAssetAttributeBean> odrAssetAttributeBean) {
		this.odrAssetAttributeBean = odrAssetAttributeBean;
	}
	
	public void addOdrAssetAttributeBean(OdrAssetAttributeBean odrAssetAttributeBean) {
		if(this.odrAssetAttributeBean == null)
			this.odrAssetAttributeBean = new ArrayList<>();
		this.odrAssetAttributeBean.add(odrAssetAttributeBean);
	}

	public List<OdrAssetRelationBean> getOdrAssetRelationBeans() {
		return odrAssetRelationBeans;
	}

	public void setOdrAssetRelationBeans(List<OdrAssetRelationBean> odrAssetRelationBeans) {
		this.odrAssetRelationBeans = odrAssetRelationBeans;
	}
	
	
	public String getCustomerOutpulse() {
		return customerOutpulse;
	}

	public void setCustomerOutpulse(String customerOutpulse) {
		this.customerOutpulse = customerOutpulse;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}

	public String getCdrLog() {
		return cdrLog;
	}

	public void setCdrLog(String cdrLog) {
		this.cdrLog = cdrLog;
	}
	

	public String getServiceAcceptenceStatus() {
		return serviceAcceptenceStatus;
	}

	public void setServiceAcceptenceStatus(String serviceAcceptenceStatus) {
		this.serviceAcceptenceStatus = serviceAcceptenceStatus;
	}

	public void addOdrAssetRelationBean(OdrAssetRelationBean odrAssetRelationBean) {
		if(this.odrAssetRelationBeans == null)
			this.odrAssetRelationBeans = new ArrayList<>();
		this.odrAssetRelationBeans.add(odrAssetRelationBean);
	}

	@Override
    public String toString() {
        return "OdrAssetBean{" +
                "id=" + id +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", fqdn='" + fqdn + '\'' +
                ", name='" + name + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", type='" + type + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedDate=" + updatedDate +
                ", isActive='" + isActive + '\'' +
                ", odrServiceDetailId=" + odrServiceDetailId +
                ", originnetwork='" + originnetwork + '\'' +
                ", isSharedInd='" + isSharedInd + '\'' +
                '}';
    }
}
