package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;

/**
 * Repository for SdwanEnpoint entity
 * @author archchan
 *
 */

@Repository
public interface SdwanEndpointsRepository extends JpaRepository<SdwanEndpoints, Integer> {

	SdwanEndpoints findByServerCode(String serverCode);
	
	List<SdwanEndpoints> findByServerCodeIn(Set<String> serverCodes);
	
	List<SdwanEndpoints> findByServerCodeAndIsDirector(String serverCode, byte isDirector);
}
