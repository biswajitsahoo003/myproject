package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "product_component_assoc")
@NamedQuery(name = "ProductComponentAssoc.findAll", query = "SELECT p FROM ProductComponentAssoc p")
public class ProductComponentAssoc extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name = "is_advanced_ind")
	private String isAdvanced;

	@Column(name = "is_backup_ind")
	private String isBackup;

	// bi-directional many-to-one association to ProductFamilyComponentAssoc
	@ManyToOne
	@JoinColumn(name = "component_id")
	private Component component;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	//bi-directional many-to-one association to ProductServiceComponentAssoc
		@OneToMany(mappedBy="productComponentAssoc")
		private Set<ProductServiceComponentAssoc> productServiceComponentAssocs;


	public ProductComponentAssoc() {
		//default constructor
	}

	
	public String getIsAdvanced() {
		return isAdvanced;
	}

	public void setIsAdvanced(String isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public String getIsBackup() {
		return isBackup;
	}

	public void setIsBackup(String isBackup) {
		this.isBackup = isBackup;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	public Set<ProductServiceComponentAssoc> getProductServiceComponentAssocs() {
		return this.productServiceComponentAssocs;
	}

	public void setProductServiceComponentAssocs(Set<ProductServiceComponentAssoc> productServiceComponentAssocs) {
		this.productServiceComponentAssocs = productServiceComponentAssocs;
	}
}