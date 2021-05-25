
package com.tcl.dias.oms.gvpn.pricing.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "all_sites_count",
    "all_sites_long",
    "all_sites_short",
    "allowedpop",
    "allowedprod",
    "buildingaddress",
    "bw",
    "bw_band",
    "city",
    "cluster_id",
    "contract_term_with_vendor_months",
    "country",
    "country_id",
    "coverage",
    "coverage_area_name",
    "coverage_type",
    "db_code",
    "dist_mtrs",
    "distance_to_pop",
    "eas",
    "enddate",
    "global_dedicated_ethernet",
    "global_vpn",
    "index",
    "inter_connection_type",
    "ip_transit",
    "ipl",
    "izo_internet_wan",
    "lat",
    "latdiff_deg",
    "local_loop_capacity",
    "local_loop_comment",
    "local_loop_interface",
    "long",
    "longdiff_deg",
    "low_latency_gde",
    "mrc_bw",
    "mrc_bw_currency",
    "otc_nrc_installation",
    "owner_region",
    "pop_coord",
    "priority",
    "priority_ethernet",
    "provider_name",
    "provider_product_name",
    "provider_product_name_subset",
    "quote_category_short",
    "source",
    "startdate",
    "status",
    "tcl_pop_address",
    "tcl_pop_latitude",
    "tcl_pop_longitude",
    "telepresence",
    "termdiscountmrc24months",
    "termdiscountmrc36months",
    "termdiscountnrc24months",
    "termdiscountnrc36months",
    "updatedby",
    "updatedon",
    "video_connect",
    "vpls",
    "access_type",
    "address_1",
    "address_2",
    "address_a_end",
    "address_b_end",
    "address_from_tool_1",
    "apt_suite_info",
    "building_name",
    "building_number",
    "bw_log",
    "cable_system_name",
    "city_2",
    "city_a_end",
    "city_b_end",
    "country_a_end",
    "country_b_end",
    "created_by_full_name",
    "co_ordinates_a_end_latitude",
    "co_ordinates_a_end_longitude",
    "co_ordinates_b_end_latitude",
    "co_ordinates_b_end_longitude",
    "customer_latitude1",
    "customer_longitude1",
    "dol_per_mb",
    "feasibility_request_request_no",
    "feasibility_response_auto_no",
    "feasibility_response_id",
    "feasibility_task_auto_no",
     "feasibility_type",
     "floor",
 	"isactive",
 	"landmark_optional",
 	"last_mile_contract_term",
 	"last_modified_by_full_name",
 	"latitude",
 	"locality",
 	"longitude",
 	"mtu_frame_size_bytes",
 	"opened_on",
 	"opportunity_account_name",
 	"opportunity_id",
 	"opportunity_stage",
 	"order_type",
 	"orderable_mrc_bw",
 	"orderable_mrc_bw_currency",
 	"orderable_otc_nrc_install_curr",
 	"orderable_otc_nrc_installation",
 	"orderable_otc_nrc_installation_currency",
 	"orderable_xconnect_mrc",
 	"orderable_xconnect_mrc_currency",
 	"orderable_xconnect_nrc",
 	"orderable_xconnect_nrc_currency",
 	"otc_nrc_installation_currency",
 	"other_provider_name",
 	"pin_zip_a_end",
 	"pin_zip_b_end",
 	"pincode",
 	"product_name",
	"product_subtype",
	"provider_product_name_other",
	"quote_age",
	"quote_category_long",
	"rec_id",
	"record_type",
	"region",
	"remarks",
	"response_cloned_from",
	"response_related_to",
	"response_type",
	"returned_diversity",
	"returned_underlying_technology",
	"room",
	"sales_selected_response",
	"task_acknowledged_by",
	"task_close_date",
	"tcl_pop_short_code",
	"tower",
	"type_2_provider",
	"update_date",
	"vendor_id",
	"vendor_id_sfdc",
	"vendor_name",
	"vendor_name_vendor_id1",
	"vlan_transparent",
	"wing",
	"xconnect_mrc",
	"xconnect_mrc_currency",
	"xconnect_nrc",
	"xconnect_nrc_currency",
	"xconnect_provider_name",
	"xconnect_provider_name_vendor",
	"xconnect_provider_name_vendor_id",
	"xconnect_provider_name_vendor_id1",
	"xconnect_provider_name_vendor_name",
	"xconnect_vendor_id_sfdc",
	"access_type",
	"country_id_a_end",
	"country_id_b_end",
	"cq_ee_id",
	"selected_quote"
 	

})
public class RelatedQuote {

