package com.tcl.dias.serviceactivation.activation.factory.gvpn.macd.base;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.dias.serviceactivation.activation.constants.AceConstants;
import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.netp.beans.ACEHeader;
import com.tcl.dias.serviceactivation.activation.netp.beans.ALUSchedulerPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.ASNumber;
import com.tcl.dias.serviceactivation.activation.netp.beans.AccessControListlEntry;
import com.tcl.dias.serviceactivation.activation.netp.beans.AccessControlList;
import com.tcl.dias.serviceactivation.activation.netp.beans.ActionRequired;
import com.tcl.dias.serviceactivation.activation.netp.beans.AddonFeatures;
import com.tcl.dias.serviceactivation.activation.netp.beans.Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.BFDConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.BGPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.Bandwidth;
import com.tcl.dias.serviceactivation.activation.netp.beans.BandwidthUnit;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumAAAConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumAntennaParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumLastmile;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumSite;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumVLANAttributes;
import com.tcl.dias.serviceactivation.activation.netp.beans.ChannelizedSDHInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.CoS;
import com.tcl.dias.serviceactivation.activation.netp.beans.Customer;
import com.tcl.dias.serviceactivation.activation.netp.beans.CustomerPremiseEquipment;
import com.tcl.dias.serviceactivation.activation.netp.beans.DataMDT;
import com.tcl.dias.serviceactivation.activation.netp.beans.DscpValue;
import com.tcl.dias.serviceactivation.activation.netp.beans.EthernetInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.ExtendedLAN;
import com.tcl.dias.serviceactivation.activation.netp.beans.GVPNService;
import com.tcl.dias.serviceactivation.activation.netp.beans.HBSConfigParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.HSRPProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.HSUConfigParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPAddress;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPAddressList;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPPrecedent;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPService.WanRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPSubnetADValueMap;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPSubnetADValueMap.IpSubnet;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV4Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV6Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.Interface;
import com.tcl.dias.serviceactivation.activation.netp.beans.LANInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.LastMile;
import com.tcl.dias.serviceactivation.activation.netp.beans.Leg;
import com.tcl.dias.serviceactivation.activation.netp.beans.LoopbackInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.MACAddressVIDMap;
import com.tcl.dias.serviceactivation.activation.netp.beans.MVLANUntaggedMode;
import com.tcl.dias.serviceactivation.activation.netp.beans.MaximumRoutes;
import com.tcl.dias.serviceactivation.activation.netp.beans.NATMode;
import com.tcl.dias.serviceactivation.activation.netp.beans.OSPFRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderCategory;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo2;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3.Service;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderType2;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderType3;
import com.tcl.dias.serviceactivation.activation.netp.beans.PerformIPServiceActivation;
import com.tcl.dias.serviceactivation.activation.netp.beans.PerformTransmissionServiceActivation;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixList;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixListEntry;
import com.tcl.dias.serviceactivation.activation.netp.beans.Protocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.QoS;
import com.tcl.dias.serviceactivation.activation.netp.beans.RPAddress;
import com.tcl.dias.serviceactivation.activation.netp.beans.Radwin5KLastmile;
import com.tcl.dias.serviceactivation.activation.netp.beans.Router;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutesExchanged;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.SNMPParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.SerialInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.ServiceLink;
import com.tcl.dias.serviceactivation.activation.netp.beans.StaticRoutes;
import com.tcl.dias.serviceactivation.activation.netp.beans.StaticRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.Switch;
import com.tcl.dias.serviceactivation.activation.netp.beans.TopologyInfo;
import com.tcl.dias.serviceactivation.activation.netp.beans.TransmissionService;
import com.tcl.dias.serviceactivation.activation.netp.beans.VLANQOSProfile;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPN;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPNSolutionTable;
import com.tcl.dias.serviceactivation.activation.netp.beans.VirtualRouteForwardingServiceInstance;
import com.tcl.dias.serviceactivation.activation.netp.beans.WANInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.WimaxLastMile;
import com.tcl.dias.serviceactivation.activation.utils.ActivationUtils;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Broadcast;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.CienaParam;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.GetCLRInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.L2Params;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Protection;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.AluSchedulerPolicy;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.HsrpVrrpProtocol;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpDummyDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.MstCambiumDetails;
import com.tcl.dias.serviceactivation.entity.entities.MstRadwinDetails;
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.RouterUplinkport;
import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.TxConfiguration;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.VlanQosProfile;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.entities.WimaxLastmile;
import com.tcl.dias.serviceactivation.entity.repository.InterfaceProtocolMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpDummyDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstCambiumDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRadwinDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.NeighbourCommunityConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RegexAspathConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceCosCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.TxConfigurationRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;

/**
 * This class is used to build all the common configurations for RF and IP in
 * Ill
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public abstract class AbstractGvpnMACDConfiguration extends AbstractConfiguration {

	/**
	 * 
	 * generateRequestId
	 * 
	 * @param serviceUuid
	 * @return
	 */
	public String generateRequestId(String serviceUuid) {
		return "RID_" + serviceUuid + "_" + Calendar.getInstance().getTimeInMillis() + "_IASE2E";

	}
	
	


	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfiguration.class);

	@Autowired
	protected ServiceDetailRepository serviceDetailRepository;

	@Autowired
	protected IpAddressDetailRepository ipAddressDetailRepository;

	@Autowired
	protected InterfaceProtocolMappingRepository interfaceProtocolMappingRepository;

	@Autowired
	protected PrefixlistConfigRepository prefixListConfigRepository;

	@Autowired
	protected ServiceCosCriteriaRepository serviceCosCriteriaRepository;

	@Autowired
	protected PolicyCriteriaRepository policyCriteriaRepository;

	@Autowired
	protected NeighbourCommunityConfigRepository neighbourCommunityConfigRepository;

	@Autowired
	protected RegexAspathConfigRepository regexAspathConfigRepository;

	@Autowired
	protected TxConfigurationRepository txConfigurationRepository;

	@Autowired
	protected VpnSolutionRepository vpnSolutionRepository;
	
	@Autowired
	protected IpDummyDetailRepository ipDummyDetailRepository;
	
	@Autowired
	MstCambiumDetailsRepository mstCambiumDetailsRepository;
	
	@Autowired
	MstRadwinDetailsRepository mstRadwinDetailsRepository;
	
	/**
	 * This method is the main method for generating the xml for Ip Config
	 * generateDummyIPConfigXml
	 * 
	 * @param serviceId
	 * @param actionMode
	 * @param requestId
	 * @return
	 */
	public String generateDummyIPConfigXml(ServiceDetail serviceDetail, String actionMode, String requestId) {
		LOGGER.info("Inside AbstractGvpnMACDConfiguration.generateIpConfigXml with input serviceId {}", serviceDetail.getId());
		String response = null;
		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		performIPServiceActivation
				.setHeader(constructAceHeader(serviceDetail.getServiceId(), orderDetail, actionMode, requestId));
		performIPServiceActivation.setOrderDetails(createDummyOrderDetails(serviceDetail, orderDetail,actionMode));
		response = jaxbObjectToXML(performIPServiceActivation);
		return mapXmlHeaders(response);
	}
	

	/**
	 * This method is the main method for generating the xml for Ip Config
	 * generateIPConfigXml
	 * 
	 * @param serviceId
	 * @return
	 */
	public String generateIPConfigXml(ServiceDetail serviceDetail, String actionMode, String requestId) {
		LOGGER.info("Inside generateIpConfigXml with input serviceId {}", serviceDetail.getId());
		String response = null;
		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		performIPServiceActivation
				.setHeader(constructAceHeader(serviceDetail.getServiceId(), orderDetail, actionMode, requestId));
		performIPServiceActivation.setOrderDetails(createOrderDetails(serviceDetail, orderDetail));
		
		// Get PrevActiveServiceDetail based on Order Type condition
		String orderType=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);
		LOGGER.info("AbstractGvpnMACD Order Type {}", orderType);
		ServiceDetail prevActiveServiceDetail =null;
		if(!orderType.equalsIgnoreCase("NEW")){
					prevActiveServiceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceDetail.getServiceId(),TaskStatusConstants.ACTIVE);
		}
		if(Objects.nonNull(prevActiveServiceDetail)){
			//Validate isServiceBandwidth modified
			isServiceBandwidthModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isBustableBandwidth modified
			isBustableBandwidthModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isCustomerName modified
			isCustomerNameModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isRedundancyRole modified
			isRedundancyRoleModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isLastMile modified
			isLastMileModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isServiceType modified
			isServiceSubTypeModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isManagedType modified
			isManagedTypeModified(performIPServiceActivation,prevActiveServiceDetail);
			Map<String,Object> prevCPEMap = new HashMap<>();
			findCPEBasicValues(prevActiveServiceDetail,prevCPEMap);
			Map<String,Object> currentCPEMap = new HashMap<>();
			findCPECurrentBasicValues(performIPServiceActivation,currentCPEMap);
			LOGGER.info("PrevCpeMap {}", prevCPEMap);
			LOGGER.info("CurrentCpeMap {}", currentCPEMap);
			//Validate isCPEHostName modified
			isCPEHostNameModified(performIPServiceActivation,prevCPEMap);
			//Validate isCPEWANInterfacePhysicalPort modified
			isCPEWANInterfacePhysicalPortModified(performIPServiceActivation,prevCPEMap,currentCPEMap);
			//Validate isCPEWANInterfaceName modified
			isCPEWANInterfaceNameModified(performIPServiceActivation,prevCPEMap,currentCPEMap);
			//Validate isCPEEthernetVLAN modified
			isCPEEthernetVLANModified(performIPServiceActivation,prevCPEMap,currentCPEMap);
			//Validate isCPEEthernetVLAN modified
			isCPESNMPModified(performIPServiceActivation,prevCPEMap);
			//Validate isCPEEthernetIpDetail modified
			isCPEIpDetailModified(performIPServiceActivation,prevCPEMap,currentCPEMap);
			Map<String,Object> prevRouterMap = new HashMap<>();
			findRouterBasicValues(prevActiveServiceDetail,prevRouterMap);
			Map<String,Object> currentRouterMap = new HashMap<>();
			findRouterCurrentBasicValues(performIPServiceActivation,currentRouterMap);
			LOGGER.info("PrevRouterMap {}", prevRouterMap);
			LOGGER.info("CurrentRouterMap {}", currentRouterMap);
			//Validate isRouterHostName modified
			isRouterHostNameModified(performIPServiceActivation,prevRouterMap);
			//Validate isRouterWANInterfacePhysicalPort modified
			isRouterWANInterfacePhysicalPortModified(performIPServiceActivation,prevRouterMap,currentRouterMap);
			//Validate isRouterWANInterfaceName modified
			isRouterWANInterfaceNameModified(performIPServiceActivation,prevRouterMap,currentRouterMap);
			//Validate isRouterEthernetVLAN modified
			isRouterEthernetVLANModified(performIPServiceActivation,prevRouterMap,currentRouterMap);
			//Validate isRouterMgmtIpv4 modified
			isRouterMgmtIpv4Modified(performIPServiceActivation,prevRouterMap);
			//Validate isRouterInterfaceDetail modified
			isRouterInterfaceDetailModified(performIPServiceActivation,prevRouterMap,currentRouterMap);
			//Validate isRouterIpDetail modified
			isRouterIpDetailModified(performIPServiceActivation,prevRouterMap,currentRouterMap);
			//Validate isRouterInterfaceAccessCtrlList modified
			isRouterInterfaceAccessCtrlListModified(performIPServiceActivation,prevRouterMap,currentRouterMap);
			//Validate isRouterUniSwitchHandOffDetail modified
			isRouterUniSwitchHandOffModified(performIPServiceActivation,prevActiveServiceDetail,serviceDetail);
			//Validate isExtendedLAN modified
			isExtendedLANModified(performIPServiceActivation,prevActiveServiceDetail,serviceDetail);
			//Validate isServiceId modified
			isServiceIdModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isCOSDetail modified
			isCOSDetailModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isVRFName modified
			isVRFNameModified(performIPServiceActivation,prevActiveServiceDetail,serviceDetail);
			//Validate isSolutionTableDetail modified
			isSolutionTableDetailModified(performIPServiceActivation,prevActiveServiceDetail,serviceDetail);
			//Validate isIpAddressDetail modified
			isIpAddressDetailModified(performIPServiceActivation,prevActiveServiceDetail);
			//Validate isMultiCastDetail modified
			isMultiCastDetailModified(performIPServiceActivation,prevActiveServiceDetail,serviceDetail);
			
		}
		response = jaxbObjectToXML(performIPServiceActivation);
		return mapXmlHeaders(response);
	}
	
	private void isMultiCastDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		LOGGER.info("isMultiCastDetailModified");
		if((Objects.nonNull(currentServiceDetail.getMulticastings()) && Objects.isNull(currentServiceDetail.getMulticastings()))
				|| (Objects.isNull(currentServiceDetail.getMulticastings()) && Objects.nonNull(currentServiceDetail.getMulticastings()))){
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
		}
	}


	private void findRouterCurrentBasicValues(PerformIPServiceActivation performIPServiceActivation,
			Map<String, Object> currentRouterMap) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_PHYSICAL_PORT,wanInterfaceType.getEthernetInterface().getPhysicalPortName());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INTERFACE_NAME,wanInterfaceType.getEthernetInterface().getName());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_ETHERNET_VLAN,wanInterfaceType.getEthernetInterface().getVlan());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_V4_ADDRESS,Objects.nonNull(wanInterfaceType.getEthernetInterface().getV4IpAddress())?wanInterfaceType.getEthernetInterface().getV4IpAddress().getAddress():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_V6_ADDRESS,Objects.nonNull(wanInterfaceType.getEthernetInterface().getV6IpAddress())?wanInterfaceType.getEthernetInterface().getV6IpAddress().getAddress():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V4,Objects.nonNull(wanInterfaceType.getEthernetInterface().getInboundAccessControlList())?wanInterfaceType.getEthernetInterface().getInboundAccessControlList().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V6,Objects.nonNull(wanInterfaceType.getEthernetInterface().getInboundAccessControlListV6())?wanInterfaceType.getEthernetInterface().getInboundAccessControlListV6().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V4,Objects.nonNull(wanInterfaceType.getEthernetInterface().getOutboundAccessControlList())?wanInterfaceType.getEthernetInterface().getOutboundAccessControlList().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V6,Objects.nonNull(wanInterfaceType.getEthernetInterface().getOutboundAccessControlListV6())?wanInterfaceType.getEthernetInterface().getOutboundAccessControlListV6().getName():null);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_PHYSICAL_PORT,wanInterfaceType.getSerialInterface().getPhysicalPortName());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INTERFACE_NAME,wanInterfaceType.getSerialInterface().getName());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_V4_ADDRESS,Objects.nonNull(wanInterfaceType.getSerialInterface().getV4IpAddress())?wanInterfaceType.getSerialInterface().getV4IpAddress().getAddress():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_V6_ADDRESS,Objects.nonNull(wanInterfaceType.getSerialInterface().getV6IpAddress())?wanInterfaceType.getSerialInterface().getV6IpAddress().getAddress():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_CHANNEL_GROUP_NUMBER,wanInterfaceType.getSerialInterface().getChannelGroupNumber());
			List<String> timeSlotList=wanInterfaceType.getSerialInterface().getTimeslot();
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_FIRST_TIME_SLOT,timeSlotList.get(0));
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_LAST_TIME_SLOT,timeSlotList.get(timeSlotList.size()-1));
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V4,Objects.nonNull(wanInterfaceType.getSerialInterface().getInboundAccessControlList())?wanInterfaceType.getSerialInterface().getInboundAccessControlList().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V6,Objects.nonNull(wanInterfaceType.getSerialInterface().getInboundAccessControlListV6())?wanInterfaceType.getSerialInterface().getInboundAccessControlListV6().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V4,Objects.nonNull(wanInterfaceType.getSerialInterface().getOutboundAccessControlList())?wanInterfaceType.getSerialInterface().getOutboundAccessControlList().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V6,Objects.nonNull(wanInterfaceType.getSerialInterface().getOutboundAccessControlListV6())?wanInterfaceType.getSerialInterface().getOutboundAccessControlListV6().getName():null);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_PHYSICAL_PORT,wanInterfaceType.getChannelizedSDHInterface().getPhysicalPortName());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INTERFACE_NAME,wanInterfaceType.getChannelizedSDHInterface().getName());
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_V4_ADDRESS,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getV4IpAddress())?wanInterfaceType.getChannelizedSDHInterface().getV4IpAddress().getAddress():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_V6_ADDRESS,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getV6IpAddress())?wanInterfaceType.getChannelizedSDHInterface().getV6IpAddress().getAddress():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_CHANNEL_GROUP_NUMBER,wanInterfaceType.getChannelizedSDHInterface().getChannelGroupNumber());
			List<String> timeSlotList=wanInterfaceType.getChannelizedSDHInterface().getSixtyFourKTimeslot();
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_FIRST_TIME_SLOT,timeSlotList.get(0));
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_LAST_TIME_SLOT,timeSlotList.get(timeSlotList.size()-1));
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V4,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getInboundAccessControlList())?wanInterfaceType.getChannelizedSDHInterface().getInboundAccessControlList().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V6,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getInboundAccessControlListV6())?wanInterfaceType.getChannelizedSDHInterface().getInboundAccessControlListV6().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V4,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getOutboundAccessControlList())?wanInterfaceType.getChannelizedSDHInterface().getOutboundAccessControlList().getName():null);
			currentRouterMap.put(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V6,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getOutboundAccessControlListV6())?wanInterfaceType.getChannelizedSDHInterface().getOutboundAccessControlListV6().getName():null);
		}
	}
	
	private void findCPECurrentBasicValues(PerformIPServiceActivation performIPServiceActivation,
			Map<String, Object> currentCPEMap) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_PHYSICAL_PORT,wanInterfaceType.getEthernetInterface().getPhysicalPortName());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_INTERFACE_NAME,wanInterfaceType.getEthernetInterface().getName());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_ETHERNET_VLAN,wanInterfaceType.getEthernetInterface().getVlan());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_V4_ADDRESS,Objects.nonNull(wanInterfaceType.getEthernetInterface().getV4IpAddress())?wanInterfaceType.getEthernetInterface().getV4IpAddress().getAddress():null);
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_V6_ADDRESS,Objects.nonNull(wanInterfaceType.getEthernetInterface().getV6IpAddress())?wanInterfaceType.getEthernetInterface().getV6IpAddress().getAddress():null);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_PHYSICAL_PORT,wanInterfaceType.getSerialInterface().getPhysicalPortName());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_INTERFACE_NAME,wanInterfaceType.getSerialInterface().getName());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_V4_ADDRESS,Objects.nonNull(wanInterfaceType.getSerialInterface().getV4IpAddress())?wanInterfaceType.getSerialInterface().getV4IpAddress().getAddress():null);
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_V6_ADDRESS,Objects.nonNull(wanInterfaceType.getSerialInterface().getV6IpAddress())?wanInterfaceType.getSerialInterface().getV6IpAddress().getAddress():null);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_PHYSICAL_PORT,wanInterfaceType.getChannelizedSDHInterface().getPhysicalPortName());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_INTERFACE_NAME,wanInterfaceType.getChannelizedSDHInterface().getName());
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_V4_ADDRESS,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getV4IpAddress())?wanInterfaceType.getChannelizedSDHInterface().getV4IpAddress().getAddress():null);
			currentCPEMap.put(AceConstants.CPE.CPE_CURRENT_V6_ADDRESS,Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface().getV6IpAddress())?wanInterfaceType.getChannelizedSDHInterface().getV6IpAddress().getAddress():null);
		}
	}
	
	private void findRouterBasicValues(ServiceDetail serviceDetail, Map<String, Object> map) {
		List<InterfaceProtocolMapping> routerProtocolMappings = serviceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> Objects.nonNull(serIn.getRouterDetail()))
				.collect(Collectors.toList());
		if (!routerProtocolMappings.isEmpty()) {
			routerProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping routerProtocolMapping = routerProtocolMappings.get(0);
			if (Objects.nonNull(routerProtocolMapping)) {
				map.put(AceConstants.ROUTER.ROUTER_HOST_NAME,findRouterHostName(routerProtocolMapping));
				map.put(AceConstants.ROUTER.ROUTER_MGMT_V4_ADDRESS,findRouterMgmtIpv4Address(routerProtocolMapping));
				findRouterInterfaceBasicValues(routerProtocolMapping,map);
			}
		}	
	}

	private void findRouterInterfaceBasicValues(InterfaceProtocolMapping routerProtocolMapping, Map<String, Object> map) {
		if (Objects.nonNull(routerProtocolMapping.getChannelizedE1serialInterface())) {
			map.put(AceConstants.ROUTER.ROUTER_PHYSICAL_PORT,routerProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort());
			map.put(AceConstants.ROUTER.ROUTER_INTERFACE_NAME,routerProtocolMapping.getChannelizedE1serialInterface().getInterfaceName());
			map.put(AceConstants.ROUTER.ROUTER_V4_ADDRESS,routerProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv4Address());
			map.put(AceConstants.ROUTER.ROUTER_V6_ADDRESS,routerProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv6Address());
			map.put(AceConstants.ROUTER.ROUTER_CHANNEL_GROUP_NUMBER,routerProtocolMapping.getChannelizedE1serialInterface().getChannelGroupNumber());
			map.put(AceConstants.ROUTER.ROUTER_FIRST_TIME_SLOT,routerProtocolMapping.getChannelizedE1serialInterface().getFirsttimeSlot());
			map.put(AceConstants.ROUTER.ROUTER_LAST_TIME_SLOT,routerProtocolMapping.getChannelizedE1serialInterface().getLasttimeSlot());
			if(Objects.nonNull(routerProtocolMapping.getChannelizedE1serialInterface().getAclPolicyCriterias())){
				findAclPolicyCriteria(routerProtocolMapping.getChannelizedE1serialInterface().getAclPolicyCriterias(),map);
			}
		} else if (Objects.nonNull(routerProtocolMapping.getEthernetInterface())) {
			map.put(AceConstants.ROUTER.ROUTER_PHYSICAL_PORT,routerProtocolMapping.getEthernetInterface().getPhysicalPort());
			map.put(AceConstants.ROUTER.ROUTER_INTERFACE_NAME,routerProtocolMapping.getEthernetInterface().getInterfaceName());
			map.put(AceConstants.ROUTER.ROUTER_V4_ADDRESS,routerProtocolMapping.getEthernetInterface().getModifiedIpv4Address());
			map.put(AceConstants.ROUTER.ROUTER_V6_ADDRESS,routerProtocolMapping.getEthernetInterface().getModifiedIpv6Address());
			if(Objects.nonNull(routerProtocolMapping.getEthernetInterface().getInnerVlan()) 
					&& !routerProtocolMapping.getEthernetInterface().getInnerVlan().isEmpty()){
				map.put(AceConstants.ROUTER.ROUTER_ETHERNET_VLAN,routerProtocolMapping.getEthernetInterface().getInnerVlan());
			}
			if(Objects.nonNull(routerProtocolMapping.getEthernetInterface().getAclPolicyCriterias())){
				findAclPolicyCriteria(routerProtocolMapping.getEthernetInterface().getAclPolicyCriterias(),map);
			}
		} else if (Objects.nonNull(routerProtocolMapping.getChannelizedSdhInterface())) {
			map.put(AceConstants.ROUTER.ROUTER_PHYSICAL_PORT,routerProtocolMapping.getChannelizedSdhInterface().getPhysicalPort());
			map.put(AceConstants.ROUTER.ROUTER_INTERFACE_NAME,routerProtocolMapping.getChannelizedSdhInterface().getInterfaceName());
			map.put(AceConstants.ROUTER.ROUTER_V4_ADDRESS,routerProtocolMapping.getChannelizedSdhInterface().getModifiedIpv4Address());
			map.put(AceConstants.ROUTER.ROUTER_V6_ADDRESS,routerProtocolMapping.getChannelizedSdhInterface().getModifiedIipv6Address());
			map.put(AceConstants.ROUTER.ROUTER_CHANNEL_GROUP_NUMBER,routerProtocolMapping.getChannelizedSdhInterface().getChannelGroupNumber());
			map.put(AceConstants.ROUTER.ROUTER_FIRST_TIME_SLOT,routerProtocolMapping.getChannelizedSdhInterface().get_4Kfirsttime_slot());
			map.put(AceConstants.ROUTER.ROUTER_LAST_TIME_SLOT,routerProtocolMapping.getChannelizedSdhInterface().get_4klasttimeSlot());
			if(Objects.nonNull(routerProtocolMapping.getChannelizedSdhInterface().getAclPolicyCriterias())){
				findAclPolicyCriteria(routerProtocolMapping.getChannelizedSdhInterface().getAclPolicyCriterias(),map);
			}
		}
	}
	
	private void findAclPolicyCriteria(Set<AclPolicyCriteria> apcs, Map<String, Object> map) {
		apcs.stream().forEach(apc ->{
			if (Objects.nonNull(apc.getInboundIpv4AclName())) {
				map.put(AceConstants.ROUTER.ROUTER_INBOUND_V4,apc.getInboundIpv4AclName());
			}
			if (Objects.nonNull(apc.getInboundIpv6AclName())) {
				map.put(AceConstants.ROUTER.ROUTER_INBOUND_V6,apc.getInboundIpv6AclName());
			}
			if (Objects.nonNull(apc.getOutboundIpv4AclName())) {
				map.put(AceConstants.ROUTER.ROUTER_OUTBOUND_V4,apc.getOutboundIpv4AclName());
			}
			if (Objects.nonNull(apc.getOutboundIpv6AclName())) {
				map.put(AceConstants.ROUTER.ROUTER_OUTBOUND_V6,apc.getOutboundIpv6AclName());
			}
		});	
	}

	private String findRouterMgmtIpv4Address(InterfaceProtocolMapping routerProtocolMapping) {
		return routerProtocolMapping.getRouterDetail().getIpv4MgmtAddress();
	}

	private String findRouterHostName(InterfaceProtocolMapping routerProtocolMapping) {
		return routerProtocolMapping.getRouterDetail().getRouterHostname();
	}


	
	private boolean isValueExists(Map<String,Object> map, String key){
		return map.containsKey(key) && Objects.nonNull(map.get(key));
	}
	
	private void findCPEBasicValues(ServiceDetail serviceDetail, Map<String, Object> map) {
		List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> Objects.nonNull(serIn.getCpe())
						&& Objects.nonNull(serIn.getIscpeWanInterface()) && serIn.getIscpeWanInterface() == 1)
				.collect(Collectors.toList());
		if (!cpeProtocolMappings.isEmpty()) {
			cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);
			if (Objects.nonNull(cpeProtocolMapping)) {
				map.put(AceConstants.CPE.CPE_HOST_NAME,findCpeHostName(cpeProtocolMapping));
				map.put(AceConstants.CPE.CPE_SNP_SERVER_COMMUNITY,findCpeSnmpServerCommunity(cpeProtocolMapping));
				findCpeInterfaceBasicValues(cpeProtocolMapping,map);
			}
		}
	}
	
	private void findCpeInterfaceBasicValues(InterfaceProtocolMapping cpeProtocolMapping, Map<String, Object> map) {
		if (Objects.nonNull(cpeProtocolMapping.getChannelizedE1serialInterface())) {
			map.put(AceConstants.CPE.CPE_PHYSICAL_PORT,cpeProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort());
			map.put(AceConstants.CPE.CPE_INTERFACE_NAME,cpeProtocolMapping.getChannelizedE1serialInterface().getInterfaceName());
			map.put(AceConstants.CPE.CPE_V4_ADDRESS,cpeProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv4Address());
			map.put(AceConstants.CPE.CPE_V6_ADDRESS,cpeProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv6Address());
		} else if (Objects.nonNull(cpeProtocolMapping.getEthernetInterface())) {
			map.put(AceConstants.CPE.CPE_PHYSICAL_PORT,cpeProtocolMapping.getEthernetInterface().getPhysicalPort());
			map.put(AceConstants.CPE.CPE_INTERFACE_NAME,cpeProtocolMapping.getEthernetInterface().getInterfaceName());
			map.put(AceConstants.CPE.CPE_V4_ADDRESS,cpeProtocolMapping.getEthernetInterface().getModifiedIpv4Address());
			map.put(AceConstants.CPE.CPE_V6_ADDRESS,cpeProtocolMapping.getEthernetInterface().getModifiedIpv6Address());
			if(Objects.nonNull(cpeProtocolMapping.getEthernetInterface().getInnerVlan()) 
					&& !cpeProtocolMapping.getEthernetInterface().getInnerVlan().isEmpty()){
				map.put(AceConstants.CPE.CPE_ETHERNET_VLAN,cpeProtocolMapping.getEthernetInterface().getInnerVlan());
			}
		} else if (Objects.nonNull(cpeProtocolMapping.getChannelizedSdhInterface())) {
			map.put(AceConstants.CPE.CPE_PHYSICAL_PORT,cpeProtocolMapping.getChannelizedSdhInterface().getPhysicalPort());
			map.put(AceConstants.CPE.CPE_INTERFACE_NAME,cpeProtocolMapping.getChannelizedSdhInterface().getInterfaceName());
			map.put(AceConstants.CPE.CPE_V4_ADDRESS,cpeProtocolMapping.getChannelizedSdhInterface().getModifiedIpv4Address());
			map.put(AceConstants.CPE.CPE_V6_ADDRESS,cpeProtocolMapping.getChannelizedSdhInterface().getModifiedIipv6Address());
		}
	}

	private String findCpeSnmpServerCommunity(InterfaceProtocolMapping cpeProtocolMapping) {
		return cpeProtocolMapping.getCpe().getSnmpServerCommunity();
	}

	private String findCpeHostName(InterfaceProtocolMapping cpeProtocolMapping) {
		return cpeProtocolMapping.getCpe().getHostName();
	}
	
	
	private void isIpAddressDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		String prevInternetGatewayv4IPAddress[]={null};
		String prevNmsServerv4IPAddress[]={null};
		prevActiveServiceDetail.getIpAddressDetails().stream().forEach(prevIpAddress -> {
			prevInternetGatewayv4IPAddress[0]=prevIpAddress.getPingAddress1();
			prevNmsServerv4IPAddress[0]=prevIpAddress.getNmsServiceIpv4Address();
		});
		String currentInternetGatewayv4IPAddress =null;
		String currentNmsServerv4IPAddress =null;
		GVPNService gvpnService=performIPServiceActivation.getOrderDetails().getService().getGvpnService();
		if(Objects.nonNull(gvpnService)){
			LOGGER.info("isIpAddressDetailModified");
			currentInternetGatewayv4IPAddress=Objects.nonNull(gvpnService.getPingV4IPAddress())?gvpnService.getPingV4IPAddress().getAddress():null;
			currentNmsServerv4IPAddress=Objects.nonNull(gvpnService.getNmsServerv4IPAddress())?gvpnService.getNmsServerv4IPAddress().getAddress():null;
		}
		validateIsIpAddressDetailModified(prevInternetGatewayv4IPAddress,currentInternetGatewayv4IPAddress,prevNmsServerv4IPAddress,currentNmsServerv4IPAddress,performIPServiceActivation);
	}
	
	private void validateIsIpAddressDetailModified(String[] prevInternetGatewayv4IPAddress,
			String currentInternetGatewayv4IPAddress, String[] prevNmsServerv4IPAddress,
			String currentNmsServerv4IPAddress, PerformIPServiceActivation performIPServiceActivation) {
		boolean isInternetGatewayv4Modified=compareStrings(prevInternetGatewayv4IPAddress[0],currentInternetGatewayv4IPAddress);
		boolean isNmsServerv4Modified=compareStrings(prevNmsServerv4IPAddress[0],currentNmsServerv4IPAddress);
			if(isInternetGatewayv4Modified || isNmsServerv4Modified){
				LOGGER.info("validateIsIpAddressDetailModified Either One");
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			}
			if(isInternetGatewayv4Modified && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getPingV4IPAddress())){
				LOGGER.info("isInternetGatewayv4Modified");
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getPingV4IPAddress().setIsObjectModifiedInstance(true);
			}
			if(isNmsServerv4Modified && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getNmsServerv4IPAddress())){
				LOGGER.info("isNmsServerv4Modified");
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getNmsServerv4IPAddress().setIsObjectModifiedInstance(true);
			}
	}

	private void isSolutionTableDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail,ServiceDetail currentServiceDetail) {
		String prevVpnName[]={null};
		String prevVpnTopology[]={null};
		String prevVpnType[]={null};
		String prevVpnSiteId[]={null};
		String prevVpnLegRole[]={null};
		String prevVpnLegId[]={null};
		if(Objects.nonNull(prevActiveServiceDetail.getVpnSolutions())){
			Optional<VpnSolution> prevVpnSolutionOptional=prevActiveServiceDetail.getVpnSolutions().stream().findFirst();
			if(prevVpnSolutionOptional.isPresent()){
				VpnSolution prevVpnSolution=prevVpnSolutionOptional.get();
				if(Objects.isNull(prevVpnSolution.getVpnType())){
					prevVpnSolution.setVpnType("CUSTOMER");
					vpnSolutionRepository.save(prevVpnSolution);
				}
			}
			prevActiveServiceDetail.getVpnSolutions().stream().filter(prevVpnSoln -> 
				prevVpnSoln.getVpnType().equalsIgnoreCase("CUSTOMER")).forEach(prevVpnSoln ->{
					prevVpnName[0]=prevVpnSoln.getVpnName();
					prevVpnTopology[0]=prevVpnSoln.getVpnTopology();
					prevVpnType[0]=prevVpnSoln.getVpnType();
					prevVpnSiteId[0]=prevVpnSoln.getSiteId();
					prevVpnLegRole[0]=prevVpnSoln.getLegRole();
					prevVpnLegId[0]=prevVpnSoln.getVpnLegId();
			});
		}
		String currentVpnName[]={null};
		String currentVpnTopology[]={null};
		String currenVpnType[]={null};
		String currentVpnSiteId[]={null};
		String currentVpnLegRole[]={null};
		String currentVpnLegId[]={null};
		if(Objects.nonNull(currentServiceDetail.getVpnSolutions())){
			currentServiceDetail.getVpnSolutions().stream().filter(currentVpnSoln -> 
				currentVpnSoln.getVpnType().equalsIgnoreCase("CUSTOMER")).forEach(currentVpnSoln ->{
				currentVpnName[0]=currentVpnSoln.getVpnName();
				currentVpnTopology[0]=currentVpnSoln.getVpnTopology();
				currenVpnType[0]=currentVpnSoln.getVpnType();
				currentVpnSiteId[0]=currentVpnSoln.getSiteId();
				currentVpnLegRole[0]=currentVpnSoln.getLegRole();
				currentVpnLegId[0]=currentVpnSoln.getVpnLegId();
			});
		}
		boolean isVpnNameModified=compareStrings(prevVpnName[0], currentVpnName[0]);
		boolean isVpnTopologyModified=compareStrings(prevVpnTopology[0], currentVpnTopology[0]);
		boolean isVpnTypeModified=compareStrings(prevVpnType[0], currenVpnType[0]);
		boolean isVpnSiteIdModified=compareStrings(prevVpnSiteId[0], currentVpnSiteId[0]);
		boolean isVpnLegRoleModified=compareStrings(prevVpnLegRole[0], currentVpnLegRole[0]);
		boolean isVpnLegIdModified=compareStrings(prevVpnLegId[0], currentVpnLegId[0]);
		GVPNService gvpnService=performIPServiceActivation.getOrderDetails().getService().getGvpnService();
		if(isVpnNameModified || isVpnTopologyModified || isVpnTypeModified){
			if(Objects.nonNull(gvpnService.getSolutionTable()) && Objects.nonNull(gvpnService.getSolutionTable().getVPN())) {
				gvpnService.getSolutionTable().getVPN().stream().filter(vpn -> vpn.getVpnType().equals("CUSTOMER")).forEach(vpn ->{
					vpn.setIsObjectInstanceModified(true);
				});
			}
		}
		if(isVpnSiteIdModified || isVpnLegRoleModified){
			if(Objects.nonNull(gvpnService.getSolutionTable()) && Objects.nonNull(gvpnService.getSolutionTable().getVPN())) {
				gvpnService.getSolutionTable().getVPN().stream().filter(vpn -> vpn.getVpnType().equals("CUSTOMER")).forEach(vpn ->{
					vpn.getLeg().stream().forEach(vpnLeg -> {
						vpnLeg.setIsObjectInstanceModified("true");
					});
				});
			}
		}
		if(isVpnLegIdModified){
			LOGGER.info("isSolutionTableDetailModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
		}
	}

	private void isRouterUniSwitchHandOffModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		String prevRouterUniSwitchHandoff[]={null};
		getPreviousRouterUniSwitch(prevActiveServiceDetail,prevRouterUniSwitchHandoff);
		String currentRouterUniSwitchHandoff[]={null};
		getCurrentRouterUniSwitch(currentServiceDetail,currentRouterUniSwitchHandoff);
		validateIsRouterUniSwitchHandOffModified(prevRouterUniSwitchHandoff[0],currentRouterUniSwitchHandoff[0],performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
	}

	private void getPreviousRouterUniSwitch(ServiceDetail prevActiveServiceDetail,
			String[] prevRouterUniSwitchHandoff) {
		prevActiveServiceDetail.getTopologies().stream().forEach(prevTopology ->{
			if(Objects.nonNull(prevTopology.getUniswitchDetails()) && !prevTopology.getUniswitchDetails().isEmpty()){
				prevTopology.getUniswitchDetails().stream().forEach(prevUniswitch ->{
					prevRouterUniSwitchHandoff[0]=prevUniswitch.getHandoff();
				});
			}
		});
	}

	private void getCurrentRouterUniSwitch(ServiceDetail currentServiceDetail, String[] currentRouterUniSwitchHandoff) {
		currentServiceDetail.getTopologies().stream().forEach(currentTopology ->{
			if(Objects.nonNull(currentTopology.getUniswitchDetails()) && !currentTopology.getUniswitchDetails().isEmpty()){
				currentTopology.getUniswitchDetails().stream().forEach(currentUniswitch ->{
					currentRouterUniSwitchHandoff[0]=currentUniswitch.getHandoff();
				});
			}
		});
	}

	private void validateIsRouterUniSwitchHandOffModified(String prevRouterUniSwitchHandoff, String currentRouterUniSwitchHandoff,PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		if((Objects.nonNull(prevRouterUniSwitchHandoff) && Objects.nonNull(currentRouterUniSwitchHandoff)) 
				&& ((!prevRouterUniSwitchHandoff.equalsIgnoreCase("NNI") && !currentRouterUniSwitchHandoff.equalsIgnoreCase("NNI"))
						|| (prevRouterUniSwitchHandoff.equalsIgnoreCase("NNI") && !currentRouterUniSwitchHandoff.equalsIgnoreCase("NNI"))
						|| (!prevRouterUniSwitchHandoff.equalsIgnoreCase("NNI") && currentRouterUniSwitchHandoff.equalsIgnoreCase("NNI")))){
			boolean isUniSwitchHandOffModified=compareString(prevRouterUniSwitchHandoff,currentRouterUniSwitchHandoff);
			if(isUniSwitchHandOffModified){
				LOGGER.info("validateIsRouterUniSwitchHandOffModified");
				//Validate isRouterTopology modified
				isRouterTopologyModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
				//Validate isRouterTopologyUpLinkPort modified
				isRouterUpLinkPortModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
				//Validate isRouterUniSwitchDetail modified
				isRouterUniSwitchDetailModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
			}
		}
	}

	private void isRouterStaticRouteDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		List<String> prevIpSubnetList=null;
		List<String> prevHopIdList=null;
		Set<InterfaceProtocolMapping> prevInterfaceProtocalMappingSet=prevActiveServiceDetail.getInterfaceProtocolMappings();
		for(InterfaceProtocolMapping prevIPM:prevInterfaceProtocalMappingSet){
			if(Objects.nonNull(prevIPM.getRouterDetail()) 
					&& Objects.nonNull(prevIPM.getStaticProtocol())
					&& Objects.nonNull(prevIPM.getStaticProtocol().getWanStaticRoutes())){
				prevIpSubnetList= new ArrayList<>();
				prevHopIdList= new ArrayList<>();
				for(WanStaticRoute wanStaticRoute:prevIPM.getStaticProtocol().getWanStaticRoutes()){
					prevIpSubnetList.add(wanStaticRoute.getIpsubnet());
					prevHopIdList.add(wanStaticRoute.getNextHopid());
				}
			}
		}
		
		List<String> currentIpSubnetList=null;
		List<String> currentHopIdList=null;
		Set<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet=currentServiceDetail.getInterfaceProtocolMappings();
		for(InterfaceProtocolMapping currentIPM:currentInterfaceProtocalMappingSet){
			if(Objects.nonNull(currentIPM.getRouterDetail())
					&& Objects.nonNull(currentIPM.getStaticProtocol()) 
					&& Objects.nonNull(currentIPM.getStaticProtocol().getWanStaticRoutes())){
				currentIpSubnetList= new ArrayList<>();
				currentHopIdList= new ArrayList<>();
				for(WanStaticRoute wanStaticRoute:currentIPM.getStaticProtocol().getWanStaticRoutes()){
					currentIpSubnetList.add(wanStaticRoute.getIpsubnet());
					currentHopIdList.add(wanStaticRoute.getNextHopid());
				}
			}
		}
		boolean isIpSubnetModified=compareCollection(prevIpSubnetList, currentIpSubnetList);
		boolean isHopIdModified=compareCollection(prevHopIdList, currentHopIdList);
		if(isIpSubnetModified
				|| isHopIdModified){
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol())){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().setIsObjectInstanceModified(true);
				if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().getPEWANStaticRoutes())){
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().getPEWANStaticRoutes().setIsObjectInstanceModified(true);
				}
			}
		}
	}

	private void isRouterStaticRouteSizeModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		Integer prevWanStaticRouteSize[]={null};
		Set<InterfaceProtocolMapping> prevInterfaceProtocalMappingSet=prevActiveServiceDetail.getInterfaceProtocolMappings();
		prevInterfaceProtocalMappingSet.stream().filter(prevIPM -> 
		Objects.nonNull(prevIPM.getRouterDetail()) 
				&& Objects.nonNull(prevIPM.getStaticProtocol()) 
				&& Objects.nonNull(prevIPM.getStaticProtocol().getWanStaticRoutes())).forEach(prevIPM ->{
				prevWanStaticRouteSize[0]=prevIPM.getStaticProtocol().getWanStaticRoutes().size();
		});
		Integer currentWanStaticRouteSize[]={null};
		Set<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet=currentServiceDetail.getInterfaceProtocolMappings();
		currentInterfaceProtocalMappingSet.stream().filter(currentIPM -> 
		Objects.nonNull(currentIPM.getRouterDetail())
				&& Objects.nonNull(currentIPM.getStaticProtocol())
				&& Objects.nonNull(currentIPM.getStaticProtocol().getWanStaticRoutes())).forEach(currentIPM ->{
					currentWanStaticRouteSize[0]=currentIPM.getStaticProtocol().getWanStaticRoutes().size();
		});
		boolean isWanStaticRouteModified=compareIntegers(prevWanStaticRouteSize[0], currentWanStaticRouteSize[0]);
		if(isWanStaticRouteModified){
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol())){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().setIsObjectInstanceModified(true);
			}
		}
	}
	
	private void isRouterIpDetailModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map,Map<String,Object> currentMap) {
		String prevIpv4Address=null;
		String prevIpv6Address=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_V4_ADDRESS)){
			prevIpv4Address=(String) map.get(AceConstants.ROUTER.ROUTER_V4_ADDRESS);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_V6_ADDRESS)){
			prevIpv6Address=(String) map.get(AceConstants.ROUTER.ROUTER_V6_ADDRESS);
		}
		
		String currentIpv4Address=null;
		String currentIpv6Address=null;
		if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_V4_ADDRESS)){
				currentIpv4Address=(String) currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_V4_ADDRESS);
		}
		if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_V6_ADDRESS)){
				currentIpv6Address=(String) currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_V6_ADDRESS);
		}
		validateIsRouterIpDetailModified(prevIpv4Address,prevIpv6Address,currentIpv4Address,currentIpv6Address,performIPServiceActivation);
	}

	private void validateIsRouterIpDetailModified(String prevIpv4Address, String prevIpv6Address,
			String currentIpv4Address, String currentIpv6Address,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isIpv4Modified=false;
		boolean isIpv4NullModified=false;
		if((compareNonNull(prevIpv4Address,currentIpv4Address))){
			isIpv4Modified=compareString(prevIpv4Address,currentIpv4Address);
			isIpv4NullModified=compareNull(prevIpv4Address,currentIpv4Address);
		}
		boolean isIpv6Modified=false;
		boolean isIpv6NullModified=false;
		if((compareNonNull(prevIpv4Address,currentIpv4Address))){
			isIpv6Modified=compareString(getIpAddress(prevIpv6Address),getIpAddress(currentIpv6Address));
			isIpv6NullModified=compareNull(prevIpv6Address,currentIpv6Address);
		}
		if(isIpv4Modified || isIpv4NullModified
				|| isIpv6Modified ||isIpv6NullModified){
			LOGGER.info("validateIsRouterIpDetailModified Either One");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
			updateRouterInterfaceModified(performIPServiceActivation);
		}
		if(isIpv4Modified){
			LOGGER.info("isIpv4Modified");
			updateRouterInterfaceIpv4Modified(performIPServiceActivation);
		}
		if(isIpv6Modified){
			LOGGER.info("isIpv6Modified");
			updateRouterInterfaceIpv6Modified(performIPServiceActivation);
		}
	}

	private void updateRouterInterfaceIpv6Modified(PerformIPServiceActivation performIPServiceActivation) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			LOGGER.info("updateRouterInterfaceIpv6Modified: Ethernet");
			wanInterfaceType.getEthernetInterface().getV6IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			LOGGER.info("updateRouterInterfaceIpv6Modified: Serial");
			wanInterfaceType.getSerialInterface().getV6IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			LOGGER.info("updateRouterInterfaceIpv6Modified: SDH");
			wanInterfaceType.getChannelizedSDHInterface().getV6IpAddress().setIsObjectModifiedInstance(true);
		}
	}

	private void updateRouterInterfaceIpv4Modified(PerformIPServiceActivation performIPServiceActivation) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			LOGGER.info("updateRouterInterfaceIpv4Modified: Ethernet");
			wanInterfaceType.getEthernetInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			LOGGER.info("updateRouterInterfaceIpv4Modified: Serial");
			wanInterfaceType.getSerialInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			LOGGER.info("updateRouterInterfaceIpv4Modified: SDH");
			wanInterfaceType.getChannelizedSDHInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
		}
	}

	private void isCPEIpDetailModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map, Map<String, Object> currentCPEMap) {
		String prevIpv4Address=null;
		String prevIpv6Address=null;
		if(isValueExists(map,AceConstants.CPE.CPE_V4_ADDRESS)){
			prevIpv4Address=(String) map.get(AceConstants.CPE.CPE_V4_ADDRESS);
		}
		if(isValueExists(map,AceConstants.CPE.CPE_V6_ADDRESS)){
			prevIpv6Address=(String) map.get(AceConstants.CPE.CPE_V6_ADDRESS);
		}
		String currentIpv4Address=null;
		String currentIpv6Address=null;
		boolean isCpeManaged=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		if(isCpeManaged){
			if(isValueExists(currentCPEMap,AceConstants.CPE.CPE_CURRENT_V4_ADDRESS)){
				currentIpv4Address=(String) currentCPEMap.get(AceConstants.CPE.CPE_CURRENT_V4_ADDRESS);
			}
			if(isValueExists(currentCPEMap,AceConstants.CPE.CPE_CURRENT_V6_ADDRESS)){
				currentIpv6Address=(String) currentCPEMap.get(AceConstants.CPE.CPE_CURRENT_V6_ADDRESS);
			}
			validateisCPEIpDetailModified(prevIpv4Address,prevIpv6Address,currentIpv4Address,currentIpv6Address,performIPServiceActivation);
		}
	}

	private void validateisCPEIpDetailModified(String prevIpv4Address, String prevIpv6Address,
			String currentIpv4Address, String currentIpv6Address,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isIpv4Modified=false;
		boolean isIpv4NullModified=false;
		if((compareNonNull(prevIpv4Address,currentIpv4Address))){
			isIpv4Modified=compareString(prevIpv4Address,currentIpv4Address);
			isIpv4NullModified=compareNull(prevIpv4Address,currentIpv4Address);
		}
		boolean isIpv6Modified=false;
		boolean isIpv6NullModified=false;
		if((compareNonNull(prevIpv4Address,currentIpv4Address))){
			isIpv6Modified=compareString(getIpAddress(prevIpv6Address),getIpAddress(currentIpv6Address));
			isIpv6NullModified=compareNull(prevIpv6Address,currentIpv6Address);
		}
		if(isIpv4Modified || isIpv4NullModified
				|| isIpv6Modified ||isIpv6NullModified){
			LOGGER.info("validateisCPEIpDetailModified Either One");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsObjectInstanceModified(true);
			updateCpeInterfaceModified(performIPServiceActivation);
		}
		if(isIpv4Modified){
			LOGGER.info("isIpv4Modified");
			updateCpeInterfaceIpv4Modified(performIPServiceActivation);
		}
		if(isIpv6Modified){
			LOGGER.info("isIpv6Modified");
			updateCpeInterfaceIpv6Modified(performIPServiceActivation);
		}
	}
	
	private void updateCpeInterfaceIpv4Modified(PerformIPServiceActivation performIPServiceActivation) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			LOGGER.info("updateCpeInterfaceIpv4Modified: Ethernet");
			wanInterfaceType.getEthernetInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			LOGGER.info("updateCpeInterfaceIpv4Modified: Serial");
			wanInterfaceType.getSerialInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			LOGGER.info("updateCpeInterfaceIpv4Modified: SDH");
			wanInterfaceType.getChannelizedSDHInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
		}
	}
	
	private void updateCpeInterfaceIpv6Modified(PerformIPServiceActivation performIPServiceActivation) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			LOGGER.info("updateCpeInterfaceIpv6Modified: Ethernet");
			wanInterfaceType.getEthernetInterface().getV6IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			LOGGER.info("updateCpeInterfaceIpv6Modified: Serial");
			wanInterfaceType.getSerialInterface().getV6IpAddress().setIsObjectModifiedInstance(true);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			LOGGER.info("updateCpeInterfaceIpv6Modified: SDH");
			wanInterfaceType.getChannelizedSDHInterface().getV6IpAddress().setIsObjectModifiedInstance(true);
		}
	}

	
	private void isBgpPrefixListModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		List<Integer> prevIpv4CriteriaIds=null;
		List<Integer> prevIpv6CriteriaIds=null;
		List<PolicyCriteria> prevV4PolicyCriteriaList=null;
		List<PolicyCriteria> prevV6PolicyCriteriaList=null;
		List<String> prevV4PrefixList=null;
		List<String> prevV6PrefixList=null;
		List<InterfaceProtocolMapping> prevInterfaceProtocalMappingSet = prevActiveServiceDetail.getInterfaceProtocolMappings()
				.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
				.collect(Collectors.toList());
		//Set<InterfaceProtocolMapping> prevInterfaceProtocalMappingSet=prevActiveServiceDetail.getInterfaceProtocolMappings();
		prevIpv4CriteriaIds=getV4PolicyCriteriaIds(prevInterfaceProtocalMappingSet,prevIpv4CriteriaIds);
		if(Objects.nonNull(prevIpv4CriteriaIds)){
			prevV4PolicyCriteriaList=policyCriteriaRepository.findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(prevIpv4CriteriaIds,(byte)1);
			if(Objects.nonNull(prevV4PolicyCriteriaList)){
				prevV4PrefixList = new ArrayList<>();
				getPrexList(prevV4PolicyCriteriaList,prevV4PrefixList);
			}
		}
		prevIpv6CriteriaIds=getV6PolicyCriteriaIds(prevInterfaceProtocalMappingSet,prevIpv6CriteriaIds);
		if(Objects.nonNull(prevIpv6CriteriaIds)){
			prevV6PolicyCriteriaList=policyCriteriaRepository.findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(prevIpv6CriteriaIds,(byte)1);
			if(Objects.nonNull(prevV6PolicyCriteriaList)){
				prevV6PrefixList = new ArrayList<>();
				getPrexList(prevV6PolicyCriteriaList,prevV6PrefixList);
			}
		}
		List<Integer> currentIpv4CriteriaIds=null;
		List<Integer> currentIpv6CriteriaIds=null;
		List<PolicyCriteria> currentV4PolicyCriteriaList=null;
		List<PolicyCriteria> currentV6PolicyCriteriaList=null;
		List<String> currentV4PrefixList=null;
		List<String> currentV6PrefixList=null;
		//Set<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet=currentServiceDetail.getInterfaceProtocolMappings();
		List<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet = currentServiceDetail.getInterfaceProtocolMappings()
				.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
				.collect(Collectors.toList());
		currentIpv4CriteriaIds=getV4PolicyCriteriaIds(currentInterfaceProtocalMappingSet,currentIpv4CriteriaIds);
		if(Objects.nonNull(currentIpv4CriteriaIds)){
			currentV4PolicyCriteriaList=policyCriteriaRepository.findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(currentIpv4CriteriaIds,(byte)1);
			if(Objects.nonNull(currentV4PolicyCriteriaList)){
				currentV4PrefixList = new ArrayList<>();
				getPrexList(currentV4PolicyCriteriaList,currentV4PrefixList);
			}
		}
		currentIpv6CriteriaIds=getV6PolicyCriteriaIds(currentInterfaceProtocalMappingSet,currentIpv6CriteriaIds);
		if(Objects.nonNull(currentIpv6CriteriaIds)){
			currentV6PolicyCriteriaList=policyCriteriaRepository.findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(currentIpv6CriteriaIds,(byte)1);
			if(Objects.nonNull(currentV6PolicyCriteriaList)){
				currentV6PrefixList = new ArrayList<>();
				getPrexList(currentV6PolicyCriteriaList,currentV6PrefixList);
			}
		}
		if(!isPolicyTpeV4PrevProv(currentInterfaceProtocalMappingSet) || !isPolicyTpeV6PrevProv(currentInterfaceProtocalMappingSet)){
			LOGGER.info("Policyv4 or Policyv6");
			validateIsBgpPrefixListModified(prevV4PrefixList,currentV4PrefixList,prevV6PrefixList,currentV6PrefixList,performIPServiceActivation);
		}
	}
	
	
	private boolean isPolicyTpeV6PrevProv(List<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet) {
		for(InterfaceProtocolMapping ipm:currentInterfaceProtocalMappingSet){
			if(Objects.nonNull(ipm.getRouterDetail()) 
				&& Objects.nonNull(ipm.getBgp()) && Objects.nonNull(ipm.getBgp().getPolicyTypes()) 
					&& !ipm.getBgp().getPolicyTypes().isEmpty()){
				for(PolicyType bgpPolicyType :ipm.getBgp().getPolicyTypes()){
					if(Objects.nonNull(bgpPolicyType.getInboundRoutingIpv6Policy()) && Objects.nonNull(bgpPolicyType.getInboundIpv6Preprovisioned())
							&& bgpPolicyType.getInboundRoutingIpv6Policy()==1 && bgpPolicyType.getInboundIpv6Preprovisioned()==1){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isPolicyTpeV4PrevProv(List<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet) {
		for(InterfaceProtocolMapping ipm:currentInterfaceProtocalMappingSet){
			if(Objects.nonNull(ipm.getRouterDetail()) 
				&& Objects.nonNull(ipm.getBgp()) && Objects.nonNull(ipm.getBgp().getPolicyTypes()) 
					&& !ipm.getBgp().getPolicyTypes().isEmpty()){
				for(PolicyType bgpPolicyType :ipm.getBgp().getPolicyTypes()){
					if(Objects.nonNull(bgpPolicyType.getInboundRoutingIpv4Policy()) && Objects.nonNull(bgpPolicyType.getInboundIpv4Preprovisioned())
							&& bgpPolicyType.getInboundRoutingIpv4Policy()==1 && bgpPolicyType.getInboundIpv4Preprovisioned()==1){
						return true;
					}
				}
			}
		}
		return false;
	}

	private void validateIsBgpPrefixListModified(List<String> prevV4PrefixList, List<String> currentV4PrefixList,
			List<String> prevV6PrefixList, List<String> currentV6PrefixList,PerformIPServiceActivation performIPServiceActivation) {
		if(Objects.nonNull(prevV4PrefixList) && Objects.nonNull(currentV4PrefixList)){
			boolean isV4PrefixListModified=compareCollection(prevV4PrefixList,currentV4PrefixList);
			if(isV4PrefixListModified){
				LOGGER.info("isV4PrefixListModified");
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol())
						&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol())){
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
					if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicy())
							&& !performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicy().isEmpty() && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicy().get(0)) && 
							Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicy().get(0).getMatchCriteria())
							&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicy().get(0).getMatchCriteria().getPrefixList()))
						performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicy().get(0).getMatchCriteria().getPrefixList().setIsObjectInstanceModified(true);
				}
			}
		}
		if(Objects.nonNull(prevV6PrefixList) && Objects.nonNull(currentV6PrefixList)){
			boolean isV6PrefixListModified=compareCollection(prevV6PrefixList,currentV6PrefixList);
			if(isV6PrefixListModified){
				LOGGER.info("isV6PrefixListModified");
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol())
						&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol())){
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
					if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicyV6())
							&& !performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicyV6().isEmpty() && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicyV6().get(0)) && 
							Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicyV6().get(0).getMatchCriteria())
							&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicyV6().get(0).getMatchCriteria().getPrefixList())){
						performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().getALUBGPPeerGroupInboundRoutingPolicyV6().get(0).getMatchCriteria().getPrefixList().setIsObjectInstanceModified(true);
					}
				}
			}
		}
	}

	private void getPrexList(List<PolicyCriteria> policyCriteriaList, List<String> prefixList) {
		for(PolicyCriteria policyCriteria:policyCriteriaList){
			if(Objects.nonNull(policyCriteria.getPrefixlistConfigs()) 
					&& !policyCriteria.getPrefixlistConfigs().isEmpty()){
				for(PrefixlistConfig prefixListConfig:policyCriteria.getPrefixlistConfigs()){
					prefixList.add(prefixListConfig.getNetworkPrefix());
				}
			}
		}
	}

	private List<Integer> getV4PolicyCriteriaIds(List<InterfaceProtocolMapping> interfaceProtocalMappingSet,
			List<Integer> ipv4CriteriaIds) {
		for(InterfaceProtocolMapping ipm:interfaceProtocalMappingSet){
			if(Objects.nonNull(ipm.getRouterDetail()) 
				&& Objects.nonNull(ipm.getBgp()) && Objects.nonNull(ipm.getBgp().getPolicyTypes()) 
					&& !ipm.getBgp().getPolicyTypes().isEmpty()){
				ipv4CriteriaIds = new ArrayList<>();
				for(PolicyType bgpPolicyType :ipm.getBgp().getPolicyTypes()){
					LOGGER.info("Policy criteria exists:: {},{},{}",Objects.nonNull(bgpPolicyType.getPolicyTypeCriteriaMappings()),Objects.nonNull(bgpPolicyType.getInboundRoutingIpv4Policy()),bgpPolicyType.getPolicyTypeCriteriaMappings().size());
					if(Objects.nonNull(bgpPolicyType.getInboundRoutingIpv4Policy()) 
							&& bgpPolicyType.getInboundRoutingIpv4Policy()==1 
								&& Objects.nonNull(bgpPolicyType.getPolicyTypeCriteriaMappings())){
						getPolicyCriteriaIds(bgpPolicyType,ipv4CriteriaIds);
						LOGGER.info("v4:size: {}",ipv4CriteriaIds.size());
					}
				}
			}
		}
		return ipv4CriteriaIds;
	}
	
	private List<Integer> getV6PolicyCriteriaIds(List<InterfaceProtocolMapping> interfaceProtocalMappingSet,
			List<Integer> ipv6CriteriaIds) {
		for(InterfaceProtocolMapping ipm:interfaceProtocalMappingSet){
			if(Objects.nonNull(ipm.getRouterDetail()) 
				&& Objects.nonNull(ipm.getBgp()) && Objects.nonNull(ipm.getBgp().getPolicyTypes()) 
					&& !ipm.getBgp().getPolicyTypes().isEmpty()){
				ipv6CriteriaIds = new ArrayList<>();
				for(PolicyType bgpPolicyType :ipm.getBgp().getPolicyTypes()){
					if(Objects.nonNull(bgpPolicyType.getInboundRoutingIpv6Policy()) 
							&& bgpPolicyType.getInboundRoutingIpv6Policy()==1
							 && Objects.nonNull(bgpPolicyType.getPolicyTypeCriteriaMappings())){
						getPolicyCriteriaIds(bgpPolicyType,ipv6CriteriaIds);
						LOGGER.info("v6:size: {}",ipv6CriteriaIds.size());
					}
				}
			}
		}
		return ipv6CriteriaIds;
	}

	private void getPolicyCriteriaIds(PolicyType bgpPolicyType, List<Integer> criteriaIds) {
		for(PolicyTypeCriteriaMapping policTypeCriteriaMap:bgpPolicyType.getPolicyTypeCriteriaMappings()){
			criteriaIds.add(policTypeCriteriaMap.getPolicyCriteriaId());
		}
	}

	private void isBgpDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		List<InterfaceProtocolMapping> prevInterfaceProtocalMappingList = prevActiveServiceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> Objects.nonNull(serIn.getRouterDetail()) && Objects.nonNull(serIn.getBgp()))
				.collect(Collectors.toList());
		List<InterfaceProtocolMapping> currentInterfaceProtocalMappingList = currentServiceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> Objects.nonNull(serIn.getRouterDetail()) && Objects.nonNull(serIn.getBgp()))
				.collect(Collectors.toList());
		if(compareNonNull(prevInterfaceProtocalMappingList, currentInterfaceProtocalMappingList) 
				&& (!prevInterfaceProtocalMappingList.isEmpty() && !currentInterfaceProtocalMappingList.isEmpty())){
			InterfaceProtocolMapping prevIPM=prevInterfaceProtocalMappingList.get(0);
			InterfaceProtocolMapping currentIPM=currentInterfaceProtocalMappingList.get(0);
			if(compareNonNull(prevIPM.getBgp(),currentIPM.getBgp())){
				if(compareIntegers(prevIPM.getBgp().getRemoteAsNumber(), currentIPM.getBgp().getRemoteAsNumber()) || compareStrings(prevIPM.getBgp().getLocalAsNumber(), currentIPM.getBgp().getLocalAsNumber()) || compareStrings(prevIPM.getBgp().getNeighborOn(),currentIPM.getBgp().getNeighborOn())
						|| compareStrings(prevIPM.getBgp().getRoutesExchanged(),currentIPM.getBgp().getRoutesExchanged()) || compareStrings(prevIPM.getBgp().getRemoteIpv4Address(),currentIPM.getBgp().getRemoteIpv4Address())|| compareStrings(prevIPM.getBgp().getRemoteIpv6Address(),currentIPM.getBgp().getRemoteIpv6Address())
					|| compareStrings(prevIPM.getBgp().getLocalPreference(),currentIPM.getBgp().getLocalPreference()) || compareStrings(prevIPM.getBgp().getV6LocalPreference(),currentIPM.getBgp().getV6LocalPreference()) || compareStrings(prevIPM.getBgp().getMaxPrefix(),currentIPM.getBgp().getMaxPrefix())
					|| compareBytes(prevIPM.getBgp().getRedistributeConnectedEnabled(), currentIPM.getBgp().getRedistributeConnectedEnabled()) || compareBytes(prevIPM.getBgp().getRedistributeStaticEnabled(), currentIPM.getBgp().getRedistributeStaticEnabled()) || compareBytes(prevIPM.getBgp().getSplitHorizon(), currentIPM.getBgp().getSplitHorizon())
					|| compareStrings(prevIPM.getBgp().getNeighbourCommunity(),currentIPM.getBgp().getNeighbourCommunity())){
						LOGGER.info("isBgpDetailModified");
						performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
						performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
						performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
					}
			}
		}
	}

	private void isServiceIdModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		String prevServiceId=prevActiveServiceDetail.getServiceId();
		String currentServiceId=performIPServiceActivation.getOrderDetails().getServiceId();
		boolean isServiceIdModified=compareStrings(prevServiceId,currentServiceId);
		if(isServiceIdModified){
			LOGGER.info("isServiceIdModified");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
		}
	}
	
	private void isExtendedLANModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		Integer prevLANValue[]={null};
		prevActiveServiceDetail.getIpAddressDetails().stream().forEach(prevIpAddress -> {
			prevLANValue[0]=Objects.nonNull(prevIpAddress.getExtendedLanEnabled())?Integer.valueOf(prevIpAddress.getExtendedLanEnabled()):0;
		});
		ExtendedLAN currentExtendedLAN=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getExtendedLAN();
		Integer currentLANValue=null;
		if(Objects.nonNull(currentExtendedLAN)){
			Boolean currentBoolLANValue=currentExtendedLAN.isIsEnabled();
			if(Objects.nonNull(currentBoolLANValue)){
				currentLANValue=(currentBoolLANValue) ? 1 : 0;
			}
		}
		boolean isExtendLANModified=compareIntegers(prevLANValue[0],currentLANValue);
		if((Objects.nonNull(prevLANValue[0]) && Objects.nonNull(currentLANValue)) 
				&& (prevLANValue[0]==1 && currentLANValue==0)){
			Set<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet=currentServiceDetail.getInterfaceProtocolMappings();
			currentInterfaceProtocalMappingSet.stream().filter(currentIPM -> 
				Objects.nonNull(currentIPM.getRouterDetail())).forEach(currentIPM ->{
						if (Objects.nonNull(currentIPM.getBgp())) {
							performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
						}else if (Objects.nonNull(currentIPM.getOspf())) {
							performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol().setIsObjectInstanceModified(true);
						}else if (Objects.nonNull(currentIPM.getStaticProtocol())) {
							performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().setIsObjectInstanceModified(true);
						}
			});
		}
		if(isExtendLANModified){
			LOGGER.info("isExtendedLANModified");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
		}
		if((Objects.isNull(prevLANValue[0]) && Objects.isNull(currentLANValue))
				|| (prevLANValue[0]==0 && currentLANValue==0)){
			LOGGER.info("isExtendedLANModified Either One");
			//Validate isRouterProtocols modified
			isRouterProtocolsModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
			//Validate isRouterStaticRouteSize modified
			isRouterStaticRouteSizeModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
			//Validate isRouterStaticRouteDetail modified
			isRouterStaticRouteDetailModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
			//Validate isBgpDetail modified
			isBgpDetailModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
			//Validate isBgpPrefixList modified
			isBgpPrefixListModified(performIPServiceActivation,prevActiveServiceDetail,currentServiceDetail);
		}
	}

	private void isRouterProtocolsModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		Integer prevBgpId[]={null};
		String prevBgpInboundV4RouteMapName[]={null};
		String prevBgpInboundV6RouteMapName[]={null};
		Integer prevOspfId[]={null};
		String prevOspfReditributeRouteMapName[]={null};
		Integer prevStaticId[]={null};
		String prevStaticReditributeRouteMapName[]={null};
		Set<InterfaceProtocolMapping> prevInterfaceProtocalMappingSet=prevActiveServiceDetail.getInterfaceProtocolMappings();
		prevInterfaceProtocalMappingSet.stream().filter(prevIPM -> 
			Objects.nonNull(prevIPM.getRouterDetail())).forEach(prevIPM ->{
					if (Objects.nonNull(prevIPM.getBgp())) {
						prevBgpId[0]=prevIPM.getBgp().getBgpId();
						prevBgpInboundV4RouteMapName[0]=prevIPM.getBgp().getBgpneighbourinboundv4routermapname();
						prevBgpInboundV6RouteMapName[0]=prevIPM.getBgp().getBgpneighbourinboundv6routermapname();
					}else if (Objects.nonNull(prevIPM.getOspf())) {
						prevOspfId[0]=prevIPM.getOspf().getOspfId();
						prevOspfReditributeRouteMapName[0]=prevIPM.getOspf().getRedistributeRoutermapname();
					}else if (Objects.nonNull(prevIPM.getStaticProtocol())) {
						prevStaticId[0]=prevIPM.getStaticProtocol().getStaticprotocolId();
						prevStaticReditributeRouteMapName[0]=prevIPM.getStaticProtocol().getRedistributeRoutemapIpv4();
					}
		});
		
		Integer currentBgpId[]={null};
		String currentBgpInboundV4RouteMapName[]={null};
		String currentBgpInboundV6RouteMapName[]={null};
		Integer currentOspfId[]={null};
		String currentOspfReditributeRouteMapName[]={null};
		Integer currentStaticId[]={null};
		String currentStaticReditributeRouteMapName[]={null};
		Set<InterfaceProtocolMapping> currentInterfaceProtocalMappingSet=currentServiceDetail.getInterfaceProtocolMappings();
		currentInterfaceProtocalMappingSet.stream().filter(currentIPM -> 
			Objects.nonNull(currentIPM.getRouterDetail())).forEach(currentIPM ->{
					if (Objects.nonNull(currentIPM.getBgp())) {
						currentBgpId[0]=currentIPM.getBgp().getBgpId();
						currentBgpInboundV4RouteMapName[0]=currentIPM.getBgp().getBgpneighbourinboundv4routermapname();
						currentBgpInboundV6RouteMapName[0]=currentIPM.getBgp().getBgpneighbourinboundv6routermapname();
					}else if (Objects.nonNull(currentIPM.getOspf())) {
						currentOspfId[0]=currentIPM.getOspf().getOspfId();
						currentOspfReditributeRouteMapName[0]=currentIPM.getOspf().getRedistributeRoutermapname();
					}else if (Objects.nonNull(currentIPM.getStaticProtocol())) {
						currentStaticId[0]=currentIPM.getStaticProtocol().getStaticprotocolId();
						currentStaticReditributeRouteMapName[0]=currentIPM.getStaticProtocol().getRedistributeRoutemapIpv4();
					}
		});
		
		boolean isBgpNullModified=compareNull(prevBgpId[0],currentBgpId[0]);
		//boolean isBgpModified=compareInteger(prevBgpId[0],currentBgpId[0]);
		boolean isBgpInBoundV4RouteNameModified=compareStrings(prevBgpInboundV4RouteMapName[0],currentBgpInboundV4RouteMapName[0]);
		boolean isBgpInBoundV6RouteNameModified=compareStrings(prevBgpInboundV6RouteMapName[0],currentBgpInboundV6RouteMapName[0]);
		boolean isOspfNullModified=compareNull(prevOspfId[0],currentOspfId[0]);
		/*boolean isOspfModified=false;
		if(!isOspfNullModified){
			isOspfModified=compareInteger(prevOspfId[0],currentOspfId[0]);
		}*/
		boolean isOspfRouteNameModified=compareStrings(prevOspfReditributeRouteMapName[0],currentOspfReditributeRouteMapName[0]);
		boolean isStaticNullModified=compareNull(prevBgpId[0],currentBgpId[0]);
		//boolean isStaticModified=compareInteger(prevStaticId[0],currentStaticId[0]);
		boolean isStaticRouteNameModified=compareStrings(prevStaticReditributeRouteMapName[0],currentStaticReditributeRouteMapName[0]);
		
		if(isBgpNullModified || /*isBgpModified ||*/ isBgpInBoundV4RouteNameModified || isBgpInBoundV6RouteNameModified
				|| isOspfNullModified || /*isOspfModified ||*/ isOspfRouteNameModified
				 || isStaticNullModified || /*isStaticModified ||*/ isStaticRouteNameModified ){
			LOGGER.info("isRouterProtocolsModified Either One");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
		}
		if(/*isBgpModified ||*/ (isBgpInBoundV4RouteNameModified || isBgpInBoundV6RouteNameModified) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol())){
			LOGGER.info("isBgpModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if(isOspfNullModified){
			LOGGER.info("isOspfNullModified");
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
		}
		if(/*isOspfModified ||*/ isOspfRouteNameModified && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol())){
			LOGGER.info("isOspfModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if(/*isStaticModified ||*/ isStaticRouteNameModified && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol())){
			LOGGER.info("isStaticModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if((isBgpNullModified && Objects.isNull(currentBgpId[0])) 
				&& Objects.nonNull(currentOspfId[0]) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol())){
			LOGGER.info("Bgp to Ospf");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if((isBgpNullModified && Objects.isNull(currentBgpId[0]) ) 
				&& Objects.nonNull(currentStaticId[0]) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol())){
			LOGGER.info("Bgp to Static");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if((isOspfNullModified && Objects.isNull(currentOspfId[0])) 
				&& Objects.nonNull(currentBgpId[0]) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol())){
			LOGGER.info("Ospf to Bgp");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if((isOspfNullModified && Objects.isNull(currentOspfId[0])) 
				&& Objects.nonNull(currentStaticId[0]) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol())){
			LOGGER.info("Ospf to Static");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getStaticRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if((isStaticNullModified && Objects.isNull(currentStaticId[0])) 
				&& Objects.nonNull(currentBgpId[0]) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol())){
			LOGGER.info("Static to Bgp");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getBgpRoutingProtocol().setIsObjectInstanceModified(true);
		}
		if((isStaticNullModified && Objects.isNull(currentStaticId[0])) 
				&& Objects.nonNull(currentOspfId[0]) && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol()) 
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol())){
			LOGGER.info("Static to Ospf");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getWanRoutingProtocol().getOspfRoutingProtocol().setIsObjectInstanceModified(true);
		}
	}
	
	private void isRouterSDHInterfaceAccessCtrlListModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map) {
		String prevInboundV4=null;
		String prevInboundV6=null;
		String prevOutboundV4=null;
		String prevOutboundV6=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V4)){
			prevInboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V6)){
			prevInboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V6);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V4)){
			prevOutboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V6)){
			prevOutboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V6);
		}
		
		WANInterface.Interface wanInterface=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		String currentInboundV4=null;
		String currentInboundV6=null;
		String currentOutboundV4=null;
		String currentOutboundV6=null;
		if(Objects.nonNull(wanInterface) && Objects.nonNull(wanInterface.getChannelizedSDHInterface())){
			AccessControlList inboundAccessControlV4 =wanInterface.getChannelizedSDHInterface().getInboundAccessControlList();
			AccessControlList inboundAccessControlV6 =wanInterface.getChannelizedSDHInterface().getInboundAccessControlListV6();
			AccessControlList outboundAccessControlV4 =wanInterface.getChannelizedSDHInterface().getOutboundAccessControlList();
			AccessControlList outboundAccessControlV6 =wanInterface.getChannelizedSDHInterface().getOutboundAccessControlListV6();
			
			if(Objects.nonNull(inboundAccessControlV4)){
				currentInboundV4=inboundAccessControlV4.getName();
			}
			if(Objects.nonNull(inboundAccessControlV6)){
				currentInboundV6=inboundAccessControlV6.getName();
			}
			if(Objects.nonNull(outboundAccessControlV4)){
				currentOutboundV4=outboundAccessControlV4.getName();
			}
			if(Objects.nonNull(outboundAccessControlV6)){
				currentOutboundV6=outboundAccessControlV6.getName();
			}
		}
		boolean isInBoundV4NullModified=compareNull(prevInboundV4,currentInboundV4);
		boolean isInBoundV6NullModified=compareNull(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4NullModified=compareNull(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6NullModified=compareNull(prevOutboundV6,currentOutboundV6);
		boolean isInBoundV4Modified=compareString(prevInboundV4,currentInboundV4);
		boolean isInBoundV6Modified=compareString(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4Modified=compareString(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6Modified=compareString(prevOutboundV6,currentOutboundV6);
			
		if(isInBoundV4Modified || isInBoundV4NullModified
					|| isInBoundV6Modified || isInBoundV6NullModified
					 || isOutBoundV4Modified || isOutBoundV4NullModified
					  || isOutBoundV6Modified || isOutBoundV6NullModified){
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getChannelizedSDHInterface().setIsObjectInstanceModified(true);
		}
		if(isInBoundV4Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getChannelizedSDHInterface().getInboundAccessControlList().setIsObjectInstanceModified(true);
		}
		if(isInBoundV6Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getChannelizedSDHInterface().getInboundAccessControlListV6().setIsObjectInstanceModified(true);
		}
		if(isOutBoundV4Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getChannelizedSDHInterface().getOutboundAccessControlList().setIsObjectInstanceModified(true);
		}
		if(isOutBoundV6Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getChannelizedSDHInterface().getOutboundAccessControlListV6().setIsObjectInstanceModified(true);
		}
	}
	
	private void isRouterSerailInterfaceAccessCtrlListModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map) {
		String prevInboundV4=null;
		String prevInboundV6=null;
		String prevOutboundV4=null;
		String prevOutboundV6=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V4)){
			prevInboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V6)){
			prevInboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V6);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V4)){
			prevOutboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V6)){
			prevOutboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V6);
		}
		
		WANInterface.Interface wanInterface=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		String currentInboundV4=null;
		String currentInboundV6=null;
		String currentOutboundV4=null;
		String currentOutboundV6=null;
		if(Objects.nonNull(wanInterface) && Objects.nonNull(wanInterface.getSerialInterface())){
			AccessControlList inboundAccessControlV4 =wanInterface.getSerialInterface().getInboundAccessControlList();
			AccessControlList inboundAccessControlV6 =wanInterface.getSerialInterface().getInboundAccessControlListV6();
			AccessControlList outboundAccessControlV4 =wanInterface.getSerialInterface().getOutboundAccessControlList();
			AccessControlList outboundAccessControlV6 =wanInterface.getSerialInterface().getOutboundAccessControlListV6();
			
			if(Objects.nonNull(inboundAccessControlV4)){
				currentInboundV4=inboundAccessControlV4.getName();
			}
			if(Objects.nonNull(inboundAccessControlV6)){
				currentInboundV6=inboundAccessControlV6.getName();
			}
			if(Objects.nonNull(outboundAccessControlV4)){
				currentOutboundV4=outboundAccessControlV4.getName();
			}
			if(Objects.nonNull(outboundAccessControlV6)){
				currentOutboundV6=outboundAccessControlV6.getName();
			}
		}
		boolean isInBoundV4NullModified=compareNull(prevInboundV4,currentInboundV4);
		boolean isInBoundV6NullModified=compareNull(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4NullModified=compareNull(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6NullModified=compareNull(prevOutboundV6,currentOutboundV6);
		boolean isInBoundV4Modified=compareString(prevInboundV4,currentInboundV4);
		boolean isInBoundV6Modified=compareString(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4Modified=compareString(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6Modified=compareString(prevOutboundV6,currentOutboundV6);
			
		if(isInBoundV4Modified || isInBoundV4NullModified
					|| isInBoundV6Modified || isInBoundV6NullModified
					 || isOutBoundV4Modified || isOutBoundV4NullModified
					  || isOutBoundV6Modified || isOutBoundV6NullModified){
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getSerialInterface().setIsObjectInstanceModified(true);
		}
		if(isInBoundV4Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getSerialInterface().getInboundAccessControlList().setIsObjectInstanceModified(true);
		}
		if(isInBoundV6Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getSerialInterface().getInboundAccessControlListV6().setIsObjectInstanceModified(true);
		}
		if(isOutBoundV4Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getSerialInterface().getOutboundAccessControlList().setIsObjectInstanceModified(true);
		}
		if(isOutBoundV6Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getSerialInterface().getOutboundAccessControlListV6().setIsObjectInstanceModified(true);
		}
	}

	private void isRouterEthernetInterfaceAccessCtrlListModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map) {
		String prevInboundV4=null;
		String prevInboundV6=null;
		String prevOutboundV4=null;
		String prevOutboundV6=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V4)){
			prevInboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V6)){
			prevInboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V6);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V4)){
			prevOutboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V6)){
			prevOutboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V6);
		}
		WANInterface.Interface wanInterface=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		String currentInboundV4=null;
		String currentInboundV6=null;
		String currentOutboundV4=null;
		String currentOutboundV6=null;
		if(Objects.nonNull(wanInterface) && Objects.nonNull(wanInterface.getEthernetInterface())){
			AccessControlList inboundAccessControlV4 =wanInterface.getEthernetInterface().getInboundAccessControlList();
			AccessControlList inboundAccessControlV6 =wanInterface.getEthernetInterface().getInboundAccessControlListV6();
			AccessControlList outboundAccessControlV4 =wanInterface.getEthernetInterface().getOutboundAccessControlList();
			AccessControlList outboundAccessControlV6 =wanInterface.getEthernetInterface().getOutboundAccessControlListV6();
			
			if(Objects.nonNull(inboundAccessControlV4)){
				currentInboundV4=inboundAccessControlV4.getName();
			}
			if(Objects.nonNull(inboundAccessControlV6)){
				currentInboundV6=inboundAccessControlV6.getName();
			}
			if(Objects.nonNull(outboundAccessControlV4)){
				currentOutboundV4=outboundAccessControlV4.getName();
			}
			if(Objects.nonNull(outboundAccessControlV6)){
				currentOutboundV6=outboundAccessControlV6.getName();
			}
		}
		boolean isInBoundV4NullModified=compareNull(prevInboundV4,currentInboundV4);
		boolean isInBoundV6NullModified=compareNull(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4NullModified=compareNull(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6NullModified=compareNull(prevOutboundV6,currentOutboundV6);
		boolean isInBoundV4Modified=compareString(prevInboundV4,currentInboundV4);
		boolean isInBoundV6Modified=compareString(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4Modified=compareString(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6Modified=compareString(prevOutboundV6,currentOutboundV6);
			
		if(isInBoundV4Modified || isInBoundV4NullModified
					|| isInBoundV6Modified || isInBoundV6NullModified
					 || isOutBoundV4Modified || isOutBoundV4NullModified
					  || isOutBoundV6Modified || isOutBoundV6NullModified){
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getEthernetInterface().setIsObjectInstanceModified(true);
		}
		if(isInBoundV4Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getEthernetInterface().getInboundAccessControlList().setIsObjectInstanceModified(true);
		}
		if(isInBoundV6Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getEthernetInterface().getInboundAccessControlListV6().setIsObjectInstanceModified(true);
		}
		if(isOutBoundV4Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getEthernetInterface().getOutboundAccessControlList().setIsObjectInstanceModified(true);
		}
		if(isOutBoundV6Modified){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface().getEthernetInterface().getOutboundAccessControlListV6().setIsObjectInstanceModified(true);
		}
	}
	
	private void isRouterInterfaceAccessCtrlListModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map,Map<String,Object> currentRouterMap) {
		String prevInboundV4=null;
		String prevInboundV6=null;
		String prevOutboundV4=null;
		String prevOutboundV6=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V4)){
			prevInboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INBOUND_V6)){
			prevInboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_INBOUND_V6);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V4)){
			prevOutboundV4=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V4);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_OUTBOUND_V6)){
			prevOutboundV6=(String)map.get(AceConstants.ROUTER.ROUTER_OUTBOUND_V6);
		}
		String currentInboundV4=null;
		String currentInboundV6=null;
		String currentOutboundV4=null;
		String currentOutboundV6=null;
		if(isValueExists(currentRouterMap,AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V4)){
				currentInboundV4=(String)currentRouterMap.get(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V4);
		}
		if(isValueExists(currentRouterMap,AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V6)){
				currentInboundV6=(String)currentRouterMap.get(AceConstants.ROUTER.ROUTER_CURRENT_INBOUND_V6);
		}
		if(isValueExists(currentRouterMap,AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V4)){
				currentOutboundV4=(String)currentRouterMap.get(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V4);
		}
		if(isValueExists(currentRouterMap,AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V6)){
				currentOutboundV6=(String)currentRouterMap.get(AceConstants.ROUTER.ROUTER_CURRENT_OUTBOUND_V6);
		}
		validateIsRouterInterfaceAccessCtrlListModified(prevInboundV4,currentInboundV4,prevInboundV6,currentInboundV6,prevOutboundV4,currentOutboundV4,prevOutboundV6,currentOutboundV6,performIPServiceActivation);
	}

	private void validateIsRouterInterfaceAccessCtrlListModified(String prevInboundV4, String currentInboundV4,
			String prevInboundV6, String currentInboundV6, String prevOutboundV4, String currentOutboundV4,
			String prevOutboundV6, String currentOutboundV6,PerformIPServiceActivation performIPServiceActivation) {
		boolean isInBoundV4NullModified=compareNull(prevInboundV4,currentInboundV4);
		boolean isInBoundV6NullModified=compareNull(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4NullModified=compareNull(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6NullModified=compareNull(prevOutboundV6,currentOutboundV6);
		boolean isInBoundV4Modified=compareString(prevInboundV4,currentInboundV4);
		boolean isInBoundV6Modified=compareString(prevInboundV6,currentInboundV6);
		boolean isOutBoundV4Modified=compareString(prevOutboundV4,currentOutboundV4);
		boolean isOutBoundV6Modified=compareString(prevOutboundV6,currentOutboundV6);
			
		if(isInBoundV4Modified || isInBoundV4NullModified
					|| isInBoundV6Modified || isInBoundV6NullModified
					 || isOutBoundV4Modified || isOutBoundV4NullModified
					  || isOutBoundV6Modified || isOutBoundV6NullModified){
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
				updateRouterInterfaceModified(performIPServiceActivation);
		}
		updateRouterInterfaceIpDetailsModified(performIPServiceActivation,isInBoundV4Modified,isInBoundV6Modified,isOutBoundV4Modified,isOutBoundV6Modified);
		
		
	}

	private void updateRouterInterfaceIpDetailsModified(PerformIPServiceActivation performIPServiceActivation,
			boolean isInBoundV4Modified, boolean isInBoundV6Modified, boolean isOutBoundV4Modified,
			boolean isOutBoundV6Modified) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			if(isInBoundV4Modified){
				wanInterfaceType.getEthernetInterface().getInboundAccessControlList().setIsObjectInstanceModified(true);
			}
			if(isInBoundV6Modified){
				wanInterfaceType.getEthernetInterface().getInboundAccessControlListV6().setIsObjectInstanceModified(true);
			}
			if(isOutBoundV4Modified){
				wanInterfaceType.getEthernetInterface().getOutboundAccessControlList().setIsObjectInstanceModified(true);
			}
			if(isOutBoundV6Modified){
				wanInterfaceType.getEthernetInterface().getOutboundAccessControlListV6().setIsObjectInstanceModified(true);
			}
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			if(isInBoundV4Modified){
				wanInterfaceType.getSerialInterface().getInboundAccessControlList().setIsObjectInstanceModified(true);
			}
			if(isInBoundV6Modified){
				wanInterfaceType.getSerialInterface().getInboundAccessControlListV6().setIsObjectInstanceModified(true);
			}
			if(isOutBoundV4Modified){
				wanInterfaceType.getSerialInterface().getOutboundAccessControlList().setIsObjectInstanceModified(true);
			}
			if(isOutBoundV6Modified){
				wanInterfaceType.getSerialInterface().getOutboundAccessControlListV6().setIsObjectInstanceModified(true);
			}
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			if(isInBoundV4Modified){
				wanInterfaceType.getChannelizedSDHInterface().getInboundAccessControlList().setIsObjectInstanceModified(true);
			}
			if(isInBoundV6Modified){
				wanInterfaceType.getChannelizedSDHInterface().getInboundAccessControlListV6().setIsObjectInstanceModified(true);
			}
			if(isOutBoundV4Modified){
				wanInterfaceType.getChannelizedSDHInterface().getOutboundAccessControlList().setIsObjectInstanceModified(true);
			}
			if(isOutBoundV6Modified){
				wanInterfaceType.getChannelizedSDHInterface().getOutboundAccessControlListV6().setIsObjectInstanceModified(true);
			}
		}
	}

	private void isVRFNameModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail,ServiceDetail serviceDetail) {
		String prevVrfName[]={null};
		String currentVrfName[]={null};
		prevActiveServiceDetail.getVrfs().stream().forEach(prevVrf ->{
			prevVrfName[0]=prevVrf.getVrfName();
		});
		serviceDetail.getVrfs().stream().forEach(currentVrf ->{
			currentVrfName[0]=currentVrf.getVrfName();
		});
		boolean isVrfModified=compareStrings(prevVrfName[0],currentVrfName[0]);
		if(isVrfModified){
			LOGGER.info("isVRFNameModified");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
		}
	}

	private void isCOSDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		String prevServiceCosType[]={null};
		String prevServiceCosProfile[]={null};
		String prevServiceQosTrafficMode[]={null};
		prevActiveServiceDetail.getServiceQos().stream().forEach(prevServiceQos -> {
			prevServiceCosType[0]=Objects.nonNull(prevServiceQos.getCosType())?prevServiceQos.getCosType().toUpperCase():null;
			prevServiceCosProfile[0]=Objects.nonNull(prevServiceQos.getCosProfile())?prevServiceQos.getCosProfile().toUpperCase():null;
			prevServiceQosTrafficMode[0]=Objects.nonNull(prevServiceQos.getQosTrafiicMode())?prevServiceQos.getQosTrafiicMode().toUpperCase():null;
		});
		
		QoS qos=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getQos();
		if(Objects.nonNull(qos)){
			String currentServiceCosType=qos.getCosType();
			String currentServiceCosProfile=qos.getQoSProfile();
			String currentServiceQosTrafficMode=qos.getQoSTrafficMode();
			boolean isServiceCosTypeModified=compareStrings(prevServiceCosType[0],currentServiceCosType);
			boolean isServiceCosProfileModified=compareStrings(prevServiceCosProfile[0],currentServiceCosProfile);
			boolean isServiceQosTrafficModified=compareStrings(prevServiceQosTrafficMode[0],currentServiceQosTrafficMode);
			if(isServiceCosTypeModified 
					|| isServiceCosProfileModified
					 || isServiceQosTrafficModified){
				 LOGGER.info("isCOSDetailModified Either One");
				 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
				 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getQos().setIsObjectInstanceModified(true);
			}
		}
	}

	private void isRouterUniSwitchDetailModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		String prevRouterUniSwitchHostName[]={null};
		String prevRouterUniSwitchPhysicalPort[]={null};
		String prevRouterUniSwitchVLAN[]={null};
		boolean isPrevUniSwitchExists[]={false};
		prevActiveServiceDetail.getTopologies().stream().forEach(prevTopology ->{
			if(Objects.nonNull(prevTopology.getUniswitchDetails()) && !prevTopology.getUniswitchDetails().isEmpty()){
				prevTopology.getUniswitchDetails().stream().forEach(prevUniswitch ->{
					prevRouterUniSwitchHostName[0]=prevUniswitch.getHostName();
					prevRouterUniSwitchPhysicalPort[0]=prevUniswitch.getPhysicalPort();
					prevRouterUniSwitchVLAN[0]=prevUniswitch.getInnerVlan();
					isPrevUniSwitchExists[0]=true;
				});
			}
		});
		String currentRouterUniSwitchHostName[]={null};
		String currentRouterUniSwitchPhysicalPort[]={null};
		String currentRouterUniSwitchVLAN[]={null};
		boolean isCurrentUniSwitchExists[]={false};
		currentServiceDetail.getTopologies().stream().forEach(currentTopology ->{
			if(Objects.nonNull(currentTopology.getUniswitchDetails()) && !currentTopology.getUniswitchDetails().isEmpty()){
				currentTopology.getUniswitchDetails().stream().forEach(currentUniswitch ->{
					currentRouterUniSwitchHostName[0]=currentUniswitch.getHostName();
					currentRouterUniSwitchPhysicalPort[0]=currentUniswitch.getPhysicalPort();
					currentRouterUniSwitchVLAN[0]=currentUniswitch.getInnerVlan();
					isCurrentUniSwitchExists[0]=true;
				});
			}
		});
		boolean isHostNameModified=compareStrings(prevRouterUniSwitchHostName[0],currentRouterUniSwitchHostName[0]);
		boolean isPhysicalPortModified=compareStrings(prevRouterUniSwitchPhysicalPort[0],currentRouterUniSwitchPhysicalPort[0]);
		boolean isVLANModified=compareStrings(prevRouterUniSwitchVLAN[0],currentRouterUniSwitchVLAN[0]);
		if(isHostNameModified 
				|| isPhysicalPortModified
				 || isVLANModified){
			 LOGGER.info("isRouterUniSwitchDetailModified Either One");
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			 if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0))){
				 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
			 }
		}
		if(isPrevUniSwitchExists[0] && isCurrentUniSwitchExists[0]){
			 LOGGER.info("isPrevUniSwitchExists && isCurrentUniSwitchExists");
			 if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0))){
				 if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo())){
					 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo().setIsObjectModifiedInstance(true);
					 if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo().getUNISwitch())){
						 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo().getUNISwitch().setIsObjectInstanceModified(true);
					 }
				 }
			 }
		}
	}

	private void isRouterUpLinkPortModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		String prevTopologyUpLinkPortName[]={null};
		prevActiveServiceDetail.getTopologies().stream().forEach(prevTopology ->{
			if(Objects.nonNull(prevTopology.getRouterUplinkports())){
				prevTopology.getRouterUplinkports().stream().forEach(prevTopologyUpLinkPort ->{
					prevTopologyUpLinkPortName[0]=prevTopologyUpLinkPort.getPhysicalPort1Name();
				});
			}
		});
		String currentTopologyUpLinkPortName[]={null};
		currentServiceDetail.getTopologies().stream().forEach(currentTopology ->{
			if(Objects.nonNull(currentTopology.getRouterUplinkports())){
				currentTopology.getRouterUplinkports().stream().forEach(currentTopologyUpLinkPort ->{
					currentTopologyUpLinkPortName[0]=currentTopologyUpLinkPort.getPhysicalPort1Name();
				});
			}
		});
		boolean isTopologyUpLinkPortNullModified=compareNull(prevTopologyUpLinkPortName[0],currentTopologyUpLinkPortName[0]);
		boolean isTopologyUpLinkPortNameModified=compareString(prevTopologyUpLinkPortName[0],currentTopologyUpLinkPortName[0]);
		if(isTopologyUpLinkPortNullModified 
				|| isTopologyUpLinkPortNameModified){
			LOGGER.info("isRouterUpLinkPortModified Either One");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0))){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
			}
			if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo())){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo().setIsObjectModifiedInstance(true);
			}
		}
		if(isTopologyUpLinkPortNameModified){
			LOGGER.info("isTopologyUpLinkPortNameModified");
			if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0))){
				if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getRouterTopologyInterface1())){
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getRouterTopologyInterface1().setIsObjectInstanceModified(true);
				}
				if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getRouterTopologyInterface2())){
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getRouterTopologyInterface2().setIsObjectInstanceModified(true);
				}
			}
		}
	}

	private void isRouterTopologyModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail, ServiceDetail currentServiceDetail) {
		String topologyName[]={null};
		prevActiveServiceDetail.getTopologies().stream().forEach(prevTopology ->{
			topologyName[0]=prevTopology.getTopologyName();
		});
		String currentTopologyName[]={null};
		currentServiceDetail.getTopologies().stream().forEach(currentTopology ->{
			currentTopologyName[0]=currentTopology.getTopologyName();
		});
		boolean isTopologyNameNullValueModified=compareNull(topologyName[0],currentTopologyName[0]);
		boolean isTopologyNameModified=compareString(topologyName[0],currentTopologyName[0]);
		if(isTopologyNameNullValueModified 
				|| isTopologyNameModified){
			 LOGGER.info("isRouterTopologyModified Either One");
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
		}
		if(isTopologyNameModified && Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0))
				&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo())){
			LOGGER.info("isTopologyNameModified");
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getTopologyInfo().setIsObjectModifiedInstance(true);
		}
		if(isTopologyNameNullValueModified){
			 LOGGER.info("isTopologyNameNullValueModified");
			 performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
		}
	}

	private void isRouterInterfaceDetailModified(
			PerformIPServiceActivation performIPServiceActivation,Map<String,Object> map,Map<String,Object> currentMap) {
		String prevRouterSerialInterfaceChannelGroupNumber=null;
		String prevRouterSerialInterfaceFirstTimeSlot=null;
		String prevRouterSerialInterfaceLastTimeSlot=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_CHANNEL_GROUP_NUMBER)){
			prevRouterSerialInterfaceChannelGroupNumber=(String)map.get(AceConstants.ROUTER.ROUTER_CHANNEL_GROUP_NUMBER);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_FIRST_TIME_SLOT)){
			prevRouterSerialInterfaceFirstTimeSlot=(String)map.get(AceConstants.ROUTER.ROUTER_FIRST_TIME_SLOT);
		}
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_LAST_TIME_SLOT)){
			prevRouterSerialInterfaceLastTimeSlot=(String)map.get(AceConstants.ROUTER.ROUTER_LAST_TIME_SLOT);
		}
		String currentRouterSerialInterfaceChannelGroupNumber=null;
		String currentRouterSerialInterfaceFirstTimeSlot=null;
		String currentRouterSerialInterfaceLastTimeSlot=null;
		WANInterface.Interface routerWANInterface=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(routerWANInterface) && Objects.nonNull(routerWANInterface.getSerialInterface())){
			if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_CHANNEL_GROUP_NUMBER)){
				currentRouterSerialInterfaceChannelGroupNumber=(String)currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_CHANNEL_GROUP_NUMBER);
			}
			if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_FIRST_TIME_SLOT)){
				currentRouterSerialInterfaceFirstTimeSlot=(String)currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_FIRST_TIME_SLOT);
			}
			if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_LAST_TIME_SLOT)){
				currentRouterSerialInterfaceLastTimeSlot=(String)currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_LAST_TIME_SLOT);
			}
		}
		LOGGER.info("isRouterInterfaceDetailModified");
		validateIsRouterInterfaceDetailModified(prevRouterSerialInterfaceChannelGroupNumber,currentRouterSerialInterfaceChannelGroupNumber,
				prevRouterSerialInterfaceFirstTimeSlot,currentRouterSerialInterfaceFirstTimeSlot,
				prevRouterSerialInterfaceLastTimeSlot,currentRouterSerialInterfaceLastTimeSlot,performIPServiceActivation);
	}

	private void validateIsRouterInterfaceDetailModified(String prevInterfaceChannelGroupNumber,
			String currentInterfaceChannelGroupNumber, String prevInterfaceFirstTimeSlot,
			String currentInterfaceFirstTimeSlot, String prevInterfaceLastTimeSlot,
			String currentInterfaceLastTimeSlot, PerformIPServiceActivation performIPServiceActivation) {
		boolean isGroupNumberModified=compareStrings(prevInterfaceChannelGroupNumber,currentInterfaceChannelGroupNumber);
		boolean isFirstTimeSlotModified=compareStrings(prevInterfaceFirstTimeSlot,currentInterfaceFirstTimeSlot);
		boolean isLastTimeSlotModified=compareStrings(prevInterfaceLastTimeSlot,currentInterfaceLastTimeSlot);
		if(isGroupNumberModified 
				|| isFirstTimeSlotModified
				 || isLastTimeSlotModified){
			 LOGGER.info("validateIsRouterInterfaceDetailModified");
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
			 updateRouterInterfaceModified(performIPServiceActivation);
		}
	}

	private void isRouterMgmtIpv4Modified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map) {
		String prevRouterMgmtIp=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_MGMT_V4_ADDRESS)){
			prevRouterMgmtIp=(String)map.get(AceConstants.ROUTER.ROUTER_MGMT_V4_ADDRESS);
		}
		IPV4Address ipv4Address=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getV4ManagementIPAddress();
		validateIsRouterMgmtIpv4Modified(prevRouterMgmtIp,ipv4Address,performIPServiceActivation);
	}

	private void validateIsRouterMgmtIpv4Modified(String prevRouterMgmtIp, IPV4Address ipv4Address,
			PerformIPServiceActivation performIPServiceActivation) {
		if(Objects.nonNull(ipv4Address)){
			String currentIPV4Address=ipv4Address.getAddress();
			boolean isIPV4AddressModified=compareStrings(prevRouterMgmtIp,currentIPV4Address);
			if(isIPV4AddressModified){
					LOGGER.info("validateIsRouterMgmtIpv4Modified");
					performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
					//performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsConfigManaged(true);
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
					performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getV4ManagementIPAddress().setIsObjectModifiedInstance(true);
					
			}
		}
	}

	private void isRouterEthernetVLANModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map,Map<String,Object> currentMap) {
		Integer prevRouterEthernetVLAN=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_ETHERNET_VLAN)){
			prevRouterEthernetVLAN=Integer.valueOf(String.valueOf(map.get(AceConstants.ROUTER.ROUTER_ETHERNET_VLAN)));
		}
		Integer currentRouterEthernetVLAN=null;
		if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_ETHERNET_VLAN)){
			currentRouterEthernetVLAN=Integer.valueOf(String.valueOf(currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_ETHERNET_VLAN)));
		}
		LOGGER.info("isRouterEthernetVLANModified");
		validateIsRouterEthernetVLANModified(prevRouterEthernetVLAN,currentRouterEthernetVLAN,performIPServiceActivation);
	}

	private void validateIsRouterEthernetVLANModified(Integer prevRouterEthernetVLAN, Integer currentRouterEthernetVLAN,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isCpeWANInterfaceTypeNameModified=compareIntegers(prevRouterEthernetVLAN,currentRouterEthernetVLAN);
		if(isCpeWANInterfaceTypeNameModified){
			LOGGER.info("validateIsRouterEthernetVLANModified");
			updateRouterInterfaceModified(performIPServiceActivation);
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
		}
	}

	private void isRouterWANInterfaceNameModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map,Map<String,Object> currentMap) {
		String prevRouterWANInterfaceName=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_INTERFACE_NAME)){
			prevRouterWANInterfaceName=(String) map.get(AceConstants.ROUTER.ROUTER_INTERFACE_NAME);
		}
		String currentRouterWANInterfaceName=null;
		if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_INTERFACE_NAME)){
			currentRouterWANInterfaceName=(String) currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_INTERFACE_NAME);
		}
		LOGGER.info("isRouterWANInterfaceNameModified");
		validateIsRouterWANInterfaceBasicDetailModified(prevRouterWANInterfaceName,currentRouterWANInterfaceName,performIPServiceActivation);
	}

	private void isRouterWANInterfacePhysicalPortModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map,Map<String,Object> currentMap) {
		String prevRouterWANPhysicalPortName=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_PHYSICAL_PORT)){
			prevRouterWANPhysicalPortName=(String) map.get(AceConstants.ROUTER.ROUTER_PHYSICAL_PORT);
		}
		String currentRouterPhysicalPortName=null;
		if(isValueExists(currentMap,AceConstants.ROUTER.ROUTER_CURRENT_PHYSICAL_PORT)){
			currentRouterPhysicalPortName=(String) currentMap.get(AceConstants.ROUTER.ROUTER_CURRENT_PHYSICAL_PORT);
		}
		LOGGER.info("isRouterWANInterfacePhysicalPortModified");
		validateIsRouterWANInterfaceBasicDetailModified(prevRouterWANPhysicalPortName,currentRouterPhysicalPortName,performIPServiceActivation);
	}

	private void validateIsRouterWANInterfaceBasicDetailModified(String prevValue,
			String currentValue, PerformIPServiceActivation performIPServiceActivation) {
		 boolean isRouterWANInterfaceTypeNameModified=compareStrings(prevValue,currentValue);
		 if(isRouterWANInterfaceTypeNameModified){
			LOGGER.info("validateIsRouterWANInterfaceBasicDetailModified");
			updateRouterInterfaceModified(performIPServiceActivation);
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
		}
	}
	
	private void updateRouterInterfaceModified(PerformIPServiceActivation performIPServiceActivation) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			LOGGER.info("REthernetInterface");
			wanInterfaceType.getEthernetInterface().setIsObjectInstanceModified(true);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			LOGGER.info("RSerial");
			wanInterfaceType.getSerialInterface().setIsObjectInstanceModified(true);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			LOGGER.info("RSDH");
			wanInterfaceType.getChannelizedSDHInterface().setIsObjectInstanceModified(true);
		}
	}
	
	private void isRouterHostNameModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map) {
		String prevRouterHostName=null;
		if(isValueExists(map,AceConstants.ROUTER.ROUTER_HOST_NAME)){
			prevRouterHostName=(String) map.get(AceConstants.ROUTER.ROUTER_HOST_NAME);
		}
		String currentRouterHostName=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).getHostName();
		validateIsRouterHostNameModified(prevRouterHostName,currentRouterHostName,performIPServiceActivation);
		}
		
	private void validateIsRouterHostNameModified(String prevRouterHostName, String currentRouterHostName,
				PerformIPServiceActivation performIPServiceActivation) {
		boolean isRouterHostNameModified=compareStrings(prevRouterHostName,currentRouterHostName);
		if(isRouterHostNameModified){
			LOGGER.info("validateIsRouterHostNameModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			//performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsConfigManaged(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRouter().get(0).setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
		}
	}

	private void isCPEEthernetVLANModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String,Object> map,Map<String,Object> currentCPEMap) {
		Integer prevCpeEthernetVLAN=null;
		if(isValueExists(map,AceConstants.CPE.CPE_ETHERNET_VLAN)){
			prevCpeEthernetVLAN=Integer.valueOf(String.valueOf(map.get(AceConstants.CPE.CPE_ETHERNET_VLAN)));
		}
		boolean isCpeManaged=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		Integer currentCpeEthernetVLAN=null;
		if(isCpeManaged){
			LOGGER.info("isCPEEthernetVLANModified");
			if(isValueExists(currentCPEMap,AceConstants.CPE.CPE_CURRENT_ETHERNET_VLAN)){
				currentCpeEthernetVLAN=Integer.valueOf(String.valueOf(currentCPEMap.get(AceConstants.CPE.CPE_CURRENT_ETHERNET_VLAN)));
			}
			validateIsCPEEthernetVLANModified(prevCpeEthernetVLAN,currentCpeEthernetVLAN,performIPServiceActivation);
		}
	}

	private void validateIsCPEEthernetVLANModified(Integer prevCpeEthernetVLAN, Integer currentCpeEthernetVLAN,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isCpeWANInterfaceTypeNameModified=compareIntegers(prevCpeEthernetVLAN,currentCpeEthernetVLAN);
		 if(isCpeWANInterfaceTypeNameModified){
			 LOGGER.info("validateIsCPEEthernetVLANModified");
			 updateCpeInterfaceModified(performIPServiceActivation);
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsObjectInstanceModified(true);
		}
		
	}

	private void isCPEWANInterfacePhysicalPortModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String, Object> map,Map<String, Object> currentCPEMap) {
		String prevCpeWANPhysicalPortName=null;
		if(isValueExists(map,AceConstants.CPE.CPE_PHYSICAL_PORT)){
			prevCpeWANPhysicalPortName=(String) map.get(AceConstants.CPE.CPE_PHYSICAL_PORT);
		}
		boolean isCpeManaged=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		String currentCpePhysicalPortName=null;
		if(isCpeManaged){
			LOGGER.info("isCPEWANInterfacePhysicalPortModified");
			if(isValueExists(currentCPEMap,AceConstants.CPE.CPE_CURRENT_PHYSICAL_PORT)){
				currentCpePhysicalPortName=(String) currentCPEMap.get(AceConstants.CPE.CPE_CURRENT_PHYSICAL_PORT);
			}
			validateIsCPEWANInterfaceBasicDetailModified(prevCpeWANPhysicalPortName,currentCpePhysicalPortName,performIPServiceActivation);		
		}
	}

	private void validateIsCPEWANInterfaceBasicDetailModified(String prevValue,
			String currentValue, PerformIPServiceActivation performIPServiceActivation) {
		 boolean isCpeWANInterfaceTypeNameModified=compareStrings(prevValue,currentValue);
		 if(isCpeWANInterfaceTypeNameModified){
			 LOGGER.info("validateIsCPEWANInterfaceBasicDetailModified");
			 updateCpeInterfaceModified(performIPServiceActivation);
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsObjectInstanceModified(true);
		}
	}
	
	private void updateCpeInterfaceModified(PerformIPServiceActivation performIPServiceActivation) {
		WANInterface.Interface wanInterfaceType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getWanInterface().getInterface();
		if(Objects.nonNull(wanInterfaceType.getEthernetInterface())){
			LOGGER.info("updateCpeInterfaceModified: Ethernet");
			wanInterfaceType.getEthernetInterface().setIsObjectInstanceModified(true);
		}else if(Objects.nonNull(wanInterfaceType.getSerialInterface())){
			LOGGER.info("updateCpeInterfaceModified: Serial");
			wanInterfaceType.getSerialInterface().setIsObjectInstanceModified(true);
		}else if(Objects.nonNull(wanInterfaceType.getChannelizedSDHInterface())){
			LOGGER.info("updateCpeInterfaceModified: SDH");
			wanInterfaceType.getChannelizedSDHInterface().setIsObjectInstanceModified(true);
		}
	}

	private void isCPEWANInterfaceNameModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String, Object> map,Map<String, Object> currentCPEMap) {
		String prevCpeWANInterfaceName=null;
		if(isValueExists(map,AceConstants.CPE.CPE_INTERFACE_NAME)){
			prevCpeWANInterfaceName=(String) map.get(AceConstants.CPE.CPE_INTERFACE_NAME);
		}
		boolean isCpeManaged=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		String currentCpeWANInterfaceName=null;
		if(isCpeManaged){
			LOGGER.info("isCPEWANInterfaceNameModified");
			if(isValueExists(currentCPEMap,AceConstants.CPE.CPE_CURRENT_INTERFACE_NAME)){
				currentCpeWANInterfaceName=(String) currentCPEMap.get(AceConstants.CPE.CPE_CURRENT_INTERFACE_NAME);
			}
			validateIsCPEWANInterfaceBasicDetailModified(prevCpeWANInterfaceName,currentCpeWANInterfaceName,performIPServiceActivation);	 
		}
	}
	
	private void isCPESNMPModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String, Object> map) {
		String prevCpeSNMP=null;
		if(isValueExists(map,AceConstants.CPE.CPE_SNP_SERVER_COMMUNITY)){
			prevCpeSNMP=(String) map.get(AceConstants.CPE.CPE_SNP_SERVER_COMMUNITY);
		}
		boolean isCpeManaged=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		if(isCpeManaged){
			LOGGER.info("isCPESNMPModified");
			String currentCpeSNMP=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getSnmpServerCommunity();
			validateCPESNMPModified(prevCpeSNMP,currentCpeSNMP,performIPServiceActivation);
		}
	}
	
	private void validateCPESNMPModified(String prevCpeSNMP, String currentCpeSNMP,
			PerformIPServiceActivation performIPServiceActivation) {
		 boolean isCpeSNMPModified=compareStrings(prevCpeSNMP,currentCpeSNMP);
		 if(isCpeSNMPModified){
			LOGGER.info("validateCPESNMPModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			//performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsConfigManaged(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
		}
	}


	private void isCPEHostNameModified(PerformIPServiceActivation performIPServiceActivation,
			Map<String, Object> map) {
		String prevCpeHostName=null;
		if(isValueExists(map,AceConstants.CPE.CPE_HOST_NAME)){
			prevCpeHostName=(String) map.get(AceConstants.CPE.CPE_HOST_NAME);
		}
		boolean isCpeManaged=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		if(isCpeManaged){
			LOGGER.info("isCPEHostNameModified");
			String currentCpeHostName=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getHostName();
			validateCPEHostNameModified(prevCpeHostName,currentCpeHostName,performIPServiceActivation);
		}
	}

	private void validateCPEHostNameModified(String prevCpeHostName, String currentCpeHostName,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isCpeHostNameModified=compareStrings(prevCpeHostName,currentCpeHostName);
		 if(isCpeHostNameModified){
			LOGGER.info("validateCPEHostNameModified");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			//performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsConfigManaged(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsCEACEConfigurable(false);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
		}
	}

	private void isManagedTypeModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		boolean prevManagedType=prevActiveServiceDetail.getMgmtType().toUpperCase().contains("UNMANAGED")?false:true;
		boolean currentManagedType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().isIsConfigManaged();
		
		if((!prevManagedType && currentManagedType) 
				|| (prevManagedType && !currentManagedType)){
			LOGGER.info("validateManagedTypeModified:: Either One exists");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).setIsCEACEConfigurable(false);
		}
		//Prev - UnManaged, Current - Managed
		if(!prevManagedType && currentManagedType){
			LOGGER.info("validateManagedTypeModified:: Prev - UnManaged, Current - Managed");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsConfigManaged(true);
			if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe())
					&& !performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().isEmpty()
					&& Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getLoopbackInterface())){
						performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getLoopbackInterface().setIsObjectInstanceModified(true);
						if(Objects.nonNull(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getLoopbackInterface().getV4IpAddress())){
							performIPServiceActivation.getOrderDetails().getService().getGvpnService().getCpe().get(0).getLoopbackInterface().getV4IpAddress().setIsObjectModifiedInstance(true);
						}
			}
			
			
			if(performIPServiceActivation.getOrderDetails().getService().getGvpnService().getIsProactiveMonitoringEnabled().equalsIgnoreCase("true")){
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getQos().setIsObjectInstanceModified(true);
			}		
		}
		//Prev - Managed, Current - UnManaged
		else if(prevManagedType && !currentManagedType){
			LOGGER.info("validateManagedTypeModified:: Prev - Managed, Current - UnManaged");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsConfigManaged(false);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsProactiveMonitoringEnabled("false");
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getQos().setIsObjectInstanceModified(true);
		}
	}

	private void isServiceSubTypeModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		String prevServiceSubType=prevActiveServiceDetail.getServiceSubtype();
		String currentServiceSubType=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getServiceSubType();
		validateBasicServiceDetailModified(prevServiceSubType,currentServiceSubType,performIPServiceActivation);
	}

	private void isLastMileModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		String prevLastMile=prevActiveServiceDetail.getLastmileType();
		String currentLastMile=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getLastMile().get(0).getType();
		validateBasicServiceDetailModified(prevLastMile,currentLastMile,performIPServiceActivation);
	}

	private void isRedundancyRoleModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		String prevRedundancyRole=Objects.nonNull(prevActiveServiceDetail.getRedundancyRole())?prevActiveServiceDetail.getRedundancyRole().toUpperCase():null;
		String currentRedundancyRole=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getRedundancyRole();
		validateBasicServiceDetailModified(prevRedundancyRole,currentRedundancyRole,performIPServiceActivation);
	}

	private void isCustomerNameModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		 String prevCustomerName=prevActiveServiceDetail.getOrderDetail().getCustomerName();
		 String currentCustomerName=performIPServiceActivation.getOrderDetails().getCustomerDetails().getName();
		 validateCustomerDetailModified(prevCustomerName,currentCustomerName,performIPServiceActivation);
	}
	
	private void validateCustomerDetailModified(String prevValue, String currentValue,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isValueModified=compareStrings(prevValue,currentValue);
		if(isValueModified){
			 LOGGER.info("validateCustomerDetailModified");
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getCustomerDetails().setIsObjectInstanceModified(true);
		}
	}
	
	private void validateBasicServiceDetailModified(String prevValue, String currentValue,
			PerformIPServiceActivation performIPServiceActivation) {
		boolean isValueModified=compareStrings(prevValue,currentValue);
		if(isValueModified){
			 LOGGER.info("validateBasicServiceDetailModified");
			 performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			 performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
		}
	}

	private void isBustableBandwidthModified(PerformIPServiceActivation performIPServiceActivation,
			ServiceDetail prevActiveServiceDetail) {
		Float prevBurstableBandwidthValue=prevActiveServiceDetail.getBurstableBw();
		String prevBurstableBandwidthUnit=prevActiveServiceDetail.getBurstableBwUnit();
		Bandwidth currentBurstableBandwidth=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getBurstableBandwidth();
		if(Objects.nonNull(currentBurstableBandwidth)){
			Float curBurstableBandwidthValue=currentBurstableBandwidth.getSpeed();
			String curBurstableBandwidthUnit=Objects.nonNull(currentBurstableBandwidth.getUnit())?currentBurstableBandwidth.getUnit().value():null;
			validateIsBustableBandwidthModified(prevBurstableBandwidthValue,prevBurstableBandwidthUnit,curBurstableBandwidthValue,curBurstableBandwidthUnit,performIPServiceActivation);
		}
	}
	
	private void validateIsBustableBandwidthModified(Float prevBurstableBandwidthValue,
			String prevBurstableBandwidthUnit, Float curBurstableBandwidthValue, String curBurstableBandwidthUnit,PerformIPServiceActivation performIPServiceActivation) {
		boolean isBurstableBwUnitModified=compareNull(prevBurstableBandwidthUnit,curBurstableBandwidthUnit);
		if(isBurstableBwUnitModified){
			LOGGER.info("validateIsBustableBandwidthModified");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
		}else{
			boolean isBurstableBwSpeedModified=compareFloat(prevBurstableBandwidthValue,curBurstableBandwidthValue);
			boolean isBurstableStringBwUnitModified=compareString(prevBurstableBandwidthUnit,curBurstableBandwidthUnit);
			if(isBurstableBwSpeedModified || isBurstableStringBwUnitModified){
				LOGGER.info("Unit/Speed validateIsBustableBandwidthModified");
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getBurstableBandwidth().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
				performIPServiceActivation.getOrderDetails().getService().getGvpnService().getQos().setIsObjectInstanceModified(true);
			}
		}
	}

	private void isServiceBandwidthModified(PerformIPServiceActivation performIPServiceActivation,ServiceDetail prevActiveServiceDetail) {
		LOGGER.info("isServiceBandwidthModified invoked");
		Float prevServiceBandwidthValue=prevActiveServiceDetail.getServiceBandwidth();
		String prevServiceBandwidthUnit=prevActiveServiceDetail.getServiceBandwidthUnit();
		Bandwidth currentBandwidth=performIPServiceActivation.getOrderDetails().getService().getGvpnService().getServiceBandwidth();
		if(Objects.nonNull(currentBandwidth)){
			Float curServiceBandwidthValue=currentBandwidth.getSpeed();
			String curServiceBandwidthUnit=Objects.nonNull(currentBandwidth.getUnit())?currentBandwidth.getUnit().value():null;
			validateIsServiceBandwidthModified(prevServiceBandwidthValue,curServiceBandwidthValue,prevServiceBandwidthUnit,curServiceBandwidthUnit,performIPServiceActivation);
		}
		LOGGER.info("isServiceBandwidthModified ends");
	}
	
	private void validateIsServiceBandwidthModified(Float prevServiceBandwidthValue,
			Float curServiceBandwidthValue, String prevServiceBandwidthUnit, String curServiceBandwidthUnit, PerformIPServiceActivation performIPServiceActivation) {
		boolean isBandwidthSpeedModified=compareFloat(prevServiceBandwidthValue,curServiceBandwidthValue);
		boolean isBandwidthUnitModified=compareStrings(prevServiceBandwidthUnit,curServiceBandwidthUnit);
		if(isBandwidthSpeedModified || isBandwidthUnitModified){
			LOGGER.info("validateIsServiceBandwidthModified");
			performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().setIsDowntimeRequired(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getQos().setIsObjectInstanceModified(true);
			performIPServiceActivation.getOrderDetails().getService().getGvpnService().getServiceBandwidth().setIsObjectInstanceModified(true);
		}
		
	}

	private boolean compareInt(int value1, int value2){
		boolean isModified= false;
		if(value1!=value2){
			isModified=true;
		}
		return isModified;
	}
	
	private boolean compareInteger(Integer value1, Integer value2){
		boolean isModified= false;
		if((compareNonNull(value1,value2)) 
				&& (compareInt(value1,value2))){
			isModified=true;
		}
		return isModified;
	}
	
	private boolean compareIntegers(Integer value1, Integer value2){
		boolean isModified= false;
		if(compareNull(value1,value2)){
				isModified=true;
		}else if(compareInteger(value1,value2)){
				isModified=true;
		}
		return isModified;
	}
	
	private boolean compareFloat(Float value1, Float value2){
		boolean isModified= false;
		if(compareNull(value1,value2)){
				isModified=true;
		}else if(compareFloatValue(value1,value2)){
				isModified=true;
		}
		return isModified;
	}
	
	private boolean compareFloatValue(Float value1, Float value2){
		boolean isModified= false;
		if((compareNonNull(value1,value2)) 
				&& (compareFloat1(value1,value2))){
			isModified=true;
		}
		return isModified;
	}
	
	private boolean compareFloat1(Float value1, Float value2){
		boolean isModified= false;
		if(value1!=value2){
			isModified=true;
		}
		return isModified;
	}
	
	private boolean compareBytes(Byte value1, Byte value2){
		boolean isModified= false;
		if(compareNull(value1,value2)){
				isModified=true;
		}else if(compareByte(value1,value2)){
				isModified=true;
		}
		return isModified;
	}

	private boolean compareByte(Byte value1, Byte value2) {
		boolean isModified= false;
		if((compareNonNull(value1,value2)) 
				&& (value1!=value2)){
			isModified=true;
		}
		return isModified;
	}

	private boolean compareStrings(String value1, String value2) {
		boolean isModified= false;
		if(compareNull(value1,value2)){
				isModified=true;
		}else if(compareString(value1,value2)){
				isModified=true;
		}
		return isModified;
	}
	
	private boolean compareString(String value1, String value2) {
		boolean isModified= false;
		if((compareNonNull(value1,value2)) 
				&& (!value1.equalsIgnoreCase(value2))){
			isModified=true;
		}
		return isModified;
	}
	
	private boolean compareCollection(List<String> value1,
			List<String> value2) {
		boolean isModified= false;
		if(compareNull(value1,value2)){
			isModified=true;
		}else if((compareNonNull(value1,value2))
				&& (!value1.isEmpty() && !value2.isEmpty())){
			if(compareCollectionSize(value1,value2)){
				isModified=true;
			}else{
				return compareCollectionValues(value1,value2);
			}
		}
		return isModified;
	}
	
	private boolean compareNonNull(Object value1, Object value2){
		return (Objects.nonNull(value1) && Objects.nonNull(value2));
	}
	
	private boolean compareCollectionValues(List<String> value1, List<String> value2) {
		List<String> tempValue=value1;
		tempValue.retainAll(value2);
		return compareCollectionSize(tempValue,value2);
	}

	private boolean compareCollectionSize(List<String> value1,
			List<String> value2){
		boolean isModified= false;
		if(value1.size()!=value2.size()){
			isModified=true;
		}
		return isModified;
	}
	
	private boolean compareNull(Object value1, Object value2) {
		boolean isModified= false;
		if((Objects.nonNull(value1) && Objects.isNull(value2))
				|| (Objects.nonNull(value2) && Objects.isNull(value1))){
			isModified=true;
		}
		return isModified;
	}
	
	/**
	 * mapXmlHeaders
	 */
	private String mapXmlHeaders(String xml) {
		String header = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n";
		header = header + "<soapenv:Body>";
		String tailer = " </soapenv:Body>\r\n</soapenv:Envelope>";
		xml = header + xml + tailer;
		xml = xml.replace("<header>",
				"<header xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">");
		xml = xml.replace(
				"<ns2:performIPServiceActivation xmlns:ns2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\">",
				"<p:performIPServiceActivation xmlns:p=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\">");
		xml = xml.replace("<orderDetails>",
				"<orderDetails xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">");
		xml = xml.replace("<actionRequired>",
				"<actionRequired xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<requestID>", "<requestID xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<originatingSystem>",
				"<originatingSystem xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<originationTime>",
				"<originationTime xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<authUser>", "<authUser xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<location>", "<location xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<addressLine1>", "<addressLine1 xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<city>", "<city xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<state>", "<state xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<pincode>", "<pincode xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<country>", "<country xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");

		xml = xml.replaceAll("<speed>", "<speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replaceAll("<unit>", "<unit xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");

		xml = xml.replaceAll("<isCEACEConfigurable>",
				"<isCEACEConfigurable xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");
		xml = xml.replaceAll("<wanInterface>",
				"<wanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");
		xml = xml.replaceAll("<lanInterface>",
				"<lanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");
		xml = xml.replaceAll("<snmpServerCommunity>",
				"<snmpServerCommunity xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");

		xml = xml.replaceAll("<VPN>",
				"<VPN xmlns:out2=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\">");
		xml = xml.replaceAll("<vpnId>", "<vpnId xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<topology>", "<topology xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<leg>", "<leg xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<vpnType>", "<vpnType xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<SolutionID>",
				"<SolutionID xmlns:out2=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\">");
		xml = xml.replaceAll("<PEWANStaticRoutes>",
				"<PEWANStaticRoutes xmlns:out3=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replaceAll("<isEnabled>", "<isEnabled xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<address>", "<address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
		xml = xml.replace("</ns2:performIPServiceActivation>", "</p:performIPServiceActivation>");
		return xml;
	}
	
	/**
	 * This method is the main method for generating the xml for RF Config
	 * generateDummyRFConfigXml
	 * 
	 * @param serviceId
	 * @return
	 */
	public String generateDummyRFConfigXml(ServiceDetail currentActiveServiceDetail, String actionMode, String requestId) {
		LOGGER.info("Inside AbstractGvpnMACDConfiguration.generateDummyRFConfigXml with input serviceId {}", currentActiveServiceDetail.getId());
		String response = null;
		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		OrderDetail orderDetail = currentActiveServiceDetail.getOrderDetail();
		performIPServiceActivation
				.setHeader(constructAceHeader(currentActiveServiceDetail.getServiceId(), orderDetail, actionMode, requestId));
		performIPServiceActivation.setOrderDetails(createDummyRfOrderDetails(currentActiveServiceDetail, orderDetail));
		response = jaxbObjectToXML(performIPServiceActivation);
		return mapXmlHeaders(response);
	}

	private OrderInfo3 createDummyRfOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		 LOGGER.info("AsbtractGVPNMACDConfig.createDummyRfOrderDetails");
		OrderInfo3 orderInfo3 = new OrderInfo3();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			orderInfo3.setServiceId("DUM"+serviceDetail.getServiceId());
			orderInfo3.setOldServiceId("DUM"+serviceDetail.getOldServiceId());
			orderInfo3.setOrderType(OrderType2.fromValue("CHANGE_ORDER"));
			orderInfo3.setService(createDummyRfServiceDetails(serviceDetail, orderDetail));
			if (StringUtils.isNotBlank(orderDetail.getCopfId())){
				orderInfo3.setCopfId(orderDetail.getCopfId());
			}
			orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
			orderInfo3.setIsDowntimeRequired(true);
			orderInfo3.setIsObjectInstanceModified(true);
			orderInfo3.setCustomerDetails(createCustomerDetails(orderDetail, serviceDetail,true));
		}
		return orderInfo3;
	}


	private Service createDummyRfServiceDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		 LOGGER.info("AsbtractGVPNMACDConfig.createDummyRfServiceDetails");
		OrderInfo3.Service service = new OrderInfo3.Service();
		Set<LmComponent> lmComponents = serviceDetail.getLmComponents();
		for (LmComponent lmComponent : lmComponents) {
			createDummyRfCambium(serviceDetail, service, lmComponent);
			createDummyRfRadwin(serviceDetail, service, lmComponent);
		}
		return service;
	}


	private void createDummyRfRadwin(ServiceDetail serviceDetail, Service service, LmComponent lmComponent) {
		 LOGGER.info("AsbtractGVPNMACDConfig.createDummyRfRadwin");
		for (RadwinLastmile radwinLastmile : lmComponent.getRadwinLastmiles()) {
			Radwin5KLastmile radwin5kLastMile = new Radwin5KLastmile();
			radwin5kLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			radwin5kLastMile.setServiceType(serviceDetail.getServiceType());

			if (serviceDetail.getBurstableBw() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
					}
				}
				radwin5kLastMile.setPortSpeed(portBandwidth);
			} else {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
					}
				}
				radwin5kLastMile.setPortSpeed(portBandwidth);
			}
			MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstRadwinDetails) && Objects.nonNull(mstRadwinDetails.getNetpRefId()) && !mstRadwinDetails.getNetpRefId().isEmpty()){
				radwin5kLastMile.setInstanceID(mstRadwinDetails.getNetpRefId()+new Timestamp(new Date().getTime()));
			}else if(Objects.nonNull(serviceDetail.getNetpRefid())){
				radwin5kLastMile.setInstanceID(serviceDetail.getNetpRefid()+new Timestamp(new Date().getTime()));
			}
			if (StringUtils.isNotBlank(radwinLastmile.getBtsIp()))
				radwin5kLastMile.setBTSIP(radwinLastmile.getBtsIp());
			if (StringUtils.isNotBlank(radwinLastmile.getBtsName()))
				radwin5kLastMile.setBSName(radwinLastmile.getBtsName());
			HSUConfigParameters hsuConfigParameters = new HSUConfigParameters();
			if (StringUtils.isNotBlank(radwinLastmile.getSiteName())){
				String name="DUM"+radwinLastmile.getSiteName();
				if (name != null && name.length() > 60) {
					name=name.substring(0, 61);
				}
				hsuConfigParameters.setName(name);
			}
				
			if (StringUtils.isNotBlank(radwinLastmile.getSectorId()))
				hsuConfigParameters.setSectorID(radwinLastmile.getSectorId());
			if (StringUtils.isNotBlank(radwinLastmile.getFrequency()))
				hsuConfigParameters.setFrequency(radwinLastmile.getFrequency());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteLat()))
				hsuConfigParameters.setLatitude(radwinLastmile.getSiteLat().trim());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteLong()))
				hsuConfigParameters.setLongitude(radwinLastmile.getSiteLong().trim());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteContact())){
				String name="DUM"+radwinLastmile.getSiteContact();
				if(name.length()>30){
					name=name.substring(0, 30);
				}
				hsuConfigParameters.setSiteContact(name);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getCustomerLocation()))
				hsuConfigParameters.setCustomerLocation("DUM"+radwinLastmile.getCustomerLocation());
			if (StringUtils.isNotBlank(radwinLastmile.getHsuIp())) {
				IPAddress hscuMgmtIp = new IPAddress();
				hscuMgmtIp.setAddress(radwinLastmile.getHsuIp());
				hsuConfigParameters.setHsuMgmtIP(hscuMgmtIp);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getHsuSubnet())) {
				IPAddress hscuMgmtSubnet = new IPAddress();
				hscuMgmtSubnet.setAddress(radwinLastmile.getHsuSubnet());
				hsuConfigParameters.setHsuMgmtSubnet(hscuMgmtSubnet);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getGatewayIp())) {
				IPAddress hscuGatewayIp = new IPAddress();
				hscuGatewayIp.setAddress(radwinLastmile.getGatewayIp());
				hsuConfigParameters.setHsuGatewayIP(hscuGatewayIp);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getHsuMacAddr()))
				hsuConfigParameters.setHsuMacAddress(radwinLastmile.getHsuMacAddr().replaceAll(" ", ":").replaceAll("-", ":"));
			if (StringUtils.isNotBlank(radwinLastmile.getMgmtVlanid()))
				hsuConfigParameters.setManagementVlanID(ActivationUtils.toInteger(radwinLastmile.getMgmtVlanid().trim()));
			if (StringUtils.isNotBlank(radwinLastmile.getNtpServerIp())) {
				IPAddress ntpServerIp = new IPAddress();
				ntpServerIp.setAddress(radwinLastmile.getNtpServerIp());
				hsuConfigParameters.setNtpServerIP(ntpServerIp);
			}
			if (radwinLastmile.getCustomerLocation() != null)
				hsuConfigParameters.setCustomerLocation(radwinLastmile.getCustomerLocation());
			if (radwinLastmile.getSiteName() != null)
				hsuConfigParameters.setName(radwinLastmile.getSiteName());
			if (StringUtils.isNotBlank(radwinLastmile.getNtpOffset()))
				hsuConfigParameters.setNtpOffset(ActivationUtils.toInteger(radwinLastmile.getNtpOffset()));
			Protocol protocol = new Protocol();
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolSnmp()))
				protocol.setSnmp(radwinLastmile.getProtocolSnmp());
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolWebinterface()))
				protocol.setWebAccess(radwinLastmile.getProtocolWebinterface());
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolTelnet()))
				protocol.setTelnet(radwinLastmile.getProtocolTelnet());
			hsuConfigParameters.setProtocol(protocol);
			if (StringUtils.isNotBlank(radwinLastmile.getReqd_tx_power()))
				hsuConfigParameters.setRequiredTxPower(ActivationUtils.toInteger(radwinLastmile.getReqd_tx_power()));
			if (StringUtils.isNotBlank(radwinLastmile.getEthernet_Port_Config()))
				hsuConfigParameters.setEthernetPortConfiguration(radwinLastmile.getEthernet_Port_Config());
			radwin5kLastMile.setHSUConfigParameters(hsuConfigParameters);
			HBSConfigParameters hbsConfigParameters = new HBSConfigParameters();
			if (StringUtils.isNotBlank(radwinLastmile.getVlanMode()))
				hbsConfigParameters.setVlanMode((!radwinLastmile.getVlanMode().contains("NOT_APPLICABLE"))
						? radwinLastmile.getVlanMode().toLowerCase()
						: radwinLastmile.getVlanMode());
			if (StringUtils.isNotBlank(radwinLastmile.getDataVlanid()))
				hbsConfigParameters.setDataVlanID(ActivationUtils.toInteger(radwinLastmile.getDataVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getDataVlanPriority()))
				hbsConfigParameters
						.setDataVlanPriority(ActivationUtils.toInteger(radwinLastmile.getDataVlanPriority()));
			if (StringUtils.isNotBlank(radwinLastmile.getAllowedVlanid()))
				hbsConfigParameters.setAllowedVlanID(ActivationUtils.toInteger(radwinLastmile.getAllowedVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getHsuEgressTraffic()))
				hbsConfigParameters.setHsuEgressTraffic(radwinLastmile.getHsuEgressTraffic().toUpperCase());
			if (StringUtils.isNotBlank(radwinLastmile.getHsuIngressTraffic()))
				hbsConfigParameters.setHsuIngressTraffic(radwinLastmile.getHsuIngressTraffic().toUpperCase());
			if (radwinLastmile.getMirUl() != null)
				hbsConfigParameters.setMirUL(Math.round(radwinLastmile.getMirUl()));
			if (radwinLastmile.getMirDl() != null)
				hbsConfigParameters.setMirDL(Math.round(radwinLastmile.getMirDl()));
			if (radwinLastmile.getUntagVlanId() != null)
				hbsConfigParameters.setUntagVlanID(ActivationUtils.getBooleanValue(radwinLastmile.getUntagVlanId()));
			radwin5kLastMile.setHBSConfigParameters(hbsConfigParameters);
			service.setRadwin5KLastmile(radwin5kLastMile);
		}
	}


	private void createDummyRfCambium(ServiceDetail serviceDetail, Service service, LmComponent lmComponent) {
		 LOGGER.info("AsbtractGVPNMACDConfig.createDummyRfCambium");
		for (com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile camLastMile : lmComponent
				.getCambiumLastmiles()) {
			CambiumLastmile cambiumLastMile = new CambiumLastmile();
			cambiumLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			cambiumLastMile.setServiceType(serviceDetail.getServiceType());
			MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstCambiumDetails) && Objects.nonNull(mstCambiumDetails.getNetpRefId()) && !mstCambiumDetails.getNetpRefId().isEmpty()){
				cambiumLastMile.setInstanceID(mstCambiumDetails.getNetpRefId()+new Timestamp(new Date().getTime()));
			}else if(Objects.nonNull(serviceDetail.getNetpRefid())){
				cambiumLastMile.setInstanceID(serviceDetail.getNetpRefid()+new Timestamp(new Date().getTime()));
			}
			if (StringUtils.isNotBlank(camLastMile.getBsName()))
				cambiumLastMile.setBSName(camLastMile.getBsName());
			if (StringUtils.isNotBlank(camLastMile.getBsIp()))
				cambiumLastMile.setBTSIP(camLastMile.getBsIp());
			if (StringUtils.isNotBlank(serviceDetail.getServiceType()))
				cambiumLastMile.setServiceType(serviceDetail.getServiceType());

			if (camLastMile.getPortSpeed() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				portBandwidth.setSpeed(camLastMile.getPortSpeed());
				if (StringUtils.isNotBlank(camLastMile.getPortSpeedUnit()))
					portBandwidth.setUnit(ActivationUtils.getBandwithUnit(camLastMile.getPortSpeedUnit()));
				cambiumLastMile.setPortSpeed(portBandwidth);
			}
			if (StringUtils.isNotBlank(camLastMile.getSuMacAddress()))
				cambiumLastMile.setSUMACAddress(camLastMile.getSuMacAddress());
			if (StringUtils.isNotBlank(camLastMile.getMgmtIpGateway())) {
				IPV4Address suGatewayIpv4 = new IPV4Address();
				suGatewayIpv4.setAddress(camLastMile.getMgmtIpGateway());
				cambiumLastMile.setGatewayMgmtIP(suGatewayIpv4);
			}
			if (StringUtils.isNotBlank(camLastMile.getMgmtIpForSsSu())) {
				IPV4Address suMgmtIpv4 = new IPV4Address();
				suMgmtIpv4.setAddress(camLastMile.getMgmtIpForSsSu());
				cambiumLastMile.setSUMgmtIP(suMgmtIpv4);
			}
			if (StringUtils.isNotBlank(camLastMile.getMgmtSubnetForSsSu())) {
				IPV4Address suMgtSubnetIpv4 = new IPV4Address();
				suMgtSubnetIpv4.setAddress(camLastMile.getMgmtSubnetForSsSu());
				cambiumLastMile.setSUMgmtSubnet(suMgtSubnetIpv4);
			}
			cambiumLastMile.setAPIVersion("PMP.SM(v13.1.3)");
			if (StringUtils.isNotBlank(camLastMile.getDhcpState()))
				cambiumLastMile.setDHCPState(camLastMile.getDhcpState().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getLinkSpeed()))
				cambiumLastMile.setLinkSpeed(camLastMile.getLinkSpeed().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getRegionCode()))
				cambiumLastMile.setRegionCode(camLastMile.getRegionCode());
			if (camLastMile.getWebpageAutoUpdate() != null)
				cambiumLastMile.setWebpageAutoUpdate(camLastMile.getWebpageAutoUpdate());
			if (StringUtils.isNotBlank(camLastMile.getCustomRadioFrequencyList()))
				cambiumLastMile.setRFScanList(camLastMile.getCustomRadioFrequencyList());
			if (StringUtils.isNotBlank(camLastMile.getBridgeEntryTimeout()))
				cambiumLastMile.setBridgeEntryTimeout(camLastMile.getBridgeEntryTimeout());
			if (StringUtils.isNotBlank(camLastMile.getDynamicRateAdapt())) {
				if (camLastMile.getDynamicRateAdapt().equalsIgnoreCase("1x"))
					cambiumLastMile.setDynamicRateAdapt("0");
				else
					cambiumLastMile.setDynamicRateAdapt("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getMulticastDestinationAddr()))
				cambiumLastMile.setMulticastDestAddress(camLastMile.getMulticastDestinationAddr().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getDeviceDefaultReset()))
				cambiumLastMile.setDeviceDefaultReset(camLastMile.getDeviceDefaultReset());
			if (StringUtils.isNotBlank(camLastMile.getPowerupmodeWithno802_3link())) {
				if (camLastMile.getPowerupmodeWithno802_3link().toLowerCase().contains("operational"))
					cambiumLastMile.setPowerupMode("0");
				else
					cambiumLastMile.setPowerupMode("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getFrameTimingPulseGated()))
				cambiumLastMile.setFrameTimingPulseGated(camLastMile.getFrameTimingPulseGated().toUpperCase());
			if (camLastMile.getColorCode1() != null)
				cambiumLastMile.setColourCode(camLastMile.getColorCode1());
			if (StringUtils.isNotBlank(camLastMile.getTransmitterOutputPower()))
				cambiumLastMile
						.setTransmitterOutputPower(ActivationUtils.toInteger(camLastMile.getTransmitterOutputPower()));
			if (StringUtils.isNotBlank(camLastMile.getLargevcDataq()))
				cambiumLastMile.setEnableLargeDataVCQ(camLastMile.getLargevcDataq().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getInstallationColorCode()))
				cambiumLastMile.setInstallationColourCode(camLastMile.getInstallationColorCode().toUpperCase());
			CambiumAAAConfig cambiumAAAConfig = new CambiumAAAConfig();
			if (StringUtils.isNotBlank(camLastMile.getEthernetAccess()))
				cambiumAAAConfig.setEthernetAccess(camLastMile.getEthernetAccess().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getUserName()))
				cambiumAAAConfig.setUsername(camLastMile.getUserName());
			if (StringUtils.isNotBlank(camLastMile.getAllowlocalloginAfteraaareject())) {
				if (camLastMile.getAllowlocalloginAfteraaareject().toLowerCase().contains("enabled"))
					cambiumAAAConfig.setAllowLocalLoginAfterAAAReject("1");
				else
					cambiumAAAConfig.setAllowLocalLoginAfterAAAReject("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getHipriorityUplinkCir()))
				cambiumAAAConfig.setHighPriorityUplinkCIR(camLastMile.getHipriorityUplinkCir());
			if (StringUtils.isNotBlank(camLastMile.getAuthenticationKey()))
				cambiumAAAConfig.setAuthenticationKey(ActivationUtils.toInteger(camLastMile.getAuthenticationKey()));
			if (StringUtils.isNotBlank(camLastMile.getRealm()))
				cambiumAAAConfig.setRealm(ActivationUtils.toInteger(camLastMile.getRealm()));
			if (StringUtils.isNotBlank(camLastMile.getDeviceaccessTracking()))
				cambiumAAAConfig.setDeviceAccessTracking(camLastMile.getDeviceaccessTracking());
			if (StringUtils.isNotBlank(camLastMile.getLowpriorityDownlinkCir()))
				cambiumAAAConfig.setLowPriorityDownlinkCIR(camLastMile.getLowpriorityDownlinkCir());
			if (StringUtils.isNotBlank(camLastMile.getSelectKey())) {
				if (camLastMile.getSelectKey().toLowerCase().contains("default"))
					cambiumAAAConfig.setSelectKey("0");
				else
					cambiumAAAConfig.setSelectKey("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getEnforceAuthentication())) {
				if (camLastMile.getEnforceAuthentication().toLowerCase().contains("default"))
					cambiumAAAConfig.setEnforceAuth("0");
				else if(camLastMile.getEnforceAuthentication().toUpperCase().contains("AAA"))
					cambiumAAAConfig.setEnforceAuth("1");
				else
					cambiumAAAConfig.setEnforceAuth("2");
			}
			if (StringUtils.isNotBlank(camLastMile.getIdentity())) {
				cambiumAAAConfig.setIdentity(camLastMile.getIdentity());
			}else {
				cambiumAAAConfig.setIdentity(Objects.nonNull(camLastMile.getSuMacAddress()) && !camLastMile.getSuMacAddress().isEmpty()?camLastMile.getSuMacAddress().replaceAll(" ", "-"):camLastMile.getSuMacAddress());
			}
			if (StringUtils.isNotBlank(camLastMile.getDownlinkBurstAllocation()))
				cambiumAAAConfig.setDownlinkBurstAllocation(camLastMile.getDownlinkBurstAllocation());
			if (StringUtils.isNotBlank(camLastMile.getLowpriorityUplinkCir()))
				cambiumAAAConfig.setLowPriorityUplinkCIR(camLastMile.getLowpriorityUplinkCir());
			if (StringUtils.isNotBlank(camLastMile.getPassword()))
				cambiumAAAConfig.setPassword(camLastMile.getPassword());
			if (StringUtils.isNotBlank(camLastMile.getUplinkBurstAllocation()))
				cambiumAAAConfig.setUplinkBurstAllocation(camLastMile.getUplinkBurstAllocation());
			if (StringUtils.isNotBlank(camLastMile.getEnableBroadcastMulticastDatarate()))
				cambiumAAAConfig.setEnableBroadcastMulticastRate(camLastMile.getEnableBroadcastMulticastDatarate());
			if (StringUtils.isNotBlank(camLastMile.getPhase1())) {
				if (camLastMile.getPhase1().toUpperCase().contains("EAPTTLS"))
					cambiumAAAConfig.setPhase1("0");
				else
					cambiumAAAConfig.setPhase1("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getServerCommonName()))
				cambiumAAAConfig.setServerCommonName(camLastMile.getServerCommonName());
			if (StringUtils.isNotBlank(camLastMile.getBwUplinkSustainedRate()))
				cambiumAAAConfig.setBandwidthUplinkSustainRate(camLastMile.getBwUplinkSustainedRate());
			if (StringUtils.isNotBlank(camLastMile.getHipriorityChannel()))
				cambiumAAAConfig.setHighPriorityChannel(camLastMile.getHipriorityChannel());
			if (StringUtils.isNotBlank(camLastMile.getPhase2())) {
				if (camLastMile.getPhase2().toUpperCase().contains("PAP"))
					cambiumAAAConfig.setPhase2("0");
				else if (camLastMile.getPhase2().toUpperCase().contains("CHAP"))
					cambiumAAAConfig.setPhase2("1");
				else
					cambiumAAAConfig.setPhase2("2");
			}
			if (StringUtils.isNotBlank(camLastMile.getNetworkAccessibility())) {
				if (camLastMile.getNetworkAccessibility().toLowerCase().contains("public"))
					cambiumAAAConfig.setNetworkAccessibility("1");
				else
					cambiumAAAConfig.setNetworkAccessibility("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getBwDownlinkSustainedRate()))
				cambiumAAAConfig.setBandwidthDownlinkSustainRate(camLastMile.getBwDownlinkSustainedRate());
			if (StringUtils.isNotBlank(camLastMile.getUseRealmStatus()))
				cambiumAAAConfig.setUseRealmStatus(camLastMile.getUseRealmStatus());
			if (StringUtils.isNotBlank(camLastMile.getUserAuthenticationMode())) {
				if (camLastMile.getUserAuthenticationMode().toLowerCase().contains("than"))
					cambiumAAAConfig.setUserAuthenticationMode("2");
				else if (camLastMile.getUserAuthenticationMode().toLowerCase().contains("local"))
					cambiumAAAConfig.setUserAuthenticationMode("1");
				else
					cambiumAAAConfig.setUserAuthenticationMode("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getHipriorityDownlinkCir()))
				cambiumAAAConfig.setHighPriorityDownlinkCIR(camLastMile.getHipriorityDownlinkCir());
			cambiumLastMile.setCambiumAAAConfig(cambiumAAAConfig);
			CambiumAntennaParameters cambiumAntennaParameters = new CambiumAntennaParameters();
			if (StringUtils.isNotBlank(camLastMile.getSmHeight())){
				Double smHeight = Double.valueOf(camLastMile.getSmHeight());
				Integer value = smHeight.intValue();
				cambiumAntennaParameters.setHeight(value.toString());
			}
			if (StringUtils.isNotBlank(camLastMile.getLatitudeSettings()))
				cambiumAntennaParameters.setLattitude(camLastMile.getLatitudeSettings().trim());
			if (StringUtils.isNotBlank(camLastMile.getLongitudeSettings()))
				cambiumAntennaParameters.setLongitude(camLastMile.getLongitudeSettings().trim());
			cambiumLastMile.setCambiumAntennaParameters(cambiumAntennaParameters);
			CambiumVLANAttributes cambiumVLANAttributes = new CambiumVLANAttributes();
			if (StringUtils.isNotBlank(camLastMile.getAllowFrametypes())) {
				if (camLastMile.getAllowFrametypes().toLowerCase().contains("untag"))
					cambiumVLANAttributes.setAllowFrameTypes("2");
				else if (camLastMile.getAllowFrametypes().toLowerCase().contains("tag"))
					cambiumVLANAttributes.setAllowFrameTypes("1");
				else
					cambiumVLANAttributes.setAllowFrameTypes("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getDynamicLearning()))
				cambiumVLANAttributes.setDynamicLearning(camLastMile.getDynamicLearning().toUpperCase());
			if (camLastMile.getVlanAgeingTimeout() != null)
				cambiumVLANAttributes.setVlanAgingTimeout(ActivationUtils.toString(camLastMile.getVlanAgeingTimeout()));
			if (StringUtils.isNotBlank(camLastMile.getMgmtVid()))
				cambiumVLANAttributes.setManagementVID(camLastMile.getMgmtVid());
			if (StringUtils.isNotBlank(camLastMile.getDefaultPortVid()))
				cambiumVLANAttributes.setDefaultPortVID(ActivationUtils.toInteger(camLastMile.getDefaultPortVid()));
			if (StringUtils.isNotBlank(camLastMile.getSmMgmtVidPassthrough()))
				cambiumVLANAttributes
						.setSSManagementVIDPassthrough(camLastMile.getSmMgmtVidPassthrough().toUpperCase());
			if (camLastMile.getProviderVid() != null)
				cambiumVLANAttributes.setProviderVID(ActivationUtils.toString(camLastMile.getProviderVid()));
			if (StringUtils.isNotBlank(camLastMile.getMappedMacAddress())) {
				MACAddressVIDMap macAddressVIDMap = new MACAddressVIDMap();
				macAddressVIDMap.setMACAddress(camLastMile.getMappedMacAddress());
				cambiumVLANAttributes.getMappedMACAddress().add(macAddressVIDMap);
			}
			if (StringUtils.isNotBlank(camLastMile.getMappedVid1()))
				cambiumVLANAttributes.setMappedVID1(camLastMile.getMappedVid1());
			if (camLastMile.getMappedVid2() != null)
				cambiumVLANAttributes.setMappedVID2(ActivationUtils.toString(camLastMile.getMappedVid2()));
			if (StringUtils.isNotBlank(camLastMile.getVlanPorttype())) {
				if (camLastMile.getVlanPorttype().toLowerCase().contains("qinq"))
					cambiumVLANAttributes.setVlanPortType("1");
				else
					cambiumVLANAttributes.setVlanPortType("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getAcceptQinqFrames()))
				cambiumVLANAttributes.setAcceptQinQFrames(camLastMile.getAcceptQinqFrames().toUpperCase());
			if(camLastMile.getBsName().toLowerCase().contains("lab") || camLastMile.getBsName().toLowerCase().contains("test")){
				cambiumVLANAttributes.setManagementVID("3551");
				cambiumVLANAttributes.setDefaultPortVID(3551);
				cambiumVLANAttributes.setMappedVID1("3551");
				cambiumVLANAttributes.setMappedVID2("3551");
			}

			cambiumLastMile.setCambiumVLANAttributes(cambiumVLANAttributes);
			SNMPParameters snmpParameters = new SNMPParameters();

			IPAddressList ipSnmpTrapIpAddressList = new IPAddressList();
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp1())) {
				IPAddress snmpIp1 = new IPV4Address();
				snmpIp1.setAddress(camLastMile.getSnmpTrapIp1());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp1);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp2())) {
				IPAddress snmpIp2 = new IPV4Address();
				snmpIp2.setAddress(camLastMile.getSnmpTrapIp2());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp2);

			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp3())) {
				IPAddress snmpIp3 = new IPV4Address();
				snmpIp3.setAddress(camLastMile.getSnmpTrapIp3());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp3);
			}

			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp4())) {
				IPAddress snmpIp4 = new IPV4Address();
				snmpIp4.setAddress(camLastMile.getSnmpTrapIp4());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp4);
			}

			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp5())) {
				IPAddress snmpIp5 = new IPV4Address();
				snmpIp5.setAddress(camLastMile.getSnmpTrapIp5());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp5);
			}

			snmpParameters.setSNMPTrapIPList(ipSnmpTrapIpAddressList);

			IPAddressList ipSnmpAccessAddressList = new IPAddressList();
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp1())) {
				IPAddress accessIp1 = new IPV4Address();
				accessIp1.setAddress(camLastMile.getSnmpAccessingIp1());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp1);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp2())) {
				IPAddress accessIp2 = new IPV4Address();
				accessIp2.setAddress(camLastMile.getSnmpAccessingIp2());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp2);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp3())) {
				IPAddress accessIp3 = new IPV4Address();
				accessIp3.setAddress(camLastMile.getSnmpAccessingIp3());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp3);
			}

			snmpParameters.setSNMPAccessingIPList(ipSnmpAccessAddressList);
			cambiumLastMile.setSNMPParameters(snmpParameters);
			if (StringUtils.isNotBlank(camLastMile.getSiteinfoViewabletoGuestusers()))
				cambiumLastMile.setViewableToGuestUsers(camLastMile.getSiteinfoViewabletoGuestusers().toUpperCase());
			CambiumSite cambiumSite = new CambiumSite();
			if (StringUtils.isNotBlank(camLastMile.getSiteContact()))
				cambiumSite.setContact("DUM"+camLastMile.getSiteContact());
			if (StringUtils.isNotBlank(camLastMile.getSiteLocation()))
				cambiumSite.setLocation(camLastMile.getSiteLocation());
			if (StringUtils.isNotBlank(camLastMile.getSiteName()))
				cambiumSite.setName(camLastMile.getSiteName().substring(0, camLastMile.getSiteName().lastIndexOf("-"))+"-DUM"+serviceDetail.getServiceId());
			cambiumLastMile.setCambiumSiteDetails(cambiumSite);
			if (StringUtils.isNotBlank(camLastMile.getDeviceType()))
				cambiumLastMile.setDeviceType(camLastMile.getDeviceType());

			if(camLastMile.getBsName().toLowerCase().contains("lab") || camLastMile.getBsName().toLowerCase().contains("test"))
				cambiumLastMile.setColourCode("252");
			service.setCambiumLastmile(cambiumLastMile);
		}
	}


	/**
	 * This method is the main method for generating the xml for RF Config
	 * generateRFConfigXml
	 * 
	 * @param serviceId
	 * @return
	 */
	public String generateRFConfigXml(ServiceDetail currentActiveServiceDetail, String actionMode, String requestId) {
		LOGGER.info("Inside generateRFConfigXml with input serviceId {}", currentActiveServiceDetail.getId());
		String response = null;
		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		OrderDetail orderDetail = currentActiveServiceDetail.getOrderDetail();
		performIPServiceActivation
				.setHeader(constructAceHeader(currentActiveServiceDetail.getServiceId(), orderDetail, actionMode, requestId));
		performIPServiceActivation.setOrderDetails(createRfOrderDetails(currentActiveServiceDetail, orderDetail));
		// Get PrevActiveServiceDetail based on Order Type condition
		String orderType=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderType(currentActiveServiceDetail, orderDetail);
		LOGGER.info("Order Type {}", orderType);
		ServiceDetail prevActiveServiceDetail =null;
		if(!orderType.equalsIgnoreCase("NEW")){
									prevActiveServiceDetail = serviceDetailRepository
											.findFirstByServiceIdAndServiceStateOrderByIdDesc(currentActiveServiceDetail.getServiceId(),TaskStatusConstants.ACTIVE);
		}
		if(Objects.nonNull(prevActiveServiceDetail)){
			LmComponent currentLmComponent = currentActiveServiceDetail.getLmComponents().stream().filter(lm -> lm.getEndDate() == null)
					.findFirst().orElse(null);
			if(Objects.nonNull(currentLmComponent) && Objects.nonNull(currentLmComponent.getLmOnwlProvider()) && currentLmComponent.getLmOnwlProvider().toLowerCase().contains("radwin")){
				LOGGER.info("Radwin");
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getRadwin5KLastmile().setIsObjectInstanceModified(true);
			}else if(Objects.nonNull(currentLmComponent) && Objects.nonNull(currentLmComponent.getLmOnwlProvider()) && currentLmComponent.getLmOnwlProvider().toLowerCase().contains("cambium")){
				LOGGER.info("Cambium");
				performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
				performIPServiceActivation.getOrderDetails().getService().getCambiumLastmile().setIsObjectInstanceModified(true);
			}
			//Validate isDevice modified
			//isDeviceModified(performIPServiceActivation,prevActiveServiceDetail,currentActiveServiceDetail);	
		}
		response = jaxbObjectToXML(performIPServiceActivation);
		return mapXmlHeaders(response);
	}

	private void isDeviceModified(PerformIPServiceActivation performIPServiceActivation,
					ServiceDetail prevActiveServiceDetail, ServiceDetail currentActiveServiceDetail) {
			LOGGER.info("isDeviceModified");
				LmComponent prevLmComponent = prevActiveServiceDetail.getLmComponents().stream().filter(lm -> lm.getEndDate() == null)
						.findFirst().orElse(null);
				LmComponent currentLmComponent = currentActiveServiceDetail.getLmComponents().stream().filter(lm -> lm.getEndDate() == null)
						.findFirst().orElse(null);
				if(compareNonNull(prevLmComponent,currentLmComponent)){
					LOGGER.info("LmComponent exists");
					Set<RadwinLastmile> prevRadwinLastMileSet=prevLmComponent.getRadwinLastmiles();
					Set<RadwinLastmile> currentRadwinLastMileSet=currentLmComponent.getRadwinLastmiles();
					Set<com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile> prevCambiumLastmileSet=prevLmComponent.getCambiumLastmiles();
					Set<com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile> currentCambiumLastmileSet=currentLmComponent.getCambiumLastmiles();
					Set<WimaxLastmile> prevWimaxLastMileSet=prevLmComponent.getWimaxLastmiles();
					validateIsObjectModified(performIPServiceActivation,prevRadwinLastMileSet,currentRadwinLastMileSet,prevCambiumLastmileSet,prevWimaxLastMileSet,currentCambiumLastmileSet);
				}
			}

	private void validateIsObjectModified(PerformIPServiceActivation performIPServiceActivation,
					Set<RadwinLastmile> prevRadwinLastMileSet, Set<RadwinLastmile> currentRadwinLastMileSet,
					Set<com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile> prevCambiumLastmileSet,
					Set<WimaxLastmile> prevWimaxLastMileSet,Set<com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile> currentCambiumLastmileSet) {
			LOGGER.info("validateIsObjectModified");	
			if(compareNull(prevRadwinLastMileSet,currentRadwinLastMileSet)
						|| compareNull(prevCambiumLastmileSet,currentCambiumLastmileSet)){
				LOGGER.info("Radwin or Cambium");
					performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
					if(Objects.isNull(prevRadwinLastMileSet) && Objects.nonNull(currentCambiumLastmileSet) || 
							(Objects.isNull(prevWimaxLastMileSet) && Objects.nonNull(currentCambiumLastmileSet))){
						LOGGER.info("Cambium exists");
						performIPServiceActivation.getOrderDetails().getService().getCambiumLastmile().setIsObjectInstanceModified(true);
					}else if(Objects.isNull(prevCambiumLastmileSet) && Objects.nonNull(currentRadwinLastMileSet) ||
								(Objects.isNull(prevWimaxLastMileSet) && Objects.nonNull(currentRadwinLastMileSet))){
						LOGGER.info("Radwin exists");
						performIPServiceActivation.getOrderDetails().getService().getRadwin5KLastmile().setIsObjectInstanceModified(true);
					}
				}else if(compareNonNull(prevRadwinLastMileSet, currentRadwinLastMileSet)){
					//isRadwinObjectModified(performIPServiceActivation,prevRadwinLastMileSet,currentRadwinLastMileSet);
					performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
					performIPServiceActivation.getOrderDetails().getService().getRadwin5KLastmile().setIsObjectInstanceModified(true);
				}else if(compareNonNull(prevCambiumLastmileSet, currentCambiumLastmileSet)){
					//isCambiumbjectModified(performIPServiceActivation,prevCambiumLastmileSet,currentCambiumLastmileSet);
					performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
					performIPServiceActivation.getOrderDetails().getService().getCambiumLastmile().setIsObjectInstanceModified(true);
				}
			}

	private void isCambiumbjectModified(
					PerformIPServiceActivation performIPServiceActivation, Set<com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile> prevCambiumLastmileSet,
					Set<com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile> currentCambiumLastmileSet) {
				com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile prevCambiumLastmile = prevCambiumLastmileSet.stream().findFirst().orElse(null);
				com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile currentCambiumLastmile = currentCambiumLastmileSet.stream().findFirst().orElse(null);
				if((compareNonNull(prevCambiumLastmile, currentCambiumLastmile)) && 
					(compareStrings(prevCambiumLastmile.getBsName(),currentCambiumLastmile.getBsName()) || compareStrings(prevCambiumLastmile.getBsIp(),currentCambiumLastmile.getBsIp()) || compareStrings(prevCambiumLastmile.getMgmtIpGateway(),currentCambiumLastmile.getMgmtIpGateway())
					 || compareFloat(prevCambiumLastmile.getPortSpeed(),currentCambiumLastmile.getPortSpeed()) || compareStrings(prevCambiumLastmile.getPortSpeedUnit(),currentCambiumLastmile.getPortSpeedUnit()) || compareStrings(prevCambiumLastmile.getSuMacAddress(),currentCambiumLastmile.getSuMacAddress())
					 || compareStrings(prevCambiumLastmile.getMgmtIpForSsSu(),currentCambiumLastmile.getMgmtIpForSsSu()) || compareStrings(prevCambiumLastmile.getMgmtIpGateway(),currentCambiumLastmile.getMgmtIpGateway()) || compareStrings(prevCambiumLastmile.getRegion(),currentCambiumLastmile.getRegion())
					 || compareStrings(prevCambiumLastmile.getRegionCode(),currentCambiumLastmile.getRegionCode()) || compareStrings(prevCambiumLastmile.getHomeRegion(),currentCambiumLastmile.getHomeRegion()) || compareStrings(prevCambiumLastmile.getLinkSpeed(),currentCambiumLastmile.getLinkSpeed())
					 || compareStrings(prevCambiumLastmile.getCustomRadioFrequencyList(),currentCambiumLastmile.getCustomRadioFrequencyList()) || compareStrings(prevCambiumLastmile.getDeviceType(),currentCambiumLastmile.getDeviceType()) || compareStrings(prevCambiumLastmile.getFrameTimingPulseGated(),currentCambiumLastmile.getFrameTimingPulseGated())
					 || compareStrings(prevCambiumLastmile.getInstallationColorCode(),currentCambiumLastmile.getInstallationColorCode()) || compareStrings(prevCambiumLastmile.getColorCode1(),currentCambiumLastmile.getColorCode1()) || compareStrings(prevCambiumLastmile.getAuthenticationKey(),currentCambiumLastmile.getAuthenticationKey())
					 || compareStrings(prevCambiumLastmile.getRealm(),currentCambiumLastmile.getRealm()) || compareStrings(prevCambiumLastmile.getEnforceAuthentication(),currentCambiumLastmile.getEnforceAuthentication()) || compareStrings(prevCambiumLastmile.getDownlinkBurstAllocation(),currentCambiumLastmile.getDownlinkBurstAllocation())
					 ||	compareStrings(prevCambiumLastmile.getUplinkBurstAllocation(),currentCambiumLastmile.getUplinkBurstAllocation()) || compareStrings(prevCambiumLastmile.getBwUplinkSustainedRate(),currentCambiumLastmile.getBwUplinkSustainedRate()) || compareStrings(prevCambiumLastmile.getBwDownlinkSustainedRate(),currentCambiumLastmile.getBwDownlinkSustainedRate())
					 ||	compareStrings(prevCambiumLastmile.getHipriorityDownlinkCir(),currentCambiumLastmile.getHipriorityDownlinkCir()) ||	compareStrings(prevCambiumLastmile.getHipriorityUplinkCir(),currentCambiumLastmile.getHipriorityUplinkCir()) ||	compareStrings(prevCambiumLastmile.getLowpriorityUplinkCir(),currentCambiumLastmile.getLowpriorityUplinkCir())
					 || compareStrings(prevCambiumLastmile.getLowpriorityDownlinkCir(),currentCambiumLastmile.getLowpriorityDownlinkCir()) || compareStrings(prevCambiumLastmile.getHipriorityChannel(),currentCambiumLastmile.getHipriorityChannel()) || compareStrings(prevCambiumLastmile.getSmHeight(),currentCambiumLastmile.getSmHeight())
					 || compareStrings(prevCambiumLastmile.getLatitudeSettings(),currentCambiumLastmile.getLatitudeSettings()) || compareStrings(prevCambiumLastmile.getLongitudeSettings(),currentCambiumLastmile.getLongitudeSettings()) || compareStrings(prevCambiumLastmile.getMgmtVid(),currentCambiumLastmile.getMgmtVid())
					 || compareStrings(prevCambiumLastmile.getDefaultPortVid(),currentCambiumLastmile.getDefaultPortVid()) || compareIntegers(prevCambiumLastmile.getProviderVid(),currentCambiumLastmile.getProviderVid()) || compareIntegers(prevCambiumLastmile.getMappedVid2(),currentCambiumLastmile.getMappedVid2())
					 || compareStrings(prevCambiumLastmile.getSiteContact(),currentCambiumLastmile.getSiteContact()) || compareStrings(prevCambiumLastmile.getSiteName(),currentCambiumLastmile.getSiteName()) || compareStrings(prevCambiumLastmile.getSiteLocation(),currentCambiumLastmile.getSiteLocation())
					 || compareStrings(prevCambiumLastmile.getDhcpState(),currentCambiumLastmile.getDhcpState()) || compareStrings(prevCambiumLastmile.getBridgeEntryTimeout(),currentCambiumLastmile.getBridgeEntryTimeout()) ||	compareStrings(prevCambiumLastmile.getDynamicRateAdapt(),currentCambiumLastmile.getDynamicRateAdapt())
					 ||	compareStrings(prevCambiumLastmile.getMulticastDestinationAddr(),currentCambiumLastmile.getMulticastDestinationAddr()) || compareStrings(prevCambiumLastmile.getDeviceDefaultReset(),currentCambiumLastmile.getDeviceDefaultReset()) || compareStrings(prevCambiumLastmile.getPowerupmodeWithno802_3link(),currentCambiumLastmile.getPowerupmodeWithno802_3link())
					 || compareStrings(prevCambiumLastmile.getTransmitterOutputPower(),currentCambiumLastmile.getTransmitterOutputPower()) || compareStrings(prevCambiumLastmile.getLargevcDataq(),currentCambiumLastmile.getLargevcDataq()) || compareStrings(prevCambiumLastmile.getEthernetAccess(),currentCambiumLastmile.getEthernetAccess()) 
					 || compareStrings(prevCambiumLastmile.getUserName(),currentCambiumLastmile.getUserName()) || compareStrings(prevCambiumLastmile.getAllowlocalloginAfteraaareject(),currentCambiumLastmile.getAllowlocalloginAfteraaareject()) || compareStrings(prevCambiumLastmile.getDeviceaccessTracking(),currentCambiumLastmile.getDeviceaccessTracking())
					 || compareStrings(prevCambiumLastmile.getSelectKey(),currentCambiumLastmile.getSelectKey()) ||	compareStrings(prevCambiumLastmile.getIdentity(),currentCambiumLastmile.getIdentity()) || compareStrings(prevCambiumLastmile.getPassword(),currentCambiumLastmile.getPassword())
					 ||	compareStrings(prevCambiumLastmile.getPhase1(),currentCambiumLastmile.getPhase1()) || compareStrings(prevCambiumLastmile.getEnableBroadcastMulticastDatarate(),currentCambiumLastmile.getEnableBroadcastMulticastDatarate()) || compareStrings(prevCambiumLastmile.getServerCommonName(),currentCambiumLastmile.getServerCommonName())
					 || compareStrings(prevCambiumLastmile.getPhase2(),currentCambiumLastmile.getPhase2()) || compareStrings(prevCambiumLastmile.getNetworkAccessibility(),currentCambiumLastmile.getNetworkAccessibility()) || compareStrings(prevCambiumLastmile.getUseRealmStatus(),currentCambiumLastmile.getUseRealmStatus())
					 || compareStrings(prevCambiumLastmile.getUserAuthenticationMode(),currentCambiumLastmile.getUserAuthenticationMode()) || compareStrings(prevCambiumLastmile.getAllowFrametypes(),currentCambiumLastmile.getAllowFrametypes()) || compareStrings(prevCambiumLastmile.getDynamicLearning(),currentCambiumLastmile.getDynamicLearning())
					 || compareIntegers(prevCambiumLastmile.getVlanAgeingTimeout(),currentCambiumLastmile.getVlanAgeingTimeout()) || compareStrings(prevCambiumLastmile.getSmMgmtVidPassthrough(),currentCambiumLastmile.getSmMgmtVidPassthrough()) || compareStrings(prevCambiumLastmile.getMappedVid1(),currentCambiumLastmile.getMappedVid1())
					 || compareStrings(prevCambiumLastmile.getMappedMacAddress(),currentCambiumLastmile.getMappedMacAddress()) || compareStrings(prevCambiumLastmile.getVlanPorttype(),currentCambiumLastmile.getVlanPorttype()) || compareStrings(prevCambiumLastmile.getAcceptQinqFrames(),currentCambiumLastmile.getAcceptQinqFrames())
					 || compareStrings(prevCambiumLastmile.getSnmpTrapIp1(),currentCambiumLastmile.getSnmpTrapIp1()) || compareStrings(prevCambiumLastmile.getSnmpTrapIp2(),currentCambiumLastmile.getSnmpTrapIp2()) || compareStrings(prevCambiumLastmile.getSnmpTrapIp3(),currentCambiumLastmile.getSnmpTrapIp3())
					 || compareStrings(prevCambiumLastmile.getSnmpTrapIp4(),currentCambiumLastmile.getSnmpTrapIp4()) || compareStrings(prevCambiumLastmile.getSnmpTrapIp5(),currentCambiumLastmile.getSnmpTrapIp5()) || compareStrings(prevCambiumLastmile.getSnmpAccessingIp1(),currentCambiumLastmile.getSnmpAccessingIp1())
					 || compareStrings(prevCambiumLastmile.getSnmpAccessingIp2(),currentCambiumLastmile.getSnmpAccessingIp2()) || compareStrings(prevCambiumLastmile.getSnmpAccessingIp3(),currentCambiumLastmile.getSnmpAccessingIp3()) || compareStrings(prevCambiumLastmile.getSiteinfoViewabletoGuestusers(),currentCambiumLastmile.getSiteinfoViewabletoGuestusers())
					 || compareStrings(prevCambiumLastmile.getCambiumLastmilecol(),currentCambiumLastmile.getCambiumLastmilecol()) || compareStrings(prevCambiumLastmile.getProvider(),currentCambiumLastmile.getProvider()) || compareStrings(prevCambiumLastmile.getSnmpAccessingSubnetMask1(),currentCambiumLastmile.getSnmpAccessingSubnetMask1())
					 || compareStrings(prevCambiumLastmile.getSnmpAccessingSubnetMask2(),currentCambiumLastmile.getSnmpAccessingSubnetMask2()) || compareStrings(prevCambiumLastmile.getSnmpAccessingSubnetMask3(),currentCambiumLastmile.getSnmpAccessingSubnetMask3()) || compareIntegers(prevCambiumLastmile.getWebpageAutoUpdate(),currentCambiumLastmile.getWebpageAutoUpdate())
					 || compareStrings(prevCambiumLastmile.getTechnology(),currentCambiumLastmile.getTechnology()))){
					performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
					performIPServiceActivation.getOrderDetails().getService().getCambiumLastmile().setIsObjectInstanceModified(true);
				}
						
				
		}

	private void isRadwinObjectModified(PerformIPServiceActivation performIPServiceActivation,Set<RadwinLastmile> prevRadwinLastMileSet,
					Set<RadwinLastmile> currentRadwinLastMileSet) {
				RadwinLastmile prevRadwinLastmile = prevRadwinLastMileSet.stream().findFirst().orElse(null);
				RadwinLastmile currentRadwinLastmile = currentRadwinLastMileSet.stream().findFirst().orElse(null);	
				if((compareNonNull(prevRadwinLastmile, currentRadwinLastmile)) 
						&& (compareStrings(prevRadwinLastmile.getBtsIp(),currentRadwinLastmile.getBtsIp()) ||compareStrings(prevRadwinLastmile.getBtsName(),currentRadwinLastmile.getBtsName())
							|| compareStrings(prevRadwinLastmile.getSiteName(),currentRadwinLastmile.getSiteName()) || compareStrings(prevRadwinLastmile.getSectorId(),currentRadwinLastmile.getSectorId())
							|| compareStrings(prevRadwinLastmile.getFrequency(),currentRadwinLastmile.getFrequency()) || compareStrings(prevRadwinLastmile.getSiteLat(),currentRadwinLastmile.getSiteLat())
							|| compareStrings(prevRadwinLastmile.getSiteLong(),currentRadwinLastmile.getSiteLong()) || compareStrings(prevRadwinLastmile.getSiteContact(),currentRadwinLastmile.getSiteContact())
							|| compareStrings(prevRadwinLastmile.getCustomerLocation(),currentRadwinLastmile.getCustomerLocation()) || compareStrings(currentRadwinLastmile.getRegion(),currentRadwinLastmile.getRegion())
							|| compareStrings(prevRadwinLastmile.getHsuIp(),currentRadwinLastmile.getHsuIp()) || compareStrings(prevRadwinLastmile.getHsuSubnet(),currentRadwinLastmile.getHsuSubnet())
							|| compareStrings(prevRadwinLastmile.getGatewayIp(),currentRadwinLastmile.getGatewayIp()) || compareStrings(prevRadwinLastmile.getHsuMacAddr(),currentRadwinLastmile.getHsuMacAddr())
							|| compareStrings(prevRadwinLastmile.getMgmtVlanid(),currentRadwinLastmile.getMgmtVlanid()) || compareStrings(prevRadwinLastmile.getNtpServerIp(),currentRadwinLastmile.getNtpServerIp())
							|| compareStrings(prevRadwinLastmile.getNtpOffset(),currentRadwinLastmile.getNtpOffset()) || compareStrings(prevRadwinLastmile.getProtocolSnmp(),currentRadwinLastmile.getProtocolSnmp())
							|| compareStrings(prevRadwinLastmile.getProtocolWebinterface(),currentRadwinLastmile.getProtocolWebinterface()) || compareStrings(prevRadwinLastmile.getProtocolTelnet(),currentRadwinLastmile.getProtocolTelnet())
							|| compareStrings(prevRadwinLastmile.getReqd_tx_power(),currentRadwinLastmile.getReqd_tx_power()) || compareStrings(prevRadwinLastmile.getEthernet_Port_Config(),currentRadwinLastmile.getEthernet_Port_Config())
							|| compareStrings(prevRadwinLastmile.getVlanMode(),currentRadwinLastmile.getVlanMode()) || compareStrings(prevRadwinLastmile.getDataVlanid(),currentRadwinLastmile.getDataVlanid())
							|| compareStrings(prevRadwinLastmile.getDataVlanPriority(),currentRadwinLastmile.getDataVlanPriority()) || compareStrings(prevRadwinLastmile.getAllowedVlanid(),currentRadwinLastmile.getAllowedVlanid())
							|| compareIntegers(prevRadwinLastmile.getSsvlan_tagging(),currentRadwinLastmile.getSsvlan_tagging()) || compareStrings(prevRadwinLastmile.getHsuEgressTraffic(),currentRadwinLastmile.getHsuEgressTraffic())
							|| compareStrings(prevRadwinLastmile.getHsuIngressTraffic(),currentRadwinLastmile.getHsuIngressTraffic()) || compareFloat(prevRadwinLastmile.getMirDl(),currentRadwinLastmile.getMirDl())
							|| compareFloat(prevRadwinLastmile.getMirUl(),currentRadwinLastmile.getMirUl()) || compareBytes(prevRadwinLastmile.getUntagVlanId(),currentRadwinLastmile.getUntagVlanId()))){
						performIPServiceActivation.getOrderDetails().setIsObjectInstanceModified(true);
						performIPServiceActivation.getOrderDetails().getService().getRadwin5KLastmile().setIsObjectInstanceModified(true);
				}
	}

	public String generateTxConfigXml(ServiceDetail serviceDetail, String actionMode, String requestId, String txType) {
		LOGGER.info("Inside generateTxConfigXml with input serviceId {}", serviceDetail.getId());
		String response = "";
		PerformTransmissionServiceActivation performTransmissionServiceActivation = new PerformTransmissionServiceActivation();
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		ACEHeader header = constructAceHeader(serviceDetail.getServiceId(), orderDetail, actionMode, requestId);
		performTransmissionServiceActivation.setHeader(header);
		OrderInfo2 orderInfo2 = createTxOrderDetails(serviceDetail, orderDetail, txType);

		performTransmissionServiceActivation.setOrderDetails(orderInfo2);
		response = mapXmlHeaders(jaxbObjectToXML(performTransmissionServiceActivation));

		return response;
	}

	/**
	 * 
	 * jaxbObjectToXML - This method is used to marshell the object to XML
	 * 
	 * @param performIPServiceActivation
	 * @return
	 */
	private String jaxbObjectToXML(PerformIPServiceActivation performIPServiceActivation) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(PerformIPServiceActivation.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
			StringWriter sw = new StringWriter();
			m.marshal(performIPServiceActivation, sw);
			xmlString = sw.toString();
			LOGGER.trace("Unmarshelled XML {}", xmlString);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb marshalling ", e);
		}
		return xmlString;
	}

	private String jaxbObjectToXML(PerformTransmissionServiceActivation performTransmissionServiceActivation) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(PerformTransmissionServiceActivation.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
			StringWriter sw = new StringWriter();
			m.marshal(performTransmissionServiceActivation, sw);
			xmlString = sw.toString();
			LOGGER.trace("Unmarshelled XML {}", xmlString);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb marshalling ", e);
		}
		return xmlString;
	}

	private GetCLRInfoResponse jaxbXmlToGetCLRInfoResponse(String inputXml) {
		try {
			JAXBContext context = JAXBContext.newInstance(GetCLRInfoResponse.class);
			Unmarshaller um = context.createUnmarshaller();
			// um.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format
			// XML
			StringReader reader = new StringReader(inputXml);
			return (GetCLRInfoResponse) um.unmarshal(reader);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb unmarshalling ", e);
		}
		return null;
	}

	private ACEHeader constructAceHeader(String serviceUuid, OrderDetail orderDetail, String actionMode,
			String requestId) {
		ACEHeader aceHeader = new ACEHeader();
		try {
			aceHeader.setAuthUser("tw_admin");
			if (StringUtils.isNotBlank(orderDetail.getOriginatorName()))
				aceHeader.setOriginatingSystem(orderDetail.getOriginatorName());
			else
				aceHeader.setOriginatingSystem("OPTIMUS");
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			aceHeader.setOriginationTime(xgcal);
			if (requestId == null) {
				requestId = generateRequestId(serviceUuid);
			}
			aceHeader.setRequestID(requestId);
			aceHeader.getActionRequired()
					.add(actionMode != null ? ActionRequired.fromValue(actionMode) : ActionRequired.CONFIG);
			LOGGER.info("Request Id Generated is {}", aceHeader.getRequestID());
		} catch (Exception e) {
			LOGGER.error("Error in constructing Header ", e);
		}
		return aceHeader;
	}

	protected OrderInfo2 createTxOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail, String txType) {
		String orderType=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);

		OrderInfo2 orderInfo2 = new OrderInfo2();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			orderInfo2.setServiceId(serviceDetail.getServiceId());
			if("Partial".equalsIgnoreCase(serviceDetail.getResourcePath())){
				if(Objects.nonNull(serviceDetail.getOrderSubCategory()) && !serviceDetail.getOrderSubCategory().isEmpty()
						&& (serviceDetail.getOrderSubCategory().equalsIgnoreCase("LM Shifting")
								|| serviceDetail.getOrderSubCategory().equalsIgnoreCase("LM Shifting-BSO change"))){
					LOGGER.info("AbstractGVPNMACD::Partial with LM Shifting");
					orderInfo2.setOrderType(OrderType3.fromValue("HOT_UPGRADE"));
				}else{
					LOGGER.info("AbstractGVPNMACD::Partial other than LM Shifting");
					orderInfo2.setOrderType(OrderType3.fromValue(orderType));
				}
			}else if("Parallel".equalsIgnoreCase(serviceDetail.getResourcePath())){
				LOGGER.info("AbstractGVPNMACD::Parallel");
				orderInfo2.setOrderType(OrderType3.fromValue("NEW"));
			} else {
				LOGGER.info("AbstractGVPNMACD::Other Than Partial and Parallel");
					orderInfo2.setOrderType(OrderType3.fromValue(orderType));
			}
			TxConfiguration txConfig = txConfigurationRepository
					.findFirstByServiceIdAndStatusOrderByIdDesc(serviceDetail.getServiceId(), "ISSUED");
			
			
			createTxServiceDetails(serviceDetail, txConfig,orderInfo2);
		
			if (StringUtils.isNotBlank(orderDetail.getCopfId()))
				orderInfo2.setCopfId(orderDetail.getCopfId());
			if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
				orderInfo2.setOldServiceId(serviceDetail.getOldServiceId());
			}
			//if (StringUtils.isNotBlank(orderDetail.getOrderCategory())){
			orderInfo2.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
			//}
			orderInfo2.setPortBandwidth("NA");
			if (serviceDetail.getServiceBandwidth() != null)
				orderInfo2.setServiceBandwidth(ActivationUtils.toString(serviceDetail.getServiceBandwidth()));
			//orderInfo2.setCustomerDetails(createTxCustomerDetails(orderDetail));

			OrderInfo2 mplsOrderInfo = new OrderInfo2();
			BeanUtils.copyProperties(orderInfo2, mplsOrderInfo);

			if (txType.toLowerCase().contains("tx") || txType.toLowerCase().contains("sdh")
					|| txType.toLowerCase().contains("access")) {
				orderInfo2.setIsSDHConfigurable(true);
			} else if (txType.toLowerCase().contains("mpls")) {
				orderInfo2.setIsMPLSTPConfigurable(true);
			}
		}
		return orderInfo2;

	}

	/**
	 * 
	 * createInitiateOrder
	 * 
	 * @return
	 */
	protected OrderInfo3 createOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		String orderType=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);

		OrderInfo3 orderInfo3 = new OrderInfo3();
		if (serviceDetail.getServiceId() != null)
			orderInfo3.setServiceId(serviceDetail.getServiceId());
		if (orderType != null) {
			if("Cos_Change".equalsIgnoreCase(serviceDetail.getOrderSubCategory()) || "Cos Change".equalsIgnoreCase(serviceDetail.getOrderSubCategory())){
				orderInfo3.setOrderType(OrderType2.fromValue("HOT_UPGRADE"));
			}else {
				orderInfo3.setOrderType(OrderType2.fromValue(orderType));
			}
		}
		if (serviceDetail != null)
			orderInfo3.setService(createServiceDetails(serviceDetail, orderDetail));
		if (orderDetail.getCopfId() != null)
			orderInfo3.setCopfId(orderDetail.getCopfId());
		if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
			orderInfo3.setOldServiceId(serviceDetail.getOldServiceId());
		}
		if(Objects.nonNull(serviceDetail.getMulticastings()) && !serviceDetail.getMulticastings().isEmpty()){
			LOGGER.info("Current MultiCast exists");
			Optional<Multicasting> multiCastOptional=serviceDetail.getMulticastings().stream().findFirst();
			if(multiCastOptional.isPresent()){
				Multicasting multiCast=multiCastOptional.get();
				if(Objects.isNull(multiCast.getEndDate())){
					LOGGER.info("Not end dated");
					AddonFeatures addOnFeature = new AddonFeatures();
					com.tcl.dias.serviceactivation.activation.netp.beans.Multicasting netpMulticasting= new com.tcl.dias.serviceactivation.activation.netp.beans.Multicasting();
					netpMulticasting.setWanPIMMode(multiCast.getWanPimMode());
					netpMulticasting.setType(multiCast.getType());
					netpMulticasting.setRPLocation(multiCast.getRpLocation());
					netpMulticasting.setAutoDiscoveryOption(multiCast.getAutoDiscoveryOption());	
					//DataMDT
					DataMDT dataMDT = new DataMDT();
					IPV4Address ipv4Address = new IPV4Address();
					ipv4Address.setAddress(multiCast.getDataMdt());
					dataMDT.setIPAddress(ipv4Address);
					dataMDT.setThreshold(1);
					netpMulticasting.setDataMDT(dataMDT);
					//DefaultMDT
					IPV4Address ipv4DefaultAddress = new IPV4Address();
					ipv4DefaultAddress.setAddress(multiCast.getDefaultMdt());
					netpMulticasting.setDefaultMDT(ipv4DefaultAddress);
					//RpAddress
					List<RPAddress> rpAddressList = new ArrayList<>();
					RPAddress rpAddress = new RPAddress();
					IPV4Address ipAddress = new IPV4Address();
					ipAddress.setAddress(multiCast.getRpAddress());
					rpAddress.setIPAddress(ipAddress);
					rpAddressList.add(rpAddress);
					netpMulticasting.setRpAddress(rpAddressList);
					addOnFeature.setMulticasting(netpMulticasting);
					LOGGER.info("Set AddOnFeature");
					orderInfo3.setAddOns(addOnFeature);
				}
			}
		}
		//if (StringUtils.isNotBlank(orderDetail.getOrderCategory()))
		orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
		if (orderDetail.getAsdOpptyFlag() != null)
			orderInfo3.setOptyBidCategory(orderDetail.getOptyBidCategory());
		orderInfo3.setIsDowntimeRequired(false);
		orderInfo3.setCustomerDetails(createCustomerDetails(orderDetail, serviceDetail,false));
		return orderInfo3;

	}
	
	protected OrderInfo3 createDummyOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail, String actionMode) {
		OrderInfo3 orderInfo3 = new OrderInfo3();
		if (serviceDetail.getServiceId() != null)
			orderInfo3.setServiceId(serviceDetail.getServiceId());
		if (orderDetail.getOrderType() != null)
			orderInfo3.setOrderType(OrderType2.fromValue("CHANGE_ORDER"));
		if (serviceDetail != null)
			orderInfo3.setService(createServiceDummyDetails(serviceDetail, orderDetail));
		if (orderDetail.getCopfId() != null)
			orderInfo3.setCopfId(orderDetail.getCopfId());
		if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
			orderInfo3.setOldServiceId(serviceDetail.getOldServiceId());
			//if (StringUtils.isNotBlank(orderDetail.getOrderCategory()))
			orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
			orderInfo3.setIsDowntimeRequired(true);
			orderInfo3.setCustomerDetails(createCustomerDetails(orderDetail, serviceDetail,true));
		}
		orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
		if (orderDetail.getAsdOpptyFlag() != null)
			orderInfo3.setOptyBidCategory(orderDetail.getOptyBidCategory());
		orderInfo3.setIsDowntimeRequired(true);
		orderInfo3.setCustomerDetails(createDummyCustomerDetails(orderDetail, serviceDetail,false));
		return orderInfo3;

	}
	
	protected Customer createDummyCustomerDetails(OrderDetail orderDetail, ServiceDetail serviceDetail,boolean rf) {
		Customer customerDetail = new Customer();
		if (Objects.nonNull(orderDetail.getCustCuId()))
			customerDetail.setId(orderDetail.getCustCuId().toString());
		if (StringUtils.isNotBlank(orderDetail.getCustomerName()))
			customerDetail.setName(orderDetail.getCustomerName());
		if (StringUtils.isNotBlank(orderDetail.getCustomerType()))
			customerDetail.setType(orderDetail.getCustomerType());
		if (StringUtils.isNotBlank(orderDetail.getCustomerCategory()))
			customerDetail.setCategory(orderDetail.getCustomerCategory());
		if (StringUtils.isNotBlank(orderDetail.getCustomerEmail()))
			customerDetail.setEmailID(orderDetail.getCustomerEmail());
		Address address = new Address();
		if (StringUtils.isNotBlank(serviceDetail.getAddressLine1()))
			address.setAddressLine1(serviceDetail.getAddressLine1());
		if (StringUtils.isNotBlank(serviceDetail.getAddressLine2()))
			address.setAddressLine2(serviceDetail.getAddressLine2());
		if (StringUtils.isNotBlank(serviceDetail.getCity()))
			address.setCity(serviceDetail.getCity());
		if (StringUtils.isNotBlank(serviceDetail.getState()))
			address.setState(serviceDetail.getState());
		if (StringUtils.isNotBlank(serviceDetail.getPincode()))
			address.setPincode(serviceDetail.getPincode());
		if (StringUtils.isNotBlank(serviceDetail.getCountry()))
			address.setCountry(serviceDetail.getCountry());
		if (StringUtils.isNotBlank(serviceDetail.getCity()))
			address.setLocation(serviceDetail.getCity());
		customerDetail.setAddress(address);
		if(!rf) {
			customerDetail.setALUCustomerID(String.valueOf(599995));
		}
		return customerDetail;
	}
	
	protected OrderInfo3.Service createServiceDummyDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		IpDummyDetail ipDummyDetail=ipDummyDetailRepository.findFirstByServiceDetail_IdOrderByIdDesc(serviceDetail.getId());
		GVPNService gvpnService = new GVPNService();
		gvpnService.setCSSSAMID(String.valueOf(12178));
		gvpnService.setServiceType(serviceDetail.getServiceType());
		gvpnService.setServiceSubType(serviceDetail.getServiceSubtype());
		gvpnService.setSAMCustomerDescription("ACE_DUMMY_SERVICE_CUSTOMER");
		gvpnService.setALUServiceName("DUMMY_SERVICE_FOR_PORT_CHANGE_CASES_ACE");
		gvpnService.setALUServiceID(String.valueOf(599995));
		if (StringUtils.isNotBlank(serviceDetail.getUsageModel()))
			gvpnService.setUsageModel(serviceDetail.getUsageModel());
		if (StringUtils.isNotBlank(serviceDetail.getDataTransferCommit()))
			gvpnService.setDataTransferCommit(serviceDetail.getDataTransferCommit());
		
		if (StringUtils.isNotBlank(serviceDetail.getDataTransferCommitUnit()))
			gvpnService.setDataTransferCommitUnit(serviceDetail.getDataTransferCommitUnit());
		if (StringUtils.isNotBlank(serviceDetail.getDescription()))
			gvpnService.setDescriptionFreeText(serviceDetail.getDescription());
		if(Objects.nonNull(serviceDetail.getNetpRefid())){
			gvpnService.setInstanceID(addDummyValue(serviceDetail.getNetpRefid(),"DUM"));
		}else{
			gvpnService.setInstanceID(serviceDetail.getNetpRefid());
		}
		List<VpnSolution> vpnSolutions = vpnSolutionRepository
				.findByServiceDetail_IdAndEndDateIsNull(serviceDetail.getId());
		String currentLegId = null;
		VPNSolutionTable solutionTable = new VPNSolutionTable();
		for (VpnSolution vpnSolution : vpnSolutions) {
			VPN vpn = new VPN();
			Leg leg = new Leg();
			if("CUSTOMER".equalsIgnoreCase(vpnSolution.getVpnType())){
				solutionTable.setSolutionID("DummyService");
				vpn.setVpnId("DummyService_VPN_M");
				vpn.setVpnType("CUSTOMER");
				vpn.setTopology("MESH");
				leg.setRole("FULL-MESH");
				leg.setLegServiceID(Objects.nonNull(vpnSolution.getVpnLegId())?"DUM"+vpnSolution.getVpnLegId():vpnSolution.getVpnLegId());
				if (vpnSolution.getInstanceId() != null){
					leg.setInstanceID(addDummyValue(vpnSolution.getInstanceId(),"DUM"));
				}
				currentLegId = Objects.nonNull(vpnSolution.getVpnLegId())?"DUM"+vpnSolution.getVpnLegId():vpnSolution.getVpnLegId();
			}else{
				solutionTable.setSolutionID(serviceDetail.getSolutionId());
				if(vpnSolution.getVpnName()!=null &&vpnSolution.getVpnName().equalsIgnoreCase("INTERNET_VPN")) {
					vpn.setVpnId("Internet_VPN");
				}else {
				vpn.setVpnId(vpnSolution.getVpnName());
				}
				vpn.setVpnType(vpnSolution.getVpnType());
				vpn.setTopology(vpnSolution.getVpnTopology());
				leg.setRole(vpnSolution.getLegRole());
				leg.setLegServiceID(vpnSolution.getVpnLegId());
				if (vpnSolution.getInstanceId() != null){
					leg.setInstanceID(vpnSolution.getInstanceId());
				}
				currentLegId = vpnSolution.getVpnLegId();
			}
			
			if (vpnSolution.getInterfaceName() != null)
				leg.setInterfacename(vpnSolution.getInterfaceName());
			leg.setSiteID(vpnSolution.getSiteId());
			vpn.getLeg().add(leg);
			solutionTable.getVPN().add(vpn);
			gvpnService.setSolutionTable(solutionTable);
		}
		gvpnService.setVrf(createVrfDetails(serviceDetail, gvpnService, currentLegId));
		if (serviceDetail.getScopeOfMgmt() != null
				&& serviceDetail.getScopeOfMgmt().toLowerCase().contains("proactive"))
			gvpnService.setIsProactiveMonitoringEnabled("True");
		else {
			gvpnService.setIsProactiveMonitoringEnabled("False");
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
				gvpnService.getRouter().add(createDummyPeRouterDetails(interfaceProtocolMapping, serviceDetail,ipDummyDetail));
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
		/*if (StringUtils.isNotBlank(ipAddressDetail.getPingAddress1())) {
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
		*/
		gvpnService.setExtendedLAN(createExtendedLanConfig(ipAddressDetail));//
		Set<IpaddrWanv4Address> wanV4Addresses = ipAddressDetail.getIpaddrWanv4Addresses();
		for (IpaddrWanv4Address ipaddrWanv4Address : wanV4Addresses) {
			IPV4Address wanV4Address = new IPV4Address();
			if(Objects.nonNull(ipDummyDetail) && Objects.nonNull(ipDummyDetail.getDummyWANIpAddressSubnet())){
				wanV4Address.setAddress(ipDummyDetail.getDummyWANIpAddressSubnet());
			}else{
				wanV4Address.setAddress(ipaddrWanv4Address.getWanv4Address());
			}
			gvpnService.getWanV4Addresses().add(wanV4Address);
		}

		/*Set<IpaddrWanv6Address> wanV6Addresses = ipAddressDetail.getIpaddrWanv6Addresses();
		for (IpaddrWanv6Address ipaddrWanv6Address : wanV6Addresses) {
			IPV6Address wanIpv6Address = new IPV6Address();
			wanIpv6Address.setAddress(ipaddrWanv6Address.getWanv6Address());
			gvpnService.getWanV6Addresses().add(wanIpv6Address);
		}*/

		OrderInfo3.Service service = new OrderInfo3.Service();
		/*List<InterfaceProtocolMapping> bgInterfaceProtocolEntity = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndEndDateIsNull(serviceDetail.getId());
																				
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
		}*/
		if(serviceDetail.getRouterMake().toLowerCase().contains("cisco")){
			gvpnService.setQos(createDummyServiceQos(serviceDetail));
		}
		List<InterfaceProtocolMapping> cpeInterfaceProtocolEntity = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndCpeNotNullAndEndDateIsNull(serviceDetail.getId());
		for (InterfaceProtocolMapping interfaceProtocolMapping : cpeInterfaceProtocolEntity) {
			CustomerPremiseEquipment cpe = createDummyCpeDetails(interfaceProtocolMapping,ipDummyDetail);
			if (cpe != null)
				gvpnService.getCpe().add(cpe);
		}
		if (serviceDetail.getLastmileType() != null) {
			LOGGER.info("LastMileType exists");
			LastMile lastMile = new LastMile();
			lastMile.setType(serviceDetail.getLastmileType());
			if(Objects.nonNull(serviceDetail.getLastmileType()) && !serviceDetail.getLastmileType().isEmpty() && serviceDetail.getLastmileType().equalsIgnoreCase("OnnetRF")){
				LOGGER.info("OnnetRF as LastMileType");
				/*List<LmComponent> lmComponents=lmComponentRepository.findByServiceDetail_id(serviceDetail.getId());
				if(Objects.nonNull(lmComponents) && !lmComponents.isEmpty()){
					lmComponents.stream().filter(lmComp -> Objects.nonNull(lmComp.getLmOnwlProvider()) 
							&& !lmComp.getLmOnwlProvider().isEmpty() && "Radwin".equalsIgnoreCase(lmComp.getLmOnwlProvider())).forEach(lmComp ->{
						LOGGER.info("OnnetRF as Radwin");
						lastMile.setType(lmComp.getLmOnwlProvider());
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
	
	protected CustomerPremiseEquipment createDummyCpeDetails(InterfaceProtocolMapping interfaceProtocolMapping, IpDummyDetail ipDummyDetail) {
		Cpe cpe = interfaceProtocolMapping.getCpe();
		CustomerPremiseEquipment customerPremiseEquipment = new CustomerPremiseEquipment();
		customerPremiseEquipment.setHostName(cpe.getHostName());

		if (isLoopBackTag(cpe)) {
			Interface loopbackInterface = new Interface();
			if (cpe.getLoopbackInterfaceName() != null) {
				loopbackInterface.setName(cpe.getLoopbackInterfaceName());
			}
			if (StringUtils.isNotBlank(cpe.getMgmtLoopbackV4address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(cpe.getMgmtLoopbackV4address());
				loopbackInterface.setV4IpAddress(ipv4Address);
			}
			/*if (StringUtils.isNotBlank(cpe.getMgmtLoopbackV6address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(cpe.getMgmtLoopbackV6address());
				loopbackInterface.setV6IpAddress(ipv6Address);
			}*/
			customerPremiseEquipment.setLoopbackInterface(loopbackInterface);
		}

		customerPremiseEquipment.setIsCEACEConfigurable(ActivationUtils.getBooleanValue(cpe.getIsaceconfigurable()));
		customerPremiseEquipment.setSnmpServerCommunity(cpe.getSnmpServerCommunity());
		if (interfaceProtocolMapping.getIscpeWanInterface() == 1)
			customerPremiseEquipment.setWanInterface(createDummyCpeWanInterface(cpe, interfaceProtocolMapping,ipDummyDetail));
		else
			return null;
		return customerPremiseEquipment;
	}

	private Router createDummyPeRouterDetails(InterfaceProtocolMapping interfaceProtocolMapping,
			ServiceDetail serviceDetail, IpDummyDetail ipDummyDetail) {
		RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
		Router router = new Router();
		if (StringUtils.isNotBlank(routerDetail.getRouterHostname()))
			router.setHostName(routerDetail.getRouterHostname());
		if (StringUtils.isNotBlank(routerDetail.getRouterRole()))
			router.setRole(routerDetail.getRouterRole());
		if (StringUtils.isNotBlank(routerDetail.getIpv4MgmtAddress())) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(routerDetail.getIpv4MgmtAddress());
			router.setV4ManagementIPAddress(ipv4Address);
		}
		if (StringUtils.isNotBlank(routerDetail.getIpv6MgmtAddress())) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(routerDetail.getIpv6MgmtAddress());
			router.setV6ManagementIPAddress(ipv6Address);
		}
		if (StringUtils.isNotBlank(routerDetail.getRouterType()))
			router.setType(routerDetail.getRouterType());
		router.setWanInterface(createDummyWanInterfaceEthernet(interfaceProtocolMapping, router,ipDummyDetail));
		if (serviceDetail != null) {
			Set<Topology> topologies = serviceDetail.getTopologies();
			for (Topology topology : topologies) {
				TopologyInfo topologyInfo = createTopologyInformation(topology);
				if (topologyInfo != null) {
					router.setTopologyInfo(topologyInfo);
					Set<RouterUplinkport> routerUplinkPorts = topology.getRouterUplinkports();
					for (RouterUplinkport routerUplinkPort : routerUplinkPorts) {
						router.setRouterTopologyInterface1(createRouterUplinkPorts1(routerUplinkPort));
						router.setRouterTopologyInterface2(createRouterUplinkPorts2(routerUplinkPort));
					}
				}
			}
		}
		return router;
	}

	private WANInterface createDummyWanInterfaceEthernet(InterfaceProtocolMapping interfaceProtocolMapping,
			Router router, IpDummyDetail ipDummyDetail) {
		WANInterface wanInterface = new WANInterface();
		WANInterface.Interface interfce = new WANInterface.Interface();
		if (interfaceProtocolMapping.getEthernetInterface() != null)
			interfce.setEthernetInterface(
					createDummyEthernetInterface(interfaceProtocolMapping.getEthernetInterface(), router, wanInterface,ipDummyDetail));
		if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null)
			interfce.setSerialInterface(
					createDummyWanInterfaceCE1Serial(interfaceProtocolMapping.getChannelizedE1serialInterface(), router,ipDummyDetail));
		if (interfaceProtocolMapping.getChannelizedSdhInterface() != null)
			interfce.setChannelizedSDHInterface(
					createDummyWanInterfaceCSdh(interfaceProtocolMapping.getChannelizedSdhInterface(), router,ipDummyDetail));
		wanInterface.setInterface(interfce);
		// wanInterface.setStaticRoutes(createPeWanStaticRoutes(interfaceProtocolMapping.getStaticProtocol()));
		return wanInterface;
	}
	
	
	protected ChannelizedSDHInterface createDummyWanInterfaceCSdh(ChannelizedSdhInterface channelizedSdhInterface,
			Router router, IpDummyDetail ipDummyDetail) {
		ChannelizedSDHInterface channelizedSDHInterface = null;
		if (channelizedSdhInterface != null) {
			channelizedSDHInterface = new ChannelizedSDHInterface();
			if (channelizedSdhInterface.getInterfaceName() != null)
				channelizedSDHInterface.setName(channelizedSdhInterface.getInterfaceName());
			if (channelizedSdhInterface.getPhysicalPort() != null)
				channelizedSDHInterface.setPhysicalPortName(channelizedSdhInterface.getPhysicalPort());
			if (channelizedSdhInterface.getModifiedIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				String interfaceIp=getInterfaceDummyIp(ipDummyDetail);
				ipv4Address.setAddress(Objects.nonNull(interfaceIp)?interfaceIp:channelizedSdhInterface.getModifiedIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			/*if (channelizedSdhInterface.getModifiedIipv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInterface.getModifiedIipv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}*/
			if (channelizedSdhInterface.getModifiedSecondaryIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				String interfaceIp=getInterfaceDummyIp(ipDummyDetail);
				ipv4Address.setAddress(Objects.nonNull(interfaceIp)?interfaceIp:channelizedSdhInterface.getModifiedSecondaryIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			/*if (channelizedSdhInterface.getModifiedSecondaryIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInterface.getModifiedSecondaryIpv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}*/
			if (StringUtils.isNotBlank(channelizedSdhInterface.getChannelGroupNumber()))
				channelizedSDHInterface.setChannelGroupNumber(channelizedSdhInterface.getChannelGroupNumber());
			if (StringUtils.isNotBlank(channelizedSdhInterface.getDlciValue()))
				channelizedSDHInterface.setDlciValue(ActivationUtils.toInteger(channelizedSdhInterface.getDlciValue()));

			for (int i = Integer.valueOf(channelizedSdhInterface.get_4Kfirsttime_slot()); i <= Integer
					.valueOf(channelizedSdhInterface.get_4klasttimeSlot()); i++) {
				channelizedSDHInterface.getSixtyFourKTimeslot().add(String.valueOf(i));
			}
			if (StringUtils.isNotBlank(channelizedSdhInterface.getEncapsulation())) {
				channelizedSDHInterface.setEncapsulation(channelizedSdhInterface.getEncapsulation());
			}

			if (StringUtils.isNotBlank(channelizedSdhInterface.getMode())) {
				channelizedSDHInterface.setMode(channelizedSdhInterface.getMode().toUpperCase());
			}
			if (channelizedSdhInterface.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(channelizedSdhInterface.getIsbfdEnabled()));
			if (channelizedSdhInterface.getIsframed() != null)
				channelizedSDHInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedSdhInterface.getIsframed()));
			if (channelizedSdhInterface.getFraming() != null) {
				channelizedSDHInterface.setFraming(channelizedSdhInterface.getFraming());
			}
			if (channelizedSdhInterface.getPosframing() != null) {
				channelizedSDHInterface.setPosFraming(channelizedSdhInterface.getPosframing());
			}
			if (channelizedSdhInterface.getJ() != null)
				channelizedSDHInterface.setJ(ActivationUtils.toString(channelizedSdhInterface.getJ()));
			if (channelizedSdhInterface.getKlm() != null)
				channelizedSDHInterface.setKLM(channelizedSdhInterface.getKlm());
			if (channelizedSdhInterface.getPortType() != null)
				channelizedSDHInterface.setPortType(channelizedSdhInterface.getPortType().toUpperCase());
			if (channelizedSdhInterface.getIsbfdEnabled() != null)
				channelizedSDHInterface.setBfdConfig(createCsdhHdlcConfig(channelizedSdhInterface));
		}
		return channelizedSDHInterface;
	}
	
	protected SerialInterface createDummyWanInterfaceCE1Serial(ChannelizedE1serialInterface channelizedE1serialInterface,
			Router router, IpDummyDetail ipDummyDetail) {
		SerialInterface serialInterface = null;
		if (channelizedE1serialInterface != null) {
			serialInterface = new SerialInterface();
			if (channelizedE1serialInterface.getInterfaceName() != null)
				serialInterface.setName(channelizedE1serialInterface.getInterfaceName());
			if (channelizedE1serialInterface.getPhysicalPort() != null)
				serialInterface.setPhysicalPortName(channelizedE1serialInterface.getPhysicalPort());
			if (channelizedE1serialInterface.getModifiedIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				String interfaceIp=getInterfaceDummyIp(ipDummyDetail);
				ipv4Address.setAddress(Objects.nonNull(interfaceIp)?interfaceIp:channelizedE1serialInterface.getModifiedIpv4Address());
				serialInterface.setV4IpAddress(ipv4Address);
			}
			/*if (channelizedE1serialInterface.getModifiedIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1serialInterface.getModifiedIpv6Address());
				serialInterface.setV6IpAddress(ipv6Address);
			}*/
			if (channelizedE1serialInterface.getModifiedSecondaryIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				String interfaceIp=getInterfaceDummyIp(ipDummyDetail);
				ipv4Address.setAddress(Objects.nonNull(interfaceIp)?interfaceIp:channelizedE1serialInterface.getModifiedSecondaryIpv4Address());
				serialInterface.setV4IpAddress(ipv4Address);
			}
			/*if (channelizedE1serialInterface.getModifiedSecondaryIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1serialInterface.getModifiedSecondaryIpv6Address());
				serialInterface.setV6IpAddress(ipv6Address);
			}*/
			if (channelizedE1serialInterface.getChannelGroupNumber() != null)
				serialInterface.setChannelGroupNumber(channelizedE1serialInterface.getChannelGroupNumber());
			if (channelizedE1serialInterface.getDlciValue() != null)
				serialInterface.setDlciValue(ActivationUtils.toInteger(channelizedE1serialInterface.getDlciValue()));
			for (int i = Integer.valueOf(channelizedE1serialInterface.getFirsttimeSlot()); i <= Integer
					.valueOf(channelizedE1serialInterface.getLasttimeSlot()); i++) {
				serialInterface.getTimeslot().add(String.valueOf(i));
			}
			if (channelizedE1serialInterface.getIsframed() != null)
				serialInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsframed()));

			if (channelizedE1serialInterface.getEncapsulation() != null) {
				serialInterface.setEncapsulation(channelizedE1serialInterface.getEncapsulation());
			}

			if (channelizedE1serialInterface.getMode() != null) {
				serialInterface.setMode(channelizedE1serialInterface.getMode().toUpperCase());
			}
			if (channelizedE1serialInterface.getIsframed() != null)
				serialInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsframed()));
			if (channelizedE1serialInterface.getFraming() != null) {
				serialInterface.setFraming(channelizedE1serialInterface.getFraming());
			}
			if (channelizedE1serialInterface.getIscrcforenabled() != null) {
				serialInterface.setCrcSize(channelizedE1serialInterface.getCrcsize());
			}
			if (channelizedE1serialInterface.getPortType() != null)
				serialInterface.setPortType(channelizedE1serialInterface.getPortType().toUpperCase());
			if (channelizedE1serialInterface.getIsbfdEnabled() != null)
				serialInterface.setBfdConfig(createCE1SerialHdlcConfig(channelizedE1serialInterface));
			if (channelizedE1serialInterface.getMtu() != null)
				serialInterface.setMtu(ActivationUtils.toInteger(channelizedE1serialInterface.getMtu()));

			if (channelizedE1serialInterface.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsbfdEnabled()));
			if (channelizedE1serialInterface.getEncapsulation() != null)
				serialInterface.setEncapsulation(channelizedE1serialInterface.getEncapsulation());

		}
		return serialInterface;
	}
	
	protected EthernetInterface createDummyEthernetInterface(
			com.tcl.dias.serviceactivation.entity.entities.EthernetInterface ethernetInterfaceEntity, Router router,
			WANInterface wanInterface, IpDummyDetail ipDummyDetail) {
		EthernetInterface ethernetInterface = null;
		if (ethernetInterfaceEntity != null) {
			ethernetInterface = new EthernetInterface();
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getInterfaceName()))
				ethernetInterface.setName(ethernetInterfaceEntity.getInterfaceName());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getPhysicalPort()))
				ethernetInterface.setPhysicalPortName(ethernetInterfaceEntity.getPhysicalPort());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				String interfaceIp=getInterfaceDummyIp(ipDummyDetail);
				ipv4Address.setAddress(Objects.nonNull(interfaceIp)?interfaceIp:ethernetInterfaceEntity.getModifiedIpv4Address());
				ethernetInterface.setV4IpAddress(ipv4Address);
			}
			/*if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(ethernetInterfaceEntity.getModifiedIpv6Address());
				ethernetInterface.setV6IpAddress(ipv6Address);
			}*/
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedSecondaryIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ethernetInterfaceEntity.getModifiedSecondaryIpv4Address());
				wanInterface.setSecondaryV4WANIPAddress(ipv4Address);
				//ethernetInterface.setV4IpAddress(ipv4Address);
			}
			/*if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedSecondaryIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(ethernetInterfaceEntity.getModifiedSecondaryIpv6Address());
				ethernetInterface.setV6IpAddress(ipv6Address);
			}*/
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMediaType()))
				ethernetInterface.setMediaType(ethernetInterfaceEntity.getMediaType());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getPortType()) && !ethernetInterfaceEntity.getPortType().toUpperCase().contains("LAG"))
				ethernetInterface.setPortType(ethernetInterfaceEntity.getPortType().toUpperCase());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getEncapsulation()))
				ethernetInterface.setEncapsulation(ethernetInterfaceEntity.getEncapsulation());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMode()))
				ethernetInterface.setMode(ethernetInterfaceEntity.getMode().toUpperCase());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getQosLoopinPassthrough()))
				ethernetInterface.setQosLoopinPassthroughInterface(ethernetInterfaceEntity.getQosLoopinPassthrough());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getFraming()))
				ethernetInterface.setFraming(ethernetInterfaceEntity.getFraming());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getInnerVlan()))
				ethernetInterface.setVlan(ActivationUtils.toInteger(ethernetInterfaceEntity.getInnerVlan()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getSpeed()))
				ethernetInterface.setSpeed(ethernetInterfaceEntity.getSpeed());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getDuplex()))
				ethernetInterface.setDuplex(ActivationUtils.getDuplex(ethernetInterfaceEntity.getDuplex()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getOuterVlan()))
				ethernetInterface.setSvlan(ActivationUtils.toInteger(ethernetInterfaceEntity.getOuterVlan()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getAutonegotiationEnabled()))
				ethernetInterface.setIsAutoNegotiation(ethernetInterfaceEntity.getAutonegotiationEnabled());
			if (ethernetInterfaceEntity.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIsbfdEnabled()));
			if (ethernetInterfaceEntity.getIsbfdEnabled() != null
					&& ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIsbfdEnabled()))
				ethernetInterface.setBfdConfig(createEthernetInterfaceHdlcConfig(ethernetInterfaceEntity));
			if (ethernetInterfaceEntity.getIshsrpEnabled() != null
					&& ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIshsrpEnabled())) {
				wanInterface.setHSRP(createCpeHrspProtocol(ethernetInterfaceEntity));
			}
		}
		return ethernetInterface;

	}

	protected OrderInfo3 createRfOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		 LOGGER.info("AsbtractGVPNMACDConfig.createRfOrderDetails");
			String orderType=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);

		OrderInfo3 orderInfo3 = new OrderInfo3();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			orderInfo3.setServiceId(serviceDetail.getServiceId());
			String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetail, orderDetail);
			String orderSubCategory=serviceDetail.getOrderSubCategory();
			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			if(Objects.nonNull(orderSubCategory) && !orderSubCategory.isEmpty()
					&& orderSubCategory.toLowerCase().contains("bso")){
				 LOGGER.info("BSO");
				orderInfo3.setOrderType(OrderType2.fromValue("NEW"));
			}else if(StringUtils.isNotBlank(orderType)){
				 LOGGER.info("Other than BSO");
				orderInfo3.setOrderType(OrderType2.fromValue(orderType));
			}
			orderInfo3.setService(createRfServiceDetails(serviceDetail, orderDetail));
			if (StringUtils.isNotBlank(orderDetail.getCopfId())){
				orderInfo3.setCopfId(orderDetail.getCopfId());
			}
			if(Objects.nonNull(orderSubCategory) && !orderSubCategory.isEmpty()
					&& (orderSubCategory.toLowerCase().contains("bso")
							|| orderSubCategory.toLowerCase().contains("lm")
							|| orderSubCategory.equalsIgnoreCase("Shifting"))){
				 LOGGER.info("BSO/Shifting for OldServiceId");
				orderInfo3.setOldServiceId("DUM"+serviceDetail.getServiceId());
			}else if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
				LOGGER.info("OldServiceId");
				orderInfo3.setOldServiceId(serviceDetail.getOldServiceId());
			}
			orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
			orderInfo3.setIsDowntimeRequired(true);
			orderInfo3.setIsObjectInstanceModified(true);
			orderInfo3.setCustomerDetails(createCustomerDetails(orderDetail, serviceDetail,true));
		}
		return orderInfo3;

	}

	protected TransmissionService createTxServiceDetails(ServiceDetail serviceDetail, TxConfiguration txConfig) {
		TransmissionService txService = new TransmissionService();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			String clrResponse = txConfig.getClrResponse();
			GetCLRInfoResponse getClr = jaxbXmlToGetCLRInfoResponse(clrResponse);
			if (getClr != null) {
				for (com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Worker cWorker : getClr.getResponse()
						.getService().getWorker()) {
					com.tcl.dias.serviceactivation.activation.netp.beans.Worker worker = new com.tcl.dias.serviceactivation.activation.netp.beans.Worker();
					BeanUtils.copyProperties(cWorker, worker);
					txService.getWorker().add(worker);
				}
				for (Protection cProtection : getClr.getResponse().getService().getProtections()) {
					com.tcl.dias.serviceactivation.activation.netp.beans.Protection protection = new com.tcl.dias.serviceactivation.activation.netp.beans.Protection();
					BeanUtils.copyProperties(cProtection, protection);
					txService.getProtections().add(protection);
				}
				if(getClr.getResponse().getService().getL2Params() !=null) {
					for (L2Params l2Params : getClr.getResponse().getService().getL2Params()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.L2Params l2Param = new com.tcl.dias.serviceactivation.activation.netp.beans.L2Params();
						BeanUtils.copyProperties(l2Params, l2Param);
						txService.getL2Params().add(l2Param);
					}
				}
				if(getClr.getResponse().getService().getCienaParam() !=null) {
					for (CienaParam cienaParams : getClr.getResponse().getService().getCienaParam()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.CienaParam cienaParam = new com.tcl.dias.serviceactivation.activation.netp.beans.CienaParam();
						BeanUtils.copyProperties(cienaParams, cienaParam);
						txService.getCienaParam().add(cienaParam);
					}
				}
				if(getClr.getResponse().getService().getTopologies() !=null) {
					for (com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Topology topologies : getClr.getResponse().getService().getTopologies()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.Topology topology = new com.tcl.dias.serviceactivation.activation.netp.beans.Topology();
						BeanUtils.copyProperties(topologies, topology);
						txService.getTopologies().add(topology);
					}
				}
				if(getClr.getResponse().getService().getBroadcast() !=null) {
					for (Broadcast broadcasts : getClr.getResponse().getService().getBroadcast()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.Broadcast broadcast = new com.tcl.dias.serviceactivation.activation.netp.beans.Broadcast();
						BeanUtils.copyProperties(broadcasts, broadcast);
						txService.getBroadcast().add(broadcast);
					}
				}
			}
			txService.setIsACEActionRequired(true);
			txService.setIsNOCActionRequired(false);
			txService.setVerifyAndActivate(true);
		}
		return txService;
	}

	protected Customer createTxCustomerDetails(OrderDetail orderDetail) {
		Customer customerDetail = new Customer();
		if (Objects.nonNull(orderDetail.getCustCuId()))
			customerDetail.setId(orderDetail.getCustCuId().toString());
		if (StringUtils.isNotBlank(orderDetail.getCustomerName()))
			customerDetail.setName(orderDetail.getCustomerName());
		if (StringUtils.isNotBlank(orderDetail.getCustomerType()))
			customerDetail.setType(orderDetail.getCustomerType());
		return customerDetail;
	}

	protected Customer createCustomerDetails(OrderDetail orderDetail, ServiceDetail serviceDetail,boolean rf) {
		Customer customerDetail = new Customer();
		if (Objects.nonNull(orderDetail.getCustCuId()))
			customerDetail.setId(orderDetail.getCustCuId().toString());
		if (StringUtils.isNotBlank(orderDetail.getCustomerName()))
			customerDetail.setName(orderDetail.getCustomerName());
		if (StringUtils.isNotBlank(orderDetail.getCustomerType()))
			customerDetail.setType(orderDetail.getCustomerType());
		if (StringUtils.isNotBlank(orderDetail.getCustomerCategory()))
			customerDetail.setCategory(orderDetail.getCustomerCategory());
		if (StringUtils.isNotBlank(orderDetail.getCustomerEmail()))
			customerDetail.setEmailID(orderDetail.getCustomerEmail());
		Address address = new Address();
		if (StringUtils.isNotBlank(serviceDetail.getAddressLine1()))
			address.setAddressLine1(serviceDetail.getAddressLine1());
		if (StringUtils.isNotBlank(serviceDetail.getAddressLine2()))
			address.setAddressLine2(serviceDetail.getAddressLine2());
		if (StringUtils.isNotBlank(serviceDetail.getCity()))
			address.setCity(serviceDetail.getCity());
		if (StringUtils.isNotBlank(serviceDetail.getState()))
			address.setState(serviceDetail.getState());
		if (StringUtils.isNotBlank(serviceDetail.getPincode()))
			address.setPincode(serviceDetail.getPincode());
		if (StringUtils.isNotBlank(serviceDetail.getCountry()))
			address.setCountry(serviceDetail.getCountry());
		if (StringUtils.isNotBlank(serviceDetail.getCity()))
			address.setLocation(serviceDetail.getCity());
		customerDetail.setAddress(address);
		if(!rf) {
		if (StringUtils.isNotBlank(orderDetail.getAluCustomerId()))
			customerDetail.setALUCustomerID(orderDetail.getAluCustomerId());// ALU SPEC
		}
		return customerDetail;
	}

	protected OrderInfo3.Service createRfServiceDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		OrderInfo3.Service service = new OrderInfo3.Service();
		Set<LmComponent> lmComponents = serviceDetail.getLmComponents();
		for (LmComponent lmComponent : lmComponents) {
			createRfCambium(serviceDetail, service, lmComponent);
			createRfRadwin(serviceDetail, service, lmComponent);
			createRfWimax(serviceDetail, service, lmComponent);
		}
		return service;
	}

	/**
	 * createRfRadwin
	 * 
	 * @param service
	 * @param lmComponent
	 */
	protected void createRfRadwin(ServiceDetail serviceDetail, OrderInfo3.Service service, LmComponent lmComponent) {
		for (RadwinLastmile radwinLastmile : lmComponent.getRadwinLastmiles()) {
			Radwin5KLastmile radwin5kLastMile = new Radwin5KLastmile();
			if("ECO INTERNET".equalsIgnoreCase(serviceDetail.getServiceSubtype())) {
				radwin5kLastMile.setServiceSubType("STDILL");
			}else {
				radwin5kLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			}
			radwin5kLastMile.setServiceType(serviceDetail.getServiceType());

			if (serviceDetail.getBurstableBw() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
					}
				}
				radwin5kLastMile.setPortSpeed(portBandwidth);
			} else {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
					}
				}
				radwin5kLastMile.setPortSpeed(portBandwidth);
			}
			MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstRadwinDetails) && Objects.nonNull(mstRadwinDetails.getNetpRefId()) && !mstRadwinDetails.getNetpRefId().isEmpty()){
				radwin5kLastMile.setInstanceID(mstRadwinDetails.getNetpRefId());
			}else if(Objects.nonNull(serviceDetail.getNetpRefid())){
				radwin5kLastMile.setInstanceID(serviceDetail.getNetpRefid());
			}
			if (StringUtils.isNotBlank(radwinLastmile.getBtsIp()))
				radwin5kLastMile.setBTSIP(radwinLastmile.getBtsIp());
			if (StringUtils.isNotBlank(radwinLastmile.getBtsName()))
				radwin5kLastMile.setBSName(radwinLastmile.getBtsName());
			HSUConfigParameters hsuConfigParameters = new HSUConfigParameters();
			if (StringUtils.isNotBlank(radwinLastmile.getSiteName()))
				hsuConfigParameters.setName(radwinLastmile.getSiteName());
			if (StringUtils.isNotBlank(radwinLastmile.getSectorId()))
				hsuConfigParameters.setSectorID(radwinLastmile.getSectorId());
			if (StringUtils.isNotBlank(radwinLastmile.getFrequency()))
				hsuConfigParameters.setFrequency(radwinLastmile.getFrequency());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteLat()))
				hsuConfigParameters.setLatitude(radwinLastmile.getSiteLat().trim());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteLong()))
				hsuConfigParameters.setLongitude(radwinLastmile.getSiteLong().trim());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteContact()))
				hsuConfigParameters.setSiteContact(radwinLastmile.getSiteContact());
			if (StringUtils.isNotBlank(radwinLastmile.getCustomerLocation()))
				hsuConfigParameters.setCustomerLocation(radwinLastmile.getCustomerLocation());
			if (StringUtils.isNotBlank(radwinLastmile.getHsuIp())) {
				IPAddress hscuMgmtIp = new IPAddress();
				hscuMgmtIp.setAddress(radwinLastmile.getHsuIp());
				hsuConfigParameters.setHsuMgmtIP(hscuMgmtIp);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getHsuSubnet())) {
				IPAddress hscuMgmtSubnet = new IPAddress();
				hscuMgmtSubnet.setAddress(radwinLastmile.getHsuSubnet());
				hsuConfigParameters.setHsuMgmtSubnet(hscuMgmtSubnet);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getGatewayIp())) {
				IPAddress hscuGatewayIp = new IPAddress();
				hscuGatewayIp.setAddress(radwinLastmile.getGatewayIp());
				hsuConfigParameters.setHsuGatewayIP(hscuGatewayIp);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getHsuMacAddr()))
				hsuConfigParameters.setHsuMacAddress(radwinLastmile.getHsuMacAddr().replaceAll(" ", ":").replaceAll("-", ":"));
			if (StringUtils.isNotBlank(radwinLastmile.getMgmtVlanid()))
				hsuConfigParameters.setManagementVlanID(ActivationUtils.toInteger(radwinLastmile.getMgmtVlanid().trim()));
			if (StringUtils.isNotBlank(radwinLastmile.getNtpServerIp())) {
				IPAddress ntpServerIp = new IPAddress();
				ntpServerIp.setAddress(radwinLastmile.getNtpServerIp());
				hsuConfigParameters.setNtpServerIP(ntpServerIp);
			}
			if (radwinLastmile.getCustomerLocation() != null)
				hsuConfigParameters.setCustomerLocation(radwinLastmile.getCustomerLocation());
			if (radwinLastmile.getSiteName() != null)
				hsuConfigParameters.setName(radwinLastmile.getSiteName());
			if (StringUtils.isNotBlank(radwinLastmile.getNtpOffset()))
				hsuConfigParameters.setNtpOffset(ActivationUtils.toInteger(radwinLastmile.getNtpOffset()));
			Protocol protocol = new Protocol();
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolSnmp()))
				protocol.setSnmp(radwinLastmile.getProtocolSnmp());
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolWebinterface()))
				protocol.setWebAccess(radwinLastmile.getProtocolWebinterface());
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolTelnet()))
				protocol.setTelnet(radwinLastmile.getProtocolTelnet());
			hsuConfigParameters.setProtocol(protocol);
			if (StringUtils.isNotBlank(radwinLastmile.getReqd_tx_power()))
				hsuConfigParameters.setRequiredTxPower(ActivationUtils.toInteger(radwinLastmile.getReqd_tx_power()));
			if (StringUtils.isNotBlank(radwinLastmile.getEthernet_Port_Config()))
				hsuConfigParameters.setEthernetPortConfiguration(radwinLastmile.getEthernet_Port_Config());
			radwin5kLastMile.setHSUConfigParameters(hsuConfigParameters);
			HBSConfigParameters hbsConfigParameters = new HBSConfigParameters();
			if (StringUtils.isNotBlank(radwinLastmile.getVlanMode()))
				hbsConfigParameters.setVlanMode((!radwinLastmile.getVlanMode().contains("NOT_APPLICABLE"))
						? radwinLastmile.getVlanMode().toLowerCase()
						: radwinLastmile.getVlanMode());
			if (StringUtils.isNotBlank(radwinLastmile.getDataVlanid()))
				hbsConfigParameters.setDataVlanID(ActivationUtils.toInteger(radwinLastmile.getDataVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getDataVlanPriority()))
				hbsConfigParameters
						.setDataVlanPriority(ActivationUtils.toInteger(radwinLastmile.getDataVlanPriority()));
			if (StringUtils.isNotBlank(radwinLastmile.getAllowedVlanid()))
				hbsConfigParameters.setAllowedVlanID(ActivationUtils.toInteger(radwinLastmile.getAllowedVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getHsuEgressTraffic()))
				hbsConfigParameters.setHsuEgressTraffic(radwinLastmile.getHsuEgressTraffic().toUpperCase());
			if (StringUtils.isNotBlank(radwinLastmile.getHsuIngressTraffic()))
				hbsConfigParameters.setHsuIngressTraffic(radwinLastmile.getHsuIngressTraffic().toUpperCase());
			if (radwinLastmile.getMirUl() != null)
				hbsConfigParameters.setMirUL(Math.round(radwinLastmile.getMirUl()));
			if (radwinLastmile.getMirDl() != null)
				hbsConfigParameters.setMirDL(Math.round(radwinLastmile.getMirDl()));
			if (radwinLastmile.getUntagVlanId() != null)
				hbsConfigParameters.setUntagVlanID(ActivationUtils.getBooleanValue(radwinLastmile.getUntagVlanId()));
			radwin5kLastMile.setHBSConfigParameters(hbsConfigParameters);
			service.setRadwin5KLastmile(radwin5kLastMile);
		}
	}

	/**
	 * 
	 * createRfWimax
	 * 
	 * @param serviceDetail
	 * @param service
	 * @param lmComponent
	 */
	protected void createRfWimax(ServiceDetail serviceDetail, OrderInfo3.Service service, LmComponent lmComponent) {
		for (WimaxLastmile wimaxLastMile : lmComponent.getWimaxLastmiles()) {
			WimaxLastMile netWimaxLastMile = new WimaxLastMile();
			if("ECO INTERNET".equalsIgnoreCase(serviceDetail.getServiceSubtype())) {
				netWimaxLastMile.setServiceSubType("STDILL");
			}else {
				netWimaxLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			}
			netWimaxLastMile.setServiceType(serviceDetail.getServiceType());
			if (serviceDetail.getBurstableBw() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
					}
				}
				netWimaxLastMile.setPortSpeed(portBandwidth);
			} else {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(Math.abs(ActivationUtils.toFloat(serviceDetail.getServiceBandwidthUnit()) * 1024));
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
					}
				}
				netWimaxLastMile.setPortSpeed(portBandwidth);
			}

			if (StringUtils.isNotBlank(serviceDetail.getNetpRefid()))
				netWimaxLastMile.setInstanceID(serviceDetail.getNetpRefid());
			if (StringUtils.isNotBlank(wimaxLastMile.getBtsName()))
				netWimaxLastMile.setBSName(wimaxLastMile.getBtsName());
			if (StringUtils.isNotBlank(wimaxLastMile.getBtsIp()))
				netWimaxLastMile.setBTSIP(wimaxLastMile.getBtsIp());
			if (StringUtils.isNotBlank(wimaxLastMile.getUniqueName()))
				netWimaxLastMile.setUniqueName(wimaxLastMile.getUniqueName());
			if (StringUtils.isNotBlank(wimaxLastMile.getSumacAddress()))
				netWimaxLastMile.setSUMACAddress(wimaxLastMile.getSumacAddress());
			if (StringUtils.isNotBlank(wimaxLastMile.getSuMgmtIp())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(wimaxLastMile.getSuMgmtIp());
				netWimaxLastMile.setSUMgmtIP(ipv4Address);
			}
			if (StringUtils.isNotBlank(wimaxLastMile.getSuMgmtSubnet())) {
				IPV4Address ipv4SuMgSubAddress = new IPV4Address();
				ipv4SuMgSubAddress.setAddress(wimaxLastMile.getSuMgmtSubnet());
				netWimaxLastMile.setSUMgmtSubnet(ipv4SuMgSubAddress);
			}
			if (StringUtils.isNotBlank(wimaxLastMile.getGatewayMgmtIp())) {
				IPV4Address ipv4SuGateAddress = new IPV4Address();
				ipv4SuGateAddress.setAddress(wimaxLastMile.getGatewayMgmtIp());
				netWimaxLastMile.setGatewayMgmtIP(ipv4SuGateAddress);
			}
			if (StringUtils.isNotBlank(wimaxLastMile.getHomeRegion()))
				netWimaxLastMile.setHomeRegion(wimaxLastMile.getHomeRegion());
			if (StringUtils.isNotBlank(wimaxLastMile.getDescription1()))
				netWimaxLastMile.setDescription1(wimaxLastMile.getDescription1());
			if (StringUtils.isNotBlank(wimaxLastMile.getDescription2()))
				netWimaxLastMile.setDescription2(wimaxLastMile.getDescription2());

			String provisioningMode = wimaxLastMile.getProvisioningMode();
			WimaxLastMile.ProvisioningMode provMode = new WimaxLastMile.ProvisioningMode();
			MVLANUntaggedMode mvlanUntaggedMode = new MVLANUntaggedMode();
			if (wimaxLastMile.getSsvlanTagging() != null)
				mvlanUntaggedMode
						.setSSVLANTaggingEnabled(ActivationUtils.getBooleanValue(wimaxLastMile.getSsvlanTagging()));
			NATMode natMode = new NATMode();
			if (StringUtils.isNotBlank(wimaxLastMile.getCuststaticWanipGateway()))
				natMode.setStaticCustomerWANIPGateway(wimaxLastMile.getCuststaticWanipGateway());
			if (StringUtils.isNotBlank(wimaxLastMile.getCuststaticWanipMask()))
				natMode.setStaticCustomerWANIPMask(wimaxLastMile.getCuststaticWanipMask());
			if (StringUtils.isNotBlank(wimaxLastMile.getCuststaticWanip()))
				natMode.setStaticCustomerWANIP(wimaxLastMile.getCuststaticWanip());

			for (VlanQosProfile vlanQosProf : wimaxLastMile.getVlanQosProfiles()) {
				VLANQOSProfile vlanQos = new VLANQOSProfile();
				if (vlanQosProf.getVlanId() != null)
					vlanQos.setVlanID(vlanQosProf.getVlanId());
				if (StringUtils.isNotBlank(vlanQosProf.getUpstreamQosProfile()))
					vlanQos.setUpstreamQoSProfile(vlanQosProf.getUpstreamQosProfile());
				if (StringUtils.isNotBlank(vlanQosProf.getDownstreamQosProfile()))
					vlanQos.setDownstreamQoSProfile(vlanQosProf.getDownstreamQosProfile());
				mvlanUntaggedMode.getMvlanQoSProfile().add(vlanQos);
				natMode.setNatQoSProfile(vlanQos);

			}
			if (provisioningMode != null && provisioningMode.toLowerCase().contains("untagged")) {
				provMode.setMvlanUntaggedmode(mvlanUntaggedMode);
			} else {
				provMode.setNatmode(natMode);
			}
			netWimaxLastMile.setProvisioningMode(provMode);
			service.setWimaxLastmile(netWimaxLastMile);
		}
	}

	/**
	 * createRfCambium
	 * 
	 * @param serviceDetail
	 * @param service
	 * @param lmComponent
	 */
	protected void createRfCambium(ServiceDetail serviceDetail, OrderInfo3.Service service, LmComponent lmComponent) {
		for (com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile camLastMile : lmComponent
				.getCambiumLastmiles()) {
			// createRFProductConfig(serviceDetail, service);
			// TODO -> LM COMPON
			CambiumLastmile cambiumLastMile = new CambiumLastmile();
			if("ECO INTERNET".equalsIgnoreCase(serviceDetail.getServiceSubtype())) {
				cambiumLastMile.setServiceSubType("STDILL");
			}else {
				cambiumLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			}
			cambiumLastMile.setServiceType(serviceDetail.getServiceType());
			MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstCambiumDetails) && Objects.nonNull(mstCambiumDetails.getNetpRefId()) && !mstCambiumDetails.getNetpRefId().isEmpty()){
				cambiumLastMile.setInstanceID(mstCambiumDetails.getNetpRefId());
			}else if(Objects.nonNull(serviceDetail.getNetpRefid())){
				cambiumLastMile.setInstanceID(serviceDetail.getNetpRefid());
			}
			if (StringUtils.isNotBlank(camLastMile.getBsName()))
				cambiumLastMile.setBSName(camLastMile.getBsName());
			if (StringUtils.isNotBlank(camLastMile.getBsIp()))
				cambiumLastMile.setBTSIP(camLastMile.getBsIp());
			if (StringUtils.isNotBlank(serviceDetail.getServiceType()))
				cambiumLastMile.setServiceType(serviceDetail.getServiceType());

			if (camLastMile.getPortSpeed() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				portBandwidth.setSpeed(camLastMile.getPortSpeed());
				if (StringUtils.isNotBlank(camLastMile.getPortSpeedUnit()))
					portBandwidth.setUnit(ActivationUtils.getBandwithUnit(camLastMile.getPortSpeedUnit()));
				cambiumLastMile.setPortSpeed(portBandwidth);
			}
			if (StringUtils.isNotBlank(camLastMile.getSuMacAddress()))
				cambiumLastMile.setSUMACAddress(camLastMile.getSuMacAddress());
			if (StringUtils.isNotBlank(camLastMile.getMgmtIpGateway())) {
				IPV4Address suGatewayIpv4 = new IPV4Address();
				suGatewayIpv4.setAddress(camLastMile.getMgmtIpGateway());
				cambiumLastMile.setGatewayMgmtIP(suGatewayIpv4);
			}
			if (StringUtils.isNotBlank(camLastMile.getMgmtIpForSsSu())) {
				IPV4Address suMgmtIpv4 = new IPV4Address();
				suMgmtIpv4.setAddress(camLastMile.getMgmtIpForSsSu());
				cambiumLastMile.setSUMgmtIP(suMgmtIpv4);
			}
			if (StringUtils.isNotBlank(camLastMile.getMgmtSubnetForSsSu())) {
				IPV4Address suMgtSubnetIpv4 = new IPV4Address();
				suMgtSubnetIpv4.setAddress(camLastMile.getMgmtSubnetForSsSu());
				cambiumLastMile.setSUMgmtSubnet(suMgtSubnetIpv4);
			}
			cambiumLastMile.setAPIVersion("PMP.SM(v13.1.3)");
			if (StringUtils.isNotBlank(camLastMile.getDhcpState()))
				cambiumLastMile.setDHCPState(camLastMile.getDhcpState().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getLinkSpeed()))
				cambiumLastMile.setLinkSpeed(camLastMile.getLinkSpeed().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getRegionCode()))
				cambiumLastMile.setRegionCode(camLastMile.getRegionCode());
			if (camLastMile.getWebpageAutoUpdate() != null)
				cambiumLastMile.setWebpageAutoUpdate(camLastMile.getWebpageAutoUpdate());
			if (StringUtils.isNotBlank(camLastMile.getCustomRadioFrequencyList()))
				cambiumLastMile.setRFScanList(camLastMile.getCustomRadioFrequencyList());
			if (StringUtils.isNotBlank(camLastMile.getBridgeEntryTimeout()))
				cambiumLastMile.setBridgeEntryTimeout(camLastMile.getBridgeEntryTimeout());
			if (StringUtils.isNotBlank(camLastMile.getDynamicRateAdapt())) {
				if (camLastMile.getDynamicRateAdapt().equalsIgnoreCase("1x"))
					cambiumLastMile.setDynamicRateAdapt("0");
				else
					cambiumLastMile.setDynamicRateAdapt("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getMulticastDestinationAddr()))
				cambiumLastMile.setMulticastDestAddress(camLastMile.getMulticastDestinationAddr().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getDeviceDefaultReset()))
				cambiumLastMile.setDeviceDefaultReset(camLastMile.getDeviceDefaultReset());
			if (StringUtils.isNotBlank(camLastMile.getPowerupmodeWithno802_3link())) {
				if (camLastMile.getPowerupmodeWithno802_3link().toLowerCase().contains("operational"))
					cambiumLastMile.setPowerupMode("0");
				else
					cambiumLastMile.setPowerupMode("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getFrameTimingPulseGated()))
				cambiumLastMile.setFrameTimingPulseGated(camLastMile.getFrameTimingPulseGated().toUpperCase());
			if (camLastMile.getColorCode1() != null)
				cambiumLastMile.setColourCode(camLastMile.getColorCode1());
			if (StringUtils.isNotBlank(camLastMile.getTransmitterOutputPower()))
				cambiumLastMile
						.setTransmitterOutputPower(ActivationUtils.toInteger(camLastMile.getTransmitterOutputPower()));
			if (StringUtils.isNotBlank(camLastMile.getLargevcDataq()))
				cambiumLastMile.setEnableLargeDataVCQ(camLastMile.getLargevcDataq().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getInstallationColorCode()))
				cambiumLastMile.setInstallationColourCode(camLastMile.getInstallationColorCode().toUpperCase());
			CambiumAAAConfig cambiumAAAConfig = new CambiumAAAConfig();
			if (StringUtils.isNotBlank(camLastMile.getEthernetAccess()))
				cambiumAAAConfig.setEthernetAccess(camLastMile.getEthernetAccess().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getUserName()))
				cambiumAAAConfig.setUsername(camLastMile.getUserName());
			if (StringUtils.isNotBlank(camLastMile.getAllowlocalloginAfteraaareject())) {
				if (camLastMile.getAllowlocalloginAfteraaareject().toLowerCase().contains("enabled"))
					cambiumAAAConfig.setAllowLocalLoginAfterAAAReject("1");
				else
					cambiumAAAConfig.setAllowLocalLoginAfterAAAReject("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getHipriorityUplinkCir()))
				cambiumAAAConfig.setHighPriorityUplinkCIR(camLastMile.getHipriorityUplinkCir());
			if (StringUtils.isNotBlank(camLastMile.getAuthenticationKey()))
				cambiumAAAConfig.setAuthenticationKey(ActivationUtils.toInteger(camLastMile.getAuthenticationKey()));
			if (StringUtils.isNotBlank(camLastMile.getRealm()))
				cambiumAAAConfig.setRealm(ActivationUtils.toInteger(camLastMile.getRealm()));
			if (StringUtils.isNotBlank(camLastMile.getDeviceaccessTracking()))
				cambiumAAAConfig.setDeviceAccessTracking(camLastMile.getDeviceaccessTracking());
			if (StringUtils.isNotBlank(camLastMile.getLowpriorityDownlinkCir()))
				cambiumAAAConfig.setLowPriorityDownlinkCIR(camLastMile.getLowpriorityDownlinkCir());
			if (StringUtils.isNotBlank(camLastMile.getSelectKey())) {
				if (camLastMile.getSelectKey().toLowerCase().contains("default"))
					cambiumAAAConfig.setSelectKey("0");
				else
					cambiumAAAConfig.setSelectKey("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getEnforceAuthentication())) {
				if (camLastMile.getEnforceAuthentication().toLowerCase().contains("default"))
					cambiumAAAConfig.setEnforceAuth("0");
				else if(camLastMile.getEnforceAuthentication().toUpperCase().contains("AAA"))
					cambiumAAAConfig.setEnforceAuth("1");
				else
					cambiumAAAConfig.setEnforceAuth("2");
			}
			if (StringUtils.isNotBlank(camLastMile.getIdentity())) {
				cambiumAAAConfig.setIdentity(camLastMile.getIdentity());
			}else {
				cambiumAAAConfig.setIdentity(Objects.nonNull(camLastMile.getSuMacAddress()) && !camLastMile.getSuMacAddress().isEmpty()?camLastMile.getSuMacAddress().replaceAll(" ", "-"):camLastMile.getSuMacAddress());
			}
			if (StringUtils.isNotBlank(camLastMile.getDownlinkBurstAllocation()))
				cambiumAAAConfig.setDownlinkBurstAllocation(camLastMile.getDownlinkBurstAllocation());
			if (StringUtils.isNotBlank(camLastMile.getLowpriorityUplinkCir()))
				cambiumAAAConfig.setLowPriorityUplinkCIR(camLastMile.getLowpriorityUplinkCir());
			if (StringUtils.isNotBlank(camLastMile.getPassword()))
				cambiumAAAConfig.setPassword(camLastMile.getPassword());
			if (StringUtils.isNotBlank(camLastMile.getUplinkBurstAllocation()))
				cambiumAAAConfig.setUplinkBurstAllocation(camLastMile.getUplinkBurstAllocation());
			if (StringUtils.isNotBlank(camLastMile.getEnableBroadcastMulticastDatarate()))
				cambiumAAAConfig.setEnableBroadcastMulticastRate(camLastMile.getEnableBroadcastMulticastDatarate());
			if (StringUtils.isNotBlank(camLastMile.getPhase1())) {
				if (camLastMile.getPhase1().toUpperCase().contains("EAPTTLS"))
					cambiumAAAConfig.setPhase1("0");
				else
					cambiumAAAConfig.setPhase1("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getServerCommonName()))
				cambiumAAAConfig.setServerCommonName(camLastMile.getServerCommonName());
			if (StringUtils.isNotBlank(camLastMile.getBwUplinkSustainedRate()))
				cambiumAAAConfig.setBandwidthUplinkSustainRate(camLastMile.getBwUplinkSustainedRate());
			if (StringUtils.isNotBlank(camLastMile.getHipriorityChannel()))
				cambiumAAAConfig.setHighPriorityChannel(camLastMile.getHipriorityChannel());
			if (StringUtils.isNotBlank(camLastMile.getPhase2())) {
				if (camLastMile.getPhase2().toUpperCase().contains("PAP"))
					cambiumAAAConfig.setPhase2("0");
				else if (camLastMile.getPhase2().toUpperCase().contains("CHAP"))
					cambiumAAAConfig.setPhase2("1");
				else
					cambiumAAAConfig.setPhase2("2");
			}
			if (StringUtils.isNotBlank(camLastMile.getNetworkAccessibility())) {
				if (camLastMile.getNetworkAccessibility().toLowerCase().contains("public"))
					cambiumAAAConfig.setNetworkAccessibility("1");
				else
					cambiumAAAConfig.setNetworkAccessibility("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getBwDownlinkSustainedRate()))
				cambiumAAAConfig.setBandwidthDownlinkSustainRate(camLastMile.getBwDownlinkSustainedRate());
			if (StringUtils.isNotBlank(camLastMile.getUseRealmStatus()))
				cambiumAAAConfig.setUseRealmStatus(camLastMile.getUseRealmStatus());
			if (StringUtils.isNotBlank(camLastMile.getUserAuthenticationMode())) {
				if (camLastMile.getUserAuthenticationMode().toLowerCase().contains("than"))
					cambiumAAAConfig.setUserAuthenticationMode("2");
				else if (camLastMile.getUserAuthenticationMode().toLowerCase().contains("local"))
					cambiumAAAConfig.setUserAuthenticationMode("1");
				else
					cambiumAAAConfig.setUserAuthenticationMode("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getHipriorityDownlinkCir()))
				cambiumAAAConfig.setHighPriorityDownlinkCIR(camLastMile.getHipriorityDownlinkCir());
			cambiumLastMile.setCambiumAAAConfig(cambiumAAAConfig);
			CambiumAntennaParameters cambiumAntennaParameters = new CambiumAntennaParameters();
			if (StringUtils.isNotBlank(camLastMile.getSmHeight())){
				Double smHeight = Double.valueOf(camLastMile.getSmHeight());
				Integer value = smHeight.intValue();
				cambiumAntennaParameters.setHeight(value.toString());
			}
			if (StringUtils.isNotBlank(camLastMile.getLatitudeSettings()))
				cambiumAntennaParameters.setLattitude(camLastMile.getLatitudeSettings().trim());
			if (StringUtils.isNotBlank(camLastMile.getLongitudeSettings()))
				cambiumAntennaParameters.setLongitude(camLastMile.getLongitudeSettings().trim());
			cambiumLastMile.setCambiumAntennaParameters(cambiumAntennaParameters);
			CambiumVLANAttributes cambiumVLANAttributes = new CambiumVLANAttributes();
			if (StringUtils.isNotBlank(camLastMile.getAllowFrametypes())) {
				if (camLastMile.getAllowFrametypes().toLowerCase().contains("untag"))
					cambiumVLANAttributes.setAllowFrameTypes("2");
				else if (camLastMile.getAllowFrametypes().toLowerCase().contains("tag"))
					cambiumVLANAttributes.setAllowFrameTypes("1");
				else
					cambiumVLANAttributes.setAllowFrameTypes("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getDynamicLearning()))
				cambiumVLANAttributes.setDynamicLearning(camLastMile.getDynamicLearning().toUpperCase());
			if (camLastMile.getVlanAgeingTimeout() != null)
				cambiumVLANAttributes.setVlanAgingTimeout(ActivationUtils.toString(camLastMile.getVlanAgeingTimeout()));
			if (StringUtils.isNotBlank(camLastMile.getMgmtVid()))
				cambiumVLANAttributes.setManagementVID(camLastMile.getMgmtVid());
			if (StringUtils.isNotBlank(camLastMile.getDefaultPortVid()))
				cambiumVLANAttributes.setDefaultPortVID(ActivationUtils.toInteger(camLastMile.getDefaultPortVid()));
			if (StringUtils.isNotBlank(camLastMile.getSmMgmtVidPassthrough()))
				cambiumVLANAttributes
						.setSSManagementVIDPassthrough(camLastMile.getSmMgmtVidPassthrough().toUpperCase());
			if (camLastMile.getProviderVid() != null)
				cambiumVLANAttributes.setProviderVID(ActivationUtils.toString(camLastMile.getProviderVid()));
			if (StringUtils.isNotBlank(camLastMile.getMappedMacAddress())) {
				MACAddressVIDMap macAddressVIDMap = new MACAddressVIDMap();
				macAddressVIDMap.setMACAddress(camLastMile.getMappedMacAddress());
				cambiumVLANAttributes.getMappedMACAddress().add(macAddressVIDMap);
			}
			if (StringUtils.isNotBlank(camLastMile.getMappedVid1()))
				cambiumVLANAttributes.setMappedVID1(camLastMile.getMappedVid1());
			if (camLastMile.getMappedVid2() != null)
				cambiumVLANAttributes.setMappedVID2(ActivationUtils.toString(camLastMile.getMappedVid2()));
			if (StringUtils.isNotBlank(camLastMile.getVlanPorttype())) {
				if (camLastMile.getVlanPorttype().toLowerCase().contains("qinq"))
					cambiumVLANAttributes.setVlanPortType("1");
				else
					cambiumVLANAttributes.setVlanPortType("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getAcceptQinqFrames()))
				cambiumVLANAttributes.setAcceptQinQFrames(camLastMile.getAcceptQinqFrames().toUpperCase());
			if(camLastMile.getBsName().toLowerCase().contains("lab") || camLastMile.getBsName().toLowerCase().contains("test")){
				cambiumVLANAttributes.setManagementVID("3551");
				cambiumVLANAttributes.setDefaultPortVID(3551);
				cambiumVLANAttributes.setMappedVID1("3551");
				cambiumVLANAttributes.setMappedVID2("3551");
			}

			cambiumLastMile.setCambiumVLANAttributes(cambiumVLANAttributes);
			SNMPParameters snmpParameters = new SNMPParameters();

			IPAddressList ipSnmpTrapIpAddressList = new IPAddressList();
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp1())) {
				IPAddress snmpIp1 = new IPV4Address();
				snmpIp1.setAddress(camLastMile.getSnmpTrapIp1());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp1);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp2())) {
				IPAddress snmpIp2 = new IPV4Address();
				snmpIp2.setAddress(camLastMile.getSnmpTrapIp2());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp2);

			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp3())) {
				IPAddress snmpIp3 = new IPV4Address();
				snmpIp3.setAddress(camLastMile.getSnmpTrapIp3());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp3);
			}

			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp4())) {
				IPAddress snmpIp4 = new IPV4Address();
				snmpIp4.setAddress(camLastMile.getSnmpTrapIp4());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp4);
			}

			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp5())) {
				IPAddress snmpIp5 = new IPV4Address();
				snmpIp5.setAddress(camLastMile.getSnmpTrapIp5());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp5);
			}

			snmpParameters.setSNMPTrapIPList(ipSnmpTrapIpAddressList);

			IPAddressList ipSnmpAccessAddressList = new IPAddressList();
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp1())) {
				IPAddress accessIp1 = new IPV4Address();
				accessIp1.setAddress(camLastMile.getSnmpAccessingIp1());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp1);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp2())) {
				IPAddress accessIp2 = new IPV4Address();
				accessIp2.setAddress(camLastMile.getSnmpAccessingIp2());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp2);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp3())) {
				IPAddress accessIp3 = new IPV4Address();
				accessIp3.setAddress(camLastMile.getSnmpAccessingIp3());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp3);
			}

			snmpParameters.setSNMPAccessingIPList(ipSnmpAccessAddressList);
			cambiumLastMile.setSNMPParameters(snmpParameters);
			if (StringUtils.isNotBlank(camLastMile.getSiteinfoViewabletoGuestusers()))
				cambiumLastMile.setViewableToGuestUsers(camLastMile.getSiteinfoViewabletoGuestusers().toUpperCase());
			CambiumSite cambiumSite = new CambiumSite();
			if (StringUtils.isNotBlank(camLastMile.getSiteContact()))
				cambiumSite.setContact(camLastMile.getSiteContact());
			if (StringUtils.isNotBlank(camLastMile.getSiteLocation()))
				cambiumSite.setLocation(camLastMile.getSiteLocation());
			if (StringUtils.isNotBlank(camLastMile.getSiteName()))
				cambiumSite.setName(camLastMile.getSiteName());
			cambiumLastMile.setCambiumSiteDetails(cambiumSite);
			if (StringUtils.isNotBlank(camLastMile.getDeviceType()))
				cambiumLastMile.setDeviceType(camLastMile.getDeviceType());

			if(camLastMile.getBsName().toLowerCase().contains("lab") || camLastMile.getBsName().toLowerCase().contains("test"))
				cambiumLastMile.setColourCode("252");
			service.setCambiumLastmile(cambiumLastMile);
		}
	}

	/**
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
		LOGGER.info("createServiceDetails");
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

	protected VirtualRouteForwardingServiceInstance createVrfDetails(ServiceDetail serviceDetail,
			GVPNService gvpnService, String currentLegId) {
		VirtualRouteForwardingServiceInstance vrf = null;
		Set<Vrf> vrfs = serviceDetail.getVrfs();
		for (Vrf vr : vrfs) {
			if (gvpnService != null) {
				gvpnService.setIsVRFLiteEnabled(ActivationUtils.getBooleanValue(vr.getIsvrfLiteEnabled()));
				gvpnService.setCurrentCustomerSiteId(serviceDetail.getServiceId());
				if (currentLegId != null)
					gvpnService.setCurrentLegID(currentLegId);
			}
			vrf = new VirtualRouteForwardingServiceInstance();
			if (StringUtils.isNotBlank(vr.getVrfName())) {
				RoutingPolicy impRoutingPolicy = new RoutingPolicy();
				RoutingPolicy expRoutingPolicy = new RoutingPolicy();
				for (PolicyType policyType : vr.getPolicyTypes()) {
					if (policyType.getIsvprnImportpolicy() != null && policyType.getIsvprnImportpolicy() == 1) {
						impRoutingPolicy.setIsPreprovisioned(
								ActivationUtils.getBooleanValue(policyType.getIsvprnImportPreprovisioned()));
						impRoutingPolicy.setName(policyType.getIsvprnImportpolicyName());
						vrf.setALUVPRNImportPolicy(impRoutingPolicy);
					}

					if (policyType.getIsvprnExportpolicy() != null && policyType.getIsvprnExportpolicy() == 1) {
						expRoutingPolicy.setIsPreprovisioned(
								ActivationUtils.getBooleanValue(policyType.getIsvprnExportPreprovisioned()));
						expRoutingPolicy.setName(policyType.getIsvprnExportpolicyName());
						vrf.setALUVPRNExportPolicy(expRoutingPolicy);
					}
				}
			}
			MaximumRoutes maximumRoutes = new MaximumRoutes();
			if (StringUtils.isNotBlank(vr.getThreshold()) && StringUtils.isNotBlank(vr.getWarnOn()))
				maximumRoutes.setThresholdValue(ActivationUtils.toInteger(vr.getThreshold()));
			if (StringUtils.isNotBlank(vr.getMaxRoutesValue()) && StringUtils.isNotBlank(vr.getWarnOn()))
				maximumRoutes.setValue(ActivationUtils.toInteger(vr.getMaxRoutesValue()));
			if (StringUtils.isNotBlank(vr.getWarnOn()))
				maximumRoutes.setWarnOn(vr.getWarnOn().toUpperCase());

			if (StringUtils.isNotBlank(vr.getThreshold()) || StringUtils.isNotBlank(vr.getWarnOn())
					|| StringUtils.isNotBlank(vr.getMaxRoutesValue())) {
				vrf.setMaximumRoutes(maximumRoutes);
			}
		}
		return vrf;
	}

	protected ServiceLink createServiceLinks(ServiceDetail serviceDetail) {
		ServiceLink serviceLink = new ServiceLink();
		if (serviceDetail.getSvclinkRole() != null)
			serviceLink.setRole(serviceDetail.getSvclinkRole());
		if (serviceDetail.getSvclinkSrvid() != null)
			serviceLink.setServiceId(serviceDetail.getSvclinkSrvid());
		return serviceLink;

	}

	protected void createIpAddressDetails() {

	}

	protected void createWanV4Address() {

	}

	protected ExtendedLAN createExtendedLanConfig(IpAddressDetail ipAddressDetail) {
		ExtendedLAN extendedLAN = new ExtendedLAN();
		if (ipAddressDetail.getExtendedLanEnabled() != null)
			extendedLAN.setIsEnabled(ActivationUtils.getBooleanValue(ipAddressDetail.getExtendedLanEnabled()));
		if (ipAddressDetail.getNoMacAddress() != null)
			extendedLAN.setNumberOfMacAddresses(ipAddressDetail.getNoMacAddress());
		return extendedLAN;

	}

	protected void createLanIpAddressV4() {

	}

	protected void createLanIpAddressV6() {

	}

	protected Router createPeRouterDetails(InterfaceProtocolMapping interfaceProtocolMapping,
			ServiceDetail serviceDetail) {
		RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
		Router router = new Router();
		if (StringUtils.isNotBlank(routerDetail.getRouterHostname()))
			router.setHostName(routerDetail.getRouterHostname());
		if (StringUtils.isNotBlank(routerDetail.getRouterRole()))
			router.setRole(routerDetail.getRouterRole());
		if (StringUtils.isNotBlank(routerDetail.getIpv4MgmtAddress())) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(routerDetail.getIpv4MgmtAddress());
			router.setV4ManagementIPAddress(ipv4Address);
		}
		if (StringUtils.isNotBlank(routerDetail.getIpv6MgmtAddress())) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(routerDetail.getIpv6MgmtAddress());
			router.setV6ManagementIPAddress(ipv6Address);
		}
		if (StringUtils.isNotBlank(routerDetail.getRouterType()))
			router.setType(routerDetail.getRouterType());
		router.setWanInterface(createWanInterfaceEthernet(interfaceProtocolMapping, router));
		if (serviceDetail != null) {
			Set<Topology> topologies = serviceDetail.getTopologies();
			for (Topology topology : topologies) {
				TopologyInfo topologyInfo = createTopologyInformation(topology);
				if (topologyInfo != null) {
					router.setTopologyInfo(topologyInfo);
					Set<RouterUplinkport> routerUplinkPorts = topology.getRouterUplinkports();
					for (RouterUplinkport routerUplinkPort : routerUplinkPorts) {
						router.setRouterTopologyInterface1(createRouterUplinkPorts1(routerUplinkPort));
						router.setRouterTopologyInterface2(createRouterUplinkPorts2(routerUplinkPort));
					}
				}
			}
		}
		return router;
	}

	protected WANInterface createWanInterfaceEthernet(InterfaceProtocolMapping interfaceProtocolMapping,
			Router router) {
		WANInterface wanInterface = new WANInterface();
		WANInterface.Interface interfce = new WANInterface.Interface();
		if (interfaceProtocolMapping.getEthernetInterface() != null)
			interfce.setEthernetInterface(
					createEthernetInterface(interfaceProtocolMapping.getEthernetInterface(), router, wanInterface));
		if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null)
			interfce.setSerialInterface(
					createWanInterfaceCE1Serial(interfaceProtocolMapping.getChannelizedE1serialInterface(), router));
		if (interfaceProtocolMapping.getChannelizedSdhInterface() != null)
			interfce.setChannelizedSDHInterface(
					createWanInterfaceCSdh(interfaceProtocolMapping.getChannelizedSdhInterface(), router));
		wanInterface.setInterface(interfce);
		// wanInterface.setStaticRoutes(createPeWanStaticRoutes(interfaceProtocolMapping.getStaticProtocol()));
		return wanInterface;
	}

	protected EthernetInterface createEthernetInterface(
			com.tcl.dias.serviceactivation.entity.entities.EthernetInterface ethernetInterfaceEntity, Router router,
			WANInterface wanInterface) {
		EthernetInterface ethernetInterface = null;
		if (ethernetInterfaceEntity != null) {
			ethernetInterface = new EthernetInterface();
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getInterfaceName()))
				ethernetInterface.setName(ethernetInterfaceEntity.getInterfaceName());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getPhysicalPort()))
				ethernetInterface.setPhysicalPortName(ethernetInterfaceEntity.getPhysicalPort());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ethernetInterfaceEntity.getModifiedIpv4Address());
				ethernetInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(ethernetInterfaceEntity.getModifiedIpv6Address());
				ethernetInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedSecondaryIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ethernetInterfaceEntity.getModifiedSecondaryIpv4Address());
				wanInterface.setSecondaryV4WANIPAddress(ipv4Address);
				//ethernetInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedSecondaryIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(ethernetInterfaceEntity.getModifiedSecondaryIpv6Address());
				ethernetInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMediaType()))
				ethernetInterface.setMediaType(ethernetInterfaceEntity.getMediaType());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getPortType()) && !ethernetInterfaceEntity.getPortType().toUpperCase().contains("LAG"))
				ethernetInterface.setPortType(ethernetInterfaceEntity.getPortType().toUpperCase());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getEncapsulation()))
				ethernetInterface.setEncapsulation(ethernetInterfaceEntity.getEncapsulation());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMode()))
				ethernetInterface.setMode(ethernetInterfaceEntity.getMode().toUpperCase());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getQosLoopinPassthrough()))
				ethernetInterface.setQosLoopinPassthroughInterface(ethernetInterfaceEntity.getQosLoopinPassthrough());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getFraming()))
				ethernetInterface.setFraming(ethernetInterfaceEntity.getFraming());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getInnerVlan()))
				ethernetInterface.setVlan(ActivationUtils.toInteger(ethernetInterfaceEntity.getInnerVlan()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getSpeed()))
				ethernetInterface.setSpeed(ethernetInterfaceEntity.getSpeed());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getDuplex()))
				ethernetInterface.setDuplex(ActivationUtils.getDuplex(ethernetInterfaceEntity.getDuplex()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getOuterVlan()))
				ethernetInterface.setSvlan(ActivationUtils.toInteger(ethernetInterfaceEntity.getOuterVlan()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getAutonegotiationEnabled()))
				ethernetInterface.setIsAutoNegotiation(ethernetInterfaceEntity.getAutonegotiationEnabled());
			if (ethernetInterfaceEntity.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIsbfdEnabled()));
			if (ethernetInterfaceEntity.getIsbfdEnabled() != null
					&& ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIsbfdEnabled()))
				ethernetInterface.setBfdConfig(createEthernetInterfaceHdlcConfig(ethernetInterfaceEntity));
			if (ethernetInterfaceEntity.getIshsrpEnabled() != null
					&& ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIshsrpEnabled())) {
				wanInterface.setHSRP(createCpeHrspProtocol(ethernetInterfaceEntity));
			}
			AccessControlList inBoundAccessControlList = new AccessControlList();
			AccessControlList outBoundAccessControlList = new AccessControlList();
			AccessControlList inBoundAccessControlListV6 = new AccessControlList();
			AccessControlList outBoundAccessControlListV6 = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = ethernetInterfaceEntity.getAclPolicyCriterias();
			for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {

				if (aclPolicyCriteria.getEndDate() == null) {
					if (aclPolicyCriteria.getInboundIpv4AclName() != null) {// TODO
						inBoundAccessControlList.setName(aclPolicyCriteria.getInboundIpv4AclName());
						ethernetInterface.setInboundAccessControlList(inBoundAccessControlList);
					}
					if (aclPolicyCriteria.getInboundIpv6AclName() != null) {// TODO
						inBoundAccessControlListV6.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						ethernetInterface.setInboundAccessControlListV6(inBoundAccessControlListV6);
					}

					if (aclPolicyCriteria.getOutboundIpv4AclName() != null) {// TODO
						outBoundAccessControlList.setName(aclPolicyCriteria.getOutboundIpv4AclName());
						ethernetInterface.setOutboundAccessControlList(outBoundAccessControlList);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundAccessControlListV6.setName(aclPolicyCriteria.getInboundIpv6AclName());
						ethernetInterface.setOutboundAccessControlListV6(outBoundAccessControlListV6);
					}
				}
			}
		}
		return ethernetInterface;

	}

	protected BFDConfig createEthernetInterfaceHdlcConfig(
			com.tcl.dias.serviceactivation.entity.entities.EthernetInterface ethernetInterfaceEntity) {
		BFDConfig bfdConfig = new BFDConfig();
		if (ethernetInterfaceEntity.getBfdtransmitInterval() != null)
			bfdConfig.setTransmitInterval(ActivationUtils.toInteger(ethernetInterfaceEntity.getBfdtransmitInterval()));
		if (ethernetInterfaceEntity.getBfdreceiveInterval() != null)
			bfdConfig.setRecieveInterval(ActivationUtils.toInteger(ethernetInterfaceEntity.getBfdreceiveInterval()));
		if (ethernetInterfaceEntity.getBfdMultiplier() != null)
			bfdConfig.setMultiplier(ActivationUtils.toInteger(ethernetInterfaceEntity.getBfdMultiplier()));
		return bfdConfig;
	}

	protected SerialInterface createWanInterfaceCE1Serial(ChannelizedE1serialInterface channelizedE1serialInterface,
			Router router) {
		SerialInterface serialInterface = null;
		if (channelizedE1serialInterface != null) {
			serialInterface = new SerialInterface();
			if (channelizedE1serialInterface.getInterfaceName() != null)
				serialInterface.setName(channelizedE1serialInterface.getInterfaceName());
			if (channelizedE1serialInterface.getPhysicalPort() != null)
				serialInterface.setPhysicalPortName(channelizedE1serialInterface.getPhysicalPort());
			if (channelizedE1serialInterface.getModifiedIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1serialInterface.getModifiedIpv4Address());
				serialInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedE1serialInterface.getModifiedIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1serialInterface.getModifiedIpv6Address());
				serialInterface.setV6IpAddress(ipv6Address);
			}
			if (channelizedE1serialInterface.getModifiedSecondaryIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1serialInterface.getModifiedSecondaryIpv4Address());
				serialInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedE1serialInterface.getModifiedSecondaryIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1serialInterface.getModifiedSecondaryIpv6Address());
				serialInterface.setV6IpAddress(ipv6Address);
			}
			if (channelizedE1serialInterface.getChannelGroupNumber() != null)
				serialInterface.setChannelGroupNumber(channelizedE1serialInterface.getChannelGroupNumber());
			if (channelizedE1serialInterface.getDlciValue() != null)
				serialInterface.setDlciValue(ActivationUtils.toInteger(channelizedE1serialInterface.getDlciValue()));
			for (int i = Integer.valueOf(channelizedE1serialInterface.getFirsttimeSlot()); i <= Integer
					.valueOf(channelizedE1serialInterface.getLasttimeSlot()); i++) {
				serialInterface.getTimeslot().add(String.valueOf(i));
			}
			if (channelizedE1serialInterface.getIsframed() != null)
				serialInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsframed()));

			if (channelizedE1serialInterface.getEncapsulation() != null) {
				serialInterface.setEncapsulation(channelizedE1serialInterface.getEncapsulation());
			}

			if (channelizedE1serialInterface.getMode() != null) {
				serialInterface.setMode(channelizedE1serialInterface.getMode().toUpperCase());
			}
			if (channelizedE1serialInterface.getIsframed() != null)
				serialInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsframed()));
			if (channelizedE1serialInterface.getFraming() != null) {
				serialInterface.setFraming(channelizedE1serialInterface.getFraming());
			}
			if (channelizedE1serialInterface.getIscrcforenabled() != null) {
				serialInterface.setCrcSize(channelizedE1serialInterface.getCrcsize());
			}
			if (channelizedE1serialInterface.getPortType() != null)
				serialInterface.setPortType(channelizedE1serialInterface.getPortType().toUpperCase());
			if (channelizedE1serialInterface.getIsbfdEnabled() != null)
				serialInterface.setBfdConfig(createCE1SerialHdlcConfig(channelizedE1serialInterface));
			if (channelizedE1serialInterface.getMtu() != null)
				serialInterface.setMtu(ActivationUtils.toInteger(channelizedE1serialInterface.getMtu()));

			if (channelizedE1serialInterface.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsbfdEnabled()));
			if (channelizedE1serialInterface.getEncapsulation() != null)
				serialInterface.setEncapsulation(channelizedE1serialInterface.getEncapsulation());

			AccessControlList inBoundAccessControlList = new AccessControlList();
			AccessControlList outBoundAccessControlList = new AccessControlList();
			AccessControlList inBoundAccessControlListV6 = new AccessControlList();
			AccessControlList outBoundAccessControlListV6 = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = channelizedE1serialInterface.getAclPolicyCriterias();
			for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
				if (aclPolicyCriteria.getEndDate() == null) {
					if (aclPolicyCriteria.getInboundIpv4AclName() != null) {// TODO
						inBoundAccessControlList.setName(aclPolicyCriteria.getInboundIpv4AclName());
						serialInterface.setInboundAccessControlList(inBoundAccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv4AclName() != null) {// TODO
						outBoundAccessControlList.setName(aclPolicyCriteria.getOutboundIpv4AclName());
						serialInterface.setOutboundAccessControlList(outBoundAccessControlList);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundAccessControlListV6.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						serialInterface.setOutboundAccessControlListV6(outBoundAccessControlListV6);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						inBoundAccessControlListV6.setName(aclPolicyCriteria.getInboundIpv6AclName());
						serialInterface.setInboundAccessControlListV6(inBoundAccessControlListV6);
					}
				}
			}

		}
		return serialInterface;
	}

	protected BFDConfig createCE1SerialHdlcConfig(ChannelizedE1serialInterface channelizedE1serialInterface) {
		BFDConfig bfdConfig = null;
		if (channelizedE1serialInterface != null) {
			bfdConfig = new BFDConfig();
			if (channelizedE1serialInterface.getBfdtransmitInterval() != null)
				bfdConfig.setTransmitInterval(
						ActivationUtils.toInteger(channelizedE1serialInterface.getBfdtransmitInterval()));
			if (channelizedE1serialInterface.getBfdreceiveInterval() != null)
				bfdConfig.setRecieveInterval(
						ActivationUtils.toInteger(channelizedE1serialInterface.getBfdreceiveInterval()));
			if (channelizedE1serialInterface.getBfdMultiplier() != null)
				bfdConfig.setMultiplier(ActivationUtils.toInteger(channelizedE1serialInterface.getBfdMultiplier()));
		}
		return bfdConfig;
	}

	protected ChannelizedSDHInterface createWanInterfaceCSdh(ChannelizedSdhInterface channelizedSdhInterface,
			Router router) {
		ChannelizedSDHInterface channelizedSDHInterface = null;
		if (channelizedSdhInterface != null) {
			channelizedSDHInterface = new ChannelizedSDHInterface();
			if (channelizedSdhInterface.getInterfaceName() != null)
				channelizedSDHInterface.setName(channelizedSdhInterface.getInterfaceName());
			if (channelizedSdhInterface.getPhysicalPort() != null)
				channelizedSDHInterface.setPhysicalPortName(channelizedSdhInterface.getPhysicalPort());
			if (channelizedSdhInterface.getModifiedIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInterface.getModifiedIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedSdhInterface.getModifiedIipv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInterface.getModifiedIipv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}
			if (channelizedSdhInterface.getModifiedSecondaryIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInterface.getModifiedSecondaryIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedSdhInterface.getModifiedSecondaryIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInterface.getModifiedSecondaryIpv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(channelizedSdhInterface.getChannelGroupNumber()))
				channelizedSDHInterface.setChannelGroupNumber(channelizedSdhInterface.getChannelGroupNumber());
			if (StringUtils.isNotBlank(channelizedSdhInterface.getDlciValue()))
				channelizedSDHInterface.setDlciValue(ActivationUtils.toInteger(channelizedSdhInterface.getDlciValue()));

			for (int i = Integer.valueOf(channelizedSdhInterface.get_4Kfirsttime_slot()); i <= Integer
					.valueOf(channelizedSdhInterface.get_4klasttimeSlot()); i++) {
				channelizedSDHInterface.getSixtyFourKTimeslot().add(String.valueOf(i));
			}
			if (StringUtils.isNotBlank(channelizedSdhInterface.getEncapsulation())) {
				channelizedSDHInterface.setEncapsulation(channelizedSdhInterface.getEncapsulation());
			}

			if (StringUtils.isNotBlank(channelizedSdhInterface.getMode())) {
				channelizedSDHInterface.setMode(channelizedSdhInterface.getMode().toUpperCase());
			}
			if (channelizedSdhInterface.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(channelizedSdhInterface.getIsbfdEnabled()));
			if (channelizedSdhInterface.getIsframed() != null)
				channelizedSDHInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedSdhInterface.getIsframed()));
			if (channelizedSdhInterface.getFraming() != null) {
				channelizedSDHInterface.setFraming(channelizedSdhInterface.getFraming());
			}
			if (channelizedSdhInterface.getPosframing() != null) {
				channelizedSDHInterface.setPosFraming(channelizedSdhInterface.getPosframing());
			}
			if (channelizedSdhInterface.getJ() != null)
				channelizedSDHInterface.setJ(ActivationUtils.toString(channelizedSdhInterface.getJ()));
			if (channelizedSdhInterface.getKlm() != null)
				channelizedSDHInterface.setKLM(channelizedSdhInterface.getKlm());
			if (channelizedSdhInterface.getPortType() != null)
				channelizedSDHInterface.setPortType(channelizedSdhInterface.getPortType().toUpperCase());
			if (channelizedSdhInterface.getIsbfdEnabled() != null)
				channelizedSDHInterface.setBfdConfig(createCsdhHdlcConfig(channelizedSdhInterface));
			AccessControlList inBoundAccessControlList = new AccessControlList();
			AccessControlList outBoundAccessControlList = new AccessControlList();
			AccessControlList inBoundAccessControlListV6 = new AccessControlList();
			AccessControlList outBoundAccessControlListV6 = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = channelizedSdhInterface.getAclPolicyCriterias();
			for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
				if (aclPolicyCriteria.getEndDate() == null) {
					if (aclPolicyCriteria.getInboundIpv4AclName() != null) {// TODO
						inBoundAccessControlList.setName(aclPolicyCriteria.getInboundIpv4AclName());
						channelizedSDHInterface.setInboundAccessControlList(inBoundAccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv4AclName() != null) {// TODO
						outBoundAccessControlList.setName(aclPolicyCriteria.getOutboundIpv4AclName());
						channelizedSDHInterface.setOutboundAccessControlList(outBoundAccessControlList);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundAccessControlListV6.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						channelizedSDHInterface.setOutboundAccessControlListV6(outBoundAccessControlListV6);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						inBoundAccessControlListV6.setName(aclPolicyCriteria.getInboundIpv6AclName());
						channelizedSDHInterface.setInboundAccessControlListV6(inBoundAccessControlListV6);
					}
				}
			}

		}
		return channelizedSDHInterface;
	}

	protected BFDConfig createCsdhHdlcConfig(ChannelizedSdhInterface channelizedSdhInterface) {
		BFDConfig bfdConfig = new BFDConfig();
		if (StringUtils.isNotBlank(channelizedSdhInterface.getBfdtransmitInterval()))
			bfdConfig.setTransmitInterval(ActivationUtils.toInteger(channelizedSdhInterface.getBfdtransmitInterval()));
		if (StringUtils.isNotBlank(channelizedSdhInterface.getBfdreceiveInterval()))
			bfdConfig.setRecieveInterval(ActivationUtils.toInteger(channelizedSdhInterface.getBfdreceiveInterval()));
		if (StringUtils.isNotBlank(channelizedSdhInterface.getBfdMultiplier()))
			bfdConfig.setMultiplier(ActivationUtils.toInteger(channelizedSdhInterface.getBfdMultiplier()));
		return bfdConfig;
	}

	protected WanRoutingProtocol createServiceWanRoutingProtocol(Bgp bgp, Ospf ospf, StaticProtocol staticProtocol) {
		WanRoutingProtocol wanRoutingProtocol = new WanRoutingProtocol();
		if (bgp != null)
			wanRoutingProtocol.setBgpRoutingProtocol(createWanRoutingBgpRoutingProtocol(bgp));
		if (ospf != null)
			wanRoutingProtocol.setOspfRoutingProtocol(createOspfRoutingProtocol(ospf));
		if (staticProtocol != null)
			wanRoutingProtocol.setStaticRoutingProtocol(createPeWanStaticRoutes(staticProtocol));

		if (wanRoutingProtocol.getBgpRoutingProtocol() == null && wanRoutingProtocol.getOspfRoutingProtocol() == null
				&& wanRoutingProtocol.getStaticRoutingProtocol() == null) {
			return null;
		}
		return wanRoutingProtocol;
	}

	/**
	 * createOspfRoutingProtocol
	 */
	protected OSPFRoutingProtocol createOspfRoutingProtocol(Ospf ospf) {
		OSPFRoutingProtocol ospfRoutingProtocol = new OSPFRoutingProtocol();
		if (ospf != null) {
			if (StringUtils.isNotBlank(ospf.getProcessId()))
				ospfRoutingProtocol.setProcessId(ospf.getProcessId());
			if (StringUtils.isNotBlank(ospf.getDomainId()))
				ospfRoutingProtocol.setDomainId(ospf.getDomainId());
			if (StringUtils.isNotBlank(ospf.getAreaId()))
				ospfRoutingProtocol.setAreaId(ospf.getAreaId());
			if (ospf.getIsnetworkP2p() != null)
				ospfRoutingProtocol.setIsNetworkTypeP2P(ActivationUtils.getStrValue(ospf.getIsnetworkP2p()));
			if (ospf.getOspfNetworkType() != null)
				ospfRoutingProtocol.setNetworkType(ospf.getOspfNetworkType());
			if (ospf.getIsignoreipOspfMtu() != null)
				ospfRoutingProtocol.setIsIPOSPFMTUIgnore(ActivationUtils.getStrValue(ospf.getIsignoreipOspfMtu()));
			if (ospf.getIsauthenticationRequired() != null)
				ospfRoutingProtocol
						.setIsAuthenticationRequired(ActivationUtils.getStrValue(ospf.getIsauthenticationRequired()));
			if (StringUtils.isNotBlank(ospf.getAuthenticationMode()))
				ospfRoutingProtocol.setAuthenticationMode(ospf.getAuthenticationMode().toUpperCase());
			if (StringUtils.isNotBlank(ospf.getPassword()))
				ospfRoutingProtocol.setAuthenticationPassword(ospf.getPassword());
			if (ospf.getIsredistributeConnectedEnabled() != null)
				ospfRoutingProtocol.setIsRedistributeConnectedEnabled(
						ActivationUtils.getStrValue(ospf.getIsredistributeConnectedEnabled()));
			if (ospf.getIsredistributeStaticEnabled() != null)
				ospfRoutingProtocol.setIsRedistributeStaticEnabled(
						ActivationUtils.getStrValue(ospf.getIsredistributeStaticEnabled()));
			if (StringUtils.isNotBlank(ospf.getCost()))
				ospfRoutingProtocol.setOspfCost(ActivationUtils.toInteger(ospf.getCost()));
			if (StringUtils.isNotBlank(ospf.getRedistributeRoutermapname()))
				ospfRoutingProtocol.setRedistributionRoutemapName(ospf.getRedistributeRoutermapname());
			if (StringUtils.isNotBlank(ospf.getVersion()))
				ospfRoutingProtocol.setVersion(ospf.getVersion().toUpperCase());
			if (StringUtils.isNotBlank(ospf.getLocalPreference()))
				ospfRoutingProtocol.setLocalPreference(ospf.getLocalPreference());
			if (StringUtils.isNotBlank(ospf.getLocalPreferenceV6()))
				ospfRoutingProtocol.setLocalPreferenceV6(ospf.getLocalPreferenceV6());
			RoutingPolicy routingPol = new RoutingPolicy();
			if (StringUtils.isNotBlank(ospf.getRedistributeRoutermapname())) {
				routingPol.setName(ospf.getRedistributeRoutermapname());
				ospfRoutingProtocol.setOSPFNeighborOutboundRoutingPolicy(routingPol);
			}
			RoutingPolicy routingPolV6 = new RoutingPolicy();
			if (StringUtils.isNotBlank(ospf.getRedistributeRoutermapname())) {
				routingPolV6.setName(ospf.getRedistributeRoutermapname());
				ospfRoutingProtocol.setOSPFNeighborOutboundRoutingPolicyV6(routingPolV6);
			}
		}
		return ospfRoutingProtocol;
	}

	protected BGPRoutingProtocol createWanRoutingBgpRoutingProtocol(Bgp bgp) {
		BGPRoutingProtocol bgpRoutingProtocol = new BGPRoutingProtocol();
		ASNumber remoteAsNumber = new ASNumber();
		if (bgp.getRemoteAsNumber() != null) {
			remoteAsNumber.setNumber(bgp.getRemoteAsNumber() + "");
			bgpRoutingProtocol.setRemoteASNumber(remoteAsNumber);
		}
		if (bgp.getRemoteUpdateSourceInterface() != null || bgp.getRemoteUpdateSourceIpv4Address() != null
				|| bgp.getRemoteUpdateSourceIpv6Address() != null) {
			LoopbackInterface loopbackInterface = new LoopbackInterface();// TODO
			if (StringUtils.isNotBlank(bgp.getRemoteUpdateSourceInterface()))
				loopbackInterface.setName(bgp.getRemoteUpdateSourceInterface());
			if (bgp.getRemoteUpdateSourceIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(bgp.getRemoteUpdateSourceIpv4Address());
				loopbackInterface.setV4IpAddress(ipv4Address);
			}
			if (bgp.getRemoteUpdateSourceIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(bgp.getRemoteUpdateSourceIpv6Address());
				loopbackInterface.setV6IpAddress(ipv6Address);
			}
			bgpRoutingProtocol.setRemoteBGPNeighbourUpdateSource(loopbackInterface);// NEIBHOR ON -> WAN/LOOPBACK
		}
		ASNumber bgpNeighbourLocalAsNumber = new ASNumber();
		if (StringUtils.isNotBlank(bgp.getLocalAsNumber())) {
			bgpNeighbourLocalAsNumber.setNumber(bgp.getLocalAsNumber());
			bgpRoutingProtocol.setBgpNeighbourLocalASNumber(bgpNeighbourLocalAsNumber);
		}
		if (bgp.getLocalUpdateSourceInterface() != null || bgp.getLocalUpdateSourceIpv4Address() != null
				|| bgp.getLocalUpdateSourceIpv6Address() != null) {
			LoopbackInterface localBgpNeibourLoopbackInterface = new LoopbackInterface();
			localBgpNeibourLoopbackInterface.setName(bgp.getLocalUpdateSourceInterface());
			if (bgp.getLocalUpdateSourceIpv4Address() != null) {
				IPV4Address lbnipv4Address = new IPV4Address();
				lbnipv4Address.setAddress(bgp.getLocalUpdateSourceIpv4Address());
				localBgpNeibourLoopbackInterface.setV4IpAddress(lbnipv4Address);
			}
			if (bgp.getLocalUpdateSourceIpv6Address() != null) {
				IPV6Address lbnipv6Address = new IPV6Address();
				lbnipv6Address.setAddress(bgp.getLocalUpdateSourceIpv6Address());
				localBgpNeibourLoopbackInterface.setV6IpAddress(lbnipv6Address);
			}
			bgpRoutingProtocol.setLocalBGPNeighbourUpdateSource(localBgpNeibourLoopbackInterface);
		}
		if (bgp.getAsoOverride() != null)
			bgpRoutingProtocol.setIsASOverriden(ActivationUtils.getStrValue(bgp.getAsoOverride()));
		if (bgp.getIsebgpMultihopReqd() != null)
			bgpRoutingProtocol.setIsEBGPMultihopRequired(ActivationUtils.getStrValue(bgp.getIsebgpMultihopReqd()));
		bgpRoutingProtocol.setMaxPrefix(ActivationUtils.toInteger(bgp.getMaxPrefix()));
		if (bgp.getSplitHorizon() != null)
			bgpRoutingProtocol.setSplitHorizon(ActivationUtils.getBooleanValue(bgp.getSplitHorizon()));
		bgpRoutingProtocol.setMaxPrefixThreshold(ActivationUtils.toInteger(bgp.getMaxPrefixThreshold()));
		bgpRoutingProtocol.setIsRedistributeConnectedEnabled(
				ActivationUtils.getBooleanValue(bgp.getRedistributeConnectedEnabled()));
		bgpRoutingProtocol
				.setIsRedistributeStaticEnabled(ActivationUtils.getBooleanValue(bgp.getRedistributeStaticEnabled()));
		if (bgp.getSooRequired() != null)
			bgpRoutingProtocol.setIsSOORequired(ActivationUtils.getBooleanValue(bgp.getSooRequired()));
		if (bgp.getHelloTime() != null)
			bgpRoutingProtocol.setHelloTime(ActivationUtils.toInteger(bgp.getHelloTime()));
		if (bgp.getHoldTime() != null)
			bgpRoutingProtocol.setHoldTime(ActivationUtils.toInteger(bgp.getHoldTime()));

		if (bgp.getPassword() != null && !bgp.getPassword().isEmpty()) {
			bgpRoutingProtocol.setAuthenticationPassword(bgp.getPassword());
			bgpRoutingProtocol.setIsAuthenticationRequired(true);
			bgpRoutingProtocol.setAuthenticationMode("MD5");
		}
		if (bgp.getRoutesExchanged() != null) {
			RoutesExchanged routesExchanged = new RoutesExchanged();

			routesExchanged.setRoute(bgp.getRoutesExchanged().toUpperCase());
			bgpRoutingProtocol.setRoutesExchanged(routesExchanged);
		}
		if (bgp.getRemoteIpv4Address() != null) {
			IPV4Address remoteIpv4Address = new IPV4Address();
			remoteIpv4Address.setAddress(bgp.getRemoteIpv4Address());
			bgpRoutingProtocol.setRemoteIPV4Address(remoteIpv4Address);
		}
		if (bgp.getRemoteIpv6Address() != null) {
			IPV6Address remoteIpv6Address = new IPV6Address();
			remoteIpv6Address.setAddress(bgp.getRemoteIpv6Address());
			bgpRoutingProtocol.setRemoteIPV6Address(remoteIpv6Address);
		}
		if (bgp.getLocalPreference() != null  && !bgp.getLocalPreference().isEmpty())
			bgpRoutingProtocol.setLocalPreference(bgp.getLocalPreference());
		if (bgp.getIsmultihopTtl() != null)
			bgpRoutingProtocol.setMultihopTTL(ActivationUtils.toInteger(bgp.getIsmultihopTtl()));
		if (bgp.getNeighbourshutdownRequired() != null)
			bgpRoutingProtocol.setIsNeighbourShutdownRequired(
					ActivationUtils.getBooleanValue(bgp.getNeighbourshutdownRequired()));
		return bgpRoutingProtocol;
	}

	protected void createArrayOfInboundPrefixes() {

	}

	protected StaticRoutes createPeWanAdditionalStaticRoutesBgp(Set<WanStaticRoute> wanstaticRoutes) {
		StaticRoutes staticRoutes = null;

		for (WanStaticRoute wanStaticRoute : wanstaticRoutes) {
			if (wanStaticRoute.getIsPewan() != null && ActivationUtils.getBooleanValue(wanStaticRoute.getIsPewan())) {
				staticRoutes = new StaticRoutes();
				IPSubnetADValueMap ipSubNetAdValue = new IPSubnetADValueMap();
				IpSubnet ipSubNet = new IpSubnet();
				if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(".")) {
					IPV4Address ipv4Address = new IPV4Address();
					ipv4Address.setAddress(wanStaticRoute.getIpsubnet());// TODO- Regular Expression
					ipSubNet.setV4Address(ipv4Address);
					ipSubNetAdValue.setIpSubnet(ipSubNet);
				}
				if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(":")) {
					IPV6Address ipv6Address = new IPV6Address();
					ipv6Address.setAddress(wanStaticRoute.getIpsubnet());// TODO
					ipSubNet.setV6Address(ipv6Address);
					ipSubNetAdValue.setIpSubnet(ipSubNet);
				}
				if (wanStaticRoute.getAdvalue() != null)
					ipSubNetAdValue.setAdValue(wanStaticRoute.getAdvalue());
				if (wanStaticRoute.getNextHopid() != null)
					ipSubNetAdValue.setNextHopIp(wanStaticRoute.getNextHopid());
				if (wanStaticRoute.getDescription() != null)
					ipSubNetAdValue.setDescription(wanStaticRoute.getDescription());
				if (wanStaticRoute.getGlobal() != null) {
					ipSubNetAdValue.setIsGlobal(wanStaticRoute.getGlobal() == 1 ? true : false);
				} // SET NULL if null in db
				if (wanStaticRoute.getPopCommunity() != null)
					staticRoutes.setPopCommunity(wanStaticRoute.getPopCommunity());
				if (wanStaticRoute.getRegionalCommunity() != null)
					staticRoutes.setRegionalCommunity(wanStaticRoute.getRegionalCommunity());
				if (wanStaticRoute.getServiceCommunity() != null)
					staticRoutes.setServiceCommunity(wanStaticRoute.getServiceCommunity());
				staticRoutes.getStaticRouteList().add(ipSubNetAdValue);
			}
		}
		return staticRoutes;
	}

	protected StaticRoutingProtocol createPeWanStaticRoutes(StaticProtocol staticProtocol) {
		StaticRoutingProtocol staticRoutingProtocol = null;
		if (staticProtocol != null) {
			staticRoutingProtocol = new StaticRoutingProtocol();
			for (WanStaticRoute wanStaticRoute : staticProtocol.getWanStaticRoutes()) {
				if (wanStaticRoute.getIsPewan() == 1) {
					staticRoutingProtocol.setPEWANStaticRoutes(createWantStaticRoutes(staticRoutingProtocol,
							wanStaticRoute, staticRoutingProtocol.getPEWANStaticRoutes()));
				}
				if (wanStaticRoute.getIsCewan() == 1) {
					staticRoutingProtocol
							.setCEWANStaticRoutes(createWantStaticRoutes(staticRoutingProtocol, wanStaticRoute,staticRoutingProtocol.getCEWANStaticRoutes()));
				}
			}
		}
		if (staticRoutingProtocol != null && staticRoutingProtocol.getPEWANStaticRoutes() == null
				&& staticRoutingProtocol.getCEWANStaticRoutes() == null) {
			return null;
		}
		return staticRoutingProtocol;

	}

	/**
	 * createWantStaticRoutes
	 * 
	 * @param staticRoutingProtocol
	 * @param wanStaticRoute
	 */
	private StaticRoutes createWantStaticRoutes(StaticRoutingProtocol staticRoutingProtocol,
			WanStaticRoute wanStaticRoute, StaticRoutes peStaticRoutes) {
		if (peStaticRoutes == null)
			peStaticRoutes = new StaticRoutes();
		IPSubnetADValueMap ipSubNetAdValue = new IPSubnetADValueMap();
		IpSubnet ipSubNet = new IpSubnet();
		if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(".")) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(wanStaticRoute.getIpsubnet());// Regex should be applies
			ipSubNet.setV4Address(ipv4Address);
		}
		if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(":")) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(wanStaticRoute.getIpsubnet());
			ipSubNet.setV6Address(ipv6Address);// Regex should be applies
		}
		ipSubNetAdValue.setIpSubnet(ipSubNet);
		ipSubNetAdValue.setAdValue(wanStaticRoute.getAdvalue());
		ipSubNetAdValue.setNextHopIp(wanStaticRoute.getNextHopid());
		ipSubNetAdValue.setDescription(wanStaticRoute.getDescription());
		ipSubNetAdValue.setIsGlobal(wanStaticRoute.getGlobal()!=null &&  wanStaticRoute.getGlobal()== 1 ? true : false);
		peStaticRoutes.getStaticRouteList().add(ipSubNetAdValue);
		if (wanStaticRoute.getPopCommunity() != null)
			peStaticRoutes.setPopCommunity(wanStaticRoute.getPopCommunity());
		if (wanStaticRoute.getRegionalCommunity() != null)
			peStaticRoutes.setRegionalCommunity(wanStaticRoute.getRegionalCommunity());
		if (wanStaticRoute.getServiceCommunity() != null)
			peStaticRoutes.setServiceCommunity(wanStaticRoute.getServiceCommunity());
		return peStaticRoutes;
	}

	protected TopologyInfo createTopologyInformation(Topology topology) {
		TopologyInfo topologyInfo = new TopologyInfo();
		topologyInfo.setName(topology.getTopologyName());
		Switch switches = createUniSwitchDetails(topology);
		if (switches != null)
			topologyInfo.setUNISwitch(switches);
		else
			return null;
		return topologyInfo;

	}

	protected EthernetInterface createRouterUplinkPorts1(RouterUplinkport routerUplinkPort) {
		EthernetInterface ethernetInterface = new EthernetInterface();
		ethernetInterface.setPhysicalPortName(routerUplinkPort.getPhysicalPort1Name());
		ethernetInterface.setName(routerUplinkPort.getPhysicalPort1Name());
		return ethernetInterface;
	}

	protected EthernetInterface createRouterUplinkPorts2(RouterUplinkport routerUplinkPort) {
		EthernetInterface ethernetInterface = new EthernetInterface();
		ethernetInterface.setPhysicalPortName(routerUplinkPort.getPhysicalPort2Name());
		ethernetInterface.setName(routerUplinkPort.getPhysicalPort2Name());
		return ethernetInterface;
	}

	protected Switch createUniSwitchDetails(Topology topology) {
		Switch swith = null;
		Set<UniswitchDetail> uniSwitches = topology.getUniswitchDetails();
		for (UniswitchDetail uniswitchDetail : uniSwitches) {
			if (uniswitchDetail.getHostName() != null) {

				swith = new Switch();
				swith.setHostName(uniswitchDetail.getHostName());
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(uniswitchDetail.getMgmtIp());
				swith.setV4ManagementIPAddress(ipv4Address);
				Switch.Interface sInterface = new Switch.Interface();
				EthernetInterface etherInterface = new EthernetInterface();
				etherInterface.setPhysicalPortName(uniswitchDetail.getPhysicalPort());
				etherInterface.setSvlan(ActivationUtils.toInteger(uniswitchDetail.getOuterVlan()));
				etherInterface.setVlan(ActivationUtils.toInteger(uniswitchDetail.getInnerVlan()));
				if(uniswitchDetail.getPortType()!=null && !uniswitchDetail.getPortType().toUpperCase().contains("LAG")) {
				etherInterface.setPortType(uniswitchDetail.getPortType().toUpperCase());
				}
				if(uniswitchDetail.getMode()!=null) {
				etherInterface.setMode(uniswitchDetail.getMode().toUpperCase());
				}
				if (uniswitchDetail.getMaxMacLimit() != null)
					etherInterface.setMaxMacLimit(uniswitchDetail.getMaxMacLimit());
				etherInterface.setMediaType(uniswitchDetail.getMediaType());
				if (uniswitchDetail.getInterfaceName() != null)
					etherInterface.setName(uniswitchDetail.getInterfaceName());
				if (uniswitchDetail.getHandoff() != null)
					etherInterface.setHandOff(uniswitchDetail.getHandoff());
				etherInterface.setSpeed(uniswitchDetail.getSpeed());
				etherInterface.setIsAutoNegotiation(uniswitchDetail.getAutonegotiationEnabled());
				etherInterface.setDuplex(ActivationUtils.getDuplex(uniswitchDetail.getDuplex()));
				if(uniswitchDetail.getSyncVlanReqd()!=null){
					etherInterface.setSyncVLAN(uniswitchDetail.getSyncVlanReqd()==1?true:false);
				}
				
				sInterface.setEthernetInterface(etherInterface);
				if (!(uniswitchDetail.getHandoff() != null && uniswitchDetail.getHandoff().equals("NNI"))) {
					swith.setInterface(sInterface);
				} else {
					swith = null;
					break;
				}

			}
		}
		return swith;
	}

	protected QoS createDummyServiceQos(ServiceDetail serviceDetail) {
		QoS qoS = new QoS();
		Set<ServiceQo> quoses = serviceDetail.getServiceQos();
		for (AluSchedulerPolicy aluSchedulerPolicy : serviceDetail.getAluSchedulerPolicies()) {
			ALUSchedulerPolicy aluSch = new ALUSchedulerPolicy();
			aluSch.setName(aluSchedulerPolicy.getSchedulerPolicyName());
			aluSch.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSchedulerPolicyIspreprovisioned()));
			aluSch.setTotalCIRbandwidth(aluSchedulerPolicy.getTotalCirBw());
			aluSch.setTotalPIRbandwidth(aluSchedulerPolicy.getTotalPirBw());
			qoS.setALUSchedulerPolicy(aluSch);
			ALUSchedulerPolicy aLUEgressPolicy = new ALUSchedulerPolicy();
			aLUEgressPolicy.setName(aluSchedulerPolicy.getSapEgressPolicyname());
			aLUEgressPolicy.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSapEgressPreprovisioned()));
			qoS.setALUEgressPolicy(aLUEgressPolicy);
			ALUSchedulerPolicy aLUingressPolicy = new ALUSchedulerPolicy();
			aLUingressPolicy.setName(aluSchedulerPolicy.getSapIngressPolicyname());
			aLUingressPolicy.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSapIngressPreprovisioned()));
			qoS.setALUIngressPolicy(aLUingressPolicy);
		}
		for (ServiceQo serviceQo : quoses) {
			qoS.setCosType(serviceQo.getCosType().toUpperCase());
			if (StringUtils.isNotBlank(serviceQo.getCosProfile()))
				qoS.setQoSProfile(serviceQo.getCosProfile().toUpperCase());
			if (StringUtils.isNotBlank(serviceQo.getQosTrafiicMode()))
				qoS.setQoSTrafficMode(serviceQo.getQosTrafiicMode().toUpperCase());
			if (serviceQo.getIsbandwidthApplicable() != null)
				qoS.setIsBandwidthApplicable(ActivationUtils.getStrValue(serviceQo.getIsbandwidthApplicable()));
			// Change the approach
			List<ServiceCosCriteria> serviceCosCriteriasv1 = serviceCosCriteriaRepository
					.findByServiceQo_ServiceQosIdAndEndDateIsNullOrderByCosNameAsc(serviceQo.getServiceQosId());// This
																												// will
																												// give
																												// the
																												// order
																												// of
																												// cos
			Map<String, CoS> cosMapper = new HashMap<>();
			for (ServiceCosCriteria serviceCosCriteria : serviceCosCriteriasv1) {// TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				CoS cos = null;
				CoS.ClassificationCriteria classCri = null;
				if (cosMapper.get(serviceCosCriteria.getCosName()) == null) {
					cos = new CoS();
					classCri = new CoS.ClassificationCriteria();// TODO
					cos.setClassificationCriteria(classCri);
					cosMapper.put(serviceCosCriteria.getCosName(), cos);
					classCri = cos.getClassificationCriteria();
				} else {
					cos = cosMapper.get(serviceCosCriteria.getCosName());
				}
				Bandwidth bw = new Bandwidth();
				bw.setSpeed(Math.abs(ActivationUtils.toFloat(serviceCosCriteria.getBwBpsunit())));
				bw.setUnit(BandwidthUnit.BPS);// Same Unit to Pir
				if (StringUtils.isNotBlank(serviceQo.getPirBw())) {
					if (serviceCosCriteria.getCosName().equals("COS4")) {
						Bandwidth pirBw = new Bandwidth();
						pirBw.setSpeed((ActivationUtils.toFloat(serviceCosCriteria.getCosPercent()) / 100F)
								* ActivationUtils.toFloat(serviceQo.getPirBw()));
						pirBw.setUnit(ActivationUtils.getBandwithUnit(serviceQo.getPirBwUnit()));
						cos.setPIRBandwidth(pirBw);
					} 
				}
				cos.setBandwidth(bw);
				cos.setCosUpdateAction(serviceQo.getCosUpdateAction().toUpperCase());
				if (serviceQo.getPirBw() != null) {
					cos.setBandwidthinKBPS(String.valueOf(Math.round((ActivationUtils.toFloat(serviceCosCriteria.getBwBpsunit())/1000))));// Already rules engine in converting to kbps
				}
				// cos.setIsDefaultFC(serviceQo.getIsdefaultFc() == 1 ? true : false);

				if (serviceCosCriteria.getClassificationCriteria().equals("ipprecedence")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					classCri.setIpprecedent(new IPPrecedent());
					classCri.getIpprecedent().setCriteriaValue1(serviceCosCriteria.getIpprecedenceVal1());
					classCri.getIpprecedent().setCriteriaValue2(serviceCosCriteria.getIpprecedenceVal2());
					classCri.getIpprecedent().setCriteriaValue3(serviceCosCriteria.getIpprecedenceVal3());
					classCri.getIpprecedent().setCriteriaValue4(serviceCosCriteria.getIpprecedenceVal4());
					classCri.getIpprecedent().setCriteriaValue5(serviceCosCriteria.getIpprecedenceVal5());
					classCri.getIpprecedent().setCriteriaValue6(serviceCosCriteria.getIpprecedenceVal6());
					classCri.getIpprecedent().setCriteriaValue7(serviceCosCriteria.getIpprecedenceVal7());
				}
				if (serviceCosCriteria.getClassificationCriteria().equals("DSCP")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					classCri.setDscpValue(new DscpValue());
					classCri.getDscpValue().setCriteriaValue1(serviceCosCriteria.getDhcpVal1());
					classCri.getDscpValue().setCriteriaValue2(serviceCosCriteria.getDhcpVal2());
					classCri.getDscpValue().setCriteriaValue3(serviceCosCriteria.getDhcpVal3());
					classCri.getDscpValue().setCriteriaValue4(serviceCosCriteria.getDhcpVal4());
					classCri.getDscpValue().setCriteriaValue5(serviceCosCriteria.getDhcpVal5());
					classCri.getDscpValue().setCriteriaValue6(serviceCosCriteria.getDhcpVal6());
					classCri.getDscpValue().setCriteriaValue7(serviceCosCriteria.getDhcpVal7());
				}

				if (serviceCosCriteria.getClassificationCriteria().toUpperCase().equals("ANY")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					if (classCri != null)
						classCri.setAny("ANY");
				}

				if (serviceCosCriteria.getClassificationCriteria().equals("IP Address")) {
					Set<AclPolicyCriteria> aclPolicies = serviceCosCriteria.getAclPolicyCriterias();
					classCri.setAccessControlList(new AccessControlList());
					for (AclPolicyCriteria aclPolicy : aclPolicies) {
						if (aclPolicy.getEndDate() == null) {
							classCri.getAccessControlList().setName(aclPolicy.getIssvcQosCoscriteriaIpaddrAclName());
							AccessControListlEntry accessControListlEntry = new AccessControListlEntry();
							accessControListlEntry.setSeqNo(ActivationUtils.toInteger(aclPolicy.getSequence()));
							accessControListlEntry.setAction(aclPolicy.getAction());
							AccessControListlEntry.SourceSubnet souceSubNetValue = new AccessControListlEntry.SourceSubnet();
							String sval = ActivationUtils.getStrValue(aclPolicy.getSourceAny());
							if (sval != null) {
								souceSubNetValue.setAny(sval.equals("TRUE") ? "ANY" : null);
							}
							if (aclPolicy.getSourceSubnet() != null) {
								if (aclPolicy.getSourceSubnet().contains(".")) {
									IPV4Address souipv4Address = new IPV4Address();
									souipv4Address.setAddress(aclPolicy.getSourceSubnet());
									souceSubNetValue.setV4Address(souipv4Address);
								} else if (aclPolicy.getSourceSubnet().contains(":")) {
									IPV6Address souipv6Address = new IPV6Address();
									souipv6Address.setAddress(aclPolicy.getSourceSubnet());
									souceSubNetValue.setV6Address(souipv6Address);
								}
							}
							accessControListlEntry.setSourceSubnet(souceSubNetValue);
							accessControListlEntry.setSourceCondition(aclPolicy.getSourceCondition());
							accessControListlEntry.setSourcePortNumber(aclPolicy.getSourcePortnumber());
							AccessControListlEntry.DestinationSubnet destinationSubNetValue = new AccessControListlEntry.DestinationSubnet();
							String dval = ActivationUtils.getStrValue(aclPolicy.getDestinationAny());
							if (dval != null) {
								destinationSubNetValue.setAny(dval.equals("TRUE") ? "ANY" : null);
							}

							if (aclPolicy.getDestinationSubnet() != null) {
								if (aclPolicy.getDestinationSubnet().contains(".")) {
									IPV4Address deipv4Address = new IPV4Address();
									deipv4Address.setAddress(aclPolicy.getDestinationSubnet());
									destinationSubNetValue.setV4Address(deipv4Address);
								} else if (aclPolicy.getDestinationSubnet().contains(":")) {
									IPV6Address deipv6Address = new IPV6Address();
									deipv6Address.setAddress(aclPolicy.getDestinationSubnet());
									destinationSubNetValue.setV6Address(deipv6Address);
								}
							}
							if (aclPolicy.getSourceCondition() != null)
								accessControListlEntry.setSourceCondition(aclPolicy.getSourceCondition().toUpperCase());
							IPV4Address deipv4Address = new IPV4Address();
							deipv4Address.setAddress(aclPolicy.getDestinationSubnet());
							destinationSubNetValue.setV4Address(deipv4Address);
							accessControListlEntry.setDestinationSubnet(destinationSubNetValue);
							if (aclPolicy.getProtocol() != null)
								accessControListlEntry.setProtocol(aclPolicy.getProtocol().toUpperCase());
							accessControListlEntry.setDescription(aclPolicy.getDescription());
							accessControListlEntry.setPortNumber(aclPolicy.getDestinationPortnumber());
							if (aclPolicy.getDestinationCondition() != null)
								accessControListlEntry.setCondition(aclPolicy.getDestinationCondition().toUpperCase());
							classCri.getAccessControlList().getAccessControlListEntry().add(accessControListlEntry);
						}
						cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					}
				}
				qoS.getCos().add(cos);
			}

			if (!qoS.getCos().isEmpty()) {
				Integer lastRecord = qoS.getCos().size() - 1;
				CoS coS=qoS.getCos().get(lastRecord);
				coS.setIsDefaultFC(serviceQo.getIsdefaultFc() == 1 ? true : false);
				/*if (serviceQo.getPirBw() != null) {
					coS.setBandwidthinKBPS(String.valueOf(Math.round(ActivationUtils.toFloat(serviceQo.getPirBw()))));// Already rules engine in converting to kbps
				}*/
			}
		}

		return qoS;
	}
	
	protected QoS createServiceQos(ServiceDetail serviceDetail) {
		QoS qoS = new QoS();
		Set<ServiceQo> quoses = serviceDetail.getServiceQos();
		for (AluSchedulerPolicy aluSchedulerPolicy : serviceDetail.getAluSchedulerPolicies()) {
			ALUSchedulerPolicy aluSch = new ALUSchedulerPolicy();
			aluSch.setName(aluSchedulerPolicy.getSchedulerPolicyName());
			aluSch.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSchedulerPolicyIspreprovisioned()));
			aluSch.setTotalCIRbandwidth(aluSchedulerPolicy.getTotalCirBw());
			aluSch.setTotalPIRbandwidth(aluSchedulerPolicy.getTotalPirBw());
			qoS.setALUSchedulerPolicy(aluSch);
			ALUSchedulerPolicy aLUEgressPolicy = new ALUSchedulerPolicy();
			aLUEgressPolicy.setName(aluSchedulerPolicy.getSapEgressPolicyname());
			aLUEgressPolicy.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSapEgressPreprovisioned()));
			qoS.setALUEgressPolicy(aLUEgressPolicy);
			ALUSchedulerPolicy aLUingressPolicy = new ALUSchedulerPolicy();
			aLUingressPolicy.setName(aluSchedulerPolicy.getSapIngressPolicyname());
			aLUingressPolicy.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSapIngressPreprovisioned()));
			qoS.setALUIngressPolicy(aLUingressPolicy);
		}
		for (ServiceQo serviceQo : quoses) {
			qoS.setCosType(serviceQo.getCosType().toUpperCase());
			if (StringUtils.isNotBlank(serviceQo.getCosProfile()))
				qoS.setQoSProfile(serviceQo.getCosProfile().toUpperCase());
			if (StringUtils.isNotBlank(serviceQo.getQosTrafiicMode()))
				qoS.setQoSTrafficMode(serviceQo.getQosTrafiicMode().toUpperCase());
			if (serviceQo.getIsbandwidthApplicable() != null)
				qoS.setIsBandwidthApplicable(ActivationUtils.getStrValue(serviceQo.getIsbandwidthApplicable()));
			// Change the approach
			List<ServiceCosCriteria> serviceCosCriteriasv1 = serviceCosCriteriaRepository
					.findByServiceQo_ServiceQosIdAndEndDateIsNullOrderByCosNameAsc(serviceQo.getServiceQosId());// This
																												// will
																												// give
																												// the
																												// order
																												// of
																												// cos
			Map<String, CoS> cosMapper = new HashMap<>();
			for (ServiceCosCriteria serviceCosCriteria : serviceCosCriteriasv1) {// TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				CoS cos = null;
				CoS.ClassificationCriteria classCri = null;
				if (cosMapper.get(serviceCosCriteria.getCosName()) == null) {
					cos = new CoS();
					classCri = new CoS.ClassificationCriteria();// TODO
					cos.setClassificationCriteria(classCri);
					cosMapper.put(serviceCosCriteria.getCosName(), cos);
					classCri = cos.getClassificationCriteria();
				} else {
					cos = cosMapper.get(serviceCosCriteria.getCosName());
				}
				Bandwidth bw = new Bandwidth();
				bw.setSpeed(Math.abs(ActivationUtils.toFloat(serviceCosCriteria.getBwBpsunit())));
				bw.setUnit(BandwidthUnit.BPS);// Same Unit to Pir
				if (StringUtils.isNotBlank(serviceQo.getPirBw())) {
					if (serviceCosCriteria.getCosName().equals("COS1")) {
						Bandwidth pirBw = new Bandwidth();
						pirBw.setSpeed((ActivationUtils.toFloat(serviceCosCriteria.getCosPercent()) / 100F)
								* ActivationUtils.toFloat(serviceQo.getPirBw()));
						pirBw.setUnit(ActivationUtils.getBandwithUnit(serviceQo.getPirBwUnit()));
						cos.setPIRBandwidth(pirBw);
					} else {
						Bandwidth pirBw = new Bandwidth();
						pirBw.setSpeed(Math.abs(ActivationUtils.toFloat(serviceQo.getPirBw())));
						pirBw.setUnit(ActivationUtils.getBandwithUnit(serviceQo.getPirBwUnit()));
						cos.setPIRBandwidth(pirBw);
					}
				}
				cos.setBandwidth(bw);
				cos.setCosUpdateAction(serviceQo.getCosUpdateAction().toUpperCase());
				if (serviceQo.getPirBw() != null) {
					cos.setBandwidthinKBPS(String.valueOf(Math.round((ActivationUtils.toFloat(serviceCosCriteria.getBwBpsunit())/1000))));// Already rules engine in converting to kbps
				}
				// cos.setIsDefaultFC(serviceQo.getIsdefaultFc() == 1 ? true : false);

				if (serviceCosCriteria.getClassificationCriteria().equals("ipprecedence")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					classCri.setIpprecedent(new IPPrecedent());
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal1()) && !serviceCosCriteria.getIpprecedenceVal1().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue1(serviceCosCriteria.getIpprecedenceVal1());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal2()) && !serviceCosCriteria.getIpprecedenceVal2().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue2(serviceCosCriteria.getIpprecedenceVal2());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal3()) && !serviceCosCriteria.getIpprecedenceVal3().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue3(serviceCosCriteria.getIpprecedenceVal3());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal4()) && !serviceCosCriteria.getIpprecedenceVal4().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue4(serviceCosCriteria.getIpprecedenceVal4());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal5()) && !serviceCosCriteria.getIpprecedenceVal5().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue5(serviceCosCriteria.getIpprecedenceVal5());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal6()) && !serviceCosCriteria.getIpprecedenceVal6().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue6(serviceCosCriteria.getIpprecedenceVal6());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal7()) && !serviceCosCriteria.getIpprecedenceVal7().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue7(serviceCosCriteria.getIpprecedenceVal7());
					}
				}
				if (serviceCosCriteria.getClassificationCriteria().equals("DSCP")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					classCri.setDscpValue(new DscpValue());
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal1()) && !serviceCosCriteria.getDhcpVal1().isEmpty()){
						classCri.getDscpValue().setCriteriaValue1(serviceCosCriteria.getDhcpVal1().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal2()) && !serviceCosCriteria.getDhcpVal2().isEmpty()){
						classCri.getDscpValue().setCriteriaValue2(serviceCosCriteria.getDhcpVal2().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal3()) && !serviceCosCriteria.getDhcpVal3().isEmpty()){
						classCri.getDscpValue().setCriteriaValue3(serviceCosCriteria.getDhcpVal3().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal4()) && !serviceCosCriteria.getDhcpVal4().isEmpty()){
						classCri.getDscpValue().setCriteriaValue4(serviceCosCriteria.getDhcpVal4().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal5()) && !serviceCosCriteria.getDhcpVal5().isEmpty()){
						classCri.getDscpValue().setCriteriaValue5(serviceCosCriteria.getDhcpVal5().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal6()) && !serviceCosCriteria.getDhcpVal6().isEmpty()){
						classCri.getDscpValue().setCriteriaValue6(serviceCosCriteria.getDhcpVal6().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal7()) && !serviceCosCriteria.getDhcpVal7().isEmpty()){
						classCri.getDscpValue().setCriteriaValue7(serviceCosCriteria.getDhcpVal7().toLowerCase());
					}
				}

				if (serviceCosCriteria.getClassificationCriteria().toUpperCase().equals("ANY")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					if (classCri != null)
						classCri.setAny("ANY");
				}

				if (serviceCosCriteria.getClassificationCriteria().equals("IP Address")) {
					Set<AclPolicyCriteria> aclPolicies = serviceCosCriteria.getAclPolicyCriterias();
					classCri.setAccessControlList(new AccessControlList());
					for (AclPolicyCriteria aclPolicy : aclPolicies) {
						if (aclPolicy.getEndDate() == null) {
							classCri.getAccessControlList().setName(aclPolicy.getIssvcQosCoscriteriaIpaddrAclName());
							AccessControListlEntry accessControListlEntry = new AccessControListlEntry();
							accessControListlEntry.setSeqNo(ActivationUtils.toInteger(aclPolicy.getSequence()));
							accessControListlEntry.setAction(Objects.nonNull(aclPolicy.getAction())?aclPolicy.getAction().toUpperCase():aclPolicy.getAction());
							AccessControListlEntry.SourceSubnet souceSubNetValue = new AccessControListlEntry.SourceSubnet();
							String sval = ActivationUtils.getStrValue(aclPolicy.getSourceAny());
							if (sval != null) {
								souceSubNetValue.setAny(sval.equals("TRUE") ? "ANY" : null);
							}
							if (aclPolicy.getSourceSubnet() != null) {
								if (aclPolicy.getSourceSubnet().contains(".")) {
									IPV4Address souipv4Address = new IPV4Address();
									souipv4Address.setAddress(aclPolicy.getSourceSubnet());
									souceSubNetValue.setV4Address(souipv4Address);
								} else if (aclPolicy.getSourceSubnet().contains(":")) {
									IPV6Address souipv6Address = new IPV6Address();
									souipv6Address.setAddress(aclPolicy.getSourceSubnet());
									souceSubNetValue.setV6Address(souipv6Address);
								}
							}
							accessControListlEntry.setSourceSubnet(souceSubNetValue);
							accessControListlEntry.setSourceCondition(aclPolicy.getSourceCondition());
							accessControListlEntry.setSourcePortNumber(aclPolicy.getSourcePortnumber());
							AccessControListlEntry.DestinationSubnet destinationSubNetValue = new AccessControListlEntry.DestinationSubnet();
							String dval = ActivationUtils.getStrValue(aclPolicy.getDestinationAny());
							if (dval != null) {
								destinationSubNetValue.setAny(dval.equals("TRUE") ? "ANY" : null);
							}

							if (aclPolicy.getDestinationSubnet() != null) {
								if (aclPolicy.getDestinationSubnet().contains(".")) {
									IPV4Address deipv4Address = new IPV4Address();
									deipv4Address.setAddress(aclPolicy.getDestinationSubnet());
									destinationSubNetValue.setV4Address(deipv4Address);
								} else if (aclPolicy.getDestinationSubnet().contains(":")) {
									IPV6Address deipv6Address = new IPV6Address();
									deipv6Address.setAddress(aclPolicy.getDestinationSubnet());
									destinationSubNetValue.setV6Address(deipv6Address);
								}
							}
							accessControListlEntry.setDestinationSubnet(destinationSubNetValue);
							if (aclPolicy.getSourceCondition() != null)
								accessControListlEntry.setSourceCondition(aclPolicy.getSourceCondition().toUpperCase());
							/*IPV4Address deipv4Address = new IPV4Address();
							deipv4Address.setAddress(aclPolicy.getDestinationSubnet());
							destinationSubNetValue.setV4Address(deipv4Address);
							accessControListlEntry.setDestinationSubnet(destinationSubNetValue);*/
							if (aclPolicy.getProtocol() != null && !aclPolicy.getProtocol().isEmpty()) {
								accessControListlEntry.setProtocol(aclPolicy.getProtocol().toUpperCase());
							} else {
								accessControListlEntry.setProtocol("IP");
							}
							accessControListlEntry.setDescription(aclPolicy.getDescription());
							if (aclPolicy.getDestinationPortnumber() != null
									&& !aclPolicy.getDestinationPortnumber().isEmpty()) {
								accessControListlEntry.setPortNumber(aclPolicy.getDestinationPortnumber());
							}
							if (aclPolicy.getDestinationCondition() != null
									&& !aclPolicy.getDestinationCondition().isEmpty()) {
								accessControListlEntry.setCondition(aclPolicy.getDestinationCondition().toUpperCase());
							}
							classCri.getAccessControlList().getAccessControlListEntry().add(accessControListlEntry);
						}
						cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					}
				}
				qoS.getCos().add(cos);
			}

			if (!qoS.getCos().isEmpty()) {
				Integer lastRecord = qoS.getCos().size() - 1;
				CoS coS=qoS.getCos().get(lastRecord);
				coS.setIsDefaultFC(serviceQo.getIsdefaultFc() == 1 ? true : false);
				/*if (serviceQo.getPirBw() != null) {
					coS.setBandwidthinKBPS(String.valueOf(Math.round(ActivationUtils.toFloat(serviceQo.getPirBw()))));// Already rules engine in converting to kbps
				}*/
			}
		}

		return qoS;
	}

	protected void createAluSchedulerPolicy() {

	}

	protected void createSapIngressPolicy() {

	}

	protected void createSapEngressPolicy() {

	}

	protected CustomerPremiseEquipment createCpeDetails(InterfaceProtocolMapping interfaceProtocolMapping) {
		Cpe cpe = interfaceProtocolMapping.getCpe();
		CustomerPremiseEquipment customerPremiseEquipment = new CustomerPremiseEquipment();
		customerPremiseEquipment.setHostName(cpe.getHostName());

		if (isLoopBackTag(cpe)) {
			Interface loopbackInterface = new Interface();
			if (cpe.getLoopbackInterfaceName() != null) {
				loopbackInterface.setName(cpe.getLoopbackInterfaceName());
			}
			if (StringUtils.isNotBlank(cpe.getMgmtLoopbackV4address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(cpe.getMgmtLoopbackV4address());
				loopbackInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(cpe.getMgmtLoopbackV6address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(cpe.getMgmtLoopbackV6address());
				loopbackInterface.setV6IpAddress(ipv6Address);
			}
			customerPremiseEquipment.setLoopbackInterface(loopbackInterface);
		}

		customerPremiseEquipment.setIsCEACEConfigurable(ActivationUtils.getBooleanValue(cpe.getIsaceconfigurable()));
		customerPremiseEquipment.setSnmpServerCommunity(cpe.getSnmpServerCommunity());
		if (interfaceProtocolMapping.getIscpeWanInterface() == 1)
			customerPremiseEquipment.setWanInterface(createCpeWanInterface(cpe, interfaceProtocolMapping));
		else
			return null;
		return customerPremiseEquipment;
	}

	protected boolean isLoopBackTag(Cpe cpe) {

		if (cpe.getLoopbackInterfaceName() != null || cpe.getMgmtLoopbackV4address() != null
				|| cpe.getMgmtLoopbackV6address() != null) {
			return true;
		}
		return false;
	}

	protected void createCpeInitConfigParam() {

	}
	
	protected WANInterface createDummyCpeWanInterface(Cpe cpe, InterfaceProtocolMapping interfaceProtocolMapping, IpDummyDetail ipDummyDetail) {
		WANInterface wanInterface = new WANInterface();
		WANInterface.Interface wIntfce = new WANInterface.Interface();
		EthernetInterface etherInterface = new EthernetInterface();
		com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter = interfaceProtocolMapping
				.getEthernetInterface();
		if (etherInter != null) {
			if (StringUtils.isNotBlank(etherInter.getInterfaceName()))
				etherInterface.setName(etherInter.getInterfaceName());
			if (StringUtils.isNotBlank(etherInter.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				String ipPort=getCpeDummyIp(ipDummyDetail);
				ipv4Address.setAddress(Objects.nonNull(ipPort)?ipPort:etherInter.getModifiedIpv4Address());
				etherInterface.setV4IpAddress(ipv4Address);
			}
			/*if (StringUtils.isNotBlank(etherInter.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(etherInter.getModifiedIpv6Address());
				etherInterface.setV6IpAddress(ipv6Address);
			}*/
			if (StringUtils.isNotBlank(etherInter.getInnerVlan()))
				etherInterface.setVlan(ActivationUtils.toInteger(etherInter.getInnerVlan()));
			if (StringUtils.isNotBlank(etherInter.getPortType()))
				etherInterface.setPortType(etherInter.getPortType().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getMode()))
				etherInterface.setMode(etherInter.getMode().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getMediaType()))
				etherInterface.setMediaType(etherInter.getMediaType().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getSpeed()))
				etherInterface.setSpeed(etherInter.getSpeed());
			if (StringUtils.isNotBlank(etherInter.getDuplex()))
				etherInterface.setDuplex(ActivationUtils.getDuplex(etherInter.getDuplex()));
			if (StringUtils.isNotBlank(etherInter.getAutonegotiationEnabled()))
				etherInterface.setIsAutoNegotiation(etherInter.getAutonegotiationEnabled());
			wIntfce.setEthernetInterface(etherInterface);
		}

		ChannelizedE1serialInterface channelizedE1Serial = interfaceProtocolMapping.getChannelizedE1serialInterface();
		SerialInterface serialInter = new SerialInterface();
		if (channelizedE1Serial != null) {
			if (StringUtils.isNotBlank(channelizedE1Serial.getInterfaceName()))
				serialInter.setName(channelizedE1Serial.getInterfaceName());
			if (StringUtils.isNotBlank(channelizedE1Serial.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1Serial.getModifiedIpv4Address());
				serialInter.setV4IpAddress(ipv4Address);
			}
			/*if (StringUtils.isNotBlank(channelizedE1Serial.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1Serial.getModifiedIpv6Address());
				serialInter.setV6IpAddress(ipv6Address);
			}*/
			wIntfce.setSerialInterface(serialInter);
		}

		ChannelizedSdhInterface channelizedSdhInfe = interfaceProtocolMapping.getChannelizedSdhInterface();
		ChannelizedSDHInterface channelizedSDHInterface = new ChannelizedSDHInterface();
		if (channelizedSdhInfe != null) {
			if (StringUtils.isNotBlank(channelizedSdhInfe.getInterfaceName()))
				channelizedSDHInterface.setName(channelizedSdhInfe.getInterfaceName());
			if (StringUtils.isNotBlank(channelizedSdhInfe.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInfe.getModifiedIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			/*if (StringUtils.isNotBlank(channelizedSdhInfe.getModifiedIipv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInfe.getModifiedIipv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}*/
			wIntfce.setChannelizedSDHInterface(channelizedSDHInterface);
		}

		wanInterface.setInterface(wIntfce);
		return wanInterface;
	}

	protected WANInterface createCpeWanInterface(Cpe cpe, InterfaceProtocolMapping interfaceProtocolMapping) {
		WANInterface wanInterface = new WANInterface();
		WANInterface.Interface wIntfce = new WANInterface.Interface();
		EthernetInterface etherInterface = new EthernetInterface();
		com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter = interfaceProtocolMapping
				.getEthernetInterface();
		if (etherInter != null) {
			if (StringUtils.isNotBlank(etherInter.getInterfaceName()))
				etherInterface.setName(etherInter.getInterfaceName());
			if (StringUtils.isNotBlank(etherInter.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(etherInter.getModifiedIpv4Address());
				etherInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(etherInter.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(etherInter.getModifiedIpv6Address());
				etherInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(etherInter.getModifiedSecondaryIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(etherInter.getModifiedSecondaryIpv4Address());
				wanInterface.setSecondaryV4WANIPAddress(ipv4Address);
				//ethernetInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(etherInter.getInnerVlan()))
				etherInterface.setVlan(ActivationUtils.toInteger(etherInter.getInnerVlan()));
			if (StringUtils.isNotBlank(etherInter.getPortType()))
				etherInterface.setPortType(etherInter.getPortType().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getMode()))
				etherInterface.setMode(etherInter.getMode().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getMediaType()))
				etherInterface.setMediaType(etherInter.getMediaType().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getSpeed()))
				etherInterface.setSpeed(etherInter.getSpeed());
			if (StringUtils.isNotBlank(etherInter.getDuplex()))
				etherInterface.setDuplex(ActivationUtils.getDuplex(etherInter.getDuplex()));
			if (StringUtils.isNotBlank(etherInter.getAutonegotiationEnabled()))
				etherInterface.setIsAutoNegotiation(etherInter.getAutonegotiationEnabled());
			wIntfce.setEthernetInterface(etherInterface);
		}

		ChannelizedE1serialInterface channelizedE1Serial = interfaceProtocolMapping.getChannelizedE1serialInterface();
		SerialInterface serialInter = new SerialInterface();
		if (channelizedE1Serial != null) {
			if (StringUtils.isNotBlank(channelizedE1Serial.getInterfaceName()))
				serialInter.setName(channelizedE1Serial.getInterfaceName());
			if (StringUtils.isNotBlank(channelizedE1Serial.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1Serial.getModifiedIpv4Address());
				serialInter.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(channelizedE1Serial.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1Serial.getModifiedIpv6Address());
				serialInter.setV6IpAddress(ipv6Address);
			}
			wIntfce.setSerialInterface(serialInter);
		}

		ChannelizedSdhInterface channelizedSdhInfe = interfaceProtocolMapping.getChannelizedSdhInterface();
		ChannelizedSDHInterface channelizedSDHInterface = new ChannelizedSDHInterface();
		if (channelizedSdhInfe != null) {
			if (StringUtils.isNotBlank(channelizedSdhInfe.getInterfaceName()))
				channelizedSDHInterface.setName(channelizedSdhInfe.getInterfaceName());
			if (StringUtils.isNotBlank(channelizedSdhInfe.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInfe.getModifiedIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(channelizedSdhInfe.getModifiedIipv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInfe.getModifiedIipv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}
			wIntfce.setChannelizedSDHInterface(channelizedSDHInterface);
		}

		wanInterface.setInterface(wIntfce);
		return wanInterface;
	}

	protected LANInterface createCpeLanInterface(Cpe cpe, InterfaceProtocolMapping interfaceProtocolMapping) {
		LANInterface lanInterface = new LANInterface();
		EthernetInterface etherInterface = new EthernetInterface();
		com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter = interfaceProtocolMapping
				.getEthernetInterface();
		etherInterface.setName(etherInter.getInterfaceName());
		IPV4Address ipv4Address = new IPV4Address();
		ipv4Address.setAddress(etherInter.getIpv4Address());
		etherInterface.setV4IpAddress(ipv4Address);
		IPV6Address ipv6Address = new IPV6Address();
		ipv6Address.setAddress(etherInter.getIpv6Address());
		etherInterface.setV6IpAddress(ipv6Address);
		etherInterface.setVlan(ActivationUtils.toInteger(etherInter.getInnerVlan()));
		etherInterface.setPortType(etherInter.getPortType().toUpperCase());
		etherInterface.setMode(etherInter.getMode().toUpperCase());
		etherInterface.setMediaType(etherInter.getMediaType().toUpperCase());
		etherInterface.setSpeed(etherInter.getSpeed());
		etherInterface.setDuplex(ActivationUtils.getDuplex(etherInter.getDuplex()));
		etherInterface.setIsAutoNegotiation(etherInter.getAutonegotiationEnabled());
		lanInterface.setInterface(etherInterface);
		lanInterface.setHSRPProtocol(createCpeHrspProtocol(etherInter));
		lanInterface.setRoutingProtocol(createCpeLanRoutingProtocol(interfaceProtocolMapping));
		return lanInterface;

	}

	protected HSRPProtocol createCpeHrspProtocol(
			com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter) {
		HSRPProtocol hsrpProtocol = null;
		Set<HsrpVrrpProtocol> hsrpProtocols = etherInter.getHsrpVrrpProtocols();// It is a array
		for (HsrpVrrpProtocol hsrpVrrpProtocol : hsrpProtocols) {
			hsrpProtocol = new HSRPProtocol();
			hsrpProtocol.setHelloValue(hsrpVrrpProtocol.getHelloValue());
			if (hsrpVrrpProtocol.getHoldValue() != null)
				hsrpProtocol.setHoldValue(hsrpVrrpProtocol.getHoldValue());
			if (hsrpVrrpProtocol.getTimerUnit() != null)
				hsrpProtocol.setTimerUnit(hsrpVrrpProtocol.getTimerUnit());
			if (hsrpVrrpProtocol.getVirtualIpv4Address() != null)
				hsrpProtocol.setVirtualIPAddress(hsrpVrrpProtocol.getVirtualIpv4Address());
			if (hsrpVrrpProtocol.getRole() != null)
				hsrpProtocol.setRole(hsrpVrrpProtocol.getRole().toUpperCase());
			if (hsrpVrrpProtocol.getVirtualIpv6Address() != null)
				hsrpProtocol.setVirtualIPv6Address(hsrpVrrpProtocol.getVirtualIpv6Address());
			if (hsrpVrrpProtocol.getVirtualIpv4Address() != null)
				hsrpProtocol.setVirtualIPAddress(hsrpVrrpProtocol.getVirtualIpv4Address());
			if (hsrpVrrpProtocol.getPriority() != null)
				hsrpProtocol.setPriority(ActivationUtils.toInteger(hsrpVrrpProtocol.getPriority()));
		}
		return hsrpProtocol;
	}

	protected LANInterface.RoutingProtocol createCpeLanRoutingProtocol(
			InterfaceProtocolMapping interfaceProtocolMapping) {
		LANInterface.RoutingProtocol lanRoutingProtocol = null;
		BGPRoutingProtocol bgpRoutingProtocol = null;
		Bgp bgp = interfaceProtocolMapping.getBgp();
		if (bgp != null) {
			lanRoutingProtocol = new LANInterface.RoutingProtocol();
			bgpRoutingProtocol = new BGPRoutingProtocol();
			if (bgp.getRemoteAsNumber() != null) {
				ASNumber asNumber = new ASNumber();
				asNumber.setNumber(ActivationUtils.toString(bgp.getRemoteAsNumber()));
				bgpRoutingProtocol.setRemoteASNumber(asNumber);
			}
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(bgp.getRemoteIpv4Address());
			bgpRoutingProtocol.setRemoteIPV4Address(ipv4Address);
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(bgp.getRemoteIpv6Address());
			bgpRoutingProtocol.setRemoteIPV6Address(ipv6Address);
			if (bgp.getLocalAsNumber() != null) {
				ASNumber bgpNeibourLocalAsNumber = new ASNumber();
				bgpNeibourLocalAsNumber.setNumber(bgp.getLocalAsNumber());
				bgpRoutingProtocol.setBgpNeighbourLocalASNumber(bgpNeibourLocalAsNumber);
			}
			if (bgp.getAsoOverride() != null)
				bgpRoutingProtocol.setIsASOverriden(ActivationUtils.getStrValue(bgp.getAsoOverride()));
			if (bgp.getIsebgpMultihopReqd() != null)
				bgpRoutingProtocol.setIsEBGPMultihopRequired(ActivationUtils.getStrValue(bgp.getIsebgpMultihopReqd()));
			if (bgp.getMaxPrefix() != null)
				bgpRoutingProtocol.setMaxPrefix(ActivationUtils.toInteger(bgp.getMaxPrefix()));
			if (bgp.getMaxPrefixThreshold() != null)
				bgpRoutingProtocol.setMaxPrefixThreshold(ActivationUtils.toInteger(bgp.getMaxPrefixThreshold()));
			if (bgp.getRedistributeStaticEnabled() != null)
				bgpRoutingProtocol.setIsRedistributeStaticEnabled(
						ActivationUtils.getBooleanValue(bgp.getRedistributeStaticEnabled()));
			if (bgp.getRedistributeConnectedEnabled() != null)
				bgpRoutingProtocol.setIsRedistributeConnectedEnabled(
						ActivationUtils.getBooleanValue(bgp.getRedistributeConnectedEnabled()));
			if (bgp.getHelloTime() != null)
				bgpRoutingProtocol.setHelloTime(ActivationUtils.toInteger(bgp.getHelloTime()));
			if (bgp.getHoldTime() != null)
				bgpRoutingProtocol.setHoldTime(ActivationUtils.toInteger(bgp.getHoldTime()));
			bgpRoutingProtocol.setIsOriginateDefaultEnabled(false);// TODO
			bgpRoutingProtocol
					.setIsAuthenticationRequired(ActivationUtils.getBooleanValue(bgp.getIsauthenticationRequired()));
			if (StringUtils.isNotBlank(bgp.getAuthenticationMode()))
				bgpRoutingProtocol.setAuthenticationMode(bgp.getAuthenticationMode().toUpperCase());
			if (StringUtils.isNotBlank(bgp.getPassword()))
				bgpRoutingProtocol.setAuthenticationPassword(bgp.getPassword());
			Set<PolicyType> policyTypes = bgp.getPolicyTypes();
			for (PolicyType policyType : policyTypes) {
				if (policyType.getInboundRoutingIpv4Policy() == 1) {
					PrefixList inboundV4PrefixList = new PrefixList();
					inboundV4PrefixList.setIsPreprovisioned(
							ActivationUtils.getBooleanValue(policyType.getInboundIpv4Preprovisioned()));
					inboundV4PrefixList.setName(policyType.getInboundIpv4PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry inBoundV4PrefixListEntry = new PrefixListEntry();
								inBoundV4PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix()!=null ?policyListConfig.getNetworkPrefix().trim():policyListConfig.getNetworkPrefix());
								if(policyListConfig.getAction()!=null) {
								inBoundV4PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								}else {
									inBoundV4PrefixListEntry.setOperator("exact");
								}
								inBoundV4PrefixListEntry.setSubnetRangeMaximum(
										ActivationUtils.toInteger(policyListConfig.getLeValue()));
								inBoundV4PrefixListEntry.setSubnetRangeMinimum(
										ActivationUtils.toInteger(policyListConfig.getGeValue()));
								inboundV4PrefixList.getPrefixListEntry().add(inBoundV4PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setInboundBGPv4PrefixesAllowed(inboundV4PrefixList);
				}

				if (policyType.getInboundRoutingIpv6Policy() == 1) {
					PrefixList inboundV6PrefixList = new PrefixList();
					inboundV6PrefixList.setIsPreprovisioned(
							ActivationUtils.getBooleanValue(policyType.getInboundIpv6Preprovisioned()));
					inboundV6PrefixList.setName(policyType.getInboundIpv6PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry inBoundV6PrefixListEntry = new PrefixListEntry();
								inBoundV6PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix()!=null?policyListConfig.getNetworkPrefix().trim():policyListConfig.getNetworkPrefix());
								if(policyListConfig.getAction()!=null) {
								inBoundV6PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								}else {
									inBoundV6PrefixListEntry.setOperator("exact");
								}
								inBoundV6PrefixListEntry.setSubnetRangeMaximum(
										ActivationUtils.toInteger(policyListConfig.getLeValue()));
								inBoundV6PrefixListEntry.setSubnetRangeMinimum(
										ActivationUtils.toInteger(policyListConfig.getGeValue()));
								inboundV6PrefixList.getPrefixListEntry().add(inBoundV6PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setInboundBGPv6PrefixesAllowed(inboundV6PrefixList);
				}

				if (policyType.getOutboundRoutingIpv6Policy() == 1) {
					PrefixList outboundV6PrefixList = new PrefixList();
					outboundV6PrefixList.setIsPreprovisioned(
							ActivationUtils.getBooleanValue(policyType.getOutboundIpv6Preprovisioned()));
					outboundV6PrefixList.setName(policyType.getOutboundIpv6PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry outBoundV6PrefixListEntry = new PrefixListEntry();
								outBoundV6PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix()!=null?policyListConfig.getNetworkPrefix().trim():policyListConfig.getNetworkPrefix());
								if(policyListConfig.getAction()!=null) {
								outBoundV6PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								}
								outBoundV6PrefixListEntry.setSubnetRangeMaximum(
										ActivationUtils.toInteger(policyListConfig.getLeValue()));
								outBoundV6PrefixListEntry.setSubnetRangeMinimum(
										ActivationUtils.toInteger(policyListConfig.getGeValue()));
								outboundV6PrefixList.getPrefixListEntry().add(outBoundV6PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setOutboundBGPv6PrefixesAllowed(outboundV6PrefixList);
				}

				if (policyType.getOutboundRoutingIpv4Policy() == 1) {
					PrefixList outboundV4PrefixList = new PrefixList();
					if (policyType.getOutboundIpv4Preprovisioned() != null)
						outboundV4PrefixList.setIsPreprovisioned(
								ActivationUtils.getBooleanValue(policyType.getOutboundIpv4Preprovisioned()));
					if (StringUtils.isNotBlank(policyType.getOutboundIpv6PolicyName()))
						outboundV4PrefixList.setName(policyType.getOutboundIpv6PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry outBoundV4PrefixListEntry = new PrefixListEntry();
								if (StringUtils.isNotBlank(policyListConfig.getNetworkPrefix()))
									outBoundV4PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix().trim());
								if (StringUtils.isNotBlank(policyListConfig.getAction()))
									outBoundV4PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								if (StringUtils.isNotBlank(policyListConfig.getLeValue()))
									outBoundV4PrefixListEntry.setSubnetRangeMaximum(
											ActivationUtils.toInteger(policyListConfig.getLeValue()));
								if (StringUtils.isNotBlank(policyListConfig.getGeValue()))
									outBoundV4PrefixListEntry.setSubnetRangeMinimum(
											ActivationUtils.toInteger(policyListConfig.getGeValue()));
								outboundV4PrefixList.getPrefixListEntry().add(outBoundV4PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setOutboundBGPv4PrefixesAllowed(outboundV4PrefixList);
				}

			}
			lanRoutingProtocol.setBgpRoutingProtocol(bgpRoutingProtocol);
		}
		return lanRoutingProtocol;
	}

	protected void createAttributes() {

	}

	protected void createPeWanAdditionalStaticBgp() {

	}

	protected void createPeWanStaticRouter() {

	}

	protected void createRoutingProtocolBgp() {

	}

	private String getInterfaceDummyIp(IpDummyDetail ipDummyDetail){
		String ipPort=null;
		if(Objects.nonNull(ipDummyDetail) && Objects.nonNull(ipDummyDetail.getDummyWANIpAddressSubnet())){
			String[] dummyIps=ipDummyDetail.getDummyWANIpAddressSubnet().split("/");
			String port=dummyIps[1];
			ipPort=Objects.nonNull(ipDummyDetail.getTclSideDummyIpAddress())?ipDummyDetail.getTclSideDummyIpAddress()+"/"+port:null;
		}
		return ipPort;
	}

	private String getCpeDummyIp(IpDummyDetail ipDummyDetail){
		String ipPort=null;
		if(Objects.nonNull(ipDummyDetail) && Objects.nonNull(ipDummyDetail.getDummyWANIpAddressSubnet())){
			String[] dummyIps=ipDummyDetail.getDummyWANIpAddressSubnet().split("/");
			String port=dummyIps[1];
			ipPort=Objects.nonNull(ipDummyDetail.getCustomerSideDummyIpAddress())?ipDummyDetail.getCustomerSideDummyIpAddress()+"/"+port:null;
		}
		return ipPort;
	}

	private String addDummyValue(String input,String addOn){
		String[] splitString=input.split("_");
		String lastValue = splitString[splitString.length-1];
		input=input.replace(lastValue,addOn+lastValue);
		return input;
	}
}
