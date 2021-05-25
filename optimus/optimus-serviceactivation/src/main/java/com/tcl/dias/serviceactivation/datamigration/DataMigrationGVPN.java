package com.tcl.dias.serviceactivation.datamigration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import com.tcl.dias.serviceactivation.datamigration.gvpn.beans.Args;
import com.tcl.dias.serviceactivation.datamigration.gvpn.beans.RunJobReturn;
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
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
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
import com.tcl.dias.serviceactivation.entity.entities.VpnMetatData;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.repository.AclPolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.AluSammgrSeqRepository;
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
import com.tcl.dias.serviceactivation.entity.repository.MulticastingRepository;
import com.tcl.dias.serviceactivation.entity.repository.NeighbourCommunityConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.OspfRepository;
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
import com.tcl.dias.serviceactivation.entity.repository.VpnMetatDataRepository;
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

/**
 * This class has methods for data migration from system xmls to
 * service_activation database
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class DataMigrationGVPN {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataMigrationGVPN.class);

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
	private VpnMetatDataRepository vpnMetatDataRepository;
	@Autowired
	private OspfRepository ospfRepository;
	@Autowired
	private AluSammgrSeqRepository aluSammgrSeqRepository;
	@Autowired
	private MigrationStatusRepository migrationStatusRepository;
	@Autowired
	private MulticastingRepository multicastingRepository;
	
	@Autowired
	NotificationService notificationService;
	

	@Autowired
	private ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	private ScOrderRepository scOrderRepository;
	/**
	 * Migration method for IAS-ILL xml data to service_activation database
	 * 
	 * @author diksha garg
	 * @param order
	 * @return
	 * @throws Exception
	 */

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

	@Transactional
	public String migrateGVPN(JSONObject order, String serviceId, String isTermination, ScServiceDetail scServiceDetail, ScOrder scOrder) throws Exception {

		Integer serviceDetailVersion = null;

		try {

			if (isTermination==null && serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId, "ISSUED") != null) {
				return "ISSUED RECORD EXISTS!";
			}

			JSONObject gvpn = (JSONObject) order.getJSONObject("MDM_GVPN_Data").get("GVPNService");

			// Error scenarios
			if (gvpn.getJSONObject("IPService").has("ExtendedLAN")
					&& gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").has("isEnabled")
					&& gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").get("isEnabled").toString()
							.toLowerCase().equals("false")) {
				if (!gvpn.getJSONObject("IPService").has("WANv4IPAddress")) {
					throw new Exception("Extended Lan is false but Wan v4 details  missing.");
				}
			}

			if (!gvpn.getJSONObject("IPService").has("Router")) {
				throw new Exception("PE Router Details missing.");
			}

			if (!gvpn.getJSONObject("IPService").has("RoutingProtocol")
					|| !gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").has("routingProtocol")) {
				throw new Exception("Protocol Details missing.");
			}

			if (!gvpn.getJSONObject("IPService").has("Router")
					|| !gvpn.getJSONObject("IPService").getJSONObject("Router").has("WANInterface")) {
				throw new Exception("PE Router Interface Details missing.");
			}

			if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").get("routingProtocol").toString()
					.toLowerCase().equals("eigrp")
					|| gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").get("routingProtocol")
							.toString().toLowerCase().equals("rip")) {
				throw new Exception("Protocol Details wrong.");
			}

			OrderDetail orderDetail = null;
			JSONObject orderInfo = null;

			orderInfo = (JSONObject) order.getJSONObject("MDM_GVPN_Data").get("OrderInfo");
			IPServiceEndDateBean ipServiceEndDateBean = new IPServiceEndDateBean();
			ipServiceEndDateBean.setCurrentDate(true);
			ipServiceEndDateBean.setModifiedBy("Optimus_Port_GVPN_migration");
			ipServiceEndDateBean.setServiceId(serviceId);
			serviceDetailVersion = endServiceConfigRecords(ipServiceEndDateBean);

			if (order.getJSONObject("MDM_GVPN_Data").has("OrderInfo")) {
				orderDetail = saveOrderDetails(gvpn, orderInfo);
			} else {
				orderDetail = new OrderDetail();
				orderDetail.setOrderStatus("CLOSED");
				orderDetail.setModifiedBy("Optimus_Port_GVPN_migration");
				orderDetail.setStartDate(new Timestamp(new Date().getTime()));
				orderDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
				orderDetailRepository.saveAndFlush(orderDetail);
			}
			if(isTermination!=null && isTermination.equalsIgnoreCase("yes")) {
				orderDetail.setOrderUuid(scOrder.getOpOrderCode());
				orderDetailRepository.saveAndFlush(orderDetail);
			}

			

			ServiceDetail serviceDetail1 = null;
			serviceDetail1 = saveServiceDetails(gvpn, orderInfo, orderDetail, serviceDetailVersion);
			if(isTermination!=null && isTermination.equalsIgnoreCase("yes")) {
				serviceDetail1.setScServiceDetailId(scServiceDetail.getId());
				serviceDetail1.setOrderType(scOrder.getParentOrderType());
				serviceDetail1.setServiceState("ACTIVE");
				serviceDetailRepository.saveAndFlush(serviceDetail1);
			}

			/*
			 * AluSammgrSeq aluSammgrSeq = null; if
			 * (gvpn.getJSONObject("IPService").has("CSSSAMManagerId") &&
			 * gvpn.getJSONObject("IPService")
			 * .getJSONObject("Router").get("routerMake").toString().equals("ALCATEL IP")) {
			 * aluSammgrSeq = new AluSammgrSeq(); aluSammgrSeq.setSammgrSeqid((Integer)
			 * gvpn.getJSONObject("IPService").get("CSSSAMManagerId"));
			 * aluSammgrSeq.setServiceId(serviceId);
			 * aluSammgrSeq.setServiceType(gvpn.get("serviceType").toString());
			 * aluSammgrSeqRepository.saveAndFlush(aluSammgrSeq); }
			 */
			ServiceQo serviceQo1 = null;
			serviceQo1 = saveServiceQo(gvpn, serviceDetail1);

			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("COS")
					&& gvpn.getJSONObject("IPService").getJSONObject("QoS").get("COS") instanceof JSONArray) {
				JSONArray CoSArray = gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONArray("COS");
				// ILL has one record, GVPN has 6 records always

				for (Object CoS : CoSArray) {
					JSONObject cos = (JSONObject) CoS;
					if (cos.has("CoSName") && cos.has("percentage") && !(cos.get("percentage") instanceof JSONArray)
							&& Integer.valueOf(cos.get("percentage").toString()) > 0) {
						
						ServiceCosCriteria serviceCosCriteria = setCosCriteria(cos, serviceQo1);
						serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);

						String acl_policy_name=null; //addition
						
						if (cos.get("ClassificationCriteria") instanceof JSONObject
								&& cos.getJSONObject("ClassificationCriteria").has("classificationCriteriaList")
								&& cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList")
										.toString().toLowerCase().contains("address")
								&& cos.getJSONObject("ClassificationCriteria")
										.getJSONObject("qosACL") instanceof JSONObject) {

							// ACLEntry
							if (cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL").has("ACLEntry")
									&& cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL")
											.get("ACLEntry") instanceof JSONArray) {
								
								if (cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL").has("name")) {
									acl_policy_name = cos.getJSONObject("ClassificationCriteria")
											.getJSONObject("qosACL").get("name").toString();
								}		
								
								JSONArray ACLEntry = cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL")
										.getJSONArray("ACLEntry");
								saveCosAclEntryIpAddress(acl_policy_name,ACLEntry, null, serviceCosCriteria);

							}
							if (cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL").has("ACLEntry")
									&& cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL")
											.get("ACLEntry") instanceof JSONObject) {

								JSONObject ACLEntry = cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL")
										.getJSONObject("ACLEntry");
								saveCosAclEntryIpAddress(acl_policy_name,null, ACLEntry, serviceCosCriteria);

							}

						}
					}
				}
			}
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("COS")
					&& gvpn.getJSONObject("IPService").getJSONObject("QoS").get("COS") instanceof JSONObject) {
				JSONObject cos = (JSONObject) gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("COS");
				
				String acl_policy_name=null; //addition
				
				
				if (cos.has("CoSName")) {
					ServiceCosCriteria serviceCosCriteria = setCosCriteria(cos, serviceQo1);
					serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);

					if (cos.get("ClassificationCriteria") instanceof JSONObject
							&& cos.getJSONObject("ClassificationCriteria").has("classificationCriteriaList")
							&& cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList").toString()
									.toLowerCase().contains("ip_address")
							&& cos.getJSONObject("ClassificationCriteria").getJSONObject("classificationCriteriaList")
									.get("qosACL") instanceof JSONObject) {

						if (cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL").has("name")) {
							acl_policy_name = cos.getJSONObject("ClassificationCriteria")
									.getJSONObject("qosACL").get("name").toString();
						}
						
						// ACLEntry
						if (cos.getJSONObject("ClassificationCriteria").getJSONObject("classificationCriteriaList")
								.getJSONObject("qosACL").get("ACLEntry") instanceof JSONArray) {

							JSONArray ACLEntry = cos.getJSONObject("ClassificationCriteria")
									.getJSONObject("classificationCriteriaList").getJSONObject("qosACL")
									.getJSONArray("ACLEntry");
							saveCosAclEntryIpAddress(acl_policy_name,ACLEntry, null, serviceCosCriteria);

						}
						if (cos.getJSONObject("ClassificationCriteria").getJSONObject("classificationCriteriaList")
								.getJSONObject("qosACL").get("ACLEntry") instanceof JSONObject) {

							JSONObject ACLEntry = cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL")
									.getJSONObject("ACLEntry");
							saveCosAclEntryIpAddress(acl_policy_name,null, ACLEntry, serviceCosCriteria);

						}

					}
				}
			}

			// ALU SCheduler policy
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("ALUSchedulerPolicy")
					&& gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerMake").toString()
							.equals("ALCATEL IP")) {
				if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("ALUSchedulerPolicy")
						.has("Name")) {
					saveAluSchedulerPolicy(gvpn, serviceDetail1);
				}
			}

			if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("topologyInfo")) {
				// topology
				Topology topology = null;
				topology = saveTopology(gvpn, serviceDetail1);

				// uplink
				if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("topologyInterface1")) {
					saveRouterUplinkPort(gvpn.getJSONObject("IPService").getJSONObject("Router"), topology);
				}

				// uniswitch
				if (gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("topologyInfo")
						.has("uniSwitch")
						&& gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("topologyInfo")
								.get("uniSwitch") instanceof JSONObject
						&& gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("topologyInfo")
								.getJSONObject("uniSwitch").has("uniInterface")
						&& gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("topologyInfo")
								.getJSONObject("uniSwitch").getJSONObject("uniInterface").has("interfaceName")) {
					JSONObject uniswitchObj = gvpn.getJSONObject("IPService").getJSONObject("Router")
							.getJSONObject("topologyInfo").getJSONObject("uniSwitch");
					saveUniswitchDetails(uniswitchObj, topology);
				}
			}

			// ipaddress
			IpAddressDetail ipAddressDetail = saveIpAddressDetail(gvpn, serviceDetail1);

			// lanv4
			String cpe_loopback=null; //enhancement
			
			// cpeMgmtLoopback
			if (order.getJSONObject("MDM_GVPN_Data").has("CPE")
					&& order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE").has("managementIPv4LoopbackInterface")
					&& order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE")
							.get("managementIPv4LoopbackInterface") instanceof JSONObject
					&& order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE")
							.getJSONObject("managementIPv4LoopbackInterface").has("IPv4Address")
					&& order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE")
							.getJSONObject("managementIPv4LoopbackInterface").get("IPv4Address") instanceof JSONObject
					&& order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE")
							.getJSONObject("managementIPv4LoopbackInterface").getJSONObject("IPv4Address")
							.has("address")) {
				try {
					cpe_loopback = order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE")
							.getJSONObject("managementIPv4LoopbackInterface").getJSONObject("IPv4Address")
							.get("address").toString();
				} catch (Exception e) {
					LOGGER.error("error in cpe_loopback value extraction code GVPN for service id {} as {} : ", serviceId,
							e);
				}

			}

			
			if (gvpn.getJSONObject("IPService").has("LANIPv4Address")) {
				Object lanv4 = gvpn.getJSONObject("IPService").get("LANIPv4Address");
				if (lanv4 instanceof JSONObject) {
					JSONObject lanv4Object = (JSONObject) lanv4;
					
					if (lanv4Object.has("LANIPAddress") && lanv4Object.getJSONObject("LANIPAddress").has("address")) {
						
						String lanv4_subnet=lanv4Object.getJSONObject("LANIPAddress").get("address").toString();
						String[] lanv4_wo_subnet=lanv4_subnet.split("/");
						
						if(cpe_loopback==null || (cpe_loopback!=null && !cpe_loopback.isEmpty() && !cpe_loopback.contains(lanv4_wo_subnet[0]))) {
						IpaddrLanv4Address ipaddrLanv4Address = setlanv4(lanv4Object.getJSONObject("LANIPAddress"),
								ipAddressDetail);
						Set<IpaddrLanv4Address> ipaddrLanv4Addresses = new HashSet<>();
						ipaddrLanv4Addresses.add(ipaddrLanv4Address);
						ipAddressDetail.setIpaddrLanv4Addresses(ipaddrLanv4Addresses);
						}
					}

				} else if (lanv4 instanceof JSONArray) {

					JSONArray lanv4Array = (JSONArray) lanv4;
					Set<IpaddrLanv4Address> ipaddrLanv4Addresses = new HashSet<>();

					for (int i = 0; i < lanv4Array.length(); i++) {
						JSONObject lanv4Object = (JSONObject) lanv4Array.get(i);
						if (lanv4Object.has("LANIPAddress")
								&& lanv4Object.getJSONObject("LANIPAddress").has("address")) {
							
							String lanv4_subnet=lanv4Object.getJSONObject("LANIPAddress").get("address").toString();
							String[] lanv4_wo_subnet=lanv4_subnet.split("/");
							if(cpe_loopback==null || (cpe_loopback!=null && !cpe_loopback.isEmpty() && !cpe_loopback.contains(lanv4_wo_subnet[0]))) {
							IpaddrLanv4Address ipaddrLanv4Address = setlanv4(lanv4Object.getJSONObject("LANIPAddress"),
									ipAddressDetail);
							ipaddrLanv4Addresses.add(ipaddrLanv4Address);
							}
						}
					}
					ipAddressDetail.setIpaddrLanv4Addresses(ipaddrLanv4Addresses);

				}
			}
			// Wanv4
			if (gvpn.getJSONObject("IPService").has("WANv4IPAddress")) {
				Object wanv4 = gvpn.getJSONObject("IPService").get("WANv4IPAddress");

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
			if (gvpn.getJSONObject("IPService").has("wanV6IPAddress")) {
				Object wanv6 = gvpn.getJSONObject("IPService").get("wanV6IPAddress");

				if (wanv6 instanceof JSONObject) {

					JSONObject wanv6Object = (JSONObject) wanv6;
					if (wanv6Object.has("address")) {
						IpaddrWanv6Address ipaddrwanv6Address = setwanv6(wanv6Object, ipAddressDetail);
						Set<IpaddrWanv6Address> ipaddrWanv6Addresses = new HashSet<>();
						ipaddrWanv6Addresses.add(ipaddrwanv6Address);
						ipAddressDetail.setIpaddrWanv6Addresses(ipaddrWanv6Addresses);
					}

				} else if (wanv6 instanceof JSONArray) {

					JSONArray wanv6Array = (JSONArray) wanv6;
					Set<IpaddrWanv6Address> ipaddrWanv6Addresses = new HashSet<>();
					for (int i = 0; i < wanv6Array.length(); i++) {

						JSONObject wanv6Object = (JSONObject) wanv6Array.get(i);
						if (wanv6Object.has("address")) {

							IpaddrWanv6Address ipaddrWanv6Address = setwanv6(wanv6Object, ipAddressDetail);
							ipaddrWanv6Addresses.add(ipaddrWanv6Address);
						}

					}
					ipAddressDetail.setIpaddrWanv6Addresses(ipaddrWanv6Addresses);

				}
			}

			// lanv6
			if (gvpn.getJSONObject("IPService").has("LANIPv6Address")) {
				Object lanv6 = gvpn.getJSONObject("IPService").get("LANIPv6Address");

				if (lanv6 instanceof JSONObject) {

					JSONObject lanv6Object = (JSONObject) lanv6;
					if (lanv6Object.has("LANIPAddress") && lanv6Object.getJSONObject("LANIPAddress").has("address")) {
						IpaddrLanv6Address ipaddrLanv6Address = setlanv6(lanv6Object.getJSONObject("LANIPAddress"),
								ipAddressDetail);
						Set<IpaddrLanv6Address> ipaddrLanv6Addresses = new HashSet<>();
						ipaddrLanv6Addresses.add(ipaddrLanv6Address);
						ipAddressDetail.setIpaddrLanv6Addresses(ipaddrLanv6Addresses);
					}

				} else if (lanv6 instanceof JSONArray) {

					JSONArray lanv6Array = (JSONArray) lanv6;
					Set<IpaddrLanv6Address> ipaddrLanv6Addresses = new HashSet<>();

					for (int i = 0; i < lanv6Array.length(); i++) {

						JSONObject lanv6Object = (JSONObject) lanv6Array.get(i);
						if (lanv6Object.has("LANIPAddress")
								&& lanv6Object.getJSONObject("LANIPAddress").has("address")) {
							IpaddrLanv6Address ipaddrLanv6Address = setlanv6(lanv6Object.getJSONObject("LANIPAddress"),
									ipAddressDetail);
							ipaddrLanv6Addresses.add(ipaddrLanv6Address);
						}

					}

					ipAddressDetail.setIpaddrLanv6Addresses(ipaddrLanv6Addresses);

				}
			}

			// router
			RouterDetail routerDetail = saveRouterDetail(gvpn);

			// ethernet interface
			EthernetInterface ethernetInterface = null;
			ChannelizedE1serialInterface channelizedE1serialInterface = null;
			ChannelizedSdhInterface channelizedSdhInterface = null;

			JSONObject interfaceObj = gvpn.getJSONObject("IPService").getJSONObject("Router")
					.getJSONObject("WANInterface");
			Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();

			if (interfaceObj.get("WANInterface").toString().toLowerCase().contains("ethernet")) {

				ethernetInterface = saveEthernetInterface(gvpn, interfaceObj);

				// ACL POLICY CRITERIA
				Set<String> keySet = interfaceObj.keySet();
				for (String key : keySet) {
					Boolean keyNeeded = false;
					if (key.contains("oundAccessControlList"))
						keyNeeded = true;
					if (keyNeeded) {
						JSONObject aclObject = interfaceObj.getJSONObject(key);
						// System.out.println(key);
						if (aclObject.get("isEnabled").toString().equalsIgnoreCase("true")) {
							AclPolicyCriteria aclPolicyCriteria = setAclPolicyCriteria(key, aclObject,
									ethernetInterface, channelizedE1serialInterface, channelizedSdhInterface,
									"ethernet");
							aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							aclPolicyCriterias.add(aclPolicyCriteria);
						}
					}

				}
			}

			if (interfaceObj.get("WANInterface").toString().toLowerCase().contains("serial")) {

				channelizedE1serialInterface = saveChannelizedE1SerialDetails(gvpn, interfaceObj);

				// ACL POLICY CRITERIA
				Set<String> keySet = interfaceObj.keySet();
				for (String key : keySet) {
					Boolean keyNeeded = false;
					if (key.contains("oundAccessControlList"))
						keyNeeded = true;
					if (keyNeeded) {
						JSONObject aclObject = interfaceObj.getJSONObject(key);
						// System.out.println(key);
						if (aclObject.get("isEnabled").toString().equalsIgnoreCase("true")) {
							AclPolicyCriteria aclPolicyCriteria = setAclPolicyCriteria(key, aclObject,
									ethernetInterface, channelizedE1serialInterface, channelizedSdhInterface, "serial");
							aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							aclPolicyCriterias.add(aclPolicyCriteria);
						}
					}

				}
			}

			if (interfaceObj.get("WANInterface").toString().toLowerCase().contains("sdh")) {

				channelizedSdhInterface = saveChannelizedSdhInterface(gvpn, interfaceObj);
				// ACL POLICY CRITERIA
				Set<String> keySet = interfaceObj.keySet();
				for (String key : keySet) {
					Boolean keyNeeded = false;
					if (key.contains("oundAccessControlList"))
						keyNeeded = true;
					if (keyNeeded) {
						JSONObject aclObject = interfaceObj.getJSONObject(key);
						// System.out.println(key);
						if (aclObject.get("isEnabled").toString().equalsIgnoreCase("true")) {
							AclPolicyCriteria aclPolicyCriteria = setAclPolicyCriteria(key, aclObject,
									ethernetInterface, channelizedE1serialInterface, channelizedSdhInterface, "sdh");
							aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							aclPolicyCriterias.add(aclPolicyCriteria);
						}
					}

				}
			}

			// CPE - ETHERNET
			EthernetInterface ethernetInterface2 = null;
			Cpe cpe = null;

			if (order.getJSONObject("MDM_GVPN_Data").has("CPE")) {

				// cpeWanInterface
				JSONObject cpewanInterface = order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE")
						.getJSONObject("WANInterface");
				if (cpewanInterface.has("WANInterface")
						&& cpewanInterface.get("WANInterface").toString().toLowerCase().contains("ethernet")) {
					ethernetInterface2 = saveEthernetCpe(cpewanInterface);

					// qos login passthrough comes for ethernet for cisco
					// cpe
					cpe = saveCpe(order.getJSONObject("MDM_GVPN_Data").getJSONObject("CPE"),
							serviceDetail1.getServiceId());
				}
			}

			// vrf
			Vrf vrf = null;
			vrf = saveVrf(gvpn, serviceDetail1);

			StaticProtocol staticProtocol = null;
			Bgp bgp = null;
			Ospf ospf = null;
			JSONObject wanRoutingProtocol;

			// static protocol
			if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").get("routingProtocol").toString()
					.toLowerCase().equals("static")) {
				// static
				wanRoutingProtocol = gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol");
				staticProtocol = saveStaticProtocol(wanRoutingProtocol);

				// WAN STATIC ROUTES
				// ispewan=yes
				if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").has("StaticRoutingProtocol")
						&& gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
								.getJSONObject("StaticRoutingProtocol").has("PEWANStaticRoutes")) {
					if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
							.getJSONObject("StaticRoutingProtocol").get("PEWANStaticRoutes") instanceof JSONObject) {

						JSONObject peWanObj = (JSONObject) gvpn.getJSONObject("IPService")
								.getJSONObject("RoutingProtocol").getJSONObject("StaticRoutingProtocol")
								.get("PEWANStaticRoutes");
						saveStaticWanPe(peWanObj, staticProtocol);

					} else if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
							.getJSONObject("StaticRoutingProtocol").get("PEWANStaticRoutes") instanceof JSONArray) {
						JSONArray peWanArray = (JSONArray) gvpn.getJSONObject("IPService")
								.getJSONObject("RoutingProtocol").getJSONObject("StaticRoutingProtocol")
								.get("PEWANStaticRoutes");
						List<StaticProtocol> list = new ArrayList<>();
						list.add(staticProtocol);
						peWanArray.forEach(peWanObj -> saveStaticWanPe((JSONObject) peWanObj, list.get(0)));

					}

				}

				// is cewan
				if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").has("StaticRoutingProtocol")
						&& gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
								.getJSONObject("StaticRoutingProtocol").has("CEWANStaticRoutes")) {

					if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
							.getJSONObject("StaticRoutingProtocol").get("CEWANStaticRoutes") instanceof JSONObject) {

						JSONObject ceWanObj = (JSONObject) gvpn.getJSONObject("IPService")
								.getJSONObject("RoutingProtocol").getJSONObject("StaticRoutingProtocol")
								.get("CEWANStaticRoutes");
						saveStaticWanCe(ceWanObj, staticProtocol);
					} else if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
							.getJSONObject("StaticRoutingProtocol").get("CEWANStaticRoutes") instanceof JSONArray) {
						JSONArray ceWanArray = (JSONArray) gvpn.getJSONObject("IPService")
								.getJSONObject("RoutingProtocol").getJSONObject("StaticRoutingProtocol")
								.get("CEWANStaticRoutes");
						List<StaticProtocol> list = new ArrayList<>();
						list.add(staticProtocol);
						ceWanArray.forEach(ceWanObj -> saveStaticWanCe((JSONObject) ceWanObj, list.get(0)));

					}

				}
			}

			if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").get("routingProtocol").toString()
					.toLowerCase().contains("bgp")) {

				// BGP
				wanRoutingProtocol = gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
						.getJSONObject("BGPRoutingProtocol");
				Set<String> keys = wanRoutingProtocol.keySet();

				bgp = saveBgp(wanRoutingProtocol, gvpn.getJSONObject("IPService").getJSONObject("Router")
						.get("routerMake").toString().equals("CISCO IP") ? true : false);

				for (String key : keys) {
					Boolean keyNeeded = key.toLowerCase().contains("routingpolicy");
					if (keyNeeded && wanRoutingProtocol.get(key) instanceof JSONObject) {
						JSONObject policyObject = wanRoutingProtocol.getJSONObject(key);
						// System.out.println(key);
						if (policyObject.get("enable").toString().toLowerCase().equals("true")
								&& policyObject.has("name"))
							setPolicyTypeAndCriterias(key, policyObject, bgp);
					} else if (keyNeeded && !(wanRoutingProtocol.get(key) instanceof JSONObject)) {
						if (!wanRoutingProtocol.get(key).toString().equals("null")) {
							String policy = wanRoutingProtocol.get(key).toString();
							setPolicyName(policy, key, ospf, bgp);
						}
					}
				}
			}

			if (gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol").get("routingProtocol").toString()
					.toLowerCase().equals("ospf")) {
				ospf = saveOspf(gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
						.getJSONObject("OSPFRoutingProtocol"));

				wanRoutingProtocol = gvpn.getJSONObject("IPService").getJSONObject("RoutingProtocol")
						.getJSONObject("OSPFRoutingProtocol");
				Set<String> keys = wanRoutingProtocol.keySet();

				for (String key : keys) {
					Boolean keyNeeded = key.toLowerCase().contains("routingpolicy");
					if (keyNeeded && wanRoutingProtocol.get(key) instanceof JSONObject) {
						JSONObject policyObject = wanRoutingProtocol.getJSONObject(key);
						// System.out.println(key);
						if (policyObject.get("enable").toString().toLowerCase().equals("true")
								&& policyObject.has("name"))
							setPolicyTypeOspf(key, policyObject, ospf);
					} else if (keyNeeded && !(wanRoutingProtocol.get(key) instanceof JSONObject)) {
						if (!wanRoutingProtocol.get(key).toString().equals("null")) {
							String policy = wanRoutingProtocol.get(key).toString();
							setPolicyName(policy, key, ospf, bgp);
						}
					}
				}
			}

			// interface protocol mapping for PE
			saveIpmPe(interfaceObj.get("WANInterface").toString().toLowerCase(), routerDetail, serviceDetail1,
					staticProtocol, bgp, ospf, ethernetInterface, channelizedSdhInterface,
					channelizedE1serialInterface);

			// interface protocol mapping -CPE
			if (order.getJSONObject("MDM_GVPN_Data").has("CPE")) {
			    LOGGER.info("CPE exists for serviceId::{}",serviceDetail1.getId());
				saveIpmCpe(interfaceObj.get("WANInterface").toString().toLowerCase(), cpe, serviceDetail1,
						ethernetInterface2, channelizedSdhInterface, channelizedE1serialInterface);
			}

			if (gvpn.has("ALUVPRNImportPolicy")) {
				JSONObject vprnPolicyObj = gvpn.getJSONObject("ALUVPRNImportPolicy");
				saveVprnImportPolicy(vprnPolicyObj, vrf);
			}

			if (gvpn.has("ALUVPRNExportPolicy")) {
				JSONObject vprnPolicyObj = gvpn.getJSONObject("ALUVPRNExportPolicy");
				saveVprnExportPolicy(vprnPolicyObj, vrf);

			}

			// VPNSOLUTION
			// Multiple and variable in GVPN, single in ILL
			if (gvpn.has("VPNSolutionTableDetails")) {
				if (gvpn.getJSONObject("VPNSolutionTableDetails").has("VPN")
						&& gvpn.getJSONObject("VPNSolutionTableDetails").get("VPN") instanceof JSONObject) {
					String vpnSolName = gvpn.getJSONObject("VPNSolutionTableDetails").get("solutionID").toString();
					JSONObject vpnObj = (JSONObject) gvpn.getJSONObject("VPNSolutionTableDetails").get("VPN");
					saveVpnSolution(vpnObj, serviceDetail1, vpnSolName);
				} else if (gvpn.getJSONObject("VPNSolutionTableDetails").has("VPN")
						&& gvpn.getJSONObject("VPNSolutionTableDetails").get("VPN") instanceof JSONArray) {
					String vpnSolName = gvpn.getJSONObject("VPNSolutionTableDetails").get("solutionID").toString();
					JSONArray vpnArray = (JSONArray) gvpn.getJSONObject("VPNSolutionTableDetails").get("VPN");
					List<ServiceDetail> list = new ArrayList<>();
					list.add(serviceDetail1);
					vpnArray.forEach(vpnObj -> saveVpnSolution((JSONObject) vpnObj, list.get(0), vpnSolName));
				} else if (!gvpn.getJSONObject("VPNSolutionTableDetails").has("VPN")
						&& gvpn.getJSONObject("VPNSolutionTableDetails").has("solutionID")) {
					String vpnSolName = gvpn.getJSONObject("VPNSolutionTableDetails").get("solutionID").toString();
					saveVpnSolution(null, serviceDetail1, vpnSolName);
				}
			}

			if (gvpn.has("vpnSolutionDetails")) {
				LOGGER.info("VpnSolution exists for serviceId::{}",serviceDetail1.getId());
				saveVpnMetaData(gvpn.getJSONObject("vpnSolutionDetails"), serviceDetail1);
			}

			if (order.getJSONObject("MDM_GVPN_Data").has("AddOnServiceDetails")) {
				JSONObject addOn = order.getJSONObject("MDM_GVPN_Data").getJSONObject("AddOnServiceDetails");
				if (addOn.has("Multicasting")) {
					Multicasting multicasting = new Multicasting();
					JSONObject multicast = addOn.getJSONObject("Multicasting");
					if (multicast.has("wanPIMMode"))
						multicasting.setWanPimMode(multicast.get("wanPIMMode").toString());
					if (multicast.has("type"))
						multicasting.setType(multicast.get("type").toString());
					if (multicast.has("defaultMDT"))
						multicasting.setDefaultMdt(multicast.get("defaultMDT").toString());
					if (multicast.has("dataMDT"))
						multicasting.setDataMdt(multicast.get("dataMDT").toString());
					if (multicast.has("dataMDTThreshold"))
						multicasting.setDataMdtThreshold(multicast.get("dataMDTThreshold").toString());
					if (multicast.has("RPAddress"))
						multicasting.setRpAddress(multicast.get("RPAddress").toString());
					if (multicast.has("RPLocation"))
						multicasting.setRpLocation(multicast.get("RPLocation").toString());
					if (multicast.has("autodiscoveryOption"))
						multicasting.setAutoDiscoveryOption(multicast.get("autodiscoveryOption").toString());
					multicasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
					multicasting.setStartDate(new Timestamp(new Date().getTime()));
					multicasting.setModifiedBy("Optimus_Port_GVPN_migration");
					multicasting.setServiceDetail(serviceDetail1);
					multicastingRepository.saveAndFlush(multicasting);

				}
			}

			return "SUCCESS";
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}
	}

	private void setPolicyName(String policy, String key, Ospf ospf, Bgp bgp) {

		PolicyType policyType = new PolicyType();
		if (key.toLowerCase().contains("inbound")) {
			if (key.toLowerCase().contains("v4")) {
				policyType.setInboundIpv4PolicyName(policy);
				byte preProv = 1;
				policyType.setInboundIpv4Preprovisioned(preProv);
				Byte flag = 1;
				policyType.setInboundRoutingIpv4Policy(flag);
				if (ospf != null)
					policyType.setOspf(ospf);
				if (bgp != null)
					policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.toLowerCase().contains("v6")) {

				policyType.setInboundIpv6PolicyName(policy);
				Byte preProv = 1;
				policyType.setInboundIpv6Preprovisioned(preProv);
				Byte flag = 1;
				policyType.setInboundRoutingIpv6Policy(flag);
				if (ospf != null)
					policyType.setOspf(ospf);
				if (bgp != null)
					policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}
		if (key.toLowerCase().contains("outbound")) {
			if (key.toLowerCase().contains("v4")) {
				policyType.setOutboundIpv4PolicyName(policy);
				Byte preProv = 1;
				policyType.setOutboundIpv4Preprovisioned(preProv);
				Byte flag = 1;
				policyType.setOutboundRoutingIpv4Policy(flag);
				if (ospf != null)
					policyType.setOspf(ospf);
				if (bgp != null)
					policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.toLowerCase().contains("v6")) {
				policyType.setOutboundIpv6PolicyName(policy);
				Byte preProv = 1;
				policyType.setOutboundIpv6Preprovisioned(preProv);
				Byte flag = 1;
				policyType.setOutboundRoutingIpv6Policy(flag);
				if (ospf != null)
					policyType.setOspf(ospf);
				if (bgp != null)
					policyType.setBgp(bgp);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}

	}

	public void setPolicyTypeOspf(String key, JSONObject policyObject, Ospf ospf) {
		PolicyType policyType = new PolicyType();
		if (key.toLowerCase().contains("inbound")) {
			if (key.toLowerCase().contains("v4")) {
				if (policyObject.has("name"))
					policyType.setInboundIpv4PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setOspf(ospf);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.toLowerCase().contains("v6")) {
				if (policyObject.has("name"))
					policyType.setInboundIpv6PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setOspf(ospf);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}
		if (key.toLowerCase().contains("outbound")) {
			if (key.toLowerCase().contains("v4")) {
				if (policyObject.has("name"))
					policyType.setOutboundIpv4PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setOspf(ospf);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.toLowerCase().contains("v6")) {
				if (policyObject.has("name"))
					policyType.setOutboundIpv6PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setOspf(ospf);
				policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}

	}

	public RouterUplinkport saveRouterUplinkPort(JSONObject router, Topology topology) {
		RouterUplinkport routerUplinkport = new RouterUplinkport();
		routerUplinkport.setEndDate(null);
		routerUplinkport.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (router.has("topologyInterface1") && router.getJSONObject("topologyInterface1").has("physicalPort"))
			routerUplinkport
					.setPhysicalPort1Name(router.getJSONObject("topologyInterface1").get("physicalPort").toString());
		if (router.has("topologyInterface2") && router.getJSONObject("topologyInterface2").has("physicalPort"))
			routerUplinkport
					.setPhysicalPort2Name(router.getJSONObject("topologyInterface2").get("physicalPort").toString());
		routerUplinkport.setStartDate(new Timestamp(new Date().getTime()));
		routerUplinkport.setTopology(topology);
		routerUplinkport.setModifiedBy("Optimus_Port_GVPN_migration");
		routerUplinkPortRepository.saveAndFlush(routerUplinkport);
		return routerUplinkport;
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
		if (vprnPolicyObj.get("enable").toString().equalsIgnoreCase("true")) {
			PolicyType policyType = new PolicyType();
			policyType.setIsvprnExportpolicy((byte) 1);
			policyType.setIsvprnExportpolicyName(vprnPolicyObj.get("name").toString());
			if (vprnPolicyObj.has("isPreprovisioned")) {
				Byte flag = 0;
				if (vprnPolicyObj.get("isPreprovisioned").toString().equalsIgnoreCase("true")) {
					flag = (byte) 1;
				}
				policyType.setIsvprnExportPreprovisioned(flag);
			}
			policyType.setStartDate(new Timestamp(new Date().getTime()));
			policyType.setModifiedBy("Optimus_Port_GVPN_migration");
			policyType.setVrf(vrf);
		}
	}

	public void saveVprnImportPolicy(JSONObject vprnPolicyObj, Vrf vrf) {
		if (vprnPolicyObj.get("enable").toString().equalsIgnoreCase("true")) {
			PolicyType policyType = new PolicyType();
			policyType.setIsvprnImportpolicy((byte) 1);
			policyType.setIsvprnImportpolicyName(vprnPolicyObj.get("name").toString());
			if (vprnPolicyObj.has("isPreprovisioned")) {
				Byte flag = 0;
				if (vprnPolicyObj.get("isPreprovisioned").toString().equalsIgnoreCase("true")) {
					flag = (byte) 1;
				}
				policyType.setIsvprnImportPreprovisioned(flag);
			}
			policyType.setStartDate(new Timestamp(new Date().getTime()));
			policyType.setModifiedBy("Optimus_Port_GVPN_migration");
			policyType.setVrf(vrf);
		}
	}

	public Ospf saveOspf(JSONObject ospfObj) {
		Ospf ospf = new Ospf();
		ospf.setAreaId(ospfObj.get("areaID").toString());
		if (ospfObj.has("processID"))
			ospf.setProcessId(ospfObj.get("processID").toString());
		if (ospfObj.has("ignoreIPOSPFMTU") && ospfObj.get("ignoreIPOSPFMTU").toString().toLowerCase().equals("true"))
			ospf.setIsignoreipOspfMtu((byte) 1);
		else if (ospfObj.has("ignoreIPOSPFMTU")
				&& ospfObj.get("ignoreIPOSPFMTU").toString().toLowerCase().equals("false"))
			ospf.setIsignoreipOspfMtu((byte) 1);

		if (ospfObj.has("isAuthReqd")) {
			if (ospfObj.get("isAuthReqd").toString().equalsIgnoreCase("true"))
				ospf.setIsauthenticationRequired((byte) 1);
			else if (ospfObj.get("isAuthReqd").toString().equalsIgnoreCase("false"))
				ospf.setIsauthenticationRequired((byte) 0);
		}

		if (ospfObj.has("version"))
			ospf.setVersion(ospfObj.get("version").toString());

		if (ospfObj.has("shamlinkConfig") && ospfObj.getJSONObject("shamlinkConfig").has("enableShamlink")) {
			if (ospfObj.getJSONObject("shamlinkConfig").get("enableShamlink").toString().equalsIgnoreCase("true"))
				ospf.setIsenableShamlink((byte) 1);
			else if (ospfObj.getJSONObject("shamlinkConfig").get("enableShamlink").toString().equalsIgnoreCase("false"))
				ospf.setIsenableShamlink((byte) 0);
		}

		if (ospfObj.has("isNetworkP2p") && ospfObj.get("isNetworkP2p").toString().equalsIgnoreCase("true"))
			ospf.setIsnetworkP2p((byte) 1);
		else if (ospfObj.has("isNetworkP2p") && ospfObj.get("isNetworkP2p").toString().equalsIgnoreCase("false"))
			ospf.setIsnetworkP2p((byte) 0);

		ospf.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ospf.setModifiedBy("Optimus_Port_GVPN_migration");
		// ospf.set policy?
		ospfRepository.saveAndFlush(ospf);
		return ospf;
	}

	public void saveVpnMetaData(JSONObject vpnSolMetatObj, ServiceDetail serviceDetail1) {
		VpnMetatData data = new VpnMetatData();
		if (vpnSolMetatObj.has("isE2EIntegrated")
				&& vpnSolMetatObj.get("isE2EIntegrated").toString().toLowerCase().equals("true"))
			data.setE2eIntegrated(true);
		else if (vpnSolMetatObj.has("isE2EIntegrated")
				&& vpnSolMetatObj.get("isE2EIntegrated").toString().toLowerCase().equals("false"))
			data.setE2eIntegrated(false);

		// data.setIsenableUccService(null);
		data.setIsUa("NA");
		data.setL2oSiteRole(null);
		data.setL2OTopology(null);
		if (vpnSolMetatObj.has("managementVPNType"))
			data.setManagementVpnType1(vpnSolMetatObj.get("managementVPNType").toString());
		data.setManagementVpnType2(null);
		data.setServiceDetail(serviceDetail1);
		if (vpnSolMetatObj.has("VPNSolutionID"))
			data.setSolutionId(vpnSolMetatObj.get("VPNSolutionID").toString());
		if (vpnSolMetatObj.has("SAMVPNTopology"))
			data.setTopology(vpnSolMetatObj.get("SAMVPNTopology").toString());
		// data.setVpnAlias(vpnAlias);
		if (vpnSolMetatObj.has("VPNLegID") && !vpnSolMetatObj.get("VPNLegID").toString().matches("^[a-zA-Z0-9]*$"))
			data.setVpnLegId((Integer) vpnSolMetatObj.get("VPNLegID"));
		if (vpnSolMetatObj.has("VPNName"))
			data.setVpnName(vpnSolMetatObj.get("VPNName").toString());
		// data.setVpnSolutionName(vpnSolMetatObj.get("").toString());
//		data.setLastModifiedDate(new Timestamp(new Date().getTime()));
//		data.setModifiedBy("Optimus_Port_GVPN_migration");
		if (vpnSolMetatObj.has("attr2") && vpnSolMetatObj.get("attr2")!=null && vpnSolMetatObj.get("attr2").toString().toLowerCase().contains("mesh")) {
			data.setL2oSiteRole("Mesh");
			data.setL2OTopology("Full Mesh");
		}else {
			data.setL2oSiteRole("Spoke");
			data.setL2OTopology("Hub & Spoke");
		}
		vpnMetatDataRepository.saveAndFlush(data);
	}

	public void saveIpmCpe(String interfaceName, Cpe cpe, ServiceDetail serviceDetail1,
			EthernetInterface ethernetInterface2, ChannelizedSdhInterface channelizedSdhInterface,
			ChannelizedE1serialInterface channelizedE1serialInterface) {
		InterfaceProtocolMapping interfaceProtocolMapping2 = new InterfaceProtocolMapping();
		interfaceProtocolMapping2.setBgp(null);
		interfaceProtocolMapping2.setStaticProtocol(null);
		interfaceProtocolMapping2.setChannelizedE1serialInterface(null);// ?
		interfaceProtocolMapping2.setChannelizedSdhInterface(null);// ?
		interfaceProtocolMapping2.setCpe(cpe);
		interfaceProtocolMapping2.setEigrp(null);
		interfaceProtocolMapping2.setEndDate(null);
		if (interfaceName.toLowerCase().contains("ethernet"))
			interfaceProtocolMapping2.setEthernetInterface(ethernetInterface2);

		/*if (interfaceName.contains("sdh"))
			interfaceProtocolMapping2.setChannelizedSdhInterface(channelizedSdhInterface);// ?

		if (interfaceName.contains("serial"))
			interfaceProtocolMapping2.setChannelizedE1serialInterface(channelizedE1serialInterface);// ?*/

		interfaceProtocolMapping2.setIscpeLanInterface((byte) 0);
		interfaceProtocolMapping2.setIscpeWanInterface((byte) 1);
		interfaceProtocolMapping2.setLastModifiedDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping2.setModifiedBy("Optimus_Port_GVPN_migration");
		interfaceProtocolMapping2.setOspf(null);
		interfaceProtocolMapping2.setRip(null);
		interfaceProtocolMapping2.setRouterDetail(null);
		interfaceProtocolMapping2.setServiceDetail(serviceDetail1);
		interfaceProtocolMapping2.setStartDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping2.setVersion(1);
		interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping2);
	}

	public void saveIpmPe(String interfaceName, RouterDetail routerDetail, ServiceDetail serviceDetail1,
			StaticProtocol staticProtocol, Bgp bgp, Ospf ospf, EthernetInterface ethernetInterface,
			ChannelizedSdhInterface channelizedSdhInterface,
			ChannelizedE1serialInterface channelizedE1serialInterface) {
		InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
		if (bgp != null) {
			interfaceProtocolMapping.setBgp(bgp);
		}
		if (ospf != null) {
			interfaceProtocolMapping.setOspf(ospf);
		}
		if (staticProtocol != null) {
			interfaceProtocolMapping.setStaticProtocol(staticProtocol);
		}
		if (interfaceName.contains("ethernet"))
			interfaceProtocolMapping.setEthernetInterface(ethernetInterface);

		if (interfaceName.contains("sdh"))
			interfaceProtocolMapping.setChannelizedSdhInterface(channelizedSdhInterface);// ?

		if (interfaceName.contains("serial"))
			interfaceProtocolMapping.setChannelizedE1serialInterface(channelizedE1serialInterface);// ?

		interfaceProtocolMapping.setCpe(null);
		interfaceProtocolMapping.setEigrp(null);
		interfaceProtocolMapping.setEndDate(null);
		interfaceProtocolMapping.setIscpeLanInterface((byte) 0);
		interfaceProtocolMapping.setIscpeWanInterface((byte) 0);
		interfaceProtocolMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
		interfaceProtocolMapping.setModifiedBy("Optimus_Port_GVPN_migration");
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
		prefixlistConfig.setModifiedBy("Optimus_Port_GVPN_migration");
		prefixlistConfig.setStartDate(new Timestamp(new Date().getTime()));
		prefixlistConfigRepository.saveAndFlush(prefixlistConfig);

		PolicyTypeCriteriaMapping policyTypeCriteriaMapping1 = new PolicyTypeCriteriaMapping();
		policyTypeCriteriaMapping1.setPolicyType(policyType);
		policyTypeCriteriaMapping1.setPolicyCriteriaId(policyCriteria1.getPolicyCriteriaId());
		policyTypeCriteriaMapping1.setVersion(1);
		policyTypeCriteriaMapping1.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyTypeCriteriaMapping1.setModifiedBy("Optimus_Port_GVPN_migration");
		policyTypeCriteriaMapping1.setStartDate(new Timestamp(new Date().getTime()));
		policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping1);

	}

	public void saveNeighbourCommunityConfigCisco(Object neighbourCommunity, PolicyType policyType,
			PolicyCriteria policyCriteria4) {
		NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
		neighbourCommunityConfig.setPolicyCriteria(policyCriteria4);
		neighbourCommunityConfig.setCommunity(neighbourCommunity.toString());
		// neighbourCommunityConfig.setName();
		neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		neighbourCommunityConfig.setModifiedBy("Optimus_Port_GVPN_migration");
		neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
		neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
		PolicyTypeCriteriaMapping policyTypeCriteriaMapping4 = new PolicyTypeCriteriaMapping();

		policyTypeCriteriaMapping4.setPolicyType(policyType);
		policyTypeCriteriaMapping4.setPolicyCriteriaId(policyCriteria4.getPolicyCriteriaId());
		policyTypeCriteriaMapping4.setVersion(1);
		policyTypeCriteriaMapping4.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyTypeCriteriaMapping4.setModifiedBy("Optimus_Port_GVPN_migration");
		policyTypeCriteriaMapping4.setStartDate(new Timestamp(new Date().getTime()));
		policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping4);
	}

	public void saveWanStaticRouteBgp(JSONObject wanRoutingProtocol, Bgp bgp) {
		WanStaticRoute wanStaticRoute = new WanStaticRoute();
		wanStaticRoute.setAdvalue(wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList")
				.get("ADValue").toString());

		wanStaticRoute.setBgp(bgp);
		wanStaticRoute.setDescription(wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
				.getJSONObject("staticRouteList").get("Description").toString());
		wanStaticRoute.setEndDate(null);

		if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList").has("IsGlobal")) {
			if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList").get("IsGlobal")
					.toString().equalsIgnoreCase("true")) {
				wanStaticRoute.setGlobal((byte) 1);
			} else {
				wanStaticRoute.setGlobal((byte) 0);
			}
		}
		if (wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList").has("IPSubnet")
				&& wanRoutingProtocol.getJSONObject("peWANStaticRoutes").getJSONObject("staticRouteList")
						.getJSONObject("IPSubnet").has("address"))
			wanStaticRoute.setIpsubnet(wanRoutingProtocol.getJSONObject("peWANStaticRoutes")
					.getJSONObject("staticRouteList").getJSONObject("IPSubnet").get("address").toString());
		wanStaticRoute.setIsCewan((byte) 0);// or null?
		wanStaticRoute.setIsCpelanStaticroutes((byte) 0);
		wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
		wanStaticRoute.setIsPewan((byte) 1); // ispewan=yes
		wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute.setModifiedBy("Optimus_Port_GVPN_migration");
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

	public Bgp saveBgp(JSONObject bgpObj, Boolean isCisco) {
		Bgp bgp = new Bgp();
		if (bgpObj.has("ALUBackupPath"))
			bgp.setAluBackupPath(bgpObj.get("ALUBackupPath").toString());

		if (bgpObj.has("ALUDisableBGPPeerGrpExtCommunity")) {
			Byte aluDisableBGPPeerGrpExtCommunity;
			if (bgpObj.get("ALUDisableBGPPeerGrpExtCommunity").toString().toUpperCase().equals("TRUE")) {
				aluDisableBGPPeerGrpExtCommunity = 1;
			} else {
				aluDisableBGPPeerGrpExtCommunity = 0;
			}
			bgp.setAluDisableBgpPeerGrpExtCommunity(aluDisableBGPPeerGrpExtCommunity);
		}
		if (bgpObj.has("ALUKeepAlive"))
			bgp.setAluKeepalive(bgpObj.get("ALUKeepAlive").toString());

		if (bgpObj.has("ASOverride")) {
			if (bgpObj.get("ASOverride").toString().toLowerCase().equals("true")) {
				bgp.setAsoOverride((byte) 1);// isASOverriden
			} else {
				bgp.setAsoOverride((byte) 0);// isASOverriden
			}
		}

		bgp.setAsPath(null);
		// bgp.setIsebgpMultihopReqd(null);

		if (isCisco) {
			Byte isRoutemapEnabledInboundRoutemapV4 = 0;
			Byte isbgpNeighbourinboundv4RoutemapPreprovisioned = 0;
			if (bgpObj.has("isRoutemapEnabledInboundRoutemapV4")
					&& bgpObj.get("isRoutemapEnabledInboundRoutemapV4").toString().equals("true")) {
				isRoutemapEnabledInboundRoutemapV4 = 1;
				bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(isRoutemapEnabledInboundRoutemapV4);
				bgp.setBgpneighbourinboundv4routermapname(bgpObj.get("BGPNeighbourInboundRoutemapV4").toString());
				if (bgpObj.has("isRouteMapPreprovisionedV4")
						&& bgpObj.get("isRouteMapPreprovisionedV4").toString().equals("true"))
					isbgpNeighbourinboundv4RoutemapPreprovisioned = 1;
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(isbgpNeighbourinboundv4RoutemapPreprovisioned);
			} else {
				bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(isRoutemapEnabledInboundRoutemapV4);
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(isbgpNeighbourinboundv4RoutemapPreprovisioned);
			}
			Byte isRoutemapEnabledInboundRoutemapV6 = 0;
			Byte isbgpNeighbourinboundv6RoutemapPreprovisioned = 0;
			if (bgpObj.has("isRoutemapEnabledInboundRoutemapV6")
					&& bgpObj.get("isRoutemapEnabledInboundRoutemapV6").toString().equals("true")) {
				isRoutemapEnabledInboundRoutemapV6 = 1;
				bgp.setIsbgpNeighbourInboundv6RoutemapEnabled(isRoutemapEnabledInboundRoutemapV6);
				bgp.setBgpneighbourinboundv6routermapname(bgpObj.get("BGPNeighbourInboundRoutemapV6").toString());
				if (bgpObj.has("isRouteMapPreprovisionedV6")
						&& bgpObj.get("isRouteMapPreprovisionedV4").toString().equals("true"))
					isbgpNeighbourinboundv6RoutemapPreprovisioned = 1;
				bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned(isbgpNeighbourinboundv6RoutemapPreprovisioned);
			} else {
				bgp.setIsbgpNeighbourInboundv6RoutemapEnabled(isRoutemapEnabledInboundRoutemapV6);
			}
		}

		if (bgpObj.has("BGPPeerGroupName"))
			bgp.setBgpPeerName(bgpObj.get("BGPPeerGroupName").toString());
		bgp.setEndDate(null);
		if (bgpObj.has("helloTime"))
			bgp.setHelloTime(bgpObj.get("helloTime").toString());
		if (bgpObj.has("holdTime"))
			bgp.setHoldTime(bgpObj.get("holdTime").toString());
		bgp.setInterfaceProtocolMappings(null);// ?
		if (bgpObj.has("isAuthReqd")) {
			Byte isAuthenticationRequired;
			if (bgpObj.get("isAuthReqd").toString().toLowerCase().equals("true")) {
				isAuthenticationRequired = 1;
				if (bgpObj.has("authMode"))
					bgp.setAuthenticationMode(bgpObj.get("authMode").toString());
				if (bgpObj.has("authPassword"))
					bgp.setPassword(bgpObj.get("authPassword").toString());
			} else {
				isAuthenticationRequired = 0;
			}
			bgp.setIsauthenticationRequired(isAuthenticationRequired);
		}
		if (bgpObj.has("isBGPMultiHopReqd")) {
			Byte isebgpMultihopReqd;
			if (bgpObj.get("isBGPMultiHopReqd").toString().toLowerCase().equals("true")) {
				isebgpMultihopReqd = 1;
			} else {
				isebgpMultihopReqd = 0;
			}
			bgp.setIsebgpMultihopReqd(isebgpMultihopReqd);
		}

		if (bgpObj.has("MultihopTTL"))
			bgp.setIsmultihopTtl(bgpObj.get("MultihopTTL").toString());

		if (bgpObj.has("isRTBH")) {
			Byte isRTBH = 0;
			if (bgpObj.get("isRTBH").toString().equals("true")) {
				isRTBH = 1;
				bgp.setRtbhOptions(bgpObj.get("RTBHOptions").toString());
			}
			bgp.setIsrtbh(isRTBH);
		}

		bgp.setLastModifiedDate(new Timestamp(new Date().getTime()));
		if (bgpObj.has("bgpNeighbourLocalASNumber") && bgpObj.getJSONObject("bgpNeighbourLocalASNumber").has("number"))
			bgp.setLocalAsNumber(bgpObj.getJSONObject("bgpNeighbourLocalASNumber").get("number").toString());

		if (bgpObj.has("LocalPreference"))
			bgp.setLocalPreference(bgpObj.get("LocalPreference").toString());

		if (bgpObj.has("localBGPNeighbourUpdateSource")) {

			if (bgpObj.getJSONObject("localBGPNeighbourUpdateSource").has("interfaceName"))
				bgp.setLocalUpdateSourceInterface(
						bgpObj.getJSONObject("localBGPNeighbourUpdateSource").get("interfaceName").toString());

			if (bgpObj.getJSONObject("localBGPNeighbourUpdateSource").has("v4IpAddress") && bgpObj
					.getJSONObject("localBGPNeighbourUpdateSource").getJSONObject("v4IpAddress").has("address"))
				bgp.setLocalUpdateSourceIpv4Address(bgpObj.getJSONObject("localBGPNeighbourUpdateSource")
						.getJSONObject("v4IpAddress").get("address").toString());

			if (bgpObj.getJSONObject("localBGPNeighbourUpdateSource").has("v6IpAddress") && bgpObj
					.getJSONObject("localBGPNeighbourUpdateSource").getJSONObject("v6IpAddress").has("address"))
				bgp.setLocalUpdateSourceIpv6Address(bgpObj.getJSONObject("localBGPNeighbourUpdateSource")
						.getJSONObject("v6IpAddress").get("address").toString());
		}

		if (bgpObj.has("maxPrefix"))
			bgp.setMaxPrefix(bgpObj.get("maxPrefix").toString());
		if (bgpObj.has("maxPrefixThreshold"))
			bgp.setMaxPrefixThreshold(bgpObj.get("maxPrefixThreshold").toString());
		bgp.setMedValue(null);
		bgp.setModifiedBy("Optimus_Port_GVPN_migration");
		if (bgpObj.has("neighbourOn"))
			bgp.setNeighborOn(bgpObj.get("neighbourOn").toString());
		if (bgpObj.has("neighbourCommunity"))
			bgp.setNeighbourCommunity(bgpObj.get("neighbourCommunity").toString());
		Byte isNeighbourShutdownRequired = 0;
		if (bgpObj.has("isNeighbourShutdownRequired")
				&& bgpObj.get("isNeighbourShutdownRequired").toString().equals("true")) {
			isNeighbourShutdownRequired = 1;
		}
		bgp.setNeighbourshutdownRequired(isNeighbourShutdownRequired);

		if (bgpObj.has("PEERType"))
			bgp.setPeerType(bgpObj.get("PEERType").toString().toUpperCase());

		bgp.setPolicyTypes(null);// ?

		if (bgpObj.has("isRedistributeConnectEnabled")) {
			Byte isRedistributeConnectedEnabled = 0;
			if (bgpObj.get("isRedistributeConnectEnabled").toString().equals("true")) {
				isRedistributeConnectedEnabled = 1;
			}
			bgp.setRedistributeConnectedEnabled(isRedistributeConnectedEnabled);
		}

		if (bgpObj.has("isRedistributeStaticEnabled")) {
			Byte isRedistributeStaticEnabled = 0;
			if (bgpObj.get("isRedistributeStaticEnabled").toString().equals("true")) {
				isRedistributeStaticEnabled = 1;
			}
			bgp.setRedistributeStaticEnabled(isRedistributeStaticEnabled);
		}

		if (bgpObj.has("remoteASNumber") && bgpObj.getJSONObject("remoteASNumber").has("number")) {
			String remoteAsNumString = bgpObj.getJSONObject("remoteASNumber").get("number").toString();
			String remoteAsNum = remoteAsNumString.replaceAll("[^0-9]", "");
			bgp.setRemoteAsNumber(Integer.valueOf(remoteAsNum));
		}

		Byte isbgpMultihopReqd = (byte) 0;
		if (bgpObj.has("remoteASNumber") && bgpObj.getJSONObject("remoteASNumber").has("isCustomerOwned") && bgpObj
				.getJSONObject("remoteASNumber").get("isCustomerOwned").toString().toLowerCase().equals("true")) {
			String isCustomerOwned = bgpObj.getJSONObject("remoteASNumber").get("isCustomerOwned").toString();
			isbgpMultihopReqd = (byte) 1;
		}
		bgp.setIsbgpMultihopReqd(isbgpMultihopReqd);

		bgp.setRemoteCeAsnumber(null);

		if (bgpObj.has("remoteIPv4address") && (bgpObj.get("remoteIPv4address") instanceof JSONObject)
				&& bgpObj.getJSONObject("remoteIPv4address").has("address"))
			bgp.setRemoteIpv4Address(bgpObj.getJSONObject("remoteIPv4address").get("address").toString());

		if (bgpObj.has("remoteIPv6address") && (bgpObj.get("remoteIPv6address") instanceof JSONObject)
				&& bgpObj.getJSONObject("remoteIPv6address").has("address"))
			bgp.setRemoteIpv4Address(bgpObj.getJSONObject("remoteIPv6address").get("address").toString());

		if (bgpObj.has("remoteBGPNeighbourUpdateSource")) {

			if (bgpObj.getJSONObject("remoteBGPNeighbourUpdateSource").has("interfaceName"))
				bgp.setRemoteUpdateSourceInterface(
						bgpObj.getJSONObject("remoteBGPNeighbourUpdateSource").get("interfaceName").toString());

			if (bgpObj.getJSONObject("remoteBGPNeighbourUpdateSource").has("v4IpAddress") && bgpObj
					.getJSONObject("remoteBGPNeighbourUpdateSource").getJSONObject("v4IpAddress").has("address"))
				bgp.setRemoteUpdateSourceIpv4Address(bgpObj.getJSONObject("remoteBGPNeighbourUpdateSource")
						.getJSONObject("v4IpAddress").get("address").toString());

			if (bgpObj.getJSONObject("remoteBGPNeighbourUpdateSource").has("v6IpAddress") && bgpObj
					.getJSONObject("remoteBGPNeighbourUpdateSource").getJSONObject("v6IpAddress").has("address"))
				bgp.setRemoteUpdateSourceIpv6Address(bgpObj.getJSONObject("remoteBGPNeighbourUpdateSource")
						.getJSONObject("v6IpAddress").get("address").toString());
		}
		if (bgpObj.has("routesExchanged") && bgpObj.getJSONObject("routesExchanged").has("routes"))
			bgp.setRoutesExchanged(bgpObj.getJSONObject("routesExchanged").get("routes").toString());
		if (bgpObj.has("isSOOReqd")) {
			Byte isSOORequired = 0;
			if (bgpObj.get("isSOOReqd").toString().equals("true")) {
				isSOORequired = 1;
			}
			bgp.setSooRequired(isSOORequired);
		}
		byte splitHorizon = 0;
		if (bgpObj.has("splitHorizon")) {
			if (bgpObj.get("splitHorizon").toString().toLowerCase().contains("yes")) {
				splitHorizon = (byte) 1;
			}
		}
		bgp.setSplitHorizon(splitHorizon);
		bgp.setStartDate(new Timestamp(new Date().getTime()));
		if (bgpObj.has("v6LocalPreference"))
			bgp.setV6LocalPreference(bgpObj.get("v6LocalPreference").toString());
		// bgp.setWanStaticRoutes(wanStaticRoutes);//?
		bgpRepository.saveAndFlush(bgp);
		return bgp;
	}

	public void saveStaticWanCe(JSONObject ceWanObj, StaticProtocol staticProtocol) {
		WanStaticRoute wanStaticRoute2 = new WanStaticRoute();
		wanStaticRoute2.setAdvalue(null);
		wanStaticRoute2.setBgp(null);
		wanStaticRoute2.setDescription(null);
		wanStaticRoute2.setEndDate(null);
		if (ceWanObj.has("Global")) {
			if (ceWanObj.get("Global").toString().equalsIgnoreCase("true")) {
				wanStaticRoute2.setGlobal((byte) 1);
			} else {
				wanStaticRoute2.setGlobal((byte) 0);
			}
		}
		if (ceWanObj.has("IPSubnet") && ceWanObj.getJSONObject("IPSubnet").has("address"))
			wanStaticRoute2.setIpsubnet(ceWanObj.getJSONObject("IPSubnet").get("address").toString());
		wanStaticRoute2.setIsCewan((byte) 1);// or null?
		wanStaticRoute2.setIsCpelanStaticroutes((byte) 0);
		wanStaticRoute2.setIsCpewanStaticroutes((byte) 0);
		wanStaticRoute2.setIsPewan((byte) 0); // ispewan=yes
		wanStaticRoute2.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute2.setModifiedBy("Optimus_Port_GVPN_migration");
		if (ceWanObj.has("nextHopIP"))
			wanStaticRoute2.setNextHopid(ceWanObj.get("nextHopIP").toString());

//		if (wanRoutingProtocol.has("POPCommunity"))
//			wanStaticRoute2.setPopCommunity(wanRoutingProtocol.get("POPCommunity").toString()); // heirarchy
//																								// as
//																								// of
//																								// original
//																								// wanroutingprtocol
//		if (wanRoutingProtocol.has("RegionalCommunity"))
//			wanStaticRoute2.setRegionalCommunity(wanRoutingProtocol.get("RegionalCommunity").toString());
//
//		if (wanRoutingProtocol.has("ServiceCommunity"))
//			wanStaticRoute2.setServiceCommunity(wanRoutingProtocol.get("ServiceCommunity").toString());

		wanStaticRoute2.setStartDate(new Timestamp(new Date().getTime()));
		wanStaticRoute2.setStaticProtocol(staticProtocol);// ?
		wanStaticRouteRepository.saveAndFlush(wanStaticRoute2);

	}

	public void saveStaticWanPe(JSONObject peWanObj, StaticProtocol staticProtocol) {

		WanStaticRoute wanStaticRoute = new WanStaticRoute();
		wanStaticRoute.setAdvalue(null);
		wanStaticRoute.setBgp(null);
		wanStaticRoute.setDescription(null);
		wanStaticRoute.setEndDate(null);
		if (peWanObj.has("Global")) {
			if (peWanObj.get("Global").toString().equalsIgnoreCase("true")) {
				wanStaticRoute.setGlobal((byte) 1);
			} else {
				wanStaticRoute.setGlobal((byte) 0);
			}
		}
		if (peWanObj.has("IPSubnet") && peWanObj.getJSONObject("IPSubnet").has("address"))
			wanStaticRoute.setIpsubnet(peWanObj.getJSONObject("IPSubnet").get("address").toString());
		wanStaticRoute.setIsCewan((byte) 0);// or null?
		wanStaticRoute.setIsCpelanStaticroutes((byte) 0);
		wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
		wanStaticRoute.setIsPewan((byte) 1); // ispewan=yes
		wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute.setModifiedBy("Optimus_Port_GVPN_migration");
		if (peWanObj.has("nextHopIP"))
			wanStaticRoute.setNextHopid(peWanObj.get("nextHopIP").toString());

//		if (wanRoutingProtocol.has("POPCommunity"))
//			wanStaticRoute.setPopCommunity(wanRoutingProtocol.get("POPCommunity").toString()); // heirarchy
//																								// as
//																								// of
//																								// original
//																								// wanroutingprtocol
//		if (wanRoutingProtocol.has("RegionalCommunity"))
//			wanStaticRoute.setRegionalCommunity(wanRoutingProtocol.get("RegionalCommunity").toString());
//
//		if (wanRoutingProtocol.has("ServiceCommunity"))
//			wanStaticRoute.setServiceCommunity(wanRoutingProtocol.get("ServiceCommunity").toString());

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

		staticProtocol.setModifiedBy("Optimus_Port_GVPN_migration");
		staticProtocol.setPolicyTypes(null);// ?

		if (wanRoutingProtocol.has("RouteMapNameV4"))
			staticProtocol.setRedistributeRoutemapIpv4(wanRoutingProtocol.get("RouteMapNameV4").toString());

		staticProtocol.setStartDate(new Timestamp(new Date().getTime()));
		staticProtocolRepository.saveAndFlush(staticProtocol);
		return staticProtocol;
	}

	public VpnSolution saveVpnSolution(JSONObject solutionObj, ServiceDetail serviceDetail1, String vpnSolName) {
		VpnSolution vpnSolution = new VpnSolution();
		vpnSolution.setEndDate(null);
		if (solutionObj != null) {
			if (solutionObj.has("leg") && solutionObj.getJSONObject("leg").has("legInstanceID")) {
				if (solutionObj.getJSONObject("leg").get("legInstanceID").toString().contains("/")) {
					String[] oldSolIdFormat = solutionObj.getJSONObject("leg").get("legInstanceID").toString()
							.split("/");
					vpnSolution.setInstanceId(oldSolIdFormat[0]);
				} else {
					vpnSolution.setInstanceId(solutionObj.getJSONObject("leg").get("legInstanceID").toString());
				}
			}
//		vpnSolution.setInterfaceName(ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg")
//				.get("interface").toString());
			if (solutionObj.has("leg") && solutionObj.getJSONObject("leg").has("role"))
				vpnSolution.setLegRole(solutionObj.getJSONObject("leg").get("role").toString());
			// vpnSolution.setManagementVpnType1(null);// ?
//		vpnSolution.setSiteId(ias.getJSONObject("SolutionTable").getJSONObject("VPN").getJSONObject("vpnLeg")
//				.get("customerSiteId").toString());
			if (solutionObj.has("leg") && solutionObj.getJSONObject("leg").has("legID"))
				vpnSolution.setVpnLegId(solutionObj.getJSONObject("leg").get("legID").toString());
			if (solutionObj.has("vpnID"))
				vpnSolution.setVpnName(solutionObj.get("vpnID").toString());
			if (solutionObj.has("topology"))
				vpnSolution.setVpnTopology(solutionObj.get("topology").toString());
			if (solutionObj.has("vpnType"))
				vpnSolution.setVpnType(solutionObj.get("vpnType").toString());
		}
		vpnSolution.setVpnSolutionName(vpnSolName);
		vpnSolution.setStartDate(new Timestamp(new Date().getTime()));
		vpnSolution.setModifiedBy("Optimus_Port_GVPN_migration");
		vpnSolution.setServiceDetail(serviceDetail1);
		vpnSolution.setLastModifiedDate(new Timestamp(new Date().getTime()));
		vpnSolutionRepository.saveAndFlush(vpnSolution);
		return vpnSolution;
	}

	public Vrf saveVrf(JSONObject gvpn, ServiceDetail serviceDetail1) {
		// masterVRFFlag?
		Vrf vrf = new Vrf();

		vrf.setEndDate(null);

		vrf.setLastModifiedDate(new Timestamp(new Date().getTime()));

		vrf.setServiceDetail(serviceDetail1);
		vrf.setStartDate(new Timestamp(new Date().getTime()));
		if (gvpn.getJSONObject("VRFDetails").has("name"))
			vrf.setVrfName(gvpn.getJSONObject("VRFDetails").get("name").toString());// internet,primmus,not
		else
			vrf.setVrfName("NOT_APPLICABLE");

		Byte isMultiVRF = 0;
		if (gvpn.getJSONObject("IPService").has("isMultiVRF")
				&& gvpn.getJSONObject("IPService").get("isMultiVRF").toString().toLowerCase().equals("true"))
			isMultiVRF = 1;
		vrf.setIsmultivrf(isMultiVRF);

		Byte isVRFLiteEnabled = 0;
		if (gvpn.getJSONObject("VRFDetails").has("isVRFLiteEnabled")
				&& gvpn.getJSONObject("VRFDetails").get("isVRFLiteEnabled").toString().toLowerCase().equals("true"))
			isVRFLiteEnabled = 1;
		vrf.setIsvrfLiteEnabled(isVRFLiteEnabled);// only for gvpn as an attribute // not null check

		vrf.setMastervrfServiceid(null);

		if (gvpn.getJSONObject("VRFDetails").has("MaxRoutesValue"))
			vrf.setMaxRoutesValue(gvpn.getJSONObject("VRFDetails").get("MaxRoutesValue").toString());// may
																										// or
																										// may
		// not come
		vrf.setModifiedBy("Optimus_Port_GVPN_migration");
		vrf.setPolicyTypes(null);// ?
		if (gvpn.getJSONObject("VRFDetails").has("threshold"))
			vrf.setThreshold(gvpn.getJSONObject("VRFDetails").get("threshold").toString());// may
																							// or
		// may
		// not
		// come
		if (gvpn.getJSONObject("VRFDetails").has("warnOn"))
			vrf.setWarnOn(gvpn.getJSONObject("IPService").getJSONObject("vrf").get("warnOn").toString());// may or may
																											// not come
		vrfRepository.saveAndFlush(vrf);
		return vrf;
	}

	public Cpe saveCpe(JSONObject cpeObj, String serviceId) {
		Cpe cpe = new Cpe();
		cpe.setInterfaceProtocolMappings(null);

		cpe.setSnmpServerCommunity(null);
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
		cpe.setModel(null);
		cpe.setModifiedBy("Optimus_Port_GVPN_migration");
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
		try {
		if (cpeObj.has("loopbackInterface") && cpeObj.getJSONObject("loopbackInterface").has("IPv4Address") 
				&& cpeObj.getJSONObject("loopbackInterface").getJSONObject("IPv4Address").has("address"))
			cpe.setMgmtLoopbackV4address(cpeObj.getJSONObject("loopbackInterface").getJSONObject("IPv4Address").get("address").toString());
		if (cpeObj.has("WANInterface") && cpeObj.getJSONObject("WANInterface").has("WANInterface"))
			cpe.setWanInterfaceName(cpeObj.getJSONObject("WANInterface").get("WANInterface").toString());
		}catch(Exception e) {
			LOGGER.error("Exception in saveCpe::{} for serviceId::{}",e,serviceId);
		}
		cpeRepository.saveAndFlush(cpe);
		return cpe;
	}

	public EthernetInterface saveEthernetCpe(JSONObject interfaceObj) {

		JSONObject cpewanInterface = interfaceObj.getJSONObject("ethernetInterface");
		EthernetInterface ethernetInterface2 = new EthernetInterface();
		if (cpewanInterface.has("isAutoNegotiation"))
			ethernetInterface2.setAutonegotiationEnabled(cpewanInterface.get("isAutoNegotiation").toString());

		if (cpewanInterface.has("isBFDEnabled")) {
			if (cpewanInterface.get("isBFDEnabled").toString().equalsIgnoreCase("true")) {
				ethernetInterface2.setIsbfdEnabled((byte) 1);
				ethernetInterface2.setBfdMultiplier(cpewanInterface.get("BFDMultiplier").toString());
				ethernetInterface2.setBfdreceiveInterval(cpewanInterface.get("BFDReceiveinterval").toString());
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

		if (cpewanInterface.has("vlan"))
			ethernetInterface2.setInnerVlan(cpewanInterface.get("vlan").toString());

		if (interfaceObj.has("WANInterface"))
			ethernetInterface2.setInterfaceName(interfaceObj.get("WANInterface").toString());

		if (cpewanInterface.has("IPv4Address") && cpewanInterface.get("IPv4Address") instanceof JSONObject
				&& cpewanInterface.getJSONObject("IPv4Address").has("address"))
			ethernetInterface2.setIpv4Address(cpewanInterface.getJSONObject("IPv4Address").get("address").toString());

		if (cpewanInterface.has("IPv6Address") && cpewanInterface.get("IPv6Address") instanceof JSONObject
				&& cpewanInterface.getJSONObject("IPv6Address").has("address"))
			ethernetInterface2.setIpv6Address(cpewanInterface.getJSONObject("IPv6Address").get("address").toString());

		Byte defvalue = (byte) 0;
		ethernetInterface2.setIshsrpEnabled(defvalue);
		ethernetInterface2.setIsvrrpEnabled(defvalue);

		ethernetInterface2.setLastModifiedDate(new Timestamp(new Date().getTime()));

		if (cpewanInterface.has("mediaType"))
			ethernetInterface2.setMediaType(cpewanInterface.get("mediaType").toString());

		if (cpewanInterface.has("mode"))
			ethernetInterface2.setMode(cpewanInterface.get("mode").toString());

		ethernetInterface2.setModifiedBy("Optimus_Port_GVPN_migration");

		if (cpewanInterface.has("MTU"))
			ethernetInterface2.setMtu(cpewanInterface.get("MTU").toString());

		if (cpewanInterface.has("IPv4Address") && cpewanInterface.get("IPv4Address") instanceof JSONObject
				&& cpewanInterface.getJSONObject("IPv4Address").has("address"))
			ethernetInterface2
					.setModifiedIpv4Address(cpewanInterface.getJSONObject("IPv4Address").get("address").toString());

		if (cpewanInterface.has("IPv6Address") && cpewanInterface.get("IPv6Address") instanceof JSONObject
				&& cpewanInterface.getJSONObject("IPv6Address").has("address"))
			ethernetInterface2
					.setModifiedIpv6Address(cpewanInterface.getJSONObject("IPv6Address").get("address").toString());

		ethernetInterface2.setModifiedSecondaryIpv4Address(null);
		ethernetInterface2.setModifiedSecondaryIpv6Address(null);

		if (cpewanInterface.has("OuterVLAN"))
			ethernetInterface2.setOuterVlan(cpewanInterface.get("OuterVLAN").toString());

		if (cpewanInterface.has("physicalPortName"))
			ethernetInterface2.setPhysicalPort(cpewanInterface.get("physicalPortName").toString());

		if (cpewanInterface.has("PortType"))
			ethernetInterface2.setPortType(cpewanInterface.get("PortType").toString());

		if (cpewanInterface.has("SecondaryV4IpAddress")
				&& cpewanInterface.get("SecondaryV4IpAddress") instanceof JSONObject
				&& cpewanInterface.getJSONObject("SecondaryV4IpAddress").has("address"))
			ethernetInterface2.setSecondaryIpv4Address(
					cpewanInterface.getJSONObject("SecondaryV4IpAddress").get("address").toString());

		if (cpewanInterface.has("SecondaryV6IpAddress")
				&& cpewanInterface.get("SecondaryV6IpAddress") instanceof JSONObject
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

	public ChannelizedSdhInterface saveChannelizedSdhInterface(JSONObject gvpn, JSONObject interfaceObj) {

		ChannelizedSdhInterface channelizedSdhInterface = new ChannelizedSdhInterface();
		channelizedSdhInterface.set_4Kfirsttime_slot(null);
		channelizedSdhInterface.set_4klasttimeSlot(null);
		channelizedSdhInterface.setAclPolicyCriterias(null);
		Byte isbfdEnabled;
		if (interfaceObj.getJSONObject("ChannelSDH").has("BFD")) {
			if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("isRouterBFDEnabled")
					&& interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").get("isRouterBFDEnabled")
							.toString().equals("false")) { // attribute names
				isbfdEnabled = (byte) 0;
				channelizedSdhInterface.setIsbfdEnabled(isbfdEnabled);
				if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("BFDMultiplier")) {
					channelizedSdhInterface.setBfdMultiplier(interfaceObj.getJSONObject("ChannelSDH")
							.getJSONObject("BFD").get("BFDMultiplier").toString());
				}
				if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("BFDReceiveinterval")) {
					channelizedSdhInterface.setBfdreceiveInterval(interfaceObj.getJSONObject("ChannelSDH")
							.getJSONObject("BFD").get("BFDReceiveinterval").toString());
				}
				if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("BFDTransmitinterval")) {
					channelizedSdhInterface.setBfdtransmitInterval(interfaceObj.getJSONObject("ChannelSDH")
							.getJSONObject("BFD").get("BFDTransmitinterval").toString());
				}
			} else if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("isRouterBFDEnabled")
					&& interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").get("isRouterBFDEnabled")
							.toString().equals("true")) {
				isbfdEnabled = (byte) 1;
				channelizedSdhInterface.setIsbfdEnabled(isbfdEnabled);
				if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("BFDMultiplier")) {
					channelizedSdhInterface.setBfdMultiplier(interfaceObj.getJSONObject("ChannelSDH")
							.getJSONObject("BFD").get("BFDMultiplier").toString());
				}
				if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("BFDReceiveinterval")) {
					channelizedSdhInterface.setBfdreceiveInterval(interfaceObj.getJSONObject("ChannelSDH")
							.getJSONObject("BFD").get("BFDReceiveinterval").toString());
				}
				if (interfaceObj.getJSONObject("ChannelSDH").getJSONObject("BFD").has("BFDTransmitinterval")) {
					channelizedSdhInterface.setBfdtransmitInterval(interfaceObj.getJSONObject("ChannelSDH")
							.getJSONObject("BFD").get("BFDTransmitinterval").toString());
				}
			}
		}
		channelizedSdhInterface.setChannelGroupNumber(null);
		channelizedSdhInterface.setDlciValue(null);
		channelizedSdhInterface.setDowncount(null);
		if (interfaceObj.getJSONObject("ChannelSDH").has("encapsulation")) {
			channelizedSdhInterface
					.setEncapsulation(interfaceObj.getJSONObject("ChannelSDH").get("encapsulation").toString());
		}
		channelizedSdhInterface.setEndDate(null);
		channelizedSdhInterface
				.setInterfaceName(interfaceObj.getJSONObject("ChannelSDH").get("interfaceName").toString());// ?
		channelizedSdhInterface.setFraming(null);
		channelizedSdhInterface.setHoldtimeDown(null);
		channelizedSdhInterface.setHoldtimeUp(null);
		channelizedSdhInterface.setInterfaceProtocolMappings(null);// ?
		channelizedSdhInterface.setJ(null);
		if (interfaceObj.getJSONObject("ChannelSDH").has("IPv4Address")
				&& interfaceObj.getJSONObject("ChannelSDH").getJSONObject("IPv4Address").has("address")) {
			channelizedSdhInterface.setIpv4Address(
					interfaceObj.getJSONObject("ChannelSDH").getJSONObject("IPv4Address").get("address").toString());
		}
		if (interfaceObj.getJSONObject("ChannelSDH").has("IPv6Address")
				&& interfaceObj.getJSONObject("ChannelSDH").getJSONObject("IPv6Address").has("address")) {
			channelizedSdhInterface.setIpv6Address(
					interfaceObj.getJSONObject("ChannelSDH").getJSONObject("IPv6Address").get("address").toString());
		}
		channelizedSdhInterface.setIsframed(null);
		channelizedSdhInterface.setIshdlcConfig(null);
		channelizedSdhInterface.setIshsrpEnabled(null);
		channelizedSdhInterface.setIsvrrpEnabled(null);
		channelizedSdhInterface.setKeepalive(null);
		channelizedSdhInterface.setKlm(null);
		channelizedSdhInterface.setPosframing(null);
		channelizedSdhInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		if (interfaceObj.getJSONObject("ChannelSDH").has("mode"))
			channelizedSdhInterface.setMode(interfaceObj.getJSONObject("ChannelSDH").get("mode").toString());
		channelizedSdhInterface.setModifiedBy("Optimus_Port_GVPN_migration");
		channelizedSdhInterface.setModifiedIpv4Address(null);
		channelizedSdhInterface.setModifiedIipv6Address(null);
		channelizedSdhInterface.setModifiedSecondaryIpv4Address(null);
		channelizedSdhInterface.setMtu(null);
		if (interfaceObj.getJSONObject("ChannelSDH").has("physicalPort")) {
			channelizedSdhInterface
					.setPhysicalPort(interfaceObj.getJSONObject("ChannelSDH").get("physicalPort").toString());
		}
		channelizedSdhInterface.setPortType(null);
		channelizedSdhInterface.setSecondaryIpv4Address(null);
		channelizedSdhInterface.setSecondaryIpv6Address(null);
		channelizedSdhInterface.setStartDate(new Timestamp(new Date().getTime()));
		channelizedSdhInterface.setUpcount(null);
		channelizedSdhInterfaceRepository.saveAndFlush(channelizedSdhInterface);
		return channelizedSdhInterface;
	}

	public ChannelizedE1serialInterface saveChannelizedE1SerialDetails(JSONObject gvpn, JSONObject interfaceObj) {
		ChannelizedE1serialInterface channelizedE1serialInterface = new ChannelizedE1serialInterface();
		channelizedE1serialInterface.setAclPolicyCriterias(null);// ?
		Byte isbfdEnabled;
		if (interfaceObj.getJSONObject("SerialInterface").has("BFD")) {
			if (interfaceObj.getJSONObject("SerialInterface").getJSONObject("BFD").get("isRouterBFDEnabled").toString()
					.equals("false")) { // attribute names
				// for bfd
				isbfdEnabled = (byte) 0;
				channelizedE1serialInterface.setIsbfdEnabled(isbfdEnabled);
				channelizedE1serialInterface.setBfdMultiplier(null);
				channelizedE1serialInterface.setBfdreceiveInterval(null);
				channelizedE1serialInterface.setBfdtransmitInterval(null);
			} else {
				isbfdEnabled = (byte) 1;
				channelizedE1serialInterface.setIsbfdEnabled(isbfdEnabled);
				channelizedE1serialInterface.setBfdMultiplier(interfaceObj.getJSONObject("SerialInterface")
						.getJSONObject("BFD").get("BFDMultiplier").toString());
				channelizedE1serialInterface.setBfdreceiveInterval(interfaceObj.getJSONObject("SerialInterface")
						.getJSONObject("BFD").get("BFDReceiveinterval").toString());
				channelizedE1serialInterface.setBfdtransmitInterval(interfaceObj.getJSONObject("SerialInterface")
						.getJSONObject("BFD").get("BFDTransmitinterval").toString());
			}
		}
		channelizedE1serialInterface.setChannelGroupNumber(null);
		if (interfaceObj.getJSONObject("SerialInterface").has("crcSize")
				&& interfaceObj.getJSONObject("SerialInterface").getJSONObject("crcSize").has("address")) {
			channelizedE1serialInterface.setCrcsize(
					(Integer) interfaceObj.getJSONObject("SerialInterface").getJSONObject("crcSize").get("address"));
		}
		channelizedE1serialInterface.setDlciValue(null);
		channelizedE1serialInterface.setDowncount(null);
		channelizedE1serialInterface.setEncapsulation(null);
		channelizedE1serialInterface.setEndDate(null);
		channelizedE1serialInterface.setFirsttimeSlot(null);
		channelizedE1serialInterface.setFraming(null);
		channelizedE1serialInterface.setHoldtimeDown(null);
		channelizedE1serialInterface.setHoldtimeUp(null);
		channelizedE1serialInterface.setInterfaceName(interfaceObj.getJSONObject("SerialInterface")
				.getJSONObject("SerialInterface").get("interfaceName").toString());
		channelizedE1serialInterface.setInterfaceProtocolMappings(null);// ?

		if (interfaceObj.getJSONObject("SerialInterface").has("IPv4Address")
				&& interfaceObj.getJSONObject("SerialInterface").getJSONObject("IPv4Address").has("address")) {
			channelizedE1serialInterface.setIpv4Address(interfaceObj.getJSONObject("SerialInterface")
					.getJSONObject("IPv4Address").get("address").toString());
		}
		if (interfaceObj.getJSONObject("SerialInterface").has("IPv6Address")
				&& interfaceObj.getJSONObject("SerialInterface").getJSONObject("IPv6Address").has("address")) {
			channelizedE1serialInterface.setIpv6Address(interfaceObj.getJSONObject("SerialInterface")
					.getJSONObject("IPv6Address").get("address").toString());
		}
		channelizedE1serialInterface.setIscrcforenabled(null);
		channelizedE1serialInterface.setIsframed(null);
		channelizedE1serialInterface.setIshdlcConfig(null);
		channelizedE1serialInterface.setIshsrpEnabled(null);
		channelizedE1serialInterface.setIsvrrpEnabled(null);
		channelizedE1serialInterface.setKeepalive(null);
		channelizedE1serialInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		channelizedE1serialInterface.setLasttimeSlot(null);
		if (interfaceObj.getJSONObject("SerialInterface").has("mode"))
			channelizedE1serialInterface
					.setMode(interfaceObj.getJSONObject("ethernetInterface").get("mode").toString());
		channelizedE1serialInterface.setModifiedBy("Optimus_Port_GVPN_migration");
		channelizedE1serialInterface.setModifiedIpv4Address(null);
		channelizedE1serialInterface.setModifiedIpv6Address(null);
		channelizedE1serialInterface.setModifiedSecondaryIpv4Address(null);
		channelizedE1serialInterface.setMtu(null);
		if (interfaceObj.has("physicalPort")) {
			channelizedE1serialInterface.setPhysicalPort(interfaceObj.get("physicalPort").toString());
		}
		channelizedE1serialInterface.setPortType(null);
		if (interfaceObj.getJSONObject("SerialInterface").has("secondaryIPv4Address")
				&& interfaceObj.getJSONObject("SerialInterface").getJSONObject("secondaryIPv4Address").has("address"))
			channelizedE1serialInterface.setSecondaryIpv4Address(interfaceObj.getJSONObject("SerialInterface")
					.getJSONObject("secondaryIPv4Address").get("address").toString());

		if (interfaceObj.getJSONObject("SerialInterface").has("secondaryIPv6Address")
				&& interfaceObj.getJSONObject("SerialInterface").getJSONObject("secondaryIPv6Address").has("address"))
			channelizedE1serialInterface.setSecondaryIpv6Address(interfaceObj.getJSONObject("SerialInterface")
					.getJSONObject("secondaryIPv6Address").get("address").toString());
		channelizedE1serialInterface.setStartDate(new Timestamp(new Date().getTime()));
		channelizedE1serialInterface.setUpcount(null);
		channelizedE1serialInterfaceRepository.saveAndFlush(channelizedE1serialInterface);
		return channelizedE1serialInterface;
	}

	public EthernetInterface saveEthernetInterface(JSONObject gvpn, JSONObject interfaceObj) {

		EthernetInterface ethernetInterface = new EthernetInterface();
		ethernetInterface.setAclPolicyCriterias(null);// ?

		if (interfaceObj.has("autoNegotiationEnabled"))
			ethernetInterface.setAutonegotiationEnabled(interfaceObj.get("autoNegotiationEnabled").toString());// corrct?

		if (interfaceObj.getJSONObject("ethernetInterface").has("BFD")
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("BFD").has("isBFDEnabled")) {
			Byte isbfdEnabled;

			if (interfaceObj.getJSONObject("ethernetInterface").getJSONObject("BFD").get("isBFDEnabled").toString()
					.equalsIgnoreCase("false")) {

				isbfdEnabled = (byte) 0;

				ethernetInterface.setIsbfdEnabled(isbfdEnabled);

			} else {
				isbfdEnabled = (byte) 1;

				ethernetInterface.setIsbfdEnabled(isbfdEnabled);

				ethernetInterface.setBfdMultiplier(interfaceObj.getJSONObject("ethernetInterface").getJSONObject("BFD")
						.get("BFDMultiplier").toString());

				ethernetInterface.setBfdreceiveInterval(interfaceObj.getJSONObject("ethernetInterface")
						.getJSONObject("BFD").get("BFDReceiveinterval").toString());

				ethernetInterface.setBfdtransmitInterval(interfaceObj.getJSONObject("ethernetInterface")
						.getJSONObject("BFD").get("BFDTransmitinterval").toString());
			}
		}
		if (interfaceObj.getJSONObject("ethernetInterface").has("duplex"))
			ethernetInterface.setDuplex(interfaceObj.getJSONObject("ethernetInterface").get("duplex").toString());

		if (interfaceObj.getJSONObject("ethernetInterface").has("encapsulation"))
			ethernetInterface
					.setEncapsulation(interfaceObj.getJSONObject("ethernetInterface").get("encapsulation").toString());

		ethernetInterface.setEndDate(null);

		ethernetInterface.setFraming(null);

		ethernetInterface.setHoldtimeDown(null);

		ethernetInterface.setHoldtimeUp(null);

		ethernetInterface.setHsrpVrrpProtocols(null);// ?

		ethernetInterface.setInterfaceProtocolMappings(null);// ?

		if (interfaceObj.getJSONObject("ethernetInterface").has("vlan"))
			ethernetInterface.setInnerVlan(interfaceObj.getJSONObject("ethernetInterface").get("vlan").toString());// ?vlan

		ethernetInterface
				.setInterfaceName(interfaceObj.getJSONObject("ethernetInterface").get("interfaceName").toString());

		if (interfaceObj.getJSONObject("ethernetInterface").has("IPv4Address")
				&& interfaceObj.getJSONObject("ethernetInterface").get("IPv4Address") instanceof JSONObject
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("IPv4Address").has("address"))
			ethernetInterface.setIpv4Address(interfaceObj.getJSONObject("ethernetInterface")
					.getJSONObject("IPv4Address").get("address").toString());

		if (interfaceObj.getJSONObject("ethernetInterface").has("IPv6Address")
				&& interfaceObj.getJSONObject("ethernetInterface").get("IPv6Address") instanceof JSONObject
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("IPv6Address").has("address"))
			ethernetInterface.setIpv6Address(interfaceObj.getJSONObject("ethernetInterface")
					.getJSONObject("IPv6Address").get("address").toString());

		Byte defvalue = (byte) 0;

		ethernetInterface.setIshsrpEnabled(defvalue);

		ethernetInterface.setIsvrrpEnabled(defvalue);

		ethernetInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));

		ethernetInterface.setMediaType(null);

		if (interfaceObj.getJSONObject("ethernetInterface").has("mode"))
			ethernetInterface.setMode(interfaceObj.getJSONObject("ethernetInterface").get("mode").toString());

		ethernetInterface.setModifiedBy("Optimus_Port_GVPN_migration");

		ethernetInterface.setMtu(null);

		if (interfaceObj.getJSONObject("ethernetInterface").has("IPv4Address")
				&& interfaceObj.getJSONObject("ethernetInterface").get("IPv4Address") instanceof JSONObject
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("IPv4Address").has("address"))
			ethernetInterface.setModifiedIpv4Address(interfaceObj.getJSONObject("ethernetInterface")
					.getJSONObject("IPv4Address").get("address").toString());

		if (interfaceObj.getJSONObject("ethernetInterface").has("IPv6Address")
				&& interfaceObj.getJSONObject("ethernetInterface").get("IPv6Address") instanceof JSONObject
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("IPv6Address").has("address"))
			ethernetInterface.setModifiedIpv6Address(interfaceObj.getJSONObject("ethernetInterface")
					.getJSONObject("IPv6Address").get("address").toString());

		ethernetInterface.setModifiedSecondaryIpv4Address(null);

		ethernetInterface.setModifiedSecondaryIpv6Address(null);

		if (interfaceObj.getJSONObject("ethernetInterface").has("OuterVLAN")) {
			ethernetInterface.setOuterVlan(
					interfaceObj.getJSONObject("ethernetInterface").get("OuterVLAN").toString().toUpperCase());
		}

		if (interfaceObj.getJSONObject("ethernetInterface").has("physicalPort"))
			ethernetInterface
					.setPhysicalPort(interfaceObj.getJSONObject("ethernetInterface").get("physicalPort").toString());

		ethernetInterface.setPortType(null);

		ethernetInterface.setQosLoopinPassthrough(null);

		if (interfaceObj.getJSONObject("ethernetInterface").has("secondaryIPv4Address")
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("secondaryIPv4Address").has("address"))
			ethernetInterface.setSecondaryIpv4Address(interfaceObj.getJSONObject("ethernetInterface")
					.getJSONObject("secondaryIPv4Address").get("address").toString());

		if (interfaceObj.getJSONObject("ethernetInterface").has("secondaryIPv6Address")
				&& interfaceObj.getJSONObject("ethernetInterface").getJSONObject("secondaryIPv6Address").has("address"))
			ethernetInterface.setSecondaryIpv6Address(
					interfaceObj.getJSONObject("secondaryIPv6Address").get("address").toString());
		if (interfaceObj.getJSONObject("ethernetInterface").has("speed"))
			ethernetInterface.setSpeed(interfaceObj.getJSONObject("ethernetInterface").get("speed").toString());

		ethernetInterface.setStartDate(new Timestamp(new Date().getTime()));

		ethernetInterfaceRepository.saveAndFlush(ethernetInterface);
		return ethernetInterface;
	}

	public RouterDetail saveRouterDetail(JSONObject gvpn) {
		RouterDetail routerDetail = new RouterDetail();

		routerDetail.setEndDate(null);
		routerDetail.setInterfaceProtocolMappings(null); // ?

		if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("IPv4ManagementAddress")
				&& gvpn.getJSONObject("IPService").getJSONObject("Router")
						.get("IPv4ManagementAddress") instanceof JSONObject
				&& gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("IPv4ManagementAddress")
						.has("address"))
			routerDetail.setIpv4MgmtAddress(gvpn.getJSONObject("IPService").getJSONObject("Router")
					.getJSONObject("IPv4ManagementAddress").get("address").toString());

		if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("IPv6ManagementAddress")
				&& gvpn.getJSONObject("IPService").getJSONObject("Router")
						.get("IPv6ManagementAddress") instanceof JSONObject
				&& gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("IPv6ManagementAddress")
						.has("address"))
			routerDetail.setIpv6MgmtAddress(gvpn.getJSONObject("IPService").getJSONObject("Router")
					.getJSONObject("IPv6ManagementAddress").get("address").toString());

		routerDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		routerDetail.setModifiedBy("Optimus_Port_GVPN_migration");
		if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("hostname"))
			routerDetail.setRouterHostname(
					gvpn.getJSONObject("IPService").getJSONObject("Router").get("hostname").toString());
		routerDetail
				.setRouterMake(gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerMake").toString());
		routerDetail
				.setRouterModel(gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerModel").toString());
		if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("routerRole"))
			routerDetail.setRouterRole(
					gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerRole").toString());
		if (gvpn.getJSONObject("IPService").getJSONObject("Router").has("routerType"))
			routerDetail.setRouterType(
					gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerType").toString());
		routerDetail.setStartDate(new Timestamp(new Date().getTime()));
		routerDetailRepository.saveAndFlush(routerDetail);
		return routerDetail;
	}

	public IpAddressDetail saveIpAddressDetail(JSONObject gvpn, ServiceDetail serviceDetail1) {
		IpAddressDetail ipAddressDetail = new IpAddressDetail();
		ipAddressDetail.setEndDate(null);
		// ipAddressDetail.setExtendedLanEnabled(null);
		ipAddressDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setModifiedBy("Optimus_Port_GVPN_migration");

		/*
		 * if (gvpn.getJSONObject("IPService").has("NMSServerIPv4Address") &&
		 * gvpn.getJSONObject("IPService").getJSONObject("NMSServerIPv4Address").has(
		 * "address")) { ipAddressDetail.setNmsServiceIpv4Address(
		 * gvpn.getJSONObject("IPService").getJSONObject("NMSServerIPv4Address").get(
		 * "address").toString()); }
		 */

		ipAddressDetail.setNniVsatIpaddress(null);

		if (gvpn.getJSONObject("IPService").has("ExtendedLAN")
				&& gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").has("isEnabled")
				&& gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").get("isEnabled").toString()
						.toLowerCase().equals("true")) {
			ipAddressDetail.setExtendedLanEnabled((byte) 1);
			if (gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").has("numberOfMacAddresses"))
				ipAddressDetail.setNoMacAddress((Integer) gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN")
						.get("numberOfMacAddresses"));
		} else if (gvpn.getJSONObject("IPService").has("ExtendedLAN")
				&& gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").has("isEnabled")
				&& gvpn.getJSONObject("IPService").getJSONObject("ExtendedLAN").get("isEnabled").toString()
						.toLowerCase().equals("false")) {
			ipAddressDetail.setExtendedLanEnabled((byte) 0);
		}

		if (gvpn.getJSONObject("IPService").get("pathIPType").toString().toLowerCase().contains("v4"))
			ipAddressDetail.setPathIpType("V4");
		else if (gvpn.getJSONObject("IPService").get("pathIPType").toString().toLowerCase().contains("v6"))
			ipAddressDetail.setPathIpType("V4");
		else if (gvpn.getJSONObject("IPService").get("pathIPType").toString().toLowerCase().contains("dual"))
			ipAddressDetail.setPathIpType("DUALSTACK");

		if (gvpn.getJSONObject("IPService").has("pingAddress1")
				&& gvpn.getJSONObject("IPService").getJSONObject("pingAddress1").has("address")) {
			ipAddressDetail.setPingAddress1(
					gvpn.getJSONObject("IPService").getJSONObject("pingAddress1").get("address").toString()); // check
		}

		if (gvpn.getJSONObject("IPService").has("pingAddress2")
				&& gvpn.getJSONObject("IPService").getJSONObject("pingAddress2").has("address")) {
			ipAddressDetail.setPingAddress2(
					gvpn.getJSONObject("IPService").getJSONObject("pingAddress2").get("address").toString());
		}

		ipAddressDetail.setServiceDetail(serviceDetail1);
		ipAddressDetail.setStartDate(new Timestamp(new Date().getTime()));
		ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
		return ipAddressDetail;
	}

	public UniswitchDetail saveUniswitchDetails(JSONObject uniswitchObj, Topology topology) {
		UniswitchDetail uniswitchDetail = new UniswitchDetail();
		if (uniswitchObj.getJSONObject("uniInterface").has("autoNegotiationEnabled"))
			uniswitchDetail.setAutonegotiationEnabled(
					uniswitchObj.getJSONObject("uniInterface").get("autoNegotiationEnabled").toString());
		if (uniswitchObj.getJSONObject("uniInterface").has("duplex"))
			uniswitchDetail.setDuplex(uniswitchObj.getJSONObject("uniInterface").get("duplex").toString());
		uniswitchDetail.setEndDate(null);
		if (uniswitchObj.getJSONObject("uniInterface").has("handOff"))
			uniswitchDetail.setHandoff(uniswitchObj.getJSONObject("uniInterface").get("handOff").toString());
		if (uniswitchObj.has("hostName"))
			uniswitchDetail.setHostName(uniswitchObj.get("hostName").toString());
		if (uniswitchObj.getJSONObject("uniInterface").has("vlan"))
			uniswitchDetail.setInnerVlan(uniswitchObj.getJSONObject("uniInterface").get("vlan").toString());
		uniswitchDetail.setInterfaceName(uniswitchObj.getJSONObject("uniInterface").get("interfaceName").toString());
		uniswitchDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		uniswitchDetail.setMaxMacLimit(null);
		if (uniswitchObj.getJSONObject("uniInterface").has("mediaType"))
			uniswitchDetail.setMediaType(uniswitchObj.getJSONObject("uniInterface").get("mediaType").toString());
		uniswitchDetail.setMgmtIp(uniswitchObj.get("v4ManagementIPAddress").toString());// ?
		if (uniswitchObj.getJSONObject("uniInterface").has("mode"))
			uniswitchDetail.setMode(uniswitchObj.getJSONObject("uniInterface").get("mode").toString());
		uniswitchDetail.setModifiedBy("Optimus_Port_GVPN_migration");
		if (uniswitchObj.getJSONObject("uniInterface").has("OuterVLAN"))
			uniswitchDetail.setOuterVlan(uniswitchObj.getJSONObject("uniInterface").get("OuterVLAN").toString());
		uniswitchDetail.setPhysicalPort(uniswitchObj.getJSONObject("uniInterface").get("physicalPort").toString()); // ?
		if (uniswitchObj.getJSONObject("uniInterface").has("portType"))
			uniswitchDetail.setPortType(uniswitchObj.getJSONObject("uniInterface").get("portType").toString());
		if (uniswitchObj.getJSONObject("uniInterface").has("speed"))
			uniswitchDetail.setSpeed(uniswitchObj.getJSONObject("uniInterface").get("speed").toString());
		uniswitchDetail.setStartDate(new Timestamp(new Date().getTime()));

// **add later col in table	
//		if (uniswitchObj.getJSONObject("uniInterface").has("make"))
//			uniswitchDetail.setSwitchMake(uniswitchObj.getJSONObject("uniInterface").get("make").toString());

		if (uniswitchObj.getJSONObject("uniInterface").has("model"))
			uniswitchDetail.setSwitchModel(uniswitchObj.getJSONObject("uniInterface").get("model").toString());
		uniswitchDetail.setSyncVlanReqd(null);
		uniswitchDetail.setTopology(topology);
		uniswitchDetailRepository.saveAndFlush(uniswitchDetail);
		return uniswitchDetail;
	}

	public Topology saveTopology(JSONObject gvpn, ServiceDetail serviceDetail1) {
		Topology topology = new Topology();
		topology.setEndDate(null);
		topology.setLastModifiedDate(new Timestamp(new Date().getTime()));
		topology.setModifiedBy("Optimus_Port_GVPN_migration");
		topology.setServiceDetail(serviceDetail1);
		topology.setStartDate(new Timestamp(new Date().getTime()));
		if (gvpn.getJSONObject("IPService").getJSONObject("Router").getJSONObject("topologyInfo").has("topologyName"))
			topology.setTopologyName(gvpn.getJSONObject("IPService").getJSONObject("Router")
					.getJSONObject("topologyInfo").get("topologyName").toString());
		topologyRepository.saveAndFlush(topology);
		return topology;
	}

	public AluSchedulerPolicy saveAluSchedulerPolicy(JSONObject gvpn, ServiceDetail serviceDetail1) {
		AluSchedulerPolicy aluSchedulerPolicy = new AluSchedulerPolicy();

		aluSchedulerPolicy.setEndDate(null);
		aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aluSchedulerPolicy.setModifiedBy("Optimus_Port_GVPN_migration");
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("SAPEgressPolicy")) {
			aluSchedulerPolicy.setSapEgressPolicyname(gvpn.getJSONObject("IPService").getJSONObject("QoS")
					.getJSONObject("SAPEgressPolicy").get("Name").toString());
			Byte egressPreProv = 0;
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("SAPEgressPolicy")
					.get("isPreProvisioned").toString().equals("true")) {
				egressPreProv = 1;
			}
			aluSchedulerPolicy.setSapEgressPreprovisioned(egressPreProv);
		}

		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("SAPIngressPolicy")) {
			aluSchedulerPolicy.setSapIngressPolicyname(gvpn.getJSONObject("IPService").getJSONObject("QoS")
					.getJSONObject("SAPIngressPolicy").get("Name").toString());

			Byte ingressPreProv = 0;
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("SAPIngressPolicy")
					.get("isPreProvisioned").toString().equals("true")) {
				ingressPreProv = 1;
			}
			aluSchedulerPolicy.setSapIngressPreprovisioned(ingressPreProv);
		}

		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("ALUSchedulerPolicy")
				.has("isPreProvisioned")) {
			Byte schedPreProv = 0;
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("ALUSchedulerPolicy")
					.get("isPreProvisioned").toString().equals("true")) {
				schedPreProv = 1;
			}
			aluSchedulerPolicy.setSchedulerPolicyIspreprovisioned(schedPreProv);
		}

		aluSchedulerPolicy.setSchedulerPolicyName(gvpn.getJSONObject("IPService").getJSONObject("QoS")
				.getJSONObject("ALUSchedulerPolicy").get("Name").toString());
		aluSchedulerPolicy.setServiceDetail(serviceDetail1);
		aluSchedulerPolicy.setStartDate(new Timestamp(new Date().getTime()));
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("ALUSchedulerPolicy")
				.has("totalCIRBandwidth")) {
			aluSchedulerPolicy.setTotalCirBw(gvpn.getJSONObject("IPService").getJSONObject("QoS")
					.getJSONObject("ALUSchedulerPolicy").get("totalCIRBandwidth").toString());
		}
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("ALUSchedulerPolicy")
				.has("totalPIRBandwidth")) {
			aluSchedulerPolicy.setTotalPirBw(gvpn.getJSONObject("IPService").getJSONObject("QoS")
					.getJSONObject("ALUSchedulerPolicy").get("totalPIRBandwidth").toString());
		}

		aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
		return aluSchedulerPolicy;
	}

	public ServiceCosCriteria setCosCriteria(JSONObject cos, ServiceQo serviceQo1) {
		ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
		serviceCosCriteria.setAclPolicyCriterias(null);
		if (cos.get("BandWidth") instanceof JSONObject) {

			if (cos.getJSONObject("BandWidth").has("bandwidthUnit")
					&& cos.getJSONObject("BandWidth").has("bandwidthValue")) {
				Integer bandwidthVal = (int) Double
						.parseDouble(cos.getJSONObject("BandWidth").get("bandwidthValue").toString());
				String bandwidthUnit = cos.getJSONObject("BandWidth").get("bandwidthUnit").toString();
				Integer bandwidthValinBps = banwidthConversion(bandwidthUnit, bandwidthVal);
				serviceCosCriteria.setBwBpsunit(bandwidthValinBps.toString());
			}
		}
		serviceCosCriteria.setServiceQo(serviceQo1);
		if (cos.get("ClassificationCriteria") instanceof JSONObject) {
			if (cos.getJSONObject("ClassificationCriteria").has("ANY")) {
				serviceCosCriteria
						.setClassificationCriteria(cos.getJSONObject("ClassificationCriteria").get("ANY").toString());
			} else if (cos.getJSONObject("ClassificationCriteria").has("classificationCriteriaList")) {

				serviceCosCriteria.setClassificationCriteria(
						cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList").toString());

				if (cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList").toString()
						.toLowerCase().contains("preced")) {
					serviceCosCriteria.setClassificationCriteria("ipprecedence");
				}
				if (cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList").toString()
						.toLowerCase().contains("address")) {
					serviceCosCriteria.setClassificationCriteria("IP Address");
				}

			} else {
				serviceCosCriteria.setClassificationCriteria("ANY"); //default permfix
			}
		}

//		serviceCosCriteria.setCosName(cos.getJSONObject("ClassificationCriteria").getJSONObject("qosACL").get("name").toString());
		serviceCosCriteria.setCosName(cos.get("CoSName").toString());
		if (cos.has("percentage"))
			serviceCosCriteria.setCosPercent(cos.get("percentage").toString());

		if (cos.get("ClassificationCriteria") instanceof JSONObject
				&& cos.getJSONObject("ClassificationCriteria").has("classificationCriteriaList")
				&& cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList").toString()
						.toLowerCase().equals("dscp")
				&& cos.getJSONObject("ClassificationCriteria").get("dscpValueList") instanceof JSONObject) {

			JSONObject dscpList = cos.getJSONObject("ClassificationCriteria").getJSONObject("dscpValueList");
			if (dscpList.has("dscpValue1")) {
				serviceCosCriteria.setDhcpVal1(dscpList.get("dscpValue1").toString());
			}

			if (dscpList.has("dscpValue2")) {
				serviceCosCriteria.setDhcpVal3(dscpList.get("dscpValue2").toString());
			}

			if (dscpList.has("dscpValue3")) {
				serviceCosCriteria.setDhcpVal4(dscpList.get("dscpValue3").toString());
			}

			if (dscpList.has("dscpValue4")) {
				serviceCosCriteria.setDhcpVal5(dscpList.get("dscpValue4").toString());
			}

			if (dscpList.has("dscpValue5")) {
				serviceCosCriteria.setDhcpVal6(dscpList.get("dscpValue5").toString());
			}

			if (dscpList.has("dscpValue6")) {
				serviceCosCriteria.setDhcpVal7(dscpList.get("dscpValue6").toString());
			}

			if (dscpList.has("dscpValue7")) {
				serviceCosCriteria.setDhcpVal8(dscpList.get("dscpValue7").toString());
			}

			if (dscpList.has("dscpValue8")) {
				serviceCosCriteria.setDhcpVal2(dscpList.get("dscpValue8").toString());
			}
		}

		if (cos.get("ClassificationCriteria") instanceof JSONObject
				&& cos.getJSONObject("ClassificationCriteria").has("classificationCriteriaList")
				&& cos.getJSONObject("ClassificationCriteria").get("classificationCriteriaList").toString()
						.toLowerCase().contains("ip")
				&& cos.getJSONObject("ClassificationCriteria").has("ipPrecedentValueList")
				&& cos.getJSONObject("ClassificationCriteria").get("ipPrecedentValueList") instanceof JSONObject) {

			JSONObject ipPrecList = cos.getJSONObject("ClassificationCriteria").getJSONObject("ipPrecedentValueList");
			// JSONArray ipPrecList =
			// cos.getJSONObject("ClassificationCriteria").getJSONObject("ipPrecedentValueList");
			// //array or jsonobj

			// add null checks too
			if (ipPrecList.has("IpprecedenceVal1"))
				serviceCosCriteria.setIpprecedenceVal1(ipPrecList.get("IpprecedenceVal1").toString());
			if (ipPrecList.has("IpprecedenceVal2"))
				serviceCosCriteria.setIpprecedenceVal2(ipPrecList.get("IpprecedenceVal2").toString());
			if (ipPrecList.has("IpprecedenceVal3"))
				serviceCosCriteria.setIpprecedenceVal3(ipPrecList.get("IpprecedenceVal3").toString());
			if (ipPrecList.has("IpprecedenceVal4"))
				serviceCosCriteria.setIpprecedenceVal4(ipPrecList.get("IpprecedenceVal4").toString());
			if (ipPrecList.has("IpprecedenceVal5"))
				serviceCosCriteria.setIpprecedenceVal5(ipPrecList.get("IpprecedenceVal5").toString());
			if (ipPrecList.has("IpprecedenceVal6"))
				serviceCosCriteria.setIpprecedenceVal6(ipPrecList.get("IpprecedenceVal6").toString());
			if (ipPrecList.has("IpprecedenceVal7"))
				serviceCosCriteria.setIpprecedenceVal7(ipPrecList.get("IpprecedenceVal7").toString());
			if (ipPrecList.has("IpprecedenceVal8"))
				serviceCosCriteria.setIpprecedenceVal8(ipPrecList.get("IpprecedenceVal7").toString());
			
			if (ipPrecList.has("IPPrecedentValue1"))
				serviceCosCriteria.setIpprecedenceVal1(ipPrecList.get("IPPrecedentValue1").toString());
			if (ipPrecList.has("IPPrecedentValue2"))
				serviceCosCriteria.setIpprecedenceVal2(ipPrecList.get("IPPrecedentValue2").toString());
			if (ipPrecList.has("IPPrecedentValue3"))
				serviceCosCriteria.setIpprecedenceVal3(ipPrecList.get("IPPrecedentValue3").toString());
			if (ipPrecList.has("IPPrecedentValue4"))
				serviceCosCriteria.setIpprecedenceVal4(ipPrecList.get("IPPrecedentValue4").toString());
			if (ipPrecList.has("IPPrecedentValue5"))
				serviceCosCriteria.setIpprecedenceVal5(ipPrecList.get("IPPrecedentValue5").toString());
			if (ipPrecList.has("IPPrecedentValue6"))
				serviceCosCriteria.setIpprecedenceVal6(ipPrecList.get("IPPrecedentValue6").toString());
			if (ipPrecList.has("IPPrecedentValue7"))
				serviceCosCriteria.setIpprecedenceVal7(ipPrecList.get("IPPrecedentValue7").toString());
			if (ipPrecList.has("IPPrecedentValue8"))
				serviceCosCriteria.setIpprecedenceVal8(ipPrecList.get("IPPrecedentValue8").toString());
			
			
		}

		serviceCosCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setModifiedBy("Optimus_Port_GVPN_migration");
		serviceCosCriteria.setServiceQo(serviceQo1);
		serviceCosCriteria.setStartDate(new Timestamp(new Date().getTime()));

		return serviceCosCriteria;
	}

	private void saveCosAclEntryIpAddress(String acl_policy_name, JSONArray aclArr, JSONObject aclObj, ServiceCosCriteria serviceCosCriteria) {
		if (aclArr != null && !aclArr.isEmpty()) {
			for (Object aclOb : aclArr) {
				JSONObject aclObject = (JSONObject) aclOb;
				saveAclObj(acl_policy_name,aclObject, serviceCosCriteria);
			}
		}
		if (aclObj != null) {
			saveAclObj(acl_policy_name,aclObj, serviceCosCriteria);
		}
	}

	private void saveAclObj(String acl_policy_name, JSONObject aclObject, ServiceCosCriteria serviceCosCriteria) {

		AclPolicyCriteria aclPolicyCriteria = new AclPolicyCriteria();

		if (aclObject.has("action"))
			aclPolicyCriteria.setAction(aclObject.get("action").toString());

		if (aclObject.has("destinationSubnet"))
			aclPolicyCriteria.setDestinationSubnet(aclObject.get("destinationSubnet").toString());

		if (aclObject.has("destinationSubnetAny")
				&& aclObject.get("destinationSubnetAny").toString().toLowerCase().contains("true")) {
			aclPolicyCriteria.setDestinationAny((byte) 1);
			aclPolicyCriteria.setDestinationSubnet("ANY");
		}
		if (aclObject.has("destinationSubnetAny")
				&& aclObject.get("destinationSubnetAny").toString().toLowerCase().contains("false"))
			aclPolicyCriteria.setDestinationAny((byte) 0);

		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAcl((byte) 1);
//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(issvcQosCoscriteriaIpaddrAclName);
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrPreprovisioned((byte) 0);

		aclPolicyCriteria.setServiceCosCriteria(serviceCosCriteria);

		aclPolicyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));

		aclPolicyCriteria.setModifiedBy("Optimus_Port_GVPN_migration");

		if (aclObject.has("seqNo"))
			aclPolicyCriteria.setSequence(aclObject.get("seqNo").toString());

		if (aclObject.has("sourceSubnet"))
			aclPolicyCriteria.setSourceSubnet(aclObject.get("sourceSubnet").toString());

		if (aclObject.has("isSourceSubnetAny")
				&& aclObject.get("isSourceSubnetAny").toString().toLowerCase().contains("true")) {
			aclPolicyCriteria.setSourceAny((byte) 1);
			aclPolicyCriteria.setSourceSubnet("ANY");
		}
		if (aclObject.has("isSourceSubnetAny")
				&& aclObject.get("isSourceSubnetAny").toString().toLowerCase().contains("false"))
			aclPolicyCriteria.setSourceAny((byte) 0);

		aclPolicyCriteria.setStartDate(new Timestamp(new Date().getTime()));

		if (aclObject.has("protocol") && aclObject.get("protocol").toString().toLowerCase().contains("all"))
			aclPolicyCriteria.setProtocol("IP");
		else if (aclObject.has("protocol") && !aclObject.get("protocol").toString().toLowerCase().contains("all"))
			aclPolicyCriteria.setProtocol(aclObject.get("protocol").toString());

		if(acl_policy_name!=null && !acl_policy_name.isEmpty()) {
			aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(acl_policy_name);
		}
		aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);

	}

	public ServiceQo saveServiceQo(JSONObject gvpn, ServiceDetail serviceDetail1) {

		ServiceQo serviceQo1 = new ServiceQo();
		serviceQo1.setChildqosPolicyname(null);
		serviceQo1.setCosPackage(null);
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("QoSProfile"))
			serviceQo1.setCosProfile(gvpn.getJSONObject("IPService").getJSONObject("QoS").get("QoSProfile").toString());
		serviceQo1.setCosType(gvpn.getJSONObject("IPService").getJSONObject("QoS").get("COSType").toString());
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("COS")
				&& gvpn.getJSONObject("IPService").getJSONObject("QoS").get("COS") instanceof JSONArray) {
			for (Object object : gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONArray("COS")) {
				JSONObject cosObj = (JSONObject) object;
				if (cosObj.has("cosUpdateAction"))
					serviceQo1.setCosUpdateAction(cosObj.get("cosUpdateAction").toString());

			}
		}
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("COS")
				&& gvpn.getJSONObject("IPService").getJSONObject("QoS").get("COS") instanceof JSONObject) {
			JSONObject cosObj = (JSONObject) gvpn.getJSONObject("IPService").getJSONObject("QoS").getJSONObject("COS");
			if (cosObj.has("cosUpdateAction"))
				serviceQo1.setCosUpdateAction(cosObj.get("cosUpdateAction").toString());
		}

		serviceQo1.setEndDate(null);
		serviceQo1.setFlexiCosIdentifier(null);
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("isBandwidthApplicable")) {
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").get("isBandwidthApplicable").toString()
					.equals("true")) {
				Byte isBandwidthApplicable = 1;
				serviceQo1.setIsbandwidthApplicable(isBandwidthApplicable);
			}
		} else {
			serviceQo1.setIsbandwidthApplicable(null);
		}

		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("isDefaultFC")) { // correct?
			Byte isdefFc = 0;
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").get("isDefaultFC").toString().equals("true")) {
				isdefFc = (byte) 1;
				serviceQo1.setIsdefaultFc(isdefFc);
			}
		} else {
			serviceQo1.setIsdefaultFc(null);
		}
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("IsFlexiCoS")) {
			Byte isFlexiCoS = 0;
			if (gvpn.getJSONObject("IPService").getJSONObject("QoS").get("IsFlexiCoS").toString()
					.equalsIgnoreCase("true"))
				isFlexiCoS = 1;

			serviceQo1.setIsflexicos(isFlexiCoS);

		} else {
			serviceQo1.setIsflexicos(null);
		}

		serviceQo1.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceQo1.setModifiedBy("Optimus_Port_GVPN_migration");
		serviceQo1.setNcTraffic(null);
		/*
		 * if (ias.getJSONObject("MultiLinkQoS").has("ALUSchedulerPolicy") &&
		 * ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy").has(
		 * "TotalPIRBandwidth") &&
		 * ias.getJSONObject("MultiLinkQoS").getJSONObject("ALUSchedulerPolicy")
		 * .getJSONObject("TotalPIRBandwidth").has("bandwidthValue")) {
		 * serviceQo1.setPirBw(ias.getJSONObject("MultiLinkQoS").getJSONObject(
		 * "ALUSchedulerPolicy")
		 * .getJSONObject("TotalPIRBandwidth").get("bandwidthValue").toString());//
		 * blank for now }
		 */
		serviceQo1.setPirBwUnit(null);// blank for now
		serviceQo1.setQosPolicyname(null);
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("QosTrafficMode"))
			serviceQo1.setQosTrafiicMode(
					gvpn.getJSONObject("IPService").getJSONObject("QoS").get("QosTrafficMode").toString());
		serviceQo1.setServiceDetail(serviceDetail1);
		serviceQo1.setStartDate(new Timestamp(new Date().getTime()));
		if (gvpn.getJSONObject("IPService").getJSONObject("QoS").has("summationOfBandwidth"))
			serviceQo1.setSummationOfBw(
					gvpn.getJSONObject("IPService").getJSONObject("QoS").get("summationOfBandwidth").toString());
		serviceQoRepository.saveAndFlush(serviceQo1);
		return serviceQo1;
	}

	public ServiceDetail saveServiceDetails(JSONObject gvpn, JSONObject orderInfo, OrderDetail orderDetail,
			Integer serviceDetailVersion) {
		ServiceDetail serviceDetail1 = new ServiceDetail();
		if (gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerMake").toString().equals("ALCATEL IP")) {
			if (gvpn.getJSONObject("IPService").has("ALUServiceID"))
				serviceDetail1.setAluSvcid(gvpn.getJSONObject("IPService").get("ALUServiceID").toString());

			if (gvpn.getJSONObject("IPService").has("ALUServiceName"))
				serviceDetail1.setAluSvcName(gvpn.getJSONObject("IPService").get("ALUServiceName").toString());
		}
		if (gvpn.getJSONObject("IPService").has("burstableBandwidth")) {
			if (gvpn.getJSONObject("IPService").get("burstableBandwidth") instanceof JSONObject) {
				if (gvpn.getJSONObject("IPService").getJSONObject("burstableBandwidth").has("bandwidthValue")) {
					Double double1 = (Double) gvpn.getJSONObject("IPService").getJSONObject("burstableBandwidth")
							.get("bandwidthValue");
					serviceDetail1.setBurstableBw(double1.floatValue());
				}
				if (gvpn.getJSONObject("IPService").getJSONObject("burstableBandwidth").has("bandwidthUnit"))
					serviceDetail1.setBurstableBwUnit(gvpn.getJSONObject("IPService")
							.getJSONObject("burstableBandwidth").get("bandwidthUnit").toString());
			}
		}

		serviceDetail1.setCiscoImportMaps(null);

		if (gvpn.getJSONObject("IPService").has("CSSSAMManagerId") && gvpn.getJSONObject("IPService")
				.getJSONObject("Router").get("routerMake").toString().equals("ALCATEL IP")) {
			serviceDetail1.setCssSammgrId((Integer) gvpn.getJSONObject("IPService").get("CSSSAMManagerId"));
		}

		if (gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerMake").toString().equals("JUNIPER")) {
			serviceDetail1.setDataTransferCommit("?");// only for junipernot null check
			serviceDetail1.setDataTransferCommitUnit("?");// only for juniper not null check
			serviceDetail1.setDescription("?");// comes or not comes only for juniper
		}

		serviceDetail1.setEligibleForRevision(null);
		serviceDetail1.setEndDate(null);
		serviceDetail1.setExpediteTerminate(null);
		serviceDetail1.setExternalRefid(null);
		serviceDetail1.setIntefaceDescSvctag("GVPN"); // interfaceDescriptionServiceTag attribute also available
		serviceDetail1.setIpAddressDetails(null); // ?

		if (gvpn.getJSONObject("IPService").has("isCustomConfigRequired")) {
			Byte iscustomConfigReqd;
			if (gvpn.getJSONObject("IPService").get("isCustomConfigRequired").toString().equals("false"))
				iscustomConfigReqd = (byte) 0;
			else
				iscustomConfigReqd = (byte) 1;
			serviceDetail1.setIscustomConfigReqd(iscustomConfigReqd);
		}

		if (orderInfo != null && orderInfo.has("isDowntimeRequired")) {
			Byte isDowntimeRequired;
			if (orderInfo.get("isDowntimeRequired").toString().equals("false"))
				isDowntimeRequired = (byte) 0;
			else
				isDowntimeRequired = (byte) 1;
			serviceDetail1.setIsdowntimeReqd(isDowntimeRequired);
		}

		if (gvpn.getJSONObject("IPService").has("isIDCService")) {
			Byte isIDCService;
			if (gvpn.getJSONObject("IPService").get("isIDCService").toString().equals("false"))
				isIDCService = (byte) 0;
			else
				isIDCService = (byte) 1;
			serviceDetail1.setIsIdcService(isIDCService);
		}

		if (gvpn.getJSONObject("IPService").has("manualPostValidation")) {
			Byte manualPostValidation;
			if (gvpn.getJSONObject("IPService").get("manualPostValidation").toString().equals("false"))
				manualPostValidation = (byte) 0;
			else
				manualPostValidation = (byte) 1;
			serviceDetail1.setIsManualpostvalidationReqd(manualPostValidation);
		}

		if (gvpn.getJSONObject("IPService").has("isPortsChanged")) {
			Byte isPortsChanged;
			if (gvpn.getJSONObject("IPService").get("isPortsChanged").toString().equals("false"))
				isPortsChanged = (byte) 0;
			else
				isPortsChanged = (byte) 1;
			serviceDetail1.setIsportChanged(isPortsChanged);
		}

		if (gvpn.getJSONObject("IPService").has("isRevised")) {
			Byte isRevised;
			if (gvpn.getJSONObject("IPService").get("isRevised").toString().equals("false"))
				isRevised = (byte) 0;
			else
				isRevised = (byte) 1;
			serviceDetail1.setIsrevised(isRevised);
		}

		if (orderInfo != null && orderInfo.has("LMProviderName")) {
			serviceDetail1.setLastmileProvider(orderInfo.get("LMProviderName").toString());
		}
		if (orderInfo != null && orderInfo.has("LastMileType")) {
			serviceDetail1.setLastmileType(orderInfo.get("LastMileType").toString());
		}

		serviceDetail1.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceDetail1.setLmComponents(null);
		serviceDetail1.setMgmtType(gvpn.getJSONObject("IPService").getString("managementType"));
		serviceDetail1.setModifiedBy("Optimus_Port_GVPN_migration");
		serviceDetail1.setMulticastings(null);
		if (gvpn.has("netpRefId")) {
			serviceDetail1.setNetpRefid(gvpn.get("netpRefId").toString());
		}

		if (orderInfo != null && orderInfo.has("OldServiceID"))
			serviceDetail1.setOldServiceId(orderInfo.get("OldServiceID").toString());//

		if (gvpn.getJSONObject("IPService").has("redundancyRole"))
			serviceDetail1.setRedundancyRole(gvpn.getJSONObject("IPService").get("redundancyRole").toString());

		serviceDetail1
				.setRouterMake(gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerMake").toString());

		if (!gvpn.getJSONObject("IPService").getString("managementType").equals("UNMANAGED")
				&& gvpn.getJSONObject("IPService").has("scopeOfManagement")) {
			serviceDetail1.setScopeOfMgmt(gvpn.getJSONObject("IPService").getString("scopeOfManagement"));
		}
		if (gvpn.getJSONObject("IPService").has("serviceBandwidth")
				&& gvpn.getJSONObject("IPService").get("serviceBandwidth") instanceof JSONObject) {
			if (gvpn.getJSONObject("IPService").getJSONObject("serviceBandwidth").has("bandwidthValue")) {
				Float bandwidthValue = Float.valueOf(gvpn.getJSONObject("IPService").getJSONObject("serviceBandwidth")
						.get("bandwidthValue").toString());
				serviceDetail1.setServiceBandwidth(bandwidthValue);
			}
			if (gvpn.getJSONObject("IPService").getJSONObject("serviceBandwidth").has("bandwidthUnit"))
				serviceDetail1.setServiceBandwidthUnit(gvpn.getJSONObject("IPService").getJSONObject("serviceBandwidth")
						.get("bandwidthUnit").toString());
		}
		serviceDetail1.setServiceComponenttype(gvpn.get("componentType").toString());
		serviceDetail1.setServiceId(gvpn.get("serviceId").toString());
		serviceDetail1.setServiceQos(null);// ?
		serviceDetail1.setServiceState("ACTIVE");
		serviceDetail1.setServiceSubtype(gvpn.get("serviceSubType").toString());
		serviceDetail1.setServiceType(gvpn.get("serviceType").toString());
		serviceDetail1.setSkipDummyConfig(null);

		if (gvpn.has("vpnSolutionDetails") && gvpn.getJSONObject("vpnSolutionDetails").has("VPNSolutionID")) {// correct?
			serviceDetail1.setSolutionId(gvpn.getJSONObject("vpnSolutionDetails").get("VPNSolutionID").toString());
		}

		serviceDetail1.setSolutionName(null);// what value?
		serviceDetail1.setStartDate(new Timestamp(new Date().getTime()));
		if (gvpn.getJSONObject("IPService").has("ServiceLink")
				&& gvpn.getJSONObject("IPService").get("ServiceLink") instanceof JSONObject) {
			if (gvpn.getJSONObject("IPService").getJSONObject("ServiceLink").has("serviceId"))
				serviceDetail1.setSvclinkSrvid(
						gvpn.getJSONObject("IPService").getJSONObject("ServiceLink").get("serviceID").toString());//
			if (gvpn.getJSONObject("IPService").getJSONObject("ServiceLink").has("role"))
				serviceDetail1.setSvclinkRole(
						gvpn.getJSONObject("IPService").getJSONObject("ServiceLink").get("role").toString());//
		}
		serviceDetail1.setTrfsDate(null);// for terminated records
		serviceDetail1.setTrfsTriggerDate(null);// for terminated records
		serviceDetail1.setTriggerNccmOrder(null);

		if (gvpn.getJSONObject("IPService").getJSONObject("Router").get("routerMake").toString().equals("JUNIPER")
				&& gvpn.getJSONObject("IPService").has("UsageModel")) { // correct?
			serviceDetail1.setUsageModel(gvpn.getJSONObject("IPService").get("UsageModel").toString());
		}

		serviceDetail1.setVpnSolutions(null);
		serviceDetail1.setVrfs(null);
		serviceDetail1.setVersion(serviceDetailVersion + 1);// (serviceDetailVersion + 1);
		if (orderInfo != null && orderInfo.has("customerInfo")) {
			if (orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress").has("addressLine1"))
				serviceDetail1.setAddressLine1(orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress")
						.get("addressLine1").toString());

			if (orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress").has("addressLine2"))
				serviceDetail1.setAddressLine2(orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress")
						.get("addressLine2").toString());

			serviceDetail1.setAddressLine3(null);

			if (orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress").has("city"))
				serviceDetail1.setCity(orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress")
						.get("city").toString());

			if (orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress").has("country"))
				serviceDetail1.setCountry(orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress")
						.get("country").toString());

			if (orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress").has("pincode"))
				serviceDetail1.setPincode(orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress")
						.get("pincode").toString());
//		  serviceDetail1.setCopfId(orderInfo.get("COPFID").toString());
//		  serviceDetail1.setCustCuId(null);
//		  serviceDetail1.setCountry(orderInfo.getJSONObject("customer").getJSONObject(
//		  "Customer") .getJSONObject("customerAddress").get("country").toString());
//		  serviceDetail1.setLocation(orderInfo.getJSONObject("customer").getJSONObject(
//		  "Customer") .getJSONObject("customerAddress").get("location").toString());
//		  serviceDetail1.setLocation(orderInfo.getJSONObject("customer").getJSONObject(
//		  "Customer") .getJSONObject("customerAddress").get("pincode").toString());
			if (orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress").has("state"))
				serviceDetail1.setState(orderInfo.getJSONObject("customerInfo").getJSONObject("customerAddress")
						.get("state").toString());
		}
		serviceDetail1.setServiceOrderType("GVPN");
		serviceDetail1.setOrderType(orderInfo.get("orderType").toString());
		serviceDetail1.setOrderCategory("Customer");
		serviceDetail1.setOrderDetail(orderDetail);
		serviceDetailRepository.saveAndFlush(serviceDetail1);
		return serviceDetail1;
	}

	public OrderDetail saveOrderDetails(JSONObject gvpn, JSONObject orderInfo) {
		OrderDetail orderDetail = new OrderDetail();
		if (orderInfo.has("SFDCOpportunityID"))
			orderDetail.setSfdcOpptyId(orderInfo.get("SFDCOpportunityID").toString());
		if (orderInfo.has("customerInfo")) {
			if (orderInfo.getJSONObject("customerInfo").has("accountId"))
				orderDetail.setAccountId(orderInfo.getJSONObject("customerInfo").get("accountId").toString());
			orderDetail.setAddressLine1(null);
			orderDetail.setAddressLine2(null);
			orderDetail.setAddressLine3(null);
			if (orderInfo.getJSONObject("customerInfo").has("ALUCustomerID") && gvpn.getJSONObject("IPService")
					.getJSONObject("Router").get("routerMake").toString().equals("ALCATEL IP")) {
				orderDetail.setAluCustomerId(orderInfo.getJSONObject("customerInfo").get("ALUCustomerID").toString());
			}
		}
		orderDetail.setAsdOpptyFlag(null);
		orderDetail.setCity(null);
		if (orderInfo.has("COPFID"))
			orderDetail.setCopfId(orderInfo.get("COPFID").toString());
		orderDetail.setCustCuId(null);
		orderDetail.setCountry(null);
		if (orderInfo.has("customerInfo")) {
			if (orderInfo.getJSONObject("customerInfo").has("customerCategory"))
				orderDetail.setCustomerCategory(orderInfo.getJSONObject("customerInfo").get("customerCategory").toString());
			orderDetail.setCustomerContact(null);
			if (orderInfo.getJSONObject("customerInfo").has("CRNID"))
				orderDetail.setCustomerCrnId(orderInfo.getJSONObject("customerInfo").get("CRNID").toString());
			if (orderInfo.getJSONObject("customerInfo").has("customerEmail"))
				orderDetail.setCustomerEmail(orderInfo.getJSONObject("customerInfo").get("customerEmail").toString());
			if (orderInfo.getJSONObject("customerInfo").has("customerName"))
				orderDetail.setCustomerName(orderInfo.getJSONObject("customerInfo").get("customerName").toString());
			orderDetail.setCustomerPhoneno(null);
			if (orderInfo.getJSONObject("customerInfo").has("customerType"))
				orderDetail.setCustomerType(orderInfo.getJSONObject("customerInfo").get("customerType").toString());
			else
				orderDetail.setCustomerType("Others");
		}
		orderDetail.setEndCustomerName(null);
		orderDetail.setGroupId(orderInfo.getJSONObject("OriginatorDetails").get("groupID").toString());
		orderDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		orderDetail.setLocation(null);
		orderDetail.setModifiedBy("Optimus_Port_GVPN_migration");
		orderDetail.setOrderCategory("Customer"); // or Internal
		orderDetail.setOrderStatus(orderInfo.get("orderStatus").toString());
		orderDetail.setOrderType(orderInfo.get("orderType").toString());
		orderDetail.setOrderUuid(orderInfo.get("orderId").toString());

		String origDate = orderInfo.getJSONObject("OriginatorDetails").get("originatedDate").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateFormatVal = null;
		try {
			dateFormatVal = sdf.parse(origDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orderDetail.setOriginatorDate(new Timestamp(dateFormatVal.getTime()));

		//orderDetail.setOriginatorName(orderInfo.getJSONObject("OriginatorDetails").get("originatorName").toString());
		orderDetail.setOriginatorName("OPTIMUS");
		orderDetail.setPincode(null);

		if (orderInfo.has("customerInfo") && orderInfo.getJSONObject("customerInfo").has("SAMCustomerDescription") && gvpn.getJSONObject("IPService")
				.getJSONObject("Router").get("routerMake").toString().equals("ALCATEL IP")) {
			orderDetail.setSamCustomerDescription(
					orderInfo.getJSONObject("customerInfo").get("SAMCustomerDescription").toString());
		}
		orderDetail.setStartDate(new Timestamp(new Date().getTime()));
		orderDetail.setState(null);
		orderDetailRepository.saveAndFlush(orderDetail);
		return orderDetail;
	}

	public IpaddrLanv4Address setlanv4(JSONObject lanv4Obj, IpAddressDetail ipAddressDetail) {
		// JSONObject lanv4 = lanv4Obj.getJSONObject("LANIPAddress");
		IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
		ipaddrLanv4Address.setEndDate(null);
		Byte iscustomerprovided;
		if (lanv4Obj.get("isCustomerOwned").toString().equals("false")) {
			iscustomerprovided = (byte) 0;
		} else {
			iscustomerprovided = (byte) 1;
		}
		ipaddrLanv4Address.setIscustomerprovided(iscustomerprovided);
		ipaddrLanv4Address.setIssecondary(null); // ?
		ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
		if (lanv4Obj.has("address"))
			ipaddrLanv4Address.setLanv4Address(lanv4Obj.get("address").toString());
		ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrLanv4Address.setModifiedBy("Optimus_Port_GVPN_migration");
		ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
		return ipaddrLanv4Address;
	}

	public IpaddrWanv4Address setwanv4(JSONObject wanv4Object, IpAddressDetail ipAddressDetail) {
		IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
		ipaddrWanv4Address.setEndDate(null);
		Byte iscustomerprovidedwanv4;
		if (wanv4Object.get("isCustomerOwned").toString().equals("false")) {
			iscustomerprovidedwanv4 = (byte) 0;
		} else {
			iscustomerprovidedwanv4 = (byte) 1;
		}
		ipaddrWanv4Address.setIscustomerprovided(iscustomerprovidedwanv4);
		ipaddrWanv4Address.setIssecondary(null); // ?
		ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
		if (wanv4Object.has("address"))
			ipaddrWanv4Address.setWanv4Address(wanv4Object.get("address").toString());
		ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrWanv4Address.setModifiedBy("Optimus_Port_GVPN_migration");
		ipaddrWanv4Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
		return ipaddrWanv4Address;
	}

	public IpaddrLanv6Address setlanv6(JSONObject lanv6Obj, IpAddressDetail ipAddressDetail) {
		// JSONObject lanv6 = lanv6Obj.getJSONObject("LANIPAddress");
		IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
		ipaddrLanv6Address.setEndDate(null);
		Byte iscustomerprovided1;
		if (lanv6Obj.get("isCustomerOwned").toString().equals("false")) {
			iscustomerprovided1 = (byte) 0;
		} else {
			iscustomerprovided1 = (byte) 1;
		}
		ipaddrLanv6Address.setIscustomerprovided(iscustomerprovided1);
		ipaddrLanv6Address.setIssecondary(null); // ?
		ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
		if (lanv6Obj.has("address"))
			ipaddrLanv6Address.setLanv6Address(lanv6Obj.get("address").toString());
		ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrLanv6Address.setModifiedBy("Optimus_Port_GVPN_migration");
		ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
		ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
		return ipaddrLanv6Address;
	}

	public IpaddrWanv6Address setwanv6(JSONObject wanv6Object, IpAddressDetail ipAddressDetail) {
		IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
		ipaddrWanv6Address.setEndDate(null);
		Byte iscustomerprovidedwanv6;
		if (wanv6Object.get("isCustomerOwned").toString().equals("false")) {
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
		ipaddrWanv6Address.setModifiedBy("Optimus_Port_GVPN_migration");
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
			if (aclObject.has("name"))
				aclPolicyCriteria.setInboundIpv4AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (aclObject.get("isPreprovisioned").toString().equals("true"))
				isPrevProv = 1;
			aclPolicyCriteria.setIsinboundaclIpv4Preprovisioned(isPrevProv);
		}

		if ((key.contains("in") || key.contains("In")) && (key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsinboundaclIpv6Applied(applied);
			if (aclObject.has("name"))
				aclPolicyCriteria.setInboundIpv6AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (aclObject.get("isPreprovisioned").toString().equals("true"))
				isPrevProv = 1;
			aclPolicyCriteria.setIsinboundaclIpv6Preprovisioned(isPrevProv);
		}

		if ((key.contains("out") || key.contains("Out")) && !(key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsoutboundaclIpv4Applied(applied);
			if (aclObject.has("name"))
				aclPolicyCriteria.setOutboundIpv4AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (aclObject.get("isPreprovisioned").toString().equals("true"))
				isPrevProv = 1;
			aclPolicyCriteria.setIsoutboundaclIpv4Preprovisioned(isPrevProv);
		}

		if ((key.contains("out") || key.contains("Out")) && (key.contains("v6") || key.contains("V6"))) {
			Byte applied = 1;
			aclPolicyCriteria.setIsoutboundaclIpv6Applied(applied);
			if (aclObject.has("name"))
				aclPolicyCriteria.setOutboundIpv6AclName(aclObject.get("name").toString());
			Byte isPrevProv = 0; // if no value found for isPreProvisioned then set false
			if (aclObject.get("isPreprovisioned").toString().equals("true"))
				isPrevProv = 1;
			aclPolicyCriteria.setIsoutboundaclIpv6Preprovisioned(isPrevProv);
		}

//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAcl(issvcQosCoscriteriaIpaddrAcl);
//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(issvcQosCoscriteriaIpaddrAclName);
//		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrPreprovisioned(issvcQosCoscriteriaIpaddrPreprovisioned);
//		aclPolicyCriteria.setServiceCosCriteria(serviceCosCriteria);

		aclPolicyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aclPolicyCriteria.setModifiedBy("Optimus_Port_GVPN_migration");

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
		if (key.toLowerCase().contains("inbound")) {
			if (key.toLowerCase().contains("v4")) {
				if (policyObject.has("name"))
					policyType.setInboundIpv4PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.toLowerCase().contains("v6")) {
				if (policyObject.has("name"))
					policyType.setInboundIpv6PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}
		if (key.toLowerCase().contains("outbound")) {
			if (key.toLowerCase().contains("v4")) {
				if (policyObject.has("name"))
					policyType.setOutboundIpv4PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
			if (key.toLowerCase().contains("v6")) {
				if (policyObject.has("name"))
					policyType.setOutboundIpv6PolicyName(policyObject.get("name").toString());
				Byte preProv = 0;
				if (policyObject.get("isPreprovisioned").toString().equals("true")) {
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
				policyType.setModifiedBy("Optimus_Port_GVPN_migration");
				policyType.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeRepository.saveAndFlush(policyType);
			}
		}
		// !policyObject.has("isPreprovisioned") ||
		if (!policyObject.get("isPreprovisioned").toString().equals("true")) { // if false
			// or
			// null

			// MATCH CRITERIA
			if (policyObject.getJSONObject("matchCriteria").has("matchingCriteriaPrefixlist")
					&& policyObject.getJSONObject("matchCriteria").getJSONObject("matchingCriteriaPrefixlist")
							.get("isEnabled").toString().equals("true")) {
				PolicyCriteria policyCriteria1 = new PolicyCriteria(); // prefix
																		// list
				Byte isPreProv = 0;
				if (policyObject.getJSONObject("matchCriteria").getJSONObject("matchingCriteriaPrefixlist")
						.get("isPreprovisioned").toString().equals("true")) {
					isPreProv = 1;
				}
				Byte flag = 1;
				policyCriteria1.setMatchcriteriaPrefixlist(flag);
				if (policyObject.getJSONObject("matchCriteria").getJSONObject("matchingCriteriaPrefixlist")
						.has("PrefixListName"))
					policyCriteria1.setMatchcriteriaPrefixlistName(policyObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaPrefixlist").get("PrefixListName").toString());
				policyCriteria1.setMatchcriteriaPprefixlistPreprovisioned(isPreProv);

				policyCriteria1.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria1.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria1.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria1);

				if (policyObject.getJSONObject("matchCriteria").getJSONObject("matchingCriteriaPrefixlist")
						.get("isPreprovisioned").toString().equals("false")) {
					JSONObject prefixList = policyObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaPrefixlist")
							.getJSONObject("matchingCriteriaPrefixlistEntry");
					PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
					prefixlistConfig.setPolicyCriteria(policyCriteria1);
					prefixlistConfig.setNetworkPrefix(prefixList.get("NetworkPrefix").toString());
					prefixlistConfig.setAction(null);
					if (prefixList.has("LEValue"))
						prefixlistConfig.setLeValue(prefixList.get("LEValue").toString());
					if (prefixList.has("GEValue"))
						prefixlistConfig.setGeValue(prefixList.get("GEValue").toString());
					prefixlistConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
					prefixlistConfig.setModifiedBy("Optimus_Port_GVPN_migration");
					prefixlistConfig.setStartDate(new Timestamp(new Date().getTime()));
					prefixlistConfigRepository.saveAndFlush(prefixlistConfig);
					PolicyTypeCriteriaMapping policyTypeCriteriaMapping1 = new PolicyTypeCriteriaMapping();
					policyTypeCriteriaMapping1.setPolicyType(policyType);
					policyTypeCriteriaMapping1.setPolicyCriteriaId(policyCriteria1.getPolicyCriteriaId());
					policyTypeCriteriaMapping1.setVersion(1);
					policyTypeCriteriaMapping1.setLastModifiedDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMapping1.setModifiedBy("Optimus_Port_GVPN_migration");
					policyTypeCriteriaMapping1.setStartDate(new Timestamp(new Date().getTime()));
					policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping1);
				}
			}
			if (policyObject.getJSONObject("matchCriteria").get("matchingCriteriaProtocol").toString().equals("true")) {
				PolicyCriteria policyCriteria2 = new PolicyCriteria(); // protocol
//				policyCriteria2.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria2.setTermName(termName);
				Byte flag = 1;
				policyCriteria2.setMatchcriteriaProtocol(flag);
				policyCriteria2.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria2.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria2.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria2);
				PolicycriteriaProtocol policycriteriaProtocol = new PolicycriteriaProtocol();
				policycriteriaProtocol.setPolicyCriteria(policyCriteria2);

				if (policyObject.getJSONObject("matchCriteria").has("protocol"))
					policycriteriaProtocol.setProtocolName(
							policyObject.getJSONObject("matchCriteria").get("protocol").toString().toUpperCase());

				if (policyObject.getJSONObject("matchCriteria").has("routingProtocol"))
					policycriteriaProtocol.setProtocolName(policyObject.getJSONObject("matchCriteria")
							.get("routingProtocol").toString().toUpperCase());

				policycriteriaProtocol.setProtocolValue(null);
				policycriteriaProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policycriteriaProtocol.setModifiedBy("Optimus_Port_GVPN_migration");
				policycriteriaProtocol.setStartDate(new Timestamp(new Date().getTime()));
				policycriteriaProtocolRepository.saveAndFlush(policycriteriaProtocol);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping2 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping2.setPolicyType(policyType);
				policyTypeCriteriaMapping2.setPolicyCriteriaId(policyCriteria2.getPolicyCriteriaId());
				policyTypeCriteriaMapping2.setVersion(1);
				policyTypeCriteriaMapping2.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping2.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping2.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping2);
			}
			if (policyObject.getJSONObject("matchCriteria").get("regexASPath").toString().equals("true")) {
				PolicyCriteria policyCriteria3 = new PolicyCriteria(); // regexASPAth
//				policyCriteria3.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria3.setTermName(termName);
				Byte flag = 1;
				policyCriteria3.setMatchcriteriaRegexAspath(flag);
				policyCriteria3.setMatchcriteriaRegexAspathName(null);
				policyCriteria3.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria3.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria3.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria3);
				RegexAspathConfig regexAspathConfig = new RegexAspathConfig();
				regexAspathConfig
						.setName(policyObject.getJSONObject("matchCriteria").get("RegexASPathName").toString());
				regexAspathConfig.setAsPath(policyObject.getJSONObject("matchCriteria").get("ASPath").toString());
				regexAspathConfig.setPolicyCriteria(policyCriteria3);
				regexAspathConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
				regexAspathConfig.setModifiedBy("Optimus_Port_GVPN_migration");
				regexAspathConfig.setStartDate(new Timestamp(new Date().getTime()));
				regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping3 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping3.setPolicyType(policyType);
				policyTypeCriteriaMapping3.setPolicyCriteriaId(policyCriteria3.getPolicyCriteriaId());
				policyTypeCriteriaMapping3.setVersion(1);
				policyTypeCriteriaMapping3.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping3.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping3.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping3);
			}
			if (policyObject.getJSONObject("matchCriteria").get("neighbourCommunity").toString().equals("true")) {
				PolicyCriteria policyCriteria4 = new PolicyCriteria(); // neighbourcomm
//				policyCriteria4.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria4.setTermName(termName);
				// policyCriteria4.setMatchcriteriaNeighbourcommunityName(termObject.getJSONObject("matchCriteria")
				// .getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("Name").toString());
				Byte flag = 1;
				policyCriteria4.setMatchcriteriaNeighbourCommunity(flag);
				// PolicyCriteria p=
				// policyCriteriaRepository.saveAndFlush(policyCriteria4);
				policyCriteria4.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria4.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria4.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria4);
				NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
				neighbourCommunityConfig.setPolicyCriteria(policyCriteria4);
				if (policyObject.getJSONObject("matchCriteria").has("matchingCriteriaNeighbourCommunityEntry")
						&& policyObject.getJSONObject("matchCriteria")
								.getJSONObject("matchingCriteriaNeighbourCommunityEntry").has("NeighbourCommunity"))
					neighbourCommunityConfig.setCommunity(policyObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("NeighbourCommunity")
							.toString());
				// routingPolicyNeighbourCommunity
				if (policyObject.getJSONObject("matchCriteria").has("matchingCriteriaNeighbourCommunityEntry")
						&& policyObject.getJSONObject("matchCriteria")
								.getJSONObject("matchingCriteriaNeighbourCommunityEntry").has("Name"))
					neighbourCommunityConfig.setName(policyObject.getJSONObject("matchCriteria")
							.getJSONObject("matchingCriteriaNeighbourCommunityEntry").get("Name").toString());
				neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));

				if (policyObject.getJSONObject("matchCriteria").has("routingPolicyNeighbourCommunity")
						&& policyObject.getJSONObject("matchCriteria").getJSONObject("routingPolicyNeighbourCommunity")
								.has("neighbourCommunity"))
					neighbourCommunityConfig.setCommunity(policyObject.getJSONObject("matchCriteria")
							.getJSONObject("routingPolicyNeighbourCommunity").get("neighbourCommunity").toString());
				// routingPolicyNeighbourCommunity
				if (policyObject.getJSONObject("matchCriteria").has("routingPolicyNeighbourCommunity") && policyObject
						.getJSONObject("matchCriteria").getJSONObject("routingPolicyNeighbourCommunity").has("name"))
					neighbourCommunityConfig.setName(policyObject.getJSONObject("matchCriteria")
							.getJSONObject("routingPolicyNeighbourCommunity").get("name").toString());

				neighbourCommunityConfig.setModifiedBy("Optimus_Port_GVPN_migration");
				neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
				neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping4 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping4.setPolicyType(policyType);
				policyTypeCriteriaMapping4.setPolicyCriteriaId(policyCriteria4.getPolicyCriteriaId());
				policyTypeCriteriaMapping4.setVersion(1);
				policyTypeCriteriaMapping4.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping4.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping4.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping4);
			}
			// SETCRITERIA
			if (policyObject.getJSONObject("setCriteria").get("setCriteriaLocalPreference").toString().equals("true")) {
				PolicyCriteria policyCriteria5 = new PolicyCriteria(); // local
																		// pref
//				policyCriteria5.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria5.setTermName(termName);
				// policyCriteria5.setSetcriteriaLocalPreference(setcriteriaLocalPreference);
				Byte flag = 1;
				policyCriteria5.setSetcriteriaLocalPreference(flag);
				if (policyObject.getJSONObject("setCriteria").has("LocalPreference"))
					policyCriteria5.setSetcriteriaLocalpreferenceName(
							policyObject.getJSONObject("setCriteria").get("LocalPreference").toString());
				policyCriteria5.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria5.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria5.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria5);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping5 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping5.setPolicyType(policyType);
				policyTypeCriteriaMapping5.setPolicyCriteriaId(policyCriteria5.getPolicyCriteriaId());
				policyTypeCriteriaMapping5.setVersion(1);
				policyTypeCriteriaMapping5.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping5.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping5.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping5);
			}
			if (policyObject.getJSONObject("setCriteria").get("SetCriteriaASPathPrepend").toString().equals("true")) {
				PolicyCriteria policyCriteria6 = new PolicyCriteria(); // aspath
																		// prepend
//				policyCriteria6.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria6.setTermName(termName);
				Byte flag = 1;
				policyCriteria6.setSetcriteriaAspathPrepend(flag);
				if (policyObject.getJSONObject("setCriteria").has("ASPathPreprendIndex"))
					policyCriteria6.setSetcriteriaAspathPrependIndex(
							policyObject.getJSONObject("setCriteria").get("ASPathPreprendIndex").toString());
				if (policyObject.getJSONObject("setCriteria").has("ASPathPrepend"))
					policyCriteria6.setSetcriteriaAspathPrependName(
							policyObject.getJSONObject("setCriteria").get("ASPathPrepend").toString());
				policyCriteria6.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria6.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria6.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria6);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping6 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping6.setPolicyType(policyType);
				policyTypeCriteriaMapping6.setPolicyCriteriaId(policyCriteria6.getPolicyCriteriaId());
				policyTypeCriteriaMapping6.setVersion(1);
				policyTypeCriteriaMapping6.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping6.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping6.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping6);
			}
			if (policyObject.getJSONObject("setCriteria").get("metric").toString().equals("true")) {
				PolicyCriteria policyCriteria7 = new PolicyCriteria(); // metric
//				policyCriteria7.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria7.setTermName(termName);
				Byte flag = 1;
				policyCriteria7.setSetcriteriaMetric(flag);
				if (policyObject.getJSONObject("setCriteria").has("MEDValue"))
					policyCriteria7.setSetcriteriaMetricName(
							policyObject.getJSONObject("setCriteria").get("MEDValue").toString());
				policyCriteria7.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria7.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria7.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria7);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping7 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping7.setPolicyType(policyType);
				policyTypeCriteriaMapping7.setPolicyCriteriaId(policyCriteria7.getPolicyCriteriaId());
				policyTypeCriteriaMapping7.setVersion(1);
				policyTypeCriteriaMapping7.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping7.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping7.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping7);
			}
			if (policyObject.getJSONObject("setCriteria").has("regexASPath")
					&& policyObject.getJSONObject("setCriteria").get("regexASPath").toString().equals("true")) {
				PolicyCriteria policyCriteria8 = new PolicyCriteria(); // regexaspath
//				policyCriteria8.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria8.setTermName(termName);
				Byte flag = 1;
				policyCriteria8.setSetcriteriaRegexAspath(flag);
//					policyCriteria8.setMatchcriteriaRegexAspathName(
//							termObject.getJSONObject("setCriteria").get("setCriteriaRegexASPathName").toString());
				// one more field will be added in future
				policyCriteria8.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria8.setModifiedBy("Optimus_Port_GVPN_migration");
				policyCriteria8.setStartDate(new Timestamp(new Date().getTime()));
				policyCriteriaRepository.saveAndFlush(policyCriteria8);
				RegexAspathConfig regexAspathConfig = new RegexAspathConfig();
				if (policyObject.getJSONObject("setCriteria").has("RegexASPathName"))
					regexAspathConfig
							.setName(policyObject.getJSONObject("setCriteria").get("RegexASPathName").toString());
				if (policyObject.getJSONObject("setCriteria").has("ASPath"))
					regexAspathConfig.setAsPath(policyObject.getJSONObject("setCriteria").get("ASPath").toString());
				regexAspathConfig.setPolicyCriteria(policyCriteria8);
				regexAspathConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
				regexAspathConfig.setModifiedBy("Optimus_Port_GVPN_migration");
				regexAspathConfig.setStartDate(new Timestamp(new Date().getTime()));
				regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping8 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping8.setPolicyType(policyType);
				policyTypeCriteriaMapping8.setPolicyCriteriaId(policyCriteria8.getPolicyCriteriaId());
				policyTypeCriteriaMapping8.setVersion(1);
				policyTypeCriteriaMapping8.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping8.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping8.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping8);
			}
			if (policyObject.getJSONObject("setCriteria").get("neighbourCommunity").toString().equals("true")) {
				PolicyCriteria policyCriteria9 = new PolicyCriteria(); // neighbourcomm
//				policyCriteria9.setTermSetcriteriaAction(termSetCriteriaAction);
//				policyCriteria9.setTermName(termName);
				Byte flag = 1;
				policyCriteria9.setSetcriteriaNeighbourCommunity(flag);
				policyCriteria9.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteria9.setModifiedBy("Optimus_Port_GVPN_migration");
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
				if (policyObject.getJSONObject("setCriteria").getJSONObject("setCriteriaNeighbourCommunityEntry")
						.has("NeighbourCommunity"))
					neighbourCommunityConfig.setCommunity(policyObject.getJSONObject("setCriteria")
							.getJSONObject("setCriteriaNeighbourCommunityEntry").get("NeighbourCommunity").toString());
				if (policyObject.getJSONObject("setCriteria").getJSONObject("setCriteriaNeighbourCommunityEntry")
						.has("Name"))
					neighbourCommunityConfig.setName(policyObject.getJSONObject("setCriteria")
							.getJSONObject("setCriteriaNeighbourCommunityEntry").get("Name").toString());
				neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
				neighbourCommunityConfig.setModifiedBy("Optimus_Port_GVPN_migration");
				neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
				neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping9 = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping9.setPolicyType(policyType);
				policyTypeCriteriaMapping9.setPolicyCriteriaId(policyCriteria9.getPolicyCriteriaId());
				policyTypeCriteriaMapping9.setVersion(1);
				policyTypeCriteriaMapping9.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping9.setModifiedBy("Optimus_Port_GVPN_migration");
				policyTypeCriteriaMapping9.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping9);
			}
			// }
		}
	}

	@Transactional
	public boolean excelUpload(MultipartFile file) throws Exception {

		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			// File fileW = new File("C:\\Users\\dikshag.VSNL\\Downloads\\ill146.txt");
			// FileWriter fr = new FileWriter(fileW, true);
			// BufferedWriter br = new BufferedWriter(fr);
			// PrintWriter pr = new PrintWriter(br);

			for (Sheet sheet : workbook) {
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {

					Cell serviceId = sheet.getRow(i).getCell(0);
//					Cell subType = sheet.getRow(i).getCell(4);
//					Cell configReqdId = sheet.getRow(i).getCell(5);
					Cell serviceInstance = sheet.getRow(i).getCell(3);
					serviceId.setCellType(CellType.STRING);
					serviceInstance.setCellType(CellType.STRING);
					runMigration(i, serviceId.getStringCellValue(), serviceInstance.getStringCellValue());
				}
				/*
				 * pr.close(); br.close(); fr.close();
				 */
			}
		} catch (Exception e) {
			LOGGER.error("error in excel upload : {} ",e);
		}
		return true;
	}

	@Transactional
	public void runMigration(int i, String serviceId, String serviceInstance) throws Exception {
		Args args = new Args();
		List<String> arguments = args.getItem();
		arguments.add("--context_param serviceType=GVPN");
		arguments.add("--context_param componentType=viewCSS");
		arguments.add("--context_param serviceId=" + serviceId);
		// arguments.add("--context_param configReqId=" + configReqdId);
		arguments.add("--context_param serviceSubType=GVPN");
		// arguments.add("--context_param orderType=CHANGE_ORDER");
		arguments.add("--context_param serviceInstanceId=" + serviceInstance);
		arguments.add("--context_param readFlag=Search");
		arguments.add("--context_param serviceState=ACTIVE");
		SoapRequest soapRequest = new SoapRequest();
		soapRequest.setUrl("http://115.110.93.236:8180/L1_GVPN_ReadData_0.1/services/L1_GVPN_ReadData?wsdl");
		soapRequest.setRequestObject(args);
		soapRequest.setContextPath("com.tcl.dias.serviceactivation.datamigration.gvpn.beans");
		soapRequest.setSoapUserName("");
		soapRequest.setSoapPwd("");
		soapRequest.setConnectionTimeout(600000);
		soapRequest.setReadTimeout(600000);
		RunJobReturn result;
		result = genericWebserviceClient.doSoapCallForObject(soapRequest, RunJobReturn.class);
		String response = "INITIAL";

		if (result.getItem().get(0).getItem().toString().contains("Failure")
				|| result.getItem().get(0).getItem().toString().contains("Error")) {
			Thread.sleep(60000);
			result = genericWebserviceClient.doSoapCallForObject(soapRequest, RunJobReturn.class);
		}

		if (!CollectionUtils.isEmpty(result.getItem()) && !CollectionUtils.isEmpty(result.getItem().get(0).getItem())) {
			try {
				MigrationStatus migrationStatus = new MigrationStatus();
				String xml = result.getItem().get(0).getItem().get(4);
				JSONObject obj = XML.toJSONObject(xml);
				response = migrateGVPN(obj, serviceId,null,null,null);
				migrationStatus.setServiceCode(serviceId);
				migrationStatus.setResponse(response);
				migrationStatus.setInstanceId(serviceInstance);
				migrationStatus.setServiceType("GVPN");
				migrationStatusRepository.saveAndFlush(migrationStatus);

			} catch (Exception e) {
				LOGGER.error("Error in GVPN Migration {}", e);
			}

			// System.out.println(response + " " + i);
		}

		/*
		 * pr.println(i + "," + sheet.getRow(i).getCell(0) + "," +
		 * sheet.getRow(i).getCell(1) + "," + sheet.getRow(i).getCell(2) + "," +
		 * sheet.getRow(i).getCell(3) + "," + sheet.getRow(i).getCell(4) + "," +
		 * sheet.getRow(i).getCell(5) + "," + sheet.getRow(i).getCell(6) + "," +
		 * sheet.getRow(i).getCell(7) + "," + sheet.getRow(i).getCell(8) + "," +
		 * sheet.getRow(i).getCell(9) + "," + sheet.getRow(i).getCell(10) + "," +
		 * sheet.getRow(i).getCell(11) + "," + sheet.getRow(i).getCell(12) + "," +
		 * sheet.getRow(i).getCell(13) + "," + sheet.getRow(i).getCell(14) + "," +
		 * sheet.getRow(i).getCell(15) + "," + response);
		 */
		LOGGER.info("In GVPN migration - {}th GVPN record for serviceId {} with response {}", i, serviceId, response);

	}

	@Transactional
	public String reMigrateAll() throws Exception {

		try {
			Integer i = 0;
			Boolean doMigration = true;
			List<MigrationStatus> listGvpn = migrationStatusRepository
					.findByServiceTypeAndResponseContainingOrderByIdDesc("GVPN", "ERROR -> ");
			for (MigrationStatus msObj : listGvpn) {
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
					runMigration(i, msObj.getServiceCode(), msObj.getInstanceId());

				}
			}

		} catch (Exception e) {

			LOGGER.error("error in reMigrateGVPN upload : {} ", e);
		}
		return "SUCCESS";
	}

	@Transactional
	public String migrateSpecInst(String serviceId, String serviceInstId) throws Exception {
		try {
			Integer i = 0;
			Boolean doMigration = true;
			/*
			 * for(Map.Entry<String,String> entry : serviceIds.entrySet()) { runMigration(i,
			 * entry.getKey(), entry.getValue()); }
			 */
			runMigration(i, serviceId, serviceInstId);

		} catch (Exception e) {

			LOGGER.error("error in Migrate GVPN Specific upload : {}",e);
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
					runMigrationWi(i, serviceId.getStringCellValue(),null,null,null);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in excel upload Without Instance : {}", e);
		}
		return true;
	}

	@Transactional
	public boolean runMigrationWi(int i, String serviceId, Integer scServiceDetailsId, Integer scOrderId, String isTermination) throws Exception {
		Args args = new Args();
		boolean migrationResponse = true;
		List<String> arguments = args.getItem();
		arguments.add("--context_param serviceType=GVPN");
		arguments.add("--context_param serviceSubType=GVPN");
		arguments.add("--context_param serviceId=" + serviceId);
		arguments.add("--context_param componentType=VPN PORT");
		arguments.add("--context_param serviceState=ACTIVE");
		SoapRequest soapRequest = new SoapRequest();
		soapRequest.setUrl("http://115.110.93.236:8180/L1_GVPN_ReadData_0.1/services/L1_GVPN_ReadData?wsdl");
		soapRequest.setRequestObject(args);
		soapRequest.setContextPath("com.tcl.dias.serviceactivation.datamigration.gvpn.beans");
		soapRequest.setSoapUserName("");
		soapRequest.setSoapPwd("");
		soapRequest.setConnectionTimeout(600000);
		soapRequest.setReadTimeout(600000);
		RunJobReturn result;
		result = genericWebserviceClient.doSoapCallForObject(soapRequest, RunJobReturn.class);
		String response = "INITIAL";

		if (result.getItem().get(0).getItem().toString().contains("Failure")
				|| result.getItem().get(0).getItem().toString().contains("Error")) {
			Thread.sleep(60000);
			result = genericWebserviceClient.doSoapCallForObject(soapRequest, RunJobReturn.class);
		}

		if (!CollectionUtils.isEmpty(result.getItem()) && !CollectionUtils.isEmpty(result.getItem().get(0).getItem())) {
			MigrationStatus migrationStatus = new MigrationStatus();
			String instanceId = "";
			try {
				String xml = result.getItem().get(0).getItem().get(4);
				JSONObject obj = XML.toJSONObject(xml);
				
				if (isTermination != null && isTermination.equalsIgnoreCase("yes")) {

					ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(scServiceDetailsId).get();
					ScOrder scOrder = scOrderRepository.findById(scOrderId).get();
					response = migrateGVPN(obj, serviceId, isTermination,scServiceDetail,scOrder);

				} else {
					response = migrateGVPN(obj, serviceId, isTermination,null,null);

				}
				if(!response.equalsIgnoreCase("SUCCESS")) {
					migrationResponse = false;
				}
				migrationStatus.setServiceCode(serviceId);
				migrationStatus.setResponse(response);
				if (obj.getJSONObject("MDM_GVPN_Data").getJSONObject("OrderInfo").has("serviceInstanceId")) {
					if (obj.getJSONObject("MDM_GVPN_Data").getJSONObject("OrderInfo")
							.get("serviceInstanceId") instanceof String) {
						instanceId = obj.getJSONObject("MDM_GVPN_Data").getJSONObject("OrderInfo")
								.get("serviceInstanceId").toString().replace("[", "").replace("]", "");
					}
					if (obj.getJSONObject("MDM_GVPN_Data").getJSONObject("OrderInfo")
							.get("serviceInstanceId") instanceof JSONArray) {
						instanceId = obj.getJSONObject("MDM_GVPN_Data").getJSONObject("OrderInfo")
								.getJSONArray("serviceInstanceId").getString(0).toString();

					}
				}
				migrationStatus.setInstanceId("wi-" + instanceId);
				migrationStatus.setServiceType("GVPN");
				migrationStatusRepository.saveAndFlush(migrationStatus);

			} catch (Exception e) {
				LOGGER.error("Error in GVPN Migration Without Instance {}", e);
				migrationResponse = false;
				migrationStatus.setServiceCode(serviceId);
				migrationStatus.setResponse(e.toString());
				migrationStatus.setInstanceId("wi-" +instanceId);
				migrationStatus.setServiceType("ILL");
				migrationStatusRepository.saveAndFlush(migrationStatus);
			}
		}
		else {
			migrationResponse = false;
		}
		LOGGER.info("In GVPN migration - {}th GVPN Without Instance record for serviceId {} with response {}", i,
				serviceId, response);
		try {
			if (response.equals("INITIAL")) {
				notificationService.notifyOptimusO2CAutoMigrationFailure(null, serviceId,
						"ERROR in GVPN Migration : response=\"INITIAL\"");
			}
		} catch (Exception e1) {
			LOGGER.error("Auto GVPN migration Email sending failure for service code {} with error {}:", serviceId, e1);
		}
		return migrationResponse;
	}
}
