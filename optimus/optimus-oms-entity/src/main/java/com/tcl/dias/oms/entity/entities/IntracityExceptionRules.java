package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This is the get API for intracity exception rules
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * @Author Chetan Chaudhary
 */
@Entity
@Table(name = "intracity_exceptions_rule")
@NamedQuery(name = "IntracityExceptionRules.findAll", query = "select  a from IntracityExceptionRules a")
public class IntracityExceptionRules implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "a_end")
    private String endA;

    @Column(name = "b_end")
    private String endB;

    @Column(name = "is_active")
    private String isActive;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getEndA() {
        return endA;
    }

    public void setEndA(String endA) {
        this.endA = endA;
    }

    public String getEndB() {
        return endB;
    }

    public void setEndB(String endB) {
        this.endB = endB;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "IntracityExceptionRules{" +
                "Id=" + Id +
                ", endA='" + endA + '\'' +
                ", endB='" + endB + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
