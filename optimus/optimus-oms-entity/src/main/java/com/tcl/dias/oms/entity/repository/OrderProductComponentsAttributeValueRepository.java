package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This file contains the OrderProductComponentsAttributeValueRepository.java
 * class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderProductComponentsAttributeValueRepository
		extends JpaRepository<OrderProductComponentsAttributeValue, Integer> {

	List<OrderProductComponentsAttributeValue> findByOrderProductComponent_Id(Integer id);

	List<OrderProductComponentsAttributeValue> findByOrderProductComponentAndProductAttributeMaster(
			OrderProductComponent orderProductComponent, ProductAttributeMaster productAttributeMaster);
	
	List<OrderProductComponentsAttributeValue> findByOrderProductComponent(OrderProductComponent orderProductComponent);

	List<OrderProductComponentsAttributeValue> findByOrderProductComponentAndProductAttributeMaster_Name(
			OrderProductComponent orderProductComponent, String name);

	List<OrderProductComponentsAttributeValue> findAllByOrderProductComponentAndProductAttributeMasterIn(
			OrderProductComponent orderProductComponent, List<ProductAttributeMaster> productAttributeMasters);
	
	OrderProductComponentsAttributeValue findFirstByOrderProductComponent_IdAndProductAttributeMaster_NameOrderByIdDesc(
			Integer componentId, String name);

	/**
	 * Find all by order component and attribute master
	 *
	 * @param orderProductComponent
	 * @param toBeDeletedIds
	 * @return {@link List<OrderProductComponentsAttributeValue>}
	 */
	List<OrderProductComponentsAttributeValue> findAllByOrderProductComponentAndProductAttributeMaster_Id(
			OrderProductComponent orderProductComponent, Set<Integer> toBeDeletedIds);

	/**
	 * Find Global Outbound Column Name
	 *
	 * @param orderCode
	 * @return
	 */
	@Query(value = "SELECT attr.attribute_values FROM quote_product_components_attribute_values attr "
			+ "left join quote_product_component pc on attr.quote_product_component_id=pc.id "
			+ "left join quote o on o.id=pc.reference_id "
			+ "left join product_attribute_master pmaster on pmaster.id=attr.attribute_id "
			+ "where o.quote_code = (:orderCode) and pmaster.name = 'Global Outbound Rate Column' and pc.type='GSIP.COMMON' and attr.attribute_values != '' order by 1", nativeQuery = true)
	String findByAttributeName(@Param("orderCode") String orderCode);

	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.type = :type", nativeQuery = true)
	List<Map<String, String>> findByRefid(@Param("refId") String refId, @Param("type") String type);
	
	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.reference_name=:refName and opc.type = :type", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefName(@Param("refId") String refId, @Param("type") String type,@Param("refName") String refName);
	
	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.type is null", nativeQuery = true)
	List<Map<String, String>> findByRefid(@Param("refId") String refId);
	
	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.reference_name=:refName", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefName(@Param("refId") String refId,@Param("refName") String refName);
	
	@Query(value = "SELECT opc.id AS id,mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.reference_name=:refName and opc.type is null", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameForIpc(@Param("refId") String refId,@Param("refName") String refName);
	
	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.reference_name=:refName", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameforOdr(@Param("refId") String refId,@Param("refName") String refName);
	
	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.reference_name=:refName and opc.type is not null", nativeQuery = true)
    List<Map<String, String>> findByRefidAndRefNameForGsc(@Param("refId") String refId,@Param("refName") String refName);
	
	@Query(value = "select mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type from order_product_components_attribute_values opa "
			+ "join order_product_component opc on opc.id=opa.order_product_component_id join product_attribute_master pam on pam.id=opa.attribute_id join "
			+ "mst_product_component mpc on mpc.id=opc.product_component_id where opc.reference_id=:refId and opc.reference_name=:refName "
			+ "and mpc.name in(:components) and pam.name not in(:attributes)", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameUnderlay(@Param("refId") String refId,
			@Param("refName") String refName, @Param("components") List<String> components,@Param("attributes") List<String> attributes);

	@Query(value = "select mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type from order_product_components_attribute_values opa "
			+ "join order_product_component opc on opc.id=opa.order_product_component_id join product_attribute_master pam on pam.id=opa.attribute_id join "
			+ "mst_product_component mpc on mpc.id=opc.product_component_id where opc.reference_id=:refId and opc.reference_name=:refName and and opc.type = :type"
			+ "and mpc.name in(:components) and pam.name not in(:attributes)", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameUnderlay(@Param("refId") String refId, @Param("type") String type,
			@Param("refName") String refName, @Param("components") List<String> components,@Param("attributes") List<String> attributes);

	@Query(value = "select mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type from order_product_components_attribute_values opa "
			+ "join order_product_component opc on opc.id=opa.order_product_component_id join product_attribute_master pam on pam.id=opa.attribute_id join "
			+ "mst_product_component mpc on mpc.id=opc.product_component_id where opc.reference_id=:refId and opc.reference_name=:refName "
			+ "and mpc.name in(:components) and pam.name in(:attributes)", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameOverlay(@Param("refId") String refId,
			@Param("refName") String refName, @Param("components") List<String> components,@Param("attributes") List<String> attributes);

	@Query(value = "select mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type from order_product_components_attribute_values opa "
			+ "join order_product_component opc on opc.id=opa.order_product_component_id join product_attribute_master pam on pam.id=opa.attribute_id join "
			+ "mst_product_component mpc on mpc.id=opc.product_component_id where opc.reference_id=:refId and opc.reference_name=:refName and and opc.type = :type"
			+ "and mpc.name in(:components) and pam.name in(:attributes)", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameOverlay(@Param("refId") String refId, @Param("type") String type,
			@Param("refName") String refName, @Param("components") List<String> components,@Param("attributes") List<String> attributes);


/*
    @Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.reference_name=:refName and and opc.type is not null", nativeQuery = true)
    List<Map<String, String>> findByRefidAndRefNameForGsc(@Param("refId") String refId,@Param("refName") String refName);
*/

	@Query(value = "SELECT pmaster.name as attributeName, opcav.attribute_values as attributeValue "
			+"FROM order_product_components_attribute_values opcav "
			+"INNER JOIN order_product_component opc ON opcav.order_product_component_id = opc.id "
			+"LEFT JOIN product_attribute_master pmaster on opcav.attribute_id=pmaster.id where  opc.reference_id=:siteId", nativeQuery = true)
	List<Map<String, String>> findAttributeValueBySiteIdAsReferenceId(@Param("siteId") String siteId);

	List<OrderProductComponentsAttributeValue> findByOrderProductComponent_IdAndProductAttributeMaster_Name(
			Integer componentId, String siteProperties);
	
	@Query(value="select oa.attribute_values from order_product_components_attribute_values oa join product_attribute_master pam"
			+ " on pam.id=oa.attribute_id where oa.order_product_component_id in(select id from order_product_component where"
			+ " reference_id=:siteId and reference_name=:referenceName and type=:type) and pam.name=:attributeName",nativeQuery = true)
	List<String> getAttributeValueByAttributeName(Integer siteId,String attributeName,String type,String referenceName);

	OrderProductComponentsAttributeValue findFirstByOrderProductComponentAndProductAttributeMasterOrderByIdDesc(
			OrderProductComponent orderProductComponent, ProductAttributeMaster productAttributeMaster);
	@Query(value = "SELECT mpc.name AS category,pam.name AS attrName,opa.attribute_values AS attrValue,opc.type AS type FROM order_product_components_attribute_values opa,order_product_component opc,product_attribute_master pam,mst_product_component mpc WHERE  opa.attribute_id = pam.Id AND opa.order_product_component_id = opc.id AND opc.product_component_id = mpc.id and opc.reference_id=:refId and opc.type = :type", nativeQuery = true)
	List<Map<String, String>> findByRefidAndRefNameRenewals(@Param("refId") String refId, @Param("type") String type);
}
