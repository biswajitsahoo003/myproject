package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.l2oworkflow.entity.entities.MfHhData;

public interface MfHhDataRepository extends JpaRepository<MfHhData, Integer>{

   // List<MfHhData> findByNumHhIgnoreCaseContaining(String name);
    
    @Query(value = "SELECT  * from mf_hh_data where num_hh LIKE %:searchValue%", nativeQuery = true)
    List<MfHhData> findByname(@Param("searchValue") String searchValue);
    
    @Query(value = "SELECT  * from mf_hh_data where hh_state LIKE %:searchValue%", nativeQuery = true)
    List<MfHhData> findByState(@Param("searchValue") String searchValue);
    
    List<MfHhData> findTop500ByOrderByNumHh();
    
}
