package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstVmi;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstVmiRepository extends JpaRepository<MstVmi, Integer> {

	List<MstVmi> findByRentalMaterialCode(String rentalMaterialCode);

	List<MstVmi> findBySaleMaterialCode(String salesMaterialCode);

}
