package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.WebexSkuDetails;

/**
 * The Repository class for the vw_ucaas_webex_cca_audio_plans_per_seat_sku_dtl
 * database table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface WebexSkuDetailsRepository extends JpaRepository<WebexSkuDetails, Integer> {
	/**
	 * Get Sku Details
	 * 
	 * @param licenseType
	 * @param audioModel
	 * @param bridgeRegion
	 * @return {@link List< WebexSkuDetails >}
	 */
	@Query(value = "select s from WebexSkuDetails s where s.licenseTypeAbbr=:licenseType and s.audioPlanAbbr=:audioPlan and s.audioPlanBridge=:bridgeRegion ")
	List<WebexSkuDetails> findSku(@Param("licenseType") String licenseType, @Param("audioPlan") String audioModel,
                                  @Param("bridgeRegion") String bridgeRegion);
}
