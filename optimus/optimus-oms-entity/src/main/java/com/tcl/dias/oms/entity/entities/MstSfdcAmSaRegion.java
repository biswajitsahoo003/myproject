package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * Entity Class
 *
 *
 * @author Manisha Manojkumar
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Entity
@Table(name = "mst_sfdc_am_sa_region")
@NamedQuery(name = "MstSfdcAmSaRegion.findAll", query = "SELECT m FROM MstSfdcAmSaRegion m")
public class MstSfdcAmSaRegion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.DATE)
    private Date createdTime;

    @Column(name = "am_email")
    private String amEmail;

    @Column(name = "am_name")
    private String amName;

    @Column(name = "sales_admin_region")
    private String salesAdminRegion;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getAmEmail() {
        return amEmail;
    }

    public void setAmEmail(String amEmail) {
        this.amEmail = amEmail;
    }

    public String getAmName() {
        return amName;
    }

    public void setAmName(String amName) {
        this.amName = amName;
    }

    public String getSalesAdminRegion() {
        return salesAdminRegion;
    }

    public void setSalesAdminRegion(String salesAdminRegion) {
        this.salesAdminRegion = salesAdminRegion;
    }


}
