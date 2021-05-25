package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity for cpe gsc selection rule
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name="vw_cpe_bom_powercable_gsc")
@IdClass(VwCpeBomPowerCableGscViewId.class)
public class VwCpeBomPowerCableGsc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="bom_name")
    private String bomName;

    @Column(name="product_code")
    private String productCode;

    @Column(name="product_description")
    private String productDescription;

    @Column(name="country_name")
    private String countryName;

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
