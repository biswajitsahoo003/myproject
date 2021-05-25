package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This file contains the QuoteRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer>, CrudRepository<Quote, Integer>, JpaSpecificationExecutor<Quote> {

	Quote findByQuoteCode(String quoteCode);
	Quote findFirstByQuoteCodeOrderByCreatedTimeDesc(String quoteCode);
	
	@Query(value = "select qle.tps_sfdc_opty_id from quote q, quote_to_le qle where q.id=qle.quote_id and q.quote_code=:quoteCode", nativeQuery = true)
	List<String> findTpsSfdcOptyIdByQuoteCode(@Param("quoteCode")  String quoteCode);
	
	@Query(value = "select qle.tps_sfdc_opty_id as optyId,qle.quote_type as quoteType,qle.id as id from quote q, quote_to_le qle where q.id=qle.quote_id and q.quote_code=:quoteCode", nativeQuery = true)
	List<Map<String,Object>> findTpsSfdcOptyIdAndQuoteTypeByQuoteCode(@Param("quoteCode")  String quoteCode);
	
	@Query(value = "SELECT tps_sfdc_opty_id FROM quote_ill_site_to_service  where erf_service_inventory_tps_service_id=:serviceId and quote_to_le_id=:quoteToLe", nativeQuery = true)
	List<String> findTpsSfdcOptyIdByQuoteToLeAndServiceId(@Param("quoteToLe")  Integer quoteToLe,@Param("serviceId")  String serviceId);
	
	@Query(value = "SELECT erf_service_inventory_tps_service_id as serviceId FROM quote_ill_site_to_service  where tps_sfdc_opty_id=:optyId and quote_to_le_id=:quoteToLe", nativeQuery = true)
	List<String> findServiceIdByQuoteToLeAndOptyId(@Param("quoteToLe")  Integer quoteToLe,@Param("optyId")  String optyId);

	List<Quote> findByCustomerIdAndStatus(Integer customerId, byte status);

	Quote findByIdAndStatus(Integer quoteId, byte status);

	Optional<Quote> findById(Integer quoteId);

	List<Quote> findTop100ByOrderByCreatedTimeDesc();

	Page<Quote> findByQuoteCodeContainsAllIgnoreCase(String searchTerm, Pageable pageable);

	Page<Quote> findAllByOrderByCreatedTimeDesc(Pageable pageable);

	/**
	 * Find Active Quote Configurations by CustomerLeId
	 * 
	 * @param customerLeIds
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "where qle.erf_cus_customer_legal_entity_id in(:custLeIds) and q.status=:status and qle.stage <> :stage group by q.id order by q.id desc)\r\n"
			+ "UNION\r\n"
			+ "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where qle.erf_cus_customer_legal_entity_id in(:custLeIds) and q.status=:status and qle.stage <> :stage group by q.id order by q.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsByCustomerLeId(@Param("custLeIds") Set<Integer> customerLeIds,
			@Param("stage") String stage,@Param("status") Integer status);
	
	@Query(value = "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "where qle.erf_cus_customer_legal_entity_id in(:custLeIds) and q.status=:status and qle.stage <> :stage group by q.id order by q.id desc)\r\n"
			+ "UNION\r\n"
			+ "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where qle.erf_cus_customer_legal_entity_id in(:custLeIds) and q.status=:status and qle.stage <> :stage group by q.id order by q.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsCountByCustomerLeId(@Param("custLeIds") Set<Integer> customerLeIds,
			@Param("stage") String stage,@Param("status") Integer status);
	
	@Query(value = "select 	count(1) from quote q,quote_to_le qle where qle.quote_id=q.id and qle.erf_cus_customer_legal_entity_id in (:custLeIds) and qle.stage!='Order Enrichment'", nativeQuery = true)
	Integer findTotalQuoteCountByLe(@Param("custLeIds") Set<Integer> customerLeIds);
	
	@Query(value = "select 	count(1) from quote q,quote_to_le qle where qle.quote_id=q.id and qle.erf_cus_customer_legal_entity_id is null and q.customer_id=:customerId and qle.stage!='Order Enrichment'", nativeQuery = true)
	Integer findTotalQuoteCountByCustomerId(@Param("customerId") Integer customerId);
	
	@Query(value = "select * from quote q, quote_to_le qle where qle.quote_id=q.id and qle.erf_cus_customer_legal_entity_id=:customerLeId and q.customer_id=:customerId and q.quote_code like :quoteCode% and q.status=:status", nativeQuery = true)
	List<Quote> findActiveQuotesByCustomerIdAndCustomerLeId(@Param("customerId")  Integer customerId, @Param("customerLeId")  Integer customerLeId, @Param("quoteCode")  String quoteCode, @Param("status")  Integer status);

	/**
	 * Find Active Quote Configurations by CustomerLeId
	 * 
	 * @param customerLeIds
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "SELECT dummy.* from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_customer_view as isCustomerView,q.created_by as createdBy \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "where q.engagement_to_opportunity_id is null and q.customer_id in (:customerId) and q.status=:status and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10))  and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and (q.ns_quote='N' or q.ns_quote is null) and qle.stage not in (:quoteStage) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT  q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount,qap.is_customer_view as isCustomerView,q.created_by as createdBy  \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.engagement_to_opportunity_id is null and (qap.is_customer_view is null or  (qap.is_customer_view=1 or qap.is_customer_view=10)) and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and q.status=:status and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended,  ole.order_type as orderType, ole.order_category as orderCategory,mpf.name as productName , null as quoteStage, ole.stage as orderStage, null as quoteCode, o.order_code as orderCode,\r\n"
			+ "o.created_time as createdTime, count(ois.id) as siteCount,null as isCustomerView,o.created_by as createdBy  from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id \r\n"
			+ " LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id \r\n"
			+ " LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id \r\n"
			+ " LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ " LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id \r\n"
			+ " LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ " where o.customer_id in (:customerId) and o.engagement_to_opportunity_id is null and mpf.id!=2 and o.id is not null \r\n"
			+ " and (ole.erf_cus_customer_legal_entity_id in(:custLeIds) or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) \r\n"
			+ " group by o.id, o.order_code, mpf.name, ole.stage, o.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory, mpf.name as productName , null as quoteStage, ole.stage as orderStage, null as quoteCode, o.order_code as orderCode,\r\n"
			+ "o.created_time as createdTime, count(ois.id) as siteCount,null as isCustomerView,o.created_by as createdBy \r\n"
			+ "from quote q LEFT JOIN quote_to_le qtl on qtl.quote_id = q.id\r\n"
			+ "LEFT JOIN orders o on o.order_code =  q.quote_code\r\n"
			+ "LEFT JOIN order_to_le ole on ole.order_id = o.id \r\n"
			+ " LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id \r\n"
			+ " LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id \r\n"
			+ " LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ " LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id \r\n"
			+ " LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ " where o.customer_id in (:customerId) and o.engagement_to_opportunity_id is not null and mpf.id!=2 and o.id is not null \r\n"
			+ " and (ole.erf_cus_customer_legal_entity_id in(:custLeIds) or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and qtl.stage = :partnerPlacedQuoteStage and (ole.order_category is null or ole.order_category <> :orderCategory) \r\n"
			+ " group by o.id, o.order_code, mpf.name, ole.stage, o.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT null as quoteId,  o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended,ole.order_type as orderType, ole.order_category as orderCategory, mpf.name as productName , null as quoteStage, ole.stage as orderStage, null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,  count(ogs.id) as siteCount,null as isCustomerView,o.created_by as createdBy  \r\n"
			+ "from quote q LEFT JOIN quote_to_le qtl on qtl.quote_id = q.id\r\n"
			+ "LEFT JOIN orders o on o.order_code =  q.quote_code\r\n"
			+ "LEFT JOIN order_to_le ole on ole.order_id = o.id \r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where o.customer_id in (:customerId) and o.engagement_to_opportunity_id is not null and qtl.stage = :partnerPlacedQuoteStage and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null)\r\n"
			+ "and ole.stage = :orderStage and  mpf.id=2 and o.id is not null and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id, o.order_code, mpf.name, ole.stage, o.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT null as quoteId,  o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory,mpf.name as productName , null as quoteStage, ole.stage as orderStage, null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,  count(ogs.id) as siteCount,null as isCustomerView ,o.created_by as createdBy \r\n"
			+ "from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where o.customer_id in (:customerId) and o.engagement_to_opportunity_id is null and ole.stage = :orderStage and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null)\r\n"
			+ "and ole.stage = :orderStage and  mpf.id=2 and o.id is not null and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id, o.order_code, mpf.name, ole.stage, o.created_time) dummy\r\n"
			+ "order by createdTime desc\r\n", countQuery = "SELECT count(*) from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName , qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_customer_view as isCustomerView,q.created_by as createdBy  \r\n" +
					"			from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"			 LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"			LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"			LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"			LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"			LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id \r\n" + 
					"			where  (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and q.engagement_to_opportunity_id is null and q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time\r\n" +
					"			UNION ALL\r\n" +
					"			SELECT  q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage,null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount ,qap.is_customer_view as isCustomerView,q.created_by as createdBy \r\n" +
					"			from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"			 LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"			LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"			LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"			LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"			LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n" + 
					"			where  (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and q.engagement_to_opportunity_id is null and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and q.status=:status and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time\r\n" +
					"			UNION ALL\r\n" + 
					"			SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended,ole.order_type as orderType, ole.order_category as orderCategory, mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,\r\n" + 
					"			o.created_time as createdTime, count(ois.id) as siteCount,null as isCustomerView,o.created_by as createdBy  from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id \r\n" + 
					"			 LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id \r\n" + 
					"			 LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id \r\n" + 
					"			 LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"			 LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id \r\n" + 
					"			 LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n" + 
					"			 where o.engagement_to_opportunity_id is null and o.customer_id in (:customerId) and mpf.id!=2 and o.id is not null \r\n" +
					"			 and (ole.erf_cus_customer_legal_entity_id in(:custLeIds) or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) \r\n" + 
					"			 group by o.id, o.order_code, mpf.name, ole.stage, o.created_time UNION ALL\r\n" +
					"			SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended,ole.order_type as orderType, ole.order_category as orderCategory, mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,\r\n" +
					"			o.created_time as createdTime, count(ois.id) as siteCount,null as isCustomerView,o.created_by as createdBy \r\n" +
					"	         from quote q LEFT JOIN quote_to_le qtl on qtl.quote_id = q.id LEFT JOIN orders o on o.order_code =  q.quote_code LEFT JOIN order_to_le ole on ole.order_id = o.id \r\n" +
					"			 LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id \r\n" +
					"			 LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id \r\n" +
					"			 LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" +
					"			 LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id \r\n" +
					"			 LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n" +
					"			 where o.engagement_to_opportunity_id is not null and o.customer_id in (:customerId) and mpf.id!=2 and o.id is not null \r\n" +
					"			 and (ole.erf_cus_customer_legal_entity_id in(:custLeIds) or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and qtl.stage = :partnerPlacedQuoteStage and (ole.order_category is null or ole.order_category <> :orderCategory) \r\n" +
					"			 group by o.id, o.order_code, mpf.name, ole.stage, o.created_time UNION ALL\r\n" +
					"			SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory, mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,  count(ogs.id) as siteCount,null as isCustomerView,o.created_by as createdBy  \r\n" +
					"			from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" +
					"			LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" +
					"			LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" +
					"			LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" +
					"			LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" +
					"			LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n" +
					"			where o.engagement_to_opportunity_id is null and o.customer_id in (:customerId) and ole.stage = :orderStage and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null)\r\n" +
					"			and ole.stage = :orderStage and  mpf.id=2 and o.id is not null and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id, o.order_code, mpf.name, ole.stage, o.created_time\r\n" +
					"			UNION ALL\r\n"+
					"			SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory,mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,  count(ogs.id) as siteCount,null as isCustomerView ,o.created_by as createdBy \r\n" +
					"			from quote q LEFT JOIN quote_to_le qtl on qtl.quote_id = q.id LEFT JOIN orders o on o.order_code =  q.quote_code LEFT JOIN order_to_le ole on ole.order_id = o.id \n\r\n" +
					"			LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"			LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"			LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"			LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"			LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n" + 
					"			where o.engagement_to_opportunity_id is not null and o.customer_id in (:customerId) and qtl.stage = :partnerPlacedQuoteStage and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null)\r\n" +
					"			and ole.stage = :orderStage and  mpf.id=2 and o.id is not null and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id, o.order_code, mpf.name, ole.stage, o.created_time) dummy\r\n" + 
					"			order by createdTime desc\r\n", nativeQuery = true)
	Page<Map<String,Object>> findActiveConfigurations(@Param("customerId") List<Integer> customerId,
			@Param("custLeIds") Set<Integer> customerLeIds, @Param("quoteStage") Set<String> quoteStage,
			@Param("orderStage") String orderStage, @Param("orderCategory") String orderCategory, @Param("partnerPlacedQuoteStage") String partnerPlacedQuoteStage,Pageable pageable, @Param("status") Integer status);

	
	/**
	 * Find Active Quote Configurations by CustomerLeId
	 * 
	 * @param customerLeIds
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "SELECT dummy.* from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_customer_view as isCustomerView,q.created_by as createdBy \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "where q.engagement_to_opportunity_id is null and q.customer_id in (:customerId) and q.status=:status and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10))  and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and (q.ns_quote='N' or q.ns_quote is null) and qle.stage not in (:quoteStage) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT  q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount,qap.is_customer_view as isCustomerView,q.created_by as createdBy  \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.engagement_to_opportunity_id is null and (qap.is_customer_view is null or  (qap.is_customer_view=1 or qap.is_customer_view=10)) and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and q.status=:status and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time) dummy\r\n"
			+ "order by createdTime desc\r\n", countQuery = "SELECT count(*) from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName , qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_customer_view as isCustomerView,q.created_by as createdBy  \r\n" +
					"			from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"			 LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"			LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"			LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"			LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"			LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id \r\n" + 
					"			where  (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and q.engagement_to_opportunity_id is null and q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time\r\n" +
					"			UNION ALL\r\n" +
					"			SELECT  q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage,null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount ,qap.is_customer_view as isCustomerView,q.created_by as createdBy \r\n" +
					"			from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"			 LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"			LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"			LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"			LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"			LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n" + 
					"			where  (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and q.engagement_to_opportunity_id is null and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and q.status=:status and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time) dummy\r\n" + 
					"			order by createdTime desc\r\n", nativeQuery = true)
	Page<Map<String,Object>> findDeActiveConfigurations(@Param("customerId") List<Integer> customerId,
			@Param("custLeIds") Set<Integer> customerLeIds, @Param("quoteStage") Set<String> quoteStage,
			@Param("orderCategory") String orderCategory,Pageable pageable, @Param("status") Integer status);

	
	/**
	 * Find Active Quote Configurations by CustomerId
	 * 
	 * @param customerId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id " + "\r\n"
			+ "where q.customer_id=:customerId and q.status=:status and qle.erf_cus_customer_legal_entity_id is null and qle.stage <> :stage group by q.id order by q.id desc)\r\n"
			+ "UNION\r\n"
			+ "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ "INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.customer_id=:customerId and q.status=:status and qle.erf_cus_customer_legal_entity_id is null and qle.stage <> :stage group by q.id order by q.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsByCustomerId(@Param("customerId") Integer customerId,
			@Param("stage") String stage,@Param("status") Integer status);

	/**
	 * Find Active Quote Configurations for sales by userId
	 * 
	 * @param userId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "where q.created_by=:userId and q.status=:status and qle.stage <> :stage group by q.id order by q.id desc)\r\n" + "UNION\r\n"
			+ "(SELECT  count(q.id) as siteCount, q.id as quoteId ,mpf.name as productName ,qle.stage as stage,q.quote_code as quoteCode,q.created_time as createdTime \r\n"
			+ "from quote q\r\n" + "INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.created_by=:userId and q.status=:status and qle.stage <> :stage group by q.id order by q.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsForSales(@Param("userId") Integer userId,
			@Param("stage") String stage,@Param("status") Integer status);

	/**
	 * Find Active Quote Configurations for sales by userId
	 * 
	 * @param userId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select dummy.* from (SELECT  q.id as quoteId, null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId,qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended,null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy  \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id \r\n"
			+ "where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or (qap.is_sales_view=1 or qap.is_sales_view=10)) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10)) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n" + "SELECT o.quote_id as quoteId,  o.id as orderId,null as nsQuote,\r\n"
			+ "null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended ,ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory, mpf.name as productName , null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,count(ois.id) as siteCount,null as isSalesView,o.created_by as createdBy \r\n"
			+ "from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ "where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time\r\n" + "UNION ALL\r\n"
			+ "SELECT o.quote_id as quoteId, o.id as orderId ,null as nsQuote, null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory,mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime, count(ogs.id) as siteCount,null as isSalesView,o.created_by as createdBy \r\n"
			+ "from orders o\r\n" + "LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time ) dummy group by quoteId order by createdTime desc",
			countQuery= "select count(*) from (SELECT  q.id as quoteId ,null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
				    "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id\r\n" +
					"where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10))  and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" +
					"UNION ALL\r\n" + 
					"SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					" LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n" +
					"where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10))  and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" + 
					"UNION ALL SELECT null as quoteId,   o.id as orderId,null as nsQuote,\r\n" + 
					"null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory,mpf.name as productName, null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,count(ois.id) as siteCount,null as isSalesView,o.created_by as createdBy \r\n" +
					"from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" + 
					"LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n" +
					"where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory)  group by o.id,mpf.name,ole.stage,o.order_code,o.created_time UNION ALL\r\n" + 
					"SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory, mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime, count(ogs.id) as siteCount,null as isSalesView,o.created_by as createdBy\r\n" +
					"from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" + 
					"LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id\r\n" +
					"where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time ) dummy group by quoteId order by createdTime desc", nativeQuery = true)
	Page<Map<String, Object>> findActiveConfigurationsForSalesPageable(@Param("customerId") List<Integer> customerId,
			  @Param("custLeIds") Set<Integer> customerLeIds,
			@Param("quoteStage") Set<String> quoteStage, @Param("orderStage") String orderStage, @Param("orderCategory") String orderCategory, Pageable pageable,@Param("status") Integer status);
	
	/**
	 * Find Active Quote Configurations for sales by userId
	 * 
	 * @param userId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select dummy.* from (SELECT  q.id as quoteId, null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId,qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended,null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy  \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id \r\n"
			+ "where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and qle.quote_type='TERMINATION' and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or (qap.is_sales_view=1 or qap.is_sales_view=10)) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and qle.quote_type='TERMINATION' and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10)) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n" + "SELECT o.quote_id as quoteId,  o.id as orderId,null as nsQuote,\r\n"
			+ "null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended ,ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory, mpf.name as productName , null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,count(ois.id) as siteCount,null as isSalesView,o.created_by as createdBy \r\n"
			+ "from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ "where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null)  and ole.order_type='TERMINATION'  and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time\r\n" + "UNION ALL\r\n"
			+ "SELECT o.quote_id as quoteId, o.id as orderId ,null as nsQuote, null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory,mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime, count(ogs.id) as siteCount,null as isSalesView,o.created_by as createdBy \r\n"
			+ "from orders o\r\n" + "LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.order_type='TERMINATION'  and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time ) dummy group by quoteId order by createdTime desc",
			countQuery= "select count(*) from (SELECT  q.id as quoteId ,null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
				    "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id\r\n" +
					"where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10)) and qle.quote_type='TERMINATION'  and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" +
					"UNION ALL\r\n" + 
					"SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					" LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n" +
					"where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10)) and qle.quote_type='TERMINATION'   and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" + 
					"UNION ALL SELECT null as quoteId,   o.id as orderId,null as nsQuote,\r\n" + 
					"null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory,mpf.name as productName, null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,count(ois.id) as siteCount,null as isSalesView,o.created_by as createdBy \r\n" +
					"from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" + 
					"LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n" +
					"where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.order_type='TERMINATION'  and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory)  group by o.id,mpf.name,ole.stage,o.order_code,o.created_time UNION ALL\r\n" + 
					"SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory, mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime, count(ogs.id) as siteCount,null as isSalesView,o.created_by as createdBy\r\n" +
					"from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" + 
					"LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id\r\n" +
					"where o.customer_id in (:customerId) and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) and ole.order_type='TERMINATION'  and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time ) dummy group by quoteId order by createdTime desc", nativeQuery = true)
	Page<Map<String, Object>> findActiveConfigurationsForTerminationPageable(@Param("customerId") List<Integer> customerId,
			  @Param("custLeIds") Set<Integer> customerLeIds,
			@Param("quoteStage") Set<String> quoteStage, @Param("orderStage") String orderStage, @Param("orderCategory") String orderCategory, Pageable pageable,@Param("status") Integer status);
	
	
	
	/**
	 * Find Active Quote Configurations for sales by userId
	 * 
	 * @param userId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select dummy.* from (SELECT  q.id as quoteId, null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId,qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended,null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy  \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id \r\n"
			+ "where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or (qap.is_sales_view=1 or qap.is_sales_view=10)) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10)) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time) dummy group by quoteId order by createdTime desc",
			countQuery= "select count(*) from (SELECT  q.id as quoteId ,null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
				    "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id\r\n" +
					"where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10))  and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" +
					"UNION ALL\r\n" + 
					"SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,qap.is_sales_view as isSalesView,q.created_by as createdBy\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					" LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "+
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n" +
					"where q.customer_id in (:customerId) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds)  or qle.erf_cus_customer_legal_entity_id is null) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10))  and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time) dummy group by quoteId order by createdTime desc", nativeQuery = true)
	Page<Map<String, Object>> findDeActiveConfigurationsForSalesPageable(@Param("customerId") List<Integer> customerId,
			  @Param("custLeIds") Set<Integer> customerLeIds,
			@Param("quoteStage") Set<String> quoteStage,@Param("orderCategory") String orderCategory, Pageable pageable,@Param("status") Integer status);
	
	
	/**
	 * Find Active Quote Configurations for sales by userId
	 * 
	 * @param userId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select dummy.* from (SELECT  q.id as quoteId, null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,cus.customer_name as customerName \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id \r\n"
			+ "JOIN customer cus ON cus.id = q.customer_id "
			+ "where q.created_by=:userId and q.status=:status and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10))  and cus.erf_cus_customer_id in(:customerIds) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,cus.customer_name as customerName \r\n"
			+ "from quote q\r\n" + "LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ " LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id =qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n"
			+ "JOIN customer cus ON cus.id = q.customer_id "
			+ "where q.created_by=:userId and q.status=:status and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_sales_view is null or  (qap.is_sales_view=1 or qap.is_sales_view=10))  and cus.erf_cus_customer_id in(:customerIds) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n"
			+ "UNION ALL\r\n" + "SELECT o.quote_id as quoteId,   o.id as orderId,null as nsQuote,\r\n"
			+ "null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory, mpf.name as productName , null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,count(ois.id) as siteCount,cus.customer_name as customerName\r\n"
			+ "from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ "JOIN customer cus ON cus.id = o.customer_id "
			+ "where o.created_by=:userId and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) and cus.erf_cus_customer_id in(:customerIds) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time\r\n" + "UNION ALL\r\n"
			+ "SELECT o.quote_id as quoteId, o.id as orderId ,null as nsQuote, null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory,mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime, count(ogs.id) as siteCount,cus.customer_name as customerName \r\n"
			+ "from orders o\r\n" + "LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "JOIN customer cus ON cus.id = o.customer_id "
			+ "where o.created_by=:userId and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) and cus.erf_cus_customer_id in(:customerIds) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time ) dummy group by quoteId order by createdTime desc",
			countQuery= "select count(*) from (SELECT  q.id as quoteId ,null as orderId,q.ns_quote as nsQuote, qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,cus.customer_name as customerName\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					"LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id\r\n" +
					"JOIN customer cus ON cus.id = q.customer_id "+
					"where q.created_by=:userId and q.status=:status and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and cus.erf_cus_customer_id in(:customerIds) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" + 
					"UNION ALL\r\n" + 
					"SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType,qle.tps_sfdc_opty_id as quoteOptyId, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType,null as orderOptyId, null as orderCategory, mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,  count(qgs.id) as siteCount,cus.customer_name as customerName\r\n" +
					"from quote q LEFT JOIN quote_to_le qle on qle.quote_id = q.id\r\n" + 
					" LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n" + 
					"LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id\r\n" + 
					"JOIN customer cus ON cus.id = q.customer_id "+
					"where q.created_by=:userId and q.status=:status and qle.stage not in (:quoteStage) and (qle.quote_category is null or qle.quote_category <> :orderCategory) and cus.erf_cus_customer_id in(:customerIds) group by q.id,mpf.name,qle.stage,q.quote_code,q.created_time\r\n" + 
					"UNION ALL SELECT null as quoteId,   o.id as orderId,null as nsQuote,\r\n" + 
					"null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory,mpf.name as productName , null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,count(ois.id) as siteCount,cus.customer_name as customerName\r\n" +
					"from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" + 
					"LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n" +
					"JOIN customer cus ON cus.id = o.customer_id "+
					"where o.created_by=:userId and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) and cus.erf_cus_customer_id in(:customerIds)  group by o.id,mpf.name,ole.stage,o.order_code,o.created_time UNION ALL\r\n" + 
					"SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType,null as quoteOptyId, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType,ole.tps_sfdc_copf_id as orderOptyId, ole.order_category as orderCategory, mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime, count(ogs.id) as siteCount,cus.customer_name as customerName\r\n" +
					"from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id\r\n" + 
					"LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n" + 
					"LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n" + 
					"LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n" + 
					"LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n" + 
					"LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id\r\n" +
					"JOIN customer cus ON cus.id = o.customer_id "+
					"where o.created_by=:userId and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) and cus.erf_cus_customer_id in(:customerIds) group by o.id,mpf.name,ole.stage,o.order_code,o.created_time ) dummy group by quoteId order by createdTime desc", nativeQuery = true)
	Page<Map<String, Object>> findActiveConfigurationsForSalesPageableBasedOnUserIdAndCustomerId(@Param("userId") Integer userId,
			@Param("quoteStage") Set<String> quoteStage, @Param("orderStage") String orderStage, @Param("orderCategory") String orderCategory, Pageable pageable,@Param("customerIds") Set<Integer> customerIds,@Param("status") Integer status);
	
	Page<Quote> findAll(Specification<Quote> specification, Pageable pageable);

	/**
	 * Find active configurations of partner
	 *
	 * @param customerId
	 * @param customerLeIds
	 * @param quoteStage
	 * @param orderStage
	 * @param orderCategory
	 * @param partnerIds
	 * @param pageable
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "SELECT dummy.* from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,"
			+ "qap.is_customer_view as isCustomerView, q.created_by as createdBy "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id "
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and q.customer_id in (:customerId) and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and (q.ns_quote='N' or q.ns_quote is null) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) "
			+ "and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time "
			+ "UNION ALL "
			+ "SELECT  q.id as quoteId , null as orderId, q.ns_quote as nsQuote, qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount,"
			+ "qap.is_customer_view as isCustomerView, q.created_by as createdBy "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id "
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) "
			+ "and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time "
			+ "UNION ALL "
			+ "SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory, "
			+ "mpf.name as productName , null as quoteStage, ole.stage as orderStage, null as quoteCode, o.order_code as orderCode, o.created_time as createdTime, count(ois.id) as siteCount,"
			+ "null as isCustomerView,o.created_by as createdBy "
			+ "from orders o "
			+ "LEFT JOIN order_to_le ole on ole.order_id = o.id "
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id "
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on o.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and o.customer_id in (:customerId) and mpf.id!=2 and o.id is not null "
			+ "and (ole.erf_cus_customer_legal_entity_id in(:custLeIds) or ole.erf_cus_customer_legal_entity_id is null) "
			+ "and ole.stage = :orderStage and (ole.order_category is null or ole.order_category <> :orderCategory) and mpf.id in (:productIds) and ole.classification in (:classifications)  "
			+ "group by o.id, o.order_code, mpf.name, ole.stage, o.created_time "
			+ "UNION ALL "
			+ "SELECT null as quoteId,  o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory, "
			+ "mpf.name as productName , null as quoteStage, ole.stage as orderStage, null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,  count(ogs.id) as siteCount,"
			+ "null as isCustomerView,o.created_by as createdBy "
			+ "from orders o "
			+ "LEFT JOIN order_to_le ole on ole.order_id = o.id "
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id "
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on o.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and o.customer_id in (:customerId) and ole.stage = :orderStage "
			+ "and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) "
			+ "and ole.stage = :orderStage and  mpf.id=2 and o.id is not null and (ole.order_category is null or ole.order_category <> :orderCategory) and mpf.id in (:productIds) and ole.classification in (:classifications)  "
			+ "group by o.id, o.order_code, mpf.name, ole.stage, o.created_time) dummy "
			+ "order by createdTime desc\r\n",
			countQuery = "SELECT count(*) from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName , qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_customer_view as isCustomerView, q.created_by as createdBy "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and q.customer_id in (:customerId) "
			+ "and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time "
			+ "UNION ALL "
			+ "SELECT  q.id as quoteId , null as orderId,q.ns_quote as nsQuote , qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName ,qle.stage as quoteStage,null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount,qap.is_customer_view as isCustomerView, q.created_by as createdBy  "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time "
			+ "UNION ALL "
			+ "SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory,ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended, ole.order_type as orderType, ole.order_category as orderCategory, "
			+ "mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode, "
			+ "o.created_time as createdTime, count(ois.id) as siteCount,null as isCustomerView,o.created_by as createdBy  from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id "
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id "
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN order_ill_sites ois on ois.product_solutions_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on o.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and o.customer_id in (:customerId) and mpf.id!=2 and o.id is not null "
			+ "and (ole.erf_cus_customer_legal_entity_id in(:custLeIds) or ole.erf_cus_customer_legal_entity_id is null) and ole.stage = :orderStage "
			+ "and (ole.order_category is null or ole.order_category <> :orderCategory) and mpf.id in (:productIds) and ole.classification in (:classifications)  "
			+ "group by o.id, o.order_code, mpf.name, ole.stage, o.created_time "
			+ "UNION ALL "
			+ "SELECT null as quoteId, o.id as orderId ,null as nsQuote, null as quoteType, null as quoteCategory, ole.is_multicircuit as isMulticircuit,ole.is_amended as isAmended,ole.order_type as orderType, ole.order_category as orderCategory, "
			+ "mpf.name as productName ,null as quoteStage, ole.stage as orderStage,null as quoteCode, o.order_code as orderCode,o.created_time as createdTime,  count(ogs.id) as siteCount, null as isCustomerView,o.created_by as createdBy "
			+ "from orders o LEFT JOIN order_to_le ole on ole.order_id = o.id "
			+ "LEFT JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = olef.product_family_id "
			+ "LEFT JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on o.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and o.customer_id in (:customerId) "
			+ "and ole.stage = :orderStage and (ole.erf_cus_customer_legal_entity_id in(:custLeIds)  or ole.erf_cus_customer_legal_entity_id is null) "
			+ "and ole.stage = :orderStage and  mpf.id=2 and o.id is not null and (ole.order_category is null or ole.order_category <> :orderCategory) and mpf.id in (:productIds) and ole.classification in (:classifications)  "
			+ "group by o.id, o.order_code, mpf.name, ole.stage, o.created_time) dummy "
			+ "order by createdTime desc ", nativeQuery = true)
	Page<Map<String,Object>> findPartnerActiveConfigurations(@Param("customerId") List<Integer> customerId,
													  @Param("custLeIds") Set<Integer> customerLeIds, @Param("quoteStage") Set<String> quoteStage,
													  @Param("orderStage") String orderStage, @Param("orderCategory") String orderCategory, @Param("partnerIds") Set<Integer> partnerIds, @Param("productIds") List<Integer> productIds, @Param("classifications") List<String> classifications, Pageable pageable,@Param("status") Integer status);
	
	/**
	 * Find active configurations of partner
	 *
	 * @param customerId
	 * @param customerLeIds
	 * @param quoteStage
	 * @param orderStage
	 * @param orderCategory
	 * @param partnerIds
	 * @param pageable
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "SELECT dummy.* from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,"
			+ "qap.is_customer_view as isCustomerView, q.created_by as createdBy "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id "
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and q.customer_id in (:customerId) and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and (q.ns_quote='N' or q.ns_quote is null) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) "
			+ "and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications)  "
			+ "group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time "
			+ "UNION ALL "
			+ "SELECT  q.id as quoteId , null as orderId, q.ns_quote as nsQuote, qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName ,qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount,"
			+ "qap.is_customer_view as isCustomerView, q.created_by as createdBy "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id "
			+ "LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) "
			+ "and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time) dummy "
			+ "order by createdTime desc\r\n",
			countQuery = "SELECT count(*) from (SELECT q.id as quoteId , null as orderId,q.ns_quote as nsQuote,  qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName , qle.stage as quoteStage, null as orderStage, q.quote_code as quoteCode, null as orderCode, q.created_time as createdTime,count(qis.id) as siteCount,qap.is_customer_view as isCustomerView, q.created_by as createdBy "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and q.customer_id in (:customerId) "
			+ "and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and  mpf.id!=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by q.id,q.quote_code, mpf.name, qle.stage, q.created_time "
			+ "UNION ALL "
			+ "SELECT  q.id as quoteId , null as orderId,q.ns_quote as nsQuote , qle.quote_type as quoteType, qle.quote_category as quoteCategory,qle.is_multicircuit as isMulticircuit,qle.is_amended as isAmended, null as orderType, null as orderCategory, "
			+ "mpf.name as productName ,qle.stage as quoteStage,null as orderStage, q.quote_code as quoteCode,null as orderCode, q.created_time as createdTime,count(qgs.id) as siteCount,qap.is_customer_view as isCustomerView, q.created_by as createdBy  "
			+ "from quote q "
			+ "LEFT JOIN quote_to_le qle on qle.quote_id = q.id "
			+ "LEFT JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id "
			+ "LEFT JOIN mst_product_family mpf on mpf.id = qlef.product_family_id LEFT JOIN quote_access_permission qap on qap.ref_id = q.quote_code and qap.product_family_id = qlef.product_family_id and qap.type='QUOTE' "
			+ "LEFT JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id "
			+ "LEFT JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id "
			+ "LEFT JOIN quote_gsc qgs on qgs.product_solution_id = ps.id "
			+ "LEFT JOIN engagement_to_opportunity eto on q.engagement_to_opportunity_id = eto.id and eto.engagement_id "
			+ "LEFT JOIN engagement e on eto.engagement_id = e.id "
			+ "where e.partner_id in (:partnerIds) and q.status=:status and (qle.erf_cus_customer_legal_entity_id in(:custLeIds) or qle.erf_cus_customer_legal_entity_id is null) "
			+ "and qle.stage not in (:quoteStage) and q.customer_id in (:customerId) and mpf.id=2 and q.id is not null and (qle.quote_category is null or qle.quote_category <> :orderCategory) and (qap.is_customer_view is null or (qap.is_customer_view=1 or qap.is_customer_view=10)) and mpf.id in (:productIds) and qle.classification in (:classifications) "
			+ "group by  q.id, q.quote_code, mpf.name, qle.stage, q.created_time) dummy "
			+ "order by createdTime desc ", nativeQuery = true)
	Page<Map<String,Object>> findPartnerDeActiveConfigurations(@Param("customerId") List<Integer> customerId,
													  @Param("custLeIds") Set<Integer> customerLeIds, @Param("quoteStage") Set<String> quoteStage,
													 @Param("orderCategory") String orderCategory, @Param("partnerIds") Set<Integer> partnerIds, @Param("productIds") List<Integer> productIds, @Param("classifications") List<String> classifications, Pageable pageable,@Param("status") Integer status);

	@Query(value = "(SELECT  q.id as quoteId, qle.id as quoteToLeId, q.quote_code as quoteCode, qle.tps_sfdc_opty_id as tpsSFDCOptyId,\r\n"
			+ "qle.stage as quoteStage, q.created_time as quoteCreatedTime, \r\n"
            + "qsstd.created_time as terminationCreatedTime, qsstd.o2c_call_initiated_date as o2cCallInitiatedDate, qsstd.sales_task_response as salesTaskResponse \r\n"
			+ "from quote q INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ "INNER JOIN quote_site_service_termination_details qsstd on qsstd.quote_to_le_id = qle.id\r\n"
			+ "where q.status= (:status) and qle.stage = (:quoteStage) and qle.tps_sfdc_opty_id  is not null\r\n"
            + "and qle.quote_type = (:quoteType) and qsstd.o2c_call_initiated_date < (:requestedDate) and qsstd.created_time is not null\r\n"
			+ "group by q.id order by q.id desc\r\n)", nativeQuery = true)
	List<Map<String, Object>> findEligibleTerminationQuotesForSFDCVerbalAggreementStageMovement(@Param("requestedDate") Date requestedDate, 
			@Param("quoteType") String quoteType, @Param("quoteStage") String quoteStage, @Param("status") Byte status);

	@Query(value = "(SELECT  q.id as quoteId, qle.id as quoteToLeId, q.quote_code as quoteCode, qle.tps_sfdc_opty_id as tpsSFDCOptyId,\r\n"
			+ "qle.stage as quoteStage, q.created_time as quoteCreatedTime, \r\n"
            + "qsstd.created_time as terminationCreatedTime, qsstd.o2c_call_initiated_date as o2cCallInitiatedDate, qsstd.sales_task_response as salesTaskResponse \r\n"
			+ "from quote q INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ "INNER JOIN quote_site_service_termination_details qsstd on qsstd.quote_to_le_id = qle.id\r\n"
			+ "where q.status= (:status) and qle.stage = (:quoteStage) and qle.tps_sfdc_opty_id  is not null\r\n"
            + "and qle.quote_type = (:quoteType) and qsstd.created_time < (:requestedDate) and qsstd.o2c_call_initiated_date is null\r\n"
			+ "group by q.id order by q.id desc\r\n)", nativeQuery = true)
	List<Map<String, Object>> findEligibleNonO2CTerminationQuotesForSFDCVerbalAggreementStageMovement(@Param("requestedDate") Date requestedDate, 
			@Param("quoteType") String quoteType, @Param("quoteStage") String quoteStage, @Param("status") Byte status);
	
	@Query(value = "(SELECT  q.id as quoteId, qle.id as quoteToLeId, q.quote_code as quoteCode, qle.tps_sfdc_opty_id as tpsSFDCOptyId,\r\n"
			+ "qle.stage as quoteStage, q.created_time as quoteCreatedTime, \r\n"
            + "qsstd.created_time as terminationCreatedTime, qsstd.o2c_call_initiated_date as o2cCallInitiatedDate, qsstd.sales_task_response as salesTaskResponse \r\n"
			+ "from quote q INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
			+ "INNER JOIN quote_site_service_termination_details qsstd on qsstd.quote_to_le_id = qle.id\r\n"
			+ "where q.status= (:status) and q.quote_code= (:quoteCode) and qle.tps_sfdc_opty_id  is not null\r\n"
            + "and qle.quote_type = (:quoteType) and qsstd.created_time is not null\r\n"
			+ "group by q.id order by q.id desc\r\n)", nativeQuery = true)
	List<Map<String, Object>> findTerminationQuoteByQuoteCode(@Param("quoteCode") String quoteCode, 
			@Param("quoteType") String quoteType, @Param("status") Byte status);
	
	@Query(value = "(SELECT  q.id as quoteId, qle.id as quoteToLeId, qists.erf_service_inventory_tps_service_id as serviceId,\r\n"
			+ "q.quote_code as quoteCode, qle.stage as quoteStage, q.created_time as quoteCreatedTime \r\n"
            + "from quote q INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
            + "INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_ill_sites qis on qis.product_solutions_id = ps.id\r\n"
            + "INNER JOIN quote_ill_site_to_service qists on qists.quote_site_id = qis.id\r\n"
			+ "where q.status= (:status) and qists.erf_service_inventory_tps_service_id in (:serviceIds)\r\n"
            + "and qle.quote_type = (:quoteType) and (qists.is_deleted!=0 or qists.is_deleted is null)\r\n"
			+ "group by q.id order by q.id desc\r\n)", nativeQuery = true)
	List<Map<String, Object>> findTerminationQuotesByServiceIds(@Param("serviceIds") List<String> serviceIds, 
			@Param("quoteType") String quoteType, @Param("status") Byte status);
	
	@Query(value = "(SELECT  q.id as quoteId, qle.id as quoteToLeId, qists.erf_service_inventory_tps_service_id as serviceId,\r\n"
			+ "q.quote_code as quoteCode, qle.stage as quoteStage, q.created_time as quoteCreatedTime \r\n"
            + "from quote q INNER JOIN quote_to_le qle on qle.quote_id = q.id\r\n"
            + "INNER JOIN quote_to_le_product_family qlef ON qlef.quote_to_le_id = qle.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = qlef.product_family_id\r\n"
			+ "INNER JOIN product_solutions ps on ps.quote_le_product_family_id = qlef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN quote_npl_link qnl on qnl.product_solution_id = ps.id\r\n"
            + "INNER JOIN quote_ill_site_to_service qists on qists.quote_link_id = qnl.id\r\n"
			+ "where q.status= (:status) and qists.erf_service_inventory_tps_service_id in (:serviceIds)\r\n"
            + "and qle.quote_type = (:quoteType) and (qists.is_deleted!=0 or qists.is_deleted is null)\r\n"
			+ "group by q.id order by q.id desc\r\n)", nativeQuery = true)
	List<Map<String, Object>> findTerminationQuotesByServiceIdsNPL(@Param("serviceIds") List<String> serviceIds, 
			@Param("quoteType") String quoteType, @Param("status") Byte status);
	
	@Query(value = "select q.id as quoteId, q.quote_code as quoteCode, le.id as quoteToLeId, le.cancelled_parent_order_code as cancelledParentOrderCode, s.erf_service_inventory_tps_service_id as serviceId  from quote q left join quote_to_le le on le.quote_id=q.id left join quote_ill_site_to_service s on s.quote_to_le_id =le.id where s.erf_service_inventory_tps_service_id in (:serviceIds) and le.cancelled_parent_order_code in (:cancelledParentOrderCode)", nativeQuery = true)
	List<Map<String, Object>> findQuotebyCancelledParentOrderandServiceId(@Param("serviceIds") List<String> serviceIds, 
			@Param("cancelledParentOrderCode") String cancelledParentOrderCode);
	
	@Query(value = "SELECT tps_sfdc_waiver_id FROM quote_ill_site_to_service  where erf_service_inventory_tps_service_id=:serviceId and quote_to_le_id=:quoteToLe", nativeQuery = true)
	List<String> findWaiverIdByQuoteToLeAndServiceId(@Param("quoteToLe")  Integer quoteToLe,@Param("serviceId")  String serviceId);
	
}
