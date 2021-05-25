package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Immutable
@Table(name="vw_cpe_gsc_bom_dtl")
@IdClass(CpeBomGscDetailViewId.class)
public class CpeBomGscDetailView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="bom_name")
	private String bomName;

	
	@Column(name="bm_bom_name")
	private String bmBomName;
	
	
	@Column(name="bom_type")
	private String bomType;

	@Column(name="product_code")
	private String productCode;
	
	
	@Column(name="long_desc")
	private String longDesc;


	@Id
	@Column(name="port_interface")
	private String portInterface;

	@Id
	@Column(name="routing_protocol")
	private String routingProtocol;

	
	@Column(name="relation")
	private String relation;
	
	
	@Column(name="list_price")
	private Double listPrice;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="product_category")
	private String productCategory;


	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getBmBomName() {
		return bmBomName;
	}

	public void setBmBomName(String bmBomName) {
		this.bmBomName = bmBomName;
	}

	public String getBomType() {
		return bomType;
	}

	public void setBomType(String bomType) {
		this.bomType = bomType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getPortInterface() {
		return portInterface;
	}

	public void setPortInterface(String portInterface) {
		this.portInterface = portInterface;
	}

	public String getRoutingProtocol() {
		return routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	
}