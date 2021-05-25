package com.tcl.dias.customer.entity.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * Entity Class
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_dop_marker")
@NamedQuery(name = "MstDopMarker.findAll", query = "SELECT m FROM MstDopMarker m")
public class MstDopMarker implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "from_value")
    private Double fromValue;


    @Column(name = "to_value")
    private Double toValue;


    @Column(name = "dop_level")
    private String dopLevel;

    // bi-directional many-to-one association to MstCustomerSegment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_segment")
    private MstCustomerSegment customerSegment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Double getFromValue() {
        return fromValue;
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }

    public String getDopLevel() {
        return dopLevel;
    }

    public void setDopLevel(String dopLevel) {
        this.dopLevel = dopLevel;
    }

    public MstCustomerSegment getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(MstCustomerSegment customerSegment) {
        this.customerSegment = customerSegment;
    }
}
