package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * 
 * Repository class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MacdDetailRepository extends JpaRepository<MacdDetail, Integer> {

	public MacdDetail findByQuoteToLeId(Integer quoteToLeId);
	public MacdDetail findByTpsSfdcId(String sfdcId);
	public MacdDetail findByTpsServiceIdAndStageAndOrderCategoryNot(String tpsServiceId,String stage,String orderCategory);
	public MacdDetail findByTpsServiceIdAndOrderCategoryAndStage(String tpsServiceId, String orderCategory,String stage);
	public MacdDetail findByTpsServiceIdAndOrderCategoryInAndStage(String tpsServiceId, List<String> orderCategory,String stage);
	public List<MacdDetail> findByTpsServiceIdInAndOrderCategoryInAndStageNotInAndIsActiveNotIn(List<String> tpsServiceId, List<String> orderCategory,String stage,byte active);

	@Query(value = "select m.* from macd_detail m where m.quote_to_le_id=:quoteToLeId", nativeQuery = true)
	public List<MacdDetail> findMacdDetailByQuoteToLeId(@Param("quoteToLeId")  Integer quoteToLeId);

	public List<MacdDetail> findByTpsServiceIdAndStage(String tpsServiceId,String stage);
	
	public List<MacdDetail> findByTpsServiceIdAndStageIn(String tpsServiceId, List<String> stages);
	
	public MacdDetail findByTpsServiceIdAndQuoteToLeId(String tpsServiceId, Integer quoteToLeId);
	
	public MacdDetail findByTpsServiceIdAndStageAndOrderType(String tpsServiceId,String stage, String orderType);
}
