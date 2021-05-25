package com.tcl.dias.oms.entity.repository;

import java.util.List;

import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;

/**
 * 
 * Repository Class
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderIllSiteToServiceRepository  extends JpaRepository<OrderIllSiteToService, Integer> {
	
	public List<OrderIllSiteToService> findByOrderIllSite(OrderIllSite orderIllSite);
	public List<OrderIllSiteToService> findByOrderIzosdwanSite(OrderIzosdwanSite orderIzosdwanSite);
	public List<OrderIllSiteToService> findByOrderIllSite_Id(Integer orderIllSiteId);
	public List<OrderIllSiteToService> findByOrderToLe_Id(Integer orderToLeId);
	public List<OrderIllSiteToService> findByErfServiceInventoryTpsServiceIdAndOrderToLe(String serviceId, OrderToLe orderToLe);
	public List<OrderIllSiteToService> findByOrderIllSite_IdAndOrderToLe_Id(Integer orderIllSiteId,Integer orderToLeId);
	public List<OrderIllSiteToService> findByTpsSfdcParentOptyIdAndOrderToLe(Integer parentOptyId,OrderToLe orderToLe);
	public List<OrderIllSiteToService> findByOrderNplLink_IdAndOrderToLe(Integer orderNplLinkId,OrderToLe orderToLe);
	public List<OrderIllSiteToService> findByOrderNplLink_Id(Integer orderNplLinkId);
	public List<OrderIllSiteToService> findByOrderIllSite_IdAndType(Integer orderIllSiteId, String Type);

}
