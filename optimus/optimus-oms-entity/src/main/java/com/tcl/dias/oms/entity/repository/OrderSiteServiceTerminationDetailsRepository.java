package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderSiteServiceTerminationDetails;


@Repository
public interface OrderSiteServiceTerminationDetailsRepository  extends JpaRepository<OrderSiteServiceTerminationDetails, Integer> {

    public OrderSiteServiceTerminationDetails findByOrderIllSiteToService(OrderIllSiteToService orderIllSiteToService);


}
