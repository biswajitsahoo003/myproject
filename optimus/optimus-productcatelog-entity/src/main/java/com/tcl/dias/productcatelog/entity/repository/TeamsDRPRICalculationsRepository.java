package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRPRICalculations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * The Repository class for the vw_mstmdr_mediagateway_AC_PRI_Calculations table
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface TeamsDRPRICalculationsRepository extends JpaRepository<TeamsDRPRICalculations, Integer> {
    Optional<TeamsDRPRICalculations> findByPriValue(Integer priValue);
}
