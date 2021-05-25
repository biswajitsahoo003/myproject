package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.MstCustomerSegment;
import com.tcl.dias.customer.entity.entities.MstDopMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * This file contains the MstCustomerSegmentRepository.java class.
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstDopMatrixRepository extends JpaRepository<MstDopMatrix, Integer> {

        MstDopMatrix findByLevelOneName(String levelOneName);

        MstDopMatrix findByLevelOneNameAndCustomerSegment(String levelOneName, MstCustomerSegment customerSegment);	
}
