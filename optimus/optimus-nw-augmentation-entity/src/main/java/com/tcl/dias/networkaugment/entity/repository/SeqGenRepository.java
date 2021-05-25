package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.SeqGen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeqGenRepository extends JpaRepository<SeqGen, Integer> {

    SeqGen findByApplicationId(String applicationId);

}
