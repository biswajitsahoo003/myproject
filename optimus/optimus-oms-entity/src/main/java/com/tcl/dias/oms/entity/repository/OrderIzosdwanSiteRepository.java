package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;



/**
 * 
 * This file contains the OrderIzosdwanSiteRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanSiteRepository extends JpaRepository<OrderIzosdwanSite, Integer>,CrudRepository<OrderIzosdwanSite, Integer>{
	
	OrderIzosdwanSite findByIdAndStatus(Integer id,byte status);

	List<OrderIzosdwanSite> findByOrderProductSolutionAndStatus(OrderProductSolution orderProductSolution,
			byte status);
	
	Optional<OrderIzosdwanSite> findById(Integer id);
	
	public List<OrderIzosdwanSite> findBySiteCodeAndStatus(String code, byte status);


	public void deleteByOrderProductSolution(OrderProductSolution solution);

	
	@Query(value="select distinct izosdwan_site_type from order_izosdwan_sites where product_solutions_id=:id",nativeQuery = true)
	public List<String> getDistinctSiteTypesForSdwan(Integer id);
	
	public List<OrderIzosdwanSite> findByOrderProductSolutionAndIzosdwanSiteType(OrderProductSolution productSolution,String type);

	List<OrderIzosdwanSite> findByOrderProductSolution(OrderProductSolution solutions);


	List<OrderIzosdwanSite> findByStatusAndIdIn(Byte bactive, List<Integer> siteIds);

	List<OrderIzosdwanSite> findByOrderProductSolutionAndErfLocSitebLocationId(OrderProductSolution solutions, Integer id);

	List<OrderIzosdwanSite> findByOrderProductSolutionAndIzosdwanSiteTypeAndErfLocSitebLocationId(
			OrderProductSolution solutions, String type, Integer id);

	
}
