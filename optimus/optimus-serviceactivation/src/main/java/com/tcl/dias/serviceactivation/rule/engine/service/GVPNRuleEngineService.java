
package com.tcl.dias.serviceactivation.rule.engine.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
import com.tcl.dias.serviceactivation.entity.entities.MstGvpnAluCustId;
import com.tcl.dias.serviceactivation.entity.entities.MstRegionalPopCommunity;
import com.tcl.dias.serviceactivation.entity.entities.MstServiceCommunity;
import com.tcl.dias.serviceactivation.entity.entities.MstVpnSamManagerId;
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PolicycriteriaProtocol;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
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
import com.tcl.dias.serviceactivation.entity.repository.BgpRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedE1serialInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedSdhInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.CpeRepository;
import com.tcl.dias.serviceactivation.entity.repository.EthernetInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstGvpnAluCustIdRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRegionalPopCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstServiceCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstVpnSamManagerIdRepository;
import com.tcl.dias.serviceactivation.entity.repository.MulticastingRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.OspfRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeCriteriaMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicycriteriaProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceQoRepository;
import com.tcl.dias.serviceactivation.entity.repository.UniswitchDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnMetatDataRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.VrfRepository;
import com.tcl.dias.serviceactivation.entity.repository.WanStaticRouteRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.StringUtils;

@Service
public class GVPNRuleEngineService extends AceBaseRuleEngine implements IRuleEngine {

	private static final Logger logger = LoggerFactory.getLogger(GVPNRuleEngineService.class);

	@Autowired
	private ServiceDetailRepository serviceDetailRepository;

	@Autowired
	private WanStaticRouteRepository wanStaticRouteRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private RouterDetailRepository routerDetailRepository;

	@Autowired
	private CpeRepository cpeRepository;

	@Autowired
	private OspfRepository ospfRepository;

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
	private VrfRepository vrfRepository;

	@Autowired
	private AluSammgrSeqRepository aluSammgrSeqRepository;

	@Autowired
	private AclPolicyCriteriaRepository aclPolicyCriteriaRepository;

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
	private VpnSolutionRepository vpnSolutionRepository;

	@Autowired
	private VpnMetatDataRepository vpnMetatDataRepository;

	@Autowired
	private MstGvpnAluCustIdRepository mstGvpnAluCustIdRepository;

	@Autowired
	private MstVpnSamManagerIdRepository mstVpnmanagerIdRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	private CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	private ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	MulticastingRepository multicastingRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ServiceActivationService serviceActivationService;

	@Value("${cramer.source.system}")
	String cramerSourceSystem;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	private IpaddrWanv4AddressRepository ipaddrWanv4AddressRepository;

