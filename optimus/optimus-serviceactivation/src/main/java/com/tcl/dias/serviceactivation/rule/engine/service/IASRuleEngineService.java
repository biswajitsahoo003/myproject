package com.tcl.dias.serviceactivation.rule.engine.service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ipv6.IPv6Address;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.serviceactivation.activation.constants.AceConstants;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.AluSammgrSeq;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.EthernetInterface;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.MstRegionalPopCommunity;
import com.tcl.dias.serviceactivation.entity.entities.MstServiceCommunity;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PolicycriteriaProtocol;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.repository.AluSammgrSeqRepository;
import com.tcl.dias.serviceactivation.entity.repository.BgpRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedE1serialInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedSdhInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.CpeRepository;
import com.tcl.dias.serviceactivation.entity.repository.EthernetInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRegionalPopCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstServiceCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeCriteriaMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicycriteriaProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceQoRepository;
import com.tcl.dias.serviceactivation.entity.repository.UniswitchDetailRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.CollectionUtils;

@Service
public class IASRuleEngineService extends AceBaseRuleEngine implements IRuleEngine {

	private static final Logger logger = LoggerFactory.getLogger(IASRuleEngineService.class);

	@Autowired
	private ServiceDetailRepository serviceDetailRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private RouterDetailRepository routerDetailRepository;

	@Autowired
	private CpeRepository cpeRepository;

	@Autowired
	private BgpRepository bgpRepository;

	@Autowired
	private IpAddressDetailRepository ipAddressDetailRepository;

	@Autowired
	private MstServiceCommunityRepository mstServiceCommunityRepository;

	@Autowired
	private EthernetInterfaceRepository ethernetInterfaceRepository;

	@Autowired
	private ChannelizedE1serialInterfaceRepository channelizedE1serialInterfaceRepository;
	@Autowired
	private ChannelizedSdhInterfaceRepository channelizedSdhInterfaceRepository;

	@Autowired
	private AluSammgrSeqRepository aluSammgrSeqRepository;

	@Autowired
	private PolicyCriteriaRepository policyCriteriaRepository;

	@Autowired
	private PolicyTypeRepository policyTypeRepository;

	@Autowired
	private PolicyTypeCriteriaMappingRepository policyTypeCriteriaMappingRepository;

	@Autowired
	private PolicycriteriaProtocolRepository policycriteriaProtocolRepository;

	@Autowired
	private PrefixlistConfigRepository prefixlistConfigRepository;

	@Autowired
	private MstRegionalPopCommunityRepository mstRegionalPopCommunityRepository;

	@Autowired
	private ServiceQoRepository serviceQoRepository;

	@Autowired
	private UniswitchDetailRepository uniswitchDetailRepository;
	
	@Autowired
	private ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Value("${cramer.source.system}")
	String cramerSourceSystem;

