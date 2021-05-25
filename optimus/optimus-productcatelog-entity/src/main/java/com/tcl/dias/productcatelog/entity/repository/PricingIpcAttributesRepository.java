package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcAttribute;

@Repository
public interface PricingIpcAttributesRepository extends JpaRepository<PricingIpcAttribute, Integer> {

}
