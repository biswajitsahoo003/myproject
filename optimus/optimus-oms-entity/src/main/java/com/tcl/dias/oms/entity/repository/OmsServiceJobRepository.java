package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OmsServiceJob;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OmsServiceJobRepository extends JpaRepository<OmsServiceJob, Integer> {

	@Query(value = "SELECT * FROM oms_service_jobs where status =:serviceStatus and retry_count < 3 and source=:source order by id desc", nativeQuery = true)
	List<OmsServiceJob> findByStatusAndSource(@Param("serviceStatus") String serviceStatus,
			@Param("source") String source);

}
