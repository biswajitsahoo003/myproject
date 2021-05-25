package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * IpaddrLanv4Address Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="ipaddr_lanv4_address")
@NamedQuery(name="IpaddrLanv4Address.findAll", query="SELECT i FROM IpaddrLanv4Address i")
public class IpaddrLanv4Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="lanv4_addr_id")
	private Integer lanv4AddrId;

	@Column(name="end_date")
	private Timestamp endDate;

	private Byte iscustomerprovided;

	private Byte issecondary;

	@Column(name="lanv4_address")
	private String lanv4Address;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to IpAddressDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ip_address_details_IP_Address_Details")
	private IpAddressDetail ipAddressDetail;

	public IpaddrLanv4Address() {
	}

	public Integer getLanv4AddrId() {
		return this.lanv4AddrId;
	}

	public void setLanv4AddrId(Integer lanv4AddrId) {
		this.lanv4AddrId = lanv4AddrId;
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

	public String getLanv4Address() {
		return this.lanv4Address;
	}

	public void setLanv4Address(String lanv4Address) {
		this.lanv4Address = lanv4Address;
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

	public IpAddressDetail getIpAddressDetail() {
		return this.ipAddressDetail;
	}

	public void setIpAddressDetail(IpAddressDetail ipAddressDetail) {
		this.ipAddressDetail = ipAddressDetail;
	}

}