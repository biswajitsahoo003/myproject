package com.tcl.dias.serviceactivation.activation.fti.service;

import com.tcl.dias.serviceactivation.activation.services.IPDetailsService;
import com.tcl.dias.serviceactivation.activation.services.SatSocService;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.Attribute;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.VpnIllPortAttributes;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.MstRegionalPopCommunity;
import com.tcl.dias.serviceactivation.entity.entities.MstServiceCommunity;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.MstRegionalPopCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstServiceCommunityRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.rule.engine.service.GVPNRuleEngineService;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AclPolicyCriteriaBean;
import com.tcl.dias.servicefulfillmentutils.beans.AluSchedulerPolicyBean;
import com.tcl.dias.servicefulfillmentutils.beans.BgpBean;
import com.tcl.dias.servicefulfillmentutils.beans.CEDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChannelizedE1serialInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChannelizedSdhInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeBean;
import com.tcl.dias.servicefulfillmentutils.beans.EthernetInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.InterfaceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.InterfaceProtocolMappingBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpAddressDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrLanv4AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrLanv6AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrWanv4AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrWanv6AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.LmComponentBean;
import com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.OspfBean;
import com.tcl.dias.servicefulfillmentutils.beans.PEDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.PolicyTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean;
import com.tcl.dias.servicefulfillmentutils.beans.RouterDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.StaticProtocolBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.VpnSolutionBean;
import com.tcl.dias.servicefulfillmentutils.beans.VrfBean;
import com.tcl.dias.servicefulfillmentutils.beans.WanStaticRouteBean;
import com.tcl.dias.servicefulfillmentutils.beans.WimaxLastmileBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FTIService {

	private static final Logger logger = LoggerFactory.getLogger(FTIService.class);

	@Autowired
	IPDetailsService ipDetailsService;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Autowired
	MstRegionalPopCommunityRepository mstRegionalPopCommunityRepository;

	@Autowired
	MstServiceCommunityRepository mstServiceCommunityRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	GVPNRuleEngineService gvpnRuleEngineService;

	@Autowired
	SatSocService satSocService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	private static void parseDir(List<String> list, String path, List<String> srvids) {
		String sid = null;
		String s[] = (String[]) null;
		File dir = new File(path);
		if (!dir.exists())
			return;
		if (dir.isDirectory()) {
			String files[] = dir.list();
			String as[];
			int j = (as = files).length;
			for (int i = 0; i < j; i++) {
				String file = as[i];
				if (file.contains("_")) {
					s = file.split("_");
					sid = s[0];
//						if (!srvids.contains(sid))
					srvids.add(sid);
					list.add((new StringBuilder(String.valueOf(path))).append("\\").append(file).toString());
				}
			}
		}
	}

	private static void processCMD(String arg, List<String> userinput, List<String> srvids) {
		parseDir(userinput, arg, srvids);
		if (userinput.isEmpty())
			System.exit(0);
	}

	public void populateEthernetInterfaceDataForCESide(EthernetInterfaceBean ceEthernetInterfaceBean, Map<String, String> inputValueMap) {
		String completeIpAddress = "", completeSubnetMask = "", subnetMask = "", subnetMaskKey = "", ipAddressKey = "";

		// Setting IPv4 Attributes
		if(inputValueMap.containsKey("cep_ipaddress") && StringUtils.isNotEmpty(inputValueMap.get("cep_ipaddress"))){

			completeIpAddress = inputValueMap.get("cep_ipaddress").split("/")[0];
			completeSubnetMask = inputValueMap.get("cep_ipaddress").split("/")[1];

			ceEthernetInterfaceBean.setIpv4Address(completeIpAddress);

			try {
				if(completeSubnetMask.contains(".")) {
					subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(completeSubnetMask)));
				}
				else{
					subnetMask = completeSubnetMask;
				}
			} catch (UnknownHostException e) {
				logger.error("UnknownHostException while populating CE Side Ethernet Interface for serviceCode {}. Error : {}",
						inputValueMap.get("rfu1"), e.getMessage());
			}
			ceEthernetInterfaceBean.setModifiedIpv4Address(completeIpAddress + "/" + subnetMask);
		}
		else {
			if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv4address.riipaddr")) {
				completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv4address.riipaddr");

				subnetMaskKey = "pewan.ipaddressing.remote.ipv4address.riaddrmask";

				ceEthernetInterfaceBean.setIpv4Address(completeIpAddress);

				if (!inputValueMap.containsKey(subnetMaskKey) || (inputValueMap.containsKey(subnetMaskKey) && inputValueMap.get(subnetMaskKey).equals("0"))) {
					try {
						subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.remote.ipv4address.riipmask"))));
					} catch (UnknownHostException e) {
						logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
								inputValueMap.get("rfu1"), e.getMessage());
					}
				} else {
					subnetMask = inputValueMap.get(subnetMaskKey);
				}
				ceEthernetInterfaceBean.setModifiedIpv4Address(completeIpAddress + "/" + subnetMask);
			}
			if (Objects.isNull(ceEthernetInterfaceBean.getIpv4Address())) {
				// Using values with Suffix '1'
				if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv4address1.riipaddr")) {
					completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv4address1.riipaddr");

					subnetMaskKey = "pewan.ipaddressing.remote.ipv4address1.riaddrmask";

					ceEthernetInterfaceBean.setIpv4Address(completeIpAddress);
					if (!inputValueMap.containsKey(subnetMaskKey) || (inputValueMap.containsKey(subnetMaskKey) && inputValueMap.get(subnetMaskKey).equals("0"))){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.remote.ipv4address1.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					} else {
						subnetMask = inputValueMap.get(subnetMaskKey);
					}
					ceEthernetInterfaceBean.setModifiedIpv4Address(completeIpAddress + "/" + subnetMask);
				}
			}
		}
		// Cisco/Juniper
		if(inputValueMap.containsKey("pewan.ipaddressing.remote.ipv4address2.riipaddr")) {

			completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv4address2.riipaddr");

			subnetMaskKey = "pewan.ipaddressing.remote.ipv4address2.riaddrmask";

			ceEthernetInterfaceBean.setSecondaryIpv4Address(completeIpAddress);

			if (!inputValueMap.containsKey(subnetMaskKey) || (inputValueMap.containsKey(subnetMaskKey) && inputValueMap.get(subnetMaskKey).equals("0"))){
				try {
					subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.remote.ipv4address2.riipmask"))));
				} catch (UnknownHostException e) {
					logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
							inputValueMap.get("rfu1"), e.getMessage());
				}
			}
			else{
				subnetMask = inputValueMap.get(subnetMaskKey);
			}
			ceEthernetInterfaceBean.setModifiedSecondaryIpv4Address(completeIpAddress + "/" + subnetMask);
		}
		else {
			if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv4address2.asipaddress")) {
				completeIpAddress = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress");
				ceEthernetInterfaceBean.setSecondaryIpv4Address(completeIpAddress);

				subnetMaskKey = "pewan.ipaddressing.local.ipv4address2.asprefixlength";

				if(inputValueMap.containsKey(subnetMaskKey)) {
					subnetMask = inputValueMap.get(subnetMaskKey);
					ceEthernetInterfaceBean.setModifiedSecondaryIpv4Address(completeIpAddress + "/" + subnetMask);
				}
			}
		}

		// Setting IPv6 Attributes
		if(inputValueMap.containsKey("cep_ipv6address") && StringUtils.isNotEmpty(inputValueMap.get("cep_ipv6address"))){
			ceEthernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("cep_ipv6address"));
			ceEthernetInterfaceBean.setIpv6Address(inputValueMap.get("cep_ipv6address").split("/")[0]);
		}
		else {
			if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address.riipaddr")) {
				completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv6address.riipaddr");
				ceEthernetInterfaceBean.setIpv6Address(completeIpAddress);

				subnetMaskKey = "pewan.ipaddressing.remote.ipv6address.riprefixlen";
				if(inputValueMap.containsKey(subnetMaskKey)) {
					subnetMask = inputValueMap.get(subnetMaskKey);
					ceEthernetInterfaceBean.setModifiedIpv6Address(completeIpAddress + "/" + subnetMask);
				}
			} else if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address.riipaddress")) {
				completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv6address.riipaddress");
				ceEthernetInterfaceBean.setIpv6Address(completeIpAddress);

				subnetMaskKey = "pewan.ipaddressing.remote.ipv6address.riprefixlen";

				if(inputValueMap.containsKey(subnetMaskKey)) {
					subnetMask = inputValueMap.get(subnetMaskKey);
					ceEthernetInterfaceBean.setModifiedIpv6Address(completeIpAddress + "/" + subnetMask);
				}
			}
			if (Objects.isNull(ceEthernetInterfaceBean.getIpv6Address())) {
				if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address1.riipaddr")) {
					completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv6address1.riipaddr");
					ceEthernetInterfaceBean.setIpv6Address(completeIpAddress);
					subnetMaskKey = "pewan.ipaddressing.remote.ipv6address1.riprefixlen";
					if(inputValueMap.containsKey(subnetMaskKey)) {
						subnetMask = inputValueMap.get(subnetMaskKey);
						ceEthernetInterfaceBean.setModifiedIpv6Address(completeIpAddress + "/" + subnetMask);
					}
				} else if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address1.riipaddress")) {
					completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv6address1.riipaddress");
					ceEthernetInterfaceBean.setIpv6Address(completeIpAddress);
					subnetMaskKey = "pewan.ipaddressing.remote.ipv6address1.riprefixlen";
					if(inputValueMap.containsKey(subnetMaskKey)) {
						subnetMask = inputValueMap.get(subnetMaskKey);
						ceEthernetInterfaceBean.setModifiedIpv6Address(completeIpAddress + "/" + subnetMask);
					}
				}
			}
		}
		if(inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address2.riipaddr")){
			completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv6address2.riipaddr");
			ceEthernetInterfaceBean.setSecondaryIpv6Address(completeIpAddress);
			subnetMaskKey = "pewan.ipaddressing.remote.ipv6address1.riprefixlen";
			if(inputValueMap.containsKey(subnetMaskKey)) {
				subnetMask = inputValueMap.get(subnetMaskKey);
				ceEthernetInterfaceBean.setModifiedSecondaryIpv6Address(completeIpAddress + "/" + subnetMask);
			}
		}
		else if(inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address2.riipaddress")){
			completeIpAddress = inputValueMap.get("pewan.ipaddressing.remote.ipv6address2.riipaddress");
			ceEthernetInterfaceBean.setSecondaryIpv6Address(completeIpAddress);
			subnetMaskKey = "pewan.ipaddressing.remote.ipv6address1.riprefixlen";
			if(inputValueMap.containsKey(subnetMaskKey)) {
				subnetMask = inputValueMap.get(subnetMaskKey);
				ceEthernetInterfaceBean.setModifiedSecondaryIpv6Address(completeIpAddress + "/" + subnetMask);
			}
		}
		else {
			if (inputValueMap.containsKey("pewan.ipaddressing.remote.ipv6address2.asipaddress")) {
				completeIpAddress = inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress");
				ceEthernetInterfaceBean.setSecondaryIpv4Address(completeIpAddress);
				subnetMaskKey = "pewan.ipaddressing.local.ipv6address2.asprefixlength";
				if(inputValueMap.containsKey(subnetMaskKey)) {
					subnetMask = inputValueMap.get(subnetMaskKey);
					ceEthernetInterfaceBean.setModifiedSecondaryIpv4Address(completeIpAddress + "/" + subnetMask);
				}
			}
		}

		// ALCATEL case
		if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
			if(inputValueMap.containsKey("pewan.interface.vprninterface.ipinterface.asinnerencapvalue")) {
				ceEthernetInterfaceBean.setInnerVlan(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asinnerencapvalue"));
			}
		}
		else{
			if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.rivlanencapid")) {
				ceEthernetInterfaceBean.setInnerVlan(inputValueMap.get("pewan.ethernetip.subint.ipinterface.rivlanencapid"));
			}
		}
	}
	
	private TaskBean processMapData(TaskBean taskBean, Map<String, String> inputValueMap) throws TclCommonException {

		String serviceId = inputValueMap.get("rfu1");
		logger.info("Service Code retrieved from FTI : {}", serviceId);

		taskBean.setServiceCode(serviceId);
		taskBean.setServiceType(inputValueMap.get("rfu2"));

		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		List<AluSchedulerPolicyBean> aluSchedulerPolicyBeans = new ArrayList<>();
		List<VrfBean> vrfBeans = new ArrayList<>();
		Set<EthernetInterfaceBean> ethernetInterfaceBeans = new HashSet<>();
		
		InterfaceProtocolMappingBean interfaceProtocolMappingBean = new InterfaceProtocolMappingBean();
		PEDetailsBean peDetailsBean = new PEDetailsBean();
		CEDetailsBean ceDetailsBean = new CEDetailsBean();

		ServiceDetailBean serviceDetailBean = new ServiceDetailBean();
		setServiceDetailBean(serviceDetailBean, serviceId, inputValueMap);

		OrderDetailBean orderDetailBean = new OrderDetailBean();
		setOrderDetailBean(orderDetailBean,inputValueMap);

		ServiceDetail activeServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateAndEndDateIsNullOrderByVersionDesc(serviceId, TaskStatusConstants.ACTIVE);

		ServiceDetail lastServiceDetailRecord = serviceDetailRepository.findFirstByServiceIdOrderByIdDesc(taskBean.getServiceCode());

		setAluSchedulerPolicyBean(aluSchedulerPolicyBeans, inputValueMap);
		serviceDetailBean.setAluSchedulerPolicyBeans(aluSchedulerPolicyBeans);

		setVrfBeans(vrfBeans, inputValueMap);
		serviceDetailBean.setVrfBeans(vrfBeans);

		serviceDetailBeans.add(serviceDetailBean);
		orderDetailBean.setServiceDetailBeans(serviceDetailBeans);

		processInformationFromExistingInstance(activeServiceDetail, serviceId, serviceDetailBean, orderDetailBean, inputValueMap, peDetailsBean, ceDetailsBean);

		interfaceProtocolMappingBean.setPeDetailsBean(peDetailsBean);
		interfaceProtocolMappingBean.setCeDetailsBean(ceDetailsBean);

		if(!CollectionUtils.isEmpty(serviceDetailBean.getIpAddressDetailBeans())){
			IpAddressDetailBean ipAddressDetailBean = serviceDetailBean.getIpAddressDetailBeans().stream().findFirst().get();
			// Overriding few values in IPAddress using FTI key value pairs
			setIpAddressDetailBean(ipAddressDetailBean,inputValueMap, activeServiceDetail);
		}

		if(Objects.nonNull(lastServiceDetailRecord)) {
			serviceDetailBean.setVersion(lastServiceDetailRecord.getVersion() + 1);
			// If last record has Sc Service Detail Mapping, override value from active record
			if(Objects.nonNull(lastServiceDetailRecord.getScServiceDetailId()))
				serviceDetailBean.setScServiceDetailId(lastServiceDetailRecord.getScServiceDetailId());
			OrderDetail lastOrderDetail = lastServiceDetailRecord.getOrderDetail();
			if(Objects.nonNull(lastOrderDetail))
				orderDetailBean.setOrderId(lastOrderDetail.getOrderId());
		}
		if(Objects.nonNull(activeServiceDetail)){
			ipDetailsService.endDateActivationRecords(activeServiceDetail, "FTI_Refresh", true, true);
			OrderDetail activeOrderDetail = activeServiceDetail.getOrderDetail();
			if(Objects.nonNull(activeOrderDetail) && Objects.nonNull(activeOrderDetail.getOrderId())) {
				orderDetailBean.setOrderId(activeOrderDetail.getOrderId());
			}
		}
		else if(Objects.isNull(serviceDetailBean.getVersion())){
			serviceDetailBean.setVersion(1);
		}

		serviceDetailBean.setInterfaceProtocolMappingBean(interfaceProtocolMappingBean);
		taskBean.setOrderDetails(orderDetailBean);
		return taskBean;
	}

	private void constructLmRecords(ServiceDetail activeServiceDetail, ServiceDetailBean serviceDetailBean) {
		// Constructing new LM Component record & linking to existing Radwin or Cambium records
		if (!CollectionUtils.isEmpty(activeServiceDetail.getLmComponents())) {
			List<LmComponentBean> lmComponentBeans = new ArrayList<>();
			activeServiceDetail.getLmComponents().forEach(lmComponent -> {
				LmComponentBean lmComponentBean = new LmComponentBean();
				ipDetailsService.setLmComponentBean(lmComponentBean, lmComponent);

				if (!CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())) {
					Set<RadwinLastmileBean> radwinLastmileBeans = new HashSet<>();
					lmComponent.getRadwinLastmiles().forEach(radwinLastmile -> {
						RadwinLastmileBean radwinLastMileBean = new RadwinLastmileBean();
						radwinLastMileBean.setRadwinLastmileId(radwinLastmile.getRadwinLastmileId());
						radwinLastmileBeans.add(radwinLastMileBean);
						lmComponentBean.setRadwinLastmiles(radwinLastmileBeans);
					});
				}
				if (!CollectionUtils.isEmpty(lmComponent.getCambiumLastmiles())) {
					Set<CambiumLastmileBean> cambiumLastmileBeans = new HashSet<>();
					lmComponent.getCambiumLastmiles().forEach(cambiumLastmile -> {
						CambiumLastmileBean cambiumLastmileBean = new CambiumLastmileBean();
						cambiumLastmileBean.setCambiumLastmileId(cambiumLastmile.getCambiumLastmileId());
						cambiumLastmileBeans.add(cambiumLastmileBean);
						lmComponentBean.setCambiumLastmiles(cambiumLastmileBeans);
					});
				}
				if (!CollectionUtils.isEmpty(lmComponent.getWimaxLastmiles())) {
					Set<WimaxLastmileBean> wimaxLastmileBeans = new HashSet<>();
					lmComponent.getWimaxLastmiles().forEach(wimaxLastmile -> {
						WimaxLastmileBean wimaxLastmileBean = new WimaxLastmileBean();
						wimaxLastmileBean.setWimaxLastmileId(wimaxLastmile.getWimaxLastmileId());
						wimaxLastmileBeans.add(wimaxLastmileBean);
						lmComponentBean.setWimaxLastmiles(wimaxLastmileBeans);
					});
				}
				lmComponentBeans.add(lmComponentBean);
			});
			serviceDetailBean.setLmComponentBeans(lmComponentBeans);
		}
	}

	private void processInformationFromExistingInstance(ServiceDetail activeServiceDetail, String serviceId, ServiceDetailBean serviceDetailBean,
														OrderDetailBean orderDetailBean, Map<String, String> inputValueMap, PEDetailsBean peDetailsBean,
														CEDetailsBean ceDetailsBean) throws TclCommonException {
		logger.info("Processing Information via existing instance for service Code {}", inputValueMap.get("rfu1"));
		Set<RouterDetailBean> routerDetailBeans = new HashSet<>();
		Set<EthernetInterfaceBean> ethernetInterfaceBeans = new HashSet<>();
		Set<BgpBean> bgpBeans = new HashSet<>();
		Set<ChannelizedE1serialInterfaceBean> channelizedE1serialInterfaceBeans = new HashSet<>();
		Set<ChannelizedSdhInterfaceBean> channelizedSdhInterfaceBeans = new HashSet<>();
		Set<StaticProtocolBean> staticProtocolBeans = new HashSet<>();
		Set<AclPolicyCriteriaBean> aclPolicyCriteriaBeans = new HashSet<>();
		Set<WanStaticRouteBean> wanStaticRouteBeans = new HashSet<>();
		Set<OspfBean> ospfBeans = new HashSet<>();

		setOspfBean(ospfBeans, inputValueMap);
		setRouterDetailBean(routerDetailBeans, inputValueMap);
		setBgpBean(bgpBeans, inputValueMap);
		setEthernetInterfaceBean(ethernetInterfaceBeans, inputValueMap);
		setChannelizedE1serialInterfaceBean(channelizedE1serialInterfaceBeans, inputValueMap);
		setChannelizedSdhInterfaceBean(channelizedSdhInterfaceBeans, inputValueMap);
		setWanStaticRouteBean(wanStaticRouteBeans, inputValueMap, activeServiceDetail);
		setStaticProtocolBean(staticProtocolBeans, wanStaticRouteBeans, inputValueMap);
		setAclPolicyCriteriaBean(aclPolicyCriteriaBeans, inputValueMap);

		if (Objects.isNull(activeServiceDetail)) {
			logger.info("No active instance exists for service Code {}", inputValueMap.get("rfu1"));
		} else {
			TaskBean existingTaskBean = ipDetailsService.getIpServiceDetails(null, activeServiceDetail.getVersion(), serviceId,null);
			OrderDetailBean existingOrderDetailBean = existingTaskBean.getOrderDetails();
			ServiceDetailBean existingServiceDetailBean = existingOrderDetailBean.getServiceDetailBeans().stream().findFirst().get();
			InterfaceProtocolMappingBean existingInterfaceProtocolMappingBean = existingServiceDetailBean.getInterfaceProtocolMappingBean();
			PEDetailsBean existingPeDetailBean = existingInterfaceProtocolMappingBean.getPeDetailsBean();
			CEDetailsBean existingCeDetailsBean = existingInterfaceProtocolMappingBean.getCeDetailsBean();

			// Populating remaining protocol and interface attributes from existing instance
			if (Objects.nonNull(existingPeDetailBean)) {
				// Populating process Id from existing instance
				if (!CollectionUtils.isEmpty(existingPeDetailBean.getOspfBeans()) && !CollectionUtils.isEmpty(ospfBeans)) {
					if(inputValueMap.containsKey("proto") && inputValueMap.get("proto").toLowerCase().equalsIgnoreCase("ospf")) {
						OspfBean migratedOspfBean = existingPeDetailBean.getOspfBeans().stream().findFirst().get();
						OspfBean ospfBean = ospfBeans.stream().findFirst().get();
						ospfBean.setProcessId(migratedOspfBean.getProcessId());
						InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
						constructInterfaceDetailBean(migratedOspfBean.getInterfaceDetailBean(), interfaceDetailBean);
						ospfBean.setInterfaceDetailBean(interfaceDetailBean);
					}
				}
				if (!CollectionUtils.isEmpty(existingPeDetailBean.getRouterDetailBeans()) && !CollectionUtils.isEmpty(routerDetailBeans)) {
					RouterDetailBean migratedRouterDetailBean = existingPeDetailBean.getRouterDetailBeans().stream().findFirst().get();
					if(!CollectionUtils.isEmpty(routerDetailBeans)) {
						RouterDetailBean routerDetailBean = routerDetailBeans.stream().findFirst().get();
						if (Objects.isNull(routerDetailBean.getRouterModel()))
							routerDetailBean.setRouterModel(migratedRouterDetailBean.getRouterModel());
						if (Objects.isNull(routerDetailBean.getIpv4MgmtAddress()))
							routerDetailBean.setIpv4MgmtAddress(migratedRouterDetailBean.getIpv4MgmtAddress());
						if (Objects.isNull(routerDetailBean.getIpv6MgmtAddress()))
							routerDetailBean.setIpv6MgmtAddress(migratedRouterDetailBean.getIpv6MgmtAddress());
						InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
						constructInterfaceDetailBean(migratedRouterDetailBean.getInterfaceDetailBean(), interfaceDetailBean);
						routerDetailBean.setInterfaceDetailBean(interfaceDetailBean);
					}
				}
				if (!CollectionUtils.isEmpty(existingPeDetailBean.getEthernetInterfaceBeans()) && !CollectionUtils.isEmpty(ethernetInterfaceBeans)) {
					EthernetInterfaceBean migratedEthernetInterfaceBean = existingPeDetailBean.getEthernetInterfaceBeans().stream().findFirst().get();
					InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
					constructInterfaceDetailBean(migratedEthernetInterfaceBean.getInterfaceDetailBean(), interfaceDetailBean);
					EthernetInterfaceBean ethernetInterfaceBean = ethernetInterfaceBeans.stream().findFirst().get();
					if (Objects.isNull(ethernetInterfaceBean.getInterfaceName())) {
						ethernetInterfaceBean.setInterfaceName(migratedEthernetInterfaceBean.getInterfaceName());
					}
					if (Objects.isNull(ethernetInterfaceBean.getPhysicalPort())) {
						ethernetInterfaceBean.setPhysicalPort(migratedEthernetInterfaceBean.getPhysicalPort());
					}
					if (Objects.isNull(ethernetInterfaceBean.getInnerVlan())) {
						ethernetInterfaceBean.setInnerVlan(migratedEthernetInterfaceBean.getInnerVlan());
					}
					if (Objects.isNull(ethernetInterfaceBean.getOuterVlan())) {
						ethernetInterfaceBean.setOuterVlan(migratedEthernetInterfaceBean.getOuterVlan());
					}
					if (Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
						ethernetInterfaceBean.setModifiedIpv6Address(migratedEthernetInterfaceBean.getModifiedIpv6Address());
					}
					if (Objects.isNull(ethernetInterfaceBean.getModifiedIpv4Address())) {
						ethernetInterfaceBean.setModifiedIpv4Address(migratedEthernetInterfaceBean.getModifiedIpv4Address());
					}
					if (Objects.isNull(ethernetInterfaceBean.getModifiedSecondaryIpv4Address())) {
						ethernetInterfaceBean.setModifiedSecondaryIpv4Address(migratedEthernetInterfaceBean.getModifiedSecondaryIpv4Address());
					}
					if (Objects.isNull(ethernetInterfaceBean.getModifiedSecondaryIpv6Address())) {
						ethernetInterfaceBean.setModifiedSecondaryIpv6Address(migratedEthernetInterfaceBean.getModifiedSecondaryIpv6Address());
					}
					if (Objects.isNull(ethernetInterfaceBean.getPhysicalPort()))
						ethernetInterfaceBean.setPhysicalPort(migratedEthernetInterfaceBean.getPhysicalPort());
					if (Objects.isNull(ethernetInterfaceBean.getMode()))
						ethernetInterfaceBean.setMode(migratedEthernetInterfaceBean.getMode());
					if (!CollectionUtils.isEmpty(migratedEthernetInterfaceBean.getAclPolicyCriterias())){
						if(!CollectionUtils.isEmpty(aclPolicyCriteriaBeans)){
							ethernetInterfaceBean.setAclPolicyCriterias(aclPolicyCriteriaBeans);
						}
						else{
							ethernetInterfaceBean.setAclPolicyCriterias(migratedEthernetInterfaceBean.getAclPolicyCriterias());
						}
					}
					ethernetInterfaceBean.setBfdMultiplier(migratedEthernetInterfaceBean.getBfdMultiplier());
					ethernetInterfaceBean.setBfdreceiveInterval(migratedEthernetInterfaceBean.getBfdreceiveInterval());
					ethernetInterfaceBean.setBfdtransmitInterval(migratedEthernetInterfaceBean.getBfdtransmitInterval());
					ethernetInterfaceBean.setIsbfdEnabled(migratedEthernetInterfaceBean.getIsbfdEnabled());
					ethernetInterfaceBean.setInterfaceDetailBean(interfaceDetailBean);
				}
				if (!CollectionUtils.isEmpty(existingPeDetailBean.getChannelizedE1serialInterfaceBeans()) && !CollectionUtils.isEmpty(channelizedE1serialInterfaceBeans)) {
					ChannelizedE1serialInterfaceBean migratedSerialInterfaceBean = existingPeDetailBean.getChannelizedE1serialInterfaceBeans()
							.stream().findFirst().get();
					ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean = channelizedE1serialInterfaceBeans.stream().findFirst().get();
					if (Objects.isNull(channelizedE1serialInterfaceBean.getInterfaceName())) {
						channelizedE1serialInterfaceBean.setInterfaceName(migratedSerialInterfaceBean.getInterfaceName());
					}
					if (Objects.isNull(channelizedE1serialInterfaceBean.getPhysicalPort())) {
						channelizedE1serialInterfaceBean.setPhysicalPort(migratedSerialInterfaceBean.getPhysicalPort());
					}
					if (Objects.isNull(channelizedE1serialInterfaceBean.getModifiedIpv6Address())) {
						channelizedE1serialInterfaceBean.setModifiedIpv6Address(migratedSerialInterfaceBean.getModifiedIpv6Address());
					}
					if (Objects.isNull(channelizedE1serialInterfaceBean.getModifiedIpv4Address())) {
						channelizedE1serialInterfaceBean.setModifiedIpv4Address(migratedSerialInterfaceBean.getModifiedIpv4Address());
					}
					if (Objects.isNull(channelizedE1serialInterfaceBean.getModifiedSecondaryIpv4Address())) {
						channelizedE1serialInterfaceBean.setModifiedSecondaryIpv4Address(migratedSerialInterfaceBean.getModifiedSecondaryIpv4Address());
					}
					if (Objects.isNull(channelizedE1serialInterfaceBean.getModifiedSecondaryIpv6Address())) {
						channelizedE1serialInterfaceBean.setModifiedSecondaryIpv6Address(migratedSerialInterfaceBean.getModifiedSecondaryIpv6Address());
					}
					if (!CollectionUtils.isEmpty(migratedSerialInterfaceBean.getAclPolicyCriterias())){
						if(!CollectionUtils.isEmpty(aclPolicyCriteriaBeans)){
							channelizedE1serialInterfaceBean.setAclPolicyCriterias(aclPolicyCriteriaBeans);
						}
						else{
							channelizedE1serialInterfaceBean.setAclPolicyCriterias(migratedSerialInterfaceBean.getAclPolicyCriterias());
						}
					}
					channelizedE1serialInterfaceBean.setBfdMultiplier(migratedSerialInterfaceBean.getBfdMultiplier());
					channelizedE1serialInterfaceBean.setBfdreceiveInterval(migratedSerialInterfaceBean.getBfdreceiveInterval());
					channelizedE1serialInterfaceBean.setBfdtransmitInterval(migratedSerialInterfaceBean.getBfdtransmitInterval());
					channelizedE1serialInterfaceBean.setIsbfdEnabled(migratedSerialInterfaceBean.getIsbfdEnabled());
					InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
					constructInterfaceDetailBean(migratedSerialInterfaceBean.getInterfaceDetailBean(), interfaceDetailBean);
					channelizedE1serialInterfaceBean.setInterfaceDetailBean(interfaceDetailBean);
				}
				if (!CollectionUtils.isEmpty(existingPeDetailBean.getChannelizedSdhInterfaceBeans()) &&
						!CollectionUtils.isEmpty(channelizedSdhInterfaceBeans)) {
					ChannelizedSdhInterfaceBean migratedSdhInterfaceBean = existingPeDetailBean.getChannelizedSdhInterfaceBeans()
							.stream().findFirst().get();
					ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean = channelizedSdhInterfaceBeans.stream().findFirst().get();
					if (Objects.isNull(channelizedSdhInterfaceBean.getInterfaceName())) {
						channelizedSdhInterfaceBean.setInterfaceName(migratedSdhInterfaceBean.getInterfaceName());
					}
					if (Objects.isNull(channelizedSdhInterfaceBean.getPhysicalPort())) {
						channelizedSdhInterfaceBean.setPhysicalPort(migratedSdhInterfaceBean.getPhysicalPort());
					}
					if (Objects.isNull(channelizedSdhInterfaceBean.getModifiedIipv6Address())) {
						channelizedSdhInterfaceBean.setModifiedIipv6Address(migratedSdhInterfaceBean.getModifiedIipv6Address());
					}
					if (Objects.isNull(channelizedSdhInterfaceBean.getModifiedIpv4Address())) {
						channelizedSdhInterfaceBean.setModifiedIpv4Address(migratedSdhInterfaceBean.getModifiedIpv4Address());
					}
					if (Objects.isNull(channelizedSdhInterfaceBean.getModifiedSecondaryIpv4Address())) {
						channelizedSdhInterfaceBean.setModifiedSecondaryIpv4Address(migratedSdhInterfaceBean.getModifiedSecondaryIpv4Address());
					}
					if (Objects.isNull(channelizedSdhInterfaceBean.getModifiedSecondaryIpv6Address())) {
						channelizedSdhInterfaceBean.setModifiedSecondaryIpv6Address(migratedSdhInterfaceBean.getModifiedSecondaryIpv6Address());
					}
					channelizedSdhInterfaceBean.setBfdMultiplier(migratedSdhInterfaceBean.getBfdMultiplier());
					channelizedSdhInterfaceBean.setBfdreceiveInterval(migratedSdhInterfaceBean.getBfdreceiveInterval());
					channelizedSdhInterfaceBean.setBfdtransmitInterval(migratedSdhInterfaceBean.getBfdtransmitInterval());
					channelizedSdhInterfaceBean.setIsbfdEnabled(migratedSdhInterfaceBean.getIsbfdEnabled());
					if (!CollectionUtils.isEmpty(migratedSdhInterfaceBean.getAclPolicyCriterias())){
						if(!CollectionUtils.isEmpty(aclPolicyCriteriaBeans)){
							channelizedSdhInterfaceBean.setAclPolicyCriterias(aclPolicyCriteriaBeans);
						}
						else{
							channelizedSdhInterfaceBean.setAclPolicyCriterias(migratedSdhInterfaceBean.getAclPolicyCriterias());
						}
					}
					InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
					constructInterfaceDetailBean(migratedSdhInterfaceBean.getInterfaceDetailBean(), interfaceDetailBean);
					channelizedSdhInterfaceBean.setInterfaceDetailBean(interfaceDetailBean);
				}
			}

			if(!CollectionUtils.isEmpty(staticProtocolBeans)){
				StaticProtocolBean newStaticProtocolBean = staticProtocolBeans.stream().findFirst().get();
				InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
				// If existing is also Static
				if(!CollectionUtils.isEmpty(existingPeDetailBean.getStaticProtocolBeans())){
					StaticProtocolBean existingStaticProtocolBean = existingPeDetailBean.getStaticProtocolBeans()
							.stream().findFirst().get();
					constructInterfaceDetailBean(existingStaticProtocolBean.getInterfaceDetailBean(), interfaceDetailBean);
				}
				else{
					if (inputValueMap.containsKey("proto") && (inputValueMap.get("proto").toLowerCase().equalsIgnoreCase("NONE") ||
							(inputValueMap.get("proto").toLowerCase().equalsIgnoreCase("static")))) {
						interfaceDetailBean.setIscpeLanInterface(false);
						interfaceDetailBean.setIscpeWanInterface(false);
						interfaceDetailBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
						interfaceDetailBean.setModifiedBy("FTI_Refresh");
						interfaceDetailBean.setStartDate(new Timestamp(new Date().getTime()));
					}
				}
				newStaticProtocolBean.setInterfaceDetailBean(interfaceDetailBean);
			}

			Set<EthernetInterfaceBean> ceEthernetInterfaceBeans = new HashSet<>();
			EthernetInterfaceBean ceEthernetInterfaceBean = new EthernetInterfaceBean();
			InterfaceDetailBean ceEthernetInterfaceDetailBean = new InterfaceDetailBean();
			populateEthernetInterfaceDataForCESide(ceEthernetInterfaceBean, inputValueMap);

			if (Objects.nonNull(existingCeDetailsBean)) {
				if (!CollectionUtils.isEmpty(existingCeDetailsBean.getCpeBeans())) {
					ceDetailsBean.setCpeBeans(existingCeDetailsBean.getCpeBeans());
					CpeBean existingCpeBean = existingCeDetailsBean.getCpeBeans().stream().findFirst().get();
					InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
					constructInterfaceDetailBean(existingCpeBean.getInterfaceDetailBean(), interfaceDetailBean);
					CpeBean cpeBean = ceDetailsBean.getCpeBeans().stream().findFirst().get();
					cpeBean.setInterfaceDetailBean(interfaceDetailBean);
				}
				// Populating few attributes from existing instance if values are NULL
				if (!CollectionUtils.isEmpty(existingCeDetailsBean.getEthernetInterfaceBeans())) {
					EthernetInterfaceBean existingEthernetInterfaceBean = existingCeDetailsBean.getEthernetInterfaceBeans().stream().findFirst().get();
					if (Objects.isNull(ceEthernetInterfaceBean.getInterfaceName()))
						ceEthernetInterfaceBean.setInterfaceName(existingEthernetInterfaceBean.getInterfaceName());
					if (Objects.isNull(ceEthernetInterfaceBean.getPhysicalPort()))
						ceEthernetInterfaceBean.setPhysicalPort(existingEthernetInterfaceBean.getPhysicalPort());
					if (Objects.isNull(ceEthernetInterfaceBean.getEncapsulation()))
						ceEthernetInterfaceBean.setEncapsulation(existingEthernetInterfaceBean.getEncapsulation());
					if (Objects.isNull(ceEthernetInterfaceBean.getMode()))
						ceEthernetInterfaceBean.setMode(existingEthernetInterfaceBean.getMode());
					constructInterfaceDetailBean(existingEthernetInterfaceBean.getInterfaceDetailBean(), ceEthernetInterfaceDetailBean);
				}
			}
			else{
				ceEthernetInterfaceDetailBean.setIscpeWanInterface(true);
				ceEthernetInterfaceDetailBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
				ceEthernetInterfaceDetailBean.setModifiedBy("FTI_Refresh");
				ceEthernetInterfaceDetailBean.setStartDate(new Timestamp(new Date().getTime()));
			}

			ceEthernetInterfaceBean.setInterfaceDetailBean(ceEthernetInterfaceDetailBean);
			ceEthernetInterfaceBeans.add(ceEthernetInterfaceBean);
			ceDetailsBean.setEthernetInterfaceBeans(ceEthernetInterfaceBeans);

			if(Objects.nonNull(activeServiceDetail)) {
				// Populating Cos and Vpn Solution Details from Active Instance
				ipDetailsService.constructServiceQo(activeServiceDetail, serviceDetailBean, true);
				ipDetailsService.constructVpnSolutionDetails(activeServiceDetail, serviceDetailBean, true);
				ipDetailsService.constructVrfDetails(activeServiceDetail, serviceDetailBean, true);
				ipDetailsService.constructTopologyAndUniDetails(activeServiceDetail, serviceDetailBean, true);
				ipDetailsService.constructIpAddressDetails(activeServiceDetail, serviceDetailBean, true);
				ipDetailsService.constructSchedulerPolicy(activeServiceDetail, serviceDetailBean, true);
				ipDetailsService.constructMultiCastDetails(activeServiceDetail, serviceDetailBean, true);
				constructLmRecords(activeServiceDetail, serviceDetailBean);
			}
		}

		// Populating other attributes
		if (!CollectionUtils.isEmpty(serviceDetailBean.getVpnSolutionBeans())) {
			logger.info("serviceDetailBean.getVpnSolutionBeans() exists for serviceId: {}", activeServiceDetail.getServiceId());
			VpnSolutionBean vpnSolutionBean = serviceDetailBean.getVpnSolutionBeans().stream()
					.findFirst().get();
			if (inputValueMap.containsKey("pewan.l3vpn.customervpn.name") || inputValueMap.containsKey("pewan.vprn.asdescription")) {
				if (inputValueMap.get("pewan.l3vpn.customervpn.name").toLowerCase().contains("vsnl")) {
					vpnSolutionBean.setVpnName(inputValueMap.get("pewan.vprn.asdescription"));
				} else {
					vpnSolutionBean.setVpnName(inputValueMap.get("pewan.l3vpn.customervpn.name"));
				}
			}
			if (Objects.isNull(vpnSolutionBean.getLegRole())) {
				logger.info("vpnSolutionBean.legRole not exists for serviceId: {}", activeServiceDetail.getServiceId());
				String legRole = null;
				if (inputValueMap.containsKey("pewan.l3vpn.community1.role")
						&& inputValueMap.get("pewan.l3vpn.community1.role") != null) {
					legRole=inputValueMap.get("pewan.l3vpn.community1.role");
					setLegRole(legRole, vpnSolutionBean);
				}
				if (inputValueMap.containsKey("pewan.l3vpn.community2.role")
						&& inputValueMap.get("pewan.l3vpn.community2.role") != null) {
					legRole=inputValueMap.get("pewan.l3vpn.community2.role");
					setLegRole(legRole, vpnSolutionBean);
				}
				if (inputValueMap.containsKey("pewan.l3vpn.community3.role")
						&& inputValueMap.get("pewan.l3vpn.community3.role") != null) {
					legRole=inputValueMap.get("pewan.l3vpn.community3.role");
					setLegRole(legRole, vpnSolutionBean);
				}
				if (inputValueMap.containsKey("pewan.l3vpn.community4.role")
						&& inputValueMap.get("pewan.l3vpn.community4.role") != null) {
					legRole=inputValueMap.get("pewan.l3vpn.community4.role");
					setLegRole(legRole, vpnSolutionBean);
				}
				if (inputValueMap.containsKey("pewan.l3vpn.community5.role")
						&& inputValueMap.get("pewan.l3vpn.community5.role") != null) {
					legRole=inputValueMap.get("pewan.l3vpn.community5.role");
					setLegRole(legRole, vpnSolutionBean);
				}
				if (inputValueMap.containsKey("pewan.l3vpn.community.customervpn.sivautocomm")
						&& inputValueMap.get("pewan.l3vpn.community.customervpn.sivautocomm")!=null) {
					legRole=inputValueMap.get("pewan.l3vpn.community.customervpn.sivautocomm");
					setLegRole(legRole, vpnSolutionBean);
				}
				if(legRole==null || (legRole!=null && !legRole.toLowerCase().contains("mesh"))){
					logger.info("No data from FTI for Leg Role or Value from FTI was wrong. So defaulted to Spoke");
					vpnSolutionBean.setLegRole("Spoke");
				}
			}
			if (Objects.nonNull(activeServiceDetail) && activeServiceDetail.getServiceType().toLowerCase().contains("gvpn") && activeServiceDetail.getOrderType().toLowerCase().contains("macd")) {
				ScComponentAttribute scComponentAttributeChangedVRFName = scComponentAttributesRepository.
						findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc
								(activeServiceDetail.getScServiceDetailId(), "changedVRFName", AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
			if (Objects.nonNull(scComponentAttributeChangedVRFName) && !scComponentAttributeChangedVRFName.getAttributeValue().isEmpty()) {
				vpnSolutionBean.setVpnName(scComponentAttributeChangedVRFName.getAttributeValue());
				logger.info("VpnName updated from changedVRFName in FTI for Service {} : {}", serviceId, scComponentAttributeChangedVRFName.getAttributeValue());
			}
		}

		} else {
			logger.info("FTI for VpnSolution");
			// Create new Vpn Solution records altogether
			List<VpnSolutionBean> vpnSolutionBeans = new ArrayList<>();
			VpnSolutionBean vpnSolutionBean = new VpnSolutionBean();
			if (inputValueMap.containsKey("pewan.l3vpn.customervpn.name") || inputValueMap.containsKey("pewan.vprn.asdescription")) {
				if(inputValueMap.get("pewan.l3vpn.customervpn.name").toLowerCase().contains("vsnl")){
					vpnSolutionBean.setVpnName(inputValueMap.get("pewan.vprn.asdescription"));
				}
				else {
					vpnSolutionBean.setVpnName(inputValueMap.get("pewan.l3vpn.customervpn.name"));
				}
			}

			String legRole = null;
			if (inputValueMap.containsKey("pewan.l3vpn.community1.role")
					&& inputValueMap.get("pewan.l3vpn.community1.role") != null) {
				legRole=inputValueMap.get("pewan.l3vpn.community1.role");
				setLegRole(legRole, vpnSolutionBean);
			}
			if (inputValueMap.containsKey("pewan.l3vpn.community2.role")
					&& inputValueMap.get("pewan.l3vpn.community2.role") != null) {
				legRole=inputValueMap.get("pewan.l3vpn.community2.role");
				setLegRole(legRole, vpnSolutionBean);
			}
			if (inputValueMap.containsKey("pewan.l3vpn.community3.role")
					&& inputValueMap.get("pewan.l3vpn.community3.role") != null) {
				legRole=inputValueMap.get("pewan.l3vpn.community3.role");
				setLegRole(legRole, vpnSolutionBean);
			}
			if (inputValueMap.containsKey("pewan.l3vpn.community4.role")
					&& inputValueMap.get("pewan.l3vpn.community4.role") != null) {
				legRole=inputValueMap.get("pewan.l3vpn.community4.role");
				setLegRole(legRole, vpnSolutionBean);
			}
			if (inputValueMap.containsKey("pewan.l3vpn.community5.role")
					&& inputValueMap.get("pewan.l3vpn.community5.role") != null) {
				legRole=inputValueMap.get("pewan.l3vpn.community5.role");
				setLegRole(legRole, vpnSolutionBean);
			}
			if (inputValueMap.containsKey("pewan.l3vpn.community.customervpn.sivautocomm")
					&& inputValueMap.get("pewan.l3vpn.community.customervpn.sivautocomm")!=null) {
				legRole=inputValueMap.get("pewan.l3vpn.community.customervpn.sivautocomm");
				setLegRole(legRole, vpnSolutionBean);
			}
			if(legRole==null){
				logger.info("No data from FTI for Leg Role. So defaulted to Spoke");
				vpnSolutionBean.setLegRole("Spoke");
			}

			if (Objects.nonNull(activeServiceDetail) && activeServiceDetail.getServiceType().toLowerCase().contains("gvpn") && activeServiceDetail.getOrderType().toLowerCase().contains("macd")) {
				ScComponentAttribute scComponentAttributeChangedVRFName = scComponentAttributesRepository.
						findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc
								(activeServiceDetail.getScServiceDetailId(), "changedVRFName", AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
				if (Objects.nonNull(scComponentAttributeChangedVRFName) && !scComponentAttributeChangedVRFName.getAttributeValue().isEmpty()) {
					vpnSolutionBean.setVpnName(scComponentAttributeChangedVRFName.getAttributeValue());
					logger.info("VpnName updated from changedVRFName in FTI for Service {} : {}", serviceId, scComponentAttributeChangedVRFName.getAttributeValue());
				}
			}

			vpnSolutionBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
			vpnSolutionBean.setStartDate(new Timestamp(new Date().getTime()));
			vpnSolutionBeans.add(vpnSolutionBean);
			serviceDetailBean.setVpnSolutionBeans(vpnSolutionBeans);
		}

		if(!CollectionUtils.isEmpty(bgpBeans)){
			BgpBean bgpBean = bgpBeans.stream().findFirst().get();
			if(Objects.isNull(bgpBean.getAsoOverride()) && inputValueMap.containsKey("pewan.proto.bgp.site.group.asoverrideas")){
				bgpBean.setAsoOverride(Boolean.valueOf(inputValueMap.get("pewan.proto.bgp.site.group.asoverrideas")));
			}
			peDetailsBean.setBgpBeans(bgpBeans);
		}

		peDetailsBean.setStaticProtocolBeans(staticProtocolBeans);
		peDetailsBean.setEthernetInterfaceBeans(ethernetInterfaceBeans);
		peDetailsBean.setRouterDetailBeans(routerDetailBeans);
		peDetailsBean.setChannelizedE1serialInterfaceBeans(channelizedE1serialInterfaceBeans);
		peDetailsBean.setChannelizedSdhInterfaceBeans(channelizedSdhInterfaceBeans);
		peDetailsBean.setOspfBeans(ospfBeans);

		VpnIllPortAttributes vpnIllPortAttributes = satSocService.getReverseIsc(serviceId,"fti");

		if(Objects.nonNull(vpnIllPortAttributes) && !CollectionUtils.isEmpty(vpnIllPortAttributes.getAttribute())) {
			String wanIpv4Address = "", lanIpv6Address = "", lanIpv4Address = "", wanIpv6Address = "";

			List<Attribute> attributes = vpnIllPortAttributes.getAttribute().stream()
					.filter(attribute -> ( ("WAN_IP".equalsIgnoreCase(attribute.getKey())) || ("LAN IPv6".equalsIgnoreCase(attribute.getKey())) ||
							("LAN_IP".equalsIgnoreCase(attribute.getKey())) || ("IPV6 WAN IP".equalsIgnoreCase(attribute.getKey())) )).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(attributes)) {
				for(Attribute attribute : attributes){
					if("WAN_IP".equalsIgnoreCase(attribute.getKey())){
						wanIpv4Address = attribute.getValue();
						logger.info("WanIpv4 Address from SatSoc Reverse for Service Code {} : {}", serviceId, wanIpv4Address);
					}
					else if("LAN IPv6".equalsIgnoreCase(attribute.getKey())){
						lanIpv6Address = attribute.getValue();
						logger.info("LanIpv6 Address from SatSoc Reverse for Service Code {} : {}", serviceId, lanIpv6Address);
					}
					else if("LAN_IP".equalsIgnoreCase(attribute.getKey())){
						lanIpv4Address = attribute.getValue();
						logger.info("LanIpv4 Address from SatSoc Reverse for Service Code {} : {}", serviceId, lanIpv4Address);
					}
					else if("IPV6 WAN IP".equalsIgnoreCase(attribute.getKey())){
						wanIpv6Address = attribute.getValue();
						logger.info("WanIpv6 Address from SatSoc Reverse for Service Code {} : {}", serviceId, wanIpv6Address);
					}
				}
			}

			if (!CollectionUtils.isEmpty(serviceDetailBean.getIpAddressDetailBeans())) {
				IpAddressDetailBean ipAddressDetailBean = serviceDetailBean.getIpAddressDetailBeans().stream().findFirst().get();
				if (Objects.nonNull(ipAddressDetailBean)) {
					if (!CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrWanv4Addresses()) && StringUtils.isNotBlank(wanIpv4Address)) {
						IpaddrWanv4AddressBean ipaddrWanv4AddressBean = ipAddressDetailBean.getIpaddrWanv4Addresses().stream().findFirst().get();
						ipaddrWanv4AddressBean.setWanv4Address(wanIpv4Address);
					}
					if(CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrLanv6Addresses())){
						IpaddrLanv6AddressBean ipaddrLanv6AddressBean = new IpaddrLanv6AddressBean();
						if(StringUtils.isNotEmpty(lanIpv6Address)) {
							ipaddrLanv6AddressBean.setLanv6Address(lanIpv6Address);
						}
						else if(inputValueMap.containsKey("proto") && inputValueMap.get("proto").toLowerCase().equalsIgnoreCase("static")){
							if(inputValueMap.containsKey("pewan.proto.static.global.v6route.ridestprefix")){
								String subnetMask = "";
								if(inputValueMap.containsKey("pewan.proto.static.global.v6route.ridestmask")){
									subnetMask = inputValueMap.get("pewan.proto.static.global.v6route.ridestmask");
								}
								ipaddrLanv6AddressBean.setLanv6Address(inputValueMap.get("pewan.proto.static.global.v6route.ridestprefix") + "/" + subnetMask);
							}
						}
						ipAddressDetailBean.setIpaddrLanv6Addresses(Stream.of(ipaddrLanv6AddressBean).collect(Collectors.toSet()));
					}
					if(CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrLanv4Addresses()) && StringUtils.isNotEmpty(lanIpv4Address)){
						IpaddrLanv4AddressBean ipaddrLanv4AddressBean = ipAddressDetailBean.getIpaddrLanv4Addresses().stream().findFirst().get();
						ipaddrLanv4AddressBean.setLanv4Address(lanIpv4Address);
						ipAddressDetailBean.setIpaddrLanv4Addresses(Stream.of(ipaddrLanv4AddressBean).collect(Collectors.toSet()));
					}
					if(CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrWanv6Addresses()) && StringUtils.isNotEmpty(wanIpv6Address)){
						IpaddrWanv6AddressBean ipaddrWanv6AddressBean = ipAddressDetailBean.getIpaddrWanv6Addresses().stream().findFirst().get();
						ipaddrWanv6AddressBean.setWanv6Address(wanIpv6Address);
						ipAddressDetailBean.setIpaddrWanv6Addresses(Stream.of(ipaddrWanv6AddressBean).collect(Collectors.toSet()));
					}
				}
			}
		}
		populateRemainingAttributes(serviceDetailBean, activeServiceDetail, orderDetailBean);
	}

	private void setLegRole(String legRole, VpnSolutionBean vpnSolutionBean) {
		if(legRole!=null && (legRole.toLowerCase().contains("mesh"))
				|| legRole.toLowerCase().contains("spoke")
			    || legRole.toLowerCase().contains("hub")){
			vpnSolutionBean.setLegRole(legRole);
		}
	}

	private void populateRemainingAttributes(ServiceDetailBean serviceDetailBean, ServiceDetail activeServiceDetail, OrderDetailBean orderDetailBean){
		if(Objects.nonNull(activeServiceDetail)) {
			populateRemainingServiceDetailAttributes(serviceDetailBean, activeServiceDetail);
			OrderDetail activeOrderDetail = activeServiceDetail.getOrderDetail();
			populateRemainingOrderDetailAttributes(orderDetailBean, activeOrderDetail);
		}
	}

	private void populateRemainingServiceDetailAttributes(ServiceDetailBean serviceDetailBean, ServiceDetail migratedServiceDetail){
		// Populating remaining Service Details
		if(Objects.nonNull(migratedServiceDetail)) {
			if (Objects.isNull(serviceDetailBean.getCsoSammgrId()) && Objects.nonNull(migratedServiceDetail.getCssSammgrId())){
				serviceDetailBean.setCsoSammgrId(new Integer(migratedServiceDetail.getCssSammgrId()).toString());
			}
			if (Objects.isNull(serviceDetailBean.getOrderType())){
				serviceDetailBean.setOrderType(migratedServiceDetail.getOrderType());
			}
			if (Objects.isNull(serviceDetailBean.getOrderCategory())){
				serviceDetailBean.setOrderCategory(migratedServiceDetail.getOrderCategory());
			}
			if(Objects.isNull(serviceDetailBean.getServiceType()))
				serviceDetailBean.setServiceType(migratedServiceDetail.getServiceType());
			if(Objects.isNull(serviceDetailBean.getScServiceDetailId()))
				serviceDetailBean.setScServiceDetailId(migratedServiceDetail.getScServiceDetailId());
			if(Objects.isNull(serviceDetailBean.getExternalRefid()))
				serviceDetailBean.setExternalRefid(migratedServiceDetail.getExternalRefid());
			if (Objects.isNull(serviceDetailBean.getSolutionId()))
				serviceDetailBean.setSolutionId(migratedServiceDetail.getSolutionId());
			if (Objects.isNull(serviceDetailBean.getSolutionName()))
				serviceDetailBean.setSolutionName(migratedServiceDetail.getSolutionName());
			if (Objects.isNull(serviceDetailBean.getAddressLine1()))
				serviceDetailBean.setAddressLine1(migratedServiceDetail.getAddressLine1());
			if (Objects.isNull(serviceDetailBean.getAddressLine2()))
				serviceDetailBean.setAddressLine2(migratedServiceDetail.getAddressLine2());
			if (Objects.isNull(serviceDetailBean.getAddressLine3()))
				serviceDetailBean.setAddressLine3(migratedServiceDetail.getAddressLine3());
			if (Objects.isNull(serviceDetailBean.getCity()))
				serviceDetailBean.setCity(migratedServiceDetail.getCity());
			if (Objects.isNull(serviceDetailBean.getCountry()))
				serviceDetailBean.setCountry(migratedServiceDetail.getCountry());
			if (Objects.isNull(serviceDetailBean.getState()))
				serviceDetailBean.setState(migratedServiceDetail.getState());
			if (Objects.isNull(serviceDetailBean.getPincode()))
				serviceDetailBean.setPincode(migratedServiceDetail.getPincode());
			if (Objects.isNull(serviceDetailBean.getAluSvcName()))
				serviceDetailBean.setAluSvcName(migratedServiceDetail.getAluSvcName());
			if (Objects.isNull(serviceDetailBean.getAluSvcid()))
				serviceDetailBean.setAluSvcid(migratedServiceDetail.getAluSvcid());
			if (Objects.isNull(serviceDetailBean.getMgmtType()))
				serviceDetailBean.setMgmtType(migratedServiceDetail.getMgmtType());
			if (Objects.isNull(serviceDetailBean.getBurstableBw()))
				serviceDetailBean.setBurstableBw(migratedServiceDetail.getBurstableBw());
			if (Objects.isNull(serviceDetailBean.getBurstableBwUnit()))
				serviceDetailBean.setBurstableBwUnit(migratedServiceDetail.getBurstableBwUnit());
			if (Objects.isNull(serviceDetailBean.getLastmileType()))
				serviceDetailBean.setLastmileType(migratedServiceDetail.getLastmileType());
			if (Objects.isNull(serviceDetailBean.getLastmileProvider()))
				serviceDetailBean.setLastmileProvider(migratedServiceDetail.getLastmileProvider());
			if (Objects.isNull(serviceDetailBean.getRedundancyRole()))
				serviceDetailBean.setRedundancyRole(migratedServiceDetail.getRedundancyRole());
			if (Objects.isNull(serviceDetailBean.getServiceOrderType()))
				serviceDetailBean.setServiceOrderType(migratedServiceDetail.getServiceOrderType());
			if (Objects.isNull(serviceDetailBean.getDataTransferCommit()))
				serviceDetailBean.setDataTransferCommit(migratedServiceDetail.getDataTransferCommit());
			if (Objects.isNull(serviceDetailBean.getDataTransferCommitUnit()))
				serviceDetailBean.setDataTransferCommitUnit(migratedServiceDetail.getDataTransferCommitUnit());
			if (Objects.isNull(serviceDetailBean.getDescription()))
				serviceDetailBean.setDescription(migratedServiceDetail.getDescription());
			if (Objects.isNull(serviceDetailBean.getServiceBandwidth()))
				serviceDetailBean.setServiceBandwidth(migratedServiceDetail.getServiceBandwidth());
			if (Objects.isNull(serviceDetailBean.getScopeOfMgmt()))
				serviceDetailBean.setScopeOfMgmt(migratedServiceDetail.getScopeOfMgmt());
			if (Objects.isNull(serviceDetailBean.getOldServiceId()))
				serviceDetailBean.setOldServiceId(migratedServiceDetail.getOldServiceId());
			if (Objects.isNull(serviceDetailBean.getServiceBandwidthUnit()))
				serviceDetailBean.setServiceBandwidthUnit(migratedServiceDetail.getServiceBandwidthUnit());
			if (Objects.isNull(serviceDetailBean.getIntefaceDescSvctag()))
				serviceDetailBean.setIntefaceDescSvctag(migratedServiceDetail.getIntefaceDescSvctag());
			if (Objects.isNull(serviceDetailBean.getSvclinkRole()))
				serviceDetailBean.setSvclinkRole(migratedServiceDetail.getSvclinkRole());
			if (Objects.isNull(serviceDetailBean.getSvclinkSrvid()))
				serviceDetailBean.setSvclinkSrvid(migratedServiceDetail.getSvclinkSrvid());
			if(Objects.isNull(serviceDetailBean.getLongitude()))
				serviceDetailBean.setLongitude(migratedServiceDetail.getLongiTude());
			if(Objects.isNull(serviceDetailBean.getLat()))
				serviceDetailBean.setLat(migratedServiceDetail.getLat());
			if(Objects.isNull(serviceDetailBean.getUsageModel()))
				serviceDetailBean.setUsageModel(migratedServiceDetail.getUsageModel());
			if(Objects.isNull(serviceDetailBean.getTrfsDate()))
				serviceDetailBean.setTrfsDate(migratedServiceDetail.getTrfsDate());
			if(Objects.isNull(serviceDetailBean.getTrfsTriggerDate()))
				serviceDetailBean.setTrfsTriggerDate(migratedServiceDetail.getTrfsTriggerDate());
		}
	}

	private void populateRemainingOrderDetailAttributes(OrderDetailBean orderDetailBean, OrderDetail activeOrderDetail){
		if(Objects.nonNull(activeOrderDetail)) {
			if (Objects.isNull(orderDetailBean.getOrderType()))
				orderDetailBean.setOrderType(activeOrderDetail.getOrderType());
			if(Objects.isNull(orderDetailBean.getOrderUuid()))
				orderDetailBean.setOrderUuid(activeOrderDetail.getOrderUuid());
			if(Objects.isNull(orderDetailBean.getSfdcOpptyId()))
				orderDetailBean.setSfdcOpptyId(activeOrderDetail.getSfdcOpptyId());
			if (Objects.isNull(orderDetailBean.getOrderStatus()))
				orderDetailBean.setOrderStatus(activeOrderDetail.getOrderStatus());
			if (Objects.isNull(orderDetailBean.getExtOrderrefId()))
				orderDetailBean.setExtOrderrefId(activeOrderDetail.getExtOrderrefId());
			if (Objects.isNull(orderDetailBean.getCustCuId()))
				orderDetailBean.setCustCuId(activeOrderDetail.getCustCuId());
			if (Objects.isNull(orderDetailBean.getOrderCategory()))
				orderDetailBean.setOrderCategory(activeOrderDetail.getOrderCategory());
			if (Objects.isNull(orderDetailBean.getCopfId()))
				orderDetailBean.setCopfId(activeOrderDetail.getCopfId());
			if (Objects.isNull(orderDetailBean.getCustomerCrnId()))
				orderDetailBean.setCustomerCrnId(activeOrderDetail.getCustomerCrnId());
			if (Objects.isNull(orderDetailBean.getCustomerType()))
				orderDetailBean.setCustomerType(activeOrderDetail.getCustomerType());
			if (Objects.isNull(orderDetailBean.getCustomerCategory()))
				orderDetailBean.setCustomerCategory(activeOrderDetail.getCustomerCategory());
			if (Objects.isNull(orderDetailBean.getEndCustomerName()))
				orderDetailBean.setEndCustomerName(activeOrderDetail.getEndCustomerName());
			if (Objects.isNull(orderDetailBean.getCustomerName()))
				orderDetailBean.setCustomerName(activeOrderDetail.getCustomerName());
			if (Objects.isNull(orderDetailBean.getCustomerEmail()))
				orderDetailBean.setCustomerEmail(activeOrderDetail.getCustomerEmail());
			if (Objects.isNull(orderDetailBean.getAddressLine1()))
				orderDetailBean.setAddressLine1(activeOrderDetail.getAddressLine1());
			if (Objects.isNull(orderDetailBean.getAddressLine2()))
				orderDetailBean.setAddressLine2(activeOrderDetail.getAddressLine2());
			if (Objects.isNull(orderDetailBean.getAddressLine3()))
				orderDetailBean.setAddressLine3(activeOrderDetail.getAddressLine3());
			if (Objects.isNull(orderDetailBean.getCity()))
				orderDetailBean.setCity(activeOrderDetail.getCity());
			if (Objects.isNull(orderDetailBean.getState()))
				orderDetailBean.setState(activeOrderDetail.getState());
			if (Objects.isNull(orderDetailBean.getCountry()))
				orderDetailBean.setCountry(activeOrderDetail.getCountry());
			if (Objects.isNull(orderDetailBean.getPincode()))
				orderDetailBean.setPincode(activeOrderDetail.getPincode());
			if (Objects.isNull(orderDetailBean.getLocation()))
				orderDetailBean.setLocation(activeOrderDetail.getLocation());
			if (Objects.isNull(orderDetailBean.getCustomerPhoneno()))
				orderDetailBean.setCustomerPhoneno(activeOrderDetail.getCustomerPhoneno());
			if (Objects.isNull(orderDetailBean.getCustomerContact()))
				orderDetailBean.setCustomerContact(activeOrderDetail.getCustomerContact());
			if (Objects.isNull(orderDetailBean.getGroupId()))
				orderDetailBean.setGroupId(activeOrderDetail.getGroupId());
			if (Objects.isNull(orderDetailBean.getOptyBidCategory()))
				orderDetailBean.setOptyBidCategory(activeOrderDetail.getOptyBidCategory());
			if (Objects.isNull(orderDetailBean.getProjectName()))
				orderDetailBean.setProjectName(activeOrderDetail.getProjectName());
			if (Objects.isNull(orderDetailBean.getOriginatorName()))
				orderDetailBean.setOriginatorName(activeOrderDetail.getOriginatorName());
		}
	}

	private void setStaticProtocolBean(Set<StaticProtocolBean> staticProtocolBeans, Set<WanStaticRouteBean> wanStaticRouteBeans, Map<String, String> valueMap){
		if (valueMap.containsKey("proto") && (valueMap.get("proto").toLowerCase().equalsIgnoreCase("NONE") ||
						(valueMap.get("proto").toLowerCase().equalsIgnoreCase("static")))) {
			StaticProtocolBean staticProtocolBean = new StaticProtocolBean();
			staticProtocolBean.setIsroutemapEnabled(false);
			staticProtocolBean.setWanStaticRoutes(wanStaticRouteBeans);
			staticProtocolBeans.add(staticProtocolBean);
		}
	}
	
	private void setWanStaticRouteBean(Set<WanStaticRouteBean> wanStaticRouteBeans, Map<String, String> valueMap, ServiceDetail activeServiceDetail) {
		if(Objects.nonNull(activeServiceDetail) && !CollectionUtils.isEmpty(activeServiceDetail.getIpAddressDetails())) {

			IpAddressDetail ipAddressDetail = activeServiceDetail.getIpAddressDetails().stream().findFirst().get();

			if(Objects.isNull(ipAddressDetail.getExtendedLanEnabled()) || convertByteToBool(ipAddressDetail.getExtendedLanEnabled())==false) {
				if (valueMap.containsKey("proto") && (valueMap.get("proto").toLowerCase().equalsIgnoreCase("NONE") ||
						(valueMap.get("proto").toLowerCase().equalsIgnoreCase("static")))) {

					String key = null, keyWithoutNumber = null, keyWithNumber = null;

					Integer count = 0;

					for (int i = 1; i <= 10; i++) {

						WanStaticRouteBean wanStaticRouteBean = new WanStaticRouteBean();

						if (valueMap.containsKey("pe_equipmodule") && valueMap.get("pe_equipmodule").toLowerCase().equalsIgnoreCase("AS")) {

							key = "pewan.proto.static.l3vpn.route";

							keyWithoutNumber = key + ".asdestination";
							keyWithNumber = key + String.valueOf(i) + ".asdestination";

							if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))
									&& valueMap.containsKey(key + ".asprefixlength") && Objects.nonNull(valueMap.get(key + ".asprefixlength"))) {
								wanStaticRouteBean.setIpsubnet(valueMap.get(keyWithoutNumber) + "/" + valueMap.get(key + ".asprefixlength"));
							} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))
									&& valueMap.containsKey(key + String.valueOf(i) + ".asprefixlength")
									&& Objects.nonNull(valueMap.get(key + String.valueOf(i) + ".asprefixlength"))) {
								wanStaticRouteBean.setIpsubnet(valueMap.get(keyWithNumber) + "/" + valueMap.get(key + String.valueOf(i) + ".asprefixlength"));
								count++;
							}

							keyWithoutNumber = key + ".asmetric";
							keyWithNumber = key + String.valueOf(i) + ".asmetric";

							if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))) {
								wanStaticRouteBean.setAdvalue(valueMap.get(keyWithoutNumber));
							} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))) {
								wanStaticRouteBean.setAdvalue(valueMap.get(keyWithNumber));
								count++;
							}

							keyWithoutNumber = key + ".astargetipaddress";
							keyWithNumber = key + String.valueOf(i) + ".astargetipaddress";

							if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))) {
								wanStaticRouteBean.setNextHopid(valueMap.get(keyWithoutNumber));
							} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))) {
								wanStaticRouteBean.setNextHopid(valueMap.get(keyWithNumber));
								count++;
							}
						}
						// Juniper
						else {
							if ((valueMap.containsKey("ipstack") && valueMap.get("ipstack").toLowerCase().contains("dual"))
									|| (valueMap.containsKey("ipstack") && valueMap.get("ipstack").toLowerCase().contains("v4"))) {

/*						key = "pewan.proto.static.l3vpn.route";
						keyWithoutNumber = key + ".ridestprefix";
						keyWithNumber = key + String.valueOf(i) + ".ridestprefix";

						if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))) {
							wanStaticRouteBean.setIpsubnet(valueMap.get(keyWithoutNumber));
						} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))) {
							wanStaticRouteBean.setIpsubnet(valueMap.get(keyWithNumber));
							count++;
						}*/

								key = "pewan.proto.static.global.route";
								keyWithoutNumber = key + ".rinexthopip";
								keyWithNumber = key + String.valueOf(i) + ".rinexthopip";

								if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))) {
									wanStaticRouteBean.setNextHopid(valueMap.get(keyWithoutNumber));
								} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))) {
									wanStaticRouteBean.setNextHopid(valueMap.get(keyWithNumber));
									count++;
								}

							}
							if ((valueMap.containsKey("ipstack") && valueMap.get("ipstack").toLowerCase().contains("dual"))
									|| (valueMap.containsKey("ipstack") && valueMap.get("ipstack").toLowerCase().contains("v6"))) {

								key = "pewan.proto.static.global.v6route";
								keyWithoutNumber = key + ".ridestprefix";
								keyWithNumber = key + String.valueOf(i) + ".ridestprefix";

								//pewan.proto.static.global.v6route.ridestprefix
								if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))
										&& valueMap.containsKey(key + ".ridestmask") && Objects.nonNull(valueMap.get(key + ".ridestmask"))) {
									wanStaticRouteBean.setIpsubnet(valueMap.get(keyWithoutNumber) + "/" + valueMap.get(key + ".ridestmask"));
									count++;
								} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))
										&& valueMap.containsKey(key + String.valueOf(i) + ".ridestmask")
										&& Objects.nonNull(valueMap.get(key + String.valueOf(i) + ".ridestmask"))) {
									wanStaticRouteBean.setIpsubnet(valueMap.get(keyWithNumber) + "/" + valueMap.get(key + String.valueOf(i) + ".ridestmask"));
									count++;
								}

								keyWithoutNumber = key + ".rinexthopip";
								keyWithNumber = key + String.valueOf(i) + ".rinexthopip";

								if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))) {
									wanStaticRouteBean.setNextHopid(valueMap.get(keyWithoutNumber));
								} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))) {
									wanStaticRouteBean.setNextHopid(valueMap.get(keyWithNumber));
									count++;
								}

							}

							key = "pewan.proto.static.l3vpn.route";
							keyWithNumber = key + String.valueOf(i) + ".rimetric";
							keyWithoutNumber = key + ".rimetric";

							if (valueMap.containsKey(keyWithoutNumber) && Objects.nonNull(valueMap.get(keyWithoutNumber))) {
								wanStaticRouteBean.setAdvalue(valueMap.get(keyWithoutNumber));
							} else if (valueMap.containsKey(keyWithNumber) && Objects.nonNull(valueMap.get(keyWithNumber))) {
								wanStaticRouteBean.setAdvalue(valueMap.get(keyWithNumber));
								count++;
							}

							if (valueMap.containsKey("pewan.endpoint")) {
								String hostname[] = valueMap.get("pewan.endpoint").split("/");
								List<MstRegionalPopCommunity> mstRegionalPopCommunityList = mstRegionalPopCommunityRepository
										.findByRouterHostname((hostname.length > 1) ? hostname[1] : hostname[0]);
								if (!CollectionUtils.isEmpty(mstRegionalPopCommunityList)) {
									wanStaticRouteBean.setPopCommunity(mstRegionalPopCommunityList.stream().findFirst().get().getPopCommunity());
									wanStaticRouteBean.setRegionalCommunity(mstRegionalPopCommunityList.stream().findFirst().get().getRegionalCommunity());
								}

								List<MstServiceCommunity> mstServiceCommunityList = mstServiceCommunityRepository
										.findByServiceSubtype((hostname.length > 1) ? hostname[1] : hostname[0]);
								if (!CollectionUtils.isEmpty(mstServiceCommunityList)) {
									wanStaticRouteBean.setServiceCommunity(mstServiceCommunityList.stream().findFirst().get().getServiceCmmunity());
								}
							}

						}

						wanStaticRouteBean.setIsPewan(true);
						wanStaticRouteBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
						wanStaticRouteBean.setStartDate(new Timestamp(new Date().getTime()));

						wanStaticRouteBeans.add(wanStaticRouteBean);
						if (count == 0)
							break;
					}
				}
			}
		}
	}

	private Boolean convertByteToBool(Byte value){
		return value!=null && value!=0;
	}

	private Byte convertBoolToByte(Boolean value){
		return (value==null || value==false) ? (byte) 0 : (byte) 1 ;
	}

	private void setOspfBean(Set<OspfBean> ospfBeans, Map<String, String> inputValueMap){
		if(inputValueMap.containsKey("proto") && inputValueMap.get("proto").toLowerCase().equalsIgnoreCase("ospf")) {
			logger.info("Protocol is OSPF for service Code {}", inputValueMap.get("rfu1"));
			OspfBean ospfBean = new OspfBean();
			ospfBean.setVersion("v2");
			ospfBean.setIsnetworkP2p(convertByteToBool((byte) 1));
			ospfBean.setIsignoreipOspfMtu(convertByteToBool((byte) 1));
			if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("CR")) {
				ospfBean.setIsredistributeConnectedEnabled(convertByteToBool((byte) 1));
				ospfBean.setIsredistributeStaticEnabled(convertByteToBool((byte) 0));
				ospfBean.setIsroutemapEnabled(convertByteToBool((byte) 0));
			}
			ospfBean.setLocalPreference("200");
			if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual")) {
				ospfBean.setLocalPreferenceV6("200");
			}
			if (inputValueMap.containsKey("pewan.proto.ospf.site.asvpndomainid")) {
				ospfBean.setDomainId(inputValueMap.get("pewan.proto.ospf.site.asvpndomainid"));
			}
			if (inputValueMap.containsKey("pewan.proto.ospf.site.areasite.asareaid")) {
				ospfBean.setAreaId(inputValueMap.get("pewan.proto.ospf.site.areasite.asareaid"));
			}
			ospfBean.setIsauthenticationRequired(false);
			if (inputValueMap.containsKey("pewan.srcost")) {
				ospfBean.setCost(inputValueMap.get("pewan.srcost"));
			}
			Set<PolicyTypeBean> policyTypeBeans = new HashSet<>();
			PolicyTypeBean outboundPolicyv4 = new PolicyTypeBean();
			PolicyTypeBean outboundPolicyv6 = new PolicyTypeBean();

			outboundPolicyv4.setOutboundRoutingIpv4Policy(convertByteToBool((byte) 1));
			outboundPolicyv4.setOutboundIpv4PolicyName("PE-CE_EXPORT");
			outboundPolicyv4.setOutboundIpv4Preprovisioned(convertByteToBool((byte) 1));
			outboundPolicyv4.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			outboundPolicyv4.setModifiedBy("FTI_Refresh");

			if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual")) {
				outboundPolicyv6.setOutboundRoutingIpv4Policy(convertByteToBool((byte) 1));
				outboundPolicyv6.setOutboundIpv4PolicyName("PE-CE_EXPORT");
				outboundPolicyv6.setOutboundIpv4Preprovisioned(convertByteToBool((byte) 1));
				outboundPolicyv6.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				outboundPolicyv6.setModifiedBy("FTI_Refresh");
				policyTypeBeans.add(outboundPolicyv6);
			}
			policyTypeBeans.add(outboundPolicyv4);
			ospfBean.setPolicyTypes(policyTypeBeans);
			ospfBeans.add(ospfBean);
		}
	}

	private void setAclPolicyCriteriaBean(Set<AclPolicyCriteriaBean> aclPolicyCriteriaBeans, Map<String, String> inputValueMap) {
		AclPolicyCriteriaBean aclPolicyCriteriaBean=new AclPolicyCriteriaBean();
		// ALCATEL
		if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
			String aclIpv4Key = "pewan.interface.vprninterface.ipinterface.asaclfilteripfilter1";
			if(inputValueMap.containsKey(aclIpv4Key) && StringUtils.isNotBlank(inputValueMap.get(aclIpv4Key))) {
				aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(true);
				aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(true);
				aclPolicyCriteriaBean.setInboundIpv4AclName(inputValueMap.get("pewan.acl.asdisplayedname"));
			}

			else if(inputValueMap.containsKey("pewan.interface.iesinterface.ipinterface.asingfilterpointer") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.iesinterface.ipinterface.asingfilterpointer"))) {
				aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(true);
				aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(true);
				aclPolicyCriteriaBean.setInboundIpv4AclName(inputValueMap.get("pewan.acl.asdescription"));
			}else {
				aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(false);
				aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(false);
				
			}
			
			if(inputValueMap.containsKey("pewan.interface.vprninterface.ipinterface.asaclfltipv6filter1") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asaclfltipv6filter1"))) {
				aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(true);
				aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(true);
				aclPolicyCriteriaBean.setInboundIpv6AclName(inputValueMap.get("pewan.acl.asdisplayedname"));
			}else if(inputValueMap.containsKey("pewan.interface.iesinterface.ipinterface.asingipv6fltrpointer") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.iesinterface.ipinterface.asingipv6fltrpointer"))) {
				aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(true);
				aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(true);
				aclPolicyCriteriaBean.setInboundIpv6AclName(inputValueMap.get("pewan.acl.asdescription"));
			}else {
				aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(false);
				aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(false);
				
			}
			
			if(inputValueMap.containsKey("pewan.interface.vprninterface.ipinterface.asaclfilteripfilter2") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asaclfilteripfilter2"))) {
				aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(true);
				aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(true);
				aclPolicyCriteriaBean.setOutboundIpv4AclName(inputValueMap.get("pewan.acl.asdisplayedname"));
			}else if(inputValueMap.containsKey("pewan.interface.iesinterface.ipinterface.asegrfilterpointer") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.iesinterface.ipinterface.asegrfilterpointer"))) {
				aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(true);
				aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(true);
				aclPolicyCriteriaBean.setOutboundIpv4AclName(inputValueMap.get("pewan.acl.asdescription"));
			}else {
				aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(false);
				aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(false);
				
			}
			
			if(inputValueMap.containsKey("pewan.interface.vprninterface.ipinterface.asaclfltipv6filter2") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asaclfltipv6filter2"))) {
				aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(true);
				aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(true);
				aclPolicyCriteriaBean.setOutboundIpv6AclName(inputValueMap.get("pewan.acl.asdisplayedname"));
			}else if(inputValueMap.containsKey("pewan.interface.iesinterface.ipinterface.asegripv6fltrpointer") &&
					StringUtils.isNotEmpty(inputValueMap.get("pewan.interface.iesinterface.ipinterface.asegripv6fltrpointer"))) {
				aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(true);
				aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(true);
				aclPolicyCriteriaBean.setOutboundIpv6AclName(inputValueMap.get("pewan.acl.asdescription"));
			}else {
				aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(false);
				aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(false);
				
			}
		}
		else if(inputValueMap.containsKey("pe_equipmodule") && (inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR") ||
				inputValueMap.get("pe_equipmodule").equalsIgnoreCase("CR"))) {
			if(inputValueMap.containsKey("conn") && inputValueMap.get("conn").toLowerCase().contains("ethernet")) {
				if(inputValueMap.containsKey("pewan.ethernetip.ipinterface.jrinfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.ipinterface.jrinfilter1"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(true);
					aclPolicyCriteriaBean.setInboundIpv4AclName(inputValueMap.get("pewan.ethernetip.ipinterface.jrinfiltername1"));
				}else if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.jrinfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.subint.ipinterface.jrinfilter1"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(true);
					aclPolicyCriteriaBean.setInboundIpv4AclName(inputValueMap.get("pewan.ethernetip.subint.ipinterface.jrinfiltername1"));
				}else {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(false);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(false);
				}
				
				if(inputValueMap.containsKey("pewan.ethernetip.ipinterface.jrinfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.ipinterface.jrinfilter1v6"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(true);
				}else if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.jrinfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.subint.ipinterface.jrinfilter1v6"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(false);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(false);
				}
				
				if(inputValueMap.containsKey("pewan.ethernetip.ipinterface.jroutfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.ipinterface.jroutfilter1"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(true);
				}else if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.jroutfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.subint.ipinterface.jroutfilter1"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(false);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(false);
				}
				
				if(inputValueMap.containsKey("pewan.ethernetip.ipinterface.jroutfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.ipinterface.jroutfilter1v6"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(true);
				}else if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.jroutfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.ethernetip.subint.ipinterface.jroutfilter1v6"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(false);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(false);
				}
				
			}else if(inputValueMap.containsKey("conn") && inputValueMap.get("conn").toLowerCase().contains("sonet")) {
				if(inputValueMap.containsKey("pewan.sonetip.ipinterface.jrinfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.sonetip.ipinterface.jrinfilter1"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(false);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(false);
				}
				
				if(inputValueMap.containsKey("pewan.sonetip.ipinterface.jrinfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.sonetip.ipinterface.jrinfilter1v6"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(false);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(false);
					
				}
				
				if(inputValueMap.containsKey("pewan.sonetip.ipinterface.jroutfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.sonetip.ipinterface.jroutfilter1"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(false);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(false);
					
				}
				
				if(inputValueMap.containsKey("pewan.sonetip.ipinterface.jroutfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.sonetip.ipinterface.jroutfilter1v6"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(false);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(false);
				}
				
			}else if(inputValueMap.containsKey("conn") && inputValueMap.get("conn").toLowerCase().contains("serial")) {
				if(inputValueMap.containsKey("pewan.serialip.ipinterface.jrinfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.serialip.ipinterface.jrinfilter1"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(false);
					aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(false);
				}
				
				if(inputValueMap.containsKey("pewan.serialip.ipinterface.jrinfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.serialip.ipinterface.jrinfilter1v6"))) {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(false);
					aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(false);
				}
				
				if(inputValueMap.containsKey("pewan.serialip.ipinterface.jroutfilter1") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.serialip.ipinterface.jroutfilter1"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(false);
					aclPolicyCriteriaBean.setIsoutboundaclIpv4Preprovisioned(false);
					
				}
				
				if(inputValueMap.containsKey("pewan.serialip.ipinterface.jroutfilter1v6") &&
						StringUtils.isNotEmpty(inputValueMap.get("pewan.serialip.ipinterface.jroutfilter1v6"))) {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(true);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(true);
				}else {
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(false);
					aclPolicyCriteriaBean.setIsoutboundaclIpv6Preprovisioned(false);
					
				}
			}
		}
		aclPolicyCriteriaBeans.add(aclPolicyCriteriaBean);
	}

	private void setChannelizedSdhInterfaceBean(Set<ChannelizedSdhInterfaceBean> channelizedSdhInterfaceBeans, Map<String, String> inputValueMap) {
		if(inputValueMap.containsKey("conn") && (inputValueMap.get("conn").toLowerCase().contains("sonet") ||
				inputValueMap.get("conn").toLowerCase().contains("sdh")) ) {
			ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean = new ChannelizedSdhInterfaceBean();
			// ALCATEL
			if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
				channelizedSdhInterfaceBean.setInterfaceName(inputValueMap.get("pewan.interface.vprninterface.ipinterface.srname"));
				channelizedSdhInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.pport.srname"));

				// Setting IPv4 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.asipaddress")){
					channelizedSdhInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asprefixlength");
					channelizedSdhInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.asipaddress") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedSdhInterfaceBean.getIpv4Address())) {
					// Using values with Suffix '1'
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.asipaddress")) {
						channelizedSdhInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asipaddress"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asprefixlength");
						channelizedSdhInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.asipaddress")) {
					channelizedSdhInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asprefixlength");
					channelizedSdhInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress") + "/" + subnetMask);
				}
				// Setting IPv6 attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.asipaddress")) {
					channelizedSdhInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.asprefixlength");
					channelizedSdhInterfaceBean.setModifiedIipv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.asipaddress") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedSdhInterfaceBean.getIpv6Address())){
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.asipaddress")){
						channelizedSdhInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asipaddress"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asprefixlength");
						channelizedSdhInterfaceBean.setModifiedIipv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.asipaddress")){
					channelizedSdhInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asprefixlength");
					channelizedSdhInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress")+ "/" + subnetMask);
				}

				channelizedSdhInterfaceBean.setPosframing(inputValueMap.get("pewan.sonetip.pport.asframing"));
				if (inputValueMap.containsKey("pewan.sonetip.ds1e1channel.ds1e1chanspec.aschnframing")) {
					channelizedSdhInterfaceBean.setFraming(inputValueMap.get("pewan.sonetip.ds1e1channel.ds1e1chanspec.aschnframing"));
				} else {
					if ("DS1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == true) {
						channelizedSdhInterfaceBean.setFraming("ESF");
					} else if ("DS1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == false) {
						channelizedSdhInterfaceBean.setFraming("DS1_UNFRAMED");
					} else if ("E1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == true) {
						channelizedSdhInterfaceBean.setFraming("NO_CRC_G704");
					} else if ("E1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == false) {
						channelizedSdhInterfaceBean.setFraming("E1_UNFRAMED");
					}
				}

				channelizedSdhInterfaceBean.setEncapsulation(inputValueMap.get("pewan.sonetip.sts3channel.asencaptype"));
				channelizedSdhInterfaceBean.setUpcount(inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.hdlc.asupcount"));
				channelizedSdhInterfaceBean.setKeepalive(inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.hdlc.askeepalive"));
				channelizedSdhInterfaceBean.setDowncount(inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.hdlc.asdowncount"));
				/*channelizedSdhInterfaceBean.setBfdMultiplier(inputValueMap.get("pewan.bfd.asbfdmultiplier"));
				channelizedSdhInterfaceBean.setBfdreceiveInterval(inputValueMap.get("pewan.bfd.asbfdrxinterval"));
				channelizedSdhInterfaceBean.setBfdtransmitInterval(inputValueMap.get("pewan.bfd.asbfdtxinterval"));

				if (channelizedSdhInterfaceBean.getBfdMultiplier().length() > 0 || channelizedSdhInterfaceBean.getBfdreceiveInterval().length() > 0 ||
						channelizedSdhInterfaceBean.getBfdtransmitInterval().length() > 0) {
					channelizedSdhInterfaceBean.setIsbfdEnabled(true);
				} else {
					channelizedSdhInterfaceBean.setIsbfdEnabled(false);
				}*/

			} else {
				channelizedSdhInterfaceBean.setInterfaceName(inputValueMap.get("pewan.sonetip.ipinterface.sremsname"));
				channelizedSdhInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.sonetip.pport.sremsname"));

				// Setting IPv4 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.riipaddr")){
					channelizedSdhInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddr"));
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask");
					}
					channelizedSdhInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddr") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedSdhInterfaceBean.getIpv4Address())) {
					// Using values with Suffix '1'
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.riipaddr")) {
						channelizedSdhInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddr"));
						String subnetMask = null;
						if(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask")==null ||
								inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask").equals("0")){
							try {
								subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipmask"))));
							} catch (UnknownHostException e) {
								logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
										inputValueMap.get("rfu1"), e.getMessage());
							}
						}
						else{
							subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask");
						}
						channelizedSdhInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddr") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.riipaddr")) {
					channelizedSdhInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddr"));
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask");
					}
					channelizedSdhInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddr") + "/" + subnetMask);
				}
				// Setting IPv6 Attributes

				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.riipaddr")) {
					channelizedSdhInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddr"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.riprefixlen");
					channelizedSdhInterfaceBean.setModifiedIipv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddr") + "/" + subnetMask);
				}
				else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.riipaddress")){
					channelizedSdhInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.riprefixlen");
					channelizedSdhInterfaceBean.setModifiedIipv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddress") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedSdhInterfaceBean.getIpv6Address())){
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.riipaddr")){
						channelizedSdhInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddr"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
						channelizedSdhInterfaceBean.setModifiedIipv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddr") + "/" + subnetMask);
					}
					else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.riipaddress")){
						channelizedSdhInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddress"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
						channelizedSdhInterfaceBean.setModifiedIipv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.riipaddr")){
					channelizedSdhInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddr"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
					channelizedSdhInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddr")+ "/" + subnetMask);
				}

				if (inputValueMap.containsKey("pewan.sonetip.ipinterface.rimtu") && Integer.valueOf(inputValueMap.get("pewan.sonetip.ipinterface.rimtu")) > 0) {
					channelizedSdhInterfaceBean.setMtu(inputValueMap.get("pewan.sonetip.ipinterface.rimtu"));
				}
				if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")) {
					channelizedSdhInterfaceBean.setPosframing(inputValueMap.get("pewan.sonetip.pport.jrframing"));
					if (inputValueMap.containsKey("pewan.sonetip.ipinterface.jrencapsulation") &&
							inputValueMap.get("pewan.sonetip.ipinterface.jrencapsulation").toLowerCase().contains("hdlc")) {
						channelizedSdhInterfaceBean.setEncapsulation("HDLC");
					}
				} else {
					channelizedSdhInterfaceBean.setPosframing(null);
					if (inputValueMap.containsKey("pewan.sonetip.ipinterface.crencapstype") &&
							inputValueMap.get("pewan.sonetip.ipinterface.crencapstype").toLowerCase().contains("hdlc")) {
						channelizedSdhInterfaceBean.setEncapsulation("HDLC");
					}
				}
			}

			if (channelizedSdhInterfaceBean.get_64kFirstTimeSlot() != null && channelizedSdhInterfaceBean.get_64kLastTimeSlot() != null) {
				channelizedSdhInterfaceBean.setIsframed(true);
			} else {
				channelizedSdhInterfaceBean.setIsframed(false);
			}
			if ("DS1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == true) {
				channelizedSdhInterfaceBean.setFraming("ESF");
			} else if ("DS1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == false) {
				channelizedSdhInterfaceBean.setFraming("DS1_UNFRAMED");
			} else if ("E1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == true) {
				channelizedSdhInterfaceBean.setFraming("NO_CRC_G704");
			} else if ("E1".equalsIgnoreCase(channelizedSdhInterfaceBean.getPortType()) && channelizedSdhInterfaceBean.getIsframed() == false) {
				channelizedSdhInterfaceBean.setFraming("E1_UNFRAMED");
			}

			channelizedSdhInterfaceBeans.add(channelizedSdhInterfaceBean);
		}
	}

	private void setChannelizedE1serialInterfaceBean(Set<ChannelizedE1serialInterfaceBean> channelizedE1serialInterfaceBeans, Map<String, String> inputValueMap) {
		if(inputValueMap.containsKey("conn") && inputValueMap.get("conn").toLowerCase().contains("serial")) {
			ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean=new ChannelizedE1serialInterfaceBean();

			// ALCATEL
			if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
				channelizedE1serialInterfaceBean.setInterfaceName(inputValueMap.get("pewan.interface.vprninterface.ipinterface.srname"));
				channelizedE1serialInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.pport.srname"));
				// Setting IPv4 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.asipaddress") &&
						inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.asipaddress")){
					channelizedE1serialInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asprefixlength");
					channelizedE1serialInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.asipaddress") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedE1serialInterfaceBean.getIpv4Address())) {
					// Using values with Suffix '1'
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.asipaddress")) {
						channelizedE1serialInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asipaddress"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asprefixlength");
						channelizedE1serialInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.asipaddress")) {
					channelizedE1serialInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asprefixlength");
					channelizedE1serialInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress") + "/" + subnetMask);
				}
				// Setting IPv6 attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.asipaddress")) {
					channelizedE1serialInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.asprefixlength");
					channelizedE1serialInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.asipaddress") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedE1serialInterfaceBean.getIpv6Address())){
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.asipaddress")){
						channelizedE1serialInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asipaddress"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asprefixlength");
						channelizedE1serialInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.asipaddress")){
					channelizedE1serialInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asprefixlength");
					channelizedE1serialInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress")+ "/" + subnetMask);
				}

				if (inputValueMap.containsKey("pewan.serialip.ds3e3channel.ds3e3chanspec.aschannelfrmng")) {
					channelizedE1serialInterfaceBean.setIsframed(true);
				} else {
					channelizedE1serialInterfaceBean.setIsframed(false);
				}
				channelizedE1serialInterfaceBean.setFraming(inputValueMap.get("pewan.serialip.pport.asframing"));
				if (inputValueMap.containsKey("pewan.serialip.ds1e1channel.ds0channel.asencaptype") && inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.asencaptype").toLowerCase().contains("hdlc")) {
					channelizedE1serialInterfaceBean.setEncapsulation("HDLC");
				}
				channelizedE1serialInterfaceBean.setUpcount(inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.hdlc.asupcount"));
				channelizedE1serialInterfaceBean.setKeepalive(inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.hdlc.askeepalive"));
				channelizedE1serialInterfaceBean.setDowncount(inputValueMap.get("pewan.serialip.ds1e1channel.ds0channel.hdlc.asdowncount"));
				/*channelizedE1serialInterfaceBean.setBfdMultiplier(inputValueMap.get("pewan.bfd.asbfdmultiplier"));
				channelizedE1serialInterfaceBean.setBfdreceiveInterval(inputValueMap.get("pewan.bfd.asbfdrxinterval"));
				channelizedE1serialInterfaceBean.setBfdtransmitInterval(inputValueMap.get("pewan.bfd.asbfdtxinterval"));

				if (channelizedE1serialInterfaceBean.getBfdMultiplier().length() > 0 || channelizedE1serialInterfaceBean.getBfdreceiveInterval().length() > 0 ||
						channelizedE1serialInterfaceBean.getBfdtransmitInterval().length() > 0) {
					channelizedE1serialInterfaceBean.setIsbfdEnabled(true);
				} else {
					channelizedE1serialInterfaceBean.setIsbfdEnabled(false);
				}*/
			} else {
				channelizedE1serialInterfaceBean.setInterfaceName(inputValueMap.get("pewan.sonetip.ipinterface.sremsname"));
				channelizedE1serialInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.sonetip.pport.sremsname"));

				// Setting IPv4 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.riipaddr")){
					channelizedE1serialInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddr"));
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask");
					}
					channelizedE1serialInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddr") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedE1serialInterfaceBean.getIpv4Address())) {
					// Using values with Suffix '1'
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.riipaddr")) {
						channelizedE1serialInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddr"));
						String subnetMask = null;
						if(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask")==null ||
								inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask").equals("0")){
							try {
								subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipmask"))));
							} catch (UnknownHostException e) {
								logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
										inputValueMap.get("rfu1"), e.getMessage());
							}
						}
						else{
							subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask");
						}
						channelizedE1serialInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddr") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.riipaddr")) {
					channelizedE1serialInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddr"));
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask");
					}
					channelizedE1serialInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddr") + "/" + subnetMask);
				}
				// Setting IPv6 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.riipaddr")) {
					channelizedE1serialInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddr"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.riprefixlen");
					channelizedE1serialInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddr") + "/" + subnetMask);
				}
				else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.riipaddress")){
					channelizedE1serialInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.riprefixlen");
					channelizedE1serialInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddress") + "/" + subnetMask);
				}
				if(Objects.isNull(channelizedE1serialInterfaceBean.getIpv6Address())){
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.riipaddr")){
						channelizedE1serialInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddr"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
						channelizedE1serialInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddr") + "/" + subnetMask);
					}
					else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.riipaddress")){
						channelizedE1serialInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddress"));
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
						channelizedE1serialInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.riipaddr")){
					channelizedE1serialInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddr"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
					channelizedE1serialInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddr")+ "/" + subnetMask);
				}

				if (channelizedE1serialInterfaceBean.getFirsttimeSlot() != null && channelizedE1serialInterfaceBean.getLasttimeSlot() != null) {
					channelizedE1serialInterfaceBean.setIsframed(true);
				} else {
					channelizedE1serialInterfaceBean.setIsframed(false);
				}

				if (inputValueMap.containsKey("pewan.serialip.ipinterface.rimtu") && Integer.valueOf(inputValueMap.get("pewan.serialip.ipinterface.rimtu")) > 0) {
					channelizedE1serialInterfaceBean.setMtu(inputValueMap.get("pewan.serialip.ipinterface.rimtu"));
				}

				if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")) {
					channelizedE1serialInterfaceBean.setFraming(inputValueMap.get("pewan.serialip.pport.jrframing"));
					if (inputValueMap.containsKey("pewan.serialip.ipinterface.jrencapsulation") && inputValueMap.get("pewan.serialip.ipinterface.jrencapsulation").toLowerCase().contains("hdlc")) {
						channelizedE1serialInterfaceBean.setEncapsulation("HDLC");
					}
				} else {
					channelizedE1serialInterfaceBean.setFraming(null);
					if (inputValueMap.containsKey("pewan.serialip.ipinterface.crencapstype") && inputValueMap.get("pewan.serialip.ipinterface.crencapstype").toLowerCase().contains("hdlc")) {
						channelizedE1serialInterfaceBean.setEncapsulation("HDLC");
					}
				}
			}
			channelizedE1serialInterfaceBeans.add(channelizedE1serialInterfaceBean);
		}

	}

	private void setBgpBean(Set<BgpBean> bgpBeans, Map<String, String> inputValueMap) {

		if(inputValueMap.containsKey("proto") && inputValueMap.get("proto").toLowerCase().equalsIgnoreCase("bgp")) {
			BgpBean bgpBean=new BgpBean();
			// ALCATEL
			if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
				bgpBean.setAluBgpPeerName(inputValueMap.get("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname"));

				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.peer.aspeeras")
						&& !inputValueMap.get("pewan.proto.bgp.site.group.peer.aspeeras").isEmpty()) {
					if (Integer.valueOf(inputValueMap.get("pewan.proto.bgp.site.group.peer.aspeeras")) != 0) {
						bgpBean.setRemoteAsNumber(inputValueMap.get("pewan.proto.bgp.site.group.peer.aspeeras"));
					}
				}
				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.aspeeras")
						&& !inputValueMap.get("pewan.proto.bgp.site.group.aspeeras").isEmpty()) {
					if (Integer.valueOf(inputValueMap.get("pewan.proto.bgp.site.group.aspeeras")) != 0) {
						bgpBean.setRemoteAsNumber(inputValueMap.get("pewan.proto.bgp.site.group.aspeeras"));
					}
				}

				bgpBean.setRemoteUpdateSourceInterface(inputValueMap.get("pewan.vprnbgploopback1.srname"));
				bgpBean.setRemoteUpdateSourceIpv4Address(inputValueMap.get("pewan.vprnbgploopback1.ipaddressing.local.ipv4address1.asipaddress"));
				bgpBean.setRemoteUpdateSourceIpv6Address(inputValueMap.get("pewan.vprnbgploopback1.ipaddressing.local.ipv6address1.asipaddress"));
				bgpBean.setLocalAsNumber(inputValueMap.get("pewan.proto.bgp.site.group.aslocalas"));
				bgpBean.setRemoteIpv4Address(inputValueMap.get("pewan.ipaddressing.remote.ipv4address.riipaddr"));
				if (bgpBean.getRemoteAsNumber() != null && Integer.valueOf(bgpBean.getRemoteAsNumber()) > 64512 &&
						Integer.valueOf(bgpBean.getRemoteAsNumber()) < 65535) {
					bgpBean.setAsoOverride(true);
				} else {
					bgpBean.setAsoOverride(false);
				}
				bgpBean.setMaxPrefix(inputValueMap.get("pewan.proto.bgp.site.group.asprefixlimit"));
				bgpBean.setMaxPrefixThreshold(inputValueMap.get("pewan.proto.bgp.site.group.asprefixthreshold"));
				if (inputValueMap.containsKey("pewan.soo.nrt.ritype") && inputValueMap.get("pewan.soo.nrt.ritype").toLowerCase().contains("siteoforigin")) {
					bgpBean.setSsoRequired(true);
				} else {
					bgpBean.setSsoRequired(false);
				}
				bgpBean.setNeighbourshutdownRequired(false);
				bgpBean.setRedistributeStaticEnabled(false);
				bgpBean.setRedistributeConnectedEnabled(false);
				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.askeepalive") &&
						Integer.valueOf(inputValueMap.get("pewan.proto.bgp.site.group.askeepalive")) > 0) {
					bgpBean.setAluKeepalive(inputValueMap.get("pewan.proto.bgp.site.group.askeepalive"));
				}
				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.asholdtime") &&
						Integer.valueOf(inputValueMap.get("pewan.proto.bgp.site.group.asholdtime")) > 0) {
					bgpBean.setHoldTime(inputValueMap.get("pewan.proto.bgp.site.group.asholdtime"));
				}
				bgpBean.setNeighborOn(inputValueMap.containsKey("pewan.vprnbgploopback1.srname") ? "LOOPBACK" : "WAN");
				bgpBean.setIsauthenticationRequired(false);
				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.asdisableextcomms") &&
						inputValueMap.get("pewan.proto.bgp.site.group.asdisableextcomms").equalsIgnoreCase("true")) {
					bgpBean.setAluDisableBgpPeerGrpExtCommunity(true);
				}
				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.asmedvalue") &&
						Integer.valueOf(inputValueMap.get("pewan.proto.bgp.site.group.asmedvalue")) > 0) {
					bgpBean.setMedValue(inputValueMap.get("pewan.proto.bgp.site.group.asmedvalue"));
				}
				if (inputValueMap.containsKey("pewan.proto.bgp.site.group.aspeertype")) {
					bgpBean.setPeerType(inputValueMap.get("pewan.proto.bgp.site.group.aspeertype").toUpperCase());
				}
				bgpBean.setLocalPreference(inputValueMap.get("pewan.proto.bgp.site.group.peer.aslocalpreference"));
				bgpBean.setV6LocalPreference(inputValueMap.get("pewan.proto.bgp.site.group.peer.aslocalpreferencev6"));

				String aluBackupKey = "pewan.proto.bgp.site.asipv4";

				if (inputValueMap.containsKey(aluBackupKey)){
					if(inputValueMap.get(aluBackupKey).toLowerCase().contains("ipv4") && inputValueMap.get(aluBackupKey).toLowerCase().contains("ipv6")) {
						bgpBean.setAluBackupPath("BOTH");
					}
					else if (inputValueMap.get(aluBackupKey).toLowerCase().contains("ipv4")) {
						bgpBean.setAluBackupPath("IPV4");
					}
					else if(inputValueMap.get(aluBackupKey).toLowerCase().contains("ipv6")) {
						bgpBean.setAluBackupPath("IPV6");
					}
				}
				else{
					bgpBean.setAluBackupPath("NOT_APPLICABLE");
				}
				bgpBean.setRemoteCeAsnumber(inputValueMap.get("cewan.proto.bgp.neighbour.crlocalasndot"));
			}
			// JUNIPER
			else {
				if(inputValueMap.containsKey("pewan.proto.bgp.neighbour.riasn")){
					bgpBean.setRemoteAsNumber(inputValueMap.get("pewan.proto.bgp.neighbour.riasn"));
				}
				else {
					bgpBean.setRemoteAsNumber(inputValueMap.get("cec_remoteasn"));
				}
				if (inputValueMap.containsKey("ipstack") && (inputValueMap.get("ipstack").toLowerCase().contains("dual") ||
						inputValueMap.get("ipstack").toLowerCase().contains("v4"))) {
					bgpBean.setRemoteUpdateSourceInterface(inputValueMap.get("pewan.proto.bgp.neighbour.riupdatesrc"));
				}
				else if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v6")) {
					bgpBean.setRemoteUpdateSourceInterface(inputValueMap.get("pewan.proto.bgp.v6neighbour.riupdatesrc"));
				}
				bgpBean.setRemoteIpv4Address(inputValueMap.get("pewan.ipaddressing.remote.ipv4address.riipaddr"));
				bgpBean.setMaxPrefix(inputValueMap.getOrDefault("pewan.proto.bgp.neighbour.af.jrmxplval",
						inputValueMap.get("pewan.proto.bgp.v6neighbour.v6af.jrmxplval")));
				bgpBean.setRemoteUpdateSourceIpv4Address(inputValueMap.get("pewan.proto.bgp.neighbour.riipaddress"));
				bgpBean.setRemoteUpdateSourceIpv6Address(inputValueMap.get("pewan.proto.bgp.v6neighbour.riipaddress"));
				bgpBean.setLocalAsNumber(inputValueMap.get("pewan.proto.bgp.instance.riasn"));
				bgpBean.setMaxPrefix(inputValueMap.get(""));
				if (inputValueMap.containsKey("pewan.proto.bgp.neighbour.riasoverride") && inputValueMap.get("pewan.proto.bgp.neighbour.riasoverride")
						.equalsIgnoreCase("Enable")) {
					bgpBean.setAsoOverride(true);
				} else {
					bgpBean.setAsoOverride(false);
				}
				bgpBean.setSsoRequired(false);
				bgpBean.setNeighbourshutdownRequired(false);
				bgpBean.setRedistributeStaticEnabled(false);
				if (inputValueMap.containsKey("pewan.proto.bgp.neighbour.ribgphold") && Integer.valueOf(inputValueMap.get("pewan.proto.bgp.neighbour.ribgphold")) > 0) {
					bgpBean.setHoldTime(inputValueMap.get("pewan.proto.bgp.neighbour.ribgphold"));
				}
				if (inputValueMap.containsKey("ipstack") && (inputValueMap.get("ipstack").toLowerCase().contains("dual") ||
						inputValueMap.get("ipstack").toLowerCase().contains("v4"))) {
					if (inputValueMap.containsKey("pewan.proto.bgp.neighbour.riipaddress") && inputValueMap.get("pewan.proto.bgp.neighbour.riipaddress")
							.equalsIgnoreCase(inputValueMap.get("pec_ipaddress").split("/")[0])) {
						bgpBean.setNeighborOn("WAN");
					} else {
						bgpBean.setNeighborOn("LOOPBACK");
					}
				} else if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v6")) {
					if (inputValueMap.containsKey("pewan.proto.bgp.v6neighbour.riipaddress") && inputValueMap.get("pewan.proto.bgp.v6neighbour.riipaddress")
							.equalsIgnoreCase(inputValueMap.get("pec_ipv6address").split("/")[0])) {
						bgpBean.setNeighborOn("WAN");
					} else {
						bgpBean.setNeighborOn("LOOPBACK");
					}
				}

				if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")) {
					if (inputValueMap.containsKey("ipstack") && (inputValueMap.get("ipstack").toLowerCase().contains("dual") ||
							inputValueMap.get("ipstack").toLowerCase().contains("v4"))) {
						bgpBean.setAluBgpPeerName(inputValueMap.get("pewan.proto.bgp.peergroup1.srname"));
					} else if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v6")) {
						bgpBean.setAluBgpPeerName(inputValueMap.get("pewan.proto.bgp.peergroup2.srname"));
					}
					if (inputValueMap.containsKey("pewan.proto.bgp.neighbour.jrlocaladdress") && !inputValueMap.get("pewan.proto.bgp.neighbour.jrlocaladdress").toLowerCase().contains("inherited")) {
						bgpBean.setLocalUpdateSourceIpv4Address(inputValueMap.get("pewan.proto.bgp.neighbour.jrlocaladdress"));
					}

					if (inputValueMap.containsKey("pewan.proto.bgp.v6neighbour.jrlocaladdress") && !inputValueMap.get("pewan.proto.bgp.v6neighbour.jrlocaladdress").toLowerCase().contains("inherited")) {
						bgpBean.setLocalUpdateSourceIpv4Address(inputValueMap.get("pewan.proto.bgp.v6neighbour.jrlocaladdress"));
					}

					if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual") || inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v4")) {
						bgpBean.setMaxPrefix(inputValueMap.get("pewan.proto.bgp.neighbour.af.jrmxplval"));
					} else if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v6")) {
						bgpBean.setMaxPrefix(inputValueMap.get("pewan.proto.bgp.v6neighbour.v6af.jrmxplval"));
					}
					if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual") || inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v4")) {
						bgpBean.setMaxPrefixThreshold(inputValueMap.get("pewan.proto.bgp.neighbour.af.jrteardnper"));
					} else if (inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v6")) {
						bgpBean.setMaxPrefixThreshold(inputValueMap.get("pewan.proto.bgp.v6neighbour.v6af.jrteardnper"));
					}
					bgpBean.setRedistributeConnectedEnabled(false);
					if (inputValueMap.containsKey("pewan.proto.bgp.neighbour.jrbgplocpref") && Integer.valueOf(inputValueMap.get("pewan.proto.bgp.neighbour.jrbgplocpref")) > 0) {
						bgpBean.setLocalPreference(inputValueMap.get("pewan.proto.bgp.neighbour.jrbgplocpref"));
					}
					if (inputValueMap.containsKey("pewan.proto.bgp.v6neighbour.jrbgplocpref") && Integer.valueOf(inputValueMap.get("pewan.proto.bgp.v6neighbour.jrbgplocpref")) > 0) {
						bgpBean.setV6LocalPreference(inputValueMap.get("pewan.proto.bgp.v6neighbour.jrbgplocpref"));
					}

				} else {
					bgpBean.setRedistributeConnectedEnabled(true);
					if (inputValueMap.containsKey("pewan.proto.bgp.neighbour.ribgphold") && Integer.valueOf(inputValueMap.get("pewan.proto.bgp.neighbour.ribgphold")) > 0) {
						bgpBean.setHelloTime(inputValueMap.get("pewan.proto.bgp.neighbour.ribgphold"));
					}
					if (inputValueMap.containsKey("pewan.proto.bgp.instance.ribgplocpref") && Integer.valueOf(inputValueMap.get("pewan.proto.bgp.instance.ribgplocpref")) > 0) {
						bgpBean.setLocalPreference(inputValueMap.get("pewan.proto.bgp.instance.ribgplocpref"));
					}
					if (inputValueMap.containsKey("pewan.proto.bgp.instance.ribgplocprefv6") && Integer.valueOf(inputValueMap.get("pewan.proto.bgp.instance.ribgplocprefv6")) > 0) {
						bgpBean.setV6LocalPreference(inputValueMap.get("pewan.proto.bgp.instance.ribgplocprefv6"));
					}

				}
			}

			// Constructing Policies

			List<PolicyTypeBean> policyTypeBeans = new ArrayList<>();
			PolicyTypeBean importPolicyTypeBean = new PolicyTypeBean();
			PolicyTypeBean importPolicyv6TypeBean = new PolicyTypeBean();
			PolicyTypeBean exportPolicyTypeBean = new PolicyTypeBean();
			PolicyTypeBean exportPolicyv6TypeBean = new PolicyTypeBean();

			// ALCATEL
			if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {

				if(inputValueMap.containsKey("pewan.proto.bgp.site.group.importpolicy.aspolicy1")){
					String completeImportPolicyName = inputValueMap.get("pewan.proto.bgp.site.group.importpolicy.aspolicy1");
					if(completeImportPolicyName!=null) {
						String cipn[]=completeImportPolicyName.split(":");
						if(cipn.length>1) {
							String number = completeImportPolicyName.split(":")[1];

							if(inputValueMap.containsKey("pewan.routingpolicy" + number + ".srname")){
								importPolicyTypeBean.setInboundIpv4PolicyName(inputValueMap.get("pewan.routingpolicy" + number + ".srname"));
							}

							else if(completeImportPolicyName.contains("SRNAME") || completeImportPolicyName.contains("$")){
								int startIndex = completeImportPolicyName.indexOf('(') + 1;
								int endIndex = completeImportPolicyName.lastIndexOf(')');
								importPolicyTypeBean.setInboundIpv4PolicyName(completeImportPolicyName.substring(startIndex, endIndex));
							}
						}
					}
				}

				importPolicyTypeBean.setInboundIpv4Preprovisioned(true);
				importPolicyTypeBean.setInboundRoutingIpv4Policy(true);
				policyTypeBeans.add(importPolicyTypeBean);

				// Export Policy

				if(inputValueMap.containsKey("pewan.proto.bgp.site.group.exportpolicy.aspolicy1")){
					String completeExportPolicyName = inputValueMap.get("pewan.proto.bgp.site.group.exportpolicy.aspolicy1");
					if(completeExportPolicyName!=null) {
						String cepn[]=completeExportPolicyName.split(":");
						if(cepn.length>1) {
							String number = completeExportPolicyName.split(":")[1];

							if(inputValueMap.containsKey("pewan.routingpolicy" + number + ".srname")){
								exportPolicyTypeBean.setOutboundIpv4PolicyName(inputValueMap.get("pewan.routingpolicy" + number + ".srname"));
							}

							else if(completeExportPolicyName.contains("SRNAME") || completeExportPolicyName.contains("$")){
								int startIndex = completeExportPolicyName.indexOf('(') + 1;
								int endIndex = completeExportPolicyName.lastIndexOf(')');
								exportPolicyTypeBean.setOutboundIpv4PolicyName(completeExportPolicyName.substring(startIndex, endIndex));
							}
						}
					}
				}

				exportPolicyTypeBean.setOutboundIpv4Preprovisioned(true);
				exportPolicyTypeBean.setOutboundRoutingIpv4Policy(true);
				policyTypeBeans.add(exportPolicyTypeBean);

			}
			// Only JUNIPER
			else if (inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")){

				// Import Policy
				if(inputValueMap.containsKey("pewan.proto.bgp.neighbour.jrimporttxt")){
					String completeImportPolicyName = inputValueMap.get("pewan.proto.bgp.neighbour.jrimporttxt");

					importPolicyTypeBean.setInboundIpv4PolicyName(completeImportPolicyName.split(" ")[0].trim().replaceAll("\\[",""));
					importPolicyTypeBean.setInboundIpv4Preprovisioned(true);
					importPolicyTypeBean.setInboundRoutingIpv4Policy(true);

					policyTypeBeans.add(importPolicyTypeBean);

				}

				// If DUAL
				if(inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual")) {

					if(inputValueMap.containsKey("pewan.proto.bgp.v6neighbour.jrimporttxt")) {
						String completeImportv6PolicyName = inputValueMap.get("pewan.proto.bgp.v6neighbour.jrimporttxt");

						importPolicyv6TypeBean.setInboundIpv6PolicyName(completeImportv6PolicyName.split(" ")[0].trim().replaceAll("\\[",""));
						importPolicyv6TypeBean.setInboundIpv6Preprovisioned(true);
						importPolicyv6TypeBean.setInboundRoutingIpv6Policy(true);

						policyTypeBeans.add(importPolicyv6TypeBean);
					}

				}

				// Export Policy
				if(inputValueMap.containsKey("pewan.proto.bgp.neighbour.jrexporttxt")){

					String completeExportPolicyName = inputValueMap.get("pewan.proto.bgp.neighbour.jrexporttxt");

					exportPolicyTypeBean.setOutboundIpv4PolicyName(completeExportPolicyName.split(" ")[0].trim().replaceAll("\\[",""));
					exportPolicyTypeBean.setOutboundIpv4Preprovisioned(true);
					exportPolicyTypeBean.setOutboundRoutingIpv4Policy(true);

					policyTypeBeans.add(exportPolicyTypeBean);

				}

				// If DUAL
				if(inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual")) {

					if(inputValueMap.containsKey("pewan.proto.bgp.v6neighbour.jrexporttxt")) {
						String completeExportv6PolicyName = inputValueMap.get("pewan.proto.bgp.v6neighbour.jrexporttxt");

						exportPolicyv6TypeBean.setOutboundIpv6PolicyName(completeExportv6PolicyName.split(" ")[0].trim().replaceAll("\\[",""));
						exportPolicyv6TypeBean.setOutboundIpv6Preprovisioned(true);
						exportPolicyv6TypeBean.setOutboundRoutingIpv6Policy(true);

						policyTypeBeans.add(exportPolicyv6TypeBean);
					}

				}
			}

			bgpBean.setPolicyTypes(policyTypeBeans);

			InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
			interfaceDetailBean.setIscpeWanInterface(false);
			interfaceDetailBean.setIscpeLanInterface(false);
			bgpBean.setInterfaceDetailBean(interfaceDetailBean);

			bgpBeans.add(bgpBean);
		}
	}

	private void setOrderDetailBean(OrderDetailBean orderDetailBean, Map<String, String> inputValueMap) {
//		orderDetailBean.setOrderStatus("CLOSED");
		orderDetailBean.setOriginatorName("FTI_REFRESH");
		orderDetailBean.setOriginatorDate(new Timestamp(new Date().getTime()));
		orderDetailBean.setGroupId("CMIP_MIGRATION_GROUP");
		if(inputValueMap.containsKey("pewan.customer.asdescription")){
			orderDetailBean.setSamCustomerDescription(inputValueMap.get("pewan.customer.asdescription"));
		}
		if(inputValueMap.containsKey("pewan.customer.assubscriberid")){
			orderDetailBean.setAluCustomerId(inputValueMap.get("pewan.customer.assubscriberid"));
		}
	}

	private void setVrfBeans(List<VrfBean> vrfBeans, Map<String, String> inputValueMap) {
//		vrfBean.setVrfName(inputValueMap.get("pewan.l3vpn.vrf.vrftable.srname"));			// Possibly Incorrect Value
		VrfBean vrfBean = new VrfBean();
		if(inputValueMap.containsKey("pewan.l3vpn.vrf.vrftable.rimaxroutes") &&
				Integer.valueOf(inputValueMap.get("pewan.l3vpn.vrf.vrftable.rimaxroutes"))>0) {
			vrfBean.setMaxRoutesValue(inputValueMap.get("pewan.l3vpn.vrf.vrftable.rimaxroutes"));
			vrfBean.setWarnOn("MAXROUTES");

		}else if(inputValueMap.containsKey("pewan.l3vpn.vrf.vrftable.rithreshold") &&
				Integer.valueOf(inputValueMap.get("pewan.l3vpn.vrf.vrftable.rithreshold"))>0) {
			vrfBean.setThreshold(inputValueMap.get("pewan.l3vpn.vrf.vrftable.rithreshold"));
			vrfBean.setWarnOn("THRESHOLD");
		}

		vrfBean.setIsvrfLiteEnabled(inputValueMap.containsKey("cewan.l3vpn.vrf.vrftable.srname") ? true : false);

		Set<PolicyTypeBean> policyTypeBeans = new HashSet<>();
		PolicyTypeBean importPolicyTypeBean=new PolicyTypeBean();
		PolicyTypeBean exportPolicyTypeBean=new PolicyTypeBean();

		importPolicyTypeBean.setIsvprnImportpolicy(true);
		importPolicyTypeBean.setIsvprnImportPreprovisioned(true);

		String vprnImportPolicyNameKey = "pewan.l3vpn.vrf.vrftable.siteimportpolicy.asrppolicystatement";

		if(inputValueMap.containsKey(vprnImportPolicyNameKey)){

			if(inputValueMap.get(vprnImportPolicyNameKey).contains("SRNAME")){
				String importName = inputValueMap.get(vprnImportPolicyNameKey);
				importPolicyTypeBean.setIsvprnImportpolicyName(importName.substring(importName.indexOf("VPRN")));
			}
			else{
				String number = inputValueMap.get(vprnImportPolicyNameKey).split(":")[1];
				String modifiedKey = "pewan.routingpolicy" + number + ".srname";

				if(inputValueMap.containsKey(modifiedKey)){
					String importName = inputValueMap.get(modifiedKey);
					importPolicyTypeBean.setIsvprnImportpolicyName(importName.substring(importName.indexOf("VPRN")));
				}
			}
		}

		exportPolicyTypeBean.setIsvprnExportpolicy(true);
		exportPolicyTypeBean.setIsvprnExportPreprovisioned(true);

		String vprnExportPolicyNameKey = "pewan.l3vpn.vrf.vrftable.siteexportpolicy.asrppolicystatement";

		if(inputValueMap.containsKey(vprnExportPolicyNameKey)){

			if(inputValueMap.get(vprnExportPolicyNameKey).contains("SRNAME")){
				String exportName = inputValueMap.get(vprnExportPolicyNameKey);
				importPolicyTypeBean.setIsvprnExportpolicyName(exportName.substring(exportName.indexOf("VPRN")));
			}
			else{
				String number = inputValueMap.get(vprnExportPolicyNameKey).split(":")[1];
				String modifiedKey = "pewan.routingpolicy" + number + ".srname";

				if(inputValueMap.containsKey(modifiedKey)){
					String exportName = inputValueMap.get(modifiedKey);
					importPolicyTypeBean.setIsvprnExportpolicyName(exportName.substring(exportName.indexOf("VPRN")));
				}
			}
		}

		policyTypeBeans.add(importPolicyTypeBean);
		policyTypeBeans.add(exportPolicyTypeBean);
		vrfBean.setIsmultivrf(false);
		vrfBean.setPolicyTypes(policyTypeBeans);
		vrfBeans.add(vrfBean);
	}

	private void setIpAddressDetailBean(IpAddressDetailBean ipAddressDetailBean, Map<String, String> inputValueMap, ServiceDetail serviceDetail) {
		ipAddressDetailBean.setPathIpType("V4");//default
		if(inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("dual")) {
			ipAddressDetailBean.setPathIpType("DUALSTACK");
		}else if(inputValueMap.containsKey("ipstack") && inputValueMap.get("ipstack").toLowerCase().contains("v6")) {
			ipAddressDetailBean.setPathIpType("V6");
		}
		if(ipAddressDetailBean.getExtendedLanEnabled()==true){
			if(Objects.nonNull(serviceDetail)){
				Set<IpAddressDetail> ipAddressDetails = serviceDetail.getIpAddressDetails();
				if(!CollectionUtils.isEmpty(ipAddressDetails)){
					IpAddressDetail ipAddressDetail = ipAddressDetails.stream().findFirst().get();
					ipAddressDetailBean.setNoMacAddress(gvpnRuleEngineService.findNoMac(ipAddressDetail));
				}
			}
		}
	}

	private void setAluSchedulerPolicyBean(List<AluSchedulerPolicyBean> aluSchedulerPolicyBeans, Map<String, String> inputValueMap) {
		AluSchedulerPolicyBean aluSchedulerPolicyBean = new AluSchedulerPolicyBean();
		if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
			if(inputValueMap.containsKey("pewan.l3qos.policy.policymap1.srname")) {
				aluSchedulerPolicyBean.setSchedulerPolicyName(inputValueMap.get("pewan.l3qos.policy.policymap1.srname"));
			    aluSchedulerPolicyBean.setSapIngressPolicyname(inputValueMap.get("pewan.l3qos.ingpolicy.policymap1.srname"));
				aluSchedulerPolicyBean.setSapEgressPolicyname(inputValueMap.get("pewan.l3qos.egrpolicy.policymap1.srname"));
			}else if(inputValueMap.containsKey("pewan.l3qos.policy.policymap2.srname")) {
				aluSchedulerPolicyBean.setSchedulerPolicyName(inputValueMap.get("pewan.l3qos.policy.policymap2.srname"));
			    aluSchedulerPolicyBean.setSapIngressPolicyname(inputValueMap.get("pewan.l3qos.ingpolicy.policymap2.srname"));
				aluSchedulerPolicyBean.setSapEgressPolicyname(inputValueMap.get("pewan.l3qos.egrpolicy.policymap2.srname"));
			}else {
				aluSchedulerPolicyBean.setSchedulerPolicyName(inputValueMap.get("pewan.l3qos.policy.policymap.srname"));
			    aluSchedulerPolicyBean.setSapIngressPolicyname(inputValueMap.get("pewan.l3qos.ingpolicy.policymap.srname"));
				aluSchedulerPolicyBean.setSapEgressPolicyname(inputValueMap.get("pewan.l3qos.egrpolicy.policymap.srname"));
			}
			aluSchedulerPolicyBean.setSapIngressPreprovisioned(false);
			aluSchedulerPolicyBean.setSapEgressPreprovisioned(false);
			aluSchedulerPolicyBean.setSchedulerPolicyIspreprovisioned(false);
			aluSchedulerPolicyBeans.add(aluSchedulerPolicyBean);
		}
	}

	private void setRouterDetailBean(Set<RouterDetailBean> routerDetailBeans, Map<String, String> inputValueMap) {
		RouterDetailBean routerDetailBean= new RouterDetailBean();
		if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")) {
			routerDetailBean.setRouterMake("JUNIPER");
		}else if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")) {
			routerDetailBean.setRouterMake("ALCATEL IP");
		}else if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("CR")) {
			routerDetailBean.setRouterMake("CISCO IP");
		}
		routerDetailBean.setRouterType("MPLS");
		routerDetailBean.setRouterRole("PE");
		if(inputValueMap.containsKey("pewan.customer.site.assubscribersiteid")){
			routerDetailBean.setIpv4MgmtAddress(inputValueMap.get("pewan.customer.site.assubscribersiteid"));
		}
		if(inputValueMap.containsKey("pewan.endpoint")) {
			String hostname[] = inputValueMap.get("pewan.endpoint").split("/");
			routerDetailBean.setRouterHostname((hostname.length > 1) ? hostname[1] : hostname[0]);
		}
		//routermodel to be populated by crammer.
		routerDetailBeans.add(routerDetailBean);
	}

	private void constructInterfaceDetailBean(InterfaceDetailBean migratedInterfaceDetailBean, InterfaceDetailBean interfaceDetailBean) {
		interfaceDetailBean.setIscpeLanInterface(migratedInterfaceDetailBean.getIscpeLanInterface());
		interfaceDetailBean.setIscpeWanInterface(migratedInterfaceDetailBean.getIscpeWanInterface());
		interfaceDetailBean.setStartDate(new Timestamp(new Date().getTime()));
		interfaceDetailBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
		interfaceDetailBean.setModifiedBy("FTI_Refresh");
	}

	private void setServiceDetailBean(ServiceDetailBean serviceDetailBean, String serviceCode, Map<String, String> inputValueMap) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findFirstByUuidAndScOrderUuidIsNotNullOrderByIdDesc(serviceCode);
		if(Objects.nonNull(scServiceDetail)){
			logger.info("setServiceDetailBean.ScServiceDetail exists : {}", scServiceDetail.getId());
			String orderCode = scServiceDetail.getScOrderUuid().trim();
			if(orderCode.startsWith("IAS") || orderCode.startsWith("ILL")){
				if(inputValueMap.containsKey("pewan.ethernetip.pport.rldescription")){
					String value = inputValueMap.get("pewan.ethernetip.pport.rldescription");
					if(value.contains("STDILL")){
						serviceDetailBean.setServiceSubtype("STDILL");
					}
					else if(value.contains("PILL")) {
						serviceDetailBean.setServiceSubtype("PILL");
					}
					else {
						String description[] = inputValueMap.get("pewan.ethernetip.pport.rldescription").split("/");
						serviceDetailBean.setServiceSubtype(description[description.length - 1]);
					}
				}else if(inputValueMap.containsKey("rfu3") && inputValueMap.get("rfu3")!=null && !inputValueMap.get("rfu3").isEmpty()){
					serviceDetailBean.setServiceSubtype(inputValueMap.get("rfu3"));
				}else if(inputValueMap.containsKey("pewan.interface.vprninterface.ipinterface.asdescription") 
						&& inputValueMap.get("pewan.interface.vprninterface.ipinterface.asdescription")!=null){
					String value = inputValueMap.get("pewan.interface.vprninterface.ipinterface.asdescription");
					if(value.contains("STDILL")){
						serviceDetailBean.setServiceSubtype("STDILL");
					}
					else if(value.contains("PILL")) {
						serviceDetailBean.setServiceSubtype("PILL");
					}
					else {
						String description[] = inputValueMap.get("pewan.interface.vprninterface.ipinterface.asdescription").split("/");
						serviceDetailBean.setServiceSubtype(description[description.length - 1]);
					}
				}
			}
			else{
				serviceDetailBean.setServiceSubtype(inputValueMap.get("rfu3"));
			}
		}
		serviceDetailBean.setServiceId(inputValueMap.get("rfu1"));
		serviceDetailBean.setServiceState("ACTIVE");
		serviceDetailBean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());			// FTI providing wrong values for few cases
		serviceDetailBean.setNetpRefid(inputValueMap.get("rfu5"));
		serviceDetailBean.setModifiedBy("FTI_Refresh");
		serviceDetailBean.setIsdowntimeReqd(false);
		serviceDetailBean.setExpediteTerminate(false);
		serviceDetailBean.setTriggerNccmOrder(false);
		serviceDetailBean.setIscustomConfigReqd(false);
		serviceDetailBean.setSkipDummyConfig(false);

		if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.ridescription") &&
				inputValueMap.get("pewan.ethernetip.subint.ipinterface.ridescription").toLowerCase().contains("/single")) {
			serviceDetailBean.setRedundancyRole("SINGLE");
		}
		else if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.ridescription") &&
				inputValueMap.get("pewan.ethernetip.subint.ipinterface.ridescription").toLowerCase().contains("/sec")) {
			serviceDetailBean.setRedundancyRole("SECONDARY");
		}
		else if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.ridescription") &&
				inputValueMap.get("pewan.ethernetip.subint.ipinterface.ridescription").toLowerCase().contains("/pri")) {
			serviceDetailBean.setRedundancyRole("PRIMARY");
		}
		if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")){
			serviceDetailBean.setRouterMake("JUNIPER");
		}
		else if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("AS")){
			serviceDetailBean.setRouterMake("ALCATEL IP");
			if(!inputValueMap.containsKey("pewan.vprn.assvmgrid") || (Integer.valueOf(inputValueMap.get("pewan.vprn.assvmgrid")) <= 0)){
				if(inputValueMap.containsKey("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname")){
					Pattern pattern = Pattern.compile("(?:_)(\\d+)(?:_)");
					Matcher matcher = pattern.matcher(inputValueMap.get("pewan.proto.bgp.site.group.bgpmd5key.asbgppeergroupname"));
					if(matcher.find()){
						serviceDetailBean.setCsoSammgrId(matcher.group(1));
					}
				}
			} else{
				serviceDetailBean.setCsoSammgrId(inputValueMap.get("pewan.vprn.assvmgrid"));
			}
			serviceDetailBean.setAluSvcName(inputValueMap.get("pewan.vprn.asdisplayedname"));
			serviceDetailBean.setAluSvcid(inputValueMap.get("pewan.vprn.asserviceid"));
		}
		else if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("CR")){
			serviceDetailBean.setRouterMake("CISCO IP");
		}
		serviceDetailBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceDetailBean.setServiceComponenttype("PORT");
		serviceDetailBean.setStartDate(new Timestamp(new Date().getTime()));
	}

	private void setEthernetInterfaceBean(Set<EthernetInterfaceBean> ethernetInterfaceBeans, Map<String, String> inputValueMap) {
		if(inputValueMap.containsKey("conn") && inputValueMap.get("conn").toLowerCase().contains("ethernet")) {
			EthernetInterfaceBean ethernetInterfaceBean = new EthernetInterfaceBean();

			// Common for both Alcatel & Cisco/Juniper
			if(inputValueMap.containsKey("pec_ipv6address") && StringUtils.isNotEmpty(inputValueMap.get("pec_ipv6address"))){
				ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pec_ipv6address").split("/")[0]);
				ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pec_ipv6address"));
			}

			if(inputValueMap.containsKey("pec_ipaddress") && StringUtils.isNotEmpty(inputValueMap.get("pec_ipaddress"))){
				ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pec_ipaddress").split("/")[0]);
				String subnetMask = null;
				try {
					if(inputValueMap.get("pec_ipaddress").split("/")[1].contains(".")) {
						subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pec_ipaddress").split("/")[1])));
					}
					else{
						subnetMask = inputValueMap.get("pec_ipaddress").split("/")[1];
					}
				} catch (UnknownHostException e) {
					logger.error("UnknownHostException while populating PE Side Ethernet Interface for serviceCode {}. Error : {}",
							inputValueMap.get("rfu1"), e.getMessage());
				}
				ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pec_ipaddress").split("/")[0] + "/" + subnetMask);
			}

			// ALCATEL
			if(inputValueMap.containsKey("pe_equipmodule") && "AS".equalsIgnoreCase(inputValueMap.get("pe_equipmodule"))) {
				ethernetInterfaceBean.setInterfaceName(inputValueMap.get("pewan.interface.vprninterface.ipinterface.srname"));
				if(inputValueMap.containsKey("pewan.pport.srname")){
					ethernetInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.pport.srname"));
				}
				else if(inputValueMap.containsKey("pewan.lagip.site.laginterface.srname")){
					ethernetInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.lagip.site.laginterface.srname"));
				}
				// Setting IPv4 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.asipaddress") &&
						inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.asipaddress")){
					if(Objects.isNull(ethernetInterfaceBean.getIpv4Address())) {
						ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.asipaddress"));
					}
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address.asprefixlength");
					if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv4Address())) {
						ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.asipaddress") + "/" + subnetMask);
					}
				}
				if(Objects.isNull(ethernetInterfaceBean.getIpv4Address())) {
					// Using values with Suffix '1'
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.asipaddress")) {
						if(Objects.isNull(ethernetInterfaceBean.getIpv4Address())) {
							ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asipaddress"));
						}
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asprefixlength");
						if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv4Address())) {
							ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.asipaddress") + "/" + subnetMask);
						}
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.asipaddress")) {
					ethernetInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asprefixlength");
					ethernetInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.asipaddress") + "/" + subnetMask);
				}
				// Setting IPv6 attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.asipaddress")) {
					if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())) {
						ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.asipaddress"));
					}
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.asprefixlength");
					if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
						ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.asipaddress") + "/" + subnetMask);
					}
				}
				if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())){
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.asipaddress")){
						if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())) {
							ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asipaddress"));
						}
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asprefixlength");
						if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
							ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.asipaddress") + "/" + subnetMask);
						}
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.asipaddress")){
					ethernetInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asprefixlength");
					ethernetInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.asipaddress")+ "/" + subnetMask);
				}
				if(Objects.isNull(ethernetInterfaceBean.getInnerVlan())){
					ethernetInterfaceBean.setInnerVlan(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asinnerencapvalue"));
				}
				if(inputValueMap.containsKey("pewan.pport.asdescription") && inputValueMap.get("pewan.pport.asdescription").toLowerCase().contains("gig")) {
					ethernetInterfaceBean.setPortType("GIGE");
					ethernetInterfaceBean.setMediaType(null);
				}

				if(inputValueMap.containsKey("pewan.lagip.site.laginterface.asmode")) {
					ethernetInterfaceBean.setMode(inputValueMap.get("pewan.lagip.site.laginterface.asmode").toUpperCase());
				}else {
					if(inputValueMap.containsKey("pewan.pport.asmode")) {
						ethernetInterfaceBean.setMode(inputValueMap.get("pewan.pport.asmode").toUpperCase());
					}
				}

				if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("ethernet")) {
					ethernetInterfaceBean.setMediaType("Electrical");
				}else if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("tdm")) {
					ethernetInterfaceBean.setMediaType("Electrical");
				}else if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("sonet")) {
					ethernetInterfaceBean.setMediaType("Optical");
				}else if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("sfp")) {
					ethernetInterfaceBean.setMediaType("Optical");
				}

				if(inputValueMap.containsKey("pewan.lagip.site.laginterface.asencaptype") &&
						inputValueMap.get("pewan.lagip.site.laginterface.asencaptype").toLowerCase().contains("8021q")) {
					ethernetInterfaceBean.setEncapsulation("DOT1Q");
				}else if(inputValueMap.containsKey("pewan.lagip.site.laginterface.asencaptype") && inputValueMap.get("pewan.lagip.site.laginterface.asencaptype").toLowerCase().startsWith("q")) {
					ethernetInterfaceBean.setEncapsulation("QnQ");
				}else{
					ethernetInterfaceBean.setEncapsulation("NULL");
				}

