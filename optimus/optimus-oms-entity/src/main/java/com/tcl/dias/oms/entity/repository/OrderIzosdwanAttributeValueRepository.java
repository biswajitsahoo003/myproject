package com.tcl.dias.oms.entity.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.tcl.dias.oms.entity.entities.OrderIzosdwanAttributeValue;
/**
 * 
 * This file contains the OrderIzosdwanAttributeValueRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanAttributeValueRepository extends JpaRepository<OrderIzosdwanAttributeValue, Integer>{

	List<OrderIzosdwanAttributeValue> findByOrderId(Integer orderId);

}
