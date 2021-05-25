package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This is the entity file for quote_izosdwan_byon_upload_details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="quote_izosdwan_byon_upload_details")
@NamedQuery(name="QuoteIzosdwanByonUploadDetail.findAll", query="SELECT q FROM QuoteIzosdwanByonUploadDetail q")
public class QuoteIzosdwanByonUploadDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;

	
	private String address;

	private String city;

	private String country;

	@Column(name="error_details")
	private String errorDetails;

	@Column(name="internet_quality")
	private String internetQuality;

	@Column(name="management_option")
	private String managementOption;
	
	@Column(name="pri_thirdparty_linkuptime")
	private String priThirdPartyLinkUptime;
	
	public String getPriThirdPartyLinkUptime() {
		return priThirdPartyLinkUptime;
	}

	public void setPriThirdPartyLinkUptime(String priThirdPartyLinkUptime) {
		this.priThirdPartyLinkUptime = priThirdPartyLinkUptime;
	}

	public String getSecThirdPartyLinkUptime() {
		return secThirdPartyLinkUptime;
	}

	public void setSecThirdPartyLinkUptime(String secThirdPartyLinkUptime) {
		this.secThirdPartyLinkUptime = secThirdPartyLinkUptime;
	}

	@Column(name="sec_thirdparty_linkuptime")
	private String secThirdPartyLinkUptime;
	
	public String getManagementOption() {
		return managementOption;
	}

	public void setManagementOption(String managementOption) {
		this.managementOption = managementOption;
	}

