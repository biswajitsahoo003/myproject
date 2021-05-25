package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.BomRentalSalesCodeAssoc;

/**
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface BomRentalSalesCodeAssocRepository extends JpaRepository<BomRentalSalesCodeAssoc, Integer> {

	List<BomRentalSalesCodeAssoc> findByBomName(String bomName);
}