	public boolean applyRule(ServiceDetail serviceDetails) throws TclCommonException {
		boolean mandatoryFields = true;
		logger.info("GVPN applyRule serviceId::{}", serviceDetails.getId());
		try {
			if (serviceDetails != null) {
				ServiceDetail parentServiceDetail = null;
				OrderDetail orderDetail = serviceDetails.getOrderDetail();
				String orderCategory = com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping
						.getOrderCategory(serviceDetails, orderDetail);

				if ("ADD_SITE".equalsIgnoreCase(orderCategory) || (Objects.nonNull(serviceDetails.getOrderSubCategory())
						&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel"))) {
					parentServiceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(
							serviceDetails.getOldServiceId(), "ACTIVE");
				} else if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())
						&& "NEW".equalsIgnoreCase(serviceDetails.getOrderType())) {
					logger.info("IZOPC New order with Fulfillment Service Id::{}",
							serviceDetails.getScServiceDetailId());
					ScServiceAttribute referenceServiceIdAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(
									serviceDetails.getScServiceDetailId(), "Service Id");
					if (referenceServiceIdAttribute != null
							&& referenceServiceIdAttribute.getAttributeValue() != null) {
						logger.info("IZOPC New order Reference Service Id exists::{}",
								referenceServiceIdAttribute.getAttributeValue());
						parentServiceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(
								referenceServiceIdAttribute.getAttributeValue(), "ACTIVE");
					}
				}
				Map<String, String> manDatoryFields = findMandatoryValues(orderDetail, serviceDetails);
				logger.info("Current Mandatory details::{}", manDatoryFields);
				VpnMetatData vpnMetatData = applyVpnMetata(serviceDetails, manDatoryFields);

				Boolean isChangedVrfExists = false;
				if ((("ADD_SITE".equalsIgnoreCase(orderCategory))
						|| (Objects.nonNull(serviceDetails.getOrderSubCategory())
								&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel"))
						|| ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())
								&& "NEW".equalsIgnoreCase(serviceDetails.getOrderType())))) {
					ScComponentAttribute scComponentChangedVrfAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									serviceDetails.getScServiceDetailId(), "changedVRFName", "LM", "A");
					if (scComponentChangedVrfAttribute != null
							&& scComponentChangedVrfAttribute.getAttributeValue() != null
							&& !scComponentChangedVrfAttribute.getAttributeValue().isEmpty()) {
						logger.info("GVPN.VRF Name by Changed VrfName::{}",
								scComponentChangedVrfAttribute.getAttributeValue());
						isChangedVrfExists = true;
						String changedVrfName = scComponentChangedVrfAttribute.getAttributeValue();
						vpnMetatData.setVpnName(changedVrfName);
						vpnMetatData.setVpnAlias(changedVrfName);
						vpnMetatData.setSolutionId(changedVrfName);
						vpnMetatData.setVpnSolutionName(changedVrfName);
						vpnMetatDataRepository.saveAndFlush(vpnMetatData);
						if (Objects.nonNull(vpnMetatData.getVpnName()) && !vpnMetatData.getVpnName().isEmpty()
								&& vpnMetatData.getVpnName().equalsIgnoreCase("STATE_BANK")) {
							logger.info("Set managementVpnType1 as NULL for STATE_BANK");
							vpnMetatData.setManagementVpnType1(null);
						}
						vpnMetatDataRepository.saveAndFlush(vpnMetatData);
						serviceDetails.setSolutionId(changedVrfName);
						serviceDetailRepository.save(serviceDetails);
					} else if (Objects.nonNull(parentServiceDetail)) {
						logger.info("Changed VrfName not Exists, Parent Service Detail Exists");
						applyVpnMetataAddSiteRule(vpnMetatData, serviceDetails, parentServiceDetail);
					}
				}
				if ("ADD_SITE".equalsIgnoreCase(orderCategory) && 
						(parentServiceDetail == null || (parentServiceDetail!=null && (parentServiceDetail.getVpnMetatDatas()==null || parentServiceDetail.getVpnMetatDatas().isEmpty()))) 
						&& !isChangedVrfExists) {
					logger.info("ADD SITE::Parent migration needed");
					vpnMetatData.setVpnName(null);
					vpnMetatData.setVpnAlias(null);
					vpnMetatData.setSolutionId(null);
					vpnMetatData.setVpnSolutionName(null);
					vpnMetatDataRepository.saveAndFlush(vpnMetatData);
				}
				applyVrf(serviceDetails, manDatoryFields);
				if ((("ADD_SITE".equalsIgnoreCase(serviceDetails.getOrderCategory()))
						|| (Objects.nonNull(serviceDetails.getOrderSubCategory())
								&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel"))
						|| ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())
								&& "NEW".equalsIgnoreCase(serviceDetails.getOrderType())))
						&& Objects.nonNull(parentServiceDetail)) {
					applyOrderAddSiteRule(orderDetail, serviceDetails, manDatoryFields, vpnMetatData,
							parentServiceDetail);
					applyServiceDetailsAddSiteRule(serviceDetails, manDatoryFields, vpnMetatData, parentServiceDetail);
				} else {
					applyOrderRule(orderDetail, serviceDetails, manDatoryFields, vpnMetatData);
					applyServiceDetailsRule(serviceDetails, manDatoryFields, vpnMetatData);
				}
				applyInterfaceRule(serviceDetails, manDatoryFields);
				applyOspfRule(serviceDetails, manDatoryFields);
				applyRoterDetails(serviceDetails, manDatoryFields);
				applyCPEForPeSide(serviceDetails, manDatoryFields);
				applyCESideRule(serviceDetails, manDatoryFields);
				applyUinRule(serviceDetails, manDatoryFields);
				applyServiceQos(serviceDetails, manDatoryFields);
				applyServiceQosMultiCastRule(serviceDetails, parentServiceDetail, orderDetail);
				applyALUpolicyRule(serviceDetails, manDatoryFields);
				applyRFRule(serviceDetails, manDatoryFields);
				applyVPnSolution(serviceDetails, manDatoryFields, vpnMetatData);
				if (!CollectionUtils.isEmpty(serviceDetails.getVrfs())) {
					if (manDatoryFields.containsKey(AceConstants.ROUTER.ROUTER_MAKE)) {
						createPoliciesForVrf(serviceDetails.getVrfs().stream().findFirst().get(), serviceDetails,
								manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE));
					}
				}

				/*
				 * if (("ADD_SITE".equalsIgnoreCase(serviceDetails.getOrderCategory()) ||
				 * (Objects.nonNull(serviceDetails.getOrderSubCategory()) &&
				 * serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel")) ||
				 * ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName()) &&
				 * "NEW".equalsIgnoreCase(serviceDetails.getOrderType()))) &&
				 * Objects.nonNull(parentServiceDetail) &&
				 * "ALCATEL IP".equals(serviceDetails.getRouterMake())) { String siteRole =
				 * getSiteRole(serviceDetails); List<InterfaceProtocolMapping>
				 * parentInterfaceProtocolMappings = parentServiceDetail
				 * .getInterfaceProtocolMappings().stream() .filter(ser -> ser.getCpe() == null
				 * && ser.getEndDate() == null) .collect(Collectors.toList());
				 * List<InterfaceProtocolMapping> currentInterfaceProtocolMappings =
				 * serviceDetails .getInterfaceProtocolMappings().stream() .filter(ser ->
				 * ser.getCpe() == null && ser.getEndDate() == null)
				 * .collect(Collectors.toList()); if (!parentInterfaceProtocolMappings.isEmpty()
				 * && !currentInterfaceProtocolMappings.isEmpty()) {
				 * parentInterfaceProtocolMappings
				 * .sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				 * InterfaceProtocolMapping parentInterfaceProtocolMapping =
				 * parentInterfaceProtocolMappings .get(0); currentInterfaceProtocolMappings
				 * .sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				 * InterfaceProtocolMapping currentInterfaceProtocolMapping =
				 * currentInterfaceProtocolMappings .get(0); if
				 * (!siteRole.equalsIgnoreCase("SPOKE")) {
				 * logger.info("Copying Bgp from previous for Hub or Mesh:: {}",
				 * serviceDetails.getServiceId()); if
				 * (Objects.nonNull(parentInterfaceProtocolMapping) &&
				 * Objects.nonNull(parentInterfaceProtocolMapping.getBgp()) &&
				 * Objects.nonNull(currentInterfaceProtocolMapping) &&
				 * Objects.nonNull(currentInterfaceProtocolMapping.getBgp())) { Bgp bgp =
				 * currentInterfaceProtocolMapping.getBgp(); if
				 * (Objects.nonNull(parentInterfaceProtocolMapping.getBgp().getBgpPeerName())) {
				 * bgp.setBgpPeerName(parentInterfaceProtocolMapping.getBgp().getBgpPeerName());
				 * } if
				 * (Objects.nonNull(parentInterfaceProtocolMapping.getBgp().getRemoteAsNumber())
				 * ) { bgp.setRemoteAsNumber(parentInterfaceProtocolMapping.getBgp().
				 * getRemoteAsNumber()); } bgpRepository.save(bgp); } } else {
				 * logger.info("Copying Bgp from previous for Spoke:: {}",
				 * serviceDetails.getServiceId()); if
				 * (Objects.nonNull(parentInterfaceProtocolMapping) &&
				 * Objects.nonNull(parentInterfaceProtocolMapping.getBgp()) &&
				 * Objects.nonNull(currentInterfaceProtocolMapping) &&
				 * Objects.nonNull(currentInterfaceProtocolMapping.getBgp())) { Bgp bgp =
				 * currentInterfaceProtocolMapping.getBgp(); if
				 * (Objects.nonNull(parentInterfaceProtocolMapping.getBgp().getRemoteAsNumber())
				 * ) { bgp.setRemoteAsNumber(parentInterfaceProtocolMapping.getBgp().
				 * getRemoteAsNumber()); } bgpRepository.save(bgp); } } } }
				 */

				applyMultiCastRule(orderDetail, parentServiceDetail, serviceDetails);

				if ((("ADD_SITE".equalsIgnoreCase(orderDetail.getOrderCategory()))
						|| (Objects.nonNull(serviceDetails.getOrderSubCategory())
								&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel")))
						&& Objects.nonNull(parentServiceDetail) && Objects.nonNull(parentServiceDetail.getServiceQos())
						&& Objects.nonNull(serviceDetails.getServiceQos())) {
					logger.info("Parent and Current Qos exists for Id::{}", serviceDetails.getId());
					if (Objects.nonNull(parentServiceDetail.getServiceQos()) && Objects.nonNull(
							parentServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias())) { // 24-12-2020
																														// Ashish
																														// Kumar
						Set<ServiceCosCriteria> prevCosCriteria = parentServiceDetail.getServiceQos().stream()
								.findFirst().get().getServiceCosCriterias();
						Set<ServiceCosCriteria> currentCosCriteria = serviceDetails.getServiceQos().stream().findFirst()
								.get().getServiceCosCriterias();
						applyCosAclPolicyCriteria(currentCosCriteria, prevCosCriteria);
					}
				}

				if (Objects.nonNull(serviceDetails.getOrderSubCategory())
						&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel")) {
					logger.info("GVPN Rule Engine:Parallel Up/DownTime");
					serviceDetails.setIsdowntimeReqd((byte) 1);
					serviceDetails.setSkipDummyConfig((byte) 1);
					serviceDetailRepository.save(serviceDetails);
				}

				if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
					logger.info("IZOPC Service Id::{}", serviceDetails.getId());
					ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
									"Cloud Provider");
					if (cloudProviderAttribute != null
							&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
							&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
						logger.info("IZOPC IBM CSP Sy Service Id::{} for wan sy update", serviceDetails.getId());
						IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
								.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
						if (ipAddressDetail != null) {
							logger.info("IZOPC IpAddressDetail exists for Service Id::{}", serviceDetails.getId());
							if (ipAddressDetail.getIpaddrWanv4Addresses() != null) {
								ipAddressDetail.getIpaddrWanv4Addresses().forEach(wanv4 -> {
									if (wanv4 != null) {
										logger.info(
												"IZOPC WanIpAddressDetail exists for Service Id::{} with wanv4 Id::{}",
												serviceDetails.getId(), wanv4.getWanv4AddrId());
										wanv4.setIssecondary((byte) 1);
										ipaddrWanv4AddressRepository.saveAndFlush(wanv4);
									}
								});
							}

						}
					}
				}

				if ("GVPN".equalsIgnoreCase(serviceDetails.getServiceType())
						&& !"IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
					logger.info("GVPN Service ID::{}", serviceDetails.getId());
					List<InterfaceProtocolMapping> currentInterfaceProtocolMappings = serviceDetails
							.getInterfaceProtocolMappings().stream()
							.filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
							.collect(Collectors.toList());
					currentInterfaceProtocolMappings
							.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
					InterfaceProtocolMapping currentInterfaceProtocolMapping = currentInterfaceProtocolMappings.get(0);
					if (Objects.nonNull(currentInterfaceProtocolMapping)
							&& Objects.nonNull(currentInterfaceProtocolMapping.getBgp()) && vpnMetatData != null
							&& vpnMetatData.getVpnName() != null) {
						logger.info("GVPN Service ID with ipm and vpn name exists::{}", serviceDetails.getId());
						MstVpnSamManagerId mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findFirstByVpnNameAndBgpPasswordIsNotNull(vpnMetatData.getVpnName());
						if (mstVpnSamManagerId != null && mstVpnSamManagerId.getBgpPassword() != null
								&& !mstVpnSamManagerId.getBgpPassword().isEmpty()) {
							logger.info("GVPN Service ID::{} with MstVpnSamManagerId::{} exists with password::{}",
									serviceDetails.getId(), mstVpnSamManagerId.getId(),
									mstVpnSamManagerId.getBgpPassword());
							Bgp bgp = currentInterfaceProtocolMapping.getBgp();
							bgp.setPassword(mstVpnSamManagerId.getBgpPassword());
							bgp.setAuthenticationMode("MD5");
							bgp.setIsauthenticationRequired((byte) 1);
							bgpRepository.saveAndFlush(bgp);
						}

					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return mandatoryFields;

	}

	private void applyCosAclPolicyCriteria(Set<ServiceCosCriteria> serviceCos,
			Set<ServiceCosCriteria> prevServiceCosCriteria) {
		logger.info("GVPNRuleEngine.applyCosAclPolicyCriteria");
		if (!serviceCos.isEmpty()) {
			logger.info("Current Cos exists");
			List<ServiceCosCriteria> ipAddressServiceCos = serviceCos.stream()
					.filter(cosCriteria -> Objects.nonNull(cosCriteria.getClassificationCriteria())
							&& !cosCriteria.getClassificationCriteria().isEmpty()
							&& cosCriteria.getClassificationCriteria().toLowerCase().contains("address"))
					.collect(Collectors.toList());
			List<ServiceCosCriteria> prevIpAddressServiceCos = prevServiceCosCriteria.stream()
					.filter(prevCosCriteria -> Objects.nonNull(prevCosCriteria.getClassificationCriteria())
							&& !prevCosCriteria.getClassificationCriteria().isEmpty()
							&& prevCosCriteria.getClassificationCriteria().toLowerCase().contains("address"))
					.collect(Collectors.toList());
			if (!ipAddressServiceCos.isEmpty() && !prevIpAddressServiceCos.isEmpty()) {
				logger.info("Prev and Current Ip Address Cos not empty");
				Optional<ServiceCosCriteria> optionalCurrentServiceCosCriteria = ipAddressServiceCos.stream()
						.findFirst();
				Optional<ServiceCosCriteria> optionalPrevServiceCosCriteria = prevIpAddressServiceCos.stream()
						.findFirst();
				if (optionalCurrentServiceCosCriteria.isPresent() && optionalPrevServiceCosCriteria.isPresent()) {
					logger.info("Prev and Current Ip Address Cos exists");
					ServiceCosCriteria currentCosCriteria = optionalCurrentServiceCosCriteria.get();
					ServiceCosCriteria prevCosCriteria = optionalPrevServiceCosCriteria.get();
					if (Objects.nonNull(prevCosCriteria.getAclPolicyCriterias())
							&& !prevCosCriteria.getAclPolicyCriterias().isEmpty()) {
						prevCosCriteria.getAclPolicyCriterias().stream().forEach(prevAclPolicy -> {
							logger.info("Copy Prev Acl Policy");
							AclPolicyCriteria acp = new AclPolicyCriteria();
							acp.setAction(prevAclPolicy.getAction());
							acp.setDestinationAny(prevAclPolicy.getDestinationAny());
							acp.setDestinationSubnet(prevAclPolicy.getDestinationSubnet());
							acp.setSourceAny(prevAclPolicy.getSourceAny());
							acp.setSourceSubnet(prevAclPolicy.getSourceSubnet());
							acp.setSequence(prevAclPolicy.getSequence());
							acp.setProtocol(prevAclPolicy.getProtocol());
							acp.setServiceCosCriteria(currentCosCriteria);
							acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
							acp.setModifiedBy(MODIFIEDBY);
							aclPolicyCriteriaRepository.save(acp);
						});
					}
				}
			}
		}
	}

	private void applyServiceQosMultiCastRule(ServiceDetail serviceDetails, ServiceDetail parentServiceDetail,
			OrderDetail orderDetail) {
		logger.info("applyServiceQosMultiCastRule invoked");
		String orderCategory = com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping
				.getOrderCategory(serviceDetails, orderDetail);

		if (("ADD_SITE".equalsIgnoreCase(orderCategory)
				|| (Objects.nonNull(serviceDetails.getOrderSubCategory())
						&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel"))
				|| ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())
						&& "NEW".equalsIgnoreCase(serviceDetails.getOrderType())))
				&& Objects.nonNull(parentServiceDetail) && Objects.nonNull(parentServiceDetail.getMulticastings())
				&& !parentServiceDetail.getMulticastings().isEmpty()) {
			logger.info("Parent MultiCast Exists");
			ServiceQo serviceQo = serviceDetails.getServiceQos().stream().filter(serQo -> serQo.getEndDate() == null)
					.findFirst().orElse(null);
			if (serviceQo != null) {
				logger.info("Current ServiceQo Exists");
				serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.BOTH);
				serviceQoRepository.save(serviceQo);
			}
		}

	}

	private void applyMultiCastRule(OrderDetail orderDetail, ServiceDetail parentServiceDetail,
			ServiceDetail serviceDetails) {
		logger.info("applyMultiCastRuleForAddSite");
		String orderCategory = com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping
				.getOrderCategory(serviceDetails, orderDetail);

		if (("ADD_SITE".equalsIgnoreCase(orderCategory)
				|| (Objects.nonNull(serviceDetails.getOrderSubCategory())
						&& serviceDetails.getOrderSubCategory().toLowerCase().contains("parallel"))
				|| ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())
						&& "NEW".equalsIgnoreCase(serviceDetails.getOrderType())))
				&& Objects.nonNull(parentServiceDetail) && Objects.nonNull(parentServiceDetail.getMulticastings())
				&& !parentServiceDetail.getMulticastings().isEmpty()) {
			logger.info("Prev MultiCast Exists");
			Optional<Multicasting> parentMultiCastOptional = parentServiceDetail.getMulticastings().stream()
					.findFirst();
			if (Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getMulticastings())
					&& !serviceDetails.getMulticastings().isEmpty()) {
				logger.info("Current MultiCast Exists");
				Optional<Multicasting> currentMultiCastOptional = serviceDetails.getMulticastings().stream()
						.findFirst();
				if (currentMultiCastOptional.isPresent() && parentMultiCastOptional.isPresent()) {
					logger.info("Both MultiCast Exists");
					Multicasting parentMultiCast = parentMultiCastOptional.get();
					Multicasting currentMultiCast = currentMultiCastOptional.get();
					if (Objects.nonNull(parentMultiCast.getRpAddress()) && !parentMultiCast.getRpAddress().isEmpty()) {
						logger.info("Copy RpAddress from Previous");
						currentMultiCast.setRpAddress(parentMultiCast.getRpAddress());
					}
					if (Objects.nonNull(parentMultiCast.getDataMdt()) && !parentMultiCast.getDataMdt().isEmpty()) {
						logger.info("Copy DataMdt from Previous");
						currentMultiCast.setDataMdt(parentMultiCast.getDataMdt());
					}
					if (Objects.nonNull(parentMultiCast.getDefaultMdt())
							&& !parentMultiCast.getDefaultMdt().isEmpty()) {
						logger.info("Copy DefaultMdt from Previous");
						currentMultiCast.setDefaultMdt(parentMultiCast.getDefaultMdt());
					}
					if (Objects.nonNull(parentMultiCast.getType()) && !parentMultiCast.getType().isEmpty()) {
						logger.info("Copy Type from Previous");
						currentMultiCast.setType(parentMultiCast.getType());
					}
					currentMultiCast.setLastModifiedDate(new Timestamp(new Date().getTime()));
					currentMultiCast.setModifiedBy("OPTIMUS_RULE");
					multicastingRepository.save(currentMultiCast);
				}
			} else {
				logger.info("Current MultiCast doesn't Exists");
				if (parentMultiCastOptional.isPresent()) {
					logger.info("Prev MultiCast Exists");
					Multicasting parentMultiCast = parentMultiCastOptional.get();
					Multicasting multiCastingEntity = new Multicasting();
					if (Objects.nonNull(parentMultiCast.getRpAddress()) && !parentMultiCast.getRpAddress().isEmpty()) {
						logger.info("Copy RpAddress from Previous");
						multiCastingEntity.setRpAddress(parentMultiCast.getRpAddress());
					}
					if (Objects.nonNull(parentMultiCast.getDataMdt()) && !parentMultiCast.getDataMdt().isEmpty()) {
						logger.info("Copy DataMdt from Previous");
						multiCastingEntity.setDataMdt(parentMultiCast.getDataMdt());
					}
					if (Objects.nonNull(parentMultiCast.getDefaultMdt())
							&& !parentMultiCast.getDefaultMdt().isEmpty()) {
						logger.info("Copy DefaultMdt from Previous");
						multiCastingEntity.setDefaultMdt(parentMultiCast.getDefaultMdt());
					}
					if (Objects.nonNull(parentMultiCast.getType()) && !parentMultiCast.getType().isEmpty()) {
						logger.info("Copy Type from Previous");
						multiCastingEntity.setType(parentMultiCast.getType());
					}
					multiCastingEntity.setWanPimMode("SPARSE");
					multiCastingEntity.setDataMdtThreshold("1");
					multiCastingEntity.setRpLocation("CE");
					multiCastingEntity.setAutoDiscoveryOption("MDT_SAFI");
					multiCastingEntity.setStartDate(new Timestamp(new Date().getTime()));
					multiCastingEntity.setLastModifiedDate(new Timestamp(new Date().getTime()));
					multiCastingEntity.setModifiedBy("OPTIMUS_RULE_INITIAL");
					multiCastingEntity.setServiceDetail(serviceDetails);
					multiCastingEntity = multicastingRepository.save(multiCastingEntity);
				}

			}
		}

	}

	private void applyServiceDetailsAddSiteRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields,
			VpnMetatData vpnMetatData, ServiceDetail parentServiceDetail) throws TclCommonException {
		String vrfName = manDatoryFields.get(AceConstants.VRF.VRF_NAME);

		String routerMake = manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE);
		MstVpnSamManagerId mstVpnSamManagerId = null;

		serviceDetails.setIsdowntimeReqd((byte) 0);
		serviceDetails.setExpediteTerminate((byte) 0);
		serviceDetails.setExternalRefid(null);
		serviceDetails.setServiceState("ISSUED");
		serviceDetails.setTriggerNccmOrder((byte) 0);
		serviceDetails.setOrderType("NEW");
		serviceDetails.setIscustomConfigReqd((byte) 0);
		serviceDetails.setIntefaceDescSvctag("GVPN");
		serviceDetails.setSkipDummyConfig((byte) 1);
		String siteRole = getSiteRole(serviceDetails);
		if (serviceDetails.getOrderCategory() == null) {
			serviceDetails.setOrderCategory("CUSTOMER_ORDER");
		}
		serviceDetails.setOrderType("NEW");
		serviceDetails.setRouterMake(routerMake);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			serviceDetails.setIsIdcService((byte) 0);
			serviceDetails.setUsageModel("FIXED");
		}

		if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerMake)) {
			if (siteRole.toLowerCase().contains("hub")) {
				mstVpnSamManagerId = mstVpnmanagerIdRepository.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRole(
						vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails), siteRole);
			} else if (siteRole.toLowerCase().contains("mesh")) {
				mstVpnSamManagerId = mstVpnmanagerIdRepository.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(
						vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails), siteRole);
			} else {
				mstVpnSamManagerId = mstVpnmanagerIdRepository
						.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
								"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(), siteRole);
			}
			Integer cssSamcustomerId = 0;
			if (siteRole.equalsIgnoreCase("SPOKE")) {
				logger.info("Spoke");
				if (Objects.isNull(mstVpnSamManagerId)) {
					logger.info("Set Spoke mstVpn doesn't exists");
					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
							serviceDetails.getServiceSubtype());
					if (aluSammgrSeq != null) {
						logger.info("Set AluSam");
						cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
					}
				} else {
					logger.info("Set Spoke mstVpn exists");
					cssSamcustomerId = Integer.valueOf(mstVpnSamManagerId.getSamMgrId());
				}
			} else {
				logger.info("Hub or Mesh");
				if (Objects.nonNull(parentServiceDetail.getCssSammgrId()) && Objects.nonNull(mstVpnSamManagerId)) {
					if (parentServiceDetail.getCssSammgrId() != Integer.valueOf(mstVpnSamManagerId.getSamMgrId())) {
						logger.info("Copying SamMgr even mstVpn exists");
						cssSamcustomerId = parentServiceDetail.getCssSammgrId();
					} else {
						logger.info("Set mstVpn exists");
						cssSamcustomerId = Integer.valueOf(mstVpnSamManagerId.getSamMgrId());
					}
				} else if (Objects.isNull(mstVpnSamManagerId)) {
					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
							serviceDetails.getServiceSubtype());
					if (aluSammgrSeq != null) {
						logger.info("Set AluSam");
						cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
					}
				} else if (Objects.nonNull(mstVpnSamManagerId)) {
					logger.info("Set mstVpn exists");
					cssSamcustomerId = Integer.valueOf(mstVpnSamManagerId.getSamMgrId());
				}
			}
			serviceDetails.setCssSammgrId(cssSamcustomerId);
			serviceDetails.setAluSvcName(serviceDetails.getOrderDetail().getSamCustomerDescription() + "_"
					+ serviceDetails.getCssSammgrId() + "_" + "ACE");

		}
		serviceDetails.setAluSvcName(null);
		serviceDetails.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		serviceDetails.setModifiedBy(MODIFIEDBY);
		/*
		 * if("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
		 * logger.info("Izopc applyServiceDetailsAddSiteRule::{}",serviceDetails.
		 * getServiceId()); ScServiceAttribute cloudProviderAttribute =
		 * scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(
		 * serviceDetails.getScServiceDetailId(), "Cloud Provider");
		 * if(cloudProviderAttribute!=null &&
		 * !"IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue(
		 * ))) {
		 * logger.info("Izopc Redudancy Role::Other than IBM CSP for Service Id::{}"
		 * ,serviceDetails.getServiceId()); serviceDetails.setRedundancyRole("Primary");
		 * } }
		 */
		serviceDetailRepository.save(serviceDetails);
	}

	private void applyOrderAddSiteRule(OrderDetail orderDetail, ServiceDetail serviceDetails, Map<String, String> map,
			VpnMetatData vpnMetatData, ServiceDetail parentServiceDetail) throws TclCommonException {

		String vrfName = map.get(AceConstants.VRF.VRF_NAME);
		logger.info("vrfName ={}", vrfName);
		if (vrfName == null)
			vrfName = AceConstants.DEFAULT.NOT_APPLICABLE;
		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (orderDetail.getCustomerCrnId() == null) {
			logger.error("CRN Id not found for serviceId : {} ", serviceDetails.getServiceId());
			throw new TclCommonException(ExceptionConstants.NO_CRN_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		}

		/*
		 * if (orderDetail.getOrderCategory() == null) {
		 * orderDetail.setOrderCategory("CUSTOMER_ORDER"); }
		 */
		MstVpnSamManagerId mstVpnSamManagerId = null;
		orderDetail.setAsdOpptyFlag((byte) 0);
		orderDetail.setModifiedBy(MODIFIEDBY);
		orderDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		// orderDetail.setOrderType("NEW");
		orderDetail.setGroupId("CMIP");
		// orderDetail.setOriginatorName("OPTIMUS");
		orderDetail.setOriginatorName(cramerSourceSystem);
		orderDetail.setOriginatorDate(new Timestamp(System.currentTimeMillis()));
		orderDetail.setLocation(orderDetail.getCity());
		orderDetail.setOrderStatus("IN PROGRESS");// other status are cancelled ,closed,jeopardy
		orderDetail.setOrderUuid(AceConstants.ORDER.SERVICE_NEW_ + orderDetail.getExtOrderrefId());
		String siteRole = getSiteRole(serviceDetails);

		if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			if (map.containsKey(AceConstants.VRF.VRF_NAME) && map.containsKey(AceConstants.SERVICE.SERVICE_TYPE)
					&& map.get(AceConstants.VRF.VRF_NAME).equalsIgnoreCase(AceConstants.DEFAULT.NOT_APPLICABLE)
					|| map.get(AceConstants.VRF.VRF_NAME).equalsIgnoreCase(AceConstants.DEFAULT.NA)) {
				if (orderDetail.getCustomerName().length() > 18) {
					orderDetail.setSamCustomerDescription(
							orderDetail.getCustomerName().substring(0, 17).replaceAll(" ", "_"));
				} else {
					orderDetail.setSamCustomerDescription(orderDetail.getCustomerName().replaceAll(" ", "_"));
				}
			}

			if (vrfName != null) {
				Integer cssSamcustomerId = 0;// for ill ,it should be increment
				if (siteRole.equalsIgnoreCase("SPOKE")) {
					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
							serviceDetails.getServiceSubtype());
					mstVpnSamManagerId = mstVpnmanagerIdRepository
							.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
									"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(),
									siteRole);
					if (mstVpnSamManagerId == null) {

						saveMstVpnSamManagerId(aluSammgrSeq, serviceDetails, vpnMetatData,
								getTopologyName(serviceDetails), siteRole);
					}
					if (orderDetail.getCustomerCrnId() != null) {
						MstGvpnAluCustId gvpnAluCustId = mstGvpnAluCustIdRepository
								.findByCrnId(orderDetail.getCustomerCrnId());
						if (gvpnAluCustId != null) {
							orderDetail.setAluCustomerId(gvpnAluCustId.getAluCustId());
						} else {
							orderDetail.setAluCustomerId(String.valueOf(aluSammgrSeq.getSammgrSeqid()));
						}
					}
				} else {
					if (siteRole.toLowerCase().contains("hub")) {
						mstVpnSamManagerId = mstVpnmanagerIdRepository.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRole(
								vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails), siteRole);
					} else if (siteRole.toLowerCase().contains("mesh")) {
						mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(vpnMetatData.getVpnName(),
										"CUSTOMER", getTopologyName(serviceDetails), siteRole);
					} else {
						mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(
										vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails),
										serviceDetails.getServiceId(), siteRole);
					}

					if (Objects.nonNull(parentServiceDetail.getCssSammgrId()) && Objects.nonNull(mstVpnSamManagerId)) {
						if (parentServiceDetail.getCssSammgrId() != Integer.valueOf(mstVpnSamManagerId.getSamMgrId())) {
							logger.info("Copying SamMgr even mstVpn exists");
							cssSamcustomerId = parentServiceDetail.getCssSammgrId();
						} else {
							logger.info("Set mstVpn exists");
							cssSamcustomerId = Integer.valueOf(mstVpnSamManagerId.getSamMgrId());
						}
					} else if (Objects.isNull(mstVpnSamManagerId)) {
						AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
								serviceDetails.getServiceSubtype());

						saveMstVpnSamManagerId(aluSammgrSeq, serviceDetails, vpnMetatData,
								getTopologyName(serviceDetails), siteRole);

						if (Objects.nonNull(aluSammgrSeq)) {
							cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
						}
					}
					if (orderDetail.getCustomerCrnId() != null) {
						MstGvpnAluCustId gvpnAluCustId = mstGvpnAluCustIdRepository
								.findByCrnId(orderDetail.getCustomerCrnId());
						if (gvpnAluCustId != null) {
							orderDetail.setAluCustomerId(gvpnAluCustId.getAluCustId());
						} else {
							AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
									serviceDetails.getServiceSubtype());
							if (aluSammgrSeq != null) {
								cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
							}
							orderDetail.setAluCustomerId(String.valueOf(cssSamcustomerId));
							MstGvpnAluCustId mstGvpnAluCustIdNew = new MstGvpnAluCustId();
							mstGvpnAluCustIdNew.setAluCustId(String.valueOf(cssSamcustomerId));
							mstGvpnAluCustIdNew.setCustomerName(orderDetail.getCustomerName());
							mstGvpnAluCustIdNew.setCrnId(orderDetail.getCustomerCrnId());
							mstGvpnAluCustIdRepository.save(mstGvpnAluCustIdNew);
						}
					}

				}
				serviceDetails.setAluSvcid(null);

			}
		}
		if (orderDetail.getCustomerCategory() == null) {
			orderDetail.setCustomerCategory("Enterprise");
		}
		orderDetailRepository.save(orderDetail);

	}

	private void applyVpnMetataAddSiteRule(VpnMetatData currentVpnMetatData, ServiceDetail serviceDetails,
			ServiceDetail parentServiceDetail) {
		logger.info("applyVpnMetataAddSiteRule::{}", serviceDetails.getServiceId());
		if (Objects.nonNull(parentServiceDetail.getVpnMetatDatas())
				&& !parentServiceDetail.getVpnMetatDatas().isEmpty()) {
			logger.info("Previous VpnMetatData exists");
			Optional<VpnMetatData> vpnMetaDataOptional = parentServiceDetail.getVpnMetatDatas().stream().findFirst();
			if (vpnMetaDataOptional.isPresent()) {
				VpnMetatData prevVpnMetatData = vpnMetaDataOptional.get();
				if (Objects.nonNull(prevVpnMetatData.getVpnName()) && (!prevVpnMetatData.getVpnName().contains("_M")
						|| !prevVpnMetatData.getVpnName().contains("_HS")
						|| !prevVpnMetatData.getVpnName().contains("_PM"))) {
					logger.info("ADD SITE Order placed above of Very old order");
					currentVpnMetatData.setVpnName(prevVpnMetatData.getVpnName());
					currentVpnMetatData.setVpnAlias(prevVpnMetatData.getVpnName());
					currentVpnMetatData.setSolutionId(prevVpnMetatData.getVpnName());
					currentVpnMetatData.setVpnSolutionName(prevVpnMetatData.getVpnName());
					vpnMetatDataRepository.save(currentVpnMetatData);
					if (Objects.nonNull(currentVpnMetatData.getVpnName()) && !currentVpnMetatData.getVpnName().isEmpty()
							&& currentVpnMetatData.getVpnName().equalsIgnoreCase("STATE_BANK")) {
						logger.info("Set managementVpnType1 as NULL for STATE_BANK");
						currentVpnMetatData.setManagementVpnType1(null);
					}
					vpnMetatDataRepository.saveAndFlush(currentVpnMetatData);
					serviceDetails.setSolutionId(prevVpnMetatData.getVpnName());
					serviceDetailRepository.save(serviceDetails);
				}
			}
		}

	}

	private void applyCESideRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream()
					.filter(ser -> (ser.getIscpeWanInterface() != null && ser.getIscpeWanInterface() == 1
							&& ser.getEndDate() == null)
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

			List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetails.getInterfaceProtocolMappings().stream()
					.filter(serIn -> serIn.getCpe() != null && serIn.getIscpeWanInterface() != null
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
						appyEthernetRule(interfaceProtocolMapping.getEthernetInterface(), map, serviceDetails, "CE");
					}

					else if (interfaceProtocolMapping.getChannelizedSdhInterface() != null) {
						applyChannelizedSdhInterfaceRule(interfaceProtocolMapping.getChannelizedSdhInterface(), map,
								serviceDetails, "CE");
					}

					else if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null) {
						applyChannelizedE1serialInterface(interfaceProtocolMapping.getChannelizedE1serialInterface(),
								map, serviceDetails, "CE");
					}

					applyRoutingProtocal(interfaceProtocolMapping, map, serviceDetails);

				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyInterfaceRuleForCESide => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void applyRFRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields) {
		LmComponent lmComponent = serviceDetails.getLmComponents().stream().filter(lm -> lm.getEndDate() == null)
				.findFirst().orElse(null);

		if (lmComponent != null) {
			applyCambiumRule(serviceDetails, manDatoryFields, lmComponent);

			applyRadwinRule(lmComponent, manDatoryFields, serviceDetails);
		}

	}

	private void applyVPnSolution(ServiceDetail serviceDetails, Map<String, String> manDatoryFields,
			VpnMetatData vpnMetatData) {

		if (serviceDetails.getVpnSolutions() != null && !serviceDetails.getVpnSolutions().isEmpty()) {
			serviceDetails.getVpnSolutions().forEach(vpnSolution -> {
				vpnSolution.setEndDate(new Timestamp(System.currentTimeMillis()));
				vpnSolutionRepository.save(vpnSolution);
			});
		}

		String custiomerName = serviceDetails.getOrderDetail().getCustomerName();// 6,6,4 word only space to be

		createCustomerSolutionRecord(vpnMetatData, serviceDetails, custiomerName);

		if (vpnMetatData.getManagementVpnType1() != null
				&& vpnMetatData.getManagementVpnType1().equalsIgnoreCase("GREY")) {// it has 3 attri

			createGreySolutionRecord(vpnMetatData, serviceDetails, custiomerName);

		}

		if (vpnMetatData.getManagementVpnType2() != null
				&& vpnMetatData.getManagementVpnType2().equalsIgnoreCase("RADWIN")) {
			createRadwinSolutionRecord(vpnMetatData, serviceDetails, custiomerName);

		}

	}

	private void applyVrf(ServiceDetail serviceDetails, Map<String, String> manDatoryFields) throws TclCommonException {
		logger.info("Inside applyVrf rule method : {}", serviceDetails.getId());
		try {
			Vrf vrf = new Vrf();
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
					.findById(serviceDetails.getScServiceDetailId());
			if (scServiceDetail.isPresent() && scServiceDetail.get().getMultiVrfSolution() != null
					&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.get().getMultiVrfSolution())) {
				List<String> vrfAttributes = Arrays.asList(CommonConstants.FLEXICOS, CommonConstants.MASTER_VRF_FLAG,
						CommonConstants.MULTI_VRF_SOLUTION, CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS,
						CommonConstants.NUMBER_OF_VRFS, CommonConstants.SLAVE_VRF_SERVICE_ID,
						CommonConstants.MASTER_VRF_SERVICE_ID, CommonConstants.CUSTOMER_PROJECT_NAME,
						CommonConstants.VRF_BASED_BILLING);
				List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeNameIn(scServiceDetail.get().getId(), vrfAttributes);
				logger.info("Inside vrf attribute list for entry of vrf table: {}", scServiceAttributes.size());
				vrf.setIsmultivrf((byte) 1);
				vrf.setMastervrfServiceid(String.valueOf(scServiceDetail.get().getMasterVrfServiceId()));
				if (scServiceAttributes != null
						&& scServiceAttributes.stream()
								.filter(vrfAttribute -> vrfAttribute.getAttributeName()
										.equalsIgnoreCase(CommonConstants.CUSTOMER_PROJECT_NAME))
								.findFirst().isPresent()) {
					vrf.setVrfProjectName(scServiceAttributes.stream()
							.filter(vrfAttribute -> vrfAttribute.getAttributeName()
									.equalsIgnoreCase(CommonConstants.CUSTOMER_PROJECT_NAME))
							.findFirst().get().getAttributeValue());
				}

				if (scServiceAttributes != null
						&& scServiceAttributes.stream()
								.filter(vrfAttribute -> vrfAttribute.getAttributeName()
										.equalsIgnoreCase(CommonConstants.SLAVE_VRF_SERVICE_ID))
								.findFirst().isPresent()) {
					vrf.setSlaveVrfServiceId(scServiceAttributes.stream()
							.filter(vrfAttribute -> vrfAttribute.getAttributeName()
									.equalsIgnoreCase(CommonConstants.SLAVE_VRF_SERVICE_ID))
							.findFirst().get().getAttributeValue());
				}
				vrf.setVrfName(vrf.getVrfProjectName());
				vrf.setIsvrfLiteEnabled((byte) 0);
				vrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				vrf.setModifiedBy(MODIFIEDBY);
				vrf.setServiceDetail(serviceDetails);
				logger.info("All vrf values set : {}", serviceDetails.getServiceId());
			} else {

				vrf.setVrfName("NOT_APPLICABLE");
				vrf.setIsvrfLiteEnabled((byte) 0);
				vrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				vrf.setModifiedBy(MODIFIEDBY);
				vrf.setServiceDetail(serviceDetails);
			}
			vrfRepository.save(vrf);
			manDatoryFields.put(AceConstants.VRF.VRF_NAME, vrf.getVrfName());

			serviceDetails.addVrf(vrf);
		} catch (Exception e) {
			logger.error("Exception in applyVrf => {}", e);

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void createPoliciesForVrf(Vrf vrf, ServiceDetail serviceDetail, String routerMake)
			throws TclCommonException {
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
				&& !vrf.getVrfName().equalsIgnoreCase("NOT_APPLICABLE") && !vrf.getVrfName().equalsIgnoreCase("NA")) {
			PolicyType policyType1 = new PolicyType();
			policyType1.setVrf(vrf);
			policyType1.setIsvprnImportpolicy((byte) 1);
			policyType1.setIsvprnImportPreprovisioned((byte) 1);
			policyType1.setIsvprnImportpolicyName("S-VPRN-IMPORT_" + serviceDetail.getCssSammgrId());
			policyTypeRepository.save(policyType1);
			PolicyType policyType2 = new PolicyType();
			policyType2.setVrf(vrf);
			policyType2.setIsvprnExportpolicy((byte) 1);
			policyType2.setIsvprnExportPreprovisioned((byte) 1);
			policyType2.setIsvprnExportpolicyName("S-VPRN-EXPORT_" + serviceDetail.getCssSammgrId());
			policyTypeRepository.save(policyType2);
		}
	}

	private void applyOspfRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());

			if (!interfaceProtocolMappings.isEmpty()) {
				interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);

				if (interfaceProtocolMapping != null && interfaceProtocolMapping.getOspf() != null) {
					String ippath = manDatoryFields.get(AceConstants.IPADDRESS.IPPATH);

					Ospf ospf = interfaceProtocolMapping.getOspf();
					if (ospf.getIsauthenticationRequired() == null || ospf.getIsauthenticationRequired() == 0) {
						ospf.setAuthenticationMode(null);
						ospf.setPassword(null);

					}
					ospf.setIsnetworkP2p((byte) 1);
					ospf.setIsignoreipOspfMtu((byte) 1);
					ospf.setVersion("v2");
					ospf.setIsauthenticationRequired((byte) 0);
					String routerMake = manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE);
					String reduncy = manDatoryFields.get(AceConstants.SERVICE.REDUNDACY_ROLE);
					String ipPath = manDatoryFields.get(AceConstants.SERVICE.IPPATH);

					if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
						ospf.setDomainId(heaxDecimalConversion(ospf.getProcessId()));// hexa conversion of process id
					}

					if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
						ospf.setIsredistributeConnectedEnabled((byte) 1);
						ospf.setIsredistributeStaticEnabled((byte) 0);
						ospf.setIsroutemapEnabled((byte) 0);

						if (reduncy.equalsIgnoreCase("PRIMARY")
								&& (ipPath.equalsIgnoreCase("DUALSTACK") || ipPath.equalsIgnoreCase("v4"))) {
							ospf.setLocalPreference("200");

						}

						if (reduncy.equalsIgnoreCase("PRIMARY")
								&& (ipPath.equalsIgnoreCase("DUALSTACK") || ipPath.equalsIgnoreCase("v6"))) {
							ospf.setLocalPreferenceV6("200");

						}

					}

					ospfRepository.save(ospf);

					if ((ippath.equalsIgnoreCase("v4") || ippath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL))) {

						createInboundPolicyV4(ospf, manDatoryFields, serviceDetails);

					}
					if ((ippath.equalsIgnoreCase("v6") || ippath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL))) {

						createInboundV6(ospf, manDatoryFields, serviceDetails);

					}

				}
			}

		} catch (Exception e) {
			logger.error("Exception in applyOspfRule => {}", e);

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void createInboundPolicyV4(Ospf ospf, Map<String, String> manDatoryFields, ServiceDetail serviceDetails)
			throws TclCommonException {
		try {
			logger.info("createInboundPolicyV4  started {}");
			PolicyType policyType = new PolicyType();
			policyType.setOspf(ospf);
			policyType.setOutboundRoutingIpv4Policy((byte) 1);
			policyType.setOutboundIpv4PolicyName("PE-CE_EXPORT");
			policyType.setOutboundIpv4Preprovisioned((byte) 1);
			policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType);
		} catch (Exception e) {
			logger.error("Exception in createInboundPolicyV4 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void createInboundV6(Ospf ospf, Map<String, String> manDatoryFields, ServiceDetail serviceDetails)
			throws TclCommonException {
		try {
			logger.info("createInboundPolicyV4  started {}");
			PolicyType policyType = new PolicyType();

			policyType.setOspf(ospf);
			policyType.setOutboundRoutingIpv4Policy((byte) 1);
			policyType.setOutboundIpv6PolicyName("PE-CE_EXPORT");
			policyType.setOutboundIpv6Preprovisioned((byte) 1);

			policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType);
		} catch (Exception e) {
			logger.error("Exception in createInboundV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void applyRoterDetails(ServiceDetail serviceDetails, Map<String, String> manDatoryFields)
			throws TclCommonException {

		try {
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
			logger.error("Exception in applyOspfRule => {}", e);

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void applyInterfaceRule(ServiceDetail serviceDetails, Map<String, String> map) throws TclCommonException {
		applyIpAddressRule(serviceDetails, map);

		List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
				.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null).collect(Collectors.toList());// based
																														// on
																														// version
		if (!interfaceProtocolMappings.isEmpty()) {
			interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);

			if (interfaceProtocolMapping != null) {

				if (interfaceProtocolMapping.getEthernetInterface() != null) {
					appyEthernetRule(interfaceProtocolMapping.getEthernetInterface(), map, serviceDetails, "PE");
				}

				if (interfaceProtocolMapping.getChannelizedSdhInterface() != null) {
					applyChannelizedSdhInterfaceRule(interfaceProtocolMapping.getChannelizedSdhInterface(), map,
							serviceDetails, "PE");
				}

				if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null) {
					applyChannelizedE1serialInterface(interfaceProtocolMapping.getChannelizedE1serialInterface(), map,
							serviceDetails, "PE");
				}

				applyRoutingProtocal(interfaceProtocolMapping, map, serviceDetails);

			}
		}
	}

	private void applyIpAddressRule(ServiceDetail serviceDetails, Map<String, String> map) {

		IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

		VpnSolution vpnSolution = serviceDetails.getVpnSolutions().stream().filter(vpn -> vpn.getEndDate() == null)
				.findFirst().orElse(null);

		VpnMetatData vpnMetatData = serviceDetails.getVpnMetatDatas().stream().findFirst().orElse(null);

		if (ipAddressDetail != null) {

			if (vpnMetatData != null && vpnMetatData.getManagementVpnType1() != null
					&& vpnMetatData.getManagementVpnType1().equalsIgnoreCase("NAT")) {
				ipAddressDetail.setPingAddress1("121.244.227.114");
				// ipAddressDetail.setNmsServiceIpv4Address("121.244.227.113");
			}

			else if (serviceDetails.getScopeOfMgmt() != null
					&& (serviceDetails.getScopeOfMgmt().equalsIgnoreCase("Fully Managed")
							|| serviceDetails.getScopeOfMgmt().equalsIgnoreCase("proactive Monitoring")
							|| serviceDetails.getScopeOfMgmt().toLowerCase().contains("config")
							|| serviceDetails.getScopeOfMgmt().contains("proactive"))) {
				ipAddressDetail.setPingAddress1("10.70.0.202");
				// ipAddressDetail.setNmsServiceIpv4Address("10.70.0.201");

			}

			if (map.get("EXTENDEDLANENABLED").equalsIgnoreCase("true")) {
				ipAddressDetail.setNoMacAddress(findNoMac(ipAddressDetail));

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

			}

			ipAddressDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			ipAddressDetail.setModifiedBy(MODIFIEDBY);

			ipAddressDetailRepository.save(ipAddressDetail);
		}

	}

	private void appyEthernetRule(EthernetInterface ethernetInterface, Map<String, String> map,
			ServiceDetail serviceDetails, String type) throws TclCommonException {
		logger.info("appyEthernetRule invoked");
		ethernetInterface.setSpeed(AceConstants.DEFAULT.NOT_APPLICABLE);
		ethernetInterface.setDuplex(AceConstants.DEFAULT.NOT_APPLICABLE);
		ethernetInterface.setAutonegotiationEnabled(AceConstants.DEFAULT.NOT_APPLICABLE);
		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerMake)
				|| AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			ethernetInterface.setQosLoopinPassthrough(null);
		}

		if (map.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED)
				&& map.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("TRUE")) {

			int count = 1;

			if (type != null && type.equalsIgnoreCase("CE")) {
				count = 2;
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
			logger.info("ExtendedLan :: FALSE");
			if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
				logger.info("WAN V4 Address exists");
				ethernetInterface.setModifiedIpv4Address(ethernetInterface.getIpv4Address() + "/"
						+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS)));

				if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)) {
					logger.info("Sy Wan from IpWanV4");
					if (Objects.nonNull(ethernetInterface.getSecondaryIpv4Address())
							&& !ethernetInterface.getSecondaryIpv4Address().isEmpty()) {
						ethernetInterface.setModifiedSecondaryIpv4Address(ethernetInterface.getSecondaryIpv4Address()
								+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)));
					} else {
						logger.info("Apply Sy Wan from IpWanV4");
						if (Objects.nonNull(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY))
								&& !map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY).isEmpty()) {
							logger.info("Sy Wan Exists");
							if (type != null && type.equalsIgnoreCase("CE")) {
								logger.info("CE Sy Wan from IpWanV4");
								ethernetInterface.setSecondaryIpv4Address(
										getIpAddressSplit(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY), 2));
								ethernetInterface.setModifiedSecondaryIpv4Address(
										getIpAddressSplit(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY), 2)
												+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)));
							} else if (type != null && type.equalsIgnoreCase("PE")) {
								logger.info("PE Sy Wan from IpWanV4");
								ethernetInterface.setSecondaryIpv4Address(
										getIpAddressSplit(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY), 1));
								ethernetInterface.setModifiedSecondaryIpv4Address(
										getIpAddressSplit(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY), 1)
												+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)));
							}
						}

					}

				}
			}

			if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
				ethernetInterface.setModifiedIpv6Address(ethernetInterface.getIpv6Address() + "/"
						+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS)));
				if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)) {
					ethernetInterface.setModifiedSecondaryIpv6Address(ethernetInterface.getSecondaryIpv6Address() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)));
				}
			}
		}

		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			if (ethernetInterface.getPhysicalPort() != null) {
				if (AceConstants.INTERFACE.XGIGABIT_ETHERNET
						.contains(ethernetInterface.getPhysicalPort().toUpperCase()))
					ethernetInterface.setFraming(AceConstants.INTERFACE.LAN_PHY);
			} else {
				ethernetInterface.setFraming(AceConstants.DEFAULT.NOT_APPLICABLE);
			}
		}

		if (ethernetInterface.getMode() == null) {

			ethernetInterface.setEncapsulation("NULL");

		} else if (ethernetInterface.getMode() != null && ethernetInterface.getMode().equalsIgnoreCase("ACCESS")) {
			ethernetInterface.setEncapsulation("NULL");

		}

		else if (ethernetInterface.getMode() != null
				&& ethernetInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.TRUNK)) {
			ethernetInterface.setEncapsulation(AceConstants.INTERFACE.DOT1Q);

		} else if (ethernetInterface.getMode() != null
				&& ethernetInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.Q_IN_Q)) {
			ethernetInterface.setEncapsulation(AceConstants.INTERFACE.QNQ);

		}

		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU) && ethernetInterface.getEncapsulation() != null
				&& ethernetInterface.getEncapsulation().equalsIgnoreCase(AceConstants.INTERFACE.DOT1Q)) {
			ethernetInterface.setOuterVlan(ethernetInterface.getInnerVlan());
			if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
				ethernetInterface.setInnerVlan(null);
			}
		}

		if (ethernetInterface.getMode() == null) {
			ethernetInterface.setMode(AceConstants.INTERFACE.MODE);

		}
		if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

			ethernetInterface.setMode(AceConstants.INTERFACE.MODE);
		} else if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			ethernetInterface.setMode(null);

		}

		ethernetInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		ethernetInterface.setModifiedBy(MODIFIEDBY);
		if (ethernetInterface.getIsbfdEnabled() != null && ethernetInterface.getIsbfdEnabled() == 0) {
			ethernetInterface.setBfdtransmitInterval(null);
			ethernetInterface.setBfdreceiveInterval(null);
			ethernetInterface.setBfdMultiplier(null);
		}
		if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
			logger.info("Izopc Ethernet specific details::{}", serviceDetails.getServiceId());
			ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
							"Cloud Provider");
			if ("PE".equalsIgnoreCase(type) && (cloudProviderAttribute != null
					&& ("Google Public Cloud".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
							|| "Google Cloud Interconnect-Private"
									.equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())))) {
				logger.info("Izopc Ethernet Mtu::Google CSP for Service Id::{}", serviceDetails.getServiceId());
				ethernetInterface.setMtu("1440");
			}
			if ("PE".equalsIgnoreCase(type) && cloudProviderAttribute != null
					&& "AWS Direct Connect".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
					&& ethernetInterface.getMode() != null
					&& ethernetInterface.getMode().equalsIgnoreCase(AceConstants.INTERFACE.Q_IN_Q)) {
				logger.info("Izopc Ethernet InnerVlan::Aws CSP for Service Id::{}", serviceDetails.getServiceId());
					ethernetInterface.setInnerVlan(ethernetInterface.getOuterVlan());
					ethernetInterface.setOuterVlan(ethernetInterface.getInnerVlan());
			}
			if(ethernetInterface.getInnerVlan()!=null && ethernetInterface.getInnerVlan().contains(".")) {
				logger.info("Izopc Ethernet InnerVlan contains DOT Service Id::{}", serviceDetails.getServiceId());
				ethernetInterface=getVlanDetails(ethernetInterface.getInnerVlan(),ethernetInterface);
			}else if(ethernetInterface.getOuterVlan()!=null && ethernetInterface.getOuterVlan().contains(".")) {
				logger.info("Izopc Ethernet OuterVlan contains DOT Service Id::{}", serviceDetails.getServiceId());
				ethernetInterface=getVlanDetails(ethernetInterface.getOuterVlan(),ethernetInterface);
			}
		}
		ethernetInterfaceRepository.save(ethernetInterface);
		// saveAclPolicyCriteriaForEthernet(serviceDetails,
		// map.get(AceConstants.IPADDRESS.IPPATH), ethernetInterface);
		map.put(AceConstants.INTERFACE.ETHRNER_INNERV_LAN, ethernetInterface.getInnerVlan());
		map.put(AceConstants.INTERFACE.ETHRNER_OUTERV_LAN, ethernetInterface.getOuterVlan());

	}

	private EthernetInterface getVlanDetails(String vlan,EthernetInterface ethernetInterface) {
		String innerVlanSplit[]=vlan.split("\\.");
		if(innerVlanSplit.length==2) {
			logger.info("Izopc vlan exists");
			ethernetInterface.setOuterVlan(innerVlanSplit[0]);
			ethernetInterface.setInnerVlan(innerVlanSplit[1]);
			ethernetInterface.setEncapsulation(AceConstants.INTERFACE.QNQ);
		}
		return ethernetInterface;
	}

	public String getIpAddressSplit(String ipAddress, int count) {
		String array[] = ipAddress.split("/");
		String ssMgmtIp = array[0];
		String[] splitValue = ssMgmtIp.split("\\.");
		splitValue[splitValue.length - 1] = String.valueOf(Integer.parseInt(splitValue[splitValue.length - 1]) + count);
		String output = Arrays.asList(splitValue).stream().map(eachVal -> eachVal.toString())
				.collect(Collectors.joining("."));
		return output;
	}

	private String addCountIpv6Address(String ipAddress, Integer count) {
		IPv6Address current = IPv6Address.fromString(ipAddress);
		IPv6Address result = null;
		result = current.add(count);
		String resultIp = null;
		if (result != null) {
			resultIp = result.toString();
		}
		return resultIp;
	}

	private void applyChannelizedE1serialInterface(ChannelizedE1serialInterface channelizedE1serialInterface,
			Map<String, String> map, ServiceDetail serviceDetails, String type) throws TclCommonException {

		if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
			channelizedE1serialInterface.setModifiedIpv4Address(channelizedE1serialInterface.getIpv4Address() + "/"
					+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS)));
		}
		if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)) {
			channelizedE1serialInterface
					.setModifiedSecondaryIpv4Address(channelizedE1serialInterface.getSecondaryIpv4Address() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)));
		}

		if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
			channelizedE1serialInterface.setModifiedIpv6Address(channelizedE1serialInterface.getIpv6Address() + "/"
					+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS)));
		}
		if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)) {
			channelizedE1serialInterface
					.setModifiedSecondaryIpv6Address(channelizedE1serialInterface.getSecondaryIpv6Address() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)));
		}

		if (channelizedE1serialInterface.getFirsttimeSlot() != null
				&& channelizedE1serialInterface.getLasttimeSlot() != null) {
			channelizedE1serialInterface.setIsframed((byte) 1);
			// channelizedE1serialInterface.setIsbfdEnabled((byte) 1);

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
		if (channelizedE1serialInterface.getIsbfdEnabled() != null
				&& channelizedE1serialInterface.getIsbfdEnabled() == 0) {
			channelizedE1serialInterface.setBfdtransmitInterval(null);
			channelizedE1serialInterface.setBfdreceiveInterval(null);
			channelizedE1serialInterface.setBfdMultiplier(null);
		}

		channelizedE1serialInterface.setIscrcforenabled((byte) 0);
		channelizedE1serialInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		channelizedE1serialInterface.setModifiedBy(MODIFIEDBY);
		channelizedE1serialInterfaceRepository.save(channelizedE1serialInterface);
		/*
		 * saveAclPolicyCriteriaForChannelizedE1serialInterface(serviceDetails,
		 * map.get(AceConstants.IPADDRESS.IPPATH), channelizedE1serialInterface);
		 */

	}

	private void applyChannelizedSdhInterfaceRule(ChannelizedSdhInterface channelizedSdhInterface,
			Map<String, String> map, ServiceDetail serviceDetails, String type) throws TclCommonException {
		channelizedSdhInterface.setIsbfdEnabled((byte) 0);

		if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
			String subnet = subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS));
			channelizedSdhInterface.setModifiedIpv4Address(channelizedSdhInterface.getIpv4Address() + "/" + subnet);

			if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
				String secSubnet = subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY));
				channelizedSdhInterface.setModifiedSecondaryIpv4Address(
						channelizedSdhInterface.getSecondaryIpv4Address() + "/" + secSubnet);
			}

		}
		if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
			String subnet = subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS));
			channelizedSdhInterface.setModifiedIipv6Address(channelizedSdhInterface.getIpv6Address() + "/" + subnet);

			if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)) {
				String seconSubnet = subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY));
				channelizedSdhInterface.setModifiedSecondaryIpv6Address(
						channelizedSdhInterface.getSecondaryIpv6Address() + "/" + seconSubnet);
			}
		}

		if (channelizedSdhInterface.get_4Kfirsttime_slot() != null
				&& channelizedSdhInterface.get_4klasttimeSlot() != null) {
			channelizedSdhInterface.setIsframed((byte) 1);
			// channelizedSdhInterface.setIsbfdEnabled((byte) 1);
		}
		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		if (routerName.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
			channelizedSdhInterface.setEncapsulation(AceConstants.INTERFACE.HDLC);

		}
		if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
				|| routerName.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			channelizedSdhInterface.setEncapsulation(AceConstants.INTERFACE.PPP);

		}
		if (channelizedSdhInterface.getIsbfdEnabled() != null && channelizedSdhInterface.getIsbfdEnabled() == 0) {
			channelizedSdhInterface.setBfdtransmitInterval(null);
			channelizedSdhInterface.setBfdreceiveInterval(null);
			channelizedSdhInterface.setBfdMultiplier(null);
		}
		channelizedSdhInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		channelizedSdhInterface.setModifiedBy(MODIFIEDBY);
		channelizedSdhInterfaceRepository.save(channelizedSdhInterface);
		/*
		 * saveAclPolicyCriteriaForChannelizedSdhInterface(serviceDetails,
		 * map.get(AceConstants.IPADDRESS.IPPATH), channelizedSdhInterface);
		 */

	}

	private Set<AclPolicyCriteria> saveAclPolicyCriteriaForChannelizedSdhInterface(ServiceDetail serviceDetail,
			String type, ChannelizedSdhInterface channelizedSdhInterface) throws TclCommonException {
		logger.info("Service Activation - saveAclPolicyCriteria - started");
		Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();
		try {
			if (type.equalsIgnoreCase("v4") || type.equals("DUALSTACK")) {
				softDeleteForChannelizedSdhInterface(channelizedSdhInterface, serviceDetail);
				AclPolicyCriteria acp = new AclPolicyCriteria();
				acp.setInboundIpv4AclName(
						"ACL_XXXSERVICE_CODEXXX_WAN_IN".replaceAll("XXXSERVICE_CODEXXX", serviceDetail.getServiceId()));
				acp.setIsinboundaclIpv4Preprovisioned((byte) 0);
				acp.setIsinboundaclIpv4Applied((byte) 1);
				acp.setChannelizedSdhInterface(channelizedSdhInterface);
				acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				acp.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(acp);
				aclPolicyCriterias.add(acp);
			}

			if (type.equalsIgnoreCase("v6") || type.equals("DUALSTACK")) {

				AclPolicyCriteria acpOutbound = new AclPolicyCriteria();

				acpOutbound.setOutboundIpv4AclName("ACL_XXXSERVICE_CODEXXX_WAN_OUT".replaceAll("XXXSERVICE_CODEXXX",
						serviceDetail.getServiceId()));
				acpOutbound.setIsoutboundaclIpv6Preprovisioned((byte) 1);
				acpOutbound.setIsoutboundaclIpv6Applied((byte) 1);
				acpOutbound.setChannelizedSdhInterface(channelizedSdhInterface);
				acpOutbound.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				acpOutbound.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(acpOutbound);
				aclPolicyCriterias.add(acpOutbound);

			}

			logger.info("Service Activation - saveAclPolicyCriteria - completed");
			return aclPolicyCriterias;
		} catch (Exception e) {
			logger.error("Exception in saveAclPolicyCriteria => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private Set<AclPolicyCriteria> saveAclPolicyCriteriaForChannelizedE1serialInterface(ServiceDetail serviceDetail,
			String type, ChannelizedE1serialInterface channelizedE1serialInterface) throws TclCommonException {
		logger.info("Service Activation - saveAclPolicyCriteria - started");
		Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();
		try {
			if (type.equalsIgnoreCase("v4") || type.equals("DUALSTACK")) {
				softDeleteChannelizedE1serialInterface(channelizedE1serialInterface, serviceDetail);
				AclPolicyCriteria acp = new AclPolicyCriteria();
				acp.setInboundIpv4AclName(
						"ACL_XXXSERVICE_CODEXXX_WAN_IN".replaceAll("XXXSERVICE_CODEXXX", serviceDetail.getServiceId()));
				acp.setIsinboundaclIpv4Preprovisioned((byte) 0);
				acp.setIsinboundaclIpv4Applied((byte) 1);
				acp.setChannelizedE1serialInterface(channelizedE1serialInterface);
				acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				acp.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(acp);
				aclPolicyCriterias.add(acp);
			}
			if (type.equalsIgnoreCase("v6") || type.equals("DUALSTACK")) {
				AclPolicyCriteria acpOutbound = new AclPolicyCriteria();

				acpOutbound.setOutboundIpv4AclName("ACL_XXXSERVICE_CODEXXX_WAN_OUT".replaceAll("XXXSERVICE_CODEXXX",
						serviceDetail.getServiceId()));
				acpOutbound.setIsoutboundaclIpv6Preprovisioned((byte) 1);
				acpOutbound.setIsoutboundaclIpv6Applied((byte) 1);
				acpOutbound.setChannelizedE1serialInterface(channelizedE1serialInterface);
				acpOutbound.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				acpOutbound.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(acpOutbound);
				aclPolicyCriterias.add(acpOutbound);

			}

			logger.info("Service Activation - saveAclPolicyCriteria - completed");
			return aclPolicyCriterias;
		} catch (Exception e) {
			logger.error("Exception in saveAclPolicyCriteria => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public Set<AclPolicyCriteria> saveAclPolicyCriteriaForEthernet(ServiceDetail serviceDetail, String type,
			EthernetInterface ethernetInterface) throws TclCommonException {
		logger.info("Service Activation - saveAclPolicyCriteria - started");
		Set<AclPolicyCriteria> aclPolicyCriterias = new HashSet<>();
		try {
			if (type.equalsIgnoreCase("v4") || type.equals("DUALSTACK")) {
				softDeleteEthernetPolicy(ethernetInterface, serviceDetail);
				AclPolicyCriteria acp = new AclPolicyCriteria();
				acp.setInboundIpv4AclName(
						"ACL_XXXSERVICE_CODEXXX_WAN_IN".replaceAll("XXXSERVICE_CODEXXX", serviceDetail.getServiceId()));
				acp.setIsinboundaclIpv4Preprovisioned((byte) 0);
				acp.setIsinboundaclIpv4Applied((byte) 1);
				acp.setEthernetInterface(ethernetInterface);
				acp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				acp.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(acp);
				aclPolicyCriterias.add(acp);
			}

			if (type.equalsIgnoreCase("v6") || type.equals("DUALSTACK")) {

				AclPolicyCriteria acpOutbound = new AclPolicyCriteria();

				acpOutbound.setOutboundIpv4AclName("ACL_XXXSERVICE_CODEXXX_WAN_OUT".replaceAll("XXXSERVICE_CODEXXX",
						serviceDetail.getServiceId()));
				acpOutbound.setIsoutboundaclIpv6Preprovisioned((byte) 1);
				acpOutbound.setIsoutboundaclIpv6Applied((byte) 1);
				acpOutbound.setEthernetInterface(ethernetInterface);
				acpOutbound.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				acpOutbound.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(acpOutbound);
				aclPolicyCriterias.add(acpOutbound);
			}

			logger.info("Service Activation - saveAclPolicyCriteria - completed");
			return aclPolicyCriterias;
		} catch (Exception e) {
			logger.error("Exception in saveAclPolicyCriteria => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void applyServiceQos(ServiceDetail serviceDetails, Map<String, String> mandatoryFields) {

		ServiceQo serviceQo = serviceDetails.getServiceQos().stream().filter(serQo -> serQo.getEndDate() == null)
				.findFirst().orElse(null);
		if (serviceQo != null) {

			serviceQo.setCosUpdateAction("COMPLETE");

			if (serviceQo.getCosProfile() == null) {
				serviceQo.setCosProfile(AceConstants.COSPROFILE.PREMIUM);
			}
			if (serviceQo.getQosTrafiicMode() == null) {
				serviceQo.setQosTrafiicMode(AceConstants.COSPROFILE.UNICAST);
			}
			serviceQo.setIsdefaultFc((byte) 1);
			Float bandWidth = maxBanwidth(
					banwidthConversion(serviceDetails.getServiceBandwidthUnit(), serviceDetails.getServiceBandwidth()),
					banwidthConversion(serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));

			if (bandWidth != null && bandWidth > 0) {
				serviceQo.setPirBw(String.valueOf(bandWidth));
				serviceQo.setPirBwUnit(AceConstants.BANDWIDTHUNIT.KBPS);
			}

			serviceQo.setIsflexicos((byte) 0);

			if (serviceQo.getIsflexicos() == 1 && serviceDetails.getOrderDetail().getCustomerName() != null
					&& !serviceDetails.getOrderDetail().getCustomerName().isEmpty()
					&& serviceDetails.getTopologies() != null && !serviceDetails.getTopologies().isEmpty()) {
				logger.info("Flexi Cos Policy Scheduler");
				Topology topology = serviceDetails.getTopologies().stream().findFirst().orElse(null);
				if (topology != null && topology.getUniswitchDetails() != null
						&& !topology.getUniswitchDetails().isEmpty()) {
					logger.info("Topology UniSwitch exists");
					UniswitchDetail uniswitchDetail = topology.getUniswitchDetails().stream().findFirst().orElse(null);
					if (uniswitchDetail != null && uniswitchDetail.getUniquePortId() != null
							&& uniswitchDetail.getPhysicalPort() != null && !uniswitchDetail.getUniquePortId().isEmpty()
							&& !uniswitchDetail.getPhysicalPort().isEmpty()) {
						logger.info("Uniswitch Flexi Cos Identifier");
						serviceQo.setFlexiCosIdentifier("FCos_"
								+ serviceDetails.getOrderDetail().getCustomerName().replaceAll("[^a-zA-Z0-9]", "")
										.substring(0, 8)
								+ "_" + uniswitchDetail.getUniquePortId() + "_"
								+ uniswitchDetail.getPhysicalPort().replaceAll("[^0-9/]", ""));
					}

				}
			}
			serviceQo.setIsbandwidthApplicable((byte) 1);
			serviceQo.setModifiedBy(MODIFIEDBY);
			serviceQo.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			serviceQoRepository.save(serviceQo);
		}

	}

	private void applyUinRule(ServiceDetail serviceDetails, Map<String, String> map) {

		if (serviceDetails.getTopologies() != null) {
			com.tcl.dias.serviceactivation.entity.entities.Topology topology = serviceDetails.getTopologies().stream()
					.filter(top -> top.getEndDate() == null).findFirst().orElse(null);

			if (topology != null && topology.getUniswitchDetails() != null) {
				UniswitchDetail uniswitchDetail = topology.getUniswitchDetails().stream()
						.filter(uni -> uni.getEndDate() == null).findFirst().orElse(null);
				if (uniswitchDetail != null) {

					if (map.containsKey(AceConstants.INTERFACE.ETHRNER_INNERV_LAN)) {
						uniswitchDetail.setInnerVlan(map.get(AceConstants.INTERFACE.ETHRNER_INNERV_LAN));
					}

					/*
					 * if(map.containsKey(AceConstants.INTERFACE.ETHRNER_OUTERV_LAN)) {
					 * uniswitchDetail.setOuterVlan(map.get(AceConstants.INTERFACE.
					 * ETHRNER_OUTERV_LAN)); }
					 */

					if (uniswitchDetail.getSwitchModel() != null && uniswitchDetail.getSwitchModel().contains("ME36")) {
						uniswitchDetail.setMaxMacLimit("10");// gvpn
					} else {
						uniswitchDetail.setMaxMacLimit(null);
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

	}

	private void applyServiceDetailsRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields,
			VpnMetatData vpnMetatData) throws TclCommonException {
		String vrfName = manDatoryFields.get(AceConstants.VRF.VRF_NAME);

		String routerMake = manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE);
		MstVpnSamManagerId mstVpnSamManagerId = null;

		serviceDetails.setIsdowntimeReqd((byte) 0);
		serviceDetails.setExpediteTerminate((byte) 0);
		serviceDetails.setExternalRefid(null);
		serviceDetails.setServiceState("ISSUED");
		serviceDetails.setTriggerNccmOrder((byte) 0);
		serviceDetails.setIscustomConfigReqd((byte) 0);
		serviceDetails.setIntefaceDescSvctag("GVPN");
		serviceDetails.setSkipDummyConfig((byte) 1);
		if (serviceDetails.getOrderCategory() == null) {
			serviceDetails.setOrderCategory("CUSTOMER_ORDER");
		}
		serviceDetails.setOrderType("NEW");
		String siteRole = getSiteRole(serviceDetails);

		serviceDetails.setRouterMake(routerMake);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			serviceDetails.setIsIdcService((byte) 0);
			serviceDetails.setUsageModel("FIXED");
		}

		if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerMake)) {
			if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(vrfName)) {
				serviceDetails.setCssSammgrId(4755);
				serviceDetails.setAluSvcName("INTERNET-VPN:INTERNET-VPN:S");

			} else if (AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(vrfName)) {

				serviceDetails.setCssSammgrId(874);
				serviceDetails.setAluSvcName("ILL:PRIMUS_INTERNET:S");

			} else {

				if (siteRole.toLowerCase().contains("hub") || siteRole.toLowerCase().contains("mesh")) {
					mstVpnSamManagerId = mstVpnmanagerIdRepository.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(
							vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails), siteRole);

				} else {
					mstVpnSamManagerId = mstVpnmanagerIdRepository
							.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
									"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(),
									siteRole);
				}
				Integer cssSamcustomerId = 0;// for ill ,it should be increment

				if (mstVpnSamManagerId != null && mstVpnSamManagerId.getSamMgrId() != null) {
					cssSamcustomerId = Integer.valueOf(mstVpnSamManagerId.getSamMgrId());
				} else {

					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
							serviceDetails.getServiceSubtype());
					if (aluSammgrSeq != null) {
						cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
					}
				}
				serviceDetails.setCssSammgrId(cssSamcustomerId);
				serviceDetails.setAluSvcName(serviceDetails.getOrderDetail().getSamCustomerDescription() + "_"
						+ serviceDetails.getCssSammgrId() + "_" + "ACE");

			}
		}
		serviceDetails.setAluSvcName(null);
		serviceDetails.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		serviceDetails.setModifiedBy(MODIFIEDBY);

		serviceDetailRepository.save(serviceDetails);
	}

	private void applyCpeRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields,
			List<InterfaceProtocolMapping> cpeProtocolMappings) throws TclCommonException {

		try {
			logger.info("applyCpeRule  started {}");

			if (!cpeProtocolMappings.isEmpty()) {
				cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);

				if (cpeProtocolMapping != null) {
					Cpe cpe = cpeProtocolMapping.getCpe();
					cpe.setSnmpServerCommunity("t2c2l2com");
					cpe.setIsaceconfigurable((byte) 0);
					cpe.setSendInittemplate((byte) 0);
					cpe.setServiceId(serviceDetails.getServiceId());
					cpe.setModifiedBy(MODIFIEDBY);
					cpe.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					cpeRepository.save(cpe);
				}
			}
		}

		catch (Exception e) {
			logger.error("Exception in applyCpeRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
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

	private void applyOrderRule(OrderDetail orderDetail, ServiceDetail serviceDetails, Map<String, String> map,
			VpnMetatData vpnMetatData) throws TclCommonException {

		String vrfName = map.get(AceConstants.VRF.VRF_NAME);
		logger.info("vrfName ={}", vrfName);
		if (vrfName == null)
			vrfName = AceConstants.DEFAULT.NOT_APPLICABLE;
		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (orderDetail.getCustomerCrnId() == null) {
			logger.error("CRN Id not found for serviceId : {} ", serviceDetails.getServiceId());
			throw new TclCommonException(ExceptionConstants.NO_CRN_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		}

		/*
		 * if (orderDetail.getOrderCategory() == null) {
		 * orderDetail.setOrderCategory("CUSTOMER_ORDER"); }
		 */
		MstVpnSamManagerId mstVpnSamManagerId = null;
		orderDetail.setAsdOpptyFlag((byte) 0);
		orderDetail.setModifiedBy(MODIFIEDBY);
		orderDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		// orderDetail.setOrderType("NEW");
		orderDetail.setGroupId("CMIP");
		// orderDetail.setOriginatorName("OPTIMUS");
		orderDetail.setOriginatorName(cramerSourceSystem);
		orderDetail.setOriginatorDate(new Timestamp(System.currentTimeMillis()));
		orderDetail.setLocation(orderDetail.getCity());
		orderDetail.setOrderStatus("IN PROGRESS");// other status are cancelled ,closed,jeopardy
		orderDetail.setOrderUuid(AceConstants.ORDER.SERVICE_NEW_ + orderDetail.getExtOrderrefId());
		String siteRole = getSiteRole(serviceDetails);

		if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			if (vrfName.equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)) {
				orderDetail.setSamCustomerDescription("INTERNET-VPN");
			} else if (vrfName.equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {

				orderDetail.setSamCustomerDescription("PRIMUS_INTERNET");

			} else if (map.containsKey(AceConstants.VRF.VRF_NAME) && map.containsKey(AceConstants.SERVICE.SERVICE_TYPE)
					&& map.get(AceConstants.VRF.VRF_NAME).equalsIgnoreCase(AceConstants.DEFAULT.NOT_APPLICABLE)
					|| map.get(AceConstants.VRF.VRF_NAME).equalsIgnoreCase(AceConstants.DEFAULT.NA)) {

				if (orderDetail.getCustomerName().length() > 18) {
					orderDetail.setSamCustomerDescription(
							orderDetail.getCustomerName().substring(0, 17).replaceAll(" ", "_"));

				} else {

					orderDetail.setSamCustomerDescription(orderDetail.getCustomerName().replaceAll(" ", "_"));
				}

			}

			if (vrfName.equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)) {
				orderDetail.setAluCustomerId("4755");
			} else if (vrfName.equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {
				orderDetail.setAluCustomerId("584");

			} else if (vrfName != null) {
				Integer cssSamcustomerId = 0;// for ill ,it should be increment

				if (siteRole.equalsIgnoreCase("SPOKE")) {

					AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
							serviceDetails.getServiceSubtype());

					mstVpnSamManagerId = mstVpnmanagerIdRepository
							.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
									"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(),
									siteRole);

					if (mstVpnSamManagerId == null) {

						saveMstVpnSamManagerId(aluSammgrSeq, serviceDetails, vpnMetatData,
								getTopologyName(serviceDetails), siteRole);
					}

					if (orderDetail.getCustomerCrnId() != null) {
						MstGvpnAluCustId gvpnAluCustId = mstGvpnAluCustIdRepository
								.findByCrnId(orderDetail.getCustomerCrnId());
						if (gvpnAluCustId != null) {
							orderDetail.setAluCustomerId(gvpnAluCustId.getAluCustId());
						} else {
							orderDetail.setAluCustomerId(String.valueOf(aluSammgrSeq.getSammgrSeqid()));
						}
					}

				} else {

					if (siteRole.toLowerCase().contains("hub") || siteRole.toLowerCase().contains("mesh")) {
						mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(vpnMetatData.getVpnName(),
										"CUSTOMER", getTopologyName(serviceDetails), siteRole);

					} else {
						mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(
										vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails),
										serviceDetails.getServiceId(), siteRole);
					}

					if (mstVpnSamManagerId != null) {
						cssSamcustomerId = Integer.valueOf(mstVpnSamManagerId.getSamMgrId());
					} else {

						AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
								serviceDetails.getServiceSubtype());

						saveMstVpnSamManagerId(aluSammgrSeq, serviceDetails, vpnMetatData,
								getTopologyName(serviceDetails), siteRole);

						if (Objects.nonNull(aluSammgrSeq)) {
							cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
						}

					}
					if (orderDetail.getCustomerCrnId() != null) {
						MstGvpnAluCustId gvpnAluCustId = mstGvpnAluCustIdRepository
								.findByCrnId(orderDetail.getCustomerCrnId());
						if (gvpnAluCustId != null) {
							orderDetail.setAluCustomerId(gvpnAluCustId.getAluCustId());
						} else {

							AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
									serviceDetails.getServiceSubtype());
							if (aluSammgrSeq != null) {
								cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
							}
							orderDetail.setAluCustomerId(String.valueOf(cssSamcustomerId));
							MstGvpnAluCustId mstGvpnAluCustIdNew = new MstGvpnAluCustId();
							mstGvpnAluCustIdNew.setAluCustId(String.valueOf(cssSamcustomerId));
							mstGvpnAluCustIdNew.setCustomerName(orderDetail.getCustomerName());
							mstGvpnAluCustIdNew.setCrnId(orderDetail.getCustomerCrnId());
							mstGvpnAluCustIdRepository.save(mstGvpnAluCustIdNew);
						}
					}

				}
				serviceDetails.setAluSvcid(null);

			}
		}
		if (orderDetail.getCustomerCategory() == null) {
			orderDetail.setCustomerCategory("Enterprise");
		}
		orderDetailRepository.save(orderDetail);

	}

	private void saveMstVpnSamManagerId(AluSammgrSeq aluSammgrSeq, ServiceDetail serviceDetails,
			VpnMetatData vpnMetatData, String string, String siteRole) {
		MstVpnSamManagerId samManagerId = new MstVpnSamManagerId();
		samManagerId.setSamMgrId(aluSammgrSeq.getSammgrSeqid().toString());
		samManagerId.setCreatedBy("OPTIMUS_RULE");
		OrderDetail orderDetail = serviceDetails.getOrderDetail();
		if (Objects.nonNull(orderDetail) && Objects.nonNull(orderDetail.getCustCuId())) {
			samManagerId.setCuid(orderDetail.getCustCuId().toString());
		}
		samManagerId.setVpnType("CUSTOMER");
		if (!StringUtils.isEmpty(vpnMetatData.getL2OTopology())) {
			if (vpnMetatData.getL2OTopology().toLowerCase().contains("hub")) {
				samManagerId.setVpnTopology("HUBnSPOKE");
			} else if (vpnMetatData.getL2OTopology().toLowerCase().contains("mesh")) {
				samManagerId.setVpnTopology("MESH");
			}
		}
		samManagerId.setVpnName(vpnMetatData.getVpnName());
		samManagerId.setSiteRole(siteRole);
		samManagerId.setServiceCode(serviceDetails.getServiceId());
		samManagerId.setLastModifiedBy(MODIFIEDBY);
		mstVpnmanagerIdRepository.save(samManagerId);
	}

	private String getTopologyName(ServiceDetail serviceDetail) {

		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),
						Arrays.asList("Site Type", "VPN Topology"));
		Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String vpnTopology = attributeMap.getOrDefault("VPN Topology", null);
		return vpnTopology.toUpperCase().contains("HUB") ? "HUBnSPOKE" : "MESH";
	}

	private String getSiteRole(ServiceDetail serviceDetail) {

		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),
						Arrays.asList("Site Type", "VPN Topology"));
		Map<String, String> attributeMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String siteType = attributeMap.getOrDefault("Site Type", null);
		return siteType != null ? siteType.toUpperCase() : siteType;

	}

	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
	public AluSammgrSeq getAluSamMgrSeq(String serviceId, String subType) throws TclCommonException {

		logger.info("Service Activation - saveAluSammgrSeq - started");
		try {
			AluSammgrSeq oldSammgrSeq = aluSammgrSeqRepository.findFirstByServiceIdOrderBySammgrSeqidDesc(serviceId);

			if (oldSammgrSeq != null) {
				return oldSammgrSeq;
			}
			AluSammgrSeq aluSammgrSeq = new AluSammgrSeq();
			aluSammgrSeq.setServiceId(serviceId);
			aluSammgrSeq.setServiceType(subType);
			aluSammgrSeq = aluSammgrSeqRepository.save(aluSammgrSeq);
			logger.info("Service Activation - saveAluSammgrSeq - completed");
			return aluSammgrSeq;
		} catch (Exception e) {
			logger.error("Exception in saveAluSammgrSeq => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void applyRoutingProtocal(InterfaceProtocolMapping interfaceProtocolMapping, Map<String, String> map,
			ServiceDetail serviceDetails) throws TclCommonException {

		if (interfaceProtocolMapping.getBgp() != null) {
			applyBgpRule(interfaceProtocolMapping.getBgp(), map, serviceDetails);
		}
		if (interfaceProtocolMapping.getStaticProtocol() != null) {
			applyStaticRule(interfaceProtocolMapping.getStaticProtocol(), serviceDetails);
		}

	}

	private void applyStaticRule(StaticProtocol staticProtocol, ServiceDetail serviceDetail) throws TclCommonException {

		List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetail.getInterfaceProtocolMappings().stream()
				.filter(serIn -> serIn.getRouterDetail() != null && serIn.getEndDate() == null)
				.collect(Collectors.toList());
		interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());

		ScComponentAttribute lanPoolRoutingNeededAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						serviceDetail.getScServiceDetailId(), "lanPoolRoutingNeeded", "LM", "A");
		if (interfaceProtocolMappings != null && !interfaceProtocolMappings.isEmpty()) {
			InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);

			if ((lanPoolRoutingNeededAttribute != null
					&& "Yes".equalsIgnoreCase(lanPoolRoutingNeededAttribute.getAttributeValue()))
					|| (lanPoolRoutingNeededAttribute == null)) { // lanPoolRoutingNeededAttribute might be null for old
																	// case
				saveWanStaticRoutes(serviceDetail, interfaceProtocolMapping.getRouterDetail(), staticProtocol);
			}
		}
	}

	private void applyBgpRule(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails)
			throws TclCommonException {
		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		if (bgp.getNeighborOn() == null) {

			bgp.setNeighborOn(AceConstants.IPADDRESS.WAN);
		}
		String redundancyRule = map.getOrDefault(AceConstants.SERVICE.REDUNDACY_ROLE, null);
		if (redundancyRule != null && (redundancyRule.equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)
				|| redundancyRule.equalsIgnoreCase(AceConstants.SERVICE.SECONDARY))) {
			bgp.setSooRequired((byte) 1);

		} else {
			bgp.setSooRequired((byte) 0);

		}

		bgp.setAsoOverride((byte) 1);
		bgp.setSplitHorizon((byte) 1);

		if (bgp.getIsauthenticationRequired() == null || bgp.getIsauthenticationRequired() == 0) {
			bgp.setAuthenticationMode(null);
			bgp.setPassword(null);

		}

		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerName)) {
			bgp.setIsmultihopTtl("10");

			bgp.setMaxPrefix("2000");
			bgp.setMaxPrefixThreshold("80");
			bgp.setIsrtbh((byte) 1);
			bgp.setRtbhOptions(AceConstants.PROTOCOL.RTBH);

			if (bgp.getNeighborOn() != null && bgp.getNeighborOn().equalsIgnoreCase(AceConstants.PROTOCOL.LOOPBACK)) {
				bgp.setIsmultihopTtl("10");
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

		} else {
			bgp.setIsbgpMultihopReqd((byte) 0);
		}

		if (map.containsKey(AceConstants.VRF.VRF_NAME) && routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

			String vrfName = map.get(AceConstants.VRF.VRF_NAME);
			String serviceCode = map.get(AceConstants.SERVICE.SERVICEID);

			String code = serviceCode.substring(3, serviceCode.length());

			if (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(vrfName)) {

				bgp.setBgpPeerName(AceConstants.DEFAULT.INTERNET + "_4755" + "_" + code);

			} else if (AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(vrfName)) {
				bgp.setBgpPeerName(AceConstants.DEFAULT.PRIMUS + "_874" + code);

			} else if (AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(vrfName)) {

				Integer cssSammgrId = serviceDetails.getCssSammgrId();
				logger.info("cssSammgrId={}", cssSammgrId);

				bgp.setBgpPeerName(splitName(map.get(AceConstants.SERVICE.CUSTOMERNAME), 4).trim() + "_" + cssSammgrId
						+ "_" + code);

			}

			bgp.setLocalAsNumber(AceConstants.PROTOCOL.LOCALASNUMBER);

		}

		if (Objects.nonNull(bgp.getRemoteAsNumber()) && bgp.getRemoteAsNumber() > 1
				&& bgp.getRemoteAsNumber() < 65535) {
			bgp.setAsoOverride((byte) 1);
		}

		else {
			bgp.setAsoOverride((byte) 0);

		}

		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerName)) {
			bgp.setRedistributeConnectedEnabled((byte) 1);
			bgp.setRedistributeStaticEnabled((byte) 1);

			if (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("DUALSTACK")
					|| map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("v4")) {
				/*
				 * bgp.setIsbgpNeighbourInboundv4RoutemapEnabled((byte) 1);
				 * bgp.setBgpneighbourinboundv4routermapname(serviceDetails.getServiceId() + "_"
				 * + "IPv4_In"); bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 1);
				 */
				if (map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase("PRIMARY")) {
					bgp.setBgpneighbourinboundv4routermapname("LP_PRIMARY");
				}
			}

			if (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("DUALSTACK")
					|| map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("v6")) {
				/*
				 * bgp.setIsbgpNeighbourInboundv6RoutemapEnabled((byte) 1);
				 * bgp.setBgpneighbourinboundv6routermapname(serviceDetails.getServiceId() + "_"
				 * + "IPv6_In");
				 */
				if (map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase("PRIMARY")) {
					bgp.setBgpneighbourinboundv4routermapname("LP_PRIMARY");
				}
				/* bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 1); */

			}

			/*
			 * bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned((byte) 0);
			 * bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 0);
			 */

		}
		if (AceConstants.ROUTER.ROUTER_ALU.equalsIgnoreCase(routerName)) {
			bgp.setAluDisableBgpPeerGrpExtCommunity((byte) 1);
			bgp.setPeerType(AceConstants.ROUTER.EXTERNAL);
			bgp.setAluBackupPath(AceConstants.DEFAULT.NOT_APPLICABLE);

		}

		if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
				|| routerName.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			bgp.setRedistributeConnectedEnabled(null);
			bgp.setRedistributeStaticEnabled(null);
		}

		if (bgp.getNeighborOn() == null || AceConstants.IPADDRESS.WAN.equalsIgnoreCase(bgp.getNeighborOn())) {

			if (map.containsKey(AceConstants.CPE.CPE_INTERFACE_IPV4)) {

				bgp.setRemoteIpv4Address(map
						.get(AceConstants.CPE.CPE_INTERFACE_IPV4) /*
																	 * + "/" + subnet(map.get(AceConstants.IPADDRESS.
																	 * WANV4ADDRESS))
																	 */);
			}
			if (map.containsKey(AceConstants.CPE.CPE_INTERFACE_IPV6)) {
				bgp.setRemoteIpv6Address(map
						.get(AceConstants.CPE.CPE_INTERFACE_IPV6) /*
																	 * + "/" + subnet(map.get(AceConstants.IPADDRESS.
																	 * WANV6ADDRESS))
																	 */);
			}

		}

		else if (bgp.getNeighborOn() != null && bgp.getNeighborOn().equalsIgnoreCase(AceConstants.PROTOCOL.LOOPBACK)) {
			bgp.setRemoteIpv4Address(bgp.getRemoteUpdateSourceIpv4Address());
			bgp.setRemoteIpv6Address(bgp.getRemoteUpdateSourceIpv6Address());
		}

		if (map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
				&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)
				&& map.containsKey(AceConstants.IPADDRESS.IPPATH)
				&& (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase(AceConstants.IPADDRESS.IPV4)
						|| AceConstants.IPADDRESS.DUAL.equalsIgnoreCase(map.get(AceConstants.IPADDRESS.IPPATH)))) {

			bgp.setLocalPreference(AceConstants.PROTOCOL.SETLOCALPREFERENCE);

		}
		if (map.containsKey(AceConstants.SERVICE.ASDFLAG)
				&& map.get(AceConstants.SERVICE.ASDFLAG).equalsIgnoreCase("TRUE")) {
			bgp.setLocalPreference(null);

		}
		bgp.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		bgp.setModifiedBy(MODIFIEDBY);

		if (map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)) {
			String redunDancy = map.get(AceConstants.SERVICE.REDUNDACY_ROLE);

			if (!redunDancy.equalsIgnoreCase("SINGLE"))
				bgp.setSooRequired((byte) 1);
		}
		if (bgp.getPassword() != null) {
			bgp.setAuthenticationMode("MD5");
			bgp.setIsauthenticationRequired((byte) 1);
		}
		bgp.setRoutesExchanged(null);
		OrderDetail orderDetail = serviceDetails.getOrderDetail();
		if (orderDetail != null && orderDetail.getExtOrderrefId() != null
				&& orderDetail.getExtOrderrefId().toLowerCase().contains("izosdwan")) {
			logger.info("GVPN.applyBgpRule Setting Local preference for sdwan");
			bgp.setV6LocalPreference(null);
			bgp.setLocalPreference(null);
		}
		if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
			logger.info("Izopc Bgp specific details::{}", serviceDetails.getServiceId());
			bgp.setSooRequired((byte) 1);
			bgp.setLocalPreference(AceConstants.PROTOCOL.SETLOCALPREFERENCE);
			ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
							"Cloud Provider");
			if (cloudProviderAttribute != null
					&& "AWS Direct Connect".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())) {
				logger.info("Izopc Bgp::AWS CSP for Service Id::{}", serviceDetails.getServiceId());
				bgp.setPassword("AWS@TCL!p$&55");
				bgp.setAuthenticationMode("MD5");
				bgp.setIsauthenticationRequired((byte) 1);
			}
			if (cloudProviderAttribute != null
					&& ("Google Public Cloud".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
							|| "Google Cloud Interconnect-Private"
									.equalsIgnoreCase(cloudProviderAttribute.getAttributeValue()))) {
				logger.info("Izopc bgp::Google CSP for Service Id::{}", serviceDetails.getServiceId());
				bgp.setIsebgpMultihopReqd((byte) 1);
				bgp.setIsmultihopTtl("5");
				bgp.setIsauthenticationRequired((byte) 1);
			}
			if (cloudProviderAttribute != null
					&& !"IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())) {
				logger.info("Izopc bgp::Other than IBM CSP for Service Id::{}", serviceDetails.getServiceId());
				bgp.setLocalPreference("200");
			}
			if (cloudProviderAttribute != null
					&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
					&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
				logger.info("Izopc bgp::IBM CSP for Secondary Service Id::{}", serviceDetails.getServiceId());
				bgp.setLocalPreference(null);
			}
		}
		bgpRepository.save(bgp);

		applyBgpPolicyType(bgp, map, serviceDetails);

	}

	private void applyBgpPolicyType(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails)
			throws TclCommonException {
		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		String ippath = map.get(AceConstants.IPADDRESS.IPPATH);

		if (Objects.isNull(ippath)) {
			String errorMessage = "IP not Allocated";
			ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(serviceDetails.getScServiceDetailId(),
							"WAN IP Provided By");
			if (Objects.nonNull(scServiceAttribute))
				errorMessage += " : WAN IP Provided By " + scServiceAttribute.getAttributeValue();
			serviceActivationService.saveErrorDetails(serviceDetails.getScServiceDetailId(), errorMessage, "IP1001",
					"enrich-service-design-ip-jeopardy");
		}
		if ((ippath.equalsIgnoreCase("v4") || ippath.equalsIgnoreCase("DUALSTACK"))
				&& (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP))) {

			createInboundPolicyV4(bgp, map, serviceDetails, routerMake);

			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				createOutboundPolicyAluV4(bgp, map, serviceDetails);

			}
			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

				createOutboundPolicyJuniperV4(bgp, map, serviceDetails);

			}

		}
		if ((ippath.equalsIgnoreCase("v6") || ippath.equalsIgnoreCase("DUALSTACK"))
				&& (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP))) {

			createInboundV6(bgp, map, serviceDetails, routerMake);

			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				createOutboundPolicyAluV6(bgp, map, serviceDetails);

			}
			if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

				createOutboundPolicyJuniperV6(bgp, map, serviceDetails);

			}

		}

	}

	private void createOutboundPolicyJuniperV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails) {
		PolicyType outBoundPolicy = new PolicyType();
		outBoundPolicy.setBgp(bgp);
		outBoundPolicy.setOutboundIpv6PolicyName(serviceDetails.getServiceId() + "_IPv6_Out");
		outBoundPolicy.setOutboundRoutingIpv6Policy((byte) 1);
		outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 1);
		outBoundPolicy.setOutboundIstandardpolicyv6((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(outBoundPolicy);
	}

	private void createOutboundPolicyAluV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails) {
		PolicyType outBoundPolicy = new PolicyType();
		outBoundPolicy.setBgp(bgp);
		if (bgp.getSooRequired() == 0) {
			outBoundPolicy.setOutboundIpv6PolicyName("PE-CE_EXPORT");
			outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 1);
		} else {
			outBoundPolicy.setOutboundIpv6PolicyName(serviceDetails.getServiceId() + "" + "_IPv6_Out");
			outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 0);

		}
		outBoundPolicy.setOutboundRoutingIpv6Policy((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		// outBoundPolicy.setOutboundIstandardpolicyv6((byte) 1);
		policyTypeRepository.save(outBoundPolicy);
	}

	private void createInboundPolicyV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routerMake) {
		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
			PolicyType policyType = new PolicyType();
			policyType.setBgp(bgp);
			policyType.setInboundRoutingIpv4Policy((byte) 1);
			policyType.setInboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv4_In");
			if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
				logger.info("Izopc createInboundPolicyV4.PolicyName for Service Id::{}", serviceDetails.getServiceId());
				policyType.setInboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_P_IPv4_In");
				ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
								"Cloud Provider");
				if (cloudProviderAttribute != null
						&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
						&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
					logger.info("Izopc IBM CSP createInboundPolicyV4.PolicyName for Sy Service Id::{}",
							serviceDetails.getServiceId());
					policyType.setInboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_S_IPv4_In");
				}
			}
			policyType.setInboundIpv4Preprovisioned((byte) 0);
			if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
				policyType.setInboundIstandardpolicyv4((byte) 1);

			}
			policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType);
			policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgp(policyType, map, serviceDetails));
		}
	}

	private void createInboundV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails, String routerMake)
			throws TclCommonException {
		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
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
		}
	}

	private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgpV6(PolicyType policyType, Map<String, String> map,
			ServiceDetail serviceDetails) throws TclCommonException {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
			if (ipAddressDetail != null && ipAddressDetail.getIpaddrLanv6Addresses() != null
					&& !ipAddressDetail.getIpaddrLanv6Addresses().isEmpty()) {
				createEnablePrefixCriteriaV6(policyTypeCriteriaMappings, serviceDetails, policyType, map);
			}
			enableLocalPreferenceV6(policyTypeCriteriaMappings, policyType, routerMake, map);

		}

		return policyTypeCriteriaMappings;
	}

	private void createEnablePrefixCriteriaV6(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, PolicyType policyType, Map<String, String> map) {

		PolicyCriteria enablePrefix = new PolicyCriteria();
		enablePrefix.setMatchcriteriaPrefixlist((byte) 0);
		enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv6_In");
		if (map.get(AceConstants.ROUTER.ROUTER_MAKE).equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

			enablePrefix.setTermName("10");
			enablePrefix.setTermSetcriteriaAction("ACCEPT");
		}
		enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefix.setModifiedBy(MODIFIEDBY);
		policyCriteriaRepository.save(enablePrefix);
		// enablePrefix.setPrefixlistConfigs(createPrefixConfigV6(enablePrefix,
		// serviceDetails, map.get(AceConstants.ROUTER.ROUTER_MAKE)));
		PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
		enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
		enablePrefixCriteriaMapping.setPolicyType(policyType);
		policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
		policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);

	}

	private void enableLocalPreferenceV6(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			PolicyType policyType, String routerMake, Map<String, String> map) throws TclCommonException {

		try {

			logger.info("enableLocalPreferenceV6  started {}");
			if (!routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)
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

	private void createOutboundPolicyJuniperV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails) {
		PolicyType outBoundPolicy = new PolicyType();
		outBoundPolicy.setBgp(bgp);

		outBoundPolicy.setOutboundIpv4PolicyName("PE-CE_EXPORT");

		outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
		outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 1);
		outBoundPolicy.setOutboundIstandardpolicyv4((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(outBoundPolicy);
	}

	private void createOutboundPolicyAluV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails) {
		PolicyType outBoundPolicy = new PolicyType();
		outBoundPolicy.setBgp(bgp);

		if (bgp.getSooRequired() == 0) {
			outBoundPolicy.setOutboundIpv4PolicyName("PE-CE_EXPORT");
			outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 1);
		} else {
			outBoundPolicy.setOutboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv4_Out");
			outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 0);
		}
		if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
			logger.info("Izopc createOutboundPolicyAluV4.PolicyName for Service Id::{}", serviceDetails.getServiceId());
			outBoundPolicy.setOutboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_P_IPv4_Out");
			ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
							"Cloud Provider");
			if (cloudProviderAttribute != null
					&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
					&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
				logger.info("Izopc IBM CSP createOutboundPolicyAluV4.PolicyName for Sy Service Id::{}",
						serviceDetails.getServiceId());
				outBoundPolicy.setOutboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_S_IPv4_Out");
			}
		}
		outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(outBoundPolicy);
		if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
			logger.info("Izopc createOutboundPolicyAluV4.PolicyCriteriaMapping for Service Id::{}",
					serviceDetails.getServiceId());
			outBoundPolicy.setPolicyTypeCriteriaMappings(
					createOutboundPolicyCriteraForBgp(outBoundPolicy, map, serviceDetails));
		}
	}

	private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgp(PolicyType policyType, Map<String, String> map,
			ServiceDetail serviceDetails) {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
			if ((ipAddressDetail != null && ipAddressDetail.getIpaddrLanv4Addresses() != null
					&& !ipAddressDetail.getIpaddrLanv4Addresses().isEmpty())
					|| ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName()))) {
				createEnablePrefixCriteria(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType, map);
			}
			enableProtocalPolicyCriteria(policyTypeCriteriaMappings, policyType, map);
			enableLocalPreference(policyTypeCriteriaMappings, map);
		}

		return policyTypeCriteriaMappings;
	}

	private Set<PolicyTypeCriteriaMapping> createOutboundPolicyCriteraForBgp(PolicyType policyType,
			Map<String, String> map, ServiceDetail serviceDetails) {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
			ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
							"Cloud Provider");
			if (cloudProviderAttribute != null
					&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
					&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
				logger.info("Izopc createOutboundPolicyCriteraForBgp for IBM CSP Sy Service Id::{}",
						serviceDetails.getServiceId());
				createEnableOutboundPrefixCriteria(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType,
						map);
			} else {
				logger.info("Izopc createOutboundPolicyCriteraForBgp for other than IBM CSP Service Id::{}",
						serviceDetails.getServiceId());
				IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
						.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
				if ((ipAddressDetail != null && ipAddressDetail.getIpaddrLanv4Addresses() != null
						&& !ipAddressDetail.getIpaddrLanv4Addresses().isEmpty())
						|| ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName()))) {
					createEnableOutboundPrefixCriteria(policyTypeCriteriaMappings, serviceDetails, routerMake,
							policyType, map);
				}
			}
			enableProtocalOutboundPolicyCriteria(policyTypeCriteriaMappings, policyType, map);
			enableLocalPreference(policyTypeCriteriaMappings, map);
		}

		return policyTypeCriteriaMappings;
	}

	private void enableProtocalOutboundPolicyCriteria(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			PolicyType policyType, Map<String, String> map) {
		PolicyCriteria enableProtocal = new PolicyCriteria();
		enableProtocal.setMatchcriteriaProtocol((byte) 0);
		if (map.get(AceConstants.ROUTER.ROUTER_MAKE).equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			enableProtocal.setTermName("10");
			enableProtocal.setTermSetcriteriaAction("ACCEPT");

		}
		// enableProtocal.setPolicycriteriaProtocols(createPolicyProtocalForBgp(enableProtocal));
		enableProtocal.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enableProtocal.setModifiedBy(MODIFIEDBY);
		policyCriteriaRepository.save(enableProtocal);
		PolicyTypeCriteriaMapping enableProtocalCriteriaMapping = new PolicyTypeCriteriaMapping();
		enableProtocalCriteriaMapping.setPolicyCriteriaId(enableProtocal.getPolicyCriteriaId());
		enableProtocalCriteriaMapping.setPolicyType(policyType);
		enableProtocalCriteriaMapping.setVersion(1);
		enableProtocalCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enableProtocalCriteriaMapping.setModifiedBy(MODIFIEDBY);
		enableProtocalCriteriaMapping.setPolicyType(policyType);
		policyTypeCriteriaMappingRepository.save(enableProtocalCriteriaMapping);
		policyTypeCriteriaMappings.add(enableProtocalCriteriaMapping);
	}

	private void createEnablePrefixCriteria(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType policyType, Map<String, String> map) {
		PolicyCriteria enablePrefix = new PolicyCriteria();
		enablePrefix.setMatchcriteriaPrefixlist((byte) 0);
		enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv4_In");
		enablePrefix.setMatchcriteriaPprefixlistPreprovisioned((byte) 0);
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			enablePrefix.setTermName("10");
			enablePrefix.setTermSetcriteriaAction("ACCEPT");
		}
		enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefix.setModifiedBy(MODIFIEDBY);
		policyCriteriaRepository.save(enablePrefix);
		if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
			logger.info("Izopc createEnablePrefixCriteria for Service Id::{}", serviceDetails.getServiceId());
			ScComponentAttribute prefixForCspProvidedByCustomerAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetails.getScServiceDetailId(), "prefixForCspProvidedByCustomer", "LM", "A");
			if (prefixForCspProvidedByCustomerAttribute != null
					&& prefixForCspProvidedByCustomerAttribute.getAttributeValue() != null
					&& "Yes".equalsIgnoreCase(prefixForCspProvidedByCustomerAttribute.getAttributeValue())) {
				logger.info("prefixForCspProvidedByCustomerAttribute for Service Id::{}",
						serviceDetails.getServiceId());
				ScComponentAttribute prefixForCspAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								serviceDetails.getScServiceDetailId(), "prefixForCsp", "LM", "A");
				if (prefixForCspAttribute != null && prefixForCspAttribute.getAttributeValue() != null) {
					logger.info("createInboundV4PrefixConfigForIZOPC for Service Id::{} with PrefixForCsp::{}",
							serviceDetails.getServiceId(), prefixForCspAttribute.getAttributeValue());
					enablePrefix.setMatchcriteriaPrefixlist((byte) 1);
					enablePrefix.setPrefixlistConfigs(
							createInboundV4PrefixConfigForIZOPC(enablePrefix, serviceDetails, routerMake));
					enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_P_IPv4_In");
					ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
									"Cloud Provider");
					if (cloudProviderAttribute != null
							&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
							&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
						logger.info("Izopc IBM CSP createEnablePrefixCriteria for Sy Service Id::{}",
								serviceDetails.getServiceId());
						enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_S_IPv4_In");
					}
					policyCriteriaRepository.saveAndFlush(enablePrefix);
				}
			}
		}
		PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setPolicyType(policyType);
		enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
		policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
		policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
	}

	private void createEnableOutboundPrefixCriteria(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType policyType, Map<String, String> map) {
		PolicyCriteria enablePrefix = new PolicyCriteria();
		enablePrefix.setMatchcriteriaPrefixlist((byte) 0);
		enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv4_Out");
		enablePrefix.setMatchcriteriaPprefixlistPreprovisioned((byte) 0);
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			enablePrefix.setTermName("10");
			enablePrefix.setTermSetcriteriaAction("ACCEPT");
		}
		enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefix.setModifiedBy(MODIFIEDBY);
		policyCriteriaRepository.save(enablePrefix);
		if ("IZOPC".equalsIgnoreCase(serviceDetails.getProductName())) {
			logger.info("Izopc createEnableOutboundPrefixCriteria for Service Id::{}", serviceDetails.getServiceId());
			ScComponentAttribute prefixFromMplsToCspProvidedByCustomerAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetails.getScServiceDetailId(), "prefixFromMplsToCspProvidedByCustomer", "LM", "A");
			if (prefixFromMplsToCspProvidedByCustomerAttribute != null
					&& prefixFromMplsToCspProvidedByCustomerAttribute.getAttributeValue() != null
					&& "Yes".equalsIgnoreCase(prefixFromMplsToCspProvidedByCustomerAttribute.getAttributeValue())) {
				logger.info("prefixFromMplsToCspProvidedByCustomerAttribute for Service Id::{}",
						serviceDetails.getServiceId());
				ScComponentAttribute prefixFromMplsToCspAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								serviceDetails.getScServiceDetailId(), "prefixFromMplsToCsp", "LM", "A");
				if (prefixFromMplsToCspAttribute != null && prefixFromMplsToCspAttribute.getAttributeValue() != null) {
					logger.info("createOutboundV4PrefixConfigForIZOPC for Service Id::{} with PrefixForMplstoCsp::{}",
							serviceDetails.getServiceId(), prefixFromMplsToCspAttribute.getAttributeValue());
					enablePrefix.setMatchcriteriaPrefixlist((byte) 1);
					enablePrefix.setPrefixlistConfigs(
							createOutboundV4PrefixConfigForIZOPC(enablePrefix, serviceDetails, routerMake));
					enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_P_IPv4_Out");
					ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(serviceDetails.getScServiceDetailId(),
									"Cloud Provider");
					if (cloudProviderAttribute != null
							&& "IBM Direct Link".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())
							&& "Secondary".equalsIgnoreCase(serviceDetails.getRedundancyRole())) {
						logger.info("Izopc IBM CSP createEnableOutboundPrefixCriteria for Sy Service Id::{}",
								serviceDetails.getServiceId());
						enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_S_IPv4_Out");
						enablePrefix.setSetcriteriaAspathPrepend((byte) 1);
						enablePrefix.setSetcriteriaAspathPrependIndex("2");
						enablePrefix.setSetcriteriaAspathPrependName("4755");
					}
					policyCriteriaRepository.saveAndFlush(enablePrefix);
				}
			}
		}
		PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setPolicyType(policyType);
		enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
		policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
		policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
	}

	private void enableLocalPreference(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			Map<String, String> map) {
		if (!map.get(AceConstants.ROUTER.ROUTER_MAKE).equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)
				&& map.containsKey(AceConstants.SERVICE.REDUNDACY_ROLE)
				&& map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)) {
			PolicyCriteria enableLocalPref = new PolicyCriteria();
			if (map.get(AceConstants.ROUTER.ROUTER_MAKE).equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
				enableLocalPref.setTermName("10");
				enableLocalPref.setTermSetcriteriaAction("ACCEPT");

			}
			enableLocalPref.setSetcriteriaLocalPreference((byte) 1);
			enableLocalPref.setSetcriteriaLocalpreferenceName("200");
			enableLocalPref.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enableLocalPref.setModifiedBy(MODIFIEDBY);
			policyCriteriaRepository.save(enableLocalPref);
			PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
			enablePrefixCriteriaMapping.setPolicyCriteriaId(enableLocalPref.getPolicyCriteriaId());
			enablePrefixCriteriaMapping.setVersion(1);
			enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
			policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
			policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
		}
	}

	private void enableProtocalPolicyCriteria(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			PolicyType policyType, Map<String, String> map) {
		PolicyCriteria enableProtocal = new PolicyCriteria();
		enableProtocal.setMatchcriteriaProtocol((byte) 1);
		if (map.get(AceConstants.ROUTER.ROUTER_MAKE).equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			enableProtocal.setTermName("10");
			enableProtocal.setTermSetcriteriaAction("ACCEPT");

		}
		enableProtocal.setPolicycriteriaProtocols(createPolicyProtocalForBgp(enableProtocal));
		enableProtocal.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enableProtocal.setModifiedBy(MODIFIEDBY);
		policyCriteriaRepository.save(enableProtocal);
		PolicyTypeCriteriaMapping enableProtocalCriteriaMapping = new PolicyTypeCriteriaMapping();
		enableProtocalCriteriaMapping.setPolicyCriteriaId(enableProtocal.getPolicyCriteriaId());
		enableProtocalCriteriaMapping.setPolicyType(policyType);
		enableProtocalCriteriaMapping.setVersion(1);
		enableProtocalCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enableProtocalCriteriaMapping.setModifiedBy(MODIFIEDBY);
		enableProtocalCriteriaMapping.setPolicyType(policyType);
		policyTypeCriteriaMappingRepository.save(enableProtocalCriteriaMapping);
		policyTypeCriteriaMappings.add(enableProtocalCriteriaMapping);
	}

	private Set<PrefixlistConfig> createPrefixConfig(PolicyCriteria enablePrefix, ServiceDetail serviceDetails,
			String routerMake) {
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();
		IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

		if (ipAddressDetail != null) {
			if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {
				for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
					PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
					prefixlistConfig.setNetworkPrefix(
							ipaddrLanv4Address.getLanv4Address() != null ? ipaddrLanv4Address.getLanv4Address().trim()
									: ipaddrLanv4Address.getLanv4Address());
					prefixlistConfig.setPolicyCriteria(enablePrefix);

					if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
						prefixlistConfig.setAction("PERMIT");

					} else {
						prefixlistConfig.setAction("EXACT");
					}
					prefixlistConfig.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					prefixlistConfig.setModifiedBy(MODIFIEDBY);
					prefixlistConfigRepository.save(prefixlistConfig);
					prefixlistConfigs.add(prefixlistConfig);

				}
			}

		}

		return prefixlistConfigs;
	}

	private Set<PrefixlistConfig> createPrefixConfigV6(PolicyCriteria enablePrefix, ServiceDetail serviceDetails,
			String routerMake) {
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();
		IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

		if (ipAddressDetail != null) {
			if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {
				for (IpaddrLanv6Address ipaddrLanv6Address : ipAddressDetail.getIpaddrLanv6Addresses()) {
					PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
					prefixlistConfig.setNetworkPrefix(
							ipaddrLanv6Address.getLanv6Address() != null ? ipaddrLanv6Address.getLanv6Address().trim()
									: ipaddrLanv6Address.getLanv6Address());
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

		return prefixlistConfigs;
	}

	private Set<PrefixlistConfig> createInboundV4PrefixConfigForIZOPC(PolicyCriteria enablePrefix,
			ServiceDetail serviceDetails, String routerMake) {
		logger.info("createInboundV4PrefixConfigForIZOPC for Service Id::{}", serviceDetails.getServiceId());
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();
		ScComponentAttribute prefixForCspProvidedByCustomerAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						serviceDetails.getScServiceDetailId(), "prefixForCspProvidedByCustomer", "LM", "A");
		if (prefixForCspProvidedByCustomerAttribute != null
				&& prefixForCspProvidedByCustomerAttribute.getAttributeValue() != null
				&& "Yes".equalsIgnoreCase(prefixForCspProvidedByCustomerAttribute.getAttributeValue())) {
			logger.info("prefixForCspProvidedByCustomerAttribute for Service Id::{}", serviceDetails.getServiceId());
			ScComponentAttribute prefixForCspAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetails.getScServiceDetailId(), "prefixForCsp", "LM", "A");
			if (prefixForCspAttribute != null && prefixForCspAttribute.getAttributeValue() != null) {
				logger.info("createInboundV4PrefixConfigForIZOPC for Service Id::{} with PrefixForCsp::{}",
						serviceDetails.getServiceId(), prefixForCspAttribute.getAttributeValue());
				String prefixForCsps[] = prefixForCspAttribute.getAttributeValue().split(",");
				for (String prefixForCsp : prefixForCsps) {
					PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
					prefixlistConfig.setNetworkPrefix(prefixForCsp);
					prefixlistConfig.setPolicyCriteria(enablePrefix);
					if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
						prefixlistConfig.setAction("PERMIT");

					} else {
						prefixlistConfig.setAction("EXACT");
					}
					prefixlistConfig.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					prefixlistConfig.setModifiedBy(MODIFIEDBY);
					prefixlistConfigRepository.save(prefixlistConfig);
					prefixlistConfigs.add(prefixlistConfig);
				}
			}
		}
		return prefixlistConfigs;
	}

	private Set<PrefixlistConfig> createOutboundV4PrefixConfigForIZOPC(PolicyCriteria enablePrefix,
			ServiceDetail serviceDetails, String routerMake) {
		logger.info("createOutboundV4PrefixConfigForIZOPC for Service Id::{}", serviceDetails.getServiceId());
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();
		ScComponentAttribute prefixFromMplsToCspProvidedByCustomerAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						serviceDetails.getScServiceDetailId(), "prefixFromMplsToCspProvidedByCustomer", "LM", "A");
		if (prefixFromMplsToCspProvidedByCustomerAttribute != null
				&& prefixFromMplsToCspProvidedByCustomerAttribute.getAttributeValue() != null
				&& "Yes".equalsIgnoreCase(prefixFromMplsToCspProvidedByCustomerAttribute.getAttributeValue())) {
			logger.info("prefixFromMplsToCspProvidedByCustomerAttribute for Service Id::{}",
					serviceDetails.getServiceId());
			ScComponentAttribute prefixFromMplsToCspAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceDetails.getScServiceDetailId(), "prefixFromMplsToCsp", "LM", "A");
			if (prefixFromMplsToCspAttribute != null && prefixFromMplsToCspAttribute.getAttributeValue() != null) {
				logger.info("createOutboundV4PrefixConfigForIZOPC for Service Id::{} with PrefixForMplstoCsp::{}",
						serviceDetails.getServiceId(), prefixFromMplsToCspAttribute.getAttributeValue());
				String prefixForCsps[] = prefixFromMplsToCspAttribute.getAttributeValue().split(",");
				for (String prefixForCsp : prefixForCsps) {
					PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
					prefixlistConfig.setNetworkPrefix(prefixForCsp);
					prefixlistConfig.setPolicyCriteria(enablePrefix);
					if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)) {
						prefixlistConfig.setAction("PERMIT");

					} else {
						prefixlistConfig.setAction("EXACT");
					}
					prefixlistConfig.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					prefixlistConfig.setModifiedBy(MODIFIEDBY);
					prefixlistConfigRepository.save(prefixlistConfig);
					prefixlistConfigs.add(prefixlistConfig);
				}
			}
		}
		return prefixlistConfigs;
	}

	private Set<PolicycriteriaProtocol> createPolicyProtocalForBgp(PolicyCriteria enableProtocal) {

		Set<PolicycriteriaProtocol> policycriteriaProtocols = new HashSet<>();
		PolicycriteriaProtocol policycriteriaProtocol = new PolicycriteriaProtocol();
		policycriteriaProtocol.setProtocolName("bgp");
		policycriteriaProtocol.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policycriteriaProtocol.setModifiedBy(MODIFIEDBY);
		policycriteriaProtocol.setPolicyCriteria(enableProtocal);
		policycriteriaProtocolRepository.save(policycriteriaProtocol);
		policycriteriaProtocols.add(policycriteriaProtocol);
		return policycriteriaProtocols;
	}

	private String findCpeInterfaceIpv4Ethernet(InterfaceProtocolMapping cpeProtocolMapping) {

		if (cpeProtocolMapping != null && cpeProtocolMapping.getEthernetInterface() != null) {

			return cpeProtocolMapping.getEthernetInterface().getIpv4Address();
		}
		return null;

	}

	public Set<WanStaticRoute> saveWanStaticRoutes(ServiceDetail serviceDetail, RouterDetail routerDetail,
			StaticProtocol staticProtocol) throws TclCommonException {
		logger.info("Service Activation - saveWanStaticRoutes - started");
		Set<WanStaticRoute> wanStaticRoutes = new HashSet<>();
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
							wanStaticRoute
									.setRegionalCommunity(mstRegionalPopCommunityList.get(0).getRegionalCommunity());

						}
					} else {
						wanStaticRoute.setPopCommunity(null);
						wanStaticRoute.setRegionalCommunity(null);
					}

					if (Objects.nonNull(routerDetail)
							&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
						List<MstServiceCommunity> mstServiceCommunityList = mstServiceCommunityRepository
								.findByServiceSubtype(routerDetail.getRouterHostname());
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

					wanStaticRoute.setIpsubnet(
							ipaddrLanv4Address.getLanv4Address() != null ? ipaddrLanv4Address.getLanv4Address().trim()
									: ipaddrLanv4Address.getLanv4Address());

					if (wanStaticRoute.getIpsubnet() != null) {

						List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetail
								.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
										&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
								.collect(Collectors.toList());

						cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());

						if (cpeProtocolMappings != null && !cpeProtocolMappings.isEmpty()) {
							InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);

							wanStaticRoute.setNextHopid(findCpeInterfaceIpv4Ethernet(cpeProtocolMapping));
						}

					}

					wanStaticRoute.setGlobal((byte) 0);
					wanStaticRoute.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					wanStaticRoute.setModifiedBy(MODIFIEDBY);
					wanStaticRouteRepository.save(wanStaticRoute);

					wanStaticRoutes.add(wanStaticRoute);

				}
			}

			for (IpaddrLanv6Address ipaddrLanv6Address : ipAddressDetail.getIpaddrLanv6Addresses()) {
				WanStaticRoute wanStaticRoute = new WanStaticRoute();

				if (Objects.nonNull(routerDetail)
						&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {

					List<MstRegionalPopCommunity> mstRegionalPopCommunityList = mstRegionalPopCommunityRepository
							.findByRouterHostname(routerDetail.getRouterHostname());
					if (!mstRegionalPopCommunityList.isEmpty()) {
						wanStaticRoute.setPopCommunity(mstRegionalPopCommunityList.get(0).getPopCommunity());
						wanStaticRoute.setRegionalCommunity(mstRegionalPopCommunityList.get(0).getRegionalCommunity());

					}
				} else {
					wanStaticRoute.setPopCommunity(null);
					wanStaticRoute.setRegionalCommunity(null);
				}

				if (Objects.nonNull(routerDetail)
						&& routerDetail.getRouterMake().equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
					List<MstServiceCommunity> mstServiceCommunityList = mstServiceCommunityRepository
							.findByServiceSubtype(routerDetail.getRouterHostname());
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

				wanStaticRoute.setIpsubnet(
						ipaddrLanv6Address.getLanv6Address() != null ? ipaddrLanv6Address.getLanv6Address().trim()
								: ipaddrLanv6Address.getLanv6Address());

				if (wanStaticRoute.getIpsubnet() != null) {
					InterfaceProtocolMapping cpeProtocolMapping = serviceDetail.getInterfaceProtocolMappings().stream()
							.filter(serIn -> serIn.getCpe() != null && serIn.getIscpeWanInterface() == 1).findFirst()
							.orElse(null);
					wanStaticRoute.setNextHopid(findCpeInterfaceIpv6Ethernet(cpeProtocolMapping));

				}

				wanStaticRoute.setGlobal((byte) 0);
				wanStaticRoute.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				wanStaticRoute.setModifiedBy(MODIFIEDBY);
				wanStaticRouteRepository.save(wanStaticRoute);

				wanStaticRoutes.add(wanStaticRoute);

			}

		}
		return wanStaticRoutes;

	}

	private void createRadwinSolutionRecord(VpnMetatData vpnMetatData, ServiceDetail serviceDetails,
			String custiomerName) {
		VpnSolution vpnSolution = new VpnSolution();
		vpnSolution.setVpnName("VSNL_RF_Mgmt_VPN");
		vpnSolution.setLegRole("MGMT-SPOKE");
		vpnSolution.setVpnTopology("MGMT");
		vpnSolution.setVpnLegId(String.valueOf(vpnMetatData.getVpnLegId()));
		vpnSolution.setSiteId(serviceDetails.getServiceId());
		vpnSolution.setVpnType("MANAGEMENT");
		vpnSolution.setModifiedBy(MODIFIEDBY);
		vpnSolution.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		vpnSolution.setServiceDetail(serviceDetails);
		vpnSolutionRepository.save(vpnSolution);
	}

	private void createGreySolutionRecord(VpnMetatData vpnMetatData, ServiceDetail serviceDetails,
			String custiomerName) {
		VpnSolution vpnSolution = new VpnSolution();

		vpnSolution.setLegRole("MGMT-SPOKE");
		vpnSolution.setVpnTopology("MGMT");
		vpnSolution.setSiteId(serviceDetails.getServiceId());
		vpnSolution.setVpnType("MANAGEMENT");
		vpnSolution.setVpnLegId(String.valueOf(vpnMetatData.getVpnLegId()));
		vpnSolution.setServiceDetail(serviceDetails);
		vpnSolution.setVpnName("TATA_VSNL_BROADBAND_grey_mgmt_vpn");
		vpnSolution.setInstanceId(serviceDetails.getNetpRefid());
		vpnSolution.setModifiedBy(MODIFIEDBY);
		vpnSolution.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		vpnSolution.setServiceDetail(serviceDetails);
		vpnSolutionRepository.save(vpnSolution);
	}

	private void createCustomerSolutionRecord(VpnMetatData vpnMetatData, ServiceDetail serviceDetails,
			String custiomerName) {
		VpnSolution vpnSolution = new VpnSolution();

		if (vpnMetatData.getL2OTopology().equalsIgnoreCase("Full Mesh")) {
			vpnSolution.setLegRole("FULL-MESH");
			vpnSolution.setVpnTopology("MESH");

		}

		if (vpnMetatData.getL2OTopology().equalsIgnoreCase("Hub & spoke")) {
			vpnSolution.setLegRole(vpnMetatData.getL2oSiteRole());// need to take from site topology metadata
			vpnSolution.setVpnTopology("HUBnSPOKE");
		}
		vpnSolution.setVpnType("CUSTOMER");
		vpnSolution.setVpnLegId(String.valueOf(vpnMetatData.getVpnLegId()));
		vpnSolution.setSiteId(serviceDetails.getServiceId());
		vpnSolution.setVpnName(vpnMetatData.getVpnName());
		vpnSolution.setVpnSolutionName(vpnMetatData.getVpnSolutionName());
		vpnSolution.setModifiedBy(MODIFIEDBY);
		vpnSolution.setServiceDetail(serviceDetails);
		vpnSolution.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		vpnSolution.setServiceDetail(serviceDetails);
		vpnSolutionRepository.save(vpnSolution);
	}

	private VpnMetatData applyVpnMetata(ServiceDetail serviceDetails, Map<String, String> manDatoryFields) {

		VpnMetatData vpnMetatData = serviceDetails.getVpnMetatDatas().stream().findFirst().orElse(null);
		String lmComponentTyp = manDatoryFields.get(AceConstants.LMCOMPONENT.LMCOMPONENTTYPE);

		if (vpnMetatData != null) {
			logger.info("applyVpnMetata Rule engine");
			vpnMetatData.setSolutionId(serviceDetails.getSolutionId());

			String alias = "";

			if (vpnMetatData.getL2OTopology().equalsIgnoreCase("Full Mesh")) {
				alias = "M";// hs, if Full mesh M,if par mesh the PM

			} else if (vpnMetatData.getL2OTopology().equalsIgnoreCase("Partial Mesh")) {
				alias = "PM";// hs, if Full mesh M,if par mesh the PM

			}

			else if (vpnMetatData.getL2OTopology().equalsIgnoreCase("Hub & Spoke")) {
				alias = "HS";// hs, if Full mesh M,if par mesh the PM

			}
			String custiomerName = serviceDetails.getOrderDetail().getCustomerName();// 6,6,4 word only space to be

			if (serviceDetails.getMgmtType().equalsIgnoreCase("MANAGED")) {
				vpnMetatData.setManagementVpnType1("GREY");
			}

			if (serviceDetails.getLastmileProvider() != null && !serviceDetails.getLastmileProvider().isEmpty()
					&& serviceDetails.getLastmileProvider().toLowerCase().contains("radwin")
					&& serviceDetails.getLastmileProvider().toLowerCase().contains("tcl")
					&& serviceDetails.getLastmileProvider().toLowerCase().contains("pop")) {
				logger.info("Set managementVpnType2 as RADWIN for radwin p2p");
				vpnMetatData.setManagementVpnType2("RADWIN");
			}
			String customeName = null;

			if (Objects.nonNull(serviceDetails.getOrderDetail())) {

				if (serviceDetails.getOrderDetail().getProjectName() == null || org.apache.commons.lang3.StringUtils
						.isBlank(serviceDetails.getOrderDetail().getProjectName())) {

					customeName = splitCustomerName(serviceDetails.getOrderDetail().getCustomerName());
				} else {
					customeName = splitCustomerName63(serviceDetails.getOrderDetail().getCustomerName());

				}
				String vpnName = splitProjectName(customeName, serviceDetails.getOrderDetail().getProjectName());
				if (serviceDetails.getOrderDetail().getExtOrderrefId().toLowerCase().contains("izosdwan")) {
					vpnName = vpnName + "_SDWAN";
				}
				vpnMetatData.setVpnSolutionName(vpnName);
				serviceDetails.setSolutionId(vpnName);
				serviceDetailRepository.save(serviceDetails);
				vpnMetatData.setVpnName(vpnName + "_" + alias);// need to handle project name in future
			}

			// solution name have to replace _

			vpnMetatData.setVpnAlias(vpnMetatData.getVpnName());
			vpnMetatData.setModifiedBy(MODIFIEDBY);
			vpnMetatDataRepository.saveAndFlush(vpnMetatData);
			if (!serviceDetails.getOrderDetail().getExtOrderrefId().toLowerCase().contains("izosdwan")
					&& Objects.nonNull(vpnMetatData.getVpnName()) && !vpnMetatData.getVpnName().isEmpty()
					&& vpnMetatData.getVpnName().equalsIgnoreCase("STATE_BANK")) {
				logger.info("Set managementVpnType1 as NULL for STATE_BANK");
				vpnMetatData.setManagementVpnType1(null);
			}
			if (Objects.nonNull(serviceDetails.getLastmileProvider()) && !serviceDetails.getLastmileProvider().isEmpty()
					&& serviceDetails.getLastmileProvider().toLowerCase().contains("radwin")
					&& serviceDetails.getLastmileProvider().toLowerCase().contains("pmp")) {
				logger.info("Set managementVpnType2 as NULL for radwin p2p");
				vpnMetatData.setManagementVpnType2(null);
			}
			vpnMetatData.setVpnLegId(10000 + vpnMetatData.getId());
			vpnMetatDataRepository.saveAndFlush(vpnMetatData);

		}
		return vpnMetatData;
	}

}
