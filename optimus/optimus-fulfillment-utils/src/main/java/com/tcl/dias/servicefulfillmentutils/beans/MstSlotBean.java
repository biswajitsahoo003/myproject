package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillment.entity.entities.MstAppointmentSlots;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for getting slot details
 */
public class MstSlotBean {
	
	private String slot;
	
	private Integer id;

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public MstSlotBean(MstAppointmentSlots mstAppointmentSlots) {
		this.slot=mstAppointmentSlots.getSlots();
		this.id=mstAppointmentSlots.getId();
	}
	
	
	

}
