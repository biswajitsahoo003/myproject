package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderUcaas;

/**
 * Handles CRUD operation of Ucaas product.
 * 
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface OrderUcaasRepository extends JpaRepository<OrderUcaas, Integer> {
	/**
	 * find by orderToLeId
	 * 
	 * @param quoteLeId
	 * @return
	 */
	List<OrderUcaas> findByOrderToLeId(Integer quoteLeId);

	/**
	 * find by orderToLeId and isConfig
	 * 
	 * @param quoteLeId
	 * @param isConfig
	 * @return
	 */
	List<OrderUcaas> findByOrderToLeIdAndIsConfig(Integer quoteLeId, byte isConfig);

	/**
	 * find by orderToLeId, name and status
	 * 
	 * @param orderLeId
	 * @param name
	 * @param statusActive
	 * @return
	 */
	OrderUcaas findByOrderToLeIdAndNameAndStatus(Integer orderLeId, String name, byte statusActive);
	
	List<OrderUcaas> findByOrderProductSolution(OrderProductSolution orderProductSolution);
	
	List<OrderUcaas> findByOrderProductSolutionAndUomNot(OrderProductSolution orderProductSolution, String uom);
	
	List<OrderUcaas> findByOrderProductSolutionAndUomAndEndpointLocationId(OrderProductSolution orderProductSolution, String uom, Integer endpointLocationId);

}
