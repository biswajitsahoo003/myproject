package com.tcl.dias.common.gsc.beans;

/**
 * Bean for gsc service bean for multi macd
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GscMultiMacdServiceBean {

    private String id;
    private String tclSwitch;
    private String ipAddress;
    private String circuitID;
    private String sipTrunkGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTclSwitch() {
        return tclSwitch;
    }

    public void setTclSwitch(String tclSwitch) {
        this.tclSwitch = tclSwitch;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCircuitID() {
        return circuitID;
    }

    public void setCircuitID(String circuitID) {
        this.circuitID = circuitID;
    }

    public String getSipTrunkGroup() {
        return sipTrunkGroup;
    }

    public void setSipTrunkGroup(String sipTrunkGroup) {
        this.sipTrunkGroup = sipTrunkGroup;
    }
}
