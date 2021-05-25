package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity class
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_cloud_service_commercial_component")
@NamedQuery(name = "ScServiceCommericalComponent.findAll", query = "SELECT s FROM ScServiceCommericalComponent s")
public class ScServiceCommericalComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "parent_item")
	private Integer parentItem;

	// bi-directional many-to-one association to ScServiceDetail
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sc_product_detail_id")
    private ScProductDetail scProductDetail;

	@Column(name = "item_type")
	private String itemType;

	@Column(name = "item")
	private String item;

	private Double mrc;

	private Double nrc;

	private Double arc;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	public ScServiceCommericalComponent() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentItem() {
		return parentItem;
	}

	public void setParentItem(Integer parentItem) {
		this.parentItem = parentItem;
	}

	public ScProductDetail getScProductDetail() {
		return scProductDetail;
	}

	public void setScProductDetail(ScProductDetail scProductDetail) {
		this.scProductDetail = scProductDetail;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}