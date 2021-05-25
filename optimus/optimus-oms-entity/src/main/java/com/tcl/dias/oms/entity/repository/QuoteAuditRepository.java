package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteAudit;

@Repository
public interface QuoteAuditRepository extends JpaRepository<QuoteAudit, Integer> {
	List<QuoteAudit> findByQuoteCode(String quoteCode);
}
