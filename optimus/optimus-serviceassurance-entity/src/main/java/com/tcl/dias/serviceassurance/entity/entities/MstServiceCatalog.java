package com.tcl.dias.serviceassurance.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

/**
 * 
 * Entity Class
 * 
 *The persistent class for the mst_service_catalog database table.
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="mst_service_catalog")
@NamedQuery(name="MstServiceCatalog.findAll", query="SELECT n FROM MstServiceCatalog n")
public class MstServiceCatalog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="parent_id")
	private Integer parentId;
	
	private String  name;
	@Column(name="display_name")
	private String  displayName;
	
	private String  code;
	
	@Column(name="is_active")
	private String  isActive;
	
	@OneToMany(mappedBy = "mstServiceCatalog")
	private Set<ProductToServiceCatalog> productToServiceCatalog;
	
	public Set<ProductToServiceCatalog> getProductToServiceCatalog() {
		return productToServiceCatalog;
	}

	public void setProductToServiceCatalog(Set<ProductToServiceCatalog> productToServiceCatalog) {
		this.productToServiceCatalog = productToServiceCatalog;
	}

	public MstServiceCatalog() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}