package com.tcl.dias.oms.entity.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;


@Repository
public interface QuoteSiteServiceTerminationDetailsRepository  extends JpaRepository<QuoteSiteServiceTerminationDetails, Integer> {

	public QuoteSiteServiceTerminationDetails findByQuoteIllSiteToService(QuoteIllSiteToService quoteIllSiteToService);
	
	public List<QuoteSiteServiceTerminationDetails> findByQuoteToLeId(Integer quoteToLeId);
	
	public List<QuoteSiteServiceTerminationDetails> findByCreatedTimeLessThan(Date stagingDate);

	@Query(value="select qst.* from quote_site_service_termination_details qst join quote_ill_site_to_service qists on qst.quote_site_to_service_id=qists.id and qst.quote_to_le_id = :quoteToLeId and (qists.is_deleted is null or qists.is_deleted=0)",nativeQuery = true)
	List<QuoteSiteServiceTerminationDetails> findByQuoteToLeIdAndIsDeleted(@Param("quoteToLeId")Integer quoteLeId);

}
