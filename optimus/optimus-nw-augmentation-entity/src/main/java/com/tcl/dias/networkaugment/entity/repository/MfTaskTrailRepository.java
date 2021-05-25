package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.MfTaskDetailAudit;
import com.tcl.dias.networkaugment.entity.entities.MfTaskTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MfTaskTrailRepository extends JpaRepository <MfTaskTrail, Integer>{

    @Query(value = "SELECT * FROM mf_task_trail WHERE task_id in (SELECT id FROM task WHERE order_code = f_get_ordercode_bytaskid(:taskId))  ORDER BY id desc ",nativeQuery = true)
    public Optional<ArrayList<MfTaskTrail>> findByTaskId(Integer taskId);

    @Query(value = "SELECT * FROM mf_task_trail WHERE task_id = :taskId AND completed_by = :completedBy",nativeQuery = true)
    public Optional<ArrayList<MfTaskTrail>> findByTaskIdAndCompletedBy(Integer taskId, String completedBy);

}