    @JsonProperty("all_sites_count")
    private String allSitesCount;
    @JsonProperty("all_sites_long")
    private String allSitesLong;
    @JsonProperty("all_sites_short")
    private String allSitesShort;
    @JsonProperty("allowedpop")
    private String allowedpop;
    @JsonProperty("allowedprod")
    private String allowedprod;
    @JsonProperty("buildingaddress")
    private String buildingaddress;
    @JsonProperty("bw")
    private Double bw;
    @JsonProperty("bw_band")
    private String bwBand;
    @JsonProperty("city")
    private String city;
    @JsonProperty("cluster_id")
    private String clusterId;
    @JsonProperty("contract_term_with_vendor_months")
    private Double contractTermWithVendorMonths;
    @JsonProperty("country")
    private String country;
    @JsonProperty("country_id")
    private String countryId;
    @JsonProperty("coverage")
    private String coverage;
    @JsonProperty("coverage_area_name")
    private String coverageAreaName;
    @JsonProperty("coverage_type")
    private String coverageType;
    @JsonProperty("db_code")
    private String dbCode;
    @JsonProperty("dist_mtrs")
    private Double distMtrs;
    @JsonProperty("distance_to_pop")
    private Double distanceToPop;
    @JsonProperty("eas")
    private String eas;
    @JsonProperty("enddate")
    private String enddate;
    @JsonProperty("global_dedicated_ethernet")
    private String globalDedicatedEthernet;
    @JsonProperty("global_vpn")
    private String globalVpn;
    @JsonProperty("index")
    private String index;
    @JsonProperty("inter_connection_type")
    private String interConnectionType;
    @JsonProperty("ip_transit")
    private String ipTransit;
    @JsonProperty("ipl")
    private String ipl;
    @JsonProperty("izo_internet_wan")
    private String izoInternetWan;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("latdiff_deg")
    private Double latdiffDeg;
    @JsonProperty("local_loop_capacity")
    private String localLoopCapacity;
    @JsonProperty("local_loop_comment")
    private String localLoopComment;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("long")
    private Double _long;
    @JsonProperty("longdiff_deg")
    private Double longdiffDeg;
    @JsonProperty("low_latency_gde")
    private String lowLatencyGde;
    @JsonProperty("mrc_bw")
    private Double mrcBw;
    @JsonProperty("mrc_bw_currency")
    private String mrcBwCurrency;
    @JsonProperty("otc_nrc_installation")
    private String otcNrcInstallation;
    @JsonProperty("owner_region")
    private String ownerRegion;
    @JsonProperty("pop_coord")
    private List<Double> popCoord = null;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("priority_ethernet")
    private String priorityEthernet;
    @JsonProperty("provider_name")
    private String providerName;
    @JsonProperty("provider_product_name")
    private String providerProductName;
    @JsonProperty("provider_product_name_subset")
    private String providerProductNameSubset;
    @JsonProperty("quote_category_short")
    private String quoteCategoryShort;
    @JsonProperty("source")
    private String source;
    @JsonProperty("startdate")
    private String startdate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("tcl_pop_address")
    private String tclPopAddress;
    @JsonProperty("tcl_pop_latitude")
    private Double tclPopLatitude;
    @JsonProperty("tcl_pop_longitude")
    private Double tclPopLongitude;
    @JsonProperty("telepresence")
    private String telepresence;
    @JsonProperty("termdiscountmrc24months")
    private Double termdiscountmrc24months;
    @JsonProperty("termdiscountmrc36months")
    private Double termdiscountmrc36months;
    @JsonProperty("termdiscountnrc24months")
    private Double termdiscountnrc24months;
    @JsonProperty("termdiscountnrc36months")
    private Double termdiscountnrc36months;
    @JsonProperty("updatedby")
    private String updatedby;
    @JsonProperty("updatedon")
    private String updatedon;
    @JsonProperty("video_connect")
    private String videoConnect;
    @JsonProperty("vpls")
    private String vpls;
    @JsonProperty("selected_quote")
    private String selectedQuote;
    @JsonIgnore
    private Map<String, String> additionalProperties = new HashMap<String, String>();
    
    @JsonProperty("address_1")
    private String address1;
    @JsonProperty("address_2")
    private String address2;
    @JsonProperty("address_a_end")
    private String addressAEnd;
    @JsonProperty("address_b_end")
    private String addressBEnd;
    @JsonProperty("address_from_tool_1")
    private String addressFromTool1;
    @JsonProperty("apt_suite_info")
    private String aptSuiteInfo;
    @JsonProperty("assigned_to_full_name")
    private String assignedToFullName;
    @JsonProperty("building_name")
    private String buildingName;
    @JsonProperty("building_number")
    private String buildingNumber;
    @JsonProperty("bw_log")
    private Double bwLog;
    @JsonProperty("cable_system_name")
    private String cableSystemName;
    @JsonProperty("city_2")
    private String city2;
    @JsonProperty("city_a_end")
    private String cityAEnd;
    @JsonProperty("city_b_end")
    private String cityBEnd;
    @JsonProperty("country_a_end")
    private String countryAEnd;
    @JsonProperty("country_b_end")
    private String countryBEnd;
    @JsonProperty("created_by_full_name")
    private String createdByFullName;
    @JsonProperty("co_ordinates_a_end_latitude")
    private String coOrdinatesAEndLatitude;
    @JsonProperty("co_ordinates_a_end_longitude")
    private String coOrdinatesAEndLongitude;
    @JsonProperty("co_ordinates_b_end_latitude")
    private String coOrdinatesBEndLatitude;
    @JsonProperty("co_ordinates_b_end_longitude")
    private String coOrdinatesBEndLongitude;
    @JsonProperty("customer_latitude1")
    private String customerLatitude1;
    @JsonProperty("customer_longitude1")
    private String customerLongitude1;
    @JsonProperty("dol_per_mb")
    private Double dolPerMb;
    @JsonProperty("feasibility_request_request_no")
    private Double feasibilityRequestRequestNo;
    @JsonProperty("feasibility_response_auto_no")
    private String feasibilityResponseAutoNo;
    @JsonProperty("feasibility_response_id")
    private String feasibilityResponseId;
    @JsonProperty("feasibility_task_auto_no")
    private String feasibilityTaskAutoNo;
    @JsonProperty("feasibility_type")
    private String feasibilityType;
   
