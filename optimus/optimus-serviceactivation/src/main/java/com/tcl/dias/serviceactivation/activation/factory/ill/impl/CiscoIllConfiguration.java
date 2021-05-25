package com.tcl.dias.serviceactivation.activation.factory.ill.impl;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.factory.ill.base.AbstractIllConfiguration;
import com.tcl.dias.serviceactivation.activation.netp.beans.BGPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.EthernetInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixList;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixListEntry;
import com.tcl.dias.serviceactivation.activation.netp.beans.Router;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.WANInterface;
import com.tcl.dias.serviceactivation.activation.utils.ActivationUtils;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RegexAspathConfig;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;

/**
 * This file contains the CiscoIllConfiguration.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class CiscoIllConfiguration extends AbstractIllConfiguration {

	/**
	 * createEthernetInterface
	 * 
	 * @return
	 */
	@Override
	protected EthernetInterface createEthernetInterface(
			ServiceDetail serviceDetail,com.tcl.dias.serviceactivation.entity.entities.EthernetInterface ethernetInterfaceEntity, Router router,
			WANInterface wanInterface) {
		EthernetInterface ethernetInterface = super.createEthernetInterface(serviceDetail,ethernetInterfaceEntity, router,
				wanInterface);
		if (ethernetInterfaceEntity != null) {
			ethernetInterface.setQosLoopinPassthroughInterface(ethernetInterfaceEntity.getQosLoopinPassthrough());
			ethernetInterface.setMode(ethernetInterfaceEntity.getMode());
		}
		return ethernetInterface;
	}

	@Override
	protected BGPRoutingProtocol createWanRoutingBgpRoutingProtocol(Bgp bgp) {
		BGPRoutingProtocol bgpRoutingProtocol = super.createWanRoutingBgpRoutingProtocol(bgp);
		Set<PolicyType> policyTypes = bgp.getPolicyTypes();
		for (PolicyType policyType : policyTypes) {
			if (policyType.getInboundRoutingIpv4Policy() != null && policyType.getInboundRoutingIpv4Policy() == 1) {
				RoutingPolicy inRoutingPolicy = new RoutingPolicy();
				inRoutingPolicy.setIsPreprovisioned(
						ActivationUtils.getBooleanValue(policyType.getInboundIpv4Preprovisioned()));// TODO and the
				inRoutingPolicy.setName(policyType.getInboundIpv4PolicyName());
				Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
				for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
					Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
					PolicyCriteria policyCriterias = policyCriteriaRepository
							.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
					if (policyCriterias != null) {
						PrefixList inV4PrefixList = new PrefixList();
						if (StringUtils.isNotBlank(policyCriterias.getMatchcriteriaPrefixlistName()))
							inV4PrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
						inV4PrefixList.setIsPreprovisioned(ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
						if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() != null && !ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
							Set<PrefixlistConfig> prefixListConfigs = policyCriterias.getPrefixlistConfigs();
							for (PrefixlistConfig prefixConfig : prefixListConfigs) {
								PrefixListEntry prefixListEntry = new PrefixListEntry();
								prefixListEntry.setNetworkPrefix(prefixConfig.getNetworkPrefix()!=null ? prefixConfig.getNetworkPrefix().trim():prefixConfig.getNetworkPrefix());
								if(prefixConfig.getAction()!=null) {
									prefixListEntry.setOperator(prefixConfig.getAction().toLowerCase());
									}
								if (prefixConfig.getLeValue() != null)
									prefixListEntry.setSubnetRangeMaximum(
											ActivationUtils.toInteger(prefixConfig.getLeValue()));
								if (prefixConfig.getGeValue() != null)
									prefixListEntry.setSubnetRangeMinimum(
											ActivationUtils.toInteger(prefixConfig.getGeValue()));
								inV4PrefixList.getPrefixListEntry().add(prefixListEntry);
							}
							bgpRoutingProtocol.setInboundBGPv4PrefixesAllowed(inV4PrefixList);
						}
						if (policyCriterias.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaNeighbourCommunity())) {
							for (NeighbourCommunityConfig neighCommutity : policyCriterias
									.getNeighbourCommunityConfigs()) {
								bgpRoutingProtocol.getNeighbourCommunity().add(neighCommutity.getCommunity());
							}

						}
						if (policyCriterias.getMatchcriteriaRegexAspath() != null
								&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
							if (policyCriterias.getMatchcriteriaRegexAspath() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
								Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
								for (RegexAspathConfig asPathConfig : asPathConfigs) {
									if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
										bgpRoutingProtocol.setASPATH(asPathConfig.getAsPath());
									
								}
							}
						}
					}
				}

			}

			if (policyType.getInboundRoutingIpv6Policy() != null && policyType.getInboundRoutingIpv6Policy() == 1) {
				Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
				for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
					Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
					PolicyCriteria policyCriterias = policyCriteriaRepository
							.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
					if (policyCriterias != null) {
						PrefixList inV6PrefixList = new PrefixList();
						if (StringUtils.isNotBlank(policyCriterias.getMatchcriteriaPrefixlistName()))
							inV6PrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
						inV6PrefixList.setIsPreprovisioned(ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
						if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() != null && !ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
							Set<PrefixlistConfig> prefixListConfigs = policyCriterias.getPrefixlistConfigs();
							for (PrefixlistConfig prefixConfig : prefixListConfigs) {
								PrefixListEntry prefixListEntry = new PrefixListEntry();
								prefixListEntry.setNetworkPrefix(prefixConfig.getNetworkPrefix()!=null ?prefixConfig.getNetworkPrefix().trim():prefixConfig.getNetworkPrefix());
								prefixListEntry.setOperator(prefixConfig.getAction());// TODOprefixConfig.
								if (prefixConfig.getLeValue() != null)
									prefixListEntry.setSubnetRangeMaximum(
											ActivationUtils.toInteger(prefixConfig.getLeValue()));
								if (prefixConfig.getGeValue() != null)
									prefixListEntry.setSubnetRangeMinimum(
											ActivationUtils.toInteger(prefixConfig.getGeValue()));
								inV6PrefixList.getPrefixListEntry().add(prefixListEntry);// TODO
							}
							bgpRoutingProtocol.setInboundBGPv6PrefixesAllowed(inV6PrefixList);
						}
					}
				}

			}

			if (policyType.getOutboundRoutingIpv6Policy() != null && policyType.getOutboundRoutingIpv6Policy() == 1) {
				Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
				for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
					Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
					PolicyCriteria policyCriterias = policyCriteriaRepository
							.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
					if (policyCriterias != null) {
						PrefixList outBV6PrefixList = new PrefixList();
						if (StringUtils.isNotBlank(policyCriterias.getMatchcriteriaPrefixlistName()))
							outBV6PrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
						outBV6PrefixList.setIsPreprovisioned(ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
						if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() != null && !ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
							Set<PrefixlistConfig> prefixListConfigs = policyCriterias.getPrefixlistConfigs();
							for (PrefixlistConfig prefixConfig : prefixListConfigs) {
								PrefixListEntry prefixListEntry = new PrefixListEntry();
								prefixListEntry.setNetworkPrefix(prefixConfig.getNetworkPrefix()!=null ?prefixConfig.getNetworkPrefix().trim():prefixConfig.getNetworkPrefix());
								if(prefixConfig.getAction()!=null) {
									prefixListEntry.setOperator(prefixConfig.getAction().toLowerCase());
									}
								if (prefixConfig.getLeValue() != null)
									prefixListEntry.setSubnetRangeMaximum(
											ActivationUtils.toInteger(prefixConfig.getLeValue()));
								if (prefixConfig.getGeValue() != null)
									prefixListEntry.setSubnetRangeMinimum(
											ActivationUtils.toInteger(prefixConfig.getGeValue()));
								outBV6PrefixList.getPrefixListEntry().add(prefixListEntry);// TODO
							}
							bgpRoutingProtocol.setOutboundBGPv6PrefixesAllowed(outBV6PrefixList);
						}
					}
				}

			}

			if (policyType.getOutboundRoutingIpv4Policy() != null && policyType.getOutboundRoutingIpv4Policy() == 1) {
				Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
				for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
					Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
					PolicyCriteria policyCriterias = policyCriteriaRepository
							.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
					if (policyCriterias != null) {
						PrefixList outBV4PrefixList = new PrefixList();
						if (StringUtils.isNotBlank(policyCriterias.getMatchcriteriaPrefixlistName()))
							outBV4PrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
						outBV4PrefixList.setIsPreprovisioned(ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
						if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() != null && !ActivationUtils
								.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
							Set<PrefixlistConfig> prefixListConfigs = policyCriterias.getPrefixlistConfigs();
							for (PrefixlistConfig prefixConfig : prefixListConfigs) {
								PrefixListEntry prefixListEntry = new PrefixListEntry();
								prefixListEntry.setNetworkPrefix(prefixConfig.getNetworkPrefix()!=null ?prefixConfig.getNetworkPrefix().trim():prefixConfig.getNetworkPrefix());
								prefixListEntry.setOperator(prefixConfig.getAction());// TODOprefixConfig.
								if (prefixConfig.getLeValue() != null)
									prefixListEntry.setSubnetRangeMaximum(
											ActivationUtils.toInteger(prefixConfig.getLeValue()));
								if (prefixConfig.getGeValue() != null)
									prefixListEntry.setSubnetRangeMinimum(
											ActivationUtils.toInteger(prefixConfig.getGeValue()));
								outBV4PrefixList.getPrefixListEntry().add(prefixListEntry);
							}
							bgpRoutingProtocol.setOutboundBGPv6PrefixesAllowed(outBV4PrefixList);
						}
					}
				}

			}

			if (bgp.getBgpneighbourinboundv4routermapname() != null)
				bgpRoutingProtocol.setBGPNeighborInboundRouteMap(bgp.getBgpneighbourinboundv4routermapname());
			if (bgp.getBgpneighbourinboundv6routermapname() != null)
				bgpRoutingProtocol.setBGPNeighborInboundRouteMapV6(bgp.getBgpneighbourinboundv6routermapname());
			//if (bgp.getAsPath() != null)
				//bgpRoutingProtocol.setASPATH(bgp.getAsPath());
			//if (bgp.getNeighbourCommunity() != null)
				//bgpRoutingProtocol.getNeighbourCommunity().add(bgp.getNeighbourCommunity());
			if (bgp.getLocalPreference() != null)
				bgpRoutingProtocol.setLocalPreference(bgp.getLocalPreference());
		}

		return bgpRoutingProtocol;

	}

}
