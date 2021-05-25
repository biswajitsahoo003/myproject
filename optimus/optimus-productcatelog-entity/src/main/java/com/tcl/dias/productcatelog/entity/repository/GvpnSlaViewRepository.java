package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GvpnSlaView;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaViewId;

/**
 * Repository class for GVPN SLA View
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface GvpnSlaViewRepository extends JpaRepository<GvpnSlaView, GvpnSlaViewId> {

	List<GvpnSlaView> findDistinctByAccessTopologyAndSlaId(String accessTopology, Integer slaId);

	
	List<GvpnSlaView> findBySlaId(Integer slaId);


	Optional<GvpnSlaView> findBySlaIdNoIn(List<Integer> slaIdNoList);
}
