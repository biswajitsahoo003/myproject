package com.tcl.dias.serviceactivation.rule.engine.macd.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.serviceactivation.activation.constants.AceConstants;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.entity.entities.AluSchedulerPolicy;
import com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.EthernetInterface;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.repository.AclPolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.AluSchedulerPolicyRepository;
import com.tcl.dias.serviceactivation.entity.repository.CambiumLastmileRepository;
import com.tcl.dias.serviceactivation.entity.repository.RadwinLastmileRepository;
import com.tcl.dias.serviceactivation.entity.repository.VrfRepository;
import com.tcl.dias.serviceactivation.entity.repository.WanStaticRouteRepository;
import com.tcl.dias.servicefulfillment.entity.entities.StateRegionMapping;
import com.tcl.dias.servicefulfillment.entity.repository.StageRegionMappingRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public abstract class AceBaseMACDRuleEngine {

	private static final Logger logger = LoggerFactory.getLogger(AceBaseMACDRuleEngine.class);

	@Autowired
	protected VrfRepository vrfRepository;

	@Autowired
	protected CambiumLastmileRepository cambiumLastmileRepository;

	@Autowired
	protected RadwinLastmileRepository radwinLastmileRepository;

	@Autowired
	protected AluSchedulerPolicyRepository aluSchedulerPolicyRepository;
	
	@Autowired
	protected AclPolicyCriteriaRepository aclPolicyCriteriaRepository;
	
	@Autowired
	protected WanStaticRouteRepository wanStaticRouteRepository;

	@Autowired
	protected StageRegionMappingRepository stageRegionMappingRepository;
	
	
	protected static final String MODIFIEDBY="OPTIMUS_RULE";

	
	protected void softDeleteEthernetPolicy(EthernetInterface ethernetInterface,ServiceDetail serviceDetail) {
		if (!ethernetInterface.getAclPolicyCriterias().isEmpty()) {
			ethernetInterface.getAclPolicyCriterias().forEach(policy -> {
				policy.setEndDate(new Timestamp(System.currentTimeMillis()));
				policy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				policy.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(policy);
			});
		}
	}
	
	protected void softDeleteChannelizedE1serialInterface(ChannelizedE1serialInterface channelizedE1serialInterface,
			ServiceDetail serviceDetail) {
		if (!channelizedE1serialInterface.getAclPolicyCriterias().isEmpty()) {
			channelizedE1serialInterface.getAclPolicyCriterias().forEach(policy -> {
				policy.setEndDate(new Timestamp(System.currentTimeMillis()));
				policy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				policy.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(policy);
			});
		}		
	}
	
	protected void softDeleteForChannelizedSdhInterface(ChannelizedSdhInterface channelizedSdhInterface,
			ServiceDetail serviceDetail) {
		if (!channelizedSdhInterface.getAclPolicyCriterias().isEmpty()) {
			channelizedSdhInterface.getAclPolicyCriterias().forEach(policy -> {
				policy.setEndDate(new Timestamp(System.currentTimeMillis()));
				policy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				policy.setModifiedBy(MODIFIEDBY);
				aclPolicyCriteriaRepository.save(policy);
			});
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

	protected Integer findNoMac(IpAddressDetail ipAddressDetail) {

		Integer macNo = 0;

		try {
			if (ipAddressDetail != null) {
				logger.info("findNoMac  started {}");

				if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {

					for (IpaddrLanv4Address ipaddrLanv4Address : ipAddressDetail.getIpaddrLanv4Addresses()) {
						String ipv4Address = ipaddrLanv4Address.getLanv4Address()!=null?ipaddrLanv4Address.getLanv4Address().trim():ipaddrLanv4Address.getLanv4Address();
						macNo = macNo + (int) (Math.pow(2, (32 - Integer.valueOf(subnet(ipv4Address)))) - 2);
					}
				}

				if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {

					for (IpaddrLanv6Address ipaddrLanv6Address : ipAddressDetail.getIpaddrLanv6Addresses()) {

						String ipv6Address = ipaddrLanv6Address.getLanv6Address()!=null?ipaddrLanv6Address.getLanv6Address().trim():ipaddrLanv6Address.getLanv6Address();
						macNo = macNo + (int) (Math.pow(2, (128 - Integer.valueOf(subnet(ipv6Address)))) - 2);

					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in findNoMac => {}", e);
		}

		return macNo;
	}

	protected String splitName(String name, Integer length) {

		if (name.length() >= length) {
			return name.substring(0, length);

		}

		return name;

	}
	
	protected void softDeleteWanRoutes(StaticProtocol staticProtocal) {

		if (staticProtocal!=null && !staticProtocal.getWanStaticRoutes().isEmpty()) {
			staticProtocal.getWanStaticRoutes().forEach(wantRoutes -> {

				wantRoutes.setEndDate(new Timestamp(System.currentTimeMillis()));
				wantRoutes.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				wantRoutes.setModifiedBy(MODIFIEDBY);
				wanStaticRouteRepository.save(wantRoutes);

			});
		}

	}

	public Map<String, String> findMandatoryValues(OrderDetail orderDetail, ServiceDetail serviceDetails) {

		Map<String, String> map = new HashMap<>();

		if (orderDetail.getAsdOpptyFlag() != null && orderDetail.getAsdOpptyFlag() == 1) {
			map.put(AceConstants.SERVICE.ASDFLAG, "true");
		}

		if (serviceDetails != null) {
			if (serviceDetails.getMgmtType() != null) {
				map.put(AceConstants.SERVICE.MANAGEMENTTYPE, serviceDetails.getMgmtType());
			}
			if (serviceDetails.getScopeOfMgmt() != null) {
				map.put(AceConstants.SERVICE.MANAGEMENTSCOPE, serviceDetails.getScopeOfMgmt());
			}
			String vrfName = findVrfDetails(serviceDetails);
			if (vrfName != null) {
				map.put(AceConstants.VRF.VRF_NAME, vrfName);
			}
			if (serviceDetails.getServiceType() != null) {
				map.put(AceConstants.SERVICE.SERVICE_TYPE, serviceDetails.getServiceType());
			}
			if (serviceDetails.getServiceId() != null) {
				map.put(AceConstants.SERVICE.SERVICEID, serviceDetails.getServiceId());
			}

			if (serviceDetails.getOrderDetail().getCustomerName() != null) {
				map.put(AceConstants.SERVICE.CUSTOMERNAME, serviceDetails.getOrderDetail().getCustomerName());
			}
			if (orderDetail.getOriginatorName() != null) {
				map.put(AceConstants.SERVICE.ORIGINATOR_NAME, orderDetail.getOriginatorName());
			}

			if (serviceDetails.getCssSammgrId() != null) {
				map.put(AceConstants.SERVICE.SAMMANAGERID, String.valueOf(serviceDetails.getCssSammgrId()));
			}
			map.put(AceConstants.SERVICE.REDUNDACY_ROLE, serviceDetails.getRedundancyRole());

			IpAddressDetail ipAddressDetail = serviceDetails.getIpAddressDetails().stream().findFirst().orElse(null);

			if (ipAddressDetail != null) {
				logger.info("IpAddressDetail exists for current Order");
				if (ipAddressDetail.getExtendedLanEnabled() != null) {
					map.put(AceConstants.IPADDRESS.EXTENDEDLANENABLED,
							ipAddressDetail.getExtendedLanEnabled() == 1 ? "TRUE" : "FALSE");
				}
				map.put(AceConstants.IPADDRESS.IPPATH, ipAddressDetail.getPathIpType());

				if (ipAddressDetail.getIpaddrLanv4Addresses() != null) {

					ipAddressDetail.getIpaddrLanv4Addresses().forEach(lanv4 -> {
						if (lanv4.getIssecondary() != null && lanv4.getIssecondary() == 0) {
							map.put(AceConstants.IPADDRESS.LANV4ADDRESS, lanv4.getLanv4Address()!=null ?lanv4.getLanv4Address().trim():lanv4.getLanv4Address());

						}

						if (lanv4.getIssecondary() != null && lanv4.getIssecondary() == 1) {
							map.put(AceConstants.IPADDRESS.LANV4ADDRESS_SECONDARY, lanv4.getLanv4Address()!=null ?lanv4.getLanv4Address().trim():lanv4.getLanv4Address());

						}
					});

				}
				if (ipAddressDetail.getIpaddrLanv6Addresses() != null) {

					ipAddressDetail.getIpaddrLanv6Addresses().forEach(lanv6 -> {
						if (lanv6.getIssecondary() != null && lanv6.getIssecondary() == 0) {
							map.put(AceConstants.IPADDRESS.LANV6ADDRESS, lanv6.getLanv6Address()!=null?lanv6.getLanv6Address().trim():lanv6.getLanv6Address());

						}

						if (lanv6.getIssecondary() != null && lanv6.getIssecondary() == 1) {
							map.put(AceConstants.IPADDRESS.LANV6ADDRESS_SECONDARY, lanv6.getLanv6Address()!=null?lanv6.getLanv6Address().trim():lanv6.getLanv6Address());

						}
					});
				}

				if (ipAddressDetail.getIpaddrWanv6Addresses() != null) {
					ipAddressDetail.getIpaddrWanv6Addresses().forEach(wan6 -> {
						if (wan6 != null) {
							if (wan6.getIssecondary() != null && wan6.getIssecondary() == 0) {
								map.put(AceConstants.IPADDRESS.WANV6ADDRESS, wan6.getWanv6Address());
							}

							if (wan6.getIssecondary() != null && wan6.getIssecondary() == 1) {
								map.put(AceConstants.IPADDRESS.WANV6ADDRESS_SECONDARY, wan6.getWanv6Address());
							}

						}
					});

				}

				if (ipAddressDetail.getIpaddrWanv4Addresses() != null) {
					ipAddressDetail.getIpaddrWanv4Addresses().forEach(wan4 -> {
						if (wan4 != null) {
							if (wan4.getIssecondary() != null && wan4.getIssecondary() == 0) {
								map.put(AceConstants.IPADDRESS.WANV4ADDRESS, wan4.getWanv4Address());
							}

							if (wan4.getIssecondary() != null && wan4.getIssecondary() == 1) {
								map.put(AceConstants.IPADDRESS.WANV4ADDRESS_SECONDARY, wan4.getWanv4Address());
							}

						}
					});

				}

			}

			List<InterfaceProtocolMapping> cpeProtocolMappings = serviceDetails
					.getInterfaceProtocolMappings().stream().filter(serIn -> serIn.getCpe() != null
							&& serIn.getIscpeWanInterface() != null && serIn.getIscpeWanInterface() == 1 && serIn.getEndDate()==null)
					.collect(Collectors.toList());

			if (!cpeProtocolMappings.isEmpty()) {
				cpeProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping cpeProtocolMapping = cpeProtocolMappings.get(0);

				if (cpeProtocolMapping != null) {

					if (findCpeInterfaceIpv4(cpeProtocolMapping) != null) {
						map.put(AceConstants.CPE.CPE_INTERFACE_IPV4, findCpeInterfaceIpv4(cpeProtocolMapping));

					}

					if (findCpeInterfaceIpv6(cpeProtocolMapping) != null) {
						{
							map.put(AceConstants.CPE.CPE_INTERFACE_IPV6, findCpeInterfaceIpv6(cpeProtocolMapping));

						}

					}
					
					if(findInterface(cpeProtocolMapping)!=null){
						map.put(AceConstants.CPE.CPE_INTERFACE_TYPE, findInterface(cpeProtocolMapping));
					}
				}
			}
			List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
					.stream().filter(serIn -> serIn.getRouterDetail() != null && serIn.getEndDate()==null).collect(Collectors.toList());
			if (!interfaceProtocolMappings.isEmpty()) {
				interfaceProtocolMappings.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
				InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings.get(0);
				logger.info("Router details mapping{}:", interfaceProtocolMapping);
				if (interfaceProtocolMapping != null) {

					if (interfaceProtocolMapping.getRouterDetail() != null) {
						map.put(AceConstants.ROUTER.ROUTER_MAKE,
								interfaceProtocolMapping.getRouterDetail().getRouterMake());
						logger.info("ROUTER_MAKEmapping{}:",
								interfaceProtocolMapping.getRouterDetail().getRouterMake());

					}
					if (interfaceProtocolMapping.getBgp() != null)

						map.put(AceConstants.PROTOCOL.ROUTINGPROTOCAL, AceConstants.PROTOCOL.BGP);

					if (interfaceProtocolMapping.getStaticProtocol() != null) {

						map.put(AceConstants.PROTOCOL.ROUTINGPROTOCAL, AceConstants.PROTOCOL.STATIC);

					}
					
					if(findInterface(interfaceProtocolMapping)!=null){
						map.put(AceConstants.ROUTER.ROUTER_INTERFACE_TYPE, findInterface(interfaceProtocolMapping));
					}
				}
			}
			
			if(serviceDetails.getLmComponents()!=null && !serviceDetails.getLmComponents().isEmpty()) {
				LmComponent lmComponent=	serviceDetails.getLmComponents().stream().findFirst().orElse(null);
				
				if(lmComponent!=null) {
					map.put(AceConstants.LMCOMPONENT.LMCOMPONENTTYPE, lmComponent.getLmOnwlProvider());

				}

			}
			

		}
		return map;

	}

	private String findInterface(InterfaceProtocolMapping cpeProtocolMapping) {
		if (cpeProtocolMapping.getChannelizedE1serialInterface() != null) {

			return AceConstants.DEFAULT.SERIAL;

		} else if (cpeProtocolMapping.getEthernetInterface() != null) {

			return AceConstants.DEFAULT.ETHERNET;

		} else if (cpeProtocolMapping.getChannelizedSdhInterface() != null) {

			return AceConstants.DEFAULT.SDH;

		}
		return null;
	}

	protected String findCpeInterfaceIpv4(InterfaceProtocolMapping cpeProtocolMapping) {

		if (cpeProtocolMapping.getChannelizedE1serialInterface() != null) {

			return cpeProtocolMapping.getChannelizedE1serialInterface().getIpv4Address();

		} else if (cpeProtocolMapping.getEthernetInterface() != null) {

			return cpeProtocolMapping.getEthernetInterface().getIpv4Address();

		} else if (cpeProtocolMapping.getChannelizedSdhInterface() != null) {

			return cpeProtocolMapping.getChannelizedSdhInterface().getIpv4Address();

		}

		return null;

	}

	protected String findCpeInterfaceIpv6(InterfaceProtocolMapping cpeProtocolMapping) {

		if (cpeProtocolMapping.getChannelizedE1serialInterface() != null) {

			return cpeProtocolMapping.getChannelizedE1serialInterface().getIpv6Address();

		} else if (cpeProtocolMapping.getEthernetInterface() != null) {

			return cpeProtocolMapping.getEthernetInterface().getIpv6Address();

		} else if (cpeProtocolMapping.getChannelizedSdhInterface() != null) {

			return cpeProtocolMapping.getChannelizedSdhInterface().getIpv6Address();

		}

		return null;

	}

	protected String findVrfDetails(ServiceDetail serviceDetails) {

		Vrf vrf = serviceDetails.getVrfs().stream().filter(vr->vr.getEndDate()==null).findFirst().orElse(null);
		if (vrf != null) {
			vrf.setIsvrfLiteEnabled((byte) 0);
			vrf.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
			vrf.setModifiedBy(MODIFIEDBY);
			vrfRepository.save(vrf);
			return vrf.getVrfName();
		}
		return null;
	}

	protected String findCpeInterfaceIpv6Ethernet(InterfaceProtocolMapping cpeProtocolMapping) {

		if (cpeProtocolMapping != null && cpeProtocolMapping.getEthernetInterface() != null) {

			return cpeProtocolMapping.getEthernetInterface().getIpv6Address();
		}
		return null;

	}

	protected String maxBanwidth(Float bandwidth, String bandwidthUnit, String burstableBandwidthUnit,
			Float burstableBandwidth) {

		try {
			logger.info("maxBanwidth  started {}");

			if (burstableBandwidth == null) {
				return (bandwidth) + "" + getUnit(bandwidthUnit);
			} else {

				if (!bandwidthUnit.equalsIgnoreCase(burstableBandwidthUnit)) {
					if (isServiceBandwidthMax(bandwidth, bandwidthUnit, burstableBandwidthUnit, burstableBandwidth)) {
						return convertValue(bandwidth) + "" + getUnit(bandwidthUnit);
					} else {
						return convertValue(burstableBandwidth) + "" + getUnit(burstableBandwidthUnit);

					}
				}

				else if (bandwidthUnit.equalsIgnoreCase(burstableBandwidthUnit)) {
					if (bandwidth > burstableBandwidth) {
						return convertValue(bandwidth) + "" + getUnit(bandwidthUnit);
					} else {

						return convertValue(burstableBandwidth) + "" + getUnit(bandwidthUnit);

					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception in maxBanwidth => {}", e);
		}
		return convertValue(bandwidth) + "" + getUnit(bandwidthUnit);

	}

	protected boolean isServiceBandwidthMax(Float bandwidth, String bandwidthUnit, String burstableBandwidthUnit,
			Float burstableBandwidth) {

		try {
			logger.info("isBandwidthMax  started {}");

			Float bandWidth = banwidthConversion(bandwidthUnit, bandwidth);
			Float burstable_Bandwidth = banwidthConversion(burstableBandwidthUnit, burstableBandwidth);

			if (bandWidth > burstable_Bandwidth) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Exception in isBandwidthMax => {}", e);
		}
		return false;
	}

	protected String getUnit(String bandwidthUnit) {

		try {
			logger.info("getUnit  started {}");

			if (bandwidthUnit != null) {
				if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.KBPS)) {
					return AceConstants.BANDWIDTHUNIT.KB;
				} else if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.MBPS)) {
					return AceConstants.BANDWIDTHUNIT.MB;
				} else if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.GBPS)) {
					return AceConstants.BANDWIDTHUNIT.GB;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getUnit => {}", e);
		}

		return bandwidthUnit;
	}

	protected Float maxBanwidth(Float bandwidth, Float burstableBandwidth) {
		return Float.max(burstableBandwidth, bandwidth);
	}

	protected Float banwidthConversion(String bandwidthUnit, Float bandwidth) {

		try {
			logger.info("banwidthConversion  started {}");

			if (bandwidth == null) {
				return 0F;
			}

			if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.MBPS)) {
				return bandwidth * 1024;
			} else if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.GBPS)) {

				return bandwidth * 1000 * 1024;

			}
			
			
		} catch (Exception e) {
			logger.error("Exception in banwidthConversion => {}", e); 
		}
		return bandwidth;

	}
	
	protected Float cambiumBandwidthConversion(String bandwidthUnit, Float bandwidth) {

		try {
			logger.info("cambiumBandwidthConversion  started {}");

			if (bandwidth == null) {
				return 0F;
			}

			if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.MBPS)) {
				return bandwidth * 1000;
			} else if (bandwidthUnit.equalsIgnoreCase(AceConstants.BANDWIDTHUNIT.GBPS)) {

				return bandwidth * 1000 * 1000;

			}
			
			
		} catch (Exception e) {
			logger.error("Exception in cambiumBandwidthConversion => {}", e); 
		}
		return bandwidth;

	}

	protected void applyALUpolicyRule(ServiceDetail serviceDetails, Map<String, String> mandatoryFields)
			throws TclCommonException {
		AluSchedulerPolicy aluSchedulerPolicy = null;
		try {

			if (mandatoryFields.containsKey(AceConstants.ROUTER.ROUTER_MAKE) && mandatoryFields
					.get(AceConstants.ROUTER.ROUTER_MAKE).equalsIgnoreCase(AceConstants.ROUTER.ROUTER_ALU)) {

				aluSchedulerPolicy = serviceDetails.getAluSchedulerPolicies().stream()
						.filter(alu -> alu.getEndDate() == null).findFirst().orElse(null);

				if (aluSchedulerPolicy == null) {
					aluSchedulerPolicy = new AluSchedulerPolicy();
				}

				ServiceQo serviceQo = serviceDetails.getServiceQos().stream().findFirst().orElse(null);

				String cosNameIn = "";
				String cosNameOut = "";

				if (serviceQo != null) {

					if (serviceQo.getQosTrafiicMode() != null
							&& serviceQo.getQosTrafiicMode().equalsIgnoreCase(AceConstants.COSPROFILE.UNICAST)) {

						if (serviceQo.getServiceCosCriterias().size() == 1) {
							ServiceCosCriteria serviceCosCriteria = serviceQo.getServiceCosCriterias().stream()
									.findFirst().orElse(null);

							if (serviceCosCriteria != null) {

								cosNameIn = "ACE_IN:" + serviceCosCriteria.getCosName().toUpperCase() + "_";
								cosNameOut = "ACE_OUT:" + serviceCosCriteria.getCosName().toUpperCase() + "_";
							}

						} else {

							if(serviceDetails.getServiceId().contains("091")){
								String serviceId=serviceDetails.getServiceId();
								serviceId=serviceId.substring(3);
								cosNameIn = "ACE_IN:" + serviceId + "_";
								cosNameOut = "ACE_OUT:" + serviceId + "_";
							}else{
								cosNameIn = "ACE_IN:" + serviceDetails.getServiceId() + "_";
								cosNameOut = "ACE_OUT:" + serviceDetails.getServiceId() + "_";
							}
							

						}
						aluSchedulerPolicy.setSchedulerPolicyName(AceConstants.ACE.ACE_
								+ maxBanwidth(serviceDetails.getServiceBandwidth(), serviceDetails.getServiceBandwidthUnit(),
										serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
					}

					else if (serviceQo.getQosTrafiicMode() != null
							&& (serviceQo.getQosTrafiicMode().equalsIgnoreCase("BOTH")|| serviceQo.getQosTrafiicMode().equalsIgnoreCase("MULTICAST"))) {

						if (serviceQo.getServiceCosCriterias().size() == 1) {
							ServiceCosCriteria serviceCosCriteria = serviceQo.getServiceCosCriterias().stream()
									.findFirst().orElse(null);

							if (serviceCosCriteria != null) {

								cosNameIn = "ACE_M_IN:" + serviceCosCriteria.getCosName().toUpperCase() + "_";
								cosNameOut = "ACE_M_OUT:" + serviceCosCriteria.getCosName().toUpperCase() + "_";
								
								aluSchedulerPolicy.setSchedulerPolicyName(AceConstants.ACE.ACE_M_ +serviceCosCriteria.getCosName().toUpperCase() + "_"
										+ maxBanwidth(serviceDetails.getServiceBandwidth(), serviceDetails.getServiceBandwidthUnit(),
												serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
							}

						} else {

							if(serviceDetails.getServiceId().contains("091")){
								String serviceId=serviceDetails.getServiceId();
								serviceId=serviceId.substring(3);
								cosNameIn = "ACE_M_IN:" + serviceId + "_";
								cosNameOut = "ACE_M_OUT:" + serviceId + "_";
								aluSchedulerPolicy.setSchedulerPolicyName(AceConstants.ACE.ACE_M_+serviceId+"_"
										+ maxBanwidth(serviceDetails.getServiceBandwidth(), serviceDetails.getServiceBandwidthUnit(),
												serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
							}else{
								cosNameIn = "ACE_M_IN:" + serviceDetails.getServiceId() + "_";
								cosNameOut = "ACE_M_OUT:" + serviceDetails.getServiceId() + "_";
								aluSchedulerPolicy.setSchedulerPolicyName(AceConstants.ACE.ACE_M_+serviceDetails.getServiceId()+"_"
										+ maxBanwidth(serviceDetails.getServiceBandwidth(), serviceDetails.getServiceBandwidthUnit(),
												serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
							}
						}

					}

				}
				logger.info("applyALUpolicyRule  started {}");

				aluSchedulerPolicy.setSchedulerPolicyIspreprovisioned((byte) 0);

				aluSchedulerPolicy.setTotalPirBw(convertValue(maxBanwidth(
						banwidthConversion(serviceDetails.getServiceBandwidthUnit(),
								serviceDetails.getServiceBandwidth()),
						banwidthConversion(serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()))));

				aluSchedulerPolicy.setTotalCirBw(aluSchedulerPolicy.getTotalPirBw());

				aluSchedulerPolicy.setSapIngressPolicyname(cosNameIn
						+ maxBanwidth(serviceDetails.getServiceBandwidth(), serviceDetails.getServiceBandwidthUnit(),
								serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
				aluSchedulerPolicy.setSapIngressPreprovisioned((byte) 0);
				aluSchedulerPolicy.setSapEgressPreprovisioned((byte) 0);

				aluSchedulerPolicy.setSapEgressPolicyname(cosNameOut
						+ maxBanwidth(serviceDetails.getServiceBandwidth(), serviceDetails.getServiceBandwidthUnit(),
								serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
				aluSchedulerPolicy.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				aluSchedulerPolicy.setModifiedBy(MODIFIEDBY);
				aluSchedulerPolicy.setServiceDetail(serviceDetails);
				aluSchedulerPolicyRepository.save(aluSchedulerPolicy);
			}
		} catch (Exception e) {
			logger.error("Exception in applyALUpolicyRule => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}
	
	private  String convertValue(float inputValue){
		
		try {
	   
	    if(Math.ceil(inputValue) == Math.floor(inputValue)){
	        return String.valueOf((int)inputValue);
	    }
	    return String.valueOf(inputValue);
		}
		catch (Exception e) {
			return String.valueOf(inputValue);
		}
	}

	protected void applyCambiumRule(ServiceDetail serviceDetails, Map<String, String> manDatoryFields,
			LmComponent lmComponent) {
		if (lmComponent != null && lmComponent.getCambiumLastmiles() != null) {

			CambiumLastmile cambiumLastmile = lmComponent.getCambiumLastmiles().stream().findFirst().orElse(null);
			if (cambiumLastmile != null) {
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

				if (cambiumLastmile.getMgmtVid() != null) {
					cambiumLastmile.setMappedVid1(cambiumLastmile.getMgmtVid());
				}
				cambiumLastmile.setSuMacAddress(Objects.nonNull(cambiumLastmile.getSuMacAddress()) && !cambiumLastmile.getSuMacAddress().isEmpty()?cambiumLastmile.getSuMacAddress().toLowerCase().replaceAll(":", " "):cambiumLastmile.getSuMacAddress());
				cambiumLastmile.setIdentity(Objects.nonNull(cambiumLastmile.getSuMacAddress()) && !cambiumLastmile.getSuMacAddress().isEmpty()?cambiumLastmile.getSuMacAddress().replaceAll(" ", "-"):cambiumLastmile.getSuMacAddress());
				Float maxBandWidth = maxBanwidth(
						cambiumBandwidthConversion(serviceDetails.getServiceBandwidthUnit(),
								serviceDetails.getServiceBandwidth()),
						cambiumBandwidthConversion(serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));

				cambiumLastmile.setPortSpeedUnit("kbps");

				cambiumLastmile.setPortSpeed(maxBandWidth);
				cambiumLastmile.setSiteName(constructSiteNameForCambium(serviceDetails, cambiumLastmile));
				cambiumLastmile.setSiteContact(constructSiteContactCambium(serviceDetails, cambiumLastmile, maxBandWidth));
				cambiumLastmile.setSiteLocation(constructSiteLocationForCambium(serviceDetails, cambiumLastmile, maxBandWidth));

				if (cambiumLastmile.getPortSpeed() != null) {
					cambiumLastmile.setBwDownlinkSustainedRate(convertValue(cambiumLastmile.getPortSpeed()));
					cambiumLastmile.setBwUplinkSustainedRate(convertValue(cambiumLastmile.getPortSpeed()));
					cambiumLastmile.setUplinkBurstAllocation(convertValue(cambiumLastmile.getPortSpeed()));
					cambiumLastmile.setDownlinkBurstAllocation(convertValue(cambiumLastmile.getPortSpeed()));
				}
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
				

				if (deviceType != null && deviceType.contains("450")) {
				cambiumLastmile.setDownlinkPlan(cambiumLastmile.getHipriorityUplinkCir());
				cambiumLastmile.setUplinkPlan(cambiumLastmile.getHipriorityUplinkCir());
				cambiumLastmile.setWeight("0.0");
				cambiumLastmile.setUserLockModulation("Disable");
				cambiumLastmile.setLockModulation("Disable"); //Disable
				cambiumLastmile.setThersholdModulation("Disable"); //Disable
				cambiumLastmile.setPrioritizationGroup("Low");
				}


				cambiumLastmile.setModifiedBy(MODIFIEDBY);
				cambiumLastmile.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				cambiumLastmileRepository.save(cambiumLastmile);

			}

		}
	}
	
	private String constructSiteLocationForCambium(ServiceDetail serviceDetails, CambiumLastmile radwinLastmile,
			Float maxBandWidth) {
		String siteLocation = "";
		
		if(serviceDetails.getAddressLine1()!=null) {

		return siteLocation = serviceDetails.getAddressLine1()+","+serviceDetails.getCity()+"-"+serviceDetails.getState();
		}

		return siteLocation;
	}

	private String constructSiteNameForCambium(ServiceDetail serviceDetail, CambiumLastmile cambiumLastmile) {
		String name = "";
		StateRegionMapping stateRegionMappin = stageRegionMappingRepository.findByStateLike(serviceDetail.getState());

		if (stateRegionMappin != null) {
			name = stateRegionMappin.getRegion() + "_" + serviceDetail.getCity() + "-" + cambiumLastmile.getBsName()
					+ "-" + serviceDetail.getOrderDetail().getCustomerName().replaceAll(" ","_") + "-" + cambiumLastmile.getMgmtIpForSsSu() + "-"+serviceDetail.getServiceId();
					
		}
		return name;
	}

	private String constructSiteContactCambium(ServiceDetail serviceDetail, CambiumLastmile cambiumLastmile,
			Float maxBandWidth) {
		String name = "";
		if(serviceDetail!=null) {
			name=serviceDetail.getServiceId()+"-"+constructQos(serviceDetail,maxBandWidth);
		}
	
		return name;
	}
	
	private String constructQos(ServiceDetail serviceDetail, Float maxBandWidth) {


		return "L3_" + serviceDetail.getServiceType() + "_" + convertValue(maxBandWidth) + "_KBPS";

	}
 

	protected String heaxDecimalConversion(String processId) {

		if (processId == null) {
			return processId;
		}
		return Integer.toHexString(Integer.valueOf(processId));
	}

	protected void applyRadwinRule(LmComponent lmComponent, Map<String, String> manDatoryFields,
			ServiceDetail serviceDetails) {
		if (lmComponent.getRadwinLastmiles() != null && !lmComponent.getRadwinLastmiles().isEmpty()) {
			RadwinLastmile radwinLastmile = lmComponent.getRadwinLastmiles().stream().findFirst().orElse(null);

			if (radwinLastmile != null) {
				radwinLastmile.setAllowedVlanid(radwinLastmile.getDataVlanid());
				Float maxBandWidth = maxBanwidth(
						banwidthConversion(serviceDetails.getServiceBandwidthUnit(),
								serviceDetails.getServiceBandwidth()),
						banwidthConversion(serviceDetails.getBurstableBwUnit(), serviceDetails.getBurstableBw()));
				radwinLastmile.setMirUl(maxBandWidth);
				radwinLastmile.setMirDl(maxBandWidth);
				radwinLastmile.setSiteName(constructSiteNameForRadwin(serviceDetails, radwinLastmile));
				radwinLastmile.setSiteContact(constructSiteContactRadwin(serviceDetails, radwinLastmile,maxBandWidth));
				radwinLastmile.setCustomerLocation(constructSiteLocationForRadwin(serviceDetails,radwinLastmile,maxBandWidth));
				radwinLastmile.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
				radwinLastmile.setNtpServerIp("192.168.199.228");
				radwinLastmile.setNtpOffset("330");
				radwinLastmile.setProtocolSnmp("V1");
				radwinLastmile.setProtocolWebinterface("true");
				radwinLastmile.setProtocolTelnet("true");
				radwinLastmile.setReqd_tx_power("21");
				radwinLastmile.setEthernet_Port_Config("100 Mbps/Full Duplex");
				radwinLastmile.setVlanMode("Tag");
				radwinLastmile.setDataVlanPriority("2");
				radwinLastmile.setHsuIngressTraffic("TAG");
				radwinLastmile.setHsuEgressTraffic("UNTAGFILTERED");
				radwinLastmile.setUntagVlanId((byte) 1);
				radwinLastmile.setModifiedBy(MODIFIEDBY);
				radwinLastmileRepository.save(radwinLastmile);

			}

		}
	}

	private String constructSiteLocationForRadwin(ServiceDetail serviceDetails, RadwinLastmile radwinLastmile,
			Float maxBandWidth) {
		String siteLocation = "";

		//siteLocation = serviceDetails.getCity() + "_" + maxBandWidth + "KBPS";
		siteLocation = serviceDetails.getServiceId();

		return siteLocation;
	}

	private String constructSiteNameForRadwin(ServiceDetail serviceDetail, RadwinLastmile radwinLastmile) {
		String name = "";
		name = serviceDetail.getServiceId() + "_"
				+ serviceDetail.getOrderDetail().getCustomerName().replaceAll(" ", "_");
		if (name != null && name.length() > 30) {
			return name.substring(0, 30);
		}
		return name;
	}

	private String constructSiteContactRadwin(ServiceDetail serviceDetail, RadwinLastmile radwinLastmile,
			Float maxBandWidth) {
		String name = "";
		StateRegionMapping stateRegionMappin = stageRegionMappingRepository.findByState(serviceDetail.getState());

		if (stateRegionMappin != null) {
			radwinLastmile.setRegion(stateRegionMappin.getRegion());
			/*name = stateRegionMappin.getRegion() + "_" + serviceDetail.getCity() + "_" + radwinLastmile.getBtsName()
					+ "_" + radwinLastmile.getSectorId() + "_" + radwinLastmile.getHsuIp() + "_"
					+ serviceDetail.getOrderDetail().getCustomerName().replaceAll(" ", "_") + "_" + maxBandWidth+"KBPS" + "_"
					+ serviceDetail.getLat() + ";" + serviceDetail.getLongiTude();
			
			name = serviceDetail.getServiceId() + "_" + serviceDetail.getServiceType() + "_"
					+ serviceDetail.getOrderDetail().getCustomerName().replaceAll(" ", "_") + "_" + maxBandWidth
					+ "KBPS";*/
			name = serviceDetail.getServiceId() + "_" + serviceDetail.getServiceType() + "_"+ convertValue(maxBandWidth)
					+ "KBPS";
			if(name.length()>30){
				name=name.substring(0, 30);
			}
		}
		return name;
	}
	
	
	protected String splitCustomerName(String customerName) {
		StringBuilder buffer = new StringBuilder();

		AtomicInteger atomicInteger = new AtomicInteger(0);
		if(customerName.length()<=3) {
			return customerName.replaceAll(" ", "_");
		}else {
			List<String> customerarr = Pattern.compile(" ").splitAsStream(customerName).limit(3)
					.collect(Collectors.toList());

			if(customerarr.size()==1){

				buffer.append(customerarr.get(0).length()<=10 ? customerarr.get(0) : customerarr.get(0).substring(0,10));
			}
			else {

				customerarr.forEach(cust -> {
					atomicInteger.incrementAndGet();

					if (atomicInteger.get() == 3) {
						buffer.append(cust.length() >= 4 ? (cust.substring(0, 4) + " ") : cust);
					} else {
						if (cust.length() > 7) {
							buffer.append(cust.substring(0, 6) + " ");

						} else {
							buffer.append(cust + " ");
						}
					}

				});
			}
			return buffer.toString().trim().replaceAll(" ", "_");
		}
	}
	
	//6,3 only if project na and 4,2
	
	protected String splitProjectName(String customerName,String projectName) {

		if(projectName==null || projectName.isEmpty()){
			return customerName;
		}

		StringBuilder buffer = new StringBuilder();
		AtomicInteger atomicInteger = new AtomicInteger(0);

		buffer.append(customerName + "_");

		if(projectName.length()<=4){
			buffer.append(projectName.replaceAll(" ", "_"));
		}
		else{
			List<String> projectArr = Pattern.compile(" ").splitAsStream(projectName).limit(3)
					.collect(Collectors.toList());

			projectArr.forEach(project->{
				atomicInteger.incrementAndGet();

				if(atomicInteger.get()==1){
					buffer.append(project.length()>=4 ? project.substring(0,4) : project).append("_");
				}
				else if(atomicInteger.get()==2){
					buffer.append(project.length()>=2 ? project.substring(0,2) : project);
				}
			});
		}

		return buffer.toString().trim().replaceAll(" ", "_");
	}
	
	protected  String getIpAddressSplit(String ipAddress,int count){
		
		String array[]=ipAddress.split("/");
		String ssMgmtIp=array[0];
		String[] splitValue =null;
		if(ssMgmtIp.contains(":"))splitValue = ssMgmtIp.split(":");
		else splitValue = ssMgmtIp.split("\\.");
	    splitValue[splitValue.length-1] = String.valueOf(Integer.parseInt(splitValue[splitValue.length-1])+count);
	    String output = Arrays.asList(splitValue).stream().map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));
	    return output;
	}
	
}
