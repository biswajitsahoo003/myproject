package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRSIPDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The Repository class for the vw_mstmdr_mediagateway_AC_SIP table
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface TeamsDRSIPDetailsRepository extends JpaRepository<TeamsDRSIPDetails, Integer> {
    Optional<TeamsDRSIPDetails> findByMaxSessionValueAndIsRedundant(Integer maxSessionValue,String isRedundant);

    TeamsDRSIPDetails findByMediaGatewayNm(String mgName);
}
