package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.SiteCategory;

/**
 * This file contains the SiteCategoryRepository.java class.
 * 
 *
 * @author Steffi.Das
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SiteCategoryRepository extends JpaRepository<SiteCategory, Integer> {
	
	public SiteCategory findBySiteCategory(String siteCategory);

	public SiteCategory findByErfLocSitebLocationId(Integer erfLocSitebLocationId);
	
	public List<SiteCategory> findAllByQuoteId(Integer quoteId);
	
}
