package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;

/**
 * 
 * This file contains the OrderIzosdwanSiteFeasibilityRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanSiteFeasibilityRepository extends JpaRepository<OrderIzosdwanSiteFeasibility, Integer>{
	List<OrderIzosdwanSiteFeasibility> findByOrderIzosdwanSite(OrderIzosdwanSite orderIllSite);

	List<OrderIzosdwanSiteFeasibility> findByOrderIzosdwanSiteAndIsSelected(OrderIzosdwanSite orderIllSite, Byte selected);
	
	List<OrderIzosdwanSiteFeasibility> findByOrderIzosdwanSiteIdAndIsSelected(Integer siteId, Byte selected);

	List<OrderIzosdwanSiteFeasibility> findByOrderIzosdwanSiteIdAndIsSelectedAndType(Integer siteId, Byte selected, String type);

}
