package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIAssetRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SIAssetRelationRepository extends JpaRepository<SIAssetRelation, Integer> {
	List<SIAssetRelation> findAllBySiAssetIdInAndRelationTypeIn(List<Integer> siAssetIds, List<String> relationTypes);
}
