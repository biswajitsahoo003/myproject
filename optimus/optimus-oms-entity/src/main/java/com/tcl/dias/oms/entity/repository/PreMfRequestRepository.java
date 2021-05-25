package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.CommercialQuoteAudit;
import com.tcl.dias.oms.entity.entities.PreMfRequest;

@Repository
public interface PreMfRequestRepository extends JpaRepository<PreMfRequest, Integer> {
	public List<PreMfRequest> findByMfQuoteCode(String quoteCode);
	public List<PreMfRequest> findByMfQuoteId(Integer quoteId);

	
}
