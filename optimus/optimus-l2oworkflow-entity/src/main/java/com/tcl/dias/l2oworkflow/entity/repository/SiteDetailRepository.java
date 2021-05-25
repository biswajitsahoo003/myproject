package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;

@Repository
public interface SiteDetailRepository extends JpaRepository<SiteDetail, Integer>{
	
	List<SiteDetail> findByStatus(String status);
	
	List<SiteDetail> findByQuoteCodeOrderByCreatedTimeDesc(String quoteCode);

	List<SiteDetail> findByQuoteId(Integer quoteId);
	
}
