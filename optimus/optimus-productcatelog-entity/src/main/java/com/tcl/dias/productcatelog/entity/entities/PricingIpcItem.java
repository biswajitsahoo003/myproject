package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pricing_ipc_items")
@NamedQuery(name = "PricingIpcItem.findAll", query = "SELECT p FROM PricingIpcItem p")
public class PricingIpcItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "uom")
	private String uom;

	@Column(name = "description")
	private String description;

	@Column(name = "is_volume_discount_applicable")
	private Byte isVolumeDiscountApplicable;

	@Column(name = "is_term_discount_applicable")
	private Byte isTermDiscountApplicable;

	public PricingIpcItem() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getIsVolumeDiscountApplicable() {
		return isVolumeDiscountApplicable;
	}

	public void setIsVolumeDiscountApplicable(Byte isVolumeDiscountApplicable) {
		this.isVolumeDiscountApplicable = isVolumeDiscountApplicable;
	}

	public Byte getIsTermDiscountApplicable() {
		return isTermDiscountApplicable;
	}

	public void setIsTermDiscountApplicable(Byte isTermDiscountApplicable) {
		this.isTermDiscountApplicable = isTermDiscountApplicable;
	}

	@Override
	public String toString() {
		return "PricingIpcItem [id=" + id + ", name=" + name + ", uom=" + uom + ", description=" + description
				+ ", isVolumeDiscountApplicable=" + isVolumeDiscountApplicable + ", isTermDiscountApplicable="
				+ isTermDiscountApplicable + "]";
	}
}
