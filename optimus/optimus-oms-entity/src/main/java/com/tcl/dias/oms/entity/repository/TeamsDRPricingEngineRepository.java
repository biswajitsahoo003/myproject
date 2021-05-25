package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.TeamsDRPricingEngine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for teamsdr pricing engine response
 * and request
 * @author Syed Ali.
 * @createdAt 23/03/2021, Tuesday, 15:34
 */

@Repository
public interface TeamsDRPricingEngineRepository extends JpaRepository<TeamsDRPricingEngine, Integer> {
}
