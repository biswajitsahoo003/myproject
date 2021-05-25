package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCPSTNView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - PSTN access type
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCPSTNViewRepository extends JpaRepository<ServiceAreaMatrixGSCPSTNView, Integer> {

	@Query(value = "select distinct s.country_cd as code, s.country_nm as name, s.intl_dial_cd as isdCode from vw_service_area_pstn_destination_GSC s", nativeQuery = true)
	Set<Map<String, Object>> findAllCountry();
}
