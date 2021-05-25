package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteUcaas;

import java.util.List;
import java.util.Set;

/**
 * Handles CRUD operation of Ucaas product.
 * 
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface QuoteUcaasRepository  extends JpaRepository<QuoteUcaas, Integer>{

	QuoteUcaas findByQuoteToLeIdAndStatus(Integer quoteLeId, byte statusActive);
	
	QuoteUcaas findByQuoteToLeIdAndNameAndStatus(Integer quoteLeId,String name, byte statusActive);

	List<QuoteUcaas> findByProductSolutionIdAndStatus(final ProductSolution productSolutionId, final byte status);

	List<QuoteUcaas> findByQuoteToLeId(Integer quoteLeId);

	List<QuoteUcaas> findByQuoteToLeAndIsConfig(QuoteToLe quoteToLe,byte isConfig);
	
	List<QuoteUcaas> findByQuoteToLeIdAndIsConfig(Integer quoteLeId, byte isConfig);

	@Query(value="select * from quote_ucaas q where q.quote_to_le_id = :quoteToLeId and q.endpoint_location_id is not null",nativeQuery = true)
	Set<QuoteUcaas> findByQuoteToLeIdAndLocationNotNull(Integer quoteToLeId);

}
