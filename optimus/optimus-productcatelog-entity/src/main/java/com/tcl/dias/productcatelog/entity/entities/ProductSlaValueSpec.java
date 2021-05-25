package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the product_sla_value_spec database table.
 * 
 */
/**
 * @author Dinahar Vivekanandan
 *
 */
@Entity
@Table(name="product_sla_value_spec")
public class ProductSlaValueSpec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="created_by")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="group_desc")
	private String groupDesc;

	@Column(name="sla_value")
	private String slaValue;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;


	//bi-directional many-to-one association to ProductSlaComponentAssoc
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="psca_id")
	private ProductSlaComponentAssoc productSlaComponentAssoc;

	//bi-directional many-to-one association to SlaSpecAttrValueGroup
	@OneToMany(mappedBy="productSlaValueSpec")
	private List<SlaSpecAttrValueGroup> slaSpecAttrValueGroups;

    public ProductSlaValueSpec() {
    	// TO DO

    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getGroupDesc() {
		return this.groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getSlaValue() {
		return this.slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	
	public ProductSlaComponentAssoc getProductSlaComponentAssoc() {
		return this.productSlaComponentAssoc;
	}

	public void setProductSlaComponentAssoc(ProductSlaComponentAssoc productSlaComponentAssoc) {
		this.productSlaComponentAssoc = productSlaComponentAssoc;
	}
	
	public List<SlaSpecAttrValueGroup> getSlaSpecAttrValueGroups() {
		return this.slaSpecAttrValueGroups;
	}

	public void setSlaSpecAttrValueGroups(List<SlaSpecAttrValueGroup> slaSpecAttrValueGroups) {
		this.slaSpecAttrValueGroups = slaSpecAttrValueGroups;
	}
	
}