package com.tcl.dias.common.webex.beans;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Pricing bean for webex
 *
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexPricingBean {

    private Integer uID;
    private String country;
    private String phoneType;
    private Integer destId;
    private String destinationName;
    private BigDecimal nrc;
    private BigDecimal mrc;
    private BigDecimal highRate;
    private String comments;

    /**
     * Default constructor
     */
    public WebexPricingBean() {
        super();
    }

    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDestId() {
        return destId;
    }

    public void setDestId(Integer destId) {
        this.destId = destId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public BigDecimal getHighRate() {
        return highRate;
    }

    public void setHighRate(BigDecimal highRate) {
        this.highRate = highRate;
    }

    public BigDecimal getNrc() {
        return nrc;
    }

    public void setNrc(BigDecimal nrc) {
        this.nrc = nrc;
    }

    public BigDecimal getMrc() {
        return mrc;
    }

    public void setMrc(BigDecimal mrc) {
        this.mrc = mrc;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
