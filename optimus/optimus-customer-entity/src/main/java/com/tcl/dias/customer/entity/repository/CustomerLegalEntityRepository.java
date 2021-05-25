package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;

/**
 * This file contains the CustomerLegal EntityRepository Details
 * 
 *
 * @author NITHYA V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface CustomerLegalEntityRepository extends JpaRepository<CustomerLegalEntity, Integer> {

	List<CustomerLegalEntity> findByCustomerId(Integer customerId);

	CustomerLegalEntity findAllById(Integer id);
	List<CustomerLegalEntity> findAllByIdIn(List<Integer> ids);
	
	List<CustomerLegalEntity> findByCustomerIdIn(List<Integer> customerIds);

	@Query(value = "select * from customer_legal_entity cle left join customer c on cle.customer_id = c.id " +
			"left join customer_le_country country on cle.id = country.customer_le_id " +
			"left join mst_countries mc on country.country_id = mc.id " +
			"where c.id =:customerId and mc.name !=:country", nativeQuery = true)
	List<CustomerLegalEntity> findAllExceptGivenCountry(@Param("customerId") Integer customerId, @Param("country") String country);

	@Query(value = "select * from customer_legal_entity cle left join customer c on cle.customer_id = c.id " +
			"left join customer_le_country country on cle.id = country.customer_le_id " +
			"left join mst_countries mc on country.country_id = mc.id " +
			"where c.id =:customerId and mc.name =:country", nativeQuery = true)
	List<CustomerLegalEntity> findAllByGivenCountry(@Param("customerId") Integer customerId, @Param("country") String country);
	
	@Query(value = "select cle.id from customer_legal_entity as cle where cle.customer_id=:customerId",nativeQuery=true)
	List<Integer> getLeIdsBasedOnCustomerId(@Param("customerId") Integer customerId);

	/**
	 * Find All Customer Legal Entity By Partner Legal Entity
	 *
	 * @param partnerLeId
	 * @return {@link List<CustomerLegalEntity>}
	 */
	@Query(value="select cle.* from customer_legal_entity cle left join partner_legal_entity ple on cle.partner_le_id = ple.id " +
			"where ple.id=:partnerLeId", nativeQuery = true)
	List<CustomerLegalEntity> findAllCustomerLegalEntityByPartnerLegalEntity(Integer partnerLeId);

	/**
	 * Find By Entity and Country
	 *
	 * @param customerLeName
	 * @param countryName
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select cle.id,cle.entity_name,cle.customer_id from customer_legal_entity cle " +
			"left join customer_le_country country on cle.id = country.customer_le_id " +
			"left join mst_countries mc on country.country_id = mc.id " +
			"where cle.entity_name like (%:customerLeName% )and mc.name =:countryName", nativeQuery = true)
	List<Map<String, Object>> findByEntityNameAndCountry(@Param("customerLeName") String customerLeName,@Param("countryName") String countryName);

	/**
	 * Find Customer Legal Entity by Name
	 *
	 * @param entityName
	 * @return
	 */
	Optional<CustomerLegalEntity> findByEntityName(String entityName);

	/**
	 * Find All Customer Legal Entity By Sap Code
	 *
	 * @param sapCodes
	 * @return
	 */
	@Query(value = "select sap.code_value, cle.entity_name, cle.id as le_id from customer_legal_entity_sap_code sap inner join customer_legal_entity cle on sap.customer_le_id=cle.id where sap.code_value in (:sapCodes)", nativeQuery = true)
	List<Map<String, Object>> findAllCustomerLegalEntityBySapCode(@Param("sapCodes") List<String> sapCodes);

	/**
	 * Find All Customer Legal Entity By Billing Account Ids
	 *
	 * @param billingAccountIds
	 * @return
	 */
	@Query(value = "select bill.bill_acc_no as code_value, cle.entity_name, cle.id as le_id from customer_le_billing_info bill inner join customer_legal_entity cle on bill.customer_le_id = cle.id where bill.bill_acc_no in (:billingAccountIds) group by bill.customer_le_id", nativeQuery = true)
	List<Map<String, Object>> findAllCustomerLegalEntityByBillingAccountIds(@Param("billingAccountIds") List<String> billingAccountIds);
	
	/**
	 * 
	 * Find all customer legalentity with pagination
	 * @param specification
	 * @param pageable
	 * @return
	 */
	Page<CustomerLegalEntity> findAll(Specification<CustomerLegalEntity> specification, Pageable pageable);


	@Query(value = "select * from customer_legal_entity cle where cle.tps_sfdc_cuid=:sfdcCuid", nativeQuery = true)
	CustomerLegalEntity findCustomerLeByCuid(@Param("sfdcCuid") String sfdcCuid);

}
