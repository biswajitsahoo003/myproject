package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstVmiTransaction;

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
public interface MstVmiTransactionRepository extends JpaRepository<MstVmiTransaction, Integer> {

}
