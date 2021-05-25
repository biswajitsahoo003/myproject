package com.tcl.dias.productcatelog.entity.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRLicensePrices;

/**
 * Repository for teamsDR license prices
 * 
 * @author Srinivasa Raghavan
 */
@Repository
public interface TeamsDRLicensePricesRepository extends JpaRepository<TeamsDRLicensePrices, String> {

	/**
	 * To return unique license agreement types
	 *
	 * @return
	 */
	@Query(value = "SELECT v.agreement from TeamsDRLicensePrices v ")
	Set<String> findDistinctAgreementType();


//	@Query(value = "SELECT v.dispNm from TeamsDRLicensePrices v where v.cd IN (:codes) and v.agreement=:agreementType and v.provider=:provider")
//	Set<String> findByCodesAndTypeAndProvider(@Param("codes") Set<String> codes,
//			@Param("agreementType") String agreementType, @Param("provider") String provider);


}
