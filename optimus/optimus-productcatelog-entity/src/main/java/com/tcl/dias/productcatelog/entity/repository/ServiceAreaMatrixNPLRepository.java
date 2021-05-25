package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixNPL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the ServiceAreaMatrixNPLRepository.java class.
 * 
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixNPLRepository extends JpaRepository<ServiceAreaMatrixNPL, Integer> {

	List<ServiceAreaMatrixNPL> findByTownsDtl(String cityName);

}
