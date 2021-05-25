package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaMakeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sun.awt.SunHints;

import java.util.List;

@Repository
public interface NwaMakeModelRepository extends JpaRepository<NwaMakeModel, Integer> {

    @Query(value = "SELECT * FROM nwa_make_model where make=:make", nativeQuery = true)
    List<NwaMakeModel> findByMake(String make);
}
