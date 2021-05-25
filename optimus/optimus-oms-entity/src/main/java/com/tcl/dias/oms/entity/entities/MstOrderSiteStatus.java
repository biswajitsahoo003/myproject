package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_order_site_status")
@NamedQuery(name = "MstOrderSiteStatus.findAll", query = "SELECT m FROM MstOrderSiteStatus m")
public class MstOrderSiteStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name = "is_active")
	private Byte isActive;

	private String name;

	// bi-directional many-to-one association to OrderIllSite
	@OneToMany(mappedBy = "mstOrderSiteStatus")
	private Set<OrderIllSite> orderIllSites;

	// bi-directional many-to-one association to OrderSiteStatusAudit
	@OneToMany(mappedBy = "mstOrderSiteStatus")
	private Set<OrderSiteStatusAudit> orderSiteStatusAudits;

	public MstOrderSiteStatus() {
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

	public Set<OrderIllSite> getOrderIllSites() {
		return this.orderIllSites;
	}

	public void setOrderIllSites(Set<OrderIllSite> orderIllSites) {
		this.orderIllSites = orderIllSites;
	}

	public OrderIllSite addOrderIllSite(OrderIllSite orderIllSite) {
		getOrderIllSites().add(orderIllSite);
		orderIllSite.setMstOrderSiteStatus(this);

		return orderIllSite;
	}

	public OrderIllSite removeOrderIllSite(OrderIllSite orderIllSite) {
		getOrderIllSites().remove(orderIllSite);
		orderIllSite.setMstOrderSiteStatus(null);

		return orderIllSite;
	}

	public Set<OrderSiteStatusAudit> getOrderSiteStatusAudits() {
		return this.orderSiteStatusAudits;
	}

	public void setOrderSiteStatusAudits(Set<OrderSiteStatusAudit> orderSiteStatusAudits) {
		this.orderSiteStatusAudits = orderSiteStatusAudits;
	}

	public OrderSiteStatusAudit addOrderSiteStatusAudit(OrderSiteStatusAudit orderSiteStatusAudit) {
		getOrderSiteStatusAudits().add(orderSiteStatusAudit);
		orderSiteStatusAudit.setMstOrderSiteStatus(this);

		return orderSiteStatusAudit;
	}

	public OrderSiteStatusAudit removeOrderSiteStatusAudit(OrderSiteStatusAudit orderSiteStatusAudit) {
		getOrderSiteStatusAudits().remove(orderSiteStatusAudit);
		orderSiteStatusAudit.setMstOrderSiteStatus(null);

		return orderSiteStatusAudit;
	}

}