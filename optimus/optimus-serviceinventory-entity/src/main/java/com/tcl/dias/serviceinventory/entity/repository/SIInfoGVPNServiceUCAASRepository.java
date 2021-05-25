package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIInfoGVPNServiceUCAAS;

/**
 * Repository class for SIInfoGVPNServiceUCAAS
 *
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface SIInfoGVPNServiceUCAASRepository extends JpaRepository<SIInfoGVPNServiceUCAAS, Integer> {
    /**
     * This Query is used to get service information of GVPN for UCAAS based on customer ID and size
     * @param customerId
     * @return
     */
	@Query(value = "SELECT * FROM vw_gvpn_services_qlfy_ucaas s where s.is_active = 'Y' and s.order_customer_id=:customerId limit :page, :size", nativeQuery = true)
	List<SIInfoGVPNServiceUCAAS> findByCustomerIdAndSize(@Param("customerId") String customerId,
			@Param("page") Integer page, @Param("size") Integer size);

	/**
	 * This query is used to get details using search specification
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<SIInfoGVPNServiceUCAAS> findAll(Specification<SIInfoGVPNServiceUCAAS> spec, Pageable pageable);
	
	/**
	 * This query is used to get details by customer ID
	 * 
	 * @param customerId
	 * @return
	 */
	@Query(value = "SELECT * FROM vw_gvpn_services_qlfy_ucaas s where s.order_customer_id=:customerId", nativeQuery = true)
	List<SIInfoGVPNServiceUCAAS> findByCustomerId(@Param("customerId") String customerId);

	/**
	 * This query is used to get details by service ID
	 * 
	 * @param serviceId
	 * @return
	 */
	@Query(value = "SELECT * FROM vw_gvpn_services_qlfy_ucaas s where s.srv_service_id =:serviceId", nativeQuery = true)
	SIInfoGVPNServiceUCAAS findByServiceId(@Param("serviceId") String serviceId);
}
