package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.QuoteUcaasDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface QuoteUcaasDetailsRepository extends JpaRepository<QuoteUcaasDetail, Integer> {
    Optional<QuoteUcaasDetail> findByQuoteUcaasId(Integer quoteUcaasId);
}