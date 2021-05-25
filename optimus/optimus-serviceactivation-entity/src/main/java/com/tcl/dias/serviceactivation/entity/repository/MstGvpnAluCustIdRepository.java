package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstGvpnAluCustId;

/**
 * This file contains the MstGvpnAluCustId.java Repository class.
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface MstGvpnAluCustIdRepository extends JpaRepository<MstGvpnAluCustId, Integer> {
	
	MstGvpnAluCustId findByCrnId(String crnId);

}
