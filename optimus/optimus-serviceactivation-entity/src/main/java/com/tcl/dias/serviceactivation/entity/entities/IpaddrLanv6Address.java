package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * IpaddrLanv6Address Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="ipaddr_lanv6_address")
@NamedQuery(name="IpaddrLanv6Address.findAll", query="SELECT i FROM IpaddrLanv6Address i")
public class IpaddrLanv6Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="lanv6_addr_id")
	private Integer lanv6AddrId;

	@Column(name="end_date")
	private Timestamp endDate;

	private Byte iscustomerprovided;

	private Byte issecondary;

	@Column(name="lanv6_address")
	private String lanv6Address;

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

	public IpaddrLanv6Address() {
	}

	public Integer getLanv6AddrId() {
		return this.lanv6AddrId;
	}

	public void setLanv6AddrId(Integer lanv6AddrId) {
		this.lanv6AddrId = lanv6AddrId;
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

	public String getLanv6Address() {
		return this.lanv6Address;
	}

	public void setLanv6Address(String lanv6Address) {
		this.lanv6Address = lanv6Address;
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