package com.tcl.dias.serviceinventory.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceSla;

import java.util.List;
import java.util.Map;

/**
 * 
 * This file contains the repository class for SIServiceSla entity
 * 
 * @author Prabhu A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SIServiceSlaRepository extends JpaRepository<SIServiceSla, Integer> {

    @Query(value = "select * from si_service_sla where SI_service_detail_id=:serviceId", nativeQuery = true)
    public List<Map<String, Object>> findByServiceId(@Param("serviceId") Integer serviceId);

}
