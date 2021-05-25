package com.tcl.dias.serviceactivation.macd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.services.IPDetailsService;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillmentutils.beans.BgpBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChannelizedE1serialInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChannelizedSdhInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.EthernetInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpAddressDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrLanv4AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrLanv6AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrWanv4AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrWanv6AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.MulticastingBean;
import com.tcl.dias.servicefulfillmentutils.beans.PEDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.TopologyBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class has methods for ace rule
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Service
public class AceRule {
	
	private static final Logger logger = LoggerFactory.getLogger(AceRule.class);


	@Autowired
	private IPDetailsService ipDetailsService;

	@Autowired
	private ServiceDetailRepository serviceDetailRepository;

	public Boolean isPortChanged(String serviceId) throws TclCommonException {

		TaskBean task2 = ipDetailsService.getIpServiceDetails(null, null, serviceId,null); // NEW
		ServiceDetail oldSerDet = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId,"ACTIVE");
		TaskBean task1 = ipDetailsService.getIpServiceDetails(null, oldSerDet.getVersion(), serviceId,null); // OLD

		Boolean isPortChanged = false;
		Map<String, String> attributeMap = new HashMap<>();

		String currentKey;
		currentKey = "RouterHostname";
		String oldK = "old";
		String newK = "new";

		PEDetailsBean bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
				.getPeDetailsBean();
		PEDetailsBean bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
				.getPeDetailsBean();

