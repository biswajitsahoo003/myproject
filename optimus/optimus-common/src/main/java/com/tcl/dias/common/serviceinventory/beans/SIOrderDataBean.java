package com.tcl.dias.common.serviceinventory.beans;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean class to contain Service Inventory order data
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@JsonInclude(NON_NULL)
public class SIOrderDataBean {
	private Integer id;
	private String uuid;
	private String opOrderCode;
	private String customerGroupName;
	private String customerSegment;
	private String demoFlag;
	private Integer erfCustCurrencyId;
	private String erfCustCustomerId;
	private String erfCustCustomerName;
	private Integer erfCustPartnerLeId;
	private String erfCustPartnerName;
	private String erfCustSpLeName;
	private Integer erfUserCustomerUserId;
	private Integer erfUserInitiatorId;
	private String isActive;
	private String isBundleOrder;
	private String isMultipleLe;
	private Timestamp lastMacdDate;
	private Timestamp macdCreatedDate;
	private String opportunityClassification;
	private Timestamp orderEndDate;
	private Integer parentId;
	private String parentOpOrderCode;
	private String sfdcAccountId;
	private String tpsCrmOptyId;
	private String tpsCrmSystem;
	private String tpsSfdcCuid;
	private String partnerCuid;
	private Integer erfCustLeId;
	private Integer erfCustPartnerId;
	private Integer erfCustSpLeId;
	private String erfPrdCatalogName;
	private String erfPrdCatalogProfileName;
	private String orderCategory;
	private String orderSource;
	private Timestamp orderStartDate;
	private String orderStatus;
	private String orderTermInMonths;
	private String orderType;
	private String sfdcCuid;
	private String tpsSecsId;
    private String tpsSfdcId;
	private List<SIAttributeBean> attributes = new ArrayList<>();
	private List<SIServiceDetailDataBean> serviceDetails = new ArrayList<>();
    private String erfCustLeName;
    private String tpsSapCrnId;
    private String tpsCrmCofId;
    private String createdBy;
    private String updatedBy;
    private Timestamp updatedDate;
    private Timestamp createdDate;

	public Integer getId() {
		return id;
	}

    public void setId(Integer id) {
        this.id = id;
    }

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOpOrderCode() {
		return opOrderCode;
	}

	public void setOpOrderCode(String opOrderCode) {
		this.opOrderCode = opOrderCode;
	}

	public String getCustomerGroupName() {
		return customerGroupName;
	}

    public void setCustomerGroupName(String customerGroupName) {
        this.customerGroupName = customerGroupName;
    }

    public String getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    public String getDemoFlag() {
        return demoFlag;
    }

    public void setDemoFlag(String demoFlag) {
        this.demoFlag = demoFlag;
    }

    public Integer getErfCustCurrencyId() {
        return erfCustCurrencyId;
    }

    public void setErfCustCurrencyId(Integer erfCustCurrencyId) {
        this.erfCustCurrencyId = erfCustCurrencyId;
    }

    public String getErfCustCustomerId() {
        return erfCustCustomerId;
    }

    public void setErfCustCustomerId(String erfCustCustomerId) {
        this.erfCustCustomerId = erfCustCustomerId;
    }

    public Integer getErfCustLeId() {
        return erfCustLeId;
    }

    public void setErfCustLeId(Integer erfCustLeId) {
        this.erfCustLeId = erfCustLeId;
    }

    public Integer getErfCustPartnerId() {
        return erfCustPartnerId;
    }

    public void setErfCustPartnerId(Integer erfCustPartnerId) {
        this.erfCustPartnerId = erfCustPartnerId;
    }

    public Integer getErfCustSpLeId() {
        return erfCustSpLeId;
    }

    public void setErfCustSpLeId(Integer erfCustSpLeId) {
        this.erfCustSpLeId = erfCustSpLeId;
    }

    public String getErfPrdCatalogName() {
        return erfPrdCatalogName;
    }

    public void setErfPrdCatalogName(String erfPrdCatalogName) {
        this.erfPrdCatalogName = erfPrdCatalogName;
    }

    public String getErfPrdCatalogProfileName() {
        return erfPrdCatalogProfileName;
    }

    public void setErfPrdCatalogProfileName(String erfPrdCatalogProfileName) {
        this.erfPrdCatalogProfileName = erfPrdCatalogProfileName;
    }

