package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCheckQueryResponse {

	private String id;
	
	private String opportunityId;
	
	private String billingFrequency;
	
	private String paymentTerm;
	
	private String billingMethod;
	
	private Boolean iccEnterpiceVoiceProductFlag;
	
	private String customerContractingEntity;
	
	private String tataBillingEntity;
	
	private Double opportunityMRC;
	
	private Double opportunityNRC;
	
	private Double annualizedContractValue;
	
	private Double bundledMRC;
	
	private String copfId;
	
	private Double previousMRC;
	
	private Double differentialMRC;
	
	private Boolean isPreApprovedOpportunity;
	
	private String conditionalApprovalType;
	
	private String conditionalApprovalRemarks;
	
	private String mrcNrc;
	
	private Boolean notifyCreditControlTeam;
	
	private String creditControlProcessComments;
	
	private String statusOfCreditControl;
	
	private String creditRating;
	
	private String reservedBy;
	
	private Double creditLimit;
	
	private String creditRemarks;
	
	private String redoCreditVerification;
	
	private String approvedBy;
	
	private String dateOfCreditApproval;
	
	private String securityDepositRequired;

	private Double securityDepositAmount;
	
	private String portalTransactionId;
	
	private String customerName;
	
	private String accountId;
	
	private ProductServicesQueryBean productServices; 
	
	private CustomerContractingEntityBean customerContractingEntityBean;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the billingFrequency
	 */
	public String getBillingFrequency() {
		return billingFrequency;
	}

	/**
	 * @param billingFrequency the billingFrequency to set
	 */
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	/**
	 * @return the paymentTerm
	 */
	public String getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm the paymentTerm to set
	 */
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the billingMethod
	 */
	public String getBillingMethod() {
		return billingMethod;
	}

	/**
	 * @param billingMethod the billingMethod to set
	 */
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	/**
	 * @return the iccEnterpiceVoiceProductFlag
	 */
	public Boolean getIccEnterpiceVoiceProductFlag() {
		return iccEnterpiceVoiceProductFlag;
	}

	/**
	 * @param iccEnterpiceVoiceProductFlag the iccEnterpiceVoiceProductFlag to set
	 */
	public void setIccEnterpiceVoiceProductFlag(Boolean iccEnterpiceVoiceProductFlag) {
		this.iccEnterpiceVoiceProductFlag = iccEnterpiceVoiceProductFlag;
	}

	/**
	 * @return the customerContractingEntity
	 */
	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	/**
	 * @param customerContractingEntity the customerContractingEntity to set
	 */
	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	/**
	 * @return the tataBillingEntity
	 */
	public String getTataBillingEntity() {
		return tataBillingEntity;
	}

	/**
	 * @param tataBillingEntity the tataBillingEntity to set
	 */
	public void setTataBillingEntity(String tataBillingEntity) {
		this.tataBillingEntity = tataBillingEntity;
	}

	/**
	 * @return the opportunityMRC
	 */
	public Double getOpportunityMRC() {
		return opportunityMRC;
	}

	/**
	 * @param opportunityMRC the opportunityMRC to set
	 */
	public void setOpportunityMRC(Double opportunityMRC) {
		this.opportunityMRC = opportunityMRC;
	}

	/**
	 * @return the opportunityNRC
	 */
	public Double getOpportunityNRC() {
		return opportunityNRC;
	}

	/**
	 * @param opportunityNRC the opportunityNRC to set
	 */
	public void setOpportunityNRC(Double opportunityNRC) {
		this.opportunityNRC = opportunityNRC;
	}

	/**
	 * @return the annualizedContractValue
	 */
	public Double getAnnualizedContractValue() {
		return annualizedContractValue;
	}

	/**
	 * @param annualizedContractValue the annualizedContractValue to set
	 */
	public void setAnnualizedContractValue(Double annualizedContractValue) {
		this.annualizedContractValue = annualizedContractValue;
	}

	/**
	 * @return the bundledMRC
	 */
	public Double getBundledMRC() {
		return bundledMRC;
	}

	/**
	 * @param bundledMRC the bundledMRC to set
	 */
	public void setBundledMRC(Double bundledMRC) {
		this.bundledMRC = bundledMRC;
	}

	/**
	 * @return the copfId
	 */
	public String getCopfId() {
		return copfId;
	}

	/**
	 * @param copfId the copfId to set
	 */
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	/**
	 * @return the previousMRC
	 */
	public Double getPreviousMRC() {
		return previousMRC;
	}

	/**
	 * @param previousMRC the previousMRC to set
	 */
	public void setPreviousMRC(Double previousMRC) {
		this.previousMRC = previousMRC;
	}

	/**
	 * @return the differentialMRC
	 */
	public Double getDifferentialMRC() {
		return differentialMRC;
	}

	/**
	 * @param differentialMRC the differentialMRC to set
	 */
	public void setDifferentialMRC(Double differentialMRC) {
		this.differentialMRC = differentialMRC;
	}

	/**
	 * @return the isPreApprovedOpportunity
	 */
	public Boolean getIsPreApprovedOpportunity() {
		return isPreApprovedOpportunity;
	}

	/**
	 * @param isPreApprovedOpportunity the isPreApprovedOpportunity to set
	 */
	public void setIsPreApprovedOpportunity(Boolean isPreApprovedOpportunity) {
		this.isPreApprovedOpportunity = isPreApprovedOpportunity;
	}

	/**
	 * @return the conditionalApprovalType
	 */
	public String getConditionalApprovalType() {
		return conditionalApprovalType;
	}

	/**
	 * @param conditionalApprovalType the conditionalApprovalType to set
	 */
	public void setConditionalApprovalType(String conditionalApprovalType) {
		this.conditionalApprovalType = conditionalApprovalType;
	}

	/**
	 * @return the conditionalApprovalRemarks
	 */
	public String getConditionalApprovalRemarks() {
		return conditionalApprovalRemarks;
	}

	/**
	 * @param conditionalApprovalRemarks the conditionalApprovalRemarks to set
	 */
	public void setConditionalApprovalRemarks(String conditionalApprovalRemarks) {
		this.conditionalApprovalRemarks = conditionalApprovalRemarks;
	}

	/**
	 * @return the mrcNrc
	 */
	public String getMrcNrc() {
		return mrcNrc;
	}

	/**
	 * @param mrcNrc the mrcNrc to set
	 */
	public void setMrcNrc(String mrcNrc) {
		this.mrcNrc = mrcNrc;
	}

	/**
	 * @return the notifyCreditControlTeam
	 */
	public Boolean getNotifyCreditControlTeam() {
		return notifyCreditControlTeam;
	}

	/**
	 * @param notifyCreditControlTeam the notifyCreditControlTeam to set
	 */
	public void setNotifyCreditControlTeam(Boolean notifyCreditControlTeam) {
		this.notifyCreditControlTeam = notifyCreditControlTeam;
	}

	/**
	 * @return the creditControlProcessComments
	 */
	public String getCreditControlProcessComments() {
		return creditControlProcessComments;
	}

	/**
	 * @param creditControlProcessComments the creditControlProcessComments to set
	 */
	public void setCreditControlProcessComments(String creditControlProcessComments) {
		this.creditControlProcessComments = creditControlProcessComments;
	}

	/**
	 * @return the statusOfCreditControl
	 */
	public String getStatusOfCreditControl() {
		return statusOfCreditControl;
	}

	/**
	 * @param statusOfCreditControl the statusOfCreditControl to set
	 */
	public void setStatusOfCreditControl(String statusOfCreditControl) {
		this.statusOfCreditControl = statusOfCreditControl;
	}

	/**
	 * @return the creditRating
	 */
	public String getCreditRating() {
		return creditRating;
	}

	/**
	 * @param creditRating the creditRating to set
	 */
	public void setCreditRating(String creditRating) {
		this.creditRating = creditRating;
	}

	/**
	 * @return the reservedBy
	 */
	public String getReservedBy() {
		return reservedBy;
	}

	/**
	 * @param reservedBy the reservedBy to set
	 */
	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}

	/**
	 * @return the creditLimit
	 */
	public Double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the creditRemarks
	 */
	public String getCreditRemarks() {
		return creditRemarks;
	}

	/**
	 * @param creditRemarks the creditRemarks to set
	 */
	public void setCreditRemarks(String creditRemarks) {
		this.creditRemarks = creditRemarks;
	}

	/**
	 * @return the redoCreditVerification
	 */
	public String getRedoCreditVerification() {
		return redoCreditVerification;
	}

	/**
	 * @param redoCreditVerification the redoCreditVerification to set
	 */
	public void setRedoCreditVerification(String redoCreditVerification) {
		this.redoCreditVerification = redoCreditVerification;
	}

	/**
	 * @return the approvedBy
	 */
	public String getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return the dateOfCreditApproval
	 */
	public String getDateOfCreditApproval() {
		return dateOfCreditApproval;
	}

	/**
	 * @param dateOfCreditApproval the dateOfCreditApproval to set
	 */
	public void setDateOfCreditApproval(String dateOfCreditApproval) {
		this.dateOfCreditApproval = dateOfCreditApproval;
	}

	/**
	 * @return the securityDepositRequired
	 */
	public String getSecurityDepositRequired() {
		return securityDepositRequired;
	}

	/**
	 * @param securityDepositRequired the securityDepositRequired to set
	 */
	public void setSecurityDepositRequired(String securityDepositRequired) {
		this.securityDepositRequired = securityDepositRequired;
	}

	/**
	 * @return the securityDepositAmount
	 */
	public Double getSecurityDepositAmount() {
		return securityDepositAmount;
	}

	/**
	 * @param securityDepositAmount the securityDepositAmount to set
	 */
	public void setSecurityDepositAmount(Double securityDepositAmount) {
		this.securityDepositAmount = securityDepositAmount;
	}

	/**
	 * @return the opportunityId
	 */
	public String getOpportunityId() {
		return opportunityId;
	}

	/**
	 * @param opportunityId the opportunityId to set
	 */
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	
	

	/**
	 * @return the portalTransactionId
	 */
	public String getPortalTransactionId() {
		return portalTransactionId;
	}

	/**
	 * @param portalTransactionId the portalTransactionId to set
	 */
	public void setPortalTransactionId(String portalTransactionId) {
		this.portalTransactionId = portalTransactionId;
	}
	
	

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	



	/**
	 * @return the productServices
	 */
	public ProductServicesQueryBean getProductServices() {
		return productServices;
	}

	/**
	 * @param productServices the productServices to set
	 */
	public void setProductServices(ProductServicesQueryBean productServices) {
		this.productServices = productServices;
	}
	
	

	public CustomerContractingEntityBean getCustomerContractingEntityBean() {
		return customerContractingEntityBean;
	}

	public void setCustomerContractingEntityBean(CustomerContractingEntityBean customerContractingEntityBean) {
		this.customerContractingEntityBean = customerContractingEntityBean;
	}

	@Override
	public String toString() {
		return "CreditCheckQueryResponse [id=" + id + ", opportunityId=" + opportunityId + ", billingFrequency="
				+ billingFrequency + ", paymentTerm=" + paymentTerm + ", billingMethod=" + billingMethod
				+ ", iccEnterpiceVoiceProductFlag=" + iccEnterpiceVoiceProductFlag + ", customerContractingEntity="
				+ customerContractingEntity + ", tataBillingEntity=" + tataBillingEntity + ", opportunityMRC="
				+ opportunityMRC + ", opportunityNRC=" + opportunityNRC + ", annualizedContractValue="
				+ annualizedContractValue + ", bundledMRC=" + bundledMRC + ", copfId=" + copfId + ", previousMRC="
				+ previousMRC + ", differentialMRC=" + differentialMRC + ", isPreApprovedOpportunity="
				+ isPreApprovedOpportunity + ", conditionalApprovalType=" + conditionalApprovalType
				+ ", conditionalApprovalRemarks=" + conditionalApprovalRemarks + ", mrcNrc=" + mrcNrc
				+ ", notifyCreditControlTeam=" + notifyCreditControlTeam + ", creditControlProcessComments="
				+ creditControlProcessComments + ", statusOfCreditControl=" + statusOfCreditControl + ", creditRating="
				+ creditRating + ", reservedBy=" + reservedBy + ", creditLimit=" + creditLimit + ", creditRemarks="
				+ creditRemarks + ", redoCreditVerification=" + redoCreditVerification + ", approvedBy=" + approvedBy
				+ ", dateOfCreditApproval=" + dateOfCreditApproval + ", securityDepositRequired="
				+ securityDepositRequired + ", securityDepositAmount=" + securityDepositAmount 
				+ ", portalTransactionId=" + portalTransactionId + ", customerName=" + customerName
				+  ", accountId=" + accountId
				+  ", productServices=" + productServices
				+  ", customerContractingEntityBean=" + customerContractingEntityBean
				+"]";
	}
	
	
	
	
	
}
