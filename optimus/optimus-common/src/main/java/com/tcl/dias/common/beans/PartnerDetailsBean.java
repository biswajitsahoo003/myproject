package com.tcl.dias.common.beans;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Partner Details Bean
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerDetailsBean {

    private Integer partnerId;
    private String partnerSfdcAccountId;
    private String accountId18;
    private String entityOwnerName;
    private String partnerName;
    private String partnerProfile;
    private Integer partnerProfileId;
    private Set<String> cuid;
    private String customerType;

    public String getEntityOwnerName() {
        return entityOwnerName;
    }

    public void setEntityOwnerName(String entityOwnerName) {
        this.entityOwnerName = entityOwnerName;
    }


    public String getPartnerSfdcAccountId() {
        return partnerSfdcAccountId;
    }

    public void setPartnerSfdcAccountId(String partnerSfdcAccountId) {
        this.partnerSfdcAccountId = partnerSfdcAccountId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getAccountId18() {
        return accountId18;
    }

    public void setAccountId18(String accountId18) {
        this.accountId18 = accountId18;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerProfile() {
        return partnerProfile;
    }

    public void setPartnerProfile(String partnerProfile) {
        this.partnerProfile = partnerProfile;
    }

    public Set<String> getCuid() {
		return cuid;
	}

	public void setCuid(Set<String> cuid) {
		this.cuid = cuid;
	}

	public Integer getPartnerProfileId() {
		return partnerProfileId;
	}

	public void setPartnerProfileId(Integer partnerProfileId) {
		this.partnerProfileId = partnerProfileId;
	}

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "PartnerDetailsBean{" +
                "partnerId=" + partnerId +
                ", partnerSfdcAccountId='" + partnerSfdcAccountId + '\'' +
                ", accountId18='" + accountId18 + '\'' +
                ", entityOwnerName='" + entityOwnerName + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", partnerProfile='" + partnerProfile + '\'' +
                ", partnerProfileId='" + partnerProfileId + '\'' +
                ", cuid=" + cuid +
                ", customerType='" + customerType + '\'' +
                '}';
    }
}
