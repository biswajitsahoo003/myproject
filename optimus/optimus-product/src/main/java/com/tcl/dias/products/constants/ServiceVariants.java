package com.tcl.dias.products.constants;

/**
 * This file contains the ServiceVariants.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum ServiceVariants {
	
	STANDARD(1),
	PREMIUM(2),
	COMPRESSED(3),
	ENHANCED(4);
	
	private int id;

	public int getId() {
		return id;
	}

	private ServiceVariants(int id) {
		this.id = id;
	}

}
