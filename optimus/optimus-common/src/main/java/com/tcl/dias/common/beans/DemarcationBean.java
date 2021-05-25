package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * This file contains the DemarcationBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DemarcationBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3995028574045814533L;
	
	private Integer locationId;


	private Integer id;

	private String appartment;

	private String buildingAltitude;

	private String floor;

	private String room;

	private String tower;

	private String wing;

	private String zone;
	
	private String buildingName;
	
	private Boolean AendDemarc;
	
	

	public Boolean getAendDemarc() {
		return AendDemarc;
	}

	public void setAendDemarc(Boolean aendDemarc) {
		AendDemarc = aendDemarc;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the appartment
	 */
	public String getAppartment() {
		return appartment;
	}

	/**
	 * @param appartment the appartment to set
	 */
	public void setAppartment(String appartment) {
		this.appartment = appartment;
	}

	/**
	 * @return the buildingAltitude
	 */
	public String getBuildingAltitude() {
		return buildingAltitude;
	}

	/**
	 * @param buildingAltitude the buildingAltitude to set
	 */
	public void setBuildingAltitude(String buildingAltitude) {
		this.buildingAltitude = buildingAltitude;
	}

	/**
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @return the tower
	 */
	public String getTower() {
		return tower;
	}

	/**
	 * @param tower the tower to set
	 */
	public void setTower(String tower) {
		this.tower = tower;
	}

	/**
	 * @return the wing
	 */
	public String getWing() {
		return wing;
	}

	/**
	 * @param wing the wing to set
	 */
	public void setWing(String wing) {
		this.wing = wing;
	}

	/**
	 * @return the zone
	 */
	public String getZone() {
		return zone;
	}

	/**
	 * @param zone the zone to set
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	@Override
	public String toString() {
		return "DemarcationBean{" +
				"locationId=" + locationId +
				", id=" + id +
				", appartment='" + appartment + '\'' +
				", buildingAltitude='" + buildingAltitude + '\'' +
				", floor='" + floor + '\'' +
				", room='" + room + '\'' +
				", tower='" + tower + '\'' +
				", wing='" + wing + '\'' +
				", zone='" + zone + '\'' +
				", buildingName='" + buildingName + '\'' +
				", AendDemarc=" + AendDemarc +
				'}';
	}
}
