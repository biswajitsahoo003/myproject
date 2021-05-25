package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This file contains the OdrSolutionComponent.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="odr_solution_components")
@NamedQuery(name="OdrSolutionComponent.findAll", query="SELECT o FROM OdrSolutionComponent o")
public class OdrSolutionComponent implements Serializable {
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

	@Column(name="order_code")
	private String orderCode;

	@Column(name="parent_service_code")
	private String parentServiceCode;

	@Column(name="solution_code")
	private String solutionCode;

	//bi-directional many-to-one association to OdrOrder
	@ManyToOne
	@JoinColumn(name="odr_order_id")
	private OdrOrder odrOrder;
	
	@Column(name="service_code")
	private String serviceCode;

	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail1;

	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="parent_odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail2;

	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="solution_service_detail_id")
	private OdrServiceDetail odrServiceDetail3;

	public OdrSolutionComponent() {
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

	public OdrOrder getOdrOrder() {
		return this.odrOrder;
	}

	public void setOdrOrder(OdrOrder odrOrder) {
		this.odrOrder = odrOrder;
	}

	public OdrServiceDetail getOdrServiceDetail1() {
		return this.odrServiceDetail1;
	}

	public void setOdrServiceDetail1(OdrServiceDetail odrServiceDetail1) {
		this.odrServiceDetail1 = odrServiceDetail1;
	}

	public OdrServiceDetail getOdrServiceDetail2() {
		return this.odrServiceDetail2;
	}

	public void setOdrServiceDetail2(OdrServiceDetail odrServiceDetail2) {
		this.odrServiceDetail2 = odrServiceDetail2;
	}

	public OdrServiceDetail getOdrServiceDetail3() {
		return this.odrServiceDetail3;
	}

	public void setOdrServiceDetail3(OdrServiceDetail odrServiceDetail3) {
		this.odrServiceDetail3 = odrServiceDetail3;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String servicenCode) {
		this.serviceCode = servicenCode;
	}
}