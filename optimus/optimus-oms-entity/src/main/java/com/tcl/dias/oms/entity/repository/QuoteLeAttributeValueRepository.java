package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * This file contains the QuoteLeAttributeValueRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteLeAttributeValueRepository extends JpaRepository<QuoteLeAttributeValue, Integer> {

	List<QuoteLeAttributeValue> findByQuoteToLeAndMstOmsAttribute_Name(QuoteToLe quoteToLe, String attributeName);

	@Query(value = "SELECT b.name,a.attribute_value as value FROM quote_le_attribute_values a, mst_oms_attributes b where a.quote_to_le_id=:quoteToLeId and (a.attribute_value is not null or trim(a.attribute_value)!='' ) and a.mst_oms_attributes_id=b.id and b.name=:attrName", nativeQuery = true)
	List<Map<String, String>> findByQuoteToLeIdAndAttributeName(@Param(value = "quoteToLeId") Integer quoteToLeId,
			@Param(value = "attrName") String attributeName);

	List<QuoteLeAttributeValue> findByQuoteToLeAndMstOmsAttribute(QuoteToLe quoteToLe, MstOmsAttribute mstOmsAttribute);

	@Query(value = "select * from quote_le_attribute_values where mst_oms_attributes_id=:mstOmsAttributeId and quote_to_le_id=:quoteToLeId", nativeQuery = true)
	List<QuoteLeAttributeValue> findByQuoteToLe_IdAndMstOmsAttribute_Id(@Param(value="quoteToLeId") Integer quoteToLeId,@Param(value="mstOmsAttributeId") Integer mstOmsAttributeId);

	QuoteLeAttributeValue findByQuoteToLeAndAttributeValue(QuoteToLe quoteToLe, String attributeValue);

	List<QuoteLeAttributeValue> findByQuoteToLe(QuoteToLe quoteToLe);

	void deleteAllByQuoteToLe(QuoteToLe quoteToLe);

	List<QuoteLeAttributeValue> findByQuoteToLe_Id(Integer quoteLeId);
	
	List<QuoteLeAttributeValue> findByQuoteToLeAndMstOmsAttribute_NameIn(QuoteToLe quoteToLe, List<String> attributeName);

	@Query(value = "select * from quote_le_attribute_values qlav left join quote_to_le qtl on qlav.quote_to_le_id = qtl.id " +
			"left join quote q on qtl.quote_id = q.id " +
			"left join mst_oms_attributes moa on qlav.mst_oms_attributes_id = moa.id " +
			"where q.id=:quoteId and moa.name=:attributeName", nativeQuery = true)
	QuoteLeAttributeValue findByQuoteIDAndMstOmsAttributeName(Integer quoteId, String attributeName);

	@Query(value = "select * from quote_le_attribute_values qlav left join quote_to_le qtl on qlav.quote_to_le_id = qtl.id " +
			"left join quote q on qtl.quote_id = q.id " +
			"left join mst_oms_attributes moa on qlav.mst_oms_attributes_id = moa.id " +
			"where q.id=:quoteId and moa.name=:attributeName", nativeQuery = true)
	List<QuoteLeAttributeValue> findAttributesByQuoteIDAndMstOmsAttributeName(Integer quoteId, String attributeName);

	void deleteAllByQuoteToLeIn(Iterable<QuoteToLe> quoteToLe);
	
	QuoteLeAttributeValue findFirstByQuoteToLeAndMstOmsAttribute_NameOrderByIdDesc(QuoteToLe quoteToLe, String attributeName);
}
