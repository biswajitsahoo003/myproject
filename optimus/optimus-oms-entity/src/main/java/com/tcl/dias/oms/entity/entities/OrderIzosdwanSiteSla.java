package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * 
 * This file contains the OrderIzosdwanSiteSla.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_izosdwan_site_sla")
@NamedQuery(name="OrderIzosdwanSiteSla.findAll", query="SELECT o FROM OrderIzosdwanSiteSla o")
public class OrderIzosdwanSiteSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sla_end_date")
	private Date slaEndDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sla_start_date")
	private Date slaStartDate;

	@Column(name="sla_value")
	private String slaValue;

	//bi-directional many-to-one association to OrderIzosdwanSite
	@ManyToOne
	@JoinColumn(name="izosdwan_site_id")
	private OrderIzosdwanSite orderIzosdwanSite;

	public OrderIzosdwanSiteSla() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getSlaEndDate() {
		return this.slaEndDate;
	}

	public void setSlaEndDate(Date slaEndDate) {
		this.slaEndDate = slaEndDate;
	}

	public SlaMaster getSlaMaster() {
		return this.slaMaster;
	}

	public void setSlaMaster(SlaMaster slaMasterId) {
		this.slaMaster = slaMasterId;
	}

	public Date getSlaStartDate() {
		return this.slaStartDate;
	}

	public void setSlaStartDate(Date slaStartDate) {
		this.slaStartDate = slaStartDate;
	}

	public String getSlaValue() {
		return this.slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	public OrderIzosdwanSite getOrderIzosdwanSite() {
		return this.orderIzosdwanSite;
	}

	public void setOrderIzosdwanSite(OrderIzosdwanSite orderIzosdwanSite) {
		this.orderIzosdwanSite = orderIzosdwanSite;
	}

}