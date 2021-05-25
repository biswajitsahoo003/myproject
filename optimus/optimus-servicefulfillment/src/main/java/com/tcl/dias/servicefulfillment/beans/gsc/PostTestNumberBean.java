package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class PostTestNumberBean extends TaskDetailsBaseBean {
    private List<AttachmentIdBean> documentIds;
    private List<ScAssetBean> scAssets;

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public List<ScAssetBean> getScAssets() {
        return scAssets;
    }

    public void setScAssets(List<ScAssetBean> scAssets) {
        this.scAssets = scAssets;
    }
}
