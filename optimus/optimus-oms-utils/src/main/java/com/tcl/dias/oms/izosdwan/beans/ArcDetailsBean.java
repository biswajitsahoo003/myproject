package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
/**
 * 
 * This bean binds the ARC price details component wise
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ArcDetailsBean implements Serializable{
	private List<ComponentDetailsBean> componentDetailsBeans;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal arcTcv;
	public List<ComponentDetailsBean> getComponentDetailsBeans() {
		return componentDetailsBeans;
	}
	public void setComponentDetailsBeans(List<ComponentDetailsBean> componentDetailsBeans) {
		this.componentDetailsBeans = componentDetailsBeans;
	}
	public BigDecimal getArcTcv() {
		return arcTcv;
	}
	public void setArcTcv(BigDecimal arcTcv) {
		this.arcTcv = arcTcv;
	}
	
}
