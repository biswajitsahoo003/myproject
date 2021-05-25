package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.SiteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteDetailRepository extends JpaRepository<SiteDetail, Integer>{
	
	List<SiteDetail> findByStatus(String status);
	
	List<SiteDetail> findByQuoteCodeOrderByCreatedTimeDesc(String quoteCode);

	
}
