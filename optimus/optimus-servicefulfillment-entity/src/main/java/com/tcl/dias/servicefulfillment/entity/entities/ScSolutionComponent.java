package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This file contains the ScSolutionComponent.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="sc_solution_components")
@NamedQuery(name="ScSolutionComponent.findAll", query="SELECT s FROM ScSolutionComponent s")
public class ScSolutionComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="component_group")
	private String componentGroup;

	@Column(name="cpe_component_id")
	private Integer cpeComponentId;

	@Column(name="is_active")
	private String isActive;
	
	private String priority;

	@Column(name="order_code")
	private String orderCode;

	@Column(name="parent_service_code")
	private String parentServiceCode;

	@Column(name="solution_code")
	private String solutionCode;
	
	@Column(name="o2c_triggered_status")
	private String o2cTriggeredStatus;

	//bi-directional many-to-one association to ScOrder
	@ManyToOne
	@JoinColumn(name="sc_order_id")
	private ScOrder scOrder;
	
	@Column(name="service_code")
	private String serviceCode;

	//bi-directional many-to-one association to ScServiceDetail
	@ManyToOne
	@JoinColumn(name="sc_service_detail_id")
	private ScServiceDetail scServiceDetail1;

	//bi-directional many-to-one association to ScServiceDetail
	@ManyToOne
	@JoinColumn(name="parent_sc_service_detail_id")
	private ScServiceDetail scServiceDetail2;

	//bi-directional many-to-one association to ScServiceDetail
	@ManyToOne
	@JoinColumn(name="solution_service_detail_id")
	private ScServiceDetail scServiceDetail3;

	public ScSolutionComponent() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComponentGroup() {
		return this.componentGroup;
	}

	public void setComponentGroup(String componentGroup) {
		this.componentGroup = componentGroup;
	}

	public Integer getCpeComponentId() {
		return this.cpeComponentId;
	}

	public void setCpeComponentId(Integer cpeComponentId) {
		this.cpeComponentId = cpeComponentId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getParentServiceCode() {
		return this.parentServiceCode;
	}

	public void setParentServiceCode(String parentServiceCode) {
		this.parentServiceCode = parentServiceCode;
	}

	public String getSolutionCode() {
		return this.solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public ScOrder getScOrder() {
		return this.scOrder;
	}

	public void setScOrder(ScOrder scOrder) {
		this.scOrder = scOrder;
	}

	public ScServiceDetail getScServiceDetail1() {
		return this.scServiceDetail1;
	}

	public void setScServiceDetail1(ScServiceDetail scServiceDetail1) {
		this.scServiceDetail1 = scServiceDetail1;
	}

	public ScServiceDetail getScServiceDetail2() {
		return this.scServiceDetail2;
	}

	public void setScServiceDetail2(ScServiceDetail scServiceDetail2) {
		this.scServiceDetail2 = scServiceDetail2;
	}

	public ScServiceDetail getScServiceDetail3() {
		return this.scServiceDetail3;
	}

	public void setScServiceDetail3(ScServiceDetail scServiceDetail3) {
		this.scServiceDetail3 = scServiceDetail3;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getO2cTriggeredStatus() {
		return o2cTriggeredStatus;
	}

	public void setO2cTriggeredStatus(String o2cTriggeredStatus) {
		this.o2cTriggeredStatus = o2cTriggeredStatus;
	}

}