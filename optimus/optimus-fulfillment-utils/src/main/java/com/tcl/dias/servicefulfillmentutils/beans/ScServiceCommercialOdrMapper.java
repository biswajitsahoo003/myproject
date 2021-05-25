package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.common.fulfillment.beans.OdrCommercialBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;

public class ScServiceCommercialOdrMapper {
	private ScServiceCommercial scServiceCommercial;
	private OdrCommercialBean odrCommercialBean;
	public ScServiceCommercial getScServiceCommercial() {
		return scServiceCommercial;
	}
	public void setScServiceCommercial(ScServiceCommercial scServiceCommercial) {
		this.scServiceCommercial = scServiceCommercial;
	}
	public OdrCommercialBean getOdrCommercialBean() {
		return odrCommercialBean;
	}
	public void setOdrCommercialBean(OdrCommercialBean odrCommercialBean) {
		this.odrCommercialBean = odrCommercialBean;
	}
	
}
