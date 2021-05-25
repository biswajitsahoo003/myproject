package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
/**
 * This file contains the ServiceCosCriteriaRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceCosCriteriaRepository extends JpaRepository<ServiceCosCriteria, Integer> {
	
	List<ServiceCosCriteria> findByClassificationCriteriaAndServiceQo_ServiceQosIdAndEndDateIsNull(String classificationCriteria,Integer serviceQosId);
	
	List<ServiceCosCriteria> findByServiceQo_ServiceQosIdAndEndDateIsNull(Integer serviceQosId);
	
	List<ServiceCosCriteria> findByServiceQo_ServiceQosIdAndEndDateIsNullOrderByCosNameAsc(Integer serviceQosId);
}
