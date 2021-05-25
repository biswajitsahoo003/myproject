package com.tcl.dias.oms.entity.entities;

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
 * The persistent class for the order_confirmation_audit database table.
 * 
 * @author SEKHAR ER
 *
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_site_provisioning_audit")
@NamedQuery(name="OrderSiteProvisionAudit.findAll", query="SELECT o FROM OrderSiteProvisionAudit o")
public class OrderSiteProvisionAudit {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;


	
	@Column(name="updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	
	@Column(name="stage")
	private String stage;
	
	@Column(name="site_uuid")
	private String siteUuid;


	public String getSiteUuid() {
		return siteUuid;
	}


	public void setSiteUuid(String siteUuid) {
		this.siteUuid = siteUuid;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}



	public Date getUpdatedTime() {
		return updatedTime;
	}


	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getStage() {
		return stage;
	}


	public void setStage(String stage) {
		this.stage = stage;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
