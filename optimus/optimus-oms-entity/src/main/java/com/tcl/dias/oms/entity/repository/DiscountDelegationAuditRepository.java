package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.DiscountDelegationAudit;
/**
 * 
 * DiscountDelegationAudit table Repository class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface DiscountDelegationAuditRepository extends JpaRepository<DiscountDelegationAudit, Integer>{

}
