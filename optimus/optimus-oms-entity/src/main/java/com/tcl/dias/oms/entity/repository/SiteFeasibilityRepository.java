package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;

/**
 * This file contains the repository for site feasibility class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SiteFeasibilityRepository extends JpaRepository<SiteFeasibility, Integer> {

	Optional<SiteFeasibility> findById(Integer id);

	Optional<SiteFeasibility> findByIdAndQuoteIllSite_Id(Integer id, Integer siteId);

	List<SiteFeasibility> findByQuoteIllSiteAndFeasibilityCheck(QuoteIllSite quoteIllSite, String feasibilityCheck);

	List<SiteFeasibility> findByQuoteIllSiteAndFeasibilityMode(QuoteIllSite quoteIllSite, String feasibilityMode);

	List<SiteFeasibility> findByQuoteIllSite_IdAndIsSelected(Integer site, byte selected);

	List<SiteFeasibility> findByQuoteIllSite(QuoteIllSite quoteIllSite);

	List<SiteFeasibility> findByQuoteIllSiteAndIsSelected(QuoteIllSite quoteIllSite, byte selected);

	List<SiteFeasibility> findByQuoteIllSite_IdAndIsSelectedAndType(Integer site, byte selected, String type);
	
	List<SiteFeasibility> findByQuoteIllSite_IdAndType(Integer site,String type);

	List<SiteFeasibility> findByFeasibilityCodeAndTypeAndFeasibilityType(String code, String type,
			String feasibilityType);

    List<SiteFeasibility> findByQuoteIllSite_Id(Integer siteId);

    List<SiteFeasibility> findByQuoteIllSite_IdAndTypeAndFeasibilityMode(Integer site,String type,String feasibilityMode);
        
    @Query(value = "select distinct(type) as type, site_id as site_id from site_feasibility where  site_id in (:siteIds)", nativeQuery = true)
    List<Map<String,Object>> findDistinctTypeAndSiteIdIn(Set<Integer> siteIds);
}
