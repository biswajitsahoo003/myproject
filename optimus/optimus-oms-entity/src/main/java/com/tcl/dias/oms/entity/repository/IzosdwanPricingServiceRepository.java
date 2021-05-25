package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.IzosdwanPricingService;

/**
 * 
 * This file contains the IzosdwanPricingServiceRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IzosdwanPricingServiceRepository extends JpaRepository<IzosdwanPricingService, Integer>{
	
	List<IzosdwanPricingService> findByRefId(String referenceCode);
	
	@Query(value="select t.*  from izosdwan_pricing_services t where TRIM(t.status)=:status and mode=:mode and t.priority in (select min(priority) "
			+ "from izosdwan_pricing_services where t.ref_id=ref_id and status=:status and mode=:mode order by created_time asc)  order by t.id asc",nativeQuery = true)
	List<IzosdwanPricingService> findPricingServicesDetailsByStatus(@Param("status") String status,@Param("mode") String mode);
	
	
	List<IzosdwanPricingService> findByRefIdAndStatusIn(String referenceCode,List<String> statusList);

}
