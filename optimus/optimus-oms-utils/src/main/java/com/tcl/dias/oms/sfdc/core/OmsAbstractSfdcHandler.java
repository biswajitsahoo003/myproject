package com.tcl.dias.oms.sfdc.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;

/**
 * This file contains the OmsAbstractSfdcService.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class OmsAbstractSfdcHandler {
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	/**
	 * 
	 * familyName
	 * @param quoteToLe
	 * @return
	 */

	protected String familyName(QuoteToLe quoteToLe) {

		if (quoteToLe.getQuoteToLeProductFamilies() != null) {
			QuoteToLeProductFamily family = quoteToLe.getQuoteToLeProductFamilies().stream()
					.filter(fam -> fam.getMstProductFamily().getName() != null).findFirst().orElse(null);
			if (family != null) {
				return family.getMstProductFamily().getName();
			}
		}
		return null;

	}
	
	/**
	 * 
	 * getOrderLeAttributes- This method is used for getting the orderLeAttributtes
	 * 
	 * @param orderToLe
	 * @param version
	 * @return
	 */
	protected Map<String, String> getQuoteLeAttributes(QuoteToLe quoteToLe) {
		Map<String, String> attributeMap = new HashMap<>();
		List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
			Optional<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
					.findById(quoteLeAttributeValue.getMstOmsAttribute().getId());
			if (mstOmsAttributes.isPresent())
				attributeMap.put(mstOmsAttributes.get().getName(), quoteLeAttributeValue.getAttributeValue());
		}
		return attributeMap;
	}

}
