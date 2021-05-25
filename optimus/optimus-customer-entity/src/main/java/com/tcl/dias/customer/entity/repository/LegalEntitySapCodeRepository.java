package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.LegalEntitySapCode;

@Repository
public interface LegalEntitySapCodeRepository extends JpaRepository<LegalEntitySapCode, Integer> {
	
	List<LegalEntitySapCode> findBycustomerLeIdInAndCodeType(List<Integer> customerLeIDs,String spaCode);
	 
	@Query (value ="SELECT concat(cc.code_value, sc.code_value) entitycode from customer_legal_entity_sap_code sc, customer_legal_entity_company_code cc " +
			"where cc.customer_le_sap_code_id=sc.id and sc.customer_le_id in (:leIds) and sc.code_type=:sapCodeType", nativeQuery=true)
	public List<String> findCpnyAndSapCodeDetails(@Param("leIds") List<Integer> leIds,@Param("sapCodeType") String sapCodeType);

	@Query (value ="SELECT * from customer_legal_entity_sap_code where customer_le_id=:leId and code_value not like '%TC%' ",nativeQuery =true)
	public List<LegalEntitySapCode> findCustomerleIdSapDetails(@Param("leId") Integer leId);
	
	@Query (value = "SELECT * from customer_legal_entity_sap_code where customer_le_id=:leId and code_value not like '%TC%' and code_type not like '%SECS%' order by id desc LIMIT 1", nativeQuery = true)
	public LegalEntitySapCode findLatestCustomerleIdSapDetails(@Param("leId") Integer leId);

	@Query (value = "SELECT * from customer_legal_entity_sap_code where customer_le_id=:leId and code_type like '%SECS%' order by id desc LIMIT 1", nativeQuery = true)
	public LegalEntitySapCode findLeSecsCode(@Param("leId") Integer leId);

	@Query (value = "SELECT * from customer_legal_entity_sap_code where customer_le_id=:leId and code_type like '%SECS%'", nativeQuery = true)
	public List<LegalEntitySapCode> findLesSecsCode(@Param("leId") Integer leId);

	@Query(value = "SELECT code_value from customer_legal_entity_sap_code where customer_le_id = :leId " +
			"and code_type like '%SECS%' and secs_sap_flag = 'PRIMARY' order by id desc LIMIT 1",nativeQuery = true)
	String findSecsCodeByCustomerLeIdForGSC(@Param("leId") Integer leId);

	@Query(value = "select * from customer_legal_entity_sap_code where code_value =:codeValue", nativeQuery = true)
	LegalEntitySapCode findIdBySecsCode(@Param("codeValue") Integer codeValue);
	
}
