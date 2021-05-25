package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;

/**
 * 
 * This file contains the ScComponent.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScComponentAttributesRepository extends JpaRepository<ScComponentAttribute, Integer> {

	ScComponentAttribute findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(Integer serviceDetailsId, String attributeName, String componentName, String siteType);
	
	ScComponentAttribute findFirstByScServiceDetailIdAndAttributeNameAndScComponentOrderByIdDesc(Integer serviceDetailsId, String attributeName, ScComponent scComponent);

    Optional<ScComponentAttribute> findFirstByAttributeValueAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(String suIp, String attributeName, String componentName, String siteType);

	Collection<ScComponentAttribute> findAllByAttributeValueInAndAttributeNameInAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(List<String> suipList, List<String> AttributeNames, String componentName, String siteType);
	
	List<ScComponentAttribute> findByScComponent(ScComponent scComponent);
	
	List<ScComponentAttribute> findByScComponent_id(Integer scComponentId);

	List<ScComponentAttribute> findByScComponentAndAttributeName(ScComponent scComponent, String attributeName);
	
	List<ScComponentAttribute> findByScComponentAndAttributeNameIn(ScComponent scComponent, List<String> attributeNames);
	
	List<ScComponentAttribute> findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType(Integer serviceId, List<String> attributes, String componentName, String siteType);

	@Query(value = "SELECT a.* FROM sc_component_attributes a, sc_component s where s.sc_service_detail_id=:serviceId and s.component_name=:componentName and s.site_type=:siteType and s.id=a.sc_component_id order by a.id desc", nativeQuery = true)
	List<ScComponentAttribute> findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
				Integer serviceId, String componentName, String siteType);
	
	@Query(value = "SELECT a.* FROM sc_component_attributes a, sc_component s where s.sc_service_detail_id=:serviceId and s.component_name=:componentName and s.site_type=:siteType and s.id=a.sc_component_id AND attribute_name IN :attributeName order by a.id desc", nativeQuery = true)
	List<ScComponentAttribute> findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
				Integer serviceId, String componentName, String siteType, List<String> attributeName);

	List<ScComponentAttribute> findByScServiceDetailIdAndScComponent_siteType(Integer serviceId, String siteType);

	@Query(value = "select * from sc_component_attributes where sc_service_detail_id=:serviceId and sc_component_id=:componentId", nativeQuery = true)
	List<ScComponentAttribute> findByScServiceDetailIdAndScComponentId(Integer serviceId, Integer componentId);
	
	List<ScComponentAttribute> findByAttributeNameAndScComponent_id(String attributeName, Integer componentId);
	
	ScComponentAttribute findFirstByScServiceDetailIdAndScComponent_idAndAttributeName(Integer serviceId,Integer componentId,String attributeName);
	
	ScComponentAttribute findFirstByScComponent_idAndAttributeName(Integer scComponentId,String attributeName);
	
	List<ScComponentAttribute> findByScComponent_idAndAttributeNameIn(Integer scComponentId,List<String> attributeNameList);
	
	List<ScComponentAttribute> findByScServiceDetailIdAndScComponent_idInAndAttributeName(Integer serviceId,List<Integer> scComponentIds,String attributeName);
	
	List<ScComponentAttribute> findByScServiceDetailIdAndAttributeName(Integer serviceId, String attributeName);
	
	List<ScComponentAttribute> findByScComponentAndAttributeNameInAndScServiceDetailId(ScComponent scComponent, List<String> attributes,Integer serviceDetailsId);

	@Query(value="SELECT ca.* FROM sc_component c, sc_component_attributes ca WHERE c.id=ca.sc_component_id AND c.sc_service_detail_id=:parentId AND ca.attribute_name=:attributeName", nativeQuery=true)
	Optional<ScComponentAttribute> findByServiceDetailAndAttributeName(Integer parentId, String attributeName);
}
