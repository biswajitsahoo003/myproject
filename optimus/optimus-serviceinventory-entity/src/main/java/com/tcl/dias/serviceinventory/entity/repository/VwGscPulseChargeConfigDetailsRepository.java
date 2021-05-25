package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.ViewGscPulseChargeConfigDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This file contains the VwGscPulseChargeConfigDetailsRepository.java class.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface VwGscPulseChargeConfigDetailsRepository extends JpaRepository<ViewGscPulseChargeConfigDetails, Integer> {

    @Query(value = "select distinct iso_ctry_cd as code, COUNTRY_NM as name, intnl_ctry_dial_cd as isdCode " +
            "from VW_GSC_PULSE_CHRG_CONFIG_DTLS where PROD_OFFERING_NM = :productName and SECS_ID in (:secsId)", nativeQuery = true)
    Set<Map<String, Object>> findAllCountriesByProductNameAndSecsId(@Param("productName") String productName, @Param("secsId") List<String> secsId);

    @Query(value = "select distinct PROD_OFFERING_NM from VW_GSC_PULSE_CHRG_CONFIG_DTLS where SECS_ID in (:secsId)", nativeQuery = true)
    List<String> findProductNamesBySecsId(@Param("secsId") List<String> secsId);

}
