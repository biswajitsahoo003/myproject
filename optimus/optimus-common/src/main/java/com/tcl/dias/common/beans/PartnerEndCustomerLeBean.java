package com.tcl.dias.common.beans;

import io.swagger.models.auth.In;

/**
 * <Comments>
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class PartnerEndCustomerLeBean {

    private String endCustomerCUID;

    private String endCustomerLeName;

    private String source;

    private String tempCustomerLeId;

    public String getEndCustomerCUID() {
        return endCustomerCUID;
    }

    public void setEndCustomerCUID(String endCustomerCUID) {
        this.endCustomerCUID = endCustomerCUID;
    }

    public String getEndCustomerLeName() {
        return endCustomerLeName;
    }

    public void setEndCustomerLeName(String endCustomerLeName) {
        this.endCustomerLeName = endCustomerLeName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTempCustomerLeId() {
        return tempCustomerLeId;
    }

    public void setTempCustomerLeId(String tempCustomerLeId) {
        this.tempCustomerLeId = tempCustomerLeId;
    }

    @Override
    public String toString() {
        return "PartnerEndCustomerLeBean{" +
                "endCustomerCUID='" + endCustomerCUID + '\'' +
                ", endCustomerLeName='" + endCustomerLeName + '\'' +
                ", source='" + source + '\'' +
                ", tempCustomerLeId='" + tempCustomerLeId + '\'' +
                '}';
    }
}
