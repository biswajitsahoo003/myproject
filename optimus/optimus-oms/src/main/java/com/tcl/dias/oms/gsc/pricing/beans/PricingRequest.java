package com.tcl.dias.oms.gsc.pricing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Request bean for Pricing
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "account_id_with_18_digit", "cu_le_id", "Account_RTM_Cust", "customer_segment", "sales_org",
		"volume_projected", "inbound_volume", "outbound_volume", "inbound_select_cust", "outbound_select_cust",
		"quotetype_quote", "macd_new_country", "org_id", "opportunity_term", "access_type", "itfs_requested",
		"lns_requested", "uifn_requested", "acs_dtf_requested", "acs_ans_requested", "acs_sns_requested",
		"dom_voice_requested", "glob_out_requested", "origin_itfs", "destination_itfs", "no_requested_itfs",
		"no_ported_itfs", "origin_lns", "destination_lns", "no_requested_lns", "no_ported_lns", "origin_uifn",
		"destination_uifn", "no_requested_uifn", "no_ported_uifn", "origin_dtfs", "destination_dtfs",
		"no_requested_dtfs", "origin_acns", "destination_acns", "no_requested_acns", "origin_acsns",
		"destination_acsns", "no_requested_acsns", "origin_dom_voice", "no_requested_dom_voice", "no_ported_dom_voice",
		"channels_dom_voice", "destination_glob_out", "ucaas_flag", "number_type", "payment_mode" })
public class PricingRequest {

	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;

	@JsonProperty("cu_le_id")
	private String cuLeId;

	@JsonProperty("customer_segment")
	private String customerSegment;

	@JsonProperty("Account_RTM_Cust")
	private String accountRTMCust;

	@JsonProperty("sales_org")
	private String salesOrg;

	@JsonProperty("volume_projected")
	private String volumeProjected;

	@JsonProperty("inbound_volume")
	private String inboundVolume;

	@JsonProperty("outbound_volume")
	private String outboundVolume;

	@JsonProperty("inbound_select_cust")
	private List<String> inboundSelectCust;

	@JsonProperty("outbound_select_cust")
	private List<String> outboundSelectCust;

	@JsonProperty("quotetype_quote")
	private String quoteTypeQuote;

	@JsonProperty("macd_new_country")
	private String macdNewCountry;

	@JsonProperty("org_id")
	private String orgId;

	@JsonProperty("opportunity_term")
	private String opportunityTerm;

	@JsonProperty("access_type")
	private String accessType;

	@JsonProperty("itfs_requested")
	private String itfsRequested;

	@JsonProperty("lns_requested")
	private String lnsRequested;

	@JsonProperty("uifn_requested")
	private String uifnRequested;

	@JsonProperty("acs_dtf_requested")
	private String acsDtfRequested;

	@JsonProperty("acs_sns_requested")
	private String acsSnsRequested;

	@JsonProperty("acs_ans_requested")
	private String acsAnsRequested;

	@JsonProperty("dom_voice_requested")
	private String domesticVoiceRequested;

	@JsonProperty("glob_out_requested")
	private String globalOutboundRequested;

	@JsonProperty("origin_itfs")
	private List<String> originItfs;

	@JsonProperty("destination_itfs")
	private List<String> destinationItfs;

	@JsonProperty("no_requested_itfs")
	private List<String> noRequestedItfs;

	@JsonProperty("no_ported_itfs")
	private List<String> noPortedItfs;

	@JsonProperty("origin_lns")
	private List<String> originLns;

	@JsonProperty("destination_lns")
	private List<String> destinationLns;

	@JsonProperty("no_requested_lns")
	private List<String> noRequestedLns;

	@JsonProperty("no_ported_lns")
	private List<String> noPortedLns;

	@JsonProperty("origin_uifn")
	private List<String> originUifn;

	@JsonProperty("destination_uifn")
	private List<String> destinationUifn;

	@JsonProperty("no_requested_uifn")
	private List<String> noRequestedUifn;

	@JsonProperty("no_ported_uifn")
	private List<String> noPortedUifn;

	@JsonProperty("origin_dtfs")
	private List<String> originDtfs;

	@JsonProperty("destination_dtfs")
	private List<String> destinationDtfs;

	@JsonProperty("no_requested_dtfs")
	private List<String> noRequestedDtfs;