/*				if(ethernetInterfaceBean.getEncapsulation()==null) {
					if(inputValueMap.containsKey("pewan.pport.asencaptype") && inputValueMap.get("pewan.pport.asencaptype").toLowerCase().contains("8021q")) {
						ethernetInterfaceBean.setEncapsulation("DOT1Q");
						//ethernetInterfaceBean.setEncapsulation("RULE");
					}else if(inputValueMap.containsKey("pewan.pport.asencaptype") && inputValueMap.get("pewan.pport.asencaptype").toLowerCase().startsWith("q")) {
						ethernetInterfaceBean.setEncapsulation("QnQ");
					}
				}*/

				if(inputValueMap.containsKey("pewan.interface.vprninterface.ipinterface.asouterencapvalue") &&
						Integer.valueOf(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asouterencapvalue"))>=0) {
					ethernetInterfaceBean.setOuterVlan(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asouterencapvalue"));
				}
				if("DOT1Q".equalsIgnoreCase(ethernetInterfaceBean.getEncapsulation()) && Objects.isNull(ethernetInterfaceBean.getOuterVlan())) {
					ethernetInterfaceBean.setOuterVlan(inputValueMap.get("pewan.interface.vprninterface.ipinterface.asinnerencapvalue"));
				}
			}
			// CISCO & JUNIPER
			else {
				if(inputValueMap.containsKey("pewan.ethernetip.lport.rlmtu") && Integer.valueOf(inputValueMap.get("pewan.ethernetip.lport.rlmtu"))>0) {
					ethernetInterfaceBean.setMtu(inputValueMap.get("pewan.ethernetip.lport.rlmtu"));
				}
				if(inputValueMap.containsKey("pewan.ethernetip.subint.ipinterface.risubinterface") && Integer.valueOf(inputValueMap.get("pewan.ethernetip.subint.ipinterface.risubinterface"))>0) {
					ethernetInterfaceBean.setInterfaceName(inputValueMap.get("pewan.ethernetip.subint.ipinterface.sremsname"));
				}
				else {
					ethernetInterfaceBean.setInterfaceName(inputValueMap.get("pewan.ethernetip.ipinterface.sremsname"));
				}
				ethernetInterfaceBean.setPhysicalPort(inputValueMap.get("pewan.ethernetip.pport.sremsname"));
				// Setting IPv4 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.riipaddr")){
					if(Objects.isNull(ethernetInterfaceBean.getIpv4Address())) {
						ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddr"));
					}
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask");
					}
					if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv4Address())) {
						ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddr") + "/" + subnetMask);
					}
				}
				else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address.riipaddress")){
					if(Objects.isNull(ethernetInterfaceBean.getIpv4Address())) {
						ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddress"));
					}
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address.riaddrmask");
					}
					if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv4Address())) {
						ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address.riipaddress") + "/" + subnetMask);
					}
				}
				if(Objects.isNull(ethernetInterfaceBean.getIpv4Address())) {
					// Using values with Suffix '1'
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.riipaddr")) {
						ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddr"));
						String subnetMask = null;
						if(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask")==null ||
								inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask").equals("0")){
							try {
								subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipmask"))));
							} catch (UnknownHostException e) {
								logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
										inputValueMap.get("rfu1"), e.getMessage());
							}
						}
						else{
							subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask");
						}
						ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddr") + "/" + subnetMask);
					}
					else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address1.riipaddress")) {
						ethernetInterfaceBean.setIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddress"));
						String subnetMask = null;
						if(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask")==null ||
								inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask").equals("0")){
							try {
								subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipmask"))));
							} catch (UnknownHostException e) {
								logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
										inputValueMap.get("rfu1"), e.getMessage());
							}
						}
						else{
							subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riaddrmask");
						}
						ethernetInterfaceBean.setModifiedIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address1.riipaddress") + "/" + subnetMask);
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.riipaddr")) {
					ethernetInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddr"));
					String subnetMask = null;
					if(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask")==null ||
							inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask").equals("0")){
						try {
							subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipmask"))));
						} catch (UnknownHostException e) {
							logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
									inputValueMap.get("rfu1"), e.getMessage());
						}
					}
					else{
						subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask");
					}
					ethernetInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddr") + "/" + subnetMask);
				}
				else{
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv4address2.riipaddress")) {
						ethernetInterfaceBean.setSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddress"));
						String subnetMask = null;
						if(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask")==null ||
								inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask").equals("0")){
							try {
								subnetMask = String.valueOf(convertNetmaskToCIDR(InetAddress.getByName(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipmask"))));
							} catch (UnknownHostException e) {
								logger.error("UnknownHostException while populating Ethernet Interface for serviceCode {}. Error : {}",
										inputValueMap.get("rfu1"), e.getMessage());
							}
						}
						else{
							subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riaddrmask");
						}
						ethernetInterfaceBean.setModifiedSecondaryIpv4Address(inputValueMap.get("pewan.ipaddressing.local.ipv4address2.riipaddress") + "/" + subnetMask);
					}
				}
				// Setting IPv6 Attributes
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.riipaddr")) {
					if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())) {
						ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddr"));
					}
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.riprefixlen");
					if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
						ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddr") + "/" + subnetMask);
					}
				}
				else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address.riipaddress")){
					if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())) {
						ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddress"));
					}
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address.riprefixlen");
					if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
						ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address.riipaddress") + "/" + subnetMask);
					}
				}
				if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())){
					if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.riipaddr")){
						if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())) {
							ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddr"));
						}
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
						if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
							ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddr") + "/" + subnetMask);
						}
					}
					else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address1.riipaddress")){
						if(Objects.isNull(ethernetInterfaceBean.getIpv6Address())) {
							ethernetInterfaceBean.setIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddress"));
						}
						String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
						if(Objects.isNull(ethernetInterfaceBean.getModifiedIpv6Address())) {
							ethernetInterfaceBean.setModifiedIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riipaddress") + "/" + subnetMask);
						}
					}
				}
				if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.riipaddr")){
					ethernetInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddr"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
					ethernetInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddr")+ "/" + subnetMask);
				}
				else if(inputValueMap.containsKey("pewan.ipaddressing.local.ipv6address2.riipaddress")){
					ethernetInterfaceBean.setSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddress"));
					String subnetMask = inputValueMap.get("pewan.ipaddressing.local.ipv6address1.riprefixlen");
					ethernetInterfaceBean.setModifiedSecondaryIpv6Address(inputValueMap.get("pewan.ipaddressing.local.ipv6address2.riipaddress")+ "/" + subnetMask);
				}

				String innerVlanKey = "pewan.ethernetip.ipinterface.rivlanencapid";

				if(inputValueMap.containsKey(innerVlanKey) && Integer.valueOf(inputValueMap.get(innerVlanKey))>=0){
					ethernetInterfaceBean.setInnerVlan(inputValueMap.get(innerVlanKey));
				}
				else{
					innerVlanKey = "pewan.ethernetip.subint.ipinterface.rivlanencapid";
					if(inputValueMap.containsKey(innerVlanKey) && Integer.valueOf(inputValueMap.get(innerVlanKey))>=0) {
						ethernetInterfaceBean.setInnerVlan(inputValueMap.get(innerVlanKey));
					}
					else if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")){
						innerVlanKey = "pewan.ethernetip.subint.ipinterface.jrinnervlanid";
						if(inputValueMap.containsKey(innerVlanKey) && Integer.valueOf(inputValueMap.get(innerVlanKey))>=0) {
							ethernetInterfaceBean.setInnerVlan(inputValueMap.get(innerVlanKey));
						}
						else{
							innerVlanKey = "pewan.ethernetip.ipinterface.jrinnervlanid";
							if(inputValueMap.containsKey(innerVlanKey) && Integer.valueOf(inputValueMap.get(innerVlanKey))>=0){
								ethernetInterfaceBean.setInnerVlan(inputValueMap.get(innerVlanKey));
							}
						}
					}
				}

				if(inputValueMap.containsKey("pewan.ethernetip.pport.srporttype") && inputValueMap.get("pewan.ethernetip.pport.srporttype").toLowerCase().contains("gig")) {
					ethernetInterfaceBean.setPortType("GIGE");
				}else if(inputValueMap.containsKey("pewan.ethernetip.pport.srporttype") && inputValueMap.get("pewan.ethernetip.pport.srporttype").toLowerCase().contains("fast")) {
					ethernetInterfaceBean.setPortType("FE");
				}

				if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("CR")) {
					if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("ethernet")) {
						ethernetInterfaceBean.setMediaType("Electrical");
					}else if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("tdm")) {
						ethernetInterfaceBean.setMediaType("Electrical");
					}else if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("sonet")) {
						ethernetInterfaceBean.setMediaType("Optical");
					}else if(inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.containsKey("pewan.pport.asportcategory") && inputValueMap.get("pewan.pport.asportcategory").toLowerCase().contains("sfp")) {
						ethernetInterfaceBean.setMediaType("Optical");
					}
				}

				if(inputValueMap.containsKey("pewan.ethernetip.lport.rlencaptype") && inputValueMap.get("pewan.ethernetip.lport.rlencaptype").toLowerCase().contains("802.1q")) {
					ethernetInterfaceBean.setEncapsulation("DOT1Q");
				}else if(inputValueMap.containsKey("pewan.ethernetip.lport.rlencaptype") && inputValueMap.get("pewan.ethernetip.lport.rlencaptype").toLowerCase().startsWith("q")) {
					ethernetInterfaceBean.setEncapsulation("QnQ");
				}
				else{
					ethernetInterfaceBean.setEncapsulation("NULL");
				}

				if(inputValueMap.containsKey("pe_equipmodule") && inputValueMap.get("pe_equipmodule").equalsIgnoreCase("JR")) {
					if(inputValueMap.containsKey("pewan.ethernetip.pport.jrholddntime") &&
							Integer.valueOf(inputValueMap.get("pewan.ethernetip.pport.jrholddntime"))>0) {
						ethernetInterfaceBean.setHoldtimeDown(inputValueMap.get("pewan.ethernetip.pport.jrholddntime"));
					}
					if(inputValueMap.containsKey("pewan.ethernetip.pport.jrholduptime") &&
							Integer.valueOf(inputValueMap.get("pewan.ethernetip.pport.jrholduptime"))>0) {
						ethernetInterfaceBean.setHoldtimeUp(inputValueMap.get("pewan.ethernetip.pport.jrholduptime"));
					}
					if(inputValueMap.containsKey("pewan.ethernetip.pport.jrframing")) {
						ethernetInterfaceBean.setFraming(inputValueMap.get("pewan.ethernetip.pport.jrframing").toUpperCase());
					}else {
						ethernetInterfaceBean.setFraming("NOT_APPLICABLE");
					}
				}
			}

			ethernetInterfaceBean.setDuplex("NOT_APPLICABLE");
			ethernetInterfaceBean.setAutonegotiationEnabled("NOT_APPLICABLE");
		    ethernetInterfaceBean.setSpeed("NOT_APPLICABLE");
			ethernetInterfaceBeans.add(ethernetInterfaceBean);
		}
		
	}

	public static Integer convertNetmaskToCIDR(InetAddress netmask){
		byte[] netmaskBytes = netmask.getAddress();
		Integer cidr = 0;
		Boolean zero = false;
		for(byte b : netmaskBytes){
			int mask = 0x80;
			for(int i = 0; i < 8; i++){
				int result = b & mask;
				if(result == 0){
					zero = true;
				}else if(zero){
					logger.error("Invalid netmask provided");
				} else {
					cidr++;
				}
				mask >>>= 1;
			}
		}
		return cidr;
	}

	public void processFTIData(String serviceCode, Map<String, String> ftiAttributesMap,String isTermination, Integer scServiceDetailId) throws TclCommonException {
		if(!CollectionUtils.isEmpty(ftiAttributesMap)) {
			TaskBean taskBean = new TaskBean();
			logger.info("Processing FTI to refresh data for service Code {}", serviceCode);
			taskBean = processMapData(taskBean, ftiAttributesMap);
			ipDetailsService.persistFTIRefreshData(taskBean,isTermination,scServiceDetailId,ftiAttributesMap);
			logger.info("FTI Data Refresh completed for {}", serviceCode);
		}
	}

	@Transactional(readOnly = false)
	public void processMigrationFiles() {
		try {
			String arg = "D:\\Mayank\\Work\\Optimus\\NetP Files\\Files 5";
			List<String> serviceIds = new ArrayList<>();
			List<String> netPFiles = new ArrayList<>();
			processCMD(arg, netPFiles, serviceIds);
			Iterator it1 = netPFiles.iterator();
			Iterator it2 = serviceIds.iterator();
			while (it1.hasNext() && it2.hasNext()) {
				TaskBean taskBean = new TaskBean();
				try {
					String completeFilePath = (String) it1.next();
					String serviceId = (String) it2.next();
					Map<String, String> valueMap = new HashMap<>();
					if (completeFilePath.contains(serviceId)) {
						File file = new File(completeFilePath);
						Scanner input = new Scanner(file);
						input.useDelimiter(",|\\n");
						while (input.hasNext()) {
							String nextToken = input.next();
							String keyval[] = nextToken.trim().split("=");
							if (keyval.length > 1 && !valueMap.containsKey(keyval[0])){
								valueMap.put(keyval[0].trim(), keyval[1].replaceAll("\"",""));
							}
						}
						input.close();
						if(!CollectionUtils.isEmpty(valueMap)){
							taskBean = processMapData(taskBean, valueMap);
							ipDetailsService.persistFTIRefreshData(taskBean,null,null,null);
						}
						else{
							logger.info("No data from FTI File for service Code {}", serviceId);
						}
					}
				}
				catch (FileNotFoundException f) {
					logger.error("File Not Found {}", f.getMessage());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
