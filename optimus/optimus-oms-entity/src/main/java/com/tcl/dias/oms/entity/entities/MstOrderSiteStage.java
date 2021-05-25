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
@Table(name = "mst_order_site_stage")
@NamedQuery(name = "MstOrderSiteStage.findAll", query = "SELECT m FROM MstOrderSiteStage m")
public class MstOrderSiteStage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name = "is_active")
	private String isActive;

	private String name;

	// bi-directional many-to-one association to OrderIllSite
	@OneToMany(mappedBy = "mstOrderSiteStage")
	private Set<OrderIllSite> orderIllSites;

	// bi-directional many-to-one association to OrderSiteStageAudit
	@OneToMany(mappedBy = "mstOrderSiteStage")
	private Set<OrderSiteStageAudit> orderSiteStageAudits;

	public MstOrderSiteStage() {
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

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
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
		orderIllSite.setMstOrderSiteStage(this);

		return orderIllSite;
	}

	public OrderIllSite removeOrderIllSite(OrderIllSite orderIllSite) {
		getOrderIllSites().remove(orderIllSite);
		orderIllSite.setMstOrderSiteStage(null);

		return orderIllSite;
	}

	public Set<OrderSiteStageAudit> getOrderSiteStageAudits() {
		return this.orderSiteStageAudits;
	}

	public void setOrderSiteStageAudits(Set<OrderSiteStageAudit> orderSiteStageAudits) {
		this.orderSiteStageAudits = orderSiteStageAudits;
	}

	public OrderSiteStageAudit addOrderSiteStageAudit(OrderSiteStageAudit orderSiteStageAudit) {
		getOrderSiteStageAudits().add(orderSiteStageAudit);
		orderSiteStageAudit.setMstOrderSiteStage(this);

		return orderSiteStageAudit;
	}

	public OrderSiteStageAudit removeOrderSiteStageAudit(OrderSiteStageAudit orderSiteStageAudit) {
		getOrderSiteStageAudits().remove(orderSiteStageAudit);
		orderSiteStageAudit.setMstOrderSiteStage(null);

		return orderSiteStageAudit;
	}

}