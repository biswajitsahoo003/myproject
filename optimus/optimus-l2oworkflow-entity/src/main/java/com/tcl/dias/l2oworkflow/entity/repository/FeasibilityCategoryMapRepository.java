package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.l2oworkflow.entity.entities.FeasibilityCategoryMap;

public interface FeasibilityCategoryMapRepository extends JpaRepository<FeasibilityCategoryMap, Integer> {

    List<FeasibilityCategoryMap> findByFModeAndFStatus(String fMode, String fStatus);

}
