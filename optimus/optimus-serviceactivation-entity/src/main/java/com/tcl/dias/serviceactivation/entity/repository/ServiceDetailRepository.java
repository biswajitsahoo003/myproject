package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;

/**
 * This file contains the ServiceDetailRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
//to user service state for fetching
@Repository
public interface ServiceDetailRepository extends JpaRepository<ServiceDetail, Integer> {

	List<ServiceDetail> findByServiceId(String serviceId);

	ServiceDetail findFirstByServiceIdAndVersionOrderByIdDesc(String serviceId, Integer version);

	ServiceDetail findFirstByServiceIdOrderByVersionDesc(String serviceId);

	ServiceDetail findFirstByServiceIdAndServiceStateOrderByVersionDesc(String serviceId, String serviceState);
	
	ServiceDetail findFirstByScServiceDetailIdAndServiceStateOrderByVersionDesc(Integer serviceId, String serviceState);
	
	ServiceDetail findFirstByScServiceDetailIdAndServiceStateOrderByIdDesc(Integer serviceId, String serviceState);

	ServiceDetail findFirstByServiceIdAndEndDateIsNotNullOrderByVersionDesc(String serviceId);

	ServiceDetail findFirstByServiceIdAndServiceStateAndEndDateIsNullOrderByVersionDesc(String serviceId,
			String serviceState);

	Long countByServiceId(String serviceId);


    ServiceDetail findFirstByServiceIdAndServiceStateAndEndDateIsNotNullOrderByEndDate(String serviceId, String issued);

    ServiceDetail findFirstByServiceIdAndEndDateIsNull(String serviceCode);

	ServiceDetail findFirstByServiceIdAndEndDateIsNotNullOrderByEndDateDesc(String serviceId);

	ServiceDetail findFirstByServiceIdAndEndDateIsNullOrderByVersionDesc(String serviceId);
	
	ServiceDetail findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(String serviceId,
			List<String> serviceState);

	ServiceDetail findFirstByServiceIdAndServiceStateOrderByIdDesc(String serviceId, String active);

	ServiceDetail findFirstByServiceIdAndOrderDetailAndServiceStateInAndEndDateIsNullOrderByVersionDesc(
			String serviceCode, OrderDetail orderDetail, List<String> asList);

	ServiceDetail findByServiceIdAndOrderDetailAndVersion(String serviceCode, OrderDetail orderDetail, Integer version);

	ServiceDetail findFirstByServiceIdAndEndDateIsNullOrderByIdDesc(String serviceId);

    ServiceDetail findFirstByServiceIdAndModifiedByInOrderByVersionDesc(String serviceId, List<String> modifiers);

	ServiceDetail findFirstByScServiceDetailIdAndServiceStateOrderByVersionDesc(String scServiceDetailId, String serviceState);

	ServiceDetail findFirstByServiceIdAndServiceStateInAndModifiedByNotInAndEndDateIsNullOrderByVersionDesc(String serviceId, List<String> serviceStates, List<String> modifiedBys);

	ServiceDetail findFirstByScServiceDetailIdAndServiceStateInAndModifiedByNotInOrderByVersionDesc(Integer scServiceDetailId, List<String> serviceStates, List<String> modifiedBys);

    ServiceDetail findFirstByServiceIdOrderByIdDesc(String serviceCode);

    ServiceDetail findFirstByScServiceDetailIdOrderByVersionDesc(Integer serviceId);
    
    ServiceDetail findFirstByServiceIdAndServiceStateAndModifiedByInOrderByVersionDesc(String serviceCode, String serviceState, List<String> modifiedBys);

	ServiceDetail findFirstByServiceIdAndServiceStateAndEndDateIsNull(String serviceCode, String issued);
	
	ServiceDetail findFirstByServiceIdAndScServiceDetailIdAndServiceStateOrderByVersionDesc(String serviceId,Integer scServiceDetailId, String serviceState);
	
	@Query(value = "select distinct(version) from service_details where service_id = :serviceCode", nativeQuery = true)
	List<Integer> getVersionByServiceCode(String serviceCode);
}
