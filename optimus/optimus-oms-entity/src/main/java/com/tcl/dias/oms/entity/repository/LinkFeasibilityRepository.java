package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;

@Repository
public interface LinkFeasibilityRepository extends JpaRepository<LinkFeasibility,Integer>{
	
	Optional<LinkFeasibility> findById(Integer id);
	
	Optional<LinkFeasibility> findByIdAndQuoteNplLink_Id(Integer id,Integer linkId);
	
	List<LinkFeasibility> findByQuoteNplLinkAndFeasibilityCheck(QuoteNplLink quoteNplLink,String feasibilityCheck);
	
	List<LinkFeasibility> findByQuoteNplLinkAndFeasibilityMode(QuoteNplLink quoteNplLink,String feasibilityMode);
	
	List<LinkFeasibility> findByQuoteNplLink_IdAndIsSelected(Integer site,byte selected);

	List<LinkFeasibility> findByQuoteNplLink(QuoteNplLink quoteNplLink);
	
	List<LinkFeasibility> findByQuoteNplLinkAndIsSelected(QuoteNplLink quoteNplLink,byte selected);
	
	List<LinkFeasibility> findByQuoteNplLink_Id(Integer linkId);
	
	List<LinkFeasibility> findByQuoteNplLink_IdAndTypeIn(Integer linkId, List<String> type);

}
