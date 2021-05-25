package com.tcl.dias.serviceinventory.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the gsc_interconnect_dtls database table.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "gsc_interconnect_dtls")
public class GscInterconnectDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "org_no")
    private Integer orgNo;

    @Column(name = "serv_type")
    private String serviceType;

    @Column(name = "interconnect_id")
    private Integer interconnectId;

    @Column(name = "interconnect_name")
    private String interconnectName;

    @Column(name = "interconnect_type")
    private String interconnectType;

    @Column(name = "partner_org_no")
    private Integer partnerOrgNo;

    public GscInterconnectDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(Integer orgNo) {
        this.orgNo = orgNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getInterconnectId() {
        return interconnectId;
    }

    public void setInterconnectId(Integer interconnectId) {
        this.interconnectId = interconnectId;
    }

    public String getInterconnectName() {
        return interconnectName;
    }

    public void setInterconnectName(String interconnectName) {
        this.interconnectName = interconnectName;
    }

    public String getInterconnectType() {
        return interconnectType;
    }

    public void setInterconnectType(String interconnectType) {
        this.interconnectType = interconnectType;
    }

    public Integer getPartnerOrgNo() {
        return partnerOrgNo;
    }

    public void setPartnerOrgNo(Integer partnerOrgNo) {
        this.partnerOrgNo = partnerOrgNo;
    }
}
