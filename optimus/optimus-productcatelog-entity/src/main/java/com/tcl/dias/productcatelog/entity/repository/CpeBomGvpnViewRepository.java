package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnViewId;

@Repository
public interface CpeBomGvpnViewRepository extends JpaRepository<CpeBomGvpnView, CpeBomGvpnViewId> {
		
	public List<CpeBomGvpnView> findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(
		    String portInterface, String routingProtocol, String cpeManageType,Double bandwidth);

	}
