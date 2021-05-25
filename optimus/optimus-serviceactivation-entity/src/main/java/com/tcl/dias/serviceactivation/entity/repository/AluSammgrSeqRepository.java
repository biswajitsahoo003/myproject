package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.AluSammgrSeq;

/**
 * This file contains the AluSammgrSeqRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface AluSammgrSeqRepository extends JpaRepository<AluSammgrSeq, Integer> {
	
	AluSammgrSeq findFirstByServiceIdOrderBySammgrSeqidDesc(String serviceId);

}
