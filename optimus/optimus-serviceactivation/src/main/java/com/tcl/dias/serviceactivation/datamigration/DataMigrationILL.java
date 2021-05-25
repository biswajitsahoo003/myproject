package com.tcl.dias.serviceactivation.datamigration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.serviceactivation.activation.services.IPDetailsService;
import com.tcl.dias.serviceactivation.beans.IPServiceEndDateBean;
import com.tcl.dias.serviceactivation.datamigration.generated.beans.Args;
import com.tcl.dias.serviceactivation.datamigration.generated.beans.RunJobReturn;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.AluSchedulerPolicy;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.EthernetInterface;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.MigrationStatus;
import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PolicycriteriaProtocol;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RegexAspathConfig;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.RouterUplinkport;
import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.repository.AclPolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.AluSchedulerPolicyRepository;
import com.tcl.dias.serviceactivation.entity.repository.BgpRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedE1serialInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedSdhInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.CpeRepository;
import com.tcl.dias.serviceactivation.entity.repository.EthernetInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.InterfaceProtocolMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.MigrationStatusRepository;
import com.tcl.dias.serviceactivation.entity.repository.NeighbourCommunityConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeCriteriaMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicycriteriaProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RegexAspathConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterUplinkPortRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceCosCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceQoRepository;
import com.tcl.dias.serviceactivation.entity.repository.StaticProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.TopologyRepository;
import com.tcl.dias.serviceactivation.entity.repository.UniswitchDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.VrfRepository;
import com.tcl.dias.serviceactivation.entity.repository.WanStaticRouteRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class has methods for data migration from system xmls to
 * service_activation database
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class DataMigrationILL {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataMigrationILL.class);

	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private ServiceDetailRepository serviceDetailRepository;
	@Autowired
	private ServiceQoRepository serviceQoRepository;
	@Autowired
	private ServiceCosCriteriaRepository serviceCosCriteriaRepository;
	@Autowired
	private AluSchedulerPolicyRepository aluSchedulerPolicyRepository;
	@Autowired
	private TopologyRepository topologyRepository;
	@Autowired
	private RouterUplinkPortRepository routerUplinkPortRepository;
	@Autowired
	private UniswitchDetailRepository uniswitchDetailRepository;
	@Autowired
	private IpAddressDetailRepository ipAddressDetailRepository;
	@Autowired
	private IpaddrLanv4AddressRepository ipaddrLanv4AddressRepository;
	@Autowired
	private IpaddrWanv4AddressRepository ipaddrWanv4AddressRepository;
	@Autowired
	private IpaddrLanv6AddressRepository ipaddrLanv6AddressRepository;
	@Autowired
	private IpaddrWanv6AddressRepository ipaddrWanv6AddressRepository;
	@Autowired
	private RouterDetailRepository routerDetailRepository;
	@Autowired
	private EthernetInterfaceRepository ethernetInterfaceRepository;
	@Autowired
	private CpeRepository cpeRepository;
	@Autowired
	private VrfRepository vrfRepository;
	@Autowired
	private AclPolicyCriteriaRepository aclPolicyCriteriaRepository;
	@Autowired
	private VpnSolutionRepository vpnSolutionRepository;
	@Autowired
	private StaticProtocolRepository staticProtocolRepository;
	@Autowired
	private WanStaticRouteRepository wanStaticRouteRepository;
	@Autowired
	private BgpRepository bgpRepository;
	@Autowired
	private InterfaceProtocolMappingRepository interfaceProtocolMappingRepository;
	@Autowired
	private ChannelizedE1serialInterfaceRepository channelizedE1serialInterfaceRepository;
	@Autowired
	private ChannelizedSdhInterfaceRepository channelizedSdhInterfaceRepository;
	@Autowired
	private GenericWebserviceClient genericWebserviceClient;
	@Autowired
	private PolicyTypeRepository policyTypeRepository;
	@Autowired
	private PolicyCriteriaRepository policyCriteriaRepository;
	@Autowired
	private PrefixlistConfigRepository prefixlistConfigRepository;
	@Autowired
	private NeighbourCommunityConfigRepository neighbourCommunityConfigRepository;
	@Autowired
	private PolicycriteriaProtocolRepository policycriteriaProtocolRepository;
	@Autowired
	private PolicyTypeCriteriaMappingRepository policyTypeCriteriaMappingRepository;
	@Autowired
	private RegexAspathConfigRepository regexAspathConfigRepository;
	@Autowired
	private IPDetailsService ipDetService;
	@Autowired
	private MigrationStatusRepository migrationStatusRepository;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	private ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	private ScOrderRepository scOrderRepository;

	public Integer endServiceConfigRecords(IPServiceEndDateBean ipServiceEndDateBean) {

		String serviceId = ipServiceEndDateBean.getServiceId();
		String modifiedBy = ipServiceEndDateBean.getModifiedBy();
		ServiceDetail serviceDetail = null;
		Integer version = 0;

		if (ipServiceEndDateBean.getVersion() == null) {
			serviceDetail = serviceDetailRepository.findFirstByServiceIdOrderByVersionDesc(serviceId);
			if (serviceDetail != null) {
				version = serviceDetail.getVersion();
			}
		}

		if (serviceDetail != null) {
			if (ipServiceEndDateBean.getCurrentDate() == true) {
				ipDetService.endDateActivationRecords(serviceDetail, modifiedBy, true, true);
			} else if (ipServiceEndDateBean.getReverseEndDate() == true) {
				ipDetService.endDateActivationRecords(serviceDetail, modifiedBy, false, true);
			}
		}

		return version;

	}

	/**
	 * Migration method for IAS-ILL xml data to service_activation database
	 * 
	 * @author diksha garg
	 * @param isTermination 
	 * @param scServiceDetail 
	 * @param scOrder 
	 * @param order
	 * @return
	 * @throws Exception
	 */

	@Transactional
	public String migrateILL(JSONObject obj,String serviceId, ScOrder scOrder, ScServiceDetail scServiceDetail, String isTermination) throws Exception {

		Integer serviceDetailVersion = null;

		try {
			
			if(isTermination==null && serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId, "ISSUED") != null) {				
				return "ISSUED RECORD EXISTS!";				
			}
			
			JSONObject order = (JSONObject) obj.get("Order");

			if (!order.has("OrderInfo")) {
				throw new Exception("Order Details missing.");
			}
			JSONObject ias = (JSONObject) order.get("IAS");
			JSONObject orderInfo = (JSONObject) order.get("OrderInfo");

			// Error scenarios
			if (ias.has("ExtendedLAN") && ias.getJSONObject("ExtendedLAN").has("isEnabled")
					&& ias.getJSONObject("ExtendedLAN").get("isEnabled").toString().toLowerCase().equals("false")) {
				if (!ias.has("wanV4IPAddress")) {
					throw new Exception("Extended Lan is false but Wan v4 details  missing.");
				}
			}

			if (!ias.has("PERouter")) {
				throw new Exception("PE Router Details missing.");
			}

			if (!ias.has("WanRoutingProtocol") || !ias.getJSONObject("WanRoutingProtocol").has("xsi:type")) {
				throw new Exception("Protocol Details missing.");
			}

			if (!ias.has("PERouter") || !ias.getJSONObject("PERouter").has("wanInterface")
					|| !ias.getJSONObject("PERouter").getJSONObject("wanInterface").has("interface")) {
				throw new Exception("PE Router Interface Details missing.");
			}

			IPServiceEndDateBean ipServiceEndDateBean = new IPServiceEndDateBean();
			ipServiceEndDateBean.setCurrentDate(true);
			ipServiceEndDateBean.setModifiedBy("Optimus_Port_ILL_migration");
			serviceId = orderInfo.get("ServiceID").toString();
			ipServiceEndDateBean.setServiceId(serviceId);
			serviceDetailVersion = endServiceConfigRecords(ipServiceEndDateBean);

			/*
			 * // EndDate set of old versions List<ServiceDetail> serviceDetailsList = new
			 * ArrayList<>(); List<OrderDetail> orderDetailsList = new ArrayList<>();
			 * serviceDetailsList =
			 * serviceDetailRepository.findByServiceId(orderInfo.get("ServiceID").toString()
			 * ); Integer count = 0; ServiceDetail serviceDetailPrev = null; OrderDetail
			 * orderDetailPrev = null; for (ServiceDetail element : serviceDetailsList) { if
			 * (element.getEndDate() == null) { count++; serviceDetailPrev = element; if
			 * (count > 1) { throw new Exception(); } } } if (count == 1) {
			 * serviceDetailPrev.setEndDate(new Timestamp(new Date().getTime()));
			 * serviceDetailPrev.setVersion(serviceDetailPrev.getVersion() + 1);
			 * serviceDetailRepository.saveAndFlush(serviceDetailPrev); orderDetailsList =
			 * orderDetailRepository.findByOrderId(serviceDetailPrev.getOrderDetail().
			 * getOrderId()); if (orderDetailsList.size() > 1) { throw new Exception(); } if
			 * (orderDetailsList.size() != 0) { orderDetailPrev = orderDetailsList.get(0);
			 * orderDetailPrev.setEndDate(new Timestamp(new Date().getTime()));
			 * orderDetailRepository.saveAndFlush(orderDetailPrev); }
			 * 
			 * }
			 */

			OrderDetail orderDetail = null;
			orderDetail = saveOrderDetails(ias, orderInfo);
			if(isTermination!=null && isTermination.equalsIgnoreCase("yes")) {
				orderDetail.setOrderUuid(scOrder.getOpOrderCode());
				orderDetailRepository.saveAndFlush(orderDetail);
			}

			ServiceDetail serviceDetail1 = null;
			serviceDetail1 = saveServiceDetails(ias, orderInfo, orderDetail, serviceDetailVersion);
			if(isTermination!=null && isTermination.equalsIgnoreCase("yes")) {
				serviceDetail1.setScServiceDetailId(scServiceDetail.getId());
				serviceDetail1.setOrderType(scOrder.getParentOrderType());
				serviceDetail1.setServiceState("ACTIVE");
				serviceDetailRepository.saveAndFlush(serviceDetail1);
			}

			ServiceQo serviceQo1 = null;
			serviceQo1 = saveServiceQo(ias, serviceDetail1);

			// ILL has one record, GVPN has 6 records always
			saveServiceCosCriteria(ias, serviceQo1);

			// ALU SCheduler policy
			if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("ALUSchedulerPolicy")
					) {
				saveAluSchedulerPolicy(ias, serviceDetail1);
			}

			if (ias.getJSONObject("PERouter").has("topologyInfo")) {
				// topology
				Topology topology = null;
				topology = saveTopology(ias, serviceDetail1);

				// uplink
				if (ias.getJSONObject("PERouter").has("routerTopologyInterface1")) {
					saveRouterUplinkPort(ias, topology);
				}

				// uniswitch
				// UniswitchDetail uniswitchDetail = new UniswitchDetail();
				if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
						.has("interface")
						&& ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
								.getJSONObject("interface").getJSONObject("ethernetInterface").has("name")) {
					saveUniswitchDetails(ias, topology);
				}
			}

			// ipaddress
			IpAddressDetail ipAddressDetail = saveIpAddressDetail(ias, serviceDetail1);

			// lanv4
			String cpe_loopback = null; // enhancement

			// cpeMgmtLoopback
			if (ias.has("cpe") && ias.getJSONObject("cpe").has("loopbackInterface")
					&& ias.getJSONObject("cpe").get("loopbackInterface") instanceof JSONObject
					&& ias.getJSONObject("cpe").getJSONObject("loopbackInterface").has("v4IpAddress")
					&& ias.getJSONObject("cpe").getJSONObject("loopbackInterface")
							.get("v4IpAddress") instanceof JSONObject
					&& ias.getJSONObject("cpe").getJSONObject("loopbackInterface").getJSONObject("v4IpAddress")
							.has("address")) {
				try {
					cpe_loopback = ias.getJSONObject("cpe").getJSONObject("loopbackInterface")
							.getJSONObject("v4IpAddress").get("address").toString();

				} catch (Exception e) {
					LOGGER.error("error in cpe_loopback value extraction code ILL for service id {} as {} : ",
							serviceId, e);
				}

			}
			
			if (ias.has("lanV4IPAddress")) {
				Object lanv4 = ias.get("lanV4IPAddress");
				if (lanv4 instanceof JSONObject) {
					JSONObject lanv4Object = (JSONObject) lanv4;
					if (lanv4Object.has("address")) {
						
						String lanv4_subnet=lanv4Object.get("address").toString();
						String[] lanv4_wo_subnet=lanv4_subnet.split("/");
						
						if(cpe_loopback==null || (cpe_loopback!=null && !cpe_loopback.isEmpty() && !cpe_loopback.contains(lanv4_wo_subnet[0]))) {
						IpaddrLanv4Address ipaddrLanv4Address = setlanv4(lanv4Object, ipAddressDetail);
						Set<IpaddrLanv4Address> ipaddrLanv4Addresses = new HashSet<>();
						ipaddrLanv4Addresses.add(ipaddrLanv4Address);
						ipAddressDetail.setIpaddrLanv4Addresses(ipaddrLanv4Addresses);
						}
					}
				} else if (lanv4 instanceof JSONArray) {
					JSONArray lanv4Array = (JSONArray) lanv4;
					Set<IpaddrLanv4Address> ipaddrLanv4Addresses = new HashSet<>();
					for (int i = 0; i < lanv4Array.length(); i++) {
						if (lanv4Array.get(i) instanceof JSONObject) {
							JSONObject lanv4Object = (JSONObject) lanv4Array.get(i);
							if (lanv4Object.has("address")) {
								
								String lanv4_subnet=lanv4Object.get("address").toString();
								String[] lanv4_wo_subnet=lanv4_subnet.split("/");
								
								if(cpe_loopback==null || (cpe_loopback!=null && !cpe_loopback.isEmpty() && !cpe_loopback.contains(lanv4_wo_subnet[0]))) {
								IpaddrLanv4Address ipaddrLanv4Address = setlanv4(lanv4Object, ipAddressDetail);
								ipaddrLanv4Addresses.add(ipaddrLanv4Address);
								}
							}
						}
					}
					ipAddressDetail.setIpaddrLanv4Addresses(ipaddrLanv4Addresses);
				}
			}

			// Wanv4
			if (ias.has("wanV4IPAddress")) {
				Object wanv4 = ias.getJSONObject("wanV4IPAddress");
				if (wanv4 instanceof JSONObject) {
					JSONObject wanv4Object = (JSONObject) wanv4;
					if (wanv4Object.has("address")) {
						IpaddrWanv4Address ipaddrwanv4Address = setwanv4(wanv4Object, ipAddressDetail);
						Set<IpaddrWanv4Address> ipaddrWanv4Addresses = new HashSet<>();
						ipaddrWanv4Addresses.add(ipaddrwanv4Address);
						ipAddressDetail.setIpaddrWanv4Addresses(ipaddrWanv4Addresses);
					}
				} else if (wanv4 instanceof JSONArray) {
					JSONArray wanv4Array = (JSONArray) wanv4;
					Set<IpaddrWanv4Address> ipaddrWanv4Addresses = new HashSet<>();
					for (int i = 0; i < wanv4Array.length(); i++) {
						JSONObject wanv4Object = (JSONObject) wanv4Array.get(i);
						if (wanv4Object.has("address")) {
							IpaddrWanv4Address ipaddrWanv4Address = setwanv4(wanv4Object, ipAddressDetail);
							ipaddrWanv4Addresses.add(ipaddrWanv4Address);
						}
					}
					ipAddressDetail.setIpaddrWanv4Addresses(ipaddrWanv4Addresses);
				}
			}
			// wanv6
			if (ias.has("wanV6IPAddress")) {
				Object wanv6 = ias.getJSONObject("wanV6IPAddress");
				if (wanv6 instanceof JSONObject) {
					JSONObject wanv6Object = (JSONObject) wanv6;
					if (wanv6Object.has("address")) {
						IpaddrWanv6Address ipaddrwanv6Address = setwanv6(wanv6Object, ipAddressDetail);
						Set<IpaddrWanv6Address> ipaddrWanv6Addresses = new HashSet<>();
						ipaddrWanv6Addresses.add(ipaddrwanv6Address);
						ipAddressDetail.setIpaddrWanv6Addresses(ipaddrWanv6Addresses);
					}
				} else if (wanv6 instanceof JSONArray) {
					/*JSONArray wanv6Array = (JSONArray) wanv6;
					Set<IpaddrWanv6Address> ipaddrWanv6Addresses = new HashSet<>();
					ipAddressDetail.setIpaddrWanv6Addresses(wanv6Array.toList().stream()
							.map(wanv6Object -> setwanv6((JSONObject) wanv6Object, ipAddressDetail))
							.collect(Collectors.toSet()));
					ipAddressDetail.setIpaddrWanv6Addresses(ipaddrWanv6Addresses);*/
					
					JSONArray wanv6Array = (JSONArray) wanv6;
					Set<IpaddrWanv6Address> ipaddrWanv6Addresses = new HashSet<>();
					for (int i = 0; i < wanv6Array.length(); i++) {
						if (wanv6Array.get(i) instanceof JSONObject) {
							JSONObject wanv6Object = (JSONObject) wanv6Array.get(i);
							if (wanv6Object.has("address")) {
								IpaddrWanv6Address ipaddrLanv6Address = setwanv6(wanv6Object, ipAddressDetail);
								ipaddrWanv6Addresses.add(ipaddrLanv6Address);
							}
						}
					}
					ipAddressDetail.setIpaddrWanv6Addresses(ipaddrWanv6Addresses);
				}
			}

			// lanv6
			if (ias.has("lanV6IPAddress")) {
				Object lanv6 = ias.get("lanV6IPAddress");

				if (lanv6 instanceof JSONObject) {
					JSONObject lanv6Object = (JSONObject) lanv6;
					if (lanv6Object.has("address")) {
						IpaddrLanv6Address ipaddrLanv6Address = setlanv6(lanv6Object, ipAddressDetail);
						Set<IpaddrLanv6Address> ipaddrLanv6Addresses = new HashSet<>();
						ipaddrLanv6Addresses.add(ipaddrLanv6Address);
						ipAddressDetail.setIpaddrLanv6Addresses(ipaddrLanv6Addresses);
					}
				} else if (lanv6 instanceof JSONArray) {
					/*JSONArray lanv6Array = (JSONArray) lanv6;
					Set<IpaddrLanv6Address> ipaddrLanv6Addresses = new HashSet<>();
					ipAddressDetail.setIpaddrLanv6Addresses(lanv6Array.toList().stream()
							.map(lanv6Object -> setlanv6((JSONObject) lanv6Object, ipAddressDetail))
							.collect(Collectors.toSet()));
					ipAddressDetail.setIpaddrLanv6Addresses(ipaddrLanv6Addresses);*/
					
					JSONArray lanv6Array = (JSONArray) lanv6;
					Set<IpaddrLanv6Address> ipaddrLanv6Addresses = new HashSet<>();
					for (int i = 0; i < lanv6Array.length(); i++) {
						if (lanv6Array.get(i) instanceof JSONObject) {
							JSONObject lanv6Object = (JSONObject) lanv6Array.get(i);
							if (lanv6Object.has("address")) {
								IpaddrLanv6Address ipaddrLanv6Address = setlanv6(lanv6Object, ipAddressDetail);
								ipaddrLanv6Addresses.add(ipaddrLanv6Address);
							}
						}
					}
					ipAddressDetail.setIpaddrLanv6Addresses(ipaddrLanv6Addresses);				
				}
			}

			// router
			RouterDetail routerDetail = saveRouterDetail(ias);

			// ethernet interface
			EthernetInterface ethernetInterface = null;
			ChannelizedE1serialInterface channelizedE1serialInterface = null;
			ChannelizedSdhInterface channelizedSdhInterface = null;

			JSONObject wanInterface = ias.getJSONObject("PERouter").getJSONObject("wanInterface")
					.getJSONObject("interface");

			Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();

			if (wanInterface.get("xsi:type").toString().contains("ethernet")
					|| wanInterface.get("xsi:type").toString().contains("Ethernet")) {

				ethernetInterface = saveEthernetInterface(wanInterface);

				// ACL POLICY CRITERIA
				Set<String> keySet = wanInterface.keySet();
				for (String key : keySet) {
					String regEx = "\\w*boundAccessControlList\\w*";

					Boolean keyNeeded = key.matches(regEx);
					if (keyNeeded) {
						JSONObject aclObject = wanInterface.getJSONObject(key);
					//	System.out.println(key);
						AclPolicyCriteria aclPolicyCriteria = setAclPolicyCriteria(key, aclObject, ethernetInterface,
								channelizedE1serialInterface, channelizedSdhInterface,
								wanInterface.get("name").toString());
						aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
						aclPolicyCriterias.add(aclPolicyCriteria);
					}

				}
			}

			if (wanInterface.get("xsi:type").toString().contains("serial")
					|| wanInterface.get("xsi:type").toString().contains("Serial")) {

				channelizedE1serialInterface = saveChannelizedE1SerialDetails(wanInterface);

				// ACL POLICY CRITERIA
				Set<String> keySet = wanInterface.keySet();
				for (String key : keySet) {
					String regEx = "\\w*boundAccessControlList\\w*";

					Boolean keyNeeded = key.matches(regEx);
					if (keyNeeded) {
						JSONObject aclObject = wanInterface.getJSONObject(key);
					//	System.out.println(key);
						AclPolicyCriteria aclPolicyCriteria = setAclPolicyCriteria(key, aclObject, ethernetInterface,
								channelizedE1serialInterface, channelizedSdhInterface,
								wanInterface.get("name").toString());
						aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
						aclPolicyCriterias.add(aclPolicyCriteria);
					}

				}
			}

			if (wanInterface.get("xsi:type").toString().contains("sdh")
					|| wanInterface.get("xsi:type").toString().contains("SDH")) {

				channelizedSdhInterface = saveChannelizedSdhInterface(wanInterface);
				// ACL POLICY CRITERIA
				Set<String> keySet = wanInterface.keySet();
				for (String key : keySet) {
					
					String regEx = "\\w*boundAccessControlList\\w*";

					Boolean keyNeeded = key.matches(regEx);
					if (keyNeeded) {
						JSONObject aclObject = wanInterface.getJSONObject(key);
					//	System.out.println(key);
						AclPolicyCriteria aclPolicyCriteria = setAclPolicyCriteria(key, aclObject, ethernetInterface,
								channelizedE1serialInterface, channelizedSdhInterface,
								wanInterface.get("name").toString());
						aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
						aclPolicyCriterias.add(aclPolicyCriteria);
					}

				}
			}

			// CPE - ETHERNET
			EthernetInterface ethernetInterface2 = null;
			Cpe cpe = null;

			if (ias.has("cpe")) {

				// cpeWanInterface
				JSONObject cpewanInterface = ias.getJSONObject("cpe").getJSONObject("wanInterface")
						.getJSONObject("interface");
				ethernetInterface2 = saveEthernetCpe(cpewanInterface);

				// qos login passthrough comes for ethernet for cisco
				// cpe
				cpe = saveCpe(ias, cpewanInterface, serviceDetail1.getServiceId());
			}

			// vrf
			Vrf vrf = null;
			try {
				if (ias.has("vrf") && ias.getJSONObject("vrf").has("name")) {
					vrf = saveVrf(ias, serviceDetail1);
				}
			}catch(Exception e) {
				LOGGER.error("Exception in saveIllVRF::{} for serviceId::{}",e,serviceId);
			}
			
			if(vrf==null) {
				LOGGER.info("Vrf not exists from Talend for serviceId::{}",serviceId);
				vrf = new Vrf();
				vrf.setEndDate(null);
				vrf.setLastModifiedDate(new Timestamp(new Date().getTime()));
				vrf.setServiceDetail(serviceDetail1);
				vrf.setStartDate(new Timestamp(new Date().getTime()));
				vrf.setVrfName("NOT_APPLICABLE");// internet,primmus,not applicable for ILL
				Byte defValue = 0;
				vrf.setIsmultivrf(defValue);
				vrf.setIsvrfLiteEnabled(defValue);
				vrf.setMastervrfServiceid(null);
				vrf.setModifiedBy("Optimus_Port_ILL_migration");
				vrf.setPolicyTypes(null);
				vrfRepository.saveAndFlush(vrf);
			}

			// VPNSOLUTION
			// Multiple and variable in GVPN, single in ILL
			try {
				if (ias.has("SolutionTable") && !ias.getJSONObject("vrf").get("name").toString().equals("NOT_APPLICABLE")
						&& !ias.getJSONObject("vrf").get("name").toString().equals("NA")) {
					saveVpnSolution(ias, serviceDetail1);
				}
			}catch(Exception e) {
				LOGGER.error("Exception in saveVpnSolution::{} for serviceId::{}",e,serviceId);
			}
			

			// static protocol
			StaticProtocol staticProtocol = null;
			JSONObject wanRoutingProtocol;

			if (ias.getJSONObject("WanRoutingProtocol").get("xsi:type").toString().equals("StaticRoutingProtocol")) {
				// static
				wanRoutingProtocol = ias.getJSONObject("WanRoutingProtocol");
				staticProtocol = saveStaticProtocol(wanRoutingProtocol);

				// WAN STATIC ROUTES
				// ispewan=yes
				if (ias.getJSONObject("WanRoutingProtocol").get("xsi:type").toString().equals("StaticRoutingProtocol")
						&& ias.getJSONObject("WanRoutingProtocol").get("id").toString().equals("Static")
						&& ias.getJSONObject("WanRoutingProtocol").has("peWANStaticRoutes")) {
					// JSONObject wanStaticRouteObject = ias.getJSONObject("WanRoutingProtocol");
					if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
							.get("staticRouteList") instanceof JSONObject) {
						saveStaticWanPe(wanRoutingProtocol,
								wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList"),
								staticProtocol);
					} else if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
							.get("staticRouteList") instanceof JSONArray) {
						
						for(Object staticListObj : wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
							.getJSONArray("staticRouteList"))
						{
							JSONObject staticListJson = (JSONObject) staticListObj;
							saveStaticWanPe(wanRoutingProtocol,staticListJson,staticProtocol);
						}

					}
				}

				// is cewan
				if (ias.getJSONObject("WanRoutingProtocol").get("xsi:type").toString().equals("StaticRoutingProtocol")
						&& ias.getJSONObject("WanRoutingProtocol").get("id").toString().equals("Static")
						&& ias.getJSONObject("WanRoutingProtocol").has("ceWANStaticRoutes")) {
					// JSONObject wanStaticRouteObject = ias.getJSONObject("WanRoutingProtocol");
					saveStaticWanCe(wanRoutingProtocol, staticProtocol);
				}
			}

			Bgp bgp = null;
			if (ias.getJSONObject("WanRoutingProtocol").get("xsi:type").toString().equals("BGPRoutingProtocol")) {

				// BGP
				wanRoutingProtocol = ias.getJSONObject("WanRoutingProtocol");
				Set<String> keys = wanRoutingProtocol.keySet();// for getting and checking keys, regex and contains

				bgp = saveBgp(ias);

				// ispewan=yes
				if (ias.getJSONObject("WanRoutingProtocol").get("xsi:type").toString().equals("StaticRoutingProtocol")
						&& ias.getJSONObject("WanRoutingProtocol").get("id").toString().equals("Static")
						&& ias.getJSONObject("WanRoutingProtocol").has("bgpWANStaticRoutes")) {
					// JSONObject wanStaticRouteObject = ias.getJSONObject("WanRoutingProtocol");
					saveWanStaticRouteBgp(wanRoutingProtocol, bgp);
				}

				// for CISCO
				if (ias.getJSONObject("PERouter").has("make") && (ias.getJSONObject("PERouter").get("make").toString().equals("CISCO IP") || ias.getJSONObject("PERouter").get("make").toString().equals("CISCO")) ){
					// set only prefix list assuming it to be match criteria
					for (String key : keys) {

						String regEx = "is\\w*boundBGP\\w*PrefixesRequired"; // isInboundBGPV4PrefixesRequired
						Boolean keyNeeded = key.matches(regEx);
						// "isInboundBGPV4PrefixesRequired"
						if (keyNeeded.toString().equals("true") && ias.getJSONObject("WanRoutingProtocol").get(key)
								.toString().toLowerCase().contains("true")) {

					//		System.out.println(key);
							PolicyType policyType = new PolicyType();

							if (key.contains("Inbound")) {
								if (key.contains("V4")) {
									Byte flag = 1;
									policyType.setInboundRoutingIpv4Policy(flag);
									policyType.setBgp(bgp);
									policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
									policyType.setModifiedBy("Optimus_Port_ILL_migration");
									policyType.setStartDate(new Timestamp(new Date().getTime()));
									policyTypeRepository.saveAndFlush(policyType);
								}
								if (key.contains("V6")) {
									Byte flag = 1;
									policyType.setInboundRoutingIpv6Policy(flag);
									policyType.setBgp(bgp);
									policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
									policyType.setModifiedBy("Optimus_Port_ILL_migration");
									policyType.setStartDate(new Timestamp(new Date().getTime()));
									policyTypeRepository.saveAndFlush(policyType);
								}
							}
							if (key.contains("Outbound")) {
								if (key.contains("V4")) {
									Byte flag = 1;
									policyType.setOutboundRoutingIpv4Policy(flag);
									policyType.setBgp(bgp);
									policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
									policyType.setModifiedBy("Optimus_Port_ILL_migration");
									policyType.setStartDate(new Timestamp(new Date().getTime()));
									policyTypeRepository.saveAndFlush(policyType);
								}
								if (key.contains("V6")) {
									Byte flag = 1;
									policyType.setOutboundRoutingIpv6Policy(flag);
									policyType.setBgp(bgp);
									policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
									policyType.setModifiedBy("Optimus_Port_ILL_migration");
									policyType.setStartDate(new Timestamp(new Date().getTime()));
									policyTypeRepository.saveAndFlush(policyType);
								}
							}

							String keyMed = key.replace("Required", "Allowed");
							String keyMed2 = keyMed.replace("V4", "v4");
							String keyMed3 = keyMed2.replace("V6", "v6");
							String objectKey1 = keyMed3.replace("isInbound", "inbound");
							String objectKey = objectKey1.replace("isOutbound", "outbound");
							JSONObject objectRequired = ias.getJSONObject("WanRoutingProtocol").getJSONObject(objectKey);

							PolicyCriteria policyCriteria1 = new PolicyCriteria();
							// since JSONObject["isPrefixListPreprovisioned"] not found.
							Byte isPreProv = 1;
							// if
							// (objectRequired.get("isPrefixListPreprovisioned").toString().equals("true"))
							// {
							if (objectRequired.has("prefixListEntry")) {
								isPreProv = 0;
							}
							Byte flag = 1;

							policyCriteria1.setMatchcriteriaPrefixlist(flag);
							if (objectRequired.has("name"))
								policyCriteria1.setMatchcriteriaPrefixlistName(objectRequired.get("name").toString());
							policyCriteria1.setMatchcriteriaPprefixlistPreprovisioned(isPreProv);

							policyCriteria1.setLastModifiedDate(new Timestamp(new Date().getTime()));
							policyCriteria1.setModifiedBy("Optimus_Port_ILL_migration");
							policyCriteria1.setStartDate(new Timestamp(new Date().getTime()));
							policyCriteriaRepository.saveAndFlush(policyCriteria1);

							PolicyTypeCriteriaMapping policyTypeCriteriaMapping1 = new PolicyTypeCriteriaMapping();
							policyTypeCriteriaMapping1.setPolicyType(policyType);
							policyTypeCriteriaMapping1.setPolicyCriteriaId(policyCriteria1.getPolicyCriteriaId());
							policyTypeCriteriaMapping1.setVersion(1);
							policyTypeCriteriaMapping1.setLastModifiedDate(new Timestamp(new Date().getTime()));
							policyTypeCriteriaMapping1.setModifiedBy("Optimus_Port_ILL_migration");
							policyTypeCriteriaMapping1.setStartDate(new Timestamp(new Date().getTime()));
							policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping1);

							// since JSONObject["isPrefixListPreprovisioned"] not found.

							/*
							 * if (objectRequired.get("isPrefixListPreprovisioned").toString().toLowerCase()
							 * .equals("false") ||
							 */
							if (objectRequired.has("prefixListEntry")) {

								if (objectRequired.get("prefixListEntry") instanceof JSONObject) {

									JSONObject prefixList = objectRequired.getJSONObject("prefixListEntry");
									savePrefixListEntryCisco(prefixList, policyType, policyCriteria1);

								} else if (objectRequired.get("prefixListEntry") instanceof JSONArray) {
									JSONArray prefixListArray = objectRequired.getJSONArray("prefixListEntry");

									for (Object prefixListObject : prefixListArray) {

										JSONObject prefixList = (JSONObject) prefixListObject;
										savePrefixListEntryCisco(prefixList, policyType, policyCriteria1);
									}

								}
							}

						}

						if (key.equals("neighbourCommunity")) {

							PolicyType policyType = new PolicyType();

							String pathIp = ipAddressDetail.getPathIpType();

							if (pathIp.contains("V4") || pathIp.equals("DUALSTACK")) {
								Byte flag = 1;
								policyType.setInboundRoutingIpv4Policy(flag);
								policyType.setBgp(bgp);
								policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
								policyType.setModifiedBy("Optimus_Port_ILL_migration");
								policyType.setStartDate(new Timestamp(new Date().getTime()));
								policyTypeRepository.saveAndFlush(policyType);
							}
							if (pathIp.contains("V6")) {
								Byte flag = 1;
								policyType.setInboundRoutingIpv6Policy(flag);
								policyType.setBgp(bgp);
								policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
								policyType.setModifiedBy("Optimus_Port_ILL_migration");
								policyType.setStartDate(new Timestamp(new Date().getTime()));
								policyTypeRepository.saveAndFlush(policyType);
							}

							PolicyCriteria policyCriteria4 = new PolicyCriteria(); // neighbourcomm
//							policyCriteria4.setTermSetcriteriaAction(termSetCriteriaAction);
//							policyCriteria4.setTermName(termName);
							// policyCriteria4.setMatchcriteriaNeighbourcommunityName(termObject.getJSONObject("matchCriteria")
							// .getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("Name").toString());
							Byte flag = 1;
							policyCriteria4.setMatchcriteriaNeighbourCommunity(flag);
							// PolicyCriteria p=
							// policyCriteriaRepository.saveAndFlush(policyCriteria4);
							policyCriteria4.setLastModifiedDate(new Timestamp(new Date().getTime()));
							policyCriteria4.setModifiedBy("Optimus_Port_ILL_migration");
							policyCriteria4.setStartDate(new Timestamp(new Date().getTime()));
							policyCriteriaRepository.saveAndFlush(policyCriteria4);

							PolicyTypeCriteriaMapping policyTypeCriteriaMapping4 = new PolicyTypeCriteriaMapping();
							policyTypeCriteriaMapping4.setPolicyType(policyType);
							policyTypeCriteriaMapping4.setPolicyCriteriaId(policyCriteria4.getPolicyCriteriaId());
							policyTypeCriteriaMapping4.setVersion(1);
							policyTypeCriteriaMapping4.setLastModifiedDate(new Timestamp(new Date().getTime()));
							policyTypeCriteriaMapping4.setModifiedBy("Optimus_Port_ILL_migration");
							policyTypeCriteriaMapping4.setStartDate(new Timestamp(new Date().getTime()));
							policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping4);

							if (wanRoutingProtocol.get("neighbourCommunity") instanceof JSONObject) {

								saveNeighbourCommunityConfigCisco(wanRoutingProtocol, policyType, policyCriteria4);

							} else if (wanRoutingProtocol.get("neighbourCommunity") instanceof JSONArray) {
								JSONArray neighbourCommunityArray = (JSONArray) wanRoutingProtocol
										.getJSONArray("neighbourCommunity");

								for (Object objectNeightbourComm : neighbourCommunityArray) {

									// JSONObject neightbourComm = (JSONObject) objectNeightbourComm;
									saveNeighbourCommunityConfigCisco(objectNeightbourComm, policyType,
											policyCriteria4);

								}
							}

						}
					}
				} else {
					for (String key : keys) {
						String regEx = "\\w*BGP\\w*eigbour\\w*boundRoutingPolicy\\w*";
						// for
						// each
						// match,
						// inbound
						// outbound
						// v4
						// v6
						// alcatel juniper, make a new record
						Boolean keyNeeded = key.matches(regEx);
						if (keyNeeded) {
							JSONObject policyObject = wanRoutingProtocol.getJSONObject(key);
						//	System.out.println(key);
							if (policyObject.has("Name"))
								setPolicyTypeAndCriterias(key, policyObject, bgp);
						}
					}
				}
			}

			// interface protocol mapping for PE
			saveIpmPe(ias, routerDetail, serviceDetail1, staticProtocol, bgp, ethernetInterface,
					channelizedSdhInterface, channelizedE1serialInterface);

			// interface protocol mapping -CPE
			if (ias.has("cpe")) {
				saveIpmCpe(ias, cpe, serviceDetail1, ethernetInterface2, channelizedSdhInterface,
						channelizedE1serialInterface);
			}

			if (ias.has("ALUVPRNImportPolicy")) {
				JSONObject vprnPolicyObj = ias.getJSONObject("ALUVPRNImportPolicy");
				saveVprnImportPolicy(vprnPolicyObj, vrf);
			}

			if (ias.has("ALUVPRNExportPolicy")) {
				JSONObject vprnPolicyObj = ias.getJSONObject("ALUVPRNExportPolicy");
				saveVprnExportPolicy(vprnPolicyObj, vrf);

			}

			return "SUCCESS";
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}
	}

	// METHODS
	public Integer banwidthConversion(String bandwidthUnit, Integer bandwidth) {

		if (bandwidth == null) {
			return 0;
		}
		if (bandwidthUnit.equalsIgnoreCase("mbps")) {
			return bandwidth * 1024;
		} else if (bandwidthUnit.equalsIgnoreCase("gbps")) {
			return bandwidth * 1000 * 1024;
		} else if (bandwidthUnit.equalsIgnoreCase("kbps")) {
			return bandwidth * 1000;
		}
		return bandwidth;

	}

	public void saveVprnExportPolicy(JSONObject vprnPolicyObj, Vrf vrf) {

		PolicyType policyType = new PolicyType();
		policyType.setIsvprnExportpolicy((byte) 1);
		if (vprnPolicyObj.has("Name")) {
			policyType.setIsvprnExportpolicyName(vprnPolicyObj.get("Name").toString());
		}
		if (vprnPolicyObj.has("isPreprovisioned")) {
			Byte flag = 0;
			if (vprnPolicyObj.get("isPreprovisioned").toString().equalsIgnoreCase("true")) {
				flag = (byte) 1;
			}
			policyType.setIsvprnExportPreprovisioned(flag);
		}
		policyType.setStartDate(new Timestamp(new Date().getTime()));
		policyType.setModifiedBy("Optimus_Port_ILL_migration");
		policyType.setVrf(vrf);
	}

	public void saveVprnImportPolicy(JSONObject vprnPolicyObj, Vrf vrf) {

		PolicyType policyType = new PolicyType();
		policyType.setIsvprnImportpolicy((byte) 1);
		if (vprnPolicyObj.has("Name")) {
			policyType.setIsvprnImportpolicyName(vprnPolicyObj.get("Name").toString());
		}
		if (vprnPolicyObj.has("isPreprovisioned")) {
			Byte flag = 0;
			if (vprnPolicyObj.get("isPreprovisioned").toString().equalsIgnoreCase("true")) {
				flag = (byte) 1;
			}
			policyType.setIsvprnImportPreprovisioned(flag);
		}
		policyType.setStartDate(new Timestamp(new Date().getTime()));
		policyType.setModifiedBy("Optimus_Port_ILL_migration");
		policyType.setVrf(vrf);
	}

	public void saveIpmCpe(JSONObject ias, Cpe cpe, ServiceDetail serviceDetail1, EthernetInterface ethernetInterface2,
			ChannelizedSdhInterface channelizedSdhInterface,
			ChannelizedE1serialInterface channelizedE1serialInterface) {
		InterfaceProtocolMapping interfaceProtocolMapping2 = new InterfaceProtocolMapping();
		interfaceProtocolMapping2.setBgp(null);
		interfaceProtocolMapping2.setStaticProtocol(null);
		interfaceProtocolMapping2.setChannelizedE1serialInterface(null);// ?
		interfaceProtocolMapping2.setChannelizedSdhInterface(null);// ?
		interfaceProtocolMapping2.setCpe(cpe);
		interfaceProtocolMapping2.setEigrp(null);
		interfaceProtocolMapping2.setEndDate(null);
		if (ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface").get("xsi:type")
				.toString().contains("ethernet")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("Ethernet"))
			interfaceProtocolMapping2.setEthernetInterface(ethernetInterface2);

		if (ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface").get("xsi:type")
				.toString().contains("SDH")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("sdh")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("Sdh"))
			interfaceProtocolMapping2.setChannelizedSdhInterface(channelizedSdhInterface);// ?

		if (ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface").get("xsi:type")
				.toString().contains("Serial")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("serial"))
			interfaceProtocolMapping2.setChannelizedE1serialInterface(channelizedE1serialInterface);// ?

		interfaceProtocolMapping2.setIscpeLanInterface((byte) 0);
		interfaceProtocolMapping2.setIscpeWanInterface((byte) 1);
		interfaceProtocolMapping2.setLastModifiedDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping2.setModifiedBy("Optimus_Port_ILL_migration");
		interfaceProtocolMapping2.setOspf(null);
		interfaceProtocolMapping2.setRip(null);
		interfaceProtocolMapping2.setRouterDetail(null);
		interfaceProtocolMapping2.setServiceDetail(serviceDetail1);
		interfaceProtocolMapping2.setStartDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping2.setVersion(1);
		interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping2);
	}

	public void saveIpmPe(JSONObject ias, RouterDetail routerDetail, ServiceDetail serviceDetail1,
			StaticProtocol staticProtocol, Bgp bgp, EthernetInterface ethernetInterface,
			ChannelizedSdhInterface channelizedSdhInterface,
			ChannelizedE1serialInterface channelizedE1serialInterface) {
		InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
		if (bgp != null) {
			interfaceProtocolMapping.setBgp(bgp);
		} else {
			interfaceProtocolMapping.setStaticProtocol(staticProtocol);
		}
		if (ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface").get("xsi:type")
				.toString().contains("ethernet")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("Ethernet"))
			interfaceProtocolMapping.setEthernetInterface(ethernetInterface);

		if (ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface").get("xsi:type")
				.toString().contains("SDH")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("sdh")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("Sdh"))
			interfaceProtocolMapping.setChannelizedSdhInterface(channelizedSdhInterface);// ?

		if (ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface").get("xsi:type")
				.toString().contains("Serial")
				|| ias.getJSONObject("PERouter").getJSONObject("wanInterface").getJSONObject("interface")
						.get("xsi:type").toString().contains("serial"))
			interfaceProtocolMapping.setChannelizedE1serialInterface(channelizedE1serialInterface);// ?

		interfaceProtocolMapping.setCpe(null);
		interfaceProtocolMapping.setEigrp(null);
		interfaceProtocolMapping.setEndDate(null);
		interfaceProtocolMapping.setIscpeLanInterface((byte) 0);
		interfaceProtocolMapping.setIscpeWanInterface((byte) 0);
		interfaceProtocolMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping.setModifiedBy("Optimus_Port_ILL_migration");
		interfaceProtocolMapping.setOspf(null);
		interfaceProtocolMapping.setRip(null);
		interfaceProtocolMapping.setRouterDetail(routerDetail);
		interfaceProtocolMapping.setServiceDetail(serviceDetail1);
		interfaceProtocolMapping.setStartDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping.setVersion(1);
		interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping);
	}

	public void savePrefixListEntryCisco(JSONObject prefixList, PolicyType policyType, PolicyCriteria policyCriteria1) {
		PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
		prefixlistConfig.setPolicyCriteria(policyCriteria1);
		if (prefixList.has("networkPrefix"))
			prefixlistConfig.setNetworkPrefix(prefixList.get("networkPrefix").toString());
		prefixlistConfig.setAction(null);
		if (prefixList.has("subnetRangeMaximum"))
			prefixlistConfig.setLeValue(prefixList.get("subnetRangeMaximum").toString());
		if (prefixList.has("subnetRangeMinimum"))
			prefixlistConfig.setGeValue(prefixList.get("subnetRangeMinimum").toString());

		prefixlistConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		prefixlistConfig.setModifiedBy("Optimus_Port_ILL_migration");
		prefixlistConfig.setStartDate(new Timestamp(new Date().getTime()));
		prefixlistConfigRepository.saveAndFlush(prefixlistConfig);

	}

	public void saveNeighbourCommunityConfigCisco(Object neighbourCommunity, PolicyType policyType,
			PolicyCriteria policyCriteria4) {
		NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
		neighbourCommunityConfig.setPolicyCriteria(policyCriteria4);
		neighbourCommunityConfig.setCommunity(neighbourCommunity.toString());
		// neighbourCommunityConfig.setName();
		neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		neighbourCommunityConfig.setModifiedBy("Optimus_Port_ILL_migration");
		neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
		neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
	}

	public void saveWanStaticRouteBgp(JSONObject wanRoutingProtocol, Bgp bgp) {
		if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").get("staticRouteList") instanceof JSONObject) {
			WanStaticRoute wanStaticRoute = new WanStaticRoute();
			wanStaticRoute.setAdvalue(wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
					.getJSONObject("staticRouteList").get("ADValue").toString());

			wanStaticRoute.setBgp(bgp);
			wanStaticRoute.setDescription(wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
					.getJSONObject("staticRouteList").get("Description").toString());
			wanStaticRoute.setEndDate(null);

			if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList")
					.has("IsGlobal")) {
				if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList")
						.get("IsGlobal").toString().equalsIgnoreCase("true")) {
					wanStaticRoute.setGlobal((byte) 1);
				} else {
					wanStaticRoute.setGlobal((byte) 0);
				}
			}
			wanStaticRoute.setIpsubnet(wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
					.getJSONObject("staticRouteList").getJSONObject("IPSubnet").get("address").toString());
			wanStaticRoute.setIsCewan((byte) 0);// or null?
			wanStaticRoute.setIsCpelanStaticroutes((byte) 0);
			wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
			wanStaticRoute.setIsPewan((byte) 1); // ispewan=yes
			wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
			wanStaticRoute.setModifiedBy("Optimus_Port_ILL_migration");
			if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").has("staticRouteList") && wanRoutingProtocol
					.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList").has("NextHopIP"))
				wanStaticRoute.setNextHopid(wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
						.getJSONObject("staticRouteList").get("NextHopIP").toString());

			if (wanRoutingProtocol.has("POPCommunity"))
				wanStaticRoute.setPopCommunity(wanRoutingProtocol.get("POPCommunity").toString()); // heirarchy
																									// as
																									// of
																									// original
																									// wanroutingprtocol
			if (wanRoutingProtocol.has("RegionalCommunity"))
				wanStaticRoute.setRegionalCommunity(wanRoutingProtocol.get("RegionalCommunity").toString());

			if (wanRoutingProtocol.has("ServiceCommunity"))
				wanStaticRoute.setServiceCommunity(wanRoutingProtocol.get("ServiceCommunity").toString());

			wanStaticRoute.setStartDate(new Timestamp(new Date().getTime()));
			wanStaticRoute.setStaticProtocol(null);// ?
			wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
		}
	}

	public Bgp saveBgp(JSONObject ias) {
		Bgp bgp = new Bgp();
		if (ias.getJSONObject("WanRoutingProtocol").has("ALUBackupPath"))
			bgp.setAluBackupPath(ias.getJSONObject("WanRoutingProtocol").get("ALUBackupPath").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("ALUDisableBGPPeerGrpExtCommunity")) {
			Byte aluDisableBGPPeerGrpExtCommunity;
			if (ias.getJSONObject("WanRoutingProtocol").get("ALUDisableBGPPeerGrpExtCommunity").toString().toUpperCase()
					.equals("TRUE")) {
				aluDisableBGPPeerGrpExtCommunity = 1;
			} else {
				aluDisableBGPPeerGrpExtCommunity = 0;
			}
			bgp.setAluDisableBgpPeerGrpExtCommunity(aluDisableBGPPeerGrpExtCommunity);
		}
		if (ias.getJSONObject("WanRoutingProtocol").has("ALUKeepaLive"))
			bgp.setAluKeepalive(ias.getJSONObject("WanRoutingProtocol").get("ALUKeepaLive").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("isASOverriden")) {
			if (ias.getJSONObject("WanRoutingProtocol").get("isASOverriden").equals("true")) {
				bgp.setAsoOverride((byte) 1);// isASOverriden
			} else {
				bgp.setAsoOverride((byte) 0);// isASOverriden
			}
		}

		bgp.setAsPath(null);
		bgp.setIsebgpMultihopReqd(null);

		if (ias.getJSONObject("PERouter").has("make") && (ias.getJSONObject("PERouter").get("make").toString().equals("CISCO IP") || ias.getJSONObject("PERouter").get("make").toString().equals("CISCO"))) {
			Byte isRoutemapEnabledInboundRoutemapV4 = 0;
			Byte isbgpNeighbourinboundv4RoutemapPreprovisioned = 0;
			if (ias.getJSONObject("WanRoutingProtocol").has("isRoutemapEnabledInboundRoutemapV4")
					&& ias.getJSONObject("WanRoutingProtocol").get("isRoutemapEnabledInboundRoutemapV4").toString()
							.equals("true")) {
				isRoutemapEnabledInboundRoutemapV4 = 1;
				bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(isRoutemapEnabledInboundRoutemapV4);
				bgp.setBgpneighbourinboundv4routermapname(
						ias.getJSONObject("WanRoutingProtocol").get("BGPNeighbourInboundRoutemapV4").toString());
				if (ias.getJSONObject("WanRoutingProtocol").has("isRouteMapPreprovisionedV4")
						&& ias.getJSONObject("WanRoutingProtocol").get("isRouteMapPreprovisionedV4").toString()
								.equals("true"))
					isbgpNeighbourinboundv4RoutemapPreprovisioned = 1;
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(isbgpNeighbourinboundv4RoutemapPreprovisioned);
			} else {
				bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(isRoutemapEnabledInboundRoutemapV4);
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(isbgpNeighbourinboundv4RoutemapPreprovisioned);
			}
			Byte isRoutemapEnabledInboundRoutemapV6 = 0;
			Byte isbgpNeighbourinboundv6RoutemapPreprovisioned = 0;
			if (ias.getJSONObject("WanRoutingProtocol").has("isRoutemapEnabledInboundRoutemapV6")
					&& ias.getJSONObject("WanRoutingProtocol").get("isRoutemapEnabledInboundRoutemapV6").toString()
							.equals("true")) {
				isRoutemapEnabledInboundRoutemapV6 = 1;
				bgp.setIsbgpNeighbourInboundv6RoutemapEnabled(isRoutemapEnabledInboundRoutemapV6);
				bgp.setBgpneighbourinboundv6routermapname(
						ias.getJSONObject("WanRoutingProtocol").get("BGPNeighbourInboundRoutemapV6").toString());
				if (ias.getJSONObject("WanRoutingProtocol").has("isRouteMapPreprovisionedV6")
						&& ias.getJSONObject("WanRoutingProtocol").get("isRouteMapPreprovisionedV4").toString()
								.equals("true"))
					isbgpNeighbourinboundv6RoutemapPreprovisioned = 1;
				bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned(isbgpNeighbourinboundv6RoutemapPreprovisioned);
			} else {
				bgp.setIsbgpNeighbourInboundv6RoutemapEnabled(isRoutemapEnabledInboundRoutemapV6);
			}
		}

		if (ias.getJSONObject("WanRoutingProtocol").has("BGPPeerGroupName"))
			bgp.setBgpPeerName(ias.getJSONObject("WanRoutingProtocol").get("BGPPeerGroupName").toString());
		bgp.setEndDate(null);
		if (ias.getJSONObject("WanRoutingProtocol").has("helloTime"))
			bgp.setHelloTime(ias.getJSONObject("WanRoutingProtocol").get("helloTime").toString());
		if (ias.getJSONObject("WanRoutingProtocol").has("holdTime"))
			bgp.setHoldTime(ias.getJSONObject("WanRoutingProtocol").get("holdTime").toString());
		bgp.setInterfaceProtocolMappings(null);// ?
		if (ias.getJSONObject("WanRoutingProtocol").has("isAuthenticationRequired")) {
			Byte isAuthenticationRequired;
			if (ias.getJSONObject("WanRoutingProtocol").get("isAuthenticationRequired").toString().toLowerCase()
					.equals("true")) {
				isAuthenticationRequired = 1;
				if (ias.getJSONObject("WanRoutingProtocol").has("authenticationMode"))
					bgp.setAuthenticationMode(
							ias.getJSONObject("WanRoutingProtocol").get("authenticationMode").toString());
				if (ias.getJSONObject("WanRoutingProtocol").has("authenticationPassword"))
					bgp.setPassword(ias.getJSONObject("WanRoutingProtocol").get("authenticationPassword").toString());
			} else {
				isAuthenticationRequired = 0;
			}
			bgp.setIsauthenticationRequired(isAuthenticationRequired);
		}
		if (ias.getJSONObject("WanRoutingProtocol").has("isEBGPMultihopRequired")) {
			Byte isebgpMultihopReqd;
			if (ias.getJSONObject("WanRoutingProtocol").get("isEBGPMultihopRequired").toString().toLowerCase()
					.equals("true")) {
				isebgpMultihopReqd = 1;
			} else {
				isebgpMultihopReqd = 0;
			}
			bgp.setIsebgpMultihopReqd(isebgpMultihopReqd);
		}

		if (ias.getJSONObject("WanRoutingProtocol").has("MultihopTTL"))
			bgp.setIsmultihopTtl(ias.getJSONObject("WanRoutingProtocol").get("MultihopTTL").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("isRTBH")) {
			Byte isRTBH = 0;
			if (ias.getJSONObject("WanRoutingProtocol").get("isRTBH").toString().equals("true")) {
				isRTBH = 1;
				bgp.setRtbhOptions(ias.getJSONObject("WanRoutingProtocol").get("RTBHOptions").toString());
			}
			bgp.setIsrtbh(isRTBH);
		}

		bgp.setLastModifiedDate(new Timestamp(new Date().getTime()));
		if (ias.getJSONObject("WanRoutingProtocol").has("bgpNeighbourLocalASNumber")
				&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("bgpNeighbourLocalASNumber").has("number"))
			bgp.setLocalAsNumber(ias.getJSONObject("WanRoutingProtocol").getJSONObject("bgpNeighbourLocalASNumber")
					.get("number").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("LocalPreference"))
			bgp.setLocalPreference(ias.getJSONObject("WanRoutingProtocol").get("LocalPreference").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("localBGPNeighbourUpdateSource")) {

			if (ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
					.has("interfaceName"))
				bgp.setLocalUpdateSourceInterface(ias.getJSONObject("WanRoutingProtocol")
						.getJSONObject("localBGPNeighbourUpdateSource").get("interfaceName").toString());

			if (ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
					.has("v4IpAddress")
					&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
							.get("v4IpAddress") instanceof JSONObject
					&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
							.getJSONObject("v4IpAddress").has("address"))
				bgp.setLocalUpdateSourceIpv4Address(
						ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
								.getJSONObject("v4IpAddress").get("address").toString());

			if (ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
					.has("v6IpAddress")
					&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
							.getJSONObject("v6IpAddress").has("address"))
				bgp.setLocalUpdateSourceIpv6Address(
						ias.getJSONObject("WanRoutingProtocol").getJSONObject("localBGPNeighbourUpdateSource")
								.getJSONObject("v6IpAddress").get("address").toString());
		}

		if (ias.getJSONObject("WanRoutingProtocol").has("maxPrefix"))
			bgp.setMaxPrefix(ias.getJSONObject("WanRoutingProtocol").get("maxPrefix").toString());
		if (ias.getJSONObject("WanRoutingProtocol").has("maxPrefixThreshold"))
			bgp.setMaxPrefixThreshold(ias.getJSONObject("WanRoutingProtocol").get("maxPrefixThreshold").toString());
		bgp.setMedValue(null);
		bgp.setModifiedBy("Optimus_Port_ILL_migration");
		if (ias.getJSONObject("WanRoutingProtocol").has("BGPNeighborOn"))
			bgp.setNeighborOn(ias.getJSONObject("WanRoutingProtocol").get("BGPNeighborOn").toString());
		/*
		 * if (ias.getJSONObject("WanRoutingProtocol").has("neighbourCommunity"))
		 * bgp.setNeighbourCommunity(ias.getJSONObject("WanRoutingProtocol").get(
		 * "neighbourCommunity").toString());
		 */
		Byte isNeighbourShutdownRequired = 0;
		if (ias.getJSONObject("WanRoutingProtocol").has("isNeighbourShutdownRequired") && ias
				.getJSONObject("WanRoutingProtocol").get("isNeighbourShutdownRequired").toString().equals("true")) {
			isNeighbourShutdownRequired = 1;
		}
		bgp.setNeighbourshutdownRequired(isNeighbourShutdownRequired);

		if (ias.getJSONObject("WanRoutingProtocol").has("PEERtype"))
			bgp.setPeerType(ias.getJSONObject("WanRoutingProtocol").get("PEERtype").toString());

		bgp.setPolicyTypes(null);// ?

		if (ias.getJSONObject("WanRoutingProtocol").has("isRedistributeConnectedEnabled")) {
			Byte isRedistributeConnectedEnabled = 0;
			if (ias.getJSONObject("WanRoutingProtocol").get("isRedistributeConnectedEnabled").toString()
					.equals("true")) {
				isRedistributeConnectedEnabled = 1;
			}
			bgp.setRedistributeConnectedEnabled(isRedistributeConnectedEnabled);
		}

		if (ias.getJSONObject("WanRoutingProtocol").has("isRedistributeStaticEnabled")) {
			Byte isRedistributeStaticEnabled = 0;
			if (ias.getJSONObject("WanRoutingProtocol").get("isRedistributeStaticEnabled").toString().equals("true")) {
				isRedistributeStaticEnabled = 1;
			}
			bgp.setRedistributeStaticEnabled(isRedistributeStaticEnabled);
		}

		if (ias.getJSONObject("WanRoutingProtocol").has("remoteASNumber")
				&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteASNumber").has("number")) {
			String remoteAsNumString = ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteASNumber")
					.get("number").toString();
			String remoteAsNum = remoteAsNumString.replaceAll("[^0-9]", "");
			bgp.setRemoteAsNumber(Integer.valueOf(remoteAsNum));
		}

		Byte isbgpMultihopReqd = (byte) 0;
		if (ias.getJSONObject("WanRoutingProtocol").has("remoteASNumber")
				&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteASNumber")
						.has("isASNumberCustomerOwned")
				&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteASNumber")
						.get("isASNumberCustomerOwned").toString().toLowerCase().equals("true")) {
			String isCustomerOwned = ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteASNumber")
					.get("isASNumberCustomerOwned").toString();
			isbgpMultihopReqd = (byte) 1;
		}
		bgp.setIsbgpMultihopReqd(isbgpMultihopReqd);
		
		bgp.setRemoteCeAsnumber(null);

		if (ias.getJSONObject("WanRoutingProtocol").has("remoteIPv4Address")
				&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteIPv4Address").has("address"))
			bgp.setRemoteIpv4Address(ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteIPv4Address")
					.get("address").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("remoteIPv6Address")
				&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteIPv6Address").has("address"))
			bgp.setRemoteIpv6Address(ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteIPv6Address")
					.get("address").toString());

		if (ias.getJSONObject("WanRoutingProtocol").has("remoteBGPNeighbourUpdateSource") && ias
				.getJSONObject("WanRoutingProtocol").get("remoteBGPNeighbourUpdateSource") instanceof JSONObject) {

			if (ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
					.has("interfaceName"))
				bgp.setRemoteUpdateSourceInterface(ias.getJSONObject("WanRoutingProtocol")
						.getJSONObject("remoteBGPNeighbourUpdateSource").get("interfaceName").toString());

			if (ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
					.has("v4IpAddress")
					&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
							.get("v4IpAddress") instanceof JSONObject
					&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
							.getJSONObject("v4IpAddress").has("address"))
				bgp.setRemoteUpdateSourceIpv4Address(
						ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
								.getJSONObject("v4IpAddress").get("address").toString());

			if (ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
					.has("v6IpAddress")
					&& ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
							.getJSONObject("v6IpAddress").has("address"))
				bgp.setRemoteUpdateSourceIpv6Address(
						ias.getJSONObject("WanRoutingProtocol").getJSONObject("remoteBGPNeighbourUpdateSource")
								.getJSONObject("v6IpAddress").get("address").toString());
		}
		if (ias.getJSONObject("WanRoutingProtocol").has("routesExchanged"))
			bgp.setRoutesExchanged(
					ias.getJSONObject("WanRoutingProtocol").getJSONObject("routesExchanged").get("routes").toString());
		if (ias.getJSONObject("WanRoutingProtocol").has("isSOORequired")) {
			Byte isSOORequired = 0;
			if (ias.getJSONObject("WanRoutingProtocol").get("isSOORequired").toString().equals("true")) {
				isSOORequired = 1;
			}
			bgp.setSooRequired(isSOORequired);
		}
		bgp.setSplitHorizon(null);
		bgp.setStartDate(new Timestamp(new Date().getTime()));
		if (ias.getJSONObject("WanRoutingProtocol").has("v6LocalPreference"))
			bgp.setV6LocalPreference(ias.getJSONObject("WanRoutingProtocol").get("v6LocalPreference").toString());
		// bgp.setWanStaticRoutes(wanStaticRoutes);//?
		bgpRepository.saveAndFlush(bgp);
		return bgp;
	}

	public void saveStaticWanCe(JSONObject wanRoutingProtocol, StaticProtocol staticProtocol) {
		WanStaticRoute wanStaticRoute2 = new WanStaticRoute();
		wanStaticRoute2.setAdvalue(null);
		wanStaticRoute2.setBgp(null);
		wanStaticRoute2.setDescription(null);
		wanStaticRoute2.setEndDate(null);
		if (wanRoutingProtocol.getJSONObject("ceWANStaticRoutes").getJSONObject("staticRouteList").has("isGlobal")) {
			if (wanRoutingProtocol.getJSONObject("ceWANStaticRoutes").getJSONObject("staticRouteList").get("isGlobal")
					.toString().equalsIgnoreCase("true")) {
				wanStaticRoute2.setGlobal((byte) 1);
			} else {
				wanStaticRoute2.setGlobal((byte) 0);
			}
		}

		wanStaticRoute2.setIpsubnet(wanRoutingProtocol.getJSONObject("ceWANStaticRoutes")
				.getJSONObject("staticRouteList").getJSONObject("ipSubnet").get("address").toString());
		wanStaticRoute2.setIsCewan((byte) 1);// or null?
		wanStaticRoute2.setIsCpelanStaticroutes((byte) 0);
		wanStaticRoute2.setIsCpewanStaticroutes((byte) 0);
		wanStaticRoute2.setIsPewan((byte) 0); // ispewan=yes
		wanStaticRoute2.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute2.setModifiedBy("Optimus_Port_ILL_migration");
		if (wanRoutingProtocol.getJSONObject("ceWANStaticRoutes").has("staticRouteList") && wanRoutingProtocol
				.getJSONObject("ceWANStaticRoutes").getJSONObject("staticRouteList").has("nextHopIp"))
			wanStaticRoute2.setNextHopid(wanRoutingProtocol.getJSONObject("ceWANStaticRoutes")
					.getJSONObject("staticRouteList").get("nextHopIp").toString());

		if (wanRoutingProtocol.has("POPCommunity"))
			wanStaticRoute2.setPopCommunity(wanRoutingProtocol.get("POPCommunity").toString()); // heirarchy
																								// as
																								// of
																								// original
																								// wanroutingprtocol
		if (wanRoutingProtocol.has("RegionalCommunity"))
			wanStaticRoute2.setRegionalCommunity(wanRoutingProtocol.get("RegionalCommunity").toString());

		if (wanRoutingProtocol.has("ServiceCommunity"))
			wanStaticRoute2.setServiceCommunity(wanRoutingProtocol.get("ServiceCommunity").toString());

		wanStaticRoute2.setStartDate(new Timestamp(new Date().getTime()));
		wanStaticRoute2.setStaticProtocol(staticProtocol);// ?
		wanStaticRouteRepository.saveAndFlush(wanStaticRoute2);

	}

	public void saveStaticWanPe(JSONObject wanRoutingProtocol, JSONObject staticList, StaticProtocol staticProtocol) {

		WanStaticRoute wanStaticRoute = new WanStaticRoute();
		wanStaticRoute.setAdvalue(null);
		wanStaticRoute.setBgp(null);
		wanStaticRoute.setDescription(null);
		wanStaticRoute.setEndDate(null);
		
		if (staticList.has("isGlobal")) {
			if (staticList.get("isGlobal")
					.toString().equalsIgnoreCase("true")) {
				wanStaticRoute.setGlobal((byte) 1);
			} else {
				wanStaticRoute.setGlobal((byte) 0);
			}
		}
		if (staticList.has("ipSubnet") && staticList.getJSONObject("ipSubnet").has("address"))
			wanStaticRoute.setIpsubnet(staticList.getJSONObject("ipSubnet").get("address").toString());
		wanStaticRoute.setIsCewan((byte) 0);// or null?
		wanStaticRoute.setIsCpelanStaticroutes((byte) 0);
		wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
		wanStaticRoute.setIsPewan((byte) 1); // ispewan=yes
		wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute.setModifiedBy("Optimus_Port_ILL_migration");
		if (staticList.has("nextHopIp"))
			wanStaticRoute.setNextHopid(staticList.get("nextHopIp").toString());

		if (wanRoutingProtocol.has("POPCommunity"))
			wanStaticRoute.setPopCommunity(wanRoutingProtocol.get("POPCommunity").toString()); // heirarchy
																								// as
																								// of
																								// original
																								// wanroutingprtocol
		if (wanRoutingProtocol.has("RegionalCommunity"))
			wanStaticRoute.setRegionalCommunity(wanRoutingProtocol.get("RegionalCommunity").toString());

		if (wanRoutingProtocol.has("ServiceCommunity"))
			wanStaticRoute.setServiceCommunity(wanRoutingProtocol.get("ServiceCommunity").toString());

		wanStaticRoute.setStartDate(new Timestamp(new Date().getTime()));
		wanStaticRoute.setStaticProtocol(staticProtocol);// ?
		wanStaticRouteRepository.saveAndFlush(wanStaticRoute);

	}

	public StaticProtocol saveStaticProtocol(JSONObject wanRoutingProtocol) {
		StaticProtocol staticProtocol = new StaticProtocol();
		staticProtocol.setEndDate(null);
		staticProtocol.setInterfaceProtocolMappings(null);// ?
		if (wanRoutingProtocol.has("isRoutemapEnabled")) {
			Byte isRoutemapEnabled;
			if (wanRoutingProtocol.get("isRoutemapEnabled").toString().equals("false")) {
				isRoutemapEnabled = 0;
			} else {
				isRoutemapEnabled = 1;
			}
			staticProtocol.setIsroutemapEnabled(isRoutemapEnabled);
		}
		if (wanRoutingProtocol.has("isRouteMapPreprovisioned")) {
			Byte isRouteMapPreprovisioned;
			if (wanRoutingProtocol.get("isRouteMapPreprovisioned").toString().equals("false")) {
				isRouteMapPreprovisioned = 0;
			} else {
				isRouteMapPreprovisioned = 1;
			}
			staticProtocol.setIsroutemapPreprovisioned(isRouteMapPreprovisioned);
		}
		staticProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (wanRoutingProtocol.has("LocalPreference"))
			staticProtocol.setLocalPreference(wanRoutingProtocol.get("LocalPreference").toString());

		if (wanRoutingProtocol.has("v6LocalPreference"))
			staticProtocol.setLocalPreferenceV6(wanRoutingProtocol.get("v6LocalPreference").toString());

		staticProtocol.setModifiedBy("Optimus_Port_ILL_migration");
		staticProtocol.setPolicyTypes(null);// ?

		if (wanRoutingProtocol.has("RouteMapNameV4"))
			staticProtocol.setRedistributeRoutemapIpv4(wanRoutingProtocol.get("RouteMapNameV4").toString());

		staticProtocol.setStartDate(new Timestamp(new Date().getTime()));
		staticProtocolRepository.saveAndFlush(staticProtocol);
		return staticProtocol;
	}

	public void saveVpnSolution(JSONObject ias, ServiceDetail serviceDetail1) {
		if (ias.has("SolutionTable") && ias.getJSONObject("SolutionTable").has("VPN")) {
			VpnSolution vpnSolution = new VpnSolution();
			vpnSolution.setEndDate(null);
			vpnSolution.setInstanceId(null);
			if (ias.getJSONObject("SolutionTable").getJSONObject("VPN").has("vpnLeg")
					&& ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg").has("interface"))
				vpnSolution.setInterfaceName(ias.getJSONObject("SolutionTable").getJSONObject("VPN")
						.getJSONObject("vpnLeg").get("interface").toString());// ?
			// vpnSolution.setIsE2eIntegrated(null);
			// vpnSolution.setIsenableUccService(null);
			// vpnSolution.setIsUa(null);
			vpnSolution.setLastModifiedDate(new Timestamp(new Date().getTime()));
			if (ias.getJSONObject("SolutionTable").getJSONObject("VPN").has("vpnLeg")
					&& ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg").has("role"))
				vpnSolution.setLegRole(ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg")
						.get("role").toString());
			// vpnSolution.setManagementVpnType1(null);// ?
			vpnSolution.setModifiedBy("Optimus_Port_ILL_migration");
			vpnSolution.setServiceDetail(serviceDetail1);
			if (ias.getJSONObject("SolutionTable").getJSONObject("VPN").has("vpnLeg")
					&& ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg").has("customerSiteId"))
			vpnSolution.setSiteId(ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg")
					.get("customerSiteId").toString());
			vpnSolution.setStartDate(new Timestamp(new Date().getTime()));
			// vpnSolution.setVpnAlias(null);
			vpnSolution.setVpnLegId(null);// ?
			if (ias.has("vrf") && ias.getJSONObject("vrf").has("name")
					&& !ias.getJSONObject("vrf").get("name").toString().equals("NOT_APPLICABLE"))
				vpnSolution.setVpnName(ias.getJSONObject("vrf").get("name").toString());// vpnID??

			if (ias.has("vrf") && ias.getJSONObject("vrf").has("name")
					&& !ias.getJSONObject("vrf").get("name").toString().equals("NOT_APPLICABLE")
					&& ias.getJSONObject("SolutionTable").getJSONObject("VPN").has("vpnLeg")
					&& ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg")
							.has("customerSiteId"))
				vpnSolution.setVpnSolutionName(
						ias.getJSONObject("vrf").get("name").toString() + ias.getJSONObject("SolutionTable")
								.getJSONObject("VPN").getJSONObject("vpnLeg").get("customerSiteId").toString());

			vpnSolution
					.setVpnTopology(ias.getJSONObject("SolutionTable").getJSONObject("VPN").get("topology").toString());
			vpnSolution.setVpnType(ias.getJSONObject("SolutionTable").getJSONObject("VPN").get("vpnType").toString());
			vpnSolutionRepository.saveAndFlush(vpnSolution);
			//return vpnSolution;
		}
	}

	public Vrf saveVrf(JSONObject ias, ServiceDetail serviceDetail1) {
		Vrf vrf = new Vrf();
		vrf.setEndDate(null);
		vrf.setLastModifiedDate(new Timestamp(new Date().getTime()));
		vrf.setServiceDetail(serviceDetail1);
		vrf.setStartDate(new Timestamp(new Date().getTime()));
		vrf.setVrfName(ias.getJSONObject("vrf").get("name").toString());// internet,primmus,not applicable for ILL
		Byte defValue = 0;
		vrf.setIsmultivrf(defValue);
		vrf.setIsvrfLiteEnabled(defValue);
		vrf.setMastervrfServiceid(null);
		if (ias.getJSONObject("vrf").has("MaxRoutesValue"))
			vrf.setMaxRoutesValue(ias.getJSONObject("vrf").get("MaxRoutesValue").toString());
		vrf.setModifiedBy("Optimus_Port_ILL_migration");
		vrf.setPolicyTypes(null);// ?
		if (ias.getJSONObject("vrf").has("threshold"))
			vrf.setThreshold(ias.getJSONObject("vrf").get("threshold").toString());
		if (ias.getJSONObject("vrf").has("warnOn"))
			vrf.setWarnOn(ias.getJSONObject("vrf").get("warnOn").toString());
		vrfRepository.saveAndFlush(vrf);
		return vrf;
	}

	public Cpe saveCpe(JSONObject ias, JSONObject cpewanInterface, String serviceId) {
		Cpe cpe = new Cpe();
		// cpe.setInterfaceProtocolMappings(null);// ?
		if (ias.getJSONObject("cpe").has("SNMPServerCommunity")) {
			cpe.setSnmpServerCommunity(ias.getJSONObject("cpe").get("SNMPServerCommunity").toString());
		} else {
			cpe.setSnmpServerCommunity("t2c2l2com");
		}
		cpe.setCpeinitConfigparams(null);
		cpe.setCpeShared(null);
		cpe.setCpeSharedComponent(null);
		cpe.setDeviceId(null);
		cpe.setDpsDmvpnIp(null);
		cpe.setDpsLoopbackIp(null);
		cpe.setEndDate(null);
		cpe.setHostName(null);
		cpe.setInitEnablepassword(null);
		cpe.setInitLoginpwd(null);
		cpe.setInitUsername(null);
		cpe.setIsaceconfigurable(null);
		cpe.setIscpeReachable(null);
		cpe.setLastModifiedDate(new Timestamp(new Date().getTime()));
		cpe.setLoopbackInterfaceName(null);
		cpe.setMake(null);
		cpe.setMgmtLoopbackV4address(null);
		cpe.setModel(null);
		cpe.setModifiedBy("Optimus_Port_ILL_migration");
		cpe.setNniCpeConfig(null);
		cpe.setRoleType(null);
		cpe.setSendInittemplate(null);
		cpe.setServiceId(serviceId);
		cpe.setSiteBgpAs(null);
		cpe.setSiteName(null);
		cpe.setSiteType(null);
		cpe.setStartDate(new Timestamp(new Date().getTime()));
		cpe.setUnmanagedCePartnerdeviceWanip(null);
		cpe.setVsatCpeConfig(null);
		cpe.setWanCircuitBwMbps(null);
		cpe.setWanebgpPeerIp(null);
		if (cpewanInterface.has("name"))
			cpe.setWanInterfaceName(cpewanInterface.get("name").toString());
		cpeRepository.saveAndFlush(cpe);
		return cpe;
	}

	public EthernetInterface saveEthernetCpe(JSONObject cpewanInterface) {

		EthernetInterface ethernetInterface2 = new EthernetInterface();

		// ethernetInterface.setAclPolicyCriterias(aclPolicyCriterias);// ?
		if (cpewanInterface.has("isAutoNegotiation"))
			ethernetInterface2.setAutonegotiationEnabled(cpewanInterface.get("isAutoNegotiation").toString());

		if (cpewanInterface.has("isBFDEnabled")) {
			if (cpewanInterface.get("isBFDEnabled").toString().equalsIgnoreCase("true")) {
				ethernetInterface2.setIsbfdEnabled((byte) 1);
				if (cpewanInterface.has("BFDMultiplier"))
					ethernetInterface2.setBfdMultiplier(cpewanInterface.get("BFDMultiplier").toString());
				if (cpewanInterface.has("BFDMultiplier"))
					if (cpewanInterface.has("BFDReceiveinterval"))
						ethernetInterface2.setBfdreceiveInterval(cpewanInterface.get("BFDReceiveinterval").toString());
				if (cpewanInterface.has("BFDTransmitinterval"))
					ethernetInterface2.setBfdtransmitInterval(cpewanInterface.get("BFDTransmitinterval").toString());
			} else {
				ethernetInterface2.setIsbfdEnabled((byte) 0);
			}
		}

		if (cpewanInterface.has("duplex"))
			ethernetInterface2.setDuplex(cpewanInterface.get("duplex").toString());
		if (cpewanInterface.has("Encapsulation"))
			ethernetInterface2.setEncapsulation(cpewanInterface.get("Encapsulation").toString());

		ethernetInterface2.setEndDate(null);

		if (cpewanInterface.has("Framing"))
			ethernetInterface2.setFraming(cpewanInterface.get("Framing").toString());

		if (cpewanInterface.has("HoldTimeDown"))
			ethernetInterface2.setHoldtimeDown(cpewanInterface.get("HoldTimeDown").toString());

		if (cpewanInterface.has("HoldTimeUp"))
			ethernetInterface2.setHoldtimeUp(cpewanInterface.get("HoldTimeUp").toString());

		ethernetInterface2.setHsrpVrrpProtocols(null);// ?
		ethernetInterface2.setInterfaceProtocolMappings(null);// ?

		if (cpewanInterface.has("vlan"))
			ethernetInterface2.setInnerVlan(cpewanInterface.get("vlan").toString());// ?vlan

		if (cpewanInterface.has("name"))
			ethernetInterface2.setInterfaceName(cpewanInterface.get("name").toString());
		
		if (cpewanInterface.has("v4IpAddress") && cpewanInterface.get("v4IpAddress") instanceof JSONObject)
			ethernetInterface2.setIpv4Address(cpewanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (cpewanInterface.has("v6IpAddress"))
			ethernetInterface2.setIpv6Address(cpewanInterface.getJSONObject("v6IpAddress").get("address").toString());

		Byte defvalue = (byte) 0;
		ethernetInterface2.setIshsrpEnabled(defvalue);
		ethernetInterface2.setIsvrrpEnabled(defvalue);

		ethernetInterface2.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (cpewanInterface.has("mediaType"))
			ethernetInterface2.setMediaType(cpewanInterface.get("mediaType").toString());

		if (cpewanInterface.has("mode"))
			ethernetInterface2.setMode(cpewanInterface.get("mode").toString());

		ethernetInterface2.setModifiedBy("Optimus_Port_ILL_migration");

		if (cpewanInterface.has("MTU"))
			ethernetInterface2.setMtu(cpewanInterface.get("MTU").toString());

		if (cpewanInterface.has("v4IpAddress") && cpewanInterface.get("v4IpAddress") instanceof JSONObject)
			ethernetInterface2
					.setModifiedIpv4Address(cpewanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (cpewanInterface.has("v6IpAddress"))
			ethernetInterface2
					.setModifiedIpv6Address(cpewanInterface.getJSONObject("v6IpAddress").get("address").toString());

		ethernetInterface2.setModifiedSecondaryIpv4Address(null);
		ethernetInterface2.setModifiedSecondaryIpv6Address(null);

		if (cpewanInterface.has("OuterVLAN"))
			ethernetInterface2.setOuterVlan(cpewanInterface.get("OuterVLAN").toString());

		if (cpewanInterface.has("physicalPortName"))
			ethernetInterface2.setPhysicalPort(cpewanInterface.get("physicalPortName").toString());

		if (cpewanInterface.has("PortType"))
			ethernetInterface2.setPortType(cpewanInterface.get("PortType").toString());

		if (cpewanInterface.has("SecondaryV4IpAddress")
				&& cpewanInterface.getJSONObject("SecondaryV4IpAddress").has("address"))
			ethernetInterface2.setSecondaryIpv4Address(
					cpewanInterface.getJSONObject("SecondaryV4IpAddress").get("address").toString());

		if (cpewanInterface.has("SecondaryV6IpAddress")
				&& cpewanInterface.getJSONObject("SecondaryV6IpAddress").has("address"))
			ethernetInterface2.setSecondaryIpv6Address(
					cpewanInterface.getJSONObject("SecondaryV6IpAddress").get("address").toString());

		if (cpewanInterface.has("qosLoopinPassthroughInterface"))
			ethernetInterface2.setQosLoopinPassthrough(cpewanInterface.get("qosLoopinPassthroughInterface").toString());

		if (cpewanInterface.has("speed"))
			ethernetInterface2.setSpeed(cpewanInterface.get("speed").toString());
		ethernetInterface2.setStartDate(new Timestamp(new Date().getTime()));
		ethernetInterfaceRepository.saveAndFlush(ethernetInterface2);
		return ethernetInterface2;
	}

	public ChannelizedSdhInterface saveChannelizedSdhInterface(JSONObject wanInterface) {

		ChannelizedSdhInterface channelizedSdhInterface = new ChannelizedSdhInterface();
		if (wanInterface.has("firsttimeslot"))
			channelizedSdhInterface.set_4Kfirsttime_slot(wanInterface.get("firsttimeslot").toString());

		if (wanInterface.has("lasttimeslot"))
			channelizedSdhInterface.set_4klasttimeSlot(wanInterface.get("lasttimeslot").toString());

		channelizedSdhInterface.setAclPolicyCriterias(null);// ?

		if (wanInterface.has("isBFDEnabled")) {

			if (wanInterface.get("isBFDEnabled").toString().toLowerCase().equals("true")) {
				channelizedSdhInterface.setIsbfdEnabled((byte) 1);
				if (wanInterface.has("BFDMultiplier"))
					channelizedSdhInterface.setBfdMultiplier(wanInterface.get("BFDMultiplier").toString());
				if (wanInterface.has("BFDReceiveIinterval"))
					channelizedSdhInterface.setBfdreceiveInterval(wanInterface.get("BFDReceiveIinterval").toString());
				if (wanInterface.has("BFDTransmitInterval"))
					channelizedSdhInterface.setBfdtransmitInterval(wanInterface.get("BFDTransmitInterval").toString());

			} else if (wanInterface.get("isBFDEnabled").toString().toLowerCase().equals("false")) {

				channelizedSdhInterface.setIsbfdEnabled((byte) 0);
			}
		}

		if (wanInterface.has("channelGroupNumber"))
			channelizedSdhInterface.setChannelGroupNumber(wanInterface.get("channelGroupNumber").toString());

		if (wanInterface.has("dlciValue"))
			channelizedSdhInterface.setDlciValue(wanInterface.get("dlciValue").toString());

		if (wanInterface.has("DownCount"))
			channelizedSdhInterface.setDowncount(wanInterface.get("DownCount").toString());

		if (wanInterface.has("encapsulation"))
			channelizedSdhInterface.setEncapsulation(wanInterface.get("encapsulation").toString());

		channelizedSdhInterface.setEndDate(null);

		channelizedSdhInterface.setInterfaceName(wanInterface.get("name").toString());// ?

		if (wanInterface.has("isFramed")) {
			if (wanInterface.get("isFramed").toString().toLowerCase().equals("true")) {

				channelizedSdhInterface.setIsframed((byte) 1);
				channelizedSdhInterface.setFraming(wanInterface.get("Framing").toString());

			} else {
				channelizedSdhInterface.setIsframed((byte) 0);
			}
		}

		if (wanInterface.has("HoldTimeDown"))
			channelizedSdhInterface.setHoldtimeDown(wanInterface.get("HoldTimeDown").toString());

		if (wanInterface.has("HoldTimeUp"))
			channelizedSdhInterface.setHoldtimeUp(wanInterface.get("HoldTimeUp").toString());

		channelizedSdhInterface.setInterfaceProtocolMappings(null);// ?

		if (wanInterface.has("J"))
			channelizedSdhInterface.setJ((Integer) wanInterface.get("J"));

		if (wanInterface.has("v4IpAddress") && wanInterface.get("v4IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v4IpAddress").has("address"))
			channelizedSdhInterface.setIpv4Address(wanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (wanInterface.has("v6IpAddress") && wanInterface.get("v6IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v6IpAddress").has("address"))
			channelizedSdhInterface.setIpv6Address(wanInterface.getJSONObject("v6IpAddress").get("address").toString());

		Byte defvalue = (byte) 0;
		channelizedSdhInterface.setIshdlcConfig(defvalue);
		channelizedSdhInterface.setIshsrpEnabled(defvalue);
		channelizedSdhInterface.setIsvrrpEnabled(defvalue);

		if (wanInterface.has("KeepAlive"))
			channelizedSdhInterface.setKeepalive(wanInterface.get("KeepAlive").toString());

		if (wanInterface.has("KLM"))
			channelizedSdhInterface.setKlm(wanInterface.get("KLM").toString());

		if (wanInterface.has("posFraming"))
			channelizedSdhInterface.setPosframing(wanInterface.get("posFraming").toString());

		channelizedSdhInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (wanInterface.has("Mode"))
			channelizedSdhInterface.setMode(wanInterface.get("Mode").toString());

		channelizedSdhInterface.setModifiedBy("Optimus_Port_ILL_migration");

		if (wanInterface.has("v4IpAddress") && wanInterface.get("v4IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v4IpAddress").has("address"))
			channelizedSdhInterface
					.setModifiedIpv4Address(wanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (wanInterface.has("v6IpAddress") && wanInterface.get("v6IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v6IpAddress").has("address"))
			channelizedSdhInterface
					.setModifiedIipv6Address(wanInterface.getJSONObject("v6IpAddress").get("address").toString());

		channelizedSdhInterface.setModifiedSecondaryIpv4Address(null);

		if (wanInterface.has("MTU"))
			channelizedSdhInterface.setMtu(wanInterface.get("MTU").toString());

		if (wanInterface.has("physicalPortName"))
			channelizedSdhInterface.setPhysicalPort(wanInterface.get("physicalPortName").toString());

		if (wanInterface.has("PortType"))
			channelizedSdhInterface.setPortType(wanInterface.get("PortType").toString());

		if (wanInterface.has("SecondaryV4IpAddress")
				&& wanInterface.getJSONObject("SecondaryV4IpAddress").has("address"))
			channelizedSdhInterface.setSecondaryIpv4Address(
					wanInterface.getJSONObject("SecondaryV4IpAddress").get("address").toString());

		if (wanInterface.has("SecondaryV6IpAddress")
				&& wanInterface.getJSONObject("SecondaryV6IpAddress").has("address"))
			channelizedSdhInterface.setSecondaryIpv6Address(
					wanInterface.getJSONObject("SecondaryV6IpAddress").get("address").toString());

		channelizedSdhInterface.setStartDate(new Timestamp(new Date().getTime()));

		if (wanInterface.has("UpCount"))
			channelizedSdhInterface.setUpcount(wanInterface.get("UpCount").toString());

		channelizedSdhInterfaceRepository.saveAndFlush(channelizedSdhInterface);
		return channelizedSdhInterface;
	}

	public ChannelizedE1serialInterface saveChannelizedE1SerialDetails(JSONObject wanInterface) {

		ChannelizedE1serialInterface channelizedE1serialInterface = new ChannelizedE1serialInterface();

		channelizedE1serialInterface.setAclPolicyCriterias(null);// ?
		if (wanInterface.has("isBFDEnabled")) {

			if (wanInterface.get("isBFDEnabled").toString().toLowerCase().equals("true")) {
				channelizedE1serialInterface.setIsbfdEnabled((byte) 1);
				if(wanInterface.has("BFDMultiplier"))
				channelizedE1serialInterface.setBfdMultiplier(wanInterface.get("BFDMultiplier").toString());
				if(wanInterface.has("BFDReceiveIinterval"))
				channelizedE1serialInterface.setBfdreceiveInterval(wanInterface.get("BFDReceiveIinterval").toString());
				if(wanInterface.has("BFDTransmitInterval"))
				channelizedE1serialInterface.setBfdtransmitInterval(wanInterface.get("BFDTransmitInterval").toString());

			} else if (wanInterface.get("isBFDEnabled").toString().toLowerCase().equals("false")) {

				channelizedE1serialInterface.setIsbfdEnabled((byte) 0);
			}
		}

		if (wanInterface.has("crcSize"))
			channelizedE1serialInterface.setCrcsize((Integer) wanInterface.get("crcSize"));

		if (wanInterface.has("channelGroupNumber"))
			channelizedE1serialInterface.setChannelGroupNumber(wanInterface.get("channelGroupNumber").toString());

		if (wanInterface.has("dlciValue"))
			channelizedE1serialInterface.setDlciValue(wanInterface.get("dlciValue").toString());

		if (wanInterface.has("DownCount"))
			channelizedE1serialInterface.setDowncount(wanInterface.get("DownCount").toString());

		if (wanInterface.has("encapsulation"))
			channelizedE1serialInterface.setEncapsulation(wanInterface.get("encapsulation").toString());

		channelizedE1serialInterface.setEndDate(null);

		if (wanInterface.has("firsttimeslot"))
			channelizedE1serialInterface.setFirsttimeSlot(wanInterface.get("firsttimeslot").toString());

		if (wanInterface.has("lasttimeslot"))
			channelizedE1serialInterface.setLasttimeSlot(wanInterface.get("lasttimeslot").toString());

		if (wanInterface.has("isFramed")) {
			if (wanInterface.get("isFramed").toString().toLowerCase().equals("true")) {

				channelizedE1serialInterface.setIsframed((byte) 1);
				channelizedE1serialInterface.setFraming(wanInterface.get("Framing").toString());

			} else {
				channelizedE1serialInterface.setIsframed((byte) 0);
			}
		}

		if (wanInterface.has("HoldTimeDown"))
			channelizedE1serialInterface.setHoldtimeDown(wanInterface.get("HoldTimeDown").toString());

		if (wanInterface.has("HoldTimeUp"))
			channelizedE1serialInterface.setHoldtimeUp(wanInterface.get("HoldTimeUp").toString());

		if (wanInterface.has("name"))
			channelizedE1serialInterface.setInterfaceName(wanInterface.get("name").toString());
		channelizedE1serialInterface.setInterfaceProtocolMappings(null);// ?

		if (wanInterface.has("v4IpAddress") && wanInterface.get("v4IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v4IpAddress").has("address"))
			channelizedE1serialInterface
					.setIpv4Address(wanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (wanInterface.has("v6IpAddress") && wanInterface.get("v6IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v6IpAddress").has("address"))
			channelizedE1serialInterface
					.setIpv6Address(wanInterface.getJSONObject("v6IpAddress").get("address").toString());

		if (wanInterface.has("isCRC4Enabled")) {

			if (wanInterface.get("isCRC4Enabled").toString().equalsIgnoreCase("true"))
				channelizedE1serialInterface.setIscrcforenabled((byte) 1);
			else
				channelizedE1serialInterface.setIscrcforenabled((byte) 0);

		}

		Byte defvalue = (byte) 0;
		channelizedE1serialInterface.setIshdlcConfig(defvalue);
		channelizedE1serialInterface.setIshsrpEnabled(defvalue);
		channelizedE1serialInterface.setIsvrrpEnabled(defvalue);

		if (wanInterface.has("KeepAlive"))
			channelizedE1serialInterface.setKeepalive(wanInterface.get("KeepAlive").toString());

		channelizedE1serialInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (wanInterface.has("Mode"))
			channelizedE1serialInterface.setMode(wanInterface.get("Mode").toString());

		channelizedE1serialInterface.setModifiedBy("Optimus_Port_ILL_migration");

		if (wanInterface.has("v4IpAddress") && wanInterface.get("v4IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v4IpAddress").has("address"))
			channelizedE1serialInterface
					.setModifiedIpv4Address(wanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (wanInterface.has("v6IpAddress") && wanInterface.get("v6IpAddress") instanceof JSONObject
				&& wanInterface.getJSONObject("v6IpAddress").has("address"))
			channelizedE1serialInterface
					.setModifiedIpv6Address(wanInterface.getJSONObject("v6IpAddress").get("address").toString());

		channelizedE1serialInterface.setModifiedSecondaryIpv4Address(null);

		if (wanInterface.has("MTU"))
			channelizedE1serialInterface.setMtu(wanInterface.get("MTU").toString());

		if (wanInterface.has("physicalPortName"))
			channelizedE1serialInterface.setPhysicalPort(wanInterface.get("physicalPortName").toString());

		if (wanInterface.has("PortType"))
			channelizedE1serialInterface.setPortType(wanInterface.get("PortType").toString());

		if (wanInterface.has("SecondaryV4IpAddress")
				&& wanInterface.getJSONObject("SecondaryV4IpAddress").has("address"))
			channelizedE1serialInterface.setSecondaryIpv4Address(
					wanInterface.getJSONObject("SecondaryV4IpAddress").get("address").toString());

		if (wanInterface.has("SecondaryV6IpAddress")
				&& wanInterface.getJSONObject("SecondaryV6IpAddress").has("address"))
			channelizedE1serialInterface.setSecondaryIpv6Address(
					wanInterface.getJSONObject("SecondaryV6IpAddress").get("address").toString());

		channelizedE1serialInterface.setStartDate(new Timestamp(new Date().getTime()));

		if (wanInterface.has("UpCount"))
			channelizedE1serialInterface.setUpcount(wanInterface.get("UpCount").toString());

		channelizedE1serialInterfaceRepository.saveAndFlush(channelizedE1serialInterface);
		return channelizedE1serialInterface;
	}

	public EthernetInterface saveEthernetInterface(JSONObject wanInterface) {

		EthernetInterface ethernetInterface = new EthernetInterface();
		// ethernetInterface.setAclPolicyCriterias(aclPolicyCriterias);// ?
		if (wanInterface.has("isAutoNegotiation"))
			ethernetInterface.setAutonegotiationEnabled(wanInterface.get("isAutoNegotiation").toString());

		if (wanInterface.has("isBFDEnabled")) {
			if (wanInterface.get("isBFDEnabled").toString().equalsIgnoreCase("true")) {
				ethernetInterface.setIsbfdEnabled((byte) 1);
				if (wanInterface.has("BFDMultiplier"))
					ethernetInterface.setBfdMultiplier(wanInterface.get("BFDMultiplier").toString());
				if (wanInterface.has("BFDReceiveinterval"))
					ethernetInterface.setBfdreceiveInterval(wanInterface.get("BFDReceiveinterval").toString());
				if (wanInterface.has("BFDTransmitinterval"))
					ethernetInterface.setBfdtransmitInterval(wanInterface.get("BFDTransmitinterval").toString());
			} else {
				ethernetInterface.setIsbfdEnabled((byte) 0);
			}
		}

		if (wanInterface.has("duplex"))
			ethernetInterface.setDuplex(wanInterface.get("duplex").toString());
		if (wanInterface.has("encapsulation"))
			ethernetInterface.setEncapsulation(wanInterface.get("encapsulation").toString());

		ethernetInterface.setEndDate(null);

		if (wanInterface.has("Framing"))
			ethernetInterface.setFraming(wanInterface.get("Framing").toString());

		if (wanInterface.has("HoldTimeDown"))
			ethernetInterface.setHoldtimeDown(wanInterface.get("HoldTimeDown").toString());

		if (wanInterface.has("HoldTimeUp"))
			ethernetInterface.setHoldtimeUp(wanInterface.get("HoldTimeUp").toString());

		ethernetInterface.setHsrpVrrpProtocols(null);// ?
		ethernetInterface.setInterfaceProtocolMappings(null);// ?

		if (wanInterface.has("vlan"))
			ethernetInterface.setInnerVlan(wanInterface.get("vlan").toString());// ?vlan

		ethernetInterface.setInterfaceName(wanInterface.get("name").toString());

		if (wanInterface.has("v4IpAddress") && wanInterface.get("v4IpAddress") instanceof JSONObject)
			ethernetInterface.setIpv4Address(wanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (wanInterface.has("v6IpAddress") && wanInterface.get("v6IpAddress") instanceof JSONObject)
			ethernetInterface.setIpv6Address(wanInterface.getJSONObject("v6IpAddress").get("address").toString());

		Byte defvalue = (byte) 0;
		ethernetInterface.setIshsrpEnabled(defvalue);
		ethernetInterface.setIsvrrpEnabled(defvalue);

		ethernetInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (wanInterface.has("mediaType"))
			ethernetInterface.setMediaType(wanInterface.get("mediaType").toString());

		if (wanInterface.has("mode"))
			ethernetInterface.setMode(wanInterface.get("mode").toString());

		ethernetInterface.setModifiedBy("Optimus_Port_ILL_migration");

		if (wanInterface.has("MTU"))
			ethernetInterface.setMtu(wanInterface.get("MTU").toString());

		if(wanInterface.has("v4IpAddress") && wanInterface.get("v4IpAddress") instanceof JSONObject)
		ethernetInterface.setModifiedIpv4Address(wanInterface.getJSONObject("v4IpAddress").get("address").toString());

		if (wanInterface.has("v6IpAddress") && wanInterface.get("v6IpAddress") instanceof JSONObject)
			ethernetInterface
					.setModifiedIpv6Address(wanInterface.getJSONObject("v6IpAddress").get("address").toString());

		if (wanInterface.has("OuterVLAN"))
			ethernetInterface.setOuterVlan(wanInterface.get("OuterVLAN").toString());

		if (wanInterface.has("physicalPortName"))
			ethernetInterface.setPhysicalPort(wanInterface.get("physicalPortName").toString());

		if (wanInterface.has("PortType"))
			ethernetInterface.setPortType(wanInterface.get("PortType").toString());

		if (wanInterface.has("SecondaryV4IpAddress")
				&& wanInterface.getJSONObject("SecondaryV4IpAddress").has("address"))
			ethernetInterface.setSecondaryIpv4Address(
					wanInterface.getJSONObject("SecondaryV4IpAddress").get("address").toString());

		if (wanInterface.has("SecondaryV6IpAddress")
				&& wanInterface.getJSONObject("SecondaryV6IpAddress").has("address"))
			ethernetInterface.setSecondaryIpv6Address(
					wanInterface.getJSONObject("SecondaryV6IpAddress").get("address").toString());

		if (wanInterface.has("qosLoopinPassthroughInterface"))
			ethernetInterface.setQosLoopinPassthrough(wanInterface.get("qosLoopinPassthroughInterface").toString());

		if (wanInterface.has("speed"))
			ethernetInterface.setSpeed(wanInterface.get("speed").toString());
		ethernetInterface.setStartDate(new Timestamp(new Date().getTime()));
		ethernetInterfaceRepository.saveAndFlush(ethernetInterface);
		return ethernetInterface;
	}

	public RouterDetail saveRouterDetail(JSONObject ias) {
		RouterDetail routerDetail = new RouterDetail();
		routerDetail.setEndDate(null);
		routerDetail.setInterfaceProtocolMappings(null); // ?
		if (ias.getJSONObject("PERouter").has("v4ManagementIPAddress")
				&& ias.getJSONObject("PERouter").getJSONObject("v4ManagementIPAddress").has("address"))
			routerDetail.setIpv4MgmtAddress(
					ias.getJSONObject("PERouter").getJSONObject("v4ManagementIPAddress").get("address").toString());
		if (ias.getJSONObject("PERouter").has("v6ManagementIPAddress")
				&& ias.getJSONObject("PERouter").getJSONObject("v6ManagementIPAddress").has("address"))
			routerDetail.setIpv6MgmtAddress(
					ias.getJSONObject("PERouter").getJSONObject("v6ManagementIPAddress").get("address").toString());
		routerDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		routerDetail.setModifiedBy("Optimus_Port_ILL_migration");
		routerDetail.setRouterHostname(ias.getJSONObject("PERouter").get("hostname").toString());
		if (ias.getJSONObject("PERouter").has("make")) {
			routerDetail.setRouterMake(ias.getJSONObject("PERouter").get("make").toString());
		}
		if (ias.getJSONObject("PERouter").has("model")) {
			routerDetail.setRouterModel(ias.getJSONObject("PERouter").get("model").toString());
		}
		if (ias.getJSONObject("PERouter").has("role")) {
			routerDetail.setRouterRole(ias.getJSONObject("PERouter").get("role").toString());
		}
		routerDetail.setRouterType(ias.getJSONObject("PERouter").get("type").toString());
		routerDetail.setStartDate(new Timestamp(new Date().getTime()));
		routerDetailRepository.saveAndFlush(routerDetail);
		return routerDetail;
	}

	public IpAddressDetail saveIpAddressDetail(JSONObject ias, ServiceDetail serviceDetail1) {
		IpAddressDetail ipAddressDetail = new IpAddressDetail();
		ipAddressDetail.setEndDate(null);
		if (ias.has("ExtendedLAN") && ias.getJSONObject("ExtendedLAN").has("isEnabled")
				&& ias.getJSONObject("ExtendedLAN").get("isEnabled").toString().toLowerCase().equals("true")) {
			ipAddressDetail.setExtendedLanEnabled((byte) 1);
			if (ias.getJSONObject("ExtendedLAN").has("numberOfMacAddresses"))
				ipAddressDetail.setNoMacAddress((Integer) ias.getJSONObject("ExtendedLAN").get("numberOfMacAddresses"));
		} else if (ias.has("ExtendedLAN") && ias.getJSONObject("ExtendedLAN").has("isEnabled")
				&& ias.getJSONObject("ExtendedLAN").get("isEnabled").toString().toLowerCase().equals("false")) {
			ipAddressDetail.setExtendedLanEnabled((byte) 0);
		}

		ipAddressDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setModifiedBy("Optimus_Port_ILL_migration");
		if (ias.has("NMSServerv4IPAddress") && ias.getJSONObject("NMSServerv4IPAddress").has("address")) {
			ipAddressDetail
					.setNmsServiceIpv4Address(ias.getJSONObject("NMSServerv4IPAddress").get("address").toString());
		} else {
			ipAddressDetail.setNmsServiceIpv4Address("115.114.9.69");
		}
		ipAddressDetail.setNniVsatIpaddress(null);
		// ipAddressDetail.setNoMacAddress(numberOfMacAddresses);
		if (ias.has("pathIPType")) {
			ipAddressDetail.setPathIpType(ias.get("pathIPType").toString());
		} else {
			ipAddressDetail.setPathIpType("V4");
		}
		// if (ias.has("pingAddress1") &&
		// ias.getJSONObject("pingAddress1").has("address")) {//for gvpn
		if (ias.has("InternetGatewayv4IPAddress") && ias.getJSONObject("InternetGatewayv4IPAddress").has("address")) {
			ipAddressDetail.setPingAddress1(ias.getJSONObject("InternetGatewayv4IPAddress").get("address").toString());
		} else {
			ipAddressDetail.setPingAddress1("4.2.2.2"); // only for ILL
		}
		// if (ias.has("pingAddress2") &&
		// ias.getJSONObject("pingAddress2").has("address")) {//for gvpn
		if (ias.has("InternetGatewayv6IPAddress") && ias.getJSONObject("InternetGatewayv6IPAddress").has("address")) {
			ipAddressDetail.setPingAddress2(ias.getJSONObject("InternetGatewayv6IPAddress").get("address").toString());
		}
		ipAddressDetail.setServiceDetail(serviceDetail1);
		ipAddressDetail.setStartDate(new Timestamp(new Date().getTime()));
		ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
		return ipAddressDetail;
	}

	public UniswitchDetail saveUniswitchDetails(JSONObject ias, Topology topology) {
		UniswitchDetail uniswitchDetail = new UniswitchDetail();
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("isAutoNegotiation"))
			uniswitchDetail.setAutonegotiationEnabled(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").getJSONObject("interface").getJSONObject("ethernetInterface")
					.optString("isAutoNegotiation"));
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("duplex"))
			uniswitchDetail
					.setDuplex(ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("duplex").toString());
		uniswitchDetail.setEndDate(null);
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch").has("HandOff"))
			uniswitchDetail.setHandoff(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").get("HandOff").toString());
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch").has("hostName"))
			uniswitchDetail.setHostName(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").get("hostName").toString());
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("vlan"))
			uniswitchDetail
					.setInnerVlan(ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("vlan").toString());
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("name"))
			uniswitchDetail.setInterfaceName(
					ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("name").toString());
		uniswitchDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		uniswitchDetail.setMaxMacLimit(null);// ** add in gvpn
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("mediaType"))
			uniswitchDetail
					.setMediaType(ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("mediaType").toString());
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.has("v4ManagementIPAddress")
				&& ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
						.getJSONObject("v4ManagementIPAddress").has("address"))
			uniswitchDetail.setMgmtIp(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").getJSONObject("v4ManagementIPAddress").get("address").toString());// ?
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("mode"))
			uniswitchDetail
					.setMode(ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("mode").toString());
		uniswitchDetail.setModifiedBy("Optimus_Port_ILL_migration");
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch").has("OuterVLAN"))
			uniswitchDetail.setOuterVlan(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").get("OuterVLAN").toString());
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("physicalPortName"))
			uniswitchDetail.setPhysicalPort(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").getJSONObject("interface").getJSONObject("ethernetInterface")
					.get("physicalPortName").toString()); // ?
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("portType"))
			uniswitchDetail
					.setPortType(ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("portType").toString());
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
				.getJSONObject("interface").getJSONObject("ethernetInterface").has("speed"))
			uniswitchDetail
					.setSpeed(ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch")
							.getJSONObject("interface").getJSONObject("ethernetInterface").get("speed").toString());
		uniswitchDetail.setStartDate(new Timestamp(new Date().getTime()));

//add later col in table
//	if(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
//		.getJSONObject("uniSwitch").has("make"))
//			uniswitchDetail.setSwitchMake(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
//	.getJSONObject("uniSwitch").get("make").toString());

		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").getJSONObject("uniSwitch").has("model"))
			uniswitchDetail.setSwitchModel(ias.getJSONObject("PERouter").getJSONObject("topologyInfo")
					.getJSONObject("uniSwitch").get("model").toString());
		uniswitchDetail.setSyncVlanReqd(null);
		uniswitchDetail.setTopology(topology);
		uniswitchDetailRepository.saveAndFlush(uniswitchDetail);
		return uniswitchDetail;
	}

	public RouterUplinkport saveRouterUplinkPort(JSONObject ias, Topology topology) {
		RouterUplinkport routerUplinkport = new RouterUplinkport();
		routerUplinkport.setEndDate(null);
		routerUplinkport.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (ias.getJSONObject("PERouter").has("routerTopologyInterface1"))
			routerUplinkport.setPhysicalPort1Name(ias.getJSONObject("PERouter")
					.getJSONObject("routerTopologyInterface1").get("physicalPortName").toString());
		if (ias.getJSONObject("PERouter").has("routerTopologyInterface2"))
			routerUplinkport.setPhysicalPort2Name(ias.getJSONObject("PERouter")
					.getJSONObject("routerTopologyInterface2").get("physicalPortName").toString());
		routerUplinkport.setStartDate(new Timestamp(new Date().getTime()));
		if (ias.getJSONObject("PERouter").has("topologyInfo")) {
			routerUplinkport.setTopology(topology);
		}

		routerUplinkport.setModifiedBy("Optimus_Port_ILL_migration");
		routerUplinkPortRepository.saveAndFlush(routerUplinkport);
		return routerUplinkport;
	}

	public Topology saveTopology(JSONObject ias, ServiceDetail serviceDetail1) {
		Topology topology = new Topology();
		topology.setEndDate(null);
		topology.setLastModifiedDate(new Timestamp(new Date().getTime()));
		topology.setModifiedBy("Optimus_Port_ILL_migration");
		topology.setServiceDetail(serviceDetail1);
		topology.setStartDate(new Timestamp(new Date().getTime()));
		if (ias.getJSONObject("PERouter").getJSONObject("topologyInfo").has("name"))
			topology.setTopologyName(
					ias.getJSONObject("PERouter").getJSONObject("topologyInfo").get("name").toString());
		topologyRepository.saveAndFlush(topology);
		return topology;
	}

	public AluSchedulerPolicy saveAluSchedulerPolicy(JSONObject ias, ServiceDetail serviceDetail1) {
		AluSchedulerPolicy aluSchedulerPolicy = new AluSchedulerPolicy();

		aluSchedulerPolicy.setEndDate(null);
		aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aluSchedulerPolicy.setModifiedBy("Optimus_Port_ILL_migration");
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("SAPEgressPolicy")) {
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("SAPEgressPolicy").has("Name")) {
				aluSchedulerPolicy.setSapEgressPolicyname(
						ias.getJSONObject("MultiLinkQoS").getJSONObject("SAPEgressPolicy").get("Name").toString());
			}

			Byte egressPreProv = 0;
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("SAPEgressPolicy").get("isPreProvisioned").toString()
					.equals("true")) {
				egressPreProv = 1;
			}
			aluSchedulerPolicy.setSapEgressPreprovisioned(egressPreProv);
		}

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("SAPIngressPolicy")) {
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("SAPIngressPolicy").has("Name")) {
				aluSchedulerPolicy.setSapIngressPolicyname(

						ias.getJSONObject("MultiLinkQoS").getJSONObject("SAPIngressPolicy").get("Name").toString());
			}

			Byte ingressPreProv = 0;
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("SAPIngressPolicy").get("isPreProvisioned").toString()
					.equals("true")) {
				ingressPreProv = 1;
			}
			aluSchedulerPolicy.setSapIngressPreprovisioned(ingressPreProv);
		}

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").has("isPreProvisioned")) {
			Byte schedPreProv = 0;
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").get("isPreProvisioned").toString()
					.equals("true")) {
				schedPreProv = 1;
			}
			aluSchedulerPolicy.setSchedulerPolicyIspreprovisioned(schedPreProv);
		}
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("ALUSchedulerPolicy")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").has("Name")) {
			aluSchedulerPolicy.setSchedulerPolicyName(
					ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").get("Name").toString());
		}
		aluSchedulerPolicy.setServiceDetail(serviceDetail1);
		aluSchedulerPolicy.setStartDate(new Timestamp(new Date().getTime()));
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("ALUSchedulerPolicy")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").has("totalCIRBandwidth")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
						.getJSONObject("totalCIRBandwidth").has("bandwidthValue")) {
			aluSchedulerPolicy.setTotalCirBw(ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
					.getJSONObject("totalCIRBandwidth").get("bandwidthValue").toString());
		}
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("ALUSchedulerPolicy")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").has("totalPIRBandwidth")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
						.getJSONObject("totalPIRBandwidth").has("bandwidthValue")) {
			aluSchedulerPolicy.setTotalPirBw(ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
					.getJSONObject("totalPIRBandwidth").get("bandwidthValue").toString());
		}

		aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
		return aluSchedulerPolicy;
	}

	public ServiceCosCriteria saveServiceCosCriteria(JSONObject ias, ServiceQo serviceQo1) {
		ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
		serviceCosCriteria.setAclPolicyCriterias(null);

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("Bandwidth")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("Bandwidth")
						.has("bandwidthValue")) {
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("Bandwidth").get("bandwidthValue")
					.toString().contains("E")) {

				Integer bandwidthVal = (int) Double.parseDouble(ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
						.getJSONObject("Bandwidth").get("bandwidthValue").toString());
				if (ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("Bandwidth").has("unit")) {
					String bandwidthUnit = ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
							.getJSONObject("Bandwidth").get("unit").toString();
					Integer bandwidthValinBps = banwidthConversion(bandwidthUnit, bandwidthVal);
					serviceCosCriteria.setBwBpsunit(bandwidthValinBps.toString());
				}
			} else if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
					&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("Bandwidth")
					&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("Bandwidth")
							.has("bandwidthValue")
					&& !ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("Bandwidth")
							.get("bandwidthValue").toString().matches("^[a-zA-Z]*$")) {
//					Long bandwidthVal = (Long) ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
//							.getJSONObject("Bandwidth").get("bandwidthValue");

				Integer bandwidthVal = (int) Math.round(Double.valueOf(ias.getJSONObject("MultiLinkQoS")
						.getJSONObject("cos").getJSONObject("Bandwidth").get("bandwidthValue").toString()));
				String bandwidthUnit = ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("Bandwidth")
						.get("unit").toString();
				Integer bandwidthValinBps = banwidthConversion(bandwidthUnit, bandwidthVal);
				serviceCosCriteria.setBwBpsunit(bandwidthValinBps.toString());
			}

		}

		serviceCosCriteria.setServiceQo(serviceQo1);

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("classificationCriteria")) {
			Object classCriteria = ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").get("classificationCriteria");
			if (classCriteria instanceof JSONObject) { // check
				String classificatonCos = ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
						.getJSONObject("classificationCriteria").get("xsi:type").toString();
				if (classificatonCos.equals("IPPrecedent"))
					serviceCosCriteria.setClassificationCriteria("ipprecedence");
				if (classificatonCos.equals("DSCPValue"))
					serviceCosCriteria.setClassificationCriteria("DSCP");
				if (classificatonCos.toLowerCase().equals("any"))
					serviceCosCriteria.setClassificationCriteria("ANY");
				if (classificatonCos.toLowerCase().equals("IPV4AccessControlList"))
					serviceCosCriteria.setClassificationCriteria("IP Address");
			}
		}

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("name"))
			serviceCosCriteria
					.setCosName(ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").get("name").toString());

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("BandWidthPercentage")) {
			Integer cosPercent = (Integer) ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
					.get("BandWidthPercentage");
			cosPercent *= 100;
			serviceCosCriteria.setCosPercent(cosPercent.toString());
		}

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("classifactionCriteria")) {
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("classifactionCriteria")
					.get("xsi:type").toString().equals("DSCPValue")) {

				JSONObject dhcpValues = ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
						.getJSONObject("classifactionCriteria").getJSONObject("enumDSCPCriteriaValue");
				if (dhcpValues.has("criteriaValue1"))
					serviceCosCriteria.setDhcpVal1(dhcpValues.get("criteriaValue1").toString());
				if (dhcpValues.has("criteriaValue2"))
					serviceCosCriteria.setDhcpVal2(dhcpValues.get("criteriaValue2").toString());
				if (dhcpValues.has("criteriaValue3"))
					serviceCosCriteria.setDhcpVal3(dhcpValues.get("criteriaValue3").toString());
				if (dhcpValues.has("criteriaValue4"))
					serviceCosCriteria.setDhcpVal4(dhcpValues.get("criteriaValue4").toString());
				if (dhcpValues.has("criteriaValue5"))
					serviceCosCriteria.setDhcpVal5(dhcpValues.get("criteriaValue5").toString());
				if (dhcpValues.has("criteriaValue6"))
					serviceCosCriteria.setDhcpVal6(dhcpValues.get("criteriaValue6").toString());
				if (dhcpValues.has("criteriaValue7"))
					serviceCosCriteria.setDhcpVal7(dhcpValues.get("criteriaValue7").toString());
				serviceCosCriteria.setDhcpVal8(null);

			}
			if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
					&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").getJSONObject("classifactionCriteria")
							.get("xsi:type").toString().equals("IPPrecedent")) {
				JSONObject ipPecedentCriteriaValues = ias.getJSONObject("MultiLinkQoS").getJSONObject("cos")
						.getJSONObject("classifactionCriteria").getJSONObject("IPPrecedentCriteriaValue");
				if (ipPecedentCriteriaValues.has("criteriaValue1"))
					serviceCosCriteria.setIpprecedenceVal1(ipPecedentCriteriaValues.get("criteriaValue1").toString());
				if (ipPecedentCriteriaValues.has("criteriaValue2"))
					serviceCosCriteria.setIpprecedenceVal2(ipPecedentCriteriaValues.get("criteriaValue2").toString());
				if (ipPecedentCriteriaValues.has("criteriaValue3"))
					serviceCosCriteria.setIpprecedenceVal3(ipPecedentCriteriaValues.get("criteriaValue3").toString());
				if (ipPecedentCriteriaValues.has("criteriaValue4"))
					serviceCosCriteria.setIpprecedenceVal4(ipPecedentCriteriaValues.get("criteriaValue4").toString());
				if (ipPecedentCriteriaValues.has("criteriaValue5"))
					serviceCosCriteria.setIpprecedenceVal5(ipPecedentCriteriaValues.get("criteriaValue5").toString());
				if (ipPecedentCriteriaValues.has("criteriaValue6"))
					serviceCosCriteria.setIpprecedenceVal6(ipPecedentCriteriaValues.get("criteriaValue6").toString());
				if (ipPecedentCriteriaValues.has("criteriaValue7"))
					serviceCosCriteria.setIpprecedenceVal7(ipPecedentCriteriaValues.get("criteriaValue7").toString());

				serviceCosCriteria.setIpprecedenceVal8(null);
			}
		}
		serviceCosCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setModifiedBy("Optimus_Port_ILL_migration");
		serviceCosCriteria.setServiceQo(serviceQo1);
		serviceCosCriteria.setStartDate(new Timestamp(new Date().getTime()));
		serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
		return serviceCosCriteria;
	}

	public ServiceQo saveServiceQo(JSONObject ias, ServiceDetail serviceDetail1) {

		ServiceQo serviceQo1 = new ServiceQo();
		serviceQo1.setChildqosPolicyname(null);
		serviceQo1.setCosPackage(null);
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("QoSProfile"))
			serviceQo1.setCosProfile(ias.getJSONObject("MultiLinkQoS").get("QoSProfile").toString());

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cosType"))
			serviceQo1.setCosType(ias.getJSONObject("MultiLinkQoS").get("cosType").toString());

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("cos")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("cosUpdateAction"))
			serviceQo1.setCosUpdateAction(
					ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").get("cosUpdateAction").toString());

		serviceQo1.setEndDate(null);
		serviceQo1.setFlexiCosIdentifier(null);
		serviceQo1.setIsbandwidthApplicable(null);

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("IsDefaultFC")) {
			Byte isdefFc = 0;
			if (ias.getJSONObject("MultiLinkQoS").get("IsDefaultFC").toString().equals("true")) {
				isdefFc = (byte) 1;
			}
			serviceQo1.setIsdefaultFc(isdefFc);
		}
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").has("IsFlexiCoS")) {
			Byte isFlexiCos = 0;
			if (ias.getJSONObject("MultiLinkQoS").getJSONObject("cos").get("IsFlexiCoS").toString()
					.equalsIgnoreCase("true"))
				isFlexiCos = 1;

			serviceQo1.setIsflexicos(isFlexiCos);
		}
		serviceQo1.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceQo1.setModifiedBy("Optimus_Port_ILL_migration");
		serviceQo1.setNcTraffic(null);

		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("ALUSchedulerPolicy")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").has("TotalPIRBandwidth")
				&& ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
						.getJSONObject("TotalPIRBandwidth").has("bandwidthValue")) {
			serviceQo1.setPirBw(ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
					.getJSONObject("TotalPIRBandwidth").get("bandwidthValue").toString());// blank for now
		}

		serviceQo1.setPirBwUnit(null);// blank for now
		serviceQo1.setQosPolicyname(null);
		if (ias.has("MultiLinkQoS") && ias.getJSONObject("MultiLinkQoS").has("QosTrafficMode"))
			serviceQo1.setQosTrafiicMode(ias.getJSONObject("MultiLinkQoS").get("QosTrafficMode").toString());
		serviceQo1.setServiceDetail(serviceDetail1);
		serviceQo1.setStartDate(new Timestamp(new Date().getTime()));
		serviceQo1.setSummationOfBw(null);//
		serviceQoRepository.saveAndFlush(serviceQo1);
		return serviceQo1;
	}

	public ServiceDetail saveServiceDetails(JSONObject ias, JSONObject orderInfo, OrderDetail orderDetail,
			Integer serviceDetailVersion) {
		ServiceDetail serviceDetail1 = new ServiceDetail();
		serviceDetail1.setAluSchedulerPolicies(null);
		if (ias.getJSONObject("PERouter").has("make") && ias.getJSONObject("PERouter").get("make").toString().equals("ALCATEL IP")
				&& ias.get("ServiceType").toString().equals("GVPN")) {
			serviceDetail1.setAluSvcid(ias.get("ALUServiceId").toString()); // check in gvpn
		}
		if (ias.getJSONObject("PERouter").has("make") && ias.getJSONObject("PERouter").get("make").toString().equals("ALCATEL IP")) {
			if (ias.has("ALUServiceName"))
				serviceDetail1.setAluSvcName(ias.get("ALUServiceName").toString());
		}
		if (ias.has("BurstableBandwidth") && ias.get("BurstableBandwidth") instanceof JSONObject) {
			Float double1;
			if (ias.getJSONObject("BurstableBandwidth").has("bandwidthValue")) {
				if (ias.getJSONObject("BurstableBandwidth").get("bandwidthValue") instanceof String) {
					String valueStr = ias.getJSONObject("BurstableBandwidth").get("bandwidthValue").toString();
					if (valueStr.matches("^[a-zA-Z]*$"))
						serviceDetail1.setBurstableBw(null);
				} else if (ias.getJSONObject("BurstableBandwidth").get("bandwidthValue") instanceof Integer) {
					serviceDetail1.setBurstableBw(Float.valueOf(ias.getJSONObject("BurstableBandwidth").get("bandwidthValue").toString()));
				}
				
				else {
					Object burstBw = ias.getJSONObject("BurstableBandwidth").get("bandwidthValue");
					 if( burstBw instanceof Double)
						 double1 = ((Double)burstBw).floatValue();
					 else
						 double1 = (Float)burstBw;
					serviceDetail1.setBurstableBw(double1);
				}
				
			}

			if (ias.getJSONObject("BurstableBandwidth").has("unit"))
				serviceDetail1.setBurstableBwUnit(ias.getJSONObject("BurstableBandwidth").get("unit").toString());
		}

		serviceDetail1.setCiscoImportMaps(null);
		if (ias.has("CSSSAMmanagerID") && ias.getJSONObject("PERouter").has("make") && ias.getJSONObject("PERouter").get("make").toString().equals("ALCATEL IP")) {
			serviceDetail1.setCssSammgrId((Integer) ias.get("CSSSAMmanagerID"));
		}
		if (ias.getJSONObject("PERouter").has("make") && ias.getJSONObject("PERouter").get("make").toString().equals("JUNIPER")) {
			if (ias.has("DataTransferCommit"))
				serviceDetail1.setDataTransferCommit(ias.get("DataTransferCommit").toString());
			if (ias.has("DataTransferCommitUnit"))
				serviceDetail1.setDataTransferCommitUnit(ias.get("DataTransferCommitUnit").toString());
			if (ias.has("DescriptionFreeText"))
				serviceDetail1.setDescription(ias.get("DescriptionFreeText").toString());
		}
		serviceDetail1.setEligibleForRevision(null);
		serviceDetail1.setEndDate(null);
		serviceDetail1.setExpediteTerminate(null);
		serviceDetail1.setExternalRefid(null);
		if (ias.get("ServiceType").toString().equals("GVPN")) {
			serviceDetail1.setIntefaceDescSvctag("GVPN");
		}
		serviceDetail1.setIpAddressDetails(null); // ?
		Byte iscustomConfigReqd;
		if (ias.has("isCustomconfigReqd")) {
			if (ias.get("isCustomconfigReqd").toString().equals("false"))
				iscustomConfigReqd = (byte) 0;
			else
				iscustomConfigReqd = (byte) 1;
			serviceDetail1.setIscustomConfigReqd(iscustomConfigReqd);
		}
		if (orderInfo.has("isDowntimeRequired")) {
			Byte isDowntimeRequired;
			if (orderInfo.get("isDowntimeRequired").toString().equals("false"))
				isDowntimeRequired = (byte) 0;
			else
				isDowntimeRequired = (byte) 1;
			serviceDetail1.setIsdowntimeReqd(isDowntimeRequired);
		}
		Byte isIDCService;
		if (ias.has("isIDCService")) {
			if (ias.get("isIDCService").toString().equals("false"))
				isIDCService = (byte) 0;
			else
				isIDCService = (byte) 1;
			serviceDetail1.setIsIdcService(isIDCService);
		}

		serviceDetail1.setIsManualpostvalidationReqd(null);
		serviceDetail1.setIsportChanged(null);
		serviceDetail1.setIsrevised(null);
		if (ias.get("ServiceType").toString().equals("GVPN") && orderInfo.has("LMProvider")) {
			serviceDetail1.setLastmileProvider(orderInfo.get("LMProvider").toString());
		}
		if (orderInfo.has("LMType")) {
			serviceDetail1.setLastmileType(orderInfo.get("LMType").toString());
		}
		serviceDetail1.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceDetail1.setLmComponents(null);
		if (ias.has("ManagementType"))
			serviceDetail1.setMgmtType(ias.get("ManagementType").toString());
		serviceDetail1.setModifiedBy("Optimus_Port_ILL_migration");
		serviceDetail1.setMulticastings(null); // ?
//		String ts = new Timestamp(new Date().getTime()).toString();
//		String timestamp1 = ts.replaceAll("\\D", "");
		serviceDetail1.setNetpRefid(ias.get("netpReferenceId").toString()); // ?
		if (orderInfo.has("OldServiceID"))
			serviceDetail1.setOldServiceId(orderInfo.get("OldServiceID").toString());// get sometimes
		if (ias.has("redundancyRole"))
			serviceDetail1.setRedundancyRole(ias.get("redundancyRole").toString());
		if (ias.getJSONObject("PERouter").has("make")) {
			serviceDetail1.setRouterMake(ias.getJSONObject("PERouter").get("make").toString());
		}
		if (ias.has("ScopeOfManagement")) {// !ias.get("ManagementType").equals("UNMANAGED") &&
			serviceDetail1.setScopeOfMgmt(ias.get("ScopeOfManagement").toString());
		}
		if (ias.has("ServiceBandwidth") && ias.getJSONObject("ServiceBandwidth").has("bandwidthValue")) {
			if (ias.getJSONObject("ServiceBandwidth").get("bandwidthValue") instanceof String) {
				String valueStr = ias.getJSONObject("ServiceBandwidth").get("bandwidthValue").toString();
				if (valueStr.matches("^[a-zA-Z]*$"))
					serviceDetail1.setServiceBandwidth(null);
			}
			else {
				Float bandwidthValue = Float
					.valueOf(ias.getJSONObject("ServiceBandwidth").get("bandwidthValue").toString());
			serviceDetail1.setServiceBandwidth(bandwidthValue);
			}
			if (ias.getJSONObject("ServiceBandwidth").has("unit"))
				serviceDetail1.setServiceBandwidthUnit(ias.getJSONObject("ServiceBandwidth").get("unit").toString());
		
		}
		serviceDetail1.setServiceComponenttype(ias.get("ComponentType").toString());
		serviceDetail1.setServiceId(orderInfo.get("ServiceID").toString());
		serviceDetail1.setServiceQos(null);
		serviceDetail1.setServiceState("ACTIVE");
		serviceDetail1.setServiceSubtype(ias.get("ServiceSubType").toString());
		serviceDetail1.setServiceType(ias.get("ServiceType").toString());
		serviceDetail1.setSkipDummyConfig(null);
		serviceDetail1.setSolutionId(null);
		serviceDetail1.setSolutionName(null);
		serviceDetail1.setStartDate(new Timestamp(new Date().getTime()));
		if (ias.has("ServiceLink") && ias.has("redundancyRole")
				&& !ias.get("redundancyRole").toString().toLowerCase().equals("single")) {
			serviceDetail1.setSvclinkSrvid(ias.getJSONObject("ServiceLink").get("serviceId").toString());
			if (ias.getJSONObject("ServiceLink").has("role"))
				serviceDetail1.setSvclinkRole(ias.getJSONObject("ServiceLink").get("role").toString());
		}
		serviceDetail1.setTrfsDate(null);// for terminated records
		serviceDetail1.setTrfsTriggerDate(null);// for terminated records
		serviceDetail1.setTriggerNccmOrder(null);
		if (ias.has("UsageModel")) {
			serviceDetail1.setUsageModel(ias.get("UsageModel").toString());
		}
		serviceDetail1.setVpnSolutions(null);// ?
		serviceDetail1.setVrfs(null); // ?
		serviceDetail1.setVersion(serviceDetailVersion + 1); // default

		// new addition
		if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerAddress")
				&& orderInfo.getJSONObject("customer").getJSONObject("Customer").getJSONObject("customerAddress")
						.has("addressLine1")) {
			serviceDetail1.setAddressLine1(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.getJSONObject("customerAddress").get("addressLine1").toString());
		}

		if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerAddress")
				&& orderInfo.getJSONObject("customer").getJSONObject("Customer").getJSONObject("customerAddress")
						.has("addressLine2"))
			serviceDetail1.setAddressLine2(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.getJSONObject("customerAddress").get("addressLine2").toString());

		serviceDetail1.setAddressLine3(null);

		if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerAddress") && orderInfo
				.getJSONObject("customer").getJSONObject("Customer").getJSONObject("customerAddress").has("city"))
			serviceDetail1.setCity(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.getJSONObject("customerAddress").get("city").toString());

		if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerAddress") && orderInfo
				.getJSONObject("customer").getJSONObject("Customer").getJSONObject("customerAddress").has("country"))
			serviceDetail1.setCountry(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.getJSONObject("customerAddress").get("country").toString());

		if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerAddress") && orderInfo
				.getJSONObject("customer").getJSONObject("Customer").getJSONObject("customerAddress").has("pincode"))
			serviceDetail1.setPincode(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.getJSONObject("customerAddress").get("pincode").toString());

