package com.tcl.dias.networkaugment.entity.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.ScComponent;
import com.tcl.dias.networkaugment.entity.entities.ScComponentAttribute;

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

    Optional<ScComponentAttribute> findFirstByAttributeValueAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(String suIp, String attributeName, String componentName, String siteType);

	Collection<ScComponentAttribute> findAllByAttributeValueInAndAttributeNameInAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(List<String> suipList, List<String> AttributeNames, String componentName, String siteType);
	
	List<ScComponentAttribute> findByScComponent(ScComponent scComponent);
	
	
	List<ScComponentAttribute> findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType(Integer serviceId, List<String> attributes, String componentName, String siteType);

		
	@Query(value = "SELECT a.* FROM sc_component_attributes a, sc_component s where s.sc_service_detail_id=:serviceId and s.component_name=:componentName and s.site_type=:siteType and s.id=a.sc_component_id order by a.id desc", nativeQuery = true)
	List<ScComponentAttribute> findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
				Integer serviceId, String componentName, String siteType);

	List<ScComponentAttribute> findByScServiceDetailIdAndScComponent_siteType(Integer serviceId, String siteType);
}
