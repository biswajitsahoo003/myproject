package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This file contains the MstServiceClassificationDetail.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="mst_service_classification_details")
@NamedQuery(name="MstServiceClassificationDetail.findAll", query="SELECT m FROM MstServiceClassificationDetail m")
public class MstServiceClassificationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="first_wan_link_type")
	private String firstWanLinkType;

	@Column(name="internet_breakout")
	private String internetBreakout;

	@Column(name="no_of_cpe")
	private String noOfCpe;

	@Column(name="no_of_wan_links")
	private String noOfWanLinks;

	@Column(name="second_wan_link_type")
	private String secondWanLinkType;

	@Column(name="service_classification")
	private String serviceClassification;

	@Column(name="site_type")
	private String siteType;

	public MstServiceClassificationDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstWanLinkType() {
		return this.firstWanLinkType;
	}

	public void setFirstWanLinkType(String firstWanLinkType) {
		this.firstWanLinkType = firstWanLinkType;
	}

	public String getInternetBreakout() {
		return this.internetBreakout;
	}

	public void setInternetBreakout(String internetBreakout) {
		this.internetBreakout = internetBreakout;
	}

	public String getNoOfCpe() {
		return this.noOfCpe;
	}

	public void setNoOfCpe(String noOfCpe) {
		this.noOfCpe = noOfCpe;
	}

	public String getNoOfWanLinks() {
		return this.noOfWanLinks;
	}

	public void setNoOfWanLinks(String noOfWanLinks) {
		this.noOfWanLinks = noOfWanLinks;
	}

	public String getSecondWanLinkType() {
		return this.secondWanLinkType;
	}

	public void setSecondWanLinkType(String secondWanLinkType) {
		this.secondWanLinkType = secondWanLinkType;
	}

	public String getServiceClassification() {
		return this.serviceClassification;
	}

	public void setServiceClassification(String serviceClassification) {
		this.serviceClassification = serviceClassification;
	}

	public String getSiteType() {
		return this.siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

}