package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomGscDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGscIntlDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGscIntlDetailViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CpeBomGscDetailIntlRepository extends JpaRepository<CpeBomGscIntlDetailView, CpeBomGscIntlDetailViewId> {

	@Query(value = "SELECT * from vw_cpe_gsc_bom_dtl_intl a where a.country=:country and a.bom_name=:bomname and a.port_interface=:portInterface and a.routing_protocol=:routingprotocal  and (a.Handoff is null  or a.Handoff=:handoff)", nativeQuery = true)
	List<CpeBomGscIntlDetailView> findByCountryCodeAndBomNameAndPortInterfaceAndRoutingProtocolAndHandoffOrHandoff(
            @Param("country") String country, @Param("bomname") String bomname,
            @Param("portInterface") String portInterface, @Param("routingprotocal") String routingprotocal, @Param("handoff") String handoff);

	List<CpeBomGscIntlDetailView> findByProductCodeIn(List<String> sfpModulesProductCodes);

	List<CpeBomGscIntlDetailView> findByCountryAndBomNameAndPortInterfaceAndRoutingProtocol(String country, String bomName, String portInterface, String routingProtocol);
}
