package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * AluSchedulerPolicy Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class AluSchedulerPolicyBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer aluSchedulerPolicyId;
	private Timestamp endDate;
	private boolean isEdited;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String sapEgressPolicyname;
	private Boolean sapEgressPreprovisioned;
	private String sapIngressPolicyname;
	private Boolean sapIngressPreprovisioned;
	private Boolean schedulerPolicyIspreprovisioned;
	private String schedulerPolicyName;
	private Timestamp startDate;
	private String totalCirBw;
	private String totalPirBw;
	public Integer getAluSchedulerPolicyId() {
		return aluSchedulerPolicyId;
	}
	public void setAluSchedulerPolicyId(Integer aluSchedulerPolicyId) {
		this.aluSchedulerPolicyId = aluSchedulerPolicyId;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getSapEgressPolicyname() {
		return sapEgressPolicyname;
	}
	public void setSapEgressPolicyname(String sapEgressPolicyname) {
		this.sapEgressPolicyname = sapEgressPolicyname;
	}
	public Boolean getSapEgressPreprovisioned() {
		return sapEgressPreprovisioned;
	}
	public void setSapEgressPreprovisioned(Boolean sapEgressPreprovisioned) {
		this.sapEgressPreprovisioned = sapEgressPreprovisioned;
	}
	public String getSapIngressPolicyname() {
		return sapIngressPolicyname;
	}
	public void setSapIngressPolicyname(String sapIngressPolicyname) {
		this.sapIngressPolicyname = sapIngressPolicyname;
	}
	public Boolean getSapIngressPreprovisioned() {
		return sapIngressPreprovisioned;
	}
	public void setSapIngressPreprovisioned(Boolean sapIngressPreprovisioned) {
		this.sapIngressPreprovisioned = sapIngressPreprovisioned;
	}
	public Boolean getSchedulerPolicyIspreprovisioned() {
		return schedulerPolicyIspreprovisioned;
	}
	public void setSchedulerPolicyIspreprovisioned(Boolean schedulerPolicyIspreprovisioned) {
		this.schedulerPolicyIspreprovisioned = schedulerPolicyIspreprovisioned;
	}
	public String getSchedulerPolicyName() {
		return schedulerPolicyName;
	}
	public void setSchedulerPolicyName(String schedulerPolicyName) {
		this.schedulerPolicyName = schedulerPolicyName;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getTotalCirBw() {
		return totalCirBw;
	}
	public void setTotalCirBw(String totalCirBw) {
		this.totalCirBw = totalCirBw;
	}
	public String getTotalPirBw() {
		return totalPirBw;
	}
	public void setTotalPirBw(String totalPirBw) {
		this.totalPirBw = totalPirBw;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
}