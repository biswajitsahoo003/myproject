package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sla_spec_attr_value_group database table.
 * 
 */
/**
 * @author Dinahar Vivekanandan
 *
 */
@Entity
@Table(name="sla_spec_attr_value_group")
public class SlaSpecAttrValueGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="created_by")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to AttributeValue
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="attr_val_id")
	private AttributeValue attributeValue;

	//bi-directional many-to-one association to ProductSlaValueSpec
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_id")
	private ProductSlaValueSpec productSlaValueSpec;

    public SlaSpecAttrValueGroup() {
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

	public AttributeValue getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	public ProductSlaValueSpec getProductSlaValueSpec() {
		return this.productSlaValueSpec;
	}

	public void setProductSlaValueSpec(ProductSlaValueSpec productSlaValueSpec) {
		this.productSlaValueSpec = productSlaValueSpec;
	}
	
}