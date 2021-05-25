package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstServiceCommunity;

/**
 * This file contains the MstServiceCommunityRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstServiceCommunityRepository extends JpaRepository<MstServiceCommunity, Integer> {

	List<MstServiceCommunity> findByServiceSubtype(String subType);
}
