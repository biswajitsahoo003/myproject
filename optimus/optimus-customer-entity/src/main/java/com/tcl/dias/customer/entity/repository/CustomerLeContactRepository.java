package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLeContact;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerLeContactRepository extends JpaRepository<CustomerLeContact, Integer> {

	/**
	 * groupByCustomerLeId native query to fetch unique customer contacts
	 * 
	 * @param customerleid
	 * @return
	 */
	@Query(value = "select g.name, g.email_id from customer_le_contacts g where g.customer_le_id=:customerleid group by g.name, g.email_id", nativeQuery = true)
	List<Map<String, Object>> groupByCustomerLeId(@Param("customerleid") Integer customerleid);

	//List<CustomerLeContact> findByCustomerLeId(Integer custmerLeId);

	List<CustomerLeContact> findByCustomerLeId(Integer custmerLeId);
	
	@Query(value="select * from customer_le_contacts where contact_type is null and customer_le_id=:custmerLeId",nativeQuery = true)
	List<CustomerLeContact> findByCustomerLeIdContactTypeIsNull(@Param("custmerLeId") Integer custmerLeId);

	@Query(value="select * from customer_le_contacts where contact_type is null and email_id =:emailId",nativeQuery = true)
	List<CustomerLeContact> findByEmailIdAndContactTypeIsNull(@Param("emailId") String emailId);

	List<CustomerLeContact> findByEmailId(String emailId);

}
