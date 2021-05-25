package com.tcl.dias.location.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.location.entity.entities.CCLocationMapping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CCLocationMappingRepository extends JpaRepository<CCLocationMapping, Integer> {

	List<CCLocationMapping> findByAEndLocId(Integer aEndLocId);
}
