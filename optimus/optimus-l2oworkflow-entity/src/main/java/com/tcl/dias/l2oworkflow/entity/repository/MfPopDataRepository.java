package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.l2oworkflow.entity.entities.MfPopData;

public interface MfPopDataRepository extends JpaRepository<MfPopData, Integer> {

    
    @Query(value = "SELECT  * from mf_pop_data where name LIKE %:searchValue%", nativeQuery = true)
    List<MfPopData> findByname(@Param("searchValue") String searchValue);
    
    @Query(value = "SELECT  * from mf_pop_data where city LIKE %:searchValue% ", nativeQuery = true)
    List<MfPopData> findByCity(@Param("searchValue") String searchValue);
    
    @Query(value = "SELECT  * from mf_pop_data where state LIKE %:searchValue%", nativeQuery = true)
    List<MfPopData> findByState(@Param("searchValue") String searchValue);
    
    List<MfPopData> findTop500ByOrderByNameAsc();
    
    

}