//		  serviceDetail1.setCopfId(orderInfo.get("COPFID").toString());
//		  serviceDetail1.setCustCuId(null);
//		  serviceDetail1.setCountry(orderInfo.getJSONObject("customer").getJSONObject(
//		  "Customer") .getJSONObject("customerAddress").get("country").toString());
//		  serviceDetail1.setLocation(orderInfo.getJSONObject("customer").getJSONObject(
//		  "Customer") .getJSONObject("customerAddress").get("location").toString());
//		  serviceDetail1.setLocation(orderInfo.getJSONObject("customer").getJSONObject(
//		  "Customer") .getJSONObject("customerAddress").get("pincode").toString());

		if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerAddress") && orderInfo
				.getJSONObject("customer").getJSONObject("Customer").getJSONObject("customerAddress").has("state"))
			serviceDetail1.setState(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.getJSONObject("customerAddress").get("state").toString());
		serviceDetail1.setServiceOrderType("ILL");
		if (orderInfo.has("OrderType")) {
			serviceDetail1.setOrderType(orderInfo.get("OrderType").toString());
		}
		serviceDetail1.setOrderCategory("Customer");
		serviceDetail1.setOrderDetail(orderDetail);
		serviceDetailRepository.saveAndFlush(serviceDetail1);
		return serviceDetail1;
	}

	public OrderDetail saveOrderDetails(JSONObject ias, JSONObject orderInfo) {
		OrderDetail orderDetail = new OrderDetail();
		if (orderInfo.has("SFDC_Opty_Id"))
			orderDetail.setSfdcOpptyId(orderInfo.get("SFDC_Opty_Id").toString());
		if (orderInfo.has("customer")) {
			if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("accountId"))
				orderDetail.setAccountId(
						orderInfo.getJSONObject("customer").getJSONObject("Customer").get("accountId").toString());
			orderDetail.setAddressLine1(null);
			orderDetail.setAddressLine2(null);
			orderDetail.setAddressLine3(null);
			if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("aluCustomerID")) {
				orderDetail.setAluCustomerId(
						orderInfo.getJSONObject("customer").getJSONObject("Customer").get("aluCustomerID").toString());
			}
		}
		orderDetail.setAsdOpptyFlag(null);
		orderDetail.setCity(null);
		if (orderInfo.has("COPFID"))
			orderDetail.setCopfId(orderInfo.get("COPFID").toString());
		orderDetail.setCustCuId(null);
		orderDetail.setCountry(null);
		if (orderInfo.has("customer")) {
			if (orderInfo.getJSONObject("customer").has("Customer")) {
				if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerCategory"))
					orderDetail.setCustomerCategory(orderInfo.getJSONObject("customer").getJSONObject("Customer")
							.get("customerCategory").toString());
				orderDetail.setCustomerContact(null);
				if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("CRNID"))
					orderDetail.setCustomerCrnId(
							orderInfo.getJSONObject("customer").getJSONObject("Customer").get("CRNID").toString());
				if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerEmailID"))
					orderDetail.setCustomerEmail(orderInfo.getJSONObject("customer").getJSONObject("Customer")
							.get("customerEmailID").toString());
				if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerName"))
					orderDetail.setCustomerName(orderInfo.getJSONObject("customer").getJSONObject("Customer")
							.get("customerName").toString());
				orderDetail.setCustomerPhoneno(null);
				if (orderInfo.getJSONObject("customer").getJSONObject("Customer").has("customerType"))
					orderDetail.setCustomerType(orderInfo.getJSONObject("customer").getJSONObject("Customer")
							.get("customerType").toString());
				else
					orderDetail.setCustomerType("Others");

			}
		}
		orderDetail.setEndCustomerName(null);
		orderDetail.setEndDate(null);
		orderDetail.setGroupId(orderInfo.getJSONObject("originatorDetails").get("group").toString());
		orderDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		orderDetail.setLocation(null);
		orderDetail.setModifiedBy("Optimus_Port_ILL_migration");
		orderDetail.setOrderCategory("Customer"); // or Internal
		orderDetail.setOrderStatus(orderInfo.get("orderStatus").toString());
		orderDetail.setOrderType(orderInfo.get("OrderType").toString());
		orderDetail.setOrderUuid(null);
		String origDate = orderInfo.getJSONObject("originatorDetails").get("origination_date").toString();
		SimpleDateFormat sdf;
		if (origDate.length() == 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		}
		Date dateFormatVal = null;
		try {
			dateFormatVal = sdf.parse(origDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orderDetail.setOriginatorDate(new Timestamp(dateFormatVal.getTime()));
		orderDetail.setOriginatorName("OPTIMUS");
		/*if (orderInfo.getJSONObject("originatorDetails").has("name"))
			orderDetail.setOriginatorName(orderInfo.getJSONObject("originatorDetails").get("name").toString());*/
		orderDetail.setPincode(null);
		if (orderInfo.getJSONObject("customer").has("Customer")
				&& orderInfo.getJSONObject("customer").getJSONObject("Customer").has("SAMCustomerDescription")) {
			orderDetail.setSamCustomerDescription(orderInfo.getJSONObject("customer").getJSONObject("Customer")
					.get("SAMCustomerDescription").toString());
		}
		orderDetail.setStartDate(new Timestamp(new Date().getTime()));
		orderDetail.setState(null);
		orderDetailRepository.saveAndFlush(orderDetail);
		return orderDetail;
	}

	public IpaddrLanv4Address setlanv4(JSONObject lanv4, IpAddressDetail ipAddressDetail) {
		IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
		ipaddrLanv4Address.setEndDate(null);
		Byte iscustomerprovided;
		if (lanv4.get("CustomerProvided").toString().equals("false")) {
			iscustomerprovided = (byte) 0;
		} else {
			iscustomerprovided = (byte) 1;
		}
		ipaddrLanv4Address.setIscustomerprovided(iscustomerprovided);
		ipaddrLanv4Address.setIssecondary(null); // ?
		ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
		ipaddrLanv4Address.setLanv4Address(lanv4.get("address").toString());
		ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrLanv4Address.setModifiedBy("Optimus_Port_ILL_migration");
		ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
		return ipaddrLanv4Address;
	}

	public IpaddrWanv4Address setwanv4(JSONObject wanv4Object, IpAddressDetail ipAddressDetail) {
		IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
		ipaddrWanv4Address.setEndDate(null);
		Byte iscustomerprovidedwanv4;
		if (wanv4Object.get("CustomerProvided").toString().equals("false")) {
			iscustomerprovidedwanv4 = (byte) 0;
		} else {
			iscustomerprovidedwanv4 = (byte) 1;
		}
		ipaddrWanv4Address.setIscustomerprovided(iscustomerprovidedwanv4);
		ipaddrWanv4Address.setIssecondary(null); // ?
		ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
		ipaddrWanv4Address.setWanv4Address(wanv4Object.get("address").toString());
		ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrWanv4Address.setModifiedBy("Optimus_Port_ILL_migration");
		ipaddrWanv4Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
		return ipaddrWanv4Address;
	}

	public IpaddrLanv6Address setlanv6(JSONObject lanv6, IpAddressDetail ipAddressDetail) {
		IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
		ipaddrLanv6Address.setEndDate(null);
		Byte iscustomerprovided1;
		if (lanv6.get("CustomerProvided").toString().equals("false")) {
			iscustomerprovided1 = (byte) 0;
		} else {
			iscustomerprovided1 = (byte) 1;
		}
		ipaddrLanv6Address.setIscustomerprovided(iscustomerprovided1);
		ipaddrLanv6Address.setIssecondary(null); // ?
		ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
		if (lanv6.has("address"))
			ipaddrLanv6Address.setLanv6Address(lanv6.get("address").toString());
		ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrLanv6Address.setModifiedBy("Optimus_Port_ILL_migration");
		ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
		return ipaddrLanv6Address;
	}

	public IpaddrWanv6Address setwanv6(JSONObject wanv6Object, IpAddressDetail ipAddressDetail) {
		IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
		ipaddrWanv6Address.setEndDate(null);
		Byte iscustomerprovidedwanv6;
		if (wanv6Object.get("CustomerProvided").toString().equals("false")) {
			iscustomerprovidedwanv6 = (byte) 0;
		} else {
			iscustomerprovidedwanv6 = (byte) 1;
		}
		ipaddrWanv6Address.setIscustomerprovided(iscustomerprovidedwanv6);
		ipaddrWanv6Address.setIssecondary(null); // ?
		ipaddrWanv6Address.setIpAddressDetail(ipAddressDetail);
		if (wanv6Object.has("address"))
			ipaddrWanv6Address.setWanv6Address(wanv6Object.get("address").toString());
		ipaddrWanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrWanv6Address.setModifiedBy("Optimus_Port_ILL_migration");
		ipaddrWanv6Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
		Set<IpaddrWanv6Address> ipaddrWanv6Addresses = new HashSet<>();
		ipaddrWanv6Addresses.add(ipaddrWanv6Address);
		ipAddressDetail.setIpaddrWanv6Addresses(ipaddrWanv6Addresses);
		return ipaddrWanv6Address;
	}

	public AclPolicyCriteria setAclPolicyCriteria(String key, JSONObject aclObject, EthernetInterface ethernetInterface,
			ChannelizedE1serialInterface channelizedE1serialInterface, ChannelizedSdhInterface channelizedSdhInterface,
			String name) {

		AclPolicyCriteria aclPolicyCriteria = new AclPolicyCriteria();
		aclPolicyCriteria.setAction(null);

		aclPolicyCriteria.setDescription(null);
		aclPolicyCriteria.setDestinationAny(null);
		aclPolicyCriteria.setDestinationCondition(null);
		aclPolicyCriteria.setDestinationPortnumber(null);
		aclPolicyCriteria.setDestinationSubnet(null);
		aclPolicyCriteria.setEndDate(null);
		if (name.contains("Serial") || name.contains("serial"))
			aclPolicyCriteria.setChannelizedE1serialInterface(channelizedE1serialInterface);
		if (name.contains("sdh") || name.contains("Sdh"))
			aclPolicyCriteria.setChannelizedSdhInterface(channelizedSdhInterface);
		if (name.contains("ethernet") || name.contains("Ethernet"))
			aclPolicyCriteria.setEthernetInterface(ethernetInterface);

		if ((key.contains("in") || key.contains("In")) && !(key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsinboundaclIpv4Applied(applied);
			aclPolicyCriteria.setInboundIpv4AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (Objects.nonNull(aclObject) && aclObject.has("ISACLPreProvisioned") && Objects.nonNull(aclObject.get("ISACLPreProvisioned")) && "true".equals(aclObject.get("ISACLPreProvisioned").toString()))
				isPrevProv = 1;
			aclPolicyCriteria.setIsinboundaclIpv4Preprovisioned(isPrevProv);
		}

		if ((key.contains("in") || key.contains("In")) && (key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsinboundaclIpv6Applied(applied);
			aclPolicyCriteria.setInboundIpv6AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (Objects.nonNull(aclObject) && aclObject.has("ISACLPreProvisioned") && Objects.nonNull(aclObject.get("ISACLPreProvisioned")) && "true".equals(aclObject.get("ISACLPreProvisioned").toString()))
				isPrevProv = 1;
			aclPolicyCriteria.setIsinboundaclIpv6Preprovisioned(isPrevProv);
		}

		if ((key.contains("out") || key.contains("Out")) && !(key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsoutboundaclIpv4Applied(applied);
			aclPolicyCriteria.setOutboundIpv4AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (Objects.nonNull(aclObject) && aclObject.has("ISACLPreProvisioned") && Objects.nonNull(aclObject.get("ISACLPreProvisioned")) && "true".equals(aclObject.get("ISACLPreProvisioned").toString()))
				isPrevProv = 1;
			aclPolicyCriteria.setIsoutboundaclIpv4Preprovisioned(isPrevProv);
		}

		if ((key.contains("out") || key.contains("Out")) && (key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsoutboundaclIpv6Applied(applied);
			aclPolicyCriteria.setOutboundIpv6AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (Objects.nonNull(aclObject) && aclObject.has("ISACLPreProvisioned") && Objects.nonNull(aclObject.get("ISACLPreProvisioned")) && "true".equals(aclObject.get("ISACLPreProvisioned").toString()))
				isPrevProv = 1;
			aclPolicyCriteria.setIsoutboundaclIpv6Preprovisioned(isPrevProv);
		}

//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAcl(issvcQosCoscriteriaIpaddrAcl);
//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(issvcQosCoscriteriaIpaddrAclName);
//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrPreprovisioned(issvcQosCoscriteriaIpaddrPreprovisioned);
//		aclPolicyCriteria.setServiceCosCriteria(serviceCosCriteria);

		aclPolicyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aclPolicyCriteria.setModifiedBy("Optimus_Port_ILL_migration");

		aclPolicyCriteria.setPolicyCriteria(null);// ?
		aclPolicyCriteria.setProtocol(null);// ?
		aclPolicyCriteria.setSequence(null);
		aclPolicyCriteria.setSourceAny(null);
		aclPolicyCriteria.setSourceCondition(null);
		aclPolicyCriteria.setSourcePortnumber(null);
		aclPolicyCriteria.setSourceSubnet(null);
		aclPolicyCriteria.setStartDate(new Timestamp(new Date().getTime()));

		return aclPolicyCriteria;
	}

	public void setPolicyTypeAndCriterias(String key, JSONObject policyObject, Bgp bgp) {
		PolicyType policyType = new PolicyType();
		if (key.contains("Inbound")) {
			if (key.contains("V4")) {
				if (policyObject.has("Name"))
					policyType.setInboundIpv4PolicyName(policyObject.get("Name").toString());
				Byte preProv = 0;
				if (policyObject.has("isPreprovisioned") && policyObject.get("isPreprovisioned").toString().equals("true")) {
					preProv = 1;
				}
				policyType.setInboundIpv4Preprovisioned(preProv);
				if (policyObject.has("isStandardPolicy")) {
					Byte stdPolicy = 0;
					if (policyObject.get("isStandardPolicy").toString().equals("true")) {
						stdPolicy = 1;
					}
					policyType.setInboundIstandardpolicyv4(stdPolicy);
				}
				Byte flag = 1;
				policyType.setInboundRoutingIpv4Policy(flag);
				policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_ILL_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.contains("V6")) {
				if (policyObject.has("Name"))
					policyType.setInboundIpv6PolicyName(policyObject.get("Name").toString());
				Byte preProv = 0;
				if (policyObject.has("isPreprovisioned") && policyObject.get("isPreprovisioned").toString().equals("true")) {
					preProv = 1;
				}
				policyType.setInboundIpv6Preprovisioned(preProv);
				if (policyObject.has("isStandardPolicy")) {
					Byte stdPolicy = 0;
					if (policyObject.get("isStandardPolicy").toString().equals("true")) {
						stdPolicy = 1;
					}
					policyType.setInboundIstandardpolicyv6(stdPolicy);
				}
				Byte flag = 1;
				policyType.setInboundRoutingIpv6Policy(flag);
				policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_ILL_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}
		if (key.contains("Outbound")) {
			if (key.contains("V4")) {
				if (policyObject.has("Name"))
					policyType.setOutboundIpv4PolicyName(policyObject.get("Name").toString());
				Byte preProv = 0;
				if (policyObject.has("isPreprovisioned") && policyObject.get("isPreprovisioned").toString().equals("true")) {
					preProv = 1;
				}
				policyType.setOutboundIpv4Preprovisioned(preProv);
				if (policyObject.has("isStandardPolicy")) {
					Byte stdPolicy = 0;
					if (policyObject.has("isStandardPolicy")
							&& policyObject.get("isStandardPolicy").toString().equals("true")) {
						stdPolicy = 1;
					}
					policyType.setOutboundIstandardpolicyv4(stdPolicy);
				}
				Byte flag = 1;
				policyType.setOutboundRoutingIpv4Policy(flag);
				policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_ILL_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.contains("V6")) {
				if (policyObject.has("Name"))
					policyType.setOutboundIpv6PolicyName(policyObject.get("Name").toString());
				Byte preProv = 0;
				if (policyObject.has("isPreprovisioned") &&  policyObject.get("isPreprovisioned").toString().equals("true")) {
					preProv = 1;
				}
				policyType.setOutboundIpv6Preprovisioned(preProv);
				if (policyObject.has("isStandardPolicy")) {
					Byte stdPolicy = 0;
					if (policyObject.has("isStandardPolicy")
							&& policyObject.get("isStandardPolicy").toString().equals("true")) {
						stdPolicy = 1;
					}
					policyType.setOutboundIstandardpolicyv6(stdPolicy);
				}
				Byte flag = 1;
				policyType.setOutboundRoutingIpv6Policy(flag);
				policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_ILL_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}
		// !policyObject.has("isPreprovisioned") ||
		if (policyObject.has("isPreprovisioned") &&  !policyObject.get("isPreprovisioned").equals("true")) { // if false
			// or
			// null
			if (policyObject.has("juniperTerm")) {
//				JSONObject policyCriteriaObject = policyObject.getJSONObject("juniperTerm")
//						.getJSONObject("matchCriteria");
				String termSetCriteriaAction ="ACCEPT";
				if (policyObject.getJSONObject("juniperTerm").has("TermSetCriteriaAction")) {
					termSetCriteriaAction = policyObject.getJSONObject("juniperTerm")
							.get("TermSetCriteriaAction").toString();
				}
				// ^set this in all 8 records
				String termName ="10";
				if (policyObject.getJSONObject("juniperTerm").has("TermName")) {
					termName = policyObject.getJSONObject("juniperTerm").get("TermName").toString();
				}
				// ^set this in all 8 records
				// get criteria obj
				/*
				 * Set<String> keys = policyObject.keySet(); JSONObject termObject; for(String
				 * objkey : keys) { String regEx = "*Term"; //for each match, inbound outbound
				 * v4 v6 alcatel juniper, make a new record Boolean keyNeeded =
				 * key.matches(regEx); if(keyNeeded) { termObject =
				 * policyObject.getJSONObject(objkey); } }
				 */ // use later to check juniperTerm alcatelterm to fetch
					// criteria sets
				JSONObject termObject = policyObject.getJSONObject("juniperTerm");
				// check null for all
				// if false, create no records and no mapping
				// MATCH CRITERIA
				if (termObject.getJSONObject("matchCriteria").get("matchingCriteriaPrefixlist").toString()
						.equals("true")) {
					PolicyCriteria policyCriteria1 = new PolicyCriteria(); // prefix
																			// list
					policyCriteria1.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria1.setTermName(termName);
					Byte isPreProv = 0;
					if (termObject.getJSONObject("matchCriteria").get("IsPrefixListPreProvisioned").toString()
							.equals("true")) {
						isPreProv = 1;
					}
					Byte flag = 1;
					policyCriteria1.setMatchcriteriaPrefixlist(flag);
					if (termObject.has("matchCriteria")
							&& termObject.getJSONObject("matchCriteria").has("PrefixListName"))
						policyCriteria1.setMatchcriteriaPrefixlistName(termObject.getJSONObject("matchCriteria").get("PrefixListName").toString());
					policyCriteria1.setMatchcriteriaPprefixlistPreprovisioned(isPreProv);

					policyCriteria1.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria1.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria1.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria1);

					if (termObject.getJSONObject("matchCriteria").get("IsPrefixListPreProvisioned").toString()
							.equals("false")) {
						if (termObject.getJSONObject("matchCriteria")
								.get("matchingCriteriaPrefixlistEntry") instanceof JSONObject) {
							JSONObject prefixList = termObject.getJSONObject("matchCriteria")
									.getJSONObject("matchingCriteriaPrefixlistEntry");
							savePrefixListObject(policyCriteria1, policyType, prefixList);

						} else if (termObject.getJSONObject("matchCriteria")
								.get("matchingCriteriaPrefixlistEntry") instanceof JSONArray) {

							JSONArray prefixListArray = (JSONArray) termObject.getJSONObject("matchCriteria")
									.get("matchingCriteriaPrefixlistEntry");
							for (int i = 0; i < prefixListArray.length(); i++) {
								JSONObject prefixList = (JSONObject) prefixListArray.get(i);
								savePrefixListObject(policyCriteria1, policyType, prefixList);
							}

						}

						PolicyTypeCriteriaMapping policyTypeCriteriaMapping1 = new PolicyTypeCriteriaMapping();
						policyTypeCriteriaMapping1.setPolicyType(policyType);
						policyTypeCriteriaMapping1.setPolicyCriteriaId(policyCriteria1.getPolicyCriteriaId());
						policyTypeCriteriaMapping1.setVersion(1);
						policyTypeCriteriaMapping1.setLastModifiedDate(new Timestamp(new Date().getTime()));
						policyTypeCriteriaMapping1.setModifiedBy("Optimus_Port_ILL_migration");
						policyTypeCriteriaMapping1.setStartDate(new Timestamp(new Date().getTime()));
						policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping1);
					}
				}
				if (termObject.getJSONObject("matchCriteria").get("matchingCriteriaProtocol").toString()
						.equals("true")) {
					PolicyCriteria policyCriteria2 = new PolicyCriteria(); // protocol
					policyCriteria2.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria2.setTermName(termName);
					Byte flag = 1;
					policyCriteria2.setMatchcriteriaProtocol(flag);
					policyCriteria2.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria2.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria2.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria2);
					PolicycriteriaProtocol policycriteriaProtocol = new PolicycriteriaProtocol();
					policycriteriaProtocol.setPolicyCriteria(policyCriteria2);
					policycriteriaProtocol.setProtocolName(termObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaProtocol").get("protocol").toString());
					policycriteriaProtocol.setProtocolValue(null);
					policycriteriaProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policycriteriaProtocol.setModifiedBy("Optimus_Port_ILL_migration");
					policycriteriaProtocol.setStartDate(new Timestamp(new Date().getTime()));
					policycriteriaProtocolRepository.saveAndFlush(policycriteriaProtocol);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping2 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping2.setPolicyType(policyType);
					policyTypeCriteriaMapping2.setPolicyCriteriaId(policyCriteria2.getPolicyCriteriaId());
					policyTypeCriteriaMapping2.setVersion(1);
					policyTypeCriteriaMapping2.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping2.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping2.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping2);
				}
				if (termObject.getJSONObject("matchCriteria").get("matchingCriteriaRegexASPath").toString()
						.equals("true")) {
					PolicyCriteria policyCriteria3 = new PolicyCriteria(); // regexASPAth
					policyCriteria3.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria3.setTermName(termName);
					Byte flag = 1;
					policyCriteria3.setMatchcriteriaRegexAspath(flag);
					policyCriteria3.setMatchcriteriaRegexAspathName(null);
					policyCriteria3.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria3.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria3.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria3);
					RegexAspathConfig regexAspathConfig = new RegexAspathConfig();
					regexAspathConfig
							.setName(termObject.getJSONObject("matchCriteria").get("RegexASPathName").toString());
					regexAspathConfig.setAsPath(termObject.getJSONObject("matchCriteria").get("ASPath").toString());
					regexAspathConfig.setPolicyCriteria(policyCriteria3);
					regexAspathConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
					regexAspathConfig.setModifiedBy("Optimus_Port_ILL_migration");
					regexAspathConfig.setStartDate(new Timestamp(new Date().getTime()));
					regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping3 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping3.setPolicyType(policyType);
					policyTypeCriteriaMapping3.setPolicyCriteriaId(policyCriteria3.getPolicyCriteriaId());
					policyTypeCriteriaMapping3.setVersion(1);
					policyTypeCriteriaMapping3.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping3.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping3.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping3);
				}
				if (termObject.getJSONObject("matchCriteria").get("matchingCriteriaNeighbourCommunity").toString()
						.equals("true")) {
					PolicyCriteria policyCriteria4 = new PolicyCriteria(); // neighbourcomm
					policyCriteria4.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria4.setTermName(termName);
					// policyCriteria4.setMatchcriteriaNeighbourcommunityName(termObject.getJSONObject("matchCriteria")
					// .getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("Name").toString());
					Byte flag = 1;
					policyCriteria4.setMatchcriteriaNeighbourCommunity(flag);
					// PolicyCriteria p=
					// policyCriteriaRepository.saveAndFlush(policyCriteria4);
					policyCriteria4.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria4.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria4.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria4);
					NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
					neighbourCommunityConfig.setPolicyCriteria(policyCriteria4);
					if (termObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaNeighbourCommunityEntry").has("NeighbourCommunity"))
						neighbourCommunityConfig.setCommunity(termObject.getJSONObject("matchCriteria")
								.getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("NeighbourCommunity")
								.toString());
					if (termObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaNeighbourCommunityEntry").has("Name"))
						neighbourCommunityConfig.setName(termObject.getJSONObject("matchCriteria")
								.getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("Name").toString());
					neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
					neighbourCommunityConfig.setModifiedBy("Optimus_Port_ILL_migration");
					neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
					neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping4 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping4.setPolicyType(policyType);
					policyTypeCriteriaMapping4.setPolicyCriteriaId(policyCriteria4.getPolicyCriteriaId());
					policyTypeCriteriaMapping4.setVersion(1);
					policyTypeCriteriaMapping4.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping4.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping4.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping4);
				}
				// SETCRITERIA
				if (termObject.getJSONObject("setCriteria").get("setCriteriaLocalPreference").toString()
						.equals("true")) {
					PolicyCriteria policyCriteria5 = new PolicyCriteria(); // local
																			// pref
					policyCriteria5.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria5.setTermName(termName);
					// policyCriteria5.setSetcriteriaLocalPreference(setcriteriaLocalPreference);
					Byte flag = 1;
					policyCriteria5.setSetcriteriaLocalPreference(flag);
					if (termObject.has("setCriteria") && termObject.getJSONObject("setCriteria").has("LocalPreference"))
						policyCriteria5.setSetcriteriaLocalpreferenceName(
								termObject.getJSONObject("setCriteria").get("LocalPreference").toString());
					policyCriteria5.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria5.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria5.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria5);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping5 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping5.setPolicyType(policyType);
					policyTypeCriteriaMapping5.setPolicyCriteriaId(policyCriteria5.getPolicyCriteriaId());
					policyTypeCriteriaMapping5.setVersion(1);
					policyTypeCriteriaMapping5.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping5.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping5.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping5);
				}
				if (termObject.getJSONObject("setCriteria").get("SetCriteriaASPathPrepend").toString().equals("true")) {
					PolicyCriteria policyCriteria6 = new PolicyCriteria(); // aspath
																			// prepend
					policyCriteria6.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria6.setTermName(termName);
					Byte flag = 1;
					policyCriteria6.setSetcriteriaAspathPrepend(flag);
					policyCriteria6.setSetcriteriaAspathPrependIndex(
							termObject.getJSONObject("setCriteria").get("ASPathPreprendIndex").toString());
					policyCriteria6.setSetcriteriaAspathPrependName(
							termObject.getJSONObject("setCriteria").get("ASPathPrepend").toString());
					policyCriteria6.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria6.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria6.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria6);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping6 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping6.setPolicyType(policyType);
					policyTypeCriteriaMapping6.setPolicyCriteriaId(policyCriteria6.getPolicyCriteriaId());
					policyTypeCriteriaMapping6.setVersion(1);
					policyTypeCriteriaMapping6.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping6.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping6.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping6);
				}
				if (termObject.getJSONObject("setCriteria").get("setCriteriaMetric").toString().equals("true")) {
					PolicyCriteria policyCriteria7 = new PolicyCriteria(); // metric
					policyCriteria7.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria7.setTermName(termName);
					Byte flag = 1;
					policyCriteria7.setSetcriteriaMetric(flag);
					policyCriteria7.setSetcriteriaMetricName(
							termObject.getJSONObject("setCriteria").get("MEDValue").toString());
					policyCriteria7.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria7.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria7.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria7);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping7 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping7.setPolicyType(policyType);
					policyTypeCriteriaMapping7.setPolicyCriteriaId(policyCriteria7.getPolicyCriteriaId());
					policyTypeCriteriaMapping7.setVersion(1);
					policyTypeCriteriaMapping7.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping7.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping7.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping7);
				}
				if (termObject.getJSONObject("setCriteria").has("setCriteriaRegexASPath") && termObject
						.getJSONObject("setCriteria").get("setCriteriaRegexASPath").toString().equals("true")) {
					PolicyCriteria policyCriteria8 = new PolicyCriteria(); // regexaspath
					policyCriteria8.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria8.setTermName(termName);
					Byte flag = 1;
					policyCriteria8.setSetcriteriaRegexAspath(flag);
//					policyCriteria8.setMatchcriteriaRegexAspathName(
//							termObject.getJSONObject("setCriteria").get("setCriteriaRegexASPathName").toString());
					// one more field will be added in future
					policyCriteria8.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria8.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria8.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria8);
					RegexAspathConfig regexAspathConfig = new RegexAspathConfig();
					regexAspathConfig
							.setName(termObject.getJSONObject("setCriteria").get("RegexASPathName").toString());
					regexAspathConfig.setAsPath(termObject.getJSONObject("setCriteria").get("ASPath").toString());
					regexAspathConfig.setPolicyCriteria(policyCriteria8);
					regexAspathConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
					regexAspathConfig.setModifiedBy("Optimus_Port_ILL_migration");
					regexAspathConfig.setStartDate(new Timestamp(new Date().getTime()));
					regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping8 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping8.setPolicyType(policyType);
					policyTypeCriteriaMapping8.setPolicyCriteriaId(policyCriteria8.getPolicyCriteriaId());
					policyTypeCriteriaMapping8.setVersion(1);
					policyTypeCriteriaMapping8.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping8.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping8.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping8);
				}
				if (termObject.getJSONObject("setCriteria").get("SetCriteriaNeighbourCommunity").toString()
						.equals("true")) {
					PolicyCriteria policyCriteria9 = new PolicyCriteria(); // neighbourcomm
					policyCriteria9.setTermSetcriteriaAction(termSetCriteriaAction);
					policyCriteria9.setTermName(termName);
					Byte flag = 1;
					policyCriteria9.setSetcriteriaNeighbourCommunity(flag);
					policyCriteria9.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyCriteria9.setModifiedBy("Optimus_Port_ILL_migration");
					policyCriteria9.setStartDate(new Timestamp(new Date().getTime()));
					policyCriteriaRepository.saveAndFlush(policyCriteria9);
					/*
					 * NeighbourCommunityConfig neighbourCommunityConfig2 = new
					 * NeighbourCommunityConfig(); neighbourCommunityConfig2.setPolicyCriteria(
					 * policyCriteria9); neighbourCommunityConfig2.setCommunity(termObject.
					 * getJSONObject( "setCriteria")
					 * .getJSONObject("SetCriteriaNeighbourCommunity").get(
					 * "Community").toString()); neighbourCommunityConfig2.setName(termObject.
					 * getJSONObject("setCriteria")
					 * .getJSONObject("SetCriteriaNeighbourCommunity").get( "Name").toString());
					 * neighbourCommunityConfigRepository.saveAndFlush( neighbourCommunityConfig2);
					 */
					NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
					neighbourCommunityConfig.setPolicyCriteria(policyCriteria9);
					if (termObject.getJSONObject("setCriteria").getJSONObject("setCriteriaNeighbourCommunityEntry")
							.has("NeighbourCommunity"))
						neighbourCommunityConfig.setCommunity(termObject.getJSONObject("setCriteria")
								.getJSONObject("setCriteriaNeighbourCommunityEntry").get("NeighbourCommunity")
								.toString());
					if (termObject.getJSONObject("setCriteria").getJSONObject("setCriteriaNeighbourCommunityEntry")
							.has("Name"))
						neighbourCommunityConfig.setName(termObject.getJSONObject("setCriteria")
								.getJSONObject("setCriteriaNeighbourCommunityEntry").get("Name").toString());
					neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
					neighbourCommunityConfig.setModifiedBy("Optimus_Port_ILL_migration");
					neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
					neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping9 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping9.setPolicyType(policyType);
					policyTypeCriteriaMapping9.setPolicyCriteriaId(policyCriteria9.getPolicyCriteriaId());
					policyTypeCriteriaMapping9.setVersion(1);
					policyTypeCriteriaMapping9.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping9.setModifiedBy("Optimus_Port_ILL_migration");
					policyTypeCriteriaMapping9.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping9);
				}
			}
		}
	}

	private void savePrefixListObject(PolicyCriteria policyCriteria1, PolicyType policyType, JSONObject prefixList) {
		PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
		prefixlistConfig.setPolicyCriteria(policyCriteria1);
		prefixlistConfig.setNetworkPrefix(prefixList.get("NetworkPrefix").toString());
		prefixlistConfig.setAction(null);
		if (prefixList.has("LEValue"))
			prefixlistConfig.setLeValue(prefixList.get("LEValue").toString());
		if (prefixList.has("GEValue"))
			prefixlistConfig.setGeValue(prefixList.get("GEValue").toString());
		prefixlistConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		prefixlistConfig.setModifiedBy("Optimus_Port_ILL_migration");
		prefixlistConfig.setStartDate(new Timestamp(new Date().getTime()));
		prefixlistConfigRepository.saveAndFlush(prefixlistConfig);
	}

	@Transactional
	public boolean excelUpload(MultipartFile file) throws Exception {
			
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			//File fileW = new File("C:\\Users\\dikshag.VSNL\\Downloads\\ill146.txt");
			//FileWriter fr = new FileWriter(fileW, true);
			//BufferedWriter br = new BufferedWriter(fr);
			//PrintWriter pr = new PrintWriter(br);

			for (Sheet sheet : workbook) {

				for (int i = 1; i <= sheet.getLastRowNum(); i++) {

					
					Cell serviceId = sheet.getRow(i).getCell(0);
					serviceId.setCellType(CellType.STRING);
					Cell serviceInstId = sheet.getRow(i).getCell(3);
					serviceInstId.setCellType(CellType.STRING);
					//Cell subType = sheet.getRow(i).getCell(16);
					//Cell configReqdId = sheet.getRow(i).getCell(17);
					
					runMigration(i,serviceId.getStringCellValue(),serviceInstId.getStringCellValue(),null,null,null);

				}

			//	pr.close();
			//	br.close();
			//	fr.close();

			}
		} catch (Exception e) {

			LOGGER.error("error in excel upload : {}", e);
		}
		return true;
	}
	
	@Transactional
	public boolean runMigration(int i, String serviceId, String serviceInstId, String isTermination, ScServiceDetail scServiceDetail, ScOrder scOrder) throws Exception {

		MigrationStatus migrationStatus = new MigrationStatus();
		Args args = new Args();
		List<String> arguments = args.getItem();
		/*
		 * arguments.add("--context_param serviceType=ILL");
		 * arguments.add("--context_param componentType=Port");
		 * serviceId.setCellType(CellType.STRING);
		 * arguments.add("--context_param serviceId=" + serviceId.getStringCellValue());
		 * arguments.add("--context_param configReqId=" + configReqdId);
		 * arguments.add("--context_param serviceSubType=" + subType);
		 * arguments.add("--context_param orderType=CHANGE_ORDER");
		 */
		arguments.add("--context_param componentType=Port");
		arguments.add("--context_param searchLevel=BOTH");
		arguments.add("--context_param dataModel=LIVE");
		arguments.add("--context_param serviceInstanceId=" + serviceInstId);
		SoapRequest soapRequest = new SoapRequest();
		// soapRequest.setUrl("http://115.110.93.236:8180/L1_ReadDataEE_1.0/services/L1_ReadDataEE?wsdl");
		soapRequest.setUrl("http://115.110.93.236:8180/L1_SearchDataEE_1.0/services/L1_SearchDataEE");
		soapRequest.setRequestObject(args);
		soapRequest.setContextPath("com.tcl.dias.serviceactivation.datamigration.generated.beans");
		soapRequest.setSoapUserName("");
		soapRequest.setSoapPwd("");
		soapRequest.setConnectionTimeout(600000);
		soapRequest.setReadTimeout(600000);
		RunJobReturn result = genericWebserviceClient.doSoapCallForObject(soapRequest, RunJobReturn.class);
		String response = "INITIAL";
		boolean migrationResponse = true;
		if (!CollectionUtils.isEmpty(result.getItem()) && !CollectionUtils.isEmpty(result.getItem().get(0).getItem())) {
			String xml = result.getItem().get(0).getItem().get(0);
			JSONObject obj = XML.toJSONObject(xml);
			try {
				response = migrateILL(obj,serviceId,scOrder,scServiceDetail,isTermination);
				migrationStatus.setServiceCode(serviceId);
				migrationStatus.setResponse(response);
				migrationStatus.setInstanceId(serviceInstId);
				migrationStatus.setServiceType("ILL");
				migrationStatusRepository.saveAndFlush(migrationStatus);
				if(!response.equalsIgnoreCase("SUCCESS")) {
					migrationResponse=false;
				}
			} catch (Exception e) {
				LOGGER.error("Error in ILL Migration {}", e);
				migrationResponse=false;
				migrationStatus.setServiceCode(serviceId);
				migrationStatus.setResponse(e.toString());
				migrationStatus.setInstanceId(serviceInstId);
				migrationStatus.setServiceType("ILL");
				migrationStatusRepository.saveAndFlush(migrationStatus);

			}

//			System.out.println(response + " " + i);
		}
		else {
			migrationResponse=false;
		}
		// pr.println(i + "th record " + response);
		LOGGER.info("In ILL migration - {}th ILL record for serviceId {} with response {}", i, serviceId, response);
		try {
			if (response.equals("INITIAL")) {
				notificationService.notifyOptimusO2CAutoMigrationFailure(null, serviceId,
						"ERROR in ILL Migration : response=\"INITIAL\"");
			}
		} catch (Exception e1) {
			LOGGER.error("Auto ILL migration Email sending failure for service code {} with error {}:", serviceId, e1);
		}
		return migrationResponse;
	}

	@Transactional
	public String reMigrateAll() throws Exception {

		try {
			Integer i = 0;
			Boolean doMigration = true;
			List<MigrationStatus> listIll = migrationStatusRepository
					.findByServiceTypeAndResponseContainingOrderByIdDesc("ILL", "ERROR -> ");
			for (MigrationStatus msObj : listIll) {
				List<MigrationStatus> listSc = migrationStatusRepository
						.findByServiceCodeOrderByIdDesc(msObj.getServiceCode());
				for (MigrationStatus scObj : listSc) {
					if (scObj.getResponse() != null && !scObj.getResponse().isEmpty()
							&& scObj.getResponse().toLowerCase().equals("success")) {
						doMigration = false;
						break;
					}
				}
				if (doMigration) {
					i++;
					runMigration(i, msObj.getServiceCode(), msObj.getInstanceId(),null,null,null);

				}
			}

		} catch (Exception e) {

			LOGGER.error("error in reMigrateILL upload : {}", e);
		}
		return "SUCCESS";
	}
	
	@Transactional
	public String migrateSpecInst(String serviceId,String serviceInstId) throws Exception {

		try {
			Integer i = 0;
			Boolean doMigration = true;
			/*for(Map.Entry<String,String> entry : serviceIds.entrySet()) {
				runMigration(i, entry.getKey(), entry.getValue());
			}*/
			runMigration(i, serviceId, serviceInstId,null,null,null);			

		} catch (Exception e) {

			LOGGER.error("error in Migrate ILL Specific upload : {}", e);
		}
		return "SUCCESS";
	}
	
	@Transactional
	public boolean excelUploadWi(MultipartFile file) throws Exception {

		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
					
			for (Sheet sheet : workbook) {
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {

					Cell serviceId = sheet.getRow(i).getCell(0);
					serviceId.setCellType(CellType.STRING);
					runMigrationWi(i,serviceId.getStringCellValue(),null,null,null);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in excel upload Without Instance : {}" , e);
		}
		return true;
	}
	
	@Transactional
	public boolean runMigrationWi(int i, String serviceId, Integer scServiceDetailsId, Integer scOrderId, String isTermination) throws Exception {
		com.tcl.dias.serviceactivation.datamigration.illinstance.beans.Args args = new com.tcl.dias.serviceactivation.datamigration.illinstance.beans.Args();
		List<String> arguments = args.getItem();
		arguments.add("--context_param componentType=Port");
		arguments.add("--context_param searchLevel=ORDERS");		
		arguments.add("--context_param serviceId=" + serviceId);
		arguments.add("--context_param dataModel=LIVE");
		SoapRequest soapRequest = new SoapRequest();
		soapRequest.
			setUrl("http://115.110.93.236:8180/L1_SearchDataEE_1.0/services/L1_SearchDataEE?wsdl");
		soapRequest.setRequestObject(args);
		soapRequest.setContextPath("com.tcl.dias.serviceactivation.datamigration.illinstance.beans");
		soapRequest.setSoapUserName("");
		soapRequest.setSoapPwd("");
		soapRequest.setConnectionTimeout(600000);
		soapRequest.setReadTimeout(600000);
		com.tcl.dias.serviceactivation.datamigration.illinstance.beans.RunJobReturn result;
		result=genericWebserviceClient.doSoapCallForObject(soapRequest, com.tcl.dias.serviceactivation.datamigration.illinstance.beans.RunJobReturn.class);
		String response = "INITIAL";
		boolean migrationResponse = true;
		
		if (result.getItem().get(0).getItem().toString().contains("Failure")
				|| result.getItem().get(0).getItem().toString().contains("Error")) {
			Thread.sleep(60000);
			result = genericWebserviceClient.doSoapCallForObject(soapRequest, com.tcl.dias.serviceactivation.datamigration.illinstance.beans.RunJobReturn.class);
		}
		
		LOGGER.info("In ILL migration - ILL Without Instance record for serviceId {} trying to fetch instance...",serviceId);

		if (!CollectionUtils.isEmpty(result.getItem())
				&& !CollectionUtils.isEmpty(result.getItem().get(0).getItem())) {
			try {
				String xml = result.getItem().get(0).getItem().get(0);
				JSONObject obj = XML.toJSONObject(xml);
				String instanceId = null;
				if (obj.has("Order")) {
					if (Integer.valueOf(obj.getJSONObject("Order").get("totalRecords").toString()) == 1) {
						instanceId = extractInstance(obj.getJSONObject("Order").getJSONObject("OrderInfo"), null, Integer.valueOf(obj.getJSONObject("Order").get("totalRecords").toString()
							));
					} else if (Integer.valueOf(obj.getJSONObject("Order").get("totalRecords").toString()) > 1 && obj.getJSONObject("Order").get("OrderInfo") instanceof JSONArray) {
						instanceId = extractInstance(null, obj.getJSONObject("Order").getJSONArray("OrderInfo"), Integer.valueOf(obj.getJSONObject("Order").get("totalRecords").toString()));
					} else if (Integer.valueOf(obj.getJSONObject("Order").get("totalRecords").toString()) <= 0) {
						throw new Exception("total Records <= 0");
					}
				} else {
					throw new Exception("Order not available");
				}

				if(instanceId==null || instanceId.equals("No CLOSED ServiceInstanceID found!!!")) {
					throw new Exception("No CLOSED ServiceInstanceID found!!!");
				}
				LOGGER.info("InstanceId found as: {} ",instanceId);
				
				if(isTermination!=null && isTermination.equalsIgnoreCase("yes")) {
					
					ScServiceDetail scServiceDetail=scServiceDetailRepository.findById(scServiceDetailsId).get();
					ScOrder scOrder=scOrderRepository.findById(scOrderId).get();
					migrationResponse= runMigration(i, serviceId, instanceId,isTermination,scServiceDetail,scOrder);

				}
				else {
					migrationResponse= runMigration(i, serviceId, instanceId,null,null,null);

				}

			} catch (Exception e) {
				LOGGER.error("Error in ILL Migration Without Instance {}", e);
				migrationResponse = false;
			}
		}
		else {
			migrationResponse=false;
		}
		return migrationResponse;
		//LOGGER.info("In ILL migration - {}th ILL Without Instance record for serviceId {} with response {}", i, serviceId, response);
	}

	private String extractInstance(JSONObject jsonObjOrder, JSONArray jsonArrayOrder, Integer count) throws TclCommonException {
		String returnString="No CLOSED ServiceInstanceID found!!!";
		
		if(jsonObjOrder!=null) {
			//JSONObject Order=(JSONObject) jsonObjOrder;
			if(jsonObjOrder.get("orderStatus").toString().toLowerCase().contains("closed")) {
				return jsonObjOrder.get("ServiceInstanceID").toString();
			}
			return returnString;
		}
		if(jsonArrayOrder!=null) {
			//Order orderbean = Utils.convertJsonToObject(jsonArrayOrder.toString(),Order.class);
			//JSONArray Orders = (JSONArray) jsonArrayOrder;
			for(int i=count-1;i>=0;i--) {
				JSONObject order = (JSONObject) jsonArrayOrder.get(i);
				if(order.get("orderStatus").toString().toLowerCase().contains("closed")) {
					return order.get("ServiceInstanceID").toString();
				}
			}
			return returnString;
		}
		return returnString;
	}
}