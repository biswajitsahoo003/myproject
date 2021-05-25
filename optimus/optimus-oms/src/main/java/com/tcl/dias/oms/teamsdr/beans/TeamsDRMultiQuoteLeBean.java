package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.gsc.beans.GscMultipleLESolutionBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Bean for Teams DR QuoteToLe
 *
 * @author Syed Ali
 *
 */
public class TeamsDRMultiQuoteLeBean {
    private Integer quoteleId;

	private String quoteLeCode;

    private Integer currencyId;

    private Integer customerLegalEntityId;

    private String customerlegalEntityName;

    private Integer supplierLegalEntityId;

    private Double finalMrc;

    private Double finalNrc;

    private Double finalArc;

    private Double proposedMrc;

    private Double proposedNrc;

    private Double proposedArc;

    private Double totalTcv;

    private String tpsSfdcParentOptyId;

    private String tpsSfdcOptyId;

    private String stage;

	private String subStage;

    private Set<QuoteToLeProductFamilyBean> productFamilies;

    private Set<LegalAttributeBean> legalAttributes;

    private String termInMonths;

    private String currency;

    private String classification;

    private Double creditLimit;

    private Double securityDepositAmount;

    private Byte isMultiCircuit;

	private String quoteType;

	private String quoteCategory;

	// for demo orders
    private Boolean isDemo;
    private String demoType;

    private String contractPeriod;
    private Boolean isDocusign;
    private Boolean isManualCofSigned = false;
    private List<GscMultipleLESolutionBean> voiceSolutions;
    private TeamsDRSolutionBean teamsDRSolution;

    private Boolean isWholesale = false;

