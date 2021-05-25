package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.HsrpVrrpProtocol;
/**
 * This file contains the HsrpVrrpProtocolRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface HsrpVrrpProtocolRepository extends JpaRepository<HsrpVrrpProtocol, Integer> {
    HsrpVrrpProtocol findByEthernetInterface_ethernetInterfaceId(Integer ethernetInterfaceId);
}
