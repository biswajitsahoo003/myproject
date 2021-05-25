package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class ReserveInputBodyBean {
    private String originCountryCode;
    private String serviceType;
    private List<String> e164;

    public String getOriginCountryCode() {
        return originCountryCode;
    }

    public void setOriginCountryCode(String originCountryCode) {
        this.originCountryCode = originCountryCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<String> getE164() {
        return e164;
    }

    public void setE164(List<String> e164) {
        this.e164 = e164;
    }

	@Override
	public String toString() {
		return "ReserveInputBodyBean [originCountryCode=" + originCountryCode + ", serviceType=" + serviceType
				+ ", e164=" + e164 + "]";
	}
}
