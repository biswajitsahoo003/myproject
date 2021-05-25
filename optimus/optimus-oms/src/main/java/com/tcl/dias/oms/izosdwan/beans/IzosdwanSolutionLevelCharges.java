package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * This bean binds solution level charges for IZOSDWAN
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanSolutionLevelCharges implements Serializable{
	private String name;
	private BigDecimal arc;
	private BigDecimal nrc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getArc() {
		return arc;
	}
	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}
	public BigDecimal getNrc() {
		return nrc;
	}
	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}
	
}
