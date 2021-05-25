package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;


/**
 * This file contains the OrderProductSolution.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderProductSolutionRepository extends JpaRepository<OrderProductSolution, Integer>{
	
	/**
	 * Get Order Product Solution by OrderToLeProductFamily
	 * @param orderToLeProductFamily
	 * @return {@link OrderProductSolution}
	 */
	List<OrderProductSolution> findByOrderToLeProductFamily(OrderToLeProductFamily orderToLeProductFamily);
	
	List<OrderProductSolution> findByIdIn(Set<Integer> ids);
	
	/**
	 * Find Product Solution of IZO_SDWAN by given reference code
	 *
	 * @param referenceId
	 * @return {@link List<ProductSolution>}
	 */
	@Query(value = "select p.* from orders q, order_to_le qle, order_to_le_product_family qlf, order_product_solutions p " +
			"where q.id = qle.order_id and qle.id = qlf.order_to_le_id and qlf.id = p.product_le_product_family_id and "
			+ "qlf.product_family_id=(select id from mst_product_family where name='IZO SDWAN')and " +
			"q.id=:referenceCode", nativeQuery = true)
	OrderProductSolution findByReferenceIdForIzoSdwan(@Param("referenceCode") Integer referenceCode);
	
	@Query(value = "select p.* from orders q, order_to_le qle, order_to_le_product_family qlf, order_product_solutions p " +
			"where q.id = qle.order_id and qle.id = qlf.order_to_le_id and qlf.id = p.product_le_product_family_id and "
			+ "qlf.product_family_id=(select id from mst_product_family where name='vProxy')and " +
			"q.id=:referenceCode", nativeQuery = true)
	List<OrderProductSolution> findByReferenceIdForVproxy(@Param("referenceCode") Integer referenceCode);
	
	@Query(value = "select p.* from orders q, order_to_le qle, order_to_le_product_family qlf, order_product_solutions p " +
			"where q.id = qle.order_id and qle.id = qlf.order_to_le_id and qlf.id = p.product_le_product_family_id and "
			+ "qlf.product_family_id=(select id from mst_product_family where name='vUTM')and " +
			"q.id=:referenceCode", nativeQuery = true)
	OrderProductSolution findByReferenceIdForVutm(@Param("referenceCode") Integer referenceCode);

}
