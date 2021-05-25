package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Bean class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_countries_currency_master")
@NamedQuery(name = "MstCountriesCurrencyMaster.findAll", query = "SELECT m FROM MstCountriesCurrencyMaster m")
public class MstCountriesCurrencyMaster  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="currency_master_id")
	private CurrencyMaster currencyMaster;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mst_countries_id")
	private MstCountry mstCountry;

	private byte status;
	
	public MstCountriesCurrencyMaster() {
		// DO NOTHING
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	/**
	 * @return the currencyMaster
	 */
	public CurrencyMaster getCurrencyMaster() {
		return currencyMaster;
	}

	/**
	 * @param currencyMaster the currencyMaster to set
	 */
	public void setCurrencyMaster(CurrencyMaster currencyMaster) {
		this.currencyMaster = currencyMaster;
	}

	/**
	 * @return the mstCountry
	 */
	public MstCountry getMstCountry() {
		return mstCountry;
	}

	/**
	 * @param mstCountry the mstCountry to set
	 */
	public void setMstCountry(MstCountry mstCountry) {
		this.mstCountry = mstCountry;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
	
	

}
