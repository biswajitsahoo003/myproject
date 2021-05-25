package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
/**
 * 
 * This is the repository class for QuoteIzosdwanByonUploadDetail
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIzosdwanByonUploadDetailRepository extends JpaRepository<QuoteIzosdwanByonUploadDetail, Integer>{
	
	public List<QuoteIzosdwanByonUploadDetail> findByQuote(Quote quote);
	
	public List<QuoteIzosdwanByonUploadDetail> findByStatus(String status);
	
	public List<QuoteIzosdwanByonUploadDetail> findByStatusAndQuote_id(String status,Integer quoteId);
	
	public List<QuoteIzosdwanByonUploadDetail> findByStatusNotInAndLocationIdIsNotNull(List<String> status);
	
	@Query(value="select distinct quote_id from quote_izosdwan_byon_upload_details where quote_id in"
			+ "(select distinct quote_id from  quote_izosdwan_byon_upload_details where status='CLOSED' "
			+ "and location_id is not null) and quote_id not in(select distinct quote_id from quote_izosdwan_byon_upload_details"
			+ " where location_id is null and status in('OPEN','INPROGRESS','CLOSED','FAILURE','COMPLETED') "
			+ "and quote_id in(select distinct quote_id from quote_izosdwan_byon_upload_details where status='CLOSED' "
			+ "and location_id is not null))",nativeQuery = true)
	public List<Integer> getUploadSuccessQuotes();
	
	public List<QuoteIzosdwanByonUploadDetail> findByQuote_idIn(List<Integer> quoteIds);
	
	public List<QuoteIzosdwanByonUploadDetail> findByStatusInAndQuote_id(List<String> status,Integer quoteId);
	
	public List<QuoteIzosdwanByonUploadDetail> findByQuote_idAndLocationErrorDetailsIsNotNull(Integer quoteId);
	
	@Query(value="select * from quote_izosdwan_byon_upload_details where quote_id=:quoteId and site_type like '%Dual CPE%'",nativeQuery = true)
	public List<QuoteIzosdwanByonUploadDetail> selectSiteTypeByQuote(Integer quoteId);
	
	public List<QuoteIzosdwanByonUploadDetail> findByQuote_id(Integer quoteId);
	
	@Query(value="select * from quote_izosdwan_byon_upload_details where quote_id=:quoteId and site_type=:siteType ",nativeQuery = true)
	public List<QuoteIzosdwanByonUploadDetail> findByQuote_idAndSite_type(Integer quoteId,String siteType);
}
