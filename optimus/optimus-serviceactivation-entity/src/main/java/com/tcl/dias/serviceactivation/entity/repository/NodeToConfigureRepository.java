package com.tcl.dias.serviceactivation.entity.repository;

import com.tcl.dias.serviceactivation.entity.entities.NodeToConfigure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeToConfigureRepository extends JpaRepository<NodeToConfigure,Integer> {
}
