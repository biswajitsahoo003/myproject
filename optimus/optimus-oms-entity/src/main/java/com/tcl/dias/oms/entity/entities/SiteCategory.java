package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * This file contains the SiteCategory.java class.
 * 
 *
 * @author Steffi.Das
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "site_category")
public class SiteCategory implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="erf_loc_siteb_location_id")
	private Integer erfLocSitebLocationId;
	
	@Column(name="site_category")
	private String siteCategory;

	@Column(name="quote_id")
	private Integer quoteId;
	
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

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	
	
	
}
