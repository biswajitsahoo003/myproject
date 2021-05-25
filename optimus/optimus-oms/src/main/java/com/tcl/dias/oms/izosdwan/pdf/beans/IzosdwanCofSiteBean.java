package com.tcl.dias.oms.izosdwan.pdf.beans;

import com.tcl.dias.oms.izosdwan.beans.IzosdwanCommericalBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanCommericalBeanCof;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class IzosdwanCofSiteBean implements Serializable {

    private String siteType;
    private String country;
    private String city;
    private String address;
    private List<IzosdwanCommericalBeanCof> izosdwanCommericalBeansCof;
    private Map<String, TechDetailCof> primaryDetails;
    private Map<String, TechDetailCof> secondaryDetails;
    private String siteProduct;
    private String portMode;
    private String billCurrency;

    public String getPortMode() {
		return portMode;
	}

	public void setPortMode(String portMode) {
		this.portMode = portMode;
	}

	public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<IzosdwanCommericalBeanCof> getIzosdwanCommericalBeansCof() {
        return izosdwanCommericalBeansCof;
    }

    public void setIzosdwanCommericalBeansCof(List<IzosdwanCommericalBeanCof> izosdwanCommericalBeansCof) {
        this.izosdwanCommericalBeansCof = izosdwanCommericalBeansCof;
    }

    public Map<String, TechDetailCof> getPrimaryDetails() {
        return primaryDetails;
    }

    public void setPrimaryDetails(Map<String, TechDetailCof> primaryDetails) {
        this.primaryDetails = primaryDetails;
    }

    public Map<String, TechDetailCof> getSecondaryDetails() {
        return secondaryDetails;
    }

    public void setSecondaryDetails(Map<String, TechDetailCof> secondaryDetails) {
        this.secondaryDetails = secondaryDetails;
    }

    public String getSiteProduct() {
        return siteProduct;
    }

    public void setSiteProduct(String siteProduct) {
        this.siteProduct = siteProduct;
    }

    public String getBillCurrency() {
        return billCurrency;
    }

    public void setBillCurrency(String billCurrency) {
        this.billCurrency = billCurrency;
    }
}
