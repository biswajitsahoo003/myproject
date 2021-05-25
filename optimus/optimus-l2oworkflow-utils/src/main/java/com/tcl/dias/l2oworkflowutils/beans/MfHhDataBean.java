package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;

import com.tcl.dias.l2oworkflow.entity.entities.MfHhData;

/**
 * Bean class for MfHhData
 * @author archchan
 *
 */
public class MfHhDataBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String numHh;
	private String hhState;
	private String hhLatitude;
	private String hhLongitude;
	
	public MfHhDataBean(MfHhData mfHhData) {
		this.id = mfHhData.getId();
		this.numHh = mfHhData.getNumHh();
		this.hhState = mfHhData.getHhState();
		this.hhLatitude = mfHhData.getHhLatitude();
		this.hhLongitude = mfHhData.getHhLongitude();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNumHh() {
		return numHh;
	}
	public void setNumHh(String numHh) {
		this.numHh = numHh;
	}
	public String getHhState() {
		return hhState;
	}
	public void setHhState(String hhState) {
		this.hhState = hhState;
	}
	public String getHhLatitude() {
		return hhLatitude;
	}
	public void setHhLatitude(String hhLatitude) {
		this.hhLatitude = hhLatitude;
	}
	public String getHhLongitude() {
		return hhLongitude;
	}
	public void setHhLongitude(String hhLongitude) {
		this.hhLongitude = hhLongitude;
	}
	
	

}
