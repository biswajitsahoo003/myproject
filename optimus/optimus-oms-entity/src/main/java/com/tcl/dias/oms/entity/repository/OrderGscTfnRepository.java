package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Jpa Repository for Order GSC TFN numbers
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderGscTfnRepository extends JpaRepository<OrderGscTfn, Integer> {

	/**
	 * Find TFN numbers by order detail
	 * @param orderGscDetail
	 * @return
	 */
	List<OrderGscTfn> findByOrderGscDetail(OrderGscDetail orderGscDetail);

	/**
	 * Find the Order GSC TFN by tfnNumber
	 * @param tfnNumber
	 * @return
	 */
	List<OrderGscTfn> findAllByTfnNumber(String tfnNumber);

	/**
	 * Find list Order GSC TFN by OrderGscDetailId
	 * @param tfnNumber
	 * @return
	 */
	List<OrderGscTfn> findByOrderGscDetailId(Integer orderGscDetailId);

	OrderGscTfn findByTfnNumberAndOrderGscDetail(String tfnNumber, OrderGscDetail orderGscDetail);

	List<OrderGscTfn> deleteByOrderGscDetail(OrderGscDetail orderGscDetail);
}
