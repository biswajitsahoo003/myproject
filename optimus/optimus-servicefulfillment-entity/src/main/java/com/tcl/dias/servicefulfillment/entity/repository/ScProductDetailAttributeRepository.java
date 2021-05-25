package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttribute;

@Repository
public interface ScProductDetailAttributeRepository extends JpaRepository<ScProductDetailAttribute,Integer>{
	
	ScProductDetailAttribute findByScProductDetail_idAndAttributeNameAndCategory(Integer serviceId,
			String attributeName, String category);
	
	List<ScProductDetailAttribute> findByScProductDetail_idAndCategory(Integer serviceId,String category);
	
	ScProductDetailAttribute findFirstByScProductDetail_idAndAttributeNameOrderByIdDesc(Integer scProductDetailId, String attributeName);

	List<ScProductDetailAttribute> findByScProductDetail_id(Integer scProductDetailId);

}
