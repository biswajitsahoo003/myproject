package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
@Repository
public interface IzosdwanSiteFeasiblityRepository extends JpaRepository<IzosdwanSiteFeasibility, Integer>{
	Optional<IzosdwanSiteFeasibility> findById(Integer id);

	Optional<IzosdwanSiteFeasibility> findByIdAndQuoteIzosdwanSite_Id(Integer id, Integer siteId);

	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSiteAndFeasibilityCheck(QuoteIzosdwanSite quoteIzosdwanSite, String feasibilityCheck);

	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSiteAndFeasibilityMode(QuoteIzosdwanSite quoteIzosdwanSite, String feasibilityMode);

	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSite_IdAndIsSelected(Integer site, byte selected);

	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSite(QuoteIzosdwanSite quoteIzosdwanSite);

	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSiteAndIsSelected(QuoteIzosdwanSite quoteIzosdwanSite, byte selected);

	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSite_IdAndIsSelectedAndType(Integer site, byte selected, String type);
	
	List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSite_IdAndType(Integer site,String type);

	List<IzosdwanSiteFeasibility> findByFeasibilityCodeAndTypeAndFeasibilityType(String code, String type,
			String feasibilityType);

    List<IzosdwanSiteFeasibility> findByQuoteIzosdwanSite_Id(Integer siteId);
}
