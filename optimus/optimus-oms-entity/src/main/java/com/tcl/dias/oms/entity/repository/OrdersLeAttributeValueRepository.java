package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;

/**
 * This file contains the OrdersLeAttributeValueRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrdersLeAttributeValueRepository extends JpaRepository<OrdersLeAttributeValue, Integer> {

	Set<OrdersLeAttributeValue> findByMstOmsAttributeAndOrderToLe(MstOmsAttribute mstomsattribute, OrderToLe orderToLe);

	Set<OrdersLeAttributeValue> findByMstOmsAttribute_NameAndOrderToLe(String mstomsattributeName, OrderToLe orderToLe);

	List<OrdersLeAttributeValue> findByOrderToLe(OrderToLe orderToLe);

	@Query(value = "SELECT mo.name, oa.attribute_value FROM orders_le_attribute_values oa,mst_oms_attributes mo where oa.mst_oms_attributes_id=mo.id and orders_to_le_id=:orderToLeId", nativeQuery = true)
	List<Map<String, Object>> findByLeId(@Param("orderToLeId") Integer orderToLeId);

	void deleteAllByOrderToLe(OrderToLe orderToLe);

	@Query(value = "select * from orders_le_attribute_values olav left join order_to_le otl on olav.orders_to_le_id = otl.id " +
			"left join orders o on otl.order_id = o.id " +
			"left join mst_oms_attributes moa on olav.mst_oms_attributes_id = moa.id " +
			"where o.id=:orderId and moa.name=:attributeName", nativeQuery = true)
	List<OrdersLeAttributeValue> findByOrderIDAndMstOmsAttributeName(Integer orderId, String attributeName);
	
	List<OrdersLeAttributeValue> findByOrderToLe_Id(Integer orderToLeId);
}
