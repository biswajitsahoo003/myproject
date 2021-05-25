package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.Map;

/**
 * This file contains the GvpnInternationalCpeDto.java class.
 * 
 *
 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class GvpnInternationalCpeDto {
	private String routing_protocol;
	private String bom_name;
	private String port_interface;
	private String country;
	private String locationid;
	private String handOff;
	private String pvdmAndMftQuantities;
	private Map<String,Integer> pvdmCardsAndQuantitiesMap;
	private Map<String,Integer> mftCardsAndQuantitiesMap;
	private String cubeLicenses;
	private String accessTopology;
	private String powerCable;
	private String additionalCpeInformation;
	private String typeOfCpe;
	private String sfpModule;
	private String nimModule;
	
	public String getHandOff() {
		return handOff;
	}
	public void setHandOff(String handOff) {
		this.handOff = handOff;
	}
	public String getRouting_protocol() {
		return routing_protocol;
	}
	public void setRouting_protocol(String routing_protocol) {
		this.routing_protocol = routing_protocol;
	}
	public String getBom_name() {
		return bom_name;
	}
	public void setBom_name(String bom_name) {
		this.bom_name = bom_name;
	}
	public String getPort_interface() {
		return port_interface;
	}
	public void setPort_interface(String port_interface) {
		this.port_interface = port_interface;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLocationid() {
		return locationid;
	}
	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}

	public Map<String, Integer> getPvdmCardsAndQuantitiesMap() {
		return pvdmCardsAndQuantitiesMap;
	}

	public void setPvdmCardsAndQuantitiesMap(Map<String, Integer> pvdmCardsAndQuantitiesMap) {
		this.pvdmCardsAndQuantitiesMap = pvdmCardsAndQuantitiesMap;
	}

	public Map<String, Integer> getMftCardsAndQuantitiesMap() {
		return mftCardsAndQuantitiesMap;
	}

	public void setMftCardsAndQuantitiesMap(Map<String, Integer> mftCardsAndQuantitiesMap) {
		this.mftCardsAndQuantitiesMap = mftCardsAndQuantitiesMap;
	}

	public String getCubeLicenses() {
		return cubeLicenses;
	}

	public void setCubeLicenses(String cubeLicenses) {
		this.cubeLicenses = cubeLicenses;
	}

	public String getAccessTopology() {
		return accessTopology;
	}

	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}

	public String getPowerCable() {
		return powerCable;
	}

	public void setPowerCable(String powerCable) {
		this.powerCable = powerCable;
	}

	public String getAdditionalCpeInformation() {
		return additionalCpeInformation;
	}

	public void setAdditionalCpeInformation(String additionalCpeInformation) {
		this.additionalCpeInformation = additionalCpeInformation;
	}

	public String getTypeOfCpe() {
		return typeOfCpe;
	}

	public void setTypeOfCpe(String typeOfCpe) {
		this.typeOfCpe = typeOfCpe;
	}

	public String getSfpModule() {
		return sfpModule;
	}

	public void setSfpModule(String sfpModule) {
		this.sfpModule = sfpModule;
	}

	public String getNimModule() {
		return nimModule;
	}

	public void setNimModule(String nimModule) {
		this.nimModule = nimModule;
	}

	public String getPvdmAndMftQuantities() {
		return pvdmAndMftQuantities;
	}

	public void setPvdmAndMftQuantities(String pvdmAndMftQuantities) {
		this.pvdmAndMftQuantities = pvdmAndMftQuantities;
	}
}
