package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRServiceChargeView;

/**
 * Repository for Teams DR service charge View
 * 
 * @author Srinivasa Raghavan
 *
 */
@Repository
public interface TeamsDRServiceChargeViewRepository extends JpaRepository<TeamsDRServiceChargeView, String> {

}
