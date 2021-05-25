package com.tcl.dias.productcatelog.entity.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.tcl.dias.productcatelog.entity.entities.BomMaster;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlDetailViewId;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlViewId;


@Repository
public interface CpeBomGvpnDetailIntlRepository extends JpaRepository<CpeBomGvpnIntlDetailView, CpeBomGvpnIntlDetailViewId> {

	CpeBomGvpnIntlDetailView  findByBomName(String cpeBomId);
	
	@Query(value = "SELECT * from vw_cpe_bom_dtl_intl a where a.country=:country and a.bom_name=:bomname and a.port_interface=:portInterface and a.routing_protocol=:routingprotocal  and (a.Handoff is null  or a.Handoff=:handoff)", nativeQuery = true)
	List<CpeBomGvpnIntlDetailView> findByCountryCodeAndBomNameAndPortInterfaceAndRoutingProtocolAndHandoffOrHandoff(
			@Param("country") String country, @Param("bomname") String bomname,
			@Param("portInterface") String portInterface,@Param("routingprotocal") String routingprotocal,@Param("handoff") String handoff);
	
	
		
	
	}
