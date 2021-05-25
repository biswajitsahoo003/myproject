package com.tcl.dias.serviceactivation.rule.engine.macd.service;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
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
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.serviceactivation.activation.constants.AceConstants;
import com.tcl.dias.serviceactivation.activation.utils.ActivationUtils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
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
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.MstGvpnAluCustId;
import com.tcl.dias.serviceactivation.entity.entities.MstRegionalPopCommunity;
import com.tcl.dias.serviceactivation.entity.entities.MstServiceCommunity;
import com.tcl.dias.serviceactivation.entity.entities.MstVpnSamManagerId;
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
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
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstGvpnAluCustIdRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRegionalPopCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstServiceCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstVpnSamManagerIdRepository;
import com.tcl.dias.serviceactivation.entity.repository.MulticastingRepository;
import com.tcl.dias.serviceactivation.entity.repository.NeighbourCommunityConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.OspfRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeCriteriaMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicycriteriaProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceCosCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceQoRepository;
import com.tcl.dias.serviceactivation.entity.repository.UniswitchDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnMetatDataRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.VrfRepository;
import com.tcl.dias.serviceactivation.entity.repository.WanStaticRouteRepository;
import com.tcl.dias.serviceactivation.macd.AceRule;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.StringUtils;

@Service
public class GVPNMACDRuleEngineService extends AceBaseMACDRuleEngine implements IMACDRuleEngine {

	private static final Logger logger = LoggerFactory.getLogger(GVPNMACDRuleEngineService.class);

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
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	private AceRule aceRule;
	
	@Autowired
	private IpaddrLanv4AddressRepository ipaddrLanv4AddressRepository;
	
	@Autowired
	private IpaddrLanv6AddressRepository ipaddrLanv6AddressRepository;
	
	@Autowired 
	private IpaddrWanv4AddressRepository ipaddrWanv4AddressRepository;
	
	@Autowired
	private IpaddrWanv6AddressRepository ipaddrWanv6AddressRepository;
	
	@Autowired
	private CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	private ServiceCosCriteriaRepository serviceCosCriteriaRepository;
	
	@Autowired
	private ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	private MulticastingRepository multicastingRepository;
	
	@Autowired
	private NeighbourCommunityConfigRepository neighbourCommunityConfigRepository;
	
	@Autowired
	private ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	private ServiceActivationService serviceActivationService;
	
	@Value("${cramer.source.system}")
	String cramerSourceSystem;
	
