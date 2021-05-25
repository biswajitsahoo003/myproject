package com.tcl.dias.serviceactivation.activation.factory.ill.impl;

import static org.hamcrest.CoreMatchers.allOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.factory.ill.base.AbstractIllConfiguration;
import com.tcl.dias.serviceactivation.activation.netp.beans.ALUASPathConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.AccessControlList;
import com.tcl.dias.serviceactivation.activation.netp.beans.BGPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.ChannelizedSDHInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.EthernetInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixList;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixListEntry;
import com.tcl.dias.serviceactivation.activation.netp.beans.Router;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicyMatchCriteria;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicyNeighbourCommunity;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicySetCriteria;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicyTerm;
import com.tcl.dias.serviceactivation.activation.netp.beans.SerialInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.WANInterface;
import com.tcl.dias.serviceactivation.activation.utils.ActivationUtils;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PolicycriteriaProtocol;
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
public class AluIllConfiguration extends AbstractIllConfiguration {

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
			ethernetInterface.setEncapsulation(ethernetInterfaceEntity.getEncapsulation());
			ethernetInterface.setMode(ethernetInterfaceEntity.getMode());
			AccessControlList inBoundV6AccessControlList = new AccessControlList();
			AccessControlList outBoundV6AccessControlList = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = ethernetInterfaceEntity.getAclPolicyCriterias();
			if (aclPolicyCriterias != null) {
				for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
					if(aclPolicyCriteria.getEndDate()==null) {
					if (aclPolicyCriteria.getInboundIpv6AclName() != null) {// TODO
						inBoundV6AccessControlList.setName(aclPolicyCriteria.getInboundIpv6AclName());
						ethernetInterface.setInboundAccessControlListV6(inBoundV6AccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundV6AccessControlList.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						ethernetInterface.setOutboundAccessControlListV6(outBoundV6AccessControlList);
					}
				}
				}
			}
		}
		return ethernetInterface;
	}

	/**
	 * createWanInterfaceCE1Serial
	 * 
	 * @return
	 */
	@Override
	protected SerialInterface createWanInterfaceCE1Serial(ChannelizedE1serialInterface channelizedE1serialInterface,
			Router router) {
		SerialInterface serialInfe = super.createWanInterfaceCE1Serial(channelizedE1serialInterface, router);
		if (channelizedE1serialInterface != null) {
			AccessControlList inBoundV6AccessControlList = new AccessControlList();
			AccessControlList outBoundV6AccessControlList = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = channelizedE1serialInterface.getAclPolicyCriterias();
			if (aclPolicyCriterias != null) {
				for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
					if(aclPolicyCriteria.getEndDate()==null) {
					if (aclPolicyCriteria.getInboundIpv6AclName() != null) {// TODO
						inBoundV6AccessControlList.setName(aclPolicyCriteria.getInboundIpv6AclName());
						serialInfe.setInboundAccessControlListV6(inBoundV6AccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundV6AccessControlList.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						serialInfe.setOutboundAccessControlListV6(outBoundV6AccessControlList);
					}
				}
				}
			}

		}
		return serialInfe;
	}

	/**
	 * createWanInterfaceCSdh
	 * 
	 * @return
	 */
	@Override
	protected ChannelizedSDHInterface createWanInterfaceCSdh(ChannelizedSdhInterface channelizedSdhInterface,
			Router router) {
		ChannelizedSDHInterface channalizedSdhIfe = super.createWanInterfaceCSdh(channelizedSdhInterface, router);
		if (channalizedSdhIfe != null && channelizedSdhInterface != null) {
			AccessControlList inBoundV6AccessControlList = new AccessControlList();
			AccessControlList outBoundV6AccessControlList = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = channelizedSdhInterface.getAclPolicyCriterias();
			if (aclPolicyCriterias != null) {
				for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
					if(aclPolicyCriteria.getEndDate()==null) {
					if (aclPolicyCriteria.getInboundIpv6AclName() != null) {// TODO
						inBoundV6AccessControlList.setName(aclPolicyCriteria.getInboundIpv6AclName());
						channalizedSdhIfe.setInboundAccessControlListV6(inBoundV6AccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundV6AccessControlList.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						channalizedSdhIfe.setOutboundAccessControlListV6(outBoundV6AccessControlList);
					}
				}
				}
			}

		}
		return channalizedSdhIfe;
	}

	/**
	 * createWanRoutingBgpRoutingProtocol
	 * 
	 * @return
	 */
	@Override
	protected BGPRoutingProtocol createWanRoutingBgpRoutingProtocol(Bgp bgp) {
		BGPRoutingProtocol bgpRoutingProtocol = super.createWanRoutingBgpRoutingProtocol(bgp);
		if (bgp.getBgpPeerName() != null)
			bgpRoutingProtocol.setALUBGPPeerName(bgp.getBgpPeerName());
		if (bgp.getAluBackupPath() != null)
			bgpRoutingProtocol.setALUBackupPath(bgp.getAluBackupPath().toUpperCase());
		if (bgp.getAluDisableBgpPeerGrpExtCommunity() != null && bgp.getAluDisableBgpPeerGrpExtCommunity() == 1) {
			bgpRoutingProtocol.setALUDisableBGPPeerGrpExtCommunity(true);

		} else {
			bgpRoutingProtocol.setALUDisableBGPPeerGrpExtCommunity(false);

		}
		if (bgp.getMedValue() != null)
			bgpRoutingProtocol.setALUBGPMEDValue(bgp.getMedValue());
		if (bgp.getPeerType() != null)
			bgpRoutingProtocol.setALUBGPPeerType(bgp.getPeerType().toLowerCase());
		if (bgp.getAluKeepalive() != null)
			bgpRoutingProtocol.setALUkeepAlive(bgp.getAluKeepalive());
		Set<PolicyType> policyTypes = bgp.getPolicyTypes();
		for (PolicyType policyType : policyTypes) {
			if (policyType.getInboundRoutingIpv4Policy() != null && policyType.getInboundRoutingIpv4Policy() == 1) {
				RoutingPolicy inRoutingPolicy = new RoutingPolicy();
				inRoutingPolicy
						.setIsStandardPolicy(ActivationUtils.getBooleanValue(policyType.getInboundIstandardpolicyv4()));
				inRoutingPolicy.setIsPreprovisioned(
						ActivationUtils.getBooleanValue(policyType.getInboundIpv4Preprovisioned()));// TODO and the
				RoutingPolicyMatchCriteria routingPolicyMatchCriteria = null;; // below
				RoutingPolicySetCriteria inRoutingPolicySetCriteria =null;;
				inRoutingPolicy.setName(policyType.getInboundIpv4PolicyName());
				if (policyType.getInboundIpv4Preprovisioned() != null
						&& !ActivationUtils.getBooleanValue(policyType.getInboundIpv4Preprovisioned())) {
					Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
					List<Integer> policyCriteriaIds = new ArrayList<Integer>();
					policyTypeCriteria.forEach(b -> {
						policyCriteriaIds.add(b.getPolicyCriteriaId());
					});

					if (!policyCriteriaIds.isEmpty()) {
						List<PolicyCriteria> policyCriteriasCheck = policyCriteriaRepository
								.findByPolicyCriteriaIdInAndEndDateIsNull(policyCriteriaIds);

						if (policyCriteriasCheck.stream().anyMatch(cri->
								 cri.getMatchcriteriaPrefixlist() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaPrefixlist())
								|| cri.getMatchcriteriaProtocol() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaProtocol())
								|| cri.getMatchcriteriaRegexAspath() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) || cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
												.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {

							routingPolicyMatchCriteria = new RoutingPolicyMatchCriteria();
							inRoutingPolicy.setMatchCriteria(routingPolicyMatchCriteria);

							

						}
						
						if (policyCriteriasCheck.stream()
								.anyMatch(cri -> cri.getSetcriteriaLocalPreference() != null && ActivationUtils
										.getBooleanValue(cri.getSetcriteriaLocalPreference()) || cri.getSetcriteriaAspathPrepend() != null
												&& ActivationUtils.getBooleanValue(cri.getSetcriteriaAspathPrepend()) || cri.getSetcriteriaMetric() != null
														&& ActivationUtils.getBooleanValue(cri.getSetcriteriaMetric()) || cri.getSetcriteriaRegexAspath() != null
																&& ActivationUtils.getBooleanValue(cri.getSetcriteriaRegexAspath())||  cri.getMatchcriteriaRegexAspath() != null
																		&& ActivationUtils.getBooleanValue(cri.getSetcriteriaRegexAspath()) 
																		|| cri.getSetcriteriaNeighbourCommunity()!=null && ActivationUtils.getBooleanValue(cri.getSetcriteriaNeighbourCommunity())
																		|| cri.getSetcriteriaNeighbourCommunity() != null && ActivationUtils
																		.getBooleanValue(cri.getSetcriteriaNeighbourCommunity()))) {
							inRoutingPolicySetCriteria = new RoutingPolicySetCriteria();
							inRoutingPolicy.setSetCriteria(inRoutingPolicySetCriteria);

						}
					}
					for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
						Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCriterias = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCriterias != null) {
							/*
							 * if (policyCriterias.getTermSetcriteriaAction() != null &&
							 * inRoutingPolicy.getRoutingPolicyTerm().size() <= 0) { RoutingPolicyTerm
							 * routingPolicyTerm = new RoutingPolicyTerm();
							 * routingPolicyTerm.setTermAction(policyCriterias.getTermSetcriteriaAction());
							 * routingPolicyTerm.setId(policyCriterias.getTermName());
							 * inRoutingPolicy.getRoutingPolicyTerm().add(routingPolicyTerm); }
							 */
							if (policyCriterias.getMatchcriteriaPrefixlist() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaPrefixlist())) {
								PrefixList outPrefixList = new PrefixList();
								outPrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
								outPrefixList.setIsPreprovisioned(ActivationUtils
										.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
								if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() == null
										|| !ActivationUtils.getBooleanValue(
												policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
									Set<PrefixlistConfig> prefixListConfigs = policyCriterias.getPrefixlistConfigs();
									for (PrefixlistConfig prefixConfig : prefixListConfigs) {
										PrefixListEntry prefixListEntry = new PrefixListEntry();
										prefixListEntry.setNetworkPrefix(prefixConfig.getNetworkPrefix()!=null ? prefixConfig.getNetworkPrefix().trim():prefixConfig.getNetworkPrefix());
										prefixListEntry.setOperator(prefixConfig.getAction().toLowerCase());
										if (prefixConfig.getLeValue() != null)
											prefixListEntry.setSubnetRangeMaximum(
													ActivationUtils.toInteger(prefixConfig.getLeValue()));
										if (prefixConfig.getGeValue() != null)
											prefixListEntry.setSubnetRangeMinimum(
													ActivationUtils.toInteger(prefixConfig.getGeValue()));
										outPrefixList.getPrefixListEntry().add(prefixListEntry);// TODO
									}
									routingPolicyMatchCriteria.setPrefixList(outPrefixList);
								}
							}
							if (policyCriterias.getMatchcriteriaProtocol() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaProtocol())) {
								for (PolicycriteriaProtocol policycriteriaProtocol : policyCriterias
										.getPolicycriteriaProtocols()) {
									routingPolicyMatchCriteria.setProtocol(policycriteriaProtocol.getProtocolName());
								}

							}

							if (policyCriterias.getMatchcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (policyCriterias.getMatchcriteriaRegexAspath() != null && ActivationUtils
										.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
								}
								routingPolicyMatchCriteria.setRegexASPath(aluPathConfig);

								// TODO - protocol
							}

							if (policyCriterias.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getMatchcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									routingPolicyMatchCriteria.getNeighbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}

							}

							if (policyCriterias.getSetcriteriaLocalPreference() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaLocalPreference())) {
								inRoutingPolicySetCriteria
										.setLocalPreference(policyCriterias.getSetcriteriaLocalpreferenceName());

							}

							if (policyCriterias.getSetcriteriaAspathPrepend() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaAspathPrepend())) {
								inRoutingPolicySetCriteria
										.setASPathPrepend(policyCriterias.getSetcriteriaAspathPrependName());
								if (policyCriterias.getSetcriteriaAspathPrependIndex() != null)
									inRoutingPolicySetCriteria.setASPathPrependIndex(ActivationUtils
											.toInteger(policyCriterias.getSetcriteriaAspathPrependIndex()));

							}

							if (policyCriterias.getSetcriteriaMetric() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaMetric())) {
								inRoutingPolicySetCriteria.setMetric(policyCriterias.getSetcriteriaMetricName());

							}

							if (policyCriterias.getSetcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (policyCriterias.getSetcriteriaRegexAspath() != null && ActivationUtils
										.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
									inRoutingPolicySetCriteria.setRegexASPath(aluPathConfig);

								}
							}

							if (policyCriterias.getSetcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									inRoutingPolicySetCriteria.getNeightbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}
							}

						}
					}
				}
				bgpRoutingProtocol.getALUBGPPeerGroupInboundRoutingPolicy().add(inRoutingPolicy);
			}

			if (policyType.getInboundRoutingIpv6Policy() != null && policyType.getInboundRoutingIpv6Policy() == 1) {
				RoutingPolicy inRoutingPolicy = new RoutingPolicy();
				inRoutingPolicy
						.setIsStandardPolicy(ActivationUtils.getBooleanValue(policyType.getInboundIstandardpolicyv6()));
				inRoutingPolicy.setIsPreprovisioned(
						ActivationUtils.getBooleanValue(policyType.getInboundIpv6Preprovisioned()));// TODO and the
			
				inRoutingPolicy.setName(policyType.getInboundIpv6PolicyName());
				if (policyType.getInboundIpv6Preprovisioned() == null
						|| !ActivationUtils.getBooleanValue(policyType.getInboundIpv6Preprovisioned())) {
					RoutingPolicyMatchCriteria routingPolicyMatchCriteria = null;; // below
					RoutingPolicySetCriteria inRoutingPolicySetCriteria = null;
					Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
					
					List<Integer> policyCriteriaIds = new ArrayList<Integer>();
					policyTypeCriteria.forEach(b -> {
						policyCriteriaIds.add(b.getPolicyCriteriaId());
					});

					if (!policyCriteriaIds.isEmpty()) {
						List<PolicyCriteria> policyCriteriasCheck = policyCriteriaRepository
								.findByPolicyCriteriaIdInAndEndDateIsNull(policyCriteriaIds);

						if (policyCriteriasCheck.stream().anyMatch(cri->
								 cri.getMatchcriteriaPrefixlist() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaPrefixlist())
								|| cri.getMatchcriteriaProtocol() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaProtocol())
								|| cri.getMatchcriteriaRegexAspath() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) || cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
												.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {

							routingPolicyMatchCriteria = new RoutingPolicyMatchCriteria();
							inRoutingPolicy.setMatchCriteria(routingPolicyMatchCriteria);

							

						}
						
						if (policyCriteriasCheck.stream()
								.anyMatch(cri -> cri.getSetcriteriaLocalPreference() != null && ActivationUtils
										.getBooleanValue(cri.getSetcriteriaLocalPreference()) || cri.getSetcriteriaAspathPrepend() != null
												&& ActivationUtils.getBooleanValue(cri.getSetcriteriaAspathPrepend()) || cri.getSetcriteriaMetric() != null
														&& ActivationUtils.getBooleanValue(cri.getSetcriteriaMetric()) || cri.getSetcriteriaRegexAspath() != null
																&& ActivationUtils.getBooleanValue(cri.getSetcriteriaRegexAspath())||  cri.getMatchcriteriaRegexAspath() != null
																		&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) || cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
																		.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {
							inRoutingPolicySetCriteria = new RoutingPolicySetCriteria();
							inRoutingPolicy.setSetCriteria(inRoutingPolicySetCriteria);

						}
					}
					for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
						Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCriterias = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCriterias != null) {
							if (policyCriterias.getTermSetcriteriaAction() != null
									&& inRoutingPolicy.getRoutingPolicyTerm().size() <= 0) {
								RoutingPolicyTerm routingPolicyTerm = new RoutingPolicyTerm();
								routingPolicyTerm.setTermAction(policyCriterias.getTermSetcriteriaAction());
								routingPolicyTerm.setId(policyCriterias.getTermName());
								inRoutingPolicy.getRoutingPolicyTerm().add(routingPolicyTerm);
							}
							if (policyCriterias.getMatchcriteriaPrefixlist() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaPrefixlist())) {
								PrefixList outPrefixList = new PrefixList();
								outPrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
								outPrefixList.setIsPreprovisioned(ActivationUtils
										.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
								if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() == null
										|| !ActivationUtils.getBooleanValue(
												policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
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
										outPrefixList.getPrefixListEntry().add(prefixListEntry);// TODO
									}
									routingPolicyMatchCriteria.setPrefixList(outPrefixList);
								}
							}
							if (policyCriterias.getMatchcriteriaProtocol() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaProtocol())) {
								for (PolicycriteriaProtocol policycriteriaProtocol : policyCriterias
										.getPolicycriteriaProtocols()) {
									routingPolicyMatchCriteria.setProtocol(policycriteriaProtocol.getProtocolName());
								}

							}

							if (policyCriterias.getMatchcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (policyCriterias.getMatchcriteriaRegexAspath() != null && ActivationUtils
										.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
								}
								routingPolicyMatchCriteria.setRegexASPath(aluPathConfig);

								// TODO - protocol
							}

							if (policyCriterias.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getMatchcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									routingPolicyMatchCriteria.getNeighbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}

							}

							if (policyCriterias.getSetcriteriaLocalPreference() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaLocalPreference())) {
								inRoutingPolicySetCriteria
										.setLocalPreference(policyCriterias.getSetcriteriaLocalpreferenceName());

							}

							if (policyCriterias.getSetcriteriaAspathPrepend() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaAspathPrepend())) {
								inRoutingPolicySetCriteria
										.setASPathPrepend(policyCriterias.getSetcriteriaAspathPrependName());
								if (policyCriterias.getSetcriteriaAspathPrependIndex() != null)
									inRoutingPolicySetCriteria.setASPathPrependIndex(ActivationUtils
											.toInteger(policyCriterias.getSetcriteriaAspathPrependIndex()));

							}

							if (policyCriterias.getSetcriteriaMetric() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaMetric())) {
								inRoutingPolicySetCriteria.setMetric(policyCriterias.getSetcriteriaMetricName());

							}

							if (policyCriterias.getSetcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (policyCriterias.getSetcriteriaRegexAspath() != null && ActivationUtils
										.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
									inRoutingPolicySetCriteria.setRegexASPath(aluPathConfig);

								}
							}

							if (policyCriterias.getSetcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									inRoutingPolicySetCriteria.getNeightbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}
							}

						}
					}

				}
				bgpRoutingProtocol.getALUBGPPeerGroupInboundRoutingPolicyV6().add(inRoutingPolicy);
			}

			if (policyType.getOutboundRoutingIpv4Policy() != null && policyType.getOutboundRoutingIpv4Policy() == 1) {
				RoutingPolicy outRoutingPolicy = new RoutingPolicy();
				outRoutingPolicy.setIsStandardPolicy(
						ActivationUtils.getBooleanValue(policyType.getOutboundIstandardpolicyv4()));
				outRoutingPolicy.setIsPreprovisioned(
						ActivationUtils.getBooleanValue(policyType.getOutboundIpv4Preprovisioned()));// TODO and the
				RoutingPolicyMatchCriteria routingPolicyMatchCriteria = null;// below
				RoutingPolicySetCriteria outRoutingPolicySetCriteria = null;
				outRoutingPolicy.setName(policyType.getOutboundIpv4PolicyName());
				if (policyType.getOutboundIpv4Preprovisioned() == null
						|| !ActivationUtils.getBooleanValue(policyType.getOutboundIpv4Preprovisioned())) {
					Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
					List<Integer> policyCriteriaIds = new ArrayList<Integer>();
					policyTypeCriteria.forEach(b -> {
						policyCriteriaIds.add(b.getPolicyCriteriaId());
					});

					if (!policyCriteriaIds.isEmpty()) {
						List<PolicyCriteria> policyCriteriasCheck = policyCriteriaRepository
								.findByPolicyCriteriaIdInAndEndDateIsNull(policyCriteriaIds);

						if (policyCriteriasCheck.stream().anyMatch(cri->
								 cri.getMatchcriteriaPrefixlist() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaPrefixlist())
								|| cri.getMatchcriteriaProtocol() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaProtocol())
								|| cri.getMatchcriteriaRegexAspath() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) || cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
												.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {

							routingPolicyMatchCriteria = new RoutingPolicyMatchCriteria();
							outRoutingPolicy.setMatchCriteria(routingPolicyMatchCriteria);

							

						}
						
						if (policyCriteriasCheck.stream()
								.anyMatch(cri -> cri.getSetcriteriaLocalPreference() != null && ActivationUtils
										.getBooleanValue(cri.getSetcriteriaLocalPreference()) || cri.getSetcriteriaAspathPrepend() != null
												&& ActivationUtils.getBooleanValue(cri.getSetcriteriaAspathPrepend()) || cri.getSetcriteriaMetric() != null
														&& ActivationUtils.getBooleanValue(cri.getSetcriteriaMetric()) || cri.getSetcriteriaRegexAspath() != null
																&& ActivationUtils.getBooleanValue(cri.getSetcriteriaRegexAspath())||  cri.getMatchcriteriaRegexAspath() != null
																		&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) 
																		|| cri.getSetcriteriaNeighbourCommunity()!=null && ActivationUtils.getBooleanValue(cri.getSetcriteriaNeighbourCommunity())
																		|| cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
																		.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {
							outRoutingPolicySetCriteria = new RoutingPolicySetCriteria();
							outRoutingPolicy.setSetCriteria(outRoutingPolicySetCriteria);

						}
					}
					for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
						Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCriterias = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCriterias != null) {
							if (policyCriterias.getTermSetcriteriaAction() != null
									&& outRoutingPolicy.getRoutingPolicyTerm().size() <= 0) {
								RoutingPolicyTerm routingPolicyTerm = new RoutingPolicyTerm();
								routingPolicyTerm.setTermAction(policyCriterias.getTermSetcriteriaAction());
								routingPolicyTerm.setId(policyCriterias.getTermName());
								outRoutingPolicy.getRoutingPolicyTerm().add(routingPolicyTerm);
							}
							if (policyCriterias.getMatchcriteriaPrefixlist() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaPrefixlist())) {
								PrefixList outPrefixList = new PrefixList();
								outPrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
								outPrefixList.setIsPreprovisioned(ActivationUtils
										.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
								if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() == null
										|| !ActivationUtils.getBooleanValue(
												policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
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
										outPrefixList.getPrefixListEntry().add(prefixListEntry);// TODO
									}
									routingPolicyMatchCriteria.setPrefixList(outPrefixList);
								}
							}
							if (policyCriterias.getMatchcriteriaProtocol() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaProtocol())) {
								for (PolicycriteriaProtocol policycriteriaProtocol : policyCriterias
										.getPolicycriteriaProtocols()) {
									routingPolicyMatchCriteria.setProtocol(policycriteriaProtocol.getProtocolName());
								}
							}

							if (policyCriterias.getMatchcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
								}
								routingPolicyMatchCriteria.setRegexASPath(aluPathConfig);

								// TODO - protocol
							}

							if (policyCriterias.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getMatchcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									routingPolicyMatchCriteria.getNeighbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}

							}

							if (policyCriterias.getSetcriteriaLocalPreference() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaLocalPreference())) {
								outRoutingPolicySetCriteria
										.setLocalPreference(policyCriterias.getSetcriteriaLocalpreferenceName());

							}

							if (policyCriterias.getSetcriteriaAspathPrepend() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaAspathPrepend())) {
								outRoutingPolicySetCriteria
										.setASPathPrepend(policyCriterias.getSetcriteriaAspathPrependName());
								if (policyCriterias.getSetcriteriaAspathPrependIndex() != null)
									outRoutingPolicySetCriteria.setASPathPrependIndex(ActivationUtils
											.toInteger(policyCriterias.getSetcriteriaAspathPrependIndex()));

							}

							if (policyCriterias.getSetcriteriaMetric() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaMetric())) {
								outRoutingPolicySetCriteria.setMetric(policyCriterias.getSetcriteriaMetricName());

							}

							if (policyCriterias.getSetcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (policyCriterias.getSetcriteriaRegexAspath() != null && ActivationUtils
										.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
									outRoutingPolicySetCriteria.setRegexASPath(aluPathConfig);

								}
							}

							if (policyCriterias.getSetcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									outRoutingPolicySetCriteria.getNeightbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}
							}

						}
					}

				}
				bgpRoutingProtocol.getALUBGPPeerGroupOutboundRoutingPolicy().add(outRoutingPolicy);
			}

			if (policyType.getOutboundRoutingIpv6Policy() != null && policyType.getOutboundRoutingIpv6Policy() == 1) {
				RoutingPolicyMatchCriteria routingPolicyMatchCriteria = null;
				RoutingPolicySetCriteria outRoutingPolicySetCriteria = null;
				RoutingPolicy outRoutingPolicy = new RoutingPolicy();
				outRoutingPolicy.setIsStandardPolicy(
						ActivationUtils.getBooleanValue(policyType.getOutboundIstandardpolicyv6()));
				outRoutingPolicy.setIsPreprovisioned(
						ActivationUtils.getBooleanValue(policyType.getOutboundIpv6Preprovisioned()));// TODO and the

				outRoutingPolicy.setName(policyType.getOutboundIpv6PolicyName());
				if (policyType.getOutboundIpv6Preprovisioned() == null
						|| !ActivationUtils.getBooleanValue(policyType.getOutboundIpv6Preprovisioned())) {

					Set<PolicyTypeCriteriaMapping> policyTypeCriteria = policyType.getPolicyTypeCriteriaMappings();
					List<Integer> policyCriteriaIds = new ArrayList<Integer>();
					policyTypeCriteria.forEach(b -> {
						policyCriteriaIds.add(b.getPolicyCriteriaId());
					});

					if (!policyCriteriaIds.isEmpty()) {
						List<PolicyCriteria> policyCriteriasCheck = policyCriteriaRepository
								.findByPolicyCriteriaIdInAndEndDateIsNull(policyCriteriaIds);

						if (policyCriteriasCheck.stream().anyMatch(cri->
								 cri.getMatchcriteriaPrefixlist() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaPrefixlist())
								|| cri.getMatchcriteriaProtocol() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaProtocol())
								|| cri.getMatchcriteriaRegexAspath() != null
										&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) || cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
												.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {

							routingPolicyMatchCriteria = new RoutingPolicyMatchCriteria();
							outRoutingPolicy.setMatchCriteria(routingPolicyMatchCriteria);

							

						}
						
						if (policyCriteriasCheck.stream()
								.anyMatch(cri -> cri.getSetcriteriaLocalPreference() != null && ActivationUtils
										.getBooleanValue(cri.getSetcriteriaLocalPreference()) || cri.getSetcriteriaAspathPrepend() != null
												&& ActivationUtils.getBooleanValue(cri.getSetcriteriaAspathPrepend()) || cri.getSetcriteriaMetric() != null
														&& ActivationUtils.getBooleanValue(cri.getSetcriteriaMetric()) || cri.getSetcriteriaRegexAspath() != null
																&& ActivationUtils.getBooleanValue(cri.getSetcriteriaRegexAspath())||  cri.getMatchcriteriaRegexAspath() != null
																		&& ActivationUtils.getBooleanValue(cri.getMatchcriteriaRegexAspath()) || cri.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
																		.getBooleanValue(cri.getMatchcriteriaNeighbourCommunity()))) {
							outRoutingPolicySetCriteria = new RoutingPolicySetCriteria();
							outRoutingPolicy.setSetCriteria(outRoutingPolicySetCriteria);

						}
					}
					for (PolicyTypeCriteriaMapping polcyTypeCriteria : policyTypeCriteria) {
						Integer policyCriteriaId = polcyTypeCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCriterias = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCriterias != null) {
							if (policyCriterias.getTermSetcriteriaAction() != null
									&& outRoutingPolicy.getRoutingPolicyTerm().size() <= 0) {
								RoutingPolicyTerm routingPolicyTerm = new RoutingPolicyTerm();
								routingPolicyTerm.setTermAction(policyCriterias.getTermSetcriteriaAction());
								routingPolicyTerm.setId(policyCriterias.getTermName());
								outRoutingPolicy.getRoutingPolicyTerm().add(routingPolicyTerm);
							}
							if (policyCriterias.getMatchcriteriaPrefixlist() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaPrefixlist())) {
								PrefixList outPrefixList = new PrefixList();
								outPrefixList.setName(policyCriterias.getMatchcriteriaPrefixlistName());
								outPrefixList.setIsPreprovisioned(ActivationUtils
										.getBooleanValue(policyCriterias.getMatchcriteriaPprefixlistPreprovisioned()));
								if (policyCriterias.getMatchcriteriaPprefixlistPreprovisioned() == null
										|| !ActivationUtils.getBooleanValue(
												policyCriterias.getMatchcriteriaPprefixlistPreprovisioned())) {
									Set<PrefixlistConfig> prefixListConfigs = policyCriterias.getPrefixlistConfigs();
									for (PrefixlistConfig prefixConfig : prefixListConfigs) {
										PrefixListEntry prefixListEntry = new PrefixListEntry();
										prefixListEntry.setNetworkPrefix(prefixConfig.getNetworkPrefix()!=null ?prefixConfig.getNetworkPrefix().trim():prefixConfig.getNetworkPrefix());
										if (prefixConfig.getAction() != null) {
											prefixListEntry.setOperator(prefixConfig.getAction().toLowerCase());
										}
										if (prefixConfig.getLeValue() != null)
											prefixListEntry.setSubnetRangeMaximum(
													ActivationUtils.toInteger(prefixConfig.getLeValue()));
										if (prefixConfig.getGeValue() != null)
											prefixListEntry.setSubnetRangeMinimum(
													ActivationUtils.toInteger(prefixConfig.getGeValue()));
										outPrefixList.getPrefixListEntry().add(prefixListEntry);// TODO
									}
									routingPolicyMatchCriteria.setPrefixList(outPrefixList);
								}
							}
							if (policyCriterias.getMatchcriteriaProtocol() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaProtocol())) {
								for (PolicycriteriaProtocol policycriteriaProtocol : policyCriterias
										.getPolicycriteriaProtocols()) {
									routingPolicyMatchCriteria.setProtocol(policycriteriaProtocol.getProtocolName());
								}

							}

							if (policyCriterias.getMatchcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (ActivationUtils.getBooleanValue(policyCriterias.getMatchcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
								}
								routingPolicyMatchCriteria.setRegexASPath(aluPathConfig);

								// TODO - protocol
							}

							if (policyCriterias.getMatchcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getMatchcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									routingPolicyMatchCriteria.getNeighbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}

							}

							if (policyCriterias.getSetcriteriaLocalPreference() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaLocalPreference())) {
								outRoutingPolicySetCriteria
										.setLocalPreference(policyCriterias.getSetcriteriaLocalpreferenceName());

							}

							if (policyCriterias.getSetcriteriaAspathPrepend() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaAspathPrepend())) {
								outRoutingPolicySetCriteria
										.setASPathPrepend(policyCriterias.getSetcriteriaAspathPrependName());
								if (policyCriterias.getSetcriteriaAspathPrependIndex() != null)
									outRoutingPolicySetCriteria.setASPathPrependIndex(ActivationUtils
											.toInteger(policyCriterias.getSetcriteriaAspathPrependIndex()));

							}

							if (policyCriterias.getSetcriteriaMetric() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaMetric())) {
								outRoutingPolicySetCriteria.setMetric(policyCriterias.getSetcriteriaMetricName());

							}

							if (policyCriterias.getSetcriteriaRegexAspath() != null
									&& ActivationUtils.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
								ALUASPathConfig aluPathConfig = new ALUASPathConfig();
								if (policyCriterias.getSetcriteriaRegexAspath() != null && ActivationUtils
										.getBooleanValue(policyCriterias.getSetcriteriaRegexAspath())) {
									Set<RegexAspathConfig> asPathConfigs = policyCriterias.getRegexAspathConfigs();
									for (RegexAspathConfig asPathConfig : asPathConfigs) {
										aluPathConfig.setName(asPathConfig.getName());
										if (StringUtils.isNotBlank(asPathConfig.getAsPath()))
											aluPathConfig.setASPath(asPathConfig.getAsPath());
									}
									outRoutingPolicySetCriteria.setRegexASPath(aluPathConfig);

								}
							}

							if (policyCriterias.getSetcriteriaNeighbourCommunity() != null && ActivationUtils
									.getBooleanValue(policyCriterias.getSetcriteriaNeighbourCommunity())) {
								for (NeighbourCommunityConfig neighCommutity : policyCriterias
										.getNeighbourCommunityConfigs()) {
									RoutingPolicyNeighbourCommunity routingPolicyNeighbourCommunity = new RoutingPolicyNeighbourCommunity();
									routingPolicyNeighbourCommunity.setName(neighCommutity.getName());
									routingPolicyNeighbourCommunity.getNeighbourCommunity()
											.add(neighCommutity.getCommunity());
									outRoutingPolicySetCriteria.getNeightbourCommunity()
											.add(routingPolicyNeighbourCommunity);
								}
							}

						}
					}

				}
				bgpRoutingProtocol.getALUBGPPeerGroupOutboundRoutingPolicyV6().add(outRoutingPolicy);
			}

		}
		return bgpRoutingProtocol;
	}

}
