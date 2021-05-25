package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.MfDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MfDetailRepository extends JpaRepository<MfDetail, Integer> {
	
	    List<MfDetail> findByQuoteIdAndSiteId(Integer quoteId,Integer siteId);
	    List<MfDetail> findByQuoteId(Integer quoteId);
	    List<MfDetail> findByCreatedByAndIsPreMfTaskOrderByIdDesc(String username,String isPreMf);

	    List<MfDetail>  findByQuoteIdAndSiteIdAndIsActiveAndSiteType(Integer quoteId, Integer siteId, Integer inactive, String siteType);
		List<MfDetail> findBySiteId(Integer siteId);
		//MfDetail findByLinkIdAndSiteType(Integer quoteId,String siteType);
		List<MfDetail> findByLinkId(Integer linkId);
	    List<MfDetail> findByQuoteIdAndStatus(Integer quoteId,String status);
		MfDetail findByLinkIdAndSiteTypeAndStatus(Integer quoteId,String siteType,String status);
		List<MfDetail> findBySiteIdAndStatus(Integer siteId,String status);
		List<MfDetail> findByLinkIdAndStatus(Integer linkId,String status);
}
