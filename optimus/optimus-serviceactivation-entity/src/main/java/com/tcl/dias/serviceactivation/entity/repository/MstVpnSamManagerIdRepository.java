package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstVpnSamManagerId;

@Repository
public interface MstVpnSamManagerIdRepository extends JpaRepository<MstVpnSamManagerId, Integer> {

	MstVpnSamManagerId findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(String vpnName, String vpnType, String topology,String serviceCode,String siteRole);
	
	MstVpnSamManagerId findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRole(String vpnName, String vpnType, String topology,String siteRole);
	
	MstVpnSamManagerId findByVpnNameAndVpnTypeAndVpnTopology(String vpnName, String vpnType, String topology);

	MstVpnSamManagerId findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(String vpnName, String vpnType,String topologyName, String siteRole);

	MstVpnSamManagerId findFirstByVpnNameAndBgpPasswordIsNotNull(String vpnName);

}
