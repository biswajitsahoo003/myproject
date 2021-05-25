package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class DidNumberTaskBean  extends TaskDetailsBaseBean {
    private String referenceNo;
    private List<AttachmentIdBean> documentIds;
    
    private List<OdrAssetBean> assets;
    
    
    


    /**
	 * @return the assets
	 */
	public List<OdrAssetBean> getAssets() {
		return assets;
	}

	/**
	 * @param assets the assets to set
	 */
	public void setAssets(List<OdrAssetBean> assets) {
		this.assets = assets;
	}

	public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
