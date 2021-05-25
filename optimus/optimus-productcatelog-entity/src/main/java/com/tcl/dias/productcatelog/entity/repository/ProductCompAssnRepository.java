package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ProductComponentAssoc;
/**
 * 
 * @author Manojkumar R
 *
 */
@Repository
public interface ProductCompAssnRepository extends JpaRepository<ProductComponentAssoc, Integer> {

	List<ProductComponentAssoc> findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Integer prodId,Integer prodId2,String isAcive);
	
	List<ProductComponentAssoc> findByProduct_Id(Integer prodId);
}
