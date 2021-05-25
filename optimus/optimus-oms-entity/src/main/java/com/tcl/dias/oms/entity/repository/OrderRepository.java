package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This file contains the OrderRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

	Order findByQuoteAndStatus(Quote quote, Byte status);

	Order findByIdAndStatus(Integer orderId, Byte status);

	Order findByIdAndStage(Integer orderId, String stage);

	Order findByQuoteAndStatus(Integer quote, Byte status);

	Order findByOrderCode(String orderCode);

	List<Order> findAllByOrderByCreatedTimeDesc();

	Page<Order> findAllByOrderByCreatedTimeDesc(Pageable pageable);

	Page<Order> findByOrderCodeContainsAllIgnoreCase(String searchTerm, Pageable pageable);

	Page<Order> findAll(Specification<Order> specification, Pageable pageable);
	
	@Query(value = "select * from orders o,order_to_le ole where ole.order_id=o.id and ole.id=:orderLeId", nativeQuery = true)
	Order findByOrderLeId(@Param("orderLeId") Integer orderLeId);

	/**
	 * Find Active Orders by CustomerLeId and stage
	 * 
	 * @param customerLeIds
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime from orders o,order_to_le ole where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and o.stage != :stage order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> findByCustomerLeIds(@Param("customerLeIds") Set<Integer> customerLeIds,
			@Param("stage") String stage);
	
	/**
	 * Find Active Orders by CustomerLeId
	 * 
	 * @param customerLeIds
	 * @return {@link List<Map<String, Object>>}
	 */
	// Added the column ole.order_type as orderType to existing query for IPC
	@Query(value = "select o.id as orderId,ole.is_amended as isAmended, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName, ole.classification as classification,o.stage as stage, ole.id as orderToLeId, ole.order_type as orderType,o.is_order_to_cash_enabled as isO2cEnabled   from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) order by o.id desc",nativeQuery=true)
	//@Query(value = "select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName, ole.classification as classification,o.stage as stage,ole.id as orderToLeId from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) order by o.id desc",nativeQuery=true)
	//@Query(value = "select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds)  order by o.id desc limit 25",nativeQuery=true)
	//@Query(value = "select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime from orders o,order_to_le ole where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> findByCustomerLeIds(@Param("customerLeIds") Set<Integer> customerLeIds);

	/**
	 * Find Active Orders by Partner
	 *
	 * @param customerLeIds
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName, ole.classification as classification,o.stage as stage,ole.order_type as orderType,ole.id as orderToLeId,o.is_order_to_cash_enabled as isO2cEnabled from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf, engagement_to_opportunity eto, engagement e where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.engagement_to_opportunity_id = eto.id and eto.engagement_id = e.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and e.partner_id in (:partnerIds) order by o.id desc",nativeQuery=true)
	List<Map<String, Object>> findPartnerCustomerLeIds(@Param("customerLeIds") Set<Integer> customerLeIds, @Param("partnerIds") List<Integer> partnerIds);
	
	@Query(value = "select count(1) from orders o,order_to_le ole where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and ole.stage not in (:orderStatus) and (ole.order_Type=:orderType)",nativeQuery=true)
	Integer findOrdersCountByCustomerLeIdsAndNotStatusAndType(@Param("customerLeIds") Set<Integer> customerLeIds,@Param("orderStatus") List<String> orderStatus,@Param("orderType") String  orderType);
	
	@Query(value = "select distinct(mpf.name) from orders o,order_to_le ole,order_to_le_product_family opf,mst_product_family mpf where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and opf.order_to_le_id=ole.id and opf.product_family_id=mpf.id",nativeQuery=true)
	List<String> findProductNamesByCustomerLe(@Param("customerLeIds") Set<Integer> customerLeIds);

	@Query(value = "select distinct(mpf.name) from orders o,order_to_le ole,order_to_le_product_family opf,mst_product_family mpf, engagement_to_opportunity eto, engagement e where ole.order_id=o.id and o.engagement_to_opportunity_id = eto.id and eto.engagement_id = e.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and e.partner_id in (:partnerIds) and opf.order_to_le_id=ole.id and opf.product_family_id=mpf.id",nativeQuery=true)
	List<String> findProductNamesByCustomerLeAndPartnerId(@Param("customerLeIds") Set<Integer> customerLeIds,@Param("partnerIds") List<Integer> partnerIds);
	
	@Query(value = "select count(1) from orders o,order_to_le ole,order_to_le_product_family opf,mst_product_family mpf where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and o.stage =:orderStatus and opf.order_to_le_id=ole.id and opf.product_family_id=mpf.id and mpf.name= :productName",nativeQuery=true)
	Integer findCountByCustomerLeAndProductName(@Param("customerLeIds") Set<Integer> customerLeIds,@Param("orderStatus") String orderStatus,@Param("productName") String productName);
	
	
	@Query(value = "select count(1) from orders o,order_to_le ole where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and ole.stage not in (:orderStatus) and (ole.order_Type='NEW' or ole.order_Type is null)",nativeQuery=true)
	Integer findNewOrdersCountByCustomerLeIdsAndNotStatusAndType(@Param("customerLeIds") Set<Integer> customerLeIds,@Param("orderStatus") List<String> orderStatus);

	/**
	 * Find Active Orders Site count by CustomerLeId
	 * 
	 * @param customerLeIds
	 * @param statusId
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "SELECT count(ois.id) FROM  orders o,  order_to_le ole,  order_to_le_product_family opy,   order_product_solutions ops,   order_ill_sites ois WHERE  ole.order_id = o.id and ole.id=opy.order_to_le_id and opy.id=ops.product_le_product_family_id and ops.id=ois.product_solutions_id  AND ole.erf_cus_customer_legal_entity_id IN (:customerLeIds) and ois.status !=:statusId", nativeQuery = true)
	Integer findSiteCountByLeId(@Param("customerLeIds") Set<Integer> customerLeIds,
			@Param("statusId") Integer statusId);



	@Query(value = "SELECT count(ois.id) FROM  orders o,  order_to_le ole,  order_to_le_product_family opy,   order_product_solutions ops, order_ill_sites ois, engagement_to_opportunity eto, engagement e WHERE  ole.order_id = o.id and ole.id=opy.order_to_le_id and opy.id=ops.product_le_product_family_id and  o.engagement_to_opportunity_id = eto.id and eto.engagement_id = e.id and ops.id=ois.product_solutions_id  AND ole.erf_cus_customer_legal_entity_id IN (:customerLeIds) and e.partner_id in (:partnerIds)  and  ois.status !=:statusId", nativeQuery = true)
	Integer findSiteCountByPartnerAndLeId(@Param("customerLeIds") Set<Integer> customerLeIds,@Param("partnerIds") List<Integer> partnerIds,
								@Param("statusId") Integer statusId);
	
	/**
	 * Find distinct order stages
	 * 
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "select distinct ole.stage as orderStage from orders o INNER JOIN order_to_le ole ON ole.order_id=o.id and ole.stage is not null", nativeQuery = true)
	List<Map<String,Object>> findDistinctStages();
	
	/**
	 * Find distinct CustomerLeIds
	 * 
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value="select distinct ole.erf_cus_customer_legal_entity_id as customerLegalEntities from orders o INNER JOIN order_to_le ole ON ole.order_id = o.id", nativeQuery = true)
	List<Map<String,Object>> findDistinctCustomerLeIds();
	
	/**
	 * Find Distinct ProductFamily
	 * 
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value="select distinct mpf.id as productFamilyId, mpf.name as productFamilyName from orders o INNER JOIN order_to_le ole ON ole.order_id = o.id INNER JOIN order_to_le_product_family olepf ON olepf.order_to_le_id = ole.id INNER JOIN mst_product_family mpf ON olepf.product_family_id=mpf.id", nativeQuery = true)
	List<Map<String,Object>> findDistinctProductFamily();

	/**
	 * Find Active Orders by userId
	 * 
	 * @param userId
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value="select o.id as orderId, ole.is_amended as isAmended, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName, ole.id as orderToLeId,o.stage as stage,ole.order_type as orderType,o.is_order_to_cash_enabled as isO2cEnabled from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.created_by=:userId order by o.id desc", nativeQuery = true)
	//@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.created_by=:userId order by o.id desc limit 25", nativeQuery = true)
	//@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime from orders o,order_to_le ole where ole.order_id=o.id and o.created_by=:userId order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> findByUserType(@Param("userId") Integer userId);
	
	/**
	 * Find Active Orders by userId and customerId
	 * 
	 * @param userId
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName,ole.id as orderToLeId,ole.order_type as orderType, ole.is_amended as isAmended, o.is_order_to_cash_enabled as isO2cEnabled from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.created_by=:userId and o.customer_id in(:customerIds) order by o.id desc", nativeQuery = true)
	//@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.created_by=:userId order by o.id desc limit 25", nativeQuery = true)
	//@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime from orders o,order_to_le ole where ole.order_id=o.id and o.created_by=:userId order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> findByUserTypeAndCustomer(@Param("userId") Integer userId,@Param("customerIds") Set<Integer> customerIds);
	
	/**
	 * Find Active Product Solution based on user Type
	 * 
	 * @param userId
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value="select ops.id as orderProductSolutionId from orders o, order_to_le ole,  order_to_le_product_family opy,   order_product_solutions ops,   order_ill_sites ois WHERE  ole.order_id = o.id and ole.id=opy.order_to_le_id and opy.id=ops.product_le_product_family_id and ops.id=ois.product_solutions_id and o.created_by=:userId order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> getProductSolutionsBasedOnUserType(@Param("userId") Integer userId);
	
	/**
	 * Find Active Orders Configurations by CustomerLeId
	 * 
	 * @param customerLeIds
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "(SELECT  count(o.id) as siteCount, o.id as orderId ,mpf.name as productName ,ole.stage as stage,o.order_code as orderCode,o.created_time as createdTime \r\n"
			+ "from orders o\r\n" + "INNER JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ " INNER JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "INNER JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ "where ole.erf_cus_customer_legal_entity_id in(:custLeIds) and ole.stage = :stage group by o.id order by o.id desc)\r\n"
			+ "UNION\r\n"
			+ "(SELECT  count(o.id) as siteCount, o.id as orderId ,mpf.name as productName ,ole.stage as stage,o.order_code as orderCode,o.created_time as createdTime \r\n"
			+ "from orders o\r\n"
			+ "INNER JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ " INNER JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "INNER JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where ole.erf_cus_customer_legal_entity_id in(:custLeIds) and ole.stage = :stage group by o.id order by o.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsByCustomerLeId(@Param("custLeIds") Set<Integer> customerLeIds,
			@Param("stage") String stage);
	
	/**
	 * Find Active Orders Configurations by CustomerId
	 * 
	 * @param customerId
	 * @param stage
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value = "(SELECT  count(o.id) as siteCount, o.id as orderId ,mpf.name as productName ,ole.stage as stage,o.order_code as orderCode,o.created_time as createdTime \r\n"
			+ "from orders o\r\n" + "INNER JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ " INNER JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "INNER JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ "where o.customer_id=:customerId and ole.erf_cus_customer_legal_entity_id is null and ole.stage = :stage group by o.id order by o.id desc)\r\n"
			+ "UNION\r\n"
			+ "(SELECT  count(o.id) as siteCount, o.id as orderId ,mpf.name as productName ,ole.stage as stage,o.order_code as orderCode,o.created_time as createdTime \r\n"
			+ "from orders o\r\n" + "INNER JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ " INNER JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "INNER JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where o.customer_id=:customerId and ole.erf_cus_customer_legal_entity_id is null and ole.stage = :stage group by o.id order by o.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsByCustomerId(@Param("customerId") Integer customerId,
			@Param("stage") String stage);
	
	@Query(value = "(SELECT  count(o.id) as siteCount, o.id as orderId ,mpf.name as productName ,ole.stage as stage,o.order_code as orderCode,o.created_time as createdTime \r\n"
			+ "from orders o\r\n" + "INNER JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ " INNER JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "INNER JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN order_ill_sites ois on ois.product_solutions_id = ps.id \r\n"
			+ "where o.created_by=:userId and ole.stage = :stage group by o.id order by o.id desc)\r\n"
			+ "UNION\r\n"
			+ "(SELECT  count(o.id) as siteCount, o.id as orderId ,mpf.name as productName ,ole.stage as stage,o.order_code as orderCode,o.created_time as createdTime \r\n"
			+ "from orders o\r\n"
			+ "INNER JOIN order_to_le ole on ole.order_id = o.id\r\n"
			+ " INNER JOIN order_to_le_product_family olef ON olef.order_to_le_id = ole.id\r\n"
			+ "INNER JOIN mst_product_family mpf on mpf.id = olef.product_family_id\r\n"
			+ "INNER JOIN order_product_solutions ps on ps.product_le_product_family_id = olef.id\r\n"
			+ "INNER JOIN mst_product_offerings mpo on mpo.id = ps.product_offering_id\r\n"
			+ "INNER JOIN order_gsc ogs on ogs.order_product_solution_id = ps.id \r\n"
			+ "where o.created_by=:userId and ole.stage = :stage group by o.id order by o.id desc)", nativeQuery = true)
	List<Map<String, Object>> findActiveConfigurationsForSales(@Param("userId") Integer userId,
			@Param("stage") String stage);

	Optional<Order> findByQuote(Quote quote);

	@Query(value = "select o.id as orderId, o.order_code as orderCode, o.created_time as createdTime, productFamily.name as productName\n" +
		"from orders o\n" +
		"inner join order_to_le orderToLe on o.id = orderToLe.order_id\n" +
		"inner join order_to_le_product_family orderToLeProductFamily on orderToLeProductFamily.order_to_le_id = orderToLe.id\n" +
		"inner join mst_product_family productFamily on productFamily.id = orderToLeProductFamily.product_family_id\n" +
		"inner join engagement_to_opportunity engagementToOpportunity on o.engagement_to_opportunity_id = engagementToOpportunity.id\n" +
		"inner join opportunity op on op.id = engagementToOpportunity.opty_id\n" +
		"where op.opty_classification = :classification\n" +
		"and orderToLe.erf_cus_customer_legal_entity_id in (:customerLeIds)\n" +
		"order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> findByCustomerLeIdsAndClassification(Set<Integer> customerLeIds, String classification);

	/**
	 * Find Active Orders by userid or customer id 
	 * 
	 * @param userId
	 * @return {@link List<Map<String, Object>>}
	 */
	@Query(value="select o.id as orderId, ole.is_amended as isAmended, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName, ole.id as orderToLeId,o.stage as stage,ole.order_type as orderType,o.is_order_to_cash_enabled as isO2cEnabled from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.created_by in (:userIds)  order by o.id desc", nativeQuery = true)
	//@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime,pf.name as productName from orders o,order_to_le ole,order_to_le_product_family opf, mst_product_family pf where ole.order_id=o.id and opf.order_to_le_id=ole.id and pf.id= opf.product_family_id and o.created_by=:userId order by o.id desc limit 25", nativeQuery = true)
	//@Query(value="select o.id as orderId, o.order_code as orderCode,o.created_time as createdTime from orders o,order_to_le ole where ole.order_id=o.id and o.created_by=:userId order by o.id desc", nativeQuery = true)
	List<Map<String, Object>> findByCreatedByUserIdIn(@Param("userIds") Set<Integer> userIds);

	@Query(value = "select count(1) from orders o,order_to_le ole,order_to_le_product_family opf,mst_product_family mpf where ole.order_id=o.id and ole.erf_cus_customer_legal_entity_id in (:customerLeIds) and ole.stage  in (:orderStatus) and opf.order_to_le_id=ole.id and opf.product_family_id=mpf.id and mpf.name= :productName",nativeQuery=true)
	Integer findCountByCustomerLeAndProductNameAndStageIn(@Param("customerLeIds") Set<Integer> customerLeIds,@Param("orderStatus") List<String> orderStatus,@Param("productName") String productName);
	
	@Query(value = ":#{#orderActiveConfig}", nativeQuery = true)
	List<Map<String, Object>> findOrderActiveConfig(@Param("orderActiveConfig") String orderActiveConfig,Pageable pageable);
}
