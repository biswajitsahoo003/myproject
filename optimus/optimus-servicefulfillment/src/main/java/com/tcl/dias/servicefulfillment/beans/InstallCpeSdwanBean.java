package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.CpeInstallationBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import java.util.List;

public class InstallCpeSdwanBean extends TaskDetailsBaseBean {

    List<CpeInstallationBean> cpeInstallationDetails;

    public List<CpeInstallationBean> getCpeInstallationDetails() {
        return cpeInstallationDetails;
    }

    public void setCpeInstallationDetails(List<CpeInstallationBean> cpeInstallationDetails) {
        this.cpeInstallationDetails = cpeInstallationDetails;
    }
}
