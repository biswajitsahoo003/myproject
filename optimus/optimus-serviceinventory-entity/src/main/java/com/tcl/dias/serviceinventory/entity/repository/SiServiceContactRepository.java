package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SiServiceContact;

/**
 * 
 * This file contains the repository class for SIOrder entity
 * 
 * @author Manojkumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface SiServiceContactRepository extends JpaRepository<SiServiceContact, Integer> {

	/* Query to get service contact detail based on service detail Id*/
	@Query(value = "SELECT contact_name as contactName,SI_service_detail_id as siServiceDetailId,business_email as businessEmail,contact_type as contactType,business_mobile as businessMobile,business_phone as businessPhone FROM si_service_contacts where SI_service_detail_id =:siServiceDetailId", nativeQuery = true)
	List<Map<String, Object>> findBySiServiceDetail_Id(@Param("siServiceDetailId") Integer siServiceDetailId);

}
