package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.Partner;

/**
 * Partner Entity Repository Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
	
	/**
	 * 
	 * Find all partner with pagination
	 * @param specification
	 * @param pageable
	 * @return
	 */
	Page<Partner> findAll(Specification<Partner> specification, Pageable pageable);

	/**
	 * Find Partner Details For Notification
	 *
	 * @param partnerLeID
	 * @return
	 */
	@Query(value = "select p.name as PartnerAccountName, p.account_id_18 as PartnerAccountId, sap.code_value as PartnerOrgId, ple.entity_name as PartnerLeName, " +
			"ple.tps_sfdc_cuid as PartnerLeCUID from partner p left join partner_legal_entity ple on p.id = ple.partner_id " +
			"left join partner_legal_entity_sap_code sap on sap.partner_le_id = ple.id " +
			"where sap.code_type = 'SECS Code' and ple.id =:partnerLeID", nativeQuery = true)
	List<Map<String, Object>> findPartnerDetailsForNotification(@Param("partnerLeID") String partnerLeID);
}
