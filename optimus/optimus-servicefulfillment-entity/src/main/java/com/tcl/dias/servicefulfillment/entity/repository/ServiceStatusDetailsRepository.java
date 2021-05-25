/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;

/**
 * @author vivek
 *
 */
@Repository
public interface ServiceStatusDetailsRepository extends JpaRepository<ServiceStatusDetails, Integer>{

	ServiceStatusDetails findFirstByScServiceDetail_idOrderByIdDesc(Integer serviceId);
	
	List<ServiceStatusDetails> findByScServiceDetail_idAndStatus(Integer serviceId,String status);


}
