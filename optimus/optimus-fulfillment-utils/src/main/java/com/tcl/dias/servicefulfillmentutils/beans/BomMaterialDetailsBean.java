package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.ArrayList;
import java.util.List;

public class BomMaterialDetailsBean {

	private String bomCode;

	private String bomName;

	private String bomType;

	private String llBwInMbps;
	
	private String scenarioType;
	private String make;
	
	


	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	private List<MaterialMasterBean> materials;

	public List<MaterialMasterBean> getMaterials() {
		
		if(materials==null) {
			materials=new ArrayList<MaterialMasterBean>();
		}
		return materials;
	}

	public void setMaterials(List<MaterialMasterBean> materials) {
		this.materials = materials;
	}


	public String getBomCode() {
		return this.bomCode;
	}

	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}

	public String getBomName() {
		return this.bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getBomType() {
		return this.bomType;
	}

	public void setBomType(String bomType) {
		this.bomType = bomType;
	}

	public String getLlBwInMbps() {
		return this.llBwInMbps;
	}

	public void setLlBwInMbps(String llBwInMbps) {
		this.llBwInMbps = llBwInMbps;
	}

	public String getScenarioType() {
		return this.scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bomCode == null) ? 0 : bomCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BomMaterialDetailsBean other = (BomMaterialDetailsBean) obj;
		if (bomCode == null) {
			if (other.bomCode != null)
				return false;
		} else if (!bomCode.equals(other.bomCode))
			return false;
		return true;
	}

}
