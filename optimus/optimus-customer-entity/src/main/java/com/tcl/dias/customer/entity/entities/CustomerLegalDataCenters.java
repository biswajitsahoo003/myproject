package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
 * 
 * CustomerLegalDataCenters Entity Class
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "customer_le_data_centers")
@NamedQuery(name = "CustomerLegalDataCenters.findAll", query = "SELECT a FROM CustomerLegalDataCenters a")
public class CustomerLegalDataCenters implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
	@Column(name = "customer_le_id")
	private Integer customerleId;
	
	@Column(name = "data_center_id")
	private Integer dataCenterId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCustomerleId() {
		return customerleId;
	}
	public void setCustomerleId(Integer customerleId) {
		this.customerleId = customerleId;
	}
	public Integer getDataCenterId() {
		return dataCenterId;
	}
	public void setDataCenterId(Integer dataCenterId) {
		this.dataCenterId = dataCenterId;
	}
	
}
