package com.tcl.dias.oms.entity.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * 
 * Repository Class
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIllSiteToServiceRepository  extends JpaRepository<QuoteIllSiteToService, Integer> {
	
	public List<QuoteIllSiteToService> findByQuoteIllSite(QuoteIllSite quoteIllSite);
	public List<QuoteIllSiteToService> findByQuoteIllSite_Id(Integer quoteIllSiteId);
	public List<QuoteIllSiteToService> findByQuoteIllSite_IdAndQuoteToLe_Id(Integer quoteIllSiteId,Integer quoteToLeId);
	public List<QuoteIllSiteToService> findByQuoteToLe_Id(Integer quoteToLeId);
	List<QuoteIllSiteToService> findByQuoteIllSiteAndBandwidthChanged(QuoteIllSite quoteIllSite,Byte bandwidthChanged);
	public List<QuoteIllSiteToService> findByErfServiceInventoryTpsServiceIdAndQuoteToLe(String serviceId, QuoteToLe quoteToLe);
	public List<QuoteIllSiteToService> findByErfServiceInventoryTpsServiceIdInAndQuoteToLe(List<String> serviceId, QuoteToLe quoteToLe);
	public List<QuoteIllSiteToService> findByTpsSfdcParentOptyIdAndQuoteToLe(Integer parentOptyId,QuoteToLe quoteToLe);
	public List<QuoteIllSiteToService> findByQuoteNplLink_IdAndQuoteToLe(Integer nplLinkId,QuoteToLe quoteToLe);
	public List<QuoteIllSiteToService> findByParentSiteId(Integer parentSiteId);
	public QuoteIllSiteToService findByO2cServiceId(String o2cServiceId);
	public List<QuoteIllSiteToService> findByQuoteIllSite_IdAndErfServiceInventoryTpsServiceIdAndType(Integer quoteSiteId, String serviceId, String type);
	public List<QuoteIllSiteToService> findByQuoteIzosdwanSite_IdAndErfServiceInventoryTpsServiceIdAndType(Integer quoteSiteId, String serviceId, String type);
	public List<QuoteIllSiteToService> findByQuoteIllSite_IdAndType(Integer quoteSiteId, String type);
	public List<QuoteIllSiteToService> findByQuoteIzosdwanSite(QuoteIzosdwanSite quoteIzosdwanSite);

	public List<QuoteIllSiteToService> findByQuoteNplLink_Id(Integer quoteNplLink);
	public List<QuoteIllSiteToService> findByCancelledParentSiteIdAndQuoteToLe_Id(Integer parentSiteId, Integer quoteToLeId);

	public QuoteIllSiteToService findByTpsSfdcOptyId(String sfdcId);

	public List<QuoteIllSiteToService> findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(Integer quoteToLeId, Integer isDeleted);
	
	//@Lock(LockModeType.NONE)
	//@Query(nativeQuery = true,value="select * from quote_ill_site_to_service where erf_service_inventory_tps_service_id=:serviceId and quote_to_le_id=:quoteLeId")
	//public List<QuoteIllSiteToService> findByQuoteToLeIdAndErfServiceInventoryTpsServiceId(@Param("serviceId") String serviceId,@Param("serviceId")  Integer quoteLeId);
	
	@Modifying
	@Query(value = "update quote_ill_site_to_service set tps_sfdc_opty_id=:tpsSfdcOptyId where id=:id", nativeQuery = true)
	@Transactional
	void updateTpsSfdcOptyId(@Param ("tpsSfdcOptyId")String tpsSfdcOptyId,@Param("id") Integer id);
	
	@Modifying
	@Query(value = "update quote_ill_site_to_service set tps_sfdc_product_id=:tpsSfdcProductId,tps_sfdc_product_name=:tpsSfdcProductName where id=:id", nativeQuery = true)
	@Transactional
	void updateTpsSfdcProdIdAndTpsSfdcProdName(@Param ("tpsSfdcProductId")String tpsSfdcProductId,@Param ("tpsSfdcProductName")String tpsSfdcProductName,@Param("id") Integer id);
}