	@Transactional
	public boolean applyRule(ServiceDetail serviceDetails) throws TclCommonException {
		boolean mandatoryFields=true;

		try {
			if (serviceDetails != null) {
				OrderDetail orderDetail = serviceDetails.getOrderDetail();
				Map<String, String> manDatoryFields = findMandatoryValues(orderDetail, serviceDetails);
				applyOrderRule(orderDetail, serviceDetails, manDatoryFields);
				applyServiceDetailsRule(serviceDetails, manDatoryFields);
				applyIpAddressRule(serviceDetails, manDatoryFields);
				applyInterfaceRule(serviceDetails, manDatoryFields);
				applyRoterDetails(serviceDetails, manDatoryFields);
				applyUinRule(serviceDetails, manDatoryFields);
				applyServiceQos(serviceDetails, manDatoryFields);
				applyALUpolicyRule(serviceDetails, manDatoryFields);
				applyCPEForPeSide(serviceDetails, manDatoryFields);
				applyRFRule(serviceDetails, manDatoryFields);
				applyCESideRule(serviceDetails, manDatoryFields);
				if(!CollectionUtils.isEmpty(serviceDetails.getVrfs())){
					if(manDatoryFields.containsKey(AceConstants.ROUTER.ROUTER_MAKE)){
						createPoliciesForVrf(serviceDetails.getVrfs().stream().findFirst().get(),serviceDetails, manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE));
					}
				}
				if(Objects.nonNull(serviceDetails.getOrderSubCategory()) && serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel")){
					logger.info("IAS Rule Engine:Parallel Up/DownTime");
					serviceDetails.setIsdowntimeReqd((byte)1);
					serviceDetails.setSkipDummyConfig((byte)1);
					serviceDetailRepository.save(serviceDetails);
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception in applyRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		
		return mandatoryFields;
	}

	private String addCountIpv6Address(String ipAddress,Integer count) {
		IPv6Address current = IPv6Address.fromString(ipAddress);
		IPv6Address result = null;
		result = current.add(count);
		String resultIp = null;
		if (result != null) {
			resultIp = result.toString();
		}
		return resultIp;
	}
	
	private void applyCPEForPeSide(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {

			List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetails
					.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
							&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
					.collect(Collectors.toList());
			applyCpeRule(serviceDetails, manDatoryFields, cpeProtocolMappings);
		} catch (Exception e) {
			logger.error("Exception in applyCPEForPeSide => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void applyCESideRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream()
					.filter(ser -> (ser.getIscpeWanInterface()!=null &&ser.getIscpeWanInterface() == 1 && ser.getEndDate() == null)
							&& (ser.getChannelizedE1serialInterface() != null || ser.getEthernetInterface() != null
									|| ser.getChannelizedSdhInterface() != null))
					.collect(Collectors.toList());

			applyInterfaceRuleForCESide(interfaceProtocolMappings, serviceDetails, manDatoryFields);
			applyCPEforCESide(serviceDetails, manDatoryFields);

		} catch (Exception e) {
			logger.error("Exception in applyCESideRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void applyCPEforCESide(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
			List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetails
					.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null && serIn.getIscpeWanInterface()!=null
							&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
					.collect(Collectors.toList());

			applyCpeRule(serviceDetails, manDatoryFields, cpeProtocolMappings);
		} catch (Exception e) {
			logger.error("Exception in applyCPEforCESide => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	private void applyInterfaceRuleForCESide(List<InterfaceProtocolMapping> interfaceProtocolMappings,
			ServiceDetail serviceDetails, Map<String, String> map) throws TclCommonException {

		try {
			if (!interfaceProtocolMappings.isEmpty()) {
				interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);
				if (interfaceProtocolMapping != null) {

					if (interfaceProtocolMapping.getEthernetInterface() != null) {
						appyEthernetRule(interfaceProtocolMapping.getEthernetInterface(), map, serviceDetails,"CE");
					}

					else if (interfaceProtocolMapping.getChannelizedSdhInterface() != null) {
						applyChannelizedSdhInterfaceRule(interfaceProtocolMapping.getChannelizedSdhInterface(), map,
								serviceDetails,"CE");
					}

					else if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null) {
						applyChannelizedE1serialInterface(interfaceProtocolMapping.getChannelizedE1serialInterface(),
								map, serviceDetails,"CE");
					}

					applyRoutingProtocal(interfaceProtocolMapping, map, serviceDetails,"CE");

				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyInterfaceRuleForCESide => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void applyServiceQos(ServiceDetail serviceDetails, Map<String, String> mandatoryFields)
			throws TclCommonException {

		try {

			ServiceQo serviceQo = serviceDetails.getServiceQos().stream().filter(serQo -> serQo.getEndDate() == null)
					.findFirst().orElse(null);

			if (serviceQo != null) {
				logger.info("applyServiceQos  started {}");
				if (serviceQo.getCosType() == null) {
					serviceQo.setCosType(AceConstants.COSPROFILE.COS6);
				}
				if (serviceQo.getCosUpdateAction() == null) {
					serviceQo.setCosUpdateAction("COMPLETE");
				}
				if (serviceQo.getCosProfile() == null) {
					serviceQo.setCosProfile(AceConstants.COSPROFILE.STANDARD);
				}

				if (serviceQo.getQosTrafiicMode() == null) {
					serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.UNICAST);
				}
				serviceQo.setIsdefaultFc((byte) 1);
				serviceQo.setIsbandwidthApplicable((byte) 1);
				
				Float bandWidth = maxBanwidth(
						banwidthConversion(serviceDetails.getServiceBandwidthUnit(),
								serviceDetails.getServiceBandwidth()),
						banwidthConversion(serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));

				if (bandWidth != null && bandWidth > 0) {
					serviceQo.setPirBw(String.valueOf(bandWidth));
					serviceQo.setPirBwUnit(AceConstants.BANDWIDTHUNIT.KBPS);
				}
				
				serviceQo.setModifiedBy(MODIFIEDBY);
				serviceQo.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				serviceQoRepository.save(serviceQo);
			}
		} catch (Exception e) {
			logger.error("Exception in applyServiceQos => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void applyUinRule(ServiceDetail serviceDetails, Map<String, String> map) throws TclCommonException {
		try {
			if (serviceDetails.getTopologies() != null) {
				logger.info("applyUinRule  started for service id {}",serviceDetails.getId());

				String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

				Topology topology = serviceDetails.getTopologies().stream().filter(top -> top.getEndDate() == null)
						.findFirst().orElse(null);
				if (topology != null && topology.getUniswitchDetails() != null) {
					UniswitchDetail uniswitchDetail = topology.getUniswitchDetails().stream()
							.filter(uni -> uni.getEndDate() == null).findFirst().orElse(null);
					if (uniswitchDetail != null) {
						if (uniswitchDetail.getInnerVlan() == null
								&& map.containsKey(AceConstants.INTERFACE.ETHRNER_INNERV_LAN)) {
							uniswitchDetail.setInnerVlan(map.get(AceConstants.INTERFACE.ETHRNER_INNERV_LAN));
						}
						/*if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

							uniswitchDetail.setMode(AceConstants.INTERFACE.MODE);
						} else if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							uniswitchDetail.setMode(null);

						}*/
						
						if (uniswitchDetail.getHandoff() != null
								&& uniswitchDetail.getHandoff().equalsIgnoreCase("VC")) {
							uniswitchDetail.setOuterVlan(map.get(AceConstants.DEFAULT.ETHERNET_OUTERVLAN));
						}
						uniswitchDetail.setSpeed(AceConstants.DEFAULT.NOT_APPLICABLE);
						uniswitchDetail.setAutonegotiationEnabled(AceConstants.DEFAULT.NOT_APPLICABLE);
						uniswitchDetail.setDuplex(AceConstants.DEFAULT.NOT_APPLICABLE);
						uniswitchDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						uniswitchDetail.setModifiedBy(MODIFIEDBY);
						uniswitchDetailRepository.save(uniswitchDetail);
					}

				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyUinRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void applyRoterDetails(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
			logger.info("applyRoterDetails  started for service Id {}",serviceDetails.getId());

			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream().filter(serIn -> serIn.getRouterDetail() != null && serIn.getEndDate() == null)
					.collect(Collectors.toList());

			if (!interfaceProtocolMappings.isEmpty()) {
				interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);

				if (interfaceProtocolMapping != null && interfaceProtocolMapping.getRouterDetail() != null) {
					RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
					routerDetail.setRouterType(AceConstants.ROUTER.ROUTER_MPLS);
					routerDetail.setRouterRole(AceConstants.ROUTER.ROUTER_PE);
					routerDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					routerDetail.setModifiedBy(MODIFIEDBY);
					routerDetailRepository.save(routerDetail);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyRoterDetails => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	public void applyServiceDetailsRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
			logger.info("serviceDetails  started {}");
			String vrfName = manDatoryFields.get(AceConstants.VRF.VRF_NAME);

			String routerMake = manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE);

			serviceDetails.setIsdowntimeReqd((byte) 0);
			serviceDetails.setExpediteTerminate((byte) 0);
			serviceDetails.setExternalRefid(null);
			serviceDetails.setServiceState(AceConstants.SERVICE.ISSUED);
			serviceDetails.setTriggerNccmOrder((byte) 0);
			serviceDetails.setIscustomConfigReqd((byte) 0);
			serviceDetails.setIntefaceDescSvctag(null);
			serviceDetails.setRouterMake(routerMake);
			serviceDetails.setIsIdcService((byte) 0);
			serviceDetails.setSkipDummyConfig((byte)1);
			serviceDetails.setOrderType("NEW");

			if (serviceDetails.getSolutionId() == null && serviceDetails.getOrderDetail().getCustomerName() != null) {
				serviceDetails.setSolutionId(splitCustomerName(serviceDetails.getOrderDetail().getCustomerName()));
			}
			if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
				if (serviceDetails.getUsageModel() == null) {
					serviceDetails.setUsageModel(AceConstants.SERVICE.FIXED);
				}
			}

			if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerMake)) {
				if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(vrfName)) {
					serviceDetails.setCssSammgrId(4755);
					serviceDetails.setAluSvcName(AceConstants.SERVICE.ALUSVCNAME_INTERNET_VPN_INTERNET_VPN_S);

				} else if (AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(vrfName)) {

					serviceDetails.setCssSammgrId(874);
					serviceDetails.setAluSvcName(AceConstants.SERVICE.ALUSVCNAME_ILL_PRIMUS_INTERNET_S);

				} else {

					Integer cssSamcustomerId = 0;// for ill ,it should be increment

					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId());
					logger.info("{} aluSammgrSeq {}",serviceDetails.getServiceId(),aluSammgrSeq);
					if (aluSammgrSeq != null) {
						cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
						logger.info("cssSamcustomerId= {}",cssSamcustomerId);
					}
					serviceDetails.setCssSammgrId(cssSamcustomerId);
					serviceDetails.setAluSvcName(serviceDetails.getOrderDetail().getSamCustomerDescription() + "_"
							+ serviceDetails.getCssSammgrId());

				}
			}
			serviceDetails.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			serviceDetails.setModifiedBy(MODIFIEDBY);

			serviceDetailRepository.save(serviceDetails);
		} catch (Exception e) {
			logger.error("Exception in applyServiceDetailsRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void applyCpeRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields,
			List<InterfaceProtocolMapping> cpeProtocolMappings) throws TclCommonException {

		try {
			logger.info("applyCpeRule  started {}");

			if (!cpeProtocolMappings.isEmpty()) {
				cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);

				if (cpeProtocolMapping != null && cpeProtocolMapping.getCpe() != null) {
					Cpe cpe = cpeProtocolMapping.getCpe();
					cpe.setSnmpServerCommunity("t2c2l2com");
					cpe.setIsaceconfigurable((byte) 0);
					cpe.setSendInittemplate((byte) 0);
					cpe.setModel(null);
					cpe.setServiceId(serviceDetails.getServiceId());
					cpe.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					cpe.setModifiedBy(MODIFIEDBY);
					cpeRepository.save(cpe);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyCpeRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	public void applyOrderRule(OrderDetail orderDetail, ServiceDetail serviceDetails, Map<String, String> map)
			throws TclCommonException {
		try {
			logger.info("applyOrderRule  started {}");

			String vrfName = map.get(AceConstants.VRF.VRF_NAME);
			String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			logger.info("Router routerName details{}:", routerName);

			//orderDetail.setOrderCategory(AceConstants.ORDER.CUSTOMER);
			if (orderDetail.getOptyBidCategory() == null) {
				orderDetail.setOptyBidCategory("CAT 1-2");
			}
			if (orderDetail.getOptyBidCategory().equalsIgnoreCase("CAT 1-2")) {
				orderDetail.setAsdOpptyFlag((byte) 0);
			} else {
				orderDetail.setAsdOpptyFlag((byte) 1);

			}
			//orderDetail.setOrderType(AceConstants.ORDER.NEW);
			orderDetail.setGroupId(AceConstants.ORDER.CMIP);
			//orderDetail.setOriginatorName(AceConstants.ORDER.OPTIMUS);
			orderDetail.setOriginatorName(cramerSourceSystem);
			orderDetail.setModifiedBy(orderDetail.getOriginatorName());
			orderDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			orderDetail.setOriginatorDate(new Timestamp(System.currentTimeMillis()));
			orderDetail.setLocation(orderDetail.getCity());
			orderDetail.setOrderStatus(AceConstants.ORDER.IN_PROGRESS);// other status are cancelled ,closed,jeopardy
			orderDetail.setOrderUuid(AceConstants.ORDER.SERVICE_NEW_ + orderDetail.getExtOrderrefId());
			if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
				if (vrfName.equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)) {
					orderDetail.setSamCustomerDescription(AceConstants.ORDER.INTERNET_VPN);
				} else if (vrfName.equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {

					orderDetail.setSamCustomerDescription(AceConstants.ORDER.PRIMUS_INTERNET);

				} else if (map.containsKey(AceConstants.VRF.VRF_NAME)

						&& map.get(AceConstants.VRF.VRF_NAME).equalsIgnoreCase(AceConstants.DEFAULT.NOT_APPLICABLE)) {
					if (orderDetail.getCustomerName().length() > 18) {
						orderDetail.setSamCustomerDescription(
								orderDetail.getCustomerName().substring(0, 17).replaceAll(" ", "_"));

					} else {

						orderDetail.setSamCustomerDescription(orderDetail.getCustomerName().replaceAll(" ", "_"));
					}

				}

				if (vrfName.equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)) {
					orderDetail.setAluCustomerId(AceConstants.ORDER.ALU4755);
				} else if (vrfName.equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {
					orderDetail.setAluCustomerId(AceConstants.ORDER.ALU584);

				} else if (vrfName != null) {
					Integer cssSamcustomerId = 0;// for ill ,it should be increment

					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId());
					logger.info("{} aluSammgrSeq {}",serviceDetails.getServiceId(),aluSammgrSeq);
					if (aluSammgrSeq != null) {
						cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
						logger.info("{} cssSamcustomerId {}",serviceDetails.getServiceId(),cssSamcustomerId);
					}
					orderDetail.setAluCustomerId(String.valueOf(cssSamcustomerId));

				}
			}

			if (orderDetail.getCustomerCategory() == null) {
				orderDetail.setCustomerCategory("Enterprise");
			}
			orderDetail.setModifiedBy(MODIFIEDBY);
			orderDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			orderDetailRepository.save(orderDetail);
		} catch (Exception e) {
			logger.error("Exception in applyOrderRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}
	
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public AluSammgrSeq getAluSamMgrSeq(String serviceId) {			
		return aluSammgrSeqRepository.findFirstByServiceIdOrderBySammgrSeqidDesc(serviceId);	
	}

	private void applyInterfaceRule(ServiceDetail serviceDetails, Map<String, String> map) throws TclCommonException {

		try {
			logger.info("applyInterfaceRule  started {}");


			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());// based
													// on
													// version
			if (!interfaceProtocolMappings.isEmpty()) {
				interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);
				if (interfaceProtocolMapping != null) {

					if (interfaceProtocolMapping.getEthernetInterface() != null) {
						appyEthernetRule(interfaceProtocolMapping.getEthernetInterface(), map, serviceDetails,"PE");
					}

					else if (interfaceProtocolMapping.getChannelizedSdhInterface() != null) {
						applyChannelizedSdhInterfaceRule(interfaceProtocolMapping.getChannelizedSdhInterface(), map,
								serviceDetails,"PE");
					}

					else if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null) {
						applyChannelizedE1serialInterface(interfaceProtocolMapping.getChannelizedE1serialInterface(),
								map, serviceDetails,"PE");
					}

					applyRoutingProtocal(interfaceProtocolMapping, map, serviceDetails,"PE");

				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyInterfaceRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void applyIpAddressRule(ServiceDetail serviceDetails, Map<String, String> map) throws TclCommonException {

		try {

			logger.info("applyIpAddressRule  started {}");

			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

			if (ipAddressDetail != null) {
				ipAddressDetail.setPingAddress1(AceConstants.IPADDRESS.PINGADDRESS1);

				if (map.containsKey(AceConstants.IPADDRESS.MANAGEMENTTYPE) && map
						.get(AceConstants.IPADDRESS.MANAGEMENTTYPE).equalsIgnoreCase(AceConstants.IPADDRESS.MANAGED)) {
					ipAddressDetail.setNmsServiceIpv4Address(AceConstants.IPADDRESS.NMSSERVICEIPV4ADDRESS);
				} else {
					ipAddressDetail.setNmsServiceIpv4Address(null);
					ipAddressDetail.setPingAddress2(null);

				}

				if (map.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED)
						&& map.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("true")) {
					if (ipAddressDetail.getIpaddrWanv4Addresses() != null) {
						ipAddressDetail.getIpaddrWanv4Addresses().forEach(wanv4 -> {
							if (wanv4 != null) {
								wanv4.setEndDate(new Timestamp(System.currentTimeMillis()));
							}
						});
					}

					if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {
						ipAddressDetail.getIpaddrWanv6Addresses().forEach(wanv6 -> {
							if (wanv6 != null) {
								wanv6.setEndDate(new Timestamp(System.currentTimeMillis()));
							}
						});
					}
					ipAddressDetail.setNoMacAddress(findNoMac(ipAddressDetail));

				}

				ipAddressDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				ipAddressDetail.setModifiedBy(MODIFIEDBY);

				ipAddressDetailRepository.save(ipAddressDetail);

			}
		} catch (Exception e) {
			logger.error("Exception in applyIpAddressRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void applyRoutingProtocal(InterfaceProtocolMapping interfaceProtocolMapping, Map<String, String> map,
			ServiceDetail serviceDetails,String type) throws TclCommonException {

		try {

			logger.info("applyRoutingProtocal  started {}");

			if (interfaceProtocolMapping.getBgp() != null) {
				applyBgpRule(interfaceProtocolMapping.getBgp(), map, serviceDetails,type);
			} else if (interfaceProtocolMapping.getStaticProtocol() != null) {
				applyStaticRule(interfaceProtocolMapping.getStaticProtocol(), serviceDetails,type,map);
			}
		} catch (Exception e) {
			logger.error("Exception in applyRoutingProtocal => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void applyStaticRule(StaticProtocol staticProtocol, ServiceDetail serviceDetail, String type, Map<String, String> map) throws TclCommonException {

		try {
			logger.info("applyStaticRule  started {}");

			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetail.getInterfaceProtocolMappings()
					.stream().filter(serIn -> serIn.getRouterDetail() != null && serIn.getEndDate() == null)
					.collect(Collectors.toList());
			interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			
			ScComponentAttribute lanPoolRoutingNeededAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetail.getScServiceDetailId(), "lanPoolRoutingNeeded", "LM", "A");
			if (interfaceProtocolMappings != null && !interfaceProtocolMappings.isEmpty()) {
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);
				
				if(type.equalsIgnoreCase("PE") && map.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED)
						&&! map.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("TRUE") && ((lanPoolRoutingNeededAttribute!=null && "Yes".equalsIgnoreCase(lanPoolRoutingNeededAttribute.getAttributeValue())) 
								 || (lanPoolRoutingNeededAttribute==null))){ // lanPoolRoutingNeededAttribute might be null for old case
				saveWanStaticRoutes(serviceDetail, interfaceProtocolMapping.getRouterDetail(), staticProtocol,type);
			}
			}
		} catch (Exception e) {
			logger.error("Exception in applyStaticRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
	

	private void applyBgpRule(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails, String type)
			throws TclCommonException {

		try {
			logger.info("applyBgpRule  started {}");

			String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			
			if (bgp.getNeighborOn() == null) {

				bgp.setNeighborOn(AceConstants.IPADDRESS.WAN);
			}
			
			if(bgp.getIsauthenticationRequired()==null || bgp.getIsauthenticationRequired()==0) {
				bgp.setAuthenticationMode(null);
				bgp.setPassword(null);
				
			}
			
			bgp.setSooRequired((byte) 0);
			if (bgp.getRoutesExchanged() == null) {
				bgp.setRoutesExchanged(AceConstants.PROTOCOL.ROUTESEXCHANGED_DEFAULT);
			}
			else {
				if (bgp.getRoutesExchanged().toUpperCase().contains(AceConstants.PROTOCOL.DEFAULT)) {
					bgp.setRoutesExchanged(AceConstants.PROTOCOL.ROUTESEXCHANGED_DEFAULT);

				} else if (bgp.getRoutesExchanged().toUpperCase().contains(AceConstants.PROTOCOL.PARTIAL)) {

					bgp.setRoutesExchanged(AceConstants.PROTOCOL.ROUTESEXCHANGED_PARTIAL);

				} else if (bgp.getRoutesExchanged().toUpperCase().contains(AceConstants.PROTOCOL.FULL)) {
					bgp.setRoutesExchanged(AceConstants.PROTOCOL.ROUTESEXCHANGED_FULL);

				}
			}
			bgp.setAsoOverride((byte) 1);
			
			

			if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerName)) {
				bgp.setIsmultihopTtl("10");

				bgp.setMaxPrefix(AceConstants.PROTOCOL.SETMAXPREFIX2000);
				bgp.setMaxPrefixThreshold(AceConstants.PROTOCOL.SETMAXPREFIXTHRESHOLD);
				bgp.setIsrtbh((byte) 1);
				bgp.setRtbhOptions(AceConstants.PROTOCOL.RTBH);

				if (bgp.getNeighborOn() != null
						&& bgp.getNeighborOn().equalsIgnoreCase(AceConstants.PROTOCOL.LOOPBACK)) {
					bgp.setIsmultihopTtl(AceConstants.PROTOCOL.ISMULTIHOPTTL);
					bgp.setIsebgpMultihopReqd((byte) 1);
				} else {
					bgp.setIsebgpMultihopReqd((byte) 0);
					bgp.setIsmultihopTtl(null);

				}

			} else if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerName)) {
				if (bgp.getNeighborOn().equalsIgnoreCase(AceConstants.PROTOCOL.LOOPBACK)) {
					bgp.setIsebgpMultihopReqd((byte) 1);
				} else {
					bgp.setIsebgpMultihopReqd((byte) 0);
				}
				if (bgp.getHelloTime() != null) {
					bgp.setAluKeepalive(bgp.getHelloTime());
				}
			}

			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetails.getScServiceDetailId(), "asNumber", "LM", "A");
			if (scComponentAttribute != null
					&& scComponentAttribute.getAttributeValue().equalsIgnoreCase("Customer public AS Number")) {
				bgp.setIsbgpMultihopReqd((byte) 1);
			}else{
				bgp.setIsbgpMultihopReqd((byte) 0);
			}

			if (map.containsKey(AceConstants.VRF.VRF_NAME) 
					&& routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				String vrfName = map.get(AceConstants.VRF.VRF_NAME);
				String serviceCode = map.get(AceConstants.SERVICE.SERVICEID);

				String code = serviceCode.substring(3, serviceCode.length());
				if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(vrfName)) {

					bgp.setBgpPeerName(AceConstants.DEFAULT.INTERNET + "_4755_" + code);// remove
																						// 091
																						// in
																						// service
																						// id

				} else if (AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(vrfName)) {
					bgp.setBgpPeerName(AceConstants.DEFAULT.PRIMUS + "_874" + code);

				} else if (AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(vrfName)
						&& map.containsKey(AceConstants.SERVICE.CUSTOMERNAME)) {
					
					Integer cssSammgrId = serviceDetails.getCssSammgrId();
					logger.info("cssSammgrId={}",cssSammgrId);
					bgp.setBgpPeerName(splitName(map.get(AceConstants.SERVICE.CUSTOMERNAME), 4)+"_"
							+ cssSammgrId + "_" + code);

				}

				bgp.setLocalAsNumber(AceConstants.PROTOCOL.LOCALASNUMBER);

			}

			if (bgp.getRemoteAsNumber() != null && bgp.getRemoteAsNumber() > 1 && bgp.getRemoteAsNumber() < 65535) {
				bgp.setAsoOverride((byte) 1);
			}

			else {
				bgp.setAsoOverride((byte) 0);

			}

			if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerName)) {
				bgp.setRedistributeConnectedEnabled((byte) 1);
				bgp.setRedistributeStaticEnabled((byte) 1);
				bgp.setIsbgpNeighbourInboundv4RoutemapEnabled((byte) 0);
				bgp.setIsbgpNeighbourInboundv6RoutemapEnabled((byte) 0);
				bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned((byte) 0);
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 0);

			}
			if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerName)) {
				bgp.setAluDisableBgpPeerGrpExtCommunity((byte) 1);
				bgp.setPeerType(AceConstants.ROUTER.EXTERNAL);
				bgp.setAluBackupPath(AceConstants.PROTOCOL.NOT_APPLICABLE);

			}

			if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
					|| routerName.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				bgp.setRedistributeConnectedEnabled(null);
				bgp.setRedistributeStaticEnabled(null);
			}

			if (bgp.getNeighborOn() == null || AceConstants.IPADDRESS.WAN.equalsIgnoreCase(bgp.getNeighborOn())) {

				if (map.containsKey(AceConstants.CPE.CPE_INTERFACE_IPV4)) {

					bgp.setRemoteIpv4Address(map.get(AceConstants.CPE.CPE_INTERFACE_IPV4) /*+ "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS))*/);
				}
				if (map.containsKey(AceConstants.CPE.CPE_INTERFACE_IPV6)) {
					bgp.setRemoteIpv6Address(map.get(AceConstants.CPE.CPE_INTERFACE_IPV6) /*+ "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS))*/);
				}

			}

			else if (bgp.getNeighborOn() != null
					&& bgp.getNeighborOn().equalsIgnoreCase(AceConstants.PROTOCOL.LOOPBACK)) {
				bgp.setRemoteIpv4Address(bgp.getRemoteUpdateSourceIpv4Address());
				bgp.setRemoteIpv6Address(bgp.getRemoteUpdateSourceIpv6Address());
			}

			if ((routerName.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP) 
					|| routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU))
					&& map.containsKey(AceConstants.IPADDRESS.IPPATH) 
					&& map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
					&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)
					&& (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase(AceConstants.IPADDRESS.IPV6)
					|| AceConstants.IPADDRESS.DUAL.equalsIgnoreCase(map.get(AceConstants.IPADDRESS.IPPATH)))) {

				bgp.setV6LocalPreference(AceConstants.PROTOCOL.SETV6LOCALPREFERENCE);

			}

			if ((routerName.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP) 
					|| routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU))
					&& map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
					&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)
					&& map.containsKey(AceConstants.IPADDRESS.IPPATH)
					&& (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase(AceConstants.IPADDRESS.IPV4)
					|| AceConstants.IPADDRESS.DUAL.equalsIgnoreCase(map.get(AceConstants.IPADDRESS.IPPATH)))) {

				bgp.setLocalPreference(AceConstants.PROTOCOL.SETLOCALPREFERENCE);

			}

			if (bgp.getRoutesExchanged() == null) {
				bgp.setRoutesExchanged(AceConstants.PROTOCOL.DEFAULTROUTE);
			}
			bgp.setModifiedBy(MODIFIEDBY);
			bgp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			if(bgp.getPassword()!=null) {
				bgp.setAuthenticationMode("MD5");
				bgp.setIsauthenticationRequired((byte) 1);
			}
			OrderDetail orderDetail=serviceDetails.getOrderDetail();
			if(orderDetail!=null && orderDetail.getExtOrderrefId()!=null && orderDetail.getExtOrderrefId().toLowerCase().contains("izosdwan")){
				logger.info("IAS.applyBgpRule Setting Local preference for sdwan");
				bgp.setV6LocalPreference(null);
				bgp.setLocalPreference(null);
			}
			bgpRepository.save(bgp);
			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream().filter(serIn -> serIn.getRouterDetail() != null && serIn.getEndDate() == null)
					.collect(Collectors.toList());
			interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());

