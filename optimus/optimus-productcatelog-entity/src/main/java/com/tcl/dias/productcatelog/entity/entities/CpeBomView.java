package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity class to load CPE BOM View
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Immutable
@Table(name = "vw_cpe_bom")
@IdClass(CpeBomViewId.class)
public class CpeBomView implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "bomId")
	private Integer bomId;
	
	@Column(name = "bomType")
	private String bomType;


	@Column(name = "bomName")
	private String bomName;

	@Column(name = "hsnCode")
	private String hsnCode;

	@Column(name = "lastMileBandwidth")
	private Integer lastMileBandwidth;

	@Id
	@Column(name = "lastMileBandwidthId")
	private Integer lastMileBandwidthId;

	@Column(name = "maxBandwidth")
	private Integer maxBandwidth;
	
	@Id
	@Column(name = "maxBandwidthId")
	private Integer maxBandwidthId;

	@Column(name = "minBandwidth")
	private Integer minBandwidth;

	@Id
	@Column(name = "minBandwidthId")
	private Integer minBandwidthId;

	@Column(name = "portInterface")
	private String portInterface;

	@Id
	@Column(name = "portInterfaceId")
	private Integer portInterfaceId;

	@Column(name = "productCode")
	private String productCode;

	@Column(name = "routingProtocol")
	private String routingProtocol;

	@Id
	@Column(name = "routingProtocolId")
	private Integer routingProtocolId;

	@Id
	@Column(name = "resourceId")
	private Integer resourceId;
	
	@Column(name = "listPrice")
	private Double listPrice;

	
	@Column(name = "relation")
	private String relation;

	

	public String getBomName() {
		return this.bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getHsnCode() {
		return this.hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	

	public int getLastMileBandwidthId() {
		return this.lastMileBandwidthId;
	}

	public void setLastMileBandwidthId(Integer lastMileBandwidthId) {
		this.lastMileBandwidthId = lastMileBandwidthId;
	}


	public int getMaxBandwidthId() {
		return this.maxBandwidthId;
	}



	public String getPortInterface() {
		return this.portInterface;
	}

	public void setPortInterface(String portInterface) {
		this.portInterface = portInterface;
	}

	public int getPortInterfaceId() {
		return this.portInterfaceId;
	}


	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getRoutingProtocol() {
		return this.routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public int getRoutingProtocolId() {
		return this.routingProtocolId;
	}

	
	public void setListPrice (Double listPrice) {
		this.listPrice = listPrice;
	}
	
	public Double getListPrice () {
		return this.listPrice;
	}

	public Integer getBomId() {
		return bomId;
	}

	public void setBomId(Integer bomId) {
		this.bomId = bomId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public void setMaxBandwidthId(Integer maxBandwidthId) {
		this.maxBandwidthId = maxBandwidthId;
	}


	public Integer getLastMileBandwidth() {
		return lastMileBandwidth;
	}

	public void setLastMileBandwidth(Integer lastMileBandwidth) {
		this.lastMileBandwidth = lastMileBandwidth;
	}

	public Integer getMaxBandwidth() {
		return maxBandwidth;
	}

	public void setMaxBandwidth(Integer maxBandwidth) {
		this.maxBandwidth = maxBandwidth;
	}

	public Integer getMinBandwidth() {
		return minBandwidth;
	}

	public void setMinBandwidth(Integer minBandwidth) {
		this.minBandwidth = minBandwidth;
	}

	public String getBomType() {
		return bomType;
	}

	public void setBomType(String bomType) {
		this.bomType = bomType;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setMinBandwidthId(Integer minBandwidthId) {
		this.minBandwidthId = minBandwidthId;
	}

	public void setPortInterfaceId(Integer portInterfaceId) {
		this.portInterfaceId = portInterfaceId;
	}

	public void setRoutingProtocolId(Integer routingProtocolId) {
		this.routingProtocolId = routingProtocolId;
	}

}
