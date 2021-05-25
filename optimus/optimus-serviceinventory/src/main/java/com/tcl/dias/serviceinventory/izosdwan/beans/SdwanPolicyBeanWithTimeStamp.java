package com.tcl.dias.serviceinventory.izosdwan.beans;

import java.util.List;

import com.tcl.dias.serviceinventory.beans.SdwanPolicyListBean;

/**
 * Bean for storing sdwan policies with Date and timestamp
 * 
 * @author Kishore Nagarajan
 */
public class SdwanPolicyBeanWithTimeStamp {

	private List<SdwanPolicyListBean> sdwanPolicyBean;
	private String lastUpdated;

	public List<SdwanPolicyListBean> getSdwanPolicyBean() {
		return sdwanPolicyBean;
	}

	public void setSdwanPolicyBean(List<SdwanPolicyListBean> sdwanPolicyBean) {
		this.sdwanPolicyBean = sdwanPolicyBean;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "SdwanPolicyBeanWithTimeStamp [sdwanPolicyBean=" + sdwanPolicyBean + ", lastUpdated=" + lastUpdated
				+ "]";
	}

}
