package com.tcl.dias.oms.gvpn.pdf.beans;

/**
 * 
 * This file contains the MultiVrfBean.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MultiVrfBean {

	private String name;
	private String category;
	private String portBandwidth;
	private Double totalArc;
	private Double totalNrc;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	public Double getTotalArc() {
		return totalArc;
	}
	public void setTotalArc(Double totalArc) {
		this.totalArc = totalArc;
	}
	public Double getTotalNrc() {
		return totalNrc;
	}
	public void setTotalNrc(Double totalNrc) {
		this.totalNrc = totalNrc;
	}
	@Override
	public String toString() {
		return "MultiVrfBean [name=" + name + ", category=" + category + ", portBandwidth=" + portBandwidth
				+ ", totalArc=" + totalArc + ", totalNrc=" + totalNrc + "]";
	}
	
	
}
