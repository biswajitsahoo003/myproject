package com.tcl.dias.oms.entity.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This file contains the MstOrderLinkStatus.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_order_link_status")
public class MstOrderLinkStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name = "is_active")
	private Byte isActive;

	private String name;

	// bi-directional many-to-one association to OrderIllSite
	@OneToMany(mappedBy = "mstOrderLinkStatus")
	private Set<OrderNplLink> orderNplLink;

	// bi-directional many-to-one association to OrderSiteStatusAudit
	@OneToMany(mappedBy = "mstOrderLinkStatus")
	private Set<OrderLinkStatusAudit> orderLinkStatusAudits;

	public MstOrderLinkStatus() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<OrderNplLink> getOrderNplLink() {
		return orderNplLink;
	}

	public void setOrderNplLink(Set<OrderNplLink> orderNplLink) {
		this.orderNplLink = orderNplLink;
	}

	public Set<OrderLinkStatusAudit> getOrderLinkStatusAudits() {
		return orderLinkStatusAudits;
	}

	public void setOrderLinkStatusAudits(Set<OrderLinkStatusAudit> orderLinkStatusAudits) {
		this.orderLinkStatusAudits = orderLinkStatusAudits;
	}

}
