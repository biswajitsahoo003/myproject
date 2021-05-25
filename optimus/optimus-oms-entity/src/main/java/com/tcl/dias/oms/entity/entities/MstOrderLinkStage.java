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
 * This file contains the MstOrderLinkStage.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_order_link_stage")
public class MstOrderLinkStage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name = "is_active")
	private String isActive;

	private String name;

	@OneToMany(mappedBy = "mstOrderLinkStage")
	private Set<OrderNplLink> orderNplLinks;

	@OneToMany(mappedBy = "mstOrderLinkStage")
	private Set<OrderLinkStageAudit> orderLinkStageAudits;

	public MstOrderLinkStage() {
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

	public Set<OrderNplLink> getOrderNplLinks() {
		return orderNplLinks;
	}

	public void setOrderNplLinks(Set<OrderNplLink> orderNplLinks) {
		this.orderNplLinks = orderNplLinks;
	}

	public Set<OrderLinkStageAudit> getOrderLinkStageAudits() {
		return orderLinkStageAudits;
	}

	public void setOrderLinkStageAudits(Set<OrderLinkStageAudit> orderLinkStageAudits) {
		this.orderLinkStageAudits = orderLinkStageAudits;
	}

}
