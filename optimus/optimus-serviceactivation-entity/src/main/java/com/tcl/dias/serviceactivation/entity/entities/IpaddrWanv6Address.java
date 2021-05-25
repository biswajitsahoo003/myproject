package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * IpaddrWanv6Address Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="ipaddr_wanv6_address")
@NamedQuery(name="IpaddrWanv6Address.findAll", query="SELECT i FROM IpaddrWanv6Address i")
public class IpaddrWanv6Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="wanv6_addr_id")
	private Integer wanv6AddrId;

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

	@Column(name="wanv6_address")
	private String wanv6Address;

	//bi-directional many-to-one association to IpAddressDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ip_address_details_IP_Address_Details")
	private IpAddressDetail ipAddressDetail;

	public IpaddrWanv6Address() {
	}

	public Integer getWanv6AddrId() {
		return this.wanv6AddrId;
	}

	public void setWanv6AddrId(Integer wanv6AddrId) {
		this.wanv6AddrId = wanv6AddrId;
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

	public String getWanv6Address() {
		return this.wanv6Address;
	}

	public void setWanv6Address(String wanv6Address) {
		this.wanv6Address = wanv6Address;
	}

	public IpAddressDetail getIpAddressDetail() {
		return this.ipAddressDetail;
	}

	public void setIpAddressDetail(IpAddressDetail ipAddressDetail) {
		this.ipAddressDetail = ipAddressDetail;
	}

}