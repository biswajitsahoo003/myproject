package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GscSlaView;
import com.tcl.dias.productcatelog.entity.entities.GscSlaViewId;

/**
 * Method related to GscSlaViewRepository
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GscSlaViewRepository extends JpaRepository<GscSlaView, GscSlaViewId> {

	List<GscSlaView> findByAccessTopology(String request);

}
