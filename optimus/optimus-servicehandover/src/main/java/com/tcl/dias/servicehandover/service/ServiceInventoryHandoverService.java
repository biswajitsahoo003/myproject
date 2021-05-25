package com.tcl.dias.servicehandover.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;

import java.util.stream.Collectors;

import com.tcl.dias.common.serviceinventory.bean.ScSolutionComponentBean;
import com.tcl.dias.common.serviceinventory.bean.SdwanScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceCosCriteriaBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import org.apache.commons.lang3.StringUtils;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.bean.ScServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceCosCriteriaBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.serviceinventory.bean.CpeBean;
import com.tcl.dias.common.serviceinventory.bean.EthernetInterfaceBean;
import com.tcl.dias.common.serviceinventory.bean.IpAddressDetailBean;
import com.tcl.dias.common.serviceinventory.bean.MuxDetailsBean;
import com.tcl.dias.common.serviceinventory.bean.RouterDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ScComponentAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScComponentBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceQoBean;
import com.tcl.dias.common.serviceinventory.bean.UniswitchDetailBean;
import com.tcl.dias.common.serviceinventory.bean.VrfBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.EthernetInterface;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.RouterUplinkport;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceFulfillmentJob;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceFulfillmentJobRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task details
 *
 */
