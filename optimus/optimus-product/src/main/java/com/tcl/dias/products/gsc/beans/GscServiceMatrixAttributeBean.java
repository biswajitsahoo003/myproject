package com.tcl.dias.products.gsc.beans;

/**
 * Country level service attributes Bean for GSC Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscServiceMatrixAttributeBean {

	private String description;
	private String value;

	/**
	 * Convert to bean
	 *
	 * @param description
	 * @param value
	 * @return
	 */
	public static GscServiceMatrixAttributeBean toAttributeBean(final String description, final String value) {
		GscServiceMatrixAttributeBean gscServiceMatrixAttributeBean = new GscServiceMatrixAttributeBean();
		gscServiceMatrixAttributeBean.setDescription(description);
		gscServiceMatrixAttributeBean.setValue(value);
		return gscServiceMatrixAttributeBean;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
