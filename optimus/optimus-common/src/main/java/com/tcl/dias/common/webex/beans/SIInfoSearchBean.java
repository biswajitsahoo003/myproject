package com.tcl.dias.common.webex.beans;

/**
 * SI Info bean for searching GVPN in inventory
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SIInfoSearchBean {
    Integer page;
    Integer size;
    String city;
    String alias;
    String searchText;
    String customerId;
    Integer partnerId;

    public SIInfoSearchBean() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SIInfoSearchBean{" +
                "city='" + city + '\'' +
                ", alias='" + alias + '\'' +
                ", searchText='" + searchText + '\'' +
                ", customerId=" + customerId +
                ", partnerId=" + partnerId +
                '}';
    }
}
