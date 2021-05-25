package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * IpaddrWanv4Address Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="ipaddr_wanv4_address")
@NamedQuery(name="IpaddrWanv4Address.findAll", query="SELECT i FROM IpaddrWanv4Address i")
public class IpaddrWanv4Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="wanv4_addr_id")
	private Integer wanv4AddrId;

	@Column(name="end_date")
	private Timestamp endDate;

	private Byte iscustomerprovided;

	private Byte issecondary;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="wanv4_address")
	private String wanv4Address;

	//bi-directional many-to-one association to IpAddressDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ip_address_details_IP_Address_Details")
	private IpAddressDetail ipAddressDetail;

	public IpaddrWanv4Address() {
	}

	public Integer getWanv4AddrId() {
		return this.wanv4AddrId;
	}

	public void setWanv4AddrId(Integer wanv4AddrId) {
		this.wanv4AddrId = wanv4AddrId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIscustomerprovided() {
		return this.iscustomerprovided;
	}

	public void setIscustomerprovided(Byte iscustomerprovided) {
		this.iscustomerprovided = iscustomerprovided;
	}

	public Byte getIssecondary() {
		return this.issecondary;
	}

	public void setIssecondary(Byte issecondary) {
		this.issecondary = issecondary;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getWanv4Address() {
		return this.wanv4Address;
	}

	public void setWanv4Address(String wanv4Address) {
		this.wanv4Address = wanv4Address;
	}

	public IpAddressDetail getIpAddressDetail() {
		return this.ipAddressDetail;
	}

	public void setIpAddressDetail(IpAddressDetail ipAddressDetail) {
		this.ipAddressDetail = ipAddressDetail;
	}

}