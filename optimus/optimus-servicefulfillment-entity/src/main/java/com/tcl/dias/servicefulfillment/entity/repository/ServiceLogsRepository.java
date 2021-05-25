/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ServiceLogs;

/**
 * @author vivek
 *
 */
@Repository
public interface ServiceLogsRepository extends JpaRepository<ServiceLogs, Integer>{
	
	List<ServiceLogs> findByServiceIdAndType(Integer serviceId, String type);
 
}
