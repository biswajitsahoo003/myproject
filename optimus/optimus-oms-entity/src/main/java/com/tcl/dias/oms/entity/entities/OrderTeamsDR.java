package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the order_teamsdr database table.
 * 
 * @author Srinivasa Raghavan
 * 
 */
@Entity
@Table(name = "order_teamsdr")
@NamedQuery(name = "OrderTeamsDR.findAll", query = "SELECT o FROM OrderTeamsDR o")
public class OrderTeamsDR implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private double arc;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "is_config")
	private byte isConfig;

	private double mrc;

	@Column(name = "no_of_users")
	private int noOfUsers;

	private double nrc;

	@ManyToOne
	@JoinColumn(name = "order_product_solution_id")
	private OrderProductSolution orderProductSolution;

	@ManyToOne
	@JoinColumn(name = "order_to_le_id")
	private OrderToLe orderToLe;

	@Column(name = "order_version")
	private int orderVersion;

	@Column(name = "profile_name")
	private String profileName;

	@Column(name = "service_name")
	private String serviceName;

	private byte status;

	private double tcv;

	// bi-directional many-to-one association to OrderDirectRouting
	@OneToMany(mappedBy = "orderTeamsDR")
	private List<OrderDirectRouting> orderDirectRoutings;

	// bi-directional many-to-one association to OrderTeamsLicense
	@OneToMany(mappedBy = "orderTeamsDR")
	private List<OrderTeamsLicense> orderTeamsLicenses;

	// bi-directional many-to-one association to OrderTeamsDRDetails
	@OneToMany(mappedBy = "orderTeamsDR")
	private List<OrderTeamsDRDetails> orderTeamsDRDetails;

	public OrderTeamsDR() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getArc() {
		return this.arc;
	}

	public void setArc(double arc) {
		this.arc = arc;
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

	public byte getIsConfig() {
		return this.isConfig;
	}

	public void setIsConfig(byte isConfig) {
		this.isConfig = isConfig;
	}

	public double getMrc() {
		return this.mrc;
	}

	public void setMrc(double mrc) {
		this.mrc = mrc;
	}

	public int getNoOfUsers() {
		return this.noOfUsers;
	}

	public void setNoOfUsers(int noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public double getNrc() {
		return this.nrc;
	}

	public void setNrc(double nrc) {
		this.nrc = nrc;
	}

	public OrderProductSolution getOrderProductSolution() {
		return orderProductSolution;
	}

	public void setOrderProductSolution(OrderProductSolution orderProductSolution) {
		this.orderProductSolution = orderProductSolution;
	}

	public OrderToLe getOrderToLe() {
		return orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

	public int getOrderVersion() {
		return this.orderVersion;
	}

	public void setOrderVersion(int orderVersion) {
		this.orderVersion = orderVersion;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public double getTcv() {
		return this.tcv;
	}

	public void setTcv(double tcv) {
		this.tcv = tcv;
	}

	public List<OrderDirectRouting> getOrderDirectRoutings() {
		return this.orderDirectRoutings;
	}

	public void setOrderDirectRoutings(List<OrderDirectRouting> orderDirectRoutings) {
		this.orderDirectRoutings = orderDirectRoutings;
	}

	public OrderDirectRouting addOrderDR(OrderDirectRouting orderDirectRouting) {
		getOrderDirectRoutings().add(orderDirectRouting);
		orderDirectRouting.setOrderTeamsDR(this);

		return orderDirectRouting;
	}

	public OrderDirectRouting removeOrderDR(OrderDirectRouting orderDirectRouting) {
		getOrderDirectRoutings().remove(orderDirectRouting);
		orderDirectRouting.setOrderTeamsDR(null);

		return orderDirectRouting;
	}

	public List<OrderTeamsLicense> getOrderTeamsLicenses() {
		return this.orderTeamsLicenses;
	}

	public void setOrderTeamsLicenses(List<OrderTeamsLicense> orderTeamsLicenses) {
		this.orderTeamsLicenses = orderTeamsLicenses;
	}

	public OrderTeamsLicense addOrderTeamsLicens(OrderTeamsLicense orderTeamsLicens) {
		getOrderTeamsLicenses().add(orderTeamsLicens);
		orderTeamsLicens.setOrderTeamsDR(this);

		return orderTeamsLicens;
	}

	public OrderTeamsLicense removeOrderTeamsLicens(OrderTeamsLicense orderTeamsLicens) {
		getOrderTeamsLicenses().remove(orderTeamsLicens);
		orderTeamsLicens.setOrderTeamsDR(null);

		return orderTeamsLicens;
	}

	public List<OrderTeamsDRDetails> getOrderTeamsDRDetails() {
		return this.orderTeamsDRDetails;
	}

	public void setOrderTeamsDRDetails(List<OrderTeamsDRDetails> orderTeamsDRDetails) {
		this.orderTeamsDRDetails = orderTeamsDRDetails;
	}

	public OrderTeamsDRDetails addOrderTeamsdrDetail(OrderTeamsDRDetails orderTeamsdrDetails) {
		getOrderTeamsDRDetails().add(orderTeamsdrDetails);
		orderTeamsdrDetails.setOrderTeamsDR(this);

		return orderTeamsdrDetails;
	}

	public OrderTeamsDRDetails removeOrderTeamsdrDetail(OrderTeamsDRDetails orderTeamsdrDetails) {
		getOrderTeamsDRDetails().remove(orderTeamsdrDetails);
		orderTeamsdrDetails.setOrderTeamsDR(null);

		return orderTeamsdrDetails;
	}

}