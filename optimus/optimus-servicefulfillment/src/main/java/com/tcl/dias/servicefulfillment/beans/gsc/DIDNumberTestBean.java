package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class DIDNumberTestBean  extends TaskDetailsBaseBean  {

	private List<AttachmentIdBean> documentIds;

	private List<OdrAssetBean> assets;

	public List<OdrAssetBean> getAssets() {
		return assets;
	}

	public void setAssets(List<OdrAssetBean> assets) {
		this.assets = assets;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

}
