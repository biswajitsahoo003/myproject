package com.tcl.dias.serviceactivation.beans;

import java.io.Serializable;

/**
 * MstGvpnAluCustIdBean class
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MstGvpnAluCustIdBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String aluCustId;
	private String crnId;
	private String customerName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAluCustId() {
		return aluCustId;
	}

	public void setAluCustId(String aluCustId) {
		this.aluCustId = aluCustId;
	}

	public String getCrnId() {
		return crnId;
	}

	public void setCrnId(String crnId) {
		this.crnId = crnId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
