package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * @author vivek
 *
 */
public class FieldEngineerDetails extends BaseRequest{
	
	private String feName;
	private String feMobileNumber;
	private String feEmail;
	private String isPrimary;
	private String feType;
	private String workType;
    private String secondaryName;
    private String secondaryEmailId;
	private String secondaryContactNumber;
	
	private String type;

	
	private Integer appointmentSlot;
	
	
	
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the secondaryName
	 */
	public String getSecondaryName() {
		return secondaryName;
	}
	/**
	 * @param secondaryName the secondaryName to set
	 */
	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}
	/**
	 * @return the secondaryEmailId
	 */
	public String getSecondaryEmailId() {
		return secondaryEmailId;
	}
	/**
	 * @param secondaryEmailId the secondaryEmailId to set
	 */
	public void setSecondaryEmailId(String secondaryEmailId) {
		this.secondaryEmailId = secondaryEmailId;
	}
	/**
	 * @return the secondaryContactNumber
	 */
	public String getSecondaryContactNumber() {
		return secondaryContactNumber;
	}
	/**
	 * @param secondaryContactNumber the secondaryContactNumber to set
	 */
	public void setSecondaryContactNumber(String secondaryContactNumber) {
		this.secondaryContactNumber = secondaryContactNumber;
	}
	/**
	 * @return the appointmentSlot
	 */
	public Integer getAppointmentSlot() {
		return appointmentSlot;
	}
	/**
	 * @param appointmentSlot the appointmentSlot to set
	 */
	public void setAppointmentSlot(Integer appointmentSlot) {
		this.appointmentSlot = appointmentSlot;
	}
	/**
	 * @return the feName
	 */
	public String getFeName() {
		return feName;
	}
	/**
	 * @param feName the feName to set
	 */
	public void setFeName(String feName) {
		this.feName = feName;
	}
	/**
	 * @return the feMobileNumber
	 */
	public String getFeMobileNumber() {
		return feMobileNumber;
	}
	/**
	 * @param feMobileNumber the feMobileNumber to set
	 */
	public void setFeMobileNumber(String feMobileNumber) {
		this.feMobileNumber = feMobileNumber;
	}
	/**
	 * @return the feEmail
	 */
	public String getFeEmail() {
		return feEmail;
	}
	/**
	 * @param feEmail the feEmail to set
	 */
	public void setFeEmail(String feEmail) {
		this.feEmail = feEmail;
	}
	/**
	 * @return the isPrimary
	 */
	public String getIsPrimary() {
		return isPrimary;
	}
	/**
	 * @param isPrimary the isPrimary to set
	 */
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	/**
	 * @return the feType
	 */
	public String getFeType() {
		return feType;
	}
	/**
	 * @param feType the feType to set
	 */
	public void setFeType(String feType) {
		this.feType = feType;
	}
	/**
	 * @return the workType
	 */
	public String getWorkType() {
		return workType;
	}
	/**
	 * @param workType the workType to set
	 */
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	
	

}
