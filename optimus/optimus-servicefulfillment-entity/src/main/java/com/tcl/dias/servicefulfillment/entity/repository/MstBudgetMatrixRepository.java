package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstBudgetMatrix;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface MstBudgetMatrixRepository extends JpaRepository<MstBudgetMatrix, Integer> {

	List<MstBudgetMatrix> findByProductNameAndCpeTypeAndTypeOfExpenses(String productName, String cpeType,
			String typeOfExpenses);
	
	List<MstBudgetMatrix> findByProductNameAndCpeType(String productName, String cpeType);

}
