package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the si_asset_commercial database table.
 *
 * @author Dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "si_asset_commercial")
@NamedQuery(name = "SIAssetCommercial.findAll", query = "SELECT s FROM SIAssetCommercial s")
public class SIAssetCommercial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private double arc;

	private double mrc;

	private double nrc;

	@Column(name = "ppu_rate")
	private Double ppuRate;

	// bi-directional many-to-one association to SiAsset
	@ManyToOne
	@JoinColumn(name = "si_asset_id")
	private SIAsset siAsset;

	// bi-directional many-to-one association to SiAssetComponent
	@OneToMany(mappedBy = "siAssetCommercial", cascade = CascadeType.ALL)
	private List<SIAssetComponent> siAssetComponents;

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

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

	public SIAsset getSIAsset() {
		return this.siAsset;
	}

	public void setSIAsset(SIAsset siAsset) {
		this.siAsset = siAsset;
	}

	public List<SIAssetComponent> getSiAssetComponents() {
		return this.siAssetComponents;
	}

	public void setSiAssetComponents(List<SIAssetComponent> siAssetComponents) {
		this.siAssetComponents = siAssetComponents;
	}

}