	@JsonProperty("origin_acns")
	private List<String> originAcns;

	@JsonProperty("destination_acns")
	private List<String> destinationAcns;

	@JsonProperty("no_requested_acns")
	private List<String> noRequestedAcns;

	@JsonProperty("origin_acsns")
	private List<String> originAcsns;

	@JsonProperty("destination_acsns")
	private List<String> destinationAcsns;

	@JsonProperty("no_requested_acsns")
	private List<String> noRequestedAcsns;

	@JsonProperty("origin_dom_voice")
	private List<String> originDomesticVoice;

	@JsonProperty("no_requested_dom_voice")
	private List<String> noRequestedDomesticVoice;

	@JsonProperty("no_ported_dom_voice")
	private List<String> noPortedDomesticVoice;

	@JsonProperty("destination_glob_out")
	private List<String> destinationGlobalOutbound;

	@JsonProperty("channels_dom_voice")
	private List<String> channelsDomesticVoice;

	@JsonProperty("quotetype_partner")
	private String quoteTypePartner;

	// For ucaas
	@JsonProperty("ucaas_flag")
	private Byte isUcaas;

	@JsonProperty("number_type")
	private String numberType;

	@JsonProperty("payment_mode")
	private String paymentMode;

	@JsonProperty("go_catalog_flag")
	private String globalOutboundCatalogFlag;

	@JsonProperty("go_catalog_curr")
	private String globalOutboundCatalogCurrency;

	public PricingRequest() {
		this.itfsRequested = "0";
		this.lnsRequested = "0";
		this.uifnRequested = "0";
		this.acsDtfRequested = "0";
		this.acsAnsRequested = "0";
		this.acsSnsRequested = "0";
		this.domesticVoiceRequested = "0";
		this.globalOutboundRequested = "0";
		this.inboundVolume = "NA";
		this.outboundVolume = "NA";
		this.macdNewCountry = "0";
		this.inboundSelectCust = new ArrayList<>();
		this.outboundSelectCust = new ArrayList<>();
		this.originItfs = new ArrayList<>();
		this.destinationItfs = new ArrayList<>();
		this.noRequestedItfs = new ArrayList<>();
		this.noPortedItfs = new ArrayList<>();
		this.originLns = new ArrayList<>();
		this.destinationLns = new ArrayList<>();
		this.noRequestedLns = new ArrayList<>();
		this.noPortedLns = new ArrayList<>();
		this.originUifn = new ArrayList<>();
		this.destinationUifn = new ArrayList<>();
		this.noRequestedUifn = new ArrayList<>();
		this.noPortedUifn = new ArrayList<>();
		this.originDtfs = new ArrayList<>();
		this.destinationDtfs = new ArrayList<>();
		this.noRequestedDtfs = new ArrayList<>();
		this.originAcns = new ArrayList<>();
		this.destinationAcns = new ArrayList<>();
		this.noRequestedAcns = new ArrayList<>();
		this.originAcsns = new ArrayList<>();
		this.destinationAcsns = new ArrayList<>();
		this.noRequestedAcsns = new ArrayList<>();
		this.originDomesticVoice = new ArrayList<>();
		this.noRequestedDomesticVoice = new ArrayList<>();
		this.noPortedDomesticVoice = new ArrayList<>();
		this.destinationGlobalOutbound = new ArrayList<>();
		this.channelsDomesticVoice = new ArrayList<>();
		this.globalOutboundCatalogFlag = "0";
		this.globalOutboundCatalogCurrency = "NA";
	}

	@JsonProperty("account_id_with_18_digit")
	public String getAccountIdWith18Digit() {
		return accountIdWith18Digit;
	}

