package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCITFSView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - ITFS product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCITFSViewRepository extends JpaRepository<ServiceAreaMatrixGSCITFSView, Integer> {

    /**
     * Get all products based on country name
     *
     * @param countyName
     * @return {@link List<ServiceAreaMatrixGSCITFSView>}
     */
    List<ServiceAreaMatrixGSCITFSView> findByIsoCountryName(final String countyName);

    /**
     * Get ISO Code and name where Indicator is 'Yes'
     *
     * @return {@link Set<Map<String, String>>}
     */
    @Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCITFSView s where s.newNumberAvailableIndicator like 'Y'")
    Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryNameWhereIndicatorPresent();

    /**
     * Get ISO Code and name
     *
     * @return {@link Set<Map<String, String>>}
     */
    // TODO : There is no pricing for Uganda and Nepal
    @Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCITFSView s where s.isoCountryName not like 'Uganda' " +
            "and s.isoCountryName not like 'Nepal' and s.newNumberAvailableIndicator like 'Y'")
    Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryNameForACDTFS();

}
