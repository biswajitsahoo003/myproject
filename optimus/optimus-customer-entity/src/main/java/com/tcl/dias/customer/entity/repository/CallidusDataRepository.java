package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CallidusData;
/**
 * Repository class for Callidus Data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface CallidusDataRepository extends JpaRepository<CallidusData, Integer> {

    List<CallidusData> findAllByPartnerIdIn(List<String> partnerLeId);

    /**
     * Method to get commissions data by data range
     *
     * @param partnerCUIDs
     * @param startDate
     * @param endDate
     * @return {@link List}
     */
    @Query(value ="SELECT  * FROM callidus_data where  partner_ID in (:partnerCUIDs) and STR_TO_DATE(comp_date, '%d-%M-%Y') between STR_TO_DATE(:startDate, '%d-%M-%Y') and STR_TO_DATE(:endDate, '%d-%M-%Y')",nativeQuery =true)
    List<CallidusData> findCommissionDataByDateRange(@Param("partnerCUIDs")List<String> partnerCUIDs, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
