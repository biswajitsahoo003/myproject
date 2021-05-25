package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author vpachava
 *
 */
@Entity
@Table(name="quote_izosdwan_mss_pricing")
@NamedQuery(name="QuoteIzoSdwanMssPricing.findAll", query="SELECT q FROM QuoteIzoSdwanMssPricing q")
public class QuoteIzoSdwanMssPricing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	
	private String product;
	
	@Column(name = "solution_id")
	private Integer solutionId;
	
	@Column(name = "component_name")
	private String componentName;
	
	@Column(name = "component_type")
	private String componentType;
	
	@Column(name = "action_type")
	private String actionType;
	

	private String value;
	
	@Column(name = "value_type")
	private String valueType;
	

	private String currency;
	
	private Double arc;
	
	
	private Double nrc;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getProduct() {
		return product;
	}


	public void setProduct(String product) {
		this.product = product;
	}


	public Integer getSolutionId() {
		return solutionId;
	}


	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}


	public String getComponentName() {
		return componentName;
	}


	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}


	public String getComponentType() {
		return componentType;
	}


	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}


	public String getActionType() {
		return actionType;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getValueType() {
		return valueType;
	}


	public void setValueType(String valueType) {
		this.valueType = valueType;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	

	public Double getArc() {
		return arc;
	}


	public void setArc(Double arc) {
		this.arc = arc;
	}


	public Double getNrc() {
		return nrc;
	}


	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}
	
	
}
