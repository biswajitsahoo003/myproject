package com.tcl.dias.serviceactivation.activation.factory.gvpn.base;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.netp.beans.Bandwidth;
import com.tcl.dias.serviceactivation.activation.netp.beans.CustomerPremiseEquipment;
import com.tcl.dias.serviceactivation.activation.netp.beans.EthernetInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.GVPNService;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV4Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV6Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.Interface;
import com.tcl.dias.serviceactivation.activation.netp.beans.LastMile;
import com.tcl.dias.serviceactivation.activation.netp.beans.Leg;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPN;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPNSolutionTable;
import com.tcl.dias.serviceactivation.activation.netp.beans.WANInterface;
import com.tcl.dias.serviceactivation.activation.utils.ActivationUtils;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;

/**
 * This class is used to build all the common configurations for RF and IP in
 * GVPN
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public abstract class AbstractGvpnConfiguration extends AbstractConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGvpnConfiguration.class);

	/**
	 * 
	 * generateRequestId
	 * 
	 * @param serviceUuid
	 * @return
	 */
	public String generateRequestId(String serviceUuid) {
		return "RID_" + serviceUuid + "_" + Calendar.getInstance().getTimeInMillis() + "_GVPNE2E";

	}

	/**
	 * 
	 * createRFProductConfig
	 * 
	 * @param serviceDetail
	 * @param service
	 */
	protected void createRFProductConfig(ServiceDetail serviceDetail, OrderInfo3.Service service) {
		GVPNService gvpnService = new GVPNService();
		Bandwidth bandwidth = new Bandwidth();
		bandwidth.setSpeed(serviceDetail.getServiceBandwidth());
		bandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
		gvpnService.setServiceBandwidth(bandwidth);
		Bandwidth burstableBandwidth = new Bandwidth();
		burstableBandwidth.setSpeed(serviceDetail.getBurstableBw());
		burstableBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
		gvpnService.setBurstableBandwidth(burstableBandwidth);
		service.setGvpnService(gvpnService);
	}

	protected OrderInfo3.Service createServiceDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		GVPNService gvpnService = new GVPNService();
		if (serviceDetail.getCssSammgrId() != null)
			gvpnService.setCSSSAMID(ActivationUtils.toString(serviceDetail.getCssSammgrId()));
		gvpnService.setServiceType(serviceDetail.getServiceType());
		gvpnService.setServiceSubType(serviceDetail.getServiceSubtype());
		gvpnService.setSAMCustomerDescription(orderDetail.getSamCustomerDescription());// ALU SPEC
		if (StringUtils.isNotBlank(serviceDetail.getUsageModel()))
			gvpnService.setUsageModel(serviceDetail.getUsageModel());
		if (StringUtils.isNotBlank(serviceDetail.getDataTransferCommit()))
			gvpnService.setDataTransferCommit(serviceDetail.getDataTransferCommit());
		
		if (StringUtils.isNotBlank(serviceDetail.getDataTransferCommitUnit()))
			gvpnService.setDataTransferCommitUnit(serviceDetail.getDataTransferCommitUnit());
		if (StringUtils.isNotBlank(serviceDetail.getDescription()))
			gvpnService.setDescriptionFreeText(serviceDetail.getDescription());
		gvpnService.setInstanceID(serviceDetail.getNetpRefid());
		List<VpnSolution> vpnSolutions = vpnSolutionRepository
				.findByServiceDetail_IdAndEndDateIsNull(serviceDetail.getId());
		String currentLegId = null;
		VPNSolutionTable solutionTable = new VPNSolutionTable();
		for (VpnSolution vpnSolution : vpnSolutions) {
			solutionTable.setSolutionID(serviceDetail.getSolutionId());
			VPN vpn = new VPN();
			if(vpnSolution.getVpnName()!=null &&vpnSolution.getVpnName().equalsIgnoreCase("INTERNET_VPN")) {
				vpn.setVpnId("Internet_VPN");

			}
			else {
			vpn.setVpnId(vpnSolution.getVpnName());
			}
			vpn.setVpnType(vpnSolution.getVpnType());
			vpn.setTopology(vpnSolution.getVpnTopology());
			Leg leg = new Leg();
			leg.setLegServiceID(vpnSolution.getVpnLegId());
			currentLegId = vpnSolution.getVpnLegId();
			leg.setRole(vpnSolution.getLegRole());
			if (vpnSolution.getInterfaceName() != null)
				leg.setInterfacename(vpnSolution.getInterfaceName());
			leg.setSiteID(vpnSolution.getSiteId());
			if (vpnSolution.getInstanceId() != null)
				leg.setInstanceID(vpnSolution.getInstanceId());
			vpn.getLeg().add(leg);
			solutionTable.getVPN().add(vpn);
			gvpnService.setSolutionTable(solutionTable);
		}

		gvpnService.setVrf(createVrfDetails(serviceDetail, gvpnService, currentLegId));
		if (serviceDetail.getScopeOfMgmt() != null
				&& serviceDetail.getScopeOfMgmt().toLowerCase().contains("proactive"))
			gvpnService.setIsProactiveMonitoringEnabled("true");
		else {
			gvpnService.setIsProactiveMonitoringEnabled("false");
		}
		if (!(serviceDetail.getRedundancyRole() != null
				&& serviceDetail.getRedundancyRole().toUpperCase().contains("SINGLE"))) {
			gvpnService.getServiceLink().add(createServiceLinks(serviceDetail));
		}
		gvpnService.setInterfaceDescriptionServiceTag(serviceDetail.getIntefaceDescSvctag());
		List<InterfaceProtocolMapping> interfaceProtocolMappings = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndRouterDetailNotNullAndEndDateIsNull(serviceDetail.getId());
		for (InterfaceProtocolMapping interfaceProtocolMapping : interfaceProtocolMappings) {
			if (interfaceProtocolMapping.getCpe() == null)// TODO
				gvpnService.getRouter().add(createPeRouterDetails(interfaceProtocolMapping, serviceDetail));
		}

		Bandwidth bandwidth = new Bandwidth();
		bandwidth.setSpeed(serviceDetail.getServiceBandwidth());
		bandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
		gvpnService.setServiceBandwidth(bandwidth);
		if(serviceDetail.getBurstableBw()!=null) {
		Bandwidth burstableBandwidth = new Bandwidth();
		burstableBandwidth.setSpeed(serviceDetail.getBurstableBw());
		burstableBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
		gvpnService.setBurstableBandwidth(burstableBandwidth);
		}
		if (StringUtils.isNotBlank(serviceDetail.getRedundancyRole())) {
			gvpnService.setRedundancyRole(serviceDetail.getRedundancyRole().toUpperCase());
		}
		
		if (serviceDetail.getMgmtType().toUpperCase().contains("UNMANAGED")) {
			gvpnService.setIsConfigManaged(false);

		} else {
			gvpnService.setIsConfigManaged(true);

		}
		IpAddressDetail ipAddressDetail = ipAddressDetailRepository
				.findByServiceDetailIdAndEndDateIsNull(serviceDetail.getId());
		if (StringUtils.isNotBlank(ipAddressDetail.getPingAddress1())) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(ipAddressDetail.getPingAddress1());
			gvpnService.setPingV4IPAddress(ipv4Address);
		}
		if (StringUtils.isNotBlank(ipAddressDetail.getPingAddress2())) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(ipAddressDetail.getPingAddress2());
			gvpnService.setPingV6IPAddress(ipv6Address);
		}
		if (StringUtils.isNotBlank(ipAddressDetail.getNmsServiceIpv4Address())) {
			IPV4Address nmsServerIpv4Address = new IPV4Address();
			nmsServerIpv4Address.setAddress(ipAddressDetail.getNmsServiceIpv4Address());
			gvpnService.setNmsServerv4IPAddress(nmsServerIpv4Address);
		}
		if(serviceDetail.getServiceQos()!=null && !serviceDetail.getServiceQos().isEmpty()){
			ServiceQo serviceQos=serviceDetail.getServiceQos().stream().findFirst().orElse(null);
			if(serviceQos!=null && serviceQos.getFlexiCosIdentifier()!=null && !serviceQos.getFlexiCosIdentifier().isEmpty()){
				gvpnService.setFlexiCOSIdentifier(serviceQos.getFlexiCosIdentifier());
			}
		}
		gvpnService.setExtendedLAN(createExtendedLanConfig(ipAddressDetail));//
		Set<IpaddrWanv4Address> wanV4Addresses = ipAddressDetail.getIpaddrWanv4Addresses();
		for (IpaddrWanv4Address ipaddrWanv4Address : wanV4Addresses) {
			IPV4Address wanV4Address = new IPV4Address();
			wanV4Address.setAddress(ipaddrWanv4Address.getWanv4Address());
			gvpnService.getWanV4Addresses().add(wanV4Address);
		}

		Set<IpaddrWanv6Address> wanV6Addresses = ipAddressDetail.getIpaddrWanv6Addresses();
		for (IpaddrWanv6Address ipaddrWanv6Address : wanV6Addresses) {
			IPV6Address wanIpv6Address = new IPV6Address();
			wanIpv6Address.setAddress(ipaddrWanv6Address.getWanv6Address());
			gvpnService.getWanV6Addresses().add(wanIpv6Address);
		}

		OrderInfo3.Service service = new OrderInfo3.Service();
		List<InterfaceProtocolMapping> bgInterfaceProtocolEntity = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndEndDateIsNull(serviceDetail.getId());// TO ASK DOUBT ---->END DATE
																				// SHOULD BE NULL
		for (InterfaceProtocolMapping interfaceProtocolMapping : bgInterfaceProtocolEntity) {
			if (interfaceProtocolMapping.getCpe() == null) {
				Bgp bgp = interfaceProtocolMapping.getBgp();
				Ospf ospf = interfaceProtocolMapping.getOspf();
				StaticProtocol staticProtocol = interfaceProtocolMapping.getStaticProtocol();
				gvpnService.setWanRoutingProtocol(createServiceWanRoutingProtocol(bgp, ospf, staticProtocol));
				if (bgp != null) {
					Set<WanStaticRoute> wanstaticRoutes = bgp.getWanStaticRoutes();
					gvpnService.setPEWANAdditionalStaticRoutes(createPeWanAdditionalStaticRoutesBgp(wanstaticRoutes));
				}
			}
		}

		gvpnService.setQos(createServiceQos(serviceDetail));
		List<InterfaceProtocolMapping> cpeInterfaceProtocolEntity = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndCpeNotNullAndEndDateIsNull(serviceDetail.getId());
		for (InterfaceProtocolMapping interfaceProtocolMapping : cpeInterfaceProtocolEntity) {
			CustomerPremiseEquipment cpe = createCpeDetails(interfaceProtocolMapping);
			if (cpe != null)
				gvpnService.getCpe().add(cpe);
		}
		if("ACTIVE".equalsIgnoreCase(serviceDetail.getServiceState()) && (cpeInterfaceProtocolEntity==null || cpeInterfaceProtocolEntity.isEmpty())) { //Applicable only for IP Blocking and Termination
			CustomerPremiseEquipment cpe = new CustomerPremiseEquipment();
			cpe.setIsCEACEConfigurable(false);
			cpe.setSnmpServerCommunity("t2c2l2com");
			Map<String,String> ipAddress=getTclWanDetailsFromReverseISC(serviceDetail.getServiceId());
			if(ipAddress.containsKey("customerEndLoopback") && ipAddress.get("customerEndLoopback")!=null) {
				Interface loopbackInterface = new Interface();
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ipAddress.get("customerEndLoopback"));
				loopbackInterface.setV4IpAddress(ipv4Address);
				cpe.setLoopbackInterface(loopbackInterface);
			}
			WANInterface wanInterface = new WANInterface();
			WANInterface.Interface wIntfce = new WANInterface.Interface();
			EthernetInterface etherInterface = new EthernetInterface();
			etherInterface.setMode("ACCESS");
			etherInterface.setDuplex(ActivationUtils.getDuplex("NOT_APPLICABLE"));
			etherInterface.setIsAutoNegotiation("NOT_APPLICABLE");
			etherInterface.setSpeed("NOT_APPLICABLE");
			if(ipAddress.containsKey("wanIpAddress") && ipAddress.get("wanIpAddress")!=null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ipAddress.get("wanIpAddress"));
				etherInterface.setV4IpAddress(ipv4Address);
			}
			wIntfce.setEthernetInterface(etherInterface);
			wanInterface.setInterface(wIntfce);
			cpe.setWanInterface(wanInterface);
			gvpnService.getCpe().add(cpe);
		}
		if (serviceDetail.getLastmileType() != null && !serviceDetail.getLastmileType().isEmpty()) {
			LOGGER.info("LastMileType exists");
				LastMile lastMile = new LastMile();
				lastMile.setType(serviceDetail.getLastmileType());
				if(Objects.nonNull(serviceDetail.getLastmileType()) && !serviceDetail.getLastmileType().isEmpty() && serviceDetail.getLastmileType().equalsIgnoreCase("OnnetRF")){
					LOGGER.info("OnnetRF as LastMileType");
					/*List<LmComponent> lmComponents=lmComponentRepository.findByServiceDetail_id(serviceDetail.getId());
					if(Objects.nonNull(lmComponents) && !lmComponents.isEmpty()){
						LOGGER.info("LM Component exists");
						lmComponents.stream().filter(lmComp -> Objects.nonNull(lmComp.getLmOnwlProvider()) 
								&& !lmComp.getLmOnwlProvider().isEmpty() && "Radwin".equalsIgnoreCase(lmComp.getLmOnwlProvider())).forEach(lmComp ->{
							LOGGER.info("OnnetRF as Radwin");
							lastMile.setType(lmComp.getLmOnwlProvider().toUpperCase());
						});
					}else{
						LOGGER.info("OnnetRF not Radwin");
						lastMile.setType(serviceDetail.getLastmileType());
					}*/
					LOGGER.info("{}=>getLastmileProvider={}",serviceDetail.getServiceId(),serviceDetail.getLastmileProvider());
					if(Objects.nonNull(serviceDetail.getLastmileProvider()) && !serviceDetail.getLastmileProvider().isEmpty() && 
							serviceDetail.getLastmileProvider().toLowerCase().contains("radwin") && !serviceDetail.getLastmileProvider().toLowerCase().contains("pmp")){
						lastMile.setType("RADWIN");
					}
				}else{
					LOGGER.info("Not OnnetRF as LastMileType");
					lastMile.setType(serviceDetail.getLastmileType());
				}
				gvpnService.getLastMile().add(lastMile);
		}
		service.setGvpnService(gvpnService);
		return service;
	}
}
