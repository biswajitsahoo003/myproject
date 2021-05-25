package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This file contains the OrderIllSiteSla.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
@Entity
@Table(name = "order_ill_sites_sla")
@NamedQuery(name = "OrderIllSiteSla.findAll", query = "SELECT o FROM OrderIllSiteSla o")
public class OrderIllSiteSla implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5963947229104265032L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sla_end_date")
	private Date slaEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sla_start_date")
	private Date slaStartDate;

	// bi-directional many-to-one association to QuoteIllSite
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ill_site_id")
	private OrderIllSite orderIllSite;

	// bi-directional many-to-one association to SlaMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;

	@Column(name = "sla_value")
	private String slaValue;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the slaEndDate
	 */
	public Date getSlaEndDate() {
		return slaEndDate;
	}

	/**
	 * @param slaEndDate
	 *            the slaEndDate to set
	 */
	public void setSlaEndDate(Date slaEndDate) {
		this.slaEndDate = slaEndDate;
	}

	/**
	 * @return the slaStartDate
	 */
	public Date getSlaStartDate() {
		return slaStartDate;
	}

	/**
	 * @param slaStartDate
	 *            the slaStartDate to set
	 */
	public void setSlaStartDate(Date slaStartDate) {
		this.slaStartDate = slaStartDate;
	}

	/**
	 * @return the orderIllSite
	 */
	public OrderIllSite getOrderIllSite() {
		return orderIllSite;
	}

	/**
	 * @param orderIllSite
	 *            the orderIllSite to set
	 */
	public void setOrderIllSite(OrderIllSite orderIllSite) {
		this.orderIllSite = orderIllSite;
	}

	/**
	 * @return the slaMaster
	 */
	public SlaMaster getSlaMaster() {
		return slaMaster;
	}

	/**
	 * @param slaMaster
	 *            the slaMaster to set
	 */
	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

	/**
	 * @return the slaValue
	 */
	public String getSlaValue() {
		return slaValue;
	}

	/**
	 * @param slaValue
	 *            the slaValue to set
	 */
	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

}
