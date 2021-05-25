package com.tcl.dias.oms.entity.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderVrfSites;




/**
 * This file contains the OrderVrfSiteRepository.java class.
 * 
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderVrfSiteRepository  extends JpaRepository<OrderVrfSites, Integer>{
	
	List<OrderVrfSites> findByOrderIllSite(OrderIllSite orderIllSite);
	
	List<OrderVrfSites> findByOrderIllSiteAndSiteType(OrderIllSite orderIllSite,String siteType);
	
	List<OrderVrfSites> findByOrderIllSiteAndSiteTypeAndVrfType(OrderIllSite orderIllSite,String siteType,String vrfType);
	


}
