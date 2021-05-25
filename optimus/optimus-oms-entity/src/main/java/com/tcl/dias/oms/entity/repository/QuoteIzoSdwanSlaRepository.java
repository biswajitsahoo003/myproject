package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSiteSla;

@Repository
public interface QuoteIzoSdwanSlaRepository extends JpaRepository<QuoteIzosdwanSiteSla, Integer>{
	
	List<QuoteIzosdwanSiteSla> findByQuoteIzosdwanSite(QuoteIzosdwanSite izoSdwanSiteSla);
}
