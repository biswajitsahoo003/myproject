package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "component_master")
@NamedQuery(name = "Component.findAll", query = "SELECT c FROM Component c")
public class Component extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	// bi-directional many-to-one association to Component
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Component parentComponent;

	

	/*// bi-directional many-to-one association to ComponentLocationAssoc
	@OneToMany(mappedBy = "component")
	private List<ComponentLocationAssoc> componentLocationAssocs;

	// bi-directional many-to-one association to ProductComponentDiscount
	@OneToMany(mappedBy = "component")
	private List<ProductComponentDiscount> productComponentDiscounts;*/

	
	// bi-directional many-to-one association to ProviderComponentAssoc
	@OneToMany(mappedBy = "component")
	private List<ProviderComponentAssoc> providerComponentAssocs;

	@OneToMany(mappedBy = "component")
	private List<ComponentAttributeGroupAssoc> componentAttributeGroupAssocs;

	// bi-directional many-to-one association to ProviderComponentAssoc
	@OneToMany(mappedBy = "component")
	private List<ProductComponentAssoc> prodCompAssocs;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Component getParentComponent() {
		return parentComponent;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentComponent = parentComponent;
	}


	/*public List<ComponentLocationAssoc> getComponentLocationAssocs() {
		return this.componentLocationAssocs;
	}

	public void setComponentLocationAssocs(List<ComponentLocationAssoc> componentLocationAssocs) {
		this.componentLocationAssocs = componentLocationAssocs;
	}

	public ComponentLocationAssoc addComponentLocationAssoc(ComponentLocationAssoc componentLocationAssoc) {
		getComponentLocationAssocs().add(componentLocationAssoc);
		componentLocationAssoc.setComponent(this);

		return componentLocationAssoc;
	}

	public ComponentLocationAssoc removeComponentLocationAssoc(ComponentLocationAssoc componentLocationAssoc) {
		getComponentLocationAssocs().remove(componentLocationAssoc);
		componentLocationAssoc.setComponent(null);

		return componentLocationAssoc;
	}

	public List<ProductComponentDiscount> getProductComponentDiscounts() {
		return this.productComponentDiscounts;
	}

	public void setProductComponentDiscounts(List<ProductComponentDiscount> productComponentDiscounts) {
		this.productComponentDiscounts = productComponentDiscounts;
	}

	public ProductComponentDiscount addProductComponentDiscount(ProductComponentDiscount productComponentDiscount) {
		getProductComponentDiscounts().add(productComponentDiscount);
		productComponentDiscount.setComponent(this);

		return productComponentDiscount;
	}

	public ProductComponentDiscount removeProductComponentDiscount(ProductComponentDiscount productComponentDiscount) {
		getProductComponentDiscounts().remove(productComponentDiscount);
		productComponentDiscount.setComponent(null);

		return productComponentDiscount;
	}*/

	

	public List<ProviderComponentAssoc> getProviderComponentAssocs() {
		return this.providerComponentAssocs;
	}

	public void setProviderComponentAssocs(List<ProviderComponentAssoc> providerComponentAssocs) {
		this.providerComponentAssocs = providerComponentAssocs;
	}

	public ProviderComponentAssoc addProviderComponentAssoc(ProviderComponentAssoc providerComponentAssoc) {
		getProviderComponentAssocs().add(providerComponentAssoc);
		providerComponentAssoc.setComponent(this);

		return providerComponentAssoc;
	}

	public ProviderComponentAssoc removeProviderComponentAssoc(ProviderComponentAssoc providerComponentAssoc) {
		getProviderComponentAssocs().remove(providerComponentAssoc);
		providerComponentAssoc.setComponent(null);

		return providerComponentAssoc;
	}

	

	public List<ComponentAttributeGroupAssoc> getComponentAttributeGroupAssocs() {
		return componentAttributeGroupAssocs;
	}

	public void setComponentAttributeGroupAssocs(List<ComponentAttributeGroupAssoc> componentAttributeGroupAssocs) {
		this.componentAttributeGroupAssocs = componentAttributeGroupAssocs;
	}

	public List<ProductComponentAssoc> getProdCompAssocs() {
		return prodCompAssocs;
	}

	public void setProdCompAssocs(List<ProductComponentAssoc> prodCompAssocs) {
		this.prodCompAssocs = prodCompAssocs;
	}

}