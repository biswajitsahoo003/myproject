package com.tcl.dias.oms.gsc.beans;

import javax.validation.constraints.NotNull;

/**
 * Api Requests for CREATE and UPDATE
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscApiRequest<T> {

	@NotNull(message = "action should not be null")
	private String action;

	@NotNull(message = "data should not be null")
	private T data;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}