package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
	
	List<FieldEngineer> findByServiceIdAndAppointmentTypeInOrderByIdDesc( Integer serviceId, List<String> types);
	
	List<FieldEngineer> findByServiceIdAndAppointmentType( Integer serviceId, String type);

}
