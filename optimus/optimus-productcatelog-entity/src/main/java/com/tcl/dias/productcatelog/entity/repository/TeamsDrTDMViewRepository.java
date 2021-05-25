package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDrTDMView;

/**
 * Repository interface for TeamsDR TDM view
 * 
 * @author Srinivasa Raghavan
 *
 */
@Repository
public interface TeamsDrTDMViewRepository extends JpaRepository<TeamsDrTDMView, String> {

	TeamsDrTDMView findByMediaGatewayNm(String mgName);
}
