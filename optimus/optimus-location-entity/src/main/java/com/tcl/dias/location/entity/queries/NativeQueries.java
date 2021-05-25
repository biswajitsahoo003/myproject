package com.tcl.dias.location.entity.queries;

/**
 * This file contains the NativeQueries.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NativeQueries {

	private NativeQueries() {
	}
	
	public static final String GET_PINCODE_DETAILS = "SELECT con.name as country , st.name as state , cy.name as city,cy.id as cityId , lc.name as locality,pin.code as pincode FROM mst_country con INNER JOIN mst_state st ON con.id = st.country_id INNER JOIN mst_city cy ON st.id = cy.state_id INNER JOIN mst_locality lc ON cy.id = lc.city_id INNER JOIN locality_pincode lp ON lc.id = lp.locality_id INNER JOIN mst_pincode pin ON lp.pincode =  pin.id WHERE pin.code = :pincode AND con.name = :country and pin.city_id=cy.id order by cy.name,lc.name desc";
	
	public static final String GET_PINCODE_DETAILS_CITY_CHANGE = "SELECT con.name as country , st.name as state , cy.name as city,cy.id as cityId , lc.name as locality,pin.code as pincode FROM mst_country con INNER JOIN mst_state st ON con.id = st.country_id INNER JOIN mst_city cy ON st.id = cy.state_id INNER JOIN mst_locality lc ON cy.id = lc.city_id INNER JOIN mst_pincode pin ON cy.id =  pin.city_id WHERE pin.code = :pincode AND con.name = :country and pin.city_id=cy.id order by cy.name,lc.name desc";

	public static final String GET_PINCODE_DETAILS_V1 = "SELECT con.name AS country,st.name AS state,cy.name AS city,cy.id AS cityId,lc.name AS locality,pin.code AS pincode FROM mst_country con INNER JOIN mst_state st ON con.id = st.country_id and st.source=:source INNER JOIN mst_city cy ON st.id = cy.state_id and cy.source=:source INNER JOIN mst_locality lc ON cy.id = lc.city_id and lc.source=:source INNER JOIN locality_pincode lp ON lc.id = lp.locality_id and lp.source=:source INNER JOIN mst_pincode pin ON lp.pincode = pin.id and pin.source=:source WHERE pin.code = :pincode AND con.name = :country and con.source=:source";

	public static final String GET_COUNTRY_DETAILS = "SELECT con.name as country , st.name as state , cy.name as city,cy.id as cityId , lc.name as locality,pin.code as pincode FROM mst_country con INNER JOIN mst_state st ON con.id = st.country_id INNER JOIN mst_city cy ON st.id = cy.state_id INNER JOIN mst_locality lc ON cy.id = lc.city_id INNER JOIN locality_pincode lp ON lc.id = lp.locality_id INNER JOIN mst_pincode pin ON lp.pincode =  pin.id WHERE con.name = :country";
	
	public static final String GET_LOCALITY_DETAILS_BY_PINCODE_AND_CITY = "select lc.name as locality from mst_locality lc where city_id in(select id from mst_city where name=:city) and id in(select locality_id from locality_pincode where pincode in(select id from mst_pincode where code=:pincode))";
	
	public static final String GET_CITY_DETAILS_BY_PINCODE="select cy.name as city,cy.id as cityId from mst_city as cy where id in(select distinct city_id from mst_pincode where code= :code) order by cy.name asc";
}
