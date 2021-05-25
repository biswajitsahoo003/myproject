package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This file contains the OrderSiteCategory.java class.
 * 
 *
 * @author Santosh.Tidke
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "order_site_category")
public class OrderSiteCategory implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="erf_loc_siteb_location_id")
	private Integer erfLocSitebLocationId;
	
	@Column(name="site_category")
	private String siteCategory;

	@Column(name="order_id")
	private Integer orderId;

	public OrderSiteCategory() {
		super();
	}

	

	public OrderSiteCategory(Integer id, Integer erfLocSitebLocationId, String siteCategory, Integer orderId) {
		super();
		this.id = id;
		this.erfLocSitebLocationId = erfLocSitebLocationId;
		this.siteCategory = siteCategory;
		this.orderId = orderId;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfLocSitebLocationId() {
		return erfLocSitebLocationId;
	}

	public void setErfLocSitebLocationId(Integer erfLocSitebLocationId) {
		this.erfLocSitebLocationId = erfLocSitebLocationId;
	}

	public String getSiteCategory() {
		return siteCategory;
	}

	public void setSiteCategory(String siteCategory) {
		this.siteCategory = siteCategory;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "OrderSiteCategory [id=" + id + ", erfLocSitebLocationId=" + erfLocSitebLocationId + ", siteCategory="
				+ siteCategory + ", orderId=" + orderId + "]";
	}

}
