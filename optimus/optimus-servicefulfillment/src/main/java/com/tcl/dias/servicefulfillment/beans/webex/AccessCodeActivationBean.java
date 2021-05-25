package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class AccessCodeActivationBean extends TaskDetailsBaseBean {
    String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
