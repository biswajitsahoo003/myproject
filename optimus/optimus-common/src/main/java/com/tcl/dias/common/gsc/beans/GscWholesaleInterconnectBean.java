package com.tcl.dias.common.gsc.beans;


/**
 * Bean for gsc interconnect details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GscWholesaleInterconnectBean {

    private String orgNo;
    private Integer id;
    private String interconnectId;
    private String interconnectName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getInterconnectId() {
        return interconnectId;
    }

    public void setInterconnectId(String interconnectId) {
        this.interconnectId = interconnectId;
    }

    public String getInterconnectName() {
        return interconnectName;
    }

    public void setInterconnectName(String interconnectName) {
        this.interconnectName = interconnectName;
    }

    @Override
    public String toString() {
        return "GscWholesaleInterconnectBean{" +
                "orgNo='" + orgNo + '\'' +
                ", id='" + id + '\'' +
                ", interconnectId='" + interconnectId + '\'' +
                ", interconnectName='" + interconnectName + '\'' +
                '}';
    }
}
