package com.tcl.dias.serviceactivation.entity.repository;

import com.tcl.dias.serviceactivation.entity.entities.TxConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TxConfigurationRepository extends JpaRepository<TxConfiguration, Integer> {

	TxConfiguration findByServiceId(String serviceId);
	
	TxConfiguration findFirstByServiceIdAndStatusOrderByIdDesc(String serviceId,String status);

    Optional<TxConfiguration> findByServiceIdAndStatus(String serviceId, String status);
}
