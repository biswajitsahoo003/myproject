package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class for Servicehanover table
 * 
 *
 * @author Yogesh 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_billing_states_cities")
@NamedQuery(name = "mst_billing_states_cities.findAll", query = "SELECT b FROM MstBillingStatesAndCities b")
public class MstBillingStatesAndCities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "states_cities_countries")
	private String validStatesAndCities;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValidStatesAndCities() {
		return validStatesAndCities;
	}

	public void setValidStatesAndCities(String validStatesAndCities) {
		this.validStatesAndCities = validStatesAndCities;
	}

	
}
