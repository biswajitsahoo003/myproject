package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIComponentAttribute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 *
 * This file contains the SIComponentAttributeRepository.java class for persistence in SIComponentAttribute
 *
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SIComponentAttributeRepository extends JpaRepository<SIComponentAttribute, Integer> {
    SIComponentAttribute findFirstBySiServiceDetailIdAndAttributeName(Integer id, String attrName);
    
    SIComponentAttribute findFirstBySiServiceDetailIdAndAttributeNameOrderByIdDesc(Integer id, String attrName);
    
    @Query(value = "select * from si_component_attributes where si_component_id =:siComponentId and attribute_name =:attribute_name order by id desc limit 1",nativeQuery=true)
    SIComponentAttribute getAttributeDetailByComponentIdAndName(@Param("siComponentId")Integer siComponentId, @Param("attribute_name") String attribute_name);
    
    List<SIComponentAttribute> findFirstBySiServiceDetailId(Integer id);
}
