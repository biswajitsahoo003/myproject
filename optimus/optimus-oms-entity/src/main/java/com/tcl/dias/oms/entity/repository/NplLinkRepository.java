package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;

/**
 * Repository class for QuoteNplLink entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface NplLinkRepository extends JpaRepository<QuoteNplLink,Integer>{

	public QuoteNplLink findByIdAndStatus(Integer id, byte status);
	
	public List<QuoteNplLink> findByProductSolutionId(Integer solutionId);
	
	public List<QuoteNplLink> findByProductSolutionIdAndStatus(Integer solutionId, byte status );
	
	public List<QuoteNplLink> findByProductSolutionIdAndStatusAndFeasibility(Integer solutionId, byte status, byte feasStatus );

	
	public Optional<QuoteNplLink> findByProductSolutionIdAndSiteAIdAndSiteBIdAndStatus (Integer solutionId, Integer siteAId, Integer siteBId,byte status);
	
	public List<QuoteNplLink> findByQuoteIdAndStatus(Integer quoteId, byte status); 
	
	public void deleteByProductSolutionId(Integer solutionId);

	@Query(value = "select qnl.* from quote_npl_link qnl left join product_solutions ps on qnl.product_solution_id = ps.id\n" +
			"left join quote_to_le_product_family qtlpf on ps.quote_le_product_family_id = qtlpf.id\n" +
			"left join quote_to_le qtl on qtlpf.quote_to_le_id = qtl.id left join quote q on qtl.quote_id = q.id\n" +
			"where q.quote_code = :quoteCode", nativeQuery = true)
    List<QuoteNplLink> findLinks(@Param("quoteCode") String quoteCode);
	
	Optional<QuoteNplLink> findById(Integer id);
	
	public List<QuoteNplLink> findByLinkCodeAndStatus(String linkCode, byte status);
	
}
