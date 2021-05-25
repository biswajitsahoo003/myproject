package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the product_sla_component_assoc database table.
 * 
 */



/**
 * @author Dinahar Vivekanandan
 *
 */
@Entity
@Table(name="product_sla_component_assoc")
public class ProductSlaComponentAssoc implements Serializable {
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

	//bi-directional many-to-one association to SlaComponentMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sla_cm_id")
	private SlaComponentMaster slaComponentMaster;

	//bi-directional many-to-one association to ProductOfferingServiceLevelAssoc
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="posl_id")
	private ProductOfferingServiceLevelAssoc productOfferingServiceLevelAssoc;

	//bi-directional many-to-one association to ProductSlaValueSpec
	@OneToMany(mappedBy="productSlaComponentAssoc")
	private List<ProductSlaValueSpec> productSlaValueSpecs;

    public ProductSlaComponentAssoc() {
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

	public SlaComponentMaster getSlaComponentMaster() {
		return this.slaComponentMaster;
	}

	public void setSlaComponentMaster(SlaComponentMaster slaComponentMaster) {
		this.slaComponentMaster = slaComponentMaster;
	}
	
	public ProductOfferingServiceLevelAssoc getProductOfferingServiceLevelAssoc() {
		return this.productOfferingServiceLevelAssoc;
	}

	public void setProductOfferingServiceLevelAssoc(ProductOfferingServiceLevelAssoc productOfferingServiceLevelAssoc) {
		this.productOfferingServiceLevelAssoc = productOfferingServiceLevelAssoc;
	}
	
	public List<ProductSlaValueSpec> getProductSlaValueSpecs() {
		return this.productSlaValueSpecs;
	}

	public void setProductSlaValueSpecs(List<ProductSlaValueSpec> productSlaValueSpecs) {
		this.productSlaValueSpecs = productSlaValueSpecs;
	}
	
}