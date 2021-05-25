package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.l2oworkflow.entity.entities.SupplierIOR;

/**
 * 
 * This file contains the SupplierIORRepository.java class.
 * 
 *
 * @author Ninad.Pingale
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SupplierIORRepository extends JpaRepository<SupplierIOR, Integer> {

	@Query(value = "SELECT * from supplier_ior where supplier_name LIKE %:searchValue%", nativeQuery = true)
	List<SupplierIOR> findBySupplierName(@Param("searchValue") String searchValue);

	@Query(value = "SELECT * from supplier_ior where ior_id LIKE %:searchValue%", nativeQuery = true)
	List<SupplierIOR> findByIorId(@Param("searchValue") String searchValue);

	SupplierIOR findAllByNmiLocation(String nmiLocation);
	
	@Query(value = "SELECT * from supplier_ior " + " where nmi_location LIKE %:searchValue%", nativeQuery = true)
	List<SupplierIOR> findByNmiLocation(@Param("searchValue") String searchValue);

}
