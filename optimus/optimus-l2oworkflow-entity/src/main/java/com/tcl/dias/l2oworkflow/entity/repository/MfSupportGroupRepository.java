package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.l2oworkflow.entity.entities.MfSupportGroup;

public interface MfSupportGroupRepository extends JpaRepository<MfSupportGroup,Integer> {

    MfSupportGroup findByGroupName(String groupName);
}
