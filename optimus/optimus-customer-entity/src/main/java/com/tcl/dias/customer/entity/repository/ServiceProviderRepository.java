package com.tcl.dias.customer.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.ServiceProvider;

/**
 * This file contains the Service Provider Repository.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {
	Optional<ServiceProvider> findByName(String name);
}
