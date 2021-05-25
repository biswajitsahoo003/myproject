package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceSla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScServiceSlaRepository extends JpaRepository<ScServiceSla,Integer> {
    List<ScServiceSla> findAllByScServiceDetail_Id(Integer serviceId);

}
