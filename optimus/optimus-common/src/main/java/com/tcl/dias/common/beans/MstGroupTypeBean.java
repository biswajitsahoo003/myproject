package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * 
 * Mst Group Type bean class
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstGroupTypeBean implements Serializable{
	private Integer id;
	private String groupTypeCode;
	private String groupType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupTypeCode() {
		return groupTypeCode;
	}
	public void setGroupTypeCode(String groupTypeCode) {
		this.groupTypeCode = groupTypeCode;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	
}
