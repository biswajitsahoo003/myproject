package com.tcl.dias.location.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "demarcation")
@NamedQuery(name = "Demarcation.findAll", query = "SELECT d FROM Demarcation d")
public class Demarcation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String appartment;

	@Column(name = "building_altitude")
	private String buildingAltitude;

	private String floor;

	private String room;

	private String tower;

	private String wing;

	private String zone;
	
	@Column(name = "building_name")
	private String buildingName;

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	// bi-directional many-to-one association to Location
	@OneToMany(mappedBy = "demarcation")
	private Set<Location> locations;

	public Demarcation() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppartment() {
		return this.appartment;
	}

	public void setAppartment(String appartment) {
		this.appartment = appartment;
	}

	public String getBuildingAltitude() {
		return this.buildingAltitude;
	}

	public void setBuildingAltitude(String buildingAltitude) {
		this.buildingAltitude = buildingAltitude;
	}

	public String getFloor() {
		return this.floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoom() {
		return this.room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getTower() {
		return this.tower;
	}

	public void setTower(String tower) {
		this.tower = tower;
	}

	public String getWing() {
		return this.wing;
	}

	public void setWing(String wing) {
		this.wing = wing;
	}

	public String getZone() {
		return this.zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Set<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public Location addLocation(Location location) {
		getLocations().add(location);
		location.setDemarcation(this);

		return location;
	}

	public Location removeLocation(Location location) {
		getLocations().remove(location);
		location.setDemarcation(null);

		return location;
	}

}