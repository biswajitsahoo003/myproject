package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import com.tcl.dias.oms.entity.NativeQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;

/**
 * This file contains the OrderToLeRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderToLeRepository extends JpaRepository<OrderToLe, Integer> {

	List<OrderToLe> findByOrder_OrderCode(String orderCode);
	
	List<OrderToLe> findByTpsSfdcCopfId(String sfdcId);

	//List<OrderToLe> findByErfCusCustomerLegalEntityIdIn(List<Integer> customerId);
	
	@Query(value="select * from order_to_le where erf_cus_customer_legal_entity_id in (:customerId) order by id desc limit 25",nativeQuery=true)
	List<OrderToLe> findByErfCusCustomerLegalEntityIdIn(@Param("customerId") List<Integer> customerId);

	List<OrderToLe> findByErfCusCustomerLegalEntityId(Integer customerId);

	Optional<OrderToLe> findById(Integer id);

	/**
	 * Get orderToLe by order
	 * @param order
	 * @return {@link OrderToLe}
	 */
	List<OrderToLe> findByOrder(Order order);

	/**
	 * Get latest in progress MACD request type for downstream order
	 * @param downstreamOrderId
	 * @return
	 */
	@Query(value = NativeQueries.GET_LATEST_MACD_TYPE_FOR_ORDER, nativeQuery = true)
	Optional<String> findLatestMACDTypeByDownstreamOrder(@Param("downstreamOrderId") String downstreamOrderId);

    List<OrderToLe> findByOrder_Id(Integer quoteId);

    OrderToLe findByOrderLeCode(String quoteLeCode);
}
