package com.tcl.dias.oms.entity.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.BulkFeasibilityInfo;

@Repository
public interface BulkFeasibilityInfoRepository extends JpaRepository<BulkFeasibilityInfo, Integer> {
	
	BulkFeasibilityInfo findByJobId(Integer jobId);

	Page<BulkFeasibilityInfo> findAllByOrderByCreatedTimeDesc(Pageable pageable);

}