    public TeamsDRMultiQuoteLeBean() {
        this.isManualCofSigned=false;
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

    public Boolean getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(Boolean isDemo) {
        this.isDemo = isDemo;
    }

    public String getDemoType() {
        return demoType;
    }

    public void setDemoType(String demoType) {
        this.demoType = demoType;
    }

    private Double partnerOptyExpectedArc;

    private Double partnerOptyExpectedNrc;

    private String partnerOptyExpectedCurrency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(String termInMonths) {
        this.termInMonths = termInMonths;
    }

    public TeamsDRMultiQuoteLeBean(QuoteToLe quoteToLe) {
        if (quoteToLe != null) {
            this.quoteleId = quoteToLe.getId();
            this.finalMrc = quoteToLe.getFinalMrc();
            this.finalNrc = quoteToLe.getFinalNrc();
            this.finalArc = quoteToLe.getFinalArc();
            this.proposedMrc = quoteToLe.getProposedMrc();
            this.proposedNrc = quoteToLe.getProposedNrc();
            this.proposedArc = quoteToLe.getProposedArc();
            this.totalTcv = quoteToLe.getTotalTcv();
            this.currencyId = quoteToLe.getCurrencyId();
            this.customerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
            this.supplierLegalEntityId = quoteToLe.getErfCusSpLegalEntityId();
            this.tpsSfdcOptyId = quoteToLe.getTpsSfdcOptyId();
            this.stage = quoteToLe.getStage();
			this.subStage = quoteToLe.getSubStage();
            this.classification = quoteToLe.getClassification();
            this.creditLimit = quoteToLe.getTpsSfdcCreditLimit();
            this.securityDepositAmount = quoteToLe.getTpsSfdcSecurityDepositAmount();
            this.isMultiCircuit = quoteToLe.getIsMultiCircuit();
            this.contractPeriod = quoteToLe.getTermInMonths();
			this.currency = quoteToLe.getCurrencyCode();
			this.quoteCategory = quoteToLe.getQuoteCategory();
			this.quoteType = quoteToLe.getQuoteType();
			this.quoteLeCode = quoteToLe.getQuoteLeCode();
        }
    }

	public static QuoteToLe toQuoteToLe(TeamsDRMultiQuoteLeBean quoteToLeBean) {
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
		quoteToLe.setSubStage(quoteToLeBean.subStage);
		quoteToLe.setTermInMonths(quoteToLeBean.getContractPeriod());
		quoteToLe.setCurrencyCode(quoteToLeBean.getCurrency());
		quoteToLe.setClassification(quoteToLeBean.classification);
		quoteToLe.setQuoteCategory(quoteToLeBean.quoteCategory);
		quoteToLe.setQuoteType(quoteToLeBean.quoteType);
		quoteToLe.setIsWholesale(quoteToLeBean.isWholesale ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE);
		quoteToLe.setQuoteLeCode(quoteToLeBean.quoteLeCode);
		return quoteToLe;
	}

	TeamsDRMultiQuoteLeBean(String a) {

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

    /**
     * @return the productFamilies
     */
    public Set<QuoteToLeProductFamilyBean> getProductFamilies() {
        return productFamilies;
    }

    /**
     * @param productFamilies the productFamilies to set
     */
    public void setProductFamilies(Set<QuoteToLeProductFamilyBean> productFamilies) {
        this.productFamilies = productFamilies;
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

    /**
     * @return the legalAttributes
     */
    public Set<LegalAttributeBean> getLegalAttributes() {
        if (legalAttributes == null) {
            legalAttributes = new HashSet<>();
        }
        return legalAttributes;
    }

    /**
     * @param legalAttributes the legalAttributes to set
     */
    public void setLegalAttributes(Set<LegalAttributeBean> legalAttributes) {
        this.legalAttributes = legalAttributes;
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

	public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    public Byte getIsMultiCircuit() {
        return isMultiCircuit;
    }

    public void setIsMultiCircuit(Byte isMultiCircuit) {
        this.isMultiCircuit = isMultiCircuit;
    }

    public String getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(String contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public Boolean getIsDocusign() {
        return isDocusign;
    }

    public void setIsDocusign(Boolean isDocusign) {
        this.isDocusign = isDocusign;
    }

    public Boolean getIsManualCofSigned() {
        return isManualCofSigned;
    }

    public void setIsManualCofSigned(Boolean isManualCofSigned) {
        this.isManualCofSigned = isManualCofSigned;
    }

    public TeamsDRSolutionBean getTeamsDRSolution() {
        return teamsDRSolution;
    }

    public void setTeamsDRSolution(TeamsDRSolutionBean teamsDRSolution) {
        this.teamsDRSolution = teamsDRSolution;
    }

    public List<GscMultipleLESolutionBean> getVoiceSolutions() {
        return voiceSolutions;
    }

    public void setVoiceSolutions(List<GscMultipleLESolutionBean> voiceSolutions) {
        this.voiceSolutions = voiceSolutions;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

    public String getTpsSfdcParentOptyId() {
        return tpsSfdcParentOptyId;
    }

    public void setTpsSfdcParentOptyId(String tpsSfdcParentOptyId) {
        this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
    }

    public String getCustomerlegalEntityName() {
        return customerlegalEntityName;
    }

    public void setCustomerlegalEntityName(String customerlegalEntityName) {
        this.customerlegalEntityName = customerlegalEntityName;
    }

	public String getQuoteLeCode() {
		return quoteLeCode;
	}

	public void setQuoteLeCode(String quoteLeCode) {
		this.quoteLeCode = quoteLeCode;
	}

    @Override
    public String toString() {
        return "TeamsDRMultiQuoteLeBean{" +
                "quoteleId=" + quoteleId +
                "quoteLeCode" + quoteLeCode +
                ", currencyId=" + currencyId +
                ", customerLegalEntityId=" + customerLegalEntityId +
                ", customerlegalEntityName='" + customerlegalEntityName + '\'' +
                ", supplierLegalEntityId=" + supplierLegalEntityId +
                ", finalMrc=" + finalMrc +
                ", finalNrc=" + finalNrc +
                ", finalArc=" + finalArc +
                ", proposedMrc=" + proposedMrc +
                ", proposedNrc=" + proposedNrc +
                ", proposedArc=" + proposedArc +
                ", totalTcv=" + totalTcv +
                ", tpsSfdcParentOptyId='" + tpsSfdcParentOptyId + '\'' +
                ", tpsSfdcOptyId='" + tpsSfdcOptyId + '\'' +
                ", stage='" + stage + '\'' +
				", subStage='" + subStage + '\'' +
				", productFamilies=" + productFamilies +
                ", legalAttributes=" + legalAttributes +
                ", termInMonths='" + termInMonths + '\'' +
                ", currency='" + currency + '\'' +
                ", classification='" + classification + '\'' +
                ", creditLimit=" + creditLimit +
                ", securityDepositAmount=" + securityDepositAmount +
                ", isMultiCircuit=" + isMultiCircuit +
                ", quoteType='" + quoteType + '\'' +
                ", quoteCategory='" + quoteCategory + '\'' +
                ", isDemo=" + isDemo +
                ", demoType='" + demoType + '\'' +
                ", contractPeriod='" + contractPeriod + '\'' +
                ", isDocusign=" + isDocusign +
                ", isManualCofSigned=" + isManualCofSigned +
                ", voiceSolutions=" + voiceSolutions +
                ", teamsDRSolution=" + teamsDRSolution +
                ", isWholesale=" + isWholesale +
                ", partnerOptyExpectedArc=" + partnerOptyExpectedArc +
                ", partnerOptyExpectedNrc=" + partnerOptyExpectedNrc +
                ", partnerOptyExpectedCurrency='" + partnerOptyExpectedCurrency + '\'' +
                '}';
    }
}
