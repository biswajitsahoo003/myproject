package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.ServiceFulfillmentJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceFulfillmentJobRepository extends JpaRepository<ServiceFulfillmentJob, Integer> {

	@Query(value = "SELECT * FROM service_fulfillment_jobs where status=:status and type=:type and retry_count <=10 order by id asc", nativeQuery = true)
	List<ServiceFulfillmentJob> findByStatusAndType(@Param("status") String status,@Param("type") String type);
	
	//List<ServiceFulfillmentJob> findByServiceUuidAndTypeAndStatus(String serviceUUid,String type,String status);
	
	List<ServiceFulfillmentJob> findByErfOdrServiceIdAndTypeAndStatus(Integer erfOdrServiceId,String type,String status);

	ServiceFulfillmentJob findFirstByServiceCodeAndServiceIdAndTypeAndStatus(String serviceCode, Integer serviceId, String type, String status);
}
