package com.tcl.dias.oms.gsc.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * Quote to le bean for GSC multi LE
 * 
 * @author Srinivasa Raghavan
 *
 */
public class GscMultiQuoteLeBean {

	private Integer quoteleId;

	private String quoteLeCode;

	private Integer currencyId;

	private Integer customerLegalEntityId;

	private Integer supplierLegalEntityId;

	private Double finalMrc;

	private Double finalNrc;

	private Double finalArc;

	private Double proposedMrc;

	private Double proposedNrc;

	private Double proposedArc;

	private Double totalTcv;

	private String tpsSfdcOptyId;

	private String stage;

	private String subStage;

	private String paymentCurrency;

	private String termsInMonths;

	private String quoteType;

	private Integer erfServiceInventoryParentOrderId;

	private String sourceSystem;

	private String quoteCategory;

	private List<String> erfServiceInventoryTpsServiceId = new ArrayList<>();

	private Boolean isDocusign;
	private Boolean isManualCofSigned = false;
	// Only for MACD case
	private Integer supplierLegalId;
	private String classification;
	private String isGscMultiMacd;
	private Boolean isWholesale = false;

	private Double partnerOptyExpectedArc;
	private Double partnerOptyExpectedNrc;
	private String partnerOptyExpectedCurrency;

	private List<GscMultipleLESolutionBean> voiceSolutions;

	public GscMultiQuoteLeBean() {
		// Default constructor
	}