	@JsonProperty("account_id_with_18_digit")
	public void setAccountIdWith18Digit(String accountIdWith18Digit) {
		this.accountIdWith18Digit = accountIdWith18Digit;
	}

	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}

	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}

	@JsonProperty("customer_segment")
	public String getCustomerSegment() {
		return customerSegment;
	}

	@JsonProperty("customer_segment")
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	@JsonProperty("Account_RTM_Cust")
	public String getAccountRTMCust() {
		return accountRTMCust;
	}

	@JsonProperty("Account_RTM_Cust")
	public void setAccountRTMCust(String accountRTMCust) {
		this.accountRTMCust = accountRTMCust;
	}

	@JsonProperty("sales_org")
	public String getSalesOrg() {
		return salesOrg;
	}

	@JsonProperty("sales_org")
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	@JsonProperty("opportunity_term")
	public String getOpportunityTerm() {
		return opportunityTerm;
	}

	@JsonProperty("volume_projected")
	public String getVolumeProjected() {
		return volumeProjected;
	}

	@JsonProperty("volume_projected")
	public void setVolumeProjected(String volumeProjected) {
		this.volumeProjected = volumeProjected;
	}

	@JsonProperty("inbound_volume")
	public String getInboundVolume() {
		return inboundVolume;
	}

	@JsonProperty("inbound_volume")
	public void setInboundVolume(String inboundVolume) {
		this.inboundVolume = inboundVolume;
	}

	@JsonProperty("outbound_volume")
	public String getOutboundVolume() {
		return outboundVolume;
	}

	@JsonProperty("outbound_volume")
	public void setOutboundVolume(String outboundVolume) {
		this.outboundVolume = outboundVolume;
	}

	@JsonProperty("inbound_select_cust")
	public List<String> getInboundSelectCust() {
		return inboundSelectCust;
	}

	@JsonProperty("inbound_select_cust")
	public void setInboundSelectCust(List<String> inboundSelectCust) {
		this.inboundSelectCust = inboundSelectCust;
	}

	@JsonProperty("outbound_select_cust")
	public List<String> getOutboundSelectCust() {
		return outboundSelectCust;
	}

	@JsonProperty("outbound_select_cust")
	public void setOutboundSelectCust(List<String> outboundSelectCust) {
		this.outboundSelectCust = outboundSelectCust;
	}

	@JsonProperty("quotetype_quote")
	public String getQuoteTypeQuote() {
		return quoteTypeQuote;
	}

	@JsonProperty("quotetype_quote")
	public void setQuoteTypeQuote(String quoteTypeQuote) {
		this.quoteTypeQuote = quoteTypeQuote;
	}

	@JsonProperty("macd_new_country")
	public String getMacdNewCountry() {
		return macdNewCountry;
	}

	@JsonProperty("macd_new_country")
	public void setMacdNewCountry(String macdNewCountry) {
		this.macdNewCountry = macdNewCountry;
	}

	@JsonProperty("org_id")
	public String getOrgId() {
		return orgId;
	}

	@JsonProperty("org_id")
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@JsonProperty("opportunity_term")
	public void setOpportunityTerm(String opportunityTerm) {
		this.opportunityTerm = opportunityTerm;
	}

	@JsonProperty("access_type")
	public String getAccessType() {
		return accessType;
	}

	@JsonProperty("access_type")
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	@JsonProperty("itfs_requested")
	public String getItfsRequested() {
		return itfsRequested;
	}

	@JsonProperty("itfs_requested")
	public void setItfsRequested(String itfsRequested) {
		this.itfsRequested = itfsRequested;
	}

	@JsonProperty("lns_requested")
	public String getLnsRequested() {
		return lnsRequested;
	}

	@JsonProperty("lns_requested")
	public void setLnsRequested(String lnsRequested) {
		this.lnsRequested = lnsRequested;
	}

	@JsonProperty("uifn_requested")
	public String getUifnRequested() {
		return uifnRequested;
	}

	@JsonProperty("uifn_requested")
	public void setUifnRequested(String uifnRequested) {
		this.uifnRequested = uifnRequested;
	}

	@JsonProperty("acs_dtf_requested")
	public String getAcsDtfRequested() {
		return acsDtfRequested;
	}

	@JsonProperty("acs_dtf_requested")
	public void setAcsDtfRequested(String acsDtfRequested) {
		this.acsDtfRequested = acsDtfRequested;
	}

	@JsonProperty("acs_ans_requested")
	public String getAcsAnsRequested() {
		return acsAnsRequested;
	}

	@JsonProperty("acs_ans_requested")
	public void setAcsAnsRequested(String acsAnsRequested) {
		this.acsAnsRequested = acsAnsRequested;
	}

	@JsonProperty("acs_sns_requested")
	public String getAcsSnsRequested() {
		return acsSnsRequested;
	}

	@JsonProperty("acs_sns_requested")
	public void setAcsSnsRequested(String acsSnsRequested) {
		this.acsSnsRequested = acsSnsRequested;
	}

	@JsonProperty("dom_voice_requested")
	public String getDomesticVoiceRequested() {
		return domesticVoiceRequested;
	}

	@JsonProperty("dom_voice_requested")
	public void setDomesticVoiceRequested(String domesticVoiceRequested) {
		this.domesticVoiceRequested = domesticVoiceRequested;
	}

	@JsonProperty("glob_out_requested")
	public String getGlobalOutboundRequested() {
		return globalOutboundRequested;
	}

	@JsonProperty("glob_out_requested")
	public void setGlobalOutboundRequested(String globalOutboundRequested) {
		this.globalOutboundRequested = globalOutboundRequested;
	}

	@JsonProperty("origin_itfs")
	public List<String> getOriginItfs() {
		return originItfs;
	}

	@JsonProperty("origin_itfs")
	public void setOriginItfs(List<String> originItfs) {
		this.originItfs = originItfs;
	}

	@JsonProperty("destination_itfs")
	public List<String> getDestinationItfs() {
		return destinationItfs;
	}

	@JsonProperty("destination_itfs")
	public void setDestinationItfs(List<String> destinationItfs) {
		this.destinationItfs = destinationItfs;
	}

	@JsonProperty("no_requested_itfs")
	public List<String> getNoRequestedItfs() {
		return noRequestedItfs;
	}

	@JsonProperty("no_requested_itfs")
	public void setNoRequestedItfs(List<String> noRequestedItfs) {
		this.noRequestedItfs = noRequestedItfs;
	}

	@JsonProperty("no_ported_itfs")
	public List<String> getNoPortedItfs() {
		return noPortedItfs;
	}

	@JsonProperty("no_ported_itfs")
	public void setNoPortedItfs(List<String> noPortedItfs) {
		this.noPortedItfs = noPortedItfs;
	}

	@JsonProperty("origin_lns")
	public List<String> getOriginLns() {
		return originLns;
	}

	@JsonProperty("origin_lns")
	public void setOriginLns(List<String> originLns) {
		this.originLns = originLns;
	}

	@JsonProperty("destination_lns")
	public List<String> getDestinationLns() {
		return destinationLns;
	}

	@JsonProperty("destination_lns")
	public void setDestinationLns(List<String> destinationLns) {
		this.destinationLns = destinationLns;
	}

	@JsonProperty("no_requested_lns")
	public List<String> getNoRequestedLns() {
		return noRequestedLns;
	}

	@JsonProperty("no_requested_lns")
	public void setNoRequestedLns(List<String> noRequestedLns) {
		this.noRequestedLns = noRequestedLns;
	}

	@JsonProperty("no_ported_lns")
	public List<String> getNoPortedLns() {
		return noPortedLns;
	}

	@JsonProperty("no_ported_lns")
	public void setNoPortedLns(List<String> noPortedLns) {
		this.noPortedLns = noPortedLns;
	}

	@JsonProperty("origin_uifn")
	public List<String> getOriginUifn() {
		return originUifn;
	}

	@JsonProperty("origin_uifn")
	public void setOriginUifn(List<String> originUifn) {
		this.originUifn = originUifn;
	}

	@JsonProperty("destination_uifn")
	public List<String> getDestinationUifn() {
		return destinationUifn;
	}

	@JsonProperty("destination_uifn")
	public void setDestinationUifn(List<String> destinationUifn) {
		this.destinationUifn = destinationUifn;
	}

	@JsonProperty("no_requested_uifn")
	public List<String> getNoRequestedUifn() {
		return noRequestedUifn;
	}

	@JsonProperty("no_requested_uifn")
	public void setNoRequestedUifn(List<String> noRequestedUifn) {
		this.noRequestedUifn = noRequestedUifn;
	}

	@JsonProperty("no_ported_uifn")
	public List<String> getNoPortedUifn() {
		return noPortedUifn;
	}

	@JsonProperty("no_ported_uifn")
	public void setNoPortedUifn(List<String> noPortedUifn) {
		this.noPortedUifn = noPortedUifn;
	}

	@JsonProperty("origin_dtfs")
	public List<String> getOriginDtfs() {
		return originDtfs;
	}

	@JsonProperty("origin_dtfs")
	public void setOriginDtfs(List<String> originDtfs) {
		this.originDtfs = originDtfs;
	}

	@JsonProperty("destination_dtfs")
	public List<String> getDestinationDtfs() {
		return destinationDtfs;
	}

	@JsonProperty("destination_dtfs")
	public void setDestinationDtfs(List<String> destinationDtfs) {
		this.destinationDtfs = destinationDtfs;
	}

	@JsonProperty("no_requested_dtfs")
	public List<String> getNoRequestedDtfs() {
		return noRequestedDtfs;
	}

	@JsonProperty("no_requested_dtfs")
	public void setNoRequestedDtfs(List<String> noRequestedDtfs) {
		this.noRequestedDtfs = noRequestedDtfs;
	}

	@JsonProperty("origin_acns")
	public List<String> getOriginAcns() {
		return originAcns;
	}

	@JsonProperty("origin_acns")
	public void setOriginAcns(List<String> originAcns) {
		this.originAcns = originAcns;
	}

	@JsonProperty("destination_acns")
	public List<String> getDestinationAcns() {
		return destinationAcns;
	}

	@JsonProperty("destination_acns")
	public void setDestinationAcns(List<String> destinationAcns) {
		this.destinationAcns = destinationAcns;
	}

	@JsonProperty("no_requested_acns")
	public List<String> getNoRequestedAcns() {
		return noRequestedAcns;
	}

	@JsonProperty("no_requested_acns")
	public void setNoRequestedAcns(List<String> noRequestedAcns) {
		this.noRequestedAcns = noRequestedAcns;
	}

	@JsonProperty("no_requested_acsns")
	public List<String> getNoRequestedAcsns() {
		return noRequestedAcsns;
	}

	@JsonProperty("no_requested_acsns")
	public void setNoRequestedAcsns(List<String> noRequestedAcsns) {
		this.noRequestedAcsns = noRequestedAcsns;
	}

	@JsonProperty("origin_dom_voice")
	public List<String> getOriginDomesticVoice() {
		return originDomesticVoice;
	}

	@JsonProperty("origin_dom_voice")
	public void setOriginDomesticVoice(List<String> originDomesticVoice) {
		this.originDomesticVoice = originDomesticVoice;
	}

	@JsonProperty("origin_acsns")
	public List<String> getOriginAcsns() {
		return originAcsns;
	}

	@JsonProperty("origin_acsns")
	public void setOriginAcsns(List<String> originAcsns) {
		this.originAcsns = originAcsns;
	}

	@JsonProperty("destination_acsns")
	public List<String> getDestinationAcsns() {
		return destinationAcsns;
	}

	@JsonProperty("destination_acsns")
	public void setDestinationAcsns(List<String> destinationAcsns) {
		this.destinationAcsns = destinationAcsns;
	}

	@JsonProperty("no_requested_dom_voice")
	public List<String> getNoRequestedDomesticVoice() {
		return noRequestedDomesticVoice;
	}

	@JsonProperty("no_requested_dom_voice")
	public void setNoRequestedDomesticVoice(List<String> noRequestedDomesticVoice) {
		this.noRequestedDomesticVoice = noRequestedDomesticVoice;
	}

	@JsonProperty("no_ported_dom_voice")
	public List<String> getNoPortedDomesticVoice() {
		return noPortedDomesticVoice;
	}

	@JsonProperty("no_ported_dom_voice")
	public void setNoPortedDomesticVoice(List<String> noPortedDomesticVoice) {
		this.noPortedDomesticVoice = noPortedDomesticVoice;
	}

	@JsonProperty("destination_glob_out")
	public List<String> getDestinationGlobalOutbound() {
		return destinationGlobalOutbound;
	}

	@JsonProperty("destination_glob_out")
	public void setDestinationGlobalOutbound(List<String> destinationGlobalOutbound) {
		this.destinationGlobalOutbound = destinationGlobalOutbound;
	}

	@JsonProperty("channels_dom_voice")
	public List<String> getChannelsDomesticVoice() {
		return channelsDomesticVoice;
	}

	@JsonProperty("channels_dom_voice")
	public void setChannelsDomesticVoice(List<String> channelsDomesticVoice) {
		this.channelsDomesticVoice = channelsDomesticVoice;
	}

	@JsonProperty("quotetype_partner")
	public String getQuoteTypePartner() {
		return quoteTypePartner;
	}

	@JsonProperty("quotetype_partner")
	public void setQuoteTypePartner(String quoteTypePartner) {
		this.quoteTypePartner = quoteTypePartner;
	}

	@JsonProperty("go_catalog_flag")
	public String getGlobalOutboundCatalogFlag() {
		return globalOutboundCatalogFlag;
	}

	@JsonProperty("go_catalog_flag")
	public void setGlobalOutboundCatalogFlag(String globalOutboundCatalogFlag) {
		this.globalOutboundCatalogFlag = globalOutboundCatalogFlag;
	}

	@JsonProperty("go_catalog_curr")
	public String getGlobalOutboundCatalogCurrency() {
		return globalOutboundCatalogCurrency;
	}

	@JsonProperty("go_catalog_curr")
	public void setGlobalOutboundCatalogCurrency(String globalOutboundCatalogCurrency) {
		this.globalOutboundCatalogCurrency = globalOutboundCatalogCurrency;
	}

	public Byte getIsUcaas() {
		return isUcaas;
	}

	public void setIsUcaas(Byte isUcaas) {
		this.isUcaas = isUcaas;
	}

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	@Override
	public String toString() {
		return "PricingRequest{" + "accountIdWith18Digit='" + accountIdWith18Digit + '\'' + ", cuLeId='" + cuLeId + '\''
				+ ", customerSegment='" + customerSegment + '\'' + ", accountRTMCust='" + accountRTMCust + '\''
				+ ", salesOrg='" + salesOrg + '\'' + ", volumeProjected='" + volumeProjected + '\''
				+ ", inboundVolume='" + inboundVolume + '\'' + ", outboundVolume='" + outboundVolume + '\''
				+ ", inboundSelectCust=" + inboundSelectCust + ", outboundSelectCust=" + outboundSelectCust
				+ ", quoteTypeQuote='" + quoteTypeQuote + '\'' + ", macdNewCountry='" + macdNewCountry + '\''
				+ ", orgId='" + orgId + '\'' + ", opportunityTerm='" + opportunityTerm + '\'' + ", accessType='"
				+ accessType + '\'' + ", itfsRequested='" + itfsRequested + '\'' + ", lnsRequested='" + lnsRequested
				+ '\'' + ", uifnRequested='" + uifnRequested + '\'' + ", acsDtfRequested='" + acsDtfRequested + '\''
				+ ", acsAnsRequested='" + acsAnsRequested + '\'' + ", domesticVoiceRequested='" + domesticVoiceRequested
				+ '\'' + ", globalOutboundRequested='" + globalOutboundRequested + '\'' + ", originItfs=" + originItfs
				+ ", destinationItfs=" + destinationItfs + ", noRequestedItfs=" + noRequestedItfs + ", noPortedItfs="
				+ noPortedItfs + ", originLns=" + originLns + ", destinationLns=" + destinationLns + ", noRequestedLns="
				+ noRequestedLns + ", noPortedLns=" + noPortedLns + ", originUifn=" + originUifn + ", destinationUifn="
				+ destinationUifn + ", noRequestedUifn=" + noRequestedUifn + ", noPortedUifn=" + noPortedUifn
				+ ", originDtfs=" + originDtfs + ", destinationDtfs=" + destinationDtfs + ", noRequestedDtfs="
				+ noRequestedDtfs + ", originAcns=" + originAcns + ", destinationAcns=" + destinationAcns
				+ ", noRequestedAcns=" + noRequestedAcns + ", originDomesticVoice=" + originDomesticVoice
				+ ", noRequestedDomesticVoice=" + noRequestedDomesticVoice + ", noPortedDomesticVoice="
				+ noPortedDomesticVoice + ", destinationGlobalOutbound=" + destinationGlobalOutbound
				+ ", channelsDomesticVoice=" + channelsDomesticVoice + ", quoteTypePartner='" + quoteTypePartner + '\''
				+ ", globalOutboundCatalogFlag='" + globalOutboundCatalogFlag + '\''
				+ ", globalOutboundCatalogCurrency='" + globalOutboundCatalogCurrency + '\'' + '}';
	}
}
