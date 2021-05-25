package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This is the entity class for the Commercial report view Table
 * 
 *
 * @author Kavya Singh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_order_vs_pe_rcmd_site_cmp" )
@NamedQuery(name="VwCommercialReport.findAll", query="SELECT v FROM VwCommercialReport v")
public class VwCommercialReport implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "site_code")
	private String siteCode;
	
	@Column(name = "order_version")
	private String orderVersion;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "tps_sfdc_copf_id")
	private String tpsSfdcCopfId;
	
	private String stage;
	
	@Column(name = "actual_mrc")
	private String actualMrc;
	
	@Column(name = "actual_nrc")
	private String actualNrc;
	
	@Column(name = "actual_arc")
	private String actualArc;
	
	@Column(name = "actual_tcv")
	private String actualTcv;    //10
	
	@Column(name = "fp_status")
	private String fpStatus;
	
	@Column(name = "price_mode")
	private String priceMode;
	
	@Column(name = "pricing_type")
	private String pricingType;
	
	@Column(name = "actual_Internet_port_mrc")
	private String actualInternetportMrc;
	
	@Column(name = "Optimus_Port_MRC")
	private String optimusPortMrc;
	
	@Column(name = "actual_Internet_port_nrc")
	private String actualInternetportNrc;
	
	@Column(name = "Optimus_Port_NRC")
	private String optimusPortNrc;
	
	@Column(name = "actual_Internet_port_arc")
	private String actualInternetportArc;
	
	@Column(name = "Optimus_Port_ARC")
	private String optimusPortARC;
	
	@Column(name = "Predicted_discount")
	private String predictedDiscount;

	@Column(name = "actual_Last_mile_mrc")
	private String actualLastMileMrc;       //11
	
	@Column(name = "Optimus_Last_Mile_Cost_MRC")
	private String optimusLastMileCostMRC;
	
	@Column(name = "actual_Last_mile_nrc")
	private String actualLastMileNrc;
	
	@Column(name = "Optimus_Last_Mile_Cost_NRC")
	private String optimusLastMileCostNRC;
	
	@Column(name = "actual_Last_mile_arc")
	private String actualLastMileArc;
	
	@Column(name = "Optimus_Last_Mile_Cost_ARC")
	private String optimusLastMileCostARC;
	
	@Column(name = "actual_cpe_mrc")
	private String actualCpeMrc;
	
	@Column(name = "Optimus_CPE_MRC")
	private String optimusCpeMrc;
	
	@Column(name = "actual_cpe_nrc")
	private String actualCpeNrc;
	
	@Column(name = "Optimus_CPE_NRC")
	private String optimusCpeNrc;
	
	@Column(name = "actual_cpe_arc")
	private String actualCpeArc;
	
	@Column(name = "Optimus_CPE_ARC")
	private String optimusCpeArc;
	
	@Column(name = "actual_Additional_ip_mrc")
	private String actualAdditionalIpMrc;       //12
	
	@Column(name = "Optimus_additional_IP_MRC")
	private String optimusAdditionalIpMrc;
	
	@Column(name = "actual_Additional_ip_arc")
	private String actualAdditionalIpArc;
	
	@Column(name = "Optimus_additional_IP_ARC")
	private String optimusAdditionalIpArc;
	
	@Column(name = "actual_Additional_ip_nrc")
	private String actualAdditionalIpNrc;
	
