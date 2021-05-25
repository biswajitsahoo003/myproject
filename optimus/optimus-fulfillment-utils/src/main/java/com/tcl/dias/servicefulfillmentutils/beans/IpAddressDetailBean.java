package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * IpAddressDetail Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class IpAddressDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer IP_Address_Details;
	private Timestamp endDate;
	private Boolean extendedLanEnabled;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String nmsServiceIpv4Address;
	private String nniVsatIpaddress;
	private Integer noMacAddress;
	private String pathIpType;
	private String pingAddress1;
	private String pingAddress2;
	private String additionalIps;
	private String ipAddressArrangement;
	private String ipv4AddressPoolSize;
	private String ipv6AddressPoolSize;
	private String ipv4AddressPoolSizeForAdditionalIps;
	private String ipv6AddressPoolSizeForAdditionalIps;
	private Timestamp startDate;
	private boolean isEdited;
	
	private Set<IpaddrLanv4AddressBean> ipaddrLanv4Addresses;

	
	private Set<IpaddrLanv6AddressBean> ipaddrLanv6Addresses;

	private Set<IpaddrWanv4AddressBean> ipaddrWanv4Addresses;
	
	private Set<IpaddrWanv6AddressBean> ipaddrWanv6Addresses;
	
	private String publicNATIpProvidedBy;
	private String publicNATIp;

	public Integer getIP_Address_Details() {
		return IP_Address_Details;
	}

	public void setIP_Address_Details(Integer iP_Address_Details) {
		IP_Address_Details = iP_Address_Details;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Boolean getExtendedLanEnabled() {
		return extendedLanEnabled;
	}

	public void setExtendedLanEnabled(Boolean extendedLanEnabled) {
		this.extendedLanEnabled = extendedLanEnabled;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNmsServiceIpv4Address() {
		return nmsServiceIpv4Address;
	}

	public void setNmsServiceIpv4Address(String nmsServiceIpv4Address) {
		this.nmsServiceIpv4Address = nmsServiceIpv4Address;
	}

	public String getNniVsatIpaddress() {
		return nniVsatIpaddress;
	}

	public void setNniVsatIpaddress(String nniVsatIpaddress) {
		this.nniVsatIpaddress = nniVsatIpaddress;
	}

	public Integer getNoMacAddress() {
		return noMacAddress;
	}

	public void setNoMacAddress(Integer noMacAddress) {
		this.noMacAddress = noMacAddress;
	}

	public String getPathIpType() {
		return pathIpType;
	}

	public void setPathIpType(String pathIpType) {
		this.pathIpType = pathIpType;
	}

	public String getPingAddress1() {
		return pingAddress1;
	}

	public void setPingAddress1(String pingAddress1) {
		this.pingAddress1 = pingAddress1;
	}

	public String getPingAddress2() {
		return pingAddress2;
	}

	public void setPingAddress2(String pingAddress2) {
		this.pingAddress2 = pingAddress2;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<IpaddrLanv4AddressBean> getIpaddrLanv4Addresses() {
		if(ipaddrLanv4Addresses==null){
			ipaddrLanv4Addresses = new HashSet<>();
		}
		return ipaddrLanv4Addresses;
	}

	public void setIpaddrLanv4Addresses(Set<IpaddrLanv4AddressBean> ipaddrLanv4Addresses) {
		this.ipaddrLanv4Addresses = ipaddrLanv4Addresses;
	}

	public Set<IpaddrLanv6AddressBean> getIpaddrLanv6Addresses() {
		if(ipaddrLanv6Addresses==null){
			ipaddrLanv6Addresses = new HashSet<>();
		}
		return ipaddrLanv6Addresses;
	}

	public void setIpaddrLanv6Addresses(Set<IpaddrLanv6AddressBean> ipaddrLanv6Addresses) {
		this.ipaddrLanv6Addresses = ipaddrLanv6Addresses;
	}

	public Set<IpaddrWanv4AddressBean> getIpaddrWanv4Addresses() {
		if(ipaddrWanv4Addresses==null){
			ipaddrWanv4Addresses = new HashSet<>();
		}
		return ipaddrWanv4Addresses;
	}

	public void setIpaddrWanv4Addresses(Set<IpaddrWanv4AddressBean> ipaddrWanv4Addresses) {
		this.ipaddrWanv4Addresses = ipaddrWanv4Addresses;
	}

	public Set<IpaddrWanv6AddressBean> getIpaddrWanv6Addresses() {
		if(ipaddrWanv6Addresses==null){
			ipaddrWanv6Addresses = new HashSet<>();
		}
		return ipaddrWanv6Addresses;
	}

	public void setIpaddrWanv6Addresses(Set<IpaddrWanv6AddressBean> ipaddrWanv6Addresses) {
		this.ipaddrWanv6Addresses = ipaddrWanv6Addresses;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAdditionalIps() {
		return additionalIps;
	}

	public void setAdditionalIps(String additionalIps) {
		this.additionalIps = additionalIps;
	}

	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	public String getIpv4AddressPoolSizeForAdditionalIps() {
		return ipv4AddressPoolSizeForAdditionalIps;
	}

	public void setIpv4AddressPoolSizeForAdditionalIps(String ipv4AddressPoolSizeForAdditionalIps) {
		this.ipv4AddressPoolSizeForAdditionalIps = ipv4AddressPoolSizeForAdditionalIps;
	}

	public String getIpv6AddressPoolSizeForAdditionalIps() {
		return ipv6AddressPoolSizeForAdditionalIps;
	}

	public void setIpv6AddressPoolSizeForAdditionalIps(String ipv6AddressPoolSizeForAdditionalIps) {
		this.ipv6AddressPoolSizeForAdditionalIps = ipv6AddressPoolSizeForAdditionalIps;
	}
	
	public String getPublicNATIpProvidedBy() {
		return publicNATIpProvidedBy;
	}

	public void setPublicNATIpProvidedBy(String publicNATIpProvidedBy) {
		this.publicNATIpProvidedBy = publicNATIpProvidedBy;
	}

	public String getPublicNATIp() {
		return publicNATIp;
	}

	public void setPublicNATIp(String publicNATIp) {
		this.publicNATIp = publicNATIp;
	}
}