package com.tcl.dias.common.beans;


public class OpportunityBean {

    private String oppotunityId;
    private String oppotunityName;
    private String opportunityOwnerName;
    private String opportunityOwnerEmail;
    private String opportunityAccountName;
    private String opportunityStage;
    private String customerSegment;
    private String salesSegment;
    private String productName;
    private String serviceId;
    private String siteContactName;
    private String siteLocalContactNumber;
    private String salesRemarks;
    private Integer mf3DOptyId;
    
    // Added for OPPORTAL-1256 MF bug
    private String primaryServiceId;
    private String secondaryServiceId;

    public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}

	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;

	}
	
	

	public Integer getMf3DOptyId() {
		return mf3DOptyId;
	}

	public void setMf3DOptyId(Integer mf3dOptyId) {
		mf3DOptyId = mf3dOptyId;
	}

	public String getOppotunityId() {
        return oppotunityId;
    }

    public void setOppotunityId(String oppotunityId) {
        this.oppotunityId = oppotunityId;
    }

    public String getOppotunityName() {
        return oppotunityName;
    }

    public void setOppotunityName(String oppotunityName) {
        this.oppotunityName = oppotunityName;
    }

    public String getOpportunityOwnerName() {
        return opportunityOwnerName;
    }

    public void setOpportunityOwnerName(String opportunityOwnerName) {
        this.opportunityOwnerName = opportunityOwnerName;
    }

    public String getOpportunityOwnerEmail() {
        return opportunityOwnerEmail;
    }

    public void setOpportunityOwnerEmail(String opportunityOwnerEmail) {
        this.opportunityOwnerEmail = opportunityOwnerEmail;
    }

    public String getOpportunityAccountName() {
        return opportunityAccountName;
    }

    public void setOpportunityAccountName(String opportunityAccountName) {
        this.opportunityAccountName = opportunityAccountName;
    }

    public String getOpportunityStage() {
        return opportunityStage;
    }

    public void setOpportunityStage(String opportunityStage) {
        this.opportunityStage = opportunityStage;
    }

    public String getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    public String getSalesSegment() {
        return salesSegment;
    }

    public void setSalesSegment(String salesSegment) {
        this.salesSegment = salesSegment;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSiteContactName() {
        return siteContactName;
    }

    public void setSiteContactName(String siteContactName) {
        this.siteContactName = siteContactName;
    }

    public String getSiteLocalContactNumber() {
        return siteLocalContactNumber;
    }

    public void setSiteLocalContactNumber(String siteLocalContactNumber) {
        this.siteLocalContactNumber = siteLocalContactNumber;
    }

    public String getSalesRemarks() {
        return salesRemarks;
    }

    public void setSalesRemarks(String salesRemarks) {
        this.salesRemarks = salesRemarks;
    }
}