    @JsonProperty("floor")
	private String floor;
	@JsonProperty("isactive")
	private String isactive;
	@JsonProperty("landmark_optional")
	private String landmarkOptional;
	@JsonProperty("last_mile_contract_term")
	private Double lastMileContractTerm;
	@JsonProperty("last_modified_by_full_name")
	private String lastModifiedByFullName;
	@JsonProperty("latitude")
	private Double latitude;
	@JsonProperty("locality")
	private String locality;
	@JsonProperty("longitude")
	private Double longitude;
	@JsonProperty("mtu_frame_size_bytes")
	private String mtuFrameSizeBytes;
	@JsonProperty("opened_on")
	private String openedOn;
	@JsonProperty("opportunity_account_name")
	private String opportunityAccountName;
	@JsonProperty("opportunity_id")
	private Double opportunityId;
	@JsonProperty("opportunity_stage")
	private String opportunityStage;
	@JsonProperty("order_type")
	private String orderType;
	@JsonProperty("orderable_mrc_bw")
	private String orderableMrcBw;
	@JsonProperty("orderable_mrc_bw_currency")
	private String orderableMrcBwCurrency;
	@JsonProperty("orderable_otc_nrc_install_curr")
	private String orderableOtcNrcInstallCurr;
	@JsonProperty("orderable_otc_nrc_installation")
	private String orderableOtcNrcInstallation;
	@JsonProperty("orderable_otc_nrc_installation_currency")
	private String orderableOtcNrcInstallationCurrency;
	@JsonProperty("orderable_xconnect_mrc")
	private String orderableXconnectMrc;
	@JsonProperty("orderable_xconnect_mrc_currency")
	private String orderableXconnectMrcCurrency;
	@JsonProperty("orderable_xconnect_nrc")
	private String orderableXconnectNrc;
	@JsonProperty("orderable_xconnect_nrc_currency")
	private String orderableXconnectNrcCurrency;
	@JsonProperty("otc_nrc_installation_currency")
	private String otcNrcInstallationCurrency;
	@JsonProperty("other_provider_name")
	private String otherProviderName;
	@JsonProperty("pin_zip_a_end")
	private String pinZipAEnd;
	@JsonProperty("pin_zip_b_end")
	private String pinZipBEnd;
	@JsonProperty("pincode")
	private String pincode;
	@JsonProperty("product_name")
	private String productName;
	@JsonProperty("product_subtype")
	private String productSubtype;
	@JsonProperty("provider_product_name_other")
	private String providerProductNameOther;
	@JsonProperty("quote_age")
	private Double quoteAge;
	@JsonProperty("quote_category_long")
	private String quoteCategoryLong;
	@JsonProperty("rec_id")
	private String recId;
	@JsonProperty("record_type")
	private String recordType;
	@JsonProperty("region")
	private String region;
	@JsonProperty("remarks")
	private String remarks;
	@JsonProperty("response_cloned_from")
	private String responseClonedFrom;
	@JsonProperty("response_related_to")
	private String responseRelatedTo;
	@JsonProperty("response_type")
	private String responseType;
	@JsonProperty("returned_diversity")
	private String returnedDiversity;
	@JsonProperty("returned_underlying_technology")
	private String returnedUnderlyingTechnology;
	@JsonProperty("room")
	private String room;
	@JsonProperty("sales_selected_response")
	private Double salesSelectedResponse;
	@JsonProperty("task_acknowledged_by")
	private String taskAcknowledgedBy;
	@JsonProperty("task_close_date")
	private String taskCloseDate;
	@JsonProperty("tcl_pop_short_code")
	private String tclPopShortCode;
	@JsonProperty("tower")
	private String tower;
	@JsonProperty("type_2_provider")
	private String type2Provider;
	@JsonProperty("update_date")
	private String updateDate;
	@JsonProperty("vendor_id")
	private String vendorId;
	@JsonProperty("vendor_id_sfdc")
	private String vendorIdSfdc;
	@JsonProperty("vendor_name")
	private String vendorName;
	@JsonProperty("vendor_name_vendor_id1")
	private String vendorNameVendorId1;
	@JsonProperty("vlan_transparent")
	private String vlanTransparent;
	@JsonProperty("wing")
	private String wing;
	@JsonProperty("xconnect_mrc")
	private Double xconnectMrc;
	@JsonProperty("xconnect_mrc_currency")
	private String xconnectMrcCurrency;
	@JsonProperty("xconnect_nrc")
	private Double xconnectNrc;
	@JsonProperty("xconnect_nrc_currency")
	private String xconnectNrcCurrency;
	@JsonProperty("xconnect_provider_name")
	private String xconnectProviderName;
	@JsonProperty("xconnect_provider_name_vendor")
	private String xconnectProviderNameVendor;
	@JsonProperty("xconnect_provider_name_vendor_id")
	private String xconnectProviderNameVendorId;
	@JsonProperty("xconnect_provider_name_vendor_id1")
	private Double xconnectProviderNameVendorId1;
	@JsonProperty("xconnect_provider_name_vendor_name")
	private String xconnectProviderNameVendorName;
	@JsonProperty("xconnect_vendor_id_sfdc")
	private String xconnectVendorIdSfdc;
	@JsonProperty("access_type")
	private String accessType;
	@JsonProperty("country_id_a_end")
	private String countryIdAEnd;
	@JsonProperty("country_id_b_end")
	private String countryIdBEnd;
	@JsonProperty("cq_ee_id")
	private String cqEeId;
	
	
    
    @JsonProperty("all_sites_count")
    public String getAllSitesCount() {
        return allSitesCount;
    }

    @JsonProperty("all_sites_count")
    public void setAllSitesCount(String allSitesCount) {
        this.allSitesCount = allSitesCount;
    }

    @JsonProperty("all_sites_long")
    public String getAllSitesLong() {
        return allSitesLong;
    }

    @JsonProperty("all_sites_long")
    public void setAllSitesLong(String allSitesLong) {
        this.allSitesLong = allSitesLong;
    }

    @JsonProperty("all_sites_short")
    public String getAllSitesShort() {
        return allSitesShort;
    }

    @JsonProperty("all_sites_short")
    public void setAllSitesShort(String allSitesShort) {
        this.allSitesShort = allSitesShort;
    }

    @JsonProperty("allowedpop")
    public String getAllowedpop() {
        return allowedpop;
    }

