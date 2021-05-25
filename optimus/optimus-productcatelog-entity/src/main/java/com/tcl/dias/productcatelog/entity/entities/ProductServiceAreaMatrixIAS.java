package com.tcl.dias.productcatelog.entity.entities; 
import java.io.Serializable;
import javax.persistence.*;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;




/**
 * The persistent class for the product_service_area_matrix_IAS database table.
 * 
 */
@Entity
@Table(name="product_service_area_matrix_IAS")
@NamedQuery(name="ProductServiceAreaMatrixIAS.findAll", query="SELECT p FROM ProductServiceAreaMatrixIAS p")
public class ProductServiceAreaMatrixIAS extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	

	
	@Column(name="location_id")
	private Integer locationId;

	@Column(name="pop_sts")
	private String popSts;

	@Column(name="tier_cd_attr_val_id")
	private Integer tierId;
	
	

	public ProductServiceAreaMatrixIAS() {
		// TO DO
	}


	public Integer getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getPopSts() {
		return this.popSts;
	}

	public void setPopSts(String popSts) {
		this.popSts = popSts;
	}

	
	public Integer getTierId() {
		return tierId;
	}


	public void setTierId(Integer tierId) {
		this.tierId = tierId;
	}

	

}