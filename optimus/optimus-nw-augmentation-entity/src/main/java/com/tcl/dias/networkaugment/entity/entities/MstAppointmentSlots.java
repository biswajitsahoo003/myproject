package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mst_appointment_slots")
@NamedQuery(name = "MstAppointmentSlots.findAll", query = "SELECT m FROM MstAppointmentSlots m")
public class MstAppointmentSlots implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8322501308173873703L;


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(name="slots")
	private String slots;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getSlots() {
		return slots;
	}


	public void setSlots(String slots) {
		this.slots = slots;
	}
	
	

}
