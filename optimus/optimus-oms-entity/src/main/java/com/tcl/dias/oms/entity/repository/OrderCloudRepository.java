package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This file contains the OrderCloudRepository.java class. Repository class
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderCloudRepository extends JpaRepository<OrderCloud, Integer>, JpaSpecificationExecutor<OrderCloud> {

   List<OrderCloud> findByOrderProductSolutionAndStatus(OrderProductSolution orderProductSolution, byte status);

   List<OrderCloud> findByOrderProductSolution(OrderProductSolution orderProductSolution);

   List<OrderCloud> findByOrderId(Integer orderId);

   OrderCloud findByIdAndOrderToLeIdAndStatus(Integer id, Integer orderToLeId, byte status);
   
   @Query(value="SELECT * FROM order_cloud WHERE order_id=:orderId AND order_to_le_id=:orderLeId GROUP BY dc_location_id",nativeQuery =true)
   OrderCloud findDcByOrderId(@Param(value="orderId") Integer orderId,@Param(value="orderLeId") Integer orderLeId);

   @Query(value="SELECT distinct(dc_cloud_type) FROM order_cloud WHERE order_id=:orderId",nativeQuery =true)
   List<String> findDistinctDcCloudTypeByOrderId(@Param(value="orderId") Integer orderId);
   
   OrderCloud findFirstByParentCloudCodeOrderByIdDesc(String cloudCode);
}