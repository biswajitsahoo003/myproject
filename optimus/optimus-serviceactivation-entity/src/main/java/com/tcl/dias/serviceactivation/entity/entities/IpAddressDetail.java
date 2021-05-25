package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * IpAddressDetail Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="ip_address_details")
@NamedQuery(name="IpAddressDetail.findAll", query="SELECT i FROM IpAddressDetail i")
public class IpAddressDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer IP_Address_Details;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="extended_lan_enabled")
	private Byte extendedLanEnabled;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="nms_service_ipv4_address")
	private String nmsServiceIpv4Address;

	@Column(name="nni_vsat_ipaddress")
	private String nniVsatIpaddress;

	@Column(name="no_mac_address")
	private Integer noMacAddress;

	@Column(name="path_ip_type")
	private String pathIpType;

	@Column(name="ping_address1")
	private String pingAddress1;

	@Column(name="ping_address2")
	private String pingAddress2;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Service_details_id")
	private ServiceDetail serviceDetail;

	//bi-directional many-to-one association to IpaddrLanv4Address
	@OneToMany(mappedBy="ipAddressDetail")
	private Set<IpaddrLanv4Address> ipaddrLanv4Addresses;

	//bi-directional many-to-one association to IpaddrLanv6Address
	@OneToMany(mappedBy="ipAddressDetail")
	private Set<IpaddrLanv6Address> ipaddrLanv6Addresses;

	//bi-directional many-to-one association to IpaddrWanv4Address
	@OneToMany(mappedBy="ipAddressDetail")
	private Set<IpaddrWanv4Address> ipaddrWanv4Addresses;

	//bi-directional many-to-one association to IpaddrWanv6Address
	@OneToMany(mappedBy="ipAddressDetail")
	private Set<IpaddrWanv6Address> ipaddrWanv6Addresses;
	
	@Column(name="public_nat_provided_by")
	private String publicNatProvidedBy;
	
	@Column(name="public_nat")
	private String publicNat;

	public IpAddressDetail() {
	}

	public Integer getIP_Address_Details() {
		return this.IP_Address_Details;
	}

	public void setIP_Address_Details(Integer IP_Address_Details) {
		this.IP_Address_Details = IP_Address_Details;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getExtendedLanEnabled() {
		return this.extendedLanEnabled;
	}

	public void setExtendedLanEnabled(Byte extendedLanEnabled) {
		this.extendedLanEnabled = extendedLanEnabled;
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

	public String getNmsServiceIpv4Address() {
		return this.nmsServiceIpv4Address;
	}

	public void setNmsServiceIpv4Address(String nmsServiceIpv4Address) {
		this.nmsServiceIpv4Address = nmsServiceIpv4Address;
	}

	public String getNniVsatIpaddress() {
		return this.nniVsatIpaddress;
	}

	public void setNniVsatIpaddress(String nniVsatIpaddress) {
		this.nniVsatIpaddress = nniVsatIpaddress;
	}

	public Integer getNoMacAddress() {
		return this.noMacAddress;
	}

	public void setNoMacAddress(Integer noMacAddress) {
		this.noMacAddress = noMacAddress;
	}

	public String getPathIpType() {
		return this.pathIpType;
	}

	public void setPathIpType(String pathIpType) {
		this.pathIpType = pathIpType;
	}

	public String getPingAddress1() {
		return this.pingAddress1;
	}

	public void setPingAddress1(String pingAddress1) {
		this.pingAddress1 = pingAddress1;
	}

	public String getPingAddress2() {
		return this.pingAddress2;
	}

	public void setPingAddress2(String pingAddress2) {
		this.pingAddress2 = pingAddress2;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public Set<IpaddrLanv4Address> getIpaddrLanv4Addresses() {
		
		if(ipaddrLanv4Addresses==null) {
			ipaddrLanv4Addresses=new HashSet<>();
		}
		return this.ipaddrLanv4Addresses;
	}

	public void setIpaddrLanv4Addresses(Set<IpaddrLanv4Address> ipaddrLanv4Addresses) {
		this.ipaddrLanv4Addresses = ipaddrLanv4Addresses;
	}

	public IpaddrLanv4Address addIpaddrLanv4Address(IpaddrLanv4Address ipaddrLanv4Address) {
		getIpaddrLanv4Addresses().add(ipaddrLanv4Address);
		ipaddrLanv4Address.setIpAddressDetail(this);

		return ipaddrLanv4Address;
	}

	public IpaddrLanv4Address removeIpaddrLanv4Address(IpaddrLanv4Address ipaddrLanv4Address) {
		getIpaddrLanv4Addresses().remove(ipaddrLanv4Address);
		ipaddrLanv4Address.setIpAddressDetail(null);

		return ipaddrLanv4Address;
	}

	public Set<IpaddrLanv6Address> getIpaddrLanv6Addresses() {
		
		if(ipaddrLanv6Addresses==null) {
			ipaddrLanv6Addresses=new HashSet<>();
		}
		return this.ipaddrLanv6Addresses;
	}

	public void setIpaddrLanv6Addresses(Set<IpaddrLanv6Address> ipaddrLanv6Addresses) {
		this.ipaddrLanv6Addresses = ipaddrLanv6Addresses;
	}

	public IpaddrLanv6Address addIpaddrLanv6Address(IpaddrLanv6Address ipaddrLanv6Address) {
		getIpaddrLanv6Addresses().add(ipaddrLanv6Address);
		ipaddrLanv6Address.setIpAddressDetail(this);

		return ipaddrLanv6Address;
	}

	public IpaddrLanv6Address removeIpaddrLanv6Address(IpaddrLanv6Address ipaddrLanv6Address) {
		getIpaddrLanv6Addresses().remove(ipaddrLanv6Address);
		ipaddrLanv6Address.setIpAddressDetail(null);

		return ipaddrLanv6Address;
	}

	public Set<IpaddrWanv4Address> getIpaddrWanv4Addresses() {
		if(ipaddrWanv4Addresses==null) {
			ipaddrWanv4Addresses=new HashSet<>();
		}
		return this.ipaddrWanv4Addresses;
	}

	public void setIpaddrWanv4Addresses(Set<IpaddrWanv4Address> ipaddrWanv4Addresses) {
		this.ipaddrWanv4Addresses = ipaddrWanv4Addresses;
	}

	public IpaddrWanv4Address addIpaddrWanv4Address(IpaddrWanv4Address ipaddrWanv4Address) {
		getIpaddrWanv4Addresses().add(ipaddrWanv4Address);
		ipaddrWanv4Address.setIpAddressDetail(this);

		return ipaddrWanv4Address;
	}

	public IpaddrWanv4Address removeIpaddrWanv4Address(IpaddrWanv4Address ipaddrWanv4Address) {
		getIpaddrWanv4Addresses().remove(ipaddrWanv4Address);
		ipaddrWanv4Address.setIpAddressDetail(null);

		return ipaddrWanv4Address;
	}

	public Set<IpaddrWanv6Address> getIpaddrWanv6Addresses() {
		
		if(ipaddrWanv6Addresses==null) {
			ipaddrWanv6Addresses=new HashSet<>();
		}
		return this.ipaddrWanv6Addresses;
	}

	public void setIpaddrWanv6Addresses(Set<IpaddrWanv6Address> ipaddrWanv6Addresses) {
		this.ipaddrWanv6Addresses = ipaddrWanv6Addresses;
	}

	public IpaddrWanv6Address addIpaddrWanv6Address(IpaddrWanv6Address ipaddrWanv6Address) {
		getIpaddrWanv6Addresses().add(ipaddrWanv6Address);
		ipaddrWanv6Address.setIpAddressDetail(this);

		return ipaddrWanv6Address;
	}

	public IpaddrWanv6Address removeIpaddrWanv6Address(IpaddrWanv6Address ipaddrWanv6Address) {
		getIpaddrWanv6Addresses().remove(ipaddrWanv6Address);
		ipaddrWanv6Address.setIpAddressDetail(null);

		return ipaddrWanv6Address;
	}
	
	public String getPublicNatProvidedBy() {
		return publicNatProvidedBy;
	}

	public void setPublicNatProvidedBy(String publicNatProvidedBy) {
		this.publicNatProvidedBy = publicNatProvidedBy;
	}

	public String getPublicNat() {
		return publicNat;
	}

	public void setPublicNat(String publicNat) {
		this.publicNat = publicNat;
	}

}