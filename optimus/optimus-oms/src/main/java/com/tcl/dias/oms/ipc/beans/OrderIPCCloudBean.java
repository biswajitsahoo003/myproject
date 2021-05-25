package com.tcl.dias.oms.ipc.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderIPCCloudBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String serviceId;

	private String cloudCode;

	private String parentCloudCode;

	private String offeringName;
	
	private String dcCloudType;

	private String dcLocationId;

	private Byte isTaxExempted;

	private Double mrc;

	private Double nrc;

	private Double arc;

	private Double tcv;

	private Byte status;

	private String stage;
	
	private Double ppuRate;

	private List<OrderProductComponentBean> orderProductComponent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	
	public String getDcCloudType() {
		return dcCloudType;
	}

	public void setDcCloudType(String dcCloudType) {
		this.dcCloudType = dcCloudType;
	}

	public String getDcLocationId() {
		return dcLocationId;
	}

	public void setDcLocationId(String dcLocationId) {
		this.dcLocationId = dcLocationId;
	}

	public Byte getIsTaxExempted() {
		return isTaxExempted;
	}

	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

	public List<OrderProductComponentBean> getOrderProductComponentBeans() {
		return orderProductComponent;
	}

	public void setOrderProductComponentBeans(List<OrderProductComponentBean> orderProductComponentBeans) {
		this.orderProductComponent = orderProductComponentBeans;
	}
}