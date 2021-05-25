package com.tcl.dias.servicefulfillmentutils.beans;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * Bean class for Field Engineer Details
 */
public class FieldEngineerDetailsBean extends TaskDetailsBaseBean {

    private String name;
    private String contactNumber;
    private String emailId;
    private String type;
    private String secondaryName;
    private String secondaryEmailId;
	private String secondaryContactNumber;
	
	private Integer appointmentSlot;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String appointmentDate;
	
	   
    public Integer getAppointmentSlot() {
		return appointmentSlot;
	}

	public void setAppointmentSlot(Integer appointmentSlot) {
		this.appointmentSlot = appointmentSlot;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getSecondaryName() {
		return secondaryName;
	}

	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}

	public String getSecondaryEmailId() {
		return secondaryEmailId;
	}

	public void setSecondaryEmailId(String secondaryEmailId) {
		this.secondaryEmailId = secondaryEmailId;
	}

	public String getSecondaryContactNumber() {
		return secondaryContactNumber;
	}

	public void setSecondaryContactNumber(String secondaryContactNumber) {
		this.secondaryContactNumber = secondaryContactNumber;
	}
   
	
}
