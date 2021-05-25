package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixIAS;

/**
 * This file contains the ServiceAreaMatrixIASRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixIASRepository extends JpaRepository<ServiceAreaMatrixIAS, Integer> {

	List<ServiceAreaMatrixIAS> findByCityDtl(String cityName);

}
