package com.tcl.dias.location.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;

/**
 * LocationLeCustomerRepository for LocationLeCustomer entity
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface LocationLeCustomerRepository extends JpaRepository<LocationLeCustomer, Integer> {

	List<LocationLeCustomer> findByErfCusCustomerLeIdIn(List<Integer> customerLeId);

	@Query(value = "SELECT l.id as locationId,csl.name as name,csl.email_id as email,csl.contact_number as contactNo, llc.erf_cus_customer_le_id as erfCustomerLeId,csl.id as localItContactId FROM location_le_customer llc INNER JOIN location l ON llc.location_id = l.id LEFT JOIN customer_site_location_it_contact csl ON csl.customer_le_location_id=llc.id where llc.erf_cus_customer_le_id IN (:customerLeIds)", nativeQuery = true)
	List<Map<String, Object>> findLoclItContactsByLeId(Set<Integer> customerLeIds);

	Optional<LocationLeCustomer> findByErfCusCustomerLeIdAndLocation(Integer erfcuscustomerleId, Location location);

	List<LocationLeCustomer> findByErfCusCustomerLeId(Integer erfCusCustomerLeId);

	List<LocationLeCustomer> findByLocation_Id(Integer locationId);

	@Query(value = "select a.address_line_one as addressLineOne,a.address_line_two as addressLineTwo,a.city as city,a.state as state,a.country as country,a.locality as locality,a.pincode as pincode,l.lat_long as latLong,l.id as locationId,a.source as source from location_le_customer as llc,location as l,mst_address as a where llc.location_id=l.id and l.address_id=a.id and llc.erf_cus_customer_le_id in (:customerLeIds) group by a.id", nativeQuery = true)
	List<Map<String, Object>> findLocationsByLe(@Param("customerLeIds") Set<Integer> customerLeId);
}