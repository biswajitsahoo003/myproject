package com.tcl.dias.productcatelog.entity.entities;
 import java.io.Serializable;
import javax.persistence.*;
import com.tcl.dias.productcatelog.entity.base.BaseEntity;
import java.util.List;


/**
 * @author Dinahar Vivekanandan
 * The persistent class for the sla_component_master database table.
 * 
 *
 */
@Entity
@Table(name="sla_component_master")
public class SlaComponentMaster extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name="long_desc")
	private String longDesc;

	private String nm;

	//bi-directional many-to-one association to ProductSlaComponentAssoc
	@OneToMany(mappedBy="slaComponentMaster")
	private List<ProductSlaComponentAssoc> productSlaComponentAssocs;

	
	//bi-directional many-to-one association to UomMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uom_id")
	private UomMaster uomMaster;

    public SlaComponentMaster() {
    	//TO DO
    }

	

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getNm() {
		return this.nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public List<ProductSlaComponentAssoc> getProductSlaComponentAssocs() {
		return this.productSlaComponentAssocs;
	}

	public void setProductSlaComponentAssocs(List<ProductSlaComponentAssoc> productSlaComponentAssocs) {
		this.productSlaComponentAssocs = productSlaComponentAssocs;
	}

	public UomMaster getUomMaster() {
		return this.uomMaster;
	}

	public void setUomMaster(UomMaster uomMaster) {
		this.uomMaster = uomMaster;
	}
	
}