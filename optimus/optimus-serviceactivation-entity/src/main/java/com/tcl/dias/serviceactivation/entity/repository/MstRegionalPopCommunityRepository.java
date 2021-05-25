package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstRegionalPopCommunity;
/**
 * This file contains the MstRegionalPopCommunityRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstRegionalPopCommunityRepository extends JpaRepository<MstRegionalPopCommunity, Integer> {
	
	List<MstRegionalPopCommunity> findByRouterHostname(String hostName);
	
}
