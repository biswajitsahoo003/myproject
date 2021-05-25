package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.PolicycriteriaProtocol;

@Repository
public interface PolicycriteriaProtocolRepository extends JpaRepository<PolicycriteriaProtocol, Integer>  {

    PolicycriteriaProtocol findFirstByPolicyCriteria_policyCriteriaId(Integer policyCriteriaId);
}