		if (bean1.getRouterDetailBeans().stream().findFirst().isPresent()
				|| bean2.getRouterDetailBeans().stream().findFirst().isPresent()) {
			if (bean1.getRouterDetailBeans().stream().findFirst().isPresent()
					&& bean1.getRouterDetailBeans().stream().findFirst().get().getRouterHostname() != null) {
				attributeMap.put(oldK + currentKey,
						bean1.getRouterDetailBeans().stream().findFirst().get().getRouterHostname().toLowerCase());
			} else {
				attributeMap.put(oldK + currentKey, "null");
			}

			if (bean2.getRouterDetailBeans().stream().findFirst().isPresent()
					&& bean2.getRouterDetailBeans().stream().findFirst().get().getRouterHostname() != null) {
				attributeMap.put(newK + currentKey,
						bean2.getRouterDetailBeans().stream().findFirst().get().getRouterHostname().toLowerCase());
			} else {
				attributeMap.put(newK + currentKey, "null");
			}
			logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
					attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
			if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
				isPortChanged = true;
			}
		}

		if (bean1.getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent()
				|| bean2.getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent()) {

			currentKey = "InterfaceName";

			if (bean1.getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent() && bean1
					.getChannelizedE1serialInterfaceBeans().stream().findFirst().get().getInterfaceName() != null) {
				attributeMap.put(oldK + currentKey, bean1.getChannelizedE1serialInterfaceBeans().stream().findFirst()
						.get().getInterfaceName().toLowerCase());
			} else {
				attributeMap.put(oldK + currentKey, "null");
			}

			if (bean2.getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent() && bean2
					.getChannelizedE1serialInterfaceBeans().stream().findFirst().get().getInterfaceName() != null) {
				attributeMap.put(newK + currentKey, bean2.getChannelizedE1serialInterfaceBeans().stream().findFirst()
						.get().getInterfaceName().toLowerCase());
			} else {
				attributeMap.put(newK + currentKey, "null");
			}
			logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
					attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
			if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
				isPortChanged = true;
			}
		}

		if (bean1.getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()
				|| bean2.getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()) {

			currentKey = "InterfaceName";

			if (bean1.getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()
					&& bean1.getChannelizedSdhInterfaceBeans().stream().findFirst().get().getInterfaceName() != null) {
				attributeMap.put(oldK + currentKey, bean1.getChannelizedSdhInterfaceBeans().stream().findFirst().get()
						.getInterfaceName().toLowerCase());
			} else {
				attributeMap.put(oldK + currentKey, "null");
			}
			if (bean2.getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()
					&& bean2.getChannelizedSdhInterfaceBeans().stream().findFirst().get().getInterfaceName() != null) {
				attributeMap.put(newK + currentKey, bean2.getChannelizedSdhInterfaceBeans().stream().findFirst().get()
						.getInterfaceName().toLowerCase());
			} else {
				attributeMap.put(newK + currentKey, "null");
			}
			logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
					attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
			if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
				isPortChanged = true;
			}
		}

		if (bean1.getEthernetInterfaceBeans().stream().findFirst().isPresent()
				|| bean2.getEthernetInterfaceBeans().stream().findFirst().isPresent()) {

			currentKey = "InterfaceName";

			if (bean1.getEthernetInterfaceBeans().stream().findFirst().isPresent()
					&& bean1.getEthernetInterfaceBeans().stream().findFirst().get().getInterfaceName() != null) {
				attributeMap.put(oldK + currentKey,
						bean1.getEthernetInterfaceBeans().stream().findFirst().get().getInterfaceName().toLowerCase());
			} else {
				attributeMap.put(oldK + currentKey, "null");
			}

			if (bean2.getEthernetInterfaceBeans().stream().findFirst().isPresent()
					&& bean2.getEthernetInterfaceBeans().stream().findFirst().get().getInterfaceName() != null) {
				attributeMap.put(newK + currentKey,
						bean2.getEthernetInterfaceBeans().stream().findFirst().get().getInterfaceName().toLowerCase());
			} else {
				attributeMap.put(newK + currentKey, "null");
			}
			logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
					attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
			if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
				isPortChanged = true;
			}
		}
		
		//uniswitch vlan for GVPN
		if (task1.getOrderDetails().getServiceDetailBeans().get(0).getServiceState().toLowerCase().contains("gvpn")
				&& task2.getOrderDetails().getServiceDetailBeans().get(0).getServiceState().toLowerCase()
						.contains("gvpn")) {
			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans() != null
					&& !task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().isEmpty()
					&& task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0) != null
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans() != null
					&& !task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().isEmpty()
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0) != null) {
				TopologyBean bean11 = task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0);
				TopologyBean bean21 = task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0);
				
				currentKey = "UniswitchInnerVLAN";
				
				if (bean11.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(oldK + currentKey,
							bean11.getUniswitchDetails().stream().findFirst().get().getInnerVlan());
				else
					attributeMap.put(oldK + currentKey, "null");
				if (bean21.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(newK + currentKey,
							bean21.getUniswitchDetails().stream().findFirst().get().getInnerVlan());
				else
					attributeMap.put(newK + currentKey, "null");
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				//Old can be null as it is not migrated
				if (!attributeMap.get(oldK + currentKey).equalsIgnoreCase("null")
						&& !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					isPortChanged = true;
				}
			}
		}

		
			

		MacdArgs result = new MacdArgs();
		result.setVar(isPortChanged);
		result.setAttributeMap(attributeMap);
		return isPortChanged;

	}

	public Boolean isDowntimeRequired(String serviceId) throws TclCommonException {
		Boolean isDowntimeRequired = false;
		try {
			
						
			TaskBean task2 = ipDetailsService.getIpServiceDetails(null, null, serviceId,null); // NEW
			ServiceDetail oldSerDet = serviceDetailRepository
					.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId,"ACTIVE");
			TaskBean task1 = ipDetailsService.getIpServiceDetails(null, oldSerDet.getVersion(), serviceId,null); // OLD

			logger.info("Downtime - service:{} and old task {}", serviceId, Utils.convertObjectToJson(task1));
			logger.info("Downtime - service:{} and new task {}", serviceId, Utils.convertObjectToJson(task2));

			Map<String, String> attributeMap = new HashMap<>();
			List<DowntimeAttributeChange> attributesChange = new ArrayList<>();

			String currentKey;
			
			String oldK = "old";
			String newK = "new";
			
			//Juniper-isidcservice
			/*currentKey="IsIdcService";
			if(task1.getOrderDetails().getServiceDetailBeans().get(0)!=null && task1.getOrderDetails().getServiceDetailBeans().get(0).getIsIdcService()!=null) {
				attributeMap.put(oldK + currentKey, String.valueOf(task1.getOrderDetails().getServiceDetailBeans().get(0).getIsIdcService()));
			} else {
				attributeMap.put(oldK + currentKey, "null");
			}
			if(task2.getOrderDetails().getServiceDetailBeans().get(0)!=null && task2.getOrderDetails().getServiceDetailBeans().get(0).getIsIdcService()!=null) {
				attributeMap.put(newK + currentKey, String.valueOf(task2.getOrderDetails().getServiceDetailBeans().get(0).getIsIdcService()));
			} else {
				attributeMap.put(newK + currentKey, "null");
			}
			logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
			if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
				attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
				isDowntimeRequired = true;
			}*/
			
			PEDetailsBean pebean1 = task1.getOrderDetails().getServiceDetailBeans().get(0)
					.getInterfaceProtocolMappingBean().getPeDetailsBean();
			PEDetailsBean pebean2 = task2.getOrderDetails().getServiceDetailBeans().get(0)
					.getInterfaceProtocolMappingBean().getPeDetailsBean();
			currentKey = "RouterHostname";
			if (pebean1.getRouterDetailBeans().stream().findFirst().isPresent()
					|| pebean2.getRouterDetailBeans().stream().findFirst().isPresent()) {
				if (pebean1.getRouterDetailBeans().stream().findFirst().isPresent()
						&& pebean1.getRouterDetailBeans().stream().findFirst().get().getRouterHostname() != null) {
					attributeMap.put(oldK + currentKey, pebean1.getRouterDetailBeans().stream().findFirst().get()
							.getRouterHostname().toLowerCase());
				} else {
					attributeMap.put(oldK + currentKey, "null");
				}

				if (pebean2.getRouterDetailBeans().stream().findFirst().isPresent()
						&& pebean2.getRouterDetailBeans().stream().findFirst().get().getRouterHostname() != null) {
					attributeMap.put(newK + currentKey, pebean2.getRouterDetailBeans().stream().findFirst().get()
							.getRouterHostname().toLowerCase());
				} else {
					attributeMap.put(newK + currentKey, "null");
				}
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					logger.info("RouterHostname");
					isDowntimeRequired = true;
				}
			}
			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
					.getPeDetailsBean().getEthernetInterfaceBeans().stream().findFirst().isPresent()
					|| task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getEthernetInterfaceBeans().stream().findFirst().isPresent()) {

				currentKey = new String("InterfaceMode");
				
				EthernetInterfaceBean bean1 = null, bean2 = null;

				if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getEthernetInterfaceBeans().stream().findFirst().isPresent()) {
					bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getEthernetInterfaceBeans().stream().findFirst().get();
				}
				if (task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getEthernetInterfaceBeans().stream().findFirst().isPresent()) {
					bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getEthernetInterfaceBeans().stream().findFirst().get();
				}

				MacdArgs result = checkDetailsEthernetInterface(oldK, newK, currentKey, bean1, bean2, attributeMap,attributesChange);
				if (result.getVar() == true) {
					logger.info("EInterfaceMode");
					isDowntimeRequired = result.getVar();
				}
				attributeMap = result.getAttributeMap();

			}

			// routermake
			currentKey = "RouterMake";
			if (pebean1.getRouterDetailBeans().stream().findFirst().isPresent()
					|| pebean2.getRouterDetailBeans().stream().findFirst().isPresent()) {
				if (pebean1.getRouterDetailBeans().stream().findFirst().isPresent()
						&& pebean1.getRouterDetailBeans().stream().findFirst().get().getRouterMake() != null) {
					attributeMap.put(oldK + currentKey,
							pebean1.getRouterDetailBeans().stream().findFirst().get().getRouterMake().toLowerCase());
				} else {
					attributeMap.put(oldK + currentKey, "null");
				}

				if (pebean2.getRouterDetailBeans().stream().findFirst().isPresent()
						&& pebean2.getRouterDetailBeans().stream().findFirst().get().getRouterMake() != null) {
					attributeMap.put(newK + currentKey,
							pebean2.getRouterDetailBeans().stream().findFirst().get().getRouterMake().toLowerCase());
				} else {
					attributeMap.put(newK + currentKey, "null");
				}
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("RouterMake");
				}
			}

			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
					.getPeDetailsBean().getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent()
					|| task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getChannelizedE1serialInterfaceBeans().stream().findFirst()
							.isPresent()) {

				currentKey = "InterfaceMode";
				
				ChannelizedE1serialInterfaceBean bean1 = null,bean2 = null;
				
				if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent()) {
					bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getChannelizedE1serialInterfaceBeans().stream().findFirst().get();
				}
				if (task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getChannelizedE1serialInterfaceBeans().stream().findFirst().isPresent()) {
					bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getChannelizedE1serialInterfaceBeans().stream().findFirst().get();
				}

				MacdArgs result = checkDetailsSerialInterface(oldK, newK, currentKey, bean1, bean2, attributeMap,attributesChange);
				if (result.getVar() == true) {
					isDowntimeRequired = result.getVar();
					logger.info("CES InterfaceMode");
				}
				attributeMap = result.getAttributeMap();
			}

			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
					.getPeDetailsBean().getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()
					|| task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()) {

				currentKey = "InterfaceMode";
				
				ChannelizedSdhInterfaceBean bean1 = null, bean2 = null;

				if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()) {
					bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0)
							.getInterfaceProtocolMappingBean().getPeDetailsBean().getChannelizedSdhInterfaceBeans()
							.stream().findFirst().get();
				}

				if (task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getChannelizedSdhInterfaceBeans().stream().findFirst().isPresent()) {
					bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getChannelizedSdhInterfaceBeans().stream().findFirst().get();
				}

				MacdArgs result = checkDetailsSdhInterface(oldK, newK, currentKey, bean1, bean2, attributeMap,attributesChange);
				if (result.getVar() == true) {
					isDowntimeRequired = result.getVar();
					logger.info("SDH InterfaceMode");
				}
				attributeMap = result.getAttributeMap();
			}

			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans() != null && !task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().isEmpty() && task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0)!=null
					&&  task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans() != null && !task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().isEmpty() && task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0)!=null) {
				TopologyBean bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0);
				TopologyBean bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0).getTopologyBeans().get(0);

				currentKey = "TopologyName";
				if (bean1.getTopologyName() != null)
					attributeMap.put(oldK + currentKey, bean1.getTopologyName());
				else
					attributeMap.put(oldK + currentKey, "null");
				if (bean2.getTopologyName() != null)
					attributeMap.put(newK + currentKey, bean2.getTopologyName());
				else
					attributeMap.put(newK + currentKey, "null");
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("TopologyName");
				}

				currentKey = "UniswitchHostName";
				if (bean1.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(oldK + currentKey,
							bean1.getUniswitchDetails().stream().findFirst().get().getHostName());
				else
					attributeMap.put(oldK + currentKey, "null");
				if (bean2.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(newK + currentKey,
							bean2.getUniswitchDetails().stream().findFirst().get().getHostName());
				else
					attributeMap.put(newK + currentKey, "null");
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("UniswitchHostName");
				}

				currentKey = "UniswitchInterfaceName";
				if (bean1.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(oldK + currentKey,
							bean1.getUniswitchDetails().stream().findFirst().get().getInterfaceName());
				else
					attributeMap.put(oldK + currentKey, "null");
				if (bean2.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(newK + currentKey,
							bean2.getUniswitchDetails().stream().findFirst().get().getInterfaceName());
				else
					attributeMap.put(newK + currentKey, "null");
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("UniswitchInterfaceName");
				}

				currentKey = "UniswitchModeName";
				if (bean1.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(oldK + currentKey,
							bean1.getUniswitchDetails().stream().findFirst().get().getMode());
				else
					attributeMap.put(oldK + currentKey, "null");
				if (bean2.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(newK + currentKey,
							bean2.getUniswitchDetails().stream().findFirst().get().getMode());
				else
					attributeMap.put(newK + currentKey, "null");
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("UniswitchModeName");
				}
				
				currentKey = "UniswitchHandoff";
				if (bean1.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(oldK + currentKey,
							bean1.getUniswitchDetails().stream().findFirst().get().getHandoff());
				else
					attributeMap.put(oldK + currentKey, "null");
				if (bean2.getUniswitchDetails().stream().findFirst().isPresent())
					attributeMap.put(newK + currentKey,
							bean2.getUniswitchDetails().stream().findFirst().get().getHandoff());
				else
					attributeMap.put(newK + currentKey, "null");
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("UniswitchHandoff");
				}

			}

			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getIpAddressDetailBeans().get(0) != null && task2
					.getOrderDetails().getServiceDetailBeans().get(0).getIpAddressDetailBeans().get(0) != null) {

				IpAddressDetailBean bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0)
						.getIpAddressDetailBeans().get(0);
				IpAddressDetailBean bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0)
						.getIpAddressDetailBeans().get(0);
				// pathiptype
				if ((bean1.getPathIpType() != null && bean2.getPathIpType() == null)
						|| (bean1.getPathIpType() == null && bean2.getPathIpType() != null)) {
					attributesChange.add(new DowntimeAttributeChange("PathIpType",bean1.getPathIpType(),bean2.getPathIpType()));
					isDowntimeRequired = true;
					logger.info("PathIpType");
				} else {

					currentKey = "PathIpType";

					if (bean1.getPathIpType() != null)
						attributeMap.put(oldK + currentKey, bean1.getPathIpType());
					else
						attributeMap.put(oldK + currentKey, "null");
					if (bean2.getPathIpType() != null)
						attributeMap.put(newK + currentKey, bean2.getPathIpType());
					else
						attributeMap.put(newK + currentKey, "null");
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("ELSE PathIpType");
					}
				}

				MacdArgs result1 = checkWanv4(oldK, newK, bean1.getIpaddrWanv4Addresses(),
						bean2.getIpaddrWanv4Addresses(), attributeMap,attributesChange);
				if (result1.getVar() == true) {
					isDowntimeRequired = result1.getVar();
					logger.info("result1");
				}
				attributeMap = result1.getAttributeMap();

				MacdArgs result2 = checkLanv4(oldK, newK, bean1.getIpaddrLanv4Addresses(),
						bean2.getIpaddrLanv4Addresses(), attributeMap,attributesChange);
				if (result2.getVar() == true) {
					isDowntimeRequired = result2.getVar();
					logger.info("result2");
				}
				attributeMap = result2.getAttributeMap();

				MacdArgs result3 = checkWanv6(oldK, newK, bean1.getIpaddrWanv6Addresses(),
						bean2.getIpaddrWanv6Addresses(), attributeMap,attributesChange);
				if (result3.getVar() == true) {
					isDowntimeRequired = result3.getVar();
					logger.info("result3");
				}
				attributeMap = result3.getAttributeMap();

				MacdArgs result4 = checkLanv6(oldK, newK, bean1.getIpaddrLanv6Addresses(),
						bean2.getIpaddrLanv6Addresses(), attributeMap,attributesChange);
				if (result4.getVar() == true) {
					isDowntimeRequired = result4.getVar();
					logger.info("result4");
				}
				attributeMap = result4.getAttributeMap();
			}

			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
					.getPeDetailsBean() != null
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean() != null) {
				
				currentKey = "Protocol";
				
				PEDetailsBean bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0)
						.getInterfaceProtocolMappingBean().getPeDetailsBean();
				PEDetailsBean bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0)
						.getInterfaceProtocolMappingBean().getPeDetailsBean();

				if (bean1.getBgpBeans() != null) {
					attributeMap.put(oldK + currentKey, "bgp");
				} else {
					if (bean1.getStaticProtocolBeans() != null) {
						attributeMap.put(oldK + currentKey, "static");
					} else if (bean1.getOspfBeans() != null) {
						attributeMap.put(oldK + currentKey, "ospf");
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
				}

				if (bean2.getBgpBeans() != null) {
					attributeMap.put(newK + currentKey, "bgp");
				} else {
					if (bean2.getStaticProtocolBeans() != null) {
						attributeMap.put(newK + currentKey, "static");
					} else if (bean2.getOspfBeans() != null) {
						attributeMap.put(newK + currentKey, "ospf");
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
				}
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("Protocol");
				}

			}

			if (!task1.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().isEmpty()
					|| !task2.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().isEmpty()) {

				currentKey = "VrfName";
				if (Objects.nonNull(task1.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans())
						&& !task1.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().isEmpty()
						&&  task1.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().get(0) != null) {
					if (task1.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().get(0)
							.getVrfName() != null) {
						attributeMap.put(oldK + currentKey, task1.getOrderDetails().getServiceDetailBeans().get(0)
								.getVrfBeans().get(0).getVrfName().toLowerCase());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
				} else {
					attributeMap.put(oldK + currentKey, "null");
				}

				if (Objects.nonNull(task2.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans()) 
						&& !task2.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().isEmpty()
						&& task2.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().get(0) != null) {
					if (task2.getOrderDetails().getServiceDetailBeans().get(0).getVrfBeans().get(0)
							.getVrfName() != null) {
						attributeMap.put(newK + currentKey, task2.getOrderDetails().getServiceDetailBeans().get(0)
								.getVrfBeans().get(0).getVrfName().toLowerCase());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
				} else {
					attributeMap.put(newK + currentKey, "null");
				}
				logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
						attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
				if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
					isDowntimeRequired = true;
					logger.info("VrfName");
				}
			}

			if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
					.getPeDetailsBean().getBgpBeans().stream().findFirst().isPresent()
					|| task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getBgpBeans().stream().findFirst().isPresent()) {
				BgpBean bean1 = null;
				BgpBean bean2 = null;
				if (task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getBgpBeans().stream().findFirst().isPresent())
					bean1 = task1.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getBgpBeans().stream().findFirst().get();

				if (task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
						.getPeDetailsBean().getBgpBeans().stream().findFirst().isPresent())
					bean2 = task2.getOrderDetails().getServiceDetailBeans().get(0).getInterfaceProtocolMappingBean()
							.getPeDetailsBean().getBgpBeans().stream().findFirst().get();

				if ((bean1 != null && bean2 == null) || (bean1 == null && bean2 != null)) {
					attributesChange.add(new DowntimeAttributeChange("BGP",bean1!=null ? Utils.convertObjectToJson(bean1) : null,bean2!=null ? Utils.convertObjectToJson(bean2) : null));
					isDowntimeRequired = true;
				} else {
					currentKey = "BgpRemoteAsNumber";
					if (bean1.getRemoteAsNumber() != null) {
						attributeMap.put(oldK + currentKey, bean1.getRemoteAsNumber());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (bean2.getRemoteAsNumber() != null) {
						attributeMap.put(newK + currentKey, bean2.getRemoteAsNumber());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("BgpRemoteAsNumber");
					}

					currentKey = "BgpRoutesExchanged";
					if (bean1.getRoutesExchanged() != null) {
						attributeMap.put(oldK + currentKey, bean1.getRoutesExchanged());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (bean2.getRoutesExchanged() != null) {
						attributeMap.put(newK + currentKey, bean2.getRoutesExchanged());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("BgpRoutesExchanged");
					}

					currentKey = "BgpNeighbourinboundv4routermapname";
					if (bean1.getBgpneighbourinboundv4routermapname() != null) {
						attributeMap.put(oldK + currentKey, bean1.getBgpneighbourinboundv4routermapname());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (bean2.getBgpneighbourinboundv4routermapname() != null) {
						attributeMap.put(newK + currentKey, bean2.getBgpneighbourinboundv4routermapname());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("BgpNeighbourinboundv4routermapname");
					}

					currentKey = "BgpNeighbourinboundv6routermapname";
					if (bean1.getBgpneighbourinboundv6routermapname() != null) {
						attributeMap.put(oldK + currentKey, bean1.getBgpneighbourinboundv6routermapname());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (bean2.getBgpneighbourinboundv6routermapname() != null) {
						attributeMap.put(newK + currentKey, bean2.getBgpneighbourinboundv6routermapname());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("BgpNeighbourinboundv6routermapname");
					}

					currentKey = "BgpNeighbourOn";
					if (bean1.getNeighborOn() != null) {
						attributeMap.put(oldK + currentKey, bean1.getNeighborOn());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (bean2.getNeighborOn() != null) {
						attributeMap.put(newK + currentKey, bean2.getNeighborOn());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("BgpNeighbourOn");
					}

					//juniper-rtbh
					currentKey = "RTBHoptions";
					if (bean1.getRtbhOptions() != null) {
						attributeMap.put(oldK + currentKey, bean1.getRtbhOptions());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (bean2.getRtbhOptions() != null) {
						attributeMap.put(newK + currentKey, bean2.getRtbhOptions());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}
					logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
							attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
						logger.info("RTBHoptions");
					}
				}

			}

			
			//MultiCast
		  /*currentKey="multiCast";
			if (task1.getOrderDetails().getServiceDetailBeans().get(0) != null
					&& task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans() != null
					&& !task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().isEmpty()
					&& task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().get(0) != null
					&& task2.getOrderDetails().getServiceDetailBeans().get(0) != null
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans() != null
					&&!task2.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().isEmpty()
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().get(0) != null){
				MulticastingBean oldMutliCast=task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().get(0);
				MulticastingBean newMutliCast=task2.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().get(0);
				
				//Current and Old As STATICRP 
				if(Objects.nonNull(newMutliCast.getType()) && Objects.nonNull(oldMutliCast.getType()) 
						&& "STATICRP ".equalsIgnoreCase(newMutliCast.getType())  && "STATICRP ".equalsIgnoreCase(oldMutliCast.getType())){
					compareRpAddress(oldMutliCast,newMutliCast,isDowntimeRequired);
				}
				//Current as AUTORP and Old As STATICRP  
				if(Objects.nonNull(newMutliCast.getType()) && Objects.nonNull(oldMutliCast.getType()) 
						&& "AUTORP ".equalsIgnoreCase(newMutliCast.getType())  && "STATICRP ".equalsIgnoreCase(oldMutliCast.getType())){
					isDowntimeRequired = true;
				}
			}else if((task1.getOrderDetails().getServiceDetailBeans().get(0)!=null 
					&& task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans()!=null && !task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().isEmpty()&&
					 task2.getOrderDetails().getServiceDetailBeans().get(0)!=null 
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().isEmpty())
					|| (task1.getOrderDetails().getServiceDetailBeans().get(0)!=null 
					&& task1.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans().isEmpty() 
					&& task2.getOrderDetails().getServiceDetailBeans().get(0)!=null 
					&& task2.getOrderDetails().getServiceDetailBeans().get(0).getMulticastingBeans()!=null)){
				isDowntimeRequired = true;
			}*/
			/*if (task1.getOrderDetails().getOrderType() != null || task2.getOrderDetails().getOrderType() != null) {
				currentKey = "orderType";
				if ((task1.getOrderDetails().getOrderType() != null && task2.getOrderDetails().getOrderType() == null)
						|| (task1.getOrderDetails().getOrderType() == null
								&& task2.getOrderDetails().getOrderType() != null)) {
					attributesChange.add(new DowntimeAttributeChange(currentKey,task1.getOrderDetails().getOrderType(),task2.getOrderDetails().getOrderType()));
					isDowntimeRequired = true;
				} else {

					if (task1.getOrderDetails().getOrderType() != null) {
						attributeMap.put(oldK + currentKey, task1.getOrderDetails().getOrderType());
					} else {
						attributeMap.put(oldK + currentKey, "null");
					}
					if (task2.getOrderDetails().getOrderType() != null) {
						attributeMap.put(newK + currentKey, task2.getOrderDetails().getOrderType());
					} else {
						attributeMap.put(newK + currentKey, "null");
					}

					if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
						attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
						isDowntimeRequired = true;
					}
				}
			}*/

			MacdArgs result = new MacdArgs();
			result.setVar(isDowntimeRequired);
			result.setAttributeMap(attributeMap);
			//result.setAttributesChangeList(attributesChange);
			if (isDowntimeRequired) {
				ServiceDetailBean newServiceDetBean = task2.getOrderDetails().getServiceDetailBeans().get(0);
				Optional<ServiceDetail> newServiceDetail = serviceDetailRepository.findById(newServiceDetBean.getId());
				if (newServiceDetail.isPresent()) {
					ServiceDetail serviceEntity = newServiceDetail.get();
					serviceEntity.setSolutionName(Utils.convertObjectToJson(attributesChange));
					serviceDetailRepository.save(serviceEntity);
				}
			}
			
			logger.info("isDowntime = {}  for service:{} and attribute Map {} and attributes change list {}", isDowntimeRequired,
					serviceId, attributeMap, attributesChange);
		
		} catch (Exception e) {
			logger.error("isDowntimeRequired error for service:{} and error:{} => {}",serviceId,e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return isDowntimeRequired;

	}



	private MacdArgs checkLanv6(String oldK, String newK, Set<IpaddrLanv6AddressBean> set1,
			Set<IpaddrLanv6AddressBean> set2, Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) throws TclCommonException {

		String currentKey = "Lanv6Addresses";
		Boolean isDowntimeRequired = false;
		Set<String> ipAddrSet1 = null;
		Set<String> ipAddrSet2 = null;
		if (set1.stream().findFirst().isPresent()) {
			ipAddrSet1 = set1.stream().map(e -> e.getLanv6Address()).collect(Collectors.toSet());
			attributeMap.put(oldK + currentKey, ipAddrSet1.toString());
		} else
			attributeMap.put(oldK + currentKey, "null");
		if (set2.stream().findFirst().isPresent()) {
			ipAddrSet2 = set2.stream().map(e -> e.getLanv6Address()).collect(Collectors.toSet());
			attributeMap.put(newK + currentKey, ipAddrSet2.toString());
		} else
			attributeMap.put(newK + currentKey, "null");

		if (ipAddrSet1 == null && ipAddrSet2 != null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,"null",Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 == null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),"null"));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 != null && !ipAddrSet1.equals(ipAddrSet2)) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		}
		MacdArgs result = new MacdArgs();
		result.setAttributeMap(attributeMap);
		result.setVar(isDowntimeRequired);
		return result;
	}

	private MacdArgs checkWanv6(String oldK, String newK, Set<IpaddrWanv6AddressBean> set1,
			Set<IpaddrWanv6AddressBean> set2, Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) throws TclCommonException {

		String currentKey = "Wanv6Addresses";
		Boolean isDowntimeRequired = false;
		Set<String> ipAddrSet1 = null;
		Set<String> ipAddrSet2 = null;
		if (set1.stream().findFirst().isPresent()) {
			ipAddrSet1 = set1.stream().map(e -> e.getWanv6Address()).collect(Collectors.toSet());
			attributeMap.put(oldK + currentKey, ipAddrSet1.toString());
		} else
			attributeMap.put(oldK + currentKey, "null");
		if (set2.stream().findFirst().isPresent()) {
			ipAddrSet2 = set2.stream().map(e -> e.getWanv6Address()).collect(Collectors.toSet());
			attributeMap.put(newK + currentKey, ipAddrSet2.toString());
		} else
			attributeMap.put(newK + currentKey, "null");

		if (ipAddrSet1 == null && ipAddrSet2 != null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,"null",Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 == null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),"null"));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 != null && !ipAddrSet1.equals(ipAddrSet2)) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		}
		MacdArgs result = new MacdArgs();
		result.setAttributeMap(attributeMap);
		result.setVar(isDowntimeRequired);
		return result;
	}

	private MacdArgs checkLanv4(String oldK, String newK, Set<IpaddrLanv4AddressBean> set1,
			Set<IpaddrLanv4AddressBean> set2, Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) throws TclCommonException {

		String currentKey = "Lanv4Addresses";
		Boolean isDowntimeRequired = false;
		Set<String> ipAddrSet1 = null;
		Set<String> ipAddrSet2 = null;
		if (set1.stream().findFirst().isPresent()) {
			ipAddrSet1 = set1.stream().map(e -> e.getLanv4Address()).collect(Collectors.toSet());
			attributeMap.put(oldK + currentKey, ipAddrSet1.toString());
		} else
			attributeMap.put(oldK + currentKey, "null");
		if (set2.stream().findFirst().isPresent()) {
			ipAddrSet2 = set2.stream().map(e -> e.getLanv4Address()).collect(Collectors.toSet());
			attributeMap.put(newK + currentKey, ipAddrSet2.toString());
		} else
			attributeMap.put(newK + currentKey, "null");

		if (ipAddrSet1 == null && ipAddrSet2 != null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,"null",Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 == null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),"null"));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 != null && !ipAddrSet1.equals(ipAddrSet2)) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		}
		MacdArgs result = new MacdArgs();
		result.setAttributeMap(attributeMap);
		result.setVar(isDowntimeRequired);
		return result;
	}

	private MacdArgs checkWanv4(String oldK, String newK, Set<IpaddrWanv4AddressBean> set1,
			Set<IpaddrWanv4AddressBean> set2, Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) throws TclCommonException {

		String currentKey = "Wanv4Addresses";
		Boolean isDowntimeRequired = false;
		Set<String> ipAddrSet1 = null;
		Set<String> ipAddrSet2 = null;
		if (set1.stream().findFirst().isPresent()) {
			ipAddrSet1 = set1.stream().map(e -> e.getWanv4Address()).collect(Collectors.toSet());
			attributeMap.put(oldK + currentKey, ipAddrSet1.toString());
		} else
			attributeMap.put(oldK + currentKey, "null");
		if (set2.stream().findFirst().isPresent()) {
			ipAddrSet2 = set2.stream().map(e -> e.getWanv4Address()).collect(Collectors.toSet());
			attributeMap.put(newK + currentKey, ipAddrSet2.toString());
		} else
			attributeMap.put(newK + currentKey, "null");

		if (ipAddrSet1 == null && ipAddrSet2 != null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,"null",Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 == null) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),"null"));
			isDowntimeRequired = true;
		} else if (ipAddrSet1 != null && ipAddrSet2 != null && !ipAddrSet1.equals(ipAddrSet2)) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,Utils.convertObjectToJson(ipAddrSet1),Utils.convertObjectToJson(ipAddrSet2)));
			isDowntimeRequired = true;
		}
		MacdArgs result = new MacdArgs();
		result.setAttributeMap(attributeMap);
		result.setVar(isDowntimeRequired);
		return result;
	}

	private MacdArgs checkDetailsSdhInterface(String oldK, String newK, String currentKey,
			ChannelizedSdhInterfaceBean bean1, ChannelizedSdhInterfaceBean bean2, Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) {

		MacdArgs macdArgs = new MacdArgs();
		Boolean isDowntimeRequired = false;

		if((bean1==null&&bean2!=null) || (bean1!=null && bean2==null)) {
			isDowntimeRequired=true;
			macdArgs.setVar(isDowntimeRequired);
			attributeMap.put("interfaceChangedSdh", "true");
			macdArgs.setAttributeMap(attributeMap);			
			return macdArgs;
		}
		
		if (bean1.getMode() != null)
			attributeMap.put(oldK + currentKey, bean1.getMode());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getMode() != null)
			attributeMap.put(newK + currentKey, bean2.getMode());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv4Address";
		if (bean1.getModifiedIpv4Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedIpv4Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedIpv4Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedIpv4Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv6Address";
		if (bean1.getModifiedIipv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedIipv6Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedIipv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedIipv6Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv4Address2";
		if (bean1.getModifiedSecondaryIpv4Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedSecondaryIpv4Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedSecondaryIpv4Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedSecondaryIpv4Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv6Address2";
		if (bean1.getModifiedSecondaryIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedSecondaryIpv6Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedSecondaryIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedSecondaryIpv6Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "SDH-InterfaceName";
		if (bean1.getIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getInterfaceName());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getInterfaceName());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "PhysicalPort";
		if (bean1.getPhysicalPort() != null)
			attributeMap.put(oldK + currentKey, bean1.getPhysicalPort());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getPhysicalPort() != null)
			attributeMap.put(newK + currentKey, bean2.getPhysicalPort());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "FirstTimeSlot";
		if (bean1.get_64kFirstTimeSlot() != null)
			attributeMap.put(oldK + currentKey, bean1.get_64kFirstTimeSlot());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.get_64kFirstTimeSlot() != null)
			attributeMap.put(newK + currentKey, bean2.get_64kFirstTimeSlot());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "LastTimeSlot";
		if (bean1.get_64kLastTimeSlot() != null)
			attributeMap.put(oldK + currentKey, bean1.get_64kLastTimeSlot());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.get_64kLastTimeSlot() != null)
			attributeMap.put(newK + currentKey, bean2.get_64kLastTimeSlot());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		

		currentKey = "DlciValue";
		if (bean1.getDlciValue() != null)
			attributeMap.put(oldK + currentKey, bean1.getDlciValue());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getDlciValue() != null)
			attributeMap.put(newK + currentKey, bean2.getDlciValue());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		macdArgs.setAttributeMap(attributeMap);
		macdArgs.setVar(isDowntimeRequired);
		return macdArgs;

	}

	private MacdArgs checkDetailsSerialInterface(String oldK, String newK, String currentKey,
			ChannelizedE1serialInterfaceBean bean1, ChannelizedE1serialInterfaceBean bean2,
			Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) {

		MacdArgs macdArgs = new MacdArgs();
		Boolean isDowntimeRequired = false;
		
		if((bean1==null&&bean2!=null) || (bean1!=null && bean2==null)) {
			isDowntimeRequired=true;
			macdArgs.setVar(isDowntimeRequired);
			attributeMap.put("interfaceChangedSerial", "true");
			macdArgs.setAttributeMap(attributeMap);			
			return macdArgs;
		}
		
		if (bean1.getMode() != null)
			attributeMap.put(oldK + currentKey, bean1.getMode());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getMode() != null)
			attributeMap.put(newK + currentKey, bean2.getMode());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv4Address";
		if (bean1.getModifiedIpv4Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedIpv4Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedIpv4Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedIpv4Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv6Address";
		if (bean1.getModifiedIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedIpv6Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedIpv6Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv4Address2";
		if (bean1.getModifiedSecondaryIpv4Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedSecondaryIpv4Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedSecondaryIpv4Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedSecondaryIpv4Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv6Address2";
		if (bean1.getModifiedSecondaryIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedSecondaryIpv6Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedSecondaryIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedSecondaryIpv6Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "Serial-InterfaceName";
		if (bean1.getIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getInterfaceName());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getInterfaceName());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "PhysicalPort";
		if (bean1.getPhysicalPort() != null)
			attributeMap.put(oldK + currentKey, bean1.getPhysicalPort());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getPhysicalPort() != null)
			attributeMap.put(newK + currentKey, bean2.getPhysicalPort());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "FirstTimeSlot";
		if (bean1.getFirsttimeSlot() != null)
			attributeMap.put(oldK + currentKey, bean1.getFirsttimeSlot());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getFirsttimeSlot() != null)
			attributeMap.put(newK + currentKey, bean2.getFirsttimeSlot());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "FirstTimeSlot";
		if (bean1.getFirsttimeSlot() != null)
			attributeMap.put(oldK + currentKey, bean1.getFirsttimeSlot());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getFirsttimeSlot() != null)
			attributeMap.put(newK + currentKey, bean2.getFirsttimeSlot());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "LastTimeSlot";
		if (bean1.getLasttimeSlot() != null)
			attributeMap.put(oldK + currentKey, bean1.getLasttimeSlot());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getLasttimeSlot() != null)
			attributeMap.put(newK + currentKey, bean2.getLasttimeSlot());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		

		currentKey = "DlciValue";
		if (bean1.getDlciValue() != null)
			attributeMap.put(oldK + currentKey, bean1.getDlciValue());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getDlciValue() != null)
			attributeMap.put(newK + currentKey, bean2.getDlciValue());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		macdArgs.setAttributeMap(attributeMap);
		macdArgs.setVar(isDowntimeRequired);
		return macdArgs;
	}

	private MacdArgs checkDetailsEthernetInterface(String oldK, String newK, String currentKey,
			EthernetInterfaceBean bean1, EthernetInterfaceBean bean2, Map<String, String> attributeMap, List<DowntimeAttributeChange> attributesChange) {

		MacdArgs macdArgs = new MacdArgs();
		Boolean isDowntimeRequired = false;

		if((bean1==null&&bean2!=null) || (bean1!=null && bean2==null)) {
			isDowntimeRequired=true;
			macdArgs.setVar(isDowntimeRequired);
			attributeMap.put("interfaceChangedEthernet", "true");
			macdArgs.setAttributeMap(attributeMap);			
			return macdArgs;
		}
		
		if (bean1.getMode() != null)
			attributeMap.put(oldK + currentKey, bean1.getMode());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getMode() != null)
			attributeMap.put(newK + currentKey, bean2.getMode());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv4Address";
		if (bean1.getModifiedIpv4Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedIpv4Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedIpv4Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedIpv4Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv6Address";
		if (bean1.getModifiedIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedIpv6Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedIpv6Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv4Address2";
		if (bean1.getModifiedSecondaryIpv4Address() != null){
			attributeMap.put(oldK + currentKey, Objects.nonNull(bean1.getModifiedSecondaryIpv4Address())?bean1.getModifiedSecondaryIpv4Address():"null");
		}
		else{
			attributeMap.put(oldK + currentKey, "null");
		}
		if (bean2.getModifiedSecondaryIpv4Address() != null){
			attributeMap.put(newK + currentKey, Objects.nonNull(bean2.getModifiedSecondaryIpv4Address())?bean2.getModifiedSecondaryIpv4Address():"null");
		}
		else{
			attributeMap.put(newK + currentKey, "null");
		}
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "InterfaceIpv6Address2";
		if (bean1.getModifiedSecondaryIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getModifiedSecondaryIpv6Address());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getModifiedSecondaryIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getModifiedSecondaryIpv6Address());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "Ethernet-InterfaceName";
		if (bean1.getIpv6Address() != null)
			attributeMap.put(oldK + currentKey, bean1.getInterfaceName());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getIpv6Address() != null)
			attributeMap.put(newK + currentKey, bean2.getInterfaceName());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		currentKey = "PhysicalPort";
		if (bean1.getPhysicalPort() != null)
			attributeMap.put(oldK + currentKey, bean1.getPhysicalPort());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getPhysicalPort() != null)
			attributeMap.put(newK + currentKey, bean2.getPhysicalPort());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}
		
		currentKey = "InnerVlan";
		if (bean1.getInnerVlan() != null)
			attributeMap.put(oldK + currentKey, bean1.getInnerVlan());
		else
			attributeMap.put(oldK + currentKey, "null");
		if (bean2.getInnerVlan() != null)
			attributeMap.put(newK + currentKey, bean2.getInnerVlan());
		else
			attributeMap.put(newK + currentKey, "null");
		logger.info("values : newK:{} , oldK:{}, currentKey:{}, oldMapValue:{}, newMapValue:{}", newK, oldK, currentKey,
				attributeMap.get(oldK + currentKey), attributeMap.get(newK + currentKey));
		if (attributeMap.get(oldK + currentKey)!=null && !attributeMap.get(oldK + currentKey).equals(attributeMap.get(newK + currentKey))) {
			attributesChange.add(new DowntimeAttributeChange(currentKey,attributeMap.get(oldK + currentKey),attributeMap.get(newK + currentKey)));
			isDowntimeRequired = true;
		}

		macdArgs.setVar(isDowntimeRequired);
		macdArgs.setAttributeMap(attributeMap);
		
		return macdArgs;

	}

}
