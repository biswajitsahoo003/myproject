package com.tcl.dias.productcatelog.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ProductSlaCosSpec;


/**
 * Repository class for ProductSlaCosSpec entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ProductSlaCosSpecRepository extends JpaRepository<ProductSlaCosSpec, Integer> {
	
	Optional<ProductSlaCosSpec> findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Integer pdtCatalogId, Integer slaMetricId, String cosSchemaNm);

}
