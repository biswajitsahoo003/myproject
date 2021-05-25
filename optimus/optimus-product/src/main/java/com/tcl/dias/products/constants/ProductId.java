package com.tcl.dias.products.constants;

/**
 * This file contains the abbreviation of a product and its ID value from database across it for global use
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum ProductId {

	GVPN(1),
	GSIP(2),
	IAS(3),
	IZO(4),
	NPL(5),
	CDN(6);
	
	private int id;

	
	public static ProductId valueOf(Integer id) {
	    for (ProductId product : values()) {
	        if (Integer.valueOf(product.getId()).equals(id)) {
	            return product;
	        }
	    }    
	    throw new IllegalArgumentException(id.toString());
	}
	

	public int getId() {
		return id;
	}

	private ProductId(int id) {
		this.id = id;
	}
	
}