@Service
@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
public class ServiceInventoryHandoverService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryHandoverService.class);

	@Autowired
	private ServiceInventoryMapper serviceInventoryMapper;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	private ScOrderRepository scOrderRepository;

	@Autowired
	private ScServiceCommercialRepository scServiceCommercialRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.service.handover.inventory}")
	String serviceHandoverInventory;

	@Value("${oms.o2c.macd.queue}")
	String omsO2CMacdQueue;

	@Value("${rabbitmq.service.handover.inventory.sdwan}")
	String sdwanInventoryHandoverQueue;

	@Autowired
	private ServiceDetailRepository serviceDetailRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	private ScComponentRepository scComponentRepository;

	@Autowired
	ServiceFulfillmentJobRepository serviceFulfillmentJobRepository;

	@Autowired
	private ScSolutionComponentRepository scSolutionComponentRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	private ScServiceAttributeRepository scServiceAttributeRepository;

	@Transactional
	public ScOrderBean handoverDetailsToServiceInventory(String orderCode, String serviceCode, Integer serviceId)
			throws TclCommonException {
		ScOrderBean scOrderBean = null;

		try {
			LOGGER.info("handoverDetailsToServiceInventory started with service code:{} and serviceId:{}", serviceCode,
					serviceId);

			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByScServiceDetailIdAndServiceStateOrderByVersionDesc(serviceId,
							TaskStatusConstants.ACTIVE);

			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();

			//String orderCode = scServiceDetail.getScOrderUuid();

			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");

			Map<String, String> scComponentsAttrMapA = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

			scOrderBean = serviceInventoryMapper.mapEntityToOrderBean(scOrder, scServiceDetail, scComponentsAttrMapA);
			ServiceDetailBean serviceDetailBean = serviceInventoryMapper.mapServiceDetailAttributes(serviceDetail, scServiceDetail);

			if(Objects.nonNull(serviceDetail)) {
				if (!CollectionUtils.isEmpty(serviceDetail.getVrfs())) {
					Vrf vrf = serviceDetail.getVrfs().stream().findFirst().get();
					VrfBean vrfBean = new VrfBean();
					vrfBean.setMasterVrfServiceId(vrf.getMastervrfServiceid());
					vrfBean.setMultiVrf(convertByteToBoolean(vrf.getIsmultivrf()));
					scOrderBean.setVrfBean(vrfBean);
				}

				if (!CollectionUtils.isEmpty(serviceDetail.getMulticastings())) {
					Multicasting multicasting = serviceDetail.getMulticastings().stream().findFirst().get();
					// Set Details only from Multicast Records which aren't end-dated
					if (Objects.isNull(multicasting.getEndDate())) {
						serviceDetailBean.setMulticastRPAddress(multicasting.getRpAddress());
						serviceDetailBean.setMulticastType(multicasting.getType());
						serviceDetailBean.setAutoDiscoveryOption(multicasting.getAutoDiscoveryOption());
						serviceDetailBean.setDataMdt(multicasting.getDataMdt());
						serviceDetailBean.setDataMdtThreshold(multicasting.getDataMdtThreshold());
						serviceDetailBean.setDefaultMdt(multicasting.getDefaultMdt());
						serviceDetailBean.setRpLocation(multicasting.getRpLocation());
						serviceDetailBean.setWanPimMode(multicasting.getWanPimMode());
					}
				}
			}

			List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailIdAndComponentNameAndSiteTypeIn(
					scServiceDetail.getId(), "LM", Arrays.asList("A", "B"));

			if (!CollectionUtils.isEmpty(scComponents)) {
				for (ScComponent scComponent : scComponents) {
					ScComponentBean scComponentBean = mapScComponentToBean(scComponent);
					List<ScComponentAttributeBean> scComponentAttributeBeans = new ArrayList<>();
					if (!CollectionUtils.isEmpty(scComponent.getScComponentAttributes())) {
						scComponent.getScComponentAttributes().forEach(scComponentAttribute -> {
							ScComponentAttributeBean scComponentAttributeBean = mapScComponentAttribute(
									scComponentAttribute);
							scComponentAttributeBeans.add(scComponentAttributeBean);
						});
						scComponentBean.setScComponentAttributeBeans(scComponentAttributeBeans);
					}
					scOrderBean.getScServiceDetails().stream().forEach(scServiceDetailBean -> {
						scServiceDetailBean.getScComponentBeans().add(scComponentBean);
					});
				}
			}

			scOrderBean.setMuxDetails(constructMuxDetails(scComponentsAttrMapA));
			if (scServiceDetail != null) {

				List<ScServiceCommercial> scServiceCommercials = scServiceCommercialRepository
						.findByScServiceId(scServiceDetail.getId());

				if (!scServiceCommercials.isEmpty()) {

					for (ScServiceCommercial scServiceCommercial : scServiceCommercials) {

						scOrderBean.getScCommercialBean()
								.add(serviceInventoryMapper.mapServiceCommercialEntityToBean(scServiceCommercial));

					}

				}

				if(Objects.nonNull(serviceDetail)) {
					IpAddressDetailBean ipAddressDetailBean = new IpAddressDetailBean();
					if (!CollectionUtils.isEmpty(serviceDetail.getIpAddressDetails())) {
						serviceDetail.getIpAddressDetails().forEach(ipAddressDetail -> {
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv4Addresses())) {
								String lanv4Address = ipAddressDetail.getIpaddrLanv4Addresses().stream()
										.map(ipaddrLanv4Address -> ipaddrLanv4Address.getLanv4Address())
										.collect(Collectors.joining(";"));
								String lanv4ProvidedBy = ipAddressDetail.getIpaddrLanv4Addresses().stream()
										.map(ipaddrLanv4Address -> String.valueOf(ipaddrLanv4Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setLanv4Address(lanv4Address);
								ipAddressDetailBean.setLanv4ProvidedBy(lanv4ProvidedBy);
							}
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv4Addresses())) {
								String wanv4Address = ipAddressDetail.getIpaddrWanv4Addresses().stream()
										.map(ipaddrWanv4Address -> ipaddrWanv4Address.getWanv4Address())
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setWanv4Address(wanv4Address);
								String wanv4ProvidedBy = ipAddressDetail.getIpaddrWanv4Addresses().stream()
										.map(ipaddrWanv4Address -> String.valueOf(ipaddrWanv4Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setWanv4Address(wanv4Address);
								ipAddressDetailBean.setWanv4ProvidedBy(wanv4ProvidedBy);
							}
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv6Addresses())) {
								String lanv6Address = ipAddressDetail.getIpaddrLanv6Addresses().stream()
										.map(ipaddrLanv6Address -> ipaddrLanv6Address.getLanv6Address())
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setLanv6Address(lanv6Address);
								String lanv6ProvidedBy = ipAddressDetail.getIpaddrLanv6Addresses().stream()
										.map(ipaddrLanv6Address -> String.valueOf(ipaddrLanv6Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setLanv6Address(lanv6Address);
								ipAddressDetailBean.setLanv6ProvidedBy(lanv6ProvidedBy);
							}
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv6Addresses())) {
								String wanv6Address = ipAddressDetail.getIpaddrWanv6Addresses().stream()
										.map(ipaddrWanv6Address -> ipaddrWanv6Address.getWanv6Address())
										.collect(Collectors.joining(";"));
								String wanv6ProvidedBy = ipAddressDetail.getIpaddrWanv4Addresses().stream()
										.map(ipaddrWanv4Address -> String.valueOf(ipaddrWanv4Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setWanv6Address(wanv6Address);
								ipAddressDetailBean.setWanv6ProvidedBy(wanv6ProvidedBy);
							}
						});
					}
					scOrderBean.setIpAddressDetailBean(ipAddressDetailBean);

					if (!CollectionUtils.isEmpty(serviceDetail.getInterfaceProtocolMappings())) {

						// Common Ethernet Interface Bean for both PE & CE
						EthernetInterfaceBean ethernetInterfaceBean = new EthernetInterfaceBean();

						for (InterfaceProtocolMapping interfaceProtocolMapping : serviceDetail
								.getInterfaceProtocolMappings()) {

							// PE Record
							if (interfaceProtocolMapping.getIscpeWanInterface() != null
									&& interfaceProtocolMapping.getIscpeLanInterface() != null && 
									interfaceProtocolMapping.getIscpeLanInterface() == 0
									&& interfaceProtocolMapping.getIscpeWanInterface() == 0
									) {
								RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
								RouterDetailBean routerDetailBean = new RouterDetailBean();
								if (routerDetail != null) {
									setRouterDetails(routerDetailBean, routerDetail);
								}
								scOrderBean.getRouterDetailBeans().add(routerDetailBean);

								EthernetInterface ethernetInterface = interfaceProtocolMapping.getEthernetInterface();
								ChannelizedSdhInterface channelizedSdhInterface = interfaceProtocolMapping
										.getChannelizedSdhInterface();
								ChannelizedE1serialInterface channelizedE1serialInterface = interfaceProtocolMapping
										.getChannelizedE1serialInterface();
								Bgp bgp = interfaceProtocolMapping.getBgp();
								if (bgp != null) {
									serviceDetailBean.setRemoteAsNumber(Objects.nonNull(bgp.getRemoteAsNumber())
											? String.valueOf(bgp.getRemoteAsNumber())
											: null);
								}

								if (Objects.nonNull(ethernetInterface)) {
									setEthernetInterfaceDetails(ethernetInterfaceBean, ethernetInterface);
								} else if (channelizedSdhInterface != null) {

									setChannelSdheDetails(ethernetInterfaceBean, channelizedSdhInterface);

								} else if (channelizedE1serialInterface != null) {
									setChannelSerialInterfaceDetails(ethernetInterfaceBean, channelizedE1serialInterface);

								}
							}
							// CPE
							if (interfaceProtocolMapping.getIscpeWanInterface() != null &&
									interfaceProtocolMapping.getIscpeWanInterface() == 1) {

								EthernetInterface ethernetInterface = interfaceProtocolMapping.getEthernetInterface();
								if (Objects.nonNull(ethernetInterface)) {
									setEthernetInterfaceDetailsForCE(ethernetInterfaceBean, ethernetInterface);
								}

								Cpe cpe = interfaceProtocolMapping.getCpe();
								CpeBean cpeBean = new CpeBean();
								if (cpe != null) {
									setCpe(cpeBean, cpe);
								}
								scOrderBean.getCpeBeans().add(cpeBean);
							}
						}
						scOrderBean.setEthernetInterfaceBean(ethernetInterfaceBean);
					}

					if (!CollectionUtils.isEmpty(serviceDetail.getTopologies())) {

						for (Topology topology : serviceDetail.getTopologies()) {

							if (!CollectionUtils.isEmpty(topology.getUniswitchDetails())) {

								for (UniswitchDetail uniswitchDetail : topology.getUniswitchDetails()) {

									UniswitchDetailBean uniswitchDetailBean = new UniswitchDetailBean();
									setUniswitchDetails(uniswitchDetailBean, uniswitchDetail);
									scOrderBean.getUniswitchDetailBeans().add(uniswitchDetailBean);

								}
							}
							if (!CollectionUtils.isEmpty(topology.getRouterUplinkports())) {
								RouterUplinkport routerUplinkport = topology.getRouterUplinkports().stream().findFirst()
										.orElse(null);
								if (routerUplinkport != null) {
									serviceDetailBean.setBusinessSwitchUplinkPort(routerUplinkport.getPhysicalPort1Name());
								}
							}

						}
					}

					if (!CollectionUtils.isEmpty(serviceDetail.getServiceQos())) {
						ServiceQo serviceQo = serviceDetail.getServiceQos().stream().findFirst().orElse(null);
						if (Objects.nonNull(serviceQo)) {
							ServiceQoBean serviceQoBean = new ServiceQoBean();
							setServiceQo(serviceQoBean, serviceQo);
							scOrderBean.setServiceQoBean(serviceQoBean);
						}
					}

					if (!CollectionUtils.isEmpty(serviceDetail.getLmComponents())) {
						LmComponent lmComponent = serviceDetail.getLmComponents().stream().findFirst().orElse(null);
						if (lmComponent != null && !CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())) {
							RadwinLastmile radwinLastmile = lmComponent.getRadwinLastmiles().stream().findFirst()
									.orElse(null);
							if (radwinLastmile != null) {
								scOrderBean.setRegion(radwinLastmile.getRegion());
							}
						}
					}

					if (!CollectionUtils.isEmpty(serviceDetail.getLmComponents())) {
						LmComponent lmComponent = serviceDetail.getLmComponents().stream().findFirst().orElse(null);
						if (lmComponent != null && !CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())) {
							RadwinLastmile radwinLastmile = lmComponent.getRadwinLastmiles().stream().findFirst()
									.orElse(null);
							if (radwinLastmile != null) {
								scOrderBean.setRegion(radwinLastmile.getRegion());
							}
						}
					}
				}
				scOrderBean.setServiceDetailBean(serviceDetailBean);
			}

			LOGGER.info("handoverDetailsToServiceInventory json to service inventory for service code:{} and json:{}",
					serviceCode, Utils.convertObjectToJson(scOrderBean));

			ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJob.setType("SERVICE HANDOVER");
			serviceFulfillmentJob.setServiceId(scServiceDetail.getId());
			serviceFulfillmentJob.setCreatedTime(new Timestamp(new Date().getTime()));
			serviceFulfillmentJob.setServiceCode(serviceCode);
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);

			mqUtils.send(serviceHandoverInventory, Utils.convertObjectToJson(scOrderBean));

			if (Objects.nonNull(serviceDetail) && Objects.nonNull(serviceDetail.getOrderDetail())
					&& Objects.nonNull(serviceDetail.getOrderDetail().getOrderType())
					&& !"NEW".equalsIgnoreCase(serviceDetail.getOrderDetail().getOrderType())) {
				LOGGER.info("Sending data to update MACD_DETAIL table for serviceid, ordercode,ordercategory {},{}",
						serviceCode, orderCode);
				String o2cDetail = orderCode;
				mqUtils.send(omsO2CMacdQueue, o2cDetail);
			}

		} catch (Exception e) {
			ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJob.setType("SERVICE HANDOVER");
			serviceFulfillmentJob.setServiceId(serviceId);
			serviceFulfillmentJob.setCreatedTime(new Timestamp(new Date().getTime()));
			serviceFulfillmentJob.setServiceCode(serviceCode);
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
			LOGGER.error("error in pushing data to service inventory for serviceid:{} and error:{}", serviceCode, e);
		}
		return scOrderBean;
	}
    private ScServiceAttributeBean mapScServiceAttributesEntityToBean(ScServiceAttribute scServiceAttribute){
		ScServiceAttributeBean scServiceAttributeBean =new ScServiceAttributeBean();
		scServiceAttributeBean.setAttributeValue(scServiceAttribute.getAttributeValue());
		scServiceAttributeBean.setAttributeAltValueLabel(scServiceAttribute.getAttributeAltValueLabel());
		scServiceAttributeBean.setAttributeName(scServiceAttribute.getAttributeName());
		return  scServiceAttributeBean;
	}
	@Transactional(readOnly = false)
	public Boolean migrateToServiceInventoryBulk(List<String> serviceCodes) {
		if(!CollectionUtils.isEmpty(serviceCodes)){
			serviceCodes.forEach(serviceCode -> {
				LOGGER.info("Service Code to be migrated to Inventory : {}", serviceCode);
				// Changing the pick from Service Details ACTIVE record to Sc Service Detail ACTIVE status
				ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, TaskStatusConstants.ACTIVE);
//				ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateAndEndDateIsNullOrderByVersionDesc(serviceCode, TaskStatusConstants.ACTIVE);
				if(Objects.nonNull(scServiceDetail)){
//					Integer scServiceDetailId = serviceDetail.getScServiceDetailId();
					Integer scServiceDetailId = scServiceDetail.getId();
					try {
						LOGGER.info("Pushing service Code {} with Sc Service Detail Id {} to inventory", serviceCode, scServiceDetailId);
						ScOrderBean scOrderBean = handoverDetailsToServiceInventory(scServiceDetail.getScOrderUuid(),serviceCode, scServiceDetailId);
					} catch (TclCommonException e) {
						LOGGER.error("Exception while pushing service Code {} to Inventory", serviceCode);
					}
				}
				else{
					LOGGER.info("No Active Instance found for service Code {} to push to Inventory", serviceCode);
				}
			});
		}
		return true;
	}

	@Transactional
	public Boolean handoverSdwanDetailsToServiceInventory(String serviceCode) throws TclCommonException {
		LOGGER.info("Inventory Handover initiated for Service Code {}", serviceCode);
		ScSolutionComponent solutionComponent = scSolutionComponentRepository.findByServiceCode(serviceCode);
		if(Objects.nonNull(solutionComponent)) {
			String solutionCode = solutionComponent.getSolutionCode();
			SdwanScOrderBean sdwanScOrderBean = new SdwanScOrderBean();
			sdwanScOrderBean.setSolutionCode(solutionCode);
			String orderCode = "";
			Map<String, String> cpeSiteReference = new HashMap<>();
			List<ScOrderBean> scOrderBeans = new ArrayList<>();
			LOGGER.info("Handover Sdwan Details to Inventory for solutionCode {}", solutionCode);
			List<ScSolutionComponent> scSolutionComponents = scSolutionComponentRepository.findAllBySolutionCodeAndIsActive(solutionCode, "Y");
			if(!CollectionUtils.isEmpty(scSolutionComponents)) {
				Optional<ScSolutionComponent> optionalScSolutionComponent = scSolutionComponents.stream().filter(scSolutionComponent -> scSolutionComponent.getOrderCode() != null).findFirst();
				if(optionalScSolutionComponent.isPresent()) {
					orderCode = optionalScSolutionComponent.get().getOrderCode();
					LOGGER.info("Parent Order Code {}", orderCode);
				}
				sdwanScOrderBean.setParentOrderCode(orderCode);
				List<ScSolutionComponentBean> scSolutionComponentBeans = new ArrayList<>();
				scSolutionComponents.forEach(scSolutionComponent -> {
					ScServiceDetail scServiceDetail = scSolutionComponent.getScServiceDetail1();
					if(scServiceDetail.getMstStatus().getCode().equals(TaskStatusConstants.ACTIVE)) {
						scSolutionComponentBeans.add(setScSolutionComponentValues(scSolutionComponent, cpeSiteReference));
					}
				});
				sdwanScOrderBean.setScSolutionComponentBeans(scSolutionComponentBeans);
				sdwanScOrderBean.setCpeSiteReference(cpeSiteReference);
				ScServiceDetail solutionScServiceDetail = scServiceDetailRepository.findFirstByUuidOrderByIdDesc(solutionCode);
				List<ScServiceDetail> scServiceDetails = scSolutionComponents.stream()
						.map(scSolutionComponent -> scSolutionComponent.getScServiceDetail1())
						.filter(scServiceDetail -> scServiceDetail.getMstStatus().getCode().equals(TaskStatusConstants.ACTIVE))
						.collect(Collectors.toList());
				if(Objects.nonNull(solutionScServiceDetail)) {
					scServiceDetails.add(solutionScServiceDetail);
				}
				processScServiceDetailsForInventory(solutionCode, scOrderBeans, scServiceDetails, orderCode);
				sdwanScOrderBean.setScOrderBeans(scOrderBeans);
				mqUtils.send(sdwanInventoryHandoverQueue, Utils.convertObjectToJson(sdwanScOrderBean));
			} else {
				LOGGER.error("No ScSolutionComponents exist for the solution code {}", solutionCode);
			}
		}
		return true;
	}

	private void processScServiceDetailsForInventory(String solutionCode, List<ScOrderBean> scOrderBeans,
													 List<ScServiceDetail> scServiceDetails, String orderCode) {
		scServiceDetails.forEach(scServiceDetail -> {
			try {
				LOGGER.info("Inside processScServiceDetailsForInventory for serviceCode {}", scServiceDetail.getUuid());
				scOrderBeans.add(populateScOrderBean(scServiceDetail, scServiceDetail.getUuid(), orderCode));
			} catch(TclCommonException e) {
				LOGGER.error("Error occured while pushing service Code {} under solution code {}. " +
						"Exception occurred : {}", scServiceDetail.getUuid(), solutionCode, e.getMessage());
			}
		});
	}

	private ScOrderBean populateScOrderBean(ScServiceDetail scServiceDetail, String serviceCode, String orderCode) throws TclCommonException {
		LOGGER.info("Inside populateScOrderBean for serviceCode {}", serviceCode);
		ScOrderBean scOrderBean = new ScOrderBean();
		if(Objects.nonNull(scServiceDetail)){
			Integer scServiceDetailId = scServiceDetail.getId();
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByScServiceDetailIdAndServiceStateOrderByVersionDesc(scServiceDetailId,
					TaskStatusConstants.ACTIVE);
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");

			Map<String, String> scComponentsAttrMapA = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

			scOrderBean = serviceInventoryMapper.mapEntityToOrderBean(scOrder, scServiceDetail, scComponentsAttrMapA);
			ServiceDetailBean serviceDetailBean = serviceInventoryMapper.mapServiceDetailAttributes(serviceDetail, scServiceDetail);

			if(Objects.nonNull(serviceDetail)) {
				if (!CollectionUtils.isEmpty(serviceDetail.getVrfs())) {
					Vrf vrf = serviceDetail.getVrfs().stream().findFirst().get();
					VrfBean vrfBean = new VrfBean();
					vrfBean.setMasterVrfServiceId(vrf.getMastervrfServiceid());
					vrfBean.setMultiVrf(convertByteToBoolean(vrf.getIsmultivrf()));
					scOrderBean.setVrfBean(vrfBean);
				}
				if (!CollectionUtils.isEmpty(serviceDetail.getMulticastings())) {
					Multicasting multicasting = serviceDetail.getMulticastings().stream().findFirst().get();
					// Set Details only from Multicast Records which aren't end-dated
					if (Objects.isNull(multicasting.getEndDate())) {
						serviceDetailBean.setMulticastRPAddress(multicasting.getRpAddress());
						serviceDetailBean.setMulticastType(multicasting.getType());
						serviceDetailBean.setAutoDiscoveryOption(multicasting.getAutoDiscoveryOption());
						serviceDetailBean.setDataMdt(multicasting.getDataMdt());
						serviceDetailBean.setDataMdtThreshold(multicasting.getDataMdtThreshold());
						serviceDetailBean.setDefaultMdt(multicasting.getDefaultMdt());
						serviceDetailBean.setRpLocation(multicasting.getRpLocation());
						serviceDetailBean.setWanPimMode(multicasting.getWanPimMode());
					}
				}
			}

			List<ScComponent> scComponents = null;
			if(scServiceDetail.getErfPrdCatalogProductName()!=null &&
					(CommonConstants.MICROSOFT_CLOUD_SOLUTIONS.equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())
							|| "TEAMSDR_SOLUTION".equals(scServiceDetail.getErfPrdCatalogProductName()))){
				LOGGER.info("Inside process of scComponent mapping for teamsRd and MS_CloudSolution : {}",scServiceDetail.getUuid());
				scComponents=scComponentRepository.findByScServiceDetailId(scServiceDetail.getId());
			}else {
				scComponents=scComponentRepository.findByScServiceDetailIdAndComponentNameAndSiteTypeIn(
						scServiceDetail.getId(), "LM", Arrays.asList("A", "B"));
			}

			if (!CollectionUtils.isEmpty(scComponents)) {
				for (ScComponent scComponent : scComponents) {
					ScComponentBean scComponentBean = mapScComponentToBean(scComponent);
					List<ScComponentAttributeBean> scComponentAttributeBeans = new ArrayList<>();
					if (!CollectionUtils.isEmpty(scComponent.getScComponentAttributes())) {
						scComponent.getScComponentAttributes().forEach(scComponentAttribute -> {
							ScComponentAttributeBean scComponentAttributeBean = mapScComponentAttribute(scComponentAttribute);
							scComponentAttributeBeans.add(scComponentAttributeBean);
						});
						scComponentBean.setScComponentAttributeBeans(scComponentAttributeBeans);
					}
					scOrderBean.getScServiceDetails().stream().forEach(scServiceDetailBean -> {
						scServiceDetailBean.getScComponentBeans().add(scComponentBean);
					});
				}
			}
			scOrderBean.setMuxDetails(constructMuxDetails(scComponentsAttrMapA));
			if (scServiceDetail != null) {
				List<ScServiceCommercial> scServiceCommercials = scServiceCommercialRepository
						.findByScServiceId(scServiceDetail.getId());
				if (!scServiceCommercials.isEmpty()) {
					for (ScServiceCommercial scServiceCommercial : scServiceCommercials) {
						scOrderBean.getScCommercialBean()
								.add(serviceInventoryMapper.mapServiceCommercialEntityToBean(scServiceCommercial));
					}
				}
				if(Objects.nonNull(serviceDetail)) {
					IpAddressDetailBean ipAddressDetailBean = new IpAddressDetailBean();
					if (!CollectionUtils.isEmpty(serviceDetail.getIpAddressDetails())) {
						serviceDetail.getIpAddressDetails().forEach(ipAddressDetail -> {
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv4Addresses())) {
								String lanv4Address = ipAddressDetail.getIpaddrLanv4Addresses().stream()
										.map(ipaddrLanv4Address -> ipaddrLanv4Address.getLanv4Address())
										.collect(Collectors.joining(";"));
								String lanv4ProvidedBy = ipAddressDetail.getIpaddrLanv4Addresses().stream()
										.map(ipaddrLanv4Address -> String.valueOf(ipaddrLanv4Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setLanv4Address(lanv4Address);
								ipAddressDetailBean.setLanv4ProvidedBy(lanv4ProvidedBy);
							}
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv4Addresses())) {
								String wanv4Address = ipAddressDetail.getIpaddrWanv4Addresses().stream()
										.map(ipaddrWanv4Address -> ipaddrWanv4Address.getWanv4Address())
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setWanv4Address(wanv4Address);
								String wanv4ProvidedBy = ipAddressDetail.getIpaddrWanv4Addresses().stream()
										.map(ipaddrWanv4Address -> String.valueOf(ipaddrWanv4Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setWanv4Address(wanv4Address);
								ipAddressDetailBean.setWanv4ProvidedBy(wanv4ProvidedBy);
							}
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv6Addresses())) {
								String lanv6Address = ipAddressDetail.getIpaddrLanv6Addresses().stream()
										.map(ipaddrLanv6Address -> ipaddrLanv6Address.getLanv6Address())
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setLanv6Address(lanv6Address);
								String lanv6ProvidedBy = ipAddressDetail.getIpaddrLanv6Addresses().stream()
										.map(ipaddrLanv6Address -> String.valueOf(ipaddrLanv6Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setLanv6Address(lanv6Address);
								ipAddressDetailBean.setLanv6ProvidedBy(lanv6ProvidedBy);
							}
							if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv6Addresses())) {
								String wanv6Address = ipAddressDetail.getIpaddrWanv6Addresses().stream()
										.map(ipaddrWanv6Address -> ipaddrWanv6Address.getWanv6Address())
										.collect(Collectors.joining(";"));
								String wanv6ProvidedBy = ipAddressDetail.getIpaddrWanv4Addresses().stream()
										.map(ipaddrWanv4Address -> String.valueOf(ipaddrWanv4Address.getIscustomerprovided()))
										.collect(Collectors.joining(";"));
								ipAddressDetailBean.setWanv6Address(wanv6Address);
								ipAddressDetailBean.setWanv6ProvidedBy(wanv6ProvidedBy);
							}
						});
					}
					scOrderBean.setIpAddressDetailBean(ipAddressDetailBean);

					if (!CollectionUtils.isEmpty(serviceDetail.getInterfaceProtocolMappings())) {

						// Common Ethernet Interface Bean for both PE & CE
						EthernetInterfaceBean ethernetInterfaceBean = new EthernetInterfaceBean();

						for (InterfaceProtocolMapping interfaceProtocolMapping : serviceDetail
								.getInterfaceProtocolMappings()) {

							// PE Record
							if (interfaceProtocolMapping.getIscpeWanInterface() != null
									&& interfaceProtocolMapping.getIscpeLanInterface() != null
									&& interfaceProtocolMapping.getIscpeLanInterface() == 0
									&& interfaceProtocolMapping.getIscpeWanInterface() == 0
									) {
								RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
								RouterDetailBean routerDetailBean = new RouterDetailBean();
								if (routerDetail != null) {
									setRouterDetails(routerDetailBean, routerDetail);
								}
								scOrderBean.getRouterDetailBeans().add(routerDetailBean);

								EthernetInterface ethernetInterface = interfaceProtocolMapping.getEthernetInterface();
								ChannelizedSdhInterface channelizedSdhInterface = interfaceProtocolMapping
										.getChannelizedSdhInterface();
								ChannelizedE1serialInterface channelizedE1serialInterface = interfaceProtocolMapping
										.getChannelizedE1serialInterface();
								Bgp bgp = interfaceProtocolMapping.getBgp();
								if (bgp != null) {
									serviceDetailBean.setRemoteAsNumber(Objects.nonNull(bgp.getRemoteAsNumber())
											? String.valueOf(bgp.getRemoteAsNumber())
											: null);
								}

								if (Objects.nonNull(ethernetInterface)) {
									setEthernetInterfaceDetails(ethernetInterfaceBean, ethernetInterface);
								} else if (channelizedSdhInterface != null) {

									setChannelSdheDetails(ethernetInterfaceBean, channelizedSdhInterface);

								} else if (channelizedE1serialInterface != null) {
									setChannelSerialInterfaceDetails(ethernetInterfaceBean, channelizedE1serialInterface);

								}
							}
							// CPE
							if (interfaceProtocolMapping.getIscpeWanInterface() != null
									&& interfaceProtocolMapping.getIscpeWanInterface() == 1) {

								EthernetInterface ethernetInterface = interfaceProtocolMapping.getEthernetInterface();
								if (Objects.nonNull(ethernetInterface)) {
									setEthernetInterfaceDetailsForCE(ethernetInterfaceBean, ethernetInterface);
								}

								Cpe cpe = interfaceProtocolMapping.getCpe();
								CpeBean cpeBean = new CpeBean();
								if (cpe != null) {
									setCpe(cpeBean, cpe);
								}
								scOrderBean.getCpeBeans().add(cpeBean);
							}
						}
						scOrderBean.setEthernetInterfaceBean(ethernetInterfaceBean);
					}

					if (!CollectionUtils.isEmpty(serviceDetail.getTopologies())) {
						for (Topology topology : serviceDetail.getTopologies()) {
							if (!CollectionUtils.isEmpty(topology.getUniswitchDetails())) {
								for (UniswitchDetail uniswitchDetail : topology.getUniswitchDetails()) {
									UniswitchDetailBean uniswitchDetailBean = new UniswitchDetailBean();
									setUniswitchDetails(uniswitchDetailBean, uniswitchDetail);
									scOrderBean.getUniswitchDetailBeans().add(uniswitchDetailBean);

								}
							}
							if (!CollectionUtils.isEmpty(topology.getRouterUplinkports())) {
								RouterUplinkport routerUplinkport = topology.getRouterUplinkports().stream().findFirst()
										.orElse(null);
								if (routerUplinkport != null) {
									serviceDetailBean.setBusinessSwitchUplinkPort(routerUplinkport.getPhysicalPort1Name());
								}
							}
						}
					}
					if (!CollectionUtils.isEmpty(serviceDetail.getServiceQos())) {
						ServiceQo serviceQo = serviceDetail.getServiceQos().stream().findFirst().orElse(null);
						if (Objects.nonNull(serviceQo)) {
							ServiceQoBean serviceQoBean = new ServiceQoBean();
							setServiceQo(serviceQoBean, serviceQo);
							scOrderBean.setServiceQoBean(serviceQoBean);
						}
					}
					if (!CollectionUtils.isEmpty(serviceDetail.getLmComponents())) {
						LmComponent lmComponent = serviceDetail.getLmComponents().stream().findFirst().orElse(null);
						if (lmComponent != null && !CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())) {
							RadwinLastmile radwinLastmile = lmComponent.getRadwinLastmiles().stream().findFirst()
									.orElse(null);
							if (radwinLastmile != null) {
								scOrderBean.setRegion(radwinLastmile.getRegion());
							}
						}
					}
					if (!CollectionUtils.isEmpty(serviceDetail.getLmComponents())) {
						LmComponent lmComponent = serviceDetail.getLmComponents().stream().findFirst().orElse(null);
						if (lmComponent != null && !CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())) {
							RadwinLastmile radwinLastmile = lmComponent.getRadwinLastmiles().stream().findFirst()
									.orElse(null);
							if (radwinLastmile != null) {
								scOrderBean.setRegion(radwinLastmile.getRegion());
							}
						}
					}
				}
				scOrderBean.setServiceDetailBean(serviceDetailBean);
			}

			LOGGER.info("handoverDetailsToServiceInventory json to service inventory for service code:{} and json:{}",
					serviceCode, Utils.convertObjectToJson(scOrderBean));
		}
		else{
			LOGGER.info("No Active Instance found for service Code {} to push to Inventory", serviceCode);
		}
		LOGGER.info("Exiting populateScOrderBean method");
		return scOrderBean;
	}

	private ScSolutionComponentBean setScSolutionComponentValues(ScSolutionComponent scSolutionComponent, Map<String, String> cpeSiteReference) {
		ScSolutionComponentBean scSolutionComponentBean = new ScSolutionComponentBean();
		scSolutionComponentBean.setComponentGroup(scSolutionComponent.getComponentGroup());
		scSolutionComponentBean.setIsActive(scSolutionComponent.getIsActive());
		scSolutionComponentBean.setO2cTriggeredStatus(scSolutionComponent.getO2cTriggeredStatus());
		scSolutionComponentBean.setOrderCode(scSolutionComponent.getOrderCode());
		scSolutionComponentBean.setParentServiceCode(scSolutionComponent.getParentServiceCode());
		scSolutionComponentBean.setServiceCode(scSolutionComponent.getServiceCode());
		scSolutionComponentBean.setSolutionCode(scSolutionComponent.getSolutionCode());
		scSolutionComponentBean.setPriority(scSolutionComponent.getPriority());
		// Setting the CPE Underlay Reference
		List<ScSolutionComponent> parentScSolutionComponents = scSolutionComponentRepository
				.findByParentServiceCode(scSolutionComponent.getServiceCode());
		if(!CollectionUtils.isEmpty(parentScSolutionComponents)){
			parentScSolutionComponents.forEach(parentComponent -> {
				Optional<ScComponent> oScComponent = scComponentRepository.findById(parentComponent.getCpeComponentId());
				if(oScComponent.isPresent()){
					ScComponent scComponent = oScComponent.get();
					cpeSiteReference.put(parentComponent.getServiceCode(), scComponent.getSiteType());
				}
			});
		}
		return scSolutionComponentBean;
	}

	private MuxDetailsBean constructMuxDetails(Map<String, String> muxValues) {
		
		if (muxValues == null) {
			return null;
		}
		
		MuxDetailsBean muxDetailsBean = new MuxDetailsBean();
		muxDetailsBean.setEndMuxNodeIp(muxValues.get("endMuxNodeIp"));
		muxDetailsBean.setEndMuxNodePort(muxValues.get("endMuxNodePort"));
		muxDetailsBean.setMuxMake(muxValues.get("muxMake"));
		return muxDetailsBean;
	}

	private Boolean convertByteToBoolean(Byte value) {
		return value != null && value != 0;
	}

	private ScComponentAttributeBean mapScComponentAttribute(ScComponentAttribute scComponentAttribute){
		ScComponentAttributeBean scComponentAttributeBean = new ScComponentAttributeBean();
		if(scComponentAttribute.getAttributeName().equalsIgnoreCase("CPE Basic Chassis")
				&& Objects.nonNull(scComponentAttribute.getAttributeValue())){
			Optional<ScAdditionalServiceParam> oScAdditionalServiceParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(StringUtils.trimToEmpty(scComponentAttribute.getAttributeValue())));
			if(oScAdditionalServiceParam.isPresent()){
				ScAdditionalServiceParam cpeChassis = oScAdditionalServiceParam.get();
				scComponentAttributeBean.setAttributeValue(cpeChassis.getValue());
				scComponentAttributeBean.setAttributeAltValueLabel(scComponentAttribute.getAttributeValue());
			}
		}else {
			scComponentAttributeBean.setAttributeAltValueLabel(scComponentAttribute.getAttributeAltValueLabel());
			scComponentAttributeBean.setAttributeValue(scComponentAttribute.getAttributeValue());
		}
		scComponentAttributeBean.setAttributeName(scComponentAttribute.getAttributeName());
		scComponentAttributeBean.setCreatedBy(scComponentAttribute.getCreatedBy());
		scComponentAttributeBean.setCreatedDate(scComponentAttribute.getCreatedDate());
		scComponentAttributeBean.setIsActive(scComponentAttribute.getIsActive());
		scComponentAttributeBean.setUpdatedBy(scComponentAttribute.getUpdatedBy());
		scComponentAttributeBean.setUpdatedDate(scComponentAttribute.getUpdatedDate());
		scComponentAttributeBean.setUuid(scComponentAttribute.getUuid());
		return scComponentAttributeBean;
	}

	private ScComponentBean mapScComponentToBean(ScComponent scComponent){
		ScComponentBean scComponentBean = new ScComponentBean();
		scComponentBean.setComponentName(scComponent.getComponentName());
		scComponentBean.setCreatedBy(scComponent.getCreatedBy());
		scComponentBean.setCreatedDate(scComponent.getCreatedDate());
		scComponentBean.setIsActive(scComponent.getIsActive());
		scComponentBean.setSiteType(scComponent.getSiteType());
		scComponentBean.setUpdatedBy(scComponent.getUpdatedBy());
		scComponentBean.setUpdatedDate(scComponent.getUpdatedDate());
		scComponentBean.setUuid(scComponent.getUuid());
		return scComponentBean;
	}

	private void setRouterDetails(RouterDetailBean routerDetailBean, RouterDetail routerDetail) {
		routerDetailBean.setEndDate(routerDetail.getEndDate());
		routerDetailBean.setIpv4MgmtAddress(routerDetail.getIpv4MgmtAddress());
		routerDetailBean.setIpv6MgmtAddress(routerDetail.getIpv6MgmtAddress());
		routerDetailBean.setLastModifiedDate(routerDetail.getLastModifiedDate());
		routerDetailBean.setModifiedBy(routerDetail.getModifiedBy());
		routerDetailBean.setRouterHostname(routerDetail.getRouterHostname());
		routerDetailBean.setRouterId(routerDetail.getRouterId());
		routerDetailBean.setRouterMake(routerDetail.getRouterMake());
		routerDetailBean.setRouterModel(routerDetail.getRouterModel());
		routerDetailBean.setRouterRole(routerDetail.getRouterRole());
		routerDetailBean.setRouterType(routerDetail.getRouterType());
		routerDetailBean.setStartDate(routerDetail.getStartDate());
	}

	private void setCpe(CpeBean cpeBean, Cpe cpe) {
		cpeBean.setCpeId(cpe.getCpeId());
		cpeBean.setCpeinitConfigparams(convertByteToBoolean(cpe.getCpeinitConfigparams()));
		cpeBean.setEndDate(cpe.getEndDate());
		cpeBean.setHostName(cpe.getHostName());
		cpeBean.setInitLoginpwd(cpe.getInitLoginpwd());
		cpeBean.setInitUsername(cpe.getInitUsername());
		cpeBean.setIsaceconfigurable(convertByteToBoolean(cpe.getIsaceconfigurable()));
		cpeBean.setModel(cpe.getModel());
		cpeBean.setLastModifiedDate(cpe.getLastModifiedDate());
		cpeBean.setLoopbackInterfaceName(cpe.getLoopbackInterfaceName());
		cpeBean.setMgmtLoopbackV4address(cpe.getMgmtLoopbackV4address());
		cpeBean.setMgmtLoopbackV6address(cpe.getMgmtLoopbackV6address());
		cpeBean.setMgmtLoopbackV6address(cpe.getMgmtLoopbackV6address());
		cpeBean.setModifiedBy(cpe.getModifiedBy());
		cpeBean.setNniCpeConfig(convertByteToBoolean(cpe.getNniCpeConfig()));
		cpeBean.setSendInittemplate(convertByteToBoolean(cpe.getSendInittemplate()));
		cpeBean.setServiceId(cpe.getServiceId());
		cpeBean.setSnmpServerCommunity(cpe.getSnmpServerCommunity());
		cpeBean.setStartDate(cpe.getStartDate());
		cpeBean.setUnmanagedCePartnerdeviceWanip(cpe.getUnmanagedCePartnerdeviceWanip());
		cpeBean.setVsatCpeConfig(convertByteToBoolean(cpe.getVsatCpeConfig()));
		cpeBean.setDeviceId(cpe.getDeviceId());
		cpeBean.setMake(cpe.getMake());
	}

	private void setUniswitchDetails(UniswitchDetailBean uniswitchDetailBean, UniswitchDetail uniswitchDetail) {
		uniswitchDetailBean.setAutonegotiationEnabled(uniswitchDetail.getAutonegotiationEnabled());
		uniswitchDetailBean.setDuplex(uniswitchDetail.getDuplex());
		uniswitchDetailBean.setEndDate(uniswitchDetail.getEndDate());
		uniswitchDetailBean.setHandoff(uniswitchDetail.getHandoff());
		uniswitchDetailBean.setInnerVlan(uniswitchDetail.getInnerVlan());
		uniswitchDetailBean.setHostName(uniswitchDetail.getHostName());
		uniswitchDetailBean.setInterfaceName(uniswitchDetail.getInterfaceName());
		uniswitchDetailBean.setLastModifiedDate(uniswitchDetail.getLastModifiedDate());
		uniswitchDetailBean.setMaxMacLimit(uniswitchDetail.getMaxMacLimit());
		uniswitchDetailBean.setMediaType(uniswitchDetail.getMediaType());
		uniswitchDetailBean.setMgmtIp(uniswitchDetail.getMgmtIp());
		uniswitchDetailBean.setMode(uniswitchDetail.getMode());
		uniswitchDetailBean.setModifiedBy(uniswitchDetail.getModifiedBy());
		uniswitchDetailBean.setOuterVlan(uniswitchDetail.getOuterVlan());
		uniswitchDetailBean.setPhysicalPort(uniswitchDetail.getPhysicalPort());
		uniswitchDetailBean.setPortType(uniswitchDetail.getPortType());
		uniswitchDetailBean.setSpeed(uniswitchDetail.getSpeed());
		uniswitchDetailBean.setStartDate(uniswitchDetail.getStartDate());
		uniswitchDetailBean.setSwitchModel(uniswitchDetail.getSwitchModel());
		uniswitchDetailBean.setSyncVlanReqd(convertByteToBoolean(uniswitchDetail.getSyncVlanReqd()));
		uniswitchDetailBean.setUniswitchId(uniswitchDetail.getUniswitchId());
	}

	private void setServiceQo(ServiceQoBean serviceQoBean, ServiceQo serviceQo){
		serviceQoBean.setCosPackage(serviceQo.getCosPackage());
		serviceQoBean.setCosProfile(serviceQo.getCosProfile());
		serviceQoBean.setCosType(serviceQo.getCosType());
		serviceQoBean.setEndDate(serviceQo.getEndDate());
		serviceQoBean.setFlexiCosIdentifier(serviceQo.getFlexiCosIdentifier());
		serviceQoBean.setIsbandwidthApplicable(convertByteToBoolean(serviceQo.getIsbandwidthApplicable()));
		serviceQoBean.setIsdefaultFc(convertByteToBoolean(serviceQo.getIsdefaultFc()));
		serviceQoBean.setIsflexicos(convertByteToBoolean(serviceQo.getIsflexicos()));
		serviceQoBean.setLastModifiedDate(serviceQo.getLastModifiedDate());
		serviceQoBean.setModifiedBy(serviceQo.getModifiedBy());
		serviceQoBean.setNcTraffic(convertByteToBoolean(serviceQo.getNcTraffic()));
		serviceQoBean.setPirBw(serviceQo.getPirBw());
		serviceQoBean.setPirBwUnit(serviceQo.getPirBwUnit());
		serviceQoBean.setQosTrafiicMode(serviceQo.getQosTrafiicMode());
		serviceQoBean.setSummationOfBw(serviceQo.getSummationOfBw());

		if(!CollectionUtils.isEmpty(serviceQo.getServiceCosCriterias())){
			List<ServiceCosCriteriaBean> serviceCosCriteriaBeans = new ArrayList<>();
			serviceQo.getServiceCosCriterias().forEach(serviceCosCriteria -> {
				ServiceCosCriteriaBean serviceCosCriteriaBean = new ServiceCosCriteriaBean();
				serviceCosCriteriaBean.setBwBpsunit(serviceCosCriteria.getBwBpsunit());
				serviceCosCriteriaBean.setClassificationCriteria(serviceCosCriteria.getClassificationCriteria());
				serviceCosCriteriaBean.setCosName(serviceCosCriteria.getCosName());
				serviceCosCriteriaBean.setCosPercent(serviceCosCriteria.getCosPercent());
				serviceCosCriteriaBean.setDhcpVal1(serviceCosCriteria.getDhcpVal1());
				serviceCosCriteriaBean.setDhcpVal2(serviceCosCriteria.getDhcpVal2());
				serviceCosCriteriaBean.setDhcpVal3(serviceCosCriteria.getDhcpVal3());
				serviceCosCriteriaBean.setDhcpVal4(serviceCosCriteria.getDhcpVal4());
				serviceCosCriteriaBean.setDhcpVal5(serviceCosCriteria.getDhcpVal5());
				serviceCosCriteriaBean.setDhcpVal6(serviceCosCriteria.getDhcpVal6());
				serviceCosCriteriaBean.setDhcpVal7(serviceCosCriteria.getDhcpVal7());
				serviceCosCriteriaBean.setDhcpVal8(serviceCosCriteria.getDhcpVal8());
				serviceCosCriteriaBean.setIpprecedenceVal1(serviceCosCriteria.getIpprecedenceVal1());
				serviceCosCriteriaBean.setIpprecedenceVal2(serviceCosCriteria.getIpprecedenceVal2());
				serviceCosCriteriaBean.setIpprecedenceVal3(serviceCosCriteria.getIpprecedenceVal3());
				serviceCosCriteriaBean.setIpprecedenceVal4(serviceCosCriteria.getIpprecedenceVal4());
				serviceCosCriteriaBean.setIpprecedenceVal5(serviceCosCriteria.getIpprecedenceVal5());
				serviceCosCriteriaBean.setIpprecedenceVal6(serviceCosCriteria.getIpprecedenceVal6());
				serviceCosCriteriaBean.setIpprecedenceVal7(serviceCosCriteria.getIpprecedenceVal7());
				serviceCosCriteriaBean.setIpprecedenceVal8(serviceCosCriteria.getIpprecedenceVal8());
				// Acl Policies to be added
				serviceCosCriteriaBeans.add(serviceCosCriteriaBean);
			});
			serviceQoBean.setServiceCosCriteriaBeans(serviceCosCriteriaBeans);
		}
	}

	private void setEthernetInterfaceDetailsForCE(EthernetInterfaceBean ethernetInterfaceBean, EthernetInterface ethernetInterface){
		LOGGER.info("Populating CE Side Records from Ethernet Interface {}", ethernetInterface.getEthernetInterfaceId());
		ethernetInterfaceBean.setCeModifiedIpv4Address(ethernetInterface.getModifiedIpv4Address());
		ethernetInterfaceBean.setCeModifiedIpv6Address(ethernetInterface.getModifiedIpv6Address());
		ethernetInterfaceBean.setCeModifiedSecondaryIpv4Address(ethernetInterface.getModifiedSecondaryIpv4Address());
		ethernetInterfaceBean.setCeModifiedSecondaryIpv6Address(ethernetInterface.getModifiedSecondaryIpv6Address());
	}

	private void setEthernetInterfaceDetails(EthernetInterfaceBean ethernetInterfaceBean, EthernetInterface ethernetInterface){
		LOGGER.info("Populating PE Side Records from Ethernet Interface {}", ethernetInterface.getEthernetInterfaceId());
		ethernetInterfaceBean.setInnerVlan(ethernetInterface.getInnerVlan());
		ethernetInterfaceBean.setInterfaceName(ethernetInterface.getInterfaceName());
		ethernetInterfaceBean.setPhysicalPort(ethernetInterface.getPhysicalPort());
		ethernetInterfaceBean.setModifiedIpv4Address(ethernetInterface.getModifiedIpv4Address());
		ethernetInterfaceBean.setOuterVlan(ethernetInterface.getOuterVlan());
		ethernetInterfaceBean.setModifiedIpv6Address(ethernetInterface.getModifiedIpv6Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(ethernetInterface.getModifiedSecondaryIpv4Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(ethernetInterface.getModifiedSecondaryIpv4Address());
	}
	
	private void setChannelSerialInterfaceDetails(EthernetInterfaceBean ethernetInterfaceBean,ChannelizedE1serialInterface channelizedE1serialInterface){
		ethernetInterfaceBean.setInnerVlan(null);
		ethernetInterfaceBean.setInterfaceName(channelizedE1serialInterface.getInterfaceName());
		ethernetInterfaceBean.setPhysicalPort(channelizedE1serialInterface.getPhysicalPort());
		ethernetInterfaceBean.setModifiedIpv4Address(channelizedE1serialInterface.getModifiedIpv4Address());
		ethernetInterfaceBean.setModifiedIpv6Address(channelizedE1serialInterface.getModifiedIpv6Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(channelizedE1serialInterface.getModifiedSecondaryIpv4Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(channelizedE1serialInterface.getModifiedSecondaryIpv4Address());
		ethernetInterfaceBean.setOuterVlan(null);
	}
	
	private void setChannelSdheDetails(EthernetInterfaceBean ethernetInterfaceBean,ChannelizedSdhInterface channelizedSdhInterface){
		ethernetInterfaceBean.setInnerVlan(null);
		ethernetInterfaceBean.setInterfaceName(channelizedSdhInterface.getInterfaceName());
		ethernetInterfaceBean.setPhysicalPort(channelizedSdhInterface.getPhysicalPort());
		ethernetInterfaceBean.setModifiedIpv4Address(channelizedSdhInterface.getModifiedIpv4Address());
		ethernetInterfaceBean.setOuterVlan(null);
		ethernetInterfaceBean.setModifiedIpv6Address(channelizedSdhInterface.getModifiedIipv6Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(channelizedSdhInterface.getModifiedSecondaryIpv4Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(channelizedSdhInterface.getModifiedSecondaryIpv4Address());
	}

	@Transactional
	public ScOrderBean handoverDetailsToServiceInventoryForRenewalWithNonCommercial(String orderCode, String serviceCode, Integer serviceId)
			throws TclCommonException {
		ScOrderBean scOrderBean = null;
		try {
			LOGGER.info("handoverDetailsToServiceInventoryForRenewalWithNonCommercial started with order code::{},service code:{} and serviceId:{}", orderCode,serviceCode,
					serviceId);
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.getServiceDetailsByScOrderUuidAndUuid(orderCode);
			for (ScServiceDetail scServiceDetail : scServiceDetailList) {
				Map<String, String> scComponentsAttrMapA = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				scOrderBean = serviceInventoryMapper.mapEntityToOrderBean(scOrder, scServiceDetail,
						scComponentsAttrMapA);
				ServiceDetailBean serviceDetailBean = serviceInventoryMapper.mapServiceDetailAttributes(null,
						scServiceDetail);
				List<ScComponent> scComponents = scComponentRepository
						.findByScServiceDetailIdAndComponentNameAndSiteTypeIn(scServiceDetail.getId(), "LM",
								Arrays.asList("A", "B"));
				if (!CollectionUtils.isEmpty(scComponents)) {
					for (ScComponent scComponent : scComponents) {
						ScComponentBean scComponentBean = mapScComponentToBean(scComponent);
						List<ScComponentAttributeBean> scComponentAttributeBeans = new ArrayList<>();
						if (!CollectionUtils.isEmpty(scComponent.getScComponentAttributes())) {
							scComponent.getScComponentAttributes().forEach(scComponentAttribute -> {
								ScComponentAttributeBean scComponentAttributeBean = mapScComponentAttribute(
										scComponentAttribute);
								scComponentAttributeBeans.add(scComponentAttributeBean);
							});
							scComponentBean.setScComponentAttributeBeans(scComponentAttributeBeans);
						}
						scOrderBean.getScServiceDetails().stream().forEach(scServiceDetailBean -> {
							scServiceDetailBean.getScComponentBeans().add(scComponentBean);
						});
					}
				}
				scOrderBean.setMuxDetails(constructMuxDetails(scComponentsAttrMapA));
				if (scServiceDetail != null) {

					List<ScServiceCommercial> scServiceCommercials = scServiceCommercialRepository
							.findByScServiceId(scServiceDetail.getId());

					if (!scServiceCommercials.isEmpty()) {

						for (ScServiceCommercial scServiceCommercial : scServiceCommercials) {

							scOrderBean.getScCommercialBean()
									.add(serviceInventoryMapper.mapServiceCommercialEntityToBean(scServiceCommercial));

						}

					}
					scOrderBean.setServiceDetailBean(serviceDetailBean);
				}
				LOGGER.info(
						"handoverDetailsToServiceInventoryForRenewalWithNonCommercial json to service inventory for service code:{} and json:{}",
						scServiceDetail.getUuid(), Utils.convertObjectToJson(scOrderBean));
				mqUtils.send(serviceHandoverInventory, Utils.convertObjectToJson(scOrderBean));
				if (Objects.nonNull(scServiceDetail) && Objects.nonNull(scServiceDetail.getOrderType())
						&& !"NEW".equalsIgnoreCase(scServiceDetail.getOrderType())) {
					LOGGER.info("Sending data to update MACD_DETAIL table for RenewalWithNonCommercial serviceid, ordercode,ordercategory {},{}",
							scServiceDetail.getId(), scServiceDetail.getScOrderUuid());
					String o2cDetail = orderCode;
					mqUtils.send(omsO2CMacdQueue, o2cDetail);
				}
			}
			ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJob.setType("SERVICE HANDOVER");
			serviceFulfillmentJob.setServiceId(serviceId);
			serviceFulfillmentJob.setCreatedTime(new Timestamp(new Date().getTime()));
			serviceFulfillmentJob.setServiceCode(serviceCode);
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
		} catch (Exception e) {
			ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJob.setType("SERVICE HANDOVER");
			serviceFulfillmentJob.setServiceId(serviceId);
			serviceFulfillmentJob.setCreatedTime(new Timestamp(new Date().getTime()));
			serviceFulfillmentJob.setServiceCode(serviceCode);
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
			LOGGER.error("error in pushing data to service inventory for RenewalWithNonCommercial serviceid:{} and error:{}", serviceCode, e);
		}
		return scOrderBean;
	}
	
	
	@Transactional
	public ScOrderBean handoverDetailsToServiceInventoryForNovation(String orderCode, String serviceCode, Integer serviceId)
			throws TclCommonException {
		ScOrderBean scOrderBean = null;
		try {
			LOGGER.info("handoverDetailsToServiceInventoryForNovation started with order code::{},service code:{} and serviceId:{}", orderCode,serviceCode,
					serviceId);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			Map<String, String> scComponentsAttrMapA = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

			scOrderBean = serviceInventoryMapper.mapEntityToOrderBean(scOrder, scServiceDetail, scComponentsAttrMapA);
			ServiceDetailBean serviceDetailBean = serviceInventoryMapper.mapServiceDetailAttributes(null, scServiceDetail);
			scOrderBean.setServiceDetailBean(serviceDetailBean);
			LOGGER.info("handoverDetailsToServiceInventoryForNovation json to service inventory for service code:{} and json:{}",
					serviceCode, Utils.convertObjectToJson(scOrderBean));
			ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJob.setType("SERVICE HANDOVER");
			serviceFulfillmentJob.setServiceId(scServiceDetail.getId());
			serviceFulfillmentJob.setCreatedTime(new Timestamp(new Date().getTime()));
			serviceFulfillmentJob.setServiceCode(serviceCode);
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
			mqUtils.send(serviceHandoverInventory, Utils.convertObjectToJson(scOrderBean));
		} catch (Exception e) {
			ServiceFulfillmentJob serviceFulfillmentJob = new ServiceFulfillmentJob();
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJob.setType("SERVICE HANDOVER");
			serviceFulfillmentJob.setServiceId(serviceId);
			serviceFulfillmentJob.setCreatedTime(new Timestamp(new Date().getTime()));
			serviceFulfillmentJob.setServiceCode(serviceCode);
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
			LOGGER.error("error in pushing data to service inventory for Novation serviceid:{} and error:{}", serviceCode, e);
		}
		return scOrderBean;
	}
}
