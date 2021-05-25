package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;

/**
 * This file contains the OrderToLeProductFamily.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderToLeProductFamilyRepository extends JpaRepository<OrderToLeProductFamily, Integer> {

	List<OrderToLeProductFamily> findByOrderToLe(OrderToLe orderToLe);
	OrderToLeProductFamily findByOrderToLeAndMstProductFamily(OrderToLe orderToLe,MstProductFamily mstProductFamily);
	
	List<OrderToLeProductFamily> findByMstProductFamilyOrderByIdAsc(MstProductFamily mstProductFamily);

	/**
	 * Get OrderToLeProductFamily by OrderLeId
	 * @param orderLeId
	 * @return {@link OrderToLeProductFamily}
	 */
	OrderToLeProductFamily findByOrderToLe_Id(Integer orderLeId);

	void deleteAllByOrderToLe(OrderToLe orderToLe);
}
