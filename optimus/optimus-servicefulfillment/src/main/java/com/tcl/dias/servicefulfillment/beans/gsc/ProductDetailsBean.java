package com.tcl.dias.servicefulfillment.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailsBean {
    private String invceGrpnFl;

    private String productName;

    public String getInvceGrpnFl() {
        return invceGrpnFl;
    }

    public void setInvceGrpnFl(String invceGrpnFl) {
        this.invceGrpnFl = invceGrpnFl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
