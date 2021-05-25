package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
/**
 * This file contains the WanStaticRouteRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface WanStaticRouteRepository extends JpaRepository<WanStaticRoute, Integer> {
	
	public List<WanStaticRoute> findByStaticProtocol(StaticProtocol staticProtocol);
}
