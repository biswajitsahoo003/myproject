package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsBySupplierBean {

	private Integer supplierId;
	private String inventoryBankNumberId;
	private String accessNumber;
	@JsonIgnore
	private Integer accessNumberId;
	@JsonIgnore
	private Integer routingId;
	private String routingNo;
	private String routingNoReservationId;
	private Integer orderNoNew;
	private String servAbbr;
	@JsonIgnore
	private String cityCode;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String platformUpdateDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String supplierReadyDate;

	private List<DetailsByCallTypeBean> detailsByCallType;

	public String getSupplierReadyDate() {
		return supplierReadyDate;
	}

	public void setSupplierReadyDate(String supplierReadyDate) {
		this.supplierReadyDate = supplierReadyDate;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getInventoryBankNumberId() {
		return inventoryBankNumberId;
	}

	public void setInventoryBankNumberId(String inventoryBankNumberId) {
		this.inventoryBankNumberId = inventoryBankNumberId;
	}

	public String getAccessNumber() {
		return accessNumber;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	public String getRoutingNo() {
		return routingNo;
	}

	public void setRoutingNo(String routingNo) {
		this.routingNo = routingNo;
	}

	public String getRoutingNoReservationId() {
		return routingNoReservationId;
	}

	public void setRoutingNoReservationId(String routingNoReservationId) {
		this.routingNoReservationId = routingNoReservationId;
	}

	public Integer getOrderNoNew() {
		return orderNoNew;
	}

	public void setOrderNoNew(Integer orderNoNew) {
		this.orderNoNew = orderNoNew;
	}

	public String getServAbbr() {
		return servAbbr;
	}

	public void setServAbbr(String servAbbr) {
		this.servAbbr = servAbbr;
	}

	public String getPlatformUpdateDate() {
		return platformUpdateDate;
	}

	public void setPlatformUpdateDate(String platformUpdateDate) {
		this.platformUpdateDate = platformUpdateDate;
	}

	public List<DetailsByCallTypeBean> getDetailsByCallType() {
		return detailsByCallType;
	}

	public void setDetailsByCallType(List<DetailsByCallTypeBean> detailsByCallType) {
		this.detailsByCallType = detailsByCallType;
	}

	public Integer getAccessNumberId() {
		return accessNumberId;
	}

	public void setAccessNumberId(Integer accessNumberId) {
		this.accessNumberId = accessNumberId;
	}

	public Integer getRoutingId() {
		return routingId;
	}

	public void setRoutingId(Integer routingId) {
		this.routingId = routingId;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
}
