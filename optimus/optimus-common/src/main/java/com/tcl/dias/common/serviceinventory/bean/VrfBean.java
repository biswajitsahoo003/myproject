package com.tcl.dias.common.serviceinventory.bean;


/**
 * This class is used to send Vrf Attributes to Service Inventory
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class VrfBean {

    private String masterVrfServiceId;
    private Boolean isMultiVrf;

    public String getMasterVrfServiceId() {
        return masterVrfServiceId;
    }

    public void setMasterVrfServiceId(String masterVrfServiceId) {
        this.masterVrfServiceId = masterVrfServiceId;
    }

    public Boolean getMultiVrf() {
        return isMultiVrf;
    }

    public void setMultiVrf(Boolean multiVrf) {
        isMultiVrf = multiVrf;
    }
}
