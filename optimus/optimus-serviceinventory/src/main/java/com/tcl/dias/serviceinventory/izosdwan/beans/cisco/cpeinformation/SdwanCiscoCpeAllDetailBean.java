package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation;

import java.io.Serializable;

/**
 * Bean for returning all details of an SDWAN CPE
 * @author Srinivasa Raghavan
 */
public class SdwanCiscoCpeAllDetailBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String cpeName;
	private String cpeAvailability;
    private String siteAlias;
    private String cpeStatus;
    private String siteName;
    private String city;
    private String country;
    private String siteAddress;
    private String sku;
    private String model;
    private String serialNumber;
    private String osVersion;
    private CiscoSiteListBean ciscoSiteList;
	private String underlayServiceId;
	private String sdwanServiceId;
	private Integer underlaySysId;
	private String cpeAlias;
	private String timestamp;
	private String manufacturer;
	private String lastUpdateDate;

    public SdwanCiscoCpeAllDetailBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpeName() {
        return cpeName;
    }

    public void setCpeName(String cpeName) {
        this.cpeName = cpeName;
    }

    public String getSiteAlias() {
        return siteAlias;
    }

    public void setSiteAlias(String siteAlias) {
        this.siteAlias = siteAlias;
    }

    public String getCpeStatus() {
        return cpeStatus;
    }

    public void setCpeStatus(String cpeStatus) {
        this.cpeStatus = cpeStatus;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    
	public CiscoSiteListBean getCiscoSiteList() {
		return ciscoSiteList;
	}

	public void setCiscoSiteList(CiscoSiteListBean ciscoSiteList) {
		this.ciscoSiteList = ciscoSiteList;
	}

	public String getUnderlayServiceId() {
		return underlayServiceId;
	}

	public void setUnderlayServiceId(String underlayServiceId) {
		this.underlayServiceId = underlayServiceId;
	}

	public String getSdwanServiceId() {
		return sdwanServiceId;
	}

	public void setSdwanServiceId(String sdwanServiceId) {
		this.sdwanServiceId = sdwanServiceId;
	}

	public String getCpeAvailability() {
		return cpeAvailability;
	}

	public void setCpeAvailability(String cpeAvailability) {
		this.cpeAvailability = cpeAvailability;
	}
	

	public Integer getUnderlaySysId() {
		return underlaySysId;
	}

	public void setUnderlaySysId(Integer underlaySysId) {
		this.underlaySysId = underlaySysId;
	}

	public String getCpeAlias() {
		return cpeAlias;
	}

	public void setCpeAlias(String cpeAlias) {
		this.cpeAlias = cpeAlias;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public String toString() {
		return "SdwanCiscoCpeAllDetailBean [id=" + id + ", cpeName=" + cpeName + ", cpeAvailability=" + cpeAvailability
				+ ", siteAlias=" + siteAlias + ", cpeStatus=" + cpeStatus + ", siteName=" + siteName + ", city=" + city
				+ ", country=" + country + ", siteAddress=" + siteAddress + ", sku=" + sku + ", model=" + model
				+ ", serialNumber=" + serialNumber + ", osVersion=" + osVersion + ", ciscoSiteList=" + ciscoSiteList
				+ ", underlayServiceId=" + underlayServiceId + ", sdwanServiceId=" + sdwanServiceId + ", underlaySysId="
				+ underlaySysId + ", cpeAlias=" + cpeAlias + ", timestamp=" + timestamp + ", manufacturer="
				+ manufacturer + ", lastUpdateDate=" + lastUpdateDate + "]";
	}

	

}
