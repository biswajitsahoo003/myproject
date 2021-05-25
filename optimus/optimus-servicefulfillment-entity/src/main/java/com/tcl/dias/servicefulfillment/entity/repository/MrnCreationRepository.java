package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MrnCreationEntity;

import java.util.Optional;

@Repository
public interface MrnCreationRepository extends JpaRepository<MrnCreationEntity, Integer>{

    Optional<MrnCreationEntity> findByMrnNumber(String mrnNo);
}
