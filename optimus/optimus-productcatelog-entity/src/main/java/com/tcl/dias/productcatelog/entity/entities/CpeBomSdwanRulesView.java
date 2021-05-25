package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

/**
 * The persistent class for the vw_sdwan_bom_rules database table.
 * 
 * @author vpachava
 *
 */

@Entity
@Table(name = "vw_sdwan_bom_rules")
//@IdClass(CpeBomSdwanRulesView.class)
public class CpeBomSdwanRulesView implements Serializable {

	private static final long serialVersionUID = 7759721446807866120L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "vendor_name")
	private String vendorName;

	@Column(name = "bom_cd")
	private String bomCd;

	@Column(name = "addon_cd")
	private String addonCd;

	@Column(name = "lic_cd")
	private String licCd;

	@Column(name = "maxbw")
	private Float maxbw;

	@Column(name = "maxbw_uom")
	private String maxbwUom;

	@Column(name = "max_l2_ports")
	private Integer maxL2Ports;

	@Column(name = "max_l3_ports")
	private Integer maxL3Ports;

	@Column(name = "cpe_priority")
	private Integer cpePriority;

	@Column(name = "max_L3_Cu_ports")
	private Integer maxL3CuPorts;

	@Column(name = "max_L3_Fi_ports")
	private Integer maxL3FiPorts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getBomCd() {
		return bomCd;
	}

	public void setBomCd(String bomCd) {
		this.bomCd = bomCd;
	}

	public String getAddonCd() {
		return addonCd;
	}

	public void setAddonCd(String addonCd) {
		this.addonCd = addonCd;
	}

	public String getLicCd() {
		return licCd;
	}

	public void setLicCd(String licCd) {
		this.licCd = licCd;
	}

	public Float getMaxbw() {
		return maxbw;
	}

	public void setMaxbw(Float maxbw) {
		this.maxbw = maxbw;
	}

	public String getMaxbwUom() {
		return maxbwUom;
	}

	public void setMaxbwUom(String maxbwUom) {
		this.maxbwUom = maxbwUom;
	}

	public Integer getMaxL2Ports() {
		return maxL2Ports;
	}

	public void setMaxL2Ports(Integer maxL2Ports) {
		this.maxL2Ports = maxL2Ports;
	}

	public Integer getMaxL3Ports() {
		return maxL3Ports;
	}

	public void setMaxL3Ports(Integer maxL3Ports) {
		this.maxL3Ports = maxL3Ports;
	}

	public Integer getCpePriority() {
		return cpePriority;
	}

	public void setCpePriority(Integer cpePriority) {
		this.cpePriority = cpePriority;
	}

	public Integer getMaxL3Cu() {
		return maxL3CuPorts;
	}

	public void setMaxL3Cu(Integer maxL3Cu) {
		this.maxL3CuPorts = maxL3Cu;
	}

	public Integer getMaxL3Fi() {
		return maxL3FiPorts;
	}

	public void setMaxL3Fi(Integer maxL3Fi) {
		this.maxL3FiPorts = maxL3Fi;
	}

}
