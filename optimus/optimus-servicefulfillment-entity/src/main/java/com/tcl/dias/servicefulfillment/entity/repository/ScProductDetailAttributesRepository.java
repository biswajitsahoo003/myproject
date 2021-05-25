package com.tcl.dias.servicefulfillment.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;

/**
 * 
 * Repository. This file holds repository methods of ScProductDetailAttributesRepository
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface ScProductDetailAttributesRepository extends JpaRepository<ScProductDetailAttributes, Integer> {

	ScProductDetailAttributes findFirstByScProductDetail_idAndAttributeNameOrderByIdDesc(Integer scProductDetailId, String attributeName);
}
