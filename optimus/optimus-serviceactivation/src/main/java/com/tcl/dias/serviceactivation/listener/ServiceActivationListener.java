package com.tcl.dias.serviceactivation.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.datamigration.DataMigrationGVPN;
import com.tcl.dias.serviceactivation.datamigration.DataMigrationILL;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.MigrationStatus;
import com.tcl.dias.serviceactivation.entity.entities.MstClrVpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.repository.MigrationStatusRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstClrVpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.service.ActivationService;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillmentutils.beans.CreatePlannedEventBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class ServiceActivationListener {

    private static final Logger logger = LoggerFactory.getLogger(ServiceActivationListener.class);

    @Autowired
    ActivationService activationService;
    
	@Autowired
	ServiceDetailRepository serviceDetailRepository;
	
	@Autowired
	MigrationStatusRepository migrationStatusRepository;
	
	@Autowired
	DataMigrationILL dataMigrationILL;
	
	@Autowired
	DataMigrationGVPN dataMigrationGVPN;
	
	@Autowired
	ServiceActivationService serviceActivationService;
	
	@Autowired
	MstClrVpnSolutionRepository mstClrVpnSolutionRepository;

    @RabbitListener(queuesToDeclare = {@Queue("${queue.serviceactivation.availabilityslots}")})
    public String triggerAvailabilitySlots(String request) throws TclCommonException {
        logger.info("ServiceActivationListener invoked for triggerAvailabilitySlots with input : {}", request);
        try {
            CreatePlannedEventBean createPlannedEventBean = Utils.convertJsonToObject(request, CreatePlannedEventBean.class);
            return Utils.convertObjectToJson(activationService.triggerAvailabilitySlots(createPlannedEventBean));
        } catch (TclCommonException e) {
            logger.error("Exception occurred ---> ServiceActivationListener:triggerAvailabilitySlots {}" ,e);
        }
        return "";
    }
    
    @RabbitListener(queuesToDeclare = {@Queue("${queue.migration}")})
    @Transactional(readOnly=false)
	public void doMigration(String request) {

		try {
			logger.info("Request received for doMigration : {}", request);

			Map<String, String> migrationAttributes = Utils.convertJsonToObject(request, Map.class);
			if (migrationAttributes != null && !CollectionUtils.isEmpty(migrationAttributes)) {

				String serviceId = migrationAttributes.get("serviceId");
				String type = migrationAttributes.get("type");
				String isTermination = migrationAttributes.get("termination");
				boolean migrationResponse = true;
				if (isTermination != null && isTermination.equalsIgnoreCase("yes")) {

					Integer scServiceDetailsId = Integer.valueOf(migrationAttributes.get("scServiceDetailsId"));

					Integer scOrderId = Integer.valueOf(migrationAttributes.get("scOrderId"));
					if (type.equals("ill")) {

						logger.info("Service code {} received for ILL Auto Migration", serviceId);
						migrationResponse = dataMigrationILL.runMigrationWi(1, serviceId,scServiceDetailsId,scOrderId,isTermination);
						logger.info("Service code {} processed for ILL Auto Migration", serviceId);

					}
					if (type.equals("gvpn")) {

						logger.info("Service code {} received for GVPN Auto Migration", serviceId);
						migrationResponse = dataMigrationGVPN.runMigrationWi(1, serviceId,scServiceDetailsId,scOrderId,isTermination);
						logger.info("Service code {} processed for GVPN Auto Migration", serviceId);

					}
				}
				else {
					
					logger.info("Service code received for auto-migration : {}", serviceId);
					
					ServiceDetail existingServiceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId, "ISSUED");
					
					List<String> modifiedByList= new ArrayList<>();
					modifiedByList.add("OPTIMUS_RULE");
					modifiedByList.add("FTI_Refresh");
					ServiceDetail existingActiveServiceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateAndModifiedByInOrderByVersionDesc(serviceId, "ACTIVE",modifiedByList);
					
					if (existingServiceDetail != null || existingActiveServiceDetail!=null) {
						logger.info("ISSUED RECORD EXISTS FOR serviceID {} So no Auto-Migration", serviceId);
					} else {
						if (type.equals("ill")) {

							logger.info("Service code {} received for ILL Auto Migration", serviceId);
							dataMigrationILL.runMigrationWi(1, serviceId,null,null,null);
							logger.info("Service code {} processed for ILL Auto Migration", serviceId);

						}
						if (type.equals("gvpn")) {

							logger.info("Service code {} received for GVPN Auto Migration", serviceId);
							dataMigrationGVPN.runMigrationWi(1, serviceId,null,null,null);
							logger.info("Service code {} processed for GVPN Auto Migration", serviceId);

						}
					}

					logger.error("REQUEST PROCESSED for {}", serviceId);
				}

				
				


				/*
				 * List<MigrationStatus> migrationStatuses = migrationStatusRepository
				 * .findByServiceCodeAndResponse(serviceId, "SUCCESS");
				 * 
				 * if (migrationStatuses != null && !CollectionUtils.isEmpty(migrationStatuses)
				 * && serviceDetailRepository.
				 * findFirstByServiceIdAndServiceStateAndEndDateIsNotNullOrderByEndDate(
				 * serviceId, "ACTIVE") != null) { logger.error("MIGRATION NOT REQUIRED for {}",
				 * serviceId); } else {
				 */
				
				
				// }
				if(!migrationResponse && isTermination !=null && isTermination.equalsIgnoreCase("yes") ) {
					List<String> serviceCodes= new ArrayList<>();
					Integer scServiceDetailsId = null;
					serviceCodes.add(serviceId);
					if (migrationAttributes != null && !CollectionUtils.isEmpty(migrationAttributes)) {
							if(migrationAttributes.get("scServiceDetailsId")!=null) {
									scServiceDetailsId = Integer.valueOf(migrationAttributes.get("scServiceDetailsId"));
							}
					}

					serviceActivationService.refreshFTIData(serviceCodes,isTermination,scServiceDetailsId);
				}

			} else {
				logger.error("NULL REQUEST ISSUE");
			}
		} catch (Exception e) {
			logger.error("request: {} for Auto migration error : {}", request, e);
		}
	}
    
    @RabbitListener(queuesToDeclare = {@Queue("${queue.migration.fti}")})
    @Transactional(readOnly=false)
	public void doFtiMigration(String request) {

		try {
			logger.info("Request received for doFtiMigration : {}", request);

			Map<String, String> migrationAttributes = Utils.convertJsonToObject(request, Map.class);
			if (migrationAttributes != null && !CollectionUtils.isEmpty(migrationAttributes)) {

				String serviceId = migrationAttributes.get("serviceId");
				String scServiceDetailId = migrationAttributes.get("scServiceDetailId");

			
				logger.info("Service code received for auto fti migration : {}", serviceId);
				
				ServiceDetail existingServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId, "ISSUED");
				
				List<String> modifiedByList= new ArrayList<>();
				modifiedByList.add("OPTIMUS_RULE");
				modifiedByList.add("FTI_Refresh");
				ServiceDetail existingActiveServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateAndModifiedByInOrderByVersionDesc(serviceId, "ACTIVE",modifiedByList);
				
				if (existingServiceDetail != null || existingActiveServiceDetail!=null) {
					logger.info("ISSUED RECORD EXISTS FOR serviceID {} So no Auto FTI Migration", serviceId);
				} else {
					List<String> serviceCodes= new ArrayList<>();
					serviceCodes.add(serviceId);
					serviceActivationService.refreshFTIData(serviceCodes,null,Integer.valueOf(scServiceDetailId));
				}

				logger.error("REQUEST PROCESSED for FTI {}", serviceId);
				// }

			} else {
				logger.error("NULL REQUEST ISSUE FTI");
			}
		} catch (Exception e) {
			logger.error("request: {} for Auto FTI migration error : {}", request, e);
		}
	}
    

    @RabbitListener(queuesToDeclare = {@Queue("${queue.serviceactivation.handovernote}")})
    @Transactional(readOnly=false)
	public String getHandoverNoteDetails(String request) throws TclCommonException {
		logger.info("Service code received for getCurrentServiceDetailsByUuid : {}", request);
		List<String> serviceStates =  new ArrayList<>();
		serviceStates.add("ACTIVE");
		serviceStates.add("ISSUED");
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(request,serviceStates);
		logger.info("Service code received for: {}", serviceDetail);

		Map<String, String> result = getHandoverNoteAttributes(serviceDetail);
		logger.info("Current service details : {}", Utils.convertObjectToJson(result));
		return Utils.convertObjectToJson(result);
	}

	private Map<String, String> getHandoverNoteAttributes(ServiceDetail serviceDetail) {
		String cpeSerialNumbers = "";
		String baSwitchHostName = "";
		String baSwitchIp = "";
		String baSwitchPort = "";
		String interfaceType = "";
		String wanInterfaceType = "";
		String routingProtocol = "";
		String extendedLanRequired = "";
		String wanv4Address = "";
		String lanv4Address = "";
		String lanv6Address = "";
		String wanv6Address = "";

		for (InterfaceProtocolMapping ipm : serviceDetail.getInterfaceProtocolMappings()) {
			if (ipm.getCpe() != null) {
				Cpe cpe = ipm.getCpe();
				cpeSerialNumbers = cpe.getDeviceId();
			}
			if (ipm.getChannelizedE1serialInterface() != null) {
				wanInterfaceType = "Channelized E1 Serial";
			}
			if (ipm.getChannelizedSdhInterface() != null) {
				wanInterfaceType = "Channelized Sdh";
			}
			if (ipm.getEthernetInterface() != null) {
				wanInterfaceType = "Ethernet";
			}
			if (ipm.getStaticProtocol() != null) {
				routingProtocol = "Static";
			}
			if (ipm.getBgp() != null) {
				routingProtocol = "BGP";
			}
			if (ipm.getOspf() != null) {
				routingProtocol = "OSPF";
			}
		}

		for (Topology topology : serviceDetail.getTopologies()) {
			if (topology != null && !topology.getUniswitchDetails().isEmpty()) {
				Set<UniswitchDetail> details = topology.getUniswitchDetails();
				for (UniswitchDetail uni : details) {
					baSwitchHostName = uni.getHostName();
					baSwitchIp = uni.getMgmtIp();
					baSwitchPort = uni.getPhysicalPort();
					interfaceType = uni.getInterfaceName();
				}
			}
		}

		for (IpAddressDetail ipAddr : serviceDetail.getIpAddressDetails()) {
			if (ipAddr.getExtendedLanEnabled() != null) {
				Byte eLan = ipAddr.getExtendedLanEnabled();
				if (eLan == (byte) 0) {
					extendedLanRequired = "false";
				} else {
					extendedLanRequired = "true";
				}

			}
			if (ipAddr.getIpaddrLanv4Addresses() != null && !ipAddr.getIpaddrLanv4Addresses().isEmpty()) {
				for (IpaddrLanv4Address ipv4 : ipAddr.getIpaddrLanv4Addresses()) {
					lanv4Address = ipv4.getLanv4Address();
				}
			}
			if (ipAddr.getIpaddrLanv6Addresses() != null && !ipAddr.getIpaddrLanv6Addresses().isEmpty()) {
				for (IpaddrLanv6Address ipv6 : ipAddr.getIpaddrLanv6Addresses()) {
					lanv6Address = ipv6.getLanv6Address();
				}
			}
			if (ipAddr.getIpaddrWanv4Addresses() != null && !ipAddr.getIpaddrWanv4Addresses().isEmpty()) {
				for (IpaddrWanv4Address ipv4 : ipAddr.getIpaddrWanv4Addresses()) {
					wanv4Address = ipv4.getWanv4Address();
				}
			}
			if (ipAddr.getIpaddrWanv6Addresses() != null && !ipAddr.getIpaddrWanv6Addresses().isEmpty()) {
				for (IpaddrWanv6Address ipv6 : ipAddr.getIpaddrWanv6Addresses()) {
					wanv6Address = ipv6.getWanv6Address();
				}
			}
		}

		Map<String, String> map = new HashMap<>();
		map.put("cpeSerialNumbers", cpeSerialNumbers);
		map.put("baSwitchHostName", baSwitchHostName);
		map.put("baSwitchIp", baSwitchIp);
		map.put("baSwitchPort", baSwitchPort);
		map.put("interfaceType", interfaceType);
		map.put("wanInterfaceType", wanInterfaceType);
		map.put("routingProtocol", routingProtocol);
		map.put("extendedLanRequired", extendedLanRequired);
		map.put("lanv4Address", lanv4Address);
		map.put("wanv4Address", wanv4Address);
		map.put("lanv6Address", lanv6Address);
		map.put("wanv6Address", wanv6Address);
		logger.info("Service code map value for: {}", map);

		return map;
	}
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sa.service.terminate.queue}") })
	public void terminateService(Message<String> request) {
		try {
			logger.info("In terminateService of service activation service : {} ",request);
			String serviceId = request.getPayload();
			logger.info("ServiceId need to terminate : {}",serviceId);
			if(Objects.nonNull(serviceId) && !serviceId.isEmpty()) {
				activationService.terminateService(Integer.valueOf(serviceId));
			}
		} catch (Exception ex) {
			logger.error("error in terminateService of service activation : {} ",ex);
		}
	}
	
	@Transactional
	public boolean autoMigration(Map<String, String> migrationAttributes) {

		try {
			logger.info("Request received for autoMigration : {}", migrationAttributes);

			if (migrationAttributes != null && !CollectionUtils.isEmpty(migrationAttributes)) {

				String serviceId = migrationAttributes.get("serviceId");
				String type = migrationAttributes.get("type");
				String isTermination = migrationAttributes.get("termination");
				if (isTermination != null && isTermination.equalsIgnoreCase("yes")) {

					Integer scServiceDetailsId = Integer.valueOf(migrationAttributes.get("scServiceDetailsId"));

					Integer scOrderId = Integer.valueOf(migrationAttributes.get("scOrderId"));
					if (type.equals("ill")) {

						logger.info("Service code {} received for ILL Auto Migration", serviceId);
						dataMigrationILL.runMigrationWi(1, serviceId, scServiceDetailsId, scOrderId, isTermination);
						logger.info("Service code {} processed for ILL Auto Migration", serviceId);

					}
					if (type.equals("gvpn")) {

						logger.info("Service code {} received for GVPN Auto Migration", serviceId);
						dataMigrationGVPN.runMigrationWi(1, serviceId, scServiceDetailsId, scOrderId, isTermination);
						logger.info("Service code {} processed for GVPN Auto Migration", serviceId);

					}
				} else {

					logger.info("Service code received for auto-migration : {}", serviceId);

					ServiceDetail existingServiceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId, "ISSUED");

					List<String> modifiedByList = new ArrayList<>();
					modifiedByList.add("OPTIMUS_RULE");
					modifiedByList.add("FTI_Refresh");
					ServiceDetail existingActiveServiceDetail = serviceDetailRepository
							.findFirstByServiceIdAndServiceStateAndModifiedByInOrderByVersionDesc(serviceId, "ACTIVE",
									modifiedByList);

					if (existingServiceDetail != null || existingActiveServiceDetail != null) {
						logger.info("ISSUED RECORD EXISTS FOR serviceID {} So no Auto-Migration", serviceId);
					} else {
						if (type.equals("ill")) {

							logger.info("Service code {} received for ILL Auto Migration", serviceId);
							dataMigrationILL.runMigrationWi(1, serviceId, null, null, null);
							logger.info("Service code {} processed for ILL Auto Migration", serviceId);

						}
						if (type.equals("gvpn")) {

							logger.info("Service code {} received for GVPN Auto Migration", serviceId);
							dataMigrationGVPN.runMigrationWi(1, serviceId, null, null, null);
							logger.info("Service code {} processed for GVPN Auto Migration", serviceId);

						}
					}

					logger.error("REQUEST PROCESSED for {}", serviceId);
				}

			} else {
				logger.error("NULL REQUEST ISSUE");
			}
		} catch (Exception e) {
			logger.error("request: {} for Auto migration error : {}", migrationAttributes, e);
		}
		
		return true;
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${queue.serviceactivation.vpnsolution}")})
    @Transactional(readOnly=false)
	public String getVpnSolutionDetails(String request) throws TclCommonException {
		logger.info("getVpnSolutionDetails request : {}", request);
		String vpnSolution=null;
		Map<String, String> vpnAttributesMap = Utils.convertJsonToObject(request, Map.class);
		if (vpnAttributesMap != null && !CollectionUtils.isEmpty(vpnAttributesMap)) {
			logger.info("vpnAttributesMap exists : {}", vpnAttributesMap);
			String serviceCode = vpnAttributesMap.get("serviceCode");
			String vpnTopology = vpnAttributesMap.get("vpnTopology");
			MstClrVpnSolution mstClrVpnSolution=mstClrVpnSolutionRepository.findFirstByServiceCodeAndVpnTopologyIgnoreCase(serviceCode, vpnTopology);
			if(mstClrVpnSolution!=null) {
				logger.info("Clr Vpn Solution exists : {}", mstClrVpnSolution.getSolutionId());
				vpnSolution=mstClrVpnSolution.getSolutionId();
			}
		}
		return vpnSolution;
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${queue.izopc.serviceactivation.handovernote}")})
    @Transactional(readOnly=false)
	public String getIzopcHandoverNoteDetails(String request) throws TclCommonException {
		logger.info("Service code received for getIzopcHandoverNoteDetails : {}", request);
		List<String> serviceStates =  new ArrayList<>();
		serviceStates.add("ACTIVE");
		serviceStates.add("ISSUED");
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(request,serviceStates);
		logger.info("IZOPC Service code received for: {}", serviceDetail);
		Map<String, String> result = getIZOPCHandoverNoteAttributes(serviceDetail);
		logger.info("IZOPC Current service details : {}", Utils.convertObjectToJson(result));
		return Utils.convertObjectToJson(result);
	}
	
	private Map<String, String> getIZOPCHandoverNoteAttributes(ServiceDetail serviceDetail) {
		logger.info("Service Id received for getIZOPCHandoverNoteAttributes : {}", serviceDetail.getServiceId());
		String routingProtocol = "";
		String wanv4Address = "";
		String lanv4Address = "";
		String bgpAsNumber = "";

		for (InterfaceProtocolMapping ipm : serviceDetail.getInterfaceProtocolMappings()) {
			if (ipm.getStaticProtocol() != null) {
				routingProtocol = "Static";
			}
			if (ipm.getBgp() != null) {
				routingProtocol = "BGP";
				if(ipm.getBgp().getRemoteAsNumber()!=null) {
					bgpAsNumber=String.valueOf(ipm.getBgp().getRemoteAsNumber());
				}
			}
			if (ipm.getOspf() != null) {
				routingProtocol = "OSPF";
			}
		}
		for (IpAddressDetail ipAddr : serviceDetail.getIpAddressDetails()) {
			if (ipAddr.getIpaddrLanv4Addresses() != null && !ipAddr.getIpaddrLanv4Addresses().isEmpty()) {
				for (IpaddrLanv4Address ipv4 : ipAddr.getIpaddrLanv4Addresses()) {
					lanv4Address = lanv4Address+","+ipv4.getLanv4Address();
				}
			}
			if (ipAddr.getIpaddrWanv4Addresses() != null && !ipAddr.getIpaddrWanv4Addresses().isEmpty()) {
				for (IpaddrWanv4Address ipv4 : ipAddr.getIpaddrWanv4Addresses()) {
					wanv4Address =wanv4Address+","+ipv4.getWanv4Address();
				}
			}
		}

		Map<String, String> map = new HashMap<>();
		map.put("routingProtocol", routingProtocol);
		map.put("bgpAsNumber", bgpAsNumber);
		map.put("lanv4Address", !lanv4Address.isEmpty()?lanv4Address.substring(1, lanv4Address.length()):lanv4Address);
		map.put("wanv4Address", !wanv4Address.isEmpty()?wanv4Address.substring(1, wanv4Address.length()):wanv4Address);
		logger.info("IZOPC Service Id::{} map value for: {}",serviceDetail.getServiceId(), map);
		return map;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sa.service.fti.netpref.queue}") })
	public void refreshNetpRefId(Message<String> request) {
		try {
			logger.info("refreshNetpRefId for service activation service : {} ",request);
			String serviceId = request.getPayload();
			logger.info("ServiceId need to refreshNetpRefId : {}",serviceId);
			if(Objects.nonNull(serviceId) && !serviceId.isEmpty()) {
				activationService.refreshNetpRefId(Integer.valueOf(serviceId));
			}
		} catch (Exception ex) {
			logger.error("Exception in refreshNetpRefId of service activation : {} ",ex);
		}
	}
}