//	@Column(name="cos_topology")
//	private String cosTopoloy;
//	
//	public String getCosTopoloy() {
//		return cosTopoloy;
//	}
//
//	public void setCosTopoloy(String cosTopoloy) {
//		this.cosTopoloy = cosTopoloy;
//	}

	private String locality;

	
	private String pincode;

	@Column(name="pri_access_type")
	private String priAccessType;

	@Column(name="pri_interface")
	private String priInterface;
	
	@Column(name="pri_thirdparty_service_id")
	private String priThirdpartyServiceId;
	
	@Column(name="pri_thirdparty_ipaddress")
	private String priThirdpartyIpAddress;
	
	@Column(name="pri_thirdparty_provider")
	private String priThirdpartyProvider;
	
	@Column(name="pri_thirdparty_byon_lte")
	private String priThirdpartyByonLte;
	
	@Column(name="sec_thirdparty_service_id")
	private String secThirdpartyServiceId;
	
	@Column(name="sec_thirdparty_ipaddress")
	private String secThirdpartyIpAddress;
	
	@Column(name="sec_thirdparty_provider")
	private String secThirdpartyProvider;
	
	@Column(name="sec_thirdparty_byon_lte")
	private String secThirdpartyByonLte;

	public String getSecThirdpartyServiceId() {
		return secThirdpartyServiceId;
	}

	public void setSecThirdpartyServiceId(String secThirdpartyServiceId) {
		this.secThirdpartyServiceId = secThirdpartyServiceId;
	}

	public String getSecThirdpartyIpAddress() {
		return secThirdpartyIpAddress;
	}

	public void setSecThirdpartyIpAddress(String secThirdpartyIpAddress) {
		this.secThirdpartyIpAddress = secThirdpartyIpAddress;
	}

	public String getSecThirdpartyProvider() {
		return secThirdpartyProvider;
	}

	public void setSecThirdpartyProvider(String secThirdpartyProvider) {
		this.secThirdpartyProvider = secThirdpartyProvider;
	}

	public String getSecThirdpartyByonLte() {
		return secThirdpartyByonLte;
	}

	public void setSecThirdpartyByonLte(String secThirdpartyByonLte) {
		this.secThirdpartyByonLte = secThirdpartyByonLte;
	}

	public String getPriThirdpartyServiceId() {
		return priThirdpartyServiceId;
	}

	public void setPriThirdpartyServiceId(String priThirdpartyServiceId) {
		this.priThirdpartyServiceId = priThirdpartyServiceId;
	}

	public String getPriThirdpartyIpAddress() {
		return priThirdpartyIpAddress;
	}

	public void setPriThirdpartyIpAddress(String priThirdpartyIpAddress) {
		this.priThirdpartyIpAddress = priThirdpartyIpAddress;
	}

	public String getPriThirdpartyProvider() {
		return priThirdpartyProvider;
	}

	public void setPriThirdpartyProvider(String priThirdpartyProvider) {
		this.priThirdpartyProvider = priThirdpartyProvider;
	}

	public String getPriThirdpartyByonLte() {
		return priThirdpartyByonLte;
	}

	public void setPriThirdpartyByonLte(String priThirdpartyByonLte) {
		this.priThirdpartyByonLte = priThirdpartyByonLte;
	}

	@Column(name="pri_lastmile_bw")
	private String priLastmileBw;

	@Column(name="pri_port_bw")
	private String priPortBw;

	@Column(name="pri_port_mode")
	private String priPortMode;

	@Column(name="sec_access_type")
	private String secAccessType;

	@Column(name="sec_interface")
	private String secInterface;

	@Column(name="sec_lastmile_bw")
	private String secLastmileBw;

	@Column(name="sec_port_bw")
	private String secPortBw;

	@Column(name="sec_port_mode")
	private String secPortMode;

	@Column(name="site_type")
	private String siteType;
	
	@Column(name="address_choice")
	private String existingAddressChoice;
	
	@Column(name="existing_address")
	private String existingAddress;
	
	@Column(name="is_shared")
	private String isShared;
	
	private String state;
	
	@Column(name="location_id")
	private Integer locationId;
	
	private String status;
	
	@Column(name="retrigger_count")
	private Integer retriggerCount;
	
	@Column(name="lat_long")
	private String latLong;

	@Column(name="location_error_details")
	private String locationErrorDetails;
	
	@Column(name="site_id")
	private Integer siteId;

	//bi-directional many-to-one association to Quote
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quote_id")
	private Quote quote;

	public QuoteIzosdwanByonUploadDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getErrorDetails() {
		return this.errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getInternetQuality() {
		return this.internetQuality;
	}

	public void setInternetQuality(String internetQuality) {
		this.internetQuality = internetQuality;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPriAccessType() {
		return this.priAccessType;
	}

	public void setPriAccessType(String priAccessType) {
		this.priAccessType = priAccessType;
	}

	public String getPriInterface() {
		return this.priInterface;
	}

	public void setPriInterface(String priInterface) {
		this.priInterface = priInterface;
	}

	public String getPriLastmileBw() {
		return this.priLastmileBw;
	}

	public void setPriLastmileBw(String priLastmileBw) {
		this.priLastmileBw = priLastmileBw;
	}

	public String getPriPortBw() {
		return this.priPortBw;
	}

	public void setPriPortBw(String priPortBw) {
		this.priPortBw = priPortBw;
	}

	public String getPriPortMode() {
		return this.priPortMode;
	}

	public void setPriPortMode(String priPortMode) {
		this.priPortMode = priPortMode;
	}

	public String getSecAccessType() {
		return this.secAccessType;
	}

	public void setSecAccessType(String secAccessType) {
		this.secAccessType = secAccessType;
	}

	public String getSecInterface() {
		return this.secInterface;
	}

	public void setSecInterface(String secInterface) {
		this.secInterface = secInterface;
	}

	public String getSecLastmileBw() {
		return this.secLastmileBw;
	}

	public void setSecLastmileBw(String secLastmileBw) {
		this.secLastmileBw = secLastmileBw;
	}

	public String getSecPortBw() {
		return this.secPortBw;
	}

	public void setSecPortBw(String secPortBw) {
		this.secPortBw = secPortBw;
	}

	public String getSecPortMode() {
		return this.secPortMode;
	}

	public void setSecPortMode(String secPortMode) {
		this.secPortMode = secPortMode;
	}

	public String getSiteType() {
		return this.siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Quote getQuote() {
		return this.quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getRetriggerCount() {
		return retriggerCount;
	}

	public void setRetriggerCount(Integer retriggerCount) {
		this.retriggerCount = retriggerCount;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getLocationErrorDetails() {
		return locationErrorDetails;
	}

	public void setLocationErrorDetails(String locationErrorDetails) {
		this.locationErrorDetails = locationErrorDetails;
	}

	public String getExistingAddressChoice() {
		return existingAddressChoice;
	}

	public void setExistingAddressChoice(String existingAddressChoice) {
		this.existingAddressChoice = existingAddressChoice;
	}

	public String getExistingAddress() {
		return existingAddress;
	}

	public void setExistingAddress(String existingAddress) {
		this.existingAddress = existingAddress;
	}


	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
}