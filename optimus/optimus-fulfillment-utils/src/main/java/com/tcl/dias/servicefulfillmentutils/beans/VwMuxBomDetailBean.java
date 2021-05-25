package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.ArrayList;
import java.util.List;

public class VwMuxBomDetailBean {

	private String bomCode;

	private String bomName;

	private String bomType;

	private String cableType;

	private String muxinterface;

	private String lineRate;

	private String make;

	private int maxLlBwInMbps;

	private int minLlBwInMbps;

	private String model;

	private String topologyType;

	private List<MaterialMasterBean> materials;

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

	public String getCableType() {
		return this.cableType;
	}

	public void setCableType(String cableType) {
		this.cableType = cableType;
	}

	public String getLineRate() {
		return this.lineRate;
	}

	public void setLineRate(String lineRate) {
		this.lineRate = lineRate;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public int getMaxLlBwInMbps() {
		return this.maxLlBwInMbps;
	}

	public void setMaxLlBwInMbps(int maxLlBwInMbps) {
		this.maxLlBwInMbps = maxLlBwInMbps;
	}

	public int getMinLlBwInMbps() {
		return this.minLlBwInMbps;
	}

	public void setMinLlBwInMbps(int minLlBwInMbps) {
		this.minLlBwInMbps = minLlBwInMbps;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMuxinterface() {
		return muxinterface;
	}

	public void setMuxinterface(String muxinterface) {
		this.muxinterface = muxinterface;
	}

	public String getTopologyType() {
		return topologyType;
	}

	public void setTopologyType(String topologyType) {
		this.topologyType = topologyType;
	}

	public List<MaterialMasterBean> getMaterials() {

		if (materials == null) {
			materials = new ArrayList<MaterialMasterBean>();
		}
		return materials;
	}

	public void setMaterials(List<MaterialMasterBean> materials) {
		this.materials = materials;
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
		VwMuxBomDetailBean other = (VwMuxBomDetailBean) obj;
		if (bomCode == null) {
			if (other.bomCode != null)
				return false;
		} else if (!bomCode.equals(other.bomCode))
			return false;
		return true;
	}

}