	public boolean applyRule(ServiceDetail currentActiveServiceDetail) throws TclCommonException {
		boolean mandatoryFields = true;

		try {
			// Get PrevActiveServiceDetail based on Order Type condition
			String orderType=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderType(currentActiveServiceDetail, currentActiveServiceDetail.getOrderDetail());
			logger.info("GVPN MACD Order Type {}", orderType);
			ServiceDetail prevActiveServiceDetail =null;
			if(!orderType.equalsIgnoreCase("NEW")){
				prevActiveServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(currentActiveServiceDetail.getServiceId(),TaskStatusConstants.ACTIVE);
			}
			if(Objects.nonNull(prevActiveServiceDetail) 
					&& Objects.nonNull(currentActiveServiceDetail)){
				applyEthernetDetails(currentActiveServiceDetail);
				applyIpAddressRule(currentActiveServiceDetail,prevActiveServiceDetail);
				Map<String, String> prevManDatoryFields = findMandatoryValues(prevActiveServiceDetail.getOrderDetail(), prevActiveServiceDetail);
				Map<String, String> currentManDatoryFields = findMandatoryValues(currentActiveServiceDetail.getOrderDetail(), currentActiveServiceDetail);
				if(Objects.isNull(currentManDatoryFields.get(AceConstants.VRF.VRF_NAME))){
					currentManDatoryFields.put(AceConstants.VRF.VRF_NAME, "NOT_APPLICABLE");
				}			
				logger.info("Prev Mandatory fields {}",prevManDatoryFields);
				logger.info("Current Mandatory fields {}",currentManDatoryFields);
				applyIpAddressRule(currentActiveServiceDetail, currentManDatoryFields,prevActiveServiceDetail);
				OrderDetail currentOrderDetail = currentActiveServiceDetail.getOrderDetail();
				VpnMetatData vpnMetatData = applyVpnMetata(currentActiveServiceDetail, currentManDatoryFields);
				ScComponentAttribute scComponentChangedVrfAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								currentActiveServiceDetail.getScServiceDetailId(), "changedVRFName", "LM", "A");
				if(scComponentChangedVrfAttribute!=null && scComponentChangedVrfAttribute.getAttributeValue()!=null && !scComponentChangedVrfAttribute.getAttributeValue().isEmpty()){
					logger.info("GVPNMACD.VRF Name by Changed VrfName::{}",scComponentChangedVrfAttribute.getAttributeValue());
					String changedVrfName=scComponentChangedVrfAttribute.getAttributeValue();
					vpnMetatData.setVpnName(changedVrfName);
					vpnMetatData.setVpnAlias(changedVrfName);
					vpnMetatData.setSolutionId(changedVrfName);
					vpnMetatData.setVpnSolutionName(changedVrfName);
					vpnMetatDataRepository.saveAndFlush(vpnMetatData);
					if(Objects.nonNull(vpnMetatData.getVpnName()) && !vpnMetatData.getVpnName().isEmpty() && vpnMetatData.getVpnName().equalsIgnoreCase("STATE_BANK")){
						logger.info("Set managementVpnType1 as NULL for STATE_BANK");
						vpnMetatData.setManagementVpnType1(null);
					}
					vpnMetatDataRepository.saveAndFlush(vpnMetatData);
					currentActiveServiceDetail.setSolutionId(changedVrfName);
					serviceDetailRepository.save(currentActiveServiceDetail);
				}else if(!currentActiveServiceDetail.getOrderDetail().getExtOrderrefId().toLowerCase().contains("izosdwan")){
					logger.info("GVPNMACD.Changed VrfName not exists for Service Id::{}",currentActiveServiceDetail.getScServiceDetailId());
					applyVpnMetataDetailRule(currentActiveServiceDetail,prevActiveServiceDetail);
				}
				applyServiceDetailsSolutionIdRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applyOrderRule(currentOrderDetail, currentActiveServiceDetail, currentManDatoryFields,prevActiveServiceDetail,vpnMetatData);
				applyVPnSolution(currentActiveServiceDetail,prevActiveServiceDetail, currentManDatoryFields, vpnMetatData);
				applyServiceDetailsRule(currentActiveServiceDetail,prevActiveServiceDetail, currentManDatoryFields,vpnMetatData);
				List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
						.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
						.collect(Collectors.toList());
				List<InterfaceProtocolMapping> prevRouterInterfaceProtocolMappings = prevActiveServiceDetail.getInterfaceProtocolMappings()
						.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
						.collect(Collectors.toList());
				if (!routerInterfaceProtocolMappings.isEmpty() && !prevRouterInterfaceProtocolMappings.isEmpty()) {
					logger.info("Current & Prev Router Protocol not empty");
					routerInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
					InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings.get(0);
					prevRouterInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
					InterfaceProtocolMapping prevRouterInterfaceProtocolMapping = prevRouterInterfaceProtocolMappings.get(0);
					if (Objects.nonNull(routerInterfaceProtocolMapping)) {
						logger.info("Current Router Protocol exists");
						applyIfRouterProtocolModified(prevManDatoryFields,currentManDatoryFields,currentActiveServiceDetail,routerInterfaceProtocolMapping,prevRouterInterfaceProtocolMapping);
					}
				}
			}
			if(Objects.nonNull(currentActiveServiceDetail)){
				List<InterfaceProtocolMapping> cpeProtocolMappings = currentActiveServiceDetail
						.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
								&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
						.collect(Collectors.toList());
				Map<String, String> currentActiveManDatoryFields = findMandatoryValues(currentActiveServiceDetail.getOrderDetail(), currentActiveServiceDetail);
				if (!cpeProtocolMappings.isEmpty()) {
					cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
					InterfaceProtocolMapping cpeInterfaceProtocolMapping = cpeProtocolMappings.get(0);
					if (Objects.nonNull(cpeInterfaceProtocolMapping)) {
						applyInterfaceRuleForCESide(cpeInterfaceProtocolMapping,currentActiveServiceDetail,currentActiveManDatoryFields);
					}
				}
				applyUinRule(currentActiveServiceDetail,currentActiveManDatoryFields);
				applyVrf(currentActiveServiceDetail,currentActiveManDatoryFields);
				applyCPEForPeSide(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyServiceQos(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyALUpolicyRule(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyInterfaceRule(currentActiveServiceDetail,currentActiveManDatoryFields,prevActiveServiceDetail);
				applyRoterDetails(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyOspfRule(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyRFRule(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyCESideRule(currentActiveServiceDetail, currentActiveManDatoryFields);
				applyVrfDetailRule(currentActiveServiceDetail,prevActiveServiceDetail);
			}
			//Migrated Order changes
			if(Objects.nonNull(prevActiveServiceDetail) 
					&& Objects.nonNull(currentActiveServiceDetail)){				
				OrderDetail currentOrderDetail = currentActiveServiceDetail.getOrderDetail();
				applyAluSvcDetailRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applyOrderDetailRule(currentOrderDetail,prevActiveServiceDetail);
				applyCpeDetailsRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applyIPAddressDetailsRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applySvcDetailRule(currentActiveServiceDetail,prevActiveServiceDetail);
				Map<String, String> currentManDatoryFields = findMandatoryValues(
						currentActiveServiceDetail.getOrderDetail(), currentActiveServiceDetail);
				if(!CollectionUtils.isEmpty(currentActiveServiceDetail.getVrfs())){
					if (Objects.nonNull(currentManDatoryFields) && currentManDatoryFields.containsKey(AceConstants.ROUTER.ROUTER_MAKE)) {
						createPoliciesForVrf(currentActiveServiceDetail.getVrfs().stream().findFirst().get(),currentActiveServiceDetail, currentManDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE));
					}
				}
				applyCosRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applyInterfaceDetailsRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applyCEEthernetIpv4Rule(currentActiveServiceDetail,currentManDatoryFields);
				applyMultiCastRule(currentActiveServiceDetail,prevActiveServiceDetail);
				applyVrfDetailRuleForCisco(currentActiveServiceDetail,prevActiveServiceDetail);
				applyOspfDetailsRule(currentActiveServiceDetail, prevActiveServiceDetail);
			}
			if(Objects.nonNull(currentActiveServiceDetail)){
				boolean isDowntimeRequired=aceRule.isDowntimeRequired(currentActiveServiceDetail.getServiceId());
				boolean isPortChanged=aceRule.isPortChanged(currentActiveServiceDetail.getServiceId());
				currentActiveServiceDetail.setIsdowntimeReqd(isDowntimeRequired == true ? (byte) 1 : (byte) 0);
				OrderDetail orderDetail=currentActiveServiceDetail.getOrderDetail();
				String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(currentActiveServiceDetail, orderDetail);
				String orderSubCategory=currentActiveServiceDetail.getOrderSubCategory();
				orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
				orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
				if(isPortChanged){
					currentActiveServiceDetail.setIsdowntimeReqd((byte) 1);
				}else{
					Map<String, String> currentManDatoryFields = findMandatoryValues(
							currentActiveServiceDetail.getOrderDetail(), currentActiveServiceDetail);
					logger.info("Port Change :False");
					if(Objects.nonNull(prevActiveServiceDetail) && currentManDatoryFields.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED)
							&& !currentManDatoryFields.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("true")
							&& Objects.nonNull(currentActiveServiceDetail.getOrderDetail()) && Objects.nonNull(currentActiveServiceDetail.getOrderCategory())
							&& ("CHANGE_BANDWIDTH".equalsIgnoreCase(currentActiveServiceDetail.getOrderCategory()) 
									||  "SHIFT_SITE".equalsIgnoreCase(currentActiveServiceDetail.getOrderCategory()))){
						logger.info("Reverse Ip Condition satisfied");
						applyReverseIpRule(currentActiveServiceDetail,prevActiveServiceDetail,currentManDatoryFields);
					}
				}
				currentActiveServiceDetail.setIsportChanged(isPortChanged == true ? (byte) 1 : (byte) 0);
				currentActiveServiceDetail.setSkipDummyConfig((byte)0);
				
				if(Objects.nonNull(currentActiveServiceDetail.getOrderSubCategory()) && !currentActiveServiceDetail.getOrderSubCategory().isEmpty()
						&& (currentActiveServiceDetail.getOrderSubCategory().contains("lm")
								|| currentActiveServiceDetail.getOrderSubCategory().contains("bso")
								|| currentActiveServiceDetail.getOrderSubCategory().equalsIgnoreCase("Shifting"))){
					logger.info("Downtime set for BSO related order sub category");
					currentActiveServiceDetail.setIsdowntimeReqd((byte) 1);
				}
				serviceDetailRepository.saveAndFlush(currentActiveServiceDetail);
				
				logger.info("isIpDownTimeRequired::{}",currentActiveServiceDetail.getIsdowntimeReqd());
				Map<String, String> downTimeDetailMap = new HashMap<>();
				downTimeDetailMap.put("isIpDownTimeRequired", currentActiveServiceDetail.getIsdowntimeReqd()==1? "true":"false");
				componentAndAttributeService.updateAttributes(currentActiveServiceDetail.getScServiceDetailId(), downTimeDetailMap, "LM","A");
				
				if(isPortChanged){
					List<InterfaceProtocolMapping> currentRouterInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
							.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
							.collect(Collectors.toList());
					if (!currentRouterInterfaceProtocolMappings.isEmpty()) {
						logger.info("Current Router Protocol not empty");
						currentRouterInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
						InterfaceProtocolMapping currentRouterInterfaceProtocolMapping = currentRouterInterfaceProtocolMappings.get(0);
						if (Objects.nonNull(currentRouterInterfaceProtocolMapping) && Objects.nonNull(currentRouterInterfaceProtocolMapping.getBgp())
								&& Objects.nonNull(currentRouterInterfaceProtocolMapping.getBgp().getPolicyTypes()) && !currentRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().isEmpty()) {
							logger.info("Bgp Policy Type exists");
							Set<PolicyType> policyTypeSet=currentRouterInterfaceProtocolMapping.getBgp().getPolicyTypes();
							policyTypeSet.stream().forEach(policyType ->{
								logger.info("Update Bgp Prev prov");
								policyType.setInboundIpv4Preprovisioned((byte)0);
								policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
								policyType.setModifiedBy(MODIFIEDBY);
								policyTypeRepository.save(policyType);
							});
						}						
						if (Objects.nonNull(currentRouterInterfaceProtocolMapping) && Objects.nonNull(currentRouterInterfaceProtocolMapping.getBgp())) {
							logger.info("Update Bgp PeerName for service Id:{} due to port change", currentActiveServiceDetail.getId());
							Integer cssSammgrId = currentActiveServiceDetail.getCssSammgrId();
							String serviceCode=currentActiveServiceDetail.getServiceId();
							String code = serviceCode.substring(3, serviceCode.length());
							Bgp bgp=currentRouterInterfaceProtocolMapping.getBgp();
							bgp.setBgpPeerName(
									splitName(currentActiveServiceDetail.getOrderDetail().getCustomerName(), 4) + "_" + cssSammgrId + "_" + code);
							bgpRepository.saveAndFlush(bgp);
						}
					}
				}
				
				if ("GVPN".equalsIgnoreCase(currentActiveServiceDetail.getServiceType()) && !"IZOPC".equalsIgnoreCase(currentActiveServiceDetail.getProductName())) {
					logger.info("GVPN Service ID::{}", currentActiveServiceDetail.getId());
					List<InterfaceProtocolMapping> currentInterfaceProtocolMappings = currentActiveServiceDetail
							.getInterfaceProtocolMappings().stream()
							.filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
							.collect(Collectors.toList());
					currentInterfaceProtocolMappings
							.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
					InterfaceProtocolMapping currentInterfaceProtocolMapping = currentInterfaceProtocolMappings.get(0);
					if (Objects.nonNull(currentInterfaceProtocolMapping)
							&& Objects.nonNull(currentInterfaceProtocolMapping.getBgp())
							&& Objects.nonNull(currentActiveServiceDetail.getVpnMetatDatas())) {
						logger.info("GVPN Service ID with ipm and vpn metadata exists::{}",currentActiveServiceDetail.getId());
						Optional<VpnMetatData> currentVpnMetaDataOptional = currentActiveServiceDetail.getVpnMetatDatas().stream().findFirst();
						if (currentVpnMetaDataOptional.isPresent() && currentVpnMetaDataOptional.get().getVpnName()!=null) {
							VpnMetatData currentVpnMetaData = currentVpnMetaDataOptional.get();
							logger.info("GVPN Service ID with ipm and vpn name exists::{}",currentActiveServiceDetail.getId());
							MstVpnSamManagerId mstVpnSamManagerId = mstVpnmanagerIdRepository
									.findFirstByVpnNameAndBgpPasswordIsNotNull(currentVpnMetaData.getVpnName());
							if (mstVpnSamManagerId != null && mstVpnSamManagerId.getBgpPassword() != null && !mstVpnSamManagerId.getBgpPassword().isEmpty()) {
								logger.info("GVPN Service ID::{} with MstVpnSamManagerId::{} exists with password::{}",
										currentActiveServiceDetail.getId(), mstVpnSamManagerId.getId(),
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
			}
		} catch (Exception e) {
			logger.error("Exception in applyRule => {}", e);

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return mandatoryFields;

	}
	
	private void applyOspfDetailsRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) throws TclCommonException {
		logger.info("applyOspfDetailsRule method invoked for Service Id::{}", currentActiveServiceDetail.getId());
		try {
			
			List<InterfaceProtocolMapping> prevInterfaceProtocolMappings = prevActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
			
			List<InterfaceProtocolMapping> interfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
			
			

			if (!prevInterfaceProtocolMappings.isEmpty() && !interfaceProtocolMappings.isEmpty()) {
				logger.info("applyOspfDetailsRule IPM exists for Service Id::{}", currentActiveServiceDetail.getId());
				prevInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping prevInterfaceProtocolMapping = prevInterfaceProtocolMappings.get(0);
				
				interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);

				if (prevInterfaceProtocolMapping!=null && prevInterfaceProtocolMapping.getOspf()!=null && interfaceProtocolMapping != null && interfaceProtocolMapping.getOspf() != null) {
					logger.info("Prev Ospf exists and  Current Ospf exists for Service Id::{}", currentActiveServiceDetail.getId());					
					Ospf ospf = interfaceProtocolMapping.getOspf();
					if(prevInterfaceProtocolMapping.getOspf().getAreaId()!=null && interfaceProtocolMapping.getOspf().getAreaId()==null ){
						logger.info("Prev Area Id exists and  Current area Id not exists for Service Id::{}", currentActiveServiceDetail.getId());
						ospf.setAreaId(prevInterfaceProtocolMapping.getOspf().getAreaId());
						ospfRepository.saveAndFlush(ospf);
					}else{
						logger.info("Prev Area Id not exists and  Current area Id not exists for Service Id::{}", currentActiveServiceDetail.getId());
						ospf.setAreaId("0.0.0.0");
						ospfRepository.saveAndFlush(ospf);
					}	
				}
			}

		} catch (Exception e) {
			logger.error("Exception in applyOspfDetailsRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	
		
	}

	private void applyReverseIpRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail, Map<String, String> currentManDatoryFields) {
		logger.info("applyReverseIpRule");
		if(Objects.nonNull(currentActiveServiceDetail) && Objects.nonNull(prevActiveServiceDetail)
				&& Objects.nonNull(currentActiveServiceDetail.getInterfaceProtocolMappings()) && Objects.nonNull(prevActiveServiceDetail.getInterfaceProtocolMappings())){
			logger.info("Prev and Current IPM exists");
			List<InterfaceProtocolMapping> currentInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
			
			List<InterfaceProtocolMapping> prevInterfaceProtocolMappings = prevActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
			
			List<InterfaceProtocolMapping> currentCEInterfaceProtocolMappings = currentActiveServiceDetail
					.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
							&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
					.collect(Collectors.toList());
			
			if (!currentInterfaceProtocolMappings.isEmpty() && !prevInterfaceProtocolMappings.isEmpty() && !currentCEInterfaceProtocolMappings.isEmpty()) {
				logger.info("Prev and Current PE exists");
				currentInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping currentInterfaceProtocolMapping = currentInterfaceProtocolMappings.get(0);
				prevInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping prevInterfaceProtocolMapping = prevInterfaceProtocolMappings.get(0);
				currentCEInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping currentCEInterfaceProtocolMapping = currentCEInterfaceProtocolMappings.get(0);
				if (Objects.nonNull(currentInterfaceProtocolMapping) && Objects.nonNull(prevInterfaceProtocolMapping) && Objects.nonNull(currentCEInterfaceProtocolMapping)
					&& currentInterfaceProtocolMapping.getEthernetInterface() != null  && prevInterfaceProtocolMapping.getEthernetInterface() != null
					&& currentCEInterfaceProtocolMapping.getEthernetInterface() !=null) {
					logger.info("Prev and Current EthernetInterface exists");
					EthernetInterface prevEthernetInterface=prevInterfaceProtocolMapping.getEthernetInterface();
					boolean isReverseIp=false;
					isReverseIp=getReverseIpDetails(prevInterfaceProtocolMapping,prevEthernetInterface,isReverseIp);
					if(isReverseIp){
						logger.info("Reverse Ip");
						saveReverseIpDetails(currentActiveServiceDetail,currentInterfaceProtocolMapping,currentCEInterfaceProtocolMapping,prevEthernetInterface,currentManDatoryFields);
					}
				
				}
			}
		}
		
	}
	
	private void saveReverseIpDetails(ServiceDetail currentActiveServiceDetail, InterfaceProtocolMapping currentInterfaceProtocolMapping,
			InterfaceProtocolMapping currentCEInterfaceProtocolMapping, EthernetInterface prevEthernetInterface, Map<String, String> currentManDatoryFields) {
		logger.info("saveReverseIpDetails");
		String ipWanv4[]={""};
		EthernetInterface currentEthernetInterface=currentInterfaceProtocolMapping.getEthernetInterface();
		if(Objects.nonNull(prevEthernetInterface.getModifiedIpv4Address())){
			logger.info("Reverse PE Modified Ipv4::{}",prevEthernetInterface.getModifiedIpv4Address());
			currentEthernetInterface.setModifiedIpv4Address(prevEthernetInterface.getModifiedIpv4Address().trim());
		}
		if(Objects.nonNull(prevEthernetInterface.getModifiedSecondaryIpv4Address())){
			logger.info("Reverse PE Modified Sy Ipv4::{}",prevEthernetInterface.getModifiedSecondaryIpv4Address());
			currentEthernetInterface.setModifiedSecondaryIpv4Address(prevEthernetInterface.getModifiedSecondaryIpv4Address().trim());
		}
		
		if(currentEthernetInterface.getModifiedSecondaryIpv4Address()==null && currentManDatoryFields.containsKey("WANV4ADDRESS_SECONDARY")){
			logger.info("Current Reverse PE Modified Sy Ipv4 not exists");
			String secondaryWanV4Address=currentManDatoryFields.get("WANV4ADDRESS_SECONDARY");
			if(secondaryWanV4Address!=null){
				logger.info("Current Reverse PE Modified Sy Ipv4 from ::{}",secondaryWanV4Address);
				currentEthernetInterface.setModifiedSecondaryIpv4Address(getIpAddressSplit(secondaryWanV4Address,2) + "/"+ subnet(currentManDatoryFields.get("WANV4ADDRESS_SECONDARY")).trim());
			}
		}
		
		if(Objects.nonNull(prevEthernetInterface.getIpv4Address())){
			String[] ipv4=prevEthernetInterface.getIpv4Address().split("/");
			logger.info("Reverse PE Ipv4::{}",prevEthernetInterface.getIpv4Address());
			currentEthernetInterface.setIpv4Address(ipv4[0].trim());
		}
		if(Objects.nonNull(prevEthernetInterface.getSecondaryIpv4Address())){
			String[] ipv4=prevEthernetInterface.getSecondaryIpv4Address().split("/");
			logger.info("Reverse PE Sy Ipv4::{}",prevEthernetInterface.getSecondaryIpv4Address());
			currentEthernetInterface.setSecondaryIpv4Address(ipv4[0].trim());
		}
		ethernetInterfaceRepository.save(currentEthernetInterface);
		EthernetInterface currentCEEthernetInterface=currentCEInterfaceProtocolMapping.getEthernetInterface();
		if(Objects.nonNull(currentEthernetInterface.getModifiedIpv4Address())){
			String cpeModifiedIpv4=getIpAddressSplit(currentEthernetInterface.getModifiedIpv4Address(), -1);
			logger.info("Reverse CE Modified Ipv4::{}",currentEthernetInterface.getModifiedIpv4Address());
			currentCEEthernetInterface.setModifiedIpv4Address(cpeModifiedIpv4 + "/"
					+ subnet(currentManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim());
			ipWanv4[0]=getIpAddressSplit(currentEthernetInterface.getModifiedIpv4Address(),-2) + "/"+ subnet(currentManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim();
			logger.info("IpvWanV4:: {}",ipWanv4[0]);
		}
		if(Objects.nonNull(currentEthernetInterface.getModifiedSecondaryIpv4Address())){
			String cpeModifiedSyIpv4=getIpAddressSplit(currentEthernetInterface.getModifiedSecondaryIpv4Address(), -1);
			logger.info("Reverse CE Modified Sy Ipv4::{}",currentEthernetInterface.getModifiedSecondaryIpv4Address());
			currentCEEthernetInterface.setModifiedSecondaryIpv4Address(cpeModifiedSyIpv4 + "/" + subnet(currentManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)).trim());
		}
		if(Objects.nonNull(currentEthernetInterface.getIpv4Address())){
			String cpeIpv4=getIpAddressSplit(currentEthernetInterface.getIpv4Address(), -1);
			logger.info("Reverse CE Ipv4::{}",currentEthernetInterface.getIpv4Address());
			currentCEEthernetInterface.setIpv4Address(cpeIpv4);
		}
		if(Objects.nonNull(currentEthernetInterface.getSecondaryIpv4Address())){
			String cpeSyIpv4=getIpAddressSplit(currentEthernetInterface.getSecondaryIpv4Address(), -1);
			logger.info("Reverse CE Sy Ipv4::{}",currentEthernetInterface.getSecondaryIpv4Address());
			currentCEEthernetInterface.setSecondaryIpv4Address(cpeSyIpv4);
		}						
		ethernetInterfaceRepository.save(currentCEEthernetInterface);
		
		if(Objects.nonNull(currentInterfaceProtocolMapping.getBgp())){
			logger.info("Reverse Ip for Bgp");
			Bgp currentBgp=currentInterfaceProtocolMapping.getBgp();
			currentBgp.setRemoteIpv4Address(currentCEEthernetInterface.getIpv4Address());
			currentBgp.setRemoteIpv6Address(currentCEEthernetInterface.getIpv6Address());
			bgpRepository.save(currentBgp);
		}
		
		if(Objects.nonNull(currentInterfaceProtocolMapping.getStaticProtocol())
				&& Objects.nonNull(currentInterfaceProtocolMapping.getStaticProtocol().getWanStaticRoutes())
				&& !currentInterfaceProtocolMapping.getStaticProtocol().getWanStaticRoutes().isEmpty()){
			logger.info("Reverse Ip for Static");
			Optional<WanStaticRoute> wanStaticRouteOptional=currentInterfaceProtocolMapping.getStaticProtocol().getWanStaticRoutes().stream().findFirst();
			if(wanStaticRouteOptional.isPresent()){
				logger.info("WanStatic Route NextHopId updated");
				WanStaticRoute wanStaticRoute =wanStaticRouteOptional.get();
				wanStaticRoute.setNextHopid(currentCEEthernetInterface.getIpv4Address().trim());
				wanStaticRouteRepository.save(wanStaticRoute);
			}
		}
		
		 if (Objects.nonNull(currentActiveServiceDetail.getIpAddressDetails()) && !currentActiveServiceDetail.getIpAddressDetails().isEmpty()) {
	          Optional<IpAddressDetail> currentAddressDetailOptional = currentActiveServiceDetail.getIpAddressDetails().stream().findFirst();
	          if(currentAddressDetailOptional.isPresent()){
	        	  logger.info("Current Ip exists");
	        	  IpAddressDetail currentAddressDetail=currentAddressDetailOptional.get();
	        	  if(Objects.nonNull(currentAddressDetail.getIpaddrWanv4Addresses()) && !currentAddressDetail.getIpaddrWanv4Addresses().isEmpty()
	        			  && !ipWanv4[0].isEmpty()){
	        		  logger.info("Updating wanv4::{}",ipWanv4[0]);
	        		  if(!currentAddressDetail.getIpaddrWanv4Addresses().isEmpty() && currentAddressDetail.getIpaddrWanv4Addresses().size()==1){
	        			  logger.info("Updating wanv4 as count::{}",currentAddressDetail.getIpaddrWanv4Addresses().size());
	        			  currentAddressDetail.getIpaddrWanv4Addresses().stream().forEach(currentIpWanV4 ->{
		        			  currentIpWanV4.setWanv4Address(ipWanv4[0]);
		        			  currentIpWanV4.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		        			  currentIpWanV4.setModifiedBy(MODIFIEDBY);
		        			  ipaddrWanv4AddressRepository.save(currentIpWanV4);
		        		  });
	        		  }
	        	  }
	          	}
			 }
		
	}

	private boolean getReverseIpDetails(InterfaceProtocolMapping prevInterfaceProtocolMapping,
			EthernetInterface prevEthernetInterface,boolean isReverseIp) {
		logger.info("getReverseIpDetails");
		if(Objects.nonNull(prevInterfaceProtocolMapping.getBgp()) 
				&& Objects.nonNull(prevInterfaceProtocolMapping.getBgp().getRemoteIpv4Address()) 
				&& Objects.nonNull(prevEthernetInterface.getModifiedIpv4Address()) 
				&& !prevInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().isEmpty()
				&& !prevEthernetInterface.getModifiedIpv4Address().isEmpty()){
			logger.info("Prev Bgp and Modified Ipv4");
			String ethernetModifiedIpv4Address=getIpAddressSplit(prevEthernetInterface.getModifiedIpv4Address(), 0);
			String bgpRemoteIpv4Address=getIpAddressSplit(prevInterfaceProtocolMapping.getBgp().getRemoteIpv4Address(),0);
			if(!ethernetModifiedIpv4Address.equals(bgpRemoteIpv4Address)){
				logger.info("Prev Bgp and Modified Ipv4 not equal {},{}::",ethernetModifiedIpv4Address,bgpRemoteIpv4Address);
				String[] eIpv4s=ethernetModifiedIpv4Address.split("\\.");
				String[] bIpv4s=bgpRemoteIpv4Address.split("\\.");
				if(eIpv4s.length==4 && bIpv4s.length==4){
					logger.info("Prev Bgp and Modified Ipv4 length is 4");
					Integer eIpv4=Integer.valueOf(eIpv4s[3].trim());
					Integer bIpv4=Integer.valueOf(bIpv4s[3].trim());
					isReverseIp=compareIps(bIpv4,eIpv4,isReverseIp);
				}
			}
		}else if(Objects.nonNull(prevInterfaceProtocolMapping.getStaticProtocol())){
			List<WanStaticRoute> prevWanStaticList=wanStaticRouteRepository.findByStaticProtocol(prevInterfaceProtocolMapping.getStaticProtocol());
			if(Objects.nonNull(prevWanStaticList) && !prevWanStaticList.isEmpty()){
				WanStaticRoute prevWanStaticRoute=prevWanStaticList.get(0);
				if(Objects.nonNull(prevWanStaticRoute.getNextHopid()) && !prevWanStaticRoute.getNextHopid().isEmpty() 
						&& Objects.nonNull(prevEthernetInterface.getModifiedIpv4Address()) && !prevEthernetInterface.getModifiedIpv4Address().isEmpty()){
					String ethernetModifiedIpv4Address=getIpAddressSplit(prevEthernetInterface.getModifiedIpv4Address(), 0);
					String nextHopIpAddress=getIpAddressSplit(prevWanStaticRoute.getNextHopid(),0);
					if(!ethernetModifiedIpv4Address.equals(nextHopIpAddress)){
						logger.info("Prev Wan and Modified Ipv4 not equal {},{}::",ethernetModifiedIpv4Address,nextHopIpAddress);
						String[] eIpv4s=ethernetModifiedIpv4Address.split("\\.");
						String[] nIpv4s=nextHopIpAddress.split("\\.");
						if(eIpv4s.length==4 && nIpv4s.length==4){
							logger.info("Prev Wan and Modified Ipv4 length is 4");
							Integer eIpv4=Integer.valueOf(eIpv4s[3].trim());
							Integer nIpv4=Integer.valueOf(nIpv4s[3].trim());
							isReverseIp=compareIps(nIpv4,eIpv4,isReverseIp);
						}
					}
				}
			}
		}
		return isReverseIp;
	}
	
	private boolean compareIps(Integer ip1 ,Integer ip2,boolean isReverseIp){
		if(ip1<ip2){
			logger.info("compareIps::",ip1,ip2);
			isReverseIp=true;
		}
		return isReverseIp;
	}
	
	private void applyVrfDetailRuleForCisco(ServiceDetail currentActiveServiceDetail,
			ServiceDetail prevActiveServiceDetail) {
		logger.info("applyVrfDetailRuleForCisco method invoked");
		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(currentActiveServiceDetail.getRouterMake())) {
			logger.info("VRF FOR CISCO");
				Optional<Vrf> vrfOptional=currentActiveServiceDetail.getVrfs().stream().findFirst();
				if(vrfOptional.isPresent() && Objects.nonNull(prevActiveServiceDetail.getVrfs())){
					Optional<Vrf> prevVrfOptional=prevActiveServiceDetail.getVrfs().stream().findFirst();
					Vrf currentVrf=vrfOptional.get();
					if(prevVrfOptional.isPresent() && Objects.nonNull(prevVrfOptional.get().getVrfName())){
						currentVrf.setVrfName(prevVrfOptional.get().getVrfName());
					}else{
						currentVrf.setVrfName("NA");
					}
					currentVrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					currentVrf.setModifiedBy(MODIFIEDBY);
					vrfRepository.save(currentVrf);
				}
			}
		logger.info("applyVrfDetailRule method ends");
		
	}

	private void applyEthernetDetails(ServiceDetail currentActiveServiceDetail) throws TclCommonException {
		logger.info("applyEthernetDetails");
		if(Objects.nonNull(currentActiveServiceDetail)
				&& Objects.nonNull(currentActiveServiceDetail.getInterfaceProtocolMappings())){
			
			List<InterfaceProtocolMapping> currentInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
		
			
			
			if (!currentInterfaceProtocolMappings.isEmpty()) {
				currentInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping currentInterfaceProtocolMapping = currentInterfaceProtocolMappings.get(0);
				if (Objects.nonNull(currentInterfaceProtocolMapping) 
						&& currentInterfaceProtocolMapping.getEthernetInterface() != null && currentInterfaceProtocolMapping.getEthernetInterface().getInterfaceName()!=null) {
					logger.info("Interface Name::{}",currentInterfaceProtocolMapping.getEthernetInterface().getInterfaceName());
					String interfaceName=currentInterfaceProtocolMapping.getEthernetInterface().getInterfaceName();
					if(!interfaceName.contains("/")){
						logger.info("Interface Name valid");
						String interfaceSplitString[]=interfaceName.split(":|\\.");
						logger.info("interfaceSplitString::{}",interfaceSplitString.length);
						if(interfaceSplitString.length==3 &&  currentInterfaceProtocolMapping.getEthernetInterface().getInnerVlan()==null){
							logger.info("InnerVlan not exists");
							//throw new TclCommonException("InnerVlan not exists in Cramer Response"); //CRAMMER-42
						}
						if(interfaceName.contains(".") && interfaceSplitString.length==2 &&  currentInterfaceProtocolMapping.getEthernetInterface().getOuterVlan()==null){
							logger.info("OuterVlan not exists");
							//throw new TclCommonException("OuterVlan not exists in Cramer Response"); //CRAMMER-42
						}
					}					
				}
				
					if (Objects.nonNull(currentInterfaceProtocolMapping) 
							&& currentInterfaceProtocolMapping.getEthernetInterface() != null && currentInterfaceProtocolMapping.getEthernetInterface().getInterfaceName()!=null
						&& currentInterfaceProtocolMapping.getEthernetInterface().getInnerVlan() != null
						&& currentInterfaceProtocolMapping.getEthernetInterface().getOuterVlan() != null) {
					String interfaceName = currentInterfaceProtocolMapping.getEthernetInterface().getInterfaceName();
					if (!interfaceName.contains("/")) {
						logger.info("Interface Name valid");
						String interfaceSplitString[] = interfaceName.split(":|\\.");
						logger.info("Interface Name::{}",
								currentInterfaceProtocolMapping.getEthernetInterface().getInterfaceName());
						if ((interfaceName.contains(":") && interfaceName.contains("."))
								|| (interfaceName.contains(".") && interfaceSplitString.length == 3)) {
							logger.info("Contains delimiter asColon and DOT Or Only DOT with length equals 3");
							if (Objects.nonNull(interfaceSplitString[2])
									&& Integer.valueOf(interfaceSplitString[2]) != 0 && !currentInterfaceProtocolMapping
											.getEthernetInterface().getInnerVlan().equals(interfaceSplitString[2])) {
								logger.info("InnerVlan mismatch");
                                serviceActivationService.saveErrorDetails(currentActiveServiceDetail.getScServiceDetailId(), "InnerVlan mismatch", "IP1001", "enrich-service-design-ip-jeopardy");
								throw new TclCommonException("InnerVlan not exists in Cramer Response");//CRAMMER-42
							}
							if (Objects.nonNull(interfaceSplitString[1])
									&& Integer.valueOf(interfaceSplitString[1]) != 0 && !currentInterfaceProtocolMapping
											.getEthernetInterface().getOuterVlan().equals(interfaceSplitString[1])) {
								logger.info("OuterVlan mismatch");
                                serviceActivationService.saveErrorDetails(currentActiveServiceDetail.getScServiceDetailId(), "OuterVlan mismatch", "IP1001", "enrich-service-design-ip-jeopardy");
								throw new TclCommonException("OuterVlan not exists in Cramer Response");//CRAMMER-42
							}
						} else if (interfaceName.contains(".") && interfaceSplitString.length == 2) {
							logger.info("Contains delimiter as DOT and length equals 2");
							if (Objects.nonNull(interfaceSplitString[1])
									&& Integer.valueOf(interfaceSplitString[1]) != 0 && !currentInterfaceProtocolMapping
											.getEthernetInterface().getOuterVlan().equals(interfaceSplitString[1])) {
								logger.info("OuterVlan mismatch");
                                //serviceActivationService.saveErrorDetails(currentActiveServiceDetail.getScServiceDetailId(), "OuterVlan mismatch", "IP1001", "enrich-service-design-ip-jeopardy");
								//throw new TclCommonException("OuterVlan not exists in Cramer Response"); // removed on 21-12-2020
							}
						}
					}
				}
				
					
			}
		}
	}
	
	private void applyMultiCastRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("applyMultiCastRule");
		if((Objects.nonNull(currentActiveServiceDetail.getMulticastings()) && !currentActiveServiceDetail.getMulticastings().isEmpty()) 
				&& (Objects.isNull(prevActiveServiceDetail.getMulticastings()) || (Objects.nonNull(prevActiveServiceDetail.getMulticastings()) && prevActiveServiceDetail.getMulticastings().isEmpty()))){
			logger.info("Prev MultiCasting doesn't exists");
			Optional<Multicasting> currentMultiCastOptional=currentActiveServiceDetail.getMulticastings().stream().findFirst();
			if(currentMultiCastOptional.isPresent()){
				Multicasting currentMulticasting=currentMultiCastOptional.get();
				currentMulticasting.setEndDate(new Timestamp(new Date().getTime()));
				currentMulticasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
				currentMulticasting.setModifiedBy("OPTIMUS_RULE_UPDATE");
				currentMulticasting.setServiceDetail(currentActiveServiceDetail);
				logger.info("Saving MultiCasting by end dating");
				multicastingRepository.save(currentMulticasting);
			}
		}else if(Objects.nonNull(currentActiveServiceDetail.getMulticastings()) && Objects.nonNull(prevActiveServiceDetail.getMulticastings()) 
				&& !currentActiveServiceDetail.getMulticastings().isEmpty() && !prevActiveServiceDetail.getMulticastings().isEmpty()){
			logger.info("Current MultiCasting exists");
			Optional<Multicasting> currentMultiCastOptional=currentActiveServiceDetail.getMulticastings().stream().findFirst();
			Optional<Multicasting> prevMultiCastOptional=prevActiveServiceDetail.getMulticastings().stream().findFirst();
			if(currentMultiCastOptional.isPresent() && prevMultiCastOptional.isPresent()){
				logger.info("Previous MultiCasting exists");
				Multicasting currentMulticasting=currentMultiCastOptional.get();
				Multicasting prevMulticasting=prevMultiCastOptional.get();
				currentMulticasting.setDefaultMdt(prevMulticasting.getDefaultMdt());
				currentMulticasting.setDataMdt(prevMulticasting.getDataMdt());
				currentMulticasting.setWanPimMode(prevMulticasting.getWanPimMode());
				currentMulticasting.setType(prevMulticasting.getType());
				currentMulticasting.setDataMdtThreshold(prevMulticasting.getDataMdtThreshold());
				currentMulticasting.setRpLocation(prevMulticasting.getRpLocation());
				currentMulticasting.setAutoDiscoveryOption(prevMulticasting.getAutoDiscoveryOption());
				currentMulticasting.setRpAddress(prevMulticasting.getRpAddress());
				currentMulticasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
				currentMulticasting.setModifiedBy("OPTIMUS_RULE_UPDATE");
				currentMulticasting.setServiceDetail(currentActiveServiceDetail);
				logger.info("Saving MultiCasting");
				multicastingRepository.save(currentMulticasting);
			}
			
		}else if(((Objects.isNull(currentActiveServiceDetail.getMulticastings())) || 
				(Objects.nonNull(currentActiveServiceDetail.getMulticastings()) && currentActiveServiceDetail.getMulticastings().isEmpty())) 
				&& Objects.nonNull(prevActiveServiceDetail.getMulticastings())){
			logger.info("Current MultiCasting not exists");
			com.tcl.dias.serviceactivation.entity.entities.Multicasting currentMulticasting = new com.tcl.dias.serviceactivation.entity.entities.Multicasting();
			Optional<Multicasting> prevMultiCastOptional=prevActiveServiceDetail.getMulticastings().stream().findFirst();
			if(prevMultiCastOptional.isPresent()){
				logger.info("Previous MultiCasting exists");
				Multicasting prevMulticasting=prevMultiCastOptional.get();
				if(Objects.nonNull(prevMulticasting.getDefaultMdt())){
					logger.info("DefaultMdt");
					currentMulticasting.setDefaultMdt(prevMulticasting.getDefaultMdt());
				}
				if(Objects.nonNull(prevMulticasting.getDataMdt())){
					logger.info("DataMdt");
					currentMulticasting.setDataMdt(prevMulticasting.getDataMdt());
				}
				if(Objects.nonNull(prevMulticasting.getWanPimMode())){
					logger.info("PimMode");
					currentMulticasting.setWanPimMode(prevMulticasting.getWanPimMode());
				}
				if(Objects.nonNull(prevMulticasting.getType())){
					logger.info("Type");
					currentMulticasting.setType(prevMulticasting.getType());
				}
				if(Objects.nonNull(prevMulticasting.getDataMdtThreshold())){
					logger.info("DataMdtThreshold");
					currentMulticasting.setDataMdtThreshold(prevMulticasting.getDataMdtThreshold());
				}
				if(Objects.nonNull(prevMulticasting.getRpLocation())){
					logger.info("RpLocation");
					currentMulticasting.setRpLocation(prevMulticasting.getRpLocation());
				}
				if(Objects.nonNull(prevMulticasting.getAutoDiscoveryOption())){
					logger.info("AutoDiscovery");
					currentMulticasting.setAutoDiscoveryOption(prevMulticasting.getAutoDiscoveryOption());
				}
				if(Objects.nonNull(prevMulticasting.getRpAddress())){
					logger.info("RpAddress");
					currentMulticasting.setRpAddress(prevMulticasting.getRpAddress());
				}
				currentMulticasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
				currentMulticasting.setModifiedBy("OPTIMUS_RULE_UPDATE");
				currentMulticasting.setServiceDetail(currentActiveServiceDetail);
				logger.info("Saving MultiCasting");
				multicastingRepository.save(currentMulticasting);
			}
			
		}
		
		
		/*else if(Objects.nonNull(currentActiveServiceDetail.getMulticastings()) 
				&& Objects.isNull(prevActiveServiceDetail.getMulticastings())){
			logger.info("Both MultiCasting is empty");
			Optional<Multicasting> currentMultiCastOptional=currentActiveServiceDetail.getMulticastings().stream().findFirst();
			if(currentMultiCastOptional.isPresent()){
				logger.info("Current MultiCasting exists");
				com.tcl.dias.serviceactivation.entity.entities.Multicasting multiCastingEntity = new com.tcl.dias.serviceactivation.entity.entities.Multicasting();
				multiCastingEntity.setWanPimMode("SPARSE");
				multiCastingEntity.setType("STATICRP");
				multiCastingEntity.setDataMdtThreshold("1");
				multiCastingEntity.setRpLocation("CE");
				multiCastingEntity.setAutoDiscoveryOption("MDT_SAFI");
				multiCastingEntity.setLastModifiedDate(new Timestamp(new Date().getTime()));
				multiCastingEntity.setModifiedBy("OPTIMUS_RULE_CREATE");
				multiCastingEntity.setServiceDetail(currentActiveServiceDetail);
				multicastingRepository.save(multiCastingEntity);
			}
		}*/
		
		
	}


	private boolean isPathTypeChanged(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("isPathTypeChanged");
		if(Objects.nonNull(currentActiveServiceDetail.getIpAddressDetails()) && Objects.nonNull(prevActiveServiceDetail.getIpAddressDetails())
				&& !currentActiveServiceDetail.getIpAddressDetails().isEmpty() && !prevActiveServiceDetail.getIpAddressDetails().isEmpty()){
			logger.info("Both Ip Address Detail not empty");
			Optional<IpAddressDetail> currentIpAddrOptional=currentActiveServiceDetail.getIpAddressDetails().stream().findFirst();
			Optional<IpAddressDetail> prevIpAddrOptional=prevActiveServiceDetail.getIpAddressDetails().stream().findFirst();
			if(currentIpAddrOptional.isPresent() && prevIpAddrOptional.isPresent()){
				logger.info("Both Ip Address Detail exists");
				IpAddressDetail currentIpAddr=currentIpAddrOptional.get();
				IpAddressDetail prevIpAddr=prevIpAddrOptional.get();
				if(Objects.nonNull(prevIpAddr.getPathIpType()) && Objects.nonNull(currentIpAddr.getPathIpType())
						&& "IPV4".equals(prevIpAddr.getPathIpType()) && "DUALSTACK".equals(currentIpAddr.getPathIpType())){
					logger.info("IPV4 to DUAL change");
					return true;
				}
			}
		}
		return false;
	}


	private void applyIpAddressRule(ServiceDetail currentActiveServiceDetail,
            ServiceDetail prevActiveServiceDetail) {
		logger.info("applyIpAddressRule invoked");
		Set<IpAddressDetail> ipAddressSet= new HashSet<>();
     if (currentActiveServiceDetail.getIpAddressDetails() == null
                   || currentActiveServiceDetail.getIpAddressDetails().isEmpty()) {
    	  logger.info("applyIpAddressRule.CurrentIpAddress exists");
            IpAddressDetail olipAddressDetail = prevActiveServiceDetail.getIpAddressDetails().stream().findFirst()
                         .orElse(null);
            IpAddressDetail ipAddressDetail = new IpAddressDetail();
            ipAddressDetail.setPathIpType(olipAddressDetail.getPathIpType());
            ipAddressDetail.setServiceDetail(currentActiveServiceDetail);

            int extendedLanRequired = 0;
            ScComponentAttribute scComponentAttributeExtendedLanRequired = scComponentAttributesRepository
                   .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
                                       currentActiveServiceDetail.getScServiceDetailId(), "extendedLanRequired", "LM", "A");
            if (null != scComponentAttributeExtendedLanRequired) {
                   extendedLanRequired = scComponentAttributeExtendedLanRequired.getAttributeValue()
                                .equalsIgnoreCase("Yes") ? 1 : 0;
            }

            ipAddressDetail.setExtendedLanEnabled((byte) extendedLanRequired);
            ipAddressDetail.setNniVsatIpaddress(null);
            ipAddressDetail.setStartDate(new Timestamp(System.currentTimeMillis()));
            ipAddressDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
            ipAddressDetail.setModifiedBy(MODIFIEDBY);
            ipAddressDetailRepository.save(ipAddressDetail);
            
            if(Objects.nonNull(currentActiveServiceDetail)
    				&& Objects.nonNull(currentActiveServiceDetail.getInterfaceProtocolMappings())){  
            	logger.info("Service Detail IPM Exists");
    			List<InterfaceProtocolMapping> currentInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
    					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
    					.collect(Collectors.toList());
    			if (!currentInterfaceProtocolMappings.isEmpty()) {
    				logger.info("IPM Exists");
    				currentInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
    				InterfaceProtocolMapping currentInterfaceProtocolMapping = currentInterfaceProtocolMappings.get(0);
    				if(currentInterfaceProtocolMapping.getBgp()!=null && extendedLanRequired!=0){
    					logger.info("Current is Bgp and extendedLan enabled");
    					
    					ScComponentAttribute lanIpProvidedBy = scComponentAttributesRepository
    							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
    									currentActiveServiceDetail.getScServiceDetailId(), "lanIpProvidedBy", "LM", "A");
    					if (olipAddressDetail.getIpaddrLanv4Addresses() != null && (lanIpProvidedBy==null ||!lanIpProvidedBy.getAttributeValue().equalsIgnoreCase("TCL"))) {
    						logger.info("Prev LanV4 exists");
    						Set<IpaddrLanv4Address> ipLanv4=new HashSet<>();
    						for (IpaddrLanv4Address lanv4Address : olipAddressDetail.getIpaddrLanv4Addresses()) {

    							IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
    							ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
    							ipaddrLanv4Address.setIscustomerprovided(lanv4Address.getIscustomerprovided());

    							if (null != lanv4Address.getLanv4Address()) {
    								ipaddrLanv4Address.setLanv4Address(lanv4Address.getLanv4Address().trim());
    							}

    							ipaddrLanv4Address.setIssecondary(
    									Objects.nonNull(lanv4Address.getIssecondary()) ? lanv4Address.getIssecondary() : (byte)0);
    							ipaddrLanv4Address.setStartDate(new Timestamp(System.currentTimeMillis()));
    							ipaddrLanv4Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
    							ipaddrLanv4Address.setModifiedBy(MODIFIEDBY);
    							ipaddrLanv4AddressRepository.save(ipaddrLanv4Address);
    							ipLanv4.add(ipaddrLanv4Address);
    							
    						}
    						ipAddressDetail.setIpaddrLanv4Addresses(ipLanv4);

    					}

    		            if (olipAddressDetail.getIpaddrLanv6Addresses() != null) {
    		            	logger.info("Prev LanV6 exists");
    		            	Set<IpaddrLanv6Address> ipLanv6=new HashSet<>();
    		                   for (IpaddrLanv6Address lanv6Address : olipAddressDetail.getIpaddrLanv6Addresses()) {
    		                         
    		                	   IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
    		                	   ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
    		                	   ipaddrLanv6Address.setIscustomerprovided(lanv6Address.getIscustomerprovided());

    		                         if (null != lanv6Address.getLanv6Address()) {
    		                        	 ipaddrLanv6Address.setLanv6Address(lanv6Address.getLanv6Address().trim());
    		                         }

    		                         ipaddrLanv6Address.setIssecondary(Objects.nonNull(lanv6Address.getIssecondary())?lanv6Address.getIssecondary():(byte)0);
    		                         ipaddrLanv6Address.setStartDate(new Timestamp(System.currentTimeMillis()));
    		                         ipaddrLanv6Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
    		                         ipaddrLanv6Address.setModifiedBy(MODIFIEDBY);
    		                          ipaddrLanv6AddressRepository.save(ipaddrLanv6Address);
    		                          ipLanv6.add(ipaddrLanv6Address);
    		                   }
    		                   ipAddressDetail.setIpaddrLanv6Addresses(ipLanv6);
    		            }
    				}
    			}
    			
            }
            if (olipAddressDetail.getIpaddrWanv4Addresses() != null) {
            	logger.info("Prev WanV4 exists");
            	Set<IpaddrWanv4Address> ipWanv4=new HashSet<>();
                   for (IpaddrWanv4Address wan4Address : olipAddressDetail.getIpaddrWanv4Addresses()) {
                         IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
                                ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
                                ipaddrWanv4Address.setIscustomerprovided(wan4Address.getIscustomerprovided());
                                

                         
                                       ipaddrWanv4Address.setIssecondary(Objects.nonNull(wan4Address.getIssecondary())?wan4Address.getIssecondary():(byte)0);

                                if (null != wan4Address.getWanv4Address()) {
                                	logger.info("Prev WanV4 Address exists::{}",wan4Address.getWanv4Address().trim());
                                       ipaddrWanv4Address.setWanv4Address(wan4Address.getWanv4Address().trim());
                                } 

                                ipaddrWanv4Address.setStartDate(new Timestamp(System.currentTimeMillis()));
                                ipaddrWanv4Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
                                ipaddrWanv4Address.setModifiedBy(MODIFIEDBY);
                                ipaddrWanv4AddressRepository.save(ipaddrWanv4Address);
                                ipWanv4.add(ipaddrWanv4Address);
            }
                   ipAddressDetail.setIpaddrWanv4Addresses(ipWanv4);
            }
            if (olipAddressDetail.getIpaddrWanv6Addresses() != null) {
            	logger.info("Prev WanV6 exists");
            	Set<IpaddrWanv6Address> ipWanv6=new HashSet<>();
                   for (IpaddrWanv6Address wan6Address : olipAddressDetail.getIpaddrWanv6Addresses()) {
                         
                         IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
                         
                                ipaddrWanv6Address.setIscustomerprovided(wan6Address.getIscustomerprovided());
                                

                                ipaddrWanv6Address.setIpAddressDetail(ipAddressDetail);

                         
                                       ipaddrWanv6Address.setIssecondary(Objects.nonNull(wan6Address.getIssecondary())?wan6Address.getIssecondary():(byte)0);
                         

                                if (null != wan6Address.getWanv6Address()) {
                                       ipaddrWanv6Address.setWanv6Address(wan6Address.getWanv6Address().trim());
                                } 

                                ipaddrWanv6Address.setStartDate(new Timestamp(System.currentTimeMillis()));
                                ipaddrWanv6Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
                                ipaddrWanv6Address.setModifiedBy(MODIFIEDBY);
                                ipaddrWanv6AddressRepository.save(ipaddrWanv6Address);
                                ipWanv6.add(ipaddrWanv6Address);

            }
                   ipAddressDetail.setIpaddrWanv6Addresses(ipWanv6);
            }
            ipAddressSet.add(ipAddressDetail);
     }           
     logger.info("IpAddressSet Size"+ipAddressSet.size());
     if(ipAddressSet.size()>0){
    	 currentActiveServiceDetail.setIpAddressDetails(ipAddressSet);
         serviceDetailRepository.save(currentActiveServiceDetail);
         logger.info("Size::"+currentActiveServiceDetail.getIpAddressDetails().size());
     }
}

	
	
	private void applyVpnMetataDetailRule(ServiceDetail currentActiveServiceDetail,
			ServiceDetail prevActiveServiceDetail) {
		logger.info("applyVpnMetataDetailRule");
		if(Objects.nonNull(currentActiveServiceDetail.getVpnMetatDatas()) && Objects.nonNull(prevActiveServiceDetail.getVpnMetatDatas())){
			Optional<VpnMetatData> currentVpnMetaDataOptional=currentActiveServiceDetail.getVpnMetatDatas().stream().findFirst();
			Optional<VpnMetatData> prevVpnMetaDataOptional=prevActiveServiceDetail.getVpnMetatDatas().stream().findFirst();
			if(currentVpnMetaDataOptional.isPresent() && prevVpnMetaDataOptional.isPresent()){
				VpnMetatData currentVpnMetaData=currentVpnMetaDataOptional.get();
				VpnMetatData prevVpnMetaData= prevVpnMetaDataOptional.get();
				logger.info("Prev VpnMetaData Id::{}",prevVpnMetaData.getId());
				if((Objects.isNull(currentVpnMetaData.getVpnSolutionName()) && Objects.nonNull(prevVpnMetaData.getVpnSolutionName()))
						|| (Objects.nonNull(prevVpnMetaData.getVpnSolutionName()) && Objects.nonNull(currentVpnMetaData.getVpnSolutionName())
								&& !prevVpnMetaData.getVpnSolutionName().equalsIgnoreCase(currentVpnMetaData.getVpnSolutionName()))){
					logger.info("updating vpn solution name");
					currentVpnMetaData.setVpnSolutionName(prevVpnMetaData.getVpnSolutionName());
				}
				if((Objects.isNull(currentVpnMetaData.getVpnName()) && Objects.nonNull(prevVpnMetaData.getVpnName()))
						|| (Objects.nonNull(prevVpnMetaData.getVpnName()) && Objects.nonNull(currentVpnMetaData.getVpnName())
								&& !prevVpnMetaData.getVpnName().equalsIgnoreCase(currentVpnMetaData.getVpnName()))){
					logger.info("updating vpn name");
					currentVpnMetaData.setVpnName(prevVpnMetaData.getVpnName());
				}
				if((Objects.isNull(currentVpnMetaData.getSolutionId()) && Objects.nonNull(prevVpnMetaData.getSolutionId()))
						|| (Objects.nonNull(prevVpnMetaData.getSolutionId()) && Objects.nonNull(currentVpnMetaData.getSolutionId())
								&& !prevVpnMetaData.getSolutionId().equalsIgnoreCase(currentVpnMetaData.getSolutionId()))){
					logger.info("updating vpn solutionid");
					currentVpnMetaData.setSolutionId(prevVpnMetaData.getSolutionId());
				}
				if((Objects.isNull(currentVpnMetaData.getVpnAlias()) && Objects.nonNull(prevVpnMetaData.getVpnAlias()))
						|| (Objects.nonNull(prevVpnMetaData.getVpnAlias()) && Objects.nonNull(currentVpnMetaData.getVpnAlias())
								&& !prevVpnMetaData.getVpnAlias().equalsIgnoreCase(currentVpnMetaData.getVpnAlias()))){
					logger.info("updating vpn alias");
					currentVpnMetaData.setVpnAlias(prevVpnMetaData.getVpnAlias());
				}
				if(Objects.nonNull(currentVpnMetaData.getVpnName()) && !currentVpnMetaData.getVpnName().isEmpty()
								&& (!currentVpnMetaData.getVpnName().contains("_M") 
										|| !currentVpnMetaData.getVpnName().contains("_HS") || !currentVpnMetaData.getVpnName().contains("_PM"))){
					logger.info("updating vpn Sol and alias name");
					currentVpnMetaData.setVpnSolutionName(prevVpnMetaData.getVpnName());
					currentVpnMetaData.setSolutionId(prevVpnMetaData.getVpnName());
					currentVpnMetaData.setVpnName(prevVpnMetaData.getVpnName());
					currentVpnMetaData.setVpnAlias(prevVpnMetaData.getVpnName());
					/*if(Objects.nonNull(prevVpnMetaData.getVpnName()) && !prevVpnMetaData.getVpnName().isEmpty()
							&& (!prevVpnMetaData.getVpnName().contains("_M") 
										|| !prevVpnMetaData.getVpnName().contains("_HS") || !prevVpnMetaData.getVpnName().contains("_PM"))){
						logger.info("Vpn Name::Prev Vpn Name exists::{}",prevVpnMetaData.getVpnName());
						if (currentVpnMetaData.getL2OTopology().equalsIgnoreCase("Full Mesh") && !currentVpnMetaData.getVpnName().contains("_M")) {
							logger.info("L2O -> FullMesh");
							currentVpnMetaData.setVpnName(prevVpnMetaData.getVpnName().contains("_M")?prevVpnMetaData.getVpnName():prevVpnMetaData.getVpnName()+"_M");
						}else if (currentVpnMetaData.getL2OTopology().equalsIgnoreCase("Partial Mesh") && !currentVpnMetaData.getVpnName().contains("_PM")) {
							logger.info("L2O -> PartialMesh");
							currentVpnMetaData.setVpnName(prevVpnMetaData.getVpnName().contains("_PM")?prevVpnMetaData.getVpnName():prevVpnMetaData.getVpnName()+"_PM");
						}else if (currentVpnMetaData.getL2OTopology().equalsIgnoreCase("Hub & Spoke") && !currentVpnMetaData.getVpnName().contains("_HS")) {
							logger.info("L2O -> Hub & Spoke");
							currentVpnMetaData.setVpnName(prevVpnMetaData.getVpnName().contains("_HS")?prevVpnMetaData.getVpnName():prevVpnMetaData.getVpnName()+"_HS");
						}
					}
					if(Objects.nonNull(prevVpnMetaData.getVpnAlias()) && !prevVpnMetaData.getVpnAlias().isEmpty()
							&& (!prevVpnMetaData.getVpnName().contains("_M") 
										|| !prevVpnMetaData.getVpnName().contains("_HS") || !prevVpnMetaData.getVpnName().contains("_PM"))){
						logger.info("Vpn Alias::Prev Vpn Name exists::{}",prevVpnMetaData.getVpnName());
						if (currentVpnMetaData.getL2OTopology().equalsIgnoreCase("Full Mesh") && !currentVpnMetaData.getVpnAlias().contains("_M")) {
							logger.info("L2O -> FullMesh");
							currentVpnMetaData.setVpnAlias(prevVpnMetaData.getVpnName().contains("_M")?prevVpnMetaData.getVpnName():prevVpnMetaData.getVpnName()+"_M");
						}else if (currentVpnMetaData.getL2OTopology().equalsIgnoreCase("Partial Mesh") && !currentVpnMetaData.getVpnAlias().contains("_PM")) {
							logger.info("L2O -> PartialMesh");
							currentVpnMetaData.setVpnAlias(prevVpnMetaData.getVpnName().contains("_PM")?prevVpnMetaData.getVpnName():prevVpnMetaData.getVpnName()+"_PM");
						}else if (currentVpnMetaData.getL2OTopology().equalsIgnoreCase("Hub & Spoke") && !currentVpnMetaData.getVpnAlias().contains("_HS")) {
							logger.info("L2O -> Hub & Spoke");
							currentVpnMetaData.setVpnAlias(prevVpnMetaData.getVpnName().contains("_HS")?prevVpnMetaData.getVpnName():prevVpnMetaData.getVpnName()+"_HS");
						}					
					}*/
				}
				/*if(Objects.nonNull(currentVpnMetaData.getVpnSolutionName()) && !currentVpnMetaData.getVpnSolutionName().isEmpty()
						&& (currentVpnMetaData.getVpnSolutionName().contains("_M") 
										|| currentVpnMetaData.getVpnSolutionName().contains("_HS") || currentVpnMetaData.getVpnSolutionName().contains("_PM"))){
					logger.info("Vpn solution name without alias");
					currentVpnMetaData.setVpnSolutionName(currentVpnMetaData.getVpnSolutionName().substring(0, currentVpnMetaData.getVpnSolutionName().lastIndexOf('_')));
				}
				if(Objects.nonNull(currentVpnMetaData.getSolutionId()) && !currentVpnMetaData.getSolutionId().isEmpty()
						&& (currentVpnMetaData.getSolutionId().contains("_M") 
										|| currentVpnMetaData.getSolutionId().contains("_HS") || currentVpnMetaData.getSolutionId().contains("_PM"))){
					logger.info("Solution Id without alias");
					currentVpnMetaData.setSolutionId(currentVpnMetaData.getSolutionId().substring(0, currentVpnMetaData.getSolutionId().lastIndexOf('_')));
				}*/
				logger.info("updating vpn meta details");
				vpnMetatDataRepository.saveAndFlush(currentVpnMetaData);
				if(Objects.nonNull(currentVpnMetaData.getVpnName()) && !currentVpnMetaData.getVpnName().isEmpty() && currentVpnMetaData.getVpnName().equalsIgnoreCase("STATE_BANK")){
					logger.info("Set managementVpnType1 as NULL for STATE_BANK");
					currentVpnMetaData.setManagementVpnType1(null);
				}
				vpnMetatDataRepository.saveAndFlush(currentVpnMetaData);
			}
		}
	}


	private void applyServiceDetailsSolutionIdRule(ServiceDetail currentActiveServiceDetail,
			ServiceDetail prevActiveServiceDetail) {
		logger.info("applyServiceDetailsSolutionIdRule");
		if((Objects.nonNull(prevActiveServiceDetail.getSolutionId()) && Objects.isNull(currentActiveServiceDetail.getSolutionId()))
				|| (Objects.nonNull(currentActiveServiceDetail.getSolutionId()) && Objects.nonNull(prevActiveServiceDetail.getSolutionId())
				&& !currentActiveServiceDetail.getSolutionId().equals(prevActiveServiceDetail.getSolutionId()))){
			logger.info("update Service Detail SolutionId");
			currentActiveServiceDetail.setSolutionId(prevActiveServiceDetail.getSolutionId());
			serviceDetailRepository.save(currentActiveServiceDetail);
		}
	}


	private void applyCEEthernetIpv4Rule(ServiceDetail currentActiveServiceDetail, Map<String, String> currentActiveManDatoryFields) throws TclCommonException {
		logger.info("applyCEEthernetIpv4Rule");
		List<InterfaceProtocolMapping> cpeProtocolMappings = currentActiveServiceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
						&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
				.collect(Collectors.toList());
		List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
				.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
				.collect(Collectors.toList());
		if (!cpeProtocolMappings.isEmpty() && !routerInterfaceProtocolMappings.isEmpty()) {
			cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping cpeInterfaceProtocolMapping = cpeProtocolMappings.get(0);
			routerInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings.get(0);
			if (Objects.nonNull(cpeInterfaceProtocolMapping) && Objects.nonNull(routerInterfaceProtocolMapping)) {
					if (cpeInterfaceProtocolMapping.getEthernetInterface() != null && routerInterfaceProtocolMapping.getBgp()!=null) {
						logger.info("Bgp::",routerInterfaceProtocolMapping.getBgp().getBgpId());
						logger.info("Router IPM Id::",routerInterfaceProtocolMapping.getInterfaceProtocolMappingId());
						EthernetInterface cpeEthernetInterface=cpeInterfaceProtocolMapping.getEthernetInterface();
						if(Objects.isNull(cpeEthernetInterface.getIpv4Address()) && Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address())){
							logger.info("setIpv4Address");
							if(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().contains("/")){
								String ipAddr[]=routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().split("/");
								cpeEthernetInterface.setIpv4Address(ipAddr[0].trim());
							}else{
								cpeEthernetInterface.setIpv4Address(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().trim());
							}
						}
						if(Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address())){
							logger.info("setModifiedIpv4Address");
							if (currentActiveManDatoryFields.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
								logger.info("setModifiedIpv4Address from WAN");
								if(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().contains("/")){
									String ipAddr[]=routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().split("/");
									cpeEthernetInterface.setModifiedIpv4Address(ipAddr[0].trim() + "/"
											+ subnet(currentActiveManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim());
								}else{
									cpeEthernetInterface.setModifiedIpv4Address(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address().trim() + "/"
											+ subnet(currentActiveManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim());
								}
								
							}
						}
						if(Objects.isNull(cpeEthernetInterface.getIpv6Address()) && Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address())){
							logger.info("setIpv6Address");
							if(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address().contains("/")){
								String ipAddr[]=routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address().split("/");
								cpeEthernetInterface.setIpv6Address(ipAddr[0].trim());
							}else{
								cpeEthernetInterface.setIpv6Address(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address().trim());
							}
						}
						if(Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address())){
							logger.info("setModifiedIpv6Address");
							if (currentActiveManDatoryFields.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
								logger.info("setModifiedIpv6Address from WAN");
								if(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address().contains("/")){
									String ipAddr[]=routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address().split("/");
									cpeEthernetInterface.setModifiedIpv6Address(ipAddr[0].trim() + "/"
											+ subnet(currentActiveManDatoryFields.get(AceConstants.IPADDRESS.WANV6ADDRESS)).trim());
								}else{
									cpeEthernetInterface.setModifiedIpv6Address(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address().trim() + "/"
											+ subnet(currentActiveManDatoryFields.get(AceConstants.IPADDRESS.WANV6ADDRESS)).trim());
								}
								
							}
						}
						ethernetInterfaceRepository.save(cpeEthernetInterface);
					}else if (cpeInterfaceProtocolMapping.getEthernetInterface() != null && routerInterfaceProtocolMapping.getStaticProtocol()!=null) {
						EthernetInterface cpeEthernetInterface=cpeInterfaceProtocolMapping.getEthernetInterface();
						if(Objects.isNull(cpeEthernetInterface.getIpv4Address()) && Objects.nonNull(routerInterfaceProtocolMapping.getEthernetInterface())
								&& Objects.nonNull(routerInterfaceProtocolMapping.getEthernetInterface().getIpv4Address())){
							logger.info("static setIpv4Address");
							cpeEthernetInterface.setIpv4Address(getIpAddressSplit(routerInterfaceProtocolMapping.getEthernetInterface().getIpv4Address().trim(), 1));
						}
						if(Objects.nonNull(cpeEthernetInterface.getIpv4Address())){
							logger.info("static setModifiedIpv4Address");
							if (currentActiveManDatoryFields.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
								logger.info("static setModifiedIpv4Address from WAN");
								cpeEthernetInterface.setModifiedIpv4Address(cpeEthernetInterface.getIpv4Address().trim() + "/"
										+ subnet(currentActiveManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim());
							}
							
						}
						ethernetInterfaceRepository.save(cpeEthernetInterface);
						logger.info("Static Id::{}",routerInterfaceProtocolMapping.getStaticProtocol().getStaticprotocolId());
						logger.info("Ipv4::{}",cpeEthernetInterface.getIpv4Address());
						List<WanStaticRoute> wanStaticList=wanStaticRouteRepository.findByStaticProtocol(routerInterfaceProtocolMapping.getStaticProtocol());
						if(Objects.nonNull(wanStaticList) && !wanStaticList.isEmpty()){
							logger.info("WanStatic Route NextHopId updated::",cpeEthernetInterface.getIpv4Address());
							WanStaticRoute wanStaticRoute =wanStaticList.get(0);
							wanStaticRoute.setNextHopid(cpeEthernetInterface.getIpv4Address().trim());
							wanStaticRouteRepository.save(wanStaticRoute);
						}else{
							if(Objects.nonNull(routerInterfaceProtocolMapping.getStaticProtocol().getWanStaticRoutes()) && !routerInterfaceProtocolMapping.getStaticProtocol().getWanStaticRoutes().isEmpty()){
								Optional<WanStaticRoute> wanStaticRouteOptional=routerInterfaceProtocolMapping.getStaticProtocol().getWanStaticRoutes().stream().findFirst();
								if(wanStaticRouteOptional.isPresent()){
									logger.info("WanStatic Route NextHopId updated from Object");
									WanStaticRoute wanStaticRoute =wanStaticRouteOptional.get();
									wanStaticRoute.setNextHopid(cpeEthernetInterface.getIpv4Address().trim());
									wanStaticRouteRepository.save(wanStaticRoute);
								}
							}
						}
					}else if (cpeInterfaceProtocolMapping.getEthernetInterface() != null && routerInterfaceProtocolMapping.getOspf()!=null) {
						EthernetInterface cpeEthernetInterface=cpeInterfaceProtocolMapping.getEthernetInterface();
						if(Objects.isNull(cpeEthernetInterface.getIpv4Address()) && Objects.nonNull(routerInterfaceProtocolMapping.getEthernetInterface())
								&& Objects.nonNull(routerInterfaceProtocolMapping.getEthernetInterface().getIpv4Address())){
							logger.info("setIpv4Address");
							cpeEthernetInterface.setIpv4Address(getIpAddressSplit(routerInterfaceProtocolMapping.getEthernetInterface().getIpv4Address(), 1).trim());
						}
						if(Objects.nonNull(cpeEthernetInterface.getIpv4Address())){
							logger.info("setModifiedIpv4Address");
							if (currentActiveManDatoryFields.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
								logger.info("setModifiedIpv4Address from WAN");
								cpeEthernetInterface.setModifiedIpv4Address(cpeEthernetInterface.getIpv4Address().trim() + "/"
										+ subnet(currentActiveManDatoryFields.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim());
							}
							
						}
						ethernetInterfaceRepository.save(cpeEthernetInterface);
					}
			}
		}
	}
	
	
	private void applyInterfaceDetailsRule(ServiceDetail currentActiveServiceDetail,
			ServiceDetail prevActiveServiceDetail) {
		logger.info("applyInterfaceDetailsRule");
		if(Objects.nonNull(currentActiveServiceDetail) && Objects.nonNull(prevActiveServiceDetail)
				&& Objects.nonNull(currentActiveServiceDetail.getInterfaceProtocolMappings()) && Objects.nonNull(prevActiveServiceDetail.getInterfaceProtocolMappings())){
			
			List<InterfaceProtocolMapping> currentInterfaceProtocolMappings = currentActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
			
			List<InterfaceProtocolMapping> prevInterfaceProtocolMappings = prevActiveServiceDetail.getInterfaceProtocolMappings()
					.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
					.collect(Collectors.toList());
			
			
			if (!currentInterfaceProtocolMappings.isEmpty() && !prevInterfaceProtocolMappings.isEmpty()) {
				currentInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping currentInterfaceProtocolMapping = currentInterfaceProtocolMappings.get(0);
				prevInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping prevInterfaceProtocolMapping = prevInterfaceProtocolMappings.get(0);
				IpAddressDetail ipAddr=currentActiveServiceDetail.getIpAddressDetails().stream().findFirst().get();
				if (Objects.nonNull(currentInterfaceProtocolMapping) && Objects.nonNull(prevInterfaceProtocolMapping)) {
					if (currentInterfaceProtocolMapping.getEthernetInterface() != null 
							&& prevInterfaceProtocolMapping.getEthernetInterface() != null) {
						logger.info("EthernetInterface");
						if(((Objects.isNull(currentInterfaceProtocolMapping.getEthernetInterface().getInnerVlan()) 
								&& Objects.isNull(prevInterfaceProtocolMapping.getEthernetInterface().getInnerVlan())) 
								|| (Objects.nonNull(currentInterfaceProtocolMapping.getEthernetInterface().getInnerVlan()) 
										&& Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getInnerVlan()) 
										&& currentInterfaceProtocolMapping.getEthernetInterface().getInnerVlan().equals(prevInterfaceProtocolMapping.getEthernetInterface().getInnerVlan())))
								&&
								((Objects.isNull(currentInterfaceProtocolMapping.getEthernetInterface().getOuterVlan()) 
										&& Objects.isNull(prevInterfaceProtocolMapping.getEthernetInterface().getOuterVlan())) 
										|| (Objects.nonNull(currentInterfaceProtocolMapping.getEthernetInterface().getOuterVlan()) 
												&& Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getOuterVlan()) 
												&& currentInterfaceProtocolMapping.getEthernetInterface().getOuterVlan().equals(prevInterfaceProtocolMapping.getEthernetInterface().getOuterVlan())))
								&&
								((Objects.isNull(currentInterfaceProtocolMapping.getEthernetInterface().getPhysicalPort()) 
										&& Objects.isNull(prevInterfaceProtocolMapping.getEthernetInterface().getPhysicalPort())) 
										|| (Objects.nonNull(currentInterfaceProtocolMapping.getEthernetInterface().getPhysicalPort()) 
												&& Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getPhysicalPort()) 
												&& currentInterfaceProtocolMapping.getEthernetInterface().getPhysicalPort().equals(prevInterfaceProtocolMapping.getEthernetInterface().getPhysicalPort())))
								&& prevInterfaceProtocolMapping.getEthernetInterface().getInterfaceName()!=null){
							logger.info("copying prev interfacename");
							EthernetInterface ethernetInterface=currentInterfaceProtocolMapping.getEthernetInterface();
							ethernetInterface.setInterfaceName(prevInterfaceProtocolMapping.getEthernetInterface().getInterfaceName());		
							ethernetInterfaceRepository.save(ethernetInterface);
						}
						EthernetInterface ethernetInterface=currentInterfaceProtocolMapping.getEthernetInterface();
						if(Objects.isNull(ethernetInterface.getModifiedIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedIpv4Address())){
							logger.info("copying prev Modified Ipv4");
							ethernetInterface.setModifiedIpv4Address(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedIpv4Address().trim());
						}
						if(Objects.isNull(ethernetInterface.getModifiedIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType())
								&& Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedIpv6Address())){
							logger.info("copying prev Modified Ipv6");
							ethernetInterface.setModifiedIpv6Address(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedIpv6Address().trim());
						}
						if(Objects.isNull(ethernetInterface.getModifiedSecondaryIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedSecondaryIpv4Address())){
							logger.info("copying prev Modified Sy Ipv4");
							ethernetInterface.setModifiedSecondaryIpv4Address(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedSecondaryIpv4Address().trim());
						}
						if(Objects.isNull(ethernetInterface.getModifiedSecondaryIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedSecondaryIpv6Address())){
							logger.info("copying prev Modified Sy Ipv6");
							ethernetInterface.setModifiedSecondaryIpv6Address(prevInterfaceProtocolMapping.getEthernetInterface().getModifiedSecondaryIpv6Address().trim());
						}
						if(Objects.isNull(ethernetInterface.getIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getIpv4Address())){
							logger.info("copying prev Ipv4");
							String[] ipv4=prevInterfaceProtocolMapping.getEthernetInterface().getIpv4Address().split("/");
							ethernetInterface.setIpv4Address(ipv4[0].trim());
						}
						if(Objects.nonNull(ethernetInterface.getModifiedIpv4Address()) && ethernetInterface.getModifiedIpv4Address().contains("null") && Objects.nonNull(ethernetInterface.getIpv4Address())){
							logger.info("Replacing null Ipv4");
							ethernetInterface.setModifiedIpv4Address(ethernetInterface.getModifiedIpv4Address().replace("null", ethernetInterface.getIpv4Address()).trim());
						}
						if(Objects.isNull(ethernetInterface.getIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getIpv6Address())){
							logger.info("copying prev Ipv6");
							String[] ipv6=prevInterfaceProtocolMapping.getEthernetInterface().getIpv6Address().split("/");
							ethernetInterface.setIpv6Address(ipv6[0].trim());
						}
						if(Objects.isNull(ethernetInterface.getSecondaryIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getSecondaryIpv4Address())){
							logger.info("copying prev Sy Ipv4");
							String[] ipv4=prevInterfaceProtocolMapping.getEthernetInterface().getSecondaryIpv4Address().split("/");
							ethernetInterface.setSecondaryIpv4Address(ipv4[0].trim());
						}
						if(Objects.isNull(ethernetInterface.getSecondaryIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getSecondaryIpv6Address())){
							logger.info("copying prev Sy Ipv6");
							String[] ipv6=prevInterfaceProtocolMapping.getEthernetInterface().getSecondaryIpv6Address().split("/");
							ethernetInterface.setSecondaryIpv6Address(ipv6[0].trim());
						}
						if(Objects.nonNull(prevInterfaceProtocolMapping.getEthernetInterface().getMode()) && Objects.nonNull(currentActiveServiceDetail.getRouterMake())
								&&!currentActiveServiceDetail.getRouterMake().isEmpty()	&& currentActiveServiceDetail.getRouterMake().toLowerCase().contains("cisco")){
							logger.info("copying Mode");
							ethernetInterface.setMode(prevInterfaceProtocolMapping.getEthernetInterface().getMode());
							/*if("ACCESS".equalsIgnoreCase(ethernetInterface.getMode())){
								ethernetInterface.setEncapsulation("NULL");
							}*/
						}
						ethernetInterfaceRepository.save(ethernetInterface);
					}else if (currentInterfaceProtocolMapping.getChannelizedSdhInterface() != null 
							&& prevInterfaceProtocolMapping.getChannelizedSdhInterface() != null) {
						logger.info("SDH");
						if((Objects.isNull(currentInterfaceProtocolMapping.getChannelizedSdhInterface().getPhysicalPort()) 
										&& Objects.isNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getPhysicalPort())) 
										|| (Objects.nonNull(currentInterfaceProtocolMapping.getChannelizedSdhInterface().getPhysicalPort()) 
												&& Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getPhysicalPort()) 
												&& currentInterfaceProtocolMapping.getChannelizedSdhInterface().getPhysicalPort().equals(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getPhysicalPort()))){
							logger.info("copying prev interfacename");
							ChannelizedSdhInterface channelizedSdhInterface=currentInterfaceProtocolMapping.getChannelizedSdhInterface();
							channelizedSdhInterface.setInterfaceName(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getInterfaceName());		
							channelizedSdhInterfaceRepository.save(channelizedSdhInterface);
						}
						
						ChannelizedSdhInterface channelizedSdhInterface=currentInterfaceProtocolMapping.getChannelizedSdhInterface();
						if(Objects.isNull(channelizedSdhInterface.getModifiedIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedIpv4Address())){
							logger.info("copying prev Modified Ipv4");
							channelizedSdhInterface.setModifiedIpv4Address(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedIpv4Address());
						}
						if(Objects.isNull(channelizedSdhInterface.getModifiedIipv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedIipv6Address())){
							logger.info("copying prev Modified Ipv6");
							channelizedSdhInterface.setModifiedIipv6Address(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedIipv6Address());
						}
						if(Objects.isNull(channelizedSdhInterface.getModifiedSecondaryIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedSecondaryIpv4Address())){
							logger.info("copying prev Modified Sy Ipv4");
							channelizedSdhInterface.setModifiedSecondaryIpv4Address(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedSecondaryIpv4Address());
						}
						if(Objects.isNull(channelizedSdhInterface.getModifiedSecondaryIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedSecondaryIpv6Address())){
							logger.info("copying prev Modified Sy Ipv6");
							channelizedSdhInterface.setModifiedSecondaryIpv6Address(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getModifiedSecondaryIpv6Address());
						}
						if(Objects.isNull(channelizedSdhInterface.getIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getIpv4Address())){
							logger.info("copying prev Ipv4");
							String[] ipv4=prevInterfaceProtocolMapping.getChannelizedSdhInterface().getIpv4Address().split("/");
							channelizedSdhInterface.setIpv4Address(ipv4[0]);
						}
						if(Objects.isNull(channelizedSdhInterface.getIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getIpv6Address())){
							logger.info("copying prev Ipv6");
							String[] ipv6=prevInterfaceProtocolMapping.getChannelizedSdhInterface().getIpv6Address().split("/");
							channelizedSdhInterface.setIpv6Address(ipv6[0]);
						}
						if(Objects.isNull(channelizedSdhInterface.getSecondaryIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getSecondaryIpv4Address())){
							logger.info("copying prev Sy Ipv4");
							String[] ipv4=prevInterfaceProtocolMapping.getChannelizedSdhInterface().getSecondaryIpv4Address().split("/");
							channelizedSdhInterface.setSecondaryIpv4Address(ipv4[0]);
						}
						if(Objects.isNull(channelizedSdhInterface.getSecondaryIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedSdhInterface().getSecondaryIpv6Address())){
							logger.info("copying prev Sy Ipv6");
							String[] ipv6=prevInterfaceProtocolMapping.getChannelizedSdhInterface().getSecondaryIpv6Address().split("/");
							channelizedSdhInterface.setSecondaryIpv6Address(ipv6[0]);
						}
						channelizedSdhInterfaceRepository.save(channelizedSdhInterface);
					}else if (currentInterfaceProtocolMapping.getChannelizedE1serialInterface() != null 
							&& prevInterfaceProtocolMapping.getChannelizedE1serialInterface() != null) {
						logger.info("Serail");
						if((Objects.isNull(currentInterfaceProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort()) 
										&& Objects.isNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort())) 
										|| (Objects.nonNull(currentInterfaceProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort()) 
												&& Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort()) 
												&& currentInterfaceProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort().equals(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getPhysicalPort()))){
							logger.info("copying prev interfacename");
							ChannelizedE1serialInterface channelizedE1serialInterface=currentInterfaceProtocolMapping.getChannelizedE1serialInterface();
							channelizedE1serialInterface.setInterfaceName(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getInterfaceName());		
							channelizedE1serialInterfaceRepository.save(channelizedE1serialInterface);
						}
						
						ChannelizedE1serialInterface channelizedE1serialInterface=currentInterfaceProtocolMapping.getChannelizedE1serialInterface();
						if(Objects.isNull(channelizedE1serialInterface.getModifiedIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv4Address())){
							logger.info("copying prev Modified Ipv4");
							channelizedE1serialInterface.setModifiedIpv4Address(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv4Address());
						}
						if(Objects.isNull(channelizedE1serialInterface.getModifiedIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv6Address())){
							logger.info("copying prev Modified Ipv6");
							channelizedE1serialInterface.setModifiedIpv6Address(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedIpv6Address());
						}
						if(Objects.isNull(channelizedE1serialInterface.getModifiedSecondaryIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedSecondaryIpv4Address())){
							logger.info("copying prev Modified Sy Ipv4");
							channelizedE1serialInterface.setModifiedSecondaryIpv4Address(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedSecondaryIpv4Address());
						}
						if(Objects.isNull(channelizedE1serialInterface.getModifiedSecondaryIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedSecondaryIpv6Address())){
							logger.info("copying prev Modified Sy Ipv6");
							channelizedE1serialInterface.setModifiedSecondaryIpv6Address(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getModifiedSecondaryIpv6Address());
						}
						if(Objects.isNull(channelizedE1serialInterface.getIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getIpv4Address())){
							logger.info("copying prev Ipv4");
							String[] ipv4=prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getIpv4Address().split("/");
							channelizedE1serialInterface.setIpv4Address(ipv4[0]);
						}
						if(Objects.isNull(channelizedE1serialInterface.getIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getIpv6Address())){
							logger.info("copying prev Ipv6");
							String[] ipv6=prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getIpv6Address().split("/");
							channelizedE1serialInterface.setIpv6Address(ipv6[0]);
						}
						if(Objects.isNull(channelizedE1serialInterface.getSecondaryIpv4Address()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getSecondaryIpv4Address())){
							logger.info("copying prev Sy Ipv4");
							String[] ipv4=prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getSecondaryIpv4Address().split("/");
							channelizedE1serialInterface.setSecondaryIpv4Address(ipv4[0]);
						}
						if(Objects.isNull(channelizedE1serialInterface.getSecondaryIpv6Address()) && "DUALSTACK".equalsIgnoreCase(ipAddr.getPathIpType()) && 
								Objects.nonNull(prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getSecondaryIpv6Address())){
							logger.info("copying prev Sy Ipv6");
							String[] ipv6=prevInterfaceProtocolMapping.getChannelizedE1serialInterface().getSecondaryIpv6Address().split("/");
							channelizedE1serialInterface.setSecondaryIpv6Address(ipv6[0]);
						}
						channelizedE1serialInterfaceRepository.save(channelizedE1serialInterface);
					}
				}
			}
		}
		
	}
	
	private void applyCosRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("applyCosRule");
		if(Objects.nonNull(currentActiveServiceDetail.getServiceQos()) && !currentActiveServiceDetail.getServiceQos().isEmpty()
			&& (Objects.isNull(currentActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias())
					|| currentActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias().isEmpty())
			&& Objects.nonNull(prevActiveServiceDetail.getServiceQos()) && !prevActiveServiceDetail.getServiceQos().isEmpty()){
			logger.info("Current Cos Criteria not exists");
			Set<ServiceQo> currentQoSet=currentActiveServiceDetail.getServiceQos();
			ServiceQo currentQos=currentQoSet.stream().findFirst().get();
			Set<ServiceCosCriteria> serviceCos=new HashSet<>();
			Set<ServiceCosCriteria> prevServiceCosCriteria=prevActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias();
			logger.info("Prev Cos List size {} ::",prevServiceCosCriteria.size());
			prevServiceCosCriteria.stream().forEach(prevCos -> {
				logger.info("applyCosRule:: creating cos");
				ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
				serviceCosCriteria.setDhcpVal1(prevCos.getDhcpVal1());
				serviceCosCriteria.setDhcpVal2(prevCos.getDhcpVal2());
				serviceCosCriteria.setDhcpVal3(prevCos.getDhcpVal3());
				serviceCosCriteria.setDhcpVal4(prevCos.getDhcpVal4());
				serviceCosCriteria.setDhcpVal5(prevCos.getDhcpVal5());
				serviceCosCriteria.setDhcpVal6(prevCos.getDhcpVal6());
				serviceCosCriteria.setDhcpVal7(prevCos.getDhcpVal7());
				serviceCosCriteria.setDhcpVal8(prevCos.getDhcpVal8());
				serviceCosCriteria.setIpprecedenceVal1(prevCos.getIpprecedenceVal1());
				serviceCosCriteria.setIpprecedenceVal2(prevCos.getIpprecedenceVal2());
				serviceCosCriteria.setIpprecedenceVal3(prevCos.getIpprecedenceVal3());
				serviceCosCriteria.setIpprecedenceVal4(prevCos.getIpprecedenceVal4());
				serviceCosCriteria.setIpprecedenceVal5(prevCos.getIpprecedenceVal5());
				serviceCosCriteria.setIpprecedenceVal6(prevCos.getIpprecedenceVal6());
				serviceCosCriteria.setIpprecedenceVal7(prevCos.getIpprecedenceVal7());
				serviceCosCriteria.setIpprecedenceVal8(prevCos.getIpprecedenceVal8());
				serviceCosCriteria.setCosName(prevCos.getCosName());
				serviceCosCriteria.setClassificationCriteria(prevCos.getClassificationCriteria());
				serviceCosCriteria.setCosPercent(prevCos.getCosPercent());
				serviceCosCriteria.setBwBpsunit(prevCos.getBwBpsunit());
				serviceCosCriteria.setModifiedBy(MODIFIEDBY);
				serviceCosCriteria.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				serviceCosCriteria.setServiceQo(currentQos);
				serviceCos.add(serviceCosCriteria);
			});		
			logger.info("Service Cos List size :: {}",serviceCos.size());
			currentQos.setServiceCosCriterias(serviceCos);
			serviceCosCriteriaRepository.saveAll(serviceCos);
			logger.info("Saved in DB");
			applyCosAclPolicyCriteria(serviceCos,prevServiceCosCriteria);
		 }else if(Objects.nonNull(currentActiveServiceDetail.getServiceQos()) && !currentActiveServiceDetail.getServiceQos().isEmpty()
			&& (Objects.nonNull(currentActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias())
					&& !currentActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias().isEmpty())
			&& Objects.nonNull(prevActiveServiceDetail.getServiceQos()) && !prevActiveServiceDetail.getServiceQos().isEmpty()
			&& (Objects.nonNull(prevActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias())
					&& !prevActiveServiceDetail.getServiceQos().stream().findFirst().get().getServiceCosCriterias().isEmpty())){
			 logger.info("Current Cos Criteria exists");
			 Set<ServiceQo> currentQoSet=currentActiveServiceDetail.getServiceQos();
			 ServiceQo currentQos=currentQoSet.stream().findFirst().get();
			 Set<ServiceCosCriteria> currentCosCriteria=currentQos.getServiceCosCriterias();
			 Set<ServiceQo> prevQoSet=prevActiveServiceDetail.getServiceQos();
			 ServiceQo prevQos=prevQoSet.stream().findFirst().get();
			 Set<ServiceCosCriteria> prevCosCriteria=prevQos.getServiceCosCriterias();
			 Set<ServiceCosCriteria> updatedCosCriteria= new HashSet<>();
			 currentCosCriteria.stream().filter(currentCos -> Objects.nonNull(currentCos.getCosName()) && !currentCos.getCosName().isEmpty() 
					 &&  Objects.nonNull(currentCos.getClassificationCriteria()) && !currentCos.getClassificationCriteria().isEmpty() 
					 && "IP Address".equalsIgnoreCase(currentCos.getClassificationCriteria())).forEach(currentCos ->{
				 prevCosCriteria.stream().filter(prevCos -> Objects.nonNull(prevCos.getCosName()) && !prevCos.getCosName().isEmpty()).forEach(prevCos ->{
					 if(currentCos.getCosName().equalsIgnoreCase(prevCos.getCosName())){
						 logger.info("Current & Prev Cos Name matches::{}",currentCos.getCosName());
							 currentCos.setDhcpVal1(prevCos.getDhcpVal1());
							 currentCos.setDhcpVal2(prevCos.getDhcpVal2());
							 currentCos.setDhcpVal3(prevCos.getDhcpVal3());
							 currentCos.setDhcpVal4(prevCos.getDhcpVal4());
							 currentCos.setDhcpVal5(prevCos.getDhcpVal5());
							 currentCos.setDhcpVal6(prevCos.getDhcpVal6());
							 currentCos.setDhcpVal7(prevCos.getDhcpVal7());
							 currentCos.setDhcpVal8(prevCos.getDhcpVal8());
							 currentCos.setIpprecedenceVal1(prevCos.getIpprecedenceVal1());
							 currentCos.setIpprecedenceVal2(prevCos.getIpprecedenceVal2());
							 currentCos.setIpprecedenceVal3(prevCos.getIpprecedenceVal3());
							 currentCos.setIpprecedenceVal4(prevCos.getIpprecedenceVal4());
							 currentCos.setIpprecedenceVal5(prevCos.getIpprecedenceVal5());
							 currentCos.setIpprecedenceVal6(prevCos.getIpprecedenceVal6());
							 currentCos.setIpprecedenceVal7(prevCos.getIpprecedenceVal7());
							 currentCos.setIpprecedenceVal8(prevCos.getIpprecedenceVal8());
							 currentCos.setClassificationCriteria(prevCos.getClassificationCriteria());
							 currentCos.setModifiedBy(MODIFIEDBY);
							 currentCos.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
							 updatedCosCriteria.add(currentCos);
							 serviceCosCriteriaRepository.save(currentCos);
					 }
				 });
			 });
			 logger.info("Updated Current Cos Criteria size::{}",updatedCosCriteria.size());
			 applyCosAclPolicyCriteria(updatedCosCriteria,prevCosCriteria);
		 }
	}

	private void applyCosAclPolicyCriteria(Set<ServiceCosCriteria> serviceCos, Set<ServiceCosCriteria> prevServiceCosCriteria) {
		logger.info("applyCosAclPolicyCriteria");
		if(!serviceCos.isEmpty()){
			logger.info("Current Cos exists");
			List<ServiceCosCriteria> ipAddressServiceCos =serviceCos.stream().filter(cosCriteria -> Objects.nonNull(cosCriteria.getClassificationCriteria())
			&& !cosCriteria.getClassificationCriteria().isEmpty() 
			&& cosCriteria.getClassificationCriteria().toLowerCase().contains("address")).collect(Collectors.toList());
			List<ServiceCosCriteria> prevIpAddressServiceCos =prevServiceCosCriteria.stream().filter(prevCosCriteria -> Objects.nonNull(prevCosCriteria.getClassificationCriteria())
					&& !prevCosCriteria.getClassificationCriteria().isEmpty() 
					&& prevCosCriteria.getClassificationCriteria().toLowerCase().contains("address")).collect(Collectors.toList());
			if(!ipAddressServiceCos.isEmpty() && !prevIpAddressServiceCos.isEmpty()){
				logger.info("Prev and Current Ip Address Cos not empty");
				Optional<ServiceCosCriteria> optionalCurrentServiceCosCriteria=ipAddressServiceCos.stream().findFirst();
				Optional<ServiceCosCriteria> optionalPrevServiceCosCriteria=prevIpAddressServiceCos.stream().findFirst();
				if(optionalCurrentServiceCosCriteria.isPresent() && optionalPrevServiceCosCriteria.isPresent()){
					logger.info("Prev and Current Ip Address Cos exists");
					ServiceCosCriteria currentCosCriteria=optionalCurrentServiceCosCriteria.get();
					ServiceCosCriteria prevCosCriteria=optionalPrevServiceCosCriteria.get();
					if(Objects.nonNull(prevCosCriteria.getAclPolicyCriterias()) && !prevCosCriteria.getAclPolicyCriterias().isEmpty()){
						prevCosCriteria.getAclPolicyCriterias().stream().forEach(prevAclPolicy ->{
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

	private void applyVrfDetailRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("applyVrfDetailRule method invoked");
		if(Objects.nonNull(currentActiveServiceDetail.getVrfs())){
			Optional<Vrf> vrfOptional=currentActiveServiceDetail.getVrfs().stream().findFirst();
			if(vrfOptional.isPresent() && Objects.isNull(vrfOptional.get().getVrfName()) && Objects.nonNull(prevActiveServiceDetail.getVrfs())){
				logger.info("Inside Multi current vrf attribute for MACD:{}",vrfOptional.get().getVrfId());
				Optional<Vrf> prevVrfOptional=prevActiveServiceDetail.getVrfs().stream().findFirst();
				if(prevVrfOptional.isPresent() && prevVrfOptional.get().getIsmultivrf()!=null && prevVrfOptional.get().getIsmultivrf()==(byte)1){
					logger.info("Saving Multi vrf attribute for MACD:{}",prevVrfOptional.get().getVrfId());
					Vrf currentVrf=vrfOptional.get();
					currentVrf.setIsmultivrf((byte)1);
					currentVrf.setMastervrfServiceid(String.valueOf(prevVrfOptional.get().getMastervrfServiceid()));
					currentVrf.setVrfProjectName(prevVrfOptional.get().getVrfProjectName());
					currentVrf.setSlaveVrfServiceId(prevVrfOptional.get().getSlaveVrfServiceId());
					currentVrf.setVrfName(prevVrfOptional.get().getVrfName());
					currentVrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					currentVrf.setIsvrfLiteEnabled(prevVrfOptional.get().getIsvrfLiteEnabled());
					currentVrf.setModifiedBy(MODIFIEDBY);
					currentVrf.setServiceDetail(currentActiveServiceDetail);
					vrfRepository.save(currentVrf);
				}else {
					if (prevVrfOptional.isPresent() && Objects.isNull(prevVrfOptional.get().getVrfName())) {
						Vrf currentVrf = vrfOptional.get();
						currentVrf.setVrfName(prevVrfOptional.get().getVrfName());
						currentVrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						currentVrf.setModifiedBy(MODIFIEDBY);
						vrfRepository.save(currentVrf);
					}
				}
			}
		}
		logger.info("applyVrfDetailRule method ends");
	}

	private void applySvcDetailRule(ServiceDetail currentActiveServiceDetail,
			ServiceDetail prevActiveServiceDetail) {
		logger.info("applySvcDetailRule method invoked");
		if(Objects.isNull(currentActiveServiceDetail.getSvclinkSrvid()) 
				&& Objects.nonNull(prevActiveServiceDetail.getSvclinkSrvid())){
			currentActiveServiceDetail.setSvclinkSrvid(prevActiveServiceDetail.getSvclinkSrvid());
			currentActiveServiceDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			currentActiveServiceDetail.setModifiedBy(MODIFIEDBY);
			serviceDetailRepository.save(currentActiveServiceDetail);
		}
		if(Objects.isNull(currentActiveServiceDetail.getSvclinkRole()) 
				&& Objects.nonNull(prevActiveServiceDetail.getSvclinkRole())){
			currentActiveServiceDetail.setSvclinkRole(prevActiveServiceDetail.getSvclinkRole());
			currentActiveServiceDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			currentActiveServiceDetail.setModifiedBy(MODIFIEDBY);
			serviceDetailRepository.save(currentActiveServiceDetail);
		}
		logger.info("applySvcDetailRule method ends");
	}
	
	private void applyIPAddressDetailsRule(ServiceDetail currentActiveServiceDetail,
			ServiceDetail prevActiveServiceDetail) {
		logger.info("applyIPAddressDetailsRule method invoked");
		if(Objects.nonNull(currentActiveServiceDetail.getIpAddressDetails()) && Objects.nonNull(prevActiveServiceDetail.getIpAddressDetails())
				&& !currentActiveServiceDetail.getIpAddressDetails().isEmpty() && !prevActiveServiceDetail.getIpAddressDetails().isEmpty()){
			String currentPingAddr[]={null};
			currentActiveServiceDetail.getIpAddressDetails().stream().forEach(currentIpAddr -> {
				currentPingAddr[0]=currentIpAddr.getPingAddress2();
			});
			String prevPingAddr[]={null};
			prevActiveServiceDetail.getIpAddressDetails().stream().forEach(prevIpAddr -> {
				prevPingAddr[0]=prevIpAddr.getPingAddress2();
			});
			if(Objects.isNull(currentPingAddr[0]) && Objects.nonNull(prevPingAddr[0])){
				Optional<IpAddressDetail> currentIpAddressDetailOptional=currentActiveServiceDetail.getIpAddressDetails().stream().findFirst();
				if(currentIpAddressDetailOptional.isPresent()){
					logger.info("Ping Address2 exists in Old ServiceDetail");
					IpAddressDetail currentIpAddressDetail =currentIpAddressDetailOptional.get();
					currentIpAddressDetail.setPingAddress2(prevPingAddr[0]);
					currentIpAddressDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					currentIpAddressDetail.setModifiedBy(MODIFIEDBY);
					ipAddressDetailRepository.save(currentIpAddressDetail);
				}
			}
		}
		logger.info("applyIPAddressDetailsRule method invoked");
	}
	
	
	private void applyCpeDetailsRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("applyCpeDetailsRule method invoked");
		List<InterfaceProtocolMapping> currentCPEProtocolMappings = currentActiveServiceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
						&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
				.collect(Collectors.toList());
		List<InterfaceProtocolMapping> prevCPEProtocolMappings = prevActiveServiceDetail
				.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
						&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
				.collect(Collectors.toList());
		if(!currentCPEProtocolMappings.isEmpty() && !prevCPEProtocolMappings.isEmpty()){
			prevCPEProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping prevCPEProtocolMapping = prevCPEProtocolMappings.get(0);
			currentCPEProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping currentCPEProtocolMapping = currentCPEProtocolMappings.get(0);
			if (Objects.nonNull(prevCPEProtocolMapping) && Objects.nonNull(prevCPEProtocolMapping.getCpe()) 
					&& Objects.nonNull(currentCPEProtocolMapping) && Objects.nonNull(currentCPEProtocolMapping.getCpe())) {
				logger.info("Both CPE exists");
				Cpe currentCPE=currentCPEProtocolMapping.getCpe();
				Cpe prevCPE=prevCPEProtocolMapping.getCpe();
				applyCpeRule(currentCPE,prevCPE);
			}
		}
		logger.info("applyCpeDetailsRule method ends");
	}

	private void applyCpeRule(Cpe currentCPE, Cpe prevCPE) {
		logger.info("applyCpeRule method invoked");
		if(Objects.isNull(currentCPE.getHostName()) 
				&& Objects.nonNull(prevCPE.getHostName())){
			currentCPE.setHostName(prevCPE.getHostName());
		}
		if(Objects.isNull(currentCPE.getMake()) 
				&& Objects.nonNull(prevCPE.getMake())){
			currentCPE.setMake(prevCPE.getMake());
		}
		if(Objects.isNull(currentCPE.getModel()) 
				&& Objects.nonNull(prevCPE.getModel())){
			currentCPE.setModel(prevCPE.getModel());
		}
		if(Objects.isNull(currentCPE.getDeviceId()) 
				&& Objects.nonNull(prevCPE.getDeviceId())){
			currentCPE.setDeviceId(prevCPE.getDeviceId());
		}
		if(Objects.isNull(currentCPE.getSiteName()) 
				&& Objects.nonNull(prevCPE.getSiteName())){
			currentCPE.setSiteName(prevCPE.getSiteName());
		}
		if(Objects.isNull(currentCPE.getSiteType()) 
				&& Objects.nonNull(prevCPE.getSiteType())){
			currentCPE.setSiteType(prevCPE.getSiteType());
		}
		currentCPE.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		currentCPE.setModifiedBy(MODIFIEDBY);
		cpeRepository.save(currentCPE);
		logger.info("applyCpeRule method ends");
	}
	
	private void applyOrderDetailRule(OrderDetail currentOrderDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("applyOrderDetailRule method invoked");
		if(Objects.nonNull(prevActiveServiceDetail) 
				&& Objects.nonNull(prevActiveServiceDetail.getOrderDetail()) && Objects.nonNull(currentOrderDetail)){
			logger.info("Prev Order detail exists");
			OrderDetail prevOrderDetail=prevActiveServiceDetail.getOrderDetail();
			if(Objects.nonNull(prevOrderDetail.getCustomerCrnId())){
				currentOrderDetail.setCustomerCrnId(prevOrderDetail.getCustomerCrnId());
			}
			if(Objects.isNull(currentOrderDetail.getAluCustomerId()) 
					&& Objects.nonNull(prevOrderDetail.getAluCustomerId())){
				currentOrderDetail.setAluCustomerId(prevOrderDetail.getAluCustomerId());
			}
			if(Objects.isNull(currentOrderDetail.getSamCustomerDescription()) 
					&& Objects.nonNull(prevOrderDetail.getSamCustomerDescription())){
				currentOrderDetail.setSamCustomerDescription(prevOrderDetail.getSamCustomerDescription());
			}
			if(Objects.isNull(currentOrderDetail.getSfdcOpptyId()) 
					&& Objects.nonNull(prevOrderDetail.getSfdcOpptyId())){
				currentOrderDetail.setSfdcOpptyId(prevOrderDetail.getSfdcOpptyId());
			}
			if(Objects.isNull(currentOrderDetail.getCopfId()) 
					&& Objects.nonNull(prevOrderDetail.getCopfId())){
				currentOrderDetail.setCopfId(prevOrderDetail.getCopfId());
			}
			if(Objects.isNull(currentOrderDetail.getCustomerType()) 
					&& Objects.nonNull(prevOrderDetail.getCustomerType())){
				currentOrderDetail.setCustomerType(prevOrderDetail.getCustomerType());
			}
			currentOrderDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			currentOrderDetail.setModifiedBy(MODIFIEDBY);
			orderDetailRepository.save(currentOrderDetail);
		}
		logger.info("applyOrderDetailRule method ends");
	}
	
	private void applyCssSamMgrIdRule(ServiceDetail currentActiveServiceDetail, ServiceDetail prevActiveServiceDetail) {
		logger.info("applyCssSamMgrIdRule method invoked");
		if(Objects.isNull(currentActiveServiceDetail.getCssSammgrId()) 
				&& Objects.nonNull(prevActiveServiceDetail.getCssSammgrId())){
			currentActiveServiceDetail.setCssSammgrId(prevActiveServiceDetail.getCssSammgrId());
			currentActiveServiceDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			currentActiveServiceDetail.setModifiedBy(MODIFIEDBY);
			serviceDetailRepository.save(currentActiveServiceDetail);
		}
		logger.info("applyCssSamMgrIdRule method ends");
	}
	
	private void applyServiceDetailsRule(ServiceDetail serviceDetails, ServiceDetail prevActiveServiceDetail, Map<String, String> manDatoryFields, VpnMetatData vpnMetatData) throws TclCommonException {
		String vrfName = manDatoryFields.get(AceConstants.VRF.VRF_NAME);

		String routerMake = manDatoryFields.get(AceConstants.ROUTER.ROUTER_MAKE);
		MstVpnSamManagerId mstVpnSamManagerId=null;

		serviceDetails.setIsdowntimeReqd((byte) 0);
		serviceDetails.setExpediteTerminate((byte) 0);
		serviceDetails.setExternalRefid(null);
		serviceDetails.setServiceState("ISSUED");
		serviceDetails.setTriggerNccmOrder((byte) 0);
		serviceDetails.setIscustomConfigReqd((byte) 0);
		serviceDetails.setIntefaceDescSvctag("GVPN");
		String siteRole=getSiteRole(serviceDetails);

		if (Objects.nonNull(serviceDetails)) {
			formOrderType(serviceDetails);
		}
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
				Integer cssSamcustomerId = 0;
				if(Objects.nonNull(prevActiveServiceDetail.getCssSammgrId())){
					cssSamcustomerId=prevActiveServiceDetail.getCssSammgrId();
				}else{
					if (siteRole.toLowerCase().contains("hub") || siteRole.toLowerCase().contains("mesh")) {
						mstVpnSamManagerId = mstVpnmanagerIdRepository.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(
								vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails),siteRole);

					} else {
						mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
										"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(),
										siteRole);
					}
					
					if (mstVpnSamManagerId != null) {
						if(prevActiveServiceDetail.getCssSammgrId()!=Integer.valueOf(mstVpnSamManagerId.getSamMgrId())){
							logger.info("Copying SamMgr even mstVpn exists");	
							cssSamcustomerId =prevActiveServiceDetail.getCssSammgrId();
						}else{
							logger.info("Set mstVpn exists");	
							cssSamcustomerId =Integer.valueOf( mstVpnSamManagerId.getSamMgrId());
						}
					}
				}
				serviceDetails.setCssSammgrId(cssSamcustomerId);
			}
		}
		serviceDetails.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		serviceDetails.setModifiedBy(MODIFIEDBY);

		serviceDetailRepository.save(serviceDetails);
	}
	
	private boolean applyVpnSolutionNameRule(ServiceDetail serviceDetails, ServiceDetail prevActiveServiceDetail){
		logger.info("applyVpnSolutionNameRule");		
		boolean isSolutionNameSame=false;	
		if(Objects.nonNull(prevActiveServiceDetail) && Objects.nonNull(prevActiveServiceDetail.getVpnSolutions()) && !prevActiveServiceDetail.getVpnSolutions().isEmpty() 
				&& Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getVpnSolutions()) && !serviceDetails.getVpnSolutions().isEmpty()){	
			List<VpnSolution> prevVpnSolutionList=prevActiveServiceDetail.getVpnSolutions().stream().
			filter(prevVpnSol -> "CUSTOMER".equalsIgnoreCase(prevVpnSol.getVpnType())).collect(Collectors.toList());
			List<VpnSolution> currentVpnSolutionList=serviceDetails.getVpnSolutions().stream().
			filter(curVpnSol -> "CUSTOMER".equalsIgnoreCase(curVpnSol.getVpnType())).collect(Collectors.toList());
			if(Objects.nonNull(currentVpnSolutionList) && Objects.nonNull(prevVpnSolutionList) && !currentVpnSolutionList.isEmpty() && !prevVpnSolutionList.isEmpty()){
				logger.info("Prev & Current Vpn Customer Solution exists");	
				Optional<VpnSolution> prevOptionalVpnSolution=prevVpnSolutionList.stream().findFirst();
				Optional<VpnSolution> currentOptionalVpnSolution=currentVpnSolutionList.stream().findFirst();
				if(prevOptionalVpnSolution.isPresent() && currentOptionalVpnSolution.isPresent() 
						&& prevOptionalVpnSolution.get().getVpnSolutionName().equalsIgnoreCase(currentOptionalVpnSolution.get().getVpnSolutionName())){
					logger.info("solution name same");		
					isSolutionNameSame=true;
				}
			}
		}
		return isSolutionNameSame;
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

					applyRoutingProtocal(interfaceProtocolMapping,null, map, serviceDetails,"CE");

				}
			}
		} catch (Exception e) {
			logger.error("Exception in applyInterfaceRuleForCESide => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void applyAluSvcDetailRule(ServiceDetail currentActiveServiceDetail,ServiceDetail prevActiveServiceDetail){
		logger.info("applyAluSvcDetailRule method invoked");
		if(Objects.isNull(currentActiveServiceDetail.getAluSvcid()) 
				&& Objects.nonNull(prevActiveServiceDetail.getAluSvcid())){
			currentActiveServiceDetail.setAluSvcid(prevActiveServiceDetail.getAluSvcid());
			serviceDetailRepository.save(currentActiveServiceDetail);
		}
		if(Objects.isNull(currentActiveServiceDetail.getAluSvcName()) 
				&& Objects.nonNull(prevActiveServiceDetail.getAluSvcName())){
			currentActiveServiceDetail.setAluSvcName(prevActiveServiceDetail.getAluSvcName());
		}
		currentActiveServiceDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		currentActiveServiceDetail.setModifiedBy(MODIFIEDBY);
		serviceDetailRepository.save(currentActiveServiceDetail);
		logger.info("applyAluSvcDetailRule method ends");
	}
	
	private void applyIfRouterProtocolModified(Map<String, String> prevManDatoryFields,
			Map<String, String> currentManDatoryFields, ServiceDetail currentActiveServiceDetail, InterfaceProtocolMapping interfaceProtocolMapping, InterfaceProtocolMapping prevRouterInterfaceProtocolMapping) throws TclCommonException {
		logger.info("applyIfRouterProtocolModified  started {}");
		String prevRoutingProtocol=prevManDatoryFields.get(AceConstants.PROTOCOL.ROUTINGPROTOCAL);
		String currentRoutingProtocol=currentManDatoryFields.get(AceConstants.PROTOCOL.ROUTINGPROTOCAL);
		if(Objects.nonNull(currentRoutingProtocol) 
				/*&& currentManDatoryFields.containsKey(AceConstants.IPADDRESS.EXTENDEDLANENABLED) 
					&& currentManDatoryFields.get(AceConstants.IPADDRESS.EXTENDEDLANENABLED).equalsIgnoreCase("TRUE")
					&& !prevRoutingProtocol.equalsIgnoreCase(currentRoutingProtocol)*/){
			logger.info("Routing protocol field exists on both side");
			applyRoutingProtocal(interfaceProtocolMapping,prevRouterInterfaceProtocolMapping,currentManDatoryFields,currentActiveServiceDetail,"PE");
		}
	}

	private void applyInterfaceRuleForCESide(InterfaceProtocolMapping interfaceProtocolMapping,
			ServiceDetail serviceDetails, Map<String, String> map) throws TclCommonException {

		try {
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

	private void applyVPnSolution(ServiceDetail serviceDetails, ServiceDetail prevActiveServiceDetail, Map<String, String> manDatoryFields,
			VpnMetatData vpnMetatData) {

		if (serviceDetails.getVpnSolutions() != null && !serviceDetails.getVpnSolutions().isEmpty()) {
			serviceDetails.getVpnSolutions().forEach(vpnSolution -> {
				vpnSolution.setEndDate(new Timestamp(System.currentTimeMillis()));
				vpnSolutionRepository.save(vpnSolution);
			});
		}

		String custiomerName = serviceDetails.getOrderDetail().getCustomerName();// 6,6,4 word only space to be

		createCustomerSolutionRecord(vpnMetatData, serviceDetails,prevActiveServiceDetail, custiomerName);

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

		try {
			Vrf vrf = new Vrf();
			vrf.setVrfName("NOT_APPLICABLE");
			vrf.setIsvrfLiteEnabled((byte) 0);
			vrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			vrf.setModifiedBy(MODIFIEDBY);
			vrf.setServiceDetail(serviceDetails);
			vrfRepository.save(vrf);
			serviceDetails.addVrf(vrf);
		} catch (Exception e) {
			logger.error("Exception in applyVrf => {}", e);

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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
			policyTypeRepository.save(policyType1);
			PolicyType policyType2 = new PolicyType();
			policyType2.setVrf(vrf);
			policyType2.setIsvprnExportpolicy((byte)1);
			policyType2.setIsvprnExportPreprovisioned((byte)1);
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

	private void applyInterfaceRule(ServiceDetail serviceDetails, Map<String, String> map, ServiceDetail prevActiveServiceDetail) throws TclCommonException {
		//applyIpAddressRule(serviceDetails, map,prevActiveServiceDetail);

		List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
				.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null).collect(Collectors.toList());// based
																														// on
																														// version
		if (!interfaceProtocolMappings.isEmpty()) {
			interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
			InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);

			if (interfaceProtocolMapping != null) {

				if (interfaceProtocolMapping.getEthernetInterface() != null) {
					appyEthernetRule(interfaceProtocolMapping.getEthernetInterface(), map, serviceDetails,"PE");
				}

				if (interfaceProtocolMapping.getChannelizedSdhInterface() != null) {
					applyChannelizedSdhInterfaceRule(interfaceProtocolMapping.getChannelizedSdhInterface(), map,
							serviceDetails,"PE");
				}

				if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null) {
					applyChannelizedE1serialInterface(interfaceProtocolMapping.getChannelizedE1serialInterface(), map,
							serviceDetails,"PE");
				}
			}
		}
	}

	private void applyIpAddressRule(ServiceDetail serviceDetails, Map<String, String> map, ServiceDetail prevActiveServiceDetail) throws TclCommonException {

		IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
		
		IpAddressDetail oldIpAddressDetail = prevActiveServiceDetail.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

		String oldIpPath = oldIpAddressDetail.getPathIpType();

		String currentIpPath = ipAddressDetail.getPathIpType();

		List<String> ftiLanv4Data = new ArrayList<>();
		List<String> ftiLanv6Data = new ArrayList<>();

		boolean ftiStatus = serviceActivationService.getFtiLanData(ipAddressDetail, ftiLanv4Data, ftiLanv6Data);

		if (oldIpPath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL)
				&& currentIpPath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL)){
			if(!ftiStatus)
				applyLanv4Rule(ipAddressDetail, oldIpAddressDetail);
			applyLanv6Rule(ipAddressDetail, oldIpAddressDetail);

			if (ipAddressDetail.getExtendedLanEnabled() == 0) {
				applyWanv4Rule(ipAddressDetail, oldIpAddressDetail);
				applyWanv6Rule(ipAddressDetail, oldIpAddressDetail);
			}
		}else if((oldIpPath.equalsIgnoreCase(AceConstants.IPADDRESS.DUAL)
				&& currentIpPath.equalsIgnoreCase(AceConstants.IPADDRESS.IPV4)) || (oldIpPath.equalsIgnoreCase(AceConstants.IPADDRESS.IPV4)
				&& currentIpPath.equalsIgnoreCase(AceConstants.IPADDRESS.IPV4))){
			if(!ftiStatus)
				applyLanv4Rule(ipAddressDetail,oldIpAddressDetail);
		}
		if(ftiStatus)
			serviceActivationService.applyLanFTIRule(ipAddressDetail,ftiLanv4Data, ftiLanv6Data);

		VpnSolution vpnSolution = serviceDetails.getVpnSolutions().stream().filter(vpn -> vpn.getEndDate() == null)
				.findFirst().orElse(null);

		VpnMetatData vpnMetatData = serviceDetails.getVpnMetatDatas().stream().findFirst().orElse(null);

		if (ipAddressDetail != null) {

			if (vpnMetatData != null && vpnMetatData.getManagementVpnType1() != null
					&& vpnMetatData.getManagementVpnType1().equalsIgnoreCase("NAT")) {
				ipAddressDetail.setPingAddress1("121.244.227.114");
				//ipAddressDetail.setNmsServiceIpv4Address("121.244.227.113");
			}

			else if (serviceDetails.getScopeOfMgmt() != null
					&& (serviceDetails.getScopeOfMgmt().equalsIgnoreCase("Fully Managed")
							|| serviceDetails.getScopeOfMgmt().equalsIgnoreCase("proactive Monitoring")
							|| serviceDetails.getScopeOfMgmt().toLowerCase().contains("config")
							|| serviceDetails.getScopeOfMgmt().contains("proactive"))) {
				ipAddressDetail.setPingAddress1("10.70.0.202");
				//ipAddressDetail.setNmsServiceIpv4Address("10.70.0.201");

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
	
	private void applyWanv6Rule(IpAddressDetail ipAddressDetail, IpAddressDetail oldIpAddressDetail) {

		if (oldIpAddressDetail.getIpaddrWanv6Addresses() != null) {

			Map<String, IpaddrWanv6Address> currentWanv6Map = new HashMap<>();
			if (ipAddressDetail.getIpaddrWanv6Addresses() != null) {

				ipAddressDetail.getIpaddrWanv6Addresses().stream().forEach(e -> {
					currentWanv6Map.put(e.getWanv6Address(), e);
				});
			}
			List<IpaddrWanv6Address> oldWanv6Set = oldIpAddressDetail.getIpaddrWanv6Addresses().stream()
					.collect(Collectors.toList());

			oldWanv6Set.stream().forEach(e -> {
				if (!currentWanv6Map.containsKey(e.getWanv6Address())) {
					IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
					ipaddrWanv6Address.setIpAddressDetail(ipAddressDetail);
					ipaddrWanv6Address.setIscustomerprovided(e.getIscustomerprovided());
					ipaddrWanv6Address.setWanv6Address(e.getWanv6Address().trim());
					ipaddrWanv6Address.setIssecondary(Objects.nonNull(e.getIssecondary())?e.getIssecondary():(byte)0);
					ipaddrWanv6Address.setStartDate(new Timestamp(System.currentTimeMillis()));
					ipaddrWanv6Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					ipaddrWanv6Address.setModifiedBy("OPTIMUS_RULE");
					ipaddrWanv6AddressRepository.save(ipaddrWanv6Address);
					ipAddressDetail.getIpaddrWanv6Addresses().add(ipaddrWanv6Address);
				}
			});
		}
	
	}

	private void applyWanv4Rule(IpAddressDetail ipAddressDetail, IpAddressDetail oldIpAddressDetail) {
		logger.info("applyWanv4Rule invoked");
		if (oldIpAddressDetail.getIpaddrWanv4Addresses() != null) {
			logger.info("applyWanv4Rule.Prev WanV4 exists");
			Map<String, IpaddrWanv4Address> currentWanv4Map = new HashMap<>();
			if (ipAddressDetail.getIpaddrWanv4Addresses() != null) {
				logger.info("applyWanv4Rule.Current WanV4 exists");
				ipAddressDetail.getIpaddrWanv4Addresses().stream().forEach(e -> {
					currentWanv4Map.put(e.getWanv4Address(), e);
				});
			}
			List<IpaddrWanv4Address> oldWanv4Set = oldIpAddressDetail.getIpaddrWanv4Addresses().stream()
					.collect(Collectors.toList());

			oldWanv4Set.stream().forEach(e -> {
				if (!currentWanv4Map.containsKey(e.getWanv4Address())) {
					logger.info("applyWanv4Rule.Current WanV4 not contains old Wanv4 Address::{}",e.getWanv4Address());
					IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
					ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
					ipaddrWanv4Address.setIscustomerprovided(e.getIscustomerprovided());
					ipaddrWanv4Address.setWanv4Address(e.getWanv4Address().trim());
					ipaddrWanv4Address.setIssecondary(Objects.nonNull(e.getIssecondary())?e.getIssecondary():(byte)0);
					ipaddrWanv4Address.setStartDate(new Timestamp(System.currentTimeMillis()));
					ipaddrWanv4Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					ipaddrWanv4Address.setModifiedBy("OPTIMUS_RULE");
					ipaddrWanv4AddressRepository.save(ipaddrWanv4Address);
					
					ipAddressDetail.getIpaddrWanv4Addresses().add(ipaddrWanv4Address);

				}
			});
		}
	
	}

	private void applyLanv6Rule(IpAddressDetail ipAddressDetail, IpAddressDetail oldIpAddressDetail) {

		if (oldIpAddressDetail.getIpaddrLanv6Addresses() != null) {

			Map<String, IpaddrLanv6Address> currentLanv6Map = new HashMap<>();
			if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {

				ipAddressDetail.getIpaddrLanv6Addresses().stream().forEach(e -> {
					currentLanv6Map.put(e.getLanv6Address(), e);
				});
			}
			List<IpaddrLanv6Address> oldLanv6Set = oldIpAddressDetail.getIpaddrLanv6Addresses().stream()
					.collect(Collectors.toList());

			oldLanv6Set.stream().forEach(e -> {
				if (!currentLanv6Map.containsKey(e.getLanv6Address())) {
					IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
					ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
					ipaddrLanv6Address.setIscustomerprovided(e.getIscustomerprovided());
					ipaddrLanv6Address.setLanv6Address(e.getLanv6Address().trim());
					ipaddrLanv6Address.setIssecondary(Objects.nonNull(e.getIssecondary())?e.getIssecondary():(byte)0);
					ipaddrLanv6Address.setStartDate(new Timestamp(System.currentTimeMillis()));
					ipaddrLanv6Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					ipaddrLanv6Address.setModifiedBy("OPTIMUS_RULE");
					if(e.getLanv6Address().contains(";") || e.getLanv6Address().contains(","))
						logger.info("Invalid Lanv6Address: {} ", e.getLanv6Address());
					else {
						ipaddrLanv6AddressRepository.save(ipaddrLanv6Address);
						ipAddressDetail.getIpaddrLanv6Addresses().add(ipaddrLanv6Address);
					}
				}
			});
		}
	
	}

	private void applyLanv4Rule(IpAddressDetail ipAddressDetail, IpAddressDetail oldIpAddressDetail) {
		
		ScComponentAttribute lanIpProvidedBy = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						ipAddressDetail.getServiceDetail().getScServiceDetailId(), "lanIpProvidedBy", "LM", "A");

		if (oldIpAddressDetail.getIpaddrLanv4Addresses() != null && (lanIpProvidedBy == null || !lanIpProvidedBy.getAttributeValue().equalsIgnoreCase("TCL"))) {

			Map<String, IpaddrLanv4Address> currentLanv4Map = new HashMap<>();
			if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {

				ipAddressDetail.getIpaddrLanv4Addresses().stream().forEach(e -> {
					currentLanv4Map.put(e.getLanv4Address(), e);
				});
			}
			List<IpaddrLanv4Address> oldLanv4Set = oldIpAddressDetail.getIpaddrLanv4Addresses().stream()
					.collect(Collectors.toList());

			oldLanv4Set.stream().forEach(e -> {
				if (!currentLanv4Map.containsKey(e.getLanv4Address())) {
					IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
					ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
					ipaddrLanv4Address.setIscustomerprovided(e.getIscustomerprovided());
					ipaddrLanv4Address.setLanv4Address(e.getLanv4Address().trim());
					ipaddrLanv4Address.setIssecondary(Objects.nonNull(e.getIssecondary())?e.getIssecondary():(byte)0);
					ipaddrLanv4Address.setStartDate(new Timestamp(System.currentTimeMillis()));
					ipaddrLanv4Address.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
					ipaddrLanv4Address.setModifiedBy("OPTIMUS_RULE");
					if(e.getLanv4Address().contains(";") || e.getLanv4Address().contains(","))
						logger.info("Invalid Lanv4Address from applyLanv4Rule:: {} ", e.getLanv4Address());
					else {
						logger.info("Valid Lanv4Address from applyLanv4Rule: {} ", e.getLanv4Address());
						if(!e.getLanv4Address().contains("/32")) {
							ipaddrLanv4AddressRepository.save(ipaddrLanv4Address);
							ipAddressDetail.getIpaddrLanv4Addresses().add(ipaddrLanv4Address);
						}
					}
				}
			});
		}
	
	}

	private void appyEthernetRule(EthernetInterface ethernetInterface, Map<String, String> map,
			ServiceDetail serviceDetails, String type) throws TclCommonException {
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

			int count=1;

			if(type!=null && type.equalsIgnoreCase("CE")){
				count=2;
			}

			if (map.containsKey(AceConstants.IPADDRESS.LANV4ADDRESS)) {
				ethernetInterface
						.setModifiedIpv4Address(getIpAddressSplit(map.get(AceConstants.IPADDRESS.LANV4ADDRESS), count).trim()
								+ "/" + subnet(map.get(AceConstants.IPADDRESS.LANV4ADDRESS)).trim());

				if (map.containsKey(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY))
					ethernetInterface.setModifiedSecondaryIpv4Address(
							getIpAddressSplit(map.get(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY), count).trim() + "/"
									+ subnet(map.get(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY)).trim());
			}

			if (map.containsKey(AceConstants.IPADDRESS.LANV6ADDRESS)) {
				ethernetInterface
						.setModifiedIpv6Address(getIpAddressSplit(map.get(AceConstants.IPADDRESS.LANV6ADDRESS), count).trim()
								+ "/" + subnet(map.get(AceConstants.IPADDRESS.LANV6ADDRESS)).trim());
				if (map.containsKey(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY)) {
					ethernetInterface.setModifiedSecondaryIpv6Address(
							addCountIpv6Address(map.get(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY), count).trim() + "/"
									+ subnet(map.get(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY)).trim());
				}
			}

		} else {

			if (map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS)) {
				if(Objects.nonNull(ethernetInterface.getIpv4Address()) && Objects.nonNull(map.get(AceConstants.IPADDRESS.WANV4ADDRESS))){
					ethernetInterface.setModifiedIpv4Address(ethernetInterface.getIpv4Address().trim() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS)).trim());
				}

				if(map.containsKey(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY) 
						&& Objects.nonNull(ethernetInterface.getSecondaryIpv4Address()) && Objects.nonNull(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY))){
						ethernetInterface.setModifiedSecondaryIpv4Address(ethernetInterface.getSecondaryIpv4Address().trim()
								+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY)).trim());
				}
			}

			if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS)) {
				if(Objects.nonNull(ethernetInterface.getIpv6Address()) && Objects.nonNull(map.get(AceConstants.IPADDRESS.WANV6ADDRESS))){
					ethernetInterface.setModifiedIpv6Address(ethernetInterface.getIpv6Address().trim() + "/"
							+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS)));
				}
				
				if (map.containsKey(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)
						&& Objects.nonNull(ethernetInterface.getSecondaryIpv6Address()) && Objects.nonNull(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY))) {
					ethernetInterface.setModifiedSecondaryIpv6Address(ethernetInterface.getSecondaryIpv6Address().trim()
							+ "/" + subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY)).trim());
				}
			}
		}
		

		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER) && AceConstants.INTERFACE.XGIGABIT_ETHERNET
				.contains(ethernetInterface.getPhysicalPort().toUpperCase())) {
			ethernetInterface.setFraming(AceConstants.INTERFACE.LAN_PHY);

		} else if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			ethernetInterface.setFraming(AceConstants.DEFAULT.NOT_APPLICABLE);

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
		}

		if (ethernetInterface.getMode() == null) {
			ethernetInterface.setMode(AceConstants.INTERFACE.MODE);

		}
		if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

			ethernetInterface.setMode(AceConstants.INTERFACE.MODE);
			//ethernetInterface.setEncapsulation("NULL");//Abhay requested->Apr4,2020
		} else if (routerMake != null && routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			ethernetInterface.setMode(null);
			//ethernetInterface.setEncapsulation("NULL");//Abhay requested->Apr4,2020

		}

		ethernetInterface.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		ethernetInterface.setModifiedBy(MODIFIEDBY);
		if (ethernetInterface.getIsbfdEnabled() != null && ethernetInterface.getIsbfdEnabled() == 0) {
			ethernetInterface.setBfdtransmitInterval(null);
			ethernetInterface.setBfdreceiveInterval(null);
			ethernetInterface.setBfdMultiplier(null);
		}

		ethernetInterfaceRepository.save(ethernetInterface);
		//saveAclPolicyCriteriaForEthernet(serviceDetails, map.get(AceConstants.IPADDRESS.IPPATH), ethernetInterface);
		map.put(AceConstants.INTERFACE.ETHRNER_INNERV_LAN, ethernetInterface.getInnerVlan());
		map.put(AceConstants.INTERFACE.ETHRNER_OUTERV_LAN, ethernetInterface.getOuterVlan());

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
		/*saveAclPolicyCriteriaForChannelizedE1serialInterface(serviceDetails, map.get(AceConstants.IPADDRESS.IPPATH),
				channelizedE1serialInterface);*/

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
		/*saveAclPolicyCriteriaForChannelizedSdhInterface(serviceDetails, map.get(AceConstants.IPADDRESS.IPPATH),
				channelizedSdhInterface);*/

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
			
			if(type.equalsIgnoreCase("v6")|| type.equals("DUALSTACK") ) {

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
			serviceQo.setSummationOfBw("0.0");

			serviceQo.setIsflexicos((byte) 0);
			serviceQo.setIsbandwidthApplicable((byte) 1);
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

	private void applyOrderRule(OrderDetail orderDetail, ServiceDetail serviceDetails, Map<String, String> map,ServiceDetail prevActiveServiceDetail, VpnMetatData vpnMetatData) throws TclCommonException {
		String vrfName = map.get(AceConstants.VRF.VRF_NAME);
		logger.info("vrfName ={}",vrfName);
		if(vrfName==null) vrfName=AceConstants.DEFAULT.NOT_APPLICABLE;
		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		
		if(orderDetail.getCustomerCrnId()==null) {
				logger.error("CRN Id not found for serviceId : {} ",serviceDetails.getServiceId()) ;
				throw new TclCommonException(ExceptionConstants.NO_CRN_FOUND, ResponseResource.R_CODE_NOT_FOUND);
			
		}
		MstVpnSamManagerId mstVpnSamManagerId=null;
		orderDetail.setAsdOpptyFlag((byte) 0);
		orderDetail.setModifiedBy(MODIFIEDBY);
		orderDetail.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		/*if(Objects.nonNull(prevActiveServiceDetail) && Objects.nonNull(serviceDetails)){
			formOrderType(prevActiveServiceDetail,serviceDetails,orderDetail);
		}
		if(orderDetail.getExtOrderrefId()!=null && orderDetail.getExtOrderrefId().toLowerCase().contains("izosdwan")
				&& serviceDetails.getOrderCategory()!=null && serviceDetails.getOrderCategory().equalsIgnoreCase("CHANGE_ORDER")
				&& serviceDetails.getOrderSubCategory()==null){
			logger.info("IZOSDWAN Order::{}",serviceDetails.getId());
			serviceDetails.setOrderType("HOT_UPGRADE");
		}
		}*/
		orderDetail.setGroupId("CMIP");
		//orderDetail.setOriginatorName("OPTIMUS");
		orderDetail.setOriginatorName(cramerSourceSystem);
		orderDetail.setOriginatorDate(new Timestamp(System.currentTimeMillis()));
		orderDetail.setLocation(orderDetail.getCity());
		orderDetail.setOrderStatus("IN PROGRESS");// other status are cancelled ,closed,jeopardy
		orderDetail.setOrderUuid(AceConstants.ORDER.SERVICE_MACD_ + orderDetail.getExtOrderrefId());
		String siteRole=getSiteRole(serviceDetails);

		if (routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			

			if(Objects.nonNull(prevActiveServiceDetail.getOrderDetail()) 
					&& Objects.nonNull(prevActiveServiceDetail.getOrderDetail().getSamCustomerDescription())){
				orderDetail.setSamCustomerDescription(prevActiveServiceDetail.getOrderDetail().getSamCustomerDescription());
			}else{
				if (vrfName.equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)) {
					orderDetail.setSamCustomerDescription("INTERNET-VPN");
				} else if (vrfName.equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {
					orderDetail.setSamCustomerDescription("PRIMUS_INTERNET");
				} else if (map.containsKey(AceConstants.VRF.VRF_NAME) && map.containsKey(AceConstants.SERVICE.SERVICE_TYPE)
						&& Objects.nonNull(map.get(AceConstants.VRF.VRF_NAME)) && AceConstants.DEFAULT.NOT_APPLICABLE.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))
						|| Objects.nonNull(map.get(AceConstants.VRF.VRF_NAME)) && AceConstants.DEFAULT.NA.equalsIgnoreCase(map.get(AceConstants.VRF.VRF_NAME))) {
					if (orderDetail.getCustomerName().length() > 18) {
						orderDetail.setSamCustomerDescription(
								orderDetail.getCustomerName().substring(0, 17).replaceAll(" ", "_"));
					} else {
						orderDetail.setSamCustomerDescription(orderDetail.getCustomerName().replaceAll(" ", "_"));
					}

				}
			}
			
			

			if (vrfName.equalsIgnoreCase(AceConstants.VRF.INTERNET_VPN)) {
				orderDetail.setAluCustomerId("4755");
			} else if (vrfName.equalsIgnoreCase(AceConstants.VRF.PRIMUS_INTERNET)) {
				orderDetail.setAluCustomerId("584");

			} else if (vrfName != null) {
				
				if(Objects.nonNull(prevActiveServiceDetail.getOrderDetail()) 
						&& Objects.nonNull(prevActiveServiceDetail.getOrderDetail().getAluCustomerId())){
					orderDetail.setAluCustomerId(prevActiveServiceDetail.getOrderDetail().getAluCustomerId());
				}else{
				Integer cssSamcustomerId = 0;// for ill ,it should be increment
				
				if(siteRole.equalsIgnoreCase("SPOKE")) {
					
					mstVpnSamManagerId = mstVpnmanagerIdRepository
							.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
									"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(),
									siteRole);
					
					String cssSamMgrId=null;
					if (mstVpnSamManagerId == null) {
						ScServiceDetail activeScServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceDetails.getServiceId(),"ACTIVE");
						if(Objects.nonNull(activeScServiceDetail) && (Objects.nonNull(activeScServiceDetail.getSiteTopology()) 
								&& "spoke".equalsIgnoreCase(siteRole) && siteRole.equalsIgnoreCase(activeScServiceDetail.getSiteTopology()) || Objects.isNull(activeScServiceDetail.getSiteTopology()))){
							logger.info("Previous SAM MGMRID exists for Spoke");		
							if(Objects.nonNull(prevActiveServiceDetail.getCssSammgrId()))
							cssSamMgrId = prevActiveServiceDetail.getCssSammgrId().toString();
						}
						if(Objects.isNull(cssSamMgrId)){
							AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
									serviceDetails.getServiceSubtype());
							cssSamMgrId=aluSammgrSeq.getSammgrSeqid().toString();
						}
						saveMstVpnSamManagerId(cssSamMgrId, serviceDetails, vpnMetatData,
								getTopologyName(serviceDetails), siteRole);
					}
					if (orderDetail.getCustomerCrnId() != null) {
						MstGvpnAluCustId gvpnAluCustId = mstGvpnAluCustIdRepository
								.findByCrnId(orderDetail.getCustomerCrnId());
						if (gvpnAluCustId != null) {
							orderDetail.setAluCustomerId(gvpnAluCustId.getAluCustId());
						} else {
							orderDetail.setAluCustomerId(cssSamMgrId);
						}
					} 
				} else {

					if (siteRole.toLowerCase().contains("hub") || siteRole.toLowerCase().contains("mesh")) {
						mstVpnSamManagerId = mstVpnmanagerIdRepository.findByVpnNameAndVpnTypeAndVpnTopologyAndSiteRoleNot(
								vpnMetatData.getVpnName(), "CUSTOMER", getTopologyName(serviceDetails),siteRole);

					} else {
						mstVpnSamManagerId = mstVpnmanagerIdRepository
								.findByVpnNameAndVpnTypeAndVpnTopologyAndServiceCodeAndSiteRole(vpnMetatData.getVpnName(),
										"CUSTOMER", getTopologyName(serviceDetails), serviceDetails.getServiceId(),
										siteRole);
					}

					if (mstVpnSamManagerId != null) {
							if(prevActiveServiceDetail.getCssSammgrId()!=Integer.valueOf(mstVpnSamManagerId.getSamMgrId())){
								logger.info("Copying SamMgr even mstVpn exists");	
								cssSamcustomerId =prevActiveServiceDetail.getCssSammgrId();
							}else{
								logger.info("Set mstVpn exists");	
								cssSamcustomerId =Integer.valueOf( mstVpnSamManagerId.getSamMgrId());
							}
					} else {
						ScServiceDetail activeScServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceDetails.getServiceId(),"ACTIVE");
						if(Objects.nonNull(activeScServiceDetail) && Objects.nonNull(activeScServiceDetail.getSiteTopology()) 
								&& "spoke".equalsIgnoreCase(siteRole) && siteRole.equalsIgnoreCase(activeScServiceDetail.getSiteTopology())){
							logger.info("Previous SAM MGMRID exists for Spoke");		
							cssSamcustomerId = prevActiveServiceDetail.getCssSammgrId();
						}
						if(cssSamcustomerId==0){
							AluSammgrSeq aluSammgrSeq = getAluSamMgrSeq(serviceDetails.getServiceId(),
									serviceDetails.getServiceSubtype());
							cssSamcustomerId = aluSammgrSeq.getSammgrSeqid();
						}
						saveMstVpnSamManagerId(cssSamcustomerId.toString(), serviceDetails, vpnMetatData,
								getTopologyName(serviceDetails),siteRole);
					}
					if (orderDetail.getCustomerCrnId() != null) {
						MstGvpnAluCustId gvpnAluCustId = mstGvpnAluCustIdRepository
								.findByCrnId(orderDetail.getCustomerCrnId());
						if (gvpnAluCustId != null) {
							orderDetail.setAluCustomerId(gvpnAluCustId.getAluCustId());
						} else {
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
		}
		if (orderDetail.getCustomerCategory() == null) {
			orderDetail.setCustomerCategory("Enterprise");
		}
		orderDetailRepository.save(orderDetail);

	}
	
	private void formOrderType(ServiceDetail currentServiceDetail) {
		logger.info("formOrderType");
		String orderCategory = currentServiceDetail.getOrderCategory();
		String orderSubCategory = currentServiceDetail.getOrderSubCategory();
		if(Objects.nonNull(currentServiceDetail) 
				&& CramerConstants.CHANGE_BANDWIDTH_SERVICE.equals(orderCategory)){
				//formOrderTypeBasedOnBandwidth(prevActiveServiceDetail,currentServiceDetail,currentOrderDetail);
			logger.info("CB::{}",currentServiceDetail.getId());
			if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("hot") && orderSubCategory.toLowerCase().contains("upgrade")){
				logger.info("Hot Upgrade");
				currentServiceDetail.setOrderType("HOT_UPGRADE");
			}else if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("hot") && orderSubCategory.toLowerCase().contains("downgrade")){
				logger.info("Hot Downgrade");
				currentServiceDetail.setOrderType("HOT_DOWNGRADE");
			}
		}else if (Objects.nonNull(currentServiceDetail)
				&& CramerConstants.ADD_IP_SERVICE.equals(orderCategory)) {
			logger.info("ADDIP::{}",currentServiceDetail.getId());
			currentServiceDetail.setOrderType("HOT_UPGRADE");
		}else if (Objects.nonNull(currentServiceDetail) && Objects.nonNull(orderSubCategory)
				&& CramerConstants.SHIFT_SITE_SERVICE.equals(orderCategory)){
			logger.info("SHIFTSITE::{}",currentServiceDetail.getId());
			if(orderSubCategory.toLowerCase().contains("hot") && orderSubCategory.toLowerCase().contains("upgrade")){
				logger.info("Hot Upgrade");
				currentServiceDetail.setOrderType("HOT_UPGRADE");
			}else if(orderSubCategory.toLowerCase().contains("hot") && orderSubCategory.toLowerCase().contains("downgrade")){
				logger.info("Hot Downgrade");
				currentServiceDetail.setOrderType("HOT_DOWNGRADE");
			}else if(orderSubCategory.toLowerCase().contains("lm") || orderSubCategory.toLowerCase().contains("bso")
					|| orderSubCategory.equalsIgnoreCase("Shifting")){
				logger.info("lm shifting or bso change");
				currentServiceDetail.setOrderType("CHANGE_ORDER");
			}
		}
	}

	private void formOrderTypeBasedOnBandwidth(ServiceDetail prevActiveServiceDetail,
			ServiceDetail currentServiceDetail, OrderDetail currentOrderDetail) {
		String oldBw="0",currentBw="0";
		if(Objects.nonNull(prevActiveServiceDetail.getServiceBandwidth())&&Objects.nonNull(prevActiveServiceDetail.getServiceBandwidthUnit())){
			logger.info("Prev ServiceBw exists");
			oldBw=setBandwidthConversion(String.valueOf(prevActiveServiceDetail.getServiceBandwidth()),prevActiveServiceDetail.getServiceBandwidthUnit());
		}
		if(Objects.nonNull(currentServiceDetail.getServiceBandwidth())&&Objects.nonNull(currentServiceDetail.getServiceBandwidthUnit())){
			logger.info("Current ServiceBw exists");
			currentBw=setBandwidthConversion(String.valueOf(currentServiceDetail.getServiceBandwidth()),String.valueOf(currentServiceDetail.getServiceBandwidthUnit()));
		}
		
		String updatedOrderType=compareBwValues("", oldBw, currentBw);
		if(!updatedOrderType.equals(CommonConstants.EQUAL)){
			currentServiceDetail.setOrderType("HOT_"+updatedOrderType.toUpperCase());
		}else{
			String oldBurstableBw="0",currentBurstableBw="0";
			if(Objects.nonNull(prevActiveServiceDetail.getBurstableBw())&&Objects.nonNull(prevActiveServiceDetail.getBurstableBwUnit())){
				logger.info("Prev BurstableBw exists");
				oldBurstableBw=setBandwidthConversion(String.valueOf(prevActiveServiceDetail.getBurstableBw()),prevActiveServiceDetail.getBurstableBwUnit());
			}
			if(Objects.nonNull(currentServiceDetail.getBurstableBw())&&Objects.nonNull(currentServiceDetail.getBurstableBwUnit())){
				logger.info("Current BurstableBw exists");
				currentBurstableBw=setBandwidthConversion(String.valueOf(currentServiceDetail.getBurstableBw()),currentServiceDetail.getBurstableBwUnit());
			}
			updatedOrderType=compareBwValues("", oldBurstableBw, currentBurstableBw);
			currentServiceDetail.setOrderType(updatedOrderType.equals(CommonConstants.EQUAL)?"HOT_UPGRADE":"HOT_"+updatedOrderType.toUpperCase());
		}
	}
	
	public String setBandwidthConversion(String bandwidth, String bandwidthUnit)
	{
		Double bandwidthValue=Double.parseDouble(bandwidth.trim());
		logger.info("Bandwidth Value in setBandwidthConversion {}",bandwidth);
		logger.info("Bandwidth Unit in setBandwidthConversion {}",bandwidthUnit);

		if(Objects.nonNull(bandwidth)&&Objects.nonNull(bandwidthUnit))
		{
			switch (bandwidthUnit.trim().toLowerCase())
			{
				case "kbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue / 1024;
					bandwidth = bandwidthValue.toString();
					break;
				}
				case "gbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue * 1000;
					bandwidth = bandwidthValue.toString();
					break;
				}
				default:
					break;
			}

			int index=bandwidth.indexOf(".");
			if(index>0) {
				logger.info("bandwidth value" + bandwidth);
				String precisions = bandwidth.substring(index + 1);
				logger.info("precision value" + precisions);
				if (precisions.length() > 3) {
					DecimalFormat df = new DecimalFormat("#.###");
					df.setRoundingMode(RoundingMode.CEILING);
					String value = df.format(bandwidthValue);
					logger.info("Formatted value" + value);
					bandwidth = value;
				}
			}
		}
		logger.info("Resultant Bandwidth in setBandwidthConversion: {}",bandwidth);
		return bandwidth;
	}
	
	private String compareBwValues(String changeInLlBw,String oldValue,String newValue){
		Double value1=Double.valueOf(oldValue.trim());
		Double value2=Double.valueOf(newValue.trim());
		if(value2>value1)
			return CommonConstants.UPGRADE;
		else if(value2<value1)
			return CommonConstants.DOWNGRADE;
		else
			return CommonConstants.EQUAL;
	}

	private void saveMstVpnSamManagerId(String cssMgmrId, ServiceDetail serviceDetails, VpnMetatData vpnMetatData, String string, String siteRole) {
		MstVpnSamManagerId samManagerId=new MstVpnSamManagerId();
		samManagerId.setSamMgrId(cssMgmrId);
		samManagerId.setCreatedBy("OPTIMUS_RULE");
		OrderDetail orderDetail = serviceDetails.getOrderDetail();
		if(Objects.nonNull(orderDetail) && Objects.nonNull(orderDetail.getCustCuId())){
			samManagerId.setCuid(orderDetail.getCustCuId().toString());
		}
		samManagerId.setVpnType("CUSTOMER");
		if(!StringUtils.isEmpty(vpnMetatData.getL2OTopology())){
			if(vpnMetatData.getL2OTopology().toLowerCase().contains("hub")) {
				samManagerId.setVpnTopology("HUBnSPOKE");
			}
			else if(vpnMetatData.getL2OTopology().toLowerCase().contains("mesh")){
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
		Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String vpnTopology = attributeMap.getOrDefault("VPN Topology", null);
		return vpnTopology.toUpperCase().contains("HUB") ? "HUBnSPOKE" :"MESH";
	}
	
	private String getSiteRole(ServiceDetail serviceDetail) {

		List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),
						Arrays.asList("Site Type", "VPN Topology"));
		Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
		String siteType = attributeMap.getOrDefault("Site Type", null);
		return siteType != null ? siteType.toUpperCase() : siteType;

	}
	
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public AluSammgrSeq getAluSamMgrSeq(String serviceId, String subType) throws TclCommonException {			
		
		
			logger.info("Service Activation - saveAluSammgrSeq - started");
			try {
				AluSammgrSeq oldSammgrSeq = aluSammgrSeqRepository
						.findFirstByServiceIdOrderBySammgrSeqidDesc(serviceId);

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
		
		

	private void applyRoutingProtocal(InterfaceProtocolMapping interfaceProtocolMapping, InterfaceProtocolMapping prevRouterInterfaceProtocolMapping, Map<String, String> map,
			ServiceDetail serviceDetails, String type) throws TclCommonException {
		logger.info("applyRoutingProtocal  started {}");
		if (interfaceProtocolMapping.getBgp() != null) {
			if("CE".equals(type)){
				applyBgpRule(interfaceProtocolMapping.getBgp(), map, serviceDetails);
			}else if("PE".equals(type)){
				applyBgpPERule(interfaceProtocolMapping.getBgp(),prevRouterInterfaceProtocolMapping, map, serviceDetails);
			}
		}
		if (interfaceProtocolMapping.getStaticProtocol() != null) {
			applyStaticRule(interfaceProtocolMapping.getStaticProtocol(), serviceDetails);
		}

	}

	private void applyBgpPERule(Bgp bgp, InterfaceProtocolMapping prevRouterInterfaceProtocolMapping,
			Map<String, String> map, ServiceDetail serviceDetails) throws TclCommonException {

		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		if (bgp.getNeighborOn() == null) {

			bgp.setNeighborOn(AceConstants.IPADDRESS.WAN);
		}
		String redundancyRule=map.getOrDefault(AceConstants.SERVICE.REDUNDACY_ROLE,null);
		if (redundancyRule != null && (redundancyRule.equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)
				|| redundancyRule.equalsIgnoreCase(AceConstants.SERVICE.SECONDARY))) {
			bgp.setSooRequired((byte) 1);

		} else {
			bgp.setSooRequired((byte) 0);

		}

		bgp.setAsoOverride((byte) 1);
		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerName)) {
			bgp.setSplitHorizon((byte) 0);
		}else{
			bgp.setSplitHorizon((byte) 1);
		}

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

		}else{
			bgp.setIsbgpMultihopReqd((byte)0);
		}

		if (map.containsKey(AceConstants.VRF.VRF_NAME) && routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			bgp.setLocalAsNumber(AceConstants.PROTOCOL.LOCALASNUMBER);
		}

		if (Objects.nonNull(bgp.getRemoteAsNumber()) && bgp.getRemoteAsNumber() > 1 && bgp.getRemoteAsNumber() < 65535) {
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
			if (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("DUALSTACK")
					|| map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("v4")) {
				//bgp.setIsbgpNeighbourInboundv4RoutemapEnabled((byte) 1);
				//bgp.setBgpneighbourinboundv4routermapname(serviceDetails.getServiceId() + "_" + "IPv4_In");
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 1);
				if (map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase("PRIMARY")) {
					bgp.setBgpneighbourinboundv4routermapname("LP_PRIMARY");
				}
			}

			if (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("DUALSTACK")
					|| map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("v6")) {
				//bgp.setIsbgpNeighbourInboundv6RoutemapEnabled((byte) 1);
				//bgp.setBgpneighbourinboundv6routermapname(serviceDetails.getServiceId() + "_" + "IPv6_In");
				if (map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase("PRIMARY")) {
					bgp.setBgpneighbourinboundv6routermapname("LP_PRIMARY");
				}
				bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned((byte) 1);

			}

			bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned((byte) 0);
			bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 0);

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
				
				if(map.get(AceConstants.CPE.CPE_INTERFACE_IPV4).contains("/")){
					String ipv4[]=map.get(AceConstants.CPE.CPE_INTERFACE_IPV4).split("/");
					bgp.setRemoteIpv4Address(ipv4[0].trim());
				}else{
					bgp.setRemoteIpv4Address(map.get(
							AceConstants.CPE.CPE_INTERFACE_IPV4)/*
							 * + "/" + subnet(map.get(AceConstants.IPADDRESS.
							 * WANV4ADDRESS))
							 */);
				}
				 
			}
			if (map.containsKey(AceConstants.CPE.CPE_INTERFACE_IPV6)) {
				
				if(map.get(AceConstants.CPE.CPE_INTERFACE_IPV6).contains("/")){
					String ipv6[]=map.get(AceConstants.CPE.CPE_INTERFACE_IPV6).split("/");
					bgp.setRemoteIpv6Address(ipv6[0].trim());
				}else{
					bgp.setRemoteIpv6Address(map.get(
							AceConstants.CPE.CPE_INTERFACE_IPV6) /*
																	 * + "/" + subnet(map.get(AceConstants.IPADDRESS.
																	 * WANV6ADDRESS))
																	 */);
				}
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
		if(bgp.getPassword()!=null) {
			bgp.setAuthenticationMode("MD5");
			bgp.setIsauthenticationRequired((byte) 1);
		}
		bgp.setRoutesExchanged(null);
		applyBgpDetailRule(serviceDetails,bgp,map,routerName);
		OrderDetail orderDetail=serviceDetails.getOrderDetail();
		if(orderDetail!=null && orderDetail.getExtOrderrefId()!=null && orderDetail.getExtOrderrefId().toLowerCase().contains("izosdwan")){
			logger.info("GVPNMACD.applyBgpRulePE Setting Local preference for sdwan");
			bgp.setV6LocalPreference(null);
			bgp.setLocalPreference(null);
		}
		bgpRepository.save(bgp);

		applyBgpPolicyTypeRule(bgp, map, serviceDetails,prevRouterInterfaceProtocolMapping);
		applyBgpNeighbourCommunity(bgp,prevRouterInterfaceProtocolMapping);
	}

	private void applyBgpNeighbourCommunity(Bgp currentBgp,
			InterfaceProtocolMapping prevRouterInterfaceProtocolMapping) {
		logger.info("applyBgpNeighbourCommunity");
		if (Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp())
				&& Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes())
				&& !prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().isEmpty()
				&& Objects.nonNull(currentBgp.getPolicyTypes()) && !currentBgp.getPolicyTypes().isEmpty()) {
			logger.info("Both policy type exists");
			List<PolicyType> prevPolicyTypeList = prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().stream()
					.filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getInboundRoutingIpv4Policy())
							&& prevBgpPolicy.getInboundRoutingIpv4Policy() == (byte) 1)
					.collect(Collectors.toList());

			List<PolicyType> currentPolicyTypeList = currentBgp.getPolicyTypes().stream()
					.filter(currentBgpPolicy -> Objects.nonNull(currentBgpPolicy.getInboundRoutingIpv4Policy())
							&& currentBgpPolicy.getInboundRoutingIpv4Policy() == (byte) 1)
					.collect(Collectors.toList());
			for (PolicyType prevPolicyType : prevPolicyTypeList) {
				logger.info("Prev policy type");
				Set<PolicyTypeCriteriaMapping> prevPolicyTypeCriterias = prevPolicyType.getPolicyTypeCriteriaMappings();
				for (PolicyTypeCriteriaMapping prevPolicyTypeCriteria : prevPolicyTypeCriterias) {
					logger.info("Prev policy type criteria");
					Integer policyCriteriaId = prevPolicyTypeCriteria.getPolicyCriteriaId();
					PolicyCriteria prevPolicyCriteria = policyCriteriaRepository
							.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
					if (Objects.nonNull(prevPolicyCriteria)
							&& prevPolicyCriteria.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(prevPolicyCriteria.getMatchcriteriaNeighbourCommunity())) {
						logger.info("Prev policy criteria");
						for (NeighbourCommunityConfig prevNeighbourCommunity : prevPolicyCriteria
								.getNeighbourCommunityConfigs()) {
							logger.info("Prev neighbour community");

							for (PolicyType currentPolicyType : currentPolicyTypeList) {
								logger.info("Current policy type");
								Set<PolicyTypeCriteriaMapping> currentPolicyTypeCriterias = currentPolicyType
										.getPolicyTypeCriteriaMappings();
								for (PolicyTypeCriteriaMapping currentPolicyTypeCriteria : currentPolicyTypeCriterias) {
									logger.info("Current policy type criteria");
									Integer currentPolicyCriteriaId = currentPolicyTypeCriteria.getPolicyCriteriaId();
									PolicyCriteria currentPolicyCriteria = policyCriteriaRepository
											.findByPolicyCriteriaIdAndEndDateIsNull(currentPolicyCriteriaId);
									if (Objects.nonNull(currentPolicyCriteria)) {
										logger.info("Current policy criteria");
										NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
										neighbourCommunityConfig.setPolicyCriteria(currentPolicyCriteria);
										neighbourCommunityConfig.setCommunity(prevNeighbourCommunity.getCommunity());
										neighbourCommunityConfig.setName(prevNeighbourCommunity.getName());
										neighbourCommunityConfig
												.setLastModifiedDate(new Timestamp(new Date().getTime()));
										neighbourCommunityConfig.setModifiedBy("OPTIMUS_RULE");
										neighbourCommunityConfig.setStartDate(new Timestamp(new Date().getTime()));
										neighbourCommunityConfigRepository.save(neighbourCommunityConfig);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	private void applyBgpPolicyTypeRule(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			InterfaceProtocolMapping prevRouterInterfaceProtocolMapping) throws TclCommonException {
		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		String ippath = map.get(AceConstants.IPADDRESS.IPPATH);

		if ((ippath.equalsIgnoreCase("v4") || ippath.equalsIgnoreCase("DUALSTACK"))
				&& (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP))) {

			
			if(Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp()) && Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes()) && !prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().isEmpty()){
				List<PolicyType> policyTypeList=prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().stream().filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getInboundRoutingIpv4Policy()) &&prevBgpPolicy.getInboundRoutingIpv4Policy()==(byte)1).collect(Collectors.toList());
				if(Objects.nonNull(policyTypeList) && !policyTypeList.isEmpty()){
					PolicyType prevPolicyType=policyTypeList.get(0);
					if(Objects.nonNull(prevPolicyType.getInboundIpv4PolicyName())){
						createInboundPolicyV4Rule(bgp,prevRouterInterfaceProtocolMapping.getBgp(), map, serviceDetails, routerMake,prevPolicyType);
					}else{
						if(routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
								|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)){
							createInboundPolicyV4(bgp, map, serviceDetails, routerMake);
						}else if(routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP)){
							createInboundPolicyV4Cisco(bgp, map, serviceDetails,prevRouterInterfaceProtocolMapping.getBgp());
						}
					}
				}
			}else{
				createInboundPolicyIpV4(bgp, map, serviceDetails, routerMake,prevRouterInterfaceProtocolMapping);
			}
			
			
			if(Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp()) && Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes()) && !prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().isEmpty()){
				List<PolicyType> policyTypeList=prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().stream().filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getOutboundRoutingIpv4Policy()) && prevBgpPolicy.getOutboundRoutingIpv4Policy()==(byte)1).collect(Collectors.toList());
				if(Objects.nonNull(policyTypeList) && !policyTypeList.isEmpty()){
					PolicyType prevPolicyType=policyTypeList.get(0);
					if(Objects.nonNull(prevPolicyType.getOutboundIpv4PolicyName())){
						createOutboundPolicyAluV4Rule(bgp,prevRouterInterfaceProtocolMapping.getBgp(), map, serviceDetails,prevPolicyType);
					}else{
						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU) ) {
							createOutboundPolicyAluV4(bgp, map, serviceDetails);
						}
						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							createOutboundPolicyJuniperV4(bgp, map, serviceDetails);
						}
					}
				}
			}else {
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
					createOutboundPolicyAluV4(bgp, map, serviceDetails);
				}
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
					createOutboundPolicyJuniperV4(bgp, map, serviceDetails);
				}
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP) && Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp())) {
					logger.info("Outbound Cisco");
					String prevMatchPrefixListName=null;
					List<PolicyType> prevPolicyTypeList = prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().stream()
							.filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getOutboundRoutingIpv4Policy())
									&& prevBgpPolicy.getOutboundRoutingIpv4Policy() == (byte) 1)
							.collect(Collectors.toList());
					aa:
					for (PolicyType prevPolicyType : prevPolicyTypeList) {
						logger.info("Prev policy type");
						Set<PolicyTypeCriteriaMapping> prevPolicyTypeCriterias = prevPolicyType.getPolicyTypeCriteriaMappings();
						for (PolicyTypeCriteriaMapping prevPolicyTypeCriteria : prevPolicyTypeCriterias) {
							logger.info("Prev policy type criteria");
							Integer policyCriteriaId = prevPolicyTypeCriteria.getPolicyCriteriaId();
							logger.info("Policy type criteriaId::",policyCriteriaId);
							List<PolicyCriteria> prevPolicyCriterias = policyCriteriaRepository
									.findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(Arrays.asList(policyCriteriaId),(byte)1);
							if(Objects.nonNull(prevPolicyCriterias) && !prevPolicyCriterias.isEmpty()){
								logger.info("Prev policy criteria exists");
								Optional<PolicyCriteria> prevPolicyCriteriaOptional= prevPolicyCriterias.stream().findFirst();
								if(prevPolicyCriteriaOptional.isPresent()){
									logger.info("Prev Match Prefix list name exists");
									PolicyCriteria prevPolicyCriteria=prevPolicyCriteriaOptional.get();
									prevMatchPrefixListName=prevPolicyCriteria.getMatchcriteriaPrefixlistName();
									logger.info("Prev Match Prefix list name::",prevMatchPrefixListName);
									break aa;
								}
							}
							
						}
					}
					if(Objects.nonNull(prevMatchPrefixListName)){
						createOutboundPolicyCiscoV4(bgp, map, serviceDetails, prevMatchPrefixListName);
					}
				}
			}
		}
		
		if ((ippath.equalsIgnoreCase("v6") || ippath.equalsIgnoreCase("DUALSTACK"))
				&& (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)
						|| routerMake.equalsIgnoreCase(AceConstants.ROUTER.CISCO_IP))) {

			if(Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp()) && Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes()) && !prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().isEmpty()){
				List<PolicyType> policyTypeList=prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().stream().filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getInboundRoutingIpv6Policy()) && prevBgpPolicy.getInboundRoutingIpv6Policy()==(byte)1).collect(Collectors.toList());
				if(Objects.nonNull(policyTypeList) && !policyTypeList.isEmpty()){
					PolicyType prevPolicyType=policyTypeList.get(0);
					if(Objects.nonNull(prevPolicyType.getInboundIpv6PolicyName())){
						createInboundV6Rule(bgp,prevRouterInterfaceProtocolMapping.getBgp(), map, serviceDetails, routerMake,prevPolicyType);
					}else{
						createInboundV6(bgp, map, serviceDetails, routerMake);
					}
				}
			}else{
				createInboundIpV6(bgp, map, serviceDetails, routerMake);
			}
			
			
			
			if(Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp()) && Objects.nonNull(prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes()) && !prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().isEmpty()){
				List<PolicyType> policyTypeList=prevRouterInterfaceProtocolMapping.getBgp().getPolicyTypes().stream().filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getOutboundRoutingIpv6Policy()) && prevBgpPolicy.getOutboundRoutingIpv6Policy()==(byte)1).collect(Collectors.toList());
				if(Objects.nonNull(policyTypeList) && !policyTypeList.isEmpty()){
					PolicyType prevPolicyType=policyTypeList.get(0);
					if(Objects.nonNull(prevPolicyType.getOutboundIpv6PolicyName())){
						createOutboundPolicyAluV6Rule(bgp,prevRouterInterfaceProtocolMapping.getBgp(), map, serviceDetails,prevPolicyType);
					}else{
						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
							createOutboundPolicyAluV6(bgp, map, serviceDetails);
						}
						if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
							createOutboundPolicyJuniperV6(bgp, map, serviceDetails);
						}
					}
				}
			}else {
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
					createOutboundPolicyAluV6(bgp, map, serviceDetails);
				}
				if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
					createOutboundPolicyJuniperV6(bgp, map, serviceDetails);
				}
			}

		}

	}
	
	private void createOutboundPolicyCiscoV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,String prevMatchPrefixListName) throws TclCommonException {
		try {
			logger.info("createOutboundPolicyCiscoV4  started {}");

			PolicyType outBoundPolicy = new PolicyType();
			outBoundPolicy.setBgp(bgp);
			outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
			outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 0);
			outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			outBoundPolicy.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(outBoundPolicy);
			outBoundPolicy.setPolicyTypeCriteriaMappings(createOutBoundPolicyCriteraForBgp(outBoundPolicy, map, serviceDetails,prevMatchPrefixListName));
		} catch (Exception e) {
			logger.error("Exception in createOutboundPolicyCiscoV4 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
	
	private Set<PolicyTypeCriteriaMapping> createOutBoundPolicyCriteraForBgp(PolicyType outBoundPolicy,
			Map<String, String> map, ServiceDetail serviceDetails, String prevMatchPrefixListName) throws TclCommonException {

			try {
				Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();
				logger.info("createOutBoundPolicyCriteraForBgp with PrevBgp  started {}");
				String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
				createEnableOutBoundPrefixCriteriaForCisco(policyTypeCriteriaMappings, serviceDetails, routerMake, outBoundPolicy,prevMatchPrefixListName);
				return policyTypeCriteriaMappings;
			} catch (Exception e) {
				logger.error("Exception in createPolicyCriteraForBgp with PrevBgp => {}", e);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}

	}

	private void createEnableOutBoundPrefixCriteriaForCisco(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType outBoundPolicy,
			String prevMatchPrefixListName) throws TclCommonException {
		try {
			logger.info("createEnableOutBoundPrefixCriteriaForCisco with PrevPrefix started {}");

			PolicyCriteria enablePrefix = new PolicyCriteria();
			enablePrefix.setMatchcriteriaPrefixlist((byte) 0);
			enablePrefix.setMatchcriteriaPrefixlistName(Objects.nonNull(prevMatchPrefixListName)?prevMatchPrefixListName:null);
			enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefix.setModifiedBy(serviceDetails.getOrderDetail().getOriginatorName());
			enablePrefix.setMatchcriteriaPprefixlistPreprovisioned((byte) 1);
			policyCriteriaRepository.save(enablePrefix);
			PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
			enablePrefixCriteriaMapping.setVersion(1);
			enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
			enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
			enablePrefixCriteriaMapping.setPolicyType(outBoundPolicy);
			policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
			policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
		} catch (Exception e) {
			logger.error("Exception in createEnableOutBoundPrefixCriteriaForCisco with PrevPrefix => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		
	}
	
	
	private void createInboundPolicyV4Cisco(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails, Bgp prevBgp) throws TclCommonException {
		try {
			logger.info("createInboundPolicyV4Cisco  started {}");
			PolicyType policyType = new PolicyType();
			policyType.setBgp(bgp);
			policyType.setInboundIpv4Preprovisioned((byte) 1);
			policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			policyType.setModifiedBy(MODIFIEDBY);
			policyTypeRepository.save(policyType);
			policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgp(policyType, map, serviceDetails,prevBgp));
		} catch (Exception e) {
			logger.error("Exception in createInboundPolicyV4Cisco => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgp(PolicyType policyType, Map<String, String> map,
		ServiceDetail serviceDetails, Bgp prevBgp) throws TclCommonException {

	try {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();
		logger.info("createPolicyCriteraForBgp with PrevBgp  started {}");
		String prevMatchPrefixListName=null;
		List<PolicyType> prevPolicyTypeList = prevBgp.getPolicyTypes().stream()
				.filter(prevBgpPolicy -> Objects.nonNull(prevBgpPolicy.getInboundRoutingIpv4Policy())
						&& prevBgpPolicy.getInboundRoutingIpv4Policy() == (byte) 1)
				.collect(Collectors.toList());
		aa:
		for (PolicyType prevPolicyType : prevPolicyTypeList) {
			logger.info("Prev policy type");
			Set<PolicyTypeCriteriaMapping> prevPolicyTypeCriterias = prevPolicyType.getPolicyTypeCriteriaMappings();
			for (PolicyTypeCriteriaMapping prevPolicyTypeCriteria : prevPolicyTypeCriterias) {
				logger.info("Prev policy type criteria");
				Integer policyCriteriaId = prevPolicyTypeCriteria.getPolicyCriteriaId();
				logger.info("Policy type criteriaId::",policyCriteriaId);
				List<PolicyCriteria> prevPolicyCriterias = policyCriteriaRepository
						.findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(Arrays.asList(policyCriteriaId),(byte)1);
				if(Objects.nonNull(prevPolicyCriterias) && !prevPolicyCriterias.isEmpty()){
					logger.info("Prev policy criteria exists");
					Optional<PolicyCriteria> prevPolicyCriteriaOptional= prevPolicyCriterias.stream().findFirst();
					if(prevPolicyCriteriaOptional.isPresent()){
						logger.info("Prev Match Prefix list name exists");
						PolicyCriteria prevPolicyCriteria=prevPolicyCriteriaOptional.get();
						prevMatchPrefixListName=prevPolicyCriteria.getMatchcriteriaPrefixlistName();
						logger.info("PrevMatchPrefixListName",prevMatchPrefixListName);
						break aa;
					}
				}
				
			}
		}
		
		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		createEnablePrefixCriteriaForCisco(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType,prevMatchPrefixListName);
		return policyTypeCriteriaMappings;
	} catch (Exception e) {
		logger.error("Exception in createPolicyCriteraForBgp with PrevBgp => {}", e);
		throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

	}

}

private void createEnablePrefixCriteriaForCisco(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
		ServiceDetail serviceDetails, String routerMake, PolicyType policyType, String prevMatchPrefixListName) throws TclCommonException {

	try {
		logger.info("createEnablePrefixCriteriaForCisco with PrevPrefix started {}");

		PolicyCriteria enablePrefix = new PolicyCriteria();
		enablePrefix.setMatchcriteriaPrefixlist((byte) 0);
		enablePrefix.setMatchcriteriaPrefixlistName(Objects.nonNull(prevMatchPrefixListName)?prevMatchPrefixListName:serviceDetails.getServiceId() + "" + "_IPv4_In");
		enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefix.setModifiedBy(serviceDetails.getOrderDetail().getOriginatorName());
		enablePrefix.setMatchcriteriaPprefixlistPreprovisioned((byte) 0);
		policyCriteriaRepository.save(enablePrefix);
		//enablePrefix.setPrefixlistConfigs(createPrefixConfig(enablePrefix, serviceDetails, routerMake));
		PolicyTypeCriteriaMapping enablePrefixCriteriaMapping = new PolicyTypeCriteriaMapping();
		enablePrefixCriteriaMapping.setVersion(1);
		enablePrefixCriteriaMapping.setPolicyCriteriaId(enablePrefix.getPolicyCriteriaId());
		enablePrefixCriteriaMapping.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefixCriteriaMapping.setModifiedBy(MODIFIEDBY);
		enablePrefixCriteriaMapping.setPolicyType(policyType);
		policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
		policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
	} catch (Exception e) {
		logger.error("Exception in createEnablePrefixCriteriaForCisco with PrevPrefix => {}", e);
		throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

	}
	
}




	private void createOutboundPolicyAluV6Rule(Bgp bgp, Bgp prevBgp, Map<String, String> map, ServiceDetail serviceDetails, PolicyType prevPolicyType) {
		PolicyType outBoundPolicy = new PolicyType();
		outBoundPolicy.setBgp(bgp);
		logger.info("Copy createOutboundPolicyAluV6Rule from previous ");
		outBoundPolicy.setOutboundIpv6PolicyName(prevPolicyType.getOutboundIpv6PolicyName());
		outBoundPolicy.setOutboundRoutingIpv6Policy((byte) 1);
		outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		// outBoundPolicy.setOutboundIstandardpolicyv6((byte) 1);
		policyTypeRepository.save(outBoundPolicy);
	}


	private void createInboundV6Rule(Bgp bgp, Bgp prevBgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routerMake, PolicyType prevPolicyType) throws TclCommonException {
		PolicyType policyType = new PolicyType();
		policyType.setBgp(bgp);
		policyType.setInboundRoutingIpv6Policy((byte) 1);
		logger.info("Copy createInboundV6Rule from previous ");
		policyType.setInboundIpv6PolicyName(prevPolicyType.getInboundIpv6PolicyName());
		policyType.setInboundIpv6Preprovisioned((byte) 1);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			policyType.setInboundIstandardpolicyv6((byte) 1);

		}
		policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policyType.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(policyType);
		policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgpV6(policyType, map, serviceDetails));
		
	}


	private void createOutboundPolicyAluV4Rule(Bgp bgp, Bgp prevBgp, Map<String, String> map,
			ServiceDetail serviceDetails, PolicyType prevPolicyType) {
		PolicyType outBoundPolicy = new PolicyType();
		outBoundPolicy.setBgp(bgp);
		logger.info("Copy OutboundPolicyV4 from previous ");
		outBoundPolicy.setOutboundIpv4PolicyName(prevPolicyType.getOutboundIpv4PolicyName());
		outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
		outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(outBoundPolicy);
	}


	private void createInboundPolicyV4Rule(Bgp bgp, Bgp prevBgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routerMake, PolicyType prevPolicyType) {
		PolicyType policyType = new PolicyType();
		policyType.setBgp(bgp);
		policyType.setInboundRoutingIpv4Policy((byte) 1);
		logger.info("Copy createInboundPolicyV4Rule from previous ");
		policyType.setInboundIpv4PolicyName(prevPolicyType.getInboundIpv4PolicyName());
		policyType.setInboundIpv4Preprovisioned((byte) 1);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			policyType.setInboundIstandardpolicyv4((byte) 1);

		}
		policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policyType.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(policyType);
		policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgp(policyType, map, serviceDetails));
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
			if((lanPoolRoutingNeededAttribute!=null && "Yes".equalsIgnoreCase(lanPoolRoutingNeededAttribute.getAttributeValue())) 
					 || (lanPoolRoutingNeededAttribute==null)){ // lanPoolRoutingNeededAttribute might be null for old case
			saveWanStaticRoutes(serviceDetail, interfaceProtocolMapping.getRouterDetail(), staticProtocol);
			}
		}
	}

	private void applyBgpRule(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails) throws TclCommonException {
		String routerName = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		if (bgp.getNeighborOn() == null) {

			bgp.setNeighborOn(AceConstants.IPADDRESS.WAN);
		}
		String redundancyRule=map.getOrDefault(AceConstants.SERVICE.REDUNDACY_ROLE,null);
		if (redundancyRule != null && (redundancyRule.equalsIgnoreCase(AceConstants.IPADDRESS.PRIMARY)
				|| redundancyRule.equalsIgnoreCase(AceConstants.SERVICE.SECONDARY))) {
			bgp.setSooRequired((byte) 1);

		} else {
			bgp.setSooRequired((byte) 0);

		}

		bgp.setAsoOverride((byte) 1);
		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerName)) {
			bgp.setSplitHorizon((byte) 0);
		}else{
			bgp.setSplitHorizon((byte) 1);
		}
		

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

		}else{
			bgp.setIsbgpMultihopReqd((byte)0);
		}

		if (map.containsKey(AceConstants.VRF.VRF_NAME) && routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
			bgp.setLocalAsNumber(AceConstants.PROTOCOL.LOCALASNUMBER);
		}

		if (Objects.nonNull(bgp.getRemoteAsNumber()) && bgp.getRemoteAsNumber() > 1 && bgp.getRemoteAsNumber() < 65535) {
			bgp.setAsoOverride((byte) 1);
		}

		else {
			bgp.setAsoOverride((byte) 0);

		}
		
		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerName)) {
			bgp.setRedistributeConnectedEnabled((byte) 1);
			bgp.setRedistributeStaticEnabled((byte) 1);
			bgp.setIsbgpNeighbourInboundv4RoutemapEnabled((byte) 0);
			if (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("DUALSTACK")
					|| map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("v4")) {
				//bgp.setIsbgpNeighbourInboundv4RoutemapEnabled((byte) 1);
				bgp.setBgpneighbourinboundv4routermapname(serviceDetails.getServiceId() + "_" + "IPv4_In");
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 1);
				if (map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase("PRIMARY")) {
					bgp.setBgpneighbourinboundv4routermapname("LP_PRIMARY");
				}
			}

			if (map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("DUALSTACK")
					|| map.get(AceConstants.IPADDRESS.IPPATH).equalsIgnoreCase("v6")) {
				bgp.setIsbgpNeighbourInboundv6RoutemapEnabled((byte) 1);
				bgp.setBgpneighbourinboundv6routermapname(serviceDetails.getServiceId() + "_" + "IPv6_In");
				if (map.get(AceConstants.SERVICE.REDUNDACY_ROLE).equalsIgnoreCase("PRIMARY")) {
					bgp.setBgpneighbourinboundv4routermapname("LP_PRIMARY");
				}
				bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 1);

			}

			bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned((byte) 0);
			bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned((byte) 0);

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

				bgp.setRemoteIpv4Address(map.get(AceConstants.CPE.CPE_INTERFACE_IPV4) /*+ "/"
						+ subnet(map.get(AceConstants.IPADDRESS.WANV4ADDRESS))*/);
			}
			if (map.containsKey(AceConstants.CPE.CPE_INTERFACE_IPV6)) {
				bgp.setRemoteIpv6Address(map.get(AceConstants.CPE.CPE_INTERFACE_IPV6) /*+ "/"
						+ subnet(map.get(AceConstants.IPADDRESS.WANV6ADDRESS))*/);
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
		if(bgp.getPassword()!=null) {
			bgp.setAuthenticationMode("MD5");
			bgp.setIsauthenticationRequired((byte) 1);
		}
		bgp.setRoutesExchanged(null);
		applyBgpDetailRule(serviceDetails,bgp,map,routerName);
		OrderDetail orderDetail=serviceDetails.getOrderDetail();
		if(orderDetail!=null && orderDetail.getExtOrderrefId()!=null && orderDetail.getExtOrderrefId().toLowerCase().contains("izosdwan")){
			logger.info("GVPNMACD.applyBgpRule Setting Local preference for sdwan");
			bgp.setV6LocalPreference(null);
			bgp.setLocalPreference(null);
		}
		bgpRepository.save(bgp);

		applyBgpPolicyType(bgp, map, serviceDetails);

	}

	private void applyBgpDetailRule(ServiceDetail serviceDetails, Bgp bgp, Map<String, String> map, String routerName) {
		ServiceDetail prevServiceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceDetails.getServiceId(),TaskStatusConstants.ACTIVE);
		if(Objects.nonNull(prevServiceDetail)){
				logger.info(" BGP PE");
				List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = prevServiceDetail.getInterfaceProtocolMappings()
						.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
						.collect(Collectors.toList());
				if (!routerInterfaceProtocolMappings.isEmpty()) {
					logger.info("Prev Router Protocol not empty");
					routerInterfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
					InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings.get(0);
					if(Objects.nonNull(routerInterfaceProtocolMapping.getBgp())){
						if (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteAsNumber())) {
							logger.info("Copy PREV Remote As Number for ROUTER Bgp");
							bgp.setRemoteAsNumber(routerInterfaceProtocolMapping.getBgp().getRemoteAsNumber());
						}
						bgp.setNeighborOn(routerInterfaceProtocolMapping.getBgp().getNeighborOn());
						bgp.setAsoOverride(Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getAsoOverride())?routerInterfaceProtocolMapping.getBgp().getAsoOverride():(byte)0);
						bgp.setSooRequired(Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getSooRequired())?routerInterfaceProtocolMapping.getBgp().getSooRequired():(byte)0);
						if (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getIsbgpMultihopReqd())) {
							logger.info("Copy PREV Remote As Customer Provided for ROUTER Bgp");
							bgp.setIsbgpMultihopReqd(routerInterfaceProtocolMapping.getBgp().getIsbgpMultihopReqd());
						} 
						if (Objects.isNull(bgp.getRemoteIpv6Address())
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address()))) {
							logger.info("Copy PREV Remote IPV6 for ROUTER Bgp");
							bgp.setRemoteIpv6Address(routerInterfaceProtocolMapping.getBgp().getRemoteIpv6Address());
						}
						if (Objects.isNull(bgp.getLocalPreference())  
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getLocalPreference()))) {
							logger.info("Copy PREV Local Pref for ROUTER Bgp");
							bgp.setLocalPreference(routerInterfaceProtocolMapping.getBgp().getLocalPreference());
						}
						if (Objects.isNull(bgp.getV6LocalPreference())
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getV6LocalPreference()))) {
							logger.info("Copy PREV Remote LocalV6Pref for ROUTER Bgp");
							bgp.setV6LocalPreference(routerInterfaceProtocolMapping.getBgp().getV6LocalPreference());
						}
						if (Objects.isNull(bgp.getAuthenticationMode())
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getAuthenticationMode()))) {
							logger.info("Copy PREV Auth Mode for ROUTER Bgp");
							bgp.setAuthenticationMode(routerInterfaceProtocolMapping.getBgp().getAuthenticationMode());
						}
						if (Objects.isNull(bgp.getIsauthenticationRequired())  
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getIsauthenticationRequired()))) {
							logger.info("Copy PREV Auth Req. for ROUTER Bgp");
							bgp.setIsauthenticationRequired(routerInterfaceProtocolMapping.getBgp().getIsauthenticationRequired());
						}
						if(!"WAN".equals(bgp.getNeighborOn())){
							if (Objects.isNull(bgp.getLocalUpdateSourceInterface())
									&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getLocalUpdateSourceInterface()))) {
								logger.info("Copy PREV Remote LocalUpdateSource for ROUTER Bgp");
								bgp.setLocalUpdateSourceInterface(routerInterfaceProtocolMapping.getBgp().getLocalUpdateSourceInterface());
							}
							if (Objects.isNull(bgp.getLocalUpdateSourceIpv4Address())
									&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getLocalUpdateSourceIpv4Address()))) {
								logger.info("Copy PREV LocalUpdateSourceIpV4 for ROUTER Bgp");
								bgp.setLocalUpdateSourceIpv4Address(routerInterfaceProtocolMapping.getBgp().getLocalUpdateSourceIpv4Address());
							}
							if (Objects.isNull(bgp.getLocalUpdateSourceIpv6Address())
									&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getLocalUpdateSourceIpv6Address()))) {
								logger.info("Copy PREV LocalUpdateSourceIpV6 for ROUTER Bgp");
								bgp.setLocalUpdateSourceIpv6Address(routerInterfaceProtocolMapping.getBgp().getLocalUpdateSourceIpv6Address());
							}
							if (Objects.isNull(bgp.getRemoteUpdateSourceInterface())
									&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteUpdateSourceInterface()))) {
								logger.info("Copy PREV RemoteUpdateSourceInterface for ROUTER Bgp");
								bgp.setRemoteUpdateSourceInterface(routerInterfaceProtocolMapping.getBgp().getRemoteUpdateSourceInterface());
							}
							if (Objects.isNull(bgp.getRemoteUpdateSourceIpv4Address())
									&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteUpdateSourceIpv4Address()))) {
								logger.info("Copy PREV RemoteUpdateSourceInterfaceIpv4 for ROUTER Bgp");
								bgp.setRemoteUpdateSourceIpv4Address(routerInterfaceProtocolMapping.getBgp().getRemoteUpdateSourceIpv4Address());
							}
							if (Objects.isNull(bgp.getRemoteUpdateSourceIpv6Address())
									&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteUpdateSourceIpv6Address()))) {
								logger.info("Copy PREV RemoteUpdateSourceInterfaceIpv6 for ROUTER Bgp");
								bgp.setRemoteUpdateSourceIpv6Address(routerInterfaceProtocolMapping.getBgp().getRemoteUpdateSourceIpv6Address());
							}
						}
						bgp.setRoutesExchanged(null);
						/*if (Objects.isNull(bgp.getRoutesExchanged())
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRoutesExchanged()))) {
							logger.info("Copy PREV RoutesExchnaged for ROUTER Bgp");
							bgp.setRoutesExchanged(routerInterfaceProtocolMapping.getBgp().getRoutesExchanged());
						}*/
						/*if (Objects.isNull(bgp.getPassword())
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getPassword()))) {
							logger.info("Copy PREV Pwd for ROUTER Bgp");
							bgp.setPassword(routerInterfaceProtocolMapping.getBgp().getPassword());
						}
						if (Objects.isNull(routerInterfaceProtocolMapping.getBgp().getSplitHorizon()) || Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getSplitHorizon())) {
							logger.info("Copy PREV SplitHorizon for ROUTER Bgp");
							bgp.setSplitHorizon(Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getSplitHorizon())?routerInterfaceProtocolMapping.getBgp().getSplitHorizon():0);
						}*/
						
						if (Objects.isNull(bgp.getBgpPeerName())  
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getBgpPeerName()))) {
							logger.info("Copy PREV Peer Name for ROUTER Bgp");
							bgp.setBgpPeerName(routerInterfaceProtocolMapping.getBgp().getBgpPeerName());
						}
						
						if (Objects.isNull(bgp.getRemoteIpv4Address())
								&& (Objects.nonNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address()))) {
							logger.info("Copy PREV Remote IPV4 for ROUTER Bgp");
							bgp.setRemoteIpv4Address(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address());
						}else if(Objects.isNull(bgp.getRemoteIpv4Address())
								&& (Objects.isNull(routerInterfaceProtocolMapping.getBgp().getRemoteIpv4Address()))
								&& Objects.nonNull(serviceDetails.getIpAddressDetails())){
							Optional<IpAddressDetail> ipAddressDetailOptional=serviceDetails.getIpAddressDetails().stream().findFirst();
							if(ipAddressDetailOptional.isPresent()){
								IpAddressDetail ipAddressDetail=ipAddressDetailOptional.get();
								if(Objects.nonNull(ipAddressDetail.getIpaddrWanv4Addresses()) 
										&& !ipAddressDetail.getIpaddrWanv4Addresses().isEmpty()){
									Optional<IpaddrWanv4Address> ipaddrWanv4AddressOptional=ipAddressDetail.getIpaddrWanv4Addresses().stream().findFirst();
									if(ipaddrWanv4AddressOptional.isPresent()){
										IpaddrWanv4Address ipaddrWanv4Address=ipaddrWanv4AddressOptional.get();
										if(Objects.nonNull(ipaddrWanv4Address.getWanv4Address())){
											logger.info("Create remote bgp remote Ip");
											String ipWanv4[]=ipaddrWanv4Address.getWanv4Address().split("/");
											logger.info(ipWanv4[0]);
											bgp.setRemoteIpv4Address(getIpAddressSplit(ipWanv4[0], 2));
										}
									}
								}
							}
						}
					}
					
				    if(Objects.isNull(bgp.getBgpPeerName()) && 
							((Objects.isNull(routerInterfaceProtocolMapping.getBgp())) || (Objects.nonNull(routerInterfaceProtocolMapping.getBgp())
									&& Objects.isNull(routerInterfaceProtocolMapping.getBgp().getBgpPeerName())))){
						logger.info("Current is Bgp and Previous is other than Bgp");
						if (map.containsKey(AceConstants.VRF.VRF_NAME) && routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
							applyBgpPeerName(map,bgp,serviceDetails);
						}
					}else if(Objects.nonNull(routerInterfaceProtocolMapping.getStaticProtocol())){
						logger.info("Static to Bgp for Bgp Peer Name");
						if (map.containsKey(AceConstants.VRF.VRF_NAME) && routerName.equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {
							applyBgpPeerName(map,bgp,serviceDetails);
						}
					}
					
					if (Objects.nonNull(bgp.getRemoteAsNumber()) && bgp.getRemoteAsNumber() > 1 && bgp.getRemoteAsNumber() < 65535) {
						bgp.setAsoOverride((byte) 1);
					}else {
						bgp.setAsoOverride((byte) 0);

					}
					
				if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerName)) {

					if (Objects.nonNull(routerInterfaceProtocolMapping.getBgp())) {
						logger.info("BGP CISCO");
						if (Objects.nonNull(
								routerInterfaceProtocolMapping.getBgp().getIsbgpNeighbourInboundv4RoutemapEnabled())) {
							logger.info("InBound V4 Route Map Enabled");
							bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(routerInterfaceProtocolMapping.getBgp()
									.getIsbgpNeighbourInboundv4RoutemapEnabled());
						}
						if (Objects.nonNull(
								routerInterfaceProtocolMapping.getBgp().getBgpneighbourinboundv4routermapname())) {
							logger.info("InBound V4 Route Map Name");
							bgp.setBgpneighbourinboundv4routermapname(
									routerInterfaceProtocolMapping.getBgp().getBgpneighbourinboundv4routermapname());
						}
					} else {
						logger.info("NON BGP CISCO");
					}
				}
					
					ScComponentAttribute custProvidedAsNumberAttr=	scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetails.getScServiceDetailId(), "custProvidedAsNumber", "LM", "A");
					if(Objects.nonNull(custProvidedAsNumberAttr) && Objects.nonNull(custProvidedAsNumberAttr.getAttributeValue()) && "Yes".equalsIgnoreCase(custProvidedAsNumberAttr.getAttributeValue())){
						logger.info("Set Bgp MultiHop");
						bgp.setIsbgpMultihopReqd((byte)1);
					}
				}
		}
	}
	
	private void applyBgpPeerName(Map<String, String> map, Bgp bgp,
			ServiceDetail serviceDetails) {
		logger.info("applyBgpPeerName");
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

			bgp.setBgpPeerName(
					splitName(map.get(AceConstants.SERVICE.CUSTOMERNAME), 4) + "_" + cssSammgrId + "_" + code);

		}
	}


	private void applyBgpPolicyType(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails) throws TclCommonException {
		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);
		String ippath = map.get(AceConstants.IPADDRESS.IPPATH);

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
			outBoundPolicy.setOutboundIpv4PolicyName("PE-CE_EXPORT");
		} else {
			outBoundPolicy.setOutboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv6_Out");

		}
		outBoundPolicy.setOutboundRoutingIpv6Policy((byte) 1);
		outBoundPolicy.setOutboundIpv6Preprovisioned((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		// outBoundPolicy.setOutboundIstandardpolicyv6((byte) 1);
		policyTypeRepository.save(outBoundPolicy);
	}

	private void createInboundPolicyV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routerMake) {
		PolicyType policyType = new PolicyType();
		policyType.setBgp(bgp);
		policyType.setInboundRoutingIpv4Policy((byte) 1);
		policyType.setInboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv4_In");
		policyType.setInboundIpv4Preprovisioned((byte) 1);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			policyType.setInboundIstandardpolicyv4((byte) 1);

		}
		policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policyType.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(policyType);
		policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgp(policyType, map, serviceDetails));
	}
	
	private void createInboundPolicyIpV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails,
			String routerMake, InterfaceProtocolMapping prevRouterInterfaceProtocolMapping) {
		PolicyType policyType = new PolicyType();
		policyType.setBgp(bgp);
		policyType.setInboundRoutingIpv4Policy((byte) 1);
		policyType.setInboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv4_In");
		if(prevRouterInterfaceProtocolMapping.getBgp()==null) {
			policyType.setInboundIpv4Preprovisioned((byte) 0);
		}else {
			policyType.setInboundIpv4Preprovisioned((byte) 1);
		}
		
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			policyType.setInboundIstandardpolicyv4((byte) 1);

		}
		policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policyType.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(policyType);
		policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgp(policyType, map, serviceDetails));
	}

	private void createInboundV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails, String routerMake) throws TclCommonException {
		PolicyType policyType = new PolicyType();
		policyType.setBgp(bgp);
		policyType.setInboundRoutingIpv6Policy((byte) 1);
		policyType.setInboundIpv6PolicyName(serviceDetails.getServiceId() + "" + "_IPv6_In");
		policyType.setInboundIpv6Preprovisioned((byte) 1);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			policyType.setInboundIstandardpolicyv6((byte) 1);

		}
		policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policyType.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(policyType);
		policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgpV6(policyType, map, serviceDetails));

	}
	
	private void createInboundIpV6(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails, String routerMake) throws TclCommonException {
		PolicyType policyType = new PolicyType();
		policyType.setBgp(bgp);
		policyType.setInboundRoutingIpv6Policy((byte) 1);
		policyType.setInboundIpv6PolicyName(serviceDetails.getServiceId() + "" + "_IPv6_In");
		policyType.setInboundIpv6Preprovisioned((byte) 1);
		if (AceConstants.ROUTER.JUNIPER.equalsIgnoreCase(routerMake)) {
			policyType.setInboundIstandardpolicyv6((byte) 1);

		}
		policyType.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		policyType.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(policyType);
		policyType.setPolicyTypeCriteriaMappings(createPolicyCriteraForBgpV6(policyType, map, serviceDetails));

	}

	private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgpV6(PolicyType policyType, Map<String, String> map,
			ServiceDetail serviceDetails) throws TclCommonException {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
			if(ipAddressDetail!=null && ipAddressDetail.getIpaddrLanv6Addresses()!=null && !ipAddressDetail.getIpaddrLanv6Addresses().isEmpty()) {
			createEnablePrefixCriteriaV6(policyTypeCriteriaMappings, serviceDetails, policyType, map);
			}
			enableLocalPreferenceV6(policyTypeCriteriaMappings, policyType,routerMake, map);

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
		//enablePrefix.setPrefixlistConfigs(createPrefixConfigV6(enablePrefix, serviceDetails,map.get(AceConstants.ROUTER.ROUTER_MAKE)));
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
				policyTypeCriteriaMappingRepository.save(enablePrefixCriteriaMapping);
				policyTypeCriteriaMappings.add(enablePrefixCriteriaMapping);
			}
		} catch (Exception e) {
			logger.error("Exception in enableLocalPreferenceV6 => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	

	private void createOutboundPolicyJuniperV4(Bgp bgp, Map<String, String> map, ServiceDetail serviceDetails
			) {
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
		} else {
			outBoundPolicy.setOutboundIpv4PolicyName(serviceDetails.getServiceId() + "" + "_IPv4_Out");

		}
		outBoundPolicy.setOutboundRoutingIpv4Policy((byte) 1);
		outBoundPolicy.setOutboundIpv4Preprovisioned((byte) 1);
		outBoundPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		outBoundPolicy.setModifiedBy(MODIFIEDBY);
		policyTypeRepository.save(outBoundPolicy);
	}

	private Set<PolicyTypeCriteriaMapping> createPolicyCriteraForBgp(PolicyType policyType, Map<String, String> map,
			ServiceDetail serviceDetails) {
		Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings = new HashSet<>();

		String routerMake = map.get(AceConstants.ROUTER.ROUTER_MAKE);

		if (AceConstants.ROUTER.CISCO_IP.equalsIgnoreCase(routerMake)) {

		} else {
			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
					.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);
			if (ipAddressDetail != null && ipAddressDetail.getIpaddrLanv4Addresses() != null
					&& !ipAddressDetail.getIpaddrLanv4Addresses().isEmpty()) {
				createEnablePrefixCriteria(policyTypeCriteriaMappings, serviceDetails, routerMake, policyType, map);
			}
			enableProtocalPolicyCriteria(policyTypeCriteriaMappings, policyType, map);
			enableLocalPreference(policyTypeCriteriaMappings, map);
		}

		return policyTypeCriteriaMappings;
	}

	private void createEnablePrefixCriteria(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings,
			ServiceDetail serviceDetails, String routerMake, PolicyType policyType, Map<String, String> map) {
		PolicyCriteria enablePrefix = new PolicyCriteria();
		enablePrefix.setMatchcriteriaPrefixlist((byte) 0);
		enablePrefix.setMatchcriteriaPrefixlistName(serviceDetails.getServiceId() + "" + "_IPv4_In");
		enablePrefix.setMatchcriteriaPprefixlistPreprovisioned((byte)0);
		if (routerMake.equalsIgnoreCase(AceConstants.ROUTER.JUNIPER)) {
			enablePrefix.setTermName("10");
			enablePrefix.setTermSetcriteriaAction("ACCEPT");
		}
		enablePrefix.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		enablePrefix.setModifiedBy(MODIFIEDBY);
		policyCriteriaRepository.save(enablePrefix);
		//enablePrefix.setPrefixlistConfigs(createPrefixConfig(enablePrefix, serviceDetails, routerMake));
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
					prefixlistConfig.setNetworkPrefix(ipaddrLanv4Address.getLanv4Address()!=null?ipaddrLanv4Address.getLanv4Address().trim():ipaddrLanv4Address.getLanv4Address());
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

	private Set<PrefixlistConfig> createPrefixConfigV6(PolicyCriteria enablePrefix, ServiceDetail serviceDetails, String routerMake) {
		Set<PrefixlistConfig> prefixlistConfigs = new HashSet<>();
		IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream()
				.filter(ip -> ip.getEndDate() == null).findFirst().orElse(null);

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
			logger.info("findCpeInterfaceIpv4Ethernet");
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
				logger.info("IpLanv4 exists:: {}",ipAddressDetail.getIpaddrLanv4Addresses().size());
				for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
					if(ipaddrLanv4Address.getLanv4Address()!=null) {
						logger.info("ipaddrLanv4Address.getLanv4Address() exists for Id: {}",ipaddrLanv4Address.getLanv4AddrId());
						WanStaticRoute wanStaticRoute = new WanStaticRoute();
						logger.info("Static Protocol Id: {}",staticProtocol.getStaticprotocolId());
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

						wanStaticRoute.setIpsubnet(ipaddrLanv4Address.getLanv4Address()!=null?ipaddrLanv4Address.getLanv4Address().trim():ipaddrLanv4Address.getLanv4Address());

						if (wanStaticRoute.getIpsubnet() != null) {

							List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetail
									.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
											&& serIn.getIscpeWanInterface() == 1 && serIn.getEndDate() == null)
									.collect(Collectors.toList());

							cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());

							if (cpeProtocolMappings != null && !cpeProtocolMappings.isEmpty()) {
								InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);
								logger.info("Updating nexthop from saveWanStaticRoutes method");
								wanStaticRoute.setNextHopid(findCpeInterfaceIpv4Ethernet(cpeProtocolMapping));
							}

						}

						wanStaticRoute.setGlobal((byte) 0);
						wanStaticRoute.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
						wanStaticRoute.setModifiedBy(MODIFIEDBY);
						wanStaticRouteRepository.save(wanStaticRoute);
						staticProtocol.getWanStaticRoutes().add(wanStaticRoute);
						logger.info("Wan static Route save successfully");
						wanStaticRoutes.add(wanStaticRoute);
					}
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
					wanStaticRoute.setServiceCommunity(null);
				}

				wanStaticRoute.setStaticProtocol(staticProtocol);
				wanStaticRoute.setIsPewan((byte) 1);
				wanStaticRoute.setIsCewan((byte) 0);
				wanStaticRoute.setIsCpewanStaticroutes((byte) 0);
				wanStaticRoute.setIsCpelanStaticroutes((byte) 0);

				wanStaticRoute.setIpsubnet(ipaddrLanv6Address.getLanv6Address()!=null?ipaddrLanv6Address.getLanv6Address().trim():ipaddrLanv6Address.getLanv6Address());

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
				staticProtocol.getWanStaticRoutes().add(wanStaticRoute);
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
		vpnSolutionRepository.save(vpnSolution);
	}

	private void createCustomerSolutionRecord(VpnMetatData vpnMetatData, ServiceDetail serviceDetails,
			ServiceDetail prevActiveServiceDetail, String custiomerName) {
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
			logger.info("applyVpnMetata GVPN MACD Rule engine");
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
			
			if (serviceDetails.getLastmileProvider()!=null && !serviceDetails.getLastmileProvider().isEmpty() 
			 && serviceDetails.getLastmileProvider().toLowerCase().contains("radwin") && serviceDetails.getLastmileProvider().toLowerCase().contains("tcl")
					&& serviceDetails.getLastmileProvider().toLowerCase().contains("pop")) {
				logger.info("Set managementVpnType2 as RADWIN for radwin p2p");
				vpnMetatData.setManagementVpnType2("RADWIN");
			}
			String customeName=null;

			if (Objects.nonNull(serviceDetails.getOrderDetail())) {

				if (serviceDetails.getOrderDetail().getProjectName() == null || org.apache.commons.lang3.StringUtils.isBlank(serviceDetails.getOrderDetail().getProjectName())) {

					customeName = splitCustomerName(serviceDetails.getOrderDetail().getCustomerName());
				} else {
					customeName = splitCustomerName63(serviceDetails.getOrderDetail().getCustomerName());

				}
				String vpnName = splitProjectName(customeName,
						serviceDetails.getOrderDetail().getProjectName());
				if(serviceDetails.getOrderDetail().getExtOrderrefId().toLowerCase().contains("izosdwan")) {
					vpnName=vpnName+"_SDWAN";
				}
				vpnMetatData.setVpnSolutionName(vpnName);
				serviceDetails.setSolutionId(vpnName);
				serviceDetailRepository.save(serviceDetails);
				vpnMetatData.setVpnName(vpnName + "_" + alias);// need to handle project name in future
			}
			
			//solution name have to replace _

			vpnMetatData.setVpnAlias(vpnMetatData.getVpnName());
			vpnMetatData.setModifiedBy(MODIFIEDBY);
			vpnMetatDataRepository.saveAndFlush(vpnMetatData);
			if(!serviceDetails.getOrderDetail().getExtOrderrefId().toLowerCase().contains("izosdwan") && Objects.nonNull(vpnMetatData.getVpnName()) && !vpnMetatData.getVpnName().isEmpty() && vpnMetatData.getVpnName().equalsIgnoreCase("STATE_BANK")){
				logger.info("Set managementVpnType1 as NULL for STATE_BANK");
				vpnMetatData.setManagementVpnType1(null);
			}
			if(Objects.nonNull(serviceDetails.getLastmileProvider()) && !serviceDetails.getLastmileProvider().isEmpty() && 
					serviceDetails.getLastmileProvider().toLowerCase().contains("radwin") && serviceDetails.getLastmileProvider().toLowerCase().contains("pmp")){
				logger.info("Set managementVpnType2 as NULL for radwin p2p");
				vpnMetatData.setManagementVpnType2(null);
			}
			vpnMetatData.setVpnLegId(10000+vpnMetatData.getId());
			vpnMetatDataRepository.saveAndFlush(vpnMetatData);


		}
		return vpnMetatData;
	}

	protected String splitCustomerName63(String customerName) {
		StringBuilder buffer = new StringBuilder();

		AtomicInteger atomicInteger = new AtomicInteger(0);
		if (customerName.length() <= 3) {
			buffer.append(customerName.replaceAll(" ", "_"));
		} else {
			List<String> customerarr = Pattern.compile(" ").splitAsStream(customerName).limit(2)
					.collect(Collectors.toList());

			if (customerarr.size() == 1) {

				buffer.append(
						customerarr.get(0).length() <= 10 ? customerarr.get(0) : customerarr.get(0).substring(0, 10));
			} else {

				customerarr.forEach(cust -> {
					atomicInteger.incrementAndGet();

					if (atomicInteger.get() == 1) {
						buffer.append(cust.length() >= 6 ? (cust.substring(0, 6) + " ") : cust + " ");
					} else if (atomicInteger.get() == 2) {
						buffer.append(cust.length() >= 3 ? (cust.substring(0, 3) + " ") : cust + " ");

					}

				});
			}
		}
		return buffer.toString().trim().replace(" ", "_");
	}
}
