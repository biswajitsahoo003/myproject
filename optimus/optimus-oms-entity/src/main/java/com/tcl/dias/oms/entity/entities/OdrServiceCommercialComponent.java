package com.tcl.dias.oms.entity.entities;

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
 * This class contains entity of odr_service_commerical_component table that will used with IPC.
 * 
 *
 * @author SELVAKUMAR PALANIANDY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "odr_cloud_service_commercial_component")
@NamedQuery(name = "OdrServiceCommercialComponent.findAll", query = "SELECT o FROM OdrServiceCommercialComponent o")
public class OdrServiceCommercialComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "parent_item")
    private Integer parentItem;

    // bi-directional many-to-one association to OdrServiceDetail
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "odr_product_detail_id")
    private OdrProductDetail odrProductDetail;

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

	public OdrServiceCommercialComponent() {
	}

	public Integer getId() {
		return this.id;
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
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public OdrProductDetail getOdrProductDetail() {
		return this.odrProductDetail;
	}

	public void setOdrProductDetail(OdrProductDetail odrProductDetail) {
		this.odrProductDetail = odrProductDetail;
	}
}