package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class PoReleaseCpeInstallationBean extends TaskDetailsBaseBean {
    private String poReleaseCpeInstall;
    private String poReleaseCpeSupply;

    public String getPoReleaseCpeInstall() {
        return poReleaseCpeInstall;
    }

    public void setPoReleaseCpeInstall(String poReleaseCpeInstall) {
        this.poReleaseCpeInstall = poReleaseCpeInstall;
    }

    public String getPoReleaseCpeSupply() {
        return poReleaseCpeSupply;
    }

    public void setPoReleaseCpeSupply(String poReleaseCpeSupply) {
        this.poReleaseCpeSupply = poReleaseCpeSupply;
    }
}
