package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the si_asset_component database table.
 * 
 * @author Dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 * 
 */
@Entity
@Table(name = "si_asset_component")
@NamedQuery(name = "SIAssetComponent.findAll", query = "SELECT s FROM SIAssetComponent s")
public class SIAssetComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private double arc;

	private String item;

	private double mrc;

	private double nrc;

	// bi-directional many-to-one association to SiAssetCommercial
	@ManyToOne
	@JoinColumn(name = "si_asset_commercial_id")
	private SIAssetCommercial siAssetCommercial;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getArc() {
		return this.arc;
	}

	public void setArc(double arc) {
		this.arc = arc;
	}

	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public double getMrc() {
		return this.mrc;
	}

	public void setMrc(double mrc) {
		this.mrc = mrc;
	}

	public double getNrc() {
		return this.nrc;
	}

	public void setNrc(double nrc) {
		this.nrc = nrc;
	}

	public SIAssetCommercial getSiAssetCommercial() {
		return this.siAssetCommercial;
	}

	public void setSiAssetCommercial(SIAssetCommercial siAssetCommercial) {
		this.siAssetCommercial = siAssetCommercial;
	}

}