package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.l2oworkflow.entity.entities.MfBtsData;

public interface MfBtsDataRepository extends JpaRepository<MfBtsData,Integer> {

   // List<MfBtsData> findBySiteNameIgnoreCaseContaining(String name);
    
    @Query(value = "SELECT  * from mf_bts_data where site_name LIKE %:searchValue%", nativeQuery = true)
    List<MfBtsData> findByname(@Param("searchValue") String searchValue);
    
    @Query(value = "SELECT  * from mf_bts_data where site_address LIKE %:searchValue%", nativeQuery = true)
    List<MfBtsData> findByAddress(@Param("searchValue") String searchValue);
    
    
    List<MfBtsData> findTop500By();

}
