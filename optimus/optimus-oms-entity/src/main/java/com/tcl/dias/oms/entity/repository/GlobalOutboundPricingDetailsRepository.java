package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.GlobalOutboundPricingEngineResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for entity globaloutbound pricing detail
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface GlobalOutboundPricingDetailsRepository extends JpaRepository<GlobalOutboundPricingEngineResponse, Integer> {

    List<GlobalOutboundPricingEngineResponse> findByQuoteCode(String quoteCode);

    GlobalOutboundPricingEngineResponse findByQuoteCodeAndIsNegotiatedPrices(String quoteCode, Byte isNegoitatedPrices);

    GlobalOutboundPricingEngineResponse findByQuoteLeCodeAndIsNegotiatedPrices(String quoteCode,
            Byte isNegoitatedPrices);
}
