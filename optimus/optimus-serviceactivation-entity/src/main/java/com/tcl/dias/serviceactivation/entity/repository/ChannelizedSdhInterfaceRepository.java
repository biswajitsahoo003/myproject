package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
/**
 * This file contains the ChannelizedSdhInterfaceRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ChannelizedSdhInterfaceRepository extends JpaRepository<ChannelizedSdhInterface, Integer> {
}
