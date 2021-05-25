package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
/**
 * 
 * This bean binds all the NRC values
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NrcDetailsBean implements Serializable{
	private List<ComponentDetailsBean> componentDetailsBeans;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal nrcTcv;
	public List<ComponentDetailsBean> getComponentDetailsBeans() {
		return componentDetailsBeans;
	}
	public void setComponentDetailsBeans(List<ComponentDetailsBean> componentDetailsBeans) {
		this.componentDetailsBeans = componentDetailsBeans;
	}
	public BigDecimal getNrcTcv() {
		return nrcTcv;
	}
	public void setNrcTcv(BigDecimal nrcTcv) {
		this.nrcTcv = nrcTcv;
	}
}
