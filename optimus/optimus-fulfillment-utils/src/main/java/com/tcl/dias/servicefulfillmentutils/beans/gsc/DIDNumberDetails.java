package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.tcl.dias.common.fulfillment.beans.OdrAssetBean;

public class DIDNumberDetails {

	private List<DIDSupplierBean> supplier;

	private List<OdrAssetBean> assets;

	private List<DocumentBean> documentBeans;
	
	private String errorDetailsBean;

	private String cmsId;
	private String secsId;

	public List<DIDSupplierBean> getSupplier() {
		return supplier;
	}

	public void setSupplier(List<DIDSupplierBean> supplier) {
		this.supplier = supplier;
	}

	public List<OdrAssetBean> getAssets() {
		return assets;
	}

	public void setAssets(List<OdrAssetBean> assets) {
		this.assets = assets;
	}

	public List<DocumentBean> getDocumentBeans() {
		return documentBeans;
	}

	public void setDocumentBeans(List<DocumentBean> documentBeans) {
		this.documentBeans = documentBeans;
	}

	public String getCmsId() {
		return cmsId;
	}

	public void setCmsId(String cmsId) {
		this.cmsId = cmsId;
	}

	public String getSecsId() {
		return secsId;
	}

	public void setSecsId(String secsId) {
		this.secsId = secsId;
	}

	public String getErrorDetailsBean() {
		return errorDetailsBean;
	}

	public void setErrorDetailsBean(String errorDetailsBean) {
		this.errorDetailsBean = errorDetailsBean;
	}
	
	

}
