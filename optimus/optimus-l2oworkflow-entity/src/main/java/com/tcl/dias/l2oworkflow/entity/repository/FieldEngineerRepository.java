package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.FieldEngineer;

/**
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface FieldEngineerRepository extends JpaRepository<FieldEngineer, Integer> {

	@Query(value = "SELECT id,service_id,task_id,name,appointment_id,type,mobile,email,secondary_name,secondary_email, secondary_mobile FROM field_engineer where service_id=:serviceId and type=:type order By id DESC limit 1", nativeQuery = true)
	Map<String, Object> findByServiceId(@Param("serviceId") Integer serviceId, @Param("type") String type);
	
	FieldEngineer findFirstByServiceIdAndAppointmentTypeOrderByIdDesc( Integer serviceId, String type);
	
	
}
