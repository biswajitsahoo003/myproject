package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCACANSView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - Audio conference product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCACANSViewRepository
        extends JpaRepository<ServiceAreaMatrixGSCACANSView, Integer> {

    /**
     * Get products based on country name
     *
     * @param countryName
     * @return {@link ServiceAreaMatrixGSCACANSView}
     */
    List<ServiceAreaMatrixGSCACANSView> findByIsoCountryName(final String countryName);

    /**
     * Get ISO Code and name
     *
     * @return {@link Set<Map<String, String>>}
     */
    @Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCACANSView s")
    Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryName();

    @Query(value = "select distinct s.cityCode as code, s.cityName as name from ServiceAreaMatrixGSCACANSView s where s.cityCode is not null and s.cityName is not null")
    Set<Map<String, Object>> findAcansCityCodesAndNames();
}
