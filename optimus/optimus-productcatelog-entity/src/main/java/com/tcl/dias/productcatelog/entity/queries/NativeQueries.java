package com.tcl.dias.productcatelog.entity.queries;

/**
 * This file contains the NativeQueries.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NativeQueries {

	private NativeQueries() {
	}

	public static final String GET_PRODUCT_LOCATIONS = "SELECT vpca.location_name FROM vw_product_country_availability vpca where vpca.product_name=:product_name";
}
