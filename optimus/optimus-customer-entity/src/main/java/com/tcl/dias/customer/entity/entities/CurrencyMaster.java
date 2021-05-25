package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "currency_master")
@NamedQuery(name = "CurrencyMaster.findAll", query = "SELECT c FROM CurrencyMaster c")
public class CurrencyMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "currency_name")
	private String currencyName;

	@Column(name = "currency_symbol")
	private String currencySymbol;

	@Column(name = "short_name")
	private String shortName;

	private Byte status;

	// bi-directional many-to-one association to CustomerLeCurrency
	@OneToMany(mappedBy = "currencyMaster")
	private Set<CustomerLeCurrency> customerLeCurrencies;

	// bi-directional many-to-one association to SpLeCurrency
	@OneToMany(mappedBy = "currencyMaster")
	private Set<SpLeCurrency> spLeCurrencies;

	public CurrencyMaster() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCurrencyName() {
		return this.currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<CustomerLeCurrency> getCustomerLeCurrencies() {
		return this.customerLeCurrencies;
	}

	public void setCustomerLeCurrencies(Set<CustomerLeCurrency> customerLeCurrencies) {
		this.customerLeCurrencies = customerLeCurrencies;
	}

	public CustomerLeCurrency addCustomerLeCurrency(CustomerLeCurrency customerLeCurrency) {
		getCustomerLeCurrencies().add(customerLeCurrency);
		customerLeCurrency.setCurrencyMaster(this);

		return customerLeCurrency;
	}

	public CustomerLeCurrency removeCustomerLeCurrency(CustomerLeCurrency customerLeCurrency) {
		getCustomerLeCurrencies().remove(customerLeCurrency);
		customerLeCurrency.setCurrencyMaster(null);

		return customerLeCurrency;
	}

	public Set<SpLeCurrency> getSpLeCurrencies() {
		return this.spLeCurrencies;
	}

	public void setSpLeCurrencies(Set<SpLeCurrency> spLeCurrencies) {
		this.spLeCurrencies = spLeCurrencies;
	}

	public SpLeCurrency addSpLeCurrency(SpLeCurrency spLeCurrency) {
		getSpLeCurrencies().add(spLeCurrency);
		spLeCurrency.setCurrencyMaster(this);

		return spLeCurrency;
	}

	public SpLeCurrency removeSpLeCurrency(SpLeCurrency spLeCurrency) {
		getSpLeCurrencies().remove(spLeCurrency);
		spLeCurrency.setCurrencyMaster(null);

		return spLeCurrency;
	}

}