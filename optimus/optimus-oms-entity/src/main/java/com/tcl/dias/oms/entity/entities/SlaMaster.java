package com.tcl.dias.oms.entity.entities;

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
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "sla_master")
@NamedQuery(name = "SlaMaster.findAll", query = "SELECT s FROM SlaMaster s")
public class SlaMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "sla_duration_in_days")
	private Integer slaDurationInDays;

	@Column(name = "sla_name")
	private String slaName;

	// bi-directional many-to-one association to QuoteIllSiteSla
	@OneToMany(mappedBy = "slaMaster")
	private Set<QuoteIllSiteSla> quoteIllSiteSlas;
	
	//bi-directional many-to-one association to QuoteIzosdwanSiteSla
	@OneToMany(mappedBy="slaMaster")
	private Set<QuoteIzosdwanSiteSla> quoteIzosdwanSiteSlas;

	public SlaMaster() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSlaDurationInDays() {
		return this.slaDurationInDays;
	}

	public void setSlaDurationInDays(Integer slaDurationInDays) {
		this.slaDurationInDays = slaDurationInDays;
	}

	public String getSlaName() {
		return this.slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public Set<QuoteIllSiteSla> getQuoteIllSiteSlas() {
		return this.quoteIllSiteSlas;
	}

	public void setQuoteIllSiteSlas(Set<QuoteIllSiteSla> quoteIllSiteSlas) {
		this.quoteIllSiteSlas = quoteIllSiteSlas;
	}

	public QuoteIllSiteSla addQuoteIllSiteSla(QuoteIllSiteSla quoteIllSiteSla) {
		getQuoteIllSiteSlas().add(quoteIllSiteSla);
		quoteIllSiteSla.setSlaMaster(this);

		return quoteIllSiteSla;
	}

	public QuoteIllSiteSla removeQuoteIllSiteSla(QuoteIllSiteSla quoteIllSiteSla) {
		getQuoteIllSiteSlas().remove(quoteIllSiteSla);
		quoteIllSiteSla.setSlaMaster(null);

		return quoteIllSiteSla;
	}

	public Set<QuoteIzosdwanSiteSla> getQuoteIzosdwanSiteSlas() {
		return quoteIzosdwanSiteSlas;
	}

	public void setQuoteIzosdwanSiteSlas(Set<QuoteIzosdwanSiteSla> quoteIzosdwanSiteSlas) {
		this.quoteIzosdwanSiteSlas = quoteIzosdwanSiteSlas;
	}

}