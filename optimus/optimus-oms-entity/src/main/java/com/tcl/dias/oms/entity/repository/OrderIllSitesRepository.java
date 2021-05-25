package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;

/**
 * This file contains the OrderIllSitesRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderIllSitesRepository extends JpaRepository<OrderIllSite, Integer> {
	
	OrderIllSite findByIdAndStatus(Integer id,byte status);

	List<OrderIllSite> findByOrderProductSolutionAndStatus(OrderProductSolution orderProductSolution,
			byte status);
	
	Optional<OrderIllSite> findById(Integer id);
	
	public List<OrderIllSite> findBySiteCodeAndStatus(String code, byte status);


	public void deleteByOrderProductSolution(OrderProductSolution solution);


}
