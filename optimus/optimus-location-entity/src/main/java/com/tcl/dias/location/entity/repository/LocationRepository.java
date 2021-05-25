package com.tcl.dias.location.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.type.TrueFalseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.Location;
import java.lang.Integer;

/**
 * This file contains the LocationRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	Optional<List<Location>> findByIdIn(List<Integer> locationIds);
	
	Location findByPopLocationId(String popLocationId);
	
	Location findByApiAddressId(Integer locationIds);
	
	List<Location> findByAddressId(Integer addressid);

	/**
	 * Used to fetch the location address using the list of customerIds 
	 * @param type
	 * @return List 
	 */
	@Query(value="SELECT locationTbl.id,customerLocationTbl.erf_cus_customer_id,locationTbl.lat_long,addressTbl.address_line_one,addressTbl.address_line_two,addressTbl.pincode,addressTbl.locality,addressTbl.city,addressTbl.state,addressTbl.country FROM location locationTbl inner join customer_location customerLocationTbl on locationTbl.id=customerLocationTbl.location_id inner join mst_address addressTbl on locationTbl.address_id=addressTbl.id where customerLocationTbl.erf_cus_customer_id in (:cusIds);",nativeQuery=true)
	List<Map<String,Object>> getLocationDetail(@Param("cusIds") List<Integer> cusIds);
	
	/**
	 * Used to fetch the location address using the locationType 
	 * @param type
	 * @return List
	 */
	@Query(value="SELECT location.id AS locationID, location.lat_long AS LatLong, address.address_line_one AS AddressLine1, address.address_line_two AS AddressLine2, address.pincode AS Pincode,  address.locality AS Locality,  address.city AS City, address.state AS State, address.country AS Country FROM location AS location INNER JOIN mst_address AS address ON  location.address_id = address.id WHERE location.pop_location_id LIKE :type% ",nativeQuery=true)
	List<Map<String, Object>> getAddressDetails(@Param("type") String type);
	
	/**
	 * 
	 * Get India based locations
	 * @param locationIds
	 * @return
	 */
	@Query(value="SELECT location.id as locId from location as location INNER JOIN mst_address AS address on location.address_id=address.id where location.id in (:locIds) and address.country='INDIA'",nativeQuery = true)
	List<Map<String,Object>> getIndiaContainingAddressByLocationIds(@Param("locIds") List<Integer> locationIds);
	
	/**
	 * 
	 * Get International locations by locations IDS
	 * @param locationIds
	 * @return
	 */
	@Query(value="SELECT location.id as locId from location as location INNER JOIN mst_address AS address on location.address_id=address.id where location.id in (:locIds) and address.country not in('INDIA')",nativeQuery = true)
	List<Map<String,Object>> getNonIndiaContainingAddressByLocationIds(@Param("locIds") List<Integer> locationIds);
}
