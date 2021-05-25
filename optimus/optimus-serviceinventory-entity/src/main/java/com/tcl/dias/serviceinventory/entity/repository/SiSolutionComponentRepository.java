package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SiSolutionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiSolutionComponentRepository extends JpaRepository<SiSolutionComponent, Integer> {

    SiSolutionComponent findFirstByServiceCodeAndIsActive(String serviceCode, String active);

    List<SiSolutionComponent> findBySolutionCode(String solutionCode);

    List<SiSolutionComponent> findByParentServiceCode(String serviceCode);
}
