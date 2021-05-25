package com.tcl.dias.productcatelog.entity.entities;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "vw_ias_DC_sam")
@NamedQuery(name = "ViewIasDataCenterSam.findAll", query = "SELECT p FROM ViewIasDataCenterSam p")
public class ViewIasDataCenterSam extends BaseEntity implements Serializable {
    @Column(name = "location_id")
    String locationId;
    @Column(name = "dc_type")
    String dcType;
    @Column(name = "floor_dtls")
    String floorDtls;
    @Column(name = "Towns_dtl")
    String townsDtl;
    @Column(name = "Region_dtl")
    String regionDtl;
    @Column(name = "address_txt")
    String addressTxt;
    @Column(name = "longitude_nbr")
    String longitudeNbr;
    @Column(name = "latitude_nbr")
    String latitudeNbr;
    @Column(name = "locality_dtl")
    String localityDtl;
    @Column(name = "pincode_nbr")
    String pincodeNbr;
    @Column(name = "is_active_ind")
    String isActive;
    @Column(name = "state_dtl")
    String stateDtl;
    @Column(name="erf_location_id")
    Integer erfLocId;


    public String getStateDtl() {
        return stateDtl;
    }
    public void setStateDtl(String stateDtl) {
        this.stateDtl = stateDtl;
    }
    public String getLocationId() {
        return locationId;
    }
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public String getDcType() {
        return dcType;
    }
    public void setDcType(String dcType) {
        this.dcType = dcType;
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
    public String getAddressTxt() {
        return addressTxt;
    }
    public void setAddressTxt(String addressTxt) {
        this.addressTxt = addressTxt;
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
    public String getLocalityDtl() {
        return localityDtl;
    }
    public void setLocalityDtl(String localityDtl) {
        this.localityDtl = localityDtl;
    }
    public String getPincodeNbr() {
        return pincodeNbr;
    }
    public void setPincodeNbr(String pincodeNbr) {
        this.pincodeNbr = pincodeNbr;
    }
    public String getIsActive() {
        return isActive;
    }
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    public String getFloorDtls() {
        return floorDtls;
    }
    public void setFloorDtls(String floorDtls) {
        this.floorDtls = floorDtls;
    }
    public Integer getErfLocId() { return erfLocId; }
    public void setErfLocId(Integer erfLocId) { this.erfLocId = erfLocId; }

}
