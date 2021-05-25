package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the order_teamsdr_details database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "order_teamsdr_details")
@NamedQuery(name = "OrderTeamsDRDetails.findAll", query = "SELECT o FROM OrderTeamsDRDetails o")
public class OrderTeamsDRDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private BigDecimal arc;

	private String country;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Date createdTime;

	private BigDecimal mrc;

	@Column(name = "no_of_common_area_devices")
	private Integer noOfCommonAreaDevices;

	@Column(name = "no_of_named_users")
	private Integer noOfNamedUsers;

	private BigDecimal nrc;

	private BigDecimal tcv;

	@Column(name = "total_users")
	private Integer totalUsers;

	// bi-directional many-to-one association to OrderTeamsDR
	@ManyToOne
	@JoinColumn(name = "order_teamsdr_id")
	private OrderTeamsDR orderTeamsDR;

	public OrderTeamsDRDetails() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getArc() {
		return this.arc;
	}

	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public BigDecimal getMrc() {
		return this.mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public Integer getNoOfCommonAreaDevices() {
		return this.noOfCommonAreaDevices;
	}

	public void setNoOfCommonAreaDevices(Integer noOfCommonAreaDevices) {
		this.noOfCommonAreaDevices = noOfCommonAreaDevices;
	}

	public Integer getNoOfNamedUsers() {
		return this.noOfNamedUsers;
	}

	public void setNoOfNamedUsers(Integer noOfNamedUsers) {
		this.noOfNamedUsers = noOfNamedUsers;
	}

	public BigDecimal getNrc() {
		return this.nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public BigDecimal getTcv() {
		return this.tcv;
	}

	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

	public Integer getTotalUsers() {
		return this.totalUsers;
	}

	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	public OrderTeamsDR getOrderTeamsDR() {
		return this.orderTeamsDR;
	}

	public void setOrderTeamsDR(OrderTeamsDR orderTeamsDR) {
		this.orderTeamsDR = orderTeamsDR;
	}

}