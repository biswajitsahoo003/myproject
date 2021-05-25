package com.tcl.dias.serviceactivation.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.RfTestResult;

@Repository
public interface RfTestResultRepository extends JpaRepository<RfTestResult,Integer> {
    Optional<RfTestResult> findByUniqueId(String uniqueId);

    Optional<RfTestResult> findByCircuitId(String circuitId);

    Optional<RfTestResult> findFirstByCircuitIdOrderByIdDesc(String value);

}
