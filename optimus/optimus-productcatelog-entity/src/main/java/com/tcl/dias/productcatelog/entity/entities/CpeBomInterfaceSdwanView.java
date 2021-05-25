package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


/**
 * @author mpalanis
 *
 */

@Entity
@Table(name="vw_bom_phy_intf_sdwan")
@IdClass(CpeBomInterfaceSdwanView.class)
public class CpeBomInterfaceSdwanView implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="bom_name_cd")
	private String bomNameCd;
	
	@Column(name="physical_resource_cd")
	private String physicalResourceCd;
	
	@Column(name = "product_category")
	private String productCategory;
	
	@Column(name="interface_type")
	private String interfaceType;
	
	@Column(name="description")
	private String description;
	
	@Column(name="Provider")
	private String provider;
	
	@Column(name="cpe_model_end_of_sale")
	private String cpeModelEndOfSale;
	
	@Column(name="cpe_model_end_of_life")
	private String cpeModelEndOfLife;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getCpeModelEndOfSale() {
		return cpeModelEndOfSale;
	}

	public void setCpeModelEndOfSale(String cpeModelEndOfSale) {
		this.cpeModelEndOfSale = cpeModelEndOfSale;
	}

	public String getCpeModelEndOfLife() {
		return cpeModelEndOfLife;
	}

	public void setCpeModelEndOfLife(String cpeModelEndOfLife) {
		this.cpeModelEndOfLife = cpeModelEndOfLife;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBomNameCd() {
		return bomNameCd;
	}

	public void setBomNameCd(String bomNameCd) {
		this.bomNameCd = bomNameCd;
	}

	public String getPhysicalResourceCd() {
		return physicalResourceCd;
	}

	public void setPhysicalResourceCd(String physicalResourceCd) {
		this.physicalResourceCd = physicalResourceCd;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	
}
