package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderLrJob;

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
public interface OrderLrJobRepository extends JpaRepository<OrderLrJob, Integer> {

	List<OrderLrJob> findByStage(String stage);

	List<OrderLrJob> findByStageAndProductName(String stage, String productName);

	OrderLrJob findByJobId(String jobId);
}
