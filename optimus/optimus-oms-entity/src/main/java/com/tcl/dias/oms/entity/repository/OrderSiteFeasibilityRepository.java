package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;

/**
 * This file contains the OrderSiteFeasibilityRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderSiteFeasibilityRepository extends JpaRepository<OrderSiteFeasibility, Integer> {
	List<OrderSiteFeasibility> findByOrderIllSite(OrderIllSite orderIllSite);

	List<OrderSiteFeasibility> findByOrderIllSiteAndIsSelected(OrderIllSite orderIllSite, Byte selected);

	List<OrderSiteFeasibility> findByOrderIllSiteIdAndIsSelectedAndType(Integer siteId, Byte selected, String type);

}
