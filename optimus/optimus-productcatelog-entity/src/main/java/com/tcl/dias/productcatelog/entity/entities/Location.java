package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "location")
@NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l")
public class Location extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nm")
	private String name;
	
	@Column(name = "is_active_ind")
	private String isActive;
	
	@Column(name = "intnl_ctry_dial_cd")
	private String intlDialCode;

	/*// bi-directional many-to-one association to ComponentLocationAssoc
	@OneToMany(mappedBy = "location")
	private List<ComponentLocationAssoc> componentLocationAssocs;
*/
	// bi-directional many-to-one association to ProductLocationAssoc
	@OneToMany(mappedBy = "location")
	private List<ProductLocationAssoc> productLocationAssocs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public List<ComponentLocationAssoc> getComponentLocationAssocs() {
		return this.componentLocationAssocs;
	}

	public void setComponentLocationAssocs(List<ComponentLocationAssoc> componentLocationAssocs) {
		this.componentLocationAssocs = componentLocationAssocs;
	}

	public ComponentLocationAssoc addComponentLocationAssoc(ComponentLocationAssoc componentLocationAssoc) {
		getComponentLocationAssocs().add(componentLocationAssoc);
		componentLocationAssoc.setLocation(this);

		return componentLocationAssoc;
	}

	public ComponentLocationAssoc removeComponentLocationAssoc(ComponentLocationAssoc componentLocationAssoc) {
		getComponentLocationAssocs().remove(componentLocationAssoc);
		componentLocationAssoc.setLocation(null);

		return componentLocationAssoc;
	}*/

	public List<ProductLocationAssoc> getProductLocationAssocs() {
		return this.productLocationAssocs;
	}

	public void setProductLocationAssocs(List<ProductLocationAssoc> productLocationAssocs) {
		this.productLocationAssocs = productLocationAssocs;
	}

	public ProductLocationAssoc addProductLocationAssoc(ProductLocationAssoc productLocationAssoc) {
		getProductLocationAssocs().add(productLocationAssoc);
		productLocationAssoc.setLocation(this);

		return productLocationAssoc;
	}

	public ProductLocationAssoc removeProductLocationAssoc(ProductLocationAssoc productLocationAssoc) {
		getProductLocationAssocs().remove(productLocationAssoc);
		productLocationAssoc.setLocation(null);

		return productLocationAssoc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIntlDialCode() {
		return intlDialCode;
	}

	public void setIntlDialCode(String intlDialCode) {
		this.intlDialCode = intlDialCode;
	}

}