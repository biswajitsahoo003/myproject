package com.tcl.dias.common.serviceinventory.beans;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author SanjeKum
 *
 */
@JsonInclude(NON_NULL)
public class SICustomerInfoBean implements Serializable {
	private Integer id;
	private String cuId;
	private String ehsId;
	private String ehsAddress;
	private String ehsLocId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCuId() {
		return cuId;
	}
	public void setCuId(String cuId) {
		this.cuId = cuId;
	}
	public String getEhsId() {
		return ehsId;
	}
	public void setEhsId(String ehsId) {
		this.ehsId = ehsId;
	}
	public String getEhsAddress() {
		return ehsAddress;
	}
	public void setEhsAddress(String ehsAddress) {
		this.ehsAddress = ehsAddress;
	}
	public String getEhsLocId() {
		return ehsLocId;
	}
	public void setEhsLocId(String ehsLocId) {
		this.ehsLocId = ehsLocId;
	}

	@Override
	public String toString() {
		return "SICustomerInfoBean{" +
				"id=" + id +
				", cuId='" + cuId + '\'' +
				", ehsId='" + ehsId + '\'' +
				", ehsAddress='" + ehsAddress + '\'' +
				", ehsLocId='" + ehsLocId + '\'' +
				'}';
	}
}
