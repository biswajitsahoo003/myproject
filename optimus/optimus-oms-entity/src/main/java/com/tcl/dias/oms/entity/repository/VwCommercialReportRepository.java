package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.VwCommercialReport;
/**
 * 
 * This file contains Repository class of VwCommercialReport entity
 * 
 *
 * @author Kavya Singh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface VwCommercialReportRepository extends JpaRepository<VwCommercialReport, String>{

}