	public String getOrderCategory() {
		return orderCategory;
	}

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Timestamp getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(Timestamp orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTermInMonths() {
        return orderTermInMonths;
    }

    public void setOrderTermInMonths(String orderTermInMonths) {
        this.orderTermInMonths = orderTermInMonths;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

	public String getSfdcCuid() {
		return sfdcCuid;
	}

    public void setSfdcCuid(String sfdcCuid) {
        this.sfdcCuid = sfdcCuid;
    }

	public String getTpsSecsId() {
		return tpsSecsId;
	}

	public void setTpsSecsId(String tpsSecsId) {
		this.tpsSecsId = tpsSecsId;
	}


    public String getTpsSfdcId() {
        return tpsSfdcId;
    }

    public void setTpsSfdcId(String tpsSfdcId) {
        this.tpsSfdcId = tpsSfdcId;
    }

	public List<SIAttributeBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SIAttributeBean> attributes) {
		this.attributes = attributes;
	}

	public List<SIServiceDetailDataBean> getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(List<SIServiceDetailDataBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

    public String getErfCustLeName() {
        return erfCustLeName;
    }

    public void setErfCustLeName(String erfCustLeName) {
        this.erfCustLeName = erfCustLeName;
    }

    public String getTpsSapCrnId() {
        return tpsSapCrnId;
    }

    public void setTpsSapCrnId(String tpsSapCrnId) {
        this.tpsSapCrnId = tpsSapCrnId;
    }

    public String getTpsCrmCofId() {
        return tpsCrmCofId;
    }

    public void setTpsCrmCofId(String tpsCrmCofId) {
        this.tpsCrmCofId = tpsCrmCofId;
    }

	public String getErfCustCustomerName() {
		return erfCustCustomerName;
	}

	public void setErfCustCustomerName(String erfCustCustomerName) {
		this.erfCustCustomerName = erfCustCustomerName;
	}

	public Integer getErfCustPartnerLeId() {
		return erfCustPartnerLeId;
	}

	public void setErfCustPartnerLeId(Integer erfCustPartnerLeId) {
		this.erfCustPartnerLeId = erfCustPartnerLeId;
	}

	public String getErfCustPartnerName() {
		return erfCustPartnerName;
	}

	public void setErfCustPartnerName(String erfCustPartnerName) {
		this.erfCustPartnerName = erfCustPartnerName;
	}

	public String getErfCustSpLeName() {
		return erfCustSpLeName;
	}

	public void setErfCustSpLeName(String erfCustSpLeName) {
		this.erfCustSpLeName = erfCustSpLeName;
	}

	public Integer getErfUserCustomerUserId() {
		return erfUserCustomerUserId;
	}

	public void setErfUserCustomerUserId(Integer erfUserCustomerUserId) {
		this.erfUserCustomerUserId = erfUserCustomerUserId;
	}

	public Integer getErfUserInitiatorId() {
		return erfUserInitiatorId;
	}

	public void setErfUserInitiatorId(Integer erfUserInitiatorId) {
		this.erfUserInitiatorId = erfUserInitiatorId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsBundleOrder() {
		return isBundleOrder;
	}

	public void setIsBundleOrder(String isBundleOrder) {
		this.isBundleOrder = isBundleOrder;
	}

	public String getIsMultipleLe() {
		return isMultipleLe;
	}

	public void setIsMultipleLe(String isMultipleLe) {
		this.isMultipleLe = isMultipleLe;
	}

	public Timestamp getLastMacdDate() {
		return lastMacdDate;
	}

	public void setLastMacdDate(Timestamp lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	public Timestamp getMacdCreatedDate() {
		return macdCreatedDate;
	}

	public void setMacdCreatedDate(Timestamp macdCreatedDate) {
		this.macdCreatedDate = macdCreatedDate;
	}

	public String getOpportunityClassification() {
		return opportunityClassification;
	}

	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
	}

	public Timestamp getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(Timestamp orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentOpOrderCode() {
		return parentOpOrderCode;
	}

	public void setParentOpOrderCode(String parentOpOrderCode) {
		this.parentOpOrderCode = parentOpOrderCode;
	}

	public String getSfdcAccountId() {
		return sfdcAccountId;
	}

	public void setSfdcAccountId(String sfdcAccountId) {
		this.sfdcAccountId = sfdcAccountId;
	}

	public String getTpsCrmOptyId() {
		return tpsCrmOptyId;
	}

	public void setTpsCrmOptyId(String tpsCrmOptyId) {
		this.tpsCrmOptyId = tpsCrmOptyId;
	}

	public String getTpsCrmSystem() {
		return tpsCrmSystem;
	}

	public void setTpsCrmSystem(String tpsCrmSystem) {
		this.tpsCrmSystem = tpsCrmSystem;
	}

	public String getTpsSfdcCuid() {
		return tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public String getPartnerCuid() {
		return partnerCuid;
	}

	public void setPartnerCuid(String partnerCuid) {
		this.partnerCuid = partnerCuid;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "SIOrderDataBean{" +
				"id=" + id +
				", uuid='" + uuid + '\'' +
				", customerGroupName='" + customerGroupName + '\'' +
				", customerSegment='" + customerSegment + '\'' +
				", demoFlag='" + demoFlag + '\'' +
				", erfCustCurrencyId=" + erfCustCurrencyId +
				", erfCustCustomerId='" + erfCustCustomerId + '\'' +
				", erfCustLeId=" + erfCustLeId +
				", erfCustPartnerId=" + erfCustPartnerId +
				", erfCustSpLeId=" + erfCustSpLeId +
				", erfPrdCatalogName='" + erfPrdCatalogName + '\'' +
				", erfPrdCatalogProfileName='" + erfPrdCatalogProfileName + '\'' +
				", orderCategory='" + orderCategory + '\'' +
				", orderSource='" + orderSource + '\'' +
				", orderStartDate=" + orderStartDate +
				", orderStatus='" + orderStatus + '\'' +
				", orderTermInMonths='" + orderTermInMonths + '\'' +
				", orderType='" + orderType + '\'' +
				", sfdcCuid='" + sfdcCuid + '\'' +
				", tpsSecsId='" + tpsSecsId + '\'' +
				", tpsSfdcId='" + tpsSfdcId + '\'' +
				", attributes=" + attributes +
				", serviceDetails=" + serviceDetails +
				", erfCustLeName='" + erfCustLeName + '\'' +
				", tpsSapCrnId='" + tpsSapCrnId + '\'' +
				", tpsCrmCofId='" + tpsCrmCofId + '\'' +
				'}';
	}
}
