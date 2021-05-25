package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlViewId;


@Repository
public interface CpeBomGvpnIntlRepository extends JpaRepository<CpeBomGvpnIntlView, CpeBomGvpnIntlViewId> {
		
	public List<CpeBomGvpnIntlView> findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(
		    String portInterface, String routingProtocol, String cpeManageType,Double bandwidth);

	@Query(value = "select * \n" +
			"from vw_cpe_bom_GVPN_INTL \n" +
			"where port_interface=:portInterface and routing_protocol=:routingProtocal \n" +
			"and cpe_management_option=:cpeManagement and max_bw_in_mbps>=:bandwidth \n" +
			"and cpe_service_config like %:cpeServiceConfig% \n" +
			"group by bom_name", nativeQuery = true)
	List<CpeBomGvpnIntlView> findCpeBomIntl(@Param("portInterface") String portInterface, @Param("cpeManagement") String cpeManagement,
											@Param("routingProtocal") String routingProtocal, @Param("bandwidth") Double bandwidth, @Param("cpeServiceConfig") String cpeServiceConfig);



}
