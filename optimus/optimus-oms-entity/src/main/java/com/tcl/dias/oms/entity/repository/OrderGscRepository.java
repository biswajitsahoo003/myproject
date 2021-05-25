package com.tcl.dias.oms.entity.repository;

import java.util.List;

import com.tcl.dias.oms.entity.NativeQueries;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Jpa Repository class of Order Gsc Table and its entity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderGscRepository extends JpaRepository<OrderGsc, Integer> {

	/**
	 * Get OrderGsc by AccessType
	 * 
	 * @param accessType
	 * @return {@link List<OrderGsc>}
	 */
	List<OrderGsc> findByAccessType(String accessType);

	/**
	 * Get OrderGsc by OrderProductSolution and status
	 * 
	 * @param orderProductSolution
	 * @param statusActive
	 * @return {@link List<OrderGsc>}
	 */
	List<OrderGsc> findByorderProductSolutionAndStatus(OrderProductSolution orderProductSolution, byte statusActive);

	List<OrderGsc> findByorderProductSolution(OrderProductSolution solution);

	void deleteByOrderProductSolution(OrderProductSolution productSolution);

	List<OrderGsc> findByOrderToLe(OrderToLe orderToLe);

	@Query(nativeQuery = true, value = NativeQueries.GET_ORDER_GSC_IDS_BY_DOWNSTREAM_ORDER_ID)
	List<Integer> getOrderGscIdsByDownstreamOrderId(@Param("downstreamOrderId") String downStreamOrderId,
			@Param("accessType") String accessType, @Param("productName") String productName);

	List<OrderGsc> findByorderProductSolutionInAndStatus(final Iterable<OrderProductSolution> productSolution, final byte status);

	List<OrderGsc> findByOrderProductSolutionInAndStatus(Iterable<OrderProductSolution> productSolutions, byte statusActive);
	
	@Query(nativeQuery = true, value = "select ogsc.product_name from order_gsc ogsc where ogsc.order_to_le_id = :orderToLeId")
	public List<String> findServiceTypeByOrderId(@Param("orderToLeId") int orderToLeId);


}
