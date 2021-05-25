package com.tcl.dias.serviceactivation.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * SSDumpDetails Entity Class
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "rf_test_results")
@NamedQuery(name = "RfTestResult.findAll", query = "SELECT s FROM RfTestResult s")
public class RfTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "circuit_id")
    private String circuitId;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "ping_test_detail")
    private String pingTestDetail;

    @Column(name = "ss_dump_detail")
    private String ssDumpDetail;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPingTestDetail() {
        return pingTestDetail;
    }

    public void setPingTestDetail(String pingTestDetail) {
        this.pingTestDetail = pingTestDetail;
    }

    public String getSsDumpDetail() {
        return ssDumpDetail;
    }

    public void setSsDumpDetail(String ssDumpDetail) {
        this.ssDumpDetail = ssDumpDetail;
    }
}
