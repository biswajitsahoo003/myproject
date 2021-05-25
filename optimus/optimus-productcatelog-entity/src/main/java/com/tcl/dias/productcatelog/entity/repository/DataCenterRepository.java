package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixDataCenter;
/**
 * This file contains the DataCenterRepository.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface DataCenterRepository extends JpaRepository<ServiceAreaMatrixDataCenter, Integer> {
	
	List<ServiceAreaMatrixDataCenter> findByDcTypeAndIsActiveAndIdIn(String dcType,String activeFlag,List<Integer> dataCenterIds);

	List<ServiceAreaMatrixDataCenter> findByDcTypeAndIsActive(String dcType,String activeFlag);

	List<ServiceAreaMatrixDataCenter> findByDcTypeAndTownsDtlAndIsActive(String dcType,String townName,String activeFlag);

	@Query(value = "select distinct Towns_dtl from service_area_matrix_DataCenter where dc_type='IDC' and is_active_ind ='Y'", nativeQuery = true)
	List<String> findDistinctCitiesForDC();
}
