package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.tcl.dias.oms.entity.entities.OrderIzosdwanCgwDetail;

/**
 * 
 * This file contains the OrderIzosdwanCgwDetailRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanCgwDetailRepository extends JpaRepository<OrderIzosdwanCgwDetail, Integer>{

	List<OrderIzosdwanCgwDetail> findByOrderId(Integer id);

}
