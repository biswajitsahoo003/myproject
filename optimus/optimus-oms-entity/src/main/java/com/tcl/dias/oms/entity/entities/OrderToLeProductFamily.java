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
@Table(name = "order_to_le_product_family")
@NamedQuery(name = "OrderToLeProductFamily.findAll", query = "SELECT o FROM OrderToLeProductFamily o")
public class OrderToLeProductFamily implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// bi-directional many-to-one association to OrderProductSolution
	@OneToMany(mappedBy = "orderToLeProductFamily")
	private Set<OrderProductSolution> orderProductSolutions;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	// bi-directional many-to-one association to OrderToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_to_le_id")
	private OrderToLe orderToLe;

	public OrderToLeProductFamily() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<OrderProductSolution> getOrderProductSolutions() {
		return this.orderProductSolutions;
	}

	public void setOrderProductSolutions(Set<OrderProductSolution> orderProductSolutions) {
		this.orderProductSolutions = orderProductSolutions;
	}

	public OrderProductSolution addOrderProductSolution(OrderProductSolution orderProductSolution) {
		getOrderProductSolutions().add(orderProductSolution);
		orderProductSolution.setOrderToLeProductFamily(this);

		return orderProductSolution;
	}

	public OrderProductSolution removeOrderProductSolution(OrderProductSolution orderProductSolution) {
		getOrderProductSolutions().remove(orderProductSolution);
		orderProductSolution.setOrderToLeProductFamily(null);

		return orderProductSolution;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public OrderToLe getOrderToLe() {
		return this.orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

}