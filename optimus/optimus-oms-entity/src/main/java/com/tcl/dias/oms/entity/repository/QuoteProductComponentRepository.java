package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This file contains the DraftQuotesRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteProductComponentRepository extends JpaRepository<QuoteProductComponent, Integer> {

	Optional<QuoteProductComponent> findById(Integer id);

	List<QuoteProductComponent> findByReferenceIdInAndReferenceName(List<Integer> ids,String referenceName);

	List<QuoteProductComponent> findByReferenceIdAndReferenceName(Integer id,String referenceName);
	
	List<QuoteProductComponent> findByReferenceIdAndReferenceNameAndMstProductComponent(Integer id,String referenceName, MstProductComponent  mstProductComponent);

	List<QuoteProductComponent> findByReferenceIdAndType(Integer id, String type);
	
	//List<QuoteProductComponent> findByReferenceIdAndMstProductFamily_NameAndReferenceName(Integer id, String productName,String referenceName);

	List<QuoteProductComponent> findByReferenceIdAndMstProductFamily_Name(Integer id, String productName);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductFamily(Integer illSItId,MstProductFamily mstProductFamily);

	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent(Integer id, MstProductComponent  mstProductComponent);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponentAndMstProductFamily(Integer id, MstProductComponent  mstProductComponent,MstProductFamily mstProductFamily);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponentAndMstProductFamilyAndReferenceName(Integer id, MstProductComponent  mstProductComponent,MstProductFamily mstProductFamily,String referenceName);
	
	Optional<QuoteProductComponent> findByReferenceIdAndMstProductComponentAndType(Integer id, MstProductComponent  mstProductComponent,String type);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent_NameAndReferenceName(Integer illSItId,
			 String name,String referenceName);

	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent_NameAndReferenceNameAndType(Integer illSItId, String name,String referenceName,String type);

	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent_Name(Integer illSItId,
			 String name);
	
	List<QuoteProductComponent> findByReferenceIdAndReferenceNameAndMstProductComponent_Name(Integer illSItId,String referenceName,
			 String name);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent_NameAndType(Integer illSItId
			, String name,String type);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent_NameAndTypeIn(Integer illSItId
			, String name,List<String> type);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(Integer id, MstProductComponent  mstProductComponent, MstProductFamily mstProductFamily, String type ); 

	void deleteAllByIdIn(List<Integer> quoteProductComponentIds);
	
	QuoteProductComponent findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(Integer id , String compName, String productName); //site,iascommon
	
	QuoteProductComponent findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_NameAndType(Integer id , String compName, String productName,
			String type);

	List<QuoteProductComponent> findByReferenceIdAndMstProductFamilyAndType(Integer id, MstProductFamily prodFamily,
			String type);
	
	List<QuoteProductComponent> findByReferenceIdInAndMstProductComponent_NameAndTypeIn(List<Integer> illSItId
			, String name,List<String> type);
	
	List<QuoteProductComponent> findByReferenceId(Integer illSiteId);

	List<QuoteProductComponent> findByReferenceIdAndMstProductComponentIn(Integer quoteCloudId,
			List<MstProductComponent> mstProductComponents);
	
	List<QuoteProductComponent> findByReferenceIdAndReferenceNameAndType(Integer illSiteId,
			String referenceName, String type);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(Integer illSItId
			, String name,String type,String refName);
	
	List<QuoteProductComponent> findByReferenceIdAndTypeAndReferenceName(Integer illSItId,String type,String refName);

	List<QuoteProductComponent> findByReferenceIdAndAndMstProductFamilyAndReferenceName(Integer id, MstProductFamily mstProductFamily,String referenceName);

	List<QuoteProductComponent> findByReferenceIdAndMstProductFamily_NameAndReferenceName(Integer quoteVrfId,
			String productName, String string);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductFamilyAndReferenceName(Integer quoteVrfId,
			MstProductFamily mstProductFamily, String string);
	
	List<QuoteProductComponent> findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(Integer id, 
			MstProductComponent  mstProductComponent, MstProductFamily mstProductFamily, String type,String refName); 

	List<QuoteProductComponent> findByReferenceIdInAndReferenceNameInAndMstProductFamily(List<Integer> ids, List<String> referenceName, MstProductFamily mstProductFamily);

	List<QuoteProductComponent> findByReferenceIdInAndMstProductComponentAndMstProductFamilyAndTypeIn(
			List<Integer> referenceId, MstProductComponent prodComponent, MstProductFamily mstProductFamily,
			List<String> type);
	
	@Query(value="select * from  quote_product_component qpc  \r\n" + 
			"left join quote_ill_sites qls on qls.id = qpc.reference_id \r\n" + 
			"left join product_solutions ps on qls.product_solutions_id = ps.id \r\n" + 
			"left join quote_to_le_product_family qtlpf on ps.quote_le_product_family_id = qtlpf.id \r\n" + 
			"left join quote_to_le qtl on qtlpf.quote_to_le_id = qtl.id \r\n" + 
			"left join quote q on qtl.quote_id = q.id \r\n" + 
			"where q.quote_code = :quoteCode\r\n" + 
			"and qpc.type in (:type) \r\n" + 
			"and qpc.product_component_id in (select id from mst_product_component where name =:componentName and status = 1)",nativeQuery = true)
	List<QuoteProductComponent> findQuoteAttributeValuesByQuoteCode(@Param("quoteCode") String quoteCode, @Param("type") List<String> type, @Param("componentName") String componentName);

	@Query(value="select qpc.* from quote_cloud qc join quote_product_component qpc on qc.id=qpc.reference_id and qc.cloud_code =:cloudCode and qpc.product_component_id =:componentId and qpc.product_family_id =:productFamilyId",nativeQuery=true)
	QuoteProductComponent findByParentCloudCodeAndMstProductComponentIdAndMstProductFamilyId(String cloudCode , Integer componentId, Integer productFamilyId);

	@Query(value = "select qpc.* from quote_cloud qc join quote_product_component qpc on qc.id = qpc.reference_id and qc.cloud_code =:cloudCode and qpc.product_component_id =:componentId", nativeQuery = true)
	QuoteProductComponent findByCloudCodeAndMstProductComponent(String cloudCode, Integer componentId);
	
	@Query(value = "select qv.attribute_values from quote_product_component qpc join quote_product_components_attribute_values qv on qpc.id=qv.quote_product_component_id and qpc.product_component_id=:prodCompId and qpc.reference_id=:quoteCloudId where qv.attribute_id=:attributeId", nativeQuery = true)
	List<String> findByQuoteCloudIdAndProdCompIdAndAttrId(Integer quoteCloudId,Integer prodCompId,Integer attributeId);
}
