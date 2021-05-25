package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 
 * This file contains the QuoteProductComponentsAttributeValueRepository.java
 * class. Repository class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface QuoteProductComponentsAttributeValueRepository
		extends JpaRepository<QuoteProductComponentsAttributeValue, Integer> {

	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponent(QuoteProductComponent quoteProductComponent);

	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
			Integer componentId, String name);
	
	QuoteProductComponentsAttributeValue findFirstByQuoteProductComponent_IdAndProductAttributeMaster_NameOrderByIdDesc(
			Integer componentId, String name);

	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponentAndProductAttributeMaster(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster);

	@Query(value = "select * from quote_product_components_attribute_values where quote_product_component_id in :quoteProductComponentId",nativeQuery=true)
	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponentId(List<Integer> quoteProductComponentId);

	
	/**
	 * Find Quote product component id and attributes by product component id
	 *
	 * @param prodCompIds
	 * @param prodAttrMasterIds
	 * @return {@link List<QuoteProductComponentsAttributeValue>}
	 */
	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponent_IdInAndProductAttributeMaster_IdIn(
			List<Integer> prodCompIds, List<Integer> prodAttrMasterIds);

	/**
	 * Find Quote product component id and attributes by component id
	 *
	 * @param componentId
	 * @param name
	 * @return {@link List<QuoteProductComponentsAttributeValue>}
	 */
	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponent_IdInAndProductAttributeMaster_Name(
			List<Integer> componentId, String name);


	/**
	 * Find Quote by product component id
	 *
	 * @param id
	 * @return {@link List<QuoteProductComponentsAttributeValue>}
	 */
	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponent_Id(Integer id);

	/**
	 * This method is used to delete the quote attributes by id list.
	 * @param List<quoteProductComponentsAttributeValueId>.
	 * @return Nothing.
	 */
	void deleteAllByIdIn(List<Integer> quoteProductComponentsAttributeValueId);

	void deleteAllByQuoteProductComponentIdIn(Set<Integer> quoteProductComponentIds);

	/**
	 * Find Global Outbound Column Name
	 *
	 * @param quoteCode
	 * @return
	 */
	@Query(value = "SELECT attr.attribute_values FROM quote_product_components_attribute_values attr "
			+ "left join quote_product_component pc on attr.quote_product_component_id=pc.id "
			+ "left join quote o on o.id=pc.reference_id "
			+ "left join product_attribute_master pmaster on pmaster.id=attr.attribute_id "
			+ "where o.quote_code = (:quoteCode) and pmaster.name = 'Global Outbound Rate Column' and pc.type='GSIP.COMMON' and " +
			"attr.attribute_values != '' group by attr.attribute_values",nativeQuery=true)
	String findByAttributeName(@Param("quoteCode") String quoteCode);


	@Query(value = "select * from quote_product_components_attribute_values where quote_product_component_id in (select id from quote_product_component where reference_id = :siteId and product_component_id = (select id from mst_product_component where name = :productComponent))", nativeQuery = true)
    List<QuoteProductComponentsAttributeValue> findQuoteProductComponentAttributesByProductComponentIdAndSiteId(String productComponent, Integer siteId);

	/**
	 * Get enrichment properties
	 *
	 * @param siteId
	 * @return
	 */
	@Query(value = "select * from quote_product_components_attribute_values qpa " +
			"left join  quote_product_component qpc on qpa.quote_product_component_id=qpc.id\n" +
			"left join  product_attribute_master pam on qpa.attribute_id = pam.id\n" +
			"left join mst_product_family mpf on qpc.product_family_id = mpf.id\n" +
			"where pam.name in (:attributes)\n" +
			"and qpc.reference_id = :siteId and mpf.name = :productName", nativeQuery = true)
	List<QuoteProductComponentsAttributeValue> findOrderEnrichmentAttributesByAttributesAndSiteId(@Param("attributes") List<String> attributes,@Param("siteId") Integer siteId,@Param("productName") String productName);
	
	@Query(value = "select distinct attribute_values from quote_product_components_attribute_values where "
			+ "quote_product_component_id in(select id from quote_product_component where reference_id in"
			+ "(select id from quote_izosdwan_sites where product_solutions_id=:productSolutionId) and "
			+ "reference_name=:referenceName) and attribute_id in(select id from product_attribute_master"
			+ " where name=:name) and attribute_values is not null", nativeQuery = true)
	List<String> listTheDistinctAttributeValues(Integer productSolutionId, String name, String referenceName);
	
	
	@Query(value="select oa.attribute_values from quote_product_components_attribute_values oa join product_attribute_master pam"
			+ " on pam.id=oa.attribute_id where oa.quote_product_component_id in(select id from quote_product_component where"
			+ " reference_id=:siteId and reference_name=:referenceName and type=:type) and pam.name=:attributeName",nativeQuery = true)
	List<String> getAttributeValueByAttributeName(Integer siteId,String attributeName,String type,String referenceName);
	
	List<QuoteProductComponentsAttributeValue> findByQuoteProductComponentAndProductAttributeMaster_Name(QuoteProductComponent orderProductComponent, String name);
	
	@Query(value="select qpv.* from quote_product_components_attribute_values qpv \r\n" + 
			"left join  quote_product_component qpc on qpv.quote_product_component_id = qpc.id  \r\n" + 
			"left join quote_ill_sites qls on qls.id = qpc.reference_id \r\n" + 
			"where qls.id =:siteId \r\n" + 
			"and qpc.product_component_id in (select id from mst_product_component where name =:componentName and status = 1) \r\n" + 
			"and qpv.attribute_id in (select id from product_attribute_master where name =:attributeMasterName and status = 1) ",nativeQuery = true)
	List<QuoteProductComponentsAttributeValue> findQuoteAttributeValuesBySiteId(@Param("siteId") Integer siteId, @Param("attributeMasterName") String attributeMasterName, @Param("componentName") String componentName);

}
