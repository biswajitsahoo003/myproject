package com.tcl.dias.servicefulfillmentutils.beans.ipcBeans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddonDetail {

    private String additionalVpnConnection;
    private String vdom;

    public String getAdditionalVpnConnection() {
        return additionalVpnConnection;
    }

    public void setAdditionalVpnConnection(String additionalVpnConnection) {
        this.additionalVpnConnection = additionalVpnConnection;
    }

    public String getVdom() {
        return vdom;
    }

    public void setVdom(String vdom) {
        this.vdom = vdom;
    }
}
