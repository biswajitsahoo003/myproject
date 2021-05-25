package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the mst_mf_prefeasible_bw database table.
 * 
 */
@Entity
@Table(name="mst_mf_prefeasible_bw")
@NamedQuery(name="MstMfPrefeasibleBw.findAll", query="SELECT m FROM MstMfPrefeasibleBw m")
public class MstMfPrefeasibleBw implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="location")
	private String location;

	@Column(name="pre_feasible_bw_macd")
	private Integer preFeasibleBwMacd;

	@Column(name="pre_feasible_bw_new")
	private Integer preFeasibleBwNew;

	@Column(name="product")
	private String product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getPreFeasibleBwMacd() {
		return preFeasibleBwMacd;
	}

	public void setPreFeasibleBwMacd(Integer preFeasibleBwMacd) {
		this.preFeasibleBwMacd = preFeasibleBwMacd;
	}

	public int getPreFeasibleBwNew() {
		return preFeasibleBwNew;
	}

	public void setPreFeasibleBwNew(Integer preFeasibleBwNew) {
		this.preFeasibleBwNew = preFeasibleBwNew;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
