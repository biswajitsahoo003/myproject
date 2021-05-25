package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MfDependantTeam;

@Repository
public interface MfDependantTeamRepository extends JpaRepository<MfDependantTeam, Integer>{
	
	public List<MfDependantTeam> findByTeamNameIn(List<String> teamNames);
	
	public List<MfDependantTeam> findByTeamName(String teamName);
}
