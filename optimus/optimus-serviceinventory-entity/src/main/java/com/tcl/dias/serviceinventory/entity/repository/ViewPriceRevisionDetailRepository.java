package com.tcl.dias.serviceinventory.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.ViewPriceRevisionDetail;

/**
 * ViewPriceRevisionDetailRepository interface
 * 
 *
 * @author Veera B
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ViewPriceRevisionDetailRepository extends JpaRepository<ViewPriceRevisionDetail, Integer> {
	
	Optional<ViewPriceRevisionDetail> findByServiceId(String serviceId);

}
