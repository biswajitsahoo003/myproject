package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.ProcessAccessRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessAccessRightsRepository extends JpaRepository<ProcessAccessRights, Integer> {

    @Query(value = "select * from process_access_right where group_name =:groupName", nativeQuery = true)
    ProcessAccessRights findByGroupName(String groupName);

}
