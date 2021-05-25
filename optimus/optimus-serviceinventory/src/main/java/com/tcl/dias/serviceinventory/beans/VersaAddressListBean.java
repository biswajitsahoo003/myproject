package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_group.Group;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_list.Address;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Service;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_custom_service.ServiceFirewall;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url.UrlCategory;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list.Zone;

import java.util.List;
import java.util.Set;

/**
 * Bean for fetching list of address/groups/zones/urls
 * 
 * @author Srinivasa Raghavan
 */
public class VersaAddressListBean {
	private List<Zone> zones;
	private List<Address> addresses;
	private List<Group> addressGroups;
	private List<String> predefinedUrls;
	private List<UrlCategory> userdefinedUrls;
	private List<Service> predefinedServices;
	private List<ServiceFirewall> userdefinedServices;
	private Set<String> asscSrcZones;
	private Set<String> asscSrcAddresses;
	private Set<String> asscSrcAddressGroups;
	private Set<String> asscDestZones;
	private Set<String> asscDestAddresses;
	private Set<String> asscDestAddressGroups;
	private Set<String> associatedPredefinedUrls;
	private Set<String> associatedUserDefinedUrls;
	private Set<String> asscPredefinedServices;
	private Set<String> asscUserdefinedServices;
	
	
	

	public Set<String> getAsscPredefinedServices() {
		return asscPredefinedServices;
	}

	public void setAsscPredefinedServices(Set<String> asscPredefinedServices) {
		this.asscPredefinedServices = asscPredefinedServices;
	}

	public Set<String> getAsscUserdefinedServices() {
		return asscUserdefinedServices;
	}

	public void setAsscUserdefinedServices(Set<String> asscUserdefinedServices) {
		this.asscUserdefinedServices = asscUserdefinedServices;
	}

	public List<Service> getPredefinedServices() {
		return predefinedServices;
	}

	public void setPredefinedServices(List<Service> predefinedServices) {
		this.predefinedServices = predefinedServices;
	}

	public List<ServiceFirewall> getUserdefinedServices() {
		return userdefinedServices;
	}

	public void setUserdefinedServices(List<ServiceFirewall> userdefinedServices) {
		this.userdefinedServices = userdefinedServices;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Group> getAddressGroups() {
		return addressGroups;
	}

	public void setAddressGroups(List<Group> addressGroups) {
		this.addressGroups = addressGroups;
	}

	public List<String> getPredefinedUrls() {
		return predefinedUrls;
	}

	public void setPredefinedUrls(List<String> predefinedUrls) {
		this.predefinedUrls = predefinedUrls;
	}

	public List<UrlCategory> getUserdefinedUrls() {
		return userdefinedUrls;
	}

	public void setUserdefinedUrls(List<UrlCategory> userdefinedUrls) {
		this.userdefinedUrls = userdefinedUrls;
	}

	public Set<String> getAsscSrcZones() {
		return asscSrcZones;
	}

	public void setAsscSrcZones(Set<String> asscSrcZones) {
		this.asscSrcZones = asscSrcZones;
	}

	public Set<String> getAsscSrcAddresses() {
		return asscSrcAddresses;
	}

	public void setAsscSrcAddresses(Set<String> asscSrcAddresses) {
		this.asscSrcAddresses = asscSrcAddresses;
	}

	public Set<String> getAsscSrcAddressGroups() {
		return asscSrcAddressGroups;
	}

	public void setAsscSrcAddressGroups(Set<String> asscSrcAddressGroups) {
		this.asscSrcAddressGroups = asscSrcAddressGroups;
	}

	public Set<String> getAsscDestZones() {
		return asscDestZones;
	}

	public void setAsscDestZones(Set<String> asscDestZones) {
		this.asscDestZones = asscDestZones;
	}

	public Set<String> getAsscDestAddresses() {
		return asscDestAddresses;
	}

	public void setAsscDestAddresses(Set<String> asscDestAddresses) {
		this.asscDestAddresses = asscDestAddresses;
	}

	public Set<String> getAsscDestAddressGroups() {
		return asscDestAddressGroups;
	}

	public void setAsscDestAddressGroups(Set<String> assDestAddressGroups) {
		this.asscDestAddressGroups = assDestAddressGroups;
	}

	public Set<String> getAssociatedPredefinedUrls() {
		return associatedPredefinedUrls;
	}

	public void setAssociatedPredefinedUrls(Set<String> associatedPredefinedUrls) {
		this.associatedPredefinedUrls = associatedPredefinedUrls;
	}

	public Set<String> getAssociatedUserDefinedUrls() {
		return associatedUserDefinedUrls;
	}

	public void setAssociatedUserDefinedUrls(Set<String> associatedUserDefinedUrls) {
		this.associatedUserDefinedUrls = associatedUserDefinedUrls;
	}
}
