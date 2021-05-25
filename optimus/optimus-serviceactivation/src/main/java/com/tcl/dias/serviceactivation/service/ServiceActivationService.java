
package com.tcl.dias.serviceactivation.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.GetIpServiceEndPoint;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.RfService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.serviceactivation.activation.constants.AceConstants;
import com.tcl.dias.serviceactivation.activation.fti.service.FTIService;
import com.tcl.dias.serviceactivation.activation.services.IPDetailsService;
import com.tcl.dias.serviceactivation.activation.services.SatSocService;
import com.tcl.dias.serviceactivation.beans.SatcoServiceDataRefreshBean;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.AssignDummyWANIPResponse;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.BGP;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerCPE;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerEthernetInterface;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerEthernetTopologyInfo;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerSDHInterface;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerSerialInterface;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerWimaxLastmile;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.GetIPServiceInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.HSRP;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.IPV4Address;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.IPV6Address;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.Multicasting;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.OSPF;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.PERouter;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.Static;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.Switch;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.VRRP;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.Attribute;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.VpnIllPortAttributes;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.AluSammgrSeq;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.Eigrp;
import com.tcl.dias.serviceactivation.entity.entities.EthernetInterface;
import com.tcl.dias.serviceactivation.entity.entities.HsrpVrrpProtocol;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpDummyDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.MstCambiumDetails;
import com.tcl.dias.serviceactivation.entity.entities.MstP2PDetails;
import com.tcl.dias.serviceactivation.entity.entities.MstRadwinDetails;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
import com.tcl.dias.serviceactivation.entity.entities.Rip;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.RouterUplinkport;
import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.VpnMetatData;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.entities.WimaxLastmile;
import com.tcl.dias.serviceactivation.entity.repository.AclPolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.AluSammgrSeqRepository;
import com.tcl.dias.serviceactivation.entity.repository.BgpRepository;
import com.tcl.dias.serviceactivation.entity.repository.CambiumLastmileRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedE1serialInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedSdhInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.CpeRepository;
import com.tcl.dias.serviceactivation.entity.repository.EigrpRepository;
import com.tcl.dias.serviceactivation.entity.repository.EthernetInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.HsrpVrrpProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.InterfaceProtocolMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpDummyDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.LmComponentRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstCambiumDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstP2PDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRadwinDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRegionalPopCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstServiceCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MulticastingRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.OspfRepository;
import com.tcl.dias.serviceactivation.entity.repository.RadwinLastmileRepository;
import com.tcl.dias.serviceactivation.entity.repository.RipRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterUplinkPortRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceCosCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceQoRepository;
import com.tcl.dias.serviceactivation.entity.repository.StaticProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.TopologyRepository;
import com.tcl.dias.serviceactivation.entity.repository.UniswitchDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnMetatDataRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.VrfRepository;
import com.tcl.dias.serviceactivation.entity.repository.WanStaticRouteRepository;
import com.tcl.dias.serviceactivation.entity.repository.WimaxLastmileRepository;
import com.tcl.dias.servicefulfillment.entity.entities.AceIPMapping;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.AceIPMappingRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean;
import com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.CollectionUtils;

@Service
public class ServiceActivationService {

	private static final Logger logger = LoggerFactory.getLogger(ServiceActivationService.class);

	Timestamp startDate = new Timestamp(new Date().getTime());
	Timestamp endDate = null;
	Timestamp lastModifiedDate = new Timestamp(new Date().getTime());

	// TODO: pick the env
	String modifiedBy = "Optimus_Initial";

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	InterfaceProtocolMappingRepository interfaceProtocolMappingRepository;

	@Autowired
	RouterDetailRepository routerDetailRepository;

	@Autowired
	CpeRepository cpeRepository;

	@Autowired
	OspfRepository ospfRepository;

	@Autowired
	EigrpRepository eigrpRepository;

	@Autowired
	StaticProtocolRepository staticProtocolRepository;

	@Autowired
	BgpRepository bgpRepository;

	@Autowired
	RipRepository ripRepository;

	@Autowired
	EthernetInterfaceRepository ethernetInterfaceRepository;

	@Autowired
	HsrpVrrpProtocolRepository hsrpVrrpProtocolRepository;

	@Autowired
	ChannelizedE1serialInterfaceRepository channelizedE1serialInterfaceRepository;

	@Autowired
	ChannelizedSdhInterfaceRepository channelizedSdhInterfaceRepository;

	@Autowired
	WimaxLastmileRepository wimaxLastmileRepository;

	@Autowired
	TopologyRepository topologyRepository;

	@Autowired
	RouterUplinkPortRepository routerUplinkPortRepository;

	@Autowired
	LmComponentRepository lmComponentRepository;

	@Autowired
	MulticastingRepository multicastingRepository;

	@Autowired
	UniswitchDetailRepository uniswitchDetailRepository;

	@Autowired
	VrfRepository vrfRepository;

	@Autowired
	AluSammgrSeqRepository aluSammgrSeqRepository;

	@Autowired
	AclPolicyCriteriaRepository aclPolicyCriteriaRepository;

	@Autowired
	ServiceQoRepository serviceQoRepository;

	@Autowired
	ServiceCosCriteriaRepository serviceCosCriteriaRepository;

	@Autowired
	IpAddressDetailRepository ipAddressDetailRepository;

	@Autowired
	MstServiceCommunityRepository mstServiceCommunityRepository;

	@Autowired
	MstRegionalPopCommunityRepository mstRegionalPopCommunityRepository;

	@Autowired
	WanStaticRouteRepository wanStaticRouteRepository;

	@Autowired
	VpnSolutionRepository vpnSolutionRepository;

	@Autowired
	VpnMetatDataRepository vpnMetaDataRepository;

	@Autowired
	IpaddrWanv6AddressRepository ipaddrWanv6AddressRepository;

	@Autowired
	IpaddrWanv4AddressRepository ipaddrWanv4AddressRepository;

	@Autowired
	IpaddrLanv4AddressRepository ipaddrLanv4AddressRepository;

	@Autowired
	IpaddrLanv6AddressRepository ipaddrLanv6AddressRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	RadwinLastmileRepository radwinLastmileRepository;

	@Autowired
	CambiumLastmileRepository cambiumLastmileRepository;
	
	@Autowired
	MstCambiumDetailsRepository mstCambiumDetailsRepository;
	
	@Autowired
	MstRadwinDetailsRepository mstRadwinDetailsRepository;
	
	@Autowired
	AceIPMappingRepository aceIPMappingRepository;
	
	@Autowired
	IpDummyDetailRepository ipDummyDetailRepository;
	
	@Autowired
	IPDetailsService iPDetailsService;

	@Autowired
	SapService sapService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	GetIpServiceEndPoint getIpServiceEndPoint;

	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	MstP2PDetailsRepository mstP2PDetailsRepository;
	
	@Autowired
	RfService rfService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskDataRepository taskDataRepository;

	@Autowired
	MstTaskAttributeRepository mstTaskAttributeRepository;

	@Autowired
	FTIService ftiService;

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.ipserviceasync}")
	String ipServiceAsyncQueue;

	@Value("${queue.rfinventory}")
	String rfInventoryHandoverQueue;

	@Value("${fti.refresh.url}")
	String ftiRefreshUrl;
	
	@Value("${application.env:PROD}")
	String appEnv;
	
	@Autowired
	SatSocService satSocService;

	
	public static final String OPTIMUS_RULE="OPTIMUS_RULE";
	public static final String OPTIMUS_REFRESH="OPTIMUS_REFRESH";
	
	public static final String ISSUED="ISSUED";
	public static final String CANCELLED="CANCELLED";
	public static final String ISSUEDSERVICEDATILS="ISSUEDSERVICEDATILS";

	
	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public String processDummyCramerResponse(AssignDummyWANIPResponse request, String serviceId) throws TclCommonException{
		logger.info("Service Activation - processDummyCramerResponse - started");
		ServiceDetail serviceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId,"ISSUED");
		if(Objects.nonNull(serviceDetail)){
			logger.info("Service Detail exists");
			IpDummyDetail ipDummyDetail = new IpDummyDetail();
			ipDummyDetail.setCustomerSideDummyIpAddress(request.getCustomersideDummyIPAddress());
			ipDummyDetail.setTclSideDummyIpAddress(request.getTclsideDummyIPAddress());
			ipDummyDetail.setDummyWANIpAddressSubnet(request.getDummyWANIPAddressSubnet());
			ipDummyDetail.setServiceDetail(serviceDetail);
			ipDummyDetail.setCreatedBy("OPTIMUS_INITIAL");
			ipDummyDetail.setCreatedDate(new Timestamp(new Date().getTime()));
			ipDummyDetailRepository.saveAndFlush(ipDummyDetail);
			return "SUCCESS";
		}
		return "ERROR";
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public String processCramerResponse(GetIPServiceInfoResponse request) throws TclCommonException {
		logger.info("Service Activation - processCramerResponse - started");
		String serviceCode = null;
		
		boolean endDateCall=false;
		

		Map<String, ServiceDetail> issuedContainer = new HashMap<>();

		try {
			// String serviceCode = request.getHeader().getRequestID().split("_")[1];
			serviceCode = request.getService().getService().getIasService().getServiceID();

			// saving values from service fulfillment to service activation
			logger.info("serviceCode {}", serviceCode);
			ServiceDetail oldServoceDetails = serviceDetailRepository
					.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);
			
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
							oldServoceDetails.getScServiceDetailId(), AttributeConstants.COMPONENT_LM, "A");
			issuedContainer.put(ISSUEDSERVICEDATILS, oldServoceDetails);

			if (oldServoceDetails!=null) {
				endDateCall = true;
				oldServoceDetails.setServiceState(CANCELLED);
				serviceDetailRepository.saveAndFlush(oldServoceDetails);
				logger.info("orderCode {}", oldServoceDetails.getOrderDetail().getExtOrderrefId());
				ScOrder scOrder=scOrderRepository.findDistinctByOpOrderCode(oldServoceDetails.getOrderDetail().getExtOrderrefId());
        		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrder_id(serviceCode,scOrder.getId());

				ServiceDetail newServiceDetails = saveServiceDetailsActivation(serviceCode,
						oldServoceDetails.getVersion(),scOrder,scServiceDetail,scComponentAttributesAMap);
				issuedContainer.put(ISSUEDSERVICEDATILS, newServiceDetails);

			}

			if (issuedContainer.containsKey(ISSUEDSERVICEDATILS)) {
				logger.info("Issued exists");

				ServiceDetail serviceDetail = issuedContainer.get(ISSUEDSERVICEDATILS);
				serviceDetail.setServiceState(ISSUED);
				serviceDetailRepository.saveAndFlush(serviceDetail);

				if (endDateCall) {
					iPDetailsService.endDateActivationRecords(oldServoceDetails, modifiedBy, true, true);
				}
				Map<String, Object> iasServiceMap = new HashMap<String, Object>();
				iasServiceMap.put("isCPEExists",
						Optional.ofNullable(
								request.getService().getService().getIasService().getCustomerPremiseEquipment())
								.isPresent());

				iasServiceMap.put("isWanRoutingProtocolExists",
						Optional.ofNullable(request.getService().getService().getIasService().getWanRoutingProtocol())
								.isPresent());
				
				if((Boolean) iasServiceMap.get("isCPEExists")) {
					iasServiceMap.put("CUSTOMERPREMISEQUIPMENT", 
								request.getService().getService().getIasService().getCustomerPremiseEquipment());
					
				}

				iasServiceMap.put("isPERouterExists", Optional
						.ofNullable(request.getService().getService().getIasService().getPERouter()).isPresent());

				if ((Boolean) iasServiceMap.get("isPERouterExists")) {
					iasServiceMap.put("peRouter", request.getService().getService().getIasService().getPERouter());

					iasServiceMap.put("isEthernetInterface", Optional.ofNullable(request.getService().getService()
							.getIasService().getPERouter().getWanInterface().getEthernetInterface()).isPresent());
					iasServiceMap.put("isE1SerialInterface", Optional.ofNullable(request.getService().getService()
							.getIasService().getPERouter().getWanInterface().getSerialInterface()).isPresent());
					iasServiceMap.put("isSHDInterface", Optional.ofNullable(request.getService().getService()
							.getIasService().getPERouter().getWanInterface().getChannelizedSDHInterface()).isPresent());
					iasServiceMap.put("isTopologyInfoPresent", Optional.ofNullable(
							request.getService().getService().getIasService().getPERouter().getTopologyInfo().getName())
							.isPresent());
					iasServiceMap.put("isRouterTopologyInterface1Present", Optional.ofNullable(request.getService()
							.getService().getIasService().getPERouter().getRouterTopologyInterface1()).isPresent());
					iasServiceMap.put("isRouterTopologyInterface2Present", Optional.ofNullable(request.getService()
							.getService().getIasService().getPERouter().getRouterTopologyInterface2()).isPresent());

					if ((Boolean) iasServiceMap.get("isEthernetInterface")) {
						iasServiceMap.put("ethernetInterface", request.getService().getService().getIasService()
								.getPERouter().getWanInterface().getEthernetInterface());
					}

					if ((Boolean) iasServiceMap.get("isE1SerialInterface")) {
						iasServiceMap.put("e1SerialInterface", request.getService().getService().getIasService()
								.getPERouter().getWanInterface().getSerialInterface());
					}

					if ((Boolean) iasServiceMap.get("isSHDInterface")) {
						iasServiceMap.put("sHDInterface", request.getService().getService().getIasService()
								.getPERouter().getWanInterface().getChannelizedSDHInterface());
					}

				} else {
					iasServiceMap.put("isEthernetInterface", false);
					iasServiceMap.put("isE1SerialInterface", false);
					iasServiceMap.put("isSHDInterface", false);
					iasServiceMap.put("isTopologyInfoPresent", false);
					iasServiceMap.put("isRouterTopologyInterface1Present", false);
					iasServiceMap.put("isRouterTopologyInterface2Present", false);
				}

				iasServiceMap.put("isWanV4AddressExists", Optional
						.ofNullable(request.getService().getService().getIasService().getWanV4Address()).isPresent());
				iasServiceMap.put("isWanV6AddressExists", Optional
						.ofNullable(request.getService().getService().getIasService().getWanV6Address()).isPresent());
				iasServiceMap.put("isSecondaryV4WANIPAddressExists",
						Optional.ofNullable(
								request.getService().getService().getIasService().getSecondaryV4WANIPAddress())
								.isPresent());
				iasServiceMap.put("isSecondaryV6WANIPAddressExists",
						Optional.ofNullable(
								request.getService().getService().getIasService().getSecondaryV6WANIPAddress())
								.isPresent());

				iasServiceMap.put("isLanV4AddressExists", Optional
						.ofNullable(request.getService().getService().getIasService().getLanV4Address()).isPresent());
				iasServiceMap.put("isLanV6AddressExists", Optional
						.ofNullable(request.getService().getService().getIasService().getLanV6Address()).isPresent());

				iasServiceMap.put("islastMileExists",
						Optional.ofNullable(request.getService().getLastmile()).isPresent());

				iasServiceMap.put("addOnServicesExist",
						Optional.ofNullable(request.getService().getAddOnServices()).isPresent());

				if ((Boolean) iasServiceMap.get("addOnServicesExist")) {
					iasServiceMap.put("isMulticastingExists",
							Optional.ofNullable(request.getService().getAddOnServices().getMulticasting()).isPresent());
				} else {
					iasServiceMap.put("isMulticastingExists", false);
				}

				if ((Boolean) iasServiceMap.get("isEthernetInterface")) {
					iasServiceMap.put("isEthernetV4IPExists",
							Optional.ofNullable(request.getService().getService().getIasService().getPERouter()
									.getWanInterface().getEthernetInterface().getV4IpAddress()).isPresent());

					iasServiceMap.put("isEthernetV6IPExists",
							Optional.ofNullable(request.getService().getService().getIasService().getPERouter()
									.getWanInterface().getEthernetInterface().getV6IpAddress()).isPresent());

					iasServiceMap.put("isEthernetSecondaryV4WANIPExists",
							Optional.ofNullable(request.getService().getService().getIasService().getPERouter()
									.getWanInterface().getEthernetInterface().getSecondaryV4WANIPAddress())
									.isPresent());

					iasServiceMap.put("isEthernetSecondaryV6WANIPExists",
							Optional.ofNullable(request.getService().getService().getIasService().getPERouter()
									.getWanInterface().getEthernetInterface().getSecondaryV6WANIPAddress())
									.isPresent());
				} else {
					iasServiceMap.put("isEthernetV4IPExists", false);
					iasServiceMap.put("isEthernetV6IPExists", false);
					iasServiceMap.put("isEthernetSecondaryV4WANIPExists", false);
					iasServiceMap.put("isEthernetSecondaryV6WANIPExists", false);
				}

				if ((Boolean) iasServiceMap.get("isWanRoutingProtocolExists")) {
					iasServiceMap.put("isBgp",
							Optional.ofNullable(
									request.getService().getService().getIasService().getWanRoutingProtocol().getBGP())
									.isPresent());
					iasServiceMap.put("isStatic", Optional.ofNullable(
							request.getService().getService().getIasService().getWanRoutingProtocol().getStatic())
							.isPresent());
					iasServiceMap.put("isOspf",
							Optional.ofNullable(
									request.getService().getService().getIasService().getWanRoutingProtocol().getOSPF())
									.isPresent());
					iasServiceMap.put("isEigrp", Optional.ofNullable(
							request.getService().getService().getIasService().getWanRoutingProtocol().getEIGRP())
							.isPresent());
					iasServiceMap.put("isRip",
							Optional.ofNullable(
									request.getService().getService().getIasService().getWanRoutingProtocol().getRIP())
									.isPresent());
				} else {
					iasServiceMap.put("isBgp", false);
					iasServiceMap.put("isStatic", false);
					iasServiceMap.put("isOspf", false);
					iasServiceMap.put("isEigrp", false);
					iasServiceMap.put("isRip", false);
				}

				iasServiceMap.put("isLanRoutingProtocolPresent",
						Optional.ofNullable(request.getService().getService().getIasService().getLanRoutingProtocol())
								.isPresent());

				iasServiceMap.put("isUNISwitchPresent", Optional
						.ofNullable(request.getService().getService().getIasService().getUNISwitch()).isPresent());
				if (iasServiceMap.containsKey("isUNISwitchPresent")
						&& (Boolean) iasServiceMap.get("isUNISwitchPresent")) {
					iasServiceMap.put("uniswitch", request.getService().getService().getIasService().getUNISwitch());
				}
				
				

				logger.info("isUNISwitchPresent {}",iasServiceMap.get("isUNISwitchPresent") );
				
				iasServiceMap.put("isWanRoutingProtocolPresent",
						Optional.ofNullable(request.getService().getService().getIasService().getWanRoutingProtocol())
								.isPresent());

				if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent")) {
					iasServiceMap.put("isHsrpPresent",
							Optional.ofNullable(
									request.getService().getService().getIasService().getLanRoutingProtocol().getHSRP())
									.isPresent());
					iasServiceMap.put("isVrrpPresent",
							Optional.ofNullable(
									request.getService().getService().getIasService().getLanRoutingProtocol().getVRRP())
									.isPresent());
				} else {
					iasServiceMap.put("isHsrpPresent", false);
					iasServiceMap.put("isVrrpPresent", false);
				}

				applyAceMappingIntoLanV4andV6(serviceDetail);
				/*if("IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
					logger.info("IZOPC Service Id::{} for CspPrefixes",serviceDetail.getServiceId());
					ScComponentAttribute prefixFromMplsToCspProvidedByCustomerAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "prefixFromMplsToCspProvidedByCustomer", "LM", "A");
					if(prefixFromMplsToCspProvidedByCustomerAttribute!=null && prefixFromMplsToCspProvidedByCustomerAttribute.getAttributeValue()!=null && "Yes".equalsIgnoreCase(prefixFromMplsToCspProvidedByCustomerAttribute.getAttributeValue())) {
						logger.info("prefixFromMplsToCspProvidedByCustomerAttribute for Service Id::{}",serviceDetail.getServiceId());
						ScComponentAttribute prefixFromMplsToCspAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "prefixFromMplsToCsp", "LM","A");
						if(prefixFromMplsToCspAttribute!=null && prefixFromMplsToCspAttribute.getAttributeValue()!=null) {
							logger.info("createOutboundV4PrefixConfigForIZOPC for Service Id::{} with PrefixForMplstoCsp::{}",serviceDetail.getServiceId(),prefixFromMplsToCspAttribute.getAttributeValue());
							try {
								savePrefixRelatedIpaddrLanv4Address(serviceDetail, prefixFromMplsToCspAttribute,0);
							} catch (TclCommonException e) {
								logger.info("Exception while saving savePrefixRelatedIpaddrLanv4Address in service activation for prefixFromMplstoCsp");
							}
						}
					}
					ScComponentAttribute prefixForCspProvidedByCustomerAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "prefixForCspProvidedByCustomer", "LM","A");
					if(prefixForCspProvidedByCustomerAttribute!=null && prefixForCspProvidedByCustomerAttribute.getAttributeValue()!=null && "Yes".equalsIgnoreCase(prefixForCspProvidedByCustomerAttribute.getAttributeValue())) {
						logger.info("prefixForCspProvidedByCustomerAttribute for Service Id::{}",serviceDetail.getServiceId());
						ScComponentAttribute prefixForCspAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "prefixForCsp", "LM","A");
						if(prefixForCspAttribute!=null && prefixForCspAttribute.getAttributeValue()!=null) {
							logger.info("createInboundV4PrefixConfigForIZOPC for Service Id::{} with PrefixForCsp::{}",serviceDetail.getServiceId(),prefixForCspAttribute.getAttributeValue());
							try {
								savePrefixRelatedIpaddrLanv4Address(serviceDetail, prefixForCspAttribute,0);
							} catch (TclCommonException e) {
								logger.info("Exception while saving savePrefixRelatedIpaddrLanv4Address in service activation for prefixForCsp");
							}
						}
					}
				}*/
				if ((Boolean) iasServiceMap.get("isLanV4AddressExists")) {

					if (null != request.getService().getService().getIasService().getLanV4Address()) {
						List<IPV4Address> ipv4AddrList = request.getService().getService().getIasService()
								.getLanV4Address();
						byte[] count={-1};
						ipv4AddrList.stream().forEach(eachIp -> {
							try {
								count[0]++;
								saveIpaddrLanv4Address(serviceDetail, eachIp,count);
							} catch (TclCommonException e) {
								logger.info("Exception while saving lan v4 address in service activation");
							}
						});

					}
				}

				if ((Boolean) iasServiceMap.get("isLanV6AddressExists")) {
					if (null != request.getService().getService().getIasService().getLanV6Address()) {
						List<IPV6Address> ipv4AddrList = request.getService().getService().getIasService()
								.getLanV6Address();
						byte[] count={-1};
						ipv4AddrList.stream().forEach(eachIp -> {
							try {
								count[0]++;
								saveIpaddrLanv6Address(serviceDetail, eachIp,count);
							} catch (TclCommonException e) {
								logger.info("Exception while saving lan v4 address in service activation");
							}
						});
					}
				}

				if(!"IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
					ScComponentAttribute attribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpAddress", "LM", "A");
					ScComponentAttribute wanIpProvidedByCustAttribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpProvidedByCust", "LM", "A");
					boolean isRadwinSyIpExists[]={false};
					boolean isRadwinCustomerIpAssignedByTcl=false;
					List<String> radwinSyIpList= new ArrayList<>();
					logger.info("ValidateRadwinDetails");
					isRadwinSyIpExists[0]=validateRadwinSyIpExists(serviceDetail,request,iasServiceMap,isRadwinSyIpExists,radwinSyIpList);
					if ((Boolean) iasServiceMap.get("isWanV4AddressExists")) {
						logger.info("Cramer WANV4 exists::{}",radwinSyIpList.size());
						if (null != request.getService().getService().getIasService().getWanV4Address() && !request.getService().getService().getIasService().getWanV4Address().isEmpty()) {
							List<IPV4Address> ipv4AddrList = request.getService().getService().getIasService()
									.getWanV4Address();
							if(ipv4AddrList!=null && ipv4AddrList.size()==2){
								isRadwinCustomerIpAssignedByTcl=true;
							}
							ipv4AddrList.stream().forEach(eachIp -> {
								try {
									if((isRadwinSyIpExists[0] && !radwinSyIpList.isEmpty() && !radwinSyIpList.get(0).equals(eachIp.getAddress()))
											|| (!isRadwinSyIpExists[0] && radwinSyIpList.isEmpty())){
										logger.info("Cramer WANV4 other than radwin");
										saveIpaddrWanv4Address(serviceDetail, eachIp,true);
									}
								} catch (TclCommonException e) {
									logger.error("Exception while saving lan v4 address in service activation",e);
								}
							});
						}else{
							logger.info("Cramer WANV4 not exists");
							saveIpAddrWanv4CustomerProvided(serviceDetail,attribute,wanIpProvidedByCustAttribute);
						}
					}else{
						logger.info("WANV4 not exists");
						saveIpAddrWanv4CustomerProvided(serviceDetail,attribute,wanIpProvidedByCustAttribute);
					}
					
					if(isRadwinSyIpExists[0]){
						//SyWanIpCase
						logger.info("Radwin Sy Ip exists");
						applySecondaryWanIp(serviceDetail,request,wanIpProvidedByCustAttribute,isRadwinCustomerIpAssignedByTcl);
						saveIpAddrWanv4CustomerProvided(serviceDetail,attribute,wanIpProvidedByCustAttribute);
					}
				}
				
				
				if("IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
					logger.info("IZOPC IP WANV4 exists");
					ScComponentAttribute attribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpPool", "LM", "A");
					ScComponentAttribute wanIpProvidedByCustAttribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpProvidedByCust", "LM", "A");
					saveIpAddrWanv4CustomerProvidedForIZOPC(serviceDetail,attribute,wanIpProvidedByCustAttribute,iasServiceMap,request);
					if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) 
							&& "No".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
						logger.info("IZOPC wanIpProvided By Tcl for Service Id:{}", serviceDetail.getServiceId());
						String subnet="";
						IPV4Address ipv4Address = request.getService().getService().getIasService().getWanV4Address().stream()
								.findFirst().orElse(null);
						if (ipv4Address != null && ipv4Address.getAddress() != null) {
							logger.info("IZOPC wanIpProvided By Tcl for ipv4Address::{} from Cramer for Service Id:{}", ipv4Address.getAddress(),serviceDetail.getServiceId());
							subnet=subnet(ipv4Address.getAddress());
						}
						getTclWanDetailsFromReverseISC(serviceDetail.getServiceId(),serviceDetail.getScServiceDetailId(),subnet);
					}
				}
				
				
				
				/*ScComponentAttribute wanIpProvidedByCustAttribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpProvidedByCust", "LM", "A");
				if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
					logger.info("WANV4 from SF");
					saveIpAddrWanv4CustomerProvided(serviceDetail);
				}
*/
				if ((Boolean) iasServiceMap.get("isWanV6AddressExists")) {
					if (null != request.getService().getService().getIasService().getWanV6Address()) {
						List<IPV6Address> ipv4AddrList = request.getService().getService().getIasService()
								.getWanV6Address();
						ipv4AddrList.stream().forEach(eachIp -> {
							try {
								saveIpaddrWanv6Address(serviceDetail, eachIp);
							} catch (TclCommonException e) {
								logger.info("Exception while saving lan v4 address in service activation");
							}
						});
					}

				}

				if ((Boolean) iasServiceMap.get("isSecondaryV4WANIPAddressExists")) {
					if (null != request.getService().getService().getIasService().getSecondaryV4WANIPAddress()) {
						List<String> wanIpFromCustomerList=new ArrayList<>();
						saveIpaddrWanv4Address(serviceDetail,
								request.getService().getService().getIasService().getSecondaryV4WANIPAddress(),false);
					}
				}

				if ((Boolean) iasServiceMap.get("isSecondaryV6WANIPAddressExists")) {
					if (null != request.getService().getService().getIasService().getSecondaryV6WANIPAddress()) {
						saveIpaddrWanv6Address(serviceDetail,
								request.getService().getService().getIasService().getSecondaryV6WANIPAddress());
					}
				}

				if ((Boolean) iasServiceMap.get("isWanRoutingProtocolPresent")) {
					if ((Boolean) iasServiceMap.get("isBgp")) {
						iasServiceMap.put("bgp",
								request.getService().getService().getIasService().getWanRoutingProtocol().getBGP());
					}

					if ((Boolean) iasServiceMap.get("isStatic")) {
						iasServiceMap.put("static",
								request.getService().getService().getIasService().getWanRoutingProtocol().getStatic());
					}

					if ((Boolean) iasServiceMap.get("isOspf")) {
						iasServiceMap.put("ospf",
								request.getService().getService().getIasService().getWanRoutingProtocol().getOSPF());
					}

					if ((Boolean) iasServiceMap.get("isEigrp")) {
						iasServiceMap.put("eigrp",
								request.getService().getService().getIasService().getWanRoutingProtocol().getEIGRP());
					}

					if ((Boolean) iasServiceMap.get("isRip")) {
						iasServiceMap.put("rip",
								request.getService().getService().getIasService().getWanRoutingProtocol().getRIP());
					}
				}

				if ((Boolean) iasServiceMap.get("isCPEExists")) {
					iasServiceMap.put("cpe",
							request.getService().getService().getIasService().getCustomerPremiseEquipment());

					iasServiceMap.put("isWANInterfaceIPExists", Optional.ofNullable(request.getService().getService()
							.getIasService().getCustomerPremiseEquipment().getWANInterfaceIP()).isPresent());

				} else {
					iasServiceMap.put("isWANInterfaceIPExists", false);
				}

				if ((Boolean) iasServiceMap.get("isRouterTopologyInterface1Present")) {
					iasServiceMap.put("WANInterfaceIP", request.getService().getService().getIasService()
							.getCustomerPremiseEquipment().getWANInterfaceIP());
				}

				// saving router info
				saveInterfaceProtocolMapping(serviceDetail, null, iasServiceMap, true, false);

				// saving cpe info
				saveInterfaceProtocolMapping(serviceDetail, null, iasServiceMap, false, true);

				if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent")
						&& (Boolean) iasServiceMap.get("isHsrpPresent")) {
					iasServiceMap.put("hsrp",
							request.getService().getService().getIasService().getLanRoutingProtocol().getHSRP());
				}

				if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent")
						&& (Boolean) iasServiceMap.get("isVrrpPresent")) {
					iasServiceMap.put("vrrp",
							request.getService().getService().getIasService().getLanRoutingProtocol().getVRRP());
				}

				// lastmile related savings
				if ((Boolean) iasServiceMap.get("islastMileExists")) {
					iasServiceMap.put("isWimaxLastMileExists",
							Optional.ofNullable(request.getService().getLastmile().getWimaxLastMile()).isPresent());

					if ((Boolean) iasServiceMap.get("isWimaxLastMileExists")) {
						iasServiceMap.put("wimax", request.getService().getLastmile().getWimaxLastMile());
						CramerWimaxLastmile cramerWimaxLastmile = (CramerWimaxLastmile) iasServiceMap.get("wimax");

						
						ScComponentAttribute rfMake = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
										"rfMakeModel", "LM", "A");
						
						String deviceType = null;
						if (rfMake != null && Objects.nonNull(rfMake.getAttributeValue()) && !rfMake.getAttributeValue().isEmpty()) {
							deviceType = rfMake.getAttributeValue();
						}else{
							ScComponentAttribute rfTechnology = scComponentAttributesRepository
									.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
											"rfTechnology", "LM", "A");
							if(Objects.nonNull(rfTechnology) && Objects.nonNull(rfTechnology.getAttributeValue()) && !rfTechnology.getAttributeValue().isEmpty()){
								deviceType = rfTechnology.getAttributeValue();
							}
						}

						Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceDetail.getScServiceDetailId());
						if(scServiceDetailOptional.isPresent()){
							ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
							if(!scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
								String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());
	
								String orderSubCategory = scServiceDetail.getOrderSubCategory();
								orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
								orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
								String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
	
								if(!orderType.equalsIgnoreCase("NEW") && !"ADD_SITE".equals(orderCategory)
										&& (Objects.isNull(orderSubCategory) ||
											(Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel")
													&& !orderSubCategory.toLowerCase().contains("bso")
													&& !orderSubCategory.toLowerCase().contains("shifting")))){
									logger.info("MACD RF");
									MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
									if(Objects.nonNull(mstRadwinDetails)){
										logger.info("RADWIN ORDER");
										LmComponent lmComponent = saveLastMile(serviceDetail, "RADWIN");
										saveMACDRadwinLastMile(lmComponent, cramerWimaxLastmile, serviceDetail, iasServiceMap);
									}
									MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
									if(Objects.nonNull(mstCambiumDetails)){
										logger.info("CAMBIUM ORDER");
										LmComponent lmComponent = saveLastMile(serviceDetail, "CAMBIUM");
										saveMACDCambiumLastmile(lmComponent, cramerWimaxLastmile, serviceDetail,iasServiceMap);
									}
									/*if (deviceType !=null && !deviceType.isEmpty() &&  "radwin".equalsIgnoreCase(deviceType)) {
										logger.info("RADWIN OPTIMUS ORDER");
										LmComponent lmComponent = saveLastMile(serviceDetail, deviceType);
										saveMACDRadwinLastMile(lmComponent, cramerWimaxLastmile, serviceDetail);
									} else if (deviceType !=null && !deviceType.isEmpty() && "cambium".equalsIgnoreCase(deviceType)) {
										logger.info("CAMBIUM OPTIMUS ORDER");
										LmComponent lmComponent = saveLastMile(serviceDetail, deviceType);
										saveMACDCambiumLastmile(lmComponent, cramerWimaxLastmile, serviceDetail);
									}else if ((deviceType !=null && !deviceType.isEmpty() && "wimax".equalsIgnoreCase(deviceType)) || (deviceType == null)) {
										logger.info("WIMAX OR DEVICE TYPE NOT EXISTS");
										MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
										if(Objects.nonNull(mstCambiumDetails)) {
											logger.info("CAMBIUM MIGRATED ORDER");
											LmComponent lmComponent = saveLastMile(serviceDetail, "CAMBIUM");
											saveMACDCambiumLastmile(lmComponent, cramerWimaxLastmile, serviceDetail);
										}else {
											MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
											if (Objects.nonNull(mstRadwinDetails)) {
												logger.info("RADWIN MIGRATED ORDER");
												LmComponent lmComponent = saveLastMile(serviceDetail, "RADWIN");
												saveMACDRadwinLastMile(lmComponent, cramerWimaxLastmile, serviceDetail);
											}
										}
									}*/
								}else{
									logger.info("NEW OR ADD SITE OR PARALLEL");
									if (deviceType !=null && !deviceType.isEmpty() &&  deviceType.toLowerCase().contains("radwin") && serviceDetail.getLastmileProvider()!=null && serviceDetail.getLastmileProvider().toLowerCase().contains("pmp")) {
										logger.info("Radwin deviceType");
										LmComponent lmComponent = saveLastMile(serviceDetail, deviceType);
										saveRadwinLastMile(lmComponent, cramerWimaxLastmile, serviceDetail, iasServiceMap);
									} else if (deviceType !=null && !deviceType.isEmpty() && (deviceType.toLowerCase().contains("cambium") || deviceType.toLowerCase().contains("wimax")
											|| deviceType.toLowerCase().contains("canopy"))) {
										if(deviceType.toLowerCase().contains("wimax")){
											deviceType="CAMBIUM";
										}
										logger.info("Cambium/Wimax deviceType");
										LmComponent lmComponent = saveLastMile(serviceDetail, deviceType);
										saveCambiumLastmile(lmComponent, cramerWimaxLastmile, serviceDetail, iasServiceMap);
									}
								}
							}
						}						
						/* else {
							LmComponent lmComponent = saveLastMile(serviceDetail, deviceType);
							saveWimaxLastmile(lmComponent, cramerWimaxLastmile);
						}*/
					} else {
						iasServiceMap.put("isWimaxLastMileExists", false);
					}
				} else {
					iasServiceMap.put("islastMileExists", false);
				}

				// multicasting related savings
				Boolean isMulticastingExists = (Boolean) iasServiceMap.get("isMulticastingExists");
				if (isMulticastingExists != null && isMulticastingExists) {
					logger.info("MultiCastExists from Exists from Cramer");
					iasServiceMap.put("multicasting", request.getService().getAddOnServices().getMulticasting());
					
					Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceDetail.getScServiceDetailId());
					if(scServiceDetailOptional.isPresent()){
						ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
						if(!scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
							String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());
							String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
							String orderSubCategory = scServiceDetail.getOrderSubCategory();
							orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
						    orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
							if(orderType.equalsIgnoreCase("NEW") || (orderType.equalsIgnoreCase("MACD") && (orderCategory.equalsIgnoreCase("ADD_SITE")
									|| (orderSubCategory!=null && orderSubCategory.toLowerCase().contains("parallel")))) ){
								logger.info("MultiCastExists for NEW or ADDSITE or Parallel");
								ScServiceAttribute multiCastExists = scServiceAttributeRepository
										.findFirstByScServiceDetail_idAndAttributeNameAndCategory(serviceDetail.getScServiceDetailId(), "isMultiCastExists",
												"GVPN Common");
								if(Objects.nonNull(multiCastExists) && Objects.nonNull(multiCastExists.getAttributeValue()) && !multiCastExists.getAttributeValue().isEmpty()
										&& "Yes".equalsIgnoreCase(multiCastExists.getAttributeValue())){
									logger.info("MultiCastExists for NEW or ADDSITE or Parallel from L2O for Service Id::{}",serviceDetail.getScServiceDetailId());
									saveMultiCastingForNewOrder((Multicasting) iasServiceMap.get("multicasting"),serviceDetail);
								}
							}
						}
					}else{
						logger.info("MultiCastExists for MACD");
						if(!"IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
							saveMultiCasting((Multicasting) iasServiceMap.get("multicasting"),serviceDetail);
						}
					}
				}

				// topology
				Topology topology = null;
				if ((Boolean) iasServiceMap.get("isTopologyInfoPresent")) {
					iasServiceMap.put("topologyInfo",
							request.getService().getService().getIasService().getPERouter().getTopologyInfo());
					topology = saveTopology(serviceDetail,
							(CramerEthernetTopologyInfo) iasServiceMap.get("topologyInfo"));
				}

				// uniswitch
				if ((Boolean) iasServiceMap.get("isUNISwitchPresent")) {
					iasServiceMap.put("uniswitch", request.getService().getService().getIasService().getUNISwitch());
					if ((Boolean) iasServiceMap.get("isTopologyInfoPresent")) {
						saveUniSwitchDetail(serviceDetail, (Switch) iasServiceMap.get("uniswitch"), topology);
					} /*
						 * else { saveUniSwitchDetail(serviceDetail, (Switch)
						 * iasServiceMap.get("uniswitch"), null); }
						 */
				}

				populateP2pCompAttr(request,iasServiceMap,serviceCode);

				// vrf - default entry insertions
				if ((Boolean) iasServiceMap.get("isPERouterExists")) {
					iasServiceMap.put("peRouter", request.getService().getService().getIasService().getPERouter());
					saveVrf(serviceDetail, (PERouter) iasServiceMap.get("peRouter"), iasServiceMap);

					// saving wan static routes
					// saveWanStaticRoutes(serviceDetail, (PERouter) iasServiceMap.get("peRouter"),
					// iasServiceMap);

				} else {
					saveVrf(serviceDetail, null, iasServiceMap);

					// saving wan static routes
					// saveWanStaticRoutes(serviceDetail, null, iasServiceMap);

				}
				
				if(CramerConstants.GVPN.equalsIgnoreCase(serviceDetail.getServiceType())){
					logger.info("GVPN METADATA");
					saveVpnMetaData(serviceDetail);
				}

				// saving RouterUplinkPort
				if ((Boolean) iasServiceMap.get("isRouterTopologyInterface1Present")
						&& (Boolean) iasServiceMap.get("isRouterTopologyInterface2Present")) {
					iasServiceMap.put("routerTopologyInterface1", request.getService().getService().getIasService()
							.getPERouter().getRouterTopologyInterface1());
					iasServiceMap.put("routerTopologyInterface2", request.getService().getService().getIasService()
							.getPERouter().getRouterTopologyInterface2());
					saveRouterUplinkPort((CramerEthernetInterface) iasServiceMap.get("routerTopologyInterface1"),
							(CramerEthernetInterface) iasServiceMap.get("routerTopologyInterface2"), topology);
				} else if ((Boolean) iasServiceMap.get("isRouterTopologyInterface1Present")) {
					iasServiceMap.put("routerTopologyInterface1", request.getService().getService().getIasService()
							.getPERouter().getRouterTopologyInterface1());
					saveRouterUplinkPort((CramerEthernetInterface) iasServiceMap.get("routerTopologyInterface1"), null,
							topology);
				} else {
					iasServiceMap.put("routerTopologyInterface2", request.getService().getService().getIasService()
							.getPERouter().getRouterTopologyInterface2());
					saveRouterUplinkPort(null, (CramerEthernetInterface) iasServiceMap.get("routerTopologyInterface2"),
							topology);
				}
				if(serviceDetail.getServiceType().equalsIgnoreCase("ILL")
						|| serviceDetail.getServiceType().equalsIgnoreCase("IAS")) {
					
					logger.info("service cos criteria for ILL with serviceCode not found => {}", serviceCode);

					
				
				saveServiceCosCritera(serviceDetail);
				}
				else if(serviceDetail.getServiceType().equalsIgnoreCase("GVPN")) {
					
					logger.info("service cos criteria for GVPN with serviceCode not found => {}", serviceCode);

					saveServiceCosCriteraForGvpn(serviceDetail);
				}
				
				if(!"IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
					List<ScServiceAttribute> attributes=	scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(), Arrays.asList("WAN IP Provided By","WAN IP Address"));
						
					
						if (!attributes.isEmpty()) {
		
							Map<String, String> map = new HashMap<>();
							attributes.stream().forEach(att -> {
								map.put(att.getAttributeName(), att.getAttributeValue());
							});
							if (map.containsKey("WAN IP Provided By")
									&& map.get("WAN IP Provided By").equalsIgnoreCase("CUSTOMER")
									&& map.containsKey("WAN IP Address")
									&& !org.apache.commons.lang.StringUtils.isBlank(map.get("WAN IP Address"))) {
								saveWanV4andV6CustomerProvided(serviceDetail, map.get("WAN IP Address"));
							}
						}
				}

				serviceDetailRepository.saveAndFlush(serviceDetail);

				logger.info("Service Activation - processCramerResponse - completed");

			} else {
				logger.error("serviceCode not found => {}", serviceCode);
			}
		} catch (Exception e) {
			logger.error("Exception in processCramerResponse => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return serviceCode;
	}


	private boolean validateRadwinSyIpExists(ServiceDetail serviceDetail, GetIPServiceInfoResponse request,
			Map<String, Object> iasServiceMap, boolean isRadwinSyIpExists[], List<String> radwinSyIpList) throws TclCommonException {
		logger.info("validateRadwinSyIpExists invoked");
		ScComponentAttribute rfMake = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
						"rfMake", "LM", "A");
		ScComponentAttribute lastMileProvider = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
						"lastMileProvider", "LM", "A");
		
		String deviceType = null;
		if (rfMake != null && Objects.nonNull(rfMake.getAttributeValue()) && !rfMake.getAttributeValue().isEmpty()) {
			deviceType = rfMake.getAttributeValue();
			logger.info("RFMake::{}",deviceType);
		}else{
			ScComponentAttribute rfTechnology = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"rfTechnology", "LM", "A");
			if(Objects.nonNull(rfTechnology) && Objects.nonNull(rfTechnology.getAttributeValue()) && !rfTechnology.getAttributeValue().isEmpty()){
				deviceType = rfTechnology.getAttributeValue();
				logger.info("RFTechnology::{}",deviceType);
			}
		}
		if (deviceType !=null && !deviceType.isEmpty() &&  "radwin".equalsIgnoreCase(deviceType) && Objects.nonNull(lastMileProvider) && Objects.nonNull(lastMileProvider.getAttributeValue()) && !lastMileProvider.getAttributeValue().isEmpty()
				&& lastMileProvider.getAttributeValue().toLowerCase().contains("radwin") && lastMileProvider.getAttributeValue().toLowerCase().contains("tcl")
				&& lastMileProvider.getAttributeValue().toLowerCase().contains("pop") && (Boolean) iasServiceMap.get("isWanV4AddressExists")) {
			logger.info("Cramer WANV4 exists");
			if (null != request.getService().getService().getIasService().getWanV4Address() && !request.getService().getService().getIasService().getWanV4Address().isEmpty()) {
				List<IPV4Address> ipv4AddrList = request.getService().getService().getIasService()
						.getWanV4Address();
				logger.info("Cramer WANV4 exists");
				ipv4AddrList.stream().filter(eachIp -> eachIp.getAddress()!=null && !eachIp.getAddress().isEmpty()).forEach(eachIp -> {
					if(eachIp.getAddress().contains("/")){
						logger.info("WanIPAdrress valid");
						String wanIp[]=eachIp.getAddress().split("/");
						if(wanIp.length==2 && Objects.nonNull(wanIp[1])){
							logger.info("Cramer WAN IP Exists");
							String wanIpSubnet=wanIp[1];
							logger.info("Cramer WAN IP SUBNET::{}",wanIpSubnet);
							if(Integer.valueOf(wanIpSubnet)==29){
								logger.info("Subnet matches");
								isRadwinSyIpExists[0]=true;
								radwinSyIpList.add(eachIp.getAddress());
							}
						}		
					}
				});
			}
		}
		return isRadwinSyIpExists[0];
		
	}


	private void applySecondaryWanIp(ServiceDetail serviceDetail, GetIPServiceInfoResponse request, ScComponentAttribute wanIpProvidedByCustAttribute, boolean isRadwinCustomerIpAssignedByTcl) {
		logger.info("applySecondaryWanIp invoked");
		if (null != request.getService().getService().getIasService().getWanV4Address()
				&& !request.getService().getService().getIasService().getWanV4Address().isEmpty()) {
			List<IPV4Address> ipv4AddrList = request.getService().getService().getIasService().getWanV4Address();
			logger.info("Cramer WANV4 exists");
			ipv4AddrList.stream().filter(eachIp -> eachIp.getAddress() != null && !eachIp.getAddress().isEmpty())
					.forEach(eachIp -> {
						try {
							if (eachIp.getAddress().contains("/")) {
								logger.info("WanIPAdrress valid");
								String wanIp[] = eachIp.getAddress().split("/");
								if (wanIp.length == 2 && Objects.nonNull(wanIp[1])) {
									logger.info("Cramer IP WANV4");
									String wanIpSubnet = wanIp[1];
									logger.info("Cramer WAN IP SUBNET::{}", wanIpSubnet);
									if (Integer.valueOf(wanIpSubnet) == 29) {
										logger.info("Subnet matches");
										saveSyIpaddrWanv4Address(serviceDetail, eachIp,wanIpProvidedByCustAttribute,isRadwinCustomerIpAssignedByTcl);
									}
								}

							}
						} catch (TclCommonException e) {
							logger.error("Exception while applySecondaryWanIp::{}", e);
						}
					});
		}
	}


	private void saveSyIpaddrWanv4Address(ServiceDetail serviceDetail, IPV4Address eachIp, ScComponentAttribute wanIpProvidedByCustAttribute, boolean isRadwinCustomerIpAssignedByTcl) throws TclCommonException {
		logger.info("saveSyIpaddrWanv4Address");
				logger.info("WanIPCust Provided");
				IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
				ipaddrWanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
				if((Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue()))
						|| isRadwinCustomerIpAssignedByTcl){
					logger.info("Radwin WanIPCust Provided by Cramer");
					ipaddrWanv4Address.setIssecondary((byte) 1);
				}else{
					logger.info("WanIPCust Provided No");
					ipaddrWanv4Address.setIssecondary((byte) 0);
				}
				ipaddrWanv4Address.setIscustomerprovided((byte) 0);
				logger.info("Sy Radwin Ip Address from Cramer::{}",eachIp.getAddress());
				ipaddrWanv4Address.setWanv4Address(eachIp.getAddress());
				ipaddrWanv4Address.setStartDate(startDate);
				ipaddrWanv4Address.setLastModifiedDate(lastModifiedDate);
				ipaddrWanv4Address.setModifiedBy(modifiedBy);
				ipaddrWanv4Address.setEndDate(endDate);
				ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
		logger.info("SyIpWanV4 saved successfully");
	}


	private void saveIpAddrWanv4CustomerProvided(ServiceDetail serviceDetail, ScComponentAttribute attribute, ScComponentAttribute wanIpProvidedByCustAttribute) throws TclCommonException {
		logger.info("saveIpAddrWanv4CustomerProvided");
		if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
				logger.info("WanIPCust Provided");
				IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
				ipaddrWanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
				ipaddrWanv4Address.setIssecondary((byte) 0);
				ipaddrWanv4Address.setIscustomerprovided((byte) 1);
				Boolean isOddIpAddressExists=false;
				isOddIpAddressExists=checkIpAddressEvenOrOdd(serviceDetail,attribute.getAttributeValue(),isOddIpAddressExists);
				if(isOddIpAddressExists) {
					logger.info("OddIpAddress exists for serviceId::{}",serviceDetail.getId());
					ipaddrWanv4Address.setWanv4Address(getIpAddressSplitForCustomerProvidedWan(attribute.getAttributeValue(),1)+"/"+subnet(attribute.getAttributeValue()).trim());
				}else {
					logger.info("OddIpAddress Not exists for serviceId::{}",serviceDetail.getId());
					ipaddrWanv4Address.setWanv4Address(getIpAddressSplitForCustomerProvidedWan(attribute.getAttributeValue(),2)+"/"+subnet(attribute.getAttributeValue()).trim());
				}
				ipaddrWanv4Address.setStartDate(startDate);
				ipaddrWanv4Address.setLastModifiedDate(lastModifiedDate);
				ipaddrWanv4Address.setModifiedBy(modifiedBy);
				ipaddrWanv4Address.setEndDate(endDate);
				ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
				logger.info("IpWanV4 saved successfully");
		}
	}
	
	private void saveIpAddrWanv4CustomerProvidedForIZOPC(ServiceDetail serviceDetail, ScComponentAttribute attribute, ScComponentAttribute wanIpProvidedByCustAttribute, Map<String, Object> iasServiceMap, GetIPServiceInfoResponse request) throws TclCommonException {
		logger.info("saveIpAddrWanv4CustomerProvidedForIZOPC");
		if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
				logger.info("saveIpAddrWanv4CustomerProvidedForIZOPC.WanIPCust Provided");
				IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
				ipaddrWanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
				ipaddrWanv4Address.setIssecondary((byte) 0);
				/*
				 * if("Secondary".equalsIgnoreCase(serviceDetail.getRedundancyRole())){
				 * ipaddrWanv4Address.setIssecondary((byte) 1); }
				 */
				ipaddrWanv4Address.setIscustomerprovided((byte) 1);
				ipaddrWanv4Address.setWanv4Address(attribute.getAttributeValue());
				ipaddrWanv4Address.setStartDate(startDate);
				ipaddrWanv4Address.setLastModifiedDate(lastModifiedDate);
				ipaddrWanv4Address.setModifiedBy(modifiedBy);
				ipaddrWanv4Address.setEndDate(endDate);
				ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
				logger.info("saveIpAddrWanv4CustomerProvidedForIZOPC.IpWanV4 saved successfully");
		}else if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) 
				&& "No".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue()) && (Boolean) iasServiceMap.get("isWanV4AddressExists")
				&& null != request.getService().getService().getIasService().getWanV4Address()
				&& !request.getService().getService().getIasService().getWanV4Address().isEmpty()) {
			logger.info("saveIpAddrWanv4CustomerProvidedForIZOPC.WanIPTcl Provided");
			IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
			ipaddrWanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
			ipaddrWanv4Address.setIssecondary((byte) 0);
			/*
			 * if("Secondary".equalsIgnoreCase(serviceDetail.getRedundancyRole())){
			 * ipaddrWanv4Address.setIssecondary((byte) 1); }
			 */
			ipaddrWanv4Address.setIscustomerprovided((byte) 0);
			IPV4Address ipv4Address = request.getService().getService().getIasService().getWanV4Address().stream()
					.findFirst().orElse(null);
			if (ipv4Address != null && ipv4Address.getAddress() != null) {
				ipaddrWanv4Address.setWanv4Address(ipv4Address.getAddress());
			}
			ipaddrWanv4Address.setStartDate(startDate);
			ipaddrWanv4Address.setLastModifiedDate(lastModifiedDate);
			ipaddrWanv4Address.setModifiedBy(modifiedBy);
			ipaddrWanv4Address.setEndDate(endDate);
			ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
			logger.info("saveIpAddrWanv4CustomerProvidedForIZOPC.IpTclWanV4 saved successfully");
		}
	}


	private com.tcl.dias.serviceactivation.entity.entities.Multicasting saveMultiCastingForNewOrder(Multicasting multicastInput, ServiceDetail serviceDetail)
			throws TclCommonException {
		logger.info("Service Activation - saveMultiCasting - started");
		try {
			if (multicastInput.getDataMDT() != null || multicastInput.getDefaultMDT() != null) {
				com.tcl.dias.serviceactivation.entity.entities.Multicasting multiCastingEntity = new com.tcl.dias.serviceactivation.entity.entities.Multicasting();
				multiCastingEntity.setDefaultMdt(StringUtils.trimToNull(multicastInput.getDefaultMDT()));
				multiCastingEntity.setDataMdt(StringUtils.trimToNull(multicastInput.getDataMDT()));
				multiCastingEntity.setWanPimMode("SPARSE");

				ScServiceAttribute rpAddressAttr = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeNameAndCategory(serviceDetail.getScServiceDetailId(),
								"rpAddress", "GVPN Common");
				ScServiceAttribute multiCastTypeAttr = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeNameAndCategory(serviceDetail.getScServiceDetailId(),
								"multiCastType", "GVPN Common");
				if (Objects.nonNull(rpAddressAttr) && Objects.nonNull(rpAddressAttr.getAttributeValue())
						&& !rpAddressAttr.getAttributeValue().isEmpty()) {
					logger.info("RP ADDRESS EXISTS");
					multiCastingEntity.setRpAddress(rpAddressAttr.getAttributeValue());
				}

				if (Objects.nonNull(multiCastTypeAttr) && Objects.nonNull(multiCastTypeAttr.getAttributeValue())
						&& !multiCastTypeAttr.getAttributeValue().isEmpty()) {
					logger.info("TYPE EXISTS");
					multiCastingEntity.setType(multiCastTypeAttr.getAttributeValue());
				} else {
					multiCastingEntity.setType("STATICRP");
				}

				multiCastingEntity.setDataMdtThreshold("1");
				multiCastingEntity.setRpLocation("CE");
				multiCastingEntity.setAutoDiscoveryOption("MDT_SAFI");
				multiCastingEntity.setStartDate(startDate);
				multiCastingEntity.setLastModifiedDate(lastModifiedDate);
				multiCastingEntity.setModifiedBy(modifiedBy);
				multiCastingEntity.setEndDate(endDate);
				multiCastingEntity.setServiceDetail(serviceDetail);
				multiCastingEntity = multicastingRepository.saveAndFlush(multiCastingEntity);
				logger.info("Service Activation - saveMultiCasting - completed");
				return multiCastingEntity;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Exception in saveMultiCasting => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void populateP2pCompAttr(GetIPServiceInfoResponse request, Map<String, Object> iasServiceMap, String serviceCode) throws TclCommonException {
		try {
			Map<String, String> p2pCompAttr = new HashMap<>();

			ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);
			if(!"IZOPC".equalsIgnoreCase(serviceDetails.getProductName()) && Objects.nonNull(serviceDetails.getLastmileType()) && !serviceDetails.getLastmileType().isEmpty() &&
					(serviceDetails.getLastmileType().equalsIgnoreCase("OnnetRf") || serviceDetails.getLastmileType().equalsIgnoreCase("Onnet Wireless"))){
				logger.info("OnnetRF NEW/MACD");
				ScServiceAttribute feasibilitySolutionTypeAttributes = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeNameAndCategory(serviceDetails.getScServiceDetailId(), "closest_provider_bso_name",
								"FEASIBILITY");
				if(Objects.nonNull(feasibilitySolutionTypeAttributes) && Objects.nonNull(feasibilitySolutionTypeAttributes.getAttributeValue())
						&& !feasibilitySolutionTypeAttributes.getAttributeValue().isEmpty() && !feasibilitySolutionTypeAttributes.getAttributeValue().toLowerCase().contains("pmp"))
				{
					logger.info("P2P Component Attributes");
					ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
					if(Objects.nonNull(scServiceDetail)){
						ScComponent currentScComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceDetails.getScServiceDetailId(),"LM","A");
						if(Objects.nonNull(currentScComponent) && Objects.nonNull(currentScComponent.getScComponentAttributes()) && !currentScComponent.getScComponentAttributes().isEmpty()){
							logger.info("CurrentScComp & Attr Exists");

							if (((Boolean) iasServiceMap.get("isUNISwitchPresent")) && ((Boolean) iasServiceMap.get("isTopologyInfoPresent"))) {
								Switch switchDetail = request.getService().getService().getIasService().getUNISwitch();
								if (switchDetail != null && switchDetail.getHostname() != null) {
									p2pCompAttr.put("DataVLAN", StringUtils.trimToNull(switchDetail.getUNIinterface().getVlan()));
									mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"data_vlan",p2pCompAttr.get("DataVLAN"));
									logger.info("P2P DataVLANComponent Attribute {}", p2pCompAttr.get("DataVLAN"));
								}
							}

							if (((Boolean) iasServiceMap.get("islastMileExists")) && ((Boolean) iasServiceMap.get("isWimaxLastMileExists"))) {
								CramerWimaxLastmile cramerWimaxLastmile = request.getService().getLastmile().getWimaxLastMile();
								if (cramerWimaxLastmile != null) {
									p2pCompAttr.put("ManagementVLAN", StringUtils.trimToNull(cramerWimaxLastmile.getManagementVID()));
									mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"mgmt_vlan",p2pCompAttr.get("ManagementVLAN"));
									logger.info("P2P ManagementVLANComponent Attribute {}", p2pCompAttr.get("ManagementVLAN"));
								}
							}
							
							if((Boolean) iasServiceMap.get("isPERouterExists")){
								if(Objects.nonNull(request) && Objects.nonNull(request.getService()) && Objects.nonNull(request.getService().getService())
										&& Objects.nonNull(request.getService().getService().getIasService()) && Objects.nonNull(request.getService().getService().getIasService().getPERouter())){
									PERouter pRouter=request.getService().getService().getIasService().getPERouter();
									if(Objects.nonNull(pRouter.getHostname())){
										logger.info("PEHostName exists");
										mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"peHostname",pRouter.getHostname());
									}else{
										logger.info("PEHostName doesn't exists");
										mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"peHostname",null);
									}
									
									if(Objects.nonNull(pRouter.getV4ManagementIPAddress())){
										logger.info("PEIp exists");
										mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"peIp",pRouter.getV4ManagementIPAddress());
									}else{
										logger.info("PEIp doesn't exists");
										mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"peIp",null);
									}
								}
							}

						}
					}
				}
			}


		} catch (Exception e) {
			logger.error("Exception in populateP2pCompAttr => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}





	private CambiumLastmile saveMACDCambiumLastmile(LmComponent lmComponent, CramerWimaxLastmile cramerWimaxLastmile,
			ServiceDetail serviceDetail, Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveMACDCambiumLastmile - started");
		try {
			MstCambiumDetails mstCambiumDetails =mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstCambiumDetails)){
				CambiumLastmile cambiumLastmile = new CambiumLastmile();
				cambiumLastmile.setBsIp(mstCambiumDetails.getBtsIp());
				cambiumLastmile.setBsName(mstCambiumDetails.getBtsName());
				cambiumLastmile.setMgmtIpForSsSu(mstCambiumDetails.getHsuIp());
				cambiumLastmile.setMgmtIpGateway(Objects.nonNull(cambiumLastmile.getMgmtIpForSsSu()) && !cambiumLastmile.getMgmtIpForSsSu().isEmpty() ?getManagementIpGateway(cambiumLastmile.getMgmtIpForSsSu()):cambiumLastmile.getMgmtIpForSsSu());
				cambiumLastmile.setMgmtSubnetForSsSu("255.255.255.0");
				if ((Boolean) iasServiceMap.get("isUNISwitchPresent") && iasServiceMap.get("uniswitch") !=null) {
					logger.info("Cramer Data Vlan exists");
					Switch uniSwitch = (Switch) iasServiceMap.get("uniswitch");
					cambiumLastmile.setDefaultPortVid(null != uniSwitch.getUNIinterface()
							? StringUtils.trimToNull(uniSwitch.getUNIinterface().getVlan())
							: null);
				}
				if(cambiumLastmile.getDefaultPortVid()==null || cambiumLastmile.getDefaultPortVid().isEmpty()){
					logger.info("Cramer Data Vlan not exists");
					cambiumLastmile.setDefaultPortVid(Objects.nonNull(mstCambiumDetails.getDataVlan()) && !mstCambiumDetails.getDataVlan().isEmpty()?mstCambiumDetails.getDataVlan():null);
				}
				cambiumLastmile.setSmHeight(mstCambiumDetails.getAntennaHeight());
				if(Objects.nonNull(mstCambiumDetails.getSectorId())){
					logger.info("Color Code exists:: {}",mstCambiumDetails.getSectorId());
					if(mstCambiumDetails.getSectorId().contains("_")){
						String sectors[]=mstCambiumDetails.getSectorId().split("_");
						if(sectors.length>1 && Objects.nonNull(sectors[1])){
							cambiumLastmile.setColorCode1(sectors[1]);
						}
					}else{
						cambiumLastmile.setColorCode1(mstCambiumDetails.getSectorId());
					}
				}else{
					logger.info("Color Code doesn't exists");
					cambiumLastmile.setColorCode1("1");
				}
				cambiumLastmile.setLongitudeSettings(mstCambiumDetails.getLongitude());
				cambiumLastmile.setLatitudeSettings(mstCambiumDetails.getLatitude());
				if(Objects.nonNull(mstCambiumDetails.getFrequency()) && !mstCambiumDetails.getFrequency().isEmpty()){
					cambiumLastmile.setCustomRadioFrequencyList(mstCambiumDetails.getFrequency().replace(".", ""));
				}
				cambiumLastmile.setMgmtVid(cramerWimaxLastmile.getManagementVID());
                if(Objects.nonNull(mstCambiumDetails.getHsuMac()) && !mstCambiumDetails.getHsuMac().isEmpty()){
                    cambiumLastmile.setSuMacAddress(mstCambiumDetails.getHsuMac().toLowerCase());
                }
				cambiumLastmile.setDeviceType(mstCambiumDetails.getDeviceType());
				cambiumLastmile.setLmComponent(lmComponent);
				cambiumLastmile = cambiumLastmileRepository.saveAndFlush(cambiumLastmile);
				logger.info("Service Activation - saveMACDCambiumLastMile - completed");
				return cambiumLastmile;
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception in saveMACDCambiumLastmile => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}






	private RadwinLastmile saveMACDRadwinLastMile(LmComponent lmComponent, CramerWimaxLastmile cramerWimaxLastmile,
			ServiceDetail serviceDetail, Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveMACDRadwinLastMile - started");
		try {
			MstRadwinDetails mstRadwinDetails =mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstRadwinDetails)){
				RadwinLastmile radwinLastmile = new RadwinLastmile();
				radwinLastmile.setBtsIp(mstRadwinDetails.getBtsIp());
				radwinLastmile.setBtsName(mstRadwinDetails.getBtsName());
				radwinLastmile.setSectorId(mstRadwinDetails.getSectorId());	
				radwinLastmile.setHsuIp(mstRadwinDetails.getHsuIp());
				radwinLastmile.setGatewayIp(Objects.nonNull(radwinLastmile.getHsuIp()) && !radwinLastmile.getHsuIp().isEmpty() ?getManagementIpGateway(radwinLastmile.getHsuIp()):radwinLastmile.getHsuIp());
				radwinLastmile.setHsuSubnet("255.255.255.0");
				if ((Boolean) iasServiceMap.get("isUNISwitchPresent") && iasServiceMap.get("uniswitch") != null) {
					Switch uniSwitch = (Switch) iasServiceMap.get("uniswitch");
					radwinLastmile.setDataVlanid(null != uniSwitch.getUNIinterface()
							? StringUtils.trimToNull(uniSwitch.getUNIinterface().getVlan()) : null);
				} else
					radwinLastmile.setDataVlanid(mstRadwinDetails.getDataVlan());
				radwinLastmile.setSiteLong(mstRadwinDetails.getLongitude());
				radwinLastmile.setSiteLat(mstRadwinDetails.getLatitude());
				if(mstRadwinDetails.getFrequency()!=null && !mstRadwinDetails.getFrequency().isEmpty()){
					radwinLastmile.setFrequency(mstRadwinDetails.getFrequency().replace(".", ""));
				}
				radwinLastmile.setMgmtVlanid(cramerWimaxLastmile.getManagementVID());

				if(Objects.nonNull(mstRadwinDetails.getHsuMac()) && !mstRadwinDetails.getHsuMac().isEmpty()){
					radwinLastmile.setHsuMacAddr(mstRadwinDetails.getHsuMac().toLowerCase()); }
				radwinLastmile.setLmComponent(lmComponent);
				radwinLastmile = radwinLastmileRepository.saveAndFlush(radwinLastmile);
				logger.info("Service Activation - saveMACDRadwinLastMile - completed");
				return radwinLastmile;
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception in saveMACDRadwinLastMile => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}






	private void saveServiceCosCriteraForGvpn(ServiceDetail serviceDetail) {
		
		List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameInAndCategory(serviceDetail.getScServiceDetailId(),
						Arrays.asList("cos 1", "cos 2", "cos 6", "cos 5", "cos 3", "cos 4"), "GVPN Common");
		
		logger.info("saveServiceCosCriteraForGvpn for GVPN with scServiceAttributes :{}and serviceCode:{} ",
				scServiceAttributes.size(),serviceDetail.getServiceId());

		Map<String, String> cosMap=new TreeMap<>();
		
		
		
		scServiceAttributes.stream().forEach( scAttr->{
			if(scAttr.getAttributeValue()!=null) {
				cosMap.put(scAttr.getAttributeName(), scAttr.getAttributeValue().replaceAll("%", ""));
			}
		});
		
		logger.info("saveServiceCosCriteraForGvpn for GVPN with map :{}and serviceCode:{} ",
				cosMap,serviceDetail.getServiceId());
		
		Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceDetail.getScServiceDetailId());
		if(scServiceDetailOptional.isPresent()){
			ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());
			String orderSubCategory = scServiceDetail.getOrderSubCategory();
			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());

			if(!orderType.equalsIgnoreCase("NEW") && !"ADD_SITE".equals(orderCategory)
					&& (Objects.isNull(orderSubCategory) || Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel"))){
				saveMACDCosCriteria(serviceDetail,cosMap);
			}else{
				saveCosCriteria(serviceDetail,cosMap);
			}
		}
		
		
		
	}

	private void saveMACDCosCriteria(ServiceDetail serviceDetail, Map<String, String> cosMap) {
		logger.info("saveMACDCosCriteria");
		ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(serviceDetail.getScServiceDetailId(), "Service Variant");

		ServiceQo serviceQo = new ServiceQo();
		serviceQo.setCosType("6COS");
		if (scServiceAttribute != null) {
			serviceQo.setCosProfile(
					scServiceAttribute.getAttributeValue().equalsIgnoreCase("Enhanced") ? "PREMIUM" : "STANDARD");
		}
		ServiceDetail activeServiceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceDetail.getServiceId(),"ACTIVE");
		if(Objects.nonNull(activeServiceDetail) && Objects.nonNull(activeServiceDetail.getMulticastings()) 
				&& !activeServiceDetail.getMulticastings().isEmpty()){
			logger.info("MultiCastExists for saveCosCriteria");
			serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.BOTH);
		}else{
			logger.info("MultiCast doesn't exists for saveCosCriteria");
			serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.UNICAST);
		}
		serviceQo.setNcTraffic((byte) 0);
		serviceQo.setServiceDetail(serviceDetail);
		serviceQo.setCosPackage("");
		serviceQo.setStartDate(startDate);
		serviceQo.setLastModifiedDate(lastModifiedDate);
		serviceQo.setModifiedBy(modifiedBy);
		serviceQoRepository.saveAndFlush(serviceQo);
		
		List<ScServiceAttribute> cosCriteriaClassification = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameInAndCategory(serviceDetail.getScServiceDetailId(),
						Arrays.asList("cos 1 criteria","cos 2 criteria", "cos 6 criteria", "cos 5 criteria", "cos 3 criteria", "cos 4 criteria","cos 1 criteria value", "cos 2 criteria value", "cos 6 criteria value", "cos 5 criteria value", "cos 3 criteria value", "cos 4 criteria value"), "GVPN Common");

		logger.info("saveServiceCosCriteraForGvpn for GVPN with cosCriteriaClassification :{}and serviceCode:{} ",
				cosCriteriaClassification.size(),serviceDetail.getServiceId());
		
		Map<String, String> cosCriteraMap=commonFulfillmentUtils.getServiceAttributesAttributes(cosCriteriaClassification);
		
		logger.info("saveServiceCosCriteraForGvpn for GVPN with cosCriteraMap :{}and serviceCode:{} ",
				cosCriteraMap,serviceDetail.getServiceId());
		
		logger.info("saveServiceCosCriteraForGvpn for GVPN with getServiceBandwidthUnit :{}and serviceCode:{} ",
				serviceDetail.getServiceBandwidthUnit(),serviceDetail.getServiceId());
		if (serviceDetail.getServiceBandwidthUnit() != null) {

			cosMap.forEach((key, value) -> {

				if (!value.isEmpty() && Integer.valueOf(value) > 0) {
					ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
					serviceCosCriteria.setCosPercent(value);
					serviceCosCriteria.setServiceQo(serviceQo);
					serviceCosCriteria.setCosName(key.toUpperCase().replaceAll(" ", ""));
					serviceCosCriteria.setClassificationCriteria(
							getClassificationCriteria(cosCriteraMap, serviceCosCriteria.getCosName()));

					if (serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase("ipprecedence")) {

						String criteraiValue = getCriteriaValue(cosCriteraMap, serviceCosCriteria.getCosName());
						logger.info("saveServiceCosCriteraForGvpn for GVPN with criteraiValue :{}and serviceCode:{} ",
								criteraiValue,serviceDetail.getServiceId());
						
						try {

						if (criteraiValue != null) {
							String values[] = criteraiValue.split(",");
							logger.info("saveServiceCosCriteraForGvpn for GVPN with values[] :{}and serviceCode:{} ",
									values,serviceDetail.getServiceId());
							int length = values.length;
							if (length >= 1) {
								serviceCosCriteria.setIpprecedenceVal1(values[0]);
							}
							if (length >= 2) {
								serviceCosCriteria.setIpprecedenceVal2(values[1]);
							}
							if (length >= 3) {
								serviceCosCriteria.setIpprecedenceVal3(values[2]);
							}
							if (length >= 4) {
								serviceCosCriteria.setIpprecedenceVal4(values[3]);
							}
							if (length >= 5) {
								serviceCosCriteria.setIpprecedenceVal5(values[4]);
							}
							if (length >= 6) {
								serviceCosCriteria.setIpprecedenceVal6(values[5]);
							}
							if (length >= 7) {
								serviceCosCriteria.setIpprecedenceVal7(values[6]);
							}
							if (length >= 8) {
								serviceCosCriteria.setIpprecedenceVal8(values[7]);
							}
						}
						}
						catch (Exception e) {
							logger.error("error in capturing cos value:{}",e);
						}

					} else if (serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase("DSCP")) {

						String criteraiValue = getCriteriaValue(cosCriteraMap, serviceCosCriteria.getCosName());
						logger.info("saveServiceCosCriteraForGvpn for GVPN with dhcp criteraiValue[] :{}and serviceCode:{} ",
								criteraiValue,serviceDetail.getServiceId());
						
						try {

						if (criteraiValue != null) {
							String values[] = criteraiValue.split(",");
							logger.info("saveServiceCosCriteraForGvpn for GVPN with dhcp values[] :{}and serviceCode:{} ",
									values,serviceDetail.getServiceId());
							int length = values.length;
							if (length >= 1) {
								serviceCosCriteria.setDhcpVal1(values[0]);
							}
							if (length >= 2) {

								serviceCosCriteria.setDhcpVal2(values[1]);
							}
							if (length >= 3) {

								serviceCosCriteria.setDhcpVal3(values[2]);
							}
							if (length >= 4) {

								serviceCosCriteria.setDhcpVal4(values[3]);
							}
							if (length >= 5) {

								serviceCosCriteria.setDhcpVal5(values[4]);
							}
							if (length >= 6) {

								serviceCosCriteria.setDhcpVal6(values[5]);
							}
							if (length >= 7) {

								serviceCosCriteria.setDhcpVal7(values[6]);
							}
							if (length >= 8) {

								serviceCosCriteria.setDhcpVal8(values[7]);
							}

						}
						}
						catch (Exception e) {
							logger.error("error in capturing cos dhcp value:{}",e);

						}

						
					}
					
					Float serviceBandwidth = serviceDetail.getServiceBandwidth();
					serviceCosCriteria.setServiceQo(serviceQo);
					String serviceBandwidthUnit = serviceDetail.getServiceBandwidthUnit();
					if(Objects.nonNull(serviceDetail.getBurstableBw()) && Objects.nonNull(serviceDetail.getBurstableBwUnit())){
						serviceCosCriteria.setBwBpsunit(
								String.valueOf(banwidthConversion(serviceDetail.getBurstableBwUnit(), serviceDetail.getBurstableBw())
										* Integer.valueOf(value) / 100));
					}else if (serviceBandwidth != null) {
							serviceCosCriteria.setBwBpsunit(
									String.valueOf(banwidthConversion(serviceBandwidthUnit, serviceBandwidth)
											* Integer.valueOf(value) / 100));
					}
					
					serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
					if (serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase("IP Address")) {

						String criteraiValue = getCriteriaValue(cosCriteraMap, serviceCosCriteria.getCosName());
						logger.info("saveServiceCosCriteraForGvpn for GVPN with criteraiValue :{}and serviceCode:{} ",
								criteraiValue, serviceDetail.getServiceId());

						if (criteraiValue != null) {
							String values[] = criteraiValue.split(",");
							logger.info(
									"saveServiceCosCriteraForGvpn IP Address GVPN with values[] :{}and serviceCode:{} ",
									values, serviceDetail.getServiceId());
							AtomicInteger atomicInteger = new AtomicInteger(0);
							

							if (values.length > 0) {
							
								
								getUniqueValue(Arrays.asList(values)).forEach(ipAddress -> {
									atomicInteger.incrementAndGet();

									AclPolicyCriteria acp = new AclPolicyCriteria();
									acp.setAction("PERMIT");
									acp.setDestinationAny((byte) 1);
									acp.setDestinationSubnet("ANY");
									acp.setSourceAny(null);
									acp.setSourceSubnet(ipAddress);
									acp.setSequence(String.valueOf(getCosName(serviceCosCriteria.getCosName())));
									acp.setProtocol("IP");
									acp.setServiceCosCriteria(serviceCosCriteria);
									acp.setIssvcQosCoscriteriaIpaddrPreprovisioned((byte) 0);
									acp.setIssvcQosCoscriteriaIpaddrAcl((byte) 1);
									Integer legId = 0;

									if (serviceDetail.getVpnMetatDatas() != null) {
										VpnMetatData vpnMetatData = serviceDetail.getVpnMetatDatas().stream()
												.findFirst().orElse(null);
										if (vpnMetatData != null) {
											legId = vpnMetatData.getVpnLegId();
										}
									}
									acp.setIssvcQosCoscriteriaIpaddrAclName(serviceCosCriteria.getCosName() + "_"
											+ serviceDetail.getServiceId() + "_" + legId + "_IN");
									acp.setModifiedBy("OPTIMUS_RULE");

									acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
									aclPolicyCriteriaRepository.save(acp);
									AclPolicyCriteria acpSecond = new AclPolicyCriteria();
									acpSecond.setIssvcQosCoscriteriaIpaddrAcl((byte) 1);
									acpSecond.setAction("PERMIT");
									acpSecond.setDestinationAny(null);
									acpSecond.setDestinationSubnet(ipAddress);
									acpSecond.setSourceAny((byte) 1);
									acpSecond.setSourceSubnet("ANY");
									acpSecond.setSequence(String.valueOf(
											getCosName(serviceCosCriteria.getCosName()) + atomicInteger.get()));
									acpSecond.setProtocol("IP");
									acpSecond.setServiceCosCriteria(serviceCosCriteria);
									acpSecond.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
									acpSecond.setModifiedBy("OPTIMUS_RULE");
									acpSecond.setIssvcQosCoscriteriaIpaddrPreprovisioned((byte) 0);
									acpSecond.setIssvcQosCoscriteriaIpaddrAclName(serviceCosCriteria.getCosName() + "_"
											+ serviceDetail.getServiceId() + "_" + legId + "_IN");
									aclPolicyCriteriaRepository.save(acpSecond);

								});
							}
						}

					}

				}
			});
		}

	}


	private List<String> getUniqueValue(List<String> ipAddress) {
		List<String> uniqueList = new ArrayList<>();
		if (ipAddress != null && !ipAddress.isEmpty()) {
			ipAddress.stream().forEach(val -> {
				if (!uniqueList.contains(val)) {
					uniqueList.add(val);
				}
			});


		}
		return uniqueList;


	}


	private void saveCosCriteria(ServiceDetail serviceDetail, Map<String, String> cosMap) {

		ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(serviceDetail.getScServiceDetailId(), "Service Variant");

		ServiceQo serviceQo = new ServiceQo();
		serviceQo.setCosType("6COS");
		if (scServiceAttribute != null) {
			serviceQo.setCosProfile(
					scServiceAttribute.getAttributeValue().equalsIgnoreCase("Enhanced") ? "PREMIUM" : "STANDARD");
		}
		ScServiceAttribute multiCastExists = scServiceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameAndCategory(serviceDetail.getScServiceDetailId(), "isMultiCastExists",
						"GVPN Common");
		if(Objects.nonNull(multiCastExists) && Objects.nonNull(multiCastExists.getAttributeValue()) && !multiCastExists.getAttributeValue().isEmpty()
				&& "Yes".equalsIgnoreCase(multiCastExists.getAttributeValue())){
			logger.info("MultiCastExists for saveCosCriteria");
			serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.BOTH);
		}else{
			logger.info("MultiCast doesn't exists for saveCosCriteria");
			serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.UNICAST);
		}
		serviceQo.setSummationOfBw("0.0"); //TODO Convert into KBPS
		serviceQo.setNcTraffic((byte) 0);
		serviceQo.setServiceDetail(serviceDetail);
		serviceQo.setCosPackage("");
		serviceQo.setStartDate(startDate);
		serviceQo.setLastModifiedDate(lastModifiedDate);
		serviceQo.setModifiedBy(modifiedBy);
		serviceQoRepository.saveAndFlush(serviceQo);
		
		List<ScServiceAttribute> cosCriteriaClassification = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameInAndCategory(serviceDetail.getScServiceDetailId(),
						Arrays.asList("cos 1 criteria","cos 2 criteria", "cos 6 criteria", "cos 5 criteria", "cos 3 criteria", "cos 4 criteria","cos 1 criteria value", "cos 2 criteria value", "cos 6 criteria value", "cos 5 criteria value", "cos 3 criteria value", "cos 4 criteria value"), "GVPN Common");

		logger.info("saveServiceCosCriteraForGvpn for GVPN with cosCriteriaClassification :{}and serviceCode:{} ",
				cosCriteriaClassification.size(),serviceDetail.getServiceId());
		
		Map<String, String> cosCriteraMap=commonFulfillmentUtils.getServiceAttributesAttributes(cosCriteriaClassification);
		
		logger.info("saveServiceCosCriteraForGvpn for GVPN with cosCriteraMap :{}and serviceCode:{} ",
				cosCriteraMap,serviceDetail.getServiceId());
		
		logger.info("saveServiceCosCriteraForGvpn for GVPN with getServiceBandwidthUnit :{}and serviceCode:{} ",
				serviceDetail.getServiceBandwidthUnit(),serviceDetail.getServiceId());
		if (serviceDetail.getServiceBandwidthUnit() != null) {

			cosMap.forEach((key, value) -> {

				if (StringUtils.isNotEmpty(value) && Integer.valueOf(value) > 0) {
					ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
					serviceCosCriteria.setCosPercent(value);
					serviceCosCriteria.setServiceQo(serviceQo);
					serviceCosCriteria.setCosName(key.toUpperCase().replaceAll(" ", ""));
					serviceCosCriteria.setClassificationCriteria(
							getClassificationCriteria(cosCriteraMap, serviceCosCriteria.getCosName()));

					if (serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase("ipprecedence")) {

						String criteraiValue = getCriteriaValue(cosCriteraMap, serviceCosCriteria.getCosName());
						logger.info("saveServiceCosCriteraForGvpn for GVPN with criteraiValue :{}and serviceCode:{} ",
								criteraiValue,serviceDetail.getServiceId());
						
						try {

						if (criteraiValue != null) {
							String values[] = criteraiValue.split(",");
							logger.info("saveServiceCosCriteraForGvpn for GVPN with values[] :{}and serviceCode:{} ",
									values,serviceDetail.getServiceId());
							int length = values.length;
							if (length >= 1) {
								serviceCosCriteria.setIpprecedenceVal1(values[0]);
							}
							if (length >= 2) {
								serviceCosCriteria.setIpprecedenceVal2(values[1]);
							}
							if (length >= 3) {
								serviceCosCriteria.setIpprecedenceVal3(values[2]);
							}
							if (length >= 4) {
								serviceCosCriteria.setIpprecedenceVal4(values[3]);
							}
							if (length >= 5) {
								serviceCosCriteria.setIpprecedenceVal5(values[4]);
							}
							if (length >= 6) {
								serviceCosCriteria.setIpprecedenceVal6(values[5]);
							}
							if (length >= 7) {
								serviceCosCriteria.setIpprecedenceVal7(values[6]);
							}
							if (length >= 8) {
								serviceCosCriteria.setIpprecedenceVal8(values[7]);
							}
						}
						}
						catch (Exception e) {
							logger.error("error in capturing cos value:{}",e);
						}

					} else if (serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase("DSCP")) {

						String criteraiValue = getCriteriaValue(cosCriteraMap, serviceCosCriteria.getCosName());
						logger.info("saveServiceCosCriteraForGvpn for GVPN with dhcp criteraiValue[] :{}and serviceCode:{} ",
								criteraiValue,serviceDetail.getServiceId());
						
						try {

						if (criteraiValue != null) {
							String values[] = criteraiValue.split(",");
							logger.info("saveServiceCosCriteraForGvpn for GVPN with dhcp values[] :{}and serviceCode:{} ",
									values,serviceDetail.getServiceId());
							int length = values.length;
							if (length >= 1) {
								serviceCosCriteria.setDhcpVal1(values[0]);
							}
							if (length >= 2) {

								serviceCosCriteria.setDhcpVal2(values[1]);
							}
							if (length >= 3) {

								serviceCosCriteria.setDhcpVal3(values[2]);
							}
							if (length >= 4) {

								serviceCosCriteria.setDhcpVal4(values[3]);
							}
							if (length >= 5) {

								serviceCosCriteria.setDhcpVal5(values[4]);
							}
							if (length >= 6) {

								serviceCosCriteria.setDhcpVal6(values[5]);
							}
							if (length >= 7) {

								serviceCosCriteria.setDhcpVal7(values[6]);
							}
							if (length >= 8) {

								serviceCosCriteria.setDhcpVal8(values[7]);
							}

						}
						}
						catch (Exception e) {
							logger.error("error in capturing cos dhcp value:{}",e);

						}

						
					}
					Float serviceBandwidth = serviceDetail.getServiceBandwidth();
					serviceCosCriteria.setServiceQo(serviceQo);
					String serviceBandwidthUnit = serviceDetail.getServiceBandwidthUnit();
					if(serviceQo!=null && serviceQo.getIsflexicos()!=null && serviceQo.getIsflexicos()==1
							&& serviceQo.getSummationOfBw()!=null && !serviceQo.getSummationOfBw().isEmpty()){
						logger.info("BwBpsUnit by FlexiCos");
						serviceCosCriteria.setBwBpsunit(String.valueOf(banwidthConversion("KBPS",Float.valueOf(serviceQo.getSummationOfBw()))
								    * Integer.valueOf(value) / 100));
					}else if(Objects.nonNull(serviceDetail.getBurstableBw()) && Objects.nonNull(serviceDetail.getBurstableBwUnit())){
						logger.info("BwBpsUnit by BurstableBw");
						serviceCosCriteria.setBwBpsunit(
								String.valueOf(banwidthConversion(serviceDetail.getBurstableBwUnit(), serviceDetail.getBurstableBw())
										* Integer.valueOf(value) / 100));
					}else if (serviceBandwidth != null) {
						logger.info("BwBpsUnit by ServiecBw");
							serviceCosCriteria.setBwBpsunit(
									String.valueOf(banwidthConversion(serviceBandwidthUnit, serviceBandwidth)
											* Integer.valueOf(value) / 100));
					}
					serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
					if (serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase("IP Address")) {

						String criteraiValue = getCriteriaValue(cosCriteraMap, serviceCosCriteria.getCosName());
						logger.info("saveServiceCosCriteraForGvpn for GVPN with criteraiValue :{}and serviceCode:{} ",
								criteraiValue, serviceDetail.getServiceId());

						if (criteraiValue != null) {
							String values[] = criteraiValue.split(",");
							logger.info(
									"saveServiceCosCriteraForGvpn IP Address GVPN with values[] :{}and serviceCode:{} ",
									values, serviceDetail.getServiceId());
							AtomicInteger atomicInteger = new AtomicInteger(0);

							if (values.length > 0) {
								getUniqueValue(Arrays.asList(values)).forEach(ipAddress -> {
									atomicInteger.incrementAndGet();

									AclPolicyCriteria acp = new AclPolicyCriteria();
									acp.setAction("PERMIT");
									acp.setDestinationAny((byte) 1);
									acp.setDestinationSubnet("ANY");
									acp.setSourceAny(null);
									acp.setSourceSubnet(ipAddress);
									acp.setSequence(String.valueOf(getCosName(serviceCosCriteria.getCosName())));
									acp.setProtocol("IP");
									acp.setServiceCosCriteria(serviceCosCriteria);
									acp.setIssvcQosCoscriteriaIpaddrPreprovisioned((byte) 0);
									acp.setIssvcQosCoscriteriaIpaddrAcl((byte) 1);
									Integer legId = 0;

									if (serviceDetail.getVpnMetatDatas() != null) {
										VpnMetatData vpnMetatData = serviceDetail.getVpnMetatDatas().stream()
												.findFirst().orElse(null);
										if (vpnMetatData != null) {
											legId = vpnMetatData.getVpnLegId();
										}
									}
									acp.setIssvcQosCoscriteriaIpaddrAclName(serviceCosCriteria.getCosName() + "_"
											+ serviceDetail.getServiceId() + "_" + legId + "_IN");
									acp.setModifiedBy("OPTIMUS_RULE");

									acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
									aclPolicyCriteriaRepository.save(acp);
									AclPolicyCriteria acpSecond = new AclPolicyCriteria();
									acpSecond.setIssvcQosCoscriteriaIpaddrAcl((byte) 1);
									acpSecond.setAction("PERMIT");
									acpSecond.setDestinationAny(null);
									acpSecond.setDestinationSubnet(ipAddress);
									acpSecond.setSourceAny((byte) 1);
									acpSecond.setSourceSubnet("ANY");
									acpSecond.setSequence(String.valueOf(
											getCosName(serviceCosCriteria.getCosName()) + atomicInteger.get()));
									acpSecond.setProtocol("IP");
									acpSecond.setServiceCosCriteria(serviceCosCriteria);
									acpSecond.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
									acpSecond.setModifiedBy("OPTIMUS_RULE");
									acpSecond.setIssvcQosCoscriteriaIpaddrPreprovisioned((byte) 0);
									acpSecond.setIssvcQosCoscriteriaIpaddrAclName(serviceCosCriteria.getCosName() + "_"
											+ serviceDetail.getServiceId() + "_" + legId + "_IN");
									aclPolicyCriteriaRepository.save(acpSecond);

								});
							}
						}

					}

				}
			});
		}

	}

	

	private String getCriteriaValue(Map<String, String> cosCriteraMap, String cosName) {

		if (cosName.equalsIgnoreCase("cos1")) {

			return cosCriteraMap.getOrDefault("cos 1 criteria value", null);

		} else if (cosName.equalsIgnoreCase("cos2")) {

			return cosCriteraMap.getOrDefault("cos 2 criteria value", null);

		} else if (cosName.equalsIgnoreCase("cos3")) {

			return cosCriteraMap.getOrDefault("cos 3 criteria value", null);

		} else if (cosName.equalsIgnoreCase("cos4")) {

			return cosCriteraMap.getOrDefault("cos 4 criteria value", null);

		} else if (cosName.equalsIgnoreCase("cos5")) {

			return cosCriteraMap.getOrDefault("cos 5 criteria value", null);

		} else if (cosName.equalsIgnoreCase("cos6")) {

			return cosCriteraMap.getOrDefault("cos 6 criteria value", null);

		}
		return null;
	}
	
	
	private Integer getCosName( String cosName) {

		if (cosName.equalsIgnoreCase("cos1")) {

			return 100;

		} else if (cosName.equalsIgnoreCase("cos2")) {

			return 200;

		} else if (cosName.equalsIgnoreCase("cos3")) {

			return 300;

		} else if (cosName.equalsIgnoreCase("cos4")) {

			return 400;

		} else if (cosName.equalsIgnoreCase("cos5")) {

			return 500;

		} else if (cosName.equalsIgnoreCase("cos6")) {

			return 600;

		}
		return null;
	}

	private String getClassificationCriteria(Map<String, String> cosCriteraMap, String cosName) {

		if (cosName.equalsIgnoreCase("cos1")) {

			String cla = cosCriteraMap.getOrDefault("cos 1 criteria", "Any");

			if (cla.equalsIgnoreCase("ip precedence")) {
				return cla.replaceAll(" ", "").toLowerCase();
			}

			return cla;

		} else if (cosName.equalsIgnoreCase("cos2")) {

			String cla = cosCriteraMap.getOrDefault("cos 2 criteria", "Any");
			if (cla.equalsIgnoreCase("ip precedence")) {
				return cla.replaceAll(" ", "").toLowerCase();
			}

			return cosCriteraMap.getOrDefault("cos 2 criteria", "Any");

		} else if (cosName.equalsIgnoreCase("cos3")) {

			String cla = cosCriteraMap.getOrDefault("cos 3 criteria", "Any");
			if (cla.equalsIgnoreCase("ip precedence")) {
				return cla.replaceAll(" ", "").toLowerCase();
			}
			return cosCriteraMap.getOrDefault("cos 3 criteria", "Any");

		} else if (cosName.equalsIgnoreCase("cos4")) {
			String cla = cosCriteraMap.getOrDefault("cos 4 criteria", "Any");
			if (cla.equalsIgnoreCase("ip precedence")) {
				return cla.replaceAll(" ", "").toLowerCase();
			}
			return cosCriteraMap.getOrDefault("cos 4 criteria", "Any");

		} else if (cosName.equalsIgnoreCase("cos5")) {
			String cla = cosCriteraMap.getOrDefault("cos 5 criteria", "Any");
			if (cla.equalsIgnoreCase("ip precedence")) {
				return cla.replaceAll(" ", "").toLowerCase();
			}
			return cosCriteraMap.getOrDefault("cos 5 criteria", "Any");

		} else if (cosName.equalsIgnoreCase("cos6")) {

			String cla = cosCriteraMap.getOrDefault("cos 6 criteria", "Any");
			if (cla.equalsIgnoreCase("ip precedence")) {
				return cla.replaceAll(" ", "").toLowerCase();
			}
			return cosCriteraMap.getOrDefault("cos 6 criteria", "Any");

		}
		return "Any";
	}

	private void applyAceMappingIntoLanV4andV6(ServiceDetail serviceDetail) throws TclCommonException {
		logger.info("applyAceMappingIntoLanV4andV6 - started");
		try {
			
			List<AceIPMapping> aceIPMappings=	aceIPMappingRepository.findAllByScServiceDetail_Id(serviceDetail.getScServiceDetailId());
			
			if(!aceIPMappings.isEmpty()) {
				for(AceIPMapping aceIPMapping:aceIPMappings) {
					
					if(aceIPMapping.getAceIp().contains("/")){
						logger.info("Valid Ace Mapping Ip");
						if(aceIPMapping.getAceIp().contains(":")) {

							IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
							ipaddrLanv6Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V6"));
							ipaddrLanv6Address.setIscustomerprovided((byte) 1);
							ipaddrLanv6Address.setIssecondary((byte) 0);
							ipaddrLanv6Address.setLanv6Address(aceIPMapping.getAceIp().trim());
							ipaddrLanv6Address.setStartDate(startDate);
							ipaddrLanv6Address.setLastModifiedDate(lastModifiedDate);
							ipaddrLanv6Address.setModifiedBy(modifiedBy);
							ipaddrLanv6Address.setEndDate(endDate);
							ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
							logger.info("Service Activation - applyAceMappingIntoLanV6 - completed");
						
							
						}
						else {
							IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
							ipaddrLanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
							ipaddrLanv4Address.setIscustomerprovided((byte) 1);
							ipaddrLanv4Address.setLanv4Address(aceIPMapping.getAceIp().trim());
							ipaddrLanv4Address.setIssecondary((byte) 0);
							ipaddrLanv4Address.setStartDate(startDate);
							ipaddrLanv4Address.setLastModifiedDate(lastModifiedDate);
							ipaddrLanv4Address.setModifiedBy(modifiedBy);
							ipaddrLanv4Address.setEndDate(endDate);
							ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
						}
					}
					
				}
			}
			
			logger.info("Service Activation - applyAceMappingIntoLanV6 - completed");
		} catch (Exception e) {
			logger.error("Exception in applyAceMappingIntoLanV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	
	
	private void saveWanV4andV6CustomerProvided(ServiceDetail serviceDetail,String customerProviderIpAddress)  
			throws TclCommonException {
		logger.info("saveWanV4andV6CustomerProvided - started");
		try {

			if (customerProviderIpAddress!=null) {

				if (customerProviderIpAddress.contains(":")) {

					IpaddrWanv6Address ipaddrLanv6Address = new IpaddrWanv6Address();
					ipaddrLanv6Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V6"));
					ipaddrLanv6Address.setIscustomerprovided((byte) 1);
					ipaddrLanv6Address.setIssecondary((byte) 0);
					ipaddrLanv6Address.setWanv6Address(customerProviderIpAddress.trim());
					ipaddrLanv6Address.setStartDate(startDate);
					ipaddrLanv6Address.setLastModifiedDate(lastModifiedDate);
					ipaddrLanv6Address.setModifiedBy(modifiedBy);
					ipaddrLanv6Address.setEndDate(endDate);
					ipaddrWanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
					logger.info("Service Activation - saveWanV4andV6CustomerProvided - completed");

				} else {
					IpaddrWanv4Address ipaddrLanv4Address = new IpaddrWanv4Address();
					ipaddrLanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
					ipaddrLanv4Address.setIscustomerprovided((byte) 1);
					ipaddrLanv4Address.setWanv4Address(customerProviderIpAddress.trim());
					ipaddrLanv4Address.setIssecondary((byte) 0);
					ipaddrLanv4Address.setStartDate(startDate);
					ipaddrLanv4Address.setLastModifiedDate(lastModifiedDate);
					ipaddrLanv4Address.setModifiedBy(modifiedBy);
					ipaddrLanv4Address.setEndDate(endDate);
					ipaddrWanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
				}

			}

			logger.info("Service Activation - saveWanV4andV6CustomerProvided - completed");
		} catch (Exception e) {
			logger.error("Exception in saveWanV4andV6CustomerProvided => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	public ServiceDetail saveServiceDetailsActivation(String serviceCode,Integer version,ScOrder scOrder, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesAMap) {
		ServiceDetail serviceDetailActivation = new ServiceDetail();
		if (Objects.nonNull(scServiceDetail) && !"Eco Internet Access".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogOfferingName())) {
			if (appEnv != null && !"DEV".equalsIgnoreCase(appEnv)) {
				updateExistingActiveServiceDetails(serviceCode,scServiceDetail);
			}
			serviceDetailActivation.setServiceId(scServiceDetail.getUuid());
			serviceDetailActivation.setServiceSubtype(scServiceDetail.getErfPrdCatalogOfferingName());
			serviceDetailActivation.setOrderCategory(scServiceDetail.getOrderCategory());
			serviceDetailActivation.setOrderType(scServiceDetail.getOrderType());
			serviceDetailActivation.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			serviceDetailActivation.setOrderType(orderType);
			serviceDetailActivation.setOrderCategory(orderCategory);
			String serviceVariant = StringUtils.trimToNull(scServiceDetail.getServiceVariant());
			ScServiceAttribute serviceVariantAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"Service Variant");
			if(serviceVariant==null && serviceVariantAttribute!=null && serviceVariantAttribute.getAttributeValue()!=null && !serviceVariantAttribute.getAttributeValue().isEmpty()) {
				serviceVariant=serviceVariantAttribute.getAttributeValue();
			}
			String productName = StringUtils.trimToNull(scServiceDetail.getErfPrdCatalogProductName());
			if(productName.equalsIgnoreCase(CramerConstants.NPL)){
				serviceDetailActivation.setServiceType(CramerConstants.NPL);
				serviceDetailActivation.setServiceSubtype(CramerConstants.NPL);
			}else if (productName.equalsIgnoreCase(CramerConstants.IAS)
					|| productName.equalsIgnoreCase(CramerConstants.ILL)) {
				serviceDetailActivation.setServiceType(CramerConstants.ILL);
				if ("standard".equalsIgnoreCase(serviceVariant)) {
					serviceDetailActivation.setServiceSubtype(CramerConstants.STDILL);
				} else if ("ECO INTERNET".equalsIgnoreCase(serviceVariant)) {
					serviceDetailActivation.setServiceSubtype(CramerConstants.ECOINTERNET);
				}else {
					serviceDetailActivation.setServiceSubtype(CramerConstants.PILL);
				}
			} else {
				serviceDetailActivation.setServiceType(CramerConstants.GVPN);
				serviceDetailActivation.setServiceSubtype(CramerConstants.GVPN);
			}

			serviceDetailActivation.setScServiceDetailId(scServiceDetail.getId());
			String latLongAttribute =scComponentAttributesAMap.get("latLong");
            if (!org.springframework.util.StringUtils.isEmpty(latLongAttribute))
            {
                String latLong[] = latLongAttribute.split(",");
                serviceDetailActivation.setLat(latLong[0]);
                serviceDetailActivation.setLongiTude(latLong[1]);
            }

			serviceDetailActivation.setServiceComponenttype("PORT");
			if(!productName.equalsIgnoreCase("IZOPC")){
				serviceDetailActivation.setLastmileType(scComponentAttributesAMap.get("lmType"));
			}
			serviceDetailActivation.setLastmileProvider(scComponentAttributesAMap.get("lastMileProvider"));
			Instant instant = Instant.now();
			long timeStampMillis = instant.toEpochMilli();
			//Start of MACD
			ServiceDetail prevActiveServiceDetail=null;

			if(!orderType.equalsIgnoreCase("NEW") && !"ADD_SITE".equals(scServiceDetail.getScOrder().getOrderCategory())
					&& (Objects.isNull(scServiceDetail.getOrderSubCategory()) || (Objects.nonNull(scServiceDetail.getOrderSubCategory()) 
					&& !scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))){
				prevActiveServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(),TaskStatusConstants.ACTIVE);
				logger.info("MACD order");
				if(Objects.nonNull(prevActiveServiceDetail)){
					serviceDetailActivation.setNetpRefid(prevActiveServiceDetail.getNetpRefid());
					serviceDetailActivation.setExternalRefid(prevActiveServiceDetail.getExternalRefid());
				}
				/*List<String> serviceCodes = new ArrayList<>();
				serviceCodes.add(serviceCode);
				refreshFTIData(serviceCodes);*/
						
				
				serviceDetailActivation.setServiceOrderType(scServiceDetail.getErfPrdCatalogProductName()+"_MACD");
			}else{
				logger.info("NEW");
				serviceDetailActivation.setNetpRefid("OPT_NETP_" + timeStampMillis);
				serviceDetailActivation.setExternalRefid("OPT_NETP_" + timeStampMillis);
				serviceDetailActivation.setServiceOrderType(scServiceDetail.getErfPrdCatalogProductName());
				if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
					serviceDetailActivation.setServiceOrderType("GVPN");
				}
			}
			String burstableBandwidth=scComponentAttributesAMap.get("burstableBandwidth");
			String bwPortSpeed=scComponentAttributesAMap.get("portBandwidth");
			String isTxDowntimeReqd=scComponentAttributesAMap.get("isTxDowntimeReqd");
			if(Objects.nonNull(isTxDowntimeReqd) && !isTxDowntimeReqd.isEmpty()){
				serviceDetailActivation.setIstxdowntimeReqd(isTxDowntimeReqd.equals("true")?(byte)1:(byte)0);
			}
			String txResourcePathType=scComponentAttributesAMap.get("txResourcePathType");
			if(Objects.nonNull(txResourcePathType) && !txResourcePathType.isEmpty()){
				serviceDetailActivation.setResourcePath(txResourcePathType);
			}
			String downtimeDuration=scComponentAttributesAMap.get("downtimeDuration");
			if(Objects.nonNull(downtimeDuration) && !downtimeDuration.isEmpty()){
				serviceDetailActivation.setDowntimeDuration(downtimeDuration);;
			}
			String fromTime=scComponentAttributesAMap.get("fromTime");
			if(Objects.nonNull(fromTime) && !fromTime.isEmpty()){
				serviceDetailActivation.setFromTime(fromTime);
			}
			String toTime=scComponentAttributesAMap.get("toTime");
			if(Objects.nonNull(toTime) && !toTime.isEmpty()){
				serviceDetailActivation.setToTime(toTime);;
			}
			String downtime=scComponentAttributesAMap.get("downtime");
			if(Objects.nonNull(downtime) && !downtime.isEmpty()){
				serviceDetailActivation.setDowntime(downtime);
			}
			//End of MACD
			serviceDetailActivation.setIsportChanged((byte) 0);
			serviceDetailActivation.setIsrevised((byte) 0);
			serviceDetailActivation.setIsIdcService((byte) 0);
			serviceDetailActivation.setServiceState("ISSUED");
			if(bwPortSpeed!=null) {
				serviceDetailActivation.setServiceBandwidth(Float.valueOf(bwPortSpeed.trim()));
			}
			serviceDetailActivation.setServiceBandwidthUnit(scComponentAttributesAMap.get("bwUnit"));
			try {
				if(burstableBandwidth!=null) {
					serviceDetailActivation.setBurstableBw(Float.valueOf(burstableBandwidth.trim()));
					serviceDetailActivation.setBurstableBwUnit(scComponentAttributesAMap.get("burstableBwUnit"));
				}
			}catch(Exception e) {
				logger.error("Exception in setBurstableBw => {}", e);
			}
			serviceDetailActivation.setSolutionId(scServiceDetail.getVpnSolutionId());

			serviceDetailActivation
					.setAddressLine1(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationAddressLineOne")));
			serviceDetailActivation
					.setAddressLine2(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationAddressLineTwo")));
			serviceDetailActivation.setAddressLine3(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationLocality")));
			serviceDetailActivation.setState(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationState")));
			serviceDetailActivation.setCity(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationCity")));
			serviceDetailActivation.setPincode(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationPincode")));
			serviceDetailActivation.setCountry(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationCountry")));
			if (serviceDetailActivation.getServiceType().equalsIgnoreCase(CramerConstants.IAS)
					|| serviceDetailActivation.getServiceType().equalsIgnoreCase(CramerConstants.ILL)) {
				serviceDetailActivation.setSolutionId(scServiceDetail.getUuid());
			}

			serviceDetailActivation
					.setOrderDetail(saveOrderDetailsActivation(scServiceDetail.getScOrder(), scServiceDetail,scComponentAttributesAMap));
			if(version==null) {
			serviceDetailActivation.setVersion(1);
			}
			else {
				serviceDetailActivation.setVersion(version+1);

			}
			
			String serviceOption=scComponentAttributesAMap.get("cpeManagementType");

			if (null != serviceOption
					&& (serviceOption.toLowerCase().contains("fully")
							|| serviceOption.toLowerCase().contains("hardware")
							|| serviceOption.toLowerCase().contains("proactive")
							|| serviceOption.toLowerCase().contains("config")
							|| serviceOption.toLowerCase().contains("physically"))) {
				serviceDetailActivation.setMgmtType("MANAGED");
				serviceDetailActivation.setScopeOfMgmt(serviceOption);
			} else {
				serviceDetailActivation.setMgmtType("UNMANAGED");
			}

			/*
			 * TODO: scServiceDetail.getPriSecServiceLink() => empty, need to check service
			 * fulfillment
			 */
			
			serviceDetailActivation.setRedundancyRole(
					null != scServiceDetail.getPrimarySecondary() ? scServiceDetail.getPrimarySecondary().toUpperCase()
							: "");
			if (null != scServiceDetail.getPrimarySecondary()
					&& scServiceDetail.getPrimarySecondary().equalsIgnoreCase("primary")) {

				if (scServiceDetail.getPriSecServiceLink() == null) {
					serviceDetailActivation.setSvclinkSrvid(null);
					serviceDetailActivation.setSvclinkRole(null);
					serviceDetailActivation.setRedundancyRole("SINGLE");
				} else {

					serviceDetailActivation.setSvclinkSrvid(scServiceDetail.getPriSecServiceLink());
					serviceDetailActivation.setSvclinkRole("Secondary");
				}

			}
		 else if (null != scServiceDetail.getPrimarySecondary()
				&& scServiceDetail.getPrimarySecondary().equalsIgnoreCase("secondary")) {
			serviceDetailActivation.setSvclinkSrvid(scServiceDetail.getPriSecServiceLink());
			serviceDetailActivation.setSvclinkRole("Primary");
		} else {
			serviceDetailActivation.setSvclinkSrvid(null);
			serviceDetailActivation.setSvclinkRole(null);
			serviceDetailActivation.setRedundancyRole("SINGLE");
		}

			serviceDetailActivation.setOldServiceId(Objects.nonNull(scServiceDetail.getParentUuid())?scServiceDetail.getParentUuid():null);
			// TOOD: check with sam => if routermake is juniper then fill usage model, data
			// transfer commit and data transfer commit unit
			serviceDetailActivation.setRouterMake("");
			serviceDetailActivation.setServiceState("ISSUED");
			serviceDetailActivation.setStartDate(startDate);
			serviceDetailActivation.setLastModifiedDate(lastModifiedDate);
			serviceDetailActivation.setModifiedBy(modifiedBy);
			serviceDetailActivation.setProductName(productName);
			serviceDetailActivation = serviceDetailRepository.saveAndFlush(serviceDetailActivation);
		
		}
		return serviceDetailActivation;

	}

	private void updateExistingActiveServiceDetails(String serviceCode, ScServiceDetail scServiceDetail) {
		logger.info("updateExistingActiveServiceDetails method invoked");
		try {
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());

			if ("MACD".equals(orderType)){
				logger.info("MACD");
				String ftiServiceCode="";
				ServiceDetail serviceDetailActivation = null;
				// if addsite & parallel -> get parentuuid,ACTIVE
				String orderCategory = scServiceDetail.getOrderCategory();
				String orderSubCategory = scServiceDetail.getOrderSubCategory();
				if ("ADD_SITE".equals(orderCategory)
						|| (Objects.nonNull(orderSubCategory)
								&& !orderSubCategory.isEmpty()
								&& orderSubCategory.toLowerCase().contains("parallel"))) {
					logger.info("ADD SITE OR PARALLEL");
					ftiServiceCode=scServiceDetail.getParentUuid();
					serviceDetailActivation = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(
							scServiceDetail.getParentUuid(), TaskStatusConstants.ACTIVE);
				} else { // else -> get uuid , ACTIVE
					logger.info("OTHER THAN ADD SITE OR PARALLEL");
					ftiServiceCode=scServiceDetail.getUuid();
					serviceDetailActivation = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(
							scServiceDetail.getUuid(), TaskStatusConstants.ACTIVE);
				}		
		
			
	
				if (serviceDetailActivation != null) {
					logger.info("serviceDetailActivation exists::{}", serviceDetailActivation.getId());
					// do full FTI Migration if protocol change
					Boolean protocolChanged = checkProtocolChange(ftiServiceCode,scServiceDetail, serviceDetailActivation);
					logger.info("updateExistingActiveServiceDetails has protocolChanged as {} for {}",protocolChanged,serviceDetailActivation.getId());
					if (!protocolChanged) {
						// get FTI details
						Map<String, String> ftiDetails = getFtiDetails(ftiServiceCode, "SIVXVPNAccessPath");
						if (CollectionUtils.isEmpty(ftiDetails)) {
							logger.info("No Data from FTI for service Code {}. Retrying with different Access Path.",
									ftiServiceCode);
							ftiDetails = getFtiDetails(ftiServiceCode, "SIApIPPath");
							if (CollectionUtils.isEmpty(ftiDetails)) {
								logger.info("No Data from FTI for service Code {} with SIApIPPath", ftiServiceCode);
							}
						}

						// rfu5 value
						if (ftiDetails.containsKey("rfu5") && ftiDetails.get("rfu5") != null
								&& !ftiDetails.get("rfu5").isEmpty()) {
							serviceDetailActivation.setNetpRefid(ftiDetails.get("rfu5"));
							logger.info("service code {} has rfu5 value {} from fti and it is updated", ftiServiceCode,
									ftiDetails.get("rfu5"));

							serviceDetailActivation.setModifiedBy(modifiedBy);
							serviceDetailActivation.setLastModifiedDate(lastModifiedDate);
						}

						if (ftiDetails.containsKey("pe_equipmodule")
								&& "AS".equalsIgnoreCase(ftiDetails.get("pe_equipmodule"))) {
							if((!ftiDetails.containsKey("pewan.vprn.assvmgrid") || (ftiDetails.containsKey("pewan.vprn.assvmgrid") && ftiDetails.get("pewan.vprn.assvmgrid")==null)) 
									&& ftiDetails.containsKey("pewan.ies.assvmgrid")
									&& ftiDetails.get("pewan.ies.assvmgrid")!=null){
								logger.info("pewan.ies.assvmgrid Exists::{}",serviceDetailActivation.getId());
								serviceDetailActivation
								.setCssSammgrId(Integer.valueOf(ftiDetails.get("pewan.ies.assvmgrid")));
							}else{
								logger.info("pewan.ies.assvmgrid Not Exists::{}",serviceDetailActivation.getId());
								if (!ftiDetails.containsKey("pewan.vprn.assvmgrid")
										|| Integer.valueOf(ftiDetails.get("pewan.vprn.assvmgrid")) <= 0) {
									if(ftiDetails.containsKey("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname")){
										Pattern pattern = Pattern.compile("(?:_)(\\d+)(?:_)");
										Matcher matcher = pattern.matcher(ftiDetails.get("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname"));
										if(matcher.find()){
											serviceDetailActivation.setCssSammgrId(Integer.valueOf(matcher.group(1)));
										}
									}
								} else {
									serviceDetailActivation
											.setCssSammgrId(Integer.valueOf(ftiDetails.get("pewan.vprn.assvmgrid")));
								}
							}
						}
						
						List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetailActivation.getInterfaceProtocolMappings()
								.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null).collect(Collectors.toList());
						if (!interfaceProtocolMappings.isEmpty()) {
							logger.info("Active IPM Exists::{}", interfaceProtocolMappings.size());
							interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
							InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);
							if (interfaceProtocolMapping != null && interfaceProtocolMapping.getEthernetInterface() != null) {
								EthernetInterface peEthernetInterface=interfaceProtocolMapping.getEthernetInterface();
								logger.info("Active PE Ethernet Interface Exists::{}", peEthernetInterface.getEthernetInterfaceId());
								if(ftiDetails.containsKey("pe_equipmodule") && "AS".equalsIgnoreCase(ftiDetails.get("pe_equipmodule"))) {
									logger.info("FTI ALACTEL DEVICE Exists::{}, for Service Id::{}",ftiDetails.get("pewan.interface.vprninterface.ipinterface.srname"),serviceDetailActivation.getId());
									if(ftiDetails.containsKey("pewan.interface.vprninterface.ipinterface.srname") && ftiDetails.get("pewan.interface.vprninterface.ipinterface.srname")!=null){
										peEthernetInterface.setInterfaceName(ftiDetails.get("pewan.interface.vprninterface.ipinterface.srname"));
										if(ftiDetails.containsKey("pewan.interface.vprninterface.ipinterface.asinnerencapvalue")){
											peEthernetInterface.setInnerVlan(ftiDetails.get("pewan.interface.vprninterface.ipinterface.asinnerencapvalue"));
										}
										if(ftiDetails.containsKey("pewan.interface.vprninterface.ipinterface.asouterencapvalue") &&
												ftiDetails.get("pewan.interface.vprninterface.ipinterface.asouterencapvalue")!=null && Integer.valueOf(ftiDetails.get("pewan.interface.vprninterface.ipinterface.asouterencapvalue"))>=0) {
											peEthernetInterface.setOuterVlan(ftiDetails.get("pewan.interface.vprninterface.ipinterface.asouterencapvalue"));
										}
										if("DOT1Q".equalsIgnoreCase(peEthernetInterface.getEncapsulation()) && Objects.isNull(peEthernetInterface.getOuterVlan())) {
											peEthernetInterface.setOuterVlan(ftiDetails.get("pewan.interface.vprninterface.ipinterface.asinnerencapvalue"));
										}
									}
									if(ftiDetails.containsKey("pewan.pport.srname")){
										peEthernetInterface.setPhysicalPort(ftiDetails.get("pewan.pport.srname"));
									}
									else if(ftiDetails.containsKey("pewan.lagip.site.laginterface.srname")){
										peEthernetInterface.setPhysicalPort(ftiDetails.get("pewan.lagip.site.laginterface.srname"));
									}
									
									ethernetInterfaceRepository.saveAndFlush(peEthernetInterface);
								}else{
									logger.info("FTI CISCO or JUNIPER DEVICE Exists::{}, for Service Id::{}",ftiDetails.get("pewan.interface.vprninterface.ipinterface.srname"),serviceDetailActivation.getId());
									if(ftiDetails.containsKey("pewan.ethernetip.subint.ipinterface.risubinterface")
											&& ftiDetails.get("pewan.ethernetip.subint.ipinterface.risubinterface")!=null && Integer.valueOf(ftiDetails.get("pewan.ethernetip.subint.ipinterface.risubinterface"))>0) {
										peEthernetInterface.setInterfaceName(ftiDetails.get("pewan.ethernetip.subint.ipinterface.sremsname"));
									}else if(ftiDetails.containsKey("pewan.ethernetip.lport.sremsname")&& ftiDetails.get("pewan.ethernetip.lport.sremsname")!=null && !ftiDetails.get("pewan.ethernetip.lport.sremsname").isEmpty()){
										peEthernetInterface.setInterfaceName(ftiDetails.get("pewan.ethernetip.lport.sremsname"));
									}else {
										peEthernetInterface.setInterfaceName(ftiDetails.get("pewan.ethernetip.ipinterface.sremsname"));
									}
									
									if(ftiDetails.containsKey("pewan.ethernetip.pport.sremsname")){
										peEthernetInterface.setPhysicalPort(ftiDetails.get("pewan.ethernetip.pport.sremsname"));
									}
									String innerVlanKey = "pewan.ethernetip.ipinterface.rivlanencapid";

									if(ftiDetails.containsKey(innerVlanKey) && ftiDetails.get(innerVlanKey)!=null && Integer.valueOf(ftiDetails.get(innerVlanKey))>=0){
										peEthernetInterface.setInnerVlan(ftiDetails.get(innerVlanKey));
									}
									else{
										innerVlanKey = "pewan.ethernetip.subint.ipinterface.rivlanencapid";
										if(ftiDetails.containsKey(innerVlanKey) && ftiDetails.get(innerVlanKey)!=null && Integer.valueOf(ftiDetails.get(innerVlanKey))>=0) {
											peEthernetInterface.setInnerVlan(ftiDetails.get(innerVlanKey));
										}
										else if(ftiDetails.containsKey("pe_equipmodule") && ftiDetails.get("pe_equipmodule")!=null && ftiDetails.get("pe_equipmodule").equalsIgnoreCase("JR")){
											innerVlanKey = "pewan.ethernetip.subint.ipinterface.jrinnervlanid";
											if(ftiDetails.containsKey(innerVlanKey) && ftiDetails.get(innerVlanKey)!=null  && Integer.valueOf(ftiDetails.get(innerVlanKey))>=0) {
												peEthernetInterface.setInnerVlan(ftiDetails.get(innerVlanKey));
											}
											else{
												innerVlanKey = "pewan.ethernetip.ipinterface.jrinnervlanid";
												if(ftiDetails.containsKey(innerVlanKey) && ftiDetails.get(innerVlanKey)!=null && Integer.valueOf(ftiDetails.get(innerVlanKey))>=0){
													peEthernetInterface.setInnerVlan(ftiDetails.get(innerVlanKey));
												}
											}
										}
									}
									ethernetInterfaceRepository.saveAndFlush(peEthernetInterface);
								}
							}
						}
						
						List<InterfaceProtocolMapping> routerInterfaceProtocolMappingDetails = serviceDetailActivation.getInterfaceProtocolMappings()
								.stream().filter(serIn -> serIn.getRouterDetail() != null && serIn.getEndDate() == null)
								.collect(Collectors.toList());

						if (!routerInterfaceProtocolMappingDetails.isEmpty()) {
							logger.info("FTI ROUTER IPM Exists for Service Id::{}",routerInterfaceProtocolMappingDetails.size());
							routerInterfaceProtocolMappingDetails.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
							InterfaceProtocolMapping routerInterfaceProtocolMappingDetail = routerInterfaceProtocolMappingDetails.get(0);
							if (routerInterfaceProtocolMappingDetail != null && routerInterfaceProtocolMappingDetail.getRouterDetail() != null) {
								RouterDetail routerDetail = routerInterfaceProtocolMappingDetail.getRouterDetail();
								logger.info("FTI IPM Router detail Exists for Service Id::{}",routerDetail.getRouterId());
								if(ftiDetails.containsKey("pe_equipmodule") && ftiDetails.get("pe_equipmodule")!=null){
									if(ftiDetails.get("pe_equipmodule").equalsIgnoreCase("JR")){
										routerDetail.setRouterMake("JUNIPER");
									}else if(ftiDetails.get("pe_equipmodule").equalsIgnoreCase("AS")){
										routerDetail.setRouterMake("ALCATEL IP");
									}else if(ftiDetails.get("pe_equipmodule").equalsIgnoreCase("CR")){
										routerDetail.setRouterMake("CISCO IP");
									}
								}
								if(ftiDetails.containsKey("pewan.endpoint") && ftiDetails.get("pewan.endpoint")!=null) {
									String hostname[] = ftiDetails.get("pewan.endpoint").split("/");
									routerDetail.setRouterHostname((hostname.length > 1) ? hostname[1] : hostname[0]);
								}
								if(routerDetail.getRouterMake()!=null && !routerDetail.getRouterMake().isEmpty()){
									serviceDetailActivation.setRouterMake(routerDetail.getRouterMake());
								}
								routerDetailRepository.saveAndFlush(routerDetail);
								serviceDetailRepository.saveAndFlush(serviceDetailActivation);
							}
						}

						if (serviceDetailActivation.getServiceType().contains("GVPN")) {

							if (Objects.nonNull(serviceDetailActivation.getVpnSolutions())
									&& !serviceDetailActivation.getVpnSolutions().isEmpty()) {
								Optional<VpnSolution> vpnSolutionOptional = serviceDetailActivation.getVpnSolutions()
										.stream().findFirst();
								if (vpnSolutionOptional.isPresent()) {
									VpnSolution vpnSolution = vpnSolutionOptional.get();
									logger.info("Prev VpnSolution Exists::{}", vpnSolution.getVpnSolutionId());
									String vpnName = "";
									if (ftiDetails.containsKey("pewan.l3vpn.customervpn.name") || ftiDetails.containsKey("pewan.vprn.asdescription")) {
										logger.info("Fti vpn name exists for vpnsolution");
										if(ftiDetails.get("pewan.vprn.asdescription")!=null && ftiDetails.get("pewan.vprn.asdescription").toLowerCase().contains("vsnl")) {
											logger.info("pewan.vprn.asdescription::{}", ftiDetails.get("pewan.vprn.asdescription"));
											vpnName = ftiDetails.get("pewan.vprn.asdescription");
										}else if(ftiDetails.get("pewan.l3vpn.customervpn.name")!=null){
											logger.info("pewan.l3vpn.customervpn.name::{}", ftiDetails.get("pewan.l3vpn.customervpn.name"));
											vpnName = ftiDetails.get("pewan.l3vpn.customervpn.name");
										}else if(ftiDetails.get("pewan.vprn.asdescription")!=null) {
											logger.info("Exists pewan.vprn.asdescription::{}", ftiDetails.get("pewan.vprn.asdescription"));
											vpnName = ftiDetails.get("pewan.vprn.asdescription");
										}
										String actualValue = "";
										if(vpnName!=null && vpnName.contains("_"))vpnName.substring(0, vpnName.lastIndexOf('_'));
										if(vpnName!=null && !vpnName.isEmpty()) {
											logger.info("Vpn name exists for vpnsolution");
											vpnSolution.setVpnName(vpnName);
											vpnSolution.setVpnSolutionName(vpnName);
										}
										serviceDetailActivation.setSolutionId(actualValue);
									}
									if (ftiDetails.containsKey("pewan.l3vpn.community1.role"))
										vpnSolution.setLegRole(ftiDetails.get("pewan.l3vpn.community1.role"));
									else if (ftiDetails.containsKey("pewan.l3vpn.community.customervpn.sivautocomm")) {
										vpnSolution.setLegRole(
												ftiDetails.get("pewan.l3vpn.community.customervpn.sivautocomm"));
									}
									vpnSolution.setVpnType("CUSTOMER");
									vpnSolution.setModifiedBy("FTI_Refresh_Rule");
									vpnSolution.setLastModifiedDate(lastModifiedDate);
									vpnSolution.setServiceDetail(serviceDetailActivation);
									vpnSolutionRepository.saveAndFlush(vpnSolution);
								}
							}

							if (Objects.nonNull(serviceDetailActivation.getVpnMetatDatas())
									&& !serviceDetailActivation.getVpnMetatDatas().isEmpty()) {
								logger.info("Prev VpnMetatData Exists for serviceId::{}", serviceDetailActivation.getId());
								Optional<VpnMetatData> vpnMetatDataOptional = serviceDetailActivation.getVpnMetatDatas()
										.stream().findFirst();
								if (vpnMetatDataOptional.isPresent()) {
									VpnMetatData vpnMetatData = vpnMetatDataOptional.get();
									logger.info("Prev VpnMetatData Exists::{}", vpnMetatData.getId());
									String vpnName = "";
									if (ftiDetails.containsKey("pewan.l3vpn.community.sivcommtype") && ftiDetails.get("pewan.l3vpn.community.sivcommtype")!=null &&
											!ftiDetails.get("pewan.l3vpn.community.sivcommtype").isEmpty() 
											&& ftiDetails.get("pewan.l3vpn.community.sivcommtype").toLowerCase().contains("mesh")) {
										logger.info("pewan.l3vpn.community.sivcommtype Exists::{}",serviceDetailActivation.getId());
										vpnMetatData.setL2oSiteRole("Mesh");
										vpnMetatData.setL2OTopology("Full Mesh");
									}else {
										logger.info("pewan.l3vpn.community.sivcommtype not Exists::{}",serviceDetailActivation.getId());
										vpnMetatData.setL2OTopology("Hub & Spoke");
									}
									if (ftiDetails.containsKey("pewan.l3vpn.customervpn.name") || ftiDetails.containsKey("pewan.vprn.asdescription")) {
										logger.info("Fti vpn name exists for vpnmetadata");
										if(ftiDetails.get("pewan.vprn.asdescription")!=null && ftiDetails.get("pewan.vprn.asdescription").toLowerCase().contains("vsnl")) {
											logger.info("pewan.vprn.asdescription::{}", ftiDetails.get("pewan.vprn.asdescription"));
											vpnName = ftiDetails.get("pewan.vprn.asdescription");
										}else if(ftiDetails.get("pewan.l3vpn.customervpn.name")!=null){
											logger.info("pewan.l3vpn.customervpn.name::{}", ftiDetails.get("pewan.l3vpn.customervpn.name"));
											vpnName = ftiDetails.get("pewan.l3vpn.customervpn.name");
										}else if(ftiDetails.get("pewan.vprn.asdescription")!=null) {
											logger.info("Exists pewan.vprn.asdescription::{}", ftiDetails.get("pewan.vprn.asdescription"));
											vpnName = ftiDetails.get("pewan.vprn.asdescription");
										}
										if(vpnName!=null && !vpnName.isEmpty()) {
											logger.info("Vpn name exists for vpnmetadata");
											vpnMetatData.setVpnName(vpnName);
											vpnMetatData.setVpnSolutionName(vpnName);
										}
									}
									vpnMetatData.setModifiedBy("FTI_Refresh_Rule");
									vpnMetatData.setServiceDetail(serviceDetailActivation);
									vpnMetaDataRepository.saveAndFlush(vpnMetatData);
									if(vpnMetatData.getL2oSiteRole()==null || vpnMetatData.getL2oSiteRole().isEmpty()) {
										logger.info("vpnL2oSiteRole not exists for service id::{}",serviceDetailActivation.getId());
										vpnMetatData.setL2oSiteRole("Spoke");
										vpnMetaDataRepository.saveAndFlush(vpnMetatData);
									}
								}
							}
//							for (int i = 1; i <= 20; i++) {
//								String key ="pewan.proto.static.l3vpn.route";
//								String keyWithNumber = key + i;
//
//								if (ftiDetails.containsKey(keyWithNumber + ".asdestaddretype")) {
//									if (ftiDetails.get(keyWithNumber + ".asdestaddretype").toLowerCase().contains("ipv4")) {
//										if (ftiDetails.containsKey(keyWithNumber + ".asdestination") && ftiDetails.containsKey(keyWithNumber + ".asprefixlength")) {
//											String lanIpv4FTI = "";
//											lanIpv4FTI = ftiDetails.get(keyWithNumber + ".asdestination") + "/" + ftiDetails.get(keyWithNumber + ".asprefixlength");
//											logger.info("lanIpv4 updated by FTI ::{}", lanIpv4FTI);
//											IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
//											ipaddrLanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
//											ipaddrLanv4Address.setIscustomerprovided((byte) 1);
//											ipaddrLanv4Address.setLanv4Address(lanIpv4FTI);
//											ipaddrLanv4Address.setIssecondary((byte) 0);
//											ipaddrLanv4Address.setStartDate(startDate);
//											ipaddrLanv4Address.setLastModifiedDate(lastModifiedDate);
//											ipaddrLanv4Address.setModifiedBy("Optimus_FTI");
//											ipaddrLanv4Address.setEndDate(endDate);
//											ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
//										}
//									}
//
//									if (ftiDetails.get(keyWithNumber + ".asdestaddretype").toLowerCase().contains("ipv6")) {
//										if (ftiDetails.containsKey(keyWithNumber + ".asdestination") && ftiDetails.containsKey(keyWithNumber + ".asprefixlength")) {
//											String lanIpv6FTI = "";
//											lanIpv6FTI = ftiDetails.get(keyWithNumber + ".asdestination") + "/" + ftiDetails.get(keyWithNumber + ".asprefixlength");
//											logger.info("lanIpv6 updated by FTI ::{}", lanIpv6FTI);
//											IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
//											ipaddrLanv6Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V6"));
//											ipaddrLanv6Address.setIscustomerprovided((byte) 1);
//											ipaddrLanv6Address.setIssecondary((byte) 0);
//											ipaddrLanv6Address.setLanv6Address(lanIpv6FTI);
//											ipaddrLanv6Address.setStartDate(startDate);
//											ipaddrLanv6Address.setLastModifiedDate(lastModifiedDate);
//											ipaddrLanv6Address.setModifiedBy("Optimus_FTI");
//											ipaddrLanv6Address.setEndDate(endDate);
//											ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
//										}
//									}
//								}
//							}
						}

						serviceDetailRepository.saveAndFlush(serviceDetailActivation);

						// ipm
						List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = serviceDetailActivation
								.getInterfaceProtocolMappings().stream()
								.filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
								.collect(Collectors.toList());

						routerInterfaceProtocolMappings
								.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
						InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings
								.get(0);

						// remotbgpasnum
						if (routerInterfaceProtocolMapping.getBgp() != null
								&& routerInterfaceProtocolMapping.getBgp().getEndDate() == null) {
							Bgp bgp = routerInterfaceProtocolMapping.getBgp();

							if (ftiDetails.containsKey("pe_equipmodule")
									&& ftiDetails.get("pe_equipmodule").equalsIgnoreCase("AS")) {
								// case1
								if (ftiDetails.containsKey("pewan.proto.bgp.site.group.peer.aspeeras")
										&& ftiDetails.get("pewan.proto.bgp.site.group.peer.aspeeras") != null
										&& !ftiDetails.get("pewan.proto.bgp.site.group.peer.aspeeras").isEmpty()) {

									logger.info(
											"service code {} has pewan.proto.bgp.site.group.peer.aspeeras value {} from fti...",
											ftiServiceCode, ftiDetails.get("pewan.proto.bgp.site.group.peer.aspeeras"));

									if (ftiDetails
											.containsKey("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname"))
										bgp.setBgpPeerName(ftiDetails
												.get("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname"));

									if (Integer
											.valueOf(ftiDetails.get("pewan.proto.bgp.site.group.peer.aspeeras")) != 0) {
										bgp.setRemoteAsNumber(Integer
												.valueOf(ftiDetails.get("pewan.proto.bgp.site.group.peer.aspeeras")));
										logger.info(
												"service code {} has pewan.proto.bgp.site.group.peer.aspeeras value {} from fti and it is updated",
												ftiServiceCode,
												ftiDetails.get("pewan.proto.bgp.site.group.peer.aspeeras"));
									}

									bgp.setLastModifiedDate(lastModifiedDate);
									bgp.setModifiedBy(modifiedBy);
									bgpRepository.saveAndFlush(bgp);
								}

								if (ftiDetails.containsKey("pewan.proto.bgp.site.group.aspeeras")
										&& ftiDetails.get("pewan.proto.bgp.site.group.aspeeras") != null
										&& !ftiDetails.get("pewan.proto.bgp.site.group.aspeeras").isEmpty()) {

									logger.info(
											"service code {} has pewan.proto.bgp.site.group.aspeeras value {} from fti...",
											ftiServiceCode, ftiDetails.get("pewan.proto.bgp.site.group.aspeeras"));

									if (Integer.valueOf(ftiDetails.get("pewan.proto.bgp.site.group.aspeeras")) != 0) {
										bgp.setRemoteAsNumber(
												Integer.valueOf(ftiDetails.get("pewan.proto.bgp.site.group.aspeeras")));
										logger.info(
												"service code {} has pewan.proto.bgp.site.group.aspeeras value {} from fti and it is updated",
												ftiServiceCode,
												ftiDetails.get("pewan.proto.bgp.site.group.aspeeras"));
									}
									bgp.setLastModifiedDate(lastModifiedDate);
									bgp.setModifiedBy(modifiedBy);
									bgpRepository.saveAndFlush(bgp);

								}

								// case2
								if (ftiDetails.containsKey("pewan.proto.bgp.neighbour.riasn")
										&& ftiDetails.get("pewan.proto.bgp.neighbour.riasn") != null
										&& !ftiDetails.get("pewan.proto.bgp.neighbour.riasn").isEmpty()) {
									bgp.setRemoteAsNumber(
											Integer.valueOf(ftiDetails.get("pewan.proto.bgp.neighbour.riasn")));
									logger.info(
											"service code {} has pewan.proto.bgp.neighbour.riasn value {} from fti and it is updated",
											ftiServiceCode, ftiDetails.get("pewan.proto.bgp.neighbour.riasn"));
								}
							} else {
								if(ftiDetails.containsKey("pewan.proto.bgp.neighbour.riasn") && !ftiDetails.get("pewan.proto.bgp.neighbour.riasn").isEmpty()
									&& Integer.valueOf(ftiDetails.get("pewan.proto.bgp.neighbour.riasn")) >= 0){
									bgp.setRemoteAsNumber(Integer.valueOf("pewan.proto.bgp.neighbour.riasn"));
								}
								else if (ftiDetails.containsKey("cec_remoteasn") && !ftiDetails.get("cec_remoteasn").isEmpty()
										&& Integer.valueOf(ftiDetails.get("cec_remoteasn")) >= 0) {
									bgp.setRemoteAsNumber(Integer.valueOf(ftiDetails.get("cec_remoteasn")));
								}
							}

							if (ftiDetails.containsKey("cep_ipaddress") && ftiDetails.get("cep_ipaddress") != null
									&& !ftiDetails.get("cep_ipaddress").isEmpty()
									&& ftiDetails.get("cep_ipaddress").contains("/")) {
								logger.info("BgpRemoteIpv4 from FTI by cep::{}", ftiDetails.get("cep_ipaddress"));
								String[] splitCepIpAddress = ftiDetails.get("cep_ipaddress").split("/");
								bgp.setRemoteIpv4Address(splitCepIpAddress[0]);
							} else if (ftiDetails.containsKey("pewan.ipaddressing.remote.ipv4address.riipaddr")
									&& ftiDetails.get("pewan.ipaddressing.remote.ipv4address.riipaddr") != null
									&& !ftiDetails.get("pewan.ipaddressing.remote.ipv4address.riipaddr").isEmpty()) {
								logger.info("BgpRemoteIpv4 from FTI by pewan::{}",
										ftiDetails.get("pewan.ipaddressing.remote.ipv4address.riipaddr"));
								bgp.setRemoteIpv4Address(
										ftiDetails.get("pewan.ipaddressing.remote.ipv4address.riipaddr"));
							}

							bgp.setLastModifiedDate(lastModifiedDate);
							bgp.setModifiedBy(modifiedBy);
							bgpRepository.saveAndFlush(bgp);

						}
						
						//ospf area id update
						if (routerInterfaceProtocolMapping.getOspf() != null
								&& routerInterfaceProtocolMapping.getOspf().getEndDate() == null) {
							Ospf ospf = routerInterfaceProtocolMapping.getOspf();
							if (ftiDetails.containsKey("pewan.proto.ospf.site.areasite.asareaid")
									&& ftiDetails.get("pewan.proto.ospf.site.areasite.asareaid") != null
									&& !ftiDetails.get("pewan.proto.ospf.site.areasite.asareaid").isEmpty()) {
							ospf.setAreaId(ftiDetails.get("pewan.proto.ospf.site.areasite.asareaid"));
							ospfRepository.saveAndFlush(ospf);
							}
						}

					}
				}else{
					logger.info("Active Service Code not exists::{} ",serviceCode);
				}
			}else {
				logger.error("Skip updateExistingActiveServiceDetails for NEW Order {}",serviceCode);
			}

		} catch (Exception e) {
			logger.error("Error in updateExistingActiveServiceDetails to set rfu5 and bgpasnum from fti {} ", e);
		}

	}


	private Boolean checkProtocolChange(String serviceCode, ScServiceDetail scServiceDetail, ServiceDetail serviceDetailActivation) {

		Boolean protocolChanged = false;
		logger.info("Inside checkProtocolChange for serviceCode {} ",serviceCode);
		try {
			String oldProtocol = null;

			// ipm
			List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = serviceDetailActivation
					.getInterfaceProtocolMappings().stream()
					.filter(ser -> ser.getCpe() == null && ser.getEndDate() == null).collect(Collectors.toList());

			routerInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			if(!routerInterfaceProtocolMappings.isEmpty()) {
				InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings.get(0);

				if (routerInterfaceProtocolMapping.getBgp() != null
						&& routerInterfaceProtocolMapping.getBgp().getEndDate() == null) {

					oldProtocol = "BGP";
					logger.info("oldProtocol inside checkProtocolChange for serviceCode {} updated as ::{}", serviceCode,
							oldProtocol);

				} else if (routerInterfaceProtocolMapping.getEigrp() != null
						&& routerInterfaceProtocolMapping.getEigrp().getEndDate() == null) {

					oldProtocol = "EIGRP";
					logger.info("oldProtocol inside checkProtocolChange for serviceCode {} updated as ::{}", serviceCode,
							oldProtocol);

				} else if (routerInterfaceProtocolMapping.getOspf() != null
						&& routerInterfaceProtocolMapping.getOspf().getEndDate() == null) {

					oldProtocol = "OSPF";
					logger.info("oldProtocol inside checkProtocolChange for serviceCode {} updated as ::{}", serviceCode,
							oldProtocol);

				} else if (routerInterfaceProtocolMapping.getRip() != null
						&& routerInterfaceProtocolMapping.getRip().getEndDate() == null) {

					oldProtocol = "RIP";
					logger.info("oldProtocol inside checkProtocolChange for serviceCode {} updated as ::{}", serviceCode,
							oldProtocol);

				} else if (routerInterfaceProtocolMapping.getStaticProtocol() != null
						&& routerInterfaceProtocolMapping.getStaticProtocol().getEndDate() == null) {

					oldProtocol = "STATIC";
					logger.info("oldProtocol inside checkProtocolChange for serviceCode {} updated as ::{}", serviceCode,
							oldProtocol);

				}
			}

			logger.info("oldProtocol inside checkProtocolChange for serviceCode {} updated finally as ::{}", serviceCode,
					oldProtocol);
			// sc_component-> final submitted ->preferred
			// Service_atributes-> from l20

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("routingProtocol"), scServiceDetail.getId(), "LM", "A");
			String currentProtocol = null;
			if (scComponentAttributesAMap.containsKey("routingProtocol")) {
				currentProtocol = scComponentAttributesAMap.get("routingProtocol");
				logger.info(
						"currentProtocol - routingProtocol inside checkProtocolChange for serviceCode {} found in ScComponent as {}",
						serviceCode, currentProtocol);
				String currentProtocolStd = standardizeProtocolNameCheck(currentProtocol);
				logger.info(
						"currentProtocolStd - routingProtocol inside checkProtocolChange/standardizeProtocolNameCheck for serviceCode {} found in ScComponent as {}",
						serviceCode, currentProtocolStd);

				if (!currentProtocolStd.equalsIgnoreCase(oldProtocol)) {
					protocolChanged = true;
					logger.info(
							"protocolChanged updated inside checkProtocolChange method for serviceCode {} with old value : {} and new value : {}",
							serviceCode, oldProtocol, currentProtocolStd);

					// refreshFTIData(Arrays.asList(serviceDetailActivation.getServiceId()));
				}
			}

			if (currentProtocol == null || currentProtocol.isEmpty()) {
				Map<String, String> scServiceAttributesAMap = new HashMap<>();

				Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
				scServiceAttributes = scServiceDetail.getScServiceAttributes();

				scServiceAttributesAMap = scServiceAttributes.stream().collect(HashMap::new,
						(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

				if (scServiceAttributesAMap.containsKey("Routing Protocol")) {

					currentProtocol = scServiceAttributesAMap.get("Routing Protocol");
					logger.info(
							"currentProtocol - Routing Protocol inside checkProtocolChange for serviceCode {} found in ScService as {}",
							serviceCode, currentProtocol);
					String currentProtocolStd = standardizeProtocolNameCheck(currentProtocol);
					logger.info(
							"currentProtocolStd - Routing Protocol inside checkProtocolChange/standardizeProtocolNameCheck for serviceCode {} found in ScService as {}",
							serviceCode, currentProtocolStd);

					if (!currentProtocolStd.equalsIgnoreCase(oldProtocol)) {
						protocolChanged = true;
						logger.info(
								"protocolChanged updated inside checkProtocolChange method for serviceCode {} with old value : {} and new value : {}",
								serviceCode, oldProtocol, currentProtocolStd);

						// refreshFTIData(Arrays.asList(serviceDetailActivation.getServiceId()));
					}

				} else if (scServiceAttributesAMap.containsKey("ROUTING_PROTOCOL")) {

					currentProtocol = scServiceAttributesAMap.get("ROUTING_PROTOCOL");
					logger.info(
							"currentProtocol - ROUTING_PROTOCOL inside checkProtocolChange for serviceCode {} found in ScService as {}",
							serviceCode, currentProtocol);
					String currentProtocolStd = standardizeProtocolNameCheck(currentProtocol);
					logger.info(
							"currentProtocolStd - ROUTING_PROTOCOL inside checkProtocolChange/standardizeProtocolNameCheck for serviceCode {} found in ScService as {}",
							serviceCode, currentProtocolStd);

					if (!currentProtocolStd.equalsIgnoreCase(oldProtocol)) {
						protocolChanged = true;
						logger.info(
								"protocolChanged updated inside checkProtocolChange method for serviceCode {} with old value : {} and new value : {}",
								serviceCode, oldProtocol, currentProtocolStd);

						// refreshFTIData(Arrays.asList(serviceDetailActivation.getServiceId()));
					}

				} else {

					logger.info("ROUTING PROTOCOL not found for current service Detail with service code {} default value false will be returned",
							serviceDetailActivation.getServiceId());

				}

			}
			if (protocolChanged) {
				logger.info("protocol changed {} with service code {}, running FTI migration", protocolChanged,
						serviceDetailActivation.getServiceId());
				//refreshFTI(Arrays.asList(serviceDetailActivation.getServiceId())); //24-07-2020 revisit - version issue
				logger.info("protocol changed {} with service code {}, FTI migration successful", protocolChanged,
						serviceDetailActivation.getServiceId());

			} else {
				logger.info("protocol not changed with service code {}, running FTI migration", protocolChanged,
						serviceDetailActivation.getServiceId());
			}

		} catch (Exception e) {
			logger.error("Error in checkProtocolChange for service code {} as {}", serviceCode,
					e);
		}

		logger.info("protocol changed value {} with service code {},returning to updateExistingActiveServiceDetails", protocolChanged,
				serviceDetailActivation.getServiceId());
		return protocolChanged;
	}


	private String standardizeProtocolNameCheck(String currentProtocol) {
		String protocolString = null;
		try {
			logger.info("Inside standardizeProtocolNameCheck with currentProtocol {}", currentProtocol);

			if (currentProtocol != null && !currentProtocol.isEmpty()) {
				if (currentProtocol.toLowerCase().contains("bgp")) {
					protocolString = "BGP";
					logger.info("Protocol String with currentProtocol {} updated as {}", currentProtocol,
							protocolString);
				} else if (currentProtocol.toLowerCase().contains("static")) {
					protocolString = "STATIC";
					logger.info("Protocol String with currentProtocol {} updated as {}", currentProtocol,
							protocolString);
				} else if (currentProtocol.toLowerCase().contains("eigrp")) {
					protocolString = "EIGRP";
					logger.info("Protocol String with currentProtocol {} updated as {}", currentProtocol,
							protocolString);
				} else if (currentProtocol.toLowerCase().contains("rip")) {
					protocolString = "RIP";
					logger.info("Protocol String with currentProtocol {} updated as {}", currentProtocol,
							protocolString);
				} else if (currentProtocol.toLowerCase().contains("ospf")) {
					protocolString = "OSPF";
					logger.info("Protocol String with currentProtocol {} updated as {}", currentProtocol,
							protocolString);
				}
				logger.info("Protocol String with currentProtocol {} final updated as {}", currentProtocol,
						protocolString);
			}
		} catch (Exception e) {
			logger.error("standardizeProtocolNameCheck method error for current protocol {} with error {}",
					currentProtocol, e);
		}
		return protocolString;
	}


	private OrderDetail saveOrderDetailsActivation(ScOrder scOrder, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesAMap) {
		OrderDetail orderDetail = null;
		String orderType=scOrder.getOrderType();
		String orderCategory=scOrder.getOrderCategory();


		if (Objects.nonNull(scOrder)) {
			orderDetail = orderDetailRepository.findByExtOrderrefId(scOrder.getOpOrderCode());
			if (orderDetail == null) {
				orderDetail = new OrderDetail();
			//	orderDetail.setProjectName("GVPN TEST OPTIMUS");//need to remove once UAT done for GVPN
				//orderDetail.setOrderId(scOrder.getId());
				orderDetail.setOrderType(orderType);
				orderDetail.setOrderCategory(orderCategory);
				//orderDetail.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
				orderDetail.setOrderStatus(scOrder.getOrderStatus());
				orderDetail.setExtOrderrefId(scOrder.getUuid());
				orderDetail.setSfdcOpptyId(scOrder.getTpsSfdcOptyId());
				orderDetail.setCopfId(scOrder.getTpsCrmCofId());
				
				String crnId =scComponentAttributesAMap.get("customerRef");
				
				// TODO: setCopfId, setOptyBidCategory(eg: category1/category2),
				if (crnId != null) {
					orderDetail.setCustomerCrnId(crnId);
				} else {
					ScOrderAttribute attrs = scOrder.getScOrderAttributes().stream()
							.filter(attr -> attr.getAttributeName().equalsIgnoreCase("CRN_ID")).findFirst()
							.orElse(null);
					if(attrs!=null) {
					orderDetail.setCustomerCrnId(attrs.getAttributeValue());
					}
				}
				orderDetail.setCustCuId(Integer.parseInt(scOrder.getTpsSfdcCuid()));
				if(scOrder.getUuid().toLowerCase().contains("izosdwan")){
					orderDetail.setOptyBidCategory("CAT 3-4");
				}else{
					orderDetail.setOptyBidCategory("CAT 1-2");
				}
				orderDetail.setCustomerName(scOrder.getErfCustLeName());
				orderDetail.setCustomerEmail(scComponentAttributesAMap.get("localItContactEmailId"));
				orderDetail.setCustomerPhoneno(scComponentAttributesAMap.get("localItContactMobile"));
				ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
				orderDetail.setAddressLine1(scContractInfo.getBillingAddressLine1());
				orderDetail.setAddressLine2(scContractInfo.getBillingAddressLine2());
				orderDetail.setAddressLine3(scContractInfo.getBillingAddressLine3());
				orderDetail.setCity(StringUtils.trimToNull(scContractInfo.getBillingCity()));
				orderDetail.setLocation(StringUtils.trimToNull(scContractInfo.getBillingCity()));
				orderDetail.setState(scContractInfo.getBillingState());
				orderDetail.setCountry(scContractInfo.getBillingCountry());
				orderDetail.setPincode(scContractInfo.getBillingPincode());
				orderDetail.setCustomerType("Others");
				orderDetail.setCustomerCategory(
						null != scOrder.getCustomerSegment() ? scOrder.getCustomerSegment() : "Enterprise");
				orderDetail.setStartDate(startDate);
				orderDetail.setLastModifiedDate(lastModifiedDate);
				orderDetail.setModifiedBy(modifiedBy);
				orderDetail.setEndDate(endDate);
				String programName = scComponentAttributesAMap.get("programName");
				orderDetail.setProjectName(programName);
				orderDetail = orderDetailRepository.saveAndFlush(orderDetail);
			} else {
				if(scComponentAttributesAMap.containsKey("programName")) {
					String programName = scComponentAttributesAMap.get("programName");
					orderDetail.setProjectName(programName);
					orderDetail = orderDetailRepository.saveAndFlush(orderDetail);
				}
				return orderDetail;
			}
		}

		return orderDetail;
	}

	public InterfaceProtocolMapping saveInterfaceProtocolMapping(ServiceDetail serviceDetail,
			OrderDetail orderDetailResp, Map<String, Object> iasServiceMap, Boolean isRouter, Boolean isCpe)
			throws TclCommonException {
		logger.info("Service Activation - saveInterfaceProtocolMapping - started ServiceId={} ServiceCode={} isRouter={} isCpe={}",serviceDetail.getId(),serviceDetail.getServiceId(),isRouter,isCpe);
		try {
			InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
			interfaceProtocolMapping.setServiceDetail(serviceDetail);

			if (isRouter) {
				if ((Boolean) iasServiceMap.get("isEthernetInterface")
						&& (Boolean) iasServiceMap.get("isPERouterExists")) {
					interfaceProtocolMapping
							.setEthernetInterface(saveEthernetInterface(serviceDetail, iasServiceMap, false));
				}

				if ((Boolean) iasServiceMap.get("isE1SerialInterface")
						&& (Boolean) iasServiceMap.get("isPERouterExists")) {
					interfaceProtocolMapping.setChannelizedE1serialInterface(saveChannelizedE1serialInterface(
							(PERouter) iasServiceMap.get("peRouter"), serviceDetail, false, iasServiceMap));
				}

				if ((Boolean) iasServiceMap.get("isSHDInterface") && (Boolean) iasServiceMap.get("isPERouterExists")) {
					interfaceProtocolMapping.setChannelizedSdhInterface(saveChannelizedSdhInterface(serviceDetail,
							(PERouter) iasServiceMap.get("peRouter"), false, iasServiceMap));

				}

				if ((Boolean) iasServiceMap.get("isWanRoutingProtocolPresent")) {
					if ((Boolean) iasServiceMap.get("isBgp")) {
						interfaceProtocolMapping
								.setBgp(saveBgp((BGP) iasServiceMap.get("bgp"), iasServiceMap, serviceDetail));
					}

					if ((Boolean) iasServiceMap.get("isOspf")) {
						interfaceProtocolMapping.setOspf(saveOspf((OSPF) iasServiceMap.get("ospf")));
					}
					
					/*if ((Boolean) iasServiceMap.get("isEigrp")) {
						interfaceProtocolMapping.setEigrp(saveEigrp());
					}*/

					if ((Boolean) iasServiceMap.get("isStatic")) {
						interfaceProtocolMapping.setStaticProtocol(saveStaticProtocol(iasServiceMap));
					}

					/*if ((Boolean) iasServiceMap.get("isRip")) {
						interfaceProtocolMapping.setRip(saveRip());
					}*/
				}
			}

			// for cpe - need to insert the interface as ethernet always
			if (isCpe) {
				if ((Boolean) iasServiceMap.get("isEthernetInterface")
						&& (Boolean) iasServiceMap.get("isPERouterExists")) {
					interfaceProtocolMapping
							.setEthernetInterface(saveEthernetInterface(serviceDetail, iasServiceMap, true));
				}
				if ((Boolean) iasServiceMap.get("isSHDInterface") && (Boolean) iasServiceMap.get("isPERouterExists")) {
					interfaceProtocolMapping.setChannelizedSdhInterface(saveChannelizedSdhInterface(serviceDetail,
							(PERouter) iasServiceMap.get("peRouter"), true, iasServiceMap));
				}
				if ((Boolean) iasServiceMap.get("isE1SerialInterface")
						&& (Boolean) iasServiceMap.get("isPERouterExists")) {
					interfaceProtocolMapping.setChannelizedE1serialInterface(saveChannelizedE1serialInterface(
							(PERouter) iasServiceMap.get("peRouter"), serviceDetail, true, iasServiceMap));
				}
			}

			if ((Boolean) iasServiceMap.get("isPERouterExists") && isRouter) {
				interfaceProtocolMapping.setRouterDetail(saveRouterDetail((PERouter) iasServiceMap.get("peRouter")));

				PERouter routerInfo = (PERouter) iasServiceMap.get("peRouter");
				// TOOD: check with sam => if routermake is juniper then fill usage model, data
				// transfer commit and data transfer commit unit
				serviceDetail.setRouterMake(routerInfo.getMake());

				interfaceProtocolMapping.setIscpeLanInterface((byte) 0);
				interfaceProtocolMapping.setIscpeWanInterface((byte) 0);
			}

			if ((Boolean) iasServiceMap.get("isCPEExists") && isCpe) {
				CramerCPE customerPremiseEquipment = (CramerCPE) iasServiceMap.get("cpe");
				interfaceProtocolMapping.setCpe(saveCpe(serviceDetail, customerPremiseEquipment));

				interfaceProtocolMapping.setIscpeLanInterface((byte) 0);
				interfaceProtocolMapping.setIscpeWanInterface((byte) 1);
			}

			interfaceProtocolMapping.setStartDate(startDate);
			interfaceProtocolMapping.setLastModifiedDate(lastModifiedDate);
			interfaceProtocolMapping.setModifiedBy(modifiedBy);
			interfaceProtocolMapping.setEndDate(endDate);
			interfaceProtocolMapping.setVersion(1);
			InterfaceProtocolMapping interfaceProtocolMappingResp = interfaceProtocolMappingRepository
					.saveAndFlush(interfaceProtocolMapping);

			logger.info("Service Activation - saveInterfaceProtocolMapping - completed");
			return interfaceProtocolMappingResp;
		} catch (Exception e) {
			logger.error("Exception in saveInterfaceProtocolMapping => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Cpe saveCpe(ServiceDetail serviceDetail, CramerCPE customerPremiseEquipment) throws TclCommonException {
		logger.info("Service Activation - saveCpe - started");
		try {
			Cpe cpe = new Cpe();
			cpe.setServiceId(serviceDetail.getServiceId());
			
			if (StringUtils.trimToNull(customerPremiseEquipment.getCPEloopbackIP()) != null) {
				if (StringUtils.trimToNull(customerPremiseEquipment.getCPEloopbackIP()).contains(".")) {
					cpe.setMgmtLoopbackV4address(StringUtils.trimToNull(customerPremiseEquipment.getCPEloopbackIP()));

				} else {
					cpe.setMgmtLoopbackV6address(StringUtils.trimToNull(customerPremiseEquipment.getCPEloopbackIP()));

				}
			}
			if (StringUtils.trimToNull(customerPremiseEquipment.getCPEloopbackIPV6()) != null) {

			cpe.setMgmtLoopbackV6address(customerPremiseEquipment.getCPEloopbackIPV6());
			}

			Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById(serviceDetail.getScServiceDetailId());
			if(oScServiceDetail.isPresent()){
				ScServiceDetail scServiceDetail = oScServiceDetail.get();
				String additionalServiceParamValue = componentAndAttributeService.getAdditionalAttributes(scServiceDetail, "CPE Basic Chassis");
				if(StringUtils.isNotBlank(additionalServiceParamValue)){
					CpeBomResource[] cpeBomResources = Utils.convertJsonToObject(additionalServiceParamValue, CpeBomResource[].class);
					if(Objects.nonNull(cpeBomResources[0])){
						cpe.setModel(cpeBomResources[0].getUniCode());
					}
				}
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc
						(scServiceDetail.getId(), "cpeSerialNumber","LM","A");
				if(Objects.nonNull(scComponentAttribute)){
					cpe.setDeviceId(scComponentAttribute.getAttributeValue());
				}
			}

			cpe.setMake("CISCO");
			cpe.setIsaceconfigurable((byte) 0);
			cpe.setSendInittemplate((byte) 0);
			cpe.setStartDate(startDate);
			cpe.setLastModifiedDate(lastModifiedDate);
			cpe.setModifiedBy(modifiedBy);
			cpe.setEndDate(endDate);
			Cpe cpeResp = cpeRepository.saveAndFlush(cpe);
			logger.info("Service Activation - saveCpe - completed");
			return cpeResp;
		} catch (Exception e) {
			logger.error("Exception in saveCpe => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Eigrp saveEigrp() throws TclCommonException {
		logger.info("Service Activation - saveEigrp - started");
		try {
			Eigrp eigrp = new Eigrp();
			eigrp.setRemoteAsnumber("AS4755");
			eigrp.setSooRequired((byte) 1);
			eigrp.setLocalAsnumber("AS4755");
			eigrp.setLoad("1");
			eigrp.setMtu("1");
			eigrp.setReliability("1");
			eigrp.setRedistributionDelay("1");
			eigrp.setInterfaceDelay("1");
			eigrp.setEigrpBwKbps("1");
			eigrp.setIsredistributeConnectedEnabled((byte) 1);
			eigrp.setIsredistributeStaticEnabled((byte) 1);
			eigrp.setIsroutemapEnabled((byte) 1);
			eigrp.setIsroutemapPreprovisioned((byte) 1);
			eigrp.setRedistributeRoutemapName("REDIS");
			eigrp.setRemoteCeAsnumber("AS4755");
			eigrp.setStartDate(startDate);
			eigrp.setLastModifiedDate(lastModifiedDate);
			eigrp.setModifiedBy(modifiedBy);
			eigrp.setEndDate(endDate);
			Eigrp eigrpResp = eigrpRepository.saveAndFlush(eigrp);
			logger.info("Service Activation - saveEigrp - completed");
			return eigrpResp;
		} catch (Exception e) {
			logger.error("Exception in saveEigrp => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public StaticProtocol saveStaticProtocol(Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveStaticProtocol - started");
		try {
			StaticProtocol staticProtocol = new StaticProtocol();
			staticProtocol.setLocalPreference(null);
			staticProtocol.setLocalPreferenceV6(null);
			staticProtocol.setIsroutemapEnabled((byte) 0);
			staticProtocol.setIsroutemapPreprovisioned((byte) 0);
			staticProtocol.setRedistributeRoutemapIpv4(null);
			staticProtocol.setStartDate(startDate);
			staticProtocol.setLastModifiedDate(lastModifiedDate);
			staticProtocol.setModifiedBy(modifiedBy);
			staticProtocol.setEndDate(endDate);
			StaticProtocol staticProtocolResp = staticProtocolRepository.saveAndFlush(staticProtocol);
			iasServiceMap.put("staticEntity", staticProtocolResp);
			logger.info("Service Activation - saveStaticProtocol - completed");
			return staticProtocolResp;
		} catch (Exception e) {
			logger.error("Exception in saveStaticProtocol => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Ospf saveOspf(OSPF ospfXml) throws TclCommonException {
		logger.info("Service Activation - saveOspf - started");
		try {
			Ospf ospf = new Ospf();
			ospf.setProcessId(ospfXml.getProcessID());
			ospf.setStartDate(startDate);
			ospf.setLastModifiedDate(lastModifiedDate);
			ospf.setModifiedBy(modifiedBy);
			ospf.setEndDate(endDate);
			Ospf ospfResp = ospfRepository.saveAndFlush(ospf);
			logger.info("Service Activation - saveOspf - completed");
			return ospfResp;
		} catch (Exception e) {
			logger.error("Exception in saveOspf => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Bgp saveBgp(BGP bgpXml, Map<String, Object> iasServiceMap, ServiceDetail serviceDetail)
			throws TclCommonException {
		logger.info("Service Activation - saveBgp - started");
		try {
			Bgp bgp = new Bgp();
			ScComponentAttribute scComponentAttributeAsNumber = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"asNumber", "LM", "A");
			if(Objects.nonNull(scComponentAttributeAsNumber) && Objects.nonNull(scComponentAttributeAsNumber.getAttributeValue()) && !scComponentAttributeAsNumber.getAttributeValue().isEmpty() && "Customer public AS Number".equalsIgnoreCase(scComponentAttributeAsNumber.getAttributeValue())){
				logger.info("AsNumber Customer Provided");
				ScComponentAttribute scComponentAttributeBgpAsNumber = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
								"bgpAsNumber", "LM", "A");
				if(Objects.nonNull(scComponentAttributeBgpAsNumber) && Objects.nonNull(scComponentAttributeBgpAsNumber.getAttributeValue()) && !scComponentAttributeBgpAsNumber.getAttributeValue().isEmpty()
						&& StringUtils.isNumeric(scComponentAttributeBgpAsNumber.getAttributeValue())){
					logger.info("BgpAsNumber Exists");
					bgp.setRemoteAsNumber(Integer.valueOf(scComponentAttributeBgpAsNumber.getAttributeValue().trim()));
				}
			}else{
				logger.info("Bgp As Number not provided by user");
				Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceDetail.getScServiceDetailId());
				if(scServiceDetailOptional.isPresent()){
					ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
					String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
					if(orderType!=null && scServiceDetail.getScOrder().getOrderCategory()!=null
							&& orderType.equalsIgnoreCase("MACD")
							&& scServiceDetail.getScOrder().getOrderCategory().equalsIgnoreCase("ADD_SITE")){
						logger.info("ADD SITE: BGP AS NUMBER");
						if(bgpXml!=null && bgpXml.getRemoteASNumber()==null){
							logger.info("BgpAsNumber not exists in Cramer Response::{}",serviceDetail.getId());
							throw new TclCommonException("BgpAsNumber not exists in Cramer Response");
						}else{
							logger.info("BgpAsNumber exists from Cramer Response");
							bgp.setRemoteAsNumber(Integer.parseInt(bgpXml.getRemoteASNumber()));
						}
					}else{
						logger.info("OTHER THAN ADD SITE: BGP AS NUMBER");
						bgp.setRemoteAsNumber(Objects.nonNull(bgpXml.getRemoteASNumber())?Integer.valueOf(bgpXml.getRemoteASNumber()):null);
					}
				}
			}
			ScComponentAttribute scComponentAttributePassword = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"asPassword", "LM", "A");
			bgp.setPassword(
					null != scComponentAttributePassword ? scComponentAttributePassword.getAttributeValue() : "");
			ScComponentAttribute scComponentAttributeAuthRequired = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"isAuthenticationRequiredForProtocol", "LM", "A");
			String authRequired = null != scComponentAttributeAuthRequired
					? scComponentAttributeAuthRequired.getAttributeValue()
					: null;

			if (authRequired == null) {
				bgp.setIsauthenticationRequired((byte) 0);
			} else {
				bgp.setIsauthenticationRequired(authRequired.equalsIgnoreCase("No") ? (byte) 0 : (byte) 1);
			}
			ScComponentAttribute scComponentAttributeAuthMode = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"authenticationMode", "LM", "A");
			ScComponentAttribute scComponentAttributeRouteExchange = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"routesExchanged", "LM", "A");
			if(scComponentAttributeRouteExchange!=null) {
			bgp.setRoutesExchanged(scComponentAttributeRouteExchange.getAttributeValue().toUpperCase());
			}
			ScComponentAttribute bgpPeerOn = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"bgpPeeringOn", "LM", "A");
			bgp.setNeighborOn(bgpPeerOn!=null?bgpPeerOn.getAttributeValue().toUpperCase():null);
			bgp.setAuthenticationMode(
					null != scComponentAttributeAuthMode ? scComponentAttributeAuthMode.getAttributeValue() : "");
			bgp.setNeighbourCommunity(null);
			bgp.setRedistributeConnectedEnabled(null);
			bgp.setRedistributeStaticEnabled(null);
			bgp.setStartDate(startDate);
			bgp.setLastModifiedDate(lastModifiedDate);
			bgp.setModifiedBy(modifiedBy);
			bgp.setEndDate(endDate);
			Bgp bgpResp = bgpRepository.saveAndFlush(bgp);
			iasServiceMap.put("bgpEntity", bgpResp);
			logger.info("Service Activation - saveBgp - completed");
			return bgpResp;
		} catch (Exception e) {
			logger.error("Exception in saveBgp => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Rip saveRip() throws TclCommonException {
		logger.info("Service Activation - saveRip - started");
		try {
			Rip rip = new Rip();
			rip.setGroupName("RIPGRP");
			rip.setVersion("1");
			rip.setLocalPreference("200");
			rip.setLocalPreferenceV6("100");
			rip.setStartDate(startDate);
			rip.setLastModifiedDate(lastModifiedDate);
			rip.setModifiedBy(modifiedBy);
			rip.setEndDate(endDate);
			Rip ripResp = ripRepository.saveAndFlush(rip);
			logger.info("Service Activation - saveRip - completed");
			return ripResp;
		} catch (Exception e) {
			logger.error("Exception in saveRip => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public HsrpVrrpProtocol saveHsrpVrrpProtocol(ServiceDetail serviceDetail, EthernetInterface ethernetInterface,
			Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveHsrpVrrpProtocol - started");
		try {
			HsrpVrrpProtocol hsrpVrrpProtocol = new HsrpVrrpProtocol();
			hsrpVrrpProtocol.setPriority("");
			hsrpVrrpProtocol.setRole("");

			if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent")
					&& (Boolean) iasServiceMap.get("isHsrpPresent")) {
				HSRP hsrp = (HSRP) iasServiceMap.get("hsrp");
				hsrpVrrpProtocol.setVirtualIpv4Address(StringUtils.trimToNull(hsrp.getVirtualIPv4Address()));
				hsrpVrrpProtocol.setVirtualIpv6Address(StringUtils.trimToNull(hsrp.getVirtualIPv6Address()));
				hsrpVrrpProtocol.setHelloValue("3");
			}

			if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent")
					&& (Boolean) iasServiceMap.get("isVrrpPresent")) {
				VRRP vrrp = (VRRP) iasServiceMap.get("vrrp");
				hsrpVrrpProtocol.setVirtualIpv4Address(StringUtils.trimToNull(vrrp.getVirtualIPv4Address()));
				hsrpVrrpProtocol.setVirtualIpv6Address(StringUtils.trimToNull(vrrp.getVirtualIPv6Address()));
				hsrpVrrpProtocol.setHelloValue("3");
			}

			if (serviceDetail.getRedundancyRole().equalsIgnoreCase("primary")) {
				hsrpVrrpProtocol.setPriority("100");
				hsrpVrrpProtocol.setRole("Active");
			} else if (serviceDetail.getRedundancyRole().equalsIgnoreCase("Secondary")) {
				hsrpVrrpProtocol.setPriority("120");
				hsrpVrrpProtocol.setRole("Standby");
			}

			hsrpVrrpProtocol.setEthernetInterface(ethernetInterface);
			hsrpVrrpProtocol.setStartDate(startDate);
			hsrpVrrpProtocol.setLastModifiedDate(lastModifiedDate);
			hsrpVrrpProtocol.setModifiedBy(modifiedBy);
			hsrpVrrpProtocol.setEndDate(endDate);
			HsrpVrrpProtocol hsrpVrrpProtocolResp = hsrpVrrpProtocolRepository.saveAndFlush(hsrpVrrpProtocol);
			logger.info("Service Activation - saveHsrpVrrpProtocol - completed");
			return hsrpVrrpProtocolResp;
		} catch (Exception e) {
			logger.error("Exception in saveHsrpVrrpProtocol => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public ChannelizedE1serialInterface saveChannelizedE1serialInterface(PERouter routerInfo,
			ServiceDetail serviceDetail, Boolean isCpe, Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveChannelizedE1serialInterface - started");
		try {
			ChannelizedE1serialInterface channelizedE1serialInterfaceResp = null;
			ChannelizedE1serialInterface channelizedE1serialInterface = new ChannelizedE1serialInterface();
			if (null != routerInfo.getWanInterface().getEthernetInterface().getName()) {

				if (isCpe) {
					if ((Boolean) iasServiceMap.get("isWANInterfaceIPExists")) {
						channelizedE1serialInterface.setIpv4Address((String) iasServiceMap.get("WANInterfaceIP"));
					}
					if ( iasServiceMap.containsKey("CUSTOMERPREMISEQUIPMENT") && iasServiceMap.get("CUSTOMERPREMISEQUIPMENT") != null) {

						CramerCPE cramerCPE = (CramerCPE) iasServiceMap.get("CUSTOMERPREMISEQUIPMENT");

						if (cramerCPE != null) {

							channelizedE1serialInterface.setIpv6Address(cramerCPE.getWANV6InterfaceIP());
						}

					}
				} else {

					// note: vlan won't be there for e1 serial interface
					if (routerInfo.getWanInterface() != null
							&& routerInfo.getWanInterface().getSerialInterface() != null
							&& routerInfo.getWanInterface().getSerialInterface().getName() != null) {
						String nameInterface = routerInfo.getWanInterface().getSerialInterface().getName();
						if (isNotAlpha(nameInterface)) {
							channelizedE1serialInterface
									.setInterfaceName(StringUtils.trimToNull("Port " + nameInterface));
						} else {
							channelizedE1serialInterface.setInterfaceName(StringUtils.trimToNull(nameInterface));
						}
					}
					channelizedE1serialInterface.setChannelGroupNumber(StringUtils
							.trimToNull(routerInfo.getWanInterface().getSerialInterface().getChannelGroupNumber()));
					channelizedE1serialInterface.setDlciValue(
							StringUtils.trimToNull(routerInfo.getWanInterface().getSerialInterface().getDlciValue()));
					channelizedE1serialInterface.setFirsttimeSlot(StringUtils
							.trimToNull(routerInfo.getWanInterface().getSerialInterface().getTimeslot().get(0)));
					channelizedE1serialInterface.setLasttimeSlot(StringUtils
							.trimToNull(routerInfo.getWanInterface().getSerialInterface().getTimeslot().get(1)));
					channelizedE1serialInterface.setPortType(
							StringUtils.trimToNull(routerInfo.getWanInterface().getSerialInterface().getPortType()));
					channelizedE1serialInterface.setIpv4Address(StringUtils.trimToNull(
							routerInfo.getWanInterface().getSerialInterface().getV4IpAddress().getAddress()));
					channelizedE1serialInterface.setIpv6Address(StringUtils.trimToNull(
							routerInfo.getWanInterface().getSerialInterface().getV6IpAddress().getAddress()));
					channelizedE1serialInterface.setSecondaryIpv4Address(StringUtils.trimToNull(routerInfo
							.getWanInterface().getSerialInterface().getSecondaryV4WANIPAddress().getAddress()));
					channelizedE1serialInterface.setSecondaryIpv6Address(StringUtils.trimToNull(routerInfo
							.getWanInterface().getSerialInterface().getSecondaryV6WANIPAddress().getAddress()));
					channelizedE1serialInterface.setPhysicalPort(StringUtils
							.trimToNull(routerInfo.getWanInterface().getSerialInterface().getPhysicalPortName()));
					channelizedE1serialInterface.setEncapsulation(StringUtils
							.trimToNull(routerInfo.getWanInterface().getSerialInterface().getEncapsulation()));
					// isframed and framing should be saved from cramer
					/*
					 * channelizedE1serialInterface.setIsframed(isframed);
					 * channelizedE1serialInterface.setFraming(StringUtils.trimToNull(
					 * routerInfo.getWanInterface().getSerialInterface().get));
					 */
					String bfdRequired = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "bfdRequired", "LM", "A").getAttributeValue();
					if (null != bfdRequired && bfdRequired.equalsIgnoreCase("yes")) {
						channelizedE1serialInterface.setIsbfdEnabled((byte) 1);
						channelizedE1serialInterface.setBfdMultiplier("10");					
						channelizedE1serialInterface.setBfdreceiveInterval("100");
						channelizedE1serialInterface.setBfdtransmitInterval("100");
					} else {
						channelizedE1serialInterface.setIsbfdEnabled((byte) 0);
					}
				}
				
				channelizedE1serialInterface.setStartDate(startDate);
				channelizedE1serialInterface.setLastModifiedDate(lastModifiedDate);
				channelizedE1serialInterface.setModifiedBy(modifiedBy);
				channelizedE1serialInterface.setEndDate(endDate);
				channelizedE1serialInterfaceResp = channelizedE1serialInterfaceRepository
						.saveAndFlush(channelizedE1serialInterface);
				logger.info("Service Activation - saveChannelizedE1serialInterface - completed");

			} else {
				logger.error("saveChannelizedE1serialInterface failed as interface name is mandatory");
			}
			return channelizedE1serialInterfaceResp;
		} catch (Exception e) {
			logger.error("Exception in saveChannelizedE1serialInterface => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public RouterDetail saveRouterDetail(PERouter routerInfo) throws TclCommonException {
		logger.info("Service Activation - saveRouterDetail - started");
		try {
			RouterDetail routerDetail = new RouterDetail();
			routerDetail.setRouterHostname(routerInfo.getHostname());
			routerDetail.setRouterMake(routerInfo.getMake());
			routerDetail.setRouterModel(routerInfo.getModel());
			if (null != routerInfo.getV4ManagementIPAddress()) {
				routerDetail.setIpv4MgmtAddress(routerInfo.getV4ManagementIPAddress());
			}

			if (null != routerInfo.getV6ManagementIPAddress()) {
				routerDetail.setIpv6MgmtAddress(routerInfo.getV6ManagementIPAddress());
			}

			routerDetail.setStartDate(startDate);
			routerDetail.setLastModifiedDate(lastModifiedDate);
			routerDetail.setModifiedBy(modifiedBy);
			routerDetail.setEndDate(endDate);
			RouterDetail routerDetailSaved = routerDetailRepository.saveAndFlush(routerDetail);
			logger.info("Service Activation - saveRouterDetail - completed");
			return routerDetailSaved;
		} catch (Exception e) {
			logger.error("Exception in saveRouterDetail => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public  String getIpAddressSplit(String ipAddress,int count){
		
		String array[]=ipAddress.split("/");
		String ssMgmtIp=array[0];
	    String[] splitValue = ssMgmtIp.split("\\.");
	    splitValue[splitValue.length-1] = String.valueOf(Integer.parseInt(splitValue[splitValue.length-1])+count);
	    String output = Arrays.asList(splitValue).stream().map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));
	    return output;
	}
	
	public  String getIpAddressSplitForCustomerProvidedWan(String ipAddress,int count){
		String array[]=ipAddress.split("/");
		String ssMgmtIp=array[0];
	    String[] splitValue = ssMgmtIp.split("\\.");
	    splitValue[splitValue.length-1] = String.valueOf(Integer.parseInt(splitValue[splitValue.length-1])-count);
	    String output = Arrays.asList(splitValue).stream().map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));
	    return output;
	}
	/* for CPE - interface is always ethernet only */
	public EthernetInterface saveEthernetInterface(ServiceDetail serviceDetail, Map<String, Object> iasServiceMap,
			Boolean isCpe) throws TclCommonException {
		logger.info("Service Activation - saveEthernetInterface - started");
		try {
			PERouter routerInfo = (PERouter) iasServiceMap.get("peRouter");
			EthernetInterface ethernetInterfaceSaved = null;
			if (null != routerInfo.getWanInterface().getEthernetInterface().getName()) {
				EthernetInterface ethernetInterface = new EthernetInterface();
				ethernetInterface.setInnerVlan(
						StringUtils.trimToNull(routerInfo.getWanInterface().getEthernetInterface().getVlan()));
				if (isCpe) {
					if ((Boolean) iasServiceMap.get("isWANInterfaceIPExists")) {
						ethernetInterface.setIpv4Address((String) iasServiceMap.get("WANInterfaceIP"));

					}
					
					ScComponentAttribute attribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									serviceDetail.getScServiceDetailId(), "wanIpAddress", "LM", "A");
					ScComponentAttribute wanIpProvidedByCustAttribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpProvidedByCust", "LM", "A");

						if (attribute != null && attribute.getAttributeValue() != null && !attribute.getAttributeValue().isEmpty()) {
							if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
								ethernetInterface.setIpv4Address(getIpAddressSplit(attribute.getAttributeValue(), 0));
							}/*else{
								ethernetInterface.setIpv4Address(getIpAddressSplit(attribute.getAttributeValue(), 2));
							}*/
						}

					if (iasServiceMap.containsKey("CUSTOMERPREMISEQUIPMENT")
							&& iasServiceMap.get("CUSTOMERPREMISEQUIPMENT") != null) {

						CramerCPE cramerCPE = (CramerCPE) iasServiceMap.get("CUSTOMERPREMISEQUIPMENT");

						if (cramerCPE != null) {

							ethernetInterface.setIpv6Address(cramerCPE.getWANV6InterfaceIP());
						}

					}
				} else {
					if ((Boolean) iasServiceMap.get("isEthernetV4IPExists")) {
						ethernetInterface.setIpv4Address(
								routerInfo.getWanInterface().getEthernetInterface().getV4IpAddress().getAddress());
						
						
					}
					
					ScComponentAttribute wanIpProvidedByCustAttribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpProvidedByCust", "LM", "A");
					
					if("IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
						logger.info("IZOPC::saveEthernetInterface:: for ServiceId::{}",serviceDetail.getServiceId());
						ScComponentAttribute attribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "vsnlWanIpAddress", "LM", "A");
						if (attribute != null && attribute.getAttributeValue() != null && !attribute.getAttributeValue().isEmpty()) {
							if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
								ethernetInterface.setIpv4Address(getIpAddressSplit(attribute.getAttributeValue(), 0));
							}
						}
					}else {
						logger.info("Other than IZOPC::saveEthernetInterface:: for ServiceId::{}",serviceDetail.getServiceId());
						ScComponentAttribute attribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpAddress", "LM", "A");
						if (attribute != null && attribute.getAttributeValue() != null && !attribute.getAttributeValue().isEmpty()) {
							if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
								Boolean isOddIpAddressExists=false;
								isOddIpAddressExists=checkIpAddressEvenOrOdd(serviceDetail,attribute.getAttributeValue(),isOddIpAddressExists);
								if(isOddIpAddressExists) {
									logger.info("ADDSITE ServiceId::{} with odd ipaddress exists",serviceDetail.getId());
									ethernetInterface.setIpv4Address(getIpAddressSplit(attribute.getAttributeValue(), 1));
								}else {
									logger.info("Other than ADDSITE ServiceId::{} or without odd ipaddress exists",serviceDetail.getId());
									ethernetInterface.setIpv4Address(getIpAddressSplitForCustomerProvidedWan(attribute.getAttributeValue(), 1));
								}
								
							}/*else{
								ethernetInterface.setIpv4Address(getIpAddressSplit(attribute.getAttributeValue(), 1));
							}*/
							

						}
					}
						

					ethernetInterface.setPhysicalPort(StringUtils
							.trimToNull(routerInfo.getWanInterface().getEthernetInterface().getPhysicalPortName()));
					
					if (routerInfo.getWanInterface() != null
							&& routerInfo.getWanInterface().getEthernetInterface() != null
							&& routerInfo.getWanInterface().getEthernetInterface().getName() != null) {
						String nameInterface = routerInfo.getWanInterface().getEthernetInterface().getName();
						if (isNotAlpha(nameInterface)) {
							ethernetInterface.setInterfaceName(StringUtils.trimToNull("Port " + nameInterface));
						} else {
							ethernetInterface.setInterfaceName(StringUtils.trimToNull(nameInterface));
						}
					}
					String bfdRequired = "";
					ScComponentAttribute bfdRequiredComponent = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									serviceDetail.getScServiceDetailId(), "bfdRequired", "LM", "A");
					if(bfdRequiredComponent!=null) {
						bfdRequired = bfdRequiredComponent.getAttributeValue();
					}
					if (StringUtils.isNotEmpty(bfdRequired) && bfdRequired.equalsIgnoreCase("yes")) {
						ethernetInterface.setIsbfdEnabled((byte) 1);

						ethernetInterface.setBfdMultiplier("10");					
						ethernetInterface.setBfdreceiveInterval("100");
						ethernetInterface.setBfdtransmitInterval("100");
					} else {
						ethernetInterface.setIsbfdEnabled((byte) 0);
					}

					// TODO: if(perouter make is only cisco then save setQosLoopinPassthrough)
					ethernetInterface.setQosLoopinPassthrough(StringUtils.trimToNull(
							routerInfo.getWanInterface().getEthernetInterface().getQosLoopinPassthroughInterface()));
					if(!"LAGInterface".equalsIgnoreCase(routerInfo.getWanInterface().getEthernetInterface().getPortType())){
						if (Objects.nonNull(routerInfo.getWanInterface().getEthernetInterface().getPortType())
								&& routerInfo.getWanInterface().getEthernetInterface().getPortType().toLowerCase().contains("giga")) {
							ethernetInterface.setPortType("GIGE");
						}else if (Objects.nonNull(routerInfo.getWanInterface().getEthernetInterface().getPortType())
								&& routerInfo.getWanInterface().getEthernetInterface().getPortType().toLowerCase().contains("fast")) {
							ethernetInterface.setPortType("FE");
						}else{
							ethernetInterface.setPortType(
									StringUtils.trimToNull(routerInfo.getWanInterface().getEthernetInterface().getPortType()));
						}
					}
					ethernetInterface.setMediaType(
							StringUtils.trimToNull(routerInfo.getWanInterface().getEthernetInterface().getMediaType()));

					if ((Boolean) iasServiceMap.get("isEthernetV6IPExists")) {
						ethernetInterface.setIpv6Address(
								routerInfo.getWanInterface().getEthernetInterface().getV6IpAddress().getAddress());
					}
					
					

					if ((Boolean) iasServiceMap.get("isEthernetSecondaryV4WANIPExists")) {
						ethernetInterface.setSecondaryIpv4Address(routerInfo.getWanInterface().getEthernetInterface()
								.getSecondaryV4WANIPAddress().getAddress());
					}

					if ((Boolean) iasServiceMap.get("isEthernetSecondaryV6WANIPExists")) {
						ethernetInterface.setSecondaryIpv6Address(routerInfo.getWanInterface().getEthernetInterface()
								.getSecondaryV6WANIPAddress().getAddress());
					}
					
					if(routerInfo.getWanInterface()!=null && routerInfo.getWanInterface().getEthernetInterface()!=null ){
						if(routerInfo.getWanInterface().getEthernetInterface().getSvlan()!=null){
							ethernetInterface.setOuterVlan(routerInfo.getWanInterface().getEthernetInterface().getSvlan());
						}else if(routerInfo.getWanInterface().getEthernetInterface().getVlan()!=null){
							ethernetInterface.setOuterVlan(routerInfo.getWanInterface().getEthernetInterface().getVlan());
						}
					}

					ethernetInterface.setMode(
							StringUtils.trimToNull(routerInfo.getWanInterface().getEthernetInterface().getMode()));

					/*
					 * if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent") && (Boolean)
					 * iasServiceMap.get("isHsrpPresent")) {
					 * ethernetInterface.setIshsrpEnabled((byte) 1); } else {
					 * ethernetInterface.setIshsrpEnabled((byte) 0); }
					 */

					/*
					 * if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent") && (Boolean)
					 * iasServiceMap.get("isVrrpPresent")) { // note: vrrp is only for juniper
					 * ethernetInterface.setIsvrrpEnabled((byte) 1); } else {
					 * ethernetInterface.setIsvrrpEnabled((byte) 0); }
					 */
					ethernetInterface.setStartDate(startDate);
					ethernetInterface.setLastModifiedDate(lastModifiedDate);
					ethernetInterface.setModifiedBy(modifiedBy);
					ethernetInterface.setEndDate(endDate);
				}
				ethernetInterfaceSaved = ethernetInterfaceRepository.saveAndFlush(ethernetInterface);
				logger.info("Service Activation - saveEthernetInterface - completed");

				if ((Boolean) iasServiceMap.get("isLanRoutingProtocolPresent")) {
					if ((Boolean) iasServiceMap.get("isHsrpPresent")) {
						saveHsrpVrrpProtocol(serviceDetail, ethernetInterfaceSaved, iasServiceMap);
					}

					if ((Boolean) iasServiceMap.get("isVrrpPresent")) {
						saveHsrpVrrpProtocol(serviceDetail, ethernetInterfaceSaved, iasServiceMap);
					}
				}
			} else {
				logger.error("Ethernet interface save failed as Ethernet interface name is mandatory");
			}
			return ethernetInterfaceSaved;
		} catch (Exception e) {
			logger.error("Exception in saveEthernetInterface => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private Boolean checkIpAddressEvenOrOdd(ServiceDetail serviceDetail, String attributeValue,Boolean isOddIpAddressExists) {
		logger.info("checkIpAddressEvenOrOdd method invoked for ServiceId::{} with wanIpAddress::{}",serviceDetail.getId(),attributeValue);
		if("ADD_SITE".equalsIgnoreCase(serviceDetail.getOrderCategory()) || (serviceDetail.getOrderSubCategory()!=null && serviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
			 String wanIpAddress=attributeValue;
			logger.info("Check ADDSITE ServiceId::{} with OddIpAddress::{}",serviceDetail.getId(),wanIpAddress);
			    String[] wanIpAddressSubnetSplit=wanIpAddress.split("/");
			    if(wanIpAddressSubnetSplit.length==2 && wanIpAddressSubnetSplit[1].equals("30")) {
			    	logger.info("Check ADDSITE ServiceId::{} with specific subnet",serviceDetail.getId());
			    	String[] wanIpAddressSplit=wanIpAddressSubnetSplit[0].split("\\.");
			    	if(wanIpAddressSplit.length==4) {
			    		logger.info("Check ADDSITE ServiceId::{} with valid Ipaddress",serviceDetail.getId());
			    		Integer lastIpValue=Integer.valueOf(wanIpAddressSplit[3]);
			    		if(lastIpValue%2==1) {
			    			logger.info("ADDSITE ServiceId::{} with odd ipaddress",serviceDetail.getId());
			    			isOddIpAddressExists=true;
			    		}
			    	}
			    }
		}
		return isOddIpAddressExists;
	}


	public Boolean isNotAlpha(String name) {
		return name != null && name.chars().noneMatch(Character::isLetter);
	}

	public ChannelizedSdhInterface saveChannelizedSdhInterface(ServiceDetail serviceDetail, PERouter routerInfo,
			Boolean isCpe, Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveChannelizedSdhInterface - started");
		try {
			ChannelizedSdhInterface channelizedSdhInterface = new ChannelizedSdhInterface();
			if (isCpe) {
				if ((Boolean) iasServiceMap.get("isWANInterfaceIPExists")) {
					channelizedSdhInterface.setIpv4Address((String) iasServiceMap.get("WANInterfaceIP"));
				}
				if ( iasServiceMap.containsKey("CUSTOMERPREMISEQUIPMENT") && iasServiceMap.get("CUSTOMERPREMISEQUIPMENT") != null) {

					CramerCPE cramerCPE = (CramerCPE) iasServiceMap.get("CUSTOMERPREMISEQUIPMENT");

					if (cramerCPE != null) {

						channelizedSdhInterface.setIpv6Address(cramerCPE.getWANV6InterfaceIP());
					}

				}
			} else {
				if (routerInfo.getWanInterface() != null
						&& routerInfo.getWanInterface().getChannelizedSDHInterface() != null
						&& routerInfo.getWanInterface().getChannelizedSDHInterface().getName() != null) {
					String nameInterface = routerInfo.getWanInterface().getChannelizedSDHInterface().getName();
					if (isNotAlpha(nameInterface)) {
						channelizedSdhInterface.setInterfaceName(StringUtils.trimToNull("Port " + nameInterface));
					} else {
						channelizedSdhInterface.setInterfaceName(StringUtils.trimToNull(nameInterface));
					}
				}
				if (routerInfo.getWanInterface() != null
						&& routerInfo.getWanInterface().getChannelizedSDHInterface() != null) {
					channelizedSdhInterface.setPhysicalPort(StringUtils
							.trimToNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getPhysicalPortName()));
					if (Objects.nonNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getV4IpAddress()))
						channelizedSdhInterface.setIpv4Address(StringUtils.trimToNull(
								routerInfo.getWanInterface().getChannelizedSDHInterface().getV4IpAddress().getAddress()));
					if (Objects.nonNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getV6IpAddress()))
						channelizedSdhInterface.setIpv6Address(StringUtils.trimToNull(
								routerInfo.getWanInterface().getChannelizedSDHInterface().getV6IpAddress().getAddress()));
					channelizedSdhInterface.setSecondaryIpv4Address(StringUtils.trimToNull(routerInfo.getWanInterface()
							.getChannelizedSDHInterface().getSecondaryV4WANIPAddress().getAddress()));
					channelizedSdhInterface.setSecondaryIpv6Address(StringUtils.trimToNull(routerInfo.getWanInterface()
							.getChannelizedSDHInterface().getSecondaryV6WANIPAddress().getAddress()));
					channelizedSdhInterface.setJ(Integer
							.parseInt(Objects.nonNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getJ())
									? routerInfo.getWanInterface().getChannelizedSDHInterface().getJ()
									: "0"));
					channelizedSdhInterface.setKlm(
							StringUtils.trimToNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getKLM()));
					channelizedSdhInterface.setChannelGroupNumber(StringUtils
							.trimToNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getChannelGroupNumber()));
					channelizedSdhInterface.setDlciValue(StringUtils
							.trimToNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getDlciValue()));
					if(!routerInfo.getWanInterface().getChannelizedSDHInterface().getSixtyFourKTimeslot().isEmpty()){
					channelizedSdhInterface.set_4Kfirsttime_slot(StringUtils.trimToNull(
							routerInfo.getWanInterface().getChannelizedSDHInterface().getSixtyFourKTimeslot().get(0)));
					channelizedSdhInterface.set_4klasttimeSlot(StringUtils.trimToNull(
							routerInfo.getWanInterface().getChannelizedSDHInterface().getSixtyFourKTimeslot().get(1)));
				}
					channelizedSdhInterface.setDlciValue(StringUtils
							.trimToNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getDlciValue()));
					channelizedSdhInterface.setEncapsulation(StringUtils
							.trimToNull(routerInfo.getWanInterface().getChannelizedSDHInterface().getEncapsulation()));
				}
				String bfdRequired = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),"bfdRequired", "LM", "A").getAttributeValue();
				if (null != bfdRequired && bfdRequired.equalsIgnoreCase("yes")) {
					channelizedSdhInterface.setIsbfdEnabled((byte) 1);
					channelizedSdhInterface.setBfdMultiplier("10");					
					channelizedSdhInterface.setBfdreceiveInterval("100");
					channelizedSdhInterface.setBfdtransmitInterval("100");
				} else {
					channelizedSdhInterface.setIsbfdEnabled((byte) 0);
				}
			}
			channelizedSdhInterface.setStartDate(startDate);
			channelizedSdhInterface.setLastModifiedDate(lastModifiedDate);
			channelizedSdhInterface.setModifiedBy(modifiedBy);
			channelizedSdhInterface.setEndDate(endDate);
			ChannelizedSdhInterface channelizedSdhInterfaceSaved = channelizedSdhInterfaceRepository
					.saveAndFlush(channelizedSdhInterface);
			logger.info("Service Activation - saveChannelizedSdhInterface - completed");
			return channelizedSdhInterfaceSaved;
		} catch (Exception e) {
			logger.error("Exception in saveChannelizedSdhInterface => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public LmComponent saveLastMile(ServiceDetail serviceDetail, String provider) throws TclCommonException {
		logger.info("Service Activation - saveLastMile - started");
		try {
			LmComponent lmComponent = new LmComponent();
			lmComponent.setLmOnwlProvider(provider);
			lmComponent.setServiceDetail(serviceDetail);
			lmComponent.setVersion(1);
			lmComponent.setStartDate(startDate);
			lmComponent.setLastModifiedDate(lastModifiedDate);
			lmComponent.setStatus("ISSUED");
			lmComponent.setModifiedBy(modifiedBy);
			lmComponent.setEndDate(endDate);
			LmComponent resp = lmComponentRepository.saveAndFlush(lmComponent);
			logger.info("Service Activation - saveLastMile - completed");
			return resp;
		} catch (Exception e) {
			logger.error("Exception in saveLastMile => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public WimaxLastmile saveWimaxLastmile(LmComponent lmComponent, CramerWimaxLastmile wimax)
			throws TclCommonException {
		logger.info("Service Activation - saveWimaxLastmile - started");
		try {
			WimaxLastmile wimaxLastmile = new WimaxLastmile();
			wimaxLastmile.setBtsIp(wimax.getBTSIP());
			wimaxLastmile.setBtsName(wimax.getBTSName());
			wimaxLastmile.setLmComponent(lmComponent);
			wimaxLastmile.setStartDate(startDate);
			wimaxLastmile.setLastModifiedDate(lastModifiedDate);
			wimaxLastmile.setModifiedBy(modifiedBy);
			wimaxLastmile.setEndDate(endDate);
			WimaxLastmile wimaxLastmileSaved = wimaxLastmileRepository.saveAndFlush(wimaxLastmile);
			logger.info("Service Activation - saveWimaxLastmile - completed");
			return wimaxLastmileSaved;
		} catch (Exception e) {
			logger.error("Exception in saveWimaxLastmile => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Topology saveTopology(ServiceDetail serviceDetail, CramerEthernetTopologyInfo topologyInfo)
			throws TclCommonException {
		logger.info("Service Activation - saveTopology - started");
		try {
			Topology topology = new Topology();
			topology.setTopologyName(StringUtils.trimToNull(topologyInfo.getName()));
			topology.setStartDate(startDate);
			topology.setLastModifiedDate(lastModifiedDate);
			topology.setModifiedBy(modifiedBy);
			topology.setEndDate(endDate);
			topology.setServiceDetail(serviceDetail);
			topology = topologyRepository.saveAndFlush(topology);
			logger.info("Service Activation - saveTopology - completed");
			return topology;
		} catch (Exception e) {
			logger.error("Exception in saveTopology => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public RouterUplinkport saveRouterUplinkPort(CramerEthernetInterface routerTopologyInterface1,
			CramerEthernetInterface routerTopologyInterface2, Topology topology) throws TclCommonException {
		logger.info("Service Activation - saveRouterUplinkPort - started");
		try {
			if (routerTopologyInterface1!=null && routerTopologyInterface1.getPhysicalPortName() != null) {
				RouterUplinkport routerUplinkport = new RouterUplinkport();
				if (Objects.nonNull(routerTopologyInterface1)) {
					routerUplinkport.setPhysicalPort1Name(
							StringUtils.trimToNull(routerTopologyInterface1.getPhysicalPortName()));
				}

				if (Objects.nonNull(routerTopologyInterface2)) {
					routerUplinkport.setPhysicalPort2Name(
							StringUtils.trimToNull(routerTopologyInterface2.getPhysicalPortName()));
				}
				routerUplinkport.setStartDate(startDate);
				routerUplinkport.setLastModifiedDate(lastModifiedDate);
				routerUplinkport.setModifiedBy(modifiedBy);
				routerUplinkport.setEndDate(endDate);
				routerUplinkport.setTopology(topology);
				routerUplinkport = routerUplinkPortRepository.saveAndFlush(routerUplinkport);
				logger.info("Service Activation - saveRouterUplinkPort - completed");
				return routerUplinkport;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Exception in saveRouterUplinkPort => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public com.tcl.dias.serviceactivation.entity.entities.Multicasting saveMultiCasting(Multicasting multicastInput, ServiceDetail serviceDetail)
			throws TclCommonException {
		logger.info("Service Activation - saveMultiCasting - started");
		try {
			if(multicastInput.getDataMDT()!=null || multicastInput.getDefaultMDT()!=null) {
			com.tcl.dias.serviceactivation.entity.entities.Multicasting multiCastingEntity = new com.tcl.dias.serviceactivation.entity.entities.Multicasting();
			multiCastingEntity.setDefaultMdt(StringUtils.trimToNull(multicastInput.getDefaultMDT()));
			multiCastingEntity.setDataMdt(StringUtils.trimToNull(multicastInput.getDataMDT()));
			multiCastingEntity.setWanPimMode("SPARSE");
			multiCastingEntity.setType("STATICRP");
			multiCastingEntity.setDataMdtThreshold("1");
			multiCastingEntity.setRpLocation("CE");
			multiCastingEntity.setAutoDiscoveryOption("MDT_SAFI");
			multiCastingEntity.setStartDate(startDate);
			multiCastingEntity.setLastModifiedDate(lastModifiedDate);
			multiCastingEntity.setModifiedBy(modifiedBy);
			multiCastingEntity.setEndDate(endDate);
			multiCastingEntity.setServiceDetail(serviceDetail);
			multiCastingEntity = multicastingRepository.saveAndFlush(multiCastingEntity);
			logger.info("Service Activation - saveMultiCasting - completed");
			return multiCastingEntity;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Exception in saveMultiCasting => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public UniswitchDetail saveUniSwitchDetail(ServiceDetail serviceDetail, Switch switchDetail, Topology topology)
			throws TclCommonException {
		logger.info("Service Activation - saveUniSwitchDetail - started");
		try {

			if (switchDetail != null && switchDetail.getHostname() != null) {

				UniswitchDetail uniswitchDetail = new UniswitchDetail();
				uniswitchDetail.setHostName(StringUtils.trimToNull(switchDetail.getHostname()));
				uniswitchDetail.setSwitchModel(StringUtils.trimToNull(switchDetail.getModel()));
				uniswitchDetail.setMgmtIp(StringUtils.trimToNull(switchDetail.getV4ManagementIPAddress()));
				uniswitchDetail.setInterfaceName(StringUtils.trimToNull(switchDetail.getUNIinterface().getName()));
				uniswitchDetail.setMediaType(StringUtils.trimToNull(switchDetail.getUNIinterface().getMediaType()));
				uniswitchDetail.setMode(StringUtils.trimToNull(switchDetail.getUNIinterface().getMode()));
				if (Objects.nonNull(switchDetail.getUNIinterface().getPortType())
						&& switchDetail.getUNIinterface().getPortType().toLowerCase().contains("giga")) {
					uniswitchDetail.setPortType("GIGE");
				} else if (Objects.nonNull(switchDetail.getUNIinterface().getPortType())
						&& switchDetail.getUNIinterface().getPortType().toLowerCase().contains("fast")) {
					uniswitchDetail.setPortType("FE");
				} else if (Objects.nonNull(switchDetail.getUNIinterface().getPortType())
						&& switchDetail.getUNIinterface().getPortType().toLowerCase().contains("lag")) {
					uniswitchDetail.setPortType("LAG");
				}
				uniswitchDetail
						.setPhysicalPort(StringUtils.trimToNull(switchDetail.getUNIinterface().getPhysicalPortName()));
				uniswitchDetail.setInnerVlan(StringUtils.trimToNull(switchDetail.getUNIinterface().getVlan()));
				uniswitchDetail.setHandoff(StringUtils.trimToNull(switchDetail.getUNIinterface().getHandOff()));
				if(Objects.nonNull(switchDetail.getUNIinterface().isSyncVLAN()) && switchDetail.getUNIinterface().isSyncVLAN() == true){
					logger.info("SyncVLAN exists");
					uniswitchDetail.setSyncVlanReqd((byte)1);
					if(Objects.nonNull(switchDetail.getModel()) && !switchDetail.getModel().isEmpty() && 
							switchDetail.getModel().toLowerCase().contains("cisco")){ //14-7-2020[Sandesh's input to change]
						logger.info("SyncVlan based on Cisco device");
						uniswitchDetail.setSyncVlanReqd((byte)0);
					}
					ScComponentAttribute lmType = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
									"lastMileProvider", "LM", "A");
					if(Objects.nonNull(lmType) && Objects.nonNull(lmType.getAttributeValue()) && !lmType.getAttributeValue().isEmpty()
							&& lmType.getAttributeValue().toLowerCase().contains("radwin") && lmType.getAttributeValue().toLowerCase().contains("tcl")
							&& lmType.getAttributeValue().toLowerCase().contains("pop") && Objects.nonNull(switchDetail.getUNIinterface().getMode())
							&& !switchDetail.getUNIinterface().getMode().isEmpty() && "Access".equalsIgnoreCase(switchDetail.getUNIinterface().getMode())
							&& Objects.nonNull(switchDetail.getModel()) && !switchDetail.getModel().isEmpty() 
							&& !switchDetail.getModel().toLowerCase().contains("cisco")){
						logger.info("Access To Trunk");
						uniswitchDetail.setMode("TRUNK");
					}
				}else if(Objects.nonNull(switchDetail.getUNIinterface().isSyncVLAN()) && switchDetail.getUNIinterface().isSyncVLAN() == false){
					logger.info("SyncVLAN not exists");
					uniswitchDetail.setSyncVlanReqd((byte)0);
				}
				if(Objects.nonNull(switchDetail.getUNIinterface().getUniquePortID()) && !switchDetail.getUNIinterface().getUniquePortID().isEmpty()){
					uniswitchDetail.setUniquePortId(switchDetail.getUNIinterface().getUniquePortID());
				}
				uniswitchDetail.setStartDate(startDate);
				uniswitchDetail.setLastModifiedDate(lastModifiedDate);
				uniswitchDetail.setModifiedBy(modifiedBy);
				uniswitchDetail.setEndDate(endDate);

				if (Objects.nonNull(topology)) {
					uniswitchDetail.setTopology(topology);
				}
				uniswitchDetail = uniswitchDetailRepository.saveAndFlush(uniswitchDetail);
				logger.info("Service Activation - saveUniSwitchDetail - completed");
				return uniswitchDetail;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Exception in saveUniSwitchDetail => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public IpaddrLanv4Address saveIpaddrLanv4Address(ServiceDetail serviceDetail, IPV4Address lanv4Addr, byte[] count)
			throws TclCommonException {
		logger.info("Service Activation - saveIpaddrLanv4Address - started");
		try {
			IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
			ipaddrLanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
			ipaddrLanv4Address.setIscustomerprovided((byte) 0);

			if (null != lanv4Addr.getAddress()) {
				ipaddrLanv4Address.setLanv4Address(lanv4Addr.getAddress());
			} 

			ipaddrLanv4Address.setIssecondary((byte) count[0]);
			ipaddrLanv4Address.setStartDate(startDate);
			ipaddrLanv4Address.setLastModifiedDate(lastModifiedDate);
			ipaddrLanv4Address.setModifiedBy(modifiedBy);
			ipaddrLanv4Address.setEndDate(endDate);
			ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
			logger.info("Service Activation - saveIpaddrLanv4Address - completed");
			return ipaddrLanv4Address;
		} catch (Exception e) {
			logger.error("Exception in saveIpaddrLanv4Address => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	public IpaddrLanv6Address saveIpaddrLanv6Address(ServiceDetail serviceDetail, IPV6Address lanv6Addr, byte[] count)
			throws TclCommonException {
		logger.info("Service Activation - saveIpaddrLanv6Address - started");
		try {
			IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
			ipaddrLanv6Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V6"));
			ipaddrLanv6Address.setIscustomerprovided((byte) 0);
			ipaddrLanv6Address.setIssecondary((byte) count[0]);

			if (null != lanv6Addr.getAddress()) {
				ipaddrLanv6Address.setLanv6Address(lanv6Addr.getAddress());
			} 
			ipaddrLanv6Address.setStartDate(startDate);
			ipaddrLanv6Address.setLastModifiedDate(lastModifiedDate);
			ipaddrLanv6Address.setModifiedBy(modifiedBy);
			ipaddrLanv6Address.setEndDate(endDate);
			ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
			logger.info("Service Activation - saveIpaddrLanv6Address - completed");
			return ipaddrLanv6Address;
		} catch (Exception e) {
			logger.error("Exception in saveIpaddrLanv6Address => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public List<IpaddrWanv4Address> saveIpaddrWanv4Address(ServiceDetail serviceDetail, IPV4Address wanv4Addr, boolean isWanIpv4)
			throws TclCommonException {
		logger.info("Service Activation - saveIpaddrWanv4Address - started");
		List<IpaddrWanv4Address> wanv4AddrList = new ArrayList<>();
		
		try {
		ScComponentAttribute attribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpAddress", "LM", "A");
		ScComponentAttribute wanIpProvidedByCustAttribute=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "wanIpProvidedByCust", "LM", "A");

		ScComponentAttribute rfMake = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
						"rfMake", "LM", "A");
		ScComponentAttribute lastMileProvider = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
						"lastMileProvider", "LM", "A");
		
		String deviceType = null;
		if (rfMake != null && Objects.nonNull(rfMake.getAttributeValue()) && !rfMake.getAttributeValue().isEmpty()) {
			deviceType = rfMake.getAttributeValue();
			logger.info("RFMake::{}",deviceType);
		}else{
			ScComponentAttribute rfTechnology = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"rfTechnology", "LM", "A");
			if(Objects.nonNull(rfTechnology) && Objects.nonNull(rfTechnology.getAttributeValue()) && !rfTechnology.getAttributeValue().isEmpty()){
				deviceType = rfTechnology.getAttributeValue();
				logger.info("RFTechnology::{}",deviceType);
			}
		}
		
			List<Boolean> isPrimaryList = new ArrayList<>();
			if (Objects.nonNull(wanv4Addr)) {
				IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
				try {
					ipaddrWanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
					ipaddrWanv4Address.setIssecondary((byte) 0);
					
					if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
						ipaddrWanv4Address.setIscustomerprovided((byte) 1);
					}else{
						ipaddrWanv4Address.setIscustomerprovided((byte) 0);
					}

					if (!isPrimaryList.isEmpty()) {
						ipaddrWanv4Address.setIssecondary((byte) 1);
					} else {
						ipaddrWanv4Address.setIssecondary((byte) 0);
					}

					if (null != wanv4Addr.getAddress()) {
						//ipaddrWanv4Address.setWanv4Address(wanv4Addr.getAddress());
						String ipAddr[]=wanv4Addr.getAddress().split("/");
						
						if(!ipAddr[1].equals("29") || (Objects.nonNull(lastMileProvider) && Objects.nonNull(lastMileProvider.getAttributeValue()) && !lastMileProvider.getAttributeValue().isEmpty()
								&& !lastMileProvider.getAttributeValue().toLowerCase().contains("radwin") 
								&& !lastMileProvider.getAttributeValue().toLowerCase().contains("pop") && ipAddr[1].equals("29"))){
							logger.info("Ip not equal to 29 or its is other than radwin from tcl pop");
							ipaddrWanv4Address.setWanv4Address(wanv4Addr.getAddress());
						}
						
						
						Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository
								.findById(serviceDetail.getScServiceDetailId());
						if (scServiceDetailOptional.isPresent()) {
							ScServiceDetail scServiceDetail = scServiceDetailOptional.get();

							if (attribute != null && attribute.getAttributeValue() != null && !attribute.getAttributeValue().isEmpty()) {
								if(isWanIpv4){
									logger.info("WAN IPv4");
									if(Objects.nonNull(wanIpProvidedByCustAttribute) && Objects.nonNull(wanIpProvidedByCustAttribute.getAttributeValue()) && "Yes".equalsIgnoreCase(wanIpProvidedByCustAttribute.getAttributeValue())){
										logger.info("WanIp customer provided");
										ipaddrWanv4Address.setWanv4Address(getIpAddressSplitForCustomerProvidedWan(attribute.getAttributeValue(),2)+"/"+subnet(attribute.getAttributeValue()).trim());
									}else{
										logger.info("WanIp Not customer provided");
										//ipaddrWanv4Address.setWanv4Address(attribute.getAttributeValue());
										if(!ipAddr[1].equals("29") || (Objects.nonNull(lastMileProvider) && Objects.nonNull(lastMileProvider.getAttributeValue()) && !lastMileProvider.getAttributeValue().isEmpty()
												&& !lastMileProvider.getAttributeValue().toLowerCase().contains("radwin") 
												&& !lastMileProvider.getAttributeValue().toLowerCase().contains("pop") && ipAddr[1].equals("29"))){
											logger.info("Ip not equal to 29 or its is other than radwin from tcl pop");
											ipaddrWanv4Address.setWanv4Address(wanv4Addr.getAddress());
										}
									}
								}else{
									logger.info("Secondary WAN IPv4");
									ipaddrWanv4Address.setWanv4Address(attribute.getAttributeValue());
								}
							}
						}
					} 

					if(Objects.nonNull(ipaddrWanv4Address.getWanv4Address()) && !ipaddrWanv4Address.getWanv4Address().isEmpty()){
						logger.info("saveIpaddrWanv4Address.WAN exists::{}",ipaddrWanv4Address.getWanv4Address());
						ipaddrWanv4Address.setStartDate(startDate);
						ipaddrWanv4Address.setLastModifiedDate(lastModifiedDate);
						ipaddrWanv4Address.setModifiedBy(modifiedBy);
						ipaddrWanv4Address.setEndDate(endDate);
						ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
						wanv4AddrList.add(ipaddrWanv4Address);
						isPrimaryList.add(true);
					}
				} catch (TclCommonException e) {
					logger.error("Exception in saveIpaddrWanv4Address stream => {}", e);
				}
			}
			logger.info("Service Activation - saveIpaddrWanv4Address - completed");
			return wanv4AddrList;
		} catch (Exception e) {
			logger.error("Exception in saveIpaddrWanv4Address => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public List<IpaddrWanv6Address> saveIpaddrWanv6Address(ServiceDetail serviceDetail, IPV6Address wanv6Addr)
			throws TclCommonException {
		logger.info("Service Activation - saveIpaddrWanv6Address - started");
		List<IpaddrWanv6Address> wanv6AddrList = new ArrayList<>();
		try {
			List<Boolean> isPrimaryList = new ArrayList<>();
			if (Objects.nonNull(wanv6Addr)) {
				IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
				try {
					String ipAddressProvidedBy = "TCL";
					ipaddrWanv6Address.setIssecondary((byte) 0);
					if (ipAddressProvidedBy.equalsIgnoreCase("TCL")) {
						ipaddrWanv6Address.setIscustomerprovided((byte) 0);
					} else {
						ipaddrWanv6Address.setIscustomerprovided((byte) 1);
					}

					ipaddrWanv6Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V6"));

					if (!isPrimaryList.isEmpty()) {
						ipaddrWanv6Address.setIssecondary((byte) 1);
					} else {
						ipaddrWanv6Address.setIssecondary((byte) 0);
					}

					if (null != wanv6Addr.getAddress()) {
						ipaddrWanv6Address.setWanv6Address(wanv6Addr.getAddress());
					} 

					ipaddrWanv6Address.setStartDate(startDate);
					ipaddrWanv6Address.setLastModifiedDate(lastModifiedDate);
					ipaddrWanv6Address.setModifiedBy(modifiedBy);
					ipaddrWanv6Address.setEndDate(endDate);
					ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
					wanv6AddrList.add(ipaddrWanv6Address);
					isPrimaryList.add(true);
				} catch (TclCommonException e) {
					logger.error("Exception in saveIpaddrWanv6Address stream => {}", e);
				}
			}
			logger.info("Service Activation - saveIpaddrWanv6Address - completed");
			return wanv6AddrList;
		} catch (Exception e) {
			logger.error("Exception in saveIpaddrWanv6Address => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/* Only one entry per request, path ip type can be v4/v6/DUALSTACK */
	public IpAddressDetail saveIpAddressDetail(ServiceDetail serviceDetail, String pathIpType)
			throws TclCommonException {
		logger.info("Service Activation - saveIpAddressDetail - started");
		IpAddressDetail ipAddressDetailExisting = ipAddressDetailRepository
				.findByServiceDetailId(serviceDetail.getId());
		try {
			// saveAclPolicyCriteria(serviceDetail,pathIpType,serviceDetail.getServiceSubtype());

			if (ipAddressDetailExisting != null
					&& !ipAddressDetailExisting.getPathIpType().equalsIgnoreCase(pathIpType)) {
				pathIpType = "DUALSTACK";
				ipAddressDetailExisting.setPathIpType(pathIpType);
				ipAddressDetailExisting.setServiceDetail(serviceDetail);
				int extendedLanRequired = 0;
				ScComponentAttribute scComponentAttributeExtendedLanRequired = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
								"extendedLanRequired", "LM", "A");
				if (null != scComponentAttributeExtendedLanRequired) {
					extendedLanRequired = scComponentAttributeExtendedLanRequired.getAttributeValue()
							.equalsIgnoreCase("Yes") ? 1 : 0;
				}

				ipAddressDetailExisting.setExtendedLanEnabled((byte) extendedLanRequired);

				// should get from cramer and only for gvpn (check the excel for different
				// xml's)
				ipAddressDetailExisting.setNniVsatIpaddress(null);
				ipAddressDetailExisting.setStartDate(startDate);
				ipAddressDetailExisting.setLastModifiedDate(lastModifiedDate);
				ipAddressDetailExisting.setModifiedBy(modifiedBy);
				if("IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
					logger.info("Service Activation - saveIpAddressDetail existing - for IZOPC Service Id::{}",serviceDetail.getServiceId());
					ScComponentAttribute publicNATIpProvidedByAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
									"publicNATIpProvidedBy", "LM", "A");
					ScComponentAttribute publicNATIpAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
									"publicNATIp", "LM", "A");
					if(publicNATIpProvidedByAttribute!=null && publicNATIpProvidedByAttribute.getAttributeValue()!=null) {
						logger.info("publicNATIpProvidedByAttribute exists::{} for IZOPC Service Id::{}",publicNATIpProvidedByAttribute.getAttributeValue(),serviceDetail.getServiceId());
						ipAddressDetailExisting.setPublicNatProvidedBy(publicNATIpProvidedByAttribute.getAttributeValue());
					}
					if(publicNATIpAttribute!=null && publicNATIpAttribute.getAttributeValue()!=null) {
						logger.info("publicNATIpAttribute exists::{} for IZOPC Service Id::{}",publicNATIpAttribute.getAttributeValue(),serviceDetail.getServiceId());
						ipAddressDetailExisting.setPublicNat(publicNATIpAttribute.getAttributeValue());
					}
				}

				ipAddressDetailRepository.saveAndFlush(ipAddressDetailExisting);
				return ipAddressDetailExisting;
			} else if (ipAddressDetailExisting == null) {
				IpAddressDetail ipAddressDetail = new IpAddressDetail();
				ipAddressDetail.setPathIpType(pathIpType);
				ipAddressDetail.setServiceDetail(serviceDetail);

				int extendedLanRequired = 0;
				ScComponentAttribute scComponentAttributeExtendedLanRequired = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
								"extendedLanRequired", "LM", "A");
				if (null != scComponentAttributeExtendedLanRequired) {
					extendedLanRequired = scComponentAttributeExtendedLanRequired.getAttributeValue()
							.equalsIgnoreCase("Yes") ? 1 : 0;
				}

				ipAddressDetail.setExtendedLanEnabled((byte) extendedLanRequired);

				// should get from cramer and only for gvpn (check the excel for different
				// xml's)
				ipAddressDetail.setNniVsatIpaddress(null);
				ipAddressDetail.setStartDate(startDate);
				ipAddressDetail.setLastModifiedDate(lastModifiedDate);
				ipAddressDetail.setModifiedBy(modifiedBy);
				if("IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
					logger.info("Service Activation - saveIpAddressDetail - for IZOPC Service Id::{}",serviceDetail.getServiceId());
					ScComponentAttribute publicNATIpProvidedByAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
									"publicNATIpProvidedBy", "LM", "A");
					ScComponentAttribute publicNATIpAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
									"publicNATIp", "LM", "A");
					if(publicNATIpProvidedByAttribute!=null && publicNATIpProvidedByAttribute.getAttributeValue()!=null) {
						logger.info("publicNATIpProvidedByAttribute exists::{} for IZOPC Service Id::{}",publicNATIpProvidedByAttribute.getAttributeValue(),serviceDetail.getServiceId());
						ipAddressDetail.setPublicNatProvidedBy(publicNATIpProvidedByAttribute.getAttributeValue());
					}
					if(publicNATIpAttribute!=null && publicNATIpAttribute.getAttributeValue()!=null) {
						logger.info("publicNATIpAttribute exists::{} for IZOPC Service Id::{}",publicNATIpAttribute.getAttributeValue(),serviceDetail.getServiceId());
						ipAddressDetail.setPublicNat(publicNATIpAttribute.getAttributeValue());
					}
				}
				ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
				return ipAddressDetail;
			}
			logger.info("Service Activation - saveIpAddressDetail - completed");
			return ipAddressDetailExisting;
		} catch (Exception e) {
			logger.error("Exception in saveIpAddressDetail => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Vrf saveVrf(ServiceDetail serviceDetail, PERouter peRouter, Map<String, Object> iasServiceMap)
			throws TclCommonException {
		logger.info("Service Activation - saveVrf - started");
		try {
			Vrf vrf = new Vrf();
			vrf.setServiceDetail(serviceDetail);
			Boolean islANIPV4isCustomerProvided = false;

			List<AceIPMapping> aceIPMappings = aceIPMappingRepository
					.findAllByScServiceDetail_Id(serviceDetail.getScServiceDetailId());

			if (!aceIPMappings.isEmpty()) {
				islANIPV4isCustomerProvided = true;
			}
			
			Integer bgpAsnumber=0;
			
			String accessType="Private";
			
			ScComponentAttribute bgpAsnumbeVal=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "bgpAsNumber", "LM", "A");
			
			if(bgpAsnumbeVal!=null) {
				bgpAsnumber=Integer.valueOf(bgpAsnumbeVal.getAttributeValue());
				
				if((bgpAsnumber>=64512 && bgpAsnumber<=65535) ) {
					accessType="Private";
	
				}else {
					accessType="Public";
				}
			} else {
				logger.info("bgpAsnumbeVal doesn't exists ");
				Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository
						.findById(serviceDetail.getScServiceDetailId());
				if (scServiceDetailOptional.isPresent()) {
					ScServiceDetail scServiceDetail = scServiceDetailOptional.get();
					String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());
					String orderSubCategory = scServiceDetail.getOrderSubCategory();
					String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());

					if (!orderType.equalsIgnoreCase("NEW")
							&& !"ADD_SITE".equals(orderCategory)
							&& (Objects.isNull(orderSubCategory) || Objects.nonNull(orderSubCategory) 
									&& !scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
						ServiceDetail activeServiceDetail = serviceDetailRepository
								.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceDetail.getServiceId(),
										"ACTIVE");
						if (Objects.nonNull(activeServiceDetail)) {
							logger.info("Active Service Detail exists::{}",activeServiceDetail.getId());
							/*List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = activeServiceDetail
									.getInterfaceProtocolMappings().stream()
									.filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
									.collect(Collectors.toList());*/
							
							List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = interfaceProtocolMappingRepository.findByServiceDetailIdAndEndDateIsNull(activeServiceDetail.getId());
							logger.info("routerInterfaceProtocolMappings for::{},=>{}",activeServiceDetail.getId(),routerInterfaceProtocolMappings);
							if (!routerInterfaceProtocolMappings.isEmpty()) {
								logger.info("Prev Router Protocol not empty");
								routerInterfaceProtocolMappings
										.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
								InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings
										.get(0);
								if (Objects.nonNull(routerInterfaceProtocolMapping.getBgp()) && Objects
										.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteAsNumber())) {
									logger.info("Prev Bgp Remote AsNumber exists");
									if ((routerInterfaceProtocolMapping.getBgp().getRemoteAsNumber() >= 64512 
											&& routerInterfaceProtocolMapping.getBgp().getRemoteAsNumber() <= 65535)) {
										logger.info("Bgp Remote AsNumber condition met");
										accessType = "Private";
									} else {
										logger.info("Bgp Remote AsNumber condition not met");
										accessType = "Public";
									}
								}
							}
						}
					}
				}
			}
			
			if (!peRouter.getMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER) || !islANIPV4isCustomerProvided
					|| accessType.equalsIgnoreCase("Private")) {
				if("ILL".equalsIgnoreCase(serviceDetail.getServiceType())){
					if (serviceDetail.getServiceSubtype().equalsIgnoreCase(AceConstants.SERVICE.SERVICE_SUBTYPE_DIL)) {
						vrf.setVrfName(AceConstants.VRF.PRIMUS_INTERNET);
					} else if (!serviceDetail.getServiceSubtype().equalsIgnoreCase(AceConstants.SERVICE.SERVICE_SUBTYPE_DIL)
							&& !islANIPV4isCustomerProvided) {
						vrf.setVrfName(AceConstants.VRF.INTERNET_VPN);
					}
				}else {
					vrf.setVrfName(AceConstants.DEFAULT.NOT_APPLICABLE);
				}
			} else {
				vrf.setVrfName(AceConstants.DEFAULT.NOT_APPLICABLE);
			}
			if (peRouter.getMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER) || islANIPV4isCustomerProvided
					|| accessType.equalsIgnoreCase("Public")) {
				vrf.setVrfName(AceConstants.DEFAULT.NOT_APPLICABLE);

			}
			
			if("ILL".equalsIgnoreCase(serviceDetail.getServiceType())){
				logger.info("ILL VRF NAME CHECK");
				ScServiceAttribute scIpServiceAttribute = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeName(serviceDetail.getScServiceDetailId(),
								CramerConstants.IP_ADDR_ATTR_NAME);
				ScServiceAttribute scAdditionalIpServiceAttribute = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeName(serviceDetail.getScServiceDetailId(),
								CramerConstants.ADDITIONAL_IP_ATTR_NAME);
				if (scIpServiceAttribute != null && scIpServiceAttribute.getAttributeValue()!=null && !scIpServiceAttribute.getAttributeValue().isEmpty() && !"IPv4".equals(scIpServiceAttribute.getAttributeValue())){
						logger.info("ILL NA FOR IP_ADDR");
						vrf.setVrfName(AceConstants.DEFAULT.NOT_APPLICABLE);
				}
				if (scAdditionalIpServiceAttribute != null && scAdditionalIpServiceAttribute.getAttributeValue()!=null && !scAdditionalIpServiceAttribute.getAttributeValue().isEmpty() && !"IPv4".equals(scAdditionalIpServiceAttribute.getAttributeValue())){
					logger.info("ILL NA FOR ADD_IP_ADDR");
					vrf.setVrfName(AceConstants.DEFAULT.NOT_APPLICABLE);
				}
			}
			
				
			
			
			if ((peRouter.getMake().equalsIgnoreCase("alcatel") || peRouter.getMake().equalsIgnoreCase("alcatel ip")
					|| peRouter.getMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER))
							&& vrf.getVrfName().equalsIgnoreCase(AceConstants.DEFAULT.NOT_APPLICABLE)) {
				saveAluSammgrSeq(serviceDetail);
			}

			vrf.setStartDate(startDate);
			vrf.setLastModifiedDate(lastModifiedDate);
			vrf.setModifiedBy(modifiedBy);

			// take the type from service fulfillment ILL\GVPN, save vrf only for ILL. GVPN
			// - future it may be required
			if (serviceDetail.getServiceType().equalsIgnoreCase("ILL")
					|| serviceDetail.getServiceType().equalsIgnoreCase("IAS") || serviceDetail.getServiceType().equalsIgnoreCase("GVPN")) {
				vrfRepository.saveAndFlush(vrf);
			}
			logger.info("VRF NAME {}::",vrf.getVrfName());
			// below is relevant to ILL only.
			if (vrf.getVrfName().equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)
					|| vrf.getVrfName().equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {
				logger.info("VRF METADATA");
				saveVpnMetaData(serviceDetail);
				saveVpnSolution(serviceDetail, peRouter, iasServiceMap, vrf.getVrfName());
			}
			return vrf;
		} catch (Exception e) {
			logger.error("Exception in saveVrf => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/* note: call this method if the pe make is only alcatel ip */
	public AluSammgrSeq saveAluSammgrSeq(ServiceDetail serviceDetail) throws TclCommonException {
		logger.info("Service Activation - saveAluSammgrSeq - started");
		try {
			AluSammgrSeq oldSammgrSeq = aluSammgrSeqRepository
					.findFirstByServiceIdOrderBySammgrSeqidDesc(serviceDetail.getServiceId());

			if (oldSammgrSeq != null) {
				return oldSammgrSeq;
			}
			AluSammgrSeq aluSammgrSeq = new AluSammgrSeq();
			aluSammgrSeq.setServiceId(serviceDetail.getServiceId());
			aluSammgrSeq.setServiceType(serviceDetail.getServiceType());
			aluSammgrSeq = aluSammgrSeqRepository.saveAndFlush(aluSammgrSeq);
			logger.info("Service Activation - saveAluSammgrSeq - completed");
			return aluSammgrSeq;
		} catch (Exception e) {
			logger.error("Exception in saveAluSammgrSeq => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public ServiceQo saveServiceQo(ServiceDetail serviceDetail) throws TclCommonException {
		logger.info("Service Activation - saveServiceQo - started");
		try {
			ServiceQo serviceQo = new ServiceQo();
			serviceQo.setCosType("6COS");
			serviceQo.setNcTraffic((byte) 0);
			serviceQo.setServiceDetail(serviceDetail);

			// TODO: if it is ILL then qos should be created COS_TYPE SHOULD BE SAVED 6COS
			// VALUE THEN SERVICE COS CRITERIA TABLE THEN SAVE THE DEFAULT ENTRIES
			// TODO: COS_PERCENT = 100%,
			// TODO: if it is GVPN then need to get from l20 and save all the 6 cos and 1
			// qos

			// TODO: setCosPackage only for ill and service subtype as eill value should
			// come l20
			// TODO: should come from setNcTraffic => for both ill & gvpn
			serviceQo.setCosPackage("");
			serviceQo.setStartDate(startDate);
			serviceQo.setLastModifiedDate(lastModifiedDate);
			serviceQo.setModifiedBy(modifiedBy);
			serviceQo = serviceQoRepository.saveAndFlush(serviceQo);
			logger.info("Service Activation - saveServiceQo - completed");
			return serviceQo;
		} catch (Exception e) {
			logger.error("Exception in saveServiceQo => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public ServiceCosCriteria saveServiceCosCritera(ServiceDetail serviceDetail) throws TclCommonException {
		logger.info("Service Activation - saveServiceCosCritera - started");
		try {

			// TODO: for gvpn 6 records needs to be inserted based on l20 values
			ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
			serviceCosCriteria.setCosPercent("100");
			serviceCosCriteria.setCosName("COS6");
			serviceCosCriteria.setClassificationCriteria("ipprecedence");

			// note -> classifcation criterias are DSCP, ANY, IP Address, ipprecedence, ACL
			// for ip address.

			serviceCosCriteria.setIpprecedenceVal1("1");
			serviceCosCriteria.setIpprecedenceVal2("2");
			serviceCosCriteria.setIpprecedenceVal3("3");
			serviceCosCriteria.setIpprecedenceVal4("4");
			serviceCosCriteria.setIpprecedenceVal5("5");
			serviceCosCriteria.setIpprecedenceVal6("6");
			serviceCosCriteria.setIpprecedenceVal7("7");
			serviceCosCriteria.setIpprecedenceVal8("8");
			ServiceQo serviceQo = saveServiceQo(serviceDetail);
			Float serviceBandwidth = serviceDetail.getServiceBandwidth();
			String serviceBandwidthUnit = serviceDetail.getServiceBandwidthUnit();
			if(Objects.nonNull(serviceDetail.getBurstableBw()) && Objects.nonNull(serviceDetail.getBurstableBwUnit())){
				logger.info("ILL Burstable Bw");
				serviceCosCriteria.setBwBpsunit(String.valueOf(banwidthConversion(serviceDetail.getBurstableBwUnit(), serviceDetail.getBurstableBw())));
			}else if(Objects.nonNull(serviceBandwidth)){
				logger.info("ILL Service Bw");
				serviceCosCriteria.setBwBpsunit(String.valueOf(banwidthConversion(serviceBandwidthUnit, serviceBandwidth)));
			}
			serviceCosCriteria.setStartDate(startDate);
			serviceCosCriteria.setLastModifiedDate(lastModifiedDate);
			serviceCosCriteria.setModifiedBy(modifiedBy);
			serviceCosCriteria.setServiceQo(serviceQo);
			serviceCosCriteria = serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
			logger.info("Service Activation - saveServiceCosCritera - completed");
			return serviceCosCriteria;
		} catch (Exception e) {
			logger.error("Exception in saveServiceCosCritera => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private Float banwidthConversion(String bandwidthUnit, Float bandwidth) {
		if (bandwidth == null) {
			return new Float(0);
		}
		if (bandwidthUnit.equalsIgnoreCase("KBPS")) {
			return bandwidth * 1000;
		}
		else if (bandwidthUnit.equalsIgnoreCase("MBPS")) {
			if(bandwidth>999){
				logger.info("Greater than 999::Mbps");
				return bandwidth * 1000 * 1000;
			}else{
				logger.info("Less than 1000::Mbps");
				return bandwidth * 1024 * 1000;
			}
		} else if (bandwidthUnit.equalsIgnoreCase("GBPS")) {
			if(bandwidth>999){
				logger.info("Greater than 999::Gbps");
				return bandwidth * 1000 * 1000 * 1000;
			}else{
				logger.info("Less than 1000::Gbps");
				return bandwidth * 1000 * 1024 * 1000;
			}
		}
		return bandwidth;
	}

	public VpnMetatData saveVpnMetaData(ServiceDetail serviceDetail) throws TclCommonException {
		logger.info("Service Activation - VpnMetaData - started");
		try {
			VpnMetatData vpnMetatData = new VpnMetatData();
			vpnMetatData.setServiceDetail(serviceDetail);
			vpnMetatData.setE2eIntegrated(false);
			// note: if ILL then site role will be spoke and topogy will be Hub & Spoke by
			// default
			if (serviceDetail.getServiceType().equalsIgnoreCase("ILL")
					|| serviceDetail.getServiceType().equalsIgnoreCase("IAS")) {
				vpnMetatData.setL2oSiteRole("SPOKE");
				vpnMetatData.setL2OTopology("Hub & Spoke");
			} else if (serviceDetail.getServiceType().equalsIgnoreCase("GVPN")) {
				logger.info("CRAMER GVPN VPN METADATA");
				List<ScServiceAttribute>  serviceAttributes=	scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),Arrays.asList("Site Type","VPN Topology"));
				Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
				String siteType=attributeMap.getOrDefault("Site Type", null);
				String vpnTopology=attributeMap.getOrDefault("VPN Topology", null);
				vpnMetatData.setL2oSiteRole(siteType!=null?siteType.toUpperCase():siteType);
				vpnMetatData.setL2OTopology(vpnTopology!=null?vpnTopology.toUpperCase():vpnTopology);
				vpnMetatData.setModifiedBy(modifiedBy);
			}
			vpnMetatData.setIsUa("NA");
			vpnMetatData = vpnMetaDataRepository.saveAndFlush(vpnMetatData);
			logger.info("Service Activation - VpnMetaData - ended");
			return vpnMetatData;
		} catch (Exception e) {
			logger.error("Exception in saveVpnSolution => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public VpnSolution saveVpnSolution(ServiceDetail serviceDetail, PERouter peRouter,
			Map<String, Object> iasServiceMap, String vrfName) throws TclCommonException {
		logger.info("Service Activation - saveVpnSolution - started");
		try {
			VpnSolution vpnSolution = new VpnSolution();
			vpnSolution.setVpnLegId(serviceDetail.getServiceId());
			if (serviceDetail.getServiceType().equalsIgnoreCase("IAS")
					|| serviceDetail.getServiceType().equalsIgnoreCase("ILL")) {
				vpnSolution.setVpnTopology("HUBnSPOKE");
				vpnSolution.setLegRole("SPOKE");

			}
			else if (serviceDetail.getServiceType().equalsIgnoreCase("GVPN")) {

				List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),
								Arrays.asList("Site Type", "VPN Topology"));

				Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);

				String siteType = attributeMap.getOrDefault("Site Type", null);
				String vpnTopology = attributeMap.getOrDefault("VPN Topology", null);
				if (vpnTopology != null) {
					vpnSolution.setVpnTopology(vpnTopology.toUpperCase().contains("HUB") ? "HUBnSPOKE" : vpnTopology.toUpperCase());
				}
				if (siteType != null) {
					vpnSolution.setLegRole(siteType.toUpperCase());
				}
			}
			vpnSolution.setVpnType("CUSTOMER");
			vpnSolution.setVpnSolutionName(serviceDetail.getServiceId());
			vpnSolution.setServiceDetail(serviceDetail);
			vpnSolution.setVpnName(vrfName != null ? vrfName.replaceAll(" ", "_") : "");

			if (Objects.nonNull(peRouter)) {
				if ((Boolean) iasServiceMap.get("isEthernetInterface")) {

					vpnSolution.setInterfaceName(
							((CramerEthernetInterface) iasServiceMap.get("ethernetInterface")).getName());
				} else if ((Boolean) iasServiceMap.get("isE1SerialInterface")) {
					vpnSolution.setInterfaceName(
							((CramerSerialInterface) iasServiceMap.get("e1SerialInterface")).getName());
				} else if ((Boolean) iasServiceMap.get("isSHDInterface")) {
					vpnSolution.setInterfaceName(((CramerSDHInterface) iasServiceMap.get("sHDInterface")).getName());
				} else {
					vpnSolution.setInterfaceName("");
				}
			} else {
				vpnSolution.setInterfaceName("");
			}

			vpnSolution.setSiteId(serviceDetail.getServiceId());

			vpnSolution.setStartDate(startDate);
			vpnSolution.setLastModifiedDate(lastModifiedDate);
			vpnSolution.setModifiedBy(modifiedBy);
			vpnSolutionRepository.saveAndFlush(vpnSolution);
			logger.info("Service Activation - saveVpnSolution - completed");
			return vpnSolution;
		} catch (Exception e) {
			logger.error("Exception in saveVpnSolution => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	
	public RadwinLastmile saveRadwinLastMile(LmComponent lmComponent, CramerWimaxLastmile cramerWimaxLastmile,
			ServiceDetail serviceDetail, Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveRadwinLastMile - started");
		try {
			RadwinLastmile radwinLastMile = new RadwinLastmile();
			//btsIp
			ScComponentAttribute btsIpComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "btsIp", "LM", "A");
			if (btsIpComponent != null && btsIpComponent.getAttributeValue() != null
					&& !btsIpComponent.getAttributeValue().isEmpty()) {
				radwinLastMile.setBtsIp(btsIpComponent.getAttributeValue());
			}
			//btsName
			ScComponentAttribute btsNameComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "btsId", "LM", "A");

			if (btsNameComponent != null && btsNameComponent.getAttributeValue() != null
					&& !btsNameComponent.getAttributeValue().isEmpty()) {
				radwinLastMile.setBtsName(btsNameComponent.getAttributeValue());
			}

			//sectorId
			ScComponentAttribute sectorId = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "sectorId", "LM", "A");

			if (sectorId != null && sectorId.getAttributeValue() != null) {

				radwinLastMile.setSectorId(sectorId.getAttributeValue());
			}
			//frequency
			ScComponentAttribute rfFrequencyComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "customerRadioFrequency", "LM", "A");
			if(rfFrequencyComponent!=null && rfFrequencyComponent.getAttributeValue()!=null && !rfFrequencyComponent.getAttributeValue().isEmpty()){
				logger.info("Custom Radio Freq exists");
				radwinLastMile.setFrequency(rfFrequencyComponent.getAttributeValue().replace(".", ""));
			}
			radwinLastMile.setSiteLat(serviceDetail.getLat());
			radwinLastMile.setSiteLong(serviceDetail.getLongiTude());
			String suiP = null;
			ScComponentAttribute suIpComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "suIp", "LM", "A");
			if (suIpComponent == null) {
				suIpComponent = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								serviceDetail.getScServiceDetailId(), "hsuIp", "LM", "A");

				suiP = suIpComponent != null ? suIpComponent.getAttributeValue() : null;
			} else {
				suiP = suIpComponent != null ? suIpComponent.getAttributeValue() : null;

			}
			ScComponentAttribute suMacAddress = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "suMacAddress", "LM", "A");
			if (suMacAddress != null) {
				radwinLastMile.setHsuMacAddr(suMacAddress.getAttributeValue().toLowerCase());
			}

			// TODO: remove hard coding
			radwinLastMile.setHsuIp(suiP);
			radwinLastMile.setHsuSubnet("255.255.255.0");
			radwinLastMile
					.setGatewayIp(Objects.nonNull(radwinLastMile.getHsuIp()) && !radwinLastMile.getHsuIp().isEmpty()
							? getManagementIpGateway(radwinLastMile.getHsuIp()) : radwinLastMile.getHsuIp());

			logger.info("isUNISwitchPresent {}, uniswitch{}", iasServiceMap.get("isUNISwitchPresent"),
					iasServiceMap.get("uniswitch"));

			if ((Boolean) iasServiceMap.get("isUNISwitchPresent") && iasServiceMap.get("uniswitch") != null) {
				Switch uniSwitch = (Switch) iasServiceMap.get("uniswitch");
				radwinLastMile.setDataVlanid(null != uniSwitch.getUNIinterface()
						? StringUtils.trimToNull(uniSwitch.getUNIinterface().getVlan()) : null);
			} else {
				radwinLastMile.setDataVlanid(null);
			}
			radwinLastMile.setMgmtVlanid(cramerWimaxLastmile.getManagementVID());
			radwinLastMile.setLmComponent(lmComponent);
			radwinLastMile = radwinLastmileRepository.saveAndFlush(radwinLastMile);
			logger.info("Service Activation - saveRadwinLastMile - completed");
			return radwinLastMile;
		} catch (Exception e) {
			logger.error("Exception in saveWanStaticRoutes => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public CambiumLastmile saveCambiumLastmile(LmComponent lmComponent, CramerWimaxLastmile cramerWimaxLastmile,
			ServiceDetail serviceDetail, Map<String, Object> iasServiceMap) throws TclCommonException {
		logger.info("Service Activation - saveCambiumLastmile - started");
		try {
			CambiumLastmile cambiumLastmile = new CambiumLastmile();
			cambiumLastmile.setBsIp(StringUtils.trimToNull(cramerWimaxLastmile.getBTSIP()));
			cambiumLastmile.setBsName(StringUtils.trimToNull(cramerWimaxLastmile.getBTSName()));

			String btsSuip = null;
			ScComponentAttribute btsSuIpComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "suIp", "LM", "A");
			if (btsSuIpComponent == null) {
				btsSuIpComponent = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								serviceDetail.getScServiceDetailId(), "hsuIp", "LM", "A");

				btsSuip = btsSuIpComponent != null ? btsSuIpComponent.getAttributeValue() : null;
			} else {
				btsSuip = btsSuIpComponent != null ? btsSuIpComponent.getAttributeValue() : null;

			}
			cambiumLastmile.setMgmtIpForSsSu(btsSuip);
			cambiumLastmile.setMgmtIpGateway(
					Objects.nonNull(cambiumLastmile.getMgmtIpForSsSu()) && !cambiumLastmile.getMgmtIpForSsSu().isEmpty()
							? getManagementIpGateway(cambiumLastmile.getMgmtIpForSsSu())
							: cambiumLastmile.getMgmtIpForSsSu());
			cambiumLastmile.setMgmtSubnetForSsSu("255.255.255.0");

			logger.info("isUNISwitchPresent {}, uniswitch{}", iasServiceMap.get("isUNISwitchPresent"),
					iasServiceMap.get("uniswitch"));

			if ((Boolean) iasServiceMap.get("isUNISwitchPresent") && iasServiceMap.get("uniswitch") != null) {
				Switch uniSwitch = (Switch) iasServiceMap.get("uniswitch");
				cambiumLastmile.setDefaultPortVid(null != uniSwitch.getUNIinterface()
						? StringUtils.trimToNull(uniSwitch.getUNIinterface().getVlan()) : null);
			} else {
				cambiumLastmile.setDefaultPortVid(null);
			}

			try {

				Map<String, String> antennaHeights = commonFulfillmentUtils.getComponentAttributesDetails(
						Arrays.asList("mastHeight", "poleHeight", "towerHeight", "buildingHeight"),
						serviceDetail.getScServiceDetailId(), "LM", "A");

				Double antenaHeight = 0D;
				if (!antennaHeights.isEmpty()) {

					for (Map.Entry<String, String> entry : antennaHeights.entrySet()) {
						if (entry.getValue() != null) {
							antenaHeight = antenaHeight + Double.valueOf(entry.getValue());
						}
					}

				}

				cambiumLastmile.setSmHeight(String.valueOf(antenaHeight));
			} catch (Exception e) {
				logger.error("Exception in antennaHeights => {}", e);
			}
			ScComponentAttribute colorCode = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "sectorId", "LM", "A");
			if (colorCode != null) {
				cambiumLastmile.setColorCode1(colorCode.getAttributeValue());

			} else {
				cambiumLastmile.setColorCode1("1");

			}
			cambiumLastmile.setLongitudeSettings(serviceDetail.getLongiTude());
			cambiumLastmile.setLatitudeSettings(serviceDetail.getLat());

			ScComponentAttribute rfFrequencyComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "customerRadioFrequency", "LM", "A");
			if(rfFrequencyComponent!=null && rfFrequencyComponent.getAttributeValue()!=null && !rfFrequencyComponent.getAttributeValue().isEmpty()){
				logger.info("Custom Radio Freq exists");
				cambiumLastmile.setCustomRadioFrequencyList(rfFrequencyComponent.getAttributeValue().replace(".", ""));
			}
			cambiumLastmile.setMgmtVid(cramerWimaxLastmile.getManagementVID());

			ScComponentAttribute suMacAddressComponent = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "suMacAddress", "LM", "A");
			cambiumLastmile.setSuMacAddress(null != suMacAddressComponent
					? StringUtils.trimToNull(suMacAddressComponent.getAttributeValue().toLowerCase()) : null);

			ScComponentAttribute suMacAddress = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "suMacAddress", "LM", "A");
			if (suMacAddress != null) {
				cambiumLastmile.setSuMacAddress(suMacAddress.getAttributeValue().toLowerCase());
			}
			ScComponentAttribute rfMakeModel = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "rfMakeModel", "LM", "A");

			if (rfMakeModel != null && rfMakeModel.getAttributeValue()!=null && rfMakeModel.getAttributeValue().contains("450")) {
				logger.info("Cambium 450i");
				cambiumLastmile.setDeviceType("Cambium450i");
			} else {
				logger.info("Cambium");
				cambiumLastmile.setDeviceType("Cambium");
			}

			cambiumLastmile.setLmComponent(lmComponent);
			cambiumLastmile = cambiumLastmileRepository.saveAndFlush(cambiumLastmile);

			logger.info("Service Activation - saveCambiumLastmile - completed");
			return cambiumLastmile;
		} catch (Exception e) {
			logger.error("Exception in saveCambiumLastmile => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	//
	public static String getManagementIpGateway(String ssMgmtIp) {
		String[] splitValue = ssMgmtIp.split("\\.");
		splitValue[splitValue.length - 1] = "1";
		String output = Arrays.asList(splitValue).stream().map(eachVal -> eachVal.toString())
				.collect(Collectors.joining("."));
		return output;
	}
	
	
	public ServiceDetail saveNPLServiceDetailsActivation(String serviceCode,Integer version,ScOrder scOrder, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesAMap) {
		ServiceDetail serviceDetailActivation = new ServiceDetail();
		if (Objects.nonNull(scServiceDetail)) {
			serviceDetailActivation.setServiceId(scServiceDetail.getUuid());
			serviceDetailActivation.setServiceSubtype(scServiceDetail.getErfPrdCatalogOfferingName());
			serviceDetailActivation.setOrderCategory(scServiceDetail.getOrderCategory());
			serviceDetailActivation.setOrderType(scServiceDetail.getOrderType());
			serviceDetailActivation.setServiceType(CramerConstants.NPL);
			serviceDetailActivation.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
			String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			serviceDetailActivation.setOrderType(orderType);
			serviceDetailActivation.setOrderCategory(orderCategory);
			String serviceSubType = StringUtils.trimToEmpty(scComponentAttributesAMap.get("serviceSubType"));
			
			if(serviceSubType.equalsIgnoreCase("intercity")) {
				serviceDetailActivation.setServiceSubtype(CramerConstants.NPL);
			}else {
				serviceDetailActivation.setServiceSubtype("NPL Intracity");
			}
				

			serviceDetailActivation.setScServiceDetailId(scServiceDetail.getId());
			String latLong[]=scComponentAttributesAMap.get("latLong").split(",");
			serviceDetailActivation.setLat(latLong[0]);
			serviceDetailActivation.setLongiTude(latLong[1]);
			serviceDetailActivation.setServiceComponenttype("TX");
			serviceDetailActivation.setLastmileType(scComponentAttributesAMap.get("lmType"));
			serviceDetailActivation.setLastmileProvider(scComponentAttributesAMap.get("lmType"));
			Instant instant = Instant.now();
			long timeStampMillis = instant.toEpochMilli();
			//Start of MACD
			ServiceDetail prevActiveServiceDetail=null;

			if(!orderType.equalsIgnoreCase("NEW")){
				prevActiveServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(),TaskStatusConstants.ACTIVE);
				if(Objects.nonNull(prevActiveServiceDetail)){
					serviceDetailActivation.setNetpRefid(prevActiveServiceDetail.getNetpRefid());
					serviceDetailActivation.setExternalRefid(prevActiveServiceDetail.getExternalRefid());
				}
				serviceDetailActivation.setServiceOrderType(scServiceDetail.getErfPrdCatalogProductName()+"_MACD");
			}else{
				serviceDetailActivation.setNetpRefid("OPT_NETP_" + timeStampMillis);
				serviceDetailActivation.setExternalRefid("OPT_NETP_" + timeStampMillis);
				serviceDetailActivation.setServiceOrderType(scServiceDetail.getErfPrdCatalogProductName());
			}
			String localLoopBandwidth=scComponentAttributesAMap.get("localLoopBandwidth");
			String isTxDowntimeReqd=scComponentAttributesAMap.get("isTxDowntimeReqd");
			//End of MACD
			serviceDetailActivation.setIsportChanged((byte) 0);
			serviceDetailActivation.setIsrevised((byte) 0);
			serviceDetailActivation.setIsIdcService((byte) 0);
			serviceDetailActivation.setServiceState("ISSUED");
			serviceDetailActivation.setServiceBandwidth(Objects.nonNull(localLoopBandwidth)?Float.valueOf(localLoopBandwidth):null);
			serviceDetailActivation.setServiceBandwidthUnit(Objects.nonNull(localLoopBandwidth)?CramerConstants.Mbps:null);
			if(Objects.nonNull(isTxDowntimeReqd) && !isTxDowntimeReqd.isEmpty()){
				serviceDetailActivation.setIstxdowntimeReqd(isTxDowntimeReqd.equals("true")?(byte)1:(byte)0);
			}
			String txResourcePathType=scComponentAttributesAMap.get("txResourcePathType");
			if(Objects.nonNull(txResourcePathType) && !txResourcePathType.isEmpty()){
				serviceDetailActivation.setResourcePath(txResourcePathType);
			}
			String downtimeDuration=scComponentAttributesAMap.get("downtimeDuration");
			if(Objects.nonNull(downtimeDuration) && !downtimeDuration.isEmpty()){
				serviceDetailActivation.setDowntimeDuration(downtimeDuration);;
			}
			String fromTime=scComponentAttributesAMap.get("fromTime");
			if(Objects.nonNull(fromTime) && !fromTime.isEmpty()){
				serviceDetailActivation.setFromTime(fromTime);
			}
			String toTime=scComponentAttributesAMap.get("toTime");
			if(Objects.nonNull(toTime) && !toTime.isEmpty()){
				serviceDetailActivation.setToTime(toTime);;
			}
			String downtime=scComponentAttributesAMap.get("downtime");
			if(Objects.nonNull(downtime) && !downtime.isEmpty()){
				serviceDetailActivation.setDowntime(downtime);
			}
			serviceDetailActivation.setServiceBandwidthUnit(CramerConstants.Mbps);		

			serviceDetailActivation.setAddressLine1(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationAddressLineOne")));
			serviceDetailActivation.setAddressLine2(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationAddressLineTwo")));
			serviceDetailActivation.setAddressLine3(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationLocality")));
			serviceDetailActivation.setState(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationState")));
			serviceDetailActivation.setCity(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationCity")));
			serviceDetailActivation.setPincode(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationPincode")));
			serviceDetailActivation.setCountry(StringUtils.trimToNull(scComponentAttributesAMap.get("destinationCountry")));
		
			serviceDetailActivation.setOrderDetail(saveNPLOrderDetailsActivation(scServiceDetail.getScOrder(), scServiceDetail,scComponentAttributesAMap));
			if(version==null) {
				serviceDetailActivation.setVersion(1);
			}else {
				serviceDetailActivation.setVersion(version+1);
			}
			
			serviceDetailActivation.setMgmtType("UNMANAGED");		
			serviceDetailActivation.setSvclinkSrvid(null);
			serviceDetailActivation.setSvclinkRole(null);
			serviceDetailActivation.setRedundancyRole("SINGLE");
			serviceDetailActivation.setRouterMake("");
			serviceDetailActivation.setServiceState("ISSUED");
			serviceDetailActivation.setStartDate(startDate);
			serviceDetailActivation.setLastModifiedDate(lastModifiedDate);
			serviceDetailActivation.setModifiedBy(modifiedBy);
			serviceDetailActivation = serviceDetailRepository.saveAndFlush(serviceDetailActivation);
		
		}
		return serviceDetailActivation;

	}
	
	private OrderDetail saveNPLOrderDetailsActivation(ScOrder scOrder, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesAMap) {
		OrderDetail orderDetail = null;
		String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
		String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
		if (Objects.nonNull(scOrder)) {
			orderDetail = orderDetailRepository.findByExtOrderrefId(scOrder.getOpOrderCode());
			if (orderDetail == null) {
				orderDetail = new OrderDetail();
			//	orderDetail.setProjectName("GVPN TEST OPTIMUS");//need to remove once UAT done for GVPN
				//orderDetail.setOrderId(scOrder.getId());
				orderDetail.setOrderType(orderType);
				if(orderCategory==null)orderDetail.setOrderCategory("CUSTOMER_ORDER");
				else orderDetail.setOrderCategory(orderCategory);
				
				orderDetail.setOrderStatus(scOrder.getOrderStatus());
				orderDetail.setExtOrderrefId(scOrder.getUuid());
				orderDetail.setSfdcOpptyId(scOrder.getTpsSfdcOptyId());
				orderDetail.setCopfId(scOrder.getTpsCrmCofId());
				
				String crnId =scComponentAttributesAMap.get("customerRef");
				
				// TODO: setCopfId, setOptyBidCategory(eg: category1/category2),
				if (crnId != null) {
					orderDetail.setCustomerCrnId(crnId);
				} else {
					ScOrderAttribute attrs = scOrder.getScOrderAttributes().stream()
							.filter(attr -> attr.getAttributeName().equalsIgnoreCase("CRN_ID")).findFirst()
							.orElse(null);
					if(attrs!=null) {
					orderDetail.setCustomerCrnId(attrs.getAttributeValue());
					}
				}
				orderDetail.setCustCuId(Integer.parseInt(scOrder.getTpsSfdcCuid()));
				if(scOrder.getUuid().toLowerCase().contains("izosdwan")){
					orderDetail.setOptyBidCategory("CAT 3-4");
				}else{
					orderDetail.setOptyBidCategory("CAT 1-2");
				}
				orderDetail.setCustomerName(scOrder.getErfCustLeName());
				orderDetail.setCustomerEmail(scComponentAttributesAMap.get("localItContactEmailId"));
				orderDetail.setCustomerPhoneno(scComponentAttributesAMap.get("localItContactMobile"));
				ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
				orderDetail.setAddressLine1(scContractInfo.getBillingAddressLine1());
				orderDetail.setAddressLine2(scContractInfo.getBillingAddressLine2());
				orderDetail.setAddressLine3(scContractInfo.getBillingAddressLine3());
				orderDetail.setCity(StringUtils.trimToNull(scContractInfo.getBillingCity()));
				orderDetail.setLocation(StringUtils.trimToNull(scContractInfo.getBillingCity()));
				orderDetail.setState(scContractInfo.getBillingState());
				orderDetail.setCountry(scContractInfo.getBillingCountry());
				orderDetail.setPincode(scContractInfo.getBillingPincode());
				orderDetail.setCustomerType("Others");
				orderDetail.setCustomerCategory(
						null != scOrder.getCustomerSegment() ? scOrder.getCustomerSegment() : "Enterprise");
				orderDetail.setStartDate(startDate);
				orderDetail.setLastModifiedDate(lastModifiedDate);
				orderDetail.setModifiedBy(modifiedBy);
				orderDetail.setEndDate(endDate);
				orderDetail.setOriginatorName("OPTIMUS");
				String programName = scComponentAttributesAMap.get("programName");
				orderDetail.setProjectName(programName);
				orderDetail = orderDetailRepository.saveAndFlush(orderDetail);
			} else {
				return orderDetail;
			}
		}

		return orderDetail;
	}
	
	 @Transactional(readOnly=false,isolation = Isolation.READ_UNCOMMITTED)
	public void saveServiceDetails(String serviceCode,String orderCode, Integer scServiceDetailId){
		logger.info("ServiceAtivation.saveServiceDetails");
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);
		logger.info("serviceDetail {}",serviceDetail);
        if(null==serviceDetail) {
            // populating service activation logics
        	logger.info("Service Detail doesn't exists");
        	ScOrder scOrder=scOrderRepository.findDistinctByOpOrderCode(orderCode);
        	ServiceDetail activeServiceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ACTIVE);
            Integer version=null;
        	if(Objects.nonNull(activeServiceDetail)){
        		version=activeServiceDetail.getVersion();
        	}
        	ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();
    		
    		Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(scServiceDetailId, AttributeConstants.COMPONENT_LM, "A");
    		

    		serviceDetail=saveServiceDetailsActivation(serviceCode,version,scOrder,scServiceDetail,scComponentAttributesAMap);
        }
	}
	
	 @Transactional(readOnly=false)
	public OptimusRfDataBean confirmOrderRFtoInv(String serviceCode, boolean flag, String taskStage) throws TclCommonException {
		try {
			ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "INPROGRESS");
			if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"ACTIVE");
			if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"TERMINATE");
			List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_key(scServiceDetail.getId(), "get-ip-service-info-async");
			Map<String, String> components = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				String lmType = components.get("lmType");
			if (org.springframework.util.StringUtils.isEmpty(lmType) && Objects.nonNull(scServiceDetail.getAccessType())) lmType=scServiceDetail.getAccessType();
					 if (("OnnetRf".equalsIgnoreCase(lmType))
							 || ("onnet wireless".equalsIgnoreCase(lmType))) {
				OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
				 String wirelessScenario= null;
				 ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"solution_type");
				 if(Objects.isNull(scServiceAttribute)) scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"closest_provider_bso_name");
				 if(Objects.nonNull(scServiceAttribute)) wirelessScenario= scServiceAttribute.getAttributeValue();
				 if(Objects.nonNull(wirelessScenario) && !(wirelessScenario.equalsIgnoreCase("Radwin from TCL POP")) && wirelessScenario.toLowerCase().contains("pmp"))
					{

//					Map<String, String> components = commonFulfillmentUtils
//							.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
//						String suip = components.get("suIp");
//						String hsuip = components.get("hsuIp");
					logger.info("RF PMP to Inventory");
					try {
						optimusRfDataBean = rfService.enrichRfDetails(scServiceDetail, taskStage);
						optimusRfDataBean.setProvider("TCL UBR PMP");
						if(scServiceDetail.getOrderSubCategory().toLowerCase().contains("add"))
							optimusRfDataBean.setLmAction("NEW");
						optimusRfDataBean.setLmType(optimusRfDataBean.getLmAction());
						logger.info("optimusRfDataBean {}", optimusRfDataBean);
					} catch (Exception ee) {
						logger.error("RF PMP to Inventory");
					}
				}
				 else if (wirelessScenario.equalsIgnoreCase("Radwin from TCL POP")
						 && !(wirelessScenario.toLowerCase().contains("pmp")))
				 {
					 Map<String, String> attributeMap=commonFulfillmentUtils.getComponentAttributesDetails(
							 Arrays.asList("suIp", "ssIp","bsMac","btsName","bsIp","ssAntennaHeight","btsSiteId"), scServiceDetail.getId(), "LM", "A");
					 optimusRfDataBean.setSsIp(attributeMap.getOrDefault("suIp", (attributeMap.getOrDefault("ssIp",null))));
					 optimusRfDataBean.setApIp(attributeMap.getOrDefault("bsIp", null));
					 optimusRfDataBean.setMac(attributeMap.getOrDefault("bsMac",null));
					 optimusRfDataBean.setBsName(attributeMap.getOrDefault("btsName",null));
					 optimusRfDataBean.setAntennaHeight(attributeMap.getOrDefault("ssAntennaHeight",null));
					 optimusRfDataBean.setBtsSiteId(attributeMap.getOrDefault("btsSiteId",null));
				 }

				if(!flag) return optimusRfDataBean;
				if (!tasks.isEmpty() && flag) {
					scServiceDetailBean.setOptimusRfDataBean(optimusRfDataBean);
					scServiceDetailBean.setUuid(serviceCode);
					mqUtils.send(rfInventoryHandoverQueue, Utils.convertObjectToJson(scServiceDetailBean));
				}
			}
		} catch (Exception e) {
			logger.error("confirmOrderRFtoInv for service id:{} and error:{}", serviceCode, e);
		}
		return null;
	}
	@Transactional(readOnly=false)
	public OptimusRfDataBean confirmOrderMacdRFtoInv(String serviceCode, boolean flag, String taskStage) throws TclCommonException {
		// MACDMigratedOrders
			
			try {

		ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
		TaskBean migratedTaskBean = new TaskBean();
		String wirelessScenario = null;
		ScServiceDetail scServiceDetail = scServiceDetailRepository
				.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "INPROGRESS");
		if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"ACTIVE");
		if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"TERMINATE");
				ScServiceAttribute scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"solution_type");
				if(Objects.isNull(scServiceAttribute)) scServiceAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"closest_provider_bso_name");
				if(Objects.nonNull(scServiceAttribute)) wirelessScenario= scServiceAttribute.getAttributeValue();
//				if(StringUtils.isEmpty(wirelessScenario) && scServiceDetail.getMstStatus().getCode().equals("ACTIVE")) wirelessScenario="TCL UBR PMP";
				OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
				List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_key(scServiceDetail.getId(), "get-ip-service-info-async");
				Map<String, String> components = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				String lmType = components.get("lmType");
				if (org.springframework.util.StringUtils.isEmpty(lmType) && Objects.nonNull(scServiceDetail.getAccessType())) lmType=scServiceDetail.getAccessType();
				if (("OnnetRf".equalsIgnoreCase(lmType) || ("onnet wireless".equalsIgnoreCase(lmType)))
						&&	(Objects.nonNull(wirelessScenario) &&
						!(wirelessScenario.equalsIgnoreCase("Radwin from TCL POP"))
								&& (wirelessScenario.toLowerCase().contains("pmp")))) {
					optimusRfDataBean.setProvider("TCL UBR PMP");
					optimusRfDataBean.setEthernetExtender(components.getOrDefault("ethernetExtenderUsed", "No"));
			MstCambiumDetails mstCambiumDetails = mstCambiumDetailsRepository
					.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceCode, "Y");
			if (Objects.nonNull(mstCambiumDetails)) {
				logger.info("RF CAMBIUM MIGRATED ORDER to Inventory");

				ServiceDetail serviceDetail = new ServiceDetail();
				serviceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
				if (Objects.isNull(serviceDetail)) serviceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(), "ISSUED");
				if ((Objects.nonNull(serviceDetail)) && !org.springframework.util.StringUtils.isEmpty(serviceDetail)) {
					optimusRfDataBean.setCustomerAddress(serviceDetail.getAddressLine1());
					optimusRfDataBean.setServiceType(serviceDetail.getServiceType());
					optimusRfDataBean.setActionType(Objects.nonNull(scServiceDetail.getOrderSubCategory()) ? scServiceDetail.getOrderSubCategory() : serviceDetail.getOrderSubCategory());
					ScOrder scOrder = scServiceDetail.getScOrder();
					String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
					if (!org.springframework.util.StringUtils.isEmpty(orderCategory))
						optimusRfDataBean.setTypeOfOrder(orderCategory);
					optimusRfDataBean.setCustomerName(scOrder.getErfCustLeName());
					Map<String, String> attributeMap = commonFulfillmentUtils
							.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
					String latLong = attributeMap.get("latLong");
					if (!org.springframework.util.StringUtils.isEmpty(latLong)) {
						String[] latlong = latLong.split(",");
						optimusRfDataBean.setLatitude(latlong[0]);
						optimusRfDataBean.setLongitude(latlong[1]);
					}
					optimusRfDataBean.setCommissionDate(Objects.nonNull(scServiceDetail.getCommissionedDate())? scServiceDetail.getCommissionedDate().toString() : attributeMap.get("commissioningDate"));
					optimusRfDataBean.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);
					String state = attributeMap.get("destinationState");
					if (org.springframework.util.StringUtils.isEmpty(state))
						state=scServiceDetail.getDestinationState();
					if (!org.springframework.util.StringUtils.isEmpty(state))
						optimusRfDataBean.setState(state);
					String city = attributeMap.get("destinationCity");
					if (org.springframework.util.StringUtils.isEmpty(city))
						city=scServiceDetail.getDestinationCity();
					if (!org.springframework.util.StringUtils.isEmpty(city))
						optimusRfDataBean.setCity(city);
					String structureType = attributeMap.get("structureType");
					if (!org.springframework.util.StringUtils.isEmpty(structureType))
						optimusRfDataBean.setStructureType(structureType);
					String rfMakeModel = mstCambiumDetails.getDeviceType();
					if (!org.springframework.util.StringUtils.isEmpty(rfMakeModel))
						optimusRfDataBean.setVendor(rfMakeModel);
					String bwUnit = serviceDetail.getServiceBandwidthUnit();
					Float portBandwidth = serviceDetail.getServiceBandwidth();
					if ((!org.springframework.util.StringUtils.isEmpty(bwUnit))
							&& (!org.springframework.util.StringUtils.isEmpty(portBandwidth))) {
						if ("Mbps".equalsIgnoreCase(bwUnit))
							optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024));
						else if ("Gbps".equalsIgnoreCase(bwUnit))
							optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024 * 1024));
						else
							optimusRfDataBean.setQosBw(String.valueOf(portBandwidth));
					}
				}
				migratedTaskBean = iPDetailsService.getIpServiceDetails(null, null, scServiceDetail.getUuid(), null);
				OrderDetailBean migratedOrderDetailBean = migratedTaskBean.getOrderDetails();
				if (migratedOrderDetailBean != null) fetchLMAttributesfromLastMile(optimusRfDataBean, migratedOrderDetailBean,components);
				optimusRfDataBean.setDeviceType(null);
				optimusRfDataBean.setCircuitId(mstCambiumDetails.getServiceCode());
				if(Objects.isNull(optimusRfDataBean.getMac())) optimusRfDataBean.setMac(mstCambiumDetails.getHsuMac());
				if(Objects.isNull(optimusRfDataBean.getApIp())) optimusRfDataBean.setApIp(mstCambiumDetails.getBtsIp());
				if(Objects.isNull(optimusRfDataBean.getSsIp())) optimusRfDataBean.setSsIp(mstCambiumDetails.getHsuIp());
				String sectorId= components.getOrDefault("sectorId","1");
				optimusRfDataBean.setSectorId(Objects.nonNull(mstCambiumDetails.getSectorId()) ? mstCambiumDetails.getSectorId() : sectorId);
				if(Objects.isNull(optimusRfDataBean.getBsName())) optimusRfDataBean.setBsName(mstCambiumDetails.getBtsName());
				optimusRfDataBean.setAntennaHeight(mstCambiumDetails.getAntennaHeight());
				optimusRfDataBean.setLmAction("MACD");
				optimusRfDataBean.setLmType("MACD");
				setOptimusRfTaskStageData(scServiceDetail, taskStage, optimusRfDataBean);

			} else {
				MstRadwinDetails mstRadwinDetails = mstRadwinDetailsRepository
						.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceCode, "Y");
				if (Objects.nonNull(mstRadwinDetails)) {
					logger.info("RF RADWIN MIGRATED ORDER to Inventory");
					ServiceDetail serviceDetail = new ServiceDetail();
					serviceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if (Objects.isNull(serviceDetail)) serviceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(), "ISSUED");
					if ((Objects.nonNull(serviceDetail))
							&& !org.springframework.util.StringUtils.isEmpty(serviceDetail)) {
						optimusRfDataBean.setCustomerAddress(serviceDetail.getAddressLine1());
						optimusRfDataBean.setServiceType(serviceDetail.getServiceType());
//						optimusRfDataBean.setProvider(serviceDetail.getLastmileProvider());
						optimusRfDataBean.setActionType(Objects.nonNull(scServiceDetail.getOrderSubCategory()) ? scServiceDetail.getOrderSubCategory() : serviceDetail.getOrderSubCategory());
						ScOrder scOrder = scServiceDetail.getScOrder();
						String orderCategory = scServiceDetail.getOrderCategory();
						if (!org.springframework.util.StringUtils.isEmpty(orderCategory))
							optimusRfDataBean.setTypeOfOrder(scServiceDetail.getOrderCategory());
						optimusRfDataBean.setCustomerName(scOrder.getErfCustLeName());
						Map<String, String> attributeMap = commonFulfillmentUtils
								.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
						String latLong = attributeMap.get("latLong");
						if (!org.springframework.util.StringUtils.isEmpty(latLong)) {
							String[] latlong = latLong.split(",");
							optimusRfDataBean.setLatitude(latlong[0]);
							optimusRfDataBean.setLongitude(latlong[1]);
						}
						optimusRfDataBean.setCommissionDate(Objects.nonNull(scServiceDetail.getCommissionedDate())? scServiceDetail.getCommissionedDate().toString() : attributeMap.get("commissioningDate"));
						optimusRfDataBean.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);
						String state = attributeMap.get("destinationState");
						if (org.springframework.util.StringUtils.isEmpty(state))
							state=scServiceDetail.getDestinationState();
						if (!org.springframework.util.StringUtils.isEmpty(state))
							optimusRfDataBean.setState(state);
						String city = attributeMap.get("destinationCity");
						if (org.springframework.util.StringUtils.isEmpty(city))
							city=scServiceDetail.getDestinationCity();
						if (!org.springframework.util.StringUtils.isEmpty(city))
							optimusRfDataBean.setCity(city);
						String structureType = attributeMap.get("structureType");
						if (!org.springframework.util.StringUtils.isEmpty(structureType))
							optimusRfDataBean.setStructureType(structureType);
						String rfMakeModel = attributeMap.get("rfMakeModel");
						if (!org.springframework.util.StringUtils.isEmpty(rfMakeModel))
							optimusRfDataBean.setVendor(rfMakeModel);
						else
							optimusRfDataBean.setVendor(mstRadwinDetails.getDeviceType());
						String bwUnit = serviceDetail.getServiceBandwidthUnit();
						Float portBandwidth = serviceDetail.getServiceBandwidth();
						if ((!org.springframework.util.StringUtils.isEmpty(bwUnit))
								&& (!org.springframework.util.StringUtils.isEmpty(portBandwidth))) {
							if ("Mbps".equalsIgnoreCase(bwUnit))
								optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024));
							else if ("Gbps".equalsIgnoreCase(bwUnit))
								optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024 * 1024));
							else
								optimusRfDataBean.setQosBw(String.valueOf(portBandwidth));
						}
					}
					migratedTaskBean = iPDetailsService.getIpServiceDetails(null, null, scServiceDetail.getUuid(), null);
					OrderDetailBean migratedOrderDetailBean = migratedTaskBean.getOrderDetails();
					if (migratedOrderDetailBean != null) fetchLMAttributesfromLastMile(optimusRfDataBean, migratedOrderDetailBean, components);
					optimusRfDataBean.setDeviceType(null);
					optimusRfDataBean.setCircuitId(mstRadwinDetails.getServiceCode());
					if(Objects.isNull(optimusRfDataBean.getMac())) optimusRfDataBean.setMac(mstRadwinDetails.getHsuMac().toLowerCase());
					if(Objects.isNull(optimusRfDataBean.getApIp())) optimusRfDataBean.setApIp(mstRadwinDetails.getBtsIp());
					if(Objects.isNull(optimusRfDataBean.getSsIp())) optimusRfDataBean.setSsIp(mstRadwinDetails.getHsuIp());
					String sectorId= components.getOrDefault("sectorId","1");
					if(Objects.isNull(optimusRfDataBean.getSectorId()))
						optimusRfDataBean.setSectorId(Objects.nonNull(mstRadwinDetails.getSectorId()) ? mstRadwinDetails.getSectorId() : sectorId);
					if(Objects.isNull(optimusRfDataBean.getBsName())) optimusRfDataBean.setBsName(mstRadwinDetails.getBtsName());
					optimusRfDataBean.setAntennaHeight(mstRadwinDetails.getAntennaHeight());
					optimusRfDataBean.setLmAction("MACD");
					optimusRfDataBean.setLmType("MACD");
					setOptimusRfTaskStageData(scServiceDetail, taskStage, optimusRfDataBean);

				} else {
					migratedTaskBean = iPDetailsService.getIpServiceDetails(null, null, scServiceDetail.getUuid(),null);


				OrderDetailBean migratedOrderDetailBean = migratedTaskBean.getOrderDetails();
				if (migratedOrderDetailBean != null) {
					optimusRfDataBean.setCircuitId(scServiceDetail.getUuid());
					optimusRfDataBean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
					optimusRfDataBean.setActionType( scServiceDetail.getOrderSubCategory());
					ScOrder scOrder = scServiceDetail.getScOrder();
					String orderCategory = scServiceDetail.getOrderCategory();
					if (!org.springframework.util.StringUtils.isEmpty(orderCategory))
						optimusRfDataBean.setTypeOfOrder(scServiceDetail.getOrderCategory());
					Map<String, String> attributeMap = commonFulfillmentUtils
							.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
					String latLong = attributeMap.get("latLong");
					if (!org.springframework.util.StringUtils.isEmpty(latLong)) {
						String[] latlong = latLong.split(",");
						optimusRfDataBean.setLatitude(latlong[0]);
						optimusRfDataBean.setLongitude(latlong[1]);
					}
					String city = attributeMap.get("destinationCity");
					if (org.springframework.util.StringUtils.isEmpty(city))
						city=scServiceDetail.getDestinationCity();
					if (!org.springframework.util.StringUtils.isEmpty(city))
						optimusRfDataBean.setCity(city);
					String structureType = attributeMap.get("structureType");
					if (!org.springframework.util.StringUtils.isEmpty(structureType))
						optimusRfDataBean.setStructureType(structureType);
					String suIp = attributeMap.get("suIp");
					String hsuIp = attributeMap.get("hsuIp");
					if (!org.springframework.util.StringUtils.isEmpty(suIp))
						optimusRfDataBean.setSsIp(suIp);
					if (!org.springframework.util.StringUtils.isEmpty(hsuIp))
						optimusRfDataBean.setSsIp(hsuIp);
					String apIp = attributeMap.get("btsIp");
					if (!org.springframework.util.StringUtils.isEmpty(apIp))
						optimusRfDataBean.setApIp(apIp);
					String lastMileProvider = attributeMap.get("lastMileProvider");
					String state = attributeMap.get("destinationState");
					if (org.springframework.util.StringUtils.isEmpty(state))
						state=scServiceDetail.getDestinationState();
					if (!org.springframework.util.StringUtils.isEmpty(state))
						optimusRfDataBean.setState(state);
					String vendor = attributeMap.get("rfMake");
					if (!org.springframework.util.StringUtils.isEmpty(vendor))
						optimusRfDataBean.setVendor(vendor);

					optimusRfDataBean.setCommissionDate(Objects.nonNull(scServiceDetail.getCommissionedDate())? scServiceDetail.getCommissionedDate().toString() : attributeMap.get("commissioningDate"));
					optimusRfDataBean.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);

					ServiceDetail serviceDetail = new ServiceDetail();
					serviceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(), "ACTIVE");
					if (Objects.isNull(serviceDetail)) serviceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(scServiceDetail.getUuid(), "ISSUED");
					if (Objects.nonNull(serviceDetail)) {
						String bwUnit = serviceDetail.getServiceBandwidthUnit();
						Float portBandwidth = serviceDetail.getServiceBandwidth();
						if ((!org.springframework.util.StringUtils.isEmpty(bwUnit))
								&& (!org.springframework.util.StringUtils.isEmpty(portBandwidth))) {
							if ("Mbps".equalsIgnoreCase(bwUnit))
								optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024));
							else if ("Gbps".equalsIgnoreCase(bwUnit))
								optimusRfDataBean.setQosBw(String.valueOf(Double.parseDouble(String.valueOf(portBandwidth)) * 1024 * 1024));
							else
								optimusRfDataBean.setQosBw(String.valueOf(portBandwidth));
						}
					}
					optimusRfDataBean.setCustomerName(migratedOrderDetailBean.getCustomerName());
					optimusRfDataBean.setCustomerAddress(migratedOrderDetailBean.getAddressLine1());
					optimusRfDataBean.setLmAction("MACD");
					optimusRfDataBean.setLmType("MACD");
					setOptimusRfTaskStageData(scServiceDetail, taskStage , optimusRfDataBean);
					fetchLMAttributesfromLastMile(optimusRfDataBean, migratedOrderDetailBean, components);
				}
				}
			}
			if ((Objects.nonNull(optimusRfDataBean))
					&& !org.springframework.util.StringUtils.isEmpty(optimusRfDataBean)
					&& (!tasks.isEmpty())
					&&  flag) {
				scServiceDetailBean.setOptimusRfDataBean(optimusRfDataBean);
				scServiceDetailBean.setUuid(serviceCode);
				mqUtils.send(rfInventoryHandoverQueue, Utils.convertObjectToJson(scServiceDetailBean));
			}
		}
				else if (("OnnetRf".equalsIgnoreCase(lmType) || ("onnet wireless".equalsIgnoreCase(lmType)))
						&&	(Objects.nonNull(wirelessScenario) &&
						(wirelessScenario.equalsIgnoreCase("Radwin from TCL POP")
								&& !(wirelessScenario.toLowerCase().contains("pmp")))))
				{
					Map<String, String> attributeMap=commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("suIp", "ssIp","bsMac","btsName","bsIp","ssAntennaHeight","btsSiteId"), scServiceDetail.getId(), "LM", "A");
					optimusRfDataBean.setSsIp(attributeMap.getOrDefault("suIp", (attributeMap.getOrDefault("ssIp",null))));
					optimusRfDataBean.setApIp(attributeMap.getOrDefault("bsIp", null));
					optimusRfDataBean.setMac(attributeMap.getOrDefault("bsMac",null));
					optimusRfDataBean.setBsName(attributeMap.getOrDefault("btsName",null));
					optimusRfDataBean.setAntennaHeight(attributeMap.getOrDefault("ssAntennaHeight",null));
					optimusRfDataBean.setBtsSiteId(attributeMap.getOrDefault("btsSiteId",null));
				}
				return optimusRfDataBean;
	}
		catch (Exception e) {
			logger.error("confirmOrderMacdRFtoInv for service id:{} and error:{}", serviceCode, e);
		}
			return null;
		}

	public void fetchLMAttributesfromLastMile(OptimusRfDataBean optimusRfDataBean, OrderDetailBean migratedOrderDetailBean, Map<String, String> components) {
		if (Objects.nonNull(migratedOrderDetailBean.getServiceDetailBeans())
				&& !migratedOrderDetailBean.getServiceDetailBeans().isEmpty()) {
			Map<String, String> atMap = new HashMap<>();
			Optional<ServiceDetailBean> serviceDetailBeanOptional = migratedOrderDetailBean
					.getServiceDetailBeans().stream().findFirst();
			if (serviceDetailBeanOptional.isPresent()) {
				ServiceDetailBean serviceDetailBean = serviceDetailBeanOptional.get();
				if (Objects.nonNull(serviceDetailBean.getLmComponentBeans())
						&& !serviceDetailBean.getLmComponentBeans().isEmpty()) {
					if (Objects
							.nonNull(serviceDetailBean.getLmComponentBeans().get(0).getCambiumLastmiles())
							&& !serviceDetailBean.getLmComponentBeans().get(0).getCambiumLastmiles()
									.isEmpty()) {
						Optional<CambiumLastmileBean> cambiumOptional = serviceDetailBean
								.getLmComponentBeans().get(0).getCambiumLastmiles().stream().findFirst();
						if (cambiumOptional.isPresent()) {
							CambiumLastmileBean cambiumLastmileBean = cambiumOptional.get();
							optimusRfDataBean.setApIp(cambiumLastmileBean.getBsIp());
							optimusRfDataBean.setBsName(cambiumLastmileBean.getBsName());
							optimusRfDataBean.setMac(cambiumLastmileBean.getSuMacAddress());
							optimusRfDataBean.setSsIp(cambiumLastmileBean.getMgmtIpForSsSu());
							if(Objects.isNull(optimusRfDataBean.getVendor()) || !(components.get("rfMake").toLowerCase().contains("cambium"))) {
								optimusRfDataBean.setVendor(cambiumLastmileBean.getDeviceType());
								atMap.put("rfMake", cambiumLastmileBean.getDeviceType());
								atMap.put("rfTechnology", cambiumLastmileBean.getDeviceType());
								componentAndAttributeService.updateAttributes(serviceDetailBean.getScServiceDetailId(), atMap,
										AttributeConstants.COMPONENT_LM, "A");}

						}

					} else if (Objects
							.nonNull(serviceDetailBean.getLmComponentBeans().get(0).getRadwinLastmiles())
							&& !serviceDetailBean.getLmComponentBeans().get(0).getRadwinLastmiles()
									.isEmpty()) {
						Optional<RadwinLastmileBean> radwinOptional = serviceDetailBean
								.getLmComponentBeans().get(0).getRadwinLastmiles().stream().findFirst();
						if (radwinOptional.isPresent()) {
							RadwinLastmileBean radwinLastmileBean = radwinOptional.get();
							optimusRfDataBean.setSsIp(radwinLastmileBean.getHsuIp());
							optimusRfDataBean.setApIp(radwinLastmileBean.getBtsIp());
							optimusRfDataBean.setBsName(radwinLastmileBean.getBtsName());
							optimusRfDataBean.setSectorId(radwinLastmileBean.getSectorId());
							optimusRfDataBean.setMac(radwinLastmileBean.getHsuMacAddr());
							if(Objects.isNull(optimusRfDataBean.getLatitude())) optimusRfDataBean.setLatitude(radwinLastmileBean.getSiteLat());
							if(Objects.isNull(optimusRfDataBean.getLongitude())) optimusRfDataBean.setLongitude(radwinLastmileBean.getSiteLong());
							if(Objects.isNull(optimusRfDataBean.getVendor()) || !(components.get("rfMake").toLowerCase().contains("radwin")))
								{
								if(!optimusRfDataBean.getVendor().toLowerCase().contains("radwin"))
									optimusRfDataBean.setVendor("Radwin");
								atMap.put("rfMake", "Radwin");
								atMap.put("rfTechnology", "Radwin");
								componentAndAttributeService.updateAttributes(serviceDetailBean.getScServiceDetailId(), atMap,
										AttributeConstants.COMPONENT_LM, "A");
							}
						}
					}
				}
			}

		}
	}

	@Transactional(readOnly=false)
	public void addComponentAttr(ScServiceDetail scServiceDetail,ServiceDetail serviceDetails, String serviceId) {
		if(Objects.nonNull(scServiceDetail)){
			ScComponent currentScComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceDetails.getScServiceDetailId(),"LM","A");
			if(Objects.nonNull(currentScComponent) && Objects.nonNull(currentScComponent.getScComponentAttributes()) && !currentScComponent.getScComponentAttributes().isEmpty()){
				logger.info("CurrentScComp & Attr Exists");
				MstP2PDetails mstP2PDetails=mstP2PDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceId, "Y");
				logger.info("addChangeBwAttr");
				if(Objects.nonNull(serviceDetails.getServiceBandwidth())){
					logger.info("serviceBandwidth exists");
					mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"qos_bw",serviceDetails.getServiceBandwidth().toString());
				}
				if(Objects.nonNull(mstP2PDetails)){
					logger.info("mstP2PDetail exists");
					if(Objects.nonNull(mstP2PDetails.getBsAddress())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_address",mstP2PDetails.getBsAddress());
					}
					if(Objects.nonNull(mstP2PDetails.getBsName())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_name",mstP2PDetails.getBsName());
					}
					if(Objects.nonNull(mstP2PDetails.getLatitude())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_latitude",mstP2PDetails.getLatitude());
					}
					if(Objects.nonNull(mstP2PDetails.getLongitude())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_longitude",mstP2PDetails.getLongitude());
					}
					if(Objects.nonNull(mstP2PDetails.getAntennaHeight())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_antenna_height",mstP2PDetails.getAntennaHeight());
					}
					if(Objects.nonNull(mstP2PDetails.getIp())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_ip",mstP2PDetails.getIp());
					}
					if(Objects.nonNull(mstP2PDetails.getMac())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_mac",mstP2PDetails.getMac());
					}
					if(Objects.nonNull(mstP2PDetails.getBsoCircuitId())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bso_ckt_id",mstP2PDetails.getBsoCircuitId());
					}
					if(Objects.nonNull(mstP2PDetails.getBhBso())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bh_bso",mstP2PDetails.getBhBso());
					}
					if(Objects.nonNull(mstP2PDetails.getMimoDiversity())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_mimo_diversity",mstP2PDetails.getMimoDiversity());
					}
					if(Objects.nonNull(mstP2PDetails.getSsLatitude())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_latitude",mstP2PDetails.getSsLatitude());
					}
					if(Objects.nonNull(mstP2PDetails.getSsLongitude())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_longitude",mstP2PDetails.getSsLongitude());
					}
					if(Objects.nonNull(mstP2PDetails.getSsAntennaHeight())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_antenna_height",mstP2PDetails.getSsAntennaHeight());
					}
					if(Objects.nonNull(mstP2PDetails.getSsMac())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_mac",mstP2PDetails.getSsMac());
					}
					if(Objects.nonNull(mstP2PDetails.getSsBsName())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_bs_name",mstP2PDetails.getSsBsName());
					}
					if(Objects.nonNull(mstP2PDetails.getSsIp())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_ip",mstP2PDetails.getSsIp());
					}
					if(Objects.nonNull(mstP2PDetails.getSsBhBso())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_bh_bso",mstP2PDetails.getSsBhBso());
					}
					if(Objects.nonNull(mstP2PDetails.getSiteId())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bts_site_id",mstP2PDetails.getSiteId());
					}
					if(Objects.nonNull(mstP2PDetails.getSsAntennaMountType())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"ss_antenna_mount_type",mstP2PDetails.getSsAntennaMountType());
					}
					if(Objects.nonNull(mstP2PDetails.getAntennaMountType())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bs_antenna_mount_type",mstP2PDetails.getAntennaMountType());
					}
					if(Objects.nonNull(mstP2PDetails.getBsSwitchIp())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"switch_ip",mstP2PDetails.getBsSwitchIp());
					}
					if(Objects.nonNull(mstP2PDetails.getAggregationSwitch())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"aggregation_switch",mstP2PDetails.getAggregationSwitch());
					}
					if(Objects.nonNull(mstP2PDetails.getAggregationSwitchPort())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"aggregation_switch_port",mstP2PDetails.getAggregationSwitchPort());
					}
					if(Objects.nonNull(mstP2PDetails.getBsConverterIp())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"converter_ip",mstP2PDetails.getBsConverterIp());
					}
					if(Objects.nonNull(mstP2PDetails.getConverterType())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"converter_type",mstP2PDetails.getConverterType());
					}
					if(Objects.nonNull(mstP2PDetails.getPopConverterIp())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"pop_converter_ip",mstP2PDetails.getPopConverterIp());
					}
					if(Objects.nonNull(mstP2PDetails.getSwitchConverterPort())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"switch_converter_port",mstP2PDetails.getSwitchConverterPort());
					}
					if(Objects.nonNull(mstP2PDetails.getBhCapacity())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bh_capacity",mstP2PDetails.getBhCapacity());
					}
					if(Objects.nonNull(mstP2PDetails.getBhOffnetOnnet())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bh_offnet_onnet",mstP2PDetails.getBhOffnetOnnet());
					}
					if(Objects.nonNull(mstP2PDetails.getBackhaulType())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"backhaul_type",mstP2PDetails.getBackhaulType());
					}
					if(Objects.nonNull(mstP2PDetails.getBhConfigSwitchConverter())){
						mapCompAttributeToSc(currentScComponent,serviceDetails.getScServiceDetailId(),"bh_config_switch_conv",mstP2PDetails.getBhConfigSwitchConverter());
					}
				}
			}
		}
		
		
		
	}
	public void mapCompAttributeToSc(ScComponent currentScComponent, Integer serviceDetailId, String attributeName, String attributeValue) {
		ScComponentAttribute scComponentAttribute = new ScComponentAttribute();
		scComponentAttribute.setAttributeAltValueLabel(attributeValue);
		scComponentAttribute.setAttributeName(attributeName);
		scComponentAttribute.setAttributeValue(attributeValue);
		scComponentAttribute.setCreatedBy("OPTIMUS_MS");
		scComponentAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponentAttribute.setIsActive(CommonConstants.Y);
		scComponentAttribute.setScComponent(currentScComponent);
		scComponentAttribute.setUuid(Utils.generateUid());
		scComponentAttribute.setScServiceDetailId(serviceDetailId);
		currentScComponent.getScComponentAttributes().add(scComponentAttribute);
		scComponentRepository.saveAndFlush(currentScComponent);
	}


	public void updateServiceStatus(String serviceCode) {
		logger.info("updateServiceStatus invoked");
		ServiceDetail issuedServiceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceCode,"ISSUED");
		if(Objects.nonNull(issuedServiceDetail)){
			logger.info("IssuedServiceDetail exists::",serviceCode);
			issuedServiceDetail.setServiceState("HOLD");
			issuedServiceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
			issuedServiceDetail.setModifiedBy("OPTIMUS_L2O_AMENDEMENT");
			serviceDetailRepository.saveAndFlush(issuedServiceDetail);
		}
		
	}
	
	protected String subnet(String ipAddress) {
		try {
			if (ipAddress != null) {

				String a[] = ipAddress.split("/");
				return a[1];
			}
		} catch (Exception e) {
			return ipAddress;
		}
		return ipAddress;
	}
	
	@Transactional(readOnly=false)
	public OptimusRfDataBean enrichRfDetails(String serviceCode) throws TclCommonException {
		OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
		ServiceDetail serviceDetails = new ServiceDetail();
		serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ACTIVE);
		if(Objects.isNull(serviceDetails)) serviceDetails =serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);
		ScServiceDetail scServiceDetail = scServiceDetailRepository
				.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "INPROGRESS");

		if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"ACTIVE");
		String orderCategory="";
		if(Objects.nonNull(serviceDetails))
			 orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetails, serviceDetails.getOrderDetail());

		if((Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail()))
				&& ((("ADD_SITE".equals(orderCategory))
				||
				(Objects.nonNull(scServiceDetail.getOrderSubCategory()) && (
						(scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))
								|| (scServiceDetail.getOrderSubCategory().toLowerCase().contains("bso"))))
		)
				|| (serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS")))) {
			logger.info("Fetch LM Details for RF NEW {}", serviceCode);
			boolean migrateToInventoryflag = false;
			optimusRfDataBean=confirmOrderRFtoInv(serviceCode, migrateToInventoryflag, null);
		}

		if((Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail()))
				&& (!(("ADD_SITE".equals(serviceDetails.getOrderCategory()))
				||
				(Objects.nonNull(scServiceDetail.getOrderSubCategory()) && (
						(scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))
								|| (scServiceDetail.getOrderSubCategory().toLowerCase().contains("bso"))))
		)
				&& (serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL_MACD")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN_MACD")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS_MACD")))) {
			logger.info("Fetch LM Details for RF MACD {}", serviceCode);
			boolean migrateToInventoryflag = false;
			optimusRfDataBean=confirmOrderMacdRFtoInv(serviceCode, migrateToInventoryflag, null);
		}
		return optimusRfDataBean;
	}

	@Transactional(readOnly=false)
	public OptimusRfDataBean RFInventoryRefresh(String serviceCode) throws TclCommonException {
		OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
		ServiceDetail serviceDetails = new ServiceDetail();
		serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ACTIVE);
		if(Objects.isNull(serviceDetails)) serviceDetails =serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);
		ScServiceDetail scServiceDetail = scServiceDetailRepository
				.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "INPROGRESS");
		if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode,"ACTIVE");
		String orderCategory="";
		if(Objects.nonNull(serviceDetails))
			orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetails, serviceDetails.getOrderDetail());

		if((Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail()))
				&& ((("ADD_SITE".equals(orderCategory))
				||
				(Objects.nonNull(scServiceDetail.getOrderSubCategory()) && (
						(scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))
								|| (scServiceDetail.getOrderSubCategory().toLowerCase().contains("bso"))))
		)
				|| (serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS")))) {
			logger.info("Push LM Details -E2E for RF NEW {}", serviceCode);
			boolean migrateToInventoryflag = true;
			optimusRfDataBean=confirmOrderRFtoInv(serviceCode, migrateToInventoryflag, "E2E");
		}

		if((Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail()))
				&& (!(("ADD_SITE".equals(orderCategory))
				||
				(Objects.nonNull(scServiceDetail.getOrderSubCategory()) && (
						(scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))
								|| (scServiceDetail.getOrderSubCategory().toLowerCase().contains("bso"))))
		)
				&& (serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL_MACD")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN_MACD")
				|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS_MACD")))) {
			logger.info("Push LM Details -E2E for RF MACD {}", serviceCode);
			boolean migrateToInventoryflag = true;
			optimusRfDataBean=confirmOrderMacdRFtoInv(serviceCode, migrateToInventoryflag, "E2E");
		}
		return optimusRfDataBean;
	}

	private String createFtiRequest(String serviceCode, String accessPath) {
		return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
				"xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" " +
				"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n<soapenv:Header/>\n<soapenv:Body>\n" +
				"<axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
				"<document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\n" +
				"<OrderId>8765435</OrderId>\n<ActivityId>1012</ActivityId>\n<RequestItem>\n<Name>Query:Generic</Name>\n<Element>\n<Name>class</Name>\n" +
				"<Value>" + accessPath + "</Value>\n</Element>\n<Element>\n<Name>attributeList</Name>\n<Value>cache</Value>\n</Element>\n<Element>\n" +
				"<Name>criteria</Name>\n<Value>rfu1=\'" + serviceCode +
				"\'</Value>\n</Element>\n<ServiceRequest>Query</ServiceRequest>\n" +
				"<ServiceOperation></ServiceOperation>\n<Action>Do</Action>\n</RequestItem>\n</ns1:Request>]]></document>\n" +
				"<controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\n</axis:processRequest>\n</soapenv:Body>\n</soapenv:Envelope>";
	}

	public Map<String, String> getFtiDetails(String serviceCode, String accessPath) throws TclCommonException {
		logger.info("getFtiDetails invoked for serviceCode {} with accessPath {}", serviceCode, accessPath);
		Map<String, String> ftiDetails = new HashMap<>();
		try {
			RestClientService restClientService = new RestClientService();
			String ftiRequest = createFtiRequest(serviceCode, accessPath);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			RestResponse response = restClientService.post(ftiRefreshUrl, ftiRequest, headers);
			logger.info("FTI Response for serviceCode {} : {}", serviceCode, response);
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				logger.info("SUCCESS::{}" + response.getData());
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				// API to obtain DOM Document instance
				DocumentBuilder builder = null;
				// Create DocumentBuilder with default configuration
				builder = factory.newDocumentBuilder();
				// Parse the content to Document object
				Document responseDocument = builder.parse(new InputSource(new StringReader(response.getData())));
				NodeList resultNodes = responseDocument.getElementsByTagName("result");
				for (int i = 0; i < resultNodes.getLength(); i++) {
					logger.info("Result node exists");
					Element resultElement = (Element) resultNodes.item(i);
					String resultValue = resultElement.getFirstChild().getNodeValue();
					logger.info("ResultValue::{}", resultValue);
					Document resultDocument = builder.parse(new InputSource(new StringReader(resultValue)));
					NodeList valueNodes = resultDocument.getElementsByTagName("Value");
					for (int j = 0; j < valueNodes.getLength(); j++) {
						logger.info("Value node exists");
						Element valueElement = (Element) valueNodes.item(i);
						String childValue = valueElement.getFirstChild().getNodeValue();
						logger.info("ChildValue::{}", childValue);
						BufferedReader reader = new BufferedReader(new StringReader(childValue));
						String line = reader.readLine();
						while (line != null) {
							String[] s = line.split("=");
							ftiDetails.put(s[0], s[1].replace("\"", ""));
							line = reader.readLine();
						}
					}
				}
			} else {
				logger.info("Response failed for FTI for service Code {}", serviceCode);
			}

		} catch (Exception e) {
			logger.error("getFtiDetails for service id:{} and error:{}", serviceCode, e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ftiDetails;
	}

	@Transactional(readOnly = false)
	public void refreshFTIData(List<String> serviceCodes,String isTermination, Integer scServiceDetailId) {
		if (!CollectionUtils.isEmpty(serviceCodes)) {
			serviceCodes.forEach(serviceCode -> {
				try {
					Map<String, String> ftiDetails = getFtiDetails(serviceCode, "SIVXVPNAccessPath");
					if (CollectionUtils.isEmpty(ftiDetails)) {
						logger.info("No Data from FTI for service Code {}. Retrying with different Access Path.",
								serviceCode);
						ftiDetails = getFtiDetails(serviceCode, "SIApIPPath");
						if (CollectionUtils.isEmpty(ftiDetails)) {
							logger.info("No Data from FTI for service Code {} with SIApIPPath", serviceCode);
						}
					}
					ftiService.processFTIData(serviceCode, ftiDetails,isTermination,scServiceDetailId);
				} catch (TclCommonException e) {
					logger.error("Error while refreshing data from FTI for service Code {}, Error : {}", serviceCode,
							e.getMessage());
				}
			});
		}
	}

	public void refreshFTI(List<String> serviceCodes) {
		if (!CollectionUtils.isEmpty(serviceCodes)) {
			serviceCodes.forEach(serviceCode -> {
				try {
					Map<String, String> ftiDetails = getFtiDetails(serviceCode, "SIVXVPNAccessPath");
					if (CollectionUtils.isEmpty(ftiDetails)) {
						logger.info("No Data from FTI for service Code {}. Retrying with different Access Path.",
								serviceCode);
						ftiDetails = getFtiDetails(serviceCode, "SIApIPPath");
						if (CollectionUtils.isEmpty(ftiDetails)) {
							logger.info("No Data from FTI for service Code {} with SIApIPPath", serviceCode);
						}
					}
					ftiService.processFTIData(serviceCode, ftiDetails,null,null);
				} catch (TclCommonException e) {
					logger.error("Error while refreshing data from FTI for service Code {}, Error : {}", serviceCode,
							e.getMessage());
				}
			});
		}
	}

	public void saveErrorDetails(Integer scServiceDetailId, String errorMessage, String code, String taskDefKey) throws TclCommonException {
		MstTaskAttribute mstTaskAttribute = mstTaskAttributeRepository.findByMstTaskDef_keyAndNodeName(taskDefKey,"errorMessage");
		componentAndAttributeService.updateAdditionalAttributes(scServiceDetailId,
				mstTaskAttribute.getAttributeName(), componentAndAttributeService.getErrorMessageDetails(errorMessage, code),
				AttributeConstants.ERROR_MESSAGE, taskDefKey);
	}
	private void setOptimusRfTaskStageData( ScServiceDetail scServiceDetail, String taskStage, OptimusRfDataBean optimusRfDataBean) {
		optimusRfDataBean.setLastUpdatedBy("OPTIMUS");
		optimusRfDataBean.setLastUpdatedDate((new Timestamp(new Date().getTime())).toString());
		optimusRfDataBean.setTaskStage(taskStage);
		optimusRfDataBean.setStatus(Objects.nonNull(scServiceDetail.getServiceAceptanceStatus())? scServiceDetail.getServiceAceptanceStatus() : "PENDING");
		optimusRfDataBean.setServiceStatus(Objects.nonNull(scServiceDetail.getMstStatus())? scServiceDetail.getMstStatus().getCode().replace("INPROGRESS","IN-PROGRESS") : "IN-PROGRESS");
		if("TERMINATE".equals(optimusRfDataBean.getServiceStatus()))
			optimusRfDataBean.setStatus("TERMINATE");
	}

	public CambiumLastmile saveManualCambiumLastmile(String serviceCode, Map<String, String> vlanMap)
			throws TclCommonException {
		logger.info("Service Activation - saveManualCambiumLastmile - started");
		try {
			ServiceDetail serviceDetail = serviceDetailRepository
					.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceCode, "ISSUED");
			MstCambiumDetails mstCambiumDetails =mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceDetail.getServiceId(),"Y");
			if(Objects.nonNull(mstCambiumDetails)){
				CambiumLastmile cambiumLastmile = new CambiumLastmile();
				cambiumLastmile.setBsIp(mstCambiumDetails.getBtsIp());
				cambiumLastmile.setBsName(mstCambiumDetails.getBtsName());
				cambiumLastmile.setMgmtIpForSsSu(mstCambiumDetails.getHsuIp());
				cambiumLastmile.setMgmtIpGateway(Objects.nonNull(cambiumLastmile.getMgmtIpForSsSu()) && !cambiumLastmile.getMgmtIpForSsSu().isEmpty() ?getManagementIpGateway(cambiumLastmile.getMgmtIpForSsSu()):cambiumLastmile.getMgmtIpForSsSu());
				cambiumLastmile.setMgmtSubnetForSsSu("255.255.255.0");
				cambiumLastmile.setDefaultPortVid(mstCambiumDetails.getDataVlan());
				cambiumLastmile.setMgmtVid(mstCambiumDetails.getMgmtVlan());
				cambiumLastmile.setMappedVid1(mstCambiumDetails.getMgmtVlan());
				String portSpeed = vlanMap.get("portSpeed");
				cambiumLastmile.setBwDownlinkSustainedRate(portSpeed);
				cambiumLastmile.setBwUplinkSustainedRate((portSpeed));
				cambiumLastmile.setUplinkBurstAllocation((portSpeed));
				cambiumLastmile.setDownlinkBurstAllocation((portSpeed));
				cambiumLastmile.setPortSpeed(Float.valueOf(portSpeed));
				cambiumLastmile.setPortSpeedUnit("kbps");
				String siteName=vlanMap.getOrDefault("siteName",null);
				cambiumLastmile.setSiteName(siteName);
				String siteContact=vlanMap.getOrDefault("siteContact",null);
				cambiumLastmile.setSiteContact(siteContact);
				String siteLocation=vlanMap.getOrDefault("siteLocation",null);
				cambiumLastmile.setSiteLocation(siteLocation);
				cambiumLastmile.setSmHeight(mstCambiumDetails.getAntennaHeight());
				if(Objects.nonNull(mstCambiumDetails.getSectorId())){
					logger.info("Color Code exists:: {}",mstCambiumDetails.getSectorId());
					if(mstCambiumDetails.getSectorId().contains("_")){
						String[] sectors =mstCambiumDetails.getSectorId().split("_");
						if(sectors.length>1 && Objects.nonNull(sectors[1])){
							cambiumLastmile.setColorCode1(sectors[1]);
						}
					}else{
						cambiumLastmile.setColorCode1(mstCambiumDetails.getSectorId());
					}
				}else{
					logger.info("Color Code doesn't exists");
					cambiumLastmile.setColorCode1("1");
				}
				cambiumLastmile.setLongitudeSettings(mstCambiumDetails.getLongitude());
				cambiumLastmile.setLatitudeSettings(mstCambiumDetails.getLatitude());
				if(Objects.nonNull(mstCambiumDetails.getFrequency()) && !mstCambiumDetails.getFrequency().isEmpty()){
					cambiumLastmile.setCustomRadioFrequencyList(mstCambiumDetails.getFrequency().replace(".", ""));
				}

				if(Objects.nonNull(mstCambiumDetails.getHsuMac()) && !mstCambiumDetails.getHsuMac().isEmpty()){
					cambiumLastmile.setSuMacAddress(mstCambiumDetails.getHsuMac().toLowerCase());
				}
				if (cambiumLastmile.getMgmtVid() != null) {
					cambiumLastmile.setMappedVid1(cambiumLastmile.getMgmtVid());
				}
                cambiumLastmile.setIdentity(Objects.nonNull(cambiumLastmile.getSuMacAddress()) && !cambiumLastmile.getSuMacAddress().isEmpty()?cambiumLastmile.getSuMacAddress().replaceAll(" ", "-"):cambiumLastmile.getSuMacAddress());
				cambiumLastmile.setRegion("Asia");
				cambiumLastmile.setRegionCode("India");
				cambiumLastmile.setLinkSpeed("FORCED100F");
				cambiumLastmile.setFrameTimingPulseGated("ENABLE");
				cambiumLastmile.setInstallationColorCode("ENABLE");
				cambiumLastmile.setEnforceAuthentication("AAA");
				cambiumLastmile.setHipriorityChannel("DISABLE");
				cambiumLastmile.setProviderVid(1);
				cambiumLastmile.setDhcpState("DISABLED");
				cambiumLastmile.setBridgeEntryTimeout("25");
				cambiumLastmile.setDynamicRateAdapt("1x");
				cambiumLastmile.setMulticastDestinationAddr("MULTICAST");
				cambiumLastmile.setDeviceDefaultReset("Disable");
				cambiumLastmile.setPowerupmodeWithno802_3link("Power up in Operational mode");
				cambiumLastmile.setTransmitterOutputPower("23");
				cambiumLastmile.setLargevcDataq("DISABLE");
				cambiumLastmile.setEthernetAccess("ENABLE");
				cambiumLastmile.setUserName("anonymous");
				cambiumLastmile.setAllowlocalloginAfteraaareject("Enabled");
				cambiumLastmile.setDeviceaccessTracking("ENABLE");
				cambiumLastmile.setSelectKey("Enabled");
				cambiumLastmile.setPassword("password");
				cambiumLastmile.setPhase1("EAPTTLS");
				cambiumLastmile.setPhase2("MSCHAPV2");
				cambiumLastmile.setEnableBroadcastMulticastDatarate("FALSE");
				cambiumLastmile.setServerCommonName("Canopy AAA Server Demo Certificate");
				cambiumLastmile.setNetworkAccessibility("Public");
				cambiumLastmile.setUseRealmStatus("DISABLE");
				cambiumLastmile.setUserAuthenticationMode("Remote than Local");
				cambiumLastmile.setAllowFrametypes("All Frames");
				cambiumLastmile.setDynamicLearning("ENABLE");
				cambiumLastmile.setVlanAgeingTimeout(25);
				cambiumLastmile.setSmMgmtVidPassthrough("ENABLE");
				cambiumLastmile.setMappedMacAddress("00:1f:28:01:02:03");
				cambiumLastmile.setVlanPorttype("q");
				cambiumLastmile.setAcceptQinqFrames("DISABLE");
				cambiumLastmile.setSnmpTrapIp1("172.31.5.72");
				cambiumLastmile.setSnmpTrapIp2("172.31.5.70");
				cambiumLastmile.setSnmpTrapIp3("115.114.79.38");
				cambiumLastmile.setSnmpTrapIp4("172.31.5.69");
				cambiumLastmile.setSnmpTrapIp5("172.31.5.71");
				cambiumLastmile.setSiteinfoViewabletoGuestusers("ENABLE");
				cambiumLastmile.setProvider("TCL UBR PMP");
				cambiumLastmile.setWebpageAutoUpdate(0);
				cambiumLastmile.setTechnology("Cambium");
				cambiumLastmile.setMappedVid2(1);
				cambiumLastmile.setDeviceType(mstCambiumDetails.getDeviceType());
				String deviceType = cambiumLastmile.getDeviceType();
				if (deviceType != null && deviceType.equalsIgnoreCase("Cambium")) {
					cambiumLastmile.setSnmpAccessingIp1("172.31.5.64/26");
					cambiumLastmile.setSnmpAccessingIp2("172.31.6.90/32");
					cambiumLastmile.setSnmpAccessingIp3("115.114.79.0/26");
				} else {
					cambiumLastmile.setSnmpAccessingIp1("172.31.5.0/24");
					cambiumLastmile.setSnmpAccessingIp2("172.31.6.90/28");
					cambiumLastmile.setSnmpAccessingIp3("115.114.79.0/28");
					cambiumLastmile.setTechnology("Cambium450PMP");
					cambiumLastmile.setDeviceType("Cambium450i");
				}
				if (deviceType != null && deviceType.contains("450")) {
					cambiumLastmile.setDownlinkPlan(cambiumLastmile.getHipriorityUplinkCir());
					cambiumLastmile.setUplinkPlan(cambiumLastmile.getHipriorityUplinkCir());
					cambiumLastmile.setWeight("0.0");
					cambiumLastmile.setUserLockModulation("Disable");
					cambiumLastmile.setLockModulation("Disable"); //Disable
					cambiumLastmile.setThersholdModulation("Disable"); //Disable
					cambiumLastmile.setPrioritizationGroup("Low");
				}
				cambiumLastmile.setModifiedBy("OPTIMUS_RULE");
				cambiumLastmile.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				if(!CollectionUtils.isEmpty(serviceDetail.getLmComponents()))
					cambiumLastmile.setLmComponent(serviceDetail.getLmComponents().stream().filter(lm -> lm.getEndDate() == null)
							.findFirst().orElse(null));
				cambiumLastmile = cambiumLastmileRepository.saveAndFlush(cambiumLastmile);
				logger.info("Service Activation - saveManualCambiumLastmile - completed");
				return cambiumLastmile;
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception in saveManualCambiumLastmile => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public boolean getFtiLanData(IpAddressDetail ipAddressDetail, List<String> ftiLanv4Data, List<String> ftiLanv6Data) throws TclCommonException {

		String serviceCode = ipAddressDetail.getServiceDetail().getServiceId();
		logger.info("Entering getFtiLanData for service Code {}", serviceCode);
		Map<String, String> ftiLanDataMap = new HashMap<>();
		Map<String, String> ftiDetails = getFtiDetails(serviceCode, "SIVXVPNAccessPath");
		if (CollectionUtils.isEmpty(ftiDetails)) {
			logger.info("No Data from FTI for service Code {}. Retrying with different Access Path.",
					serviceCode);
			ftiDetails = getFtiDetails(serviceCode, "SIApIPPath");
			if (CollectionUtils.isEmpty(ftiDetails)) {
				logger.info("No Data from FTI for service Code {} with SIApIPPath in getFtiLanData", serviceCode);
			}
			if (!ftiDetails.containsKey("rfu5") || ftiDetails.get("rfu5") == null
					|| ftiDetails.get("rfu5").isEmpty())
				return false;
		}

		for (int i = 0; i <= 75; i++) {
		String key ="pewan.proto.static.l3vpn.route";
		String keyWithNumber;
		if(i==0)
			keyWithNumber=key;
		else
			keyWithNumber = key + i;

			if (ftiDetails.containsKey(keyWithNumber + ".asdestaddretype")) {
				if (ftiDetails.get(keyWithNumber + ".asdestaddretype").toLowerCase().contains("ipv4")) {
					if (ftiDetails.containsKey(keyWithNumber + ".asdestination") && ftiDetails.containsKey(keyWithNumber + ".asprefixlength")) {
						String lanIpv4FTI = "";
						lanIpv4FTI = ftiDetails.get(keyWithNumber + ".asdestination") + "/" + ftiDetails.get(keyWithNumber + ".asprefixlength");
						logger.info("lanIpv4 updated by FTI ::{}", lanIpv4FTI);
						ftiLanv4Data.add(lanIpv4FTI);
					}
				}

				if (ftiDetails.get(keyWithNumber + ".asdestaddretype").toLowerCase().contains("ipv6")) {
					if (ftiDetails.containsKey(keyWithNumber + ".asdestination") && ftiDetails.containsKey(keyWithNumber + ".asprefixlength")) {
						String lanIpv6FTI = "";
						lanIpv6FTI = ftiDetails.get(keyWithNumber + ".asdestination") + "/" + ftiDetails.get(keyWithNumber + ".asprefixlength");
						logger.info("lanIpv6 updated by FTI ::{}", lanIpv6FTI);
						ftiLanv6Data.add(lanIpv6FTI);
					}
				}
			}
		}
		logger.info("Exiting getFtiLanData for service Code {}", serviceCode);
		return true;
	}

	public void applyLanFTIRule(IpAddressDetail ipAddressDetail, List<String> ftiLanv4Data, List<String> ftiLanv6Data) {

		logger.info("Entering applyLanFTIRule for Service Code: {}", ipAddressDetail.getServiceDetail().getServiceId());
		Set<String> updatedLanv4Set = new HashSet<>();
		Set<String> updatedLanv6Set = new HashSet<>();

		if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {
			ipAddressDetail.getIpaddrLanv4Addresses().forEach(e -> updatedLanv4Set.add(e.getLanv4Address()));
		}

		if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {
			ipAddressDetail.getIpaddrLanv6Addresses().forEach(e -> updatedLanv6Set.add(e.getLanv6Address()));
		}

		ftiLanv4Data.removeAll(updatedLanv4Set);
		ftiLanv6Data.removeAll(updatedLanv6Set);

		for(String lanv4Addr: ftiLanv4Data) {
			IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
			ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
			ipaddrLanv4Address.setIscustomerprovided((byte) 1);
			ipaddrLanv4Address.setLanv4Address(lanv4Addr);
			ipaddrLanv4Address.setIssecondary((byte) 0);
			ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
			ipaddrLanv4Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			ipaddrLanv4Address.setModifiedBy("Optimus_FTI");
			ipaddrLanv4Address.setEndDate(null);
			if(!lanv4Addr.contains("/32"))
				ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
			logger.info("applyLanFTIRule adding Lanv4: {}", lanv4Addr);
			ipAddressDetail.getIpaddrLanv4Addresses().add(ipaddrLanv4Address);
		}

		for(String lanv6Addr: ftiLanv6Data) {
			IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
			ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
			ipaddrLanv6Address.setIscustomerprovided((byte) 1);
			ipaddrLanv6Address.setLanv6Address(lanv6Addr);
			ipaddrLanv6Address.setIssecondary((byte) 0);
			ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
			ipaddrLanv6Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			ipaddrLanv6Address.setModifiedBy("Optimus_FTI");
			ipaddrLanv6Address.setEndDate(null);
			ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
			logger.info("applyLanFTIRule adding Lanv6: {}", lanv6Addr);
			ipAddressDetail.getIpaddrLanv6Addresses().add(ipaddrLanv6Address);
		}
		logger.info("Exiting applyLanFTIRule for Service Code: {}", ipAddressDetail.getServiceDetail().getServiceId());
	}
	
	@Transactional(readOnly = false)
	public SatcoServiceDataRefreshBean refreshSatcoData(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean) {

		logger.info("Inside refresh Satco Data block :{}", satcoServiceDataRefreshBean.getServiceId());

		ServiceDetail serviceDetail = null;

		if (satcoServiceDataRefreshBean.isIsSatsoc()) {
			serviceDetail = serviceDetailRepository
					.findFirstByServiceIdAndScServiceDetailIdAndServiceStateOrderByVersionDesc(
							satcoServiceDataRefreshBean.getServiceId(),
							satcoServiceDataRefreshBean.getScServiceDetailId(), TaskStatusConstants.ACTIVE);
		} else {
			serviceDetail = serviceDetailRepository
					.findFirstByServiceIdAndScServiceDetailIdAndServiceStateOrderByVersionDesc(
							satcoServiceDataRefreshBean.getServiceId(),
							satcoServiceDataRefreshBean.getScServiceDetailId(), TaskStatusConstants.ISSUED);
		}

		if (serviceDetail != null) {

			updateIpv4AndV6Address(satcoServiceDataRefreshBean, serviceDetail);

			updateSwitchDetails(satcoServiceDataRefreshBean, serviceDetail);

			for (InterfaceProtocolMapping ipm : serviceDetail.getInterfaceProtocolMappings()) {
				updateIpV6Address(satcoServiceDataRefreshBean, ipm, satcoServiceDataRefreshBean.getIpv6VsnlWanIp(),
						true);
				updateIpV4Address(satcoServiceDataRefreshBean, ipm, satcoServiceDataRefreshBean.getVsnlWanIp(),
						serviceDetail);
				if (ipm.getRouterDetail() != null && ipm.getEndDate() == null) {
					refreshEthernetDetails(satcoServiceDataRefreshBean, ipm);
					refreshRouterDetails(satcoServiceDataRefreshBean, ipm);

				}

				if (ipm.getCpe() != null && ipm.getEndDate() == null) {

					Cpe cpe = ipm.getCpe();
					if (cpe != null && cpe.getEndDate() == null && checkValueIsChangedOrNot(
							satcoServiceDataRefreshBean.getCustomerEndLoopback(), cpe.getMgmtLoopbackV4address())) {
						logger.info("MgmtLoopbackV4address has changed {} :",
								satcoServiceDataRefreshBean.getServiceId());
						cpe.setMgmtLoopbackV4address(satcoServiceDataRefreshBean.getCustomerEndLoopback());
						cpeRepository.save(cpe);
					}

				}

			}
		} else {
			logger.info("Service Detail is null for the given details : {} ",
					satcoServiceDataRefreshBean.getServiceId());
		}
		logger.info("Refresh Satco Data block end : {}", satcoServiceDataRefreshBean.getServiceId());
		return satcoServiceDataRefreshBean;
	}


	private void updateSwitchDetails(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			ServiceDetail serviceDetail) {
		Set<Topology> topologies = serviceDetail.getTopologies();

		//Need to check enddate and get find first
		
		
		Optional<Topology> optionalTopology = topologies.stream().filter(topo -> topo.getUniswitchDetails() != null)
				.findFirst();
		
		if(optionalTopology.isPresent()) {
			
			Topology topology = optionalTopology.get();
			
			if(topology.getUniswitchDetails() != null) {
				Optional<UniswitchDetail> optionalUniswitchDetail = topology.getUniswitchDetails().stream()
						.filter(switchDetails -> switchDetails.getEndDate() == null).findFirst();
				if(optionalUniswitchDetail.isPresent()) {
					UniswitchDetail uniswitchDetail = optionalUniswitchDetail.get();
					boolean isChanged = false;
					if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getBusinessSwitchHostName(), uniswitchDetail.getHostName())) {
						logger.info("UniswitchDetail BusinessSwitchHostName has changed {}", satcoServiceDataRefreshBean.getServiceId());
						uniswitchDetail.setHostName(satcoServiceDataRefreshBean.getBusinessSwitchHostName());
						isChanged = true;
					}
					if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getBusinessSwitchIp(), uniswitchDetail.getMgmtIp())) {
						logger.info("UniswitchDetail BusinessSwitchIp has changed {}", satcoServiceDataRefreshBean.getServiceId());
						uniswitchDetail.setMgmtIp(satcoServiceDataRefreshBean.getBusinessSwitchIp());
						isChanged = true;
					}
					if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getBusinessSwitchHandoffPort(), uniswitchDetail.getInterfaceName())) {
						logger.info("UniswitchDetail BusinessSwitchHandoffPort has changed {}", satcoServiceDataRefreshBean.getServiceId());
						uniswitchDetail.setInterfaceName(satcoServiceDataRefreshBean.getBusinessSwitchHandoffPort());
						isChanged = true;
					}
					if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getvLAN(), uniswitchDetail.getInnerVlan())) {
						logger.info("UniswitchDetail Vlan has changed {}", satcoServiceDataRefreshBean.getServiceId());
						uniswitchDetail.setInnerVlan(satcoServiceDataRefreshBean.getvLAN());
						isChanged = true;
					}
					
					if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getBusinessSwitchHandoffPort(), uniswitchDetail.getPhysicalPort())) {
						logger.info("UniswitchDetail BusinessSwitchHandoffPort has changed {}", satcoServiceDataRefreshBean.getServiceId());
						uniswitchDetail.setPhysicalPort(satcoServiceDataRefreshBean.getBusinessSwitchHandoffPort());
						isChanged = true;
					}
					
					if(isChanged) {
						uniswitchDetailRepository.save(uniswitchDetail);
					}
				}
			}
			
		}
	}
	
	private void updateIpv4AndV6Address(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			ServiceDetail serviceDetail) {
		Set<IpAddressDetail> ipAddressDetails = serviceDetail.getIpAddressDetails();
		//updateLanId(ipAddressDetails, satcoServiceDataRefreshBean);
		//updateLanV6(ipAddressDetails, satcoServiceDataRefreshBean);
		if (satcoServiceDataRefreshBean.getWanIp() != null
				&& !satcoServiceDataRefreshBean.getWanIp().trim().isEmpty()) {
			String[] wanIps = satcoServiceDataRefreshBean.getWanIp().split(",");
			Optional<IpAddressDetail> optinalIpAddressDetail = ipAddressDetails.stream()
					.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrWanv4Addresses() != null).findFirst();
			boolean extendedLan = false;
			if (optinalIpAddressDetail.isPresent()) {
				if (optinalIpAddressDetail.get().getExtendedLanEnabled() == 1) {
					extendedLan = true;
				}

				ScComponentAttribute scComponentAttributeExtendedLanRequired = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								optinalIpAddressDetail.get().getServiceDetail().getScServiceDetailId(),
								"extendedLanRequired", "LM", "A");

				if (scComponentAttributeExtendedLanRequired != null
						&& scComponentAttributeExtendedLanRequired.getAttributeName().equalsIgnoreCase("yes")) {
					extendedLan = true;
				}
			}
			IpAddressDetail ipAddressDetail = optinalIpAddressDetail.get();

			if (extendedLan) {

				if (ipAddressDetail.getIpaddrWanv4Addresses() != null) {
					for (IpaddrWanv4Address ipaddrWanv4Address : ipAddressDetail.getIpaddrWanv4Addresses()) {
						ipaddrWanv4Address.setEndDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
					}

				}
				if (extendedLan) {
					ipAddressDetail.setExtendedLanEnabled((byte) 1);
					ipAddressDetail.setNoMacAddress(findNoMac(ipAddressDetail));
					ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
					serviceDetail.getInterfaceProtocolMappings().forEach(ipm -> {
						if (ipm.getStaticProtocol() != null && ipm.getEndDate() == null) {
							if (ipm.getStaticProtocol() != null
									&& ipm.getStaticProtocol().getWanStaticRoutes() != null) {
								ipm.getStaticProtocol().getWanStaticRoutes().stream().forEach(wanst -> {
									wanst.setEndDate(new Timestamp(new Date().getTime()));
									wanStaticRouteRepository.saveAndFlush(wanst);
								});
							}

						}
					});

					serviceDetail.getInterfaceProtocolMappings().forEach(ipm -> {
						if (ipm.getCpe() != null && ipm.getEndDate() == null) {
							EthernetInterface ethernetInterface = ipm.getEthernetInterface();

							Optional<IpAddressDetail> optinalIpAddressDetailForLanv4 = ipAddressDetails.stream().filter(
									ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv4Addresses() != null)
									.findFirst();

							Optional<IpAddressDetail> optinalIpAddressDetailForLanv6 = ipAddressDetails.stream().filter(
									ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv6Addresses() != null)
									.findFirst();

							if (optinalIpAddressDetailForLanv4.isPresent()) {

								Optional<IpaddrLanv4Address> lanV4Address = optinalIpAddressDetailForLanv4.get()
										.getIpaddrLanv4Addresses().stream().filter(v4 -> v4.getEndDate() == null)
										.findFirst();

								int count = 2;
								if (lanV4Address.isPresent()) {

									String array[] = lanV4Address.get().getLanv4Address().split("/");
									String ssMgmtIp = array[0];
									String[] splitValue = ssMgmtIp.split("\\.");
									splitValue[splitValue.length - 1] = String
											.valueOf(Integer.parseInt(splitValue[splitValue.length - 1]) + count);
									String output = Arrays.asList(splitValue).stream()
											.map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));

									ethernetInterface.setModifiedIpv4Address(output + "/" + array[1]);

								}

							}

							if (optinalIpAddressDetailForLanv6.isPresent()) {

								Optional<IpaddrLanv6Address> lanV6Address = optinalIpAddressDetailForLanv6.get()
										.getIpaddrLanv6Addresses().stream().filter(v6 -> v6.getEndDate() == null)
										.findFirst();

								int count = 2;
								if (lanV6Address.isPresent()) {

									String array[] = lanV6Address.get().getLanv6Address().split("/");
									String ssMgmtIp = array[0];
									String[] splitValue = ssMgmtIp.split("\\.");
									splitValue[splitValue.length - 1] = String
											.valueOf(Integer.parseInt(splitValue[splitValue.length - 1]) + count);
									String output = Arrays.asList(splitValue).stream()
											.map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));

									ethernetInterface.setModifiedIpv6Address(output + "/" + array[1]);

								}

							}

						}
						if (ipm.getRouterDetail() != null && ipm.getEndDate() == null) {

							EthernetInterface ethernetInterface = ipm.getEthernetInterface();

							Optional<IpAddressDetail> optinalIpAddressDetailForLanv4 = ipAddressDetails.stream().filter(
									ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv4Addresses() != null)
									.findFirst();

							if (optinalIpAddressDetailForLanv4.isPresent()) {

								Optional<IpaddrLanv4Address> lanV4Address = optinalIpAddressDetailForLanv4.get()
										.getIpaddrLanv4Addresses().stream().filter(v4 -> v4.getEndDate() == null)
										.findFirst();

								int count = 1;
								if (lanV4Address.isPresent()) {

									String array[] = lanV4Address.get().getLanv4Address().split("/");
									String ssMgmtIp = array[0];
									String[] splitValue = ssMgmtIp.split("\\.");
									splitValue[splitValue.length - 1] = String
											.valueOf(Integer.parseInt(splitValue[splitValue.length - 1]) + count);
									String output = Arrays.asList(splitValue).stream()
											.map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));

									ethernetInterface.setModifiedIpv4Address(output + "/" + array[1]);

								}

							}
							Optional<IpAddressDetail> optinalIpAddressDetailForLanv6 = ipAddressDetails.stream().filter(
									ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv6Addresses() != null)
									.findFirst();

							if (optinalIpAddressDetailForLanv6.isPresent()) {

								Optional<IpaddrLanv6Address> lanV6Address = optinalIpAddressDetailForLanv6.get()
										.getIpaddrLanv6Addresses().stream().filter(v6 -> v6.getEndDate() == null)
										.findFirst();

								int count = 1;
								if (lanV6Address.isPresent()) {

									String array[] = lanV6Address.get().getLanv6Address().split("/");
									String ssMgmtIp = array[0];
									String[] splitValue = ssMgmtIp.split("\\.");
									splitValue[splitValue.length - 1] = String
											.valueOf(Integer.parseInt(splitValue[splitValue.length - 1]) + count);
									String output = Arrays.asList(splitValue).stream()
											.map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));

									ethernetInterface.setModifiedIpv6Address(output + "/" + array[1]);

								}

							}

						}
					});

				}
			}

			 
		}
	}
	
	/**
	 * @author vivek
	 *
	 * @param ipAddressDetails
	 * @param satcoServiceDataRefreshBean
	 */
	private void updateLanV6(Set<IpAddressDetail> ipAddressDetails,
			SatcoServiceDataRefreshBean satcoServiceDataRefreshBean) {
		Optional<IpAddressDetail> optinalIpAddressDetailForLanv6 = ipAddressDetails.stream()
				.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv6Addresses() != null)
				.findFirst();
			
		if(optinalIpAddressDetailForLanv6.isPresent()) {
			IpAddressDetail ipAddressDetail = optinalIpAddressDetailForLanv6.get();
			Optional<IpaddrLanv6Address> optionalIpaddrLanv6Address = ipAddressDetail.getIpaddrLanv6Addresses()
					.stream().filter(ipaddr -> ipaddr.getEndDate() == null
							&& ipaddr.getIscustomerprovided() != 1)
					.findFirst();
			
			if(optionalIpaddrLanv6Address.isPresent()) {
				IpaddrLanv6Address ipaddrLanv6Address = optionalIpaddrLanv6Address.get();
				if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getLanIpv6(), ipaddrLanv6Address.getLanv6Address())) {
					logger.info("IpaddrLanv6Address Lanv6Address has changed {}", satcoServiceDataRefreshBean.getServiceId());
					ipaddrLanv6Address.setLanv6Address(satcoServiceDataRefreshBean.getLanIpv6());
					ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
				}
			}
		}
		
	}


	private void updateLanId(Set<IpAddressDetail> ipAddressDetails,SatcoServiceDataRefreshBean satcoServiceDataRefreshBean) {
		

		Optional<IpAddressDetail> optinalIpAddressDetailForLanv4 = ipAddressDetails.stream()
				.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv4Addresses() != null)
				.findFirst();
		
		if (optinalIpAddressDetailForLanv4.isPresent()) {
			IpAddressDetail ipAddressDetail = optinalIpAddressDetailForLanv4.get();

			if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {
				for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
					if (ipaddrLanv4Address.getEndDate() == null
							&& ipaddrLanv4Address.getIscustomerprovided() != 1) {
						ipaddrLanv4Address.setEndDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
					}

				}

				if (satcoServiceDataRefreshBean.getLanIp() != null) {

					String lanIps[] = satcoServiceDataRefreshBean.getLanIp().split(",");
					if (lanIps.length > 0) {
						Arrays.asList(lanIps).forEach(lanip -> {
							IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
							ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
							ipaddrLanv4Address.setIscustomerprovided((byte) 0);
							ipaddrLanv4Address.setLanv4Address(lanip);
							ipaddrLanv4Address.setIssecondary((byte) 0);
							ipaddrLanv4Address.setStartDate(startDate);
							ipaddrLanv4Address.setLastModifiedDate(lastModifiedDate);
							ipaddrLanv4Address.setModifiedBy(modifiedBy);
							ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);

						});
					}

				}

			}

		}
		
	}




	
	
	private void updateIpV6Address(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			InterfaceProtocolMapping ipm, String wanIp, boolean isPE) {

		ServiceDetail serviceDetail = ipm.getServiceDetail();
		Set<IpAddressDetail> ipAddressDetails = serviceDetail.getIpAddressDetails();
		if (satcoServiceDataRefreshBean.getIpv6WanIp() != null) {
			String wanIps[] = satcoServiceDataRefreshBean.getIpv6WanIp().split(",");

			if (wanIps.length > 0) {
				if (ipm.getRouterDetail() != null && ipm.getEndDate() == null) {
					EthernetInterface ethernetInterface = ipm.getEthernetInterface();

					Optional<IpAddressDetail> optinalIpAddressDetailForWanv6 = ipAddressDetails.stream()
							.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrWanv6Addresses() != null)
							.findFirst();

					if (optinalIpAddressDetailForWanv6.isPresent()) {

						optinalIpAddressDetailForWanv6.get().getIpaddrWanv6Addresses().stream().forEach(wanV6 -> {
							if (wanV6.getIscustomerprovided() != 1) {
								wanV6.setEndDate(new Timestamp(System.currentTimeMillis()));
								ipaddrWanv6AddressRepository.save(wanV6);
							}
						});

						Arrays.asList(wanIps).forEach(wanv6Addr -> {
							IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
							ipaddrWanv6Address.setIpAddressDetail(optinalIpAddressDetailForWanv6.get());
							ipaddrWanv6Address.setIssecondary((byte) 0);
							ipaddrWanv6Address.setIscustomerprovided((byte) 0);
							ipaddrWanv6Address.setWanv6Address(wanv6Addr);
							ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
						});
					}

					String customerWanIpS[] = satcoServiceDataRefreshBean.getIpv6VsnlWanIp().split(",");

					String subNet[] = wanIps[0].split("/");
					if (customerWanIpS.length > 0) {
						ethernetInterface.setModifiedIpv6Address(customerWanIpS[0] + "/" + subNet[1]);

					}
					if (customerWanIpS.length > 1) {
						if (wanIps.length > 1) {
							String subNe[] = wanIps[1].split("/");

							ethernetInterface.setModifiedSecondaryIpv6Address(customerWanIpS[1] + subNe[1]);

						}
					}
					ethernetInterfaceRepository.save(ethernetInterface);

				}
			}
			if (satcoServiceDataRefreshBean.getIpv6VsnlWanIp() != null) {
				String customerWanIpS[] = satcoServiceDataRefreshBean.getIpv6CustomerWanIp().split(",");

				if (customerWanIpS.length > 0) {
					if (ipm.getCpe() != null && ipm.getEndDate() == null) {

						EthernetInterface ethernetInterface = ipm.getEthernetInterface();

						if (ipm.getBgp() != null && ipm.getEndDate() == null) {
							Bgp bgp = ipm.getBgp();
							bgp.setRemoteIpv6Address(customerWanIpS[0]);
							bgp.setNeighborOn("WAN");
							bgpRepository.save(bgp);
						}
						Optional<IpAddressDetail> optinalIpAddressDetailForLanv6 = ipAddressDetails.stream()
								.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv6Addresses() != null)
								.findFirst();

						if (optinalIpAddressDetailForLanv6.isPresent()) {

							if (optinalIpAddressDetailForLanv6.get().getIpaddrLanv6Addresses() != null) {
								optinalIpAddressDetailForLanv6.get().getIpaddrLanv6Addresses().forEach(lanv6 -> {
									if (lanv6.getIscustomerprovided() != 1) {
										lanv6.setEndDate(new Timestamp(System.currentTimeMillis()));
										ipaddrLanv6AddressRepository.saveAndFlush(lanv6);

									}
								});
							}
							String lanIps[] = satcoServiceDataRefreshBean.getLanIpv6().split(",");
							if (lanIps.length > 0) {
								Arrays.asList(lanIps).forEach(wanv4Addr -> {
									IpaddrLanv6Address ipaddrWanv4Address = new IpaddrLanv6Address();
									ipaddrWanv4Address.setIpAddressDetail(optinalIpAddressDetailForLanv6.get());
									ipaddrWanv4Address.setIssecondary((byte) 0);
									ipaddrWanv4Address.setIscustomerprovided((byte) 0);
									ipaddrWanv4Address.setLanv6Address(wanv4Addr);
									ipaddrLanv6AddressRepository.saveAndFlush(ipaddrWanv4Address);
								});
							}
						}
						if (ipm.getStaticProtocol() != null && ipm.getEndDate() == null) {
							StaticProtocol staticPro = ipm.getStaticProtocol();
							if (ipm.getStaticProtocol().getWanStaticRoutes() != null) {
								ipm.getStaticProtocol().getWanStaticRoutes().forEach(wanRout -> {
									if (wanRout.getIpsubnet() != null && wanRout.getIpsubnet().contains(":")) {

										wanRout.setEndDate(new Timestamp(System.currentTimeMillis()));
										wanStaticRouteRepository.saveAndFlush(wanRout);
									}
								});
							}
							List<IpaddrLanv6Address> ipaddrLanv6Addresses = ipaddrLanv6AddressRepository
									.findByIpAddressDetailId(
											optinalIpAddressDetailForLanv6.get().getIP_Address_Details());
							if (ipaddrLanv6Addresses != null) {
								ipaddrLanv6Addresses.forEach(lanv4 -> {
									if (lanv4.getLanv6Address().contains(":")) {
										WanStaticRoute wanStaticRoute = new WanStaticRoute();
										wanStaticRoute.setIpsubnet(lanv4.getLanv6Address());
										wanStaticRoute.setNextHopid(customerWanIpS[0]);
										wanStaticRoute.setStaticProtocol(staticPro);
										wanStaticRoute.setGlobal((byte) 0);
										wanStaticRoute.setIsPewan((byte) 1);
										wanStaticRoute.setIsCewan((byte) 0);
										wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
										wanStaticRouteRepository.save(wanStaticRoute);
									}

								});
							}
						}

						String subNet[] = wanIps[0].split("/");

						if (customerWanIpS.length > 0) {
							ethernetInterface.setModifiedIpv6Address(customerWanIpS[0] + "/" + subNet[1]);

						}
						if (customerWanIpS.length > 1) {
							if (wanIps.length > 1) {
								String subNe[] = wanIps[1].split("/");
								ethernetInterface.setModifiedSecondaryIpv6Address(customerWanIpS[1] + "/" + subNe[1]);
							}

						}
						ethernetInterfaceRepository.save(ethernetInterface);

					}
				}
			}

		}

	}



	private void updateWanStaticRoute(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			InterfaceProtocolMapping ipm) {
		Set<WanStaticRoute> wanStaticRoutes = ipm.getStaticProtocol().getWanStaticRoutes();
		if(wanStaticRoutes != null && !wanStaticRoutes.isEmpty()) {
			Optional<WanStaticRoute> optional = wanStaticRoutes.stream().filter(wan -> wan.getEndDate() == null).findFirst();
			if(optional.isPresent()) {
				WanStaticRoute wanStaticRoute = optional.get();
				wanStaticRoute.setIpsubnet(satcoServiceDataRefreshBean.getLanIp());
				if(satcoServiceDataRefreshBean.getLanIp().contains(".")) {
					if (checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getCustomerWanIp(), wanStaticRoute.getNextHopid())) {
						wanStaticRoute.setNextHopid(satcoServiceDataRefreshBean.getCustomerWanIp());
						logger.info("WanStaticRoute NextHopid for IPV4 has changed {}",
								satcoServiceDataRefreshBean.getServiceId());
						wanStaticRouteRepository.save(wanStaticRoute);
					}
				} else if(satcoServiceDataRefreshBean.getLanIp().contains(":")) {
					if (checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getIpv6CustomerWanIp(), wanStaticRoute.getNextHopid())) {
						wanStaticRoute.setNextHopid(satcoServiceDataRefreshBean.getIpv6CustomerWanIp());
						logger.info("WanStaticRoute Ipv6CustomerWanIp for IPV6 has changed {}",
								satcoServiceDataRefreshBean.getServiceId());
						wanStaticRouteRepository.save(wanStaticRoute);
					}
				}
			}
		}
	}


	private void updateBgp(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean, InterfaceProtocolMapping ipm) {
		Bgp bgp = ipm.getBgp();
		if (checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getCustomerWanIp(), bgp.getRemoteIpv4Address())) {
			bgp.setRemoteIpv4Address(satcoServiceDataRefreshBean.getCustomerWanIp());
			logger.info("Bgp RemoteIpv4Address has changed {}",
					satcoServiceDataRefreshBean.getServiceId());
			bgpRepository.save(bgp);
		}
		if (checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getIpv6CustomerWanIp(), bgp.getRemoteIpv6Address())) {
			bgp.setRemoteIpv6Address(satcoServiceDataRefreshBean.getIpv6CustomerWanIp());
			logger.info("Bgp RemoteIpv6Address has changed {}",
					satcoServiceDataRefreshBean.getServiceId());
			bgpRepository.save(bgp);

		}
	}


	private void refreshEthernetDetails(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			InterfaceProtocolMapping ipm) {
		EthernetInterface ethernetInterface = ipm.getEthernetInterface();
		if(ethernetInterface != null) {
			boolean isChanged = false;
			if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getLagPort(), ethernetInterface.getInterfaceName())) {
				ethernetInterface.setInterfaceName(satcoServiceDataRefreshBean.getLagPort());
				isChanged = true;
				logger.info("Lag port is changed {}", satcoServiceDataRefreshBean.getServiceId());
			}
			if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getLagInnerVlan(), ethernetInterface.getInnerVlan())) {
				ethernetInterface.setInnerVlan(satcoServiceDataRefreshBean.getLagInnerVlan());
				isChanged = true;
				logger.info("Lag inner vlan is changed {}", satcoServiceDataRefreshBean.getServiceId());
			}
			if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getLagOuterVlan(), ethernetInterface.getOuterVlan())) {
				ethernetInterface.setOuterVlan(satcoServiceDataRefreshBean.getLagOuterVlan());
				isChanged = true;
				logger.info("Lag outer vlan is changed {}", satcoServiceDataRefreshBean.getServiceId());
			}
			if(isChanged) {
				ethernetInterfaceRepository.save(ethernetInterface);
				logger.info("Ethernet Interface Data is refreshed {}", satcoServiceDataRefreshBean.getServiceId());
			}
		}
	}


	private void refreshRouterDetails(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			InterfaceProtocolMapping ipm) {
		RouterDetail routerDetail = ipm.getRouterDetail();
		if(routerDetail != null) {
			boolean isChanged = false;
			if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getPeName(), routerDetail.getRouterHostname())) {
				routerDetail.setRouterHostname(satcoServiceDataRefreshBean.getPeName());
				isChanged = true;
				logger.info("PE name  is changed {}", satcoServiceDataRefreshBean.getServiceId());
			}
			if(checkValueIsChangedOrNot(satcoServiceDataRefreshBean.getPeMgmtIp(), routerDetail.getIpv4MgmtAddress())) {
				routerDetail.setIpv4MgmtAddress(satcoServiceDataRefreshBean.getPeMgmtIp());
				isChanged = true;
				logger.info("PE mgmt IP  is changed {}", satcoServiceDataRefreshBean.getServiceId());
			}
			if(isChanged) {
				routerDetailRepository.save(routerDetail);
				logger.info("RouterDetail Data is refreshed {}", satcoServiceDataRefreshBean.getServiceId());
			}
		}
	}

	private boolean checkValueIsChangedOrNot(String apiValue, String dbValue) {
		return ((apiValue != null && dbValue == null)
				|| (dbValue != null && apiValue != null && !dbValue.equalsIgnoreCase(apiValue)));
	}
	
	public Integer findNoMac(IpAddressDetail ipAddressDetail) {

		Integer macNo = 0;

		if (ipAddressDetail != null) {
			logger.info("findNoMac  started {}");

			if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {

				for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
					String ipv4Address = ipaddrLanv4Address.getLanv4Address() != null
							? ipaddrLanv4Address.getLanv4Address().trim()
							: ipaddrLanv4Address.getLanv4Address();
					macNo = macNo + (int) (Math.pow(2, (32 - Integer.valueOf(subnet(ipv4Address)))) - 2);
				}
			}

			if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {

				for (IpaddrLanv6Address ipaddrLanv6Address : ipAddressDetail.getIpaddrLanv6Addresses()) {

					String ipv6Address = ipaddrLanv6Address.getLanv6Address() != null
							? ipaddrLanv6Address.getLanv6Address().trim()
							: ipaddrLanv6Address.getLanv6Address();
					macNo = macNo + (int) (Math.pow(2, (128 - Integer.valueOf(subnet(ipv6Address)))) - 2);

				}
			}
		}

		return macNo;
	}


	private void updateIpV4Address(SatcoServiceDataRefreshBean satcoServiceDataRefreshBean,
			InterfaceProtocolMapping ipm, String wanIp, ServiceDetail serviceDetail) {
		if (satcoServiceDataRefreshBean.getWanIp() != null && !satcoServiceDataRefreshBean.getWanIp().trim().isEmpty()
				&& wanIp != null) {
			String[] wanIps = satcoServiceDataRefreshBean.getWanIp().split(",");

			if ((wanIps.length >= 2)
					|| ((wanIps.length == 1 && wanIps[0].contains("/") && !wanIps[0].split("/")[1].trim().equals("29"))
							|| (wanIps.length == 1
									&& wanIps[0].contains("/") && wanIps[0].split("/")[1].trim().equals("29")
									&& !serviceDetail.getLastmileProvider().toUpperCase().contains("POP"))
							|| (wanIps.length == 1 && wanIps[0].contains("/")
									&& wanIps[0].split("/")[1].trim().equals("29")
									&& serviceDetail.getLastmileProvider().toUpperCase().contains("POP")))) {

				Set<IpAddressDetail> ipAddressDetails = serviceDetail.getIpAddressDetails();

				if (ipm.getCpe() != null && ipm.getEndDate() == null) {
					EthernetInterface ethernetInterface = ipm.getEthernetInterface();

					Optional<IpAddressDetail> optinalIpAddressDetailForWanv4 = ipAddressDetails.stream()
							.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrWanv4Addresses() != null)
							.findFirst();

					if (optinalIpAddressDetailForWanv4.isPresent()) {

						optinalIpAddressDetailForWanv4.get().getIpaddrWanv4Addresses().stream().forEach(wanV4 -> {
							if (wanV4.getIscustomerprovided() != 1) {
								wanV4.setEndDate(new Timestamp(System.currentTimeMillis()));
								ipaddrWanv4AddressRepository.saveAndFlush(wanV4);
							}
						});

						Arrays.asList(wanIps).forEach(wanv4Addr -> {
							IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
							ipaddrWanv4Address.setIpAddressDetail(optinalIpAddressDetailForWanv4.get());
							String[] wanIpAndSubnet = wanIps[0].split("/");
							if ((!wanIpAndSubnet[1].trim().equals("29")
									&& !serviceDetail.getLastmileProvider().toUpperCase().contains("POP"))
									|| (wanIps.length==1)) {
								ipaddrWanv4Address.setIssecondary((byte) 0);

							} else if(wanIps.length>1){
								ipaddrWanv4Address.setIssecondary((byte) 1);
							}

							ipaddrWanv4Address.setIscustomerprovided((byte) 0);
							ipaddrWanv4Address.setWanv4Address(wanv4Addr);
							ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
						});
					}

					if (satcoServiceDataRefreshBean.getCustomerWanIp() != null) {
						String customerWanIpS[] = satcoServiceDataRefreshBean.getCustomerWanIp().split(",");

						String subNet[] = wanIps[0].split("/");
						String[] wanIpAndSubnet = wanIps[0].split("/");

						if (wanIps.length == 1 && wanIpAndSubnet[1].trim().equals("29")
								&& serviceDetail.getLastmileProvider().toUpperCase().contains("POP") && customerWanIpS.length > 1) {
							ethernetInterface.setModifiedSecondaryIpv4Address(customerWanIpS[1] + "/" + "29");

						} else {
							ethernetInterface.setModifiedIpv4Address(customerWanIpS[0] + "/" + subNet[1]);
							if (customerWanIpS.length > 1) {
								String subNe[] = wanIps[1].split("/");

								ethernetInterface.setModifiedSecondaryIpv4Address(customerWanIpS[1] + "/" + subNe[1]);

							}
						}
						ethernetInterfaceRepository.saveAndFlush(ethernetInterface);

						if (ipm.getBgp() != null && ipm.getEndDate() == null) {
							Bgp bgp = ipm.getBgp();
							bgp.setRemoteIpv4Address(customerWanIpS[0]);
							bgp.setNeighborOn("WAN");
							bgpRepository.saveAndFlush(bgp);
						}

					}

				}
				if (ipm.getRouterDetail() != null && ipm.getEndDate() == null) {
					String customerWanIpS[] = satcoServiceDataRefreshBean.getVsnlWanIp().split(",");

					EthernetInterface ethernetInterface = ipm.getEthernetInterface();
					Optional<IpAddressDetail> optinalIpAddressDetailForLanv4 = ipAddressDetails.stream()
							.filter(ipAdd -> ipAdd.getEndDate() == null && ipAdd.getIpaddrLanv4Addresses() != null)
							.findFirst();

					if (optinalIpAddressDetailForLanv4.isPresent()) {

						if (optinalIpAddressDetailForLanv4.get().getIpaddrLanv4Addresses() != null) {
							optinalIpAddressDetailForLanv4.get().getIpaddrLanv4Addresses().forEach(lanv4 -> {
								if (lanv4.getIscustomerprovided() != 1) {
									lanv4.setEndDate(endDate);
									ipaddrLanv4AddressRepository.saveAndFlush(lanv4);

								}
							});
						}
						if(satcoServiceDataRefreshBean.getLanIp()!=null ) {
						String lanIps[] = satcoServiceDataRefreshBean.getLanIp().split(",");
						Arrays.asList(lanIps).forEach(wanv4Addr -> {
							IpaddrLanv4Address ipaddrWanv4Address = new IpaddrLanv4Address();
							ipaddrWanv4Address.setIpAddressDetail(optinalIpAddressDetailForLanv4.get());
							ipaddrWanv4Address.setIssecondary((byte) 0);
							ipaddrWanv4Address.setIscustomerprovided((byte) 0);
							ipaddrWanv4Address.setLanv4Address(wanv4Addr);
							ipaddrLanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
						});
						}
					}

					if (ipm.getStaticProtocol() != null && ipm.getEndDate() == null) {
						StaticProtocol staticPro = ipm.getStaticProtocol();
						ipm.getStaticProtocol().getWanStaticRoutes().forEach(wanRout -> {
							if (wanRout.getIpsubnet() != null && wanRout.getIpsubnet().contains(".")) {

								wanRout.setEndDate(new Timestamp(System.currentTimeMillis()));
							}
						});

						List<IpaddrLanv4Address> ipaddrLanv4Addresses = ipaddrLanv4AddressRepository
								.findByIpAddressDetailId(optinalIpAddressDetailForLanv4.get().getIP_Address_Details());
						if (ipaddrLanv4Addresses != null) {
							ipaddrLanv4Addresses.forEach(lanv4 -> {
								if (lanv4.getLanv4Address().contains(".")) {
									WanStaticRoute wanStaticRoute = new WanStaticRoute();
									wanStaticRoute.setIpsubnet(lanv4.getLanv4Address());
									wanStaticRoute.setNextHopid(customerWanIpS[0]);
									wanStaticRoute.setStaticProtocol(staticPro);
									wanStaticRoute.setGlobal((byte) 0);
									wanStaticRoute.setIsPewan((byte) 1);
									wanStaticRoute.setIsCewan((byte) 0);
									wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
									wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
								}

							});
						}

					}
					String subNet[] = wanIps[0].split("/");
					String[] wanIpAndSubnet = wanIps[0].split("/");

					if (wanIps.length == 1 && wanIpAndSubnet[1].trim().equals("29")
							&& serviceDetail.getLastmileProvider().toUpperCase().contains("POP") && customerWanIpS.length > 1) {
						ethernetInterface.setModifiedSecondaryIpv4Address(customerWanIpS[1] + "/" + "29");

					} else {
						ethernetInterface.setModifiedIpv4Address(customerWanIpS[0] + "/" + subNet[1]);
						if (wanIps.length > 1) {
							String subNe[] = wanIps[1].split("/");
							ethernetInterface.setModifiedSecondaryIpv4Address(customerWanIpS[1] + "/" + subNe[1]);

						}
					}

					ethernetInterfaceRepository.saveAndFlush(ethernetInterface);

				}

			}

		}

	}

	private void getTclWanDetailsFromReverseISC(String serviceId, Integer scServiceDetailId, String subnet) {
		logger.info("getTclWanDetailsFromReverseISC invoked for Service Code:{} with subnet::{}", serviceId,subnet);
		VpnIllPortAttributes vpnIllPortAttributes = satSocService.getReverseIsc(serviceId,"izopc");
		if(Objects.nonNull(vpnIllPortAttributes) && !CollectionUtils.isEmpty(vpnIllPortAttributes.getAttribute())) {
			logger.info("IZOPC reverseISCAttributes exists for Service Code:{}", serviceId);
			String wanIpAddress = "", vsnlWanIpAddress = "", wanIpPool = "";
			List<Attribute> reverseISCAttributes = vpnIllPortAttributes.getAttribute().stream()
					.filter(reverseISCAttribute -> (("WAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())) || ("VSNLWAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())) ||
							("CustomerWAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())))).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(reverseISCAttributes)) {
				logger.info("IZOPC reverseISCAttributes exists for Service Code:{}", serviceId);
				Map<String,String> atMap=new HashMap<>();
				for(Attribute reverseISCAttribute : reverseISCAttributes){
					if("WAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())){
						wanIpPool = reverseISCAttribute.getValue();
						logger.info("WAN_IP Address from SatSoc Reverse for Service Code {} : {}", serviceId, wanIpPool);
						if(wanIpPool!=null) {
							String wanIpPoolArr[]=wanIpPool.split(",");
							if(wanIpPoolArr.length>1) {
								logger.info("IZOPC wanIpPoolArr from SatSoc Reverse for Service Code:{} length greater than 1", serviceId);
								atMap.put("wanIpPool",wanIpPoolArr[0].trim());
								atMap.put("secondaryWanIpPool",wanIpPoolArr[1].trim());
							}else if(wanIpPoolArr.length==1){
								logger.info("IZOPC wanIpPoolArr from SatSoc Reverse for Service Code:{} length is 1", serviceId);
								atMap.put("wanIpPool",wanIpPoolArr[0].trim());
							}
						}
					}else if("VSNLWAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())){
						vsnlWanIpAddress = reverseISCAttribute.getValue();
						logger.info("VSNLWAN_IP Address from SatSoc Reverse for Service Code {} : {}", serviceId, vsnlWanIpAddress);
						if(vsnlWanIpAddress!=null) {
							String vsnlWanIpAddressArr[]=vsnlWanIpAddress.split(",");
							if(vsnlWanIpAddressArr.length>1) {
								logger.info("IZOPC vsnlWanIpAddressArr from SatSoc Reverse for Service Code:{} length greater than 1", serviceId);
								atMap.put("vsnlWanIpAddress",vsnlWanIpAddressArr[0].trim()+"/"+subnet);
								atMap.put("secondaryVsnlWanIpAddress",vsnlWanIpAddressArr[1].trim()+"/"+subnet);
							}else if(vsnlWanIpAddressArr.length==1){
								logger.info("IZOPC vsnlWanIpAddressArr from SatSoc Reverse for Service Code:{} length is 1", serviceId);
								atMap.put("vsnlWanIpAddress",vsnlWanIpAddressArr[0].trim()+"/"+subnet);
							}
						}
					}
					else if("CustomerWAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())){
						wanIpAddress = reverseISCAttribute.getValue();
						logger.info("CustomerWAN_IP Address from SatSoc Reverse for Service Code {} : {}", serviceId, wanIpAddress);
						if(wanIpAddress!=null) {
							String wanIpAddressArr[]=wanIpAddress.split(",");
							if(wanIpAddressArr.length>1) {
								logger.info("IZOPC wanIpAddressArr from SatSoc Reverse for Service Code:{} length greater than 1", serviceId);
								atMap.put("wanIpAddress",wanIpAddressArr[0].trim()+"/"+subnet);
								atMap.put("secondaryWanIpAddress",wanIpAddressArr[1].trim()+"/"+subnet);
							}else if(wanIpAddressArr.length==1){
								logger.info("IZOPC wanIpAddressArr from SatSoc Reverse for Service Code:{} length is 1", serviceId);
								atMap.put("wanIpAddress",wanIpAddressArr[0].trim()+"/"+subnet);
							}
						}
					}
				}
				logger.info("IZOPC AttrMap from SatSoc Reverse for Service Code:{} which is TCL Provided : {}", serviceId, atMap);
				componentAndAttributeService.updateAttributes(scServiceDetailId, atMap, AttributeConstants.COMPONENT_LM,"A");
			}
		}
	}

	private void savePrefixRelatedIpaddrLanv4Address(ServiceDetail serviceDetail, ScComponentAttribute prefixForCspAttribute, int secondary) throws TclCommonException {
		logger.info("IZOPC savePrefixRelatedIpaddrLanv4Address for Service Id:{} which has prefixForCspAttribute Value : {}", serviceDetail.getId(), prefixForCspAttribute.getAttributeValue());
		try {
			String prefixes[] = prefixForCspAttribute.getAttributeValue().split(",");
			for (String prefix : prefixes) {
				logger.info("IZOPC savePrefixRelatedIpaddrLanv4Address for Service Id:{} which has prefix : {}", serviceDetail.getId(), prefix);
				IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
				ipaddrLanv4Address.setIpAddressDetail(saveIpAddressDetail(serviceDetail, "V4"));
				ipaddrLanv4Address.setIscustomerprovided((byte) 1);
				ipaddrLanv4Address.setLanv4Address(prefix.trim());
				ipaddrLanv4Address.setIssecondary((byte) secondary);
				ipaddrLanv4Address.setStartDate(startDate);
				ipaddrLanv4Address.setLastModifiedDate(lastModifiedDate);
				ipaddrLanv4Address.setModifiedBy(modifiedBy);
				ipaddrLanv4Address.setEndDate(endDate);
				ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
			}
		} catch (Exception e) {
			logger.error("Exception in savePrefixRelatedIpaddrLanv4Address => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	

}