			if (interfaceProtocolMappings != null && !interfaceProtocolMappings.isEmpty()) {
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);
				
				if (type.equalsIgnoreCase("PE") && map.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED)
						&&! map.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("TRUE")) {
					addBgpWanStaticRoutes(serviceDetails, interfaceProtocolMapping.getRouterDetail(), bgp);

				}
			}
			applyBgpPolicyType(bgp, map, serviceDetails);
		} catch (Exception e) {
			logger.error("Exception in applyBgpRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}
	
	public void addBgpWanStaticRoutes(ServiceDetail serviceDetail, RouterDetail routerDetail, Bgp bgp) {

		if (Objects.nonNull(routerDetail)
				&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

			List<MstRegionalPopCommunity> mstRegionalPopCommunityList = mstRegionalPopCommunityRepository
					.findByRouterHostname(routerDetail.getRouterHostname());
			if (!mstRegionalPopCommunityList.isEmpty()) {
				WanStaticRoute wanStaticRoute = new WanStaticRoute();
				wanStaticRoute.setPopCommunity(mstRegionalPopCommunityList.get(0).getPopCommunity());
				wanStaticRoute.setRegionalCommunity(mstRegionalPopCommunityList.get(0).getRegionalCommunity());

				List<MstServiceCommunity> mstServiceCommunityList = mstServiceCommunityRepository
						.findByServiceSubtype(serviceDetail.getServiceSubtype());
				if (!mstServiceCommunityList.isEmpty()) {
					wanStaticRoute.setServiceCommunity(mstServiceCommunityList.get(0).getServiceCmmunity());
				}

				wanStaticRoute.setBgp(bgp);
				wanStaticRoute.setIsPewan((byte) 1);

				wanStaticRouteRepository.save(wanStaticRoute);

			}
		}
	}

	private void applyBgpPolicyType(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails)
			throws TclCommonException {

		try {

			logger.info("bgp started");
			softDeleteBgpPolicy(bgp);
			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			String ippath = map.get(AceConstants.IPADDRESS.IPPATH);
			String routeExchange = bgp.getRoutesExchanged();

			if (routeExchange == null) {
				routeExchange = "DEFAULTROUTE";
			}

			if ((ippath.equalsIgnoreCase("v4") || ippath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL))
					&& (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
							|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
							|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP))) {

				createInboundPolicyV4(bgp, map, serviceDetails, routerMake);

				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

					createOutboundPolicyAluV4(bgp, map, serviceDetails, routeExchange);

				}
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

					createOutboundPolicyJuniperV4(bgp, map, serviceDetails, routeExchange);

				}

			}
			if ((ippath.equalsIgnoreCase("v6") || ippath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL))
					&& (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
							|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
							|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP))) {

				createInboundV6(bgp, map, serviceDetails, routerMake);

				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

					createOutboundPolicyAluV6(bgp, map, serviceDetails, routeExchange);

				}
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

					createOutboundPolicyJuniperV6(bgp, map, serviceDetails, routeExchange);

				}

			}
		} catch (Exception e) {
			logger.error("Exception in applyBgpPolicyType => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void softDeleteBgpPolicy(Bgp bgp) {

		if (bgp.getPolicyTypes() != null && !bgp.getPolicyTypes().isEmpty()) {
			bgp.getPolicyTypes().forEach(policy -> {
				policy.setEndDate(new Timestamp(System.currentTimeMillis()));
				policy.getPolicyTypeCriteriaMappings().forEach(criteria -> {
					criteria.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					criteria.setModifiedBy(MODIFIEDBY);

					policyTypeRepository.save(policy);

					Optional<PolicyCriteria> optionalPolicyCriteria = policyCriteriaRepository
							.findById(criteria.getPolicyCriteriaId());

					if (optionalPolicyCriteria.isPresent()) {
						PolicyCriteria policyCriteria = optionalPolicyCriteria.get();
						policyCriteria.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						policyCriteria.setModifiedBy(MODIFIEDBY);
						policyCriteriaRepository.save(policyCriteria);
					}

				});

			});
		}

	}

	private void createOutboundPolicyJuniperV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routeExchange) throws TclCommonException {

		try {
			logger.info("createOutboundPolicyJuniperV6  started {}");
			PolicyType outBoundPolicy = new PolicyType();
			outBoundPolicy.setBgp(bgp);

			if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))
					|| AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {
				outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.SETOUTBOUNDIPV6POLICYNAME);

			} else if (map.get(AceConstants.VRF.VRF_NAME) == null
					|| AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {

				if (routeExchange.equalsIgnoreCase(AceConstants.BGPPOLICY.DEFAULTROUTE)) {
					outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.DEFAULTROUTES_IPV6_ACE);

				} else if (routeExchange.equalsIgnoreCase(AceConstants.BGPPOLICY.PARTIALROUTE)) {
					outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.PARTIALROUTES_IPV6_ACE);

				} else if (routeExchange.equalsIgnoreCase(AceConstants.BGPPOLICY.FULLROUTE)) {
					outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.FULLROUTES_IPV6_ACE);

				}
			}
			outBoundPolicy.setOutboundRoutingIpv6Policy((byte) 1);
			outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 1);
			outBoundPolicy.setOutboundIstandardpolicyv6((byte) 1);
			outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			outBoundPolicy.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(outBoundPolicy);
		} catch (Exception e) {
			logger.error("Exception in createOutboundPolicyJuniperV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void createOutboundPolicyAluV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routeExchange) throws TclCommonException {

		try {
			logger.info("createOutboundPolicyAluV6  started {}");
			PolicyType outBoundPolicy = new PolicyType();
			outBoundPolicy.setBgp(bgp);

			if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))
					|| AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {
				outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.EXPORT_EBGP_DEFAULT_IPV6_ACE);

			} else if (map.get(AceConstants.VRF.VRF_NAME) == null
					|| AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {

				if (routeExchange.equalsIgnoreCase(AceConstants.BGPPOLICY.DEFAULTROUTE)) {
					outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.EXPORT_EBGP_DEFAULT_IPV6_ACE);

				} else if (routeExchange.equalsIgnoreCase(AceConstants.BGPPOLICY.PARTIALROUTE)) {
					outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.EXPORT_EBGP_PARTIAL_IPV6_ACE);

				} else if (routeExchange.equalsIgnoreCase(AceConstants.BGPPOLICY.FULLROUTE)) {
					outBoundPolicy.setOutboundIpv6PolicyName(AceConstants.BGPPOLICY.EXPORT_EBGP_DENY_ALL_IPV6_ACE);

				}
			}
			outBoundPolicy.setOutboundRoutingIpv6Policy((byte) 1);
			outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 1);
			outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			outBoundPolicy.setModifiedBy(MODIFIEDBY);
			// outBoundPolicy.setOutboundIstandardpolicyv6((byte) 1);
			policyTypeRepository.save(outBoundPolicy);
		} catch (Exception e) {

			logger.error("Exception in createOutboundPolicyAluV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void createInboundPolicyV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routerMake) throws TclCommonException {

		try {
			logger.info("createInboundPolicyV4  started {}");
			PolicyType policyType = new PolicyType();
			policyType.setBgp(bgp);
			policyType.setInboundRoutingIpv4Policy((byte) 1);
			policyType.setInboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv4_In");
			policyType.setInboundIpv4Preprovisioned((byte) 0);
			if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
				policyType.setInboundIstandardpolicyv4((byte) 1);

			}
			policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType);
			policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgp(policyType, map, serviceDetails));
		} catch (Exception e) {
			logger.error("Exception in createInboundPolicyV4 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void createInboundV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails, String routerMake)
			throws TclCommonException {

		try {
			logger.info("createInboundV6  started {}");

			PolicyType policyType = new PolicyType();
			policyType.setBgp(bgp);
			policyType.setInboundRoutingIpv6Policy((byte) 1);
			policyType.setInboundIpv6PolicyName(serviceDetails.getServiceId() + "" + "_IPv6_In");
			policyType.setInboundIpv6Preprovisioned((byte) 0);
			if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
				policyType.setInboundIstandardpolicyv6((byte) 1);

			}
			policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType);
			policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgpV6(policyType, map, serviceDetails));
		} catch (Exception e) {
			logger.error("Exception in createInboundPolicyV4 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgpV6(PolicyType policyType, Map<String, String> map,
			ServiceDetail serviceDetails) throws TclCommonException {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

		try {
			logger.info("createPolicyCriteraForBgpV6  started {}");

			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

			if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {
				createEnablePrefixCriteriaForCiscov6(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType);

			} else {
				createEnablePrefixCriteriaV6(policyTypeCriteriaMappings, serviceDetails, policyType, routerMake, map);
			//	enableProtocalPolicyCriteriaV6(policyTypeCriteriaMappings, policyType, routerMake, map);
				enableLocalPreferenceV6(policyTypeCriteriaMappings, policyType, routerMake, map);
			
			}
		} catch (Exception e) {
			logger.error("Exception in createPolicyCriteraForBgpV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		return policyTypeCriteriaMappings;
	}

	private void createEnablePrefixCriteriaV6(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, PolicyType policyType, String routerMake, Map<String, String> map)
			throws TclCommonException {

		try {
			logger.info("createEnablePrefixCriteriaV6  started {}");

			PolicyCriteria enablePrefix = new PolicyCriteria();
			enablePrefix.setMatchcriteriaPrefixlist((byte) 1);
			enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv6_In");
			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				enablePrefix.setTermName("10");
				enablePrefix.setTermSetcriteriaAction("ACCEPT");
			}
			enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefix.setModifiedBy(MODIFIEDBY);
			policyCriteriaRepository.save(enablePrefix);
			enablePrefix.setPrefixlistConfigs(createPrefixConfigV6(enablePrefix, serviceDetails,map.get(AceConstants.ROUTER.ROUTER_MAKE)));
			PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
			enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
			enablePrefixCriteriaMapping.setVersion(1);
			enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
			enablePrefixCriteriaMapping.setPolicyType(policyType);
			policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
			policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
		} catch (Exception e) {
			logger.error("Exception in createEnablePrefixCriteriaV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void enableLocalPreferenceV6(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			PolicyType policyType, String routerMake, Map<String, String> map) throws TclCommonException {

		try {

			logger.info("enableLocalPreferenceV6  started {}");
		if(	!routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)
			&& map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
					&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)) {

				PolicyCriteria enableLocalPref = new PolicyCriteria();
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
					enableLocalPref.setTermName("10");
					enableLocalPref.setTermSetcriteriaAction("ACCEPT");

				}
				if (!routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)
						&& map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
						&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE)
								.equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)) {

					enableLocalPref.setSetcriteriaLocalpreferenceName("200");
					enableLocalPref.setSetcriteriaLocalPreference((byte) 1);

				}

				enableLocalPref.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				enableLocalPref.setModifiedBy(MODIFIEDBY);
				policyCriteriaRepository.save(enableLocalPref);
				PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
				enablePrefixCriteriaMapping.setPolicyCriteriaId(enableLocalPref.getPolicyCriteriaId());
				enablePrefixCriteriaMapping.setVersion(1);
				enablePrefixCriteriaMapping.setPolicyType(policyType);
				enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
				enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
				policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
			}
		} catch (Exception e) {
			logger.error("Exception in enableLocalPreferenceV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}


	private void createOutboundPolicyJuniperV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routeExchange) throws TclCommonException {

		try {
			logger.info("createOutboundPolicyJuniperV4  started {}");

			PolicyType outBoundPolicy = new PolicyType();
			outBoundPolicy.setBgp(bgp);

			if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))
					|| AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {
				outBoundPolicy.setOutboundIpv4PolicyName(AceConstants.BGPPOLICY.SETOUTBOUNDIPV4POLICYNAME);

			} else if (map.get(AceConstants.VRF.VRF_NAME) == null
					|| AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {

				if (routeExchange.contains(AceConstants.BGPPOLICY.DEFAULTROUTE)) {
					outBoundPolicy.setOutboundIpv4PolicyName(AceConstants.BGPPOLICY.DEFAULTROUTES_IPv4_ACE);

				} else if (routeExchange.contains(AceConstants.BGPPOLICY.PARTIALROUTE)) {
					outBoundPolicy.setOutboundIpv4PolicyName(AceConstants.BGPPOLICY.PARTIALROUTES_IPV4_ACE);

				} else if (routeExchange.contains(AceConstants.BGPPOLICY.FULLROUTE)) {
					outBoundPolicy.setOutboundIpv4PolicyName(AceConstants.BGPPOLICY.FULLROUTES_IPV4_ACE);

				}
			}
			outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
			outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 1);
			outBoundPolicy.setOutboundIstandardpolicyv4((byte) 1);
			outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			outBoundPolicy.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(outBoundPolicy);
		} catch (Exception e) {
			logger.error("Exception in createOutboundPolicyJuniperV4 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void createOutboundPolicyAluV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routeExchange) throws TclCommonException {

		try {
			logger.info("createOutboundPolicyAluV4  started {}");

			PolicyType outBoundPolicy = new PolicyType();
			outBoundPolicy.setBgp(bgp);

			if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))
					|| AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {
				outBoundPolicy.setOutboundIpv4PolicyName("EXPORT_EBGP_DEFAULT_IPv4_ACE");

			} else if (map.get(AceConstants.VRF.VRF_NAME) == null
					|| AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {

				if (routeExchange.equalsIgnoreCase("DEFAULTROUTE")) {
					outBoundPolicy.setOutboundIpv4PolicyName("EXPORT_EBGP_DEFAULT_IPv4_ACE");

				} else if (routeExchange.equalsIgnoreCase("PARTIALROUTE")) {
					outBoundPolicy.setOutboundIpv4PolicyName("EXPORT_EBGP_PARTIAL_IPv4_ACE");

				} else if (routeExchange.equalsIgnoreCase("FULLROUTE")) {
					outBoundPolicy.setOutboundIpv4PolicyName("EXPORT_EBGP_DENY-ALL_IPv4_ACE");

				}
			}
			outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
			outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 1);
			// outBoundPolicy.setOutboundIstandardpolicyv4((byte) 1);
			outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			outBoundPolicy.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(outBoundPolicy);
		}

		catch (Exception e) {
			logger.error("Exception in createOutboundPolicyAluV4 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgp(PolicyType policyType, Map<String, String> map,
			ServiceDetail serviceDetails) throws TclCommonException {

		try {
			Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

			logger.info("createPolicyCriteraForBgp  started {}");

			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

			if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {
				createEnablePrefixCriteriaForCisco(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType);

			} else {
				createEnablePrefixCriteria(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType);
			//	enableProtocalPolicyCriteria(policyTypeCriteriaMappings, policyType, routerMake, map);
				enableLocalPreference(policyTypeCriteriaMappings, policyType, routerMake, map);
			
			}
			return policyTypeCriteriaMappings;
		} catch (Exception e) {
			logger.error("Exception in createPolicyCriteraForBgp => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void createEnablePrefixCriteria(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType policyType) throws TclCommonException {
		PolicyCriteria enablePrefix = new PolicyCriteria();
		enablePrefix.setMatchcriteriaPrefixlist((byte) 1);
		enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv4_In");
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

			enablePrefix.setTermName("10");
			enablePrefix.setTermSetcriteriaAction("ACCEPT");
		}
		enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefix.setModifiedBy(serviceDetails.getOrderDetail().getOriginatorName());
		enablePrefix.setMatchcriteriaPprefixlistPreprovisioned((byte) 0);
		enablePrefix.setModifiedBy(MODIFIEDBY);

		policyCriteriaRepository.save(enablePrefix);
		enablePrefix.setPrefixlistConfigs(createPrefixConfig(enablePrefix, serviceDetails, routerMake));
		PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
		enablePrefixCriteriaMapping.setPolicyType(policyType);
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
		policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
		policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
	}

	private void createEnablePrefixCriteriaForCisco(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType policyType) throws TclCommonException {

		try {
			logger.info("createEnablePrefixCriteriaForCisco  started {}");

			PolicyCriteria enablePrefix = new PolicyCriteria();
			enablePrefix.setMatchcriteriaPrefixlist((byte) 1);
			enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv4_In");
			enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefix.setModifiedBy(serviceDetails.getOrderDetail().getOriginatorName());
			policyCriteriaRepository.save(enablePrefix);
			enablePrefix.setPrefixlistConfigs(createPrefixConfig(enablePrefix, serviceDetails, routerMake));
			PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
			enablePrefixCriteriaMapping.setVersion(1);
			enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
			enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
			enablePrefixCriteriaMapping.setPolicyType(policyType);
			policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
			policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
		} catch (Exception e) {
			logger.error("Exception in createEnablePrefixCriteriaForCisco => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
	
	
	private void createEnablePrefixCriteriaForCiscov6(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType policyType) throws TclCommonException {

		try {
			logger.info("createEnablePrefixCriteriaForCisco  started {}");

			PolicyCriteria enablePrefix = new PolicyCriteria();
			enablePrefix.setMatchcriteriaPrefixlist((byte) 1);
			enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv6_In");
			enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefix.setModifiedBy(serviceDetails.getOrderDetail().getOriginatorName());
			policyCriteriaRepository.save(enablePrefix);
			enablePrefix.setPrefixlistConfigs(createPrefixConfigV6(enablePrefix, serviceDetails,routerMake));
			PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
			enablePrefixCriteriaMapping.setVersion(1);
			enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
			enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
			enablePrefixCriteriaMapping.setPolicyType(policyType);
			policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
			policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
		} catch (Exception e) {
			logger.error("Exception in createEnablePrefixCriteriaForCisco => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void enableLocalPreference(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings, PolicyType policyType,
			String routerMake, Map<String, String> map) throws TclCommonException {

		try {
			PolicyCriteria enableLocalPref = new PolicyCriteria();

			logger.info("enableLocalPreference  started {}");
			if (!routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)
					&& map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
					&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)) {

				enableLocalPref.setSetcriteriaLocalpreferenceName("200");
				enableLocalPref.setSetcriteriaLocalPreference((byte) 1);

			

			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				enableLocalPref.setTermName("10");
				enableLocalPref.setTermSetcriteriaAction("ACCEPT");

			}
			
			enableLocalPref.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enableLocalPref.setModifiedBy(MODIFIEDBY);
			enableLocalPref = policyCriteriaRepository.save(enableLocalPref);
			PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
			enablePrefixCriteriaMapping.setPolicyCriteriaId(enableLocalPref.getPolicyCriteriaId());
			enablePrefixCriteriaMapping.setVersion(1);
			enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
			enablePrefixCriteriaMapping.setPolicyType(policyType);
			policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
			policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
			}
		} catch (Exception e) {
			logger.error("Exception in enableLocalPreference => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}


	private Set<PrefixlistConfig> createPrefixConfig(PolicyCriteria enablePrefix, ServiceDetail serviceDetails,
			String routerMake) throws TclCommonException {
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();

		try {
			logger.info("createPrefixConfig  started {}");

			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

			if (ipAddressDetail != null) {
				if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {
					for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
						PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
						prefixlistConfig.setNetworkPrefix(ipaddrLanv4Address.getLanv4Address()!=null ?ipaddrLanv4Address.getLanv4Address().trim():ipaddrLanv4Address.getLanv4Address());
						prefixlistConfig.setPolicyCriteria(enablePrefix);

						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
							prefixlistConfig.setAction("PERMIT");

						} else {

							prefixlistConfig.setAction("EXACT");
						}

						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							prefixlistConfig.setAction(null);

						}
						prefixlistConfig.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						prefixlistConfig.setModifiedBy(MODIFIEDBY);
						prefixlistConfig.setPolicyCriteria(enablePrefix);
						prefixlistConfigRepository.save(prefixlistConfig);
						prefixlistConfigs.add(prefixlistConfig);

					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception in createPrefixConfig => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		return prefixlistConfigs;
	}

	private Set<PrefixlistConfig> createPrefixConfigV6(PolicyCriteria enablePrefix, ServiceDetail serviceDetails, String routerMake)
			throws TclCommonException {
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();
		IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
		try {
			logger.info("createPrefixConfigV6  started {}");

			if (ipAddressDetail != null) {
				if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {
					for (IpaddrLanv6Address ipaddrLanv6Address : ipAddressDetail.getIpaddrLanv6Addresses()) {
						PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
						prefixlistConfig.setNetworkPrefix(ipaddrLanv6Address.getLanv6Address()!=null ?ipaddrLanv6Address.getLanv6Address().trim():ipaddrLanv6Address.getLanv6Address());
						prefixlistConfig.setPolicyCriteria(enablePrefix);
						
						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
							prefixlistConfig.setAction("PERMIT");

						} else {

							prefixlistConfig.setAction("EXACT");
						}

						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							prefixlistConfig.setAction(null);

						}
						prefixlistConfig.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						prefixlistConfig.setModifiedBy(MODIFIEDBY);
						prefixlistConfigRepository.save(prefixlistConfig);
						prefixlistConfigs.add(prefixlistConfig);

					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception in createPrefixConfigV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		return prefixlistConfigs;
	}

	private Set<PolicycriteriaProtocol> createPolicyProtocalForBgp(PolicyCriteria enableProtocal,
			Map<String, String> map) throws TclCommonException {

		try {
			Set<PolicycriteriaProtocol> policycriteriaProtocols = new HashSet<>();

			logger.info("createPolicyProtocalForBgp  started {}");

			PolicycriteriaProtocol policycriteriaProtocol = new PolicycriteriaProtocol();
			policycriteriaProtocol.setProtocolName("bgp");
			policycriteriaProtocol.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policycriteriaProtocol.setModifiedBy(MODIFIEDBY);
			policycriteriaProtocol.setPolicyCriteria(enableProtocal);
			policycriteriaProtocolRepository.save(policycriteriaProtocol);
			policycriteriaProtocols.add(policycriteriaProtocol);
			return policycriteriaProtocols;

		} catch (Exception e) {
			logger.error("Exception in createPolicyProtocalForBgp => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private void applyChannelizedE1serialInterface(ChannelizedE1serialInterface channelizedE1serialInterface,
			Map<String, String> map, ServiceDetail serviceDetails, String type) throws TclCommonException {

		try {
			logger.info("applyChannelizedE1serialInterface  started {}");

			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

			if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
				channelizedE1serialInterface.setModifiedIpv4Address(channelizedE1serialInterface.getIpv4Address() + "/"
						+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS)));

				if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)) {
					channelizedE1serialInterface
							.setModifiedSecondaryIpv4Address(channelizedE1serialInterface.getSecondaryIpv4Address()
									+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)));
				}

			}
			if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
				channelizedE1serialInterface.setModifiedIpv6Address(channelizedE1serialInterface.getIpv6Address() + "/"
						+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS)));

				if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)) {
					channelizedE1serialInterface
							.setModifiedSecondaryIpv6Address(channelizedE1serialInterface.getSecondaryIpv6Address()
									+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)));
				}
			}

			if (channelizedE1serialInterface.getFirsttimeSlot() != null
					&& channelizedE1serialInterface.getLasttimeSlot() != null) {
				channelizedE1serialInterface.setIsframed((byte) 1);
				channelizedE1serialInterface.setIsbfdEnabled((byte) 0);

			} else {
				channelizedE1serialInterface.setIsframed((byte) 0);

			}

			String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			if (routerName.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
				channelizedE1serialInterface.setEncapsulation(AceConstants.INTERFACE.HDLC);

			}
			if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
					|| routerName.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				channelizedE1serialInterface.setEncapsulation(AceConstants.INTERFACE.PPP);

			}
			/*if (channelizedE1serialInterface.getMode() == null) {

				channelizedE1serialInterface.setEncapsulation("NULL");

			} else if (channelizedE1serialInterface.getMode() != null
					&& channelizedE1serialInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.MODE)) {
				channelizedE1serialInterface.setEncapsulation("NULL");

			}

			else if (channelizedE1serialInterface.getMode() != null
					&& channelizedE1serialInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.TRUNK)) {
				channelizedE1serialInterface.setEncapsulation(AceConstants.INTERFACE.DOT1Q);

			} else if (channelizedE1serialInterface.getMode() != null
					&& channelizedE1serialInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.Q_IN_Q)) {
				channelizedE1serialInterface.setEncapsulation(AceConstants.INTERFACE.QNQ);

			}*/

			if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				channelizedE1serialInterface.setMode(AceConstants.INTERFACE.MODE);
			} else if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				channelizedE1serialInterface.setMode(null);

			}

			channelizedE1serialInterface.setIscrcforenabled((byte) 0);
			channelizedE1serialInterface.setModifiedBy(MODIFIEDBY);
			channelizedE1serialInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			channelizedE1serialInterface.setPortType(null);
			channelizedE1serialInterfaceRepository.save(channelizedE1serialInterface);
			if(type.equalsIgnoreCase("PE")) {

			saveAclPolicyCriteriaForChannelizedE1serialInterface(serviceDetails, map.get(AceConstants.IPADDRESS.IPPATH),
					channelizedE1serialInterface, map);
			}

		} catch (Exception e) {
			logger.error("Exception in applyChannelizedE1serialInterface => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private Set<AclPolicyCriteria> saveAclPolicyCriteriaForChannelizedE1serialInterface(ServiceDetail serviceDetail,
			String type, ChannelizedE1serialInterface channelizedE1serialInterface, Map<String, String> map)
			throws TclCommonException {
		try {
			logger.info("Service Activation - saveAclPolicyCriteriaForChannelizedE1serialInterface - started");
			Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();
			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

			if (!channelizedE1serialInterface.getAclPolicyCriterias().isEmpty()) {
				channelizedE1serialInterface.getAclPolicyCriterias().forEach(policy -> {
					policy.setEndDate(new Timestamp(System.currentTimeMillis()));
					policy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					policy.setModifiedBy(MODIFIEDBY);
					aclPolicyCriteriaRepository.save(policy);
				});
			}

			if (type.equalsIgnoreCase("v4") || type.equals("DUALSTACK")) {
				saveAclPolicyCriteriaV4ForChannelizedE1serialInterface(channelizedE1serialInterface, routerMake,
						serviceDetail);

			}
			if (type.equalsIgnoreCase("v6") ) {
				saveAclPolicyCriteriaV6ForChannelizedE1serialInterface(channelizedE1serialInterface, routerMake,
						serviceDetail);

			}

			logger.info("Service Activation - saveAclPolicyCriteria - completed");
			return aclPolicyCriterias;
		} catch (Exception e) {
			logger.error("Exception in saveAclPolicyCriteria => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void saveAclPolicyCriteriaV6ForChannelizedE1serialInterface(
			ChannelizedE1serialInterface channelizedE1serialInterface, String routerMake, ServiceDetail serviceDetail) {

		AclPolicyCriteria acp = new AclPolicyCriteria();

		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			acp.setInboundIpv6AclName("INFRA-ACL");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			acp.setInboundIpv6AclName("infra-acl");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			acp.setInboundIpv6AclName("infra-acl-do-not-add-or-edit");
		}

		acp.setIsinboundaclIpv6Preprovisioned((byte) 1);
		acp.setIsinboundaclIpv6Applied((byte) 1);
		acp.setChannelizedE1serialInterface(channelizedE1serialInterface);
		acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		acp.setModifiedBy(MODIFIEDBY);
		aclPolicyCriteriaRepository.save(acp);

	}

	private void saveAclPolicyCriteriaV4ForChannelizedE1serialInterface(
			ChannelizedE1serialInterface channelizedE1serialInterface, String routerMake, ServiceDetail serviceDetail) {
		AclPolicyCriteria acp = new AclPolicyCriteria();
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			acp.setInboundIpv4AclName("INFRA-ACL");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			acp.setInboundIpv4AclName("infra-acl");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			acp.setInboundIpv4AclName("infra-acl-do-not-add-or-edit");
		}

		acp.setIsinboundaclIpv4Preprovisioned((byte) 1);
		acp.setIsinboundaclIpv4Applied((byte) 1);
		acp.setChannelizedE1serialInterface(channelizedE1serialInterface);
		acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		acp.setModifiedBy(MODIFIEDBY);
		aclPolicyCriteriaRepository.save(acp);

	}

	private void applyChannelizedSdhInterfaceRule(ChannelizedSdhInterface channelizedSdhInterface,
			Map<String, String> map, ServiceDetail serviceDetails, String type) throws TclCommonException {

		try {
			logger.info("applyChannelizedSdhInterfaceRule  started {}");

			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

			channelizedSdhInterface.setIsbfdEnabled((byte) 0);

			if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
				String subnet = subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS));
				channelizedSdhInterface.setModifiedIpv4Address(channelizedSdhInterface.getIpv4Address() + "/" + subnet);

				if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)) {
					String secSubnet = subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY));
					channelizedSdhInterface.setModifiedSecondaryIpv4Address(
							channelizedSdhInterface.getSecondaryIpv4Address() + "/" + secSubnet);
				}

			}
			if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
				String subnet = subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS));
				channelizedSdhInterface
						.setModifiedIipv6Address(channelizedSdhInterface.getIpv6Address() + "/" + subnet);

				if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)) {
					String seconSubnet = subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY));
					channelizedSdhInterface.setModifiedSecondaryIpv6Address(
							channelizedSdhInterface.getSecondaryIpv6Address() + "/" + seconSubnet);
				}
			}

			if (channelizedSdhInterface.get_4Kfirsttime_slot() != null
					&& channelizedSdhInterface.get_4klasttimeSlot() != null) {
				channelizedSdhInterface.setIsframed((byte) 1);
				channelizedSdhInterface.setIsbfdEnabled((byte) 1);
			}
			String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			if (routerName.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
				channelizedSdhInterface.setEncapsulation(AceConstants.INTERFACE.HDLC);

			}
			if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
					|| routerName.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				channelizedSdhInterface.setEncapsulation(AceConstants.INTERFACE.PPP);

			}

			/*if (channelizedSdhInterface.getMode() == null) {

				channelizedSdhInterface.setEncapsulation("NULL");

			} else if (channelizedSdhInterface.getMode() != null
					&& channelizedSdhInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.MODE)) {
				channelizedSdhInterface.setEncapsulation("NULL");

			}

			else if (channelizedSdhInterface.getMode() != null
					&& channelizedSdhInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.TRUNK)) {
				channelizedSdhInterface.setEncapsulation(AceConstants.INTERFACE.DOT1Q);

			} else if (channelizedSdhInterface.getMode() != null
					&& channelizedSdhInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.Q_IN_Q)) {
				channelizedSdhInterface.setEncapsulation(AceConstants.INTERFACE.QNQ);

			}*/

			
			if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				channelizedSdhInterface.setMode(AceConstants.INTERFACE.MODE);
			} else if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				channelizedSdhInterface.setMode(null);

			}
			channelizedSdhInterface.setModifiedBy(MODIFIEDBY);
			channelizedSdhInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			channelizedSdhInterface.setPortType(null);
			channelizedSdhInterfaceRepository.save(channelizedSdhInterface);
			if(type.equalsIgnoreCase("PE")) {

			saveAclPolicyCriteriaForChannelizedSdhInterface(serviceDetails, map.get(AceConstants.IPADDRESS.IPPATH),
					channelizedSdhInterface, map);
			}
		} catch (Exception e) {
			logger.error("Exception in applyChannelizedSdhInterfaceRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private Set<AclPolicyCriteria> saveAclPolicyCriteriaForChannelizedSdhInterface(ServiceDetail serviceDetail,
			String type, ChannelizedSdhInterface channelizedSdhInterface, Map<String, String> map)
			throws TclCommonException {

		try {
			logger.info("Service Activation - saveAclPolicyCriteria - started");
			Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();
			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

			if (!channelizedSdhInterface.getAclPolicyCriterias().isEmpty()) {
				channelizedSdhInterface.getAclPolicyCriterias().forEach(policy -> {
					policy.setEndDate(new Timestamp(System.currentTimeMillis()));
					policy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					policy.setModifiedBy(MODIFIEDBY);
					aclPolicyCriteriaRepository.save(policy);
				});
			}

			if (type.equalsIgnoreCase("v4") || type.equals("DUALSTACK")) {
				saveAclPolicyCriteriaV4ForChannelizedSdhInterface(channelizedSdhInterface, routerMake, serviceDetail);

			}
			if (type.equalsIgnoreCase("v6") ) {
				saveAclPolicyCriteriaV6ForChannelizedSdhInterface(channelizedSdhInterface, routerMake, serviceDetail);

			}

			logger.info("Service Activation - saveAclPolicyCriteria - completed");
			return aclPolicyCriterias;
		} catch (Exception e) {
			logger.error("Exception in saveAclPolicyCriteria => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void saveAclPolicyCriteriaV6ForChannelizedSdhInterface(ChannelizedSdhInterface channelizedSdhInterface,
			String routerMake, ServiceDetail serviceDetail) {

		AclPolicyCriteria acp = new AclPolicyCriteria();

		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			acp.setInboundIpv6AclName("INFRA-ACL");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			acp.setInboundIpv6AclName("infra-acl");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			acp.setInboundIpv6AclName("infra-acl-do-not-add-or-edit");
		}

		acp.setIsinboundaclIpv6Preprovisioned((byte) 1);
		acp.setIsinboundaclIpv6Applied((byte) 1);
		acp.setChannelizedSdhInterface(channelizedSdhInterface);
		acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		acp.setModifiedBy(MODIFIEDBY);
		aclPolicyCriteriaRepository.save(acp);

	}

	private void saveAclPolicyCriteriaV4ForChannelizedSdhInterface(ChannelizedSdhInterface channelizedSdhInterface,
			String routerMake, ServiceDetail serviceDetail) {
		AclPolicyCriteria acp = new AclPolicyCriteria();
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			acp.setInboundIpv4AclName("INFRA-ACL");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			acp.setInboundIpv4AclName("infra-acl");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			acp.setInboundIpv4AclName("infra-acl-do-not-add-or-edit");
		}

		acp.setIsinboundaclIpv4Preprovisioned((byte) 1);
		acp.setIsinboundaclIpv4Applied((byte) 1);
		acp.setChannelizedSdhInterface(channelizedSdhInterface);
		acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		acp.setModifiedBy(MODIFIEDBY);
		aclPolicyCriteriaRepository.save(acp);
	}

	private void appyEthernetRule(EthernetInterface ethernetInterface, Map<String, String> map,
			ServiceDetail serviceDetails, String type) throws TclCommonException {

		try {
			logger.info("appyEthernetRule  started {}");
			
		

			ethernetInterface.setSpeed(AceConstants.DEFAULT.NOT_APPLICABLE);
			ethernetInterface.setDuplex(AceConstants.DEFAULT.NOT_APPLICABLE);
			ethernetInterface.setAutonegotiationEnabled(AceConstants.DEFAULT.NOT_APPLICABLE);
			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			
			map.put(AceConstants.DEFAULT.ETHERNET_OUTERVLAN,ethernetInterface.getOuterVlan());

			if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerMake)
					|| AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
				ethernetInterface.setQosLoopinPassthrough(null);
			}

			if (map.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED)
					&& map.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("TRUE")) {

				int count=1;

				if(type!=null && type.equalsIgnoreCase("CE")){
					count=2;
				}

				if (map.containsKey(AceConstants.IPADDRESS.LANV4ADDRESS)) {
					ethernetInterface
							.setModifiedIpv4Address(getIpAddressSplit(map.get(AceConstants.IPADDRESS.LANV4ADDRESS), count)
									+ "/" + subnet(map.get(AceConstants.IPADDRESS.LANV4ADDRESS)));

					if (map.containsKey(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY))
						ethernetInterface.setModifiedSecondaryIpv4Address(
								getIpAddressSplit(map.get(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY), count) + "/"
										+ subnet(map.get(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY)));
				}

				if (map.containsKey(AceConstants.IPADDRESS.LANV6ADDRESS)) {
					ethernetInterface
							.setModifiedIpv6Address(getIpAddressSplit(map.get(AceConstants.IPADDRESS.LANV6ADDRESS), count)
									+ "/" + subnet(map.get(AceConstants.IPADDRESS.LANV6ADDRESS)));
					if (map.containsKey(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY)) {
						ethernetInterface.setModifiedSecondaryIpv6Address(
								addCountIpv6Address(map.get(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY), count) + "/"
										+ subnet(map.get(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY)));
					}
				}

			} else {

				if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
					ethernetInterface.setModifiedIpv4Address(ethernetInterface.getIpv4Address() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS)));

					if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY))
						ethernetInterface.setModifiedSecondaryIpv4Address(ethernetInterface.getSecondaryIpv4Address()
								+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)));
				}

				if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
					ethernetInterface.setModifiedIpv6Address(ethernetInterface.getIpv6Address() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS)));
					if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)) {
						ethernetInterface.setModifiedSecondaryIpv6Address(ethernetInterface.getSecondaryIpv6Address()
								+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)));
					}
				}
			}
			

			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER) && ethernetInterface.getPhysicalPort() != null
					&& ethernetInterface.getPhysicalPort().toUpperCase()
							.contains(AceConstants.INTERFACE.XGIGABIT_ETHERNET.toUpperCase())) {
				ethernetInterface.setFraming(AceConstants.INTERFACE.LAN_PHY);

			}
			else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
					&& ethernetInterface.getInterfaceName() != null && ethernetInterface.getInterfaceName()
							.toUpperCase().contains(AceConstants.INTERFACE.XGIGABIT_ETHERNET.toUpperCase())) {
				ethernetInterface.setFraming(AceConstants.INTERFACE.LAN_PHY);

			} else if(routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				ethernetInterface.setFraming(AceConstants.DEFAULT.NOT_APPLICABLE);

			}

			if (ethernetInterface.getMode() == null) {

				ethernetInterface.setEncapsulation("NULL");

			} else if (ethernetInterface.getMode() != null
					&& ethernetInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.MODE)) {
				ethernetInterface.setEncapsulation("NULL");

			}

			else if (ethernetInterface.getMode() != null
					&& ethernetInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.TRUNK)) {
				ethernetInterface.setEncapsulation(AceConstants.INTERFACE.DOT1Q);

			} else if (ethernetInterface.getMode() != null
					&& ethernetInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.Q_IN_Q)) {
				ethernetInterface.setEncapsulation(AceConstants.INTERFACE.QNQ);

			}

			if (ethernetInterface.getEncapsulation() != null
					&& ethernetInterface.getEncapsulation().equalsIgnoreCase(AceConstants.INTERFACE.DOT1Q)) {
				ethernetInterface.setOuterVlan(ethernetInterface.getInnerVlan());
			}
			if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				ethernetInterface.setMode(AceConstants.INTERFACE.MODE);
			} else if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				ethernetInterface.setMode(null);

			}

			if (ethernetInterface.getOuterVlan()!=null && ethernetInterface.getOuterVlan().equalsIgnoreCase("0")) {
				ethernetInterface.setOuterVlan(null);
			}
			ethernetInterface.setModifiedBy(MODIFIEDBY);
			ethernetInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			ethernetInterface.setPortType(null);
			ethernetInterfaceRepository.save(ethernetInterface);
			if(type.equalsIgnoreCase("PE")) {

			saveAclPolicyCriteriaForEthernet(serviceDetails, map.get(AceConstants.IPADDRESS.IPPATH), ethernetInterface,
					map);
			}

			map.put(AceConstants.INTERFACE.ETHRNER_INNERV_LAN, ethernetInterface.getInnerVlan());
		} catch (Exception e) {
			logger.error("Exception in appyEthernetRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}
	



	private String findCpeInterfaceIpv4Ethernet(InterfaceProtocolMapping cpeProtocolMapping) throws TclCommonException {

		try {
			logger.info("findCpeInterfaceIpv4Ethernet  started {}");

			if (cpeProtocolMapping != null && cpeProtocolMapping.getEthernetInterface() != null) {

				return cpeProtocolMapping.getEthernetInterface().getIpv4Address();
			}
		} catch (Exception e) {
			logger.error("Exception in appyEthernetRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return null;

	}

	public Set<AclPolicyCriteria> saveAclPolicyCriteriaForEthernet(ServiceDetail serviceDetail, String type,
			EthernetInterface ethernetInterface, Map<String, String> map) throws TclCommonException {

		try {
			logger.info("Service Activation - saveAclPolicyCriteria - started");
			Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();
			String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
			softDeleteEthernetPolicy(ethernetInterface, serviceDetail);

			if (type.equalsIgnoreCase("v4") || type.equals("DUALSTACK")) {
				saveAclPolicyCriteriaV4(ethernetInterface, routerMake, serviceDetail);

			}
			if (type.equalsIgnoreCase("v6") ) {
				saveAclPolicyCriteriaV6(ethernetInterface, routerMake, serviceDetail);

			}

			logger.info("Service Activation - saveAclPolicyCriteriaForEthernet - completed");
			return aclPolicyCriterias;
		} catch (Exception e) {
			logger.error("Exception in saveAclPolicyCriteria => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void saveAclPolicyCriteriaV6(EthernetInterface ethernetInterface, String routerMake,
			ServiceDetail serviceDetail) {
		AclPolicyCriteria acp = new AclPolicyCriteria();

		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			acp.setInboundIpv6AclName("INFRA-ACL");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			acp.setInboundIpv6AclName("infra-acl");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			acp.setInboundIpv6AclName("infra-acl-do-not-add-or-edit");
		}

		acp.setIsinboundaclIpv6Preprovisioned((byte) 1);
		acp.setIsinboundaclIpv6Applied((byte) 1);
		acp.setEthernetInterface(ethernetInterface);
		acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		acp.setModifiedBy(MODIFIEDBY);
		aclPolicyCriteriaRepository.save(acp);
	}

	private void saveAclPolicyCriteriaV4(EthernetInterface ethernetInterface, String routerMake,
			ServiceDetail serviceDetail) {
		AclPolicyCriteria acp = new AclPolicyCriteria();
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			acp.setInboundIpv4AclName("INFRA-ACL");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			acp.setInboundIpv4AclName("infra-acl");
		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			acp.setInboundIpv4AclName("infra-acl-do-not-add-or-edit");
		}

		acp.setIsinboundaclIpv4Preprovisioned((byte) 1);
		acp.setIsinboundaclIpv4Applied((byte) 1);
		acp.setEthernetInterface(ethernetInterface);
		acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		acp.setModifiedBy(MODIFIEDBY);
		aclPolicyCriteriaRepository.save(acp);
	}

	public Set<WanStaticRoute> saveWanStaticRoutes(ServiceDetail serviceDetail, RouterDetail routerDetail,
			StaticProtocol staticProtocol, String type) throws TclCommonException {
		logger.info("Service Activation - saveWanStaticRoutes - started");
		Set<WanStaticRoute> wanStaticRoutes = new HashSet<>();

		try {
			IpAddressDetail ipAddressDetail = serviceDetail.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

			if (ipAddressDetail != null) {

				if (ipAddressDetail.getIpaddrLanv4Addresses() != null
						|| ipAddressDetail.getIpaddrLanv6Addresses() != null) {
					softDeleteWanRoutes(staticProtocol);
				}

				if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {

					for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
						WanStaticRoute wanStaticRoute = new WanStaticRoute();

						if (Objects.nonNull(routerDetail)
								&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

							List<MstRegionalPopCommunity> mstRegionalPopCommunityList = mstRegionalPopCommunityRepository
									.findByRouterHostname(routerDetail.getRouterHostname());
							if (!mstRegionalPopCommunityList.isEmpty()) {
								wanStaticRoute.setPopCommunity(mstRegionalPopCommunityList.get(0).getPopCommunity());
								wanStaticRoute.setRegionalCommunity(
										mstRegionalPopCommunityList.get(0).getRegionalCommunity());

							}
						} else {
							wanStaticRoute.setPopCommunity(null);
							wanStaticRoute.setRegionalCommunity(null);
						}

						if (Objects.nonNull(routerDetail)
								&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							List<MstServiceCommunity> mstServiceCommunityList = mstServiceCommunityRepository
									.findByServiceSubtype(serviceDetail.getServiceSubtype());
							if (!mstServiceCommunityList.isEmpty()) {
								wanStaticRoute.setServiceCommunity(mstServiceCommunityList.get(0).getServiceCmmunity());
							}
						} else {
							wanStaticRoute.setServiceCommunity("");
						}

						wanStaticRoute.setStaticProtocol(staticProtocol);
						wanStaticRoute.setIsPewan((byte) 1);
						wanStaticRoute.setIsCewan((byte) 0);
						wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
						wanStaticRoute.setIsCpelanStaticroutes((byte) 0);

						wanStaticRoute.setIpsubnet(ipaddrLanv4Address.getLanv4Address()!=null ? ipaddrLanv4Address.getLanv4Address().trim():ipaddrLanv4Address.getLanv4Address());// confirm

						if (wanStaticRoute.getIpsubnet() != null) {
							List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetail
									.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
											&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
									.collect(Collectors.toList());

							cpeProtocolMappings
									.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());

							if (cpeProtocolMappings != null && !cpeProtocolMappings.isEmpty()) {
								InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);
								wanStaticRoute.setNextHopid(findCpeInterfaceIpv4Ethernet(cpeProtocolMapping));

							}

							wanStaticRoute.setGlobal((byte) 0);
							wanStaticRoute.setModifiedBy(MODIFIEDBY);
							wanStaticRoute.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
							wanStaticRouteRepository.save(wanStaticRoute);

							wanStaticRoutes.add(wanStaticRoute);

						}
					}
				}

				if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {

					for (IpaddrLanv6Address ipaddrLanv6Address : ipAddressDetail.getIpaddrLanv6Addresses()) {
						WanStaticRoute wanStaticRoute = new WanStaticRoute();

						if (Objects.nonNull(routerDetail)
								&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

							List<MstRegionalPopCommunity> mstRegionalPopCommunityList = mstRegionalPopCommunityRepository
									.findByRouterHostname(routerDetail.getRouterHostname());
							if (!mstRegionalPopCommunityList.isEmpty()) {
								wanStaticRoute.setPopCommunity(mstRegionalPopCommunityList.get(0).getPopCommunity());
								wanStaticRoute.setRegionalCommunity(
										mstRegionalPopCommunityList.get(0).getRegionalCommunity());

							}
						} else {
							wanStaticRoute.setPopCommunity(null);
							wanStaticRoute.setRegionalCommunity(null);
						}

						if (Objects.nonNull(routerDetail)
								&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							List<MstServiceCommunity> mstServiceCommunityList = mstServiceCommunityRepository
									.findByServiceSubtype(serviceDetail.getServiceSubtype());
							if (!mstServiceCommunityList.isEmpty()) {
								wanStaticRoute.setServiceCommunity(mstServiceCommunityList.get(0).getServiceCmmunity());
							}
						} else {
							wanStaticRoute.setServiceCommunity("");
						}

						wanStaticRoute.setStaticProtocol(staticProtocol);
						wanStaticRoute.setIsPewan((byte) 1);
						wanStaticRoute.setIsCewan((byte) 0);
						wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
						wanStaticRoute.setIsCpelanStaticroutes((byte) 0);

						wanStaticRoute.setIpsubnet(ipaddrLanv6Address.getLanv6Address()!=null ?ipaddrLanv6Address.getLanv6Address().trim():ipaddrLanv6Address.getLanv6Address());

						if (wanStaticRoute.getIpsubnet() != null) {
							List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetail
									.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
											&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
									.collect(Collectors.toList());

							cpeProtocolMappings
									.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());

							if (cpeProtocolMappings != null && !cpeProtocolMappings.isEmpty()) {
								InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);
								wanStaticRoute.setNextHopid(findCpeInterfaceIpv6Ethernet(cpeProtocolMapping));

							}
						}

						wanStaticRoute.setGlobal((byte) 0);
						wanStaticRoute.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						wanStaticRoute.setModifiedBy(MODIFIEDBY);
						wanStaticRouteRepository.save(wanStaticRoute);

						wanStaticRoutes.add(wanStaticRoute);

					}
				}

			}

		} catch (Exception e) {
			logger.error("Exception in saveWanStaticRoutes => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return wanStaticRoutes;

	}

	private void applyRFRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields) {
		LmComponent lmComponent = serviceDetails.getLmComponents().stream().filter(lm -> lm.getEndDate() == null)
				.findFirst().orElse(null);

		if (lmComponent != null) {
			applyCambiumRule(serviceDetails, manDatoryFields, lmComponent);

			applyRadwinRule(lmComponent, manDatoryFields, serviceDetails);
		}

	}

	private void createPoliciesForVrf(Vrf vrf, ServiceDetail serviceDetail, String routerMake) throws TclCommonException {
		if(routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU) &&
				!vrf.getVrfName().equalsIgnoreCase("NOT_APPLICABLE") && !vrf.getVrfName().equalsIgnoreCase("NA")){
			PolicyType policyType1 = new PolicyType();
			policyType1.setVrf(vrf);
			policyType1.setIsvprnImportpolicy((byte)1);
			policyType1.setIsvprnImportPreprovisioned((byte)1);
			policyType1.setIsvprnImportpolicyName("S-VPRN-IMPORT_" + serviceDetail.getCssSammgrId());
			policyType1.setModifiedBy(MODIFIEDBY);
			policyType1.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyTypeRepository.save(policyType1);
			PolicyType policyType2 = new PolicyType();
			policyType2.setVrf(vrf);
			policyType2.setIsvprnExportpolicy((byte)1);
			policyType2.setIsvprnExportPreprovisioned((byte)1);
			policyType2.setIsvprnExportpolicyName("S-VPRN-EXPORT_" + serviceDetail.getCssSammgrId());
			policyType2.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType2.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType2);
		}
	}

	
}
