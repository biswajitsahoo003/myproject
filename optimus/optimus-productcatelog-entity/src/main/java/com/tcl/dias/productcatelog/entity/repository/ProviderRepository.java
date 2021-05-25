package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.productcatelog.entity.entities.Provider;
/**
 * Repo for Provider entity
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
	
	List<Provider> findByIsActive(String isActive);

	Provider findByName(String providerName);

}
