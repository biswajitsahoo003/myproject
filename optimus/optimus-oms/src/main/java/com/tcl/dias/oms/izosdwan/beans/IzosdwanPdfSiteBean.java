package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
/**
 * 
 * This bean contains details of the site
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
import java.util.List;

public class IzosdwanPdfSiteBean implements Serializable{
	
	private String siteType;
	private String country;
	private String city;
	private String address;
	private List<IzosdwanCommericalBean> izosdwanCommericalBeans;
//	private Map<String, TechDetailCof> primaryDetails;
//	private Map<String, TechDetailCof> secondaryDetails;
//	private String siteProduct;

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

    public List<IzosdwanCommericalBean> getIzosdwanCommericalBeans() {
        return izosdwanCommericalBeans;
    }

    public void setIzosdwanCommericalBeans(List<IzosdwanCommericalBean> izosdwanCommericalBeans) {
        this.izosdwanCommericalBeans = izosdwanCommericalBeans;
    }
}
