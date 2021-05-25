package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDROfferingHSNCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

/**
 * Repository interface for TeamsDROfferingHSNCode
 *
 * @author Syed Ali.
 * @createdAt 23/12/2020, Wednesday, 11:40
 */

@Repository
public interface TeamsDROfferingHSNCodeRepository extends JpaRepository<TeamsDROfferingHSNCode, Integer> {

	@Query(value = "SELECT bundled_offering_nm as bundledOfferningNm,atomic_offering_nm as atomicOfferingNm,"
			+ "charge_nm as chargeNm, vendor_nm as vendorNm , charge_line_item as chargeLineItem, hsn_code as hsnCode "
			+ "FROM vw_mstmdr_offering_hsn_code;", nativeQuery = true)
	Set<Map<String, String>> findAllHsnCodes();
}
