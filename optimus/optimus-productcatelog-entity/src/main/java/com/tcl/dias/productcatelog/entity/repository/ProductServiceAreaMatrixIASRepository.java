package com.tcl.dias.productcatelog.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ProductServiceAreaMatrixIAS;

/**
 * 
 * @author Rakesh S
 *
 */
@Repository
public interface ProductServiceAreaMatrixIASRepository extends JpaRepository<ProductServiceAreaMatrixIAS, Integer> {

	Optional<ProductServiceAreaMatrixIAS> findByLocationId(int cityId);

}
