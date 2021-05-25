package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.CommercialAttributesVproxy;
import com.tcl.dias.oms.izosdwan.pdf.beans.VproxyCommercialDetailsBean;
/**
 * 
 * This bean is used to bind the values for PDF generation
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanQuotePdfBean implements Serializable{
	private String quoteCode;
	private String createdDate;
	private List<ChargeableLineItemSummaryBean> chargeableLineItemSummaryBeans;
	private String arcTcv;
	private String nrcTcv;
	private String mrcTcv;
	private String tcv;
	private Boolean isVproxy=false;
	private Boolean isVproxyComm=false;
	private Boolean isPureByon=false;
	public Boolean getIsVproxy() {
		return isVproxy;
	}
	public Boolean getIsPureByon() {
		return isPureByon;
	}
	public void setIsPureByon(Boolean isPureByon) {
		this.isPureByon = isPureByon;
	}
	public void setIsVproxy(Boolean isVproxy) {
		this.isVproxy = isVproxy;
	}
	public Boolean getIsVproxyComm() {
		return isVproxyComm;
	}
	public void setIsVproxyComm(Boolean isVproxyComm) {
		this.isVproxyComm = isVproxyComm;
	}
	private List<IzosdwanPdfSiteBean> izosdwanPdfSiteBeans;
    private List<CommercialAttributesVproxy> commonComponentsVproxy;
    private List<VproxyCommercialDetailsBean> commercialDetailsVproxySolutions;

	public List<CommercialAttributesVproxy> getCommonComponentsVproxy() {
		return commonComponentsVproxy;
	}
	public void setCommonComponentsVproxy(List<CommercialAttributesVproxy> commonComponentsVproxy) {
		this.commonComponentsVproxy = commonComponentsVproxy;
	}
	public List<VproxyCommercialDetailsBean> getCommercialDetailsVproxySolutions() {
		return commercialDetailsVproxySolutions;
	}
	public void setCommercialDetailsVproxySolutions(List<VproxyCommercialDetailsBean> commercialDetailsVproxy) {
		this.commercialDetailsVproxySolutions = commercialDetailsVproxy;
	}
	private String primaryLocation;
	private String secondaryLocation;
	private String cgwGatewayBW;
	private String cgwServiceBW;
	private String migrationBandwidth;
	private BigDecimal effectiveArc;
	private BigDecimal effectiveNrc;
	private BigDecimal effectiveMrc;
	private String currency;
	private String contractTerm;
	private String billCurrencyArc;
    private String billCurrencyNrc;
	private String billCurrencyMrc;
   
    private String totalBillCurArc;
    private String totalBillCurNrc;
	private String totalBillCurMrc;
    private String totalBillCurrCharges;

	public Boolean isIndia=true;

	public String getBillCurrencyArc() {
		return billCurrencyArc;
	}
	public void setBillCurrencyArc(String billCurrencyArc) {
		this.billCurrencyArc = billCurrencyArc;
	}
	public String getBillCurrencyNrc() {
		return billCurrencyNrc;
	}
	public void setBillCurrencyNrc(String billCurrencyNrc) {
		this.billCurrencyNrc = billCurrencyNrc;
	}
	public String getTotalBillCurArc() {
		return totalBillCurArc;
	}
	public void setTotalBillCurArc(String totalBillCurArc) {
		this.totalBillCurArc = totalBillCurArc;
	}
	public String getTotalBillCurNrc() {
		return totalBillCurNrc;
	}
	public void setTotalBillCurNrc(String totalBillCurNrc) {
		this.totalBillCurNrc = totalBillCurNrc;
	}
	
	public String getContractTerm() {
		return contractTerm;
	}
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public List<ChargeableLineItemSummaryBean> getChargeableLineItemSummaryBeans() {
		return chargeableLineItemSummaryBeans;
	}
	public void setChargeableLineItemSummaryBeans(List<ChargeableLineItemSummaryBean> chargeableLineItemSummaryBeans) {
		this.chargeableLineItemSummaryBeans = chargeableLineItemSummaryBeans;
	}
	public String getArcTcv() {
		return arcTcv;
	}
	public void setArcTcv(String arcTcv) {
		this.arcTcv = arcTcv;
	}
	public String getNrcTcv() {
		return nrcTcv;
	}
	public void setNrcTcv(String nrcTcv) {
		this.nrcTcv = nrcTcv;
	}
	public String getTcv() {
		return tcv;
	}
	public void setTcv(String tcv) {
		this.tcv = tcv;
	}
	public List<IzosdwanPdfSiteBean> getIzosdwanPdfSiteBeans() {
		return izosdwanPdfSiteBeans;
	}
	public void setIzosdwanPdfSiteBeans(List<IzosdwanPdfSiteBean> izosdwanPdfSiteBeans) {
		this.izosdwanPdfSiteBeans = izosdwanPdfSiteBeans;
	}
	public String getPrimaryLocation() {
		return primaryLocation;
	}
	public void setPrimaryLocation(String primaryLocation) {
		this.primaryLocation = primaryLocation;
	}
	public String getSecondaryLocation() {
		return secondaryLocation;
	}
	public void setSecondaryLocation(String secondaryLocation) {
		this.secondaryLocation = secondaryLocation;
	}
	public String getCgwGatewayBW() {
		return cgwGatewayBW;
	}
	public void setCgwGatewayBW(String cgwGatewayBW) {
		this.cgwGatewayBW = cgwGatewayBW;
	}
	public String getCgwServiceBW() {
		return cgwServiceBW;
	}
	public void setCgwServiceBW(String cgwServiceBW) {
		this.cgwServiceBW = cgwServiceBW;
	}
	public String getMigrationBandwidth() {
		return migrationBandwidth;
	}
	public void setMigrationBandwidth(String migrationBandwidth) {
		this.migrationBandwidth = migrationBandwidth;
	}
	public BigDecimal getEffectiveArc() {
		return effectiveArc;
	}
	public void setEffectiveArc(BigDecimal effectiveArc) {
		this.effectiveArc = effectiveArc;
	}
	public BigDecimal getEffectiveNrc() {
		return effectiveNrc;
	}
	public void setEffectiveNrc(BigDecimal effectiveNrc) {
		this.effectiveNrc = effectiveNrc;
	}
	public String getTotalBillCurrCharges() {
		return totalBillCurrCharges;
	}
	public void setTotalBillCurrCharges(String totalBillCurrCharges) {
		this.totalBillCurrCharges = totalBillCurrCharges;
	}

	public BigDecimal getEffectiveMrc() {
		return effectiveMrc;
	}

	public void setEffectiveMrc(BigDecimal effectiveMrc) {
		this.effectiveMrc = effectiveMrc;
	}
	public Boolean getIsIndia() {
		return isIndia;
	}

	public void setIsIndia(Boolean isIndia) {
		this.isIndia = isIndia;
	}

	public String getTotalBillCurMrc() {
		return totalBillCurMrc;
	}

	public void setTotalBillCurMrc(String totalBillCurMrc) {
		this.totalBillCurMrc = totalBillCurMrc;
	}

	public String getBillCurrencyMrc() {
		return billCurrencyMrc;
	}

	public void setBillCurrencyMrc(String billCurrencyMrc) {
		this.billCurrencyMrc = billCurrencyMrc;
	}

	public String getMrcTcv() {
		return mrcTcv;
	}

	public void setMrcTcv(String mrcTcv) {
		this.mrcTcv = mrcTcv;
	}
}
