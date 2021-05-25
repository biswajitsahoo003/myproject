package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.l2oworkflow.entity.entities.MfProviderData;

public interface MfProviderDataRepository extends JpaRepository<MfProviderData,Integer> {

        List<MfProviderData> findByProviderNameIgnoreCaseContaining(String name);
}
