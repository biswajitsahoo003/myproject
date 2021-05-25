package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.PreMfResponse;

@Repository
public interface PreMfResponseRepository extends JpaRepository<PreMfResponse, Integer> {

	List<PreMfResponse> findBySiteId(Integer siteId);

	List<PreMfResponse> findByQuoteId(Integer quoteId);

	List<PreMfResponse> findBytaskId(Integer tasID);

	List<PreMfResponse> findBytaskIdAndFeasibilityType(Integer TaskID, String feasibilityType);

	List<PreMfResponse> findBySiteIdAndTaskId(Integer siteId, Integer TaskID);

}
