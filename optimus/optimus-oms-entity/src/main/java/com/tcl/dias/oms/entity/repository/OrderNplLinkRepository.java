package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderNplLink;



/**
 * Repository class for OrderNplLink entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderNplLinkRepository extends JpaRepository<OrderNplLink,Integer>{

	public OrderNplLink findByIdAndStatus(Integer id, byte status);
	
	public List<OrderNplLink> findByProductSolutionId(Integer solutionId);
	public List<OrderNplLink>  findByProductSolutionIdAndStatus(Integer solutionId,byte status);
	
	public List<OrderNplLink> findByLinkCodeAndStatus(String linkCode,byte status);
	
	public List<OrderNplLink> findByOrderId(Integer orderId);

	public OrderNplLink findBySiteAId (Integer id);
	public OrderNplLink findBySiteBId (Integer id);



}