    @JsonProperty("allowedpop")
    public void setAllowedpop(String allowedpop) {
        this.allowedpop = allowedpop;
    }

    @JsonProperty("allowedprod")
    public String getAllowedprod() {
        return allowedprod;
    }

    @JsonProperty("allowedprod")
    public void setAllowedprod(String allowedprod) {
        this.allowedprod = allowedprod;
    }

    @JsonProperty("buildingaddress")
    public String getBuildingaddress() {
        return buildingaddress;
    }

    @JsonProperty("buildingaddress")
    public void setBuildingaddress(String buildingaddress) {
        this.buildingaddress = buildingaddress;
    }

    @JsonProperty("bw")
    public Double getBw() {
        return bw;
    }

    @JsonProperty("bw")
    public void setBw(Double bw) {
        this.bw = bw;
    }

    @JsonProperty("bw_band")
    public String getBwBand() {
        return bwBand;
    }

    @JsonProperty("bw_band")
    public void setBwBand(String bwBand) {
        this.bwBand = bwBand;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("cluster_id")
    public String getClusterId() {
        return clusterId;
    }

    @JsonProperty("cluster_id")
    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    @JsonProperty("contract_term_with_vendor_months")
    public Double getContractTermWithVendorMonths() {
        return contractTermWithVendorMonths;
    }

    @JsonProperty("contract_term_with_vendor_months")
    public void setContractTermWithVendorMonths(Double contractTermWithVendorMonths) {
        this.contractTermWithVendorMonths = contractTermWithVendorMonths;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("country_id")
    public String getCountryId() {
        return countryId;
    }

    @JsonProperty("country_id")
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @JsonProperty("coverage")
    public String getCoverage() {
        return coverage;
    }

    @JsonProperty("coverage")
    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    @JsonProperty("coverage_area_name")
    public String getCoverageAreaName() {
        return coverageAreaName;
    }

    @JsonProperty("coverage_area_name")
    public void setCoverageAreaName(String coverageAreaName) {
        this.coverageAreaName = coverageAreaName;
    }

    @JsonProperty("coverage_type")
    public String getCoverageType() {
        return coverageType;
    }

    @JsonProperty("coverage_type")
    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    @JsonProperty("db_code")
    public String getDbCode() {
        return dbCode;
    }

    @JsonProperty("db_code")
    public void setDbCode(String dbCode) {
        this.dbCode = dbCode;
    }

    @JsonProperty("dist_mtrs")
    public Double getDistMtrs() {
        return distMtrs;
    }

    @JsonProperty("dist_mtrs")
    public void setDistMtrs(Double distMtrs) {
        this.distMtrs = distMtrs;
    }

    @JsonProperty("distance_to_pop")
    public Double getDistanceToPop() {
        return distanceToPop;
    }

    @JsonProperty("distance_to_pop")
    public void setDistanceToPop(Double distanceToPop) {
        this.distanceToPop = distanceToPop;
    }

    @JsonProperty("eas")
    public String getEas() {
        return eas;
    }

    @JsonProperty("eas")
    public void setEas(String eas) {
        this.eas = eas;
    }

    @JsonProperty("enddate")
    public String getEnddate() {
        return enddate;
    }

    @JsonProperty("enddate")
    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    @JsonProperty("global_dedicated_ethernet")
    public String getGlobalDedicatedEthernet() {
        return globalDedicatedEthernet;
    }

    @JsonProperty("global_dedicated_ethernet")
    public void setGlobalDedicatedEthernet(String globalDedicatedEthernet) {
        this.globalDedicatedEthernet = globalDedicatedEthernet;
    }

    @JsonProperty("global_vpn")
    public String getGlobalVpn() {
        return globalVpn;
    }

    @JsonProperty("global_vpn")
    public void setGlobalVpn(String globalVpn) {
        this.globalVpn = globalVpn;
    }

    @JsonProperty("index")
    public String getIndex() {
        return index;
    }

    @JsonProperty("index")
    public void setIndex(String index) {
        this.index = index;
    }

    @JsonProperty("inter_connection_type")
    public String getInterConnectionType() {
        return interConnectionType;
    }

    @JsonProperty("inter_connection_type")
    public void setInterConnectionType(String interConnectionType) {
        this.interConnectionType = interConnectionType;
    }

    @JsonProperty("ip_transit")
    public String getIpTransit() {
        return ipTransit;
    }

    @JsonProperty("ip_transit")
    public void setIpTransit(String ipTransit) {
        this.ipTransit = ipTransit;
    }

    @JsonProperty("ipl")
    public String getIpl() {
        return ipl;
    }

    @JsonProperty("ipl")
    public void setIpl(String ipl) {
        this.ipl = ipl;
    }

    @JsonProperty("izo_internet_wan")
    public String getIzoInternetWan() {
        return izoInternetWan;
    }

    @JsonProperty("izo_internet_wan")
    public void setIzoInternetWan(String izoInternetWan) {
        this.izoInternetWan = izoInternetWan;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonProperty("latdiff_deg")
    public Double getLatdiffDeg() {
        return latdiffDeg;
    }

    @JsonProperty("latdiff_deg")
    public void setLatdiffDeg(Double latdiffDeg) {
        this.latdiffDeg = latdiffDeg;
    }

    @JsonProperty("local_loop_capacity")
    public String getLocalLoopCapacity() {
        return localLoopCapacity;
    }

    @JsonProperty("local_loop_capacity")
    public void setLocalLoopCapacity(String localLoopCapacity) {
        this.localLoopCapacity = localLoopCapacity;
    }

    @JsonProperty("local_loop_comment")
    public String getLocalLoopComment() {
        return localLoopComment;
    }

    @JsonProperty("local_loop_comment")
    public void setLocalLoopComment(String localLoopComment) {
        this.localLoopComment = localLoopComment;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("long")
    public Double getLong() {
        return _long;
    }

    @JsonProperty("long")
    public void setLong(Double _long) {
        this._long = _long;
    }

    @JsonProperty("longdiff_deg")
    public Double getLongdiffDeg() {
        return longdiffDeg;
    }

    @JsonProperty("longdiff_deg")
    public void setLongdiffDeg(Double longdiffDeg) {
        this.longdiffDeg = longdiffDeg;
    }

    @JsonProperty("low_latency_gde")
    public String getLowLatencyGde() {
        return lowLatencyGde;
    }

    @JsonProperty("low_latency_gde")
    public void setLowLatencyGde(String lowLatencyGde) {
        this.lowLatencyGde = lowLatencyGde;
    }

    @JsonProperty("mrc_bw")
    public Double getMrcBw() {
        return mrcBw;
    }

    @JsonProperty("mrc_bw")
    public void setMrcBw(Double mrcBw) {
        this.mrcBw = mrcBw;
    }

    @JsonProperty("mrc_bw_currency")
    public String getMrcBwCurrency() {
        return mrcBwCurrency;
    }

    @JsonProperty("mrc_bw_currency")
    public void setMrcBwCurrency(String mrcBwCurrency) {
        this.mrcBwCurrency = mrcBwCurrency;
    }

    @JsonProperty("otc_nrc_installation")
    public String getOtcNrcInstallation() {
        return otcNrcInstallation;
    }

    @JsonProperty("otc_nrc_installation")
    public void setOtcNrcInstallation(String otcNrcInstallation) {
        this.otcNrcInstallation = otcNrcInstallation;
    }

    @JsonProperty("owner_region")
    public String getOwnerRegion() {
        return ownerRegion;
    }

    @JsonProperty("owner_region")
    public void setOwnerRegion(String ownerRegion) {
        this.ownerRegion = ownerRegion;
    }

    @JsonProperty("pop_coord")
    public List<Double> getPopCoord() {
        return popCoord;
    }

    @JsonProperty("pop_coord")
    public void setPopCoord(List<Double> popCoord) {
        this.popCoord = popCoord;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("priority_ethernet")
    public String getPriorityEthernet() {
        return priorityEthernet;
    }

    @JsonProperty("priority_ethernet")
    public void setPriorityEthernet(String priorityEthernet) {
        this.priorityEthernet = priorityEthernet;
    }

    @JsonProperty("provider_name")
    public String getProviderName() {
        return providerName;
    }

    @JsonProperty("provider_name")
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @JsonProperty("provider_product_name")
    public String getProviderProductName() {
        return providerProductName;
    }

    @JsonProperty("provider_product_name")
    public void setProviderProductName(String providerProductName) {
        this.providerProductName = providerProductName;
    }

    @JsonProperty("provider_product_name_subset")
    public String getProviderProductNameSubset() {
        return providerProductNameSubset;
    }

    @JsonProperty("provider_product_name_subset")
    public void setProviderProductNameSubset(String providerProductNameSubset) {
        this.providerProductNameSubset = providerProductNameSubset;
    }

    @JsonProperty("quote_category_short")
    public String getQuoteCategoryShort() {
        return quoteCategoryShort;
    }

    @JsonProperty("quote_category_short")
    public void setQuoteCategoryShort(String quoteCategoryShort) {
        this.quoteCategoryShort = quoteCategoryShort;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("startdate")
    public String getStartdate() {
        return startdate;
    }

    @JsonProperty("startdate")
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("tcl_pop_address")
    public String getTclPopAddress() {
        return tclPopAddress;
    }

    @JsonProperty("tcl_pop_address")
    public void setTclPopAddress(String tclPopAddress) {
        this.tclPopAddress = tclPopAddress;
    }

    @JsonProperty("tcl_pop_latitude")
    public Double getTclPopLatitude() {
        return tclPopLatitude;
    }

    @JsonProperty("tcl_pop_latitude")
    public void setTclPopLatitude(Double tclPopLatitude) {
        this.tclPopLatitude = tclPopLatitude;
    }

    @JsonProperty("tcl_pop_longitude")
    public Double getTclPopLongitude() {
        return tclPopLongitude;
    }

    @JsonProperty("tcl_pop_longitude")
    public void setTclPopLongitude(Double tclPopLongitude) {
        this.tclPopLongitude = tclPopLongitude;
    }

    @JsonProperty("telepresence")
    public String getTelepresence() {
        return telepresence;
    }

    @JsonProperty("telepresence")
    public void setTelepresence(String telepresence) {
        this.telepresence = telepresence;
    }

    @JsonProperty("termdiscountmrc24months")
    public Double getTermdiscountmrc24months() {
        return termdiscountmrc24months;
    }

    @JsonProperty("termdiscountmrc24months")
    public void setTermdiscountmrc24months(Double termdiscountmrc24months) {
        this.termdiscountmrc24months = termdiscountmrc24months;
    }

    @JsonProperty("termdiscountmrc36months")
    public Double getTermdiscountmrc36months() {
        return termdiscountmrc36months;
    }

    @JsonProperty("termdiscountmrc36months")
    public void setTermdiscountmrc36months(Double termdiscountmrc36months) {
        this.termdiscountmrc36months = termdiscountmrc36months;
    }

    @JsonProperty("termdiscountnrc24months")
    public Double getTermdiscountnrc24months() {
        return termdiscountnrc24months;
    }

    @JsonProperty("termdiscountnrc24months")
    public void setTermdiscountnrc24months(Double termdiscountnrc24months) {
        this.termdiscountnrc24months = termdiscountnrc24months;
    }

    @JsonProperty("termdiscountnrc36months")
    public Double getTermdiscountnrc36months() {
        return termdiscountnrc36months;
    }

    @JsonProperty("termdiscountnrc36months")
    public void setTermdiscountnrc36months(Double termdiscountnrc36months) {
        this.termdiscountnrc36months = termdiscountnrc36months;
    }

    public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddressAEnd() {
		return addressAEnd;
	}

	public void setAddressAEnd(String addressAEnd) {
		this.addressAEnd = addressAEnd;
	}

	public String getAddressBEnd() {
		return addressBEnd;
	}

	public void setAddressBEnd(String addressBEnd) {
		this.addressBEnd = addressBEnd;
	}

	public String getAddressFromTool1() {
		return addressFromTool1;
	}

	public void setAddressFromTool1(String addressFromTool1) {
		this.addressFromTool1 = addressFromTool1;
	}

	public String getAptSuiteInfo() {
		return aptSuiteInfo;
	}

	public void setAptSuiteInfo(String aptSuiteInfo) {
		this.aptSuiteInfo = aptSuiteInfo;
	}

	public String getAssignedToFullName() {
		return assignedToFullName;
	}

	public void setAssignedToFullName(String assignedToFullName) {
		this.assignedToFullName = assignedToFullName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public Double getBwLog() {
		return bwLog;
	}

	public void setBwLog(Double bwLog) {
		this.bwLog = bwLog;
	}

	public String getCableSystemName() {
		return cableSystemName;
	}

	public void setCableSystemName(String cableSystemName) {
		this.cableSystemName = cableSystemName;
	}

	public String getCity2() {
		return city2;
	}

	public void setCity2(String city2) {
		this.city2 = city2;
	}

	public String getCityAEnd() {
		return cityAEnd;
	}

	public void setCityAEnd(String cityAEnd) {
		this.cityAEnd = cityAEnd;
	}

	public String getCityBEnd() {
		return cityBEnd;
	}

	public void setCityBEnd(String cityBEnd) {
		this.cityBEnd = cityBEnd;
	}

	public String getCountryAEnd() {
		return countryAEnd;
	}

	public void setCountryAEnd(String countryAEnd) {
		this.countryAEnd = countryAEnd;
	}

	public String getCountryBEnd() {
		return countryBEnd;
	}

	public void setCountryBEnd(String countryBEnd) {
		this.countryBEnd = countryBEnd;
	}

	public String getCreatedByFullName() {
		return createdByFullName;
	}

	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}

	public String getCoOrdinatesAEndLatitude() {
		return coOrdinatesAEndLatitude;
	}

	public void setCoOrdinatesAEndLatitude(String coOrdinatesAEndLatitude) {
		this.coOrdinatesAEndLatitude = coOrdinatesAEndLatitude;
	}

	public String getCoOrdinatesAEndLongitude() {
		return coOrdinatesAEndLongitude;
	}

	public void setCoOrdinatesAEndLongitude(String coOrdinatesAEndLongitude) {
		this.coOrdinatesAEndLongitude = coOrdinatesAEndLongitude;
	}

	public String getCoOrdinatesBEndLatitude() {
		return coOrdinatesBEndLatitude;
	}

	public void setCoOrdinatesBEndLatitude(String coOrdinatesBEndLatitude) {
		this.coOrdinatesBEndLatitude = coOrdinatesBEndLatitude;
	}

	public String getCoOrdinatesBEndLongitude() {
		return coOrdinatesBEndLongitude;
	}

	public void setCoOrdinatesBEndLongitude(String coOrdinatesBEndLongitude) {
		this.coOrdinatesBEndLongitude = coOrdinatesBEndLongitude;
	}

	public String getCustomerLatitude1() {
		return customerLatitude1;
	}

	public void setCustomerLatitude1(String customerLatitude1) {
		this.customerLatitude1 = customerLatitude1;
	}

	public String getCustomerLongitude1() {
		return customerLongitude1;
	}

	public void setCustomerLongitude1(String customerLongitude1) {
		this.customerLongitude1 = customerLongitude1;
	}

	public Double getDolPerMb() {
		return dolPerMb;
	}

	public void setDolPerMb(Double dolPerMb) {
		this.dolPerMb = dolPerMb;
	}

	public Double getFeasibilityRequestRequestNo() {
		return feasibilityRequestRequestNo;
	}

	public void setFeasibilityRequestRequestNo(Double feasibilityRequestRequestNo) {
		this.feasibilityRequestRequestNo = feasibilityRequestRequestNo;
	}

	public String getFeasibilityResponseAutoNo() {
		return feasibilityResponseAutoNo;
	}

	public void setFeasibilityResponseAutoNo(String feasibilityResponseAutoNo) {
		this.feasibilityResponseAutoNo = feasibilityResponseAutoNo;
	}

	public String getFeasibilityResponseId() {
		return feasibilityResponseId;
	}

	public void setFeasibilityResponseId(String feasibilityResponseId) {
		this.feasibilityResponseId = feasibilityResponseId;
	}

	public String getFeasibilityTaskAutoNo() {
		return feasibilityTaskAutoNo;
	}

	public void setFeasibilityTaskAutoNo(String feasibilityTaskAutoNo) {
		this.feasibilityTaskAutoNo = feasibilityTaskAutoNo;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	@JsonProperty("updatedby")
    public String getUpdatedby() {
        return updatedby;
    }

    @JsonProperty("updatedby")
    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    @JsonProperty("updatedon")
    public String getUpdatedon() {
        return updatedon;
    }

    @JsonProperty("updatedon")
    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

    @JsonProperty("video_connect")
    public String getVideoConnect() {
        return videoConnect;
    }

    @JsonProperty("video_connect")
    public void setVideoConnect(String videoConnect) {
        this.videoConnect = videoConnect;
    }

    @JsonProperty("vpls")
    public String getVpls() {
        return vpls;
    }

    @JsonProperty("vpls")
    public void setVpls(String vpls) {
        this.vpls = vpls;
    }

    @JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, String value) {
        this.additionalProperties.put(name, value);
    }



	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getLandmarkOptional() {
		return landmarkOptional;
	}

	public void setLandmarkOptional(String landmarkOptional) {
		this.landmarkOptional = landmarkOptional;
	}

	public Double getLastMileContractTerm() {
		return lastMileContractTerm;
	}

	public void setLastMileContractTerm(Double lastMileContractTerm) {
		this.lastMileContractTerm = lastMileContractTerm;
	}

	public String getLastModifiedByFullName() {
		return lastModifiedByFullName;
	}

	public void setLastModifiedByFullName(String lastModifiedByFullName) {
		this.lastModifiedByFullName = lastModifiedByFullName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getMtuFrameSizeBytes() {
		return mtuFrameSizeBytes;
	}

	public void setMtuFrameSizeBytes(String mtuFrameSizeBytes) {
		this.mtuFrameSizeBytes = mtuFrameSizeBytes;
	}

	public String getOpenedOn() {
		return openedOn;
	}

	public void setOpenedOn(String openedOn) {
		this.openedOn = openedOn;
	}

	public String getOpportunityAccountName() {
		return opportunityAccountName;
	}

	public void setOpportunityAccountName(String opportunityAccountName) {
		this.opportunityAccountName = opportunityAccountName;
	}

	public Double getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Double opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getOpportunityStage() {
		return opportunityStage;
	}

	public void setOpportunityStage(String opportunityStage) {
		this.opportunityStage = opportunityStage;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderableMrcBw() {
		return orderableMrcBw;
	}

	public void setOrderableMrcBw(String orderableMrcBw) {
		this.orderableMrcBw = orderableMrcBw;
	}

	public String getOrderableMrcBwCurrency() {
		return orderableMrcBwCurrency;
	}

	public void setOrderableMrcBwCurrency(String orderableMrcBwCurrency) {
		this.orderableMrcBwCurrency = orderableMrcBwCurrency;
	}

	public String getOrderableOtcNrcInstallCurr() {
		return orderableOtcNrcInstallCurr;
	}

	public void setOrderableOtcNrcInstallCurr(String orderableOtcNrcInstallCurr) {
		this.orderableOtcNrcInstallCurr = orderableOtcNrcInstallCurr;
	}

	public String getOrderableOtcNrcInstallation() {
		return orderableOtcNrcInstallation;
	}

	public void setOrderableOtcNrcInstallation(String orderableOtcNrcInstallation) {
		this.orderableOtcNrcInstallation = orderableOtcNrcInstallation;
	}

	public String getOrderableOtcNrcInstallationCurrency() {
		return orderableOtcNrcInstallationCurrency;
	}

	public void setOrderableOtcNrcInstallationCurrency(String orderableOtcNrcInstallationCurrency) {
		this.orderableOtcNrcInstallationCurrency = orderableOtcNrcInstallationCurrency;
	}

	public String getOrderableXconnectMrc() {
		return orderableXconnectMrc;
	}

	public void setOrderableXconnectMrc(String orderableXconnectMrc) {
		this.orderableXconnectMrc = orderableXconnectMrc;
	}

	public String getOrderableXconnectMrcCurrency() {
		return orderableXconnectMrcCurrency;
	}

	public void setOrderableXconnectMrcCurrency(String orderableXconnectMrcCurrency) {
		this.orderableXconnectMrcCurrency = orderableXconnectMrcCurrency;
	}

	public String getOrderableXconnectNrc() {
		return orderableXconnectNrc;
	}

	public void setOrderableXconnectNrc(String orderableXconnectNrc) {
		this.orderableXconnectNrc = orderableXconnectNrc;
	}

	public String getOrderableXconnectNrcCurrency() {
		return orderableXconnectNrcCurrency;
	}

	public void setOrderableXconnectNrcCurrency(String orderableXconnectNrcCurrency) {
		this.orderableXconnectNrcCurrency = orderableXconnectNrcCurrency;
	}

	public String getOtcNrcInstallationCurrency() {
		return otcNrcInstallationCurrency;
	}

	public void setOtcNrcInstallationCurrency(String otcNrcInstallationCurrency) {
		this.otcNrcInstallationCurrency = otcNrcInstallationCurrency;
	}

	public String getOtherProviderName() {
		return otherProviderName;
	}

	public void setOtherProviderName(String otherProviderName) {
		this.otherProviderName = otherProviderName;
	}

	public String getPinZipAEnd() {
		return pinZipAEnd;
	}

	public void setPinZipAEnd(String pinZipAEnd) {
		this.pinZipAEnd = pinZipAEnd;
	}

	public String getPinZipBEnd() {
		return pinZipBEnd;
	}

	public void setPinZipBEnd(String pinZipBEnd) {
		this.pinZipBEnd = pinZipBEnd;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSubtype() {
		return productSubtype;
	}

	public void setProductSubtype(String productSubtype) {
		this.productSubtype = productSubtype;
	}

	public String getProviderProductNameOther() {
		return providerProductNameOther;
	}

	public void setProviderProductNameOther(String providerProductNameOther) {
		this.providerProductNameOther = providerProductNameOther;
	}

	public Double getQuoteAge() {
		return quoteAge;
	}

	public void setQuoteAge(Double quoteAge) {
		this.quoteAge = quoteAge;
	}

	public String getQuoteCategoryLong() {
		return quoteCategoryLong;
	}

	public void setQuoteCategoryLong(String quoteCategoryLong) {
		this.quoteCategoryLong = quoteCategoryLong;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getResponseClonedFrom() {
		return responseClonedFrom;
	}

	public void setResponseClonedFrom(String responseClonedFrom) {
		this.responseClonedFrom = responseClonedFrom;
	}

	public String getResponseRelatedTo() {
		return responseRelatedTo;
	}

	public void setResponseRelatedTo(String responseRelatedTo) {
		this.responseRelatedTo = responseRelatedTo;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getReturnedDiversity() {
		return returnedDiversity;
	}

	public void setReturnedDiversity(String returnedDiversity) {
		this.returnedDiversity = returnedDiversity;
	}

	public String getReturnedUnderlyingTechnology() {
		return returnedUnderlyingTechnology;
	}

	public void setReturnedUnderlyingTechnology(String returnedUnderlyingTechnology) {
		this.returnedUnderlyingTechnology = returnedUnderlyingTechnology;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Double getSalesSelectedResponse() {
		return salesSelectedResponse;
	}

	public void setSalesSelectedResponse(Double salesSelectedResponse) {
		this.salesSelectedResponse = salesSelectedResponse;
	}

	public String getTaskAcknowledgedBy() {
		return taskAcknowledgedBy;
	}

	public void setTaskAcknowledgedBy(String taskAcknowledgedBy) {
		this.taskAcknowledgedBy = taskAcknowledgedBy;
	}

	public String getTaskCloseDate() {
		return taskCloseDate;
	}

	public void setTaskCloseDate(String taskCloseDate) {
		this.taskCloseDate = taskCloseDate;
	}

	public String getTclPopShortCode() {
		return tclPopShortCode;
	}

	public void setTclPopShortCode(String tclPopShortCode) {
		this.tclPopShortCode = tclPopShortCode;
	}

	public String getTower() {
		return tower;
	}

	public void setTower(String tower) {
		this.tower = tower;
	}

	public String getType2Provider() {
		return type2Provider;
	}

	public void setType2Provider(String type2Provider) {
		this.type2Provider = type2Provider;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorIdSfdc() {
		return vendorIdSfdc;
	}

	public void setVendorIdSfdc(String vendorIdSfdc) {
		this.vendorIdSfdc = vendorIdSfdc;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorNameVendorId1() {
		return vendorNameVendorId1;
	}

	public void setVendorNameVendorId1(String vendorNameVendorId1) {
		this.vendorNameVendorId1 = vendorNameVendorId1;
	}

	public String getVlanTransparent() {
		return vlanTransparent;
	}

	public void setVlanTransparent(String vlanTransparent) {
		this.vlanTransparent = vlanTransparent;
	}

	public String getWing() {
		return wing;
	}

	public void setWing(String wing) {
		this.wing = wing;
	}

	public Double getXconnectMrc() {
		return xconnectMrc;
	}

	public void setXconnectMrc(Double xconnectMrc) {
		this.xconnectMrc = xconnectMrc;
	}

	public String getXconnectMrcCurrency() {
		return xconnectMrcCurrency;
	}

	public void setXconnectMrcCurrency(String xconnectMrcCurrency) {
		this.xconnectMrcCurrency = xconnectMrcCurrency;
	}

	public Double getXconnectNrc() {
		return xconnectNrc;
	}

	public void setXconnectNrc(Double xconnectNrc) {
		this.xconnectNrc = xconnectNrc;
	}

	public String getXconnectNrcCurrency() {
		return xconnectNrcCurrency;
	}

	public void setXconnectNrcCurrency(String xconnectNrcCurrency) {
		this.xconnectNrcCurrency = xconnectNrcCurrency;
	}

	public String getXconnectProviderName() {
		return xconnectProviderName;
	}

	public void setXconnectProviderName(String xconnectProviderName) {
		this.xconnectProviderName = xconnectProviderName;
	}

	public String getXconnectProviderNameVendor() {
		return xconnectProviderNameVendor;
	}

	public void setXconnectProviderNameVendor(String xconnectProviderNameVendor) {
		this.xconnectProviderNameVendor = xconnectProviderNameVendor;
	}

	public String getXconnectProviderNameVendorId() {
		return xconnectProviderNameVendorId;
	}

	public void setXconnectProviderNameVendorId(String xconnectProviderNameVendorId) {
		this.xconnectProviderNameVendorId = xconnectProviderNameVendorId;
	}

	public Double getXconnectProviderNameVendorId1() {
		return xconnectProviderNameVendorId1;
	}

	public void setXconnectProviderNameVendorId1(Double xconnectProviderNameVendorId1) {
		this.xconnectProviderNameVendorId1 = xconnectProviderNameVendorId1;
	}

	public String getXconnectProviderNameVendorName() {
		return xconnectProviderNameVendorName;
	}

	public void setXconnectProviderNameVendorName(String xconnectProviderNameVendorName) {
		this.xconnectProviderNameVendorName = xconnectProviderNameVendorName;
	}

	public String getXconnectVendorIdSfdc() {
		return xconnectVendorIdSfdc;
	}

	public void setXconnectVendorIdSfdc(String xconnectVendorIdSfdc) {
		this.xconnectVendorIdSfdc = xconnectVendorIdSfdc;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getCountryIdAEnd() {
		return countryIdAEnd;
	}

	public void setCountryIdAEnd(String countryIdAEnd) {
		this.countryIdAEnd = countryIdAEnd;
	}

	public String getCountryIdBEnd() {
		return countryIdBEnd;
	}

	public void setCountryIdBEnd(String countryIdBEnd) {
		this.countryIdBEnd = countryIdBEnd;
	}

	public String getCqEeId() {
		return cqEeId;
	}

	public void setCqEeId(String cqEeId) {
		this.cqEeId = cqEeId;
	}

	public void setAdditionalProperties(Map<String, String> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	public String getSelectedQuote() {
		return selectedQuote;
	}

	public void setSelectedQuote(String selectedQuote) {
		this.selectedQuote = selectedQuote;
	}

}
