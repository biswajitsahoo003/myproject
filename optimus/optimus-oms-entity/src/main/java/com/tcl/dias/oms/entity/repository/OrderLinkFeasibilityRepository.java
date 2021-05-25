package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;

/**
 * This file contains the OrderLinkFeasibilityRepository.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderLinkFeasibilityRepository extends JpaRepository<OrderLinkFeasibility,Integer>{
	
List<OrderLinkFeasibility> findByOrderNplLink(OrderNplLink orderLink);

List<OrderLinkFeasibility> findByOrderNplLinkAndIsSelected(OrderNplLink orderNplLink,Byte selected);

}
