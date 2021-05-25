package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.l2oworkflow.entity.entities.MfNplResponseDetail;

public interface MfNplResponseDetailRepository extends JpaRepository<MfNplResponseDetail, Integer> {

	Optional<MfNplResponseDetail> findById(Integer id);
	List<MfNplResponseDetail> findByQuoteId(Integer quoteId);

}
