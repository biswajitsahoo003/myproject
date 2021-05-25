package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_product_component")
@NamedQuery(name = "OrderProductComponent.findAll", query = "SELECT o FROM OrderProductComponent o")
public class OrderProductComponent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reference_id")
	private Integer referenceId;

	@Column(name = "type")
	private String type;

	// bi-directional many-to-one association to MstProductComponent
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_component_id")
	private MstProductComponent mstProductComponent;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	// bi-directional many-to-one association to
	// OrderProductComponentsAttributeValue
	@OneToMany(mappedBy = "orderProductComponent")
	private Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues;

	@Column(name = "reference_name")
	private String referenceName;

	public OrderProductComponent() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public MstProductComponent getMstProductComponent() {
		return this.mstProductComponent;
	}

	public void setMstProductComponent(MstProductComponent mstProductComponent) {
		this.mstProductComponent = mstProductComponent;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public Set<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValues() {
		return this.orderProductComponentsAttributeValues;
	}

	public void setOrderProductComponentsAttributeValues(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		this.orderProductComponentsAttributeValues = orderProductComponentsAttributeValues;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public OrderProductComponentsAttributeValue addOrderProductComponentsAttributeValue(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		getOrderProductComponentsAttributeValues().add(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValue.setOrderProductComponent(this);

		return orderProductComponentsAttributeValue;
	}

	public OrderProductComponentsAttributeValue removeOrderProductComponentsAttributeValue(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		getOrderProductComponentsAttributeValues().remove(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValue.setOrderProductComponent(null);

		return orderProductComponentsAttributeValue;
	}

}