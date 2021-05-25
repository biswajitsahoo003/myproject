package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.ServiceInvRf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceInvRfRepository extends JpaRepository<ServiceInvRf,Integer> {

	void deleteAllByServiceCode(String uuid);
	void deleteAllById(int id);

	List<ServiceInvRf> findByServiceCode(String uuid);

	List<ServiceInvRf> findByServiceCodeIsNull();

	@Query(value = "SELECT * FROM service_inv_rf_data where ss_ip=:ssIp and status='ACTIVE' or ss_ip=:ssIp and service_status='TERMINATE'", nativeQuery = true)
	ServiceInvRf findWithSsIpAndStatus(@Param("ssIp") String ssIp);

	@Query(value = "SELECT * FROM service_inv_rf_data where provider=:provider and status='ACTIVE' or provider=:provider and service_status='TERMINATE' ", nativeQuery = true)
	List<ServiceInvRf> findWithProviderAndStatus(@Param("provider") String provider);
}
