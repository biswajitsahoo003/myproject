package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "currency_conversion")
@NamedQuery(name = "CurrencyConversion.findAll", query = "SELECT c FROM CurrencyConversion c")
public class CurrencyConversion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "conversion_rate")
	private String conversionRate;

	@Column(name = "input_currency")
	private String inputCurrency;

	@Column(name = "output_currency")
	private String outputCurrency;

	private String status;

	public CurrencyConversion() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConversionRate() {
		return this.conversionRate;
	}

	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getInputCurrency() {
		return this.inputCurrency;
	}

	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}

	public String getOutputCurrency() {
		return this.outputCurrency;
	}

	public void setOutputCurrency(String outputCurrency) {
		this.outputCurrency = outputCurrency;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}