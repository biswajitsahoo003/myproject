package com.tcl.dias.servicefulfillmentutils.beans.ipcBeans;

public class VMDetail {

    private String compute;
    private String iaas;
    private String os;

    public String getCompute() {
        return compute;
    }

    public void setCompute(String compute) {
        this.compute = compute;
    }

    public String getIaas() {
        return iaas;
    }

    public void setIaas(String iaas) {
        this.iaas = iaas;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

	@Override
	public String toString() {
		return "VMDetail [compute=" + compute + ", iaas=" + iaas + ", os=" + os + "]";
	}
    
    
}
