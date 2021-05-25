package com.tcl.dias.customer.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 *
 * Entity Class
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_customer_segment")
@NamedQuery(name = "MstCustomerSegment.findAll", query = "SELECT m FROM MstCustomerSegment m")
public class MstCustomerSegment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    // bi-directional many-to-one association to Customer
    @OneToMany(mappedBy = "customerSegment")
    private Set<MstDopMatrix> mstDopMatrices;

    // bi-directional many-to-one association to Customer
    @OneToMany(mappedBy = "customerSegment")
    private Set<MstDopMarker> mstDopMarkers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

    public Set<MstDopMatrix> getMstDopMatrices() {
        return mstDopMatrices;
    }

    public void setMstDopMatrices(Set<MstDopMatrix> mstDopMatrices) {
        this.mstDopMatrices = mstDopMatrices;
    }

    public Set<MstDopMarker> getMstDopMarkers() {
        return mstDopMarkers;
    }

    public void setMstDopMarkers(Set<MstDopMarker> mstDopMarkers) {
        this.mstDopMarkers = mstDopMarkers;
    }
}
