package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.ViewGscServiceCircuitLinkDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * This file contains the VwGscServiceCircuitLinkDetailRepository.java class.
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface VwGscServiceCircuitLinkDetailRepository extends JpaRepository<ViewGscServiceCircuitLinkDetail, Integer> {

	/**
	 * Query to get all service details based on access type and customer id
	 * @param customerId
	 * @param accessType
	 * @param page
	 * @param size
	 * @return
	 */
	@Query(value = "select * from vw_gsc_srvc_circt_link_dtl where erf_cust_customer_id =  :customerId limit :page, :size", nativeQuery = true)
	List<Map<String, Object>> getServiceDetailsByCustomerId(@Param("customerId") Integer customerId,@Param("page") Integer page,@Param("size") Integer size);

	/**
	 * Query to get count of service details
	 *
	 * @param customerId
	 * @return
	 */
	@Query(value = "select count(*) as count from vw_gsc_srvc_circt_link_dtl where erf_cust_customer_id =  :customerId", nativeQuery = true)
	Integer getCountOfServiceDetails(@Param("customerId") Integer customerId);

	Page<ViewGscServiceCircuitLinkDetail> findAll(Specification<ViewGscServiceCircuitLinkDetail> serviceDetailsSpec, Pageable pageable);
}
