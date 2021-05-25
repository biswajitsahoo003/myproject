package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entity Class
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOfferingBean {

    private String productName;
    private String accessType;
    private Double mrc;
    private Double nrc;
    private Double tcv;


    private List<ScServiceDetail> gscConfigurationDetails=new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Double getMrc() {
        return mrc;
    }

    public void setMrc(Double mrc) {
        this.mrc = mrc;
    }

    public Double getNrc() {
        return nrc;
    }

    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }


    public Double getTcv() {
        return tcv;
    }

    public void setTcv(Double tcv) {
        this.tcv = tcv;
    }

    public List<ScServiceDetail> getGscConfigurationDetails() {
        return gscConfigurationDetails;
    }

    public void setGscConfigurationDetails(List<ScServiceDetail> gscConfigurationDetails) {
        this.gscConfigurationDetails = gscConfigurationDetails;
    }

}
