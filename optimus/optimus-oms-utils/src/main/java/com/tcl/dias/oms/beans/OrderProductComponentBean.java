package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;


@JsonInclude(Include.NON_NULL)
public class OrderProductComponentBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8051384875284512631L;

	private Integer id;
	
	private Integer componentMasterId;

	public Integer getComponentMasterId() {
		return componentMasterId;
	}

	public void setComponentMasterId(Integer componentMasterId) {
		this.componentMasterId = componentMasterId;
	}

	private Integer orderVersion;

	private Integer referenceId;

	private String description;

	private String name;

	private QuotePriceBean price;

	private String type;

	private List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValues;

	public static OrderProductComponentBean fromOrderProductComponent(OrderProductComponent orderProductComponent) {
		OrderProductComponentBean gscOrderProductComponentBean = new OrderProductComponentBean();
		gscOrderProductComponentBean.setId(orderProductComponent.getId());
		gscOrderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
		gscOrderProductComponentBean.setType(orderProductComponent.getType());
		gscOrderProductComponentBean.setOrderProductComponentsAttributeValues(
				orderProductComponent.getOrderProductComponentsAttributeValues().stream()
						.map(OrderProductComponentsAttributeValueBean::new).collect(Collectors.toList()));
		return gscOrderProductComponentBean;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderVersion() {
		return this.orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}



	/**
	 * @return the orderProductComponentsAttributeValues
	 */
	public List<OrderProductComponentsAttributeValueBean> getOrderProductComponentsAttributeValues() {
		return orderProductComponentsAttributeValues;
	}

	/**
	 * @param orderProductComponentsAttributeValues the orderProductComponentsAttributeValues to set
	 */
	public void setOrderProductComponentsAttributeValues(
			List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValues) {
		this.orderProductComponentsAttributeValues = orderProductComponentsAttributeValues;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price
	 */
	public QuotePriceBean getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(QuotePriceBean price) {
		this.price = price;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}





}