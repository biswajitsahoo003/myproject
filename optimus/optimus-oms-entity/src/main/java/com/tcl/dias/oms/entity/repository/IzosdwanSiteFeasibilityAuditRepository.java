package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibilityAudit;

@Repository
public interface IzosdwanSiteFeasibilityAuditRepository extends JpaRepository<IzosdwanSiteFeasibilityAudit, Integer>{
	List<IzosdwanSiteFeasibilityAudit> findByIzosdwanSiteFeasibility(IzosdwanSiteFeasibility izosdwanSiteFeasibility);
}
