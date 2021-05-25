package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * MstBsIPSsIPMapping Entity Class
 * 
 *
 * @author Dimple
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Entity
@Table(name = "mst_bsip_ssip_mapping")
@NamedQuery(name = "MstBsIPSsIPMapping.findAll", query = "SELECT v FROM MstBsIPSsIPMapping v")
public class MstBsIPSsIPMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "bs_ip")
	private String bsIp;

	@Column(name = "ss_ip")
	private String ssIp;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;


	public MstBsIPSsIPMapping() {
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getServiceCode() {
		return serviceCode;
	}


	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}


	public String getBsIp() {
		return bsIp;
	}


	public void setBsIp(String bsIp) {
		this.bsIp = bsIp;
	}


	public String getSsIp() {
		return ssIp;
	}


	public void setSsIp(String ssIp) {
		this.ssIp = ssIp;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Timestamp getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
}