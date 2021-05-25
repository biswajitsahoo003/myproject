package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.Product;

/**
 * 
 * JPA repository for DB operations related to products
 * 
 * @author Manojkumar R
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	public List<Product> findByProductFamily_IdAndIsActive(Integer prodFamilyId, String isActive);

	public Optional<Product> findById(Integer productid);

	public Optional<Product> findByIdAndIsActiveIsNullOrIdAndIsActive(Integer productid,Integer productid2, String isActive);

}