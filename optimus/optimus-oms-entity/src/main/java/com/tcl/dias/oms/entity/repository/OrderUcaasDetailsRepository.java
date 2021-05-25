package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderUcaas;
import com.tcl.dias.oms.entity.entities.OrderUcaasDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface OrderUcaasDetailsRepository extends JpaRepository<OrderUcaasDetail, Integer> {
	
	List<OrderUcaasDetail> findByOrderUcaas(OrderUcaas orderUcaas);
}