package com.tcl.dias.productcatelog.entity.entities;


import com.tcl.dias.productcatelog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the service_area_matrix_NPL database table.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "service_area_matrix_NPL")
@NamedQuery(name = "ServiceAreaMatrixNPL.findAll", query = "SELECT s FROM ServiceAreaMatrixNPL s")
public class ServiceAreaMatrixNPL extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "pop_location_id")
    private String popLocationId;

    @Column(name = "Towns_dtl")
    private String townsDtl;

    @Column(name = "Region_dtl")
    private String regionDtl;

    @Column(name = "pop_address_txt")
    private String popAddressTxt;

    @Column(name = "longitude_nbr")
    private String longitudeNbr;

    @Column(name = "latitude_nbr")
    private String latitudeNbr;

    public ServiceAreaMatrixNPL() {
        // do nothing
    }

    public String getPopLocationId() {
        return popLocationId;
    }

    public void setPopLocationId(String popLocationId) {
        this.popLocationId = popLocationId;
    }

    public String getTownsDtl() {
        return townsDtl;
    }

    public void setTownsDtl(String townsDtl) {
        this.townsDtl = townsDtl;
    }

    public String getRegionDtl() {
        return regionDtl;
    }

    public void setRegionDtl(String regionDtl) {
        this.regionDtl = regionDtl;
    }

    public String getPopAddressTxt() {
        return popAddressTxt;
    }

    public void setPopAddressTxt(String popAddressTxt) {
        this.popAddressTxt = popAddressTxt;
    }

    public String getLongitudeNbr() {
        return longitudeNbr;
    }

    public void setLongitudeNbr(String longitudeNbr) {
        this.longitudeNbr = longitudeNbr;
    }

    public String getLatitudeNbr() {
        return latitudeNbr;
    }

    public void setLatitudeNbr(String latitudeNbr) {
        this.latitudeNbr = latitudeNbr;
    }

}