	public GscMultiQuoteLeBean(QuoteToLe quoteToLe) {
		this.quoteleId = quoteToLe.getId();
		this.currencyId = quoteToLe.getCurrencyId();
		this.customerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
		this.supplierLegalEntityId = quoteToLe.getErfCusSpLegalEntityId();
		this.finalMrc = quoteToLe.getFinalMrc();
		this.finalNrc = quoteToLe.getFinalNrc();
		this.finalArc = quoteToLe.getFinalArc();
		this.proposedMrc = quoteToLe.getProposedMrc();
		this.proposedNrc = quoteToLe.getProposedNrc();
		this.proposedArc = quoteToLe.getProposedArc();
		this.totalTcv = quoteToLe.getTotalTcv();
		this.tpsSfdcOptyId = quoteToLe.getTpsSfdcOptyId();
		this.stage = quoteToLe.getStage();
		this.paymentCurrency = quoteToLe.getCurrencyCode();
		this.termsInMonths = quoteToLe.getTermInMonths();
		this.erfServiceInventoryParentOrderId = quoteToLe.getErfServiceInventoryParentOrderId();
		this.quoteType = quoteToLe.getQuoteType();
		this.sourceSystem = quoteToLe.getSourceSystem();
		this.quoteCategory = quoteToLe.getQuoteCategory();
		this.subStage = quoteToLe.getSubStage();
		this.quoteLeCode = quoteToLe.getQuoteLeCode();
		if (Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId())) {
			this.erfServiceInventoryTpsServiceId = Arrays
					.asList(quoteToLe.getErfServiceInventoryTpsServiceId().split(","));
		}
	}

	/**
	 * @return the quoteleId
	 */
	public Integer getQuoteleId() {
		return quoteleId;
	}

	/**
	 * @param quoteleId the quoteleId to set
	 */
	public void setQuoteleId(Integer quoteleId) {
		this.quoteleId = quoteleId;
	}

	public String getQuoteLeCode() {
		return quoteLeCode;
	}

	public void setQuoteLeCode(String quoteLeCode) {
		this.quoteLeCode = quoteLeCode;
	}

	/**
	 * @return the currencyId
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * @return the customerLegalEntityId
	 */
	public Integer getCustomerLegalEntityId() {
		return customerLegalEntityId;
	}

	/**
	 * @param customerLegalEntityId the customerLegalEntityId to set
	 */
	public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
		this.customerLegalEntityId = customerLegalEntityId;
	}

	/**
	 * @return the supplierLegalEntityId
	 */
	public Integer getSupplierLegalEntityId() {
		return supplierLegalEntityId;
	}

	/**
	 * @param supplierLegalEntityId the supplierLegalEntityId to set
	 */
	public void setSupplierLegalEntityId(Integer supplierLegalEntityId) {
		this.supplierLegalEntityId = supplierLegalEntityId;
	}

	/**
	 * @return the finalMrc
	 */
	public Double getFinalMrc() {
		return finalMrc;
	}

	/**
	 * @param finalMrc the finalMrc to set
	 */
	public void setFinalMrc(Double finalMrc) {
		this.finalMrc = finalMrc;
	}

	/**
	 * @return the finalNrc
	 */
	public Double getFinalNrc() {
		return finalNrc;
	}

	/**
	 * @param finalNrc the finalNrc to set
	 */
	public void setFinalNrc(Double finalNrc) {
		this.finalNrc = finalNrc;
	}

	/**
	 * @return the proposedMrc
	 */
	public Double getProposedMrc() {
		return proposedMrc;
	}

	/**
	 * @param proposedMrc the proposedMrc to set
	 */
	public void setProposedMrc(Double proposedMrc) {
		this.proposedMrc = proposedMrc;
	}

	/**
	 * @return the proposedNrc
	 */
	public Double getProposedNrc() {
		return proposedNrc;
	}

	/**
	 * @param proposedNrc the proposedNrc to set
	 */
	public void setProposedNrc(Double proposedNrc) {
		this.proposedNrc = proposedNrc;
	}

	/**
	 * @return the tpsSfdcOptyId
	 */
	public String getTpsSfdcOptyId() {
		return tpsSfdcOptyId;
	}

	/**
	 * @param tpsSfdcOptyId the tpsSfdcOptyId to set
	 */
	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	public Double getFinalArc() {
		return finalArc;
	}

	public void setFinalArc(Double finalArc) {
		this.finalArc = finalArc;
	}

	public Double getProposedArc() {
		return proposedArc;
	}

	public void setProposedArc(Double proposedArc) {
		this.proposedArc = proposedArc;
	}

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getTermsInMonths() {
		return termsInMonths;
	}

	public void setTermsInMonths(String termsInMonths) {
		this.termsInMonths = termsInMonths;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	public List<String> getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(List<String> erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public List<GscMultipleLESolutionBean> getVoiceSolutions() {
		return voiceSolutions;
	}

	public void setVoiceSolutions(List<GscMultipleLESolutionBean> voiceSolutions) {
		this.voiceSolutions = voiceSolutions;
	}

	public Boolean getDocusign() {
		return isDocusign;
	}

	public void setDocusign(Boolean docusign) {
		isDocusign = docusign;
	}

	public Boolean getManualCofSigned() {
		return isManualCofSigned;
	}

	public void setManualCofSigned(Boolean manualCofSigned) {
		isManualCofSigned = manualCofSigned;
	}

	public Integer getSupplierLegalId() {
		return supplierLegalId;
	}

	public void setSupplierLegalId(Integer supplierLegalId) {
		this.supplierLegalId = supplierLegalId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getIsGscMultiMacd() {
		return isGscMultiMacd;
	}

	public void setIsGscMultiMacd(String isGscMultiMacd) {
		this.isGscMultiMacd = isGscMultiMacd;
	}

	public Boolean getWholesale() {
		return isWholesale;
	}

	public void setWholesale(Boolean wholesale) {
		isWholesale = wholesale;
	}

	public Double getPartnerOptyExpectedArc() {
		return partnerOptyExpectedArc;
	}

	public void setPartnerOptyExpectedArc(Double partnerOptyExpectedArc) {
		this.partnerOptyExpectedArc = partnerOptyExpectedArc;
	}

	public Double getPartnerOptyExpectedNrc() {
		return partnerOptyExpectedNrc;
	}

	public void setPartnerOptyExpectedNrc(Double partnerOptyExpectedNrc) {
		this.partnerOptyExpectedNrc = partnerOptyExpectedNrc;
	}

	public String getPartnerOptyExpectedCurrency() {
		return partnerOptyExpectedCurrency;
	}

	public void setPartnerOptyExpectedCurrency(String partnerOptyExpectedCurrency) {
		this.partnerOptyExpectedCurrency = partnerOptyExpectedCurrency;
	}

	/**
	 * convert from QuoteToLe to GscQuoteToLeBean
	 *
	 * @param quoteToLe
	 * @param isMultiMacdAttribute
	 * @param opportunity
	 * @return
	 */
	public static GscMultiQuoteLeBean fromQuoteToLe(QuoteToLe quoteToLe, Boolean isDocusign, Boolean isManualCofSigned,
			String isMultiMacdAttribute, Opportunity opportunity) {
		GscMultiQuoteLeBean bean = new GscMultiQuoteLeBean();
		bean.quoteleId = quoteToLe.getId();
		bean.finalMrc = quoteToLe.getFinalMrc();
		bean.finalNrc = quoteToLe.getFinalNrc();
		bean.finalArc = quoteToLe.getFinalArc();
		bean.proposedMrc = quoteToLe.getProposedMrc();
		bean.proposedNrc = quoteToLe.getProposedNrc();
		bean.proposedArc = quoteToLe.getProposedArc();
		bean.totalTcv = quoteToLe.getTotalTcv();
		bean.currencyId = quoteToLe.getCurrencyId();
		bean.customerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
		bean.supplierLegalEntityId = quoteToLe.getErfCusSpLegalEntityId();
		bean.tpsSfdcOptyId = quoteToLe.getTpsSfdcOptyId();
		bean.stage = quoteToLe.getStage();
		bean.subStage = quoteToLe.getSubStage();
		bean.paymentCurrency = quoteToLe.getCurrencyCode();
		bean.termsInMonths = quoteToLe.getTermInMonths();
		bean.erfServiceInventoryParentOrderId = quoteToLe.getErfServiceInventoryParentOrderId();
		bean.quoteType = quoteToLe.getQuoteType();
		bean.sourceSystem = quoteToLe.getSourceSystem();
		bean.quoteCategory = quoteToLe.getQuoteCategory();
		bean.isDocusign = isDocusign;
		bean.isManualCofSigned = isManualCofSigned;
		// Only for MACD case
		bean.supplierLegalId = quoteToLe.getErfCusSpLegalEntityId();
		bean.classification = quoteToLe.getClassification();
		bean.isGscMultiMacd = isMultiMacdAttribute;
		bean.isWholesale = CommonConstants.BACTIVE.equals(quoteToLe.getIsWholesale());
		bean.quoteLeCode = quoteToLe.getQuoteLeCode();

		if (Objects.nonNull(opportunity)) {
			bean.setPartnerOptyExpectedArc((opportunity.getExpectedMrc() * 12));
			bean.setPartnerOptyExpectedNrc(opportunity.getExpectedNrc());
			bean.setPartnerOptyExpectedCurrency(opportunity.getExpectedCurrency());
		}
		if (Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId())) {
			bean.erfServiceInventoryTpsServiceId = Arrays
					.asList(quoteToLe.getErfServiceInventoryTpsServiceId().split(","));
		}
		return bean;
	}

	/**
	 * convert from GscQuoteToLeBean to QuoteToLe
	 *
	 * @param quoteToLeBean
	 * @return
	 */
	public static QuoteToLe toQuoteToLe(GscMultiQuoteLeBean quoteToLeBean) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(quoteToLeBean.quoteleId);
		quoteToLe.setFinalMrc(quoteToLeBean.finalMrc);
		quoteToLe.setFinalNrc(quoteToLeBean.finalNrc);
		quoteToLe.setFinalArc(quoteToLeBean.finalArc);
		quoteToLe.setProposedMrc(quoteToLeBean.proposedMrc);
		quoteToLe.setProposedNrc(quoteToLeBean.proposedNrc);
		quoteToLe.setProposedArc(quoteToLeBean.getProposedArc());
		quoteToLe.setTotalTcv(quoteToLeBean.totalTcv);
		quoteToLe.setCurrencyId(quoteToLeBean.currencyId);
		quoteToLe.setErfCusCustomerLegalEntityId(quoteToLeBean.customerLegalEntityId);
		quoteToLe.setErfCusSpLegalEntityId(quoteToLeBean.supplierLegalEntityId);
		quoteToLe.setTpsSfdcOptyId(quoteToLeBean.tpsSfdcOptyId);
		quoteToLe.setStage(quoteToLeBean.stage);
		quoteToLe.setTermInMonths(quoteToLeBean.getTermsInMonths());
		quoteToLe.setCurrencyCode(quoteToLeBean.getPaymentCurrency());
		quoteToLe.setClassification(quoteToLeBean.classification);
		quoteToLe.setQuoteCategory(quoteToLeBean.quoteCategory);
		quoteToLe.setQuoteType(quoteToLeBean.quoteType);
		quoteToLe.setSubStage(quoteToLeBean.subStage);
		quoteToLe.setQuoteLeCode(quoteToLeBean.quoteLeCode);
		quoteToLe.setIsWholesale(
				quoteToLeBean.isWholesale == true ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE);
		if (Objects.nonNull(quoteToLeBean.getErfServiceInventoryTpsServiceId())
				&& !CollectionUtils.isEmpty(quoteToLeBean.getErfServiceInventoryTpsServiceId())) {
			quoteToLe.setErfServiceInventoryTpsServiceId(
					String.join(",", quoteToLeBean.getErfServiceInventoryTpsServiceId()));
		}
		return quoteToLe;
	}

	@Override
	public String toString() {
		return "GscQuoteToLeBean{" + "quoteleId=" + quoteleId + ", currencyId=" + currencyId
				+ ", customerLegalEntityId=" + customerLegalEntityId + ", supplierLegalEntityId="
				+ supplierLegalEntityId + ", finalMrc=" + finalMrc + ", finalNrc=" + finalNrc + ", finalArc=" + finalArc
				+ ", proposedMrc=" + proposedMrc + ", proposedNrc=" + proposedNrc + ", proposedArc=" + proposedArc
				+ ", totalTcv=" + totalTcv + ", tpsSfdcOptyId='" + tpsSfdcOptyId + '\'' + ", stage='" + stage + '\''
				+ "subStage" + subStage + "quoteLeCode" + quoteLeCode + ", paymentCurrency='" + paymentCurrency + '\''
				+ ", termsInMonths='" + termsInMonths + '\'' + ", quoteType='" + quoteType + '\''
				+ ", erfServiceInventoryParentOrderId=" + erfServiceInventoryParentOrderId + ", sourceSystem='"
				+ sourceSystem + '\'' + ", quoteCategory='" + quoteCategory + '\''
				+ ", erfServiceInventoryTpsServiceId=" + erfServiceInventoryTpsServiceId + '}';
	}

}