//	@Column(name = "Optimus_additional_IP_NRC")
//	private String OptimusPortArc;
	
	@Column(name = "Site_Latitude")
	private String siteLatitude;
	
	@Column(name = "Site_Longitude")
	private String siteLongitude;
	
	@Column(name = "prospect_name")
	private String prospectName;
	
	@Column(name = "Rate_Card")
	private String rateCard;
	
	@Column(name = "bw_mbps")
	private String bwMbps;
	
	@Column(name = "burstable_bw")
	private String burstableBw;  //32
	
	private String city;
	
	@Column(name = "customer_segment")
	private String customerSegment;
	
	@Column(name = "sales_org")
	private String salesOrg;
	
	@Column(name = "product_name_fsbility")
	private String productNameFsbility;
	
	@Column(name = "feasibility_created_date")
	private String feasibilityCreatedDate;
	
	@Column(name = "local_loop_interface")
	private String localLoopInterface;
	
	@Column(name = "contract_term")
	private String contractTerm;
	
	@Column(name = "quotetype_quote")
	private String quotetypeQuote;
	
	@Column(name = "connection_type")
	private String connectionType;
	
	@Column(name = "sum_no_of_sites_uni_len")
	private String sumNoOfSitesUniLen;   //10
	
	@Column(name = "cpe_variant")
	private String cpeVariant;
	
	@Column(name = "cpe_management_type")
	private String cpeManagementType;
	
	@Column(name = "cpe_supply_type")
	private String cpeSupplyType;
	
	private String topology;
	
	@Column(name = "sum_onnet_flag")
	private String sumOnnetFlag;
	
	@Column(name = "sum_offnet_flag")
	private String sumOffnetFlag;
	
	@Column(name = "lm_arc_bw_onwl")
	private String lmArcBwOnwl;
	
	@Column(name = "lm_nrc_bw_onwl")
	private String lmNrcBwOnwl;
	
	@Column(name = "lm_nrc_mux_onwl")
	private String lmNrcMuxOnwl;
	
	@Column(name = "lm_nrc_inbldg_onwl")
	private String lmNrcInbldgOnwl;
	
	@Column(name = "lm_nrc_ospcapex_onwl")
	private String lmNrcOspcapexOnwl;  //11
	
	@Column(name = "lm_nrc_nerental_onwl")
	private String lmNrcNerentalOnwl;
	
	@Column(name = "lm_arc_bw_prov_ofrf")
	private String lmArcBwProvOfrf;
	
	@Column(name = "lm_nrc_bw_prov_ofrf")
	private String lmNrcBwProvOfrf;
	
	@Column(name = "lm_nrc_mast_ofrf")
	private String lmNrcBwMastOfrf;
	
	@Column(name = "lm_arc_bw_onrf")
	private String lmArcBwOnrf;
	
	@Column(name = "lm_nrc_bw_onrf")
	private String lmNrcBwOnrf;
	
	@Column(name = "lm_nrc_mast_onrf")
	private String lmNrcBwMastOnrf;
	
	@Column(name = "Orch_Connection")
	private String orchConnection;
	
	@Column(name = "Orch_LM_Type")
	private String orchLmType;
	
	@Column(name = "additional_ip_flag")
	private String additionalIpFlag;  //10
	
	@Column(name = "ip_address_arrangement")
	private String ipAddressArrangement;
	
	@Column(name = "ipv4_address_pool_size")
	private String ipv4AddressPoolSize;
	
	@Column(name = "ipv6_address_pool_size")
	private String ipv6AddressPoolSize;
	
	@Column(name = "Account_RTM_Cust")
	private String accountRtmCust;
	
	@Id
	@Column(name = "feasibility_code")
	private String feasibilityCode;
	
	@Column(name = "feasibility_mode")
	private String feasibilityMode;
	
	@Column(name = "feasibility_check")
	private String feasibilityCheck;    //7
	
	@Column(name = "backup_port_requested")
	private String backupPortRequested;
	
	@Column(name = "No_of_sites_in_opportunity")
	private String noOfSitesInOpportunity;
	
	@Column(name = "feasibility_status")
	private String feasibilityStatus;
	
	@Column(name = "Bucket_Adjustment_Type")
	private String bucketAdjustmentType;
	
	@Column(name = "CPE_Discount")
	private String cpeDiscount;

	@Column(name = "is_selected")
	private String isSelected;
	
	private String provider;  //7

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getOrderVersion() {
		return orderVersion;
	}

	public void setOrderVersion(String orderVersion) {
		this.orderVersion = orderVersion;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTpsSfdcCopfId() {
		return tpsSfdcCopfId;
	}

	public void setTpsSfdcCopfId(String tpsSfdcCopfId) {
		this.tpsSfdcCopfId = tpsSfdcCopfId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getActualMrc() {
		return actualMrc;
	}

	public void setActualMrc(String actualMrc) {
		this.actualMrc = actualMrc;
	}

	public String getActualNrc() {
		return actualNrc;
	}

	public void setActualNrc(String actualNrc) {
		this.actualNrc = actualNrc;
	}

	public String getActualArc() {
		return actualArc;
	}

	public void setActualArc(String actualArc) {
		this.actualArc = actualArc;
	}

	public String getActualTcv() {
		return actualTcv;
	}

	public void setActualTcv(String actualTcv) {
		this.actualTcv = actualTcv;
	}

	public String getFpStatus() {
		return fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public String getPriceMode() {
		return priceMode;
	}

	public void setPriceMode(String priceMode) {
		this.priceMode = priceMode;
	}

	public String getPricingType() {
		return pricingType;
	}

	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}

	public String getActualInternetportMrc() {
		return actualInternetportMrc;
	}

	public void setActualInternetportMrc(String actualInternetportMrc) {
		this.actualInternetportMrc = actualInternetportMrc;
	}

	public String getOptimusPortMrc() {
		return optimusPortMrc;
	}

	public void setOptimusPortMrc(String optimusPortMrc) {
		this.optimusPortMrc = optimusPortMrc;
	}

	public String getActualInternetportNrc() {
		return actualInternetportNrc;
	}

	public void setActualInternetportNrc(String actualInternetportNrc) {
		this.actualInternetportNrc = actualInternetportNrc;
	}

	public String getOptimusPortNrc() {
		return optimusPortNrc;
	}

	public void setOptimusPortNrc(String optimusPortNrc) {
		this.optimusPortNrc = optimusPortNrc;
	}

	public String getActualInternetportArc() {
		return actualInternetportArc;
	}

	public void setActualInternetportArc(String actualInternetportArc) {
		this.actualInternetportArc = actualInternetportArc;
	}

	public String getOptimusPortARC() {
		return optimusPortARC;
	}

	public void setOptimusPortARC(String optimusPortARC) {
		this.optimusPortARC = optimusPortARC;
	}

	public String getPredictedDiscount() {
		return predictedDiscount;
	}

	public void setPredictedDiscount(String predictedDiscount) {
		this.predictedDiscount = predictedDiscount;
	}

	public String getActualLastMileMrc() {
		return actualLastMileMrc;
	}

	public void setActualLastMileMrc(String actuaLastMileMrc) {
		this.actualLastMileMrc = actuaLastMileMrc;
	}

	public String getOptimusLastMileCostMRC() {
		return optimusLastMileCostMRC;
	}

	public void setOptimusLastMileCostMRC(String optimusLastMileCostMRC) {
		this.optimusLastMileCostMRC = optimusLastMileCostMRC;
	}

	public String getActualLastMileNrc() {
		return actualLastMileNrc;
	}

	public void setActualLastMileNrc(String actualLastMileNrc) {
		this.actualLastMileNrc = actualLastMileNrc;
	}

	public String getOptimusLastMileCostNRC() {
		return optimusLastMileCostNRC;
	}

	public void setOptimusLastMileCostNRC(String optimusLastMileCostNRC) {
		this.optimusLastMileCostNRC = optimusLastMileCostNRC;
	}

	public String getActualLastMileArc() {
		return actualLastMileArc;
	}

	public void setActualLastMileArc(String actualLastMileArc) {
		this.actualLastMileArc = actualLastMileArc;
	}

	public String getOptimusLastMileCostARC() {
		return optimusLastMileCostARC;
	}

	public void setOptimusLastMileCostARC(String optimusLastMileCostARC) {
		this.optimusLastMileCostARC = optimusLastMileCostARC;
	}

	public String getActualCpeMrc() {
		return actualCpeMrc;
	}

	public void setActualCpeMrc(String actualCpeMrc) {
		this.actualCpeMrc = actualCpeMrc;
	}

	public String getOptimusCpeMrc() {
		return optimusCpeMrc;
	}

	public void setOptimusCpeMrc(String optimusCpeMrc) {
		this.optimusCpeMrc = optimusCpeMrc;
	}

	public String getActualCpeNrc() {
		return actualCpeNrc;
	}

	public void setActualCpeNrc(String actualCpeNrc) {
		this.actualCpeNrc = actualCpeNrc;
	}

	public String getOptimusCpeNrc() {
		return optimusCpeNrc;
	}

	public void setOptimusCpeNrc(String optimusCpeNrc) {
		this.optimusCpeNrc = optimusCpeNrc;
	}

	public String getActualCpeArc() {
		return actualCpeArc;
	}

	public void setActualCpeArc(String actualCpeArc) {
		this.actualCpeArc = actualCpeArc;
	}

	public String getOptimusCpeArc() {
		return optimusCpeArc;
	}

	public void setOptimusCpeArc(String optimusCpeArc) {
		this.optimusCpeArc = optimusCpeArc;
	}

	public String getActualAdditionalipMrc() {
		return actualAdditionalIpMrc;
	}

	public void setActualAdditionalipMrc(String actualAdditionalipMrc) {
		this.actualAdditionalIpMrc = actualAdditionalipMrc;
	}

	public String getOptimusAdditionalIpMrc() {
		return optimusAdditionalIpMrc;
	}

	public void setOptimusAdditionalIpMrc(String optimusAdditionalIpMrc) {
		this.optimusAdditionalIpMrc = optimusAdditionalIpMrc;
	}

	public String getActualAdditionalIpArc() {
		return actualAdditionalIpArc;
	}

	public void setActualAdditionalIpArc(String actualAdditionalIpArc) {
		this.actualAdditionalIpArc = actualAdditionalIpArc;
	}

	public String getOptimusAdditionalIpArc() {
		return optimusAdditionalIpArc;
	}

	public void setOptimusAdditionalIpArc(String optimusAdditionalIpArc) {
		this.optimusAdditionalIpArc = optimusAdditionalIpArc;
	}

	public String getActualAdditionalIpNrc() {
		return actualAdditionalIpNrc;
	}

	public void setActualAdditionalIpNrc(String actualAdditionalIpNrc) {
		this.actualAdditionalIpNrc = actualAdditionalIpNrc;
	}

	public String getSiteLatitude() {
		return siteLatitude;
	}

	public void setSiteLatitude(String siteLatitude) {
		this.siteLatitude = siteLatitude;
	}

	public String getSiteLongitude() {
		return siteLongitude;
	}

	public void setSiteLongitude(String siteLongitude) {
		this.siteLongitude = siteLongitude;
	}

	public String getProspectName() {
		return prospectName;
	}

	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}

	public String getRateCard() {
		return rateCard;
	}

	public void setRateCard(String rateCard) {
		this.rateCard = rateCard;
	}
	
	public String getBwMbps() {
		return bwMbps;
	}

	public void setBwMbps(String bwMbps) {
		this.bwMbps = bwMbps;
	}

	public String getBurstableBw() {
		return burstableBw;
	}

	public void setBurstable_bw(String burstableBw) {
		this.burstableBw = burstableBw;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getProductNameFsbility() {
		return productNameFsbility;
	}

	public void setProductNameFsbility(String productNameFsbility) {
		this.productNameFsbility = productNameFsbility;
	}

	public String getFeasibilityCreatedDate() {
		return feasibilityCreatedDate;
	}

	public void setFeasibilityCreatedDate(String feasibilityCreatedDate) {
		this.feasibilityCreatedDate = feasibilityCreatedDate;
	}

	public String getLocalLoopInterface() {
		return localLoopInterface;
	}

	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}

	public String getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getQuotetypeQuote() {
		return quotetypeQuote;
	}

	public void setQuotetypeQuote(String quotetypeQuote) {
		this.quotetypeQuote = quotetypeQuote;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getSumNoOfSitesUniLen() {
		return sumNoOfSitesUniLen;
	}

	public void setSumNoOfSitesUniLen(String sumNoOfSitesUniLen) {
		this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
	}

	public String getCpeVariant() {
		return cpeVariant;
	}

	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}

	public String getCpeManagementType() {
		return cpeManagementType;
	}

	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}

	public String getCpeSupplyType() {
		return cpeSupplyType;
	}

	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}

	public String getTopology() {
		return topology;
	}

	public void setTopology(String topology) {
		this.topology = topology;
	}

	public String getSumOnnetFlag() {
		return sumOnnetFlag;
	}

	public void setSumOnnetFlag(String sumOnnetFlag) {
		this.sumOnnetFlag = sumOnnetFlag;
	}

	public String getSumOffnetFlag() {
		return sumOffnetFlag;
	}

	public void setSumOffnetFlag(String sumOffnetFlag) {
		this.sumOffnetFlag = sumOffnetFlag;
	}

	public String getLmArcBwOnwl() {
		return lmArcBwOnwl;
	}

	public void setLmArcBwOnwl(String lmArcBwOnwl) {
		this.lmArcBwOnwl = lmArcBwOnwl;
	}

	public String getLmNrcBwOnwl() {
		return lmNrcBwOnwl;
	}

	public void setLmNrcBwOnwl(String lmNrcBwOnwl) {
		this.lmNrcBwOnwl = lmNrcBwOnwl;
	}

	public String getLmNrcMuxOnwl() {
		return lmNrcMuxOnwl;
	}

	public void setLmNrcMuxOnwl(String lmNrcMuxOnwl) {
		this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	}

	public String getLmNrcInbldgOnwl() {
		return lmNrcInbldgOnwl;
	}

	public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
		this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	}

	public String getLmNrcOspcapexOnwl() {
		return lmNrcOspcapexOnwl;
	}

	public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
		this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	}

	public String getLmNrcNerentalOnwl() {
		return lmNrcNerentalOnwl;
	}

	public void setLmNrcNerentalOnwl(String lmNrcNerentalOnwl) {
		this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	}

	public String getLmArcBwProvOfrf() {
		return lmArcBwProvOfrf;
	}

	public void setLmArcBwProvOfrf(String lmArcBwProvOfrf) {
		this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	}

	public String getLmNrcBwProvOfrf() {
		return lmNrcBwProvOfrf;
	}

	public void setLmNrcBwProvOfrf(String lmNrcBwProvOfrf) {
		this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	}

	public String getLmNrcBwMastOfrf() {
		return lmNrcBwMastOfrf;
	}

	public void setLmNrcBwMastOfrf(String lmNrcBwMastOfrf) {
		this.lmNrcBwMastOfrf = lmNrcBwMastOfrf;
	}

	public String getLmArcBwOnrf() {
		return lmArcBwOnrf;
	}

	public void setLmArcBwOnrf(String lmArcBwOnrf) {
		this.lmArcBwOnrf = lmArcBwOnrf;
	}

	public String getLmNrcBwOnrf() {
		return lmNrcBwOnrf;
	}

	public void setLmNrcBwOnrf(String lmNrcBwOnrf) {
		this.lmNrcBwOnrf = lmNrcBwOnrf;
	}

	public String getLmNrcBwMastOnrf() {
		return lmNrcBwMastOnrf;
	}

	public void setLmNrcBwMastOnrf(String lmNrcBwMastOnrf) {
		this.lmNrcBwMastOnrf = lmNrcBwMastOnrf;
	}

	public String getOrchConnection() {
		return orchConnection;
	}

	public void setOrchConnection(String orchConnection) {
		this.orchConnection = orchConnection;
	}

	public String getOrchLmType() {
		return orchLmType;
	}

	public void setOrchLmType(String orchLmType) {
		this.orchLmType = orchLmType;
	}

	public String getAdditionalIpFlag() {
		return additionalIpFlag;
	}

	public void setAdditionalIpFlag(String additionalIpFlag) {
		this.additionalIpFlag = additionalIpFlag;
	}

	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	public String getAccountRtmCust() {
		return accountRtmCust;
	}

	public void setAccountRtmCust(String accountRtmCust) {
		this.accountRtmCust = accountRtmCust;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getBackupPortRequested() {
		return backupPortRequested;
	}

	public void setBackupPortRequested(String backupPortRequested) {
		this.backupPortRequested = backupPortRequested;
	}

	public String getNoOfSitesInOpportunity() {
		return noOfSitesInOpportunity;
	}

	public void setNoOfSitesInOpportunity(String noOfSitesInOpportunity) {
		this.noOfSitesInOpportunity = noOfSitesInOpportunity;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getBucketAdjustmentType() {
		return bucketAdjustmentType;
	}

	public void setBucketAdjustmentType(String bucketAdjustmentType) {
		this.bucketAdjustmentType = bucketAdjustmentType;
	}

	public String getCpeDiscount() {
		return cpeDiscount;
	}

	public void setCpeDiscount(String cpeDiscount) {
		this.cpeDiscount = cpeDiscount;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}
