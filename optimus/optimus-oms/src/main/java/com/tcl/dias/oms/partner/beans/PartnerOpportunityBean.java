package com.tcl.dias.oms.partner.beans;

import java.util.List;

import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.oms.entity.entities.Opportunity;

/**
 * Bean for Partner Opportunity Information
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PartnerOpportunityBean {

	private Integer partnerId;
	private Integer partnerLeId;
	private Integer opportunityId;
	private Integer productId;
	private Double nrc;
	private Double mrc;
	private String summary;
	private String type;
	private String classification;
	private String campaignName;
	private String customerName;
	private Integer customerId;
	private String customerLeName;
	private Integer customerLeId;
	private String opportunityCode;
	private List<PartnerDocumentBean> documents;
	private Integer engagementOptyId;
	private String expectedCurrency;
	private String isDealRegistration;
	private String dealRegistrationDate;
	private String campaignId;
	private Integer tempCustomerLeId;
	private String tempCustomerName;
	private String psamEmailId;
	private boolean isdealBlocked = true;
	private String tpsOptyId;
	private String productName;
	private String psamName;
	private String endCustomerCuid;

	public String getTpsOptyId() {
		return tpsOptyId;
	}

	public void setTpsOptyId(String tpsOptyId) {
		this.tpsOptyId = tpsOptyId;
	}

	public String getPsamEmailId() {
		return psamEmailId;
	}

	public void setPsamEmailId(String psamEmailId) {
		this.psamEmailId = psamEmailId;
	}

	public Integer getTempCustomerLeId() {
		return tempCustomerLeId;
	}

	public void setTempCustomerLeId(Integer tempCustomerLeId) {
		this.tempCustomerLeId = tempCustomerLeId;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getPartnerLeId() {
		return partnerLeId;
	}

	public void setPartnerLeId(Integer partnerLeId) {
		this.partnerLeId = partnerLeId;
	}

	public Integer getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOpportunityCode() {
		return opportunityCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public void setOpportunityCode(String opportunityCode) {
		this.opportunityCode = opportunityCode;
	}

	public List<PartnerDocumentBean> getDocuments() {
		return documents;
	}

	public void setDocuments(List<PartnerDocumentBean> documents) {
		this.documents = documents;
	}

	public Integer getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(Integer engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getExpectedCurrency() {
		return expectedCurrency;
	}

	public void setExpectedCurrency(String expectedCurrency) {
		this.expectedCurrency = expectedCurrency;
	}

	public String getIsDealRegistration() {
		return isDealRegistration;
	}

	public void setIsDealRegistration(String isDealRegistration) {
		this.isDealRegistration = isDealRegistration;
	}

	public String getDealRegistrationDate() {
		return dealRegistrationDate;
	}

	public void setDealRegistrationDate(String dealRegistrationDate) {
		this.dealRegistrationDate = dealRegistrationDate;
	}

	public String getTempCustomerName() {
		return tempCustomerName;
	}

	public void setTempCustomerName(String tempCustomerName) {
		this.tempCustomerName = tempCustomerName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isIsdealBlocked() {
		return isdealBlocked;
	}

	public void setIsdealBlocked(boolean isdealBlocked) {
		this.isdealBlocked = isdealBlocked;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPsamName() {
		return psamName;
	}

	public void setPsamName(String psamName) {
		this.psamName = psamName;
	}


	public String getEndCustomerCuid() {
		return endCustomerCuid;
	}

	public void setEndCustomerCuid(String endCustomerCuid) {
		this.endCustomerCuid = endCustomerCuid;
	}

	public static PartnerOpportunityBean fromOpportunity(Opportunity opportunity, List<PartnerDocumentBean> documents) {
		PartnerOpportunityBean bean = new PartnerOpportunityBean();
		bean.setClassification(opportunity.getOptyClassification());
		bean.setMrc(opportunity.getExpectedMrc());
		bean.setNrc(opportunity.getExpectedNrc());
		bean.setOpportunityId(opportunity.getId());
		bean.setSummary(opportunity.getOptySummary());
		bean.setCampaignName(opportunity.getCampaignName());
		bean.setCampaignId(opportunity.getCampaignId());
		bean.setOpportunityCode(opportunity.getUuid());
		bean.setDocuments(documents);
		bean.setExpectedCurrency(opportunity.getExpectedCurrency());
		bean.setIsDealRegistration(opportunity.getIsDealRegistration());
		bean.setDealRegistrationDate(opportunity.getDealRegistrationDate());
		bean.setTempCustomerLeId(opportunity.getTempCustomerLeId());
		bean.setPsamEmailId(opportunity.getPsamEmailId());
		bean.setTpsOptyId(opportunity.getTpsOptyId());
		return bean;
	}

	@Override
	public String toString() {
		return "PartnerOpportunityBean{" +
				"partnerId=" + partnerId +
				", partnerLeId=" + partnerLeId +
				", opportunityId=" + opportunityId +
				", productId=" + productId +
				", nrc=" + nrc +
				", mrc=" + mrc +
				", summary='" + summary + '\'' +
				", type='" + type + '\'' +
				", classification='" + classification + '\'' +
				", campaignName='" + campaignName + '\'' +
				", customerName='" + customerName + '\'' +
				", customerId=" + customerId +
				", customerLeName='" + customerLeName + '\'' +
				", customerLeId=" + customerLeId +
				", opportunityCode='" + opportunityCode + '\'' +
				", documents=" + documents +
				", engagementOptyId=" + engagementOptyId +
				", expectedCurrency='" + expectedCurrency + '\'' +
				", isDealRegistration='" + isDealRegistration + '\'' +
				", dealRegistrationDate='" + dealRegistrationDate + '\'' +
				", campaignId='" + campaignId + '\'' +
				", tempCustomerLeId=" + tempCustomerLeId +
				", tempCustomerName='" + tempCustomerName + '\'' +
				", psamEmailId='" + psamEmailId + '\'' +
				", isdealBlocked=" + isdealBlocked +
				", tpsOptyId='" + tpsOptyId + '\'' +
				", endCustomerCuid='" + endCustomerCuid + '\'' +
				'}';
	}
}
