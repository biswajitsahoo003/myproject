package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

public class BomViewId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bomCode;

	private String materialCode;

	public BomViewId(String bomCode, String materialCode) {
		super();
		this.bomCode = bomCode;
		this.materialCode = materialCode;

	}

	public BomViewId() {

	}

}
