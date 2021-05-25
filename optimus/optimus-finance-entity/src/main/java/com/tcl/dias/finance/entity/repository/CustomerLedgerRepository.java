package com.tcl.dias.finance.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.finance.entity.entities.CustomerLedger;

@Repository
public interface CustomerLedgerRepository extends JpaRepository<CustomerLedger, Integer>{
	
}
