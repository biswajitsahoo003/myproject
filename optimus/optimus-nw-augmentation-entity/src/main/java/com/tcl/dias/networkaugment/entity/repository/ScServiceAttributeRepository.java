package com.tcl.dias.networkaugment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.ScServiceAttribute;

/**
 * 
 * This file contains the ScServiceAttributeRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScServiceAttributeRepository extends JpaRepository<ScServiceAttribute, Integer> {

	@Query(value="SELECT * FROM sc_service_attributes where sc_service_detail_id=:serviceId and is_active=:isActive and (category is null or category not in (:notInCategories))",nativeQuery=true)
	public List<ScServiceAttribute> findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(@Param("serviceId") 
			Integer serviceId,@Param("isActive")  String isActive,@Param("notInCategories")  List<String> notInCategories);

	public List<ScServiceAttribute> findByScServiceDetail_idAndIsActiveAndAttributeValueIsNotNull(Integer serviceId, String isActive);

	public List<ScServiceAttribute> findByScServiceDetail_idAndIsActiveAndIsAdditionalParam(Integer serviceId,
			String isActive, String isAdditionalParam);

	@Query(value = "SELECT * FROM sc_service_attributes where sc_service_detail_id=:serviceId and category=:category and attribute_name=:attributeName and is_additional_param=:isAdditionalParam and is_active=:isActive", nativeQuery = true)
	public List<ScServiceAttribute> findByScServiceDetail_idAndIsActiveAndIsAdditionalParamAndAttributeValueAndCategory(
			@Param("serviceId") Integer serviceId, @Param("isActive") String isActive,
			@Param("isAdditionalParam") String isAdditionalParam, @Param("attributeName") String attributeName,
			@Param("category") String category);

	@Query(value="SELECT * FROM sc_service_attributes where sc_service_detail_id=:serviceId and is_active=:isActive and  category=:category",nativeQuery=true)
	public List<ScServiceAttribute> findByScServiceDetail_idAndIsActiveAndCategory(@Param("serviceId") Integer serviceId,@Param("isActive")  String isActive,
			@Param("category") String category);

	public ScServiceAttribute findByScServiceDetail_idAndAttributeNameAndCategory(Integer serviceId,
			String attributeName, String category);
	
	public ScServiceAttribute findFirstByScServiceDetail_idAndAttributeNameAndCategory(Integer serviceId,
			String attributeName, String category);

	public ScServiceAttribute findByScServiceDetail_idAndAttributeName(Integer serviceId, String attributeName);
	
	public List<ScServiceAttribute> findByScServiceDetail_idAndAttributeNameIn(Integer serviceId, List<String> attributeName);

	
	public List<ScServiceAttribute> findByScServiceDetail_idAndAttributeNameInAndCategory(Integer serviceId, List<String> attributes,String category);
	
	public ScServiceAttribute findFirstByScServiceDetail_idAndAttributeName(Integer serviceId, String attributeName);

	List<ScServiceAttribute> findByScServiceDetail_id(Integer id);

	void deleteByScServiceDetail_idAndAttributeName(Integer serviceId, String key);

	public ScServiceAttribute findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(Integer serviceId,String attributeName);
}
