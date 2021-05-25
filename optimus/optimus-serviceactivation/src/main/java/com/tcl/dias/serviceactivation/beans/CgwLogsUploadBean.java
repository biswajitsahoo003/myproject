package com.tcl.dias.serviceactivation.beans;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class CgwLogsUploadBean extends TaskDetailsBaseBean {

    private List<AttachmentIdBean> cgwLogs;

    public List<AttachmentIdBean> getCgwLogs() {
        return cgwLogs;
    }

    public void setCgwLogs(List<AttachmentIdBean> cgwLogs) {
        this.cgwLogs = cgwLogs;
    }
}
