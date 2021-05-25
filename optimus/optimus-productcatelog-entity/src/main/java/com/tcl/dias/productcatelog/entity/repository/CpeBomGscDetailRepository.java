package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomGscDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGscDetailViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CpeBomGscDetailRepository extends JpaRepository<CpeBomGscDetailView, CpeBomGscDetailViewId> {

	@Query(value = "SELECT * from vw_cpe_gsc_bom_dtl a where a.bom_name=:bomname and a.port_interface=:portInterface and a.routing_protocol=:routingprotocal", nativeQuery = true)
	List<CpeBomGscDetailView> findByBomNameAndPortInterfaceAndRoutingProtocol(
			@Param("bomname") String bomname, @Param("portInterface") String portInterface, @Param("routingprotocal") String routingprotocal);

    List<CpeBomGscDetailView> findByProductCodeIn(List<String> nimModulesProductCodes);
}
