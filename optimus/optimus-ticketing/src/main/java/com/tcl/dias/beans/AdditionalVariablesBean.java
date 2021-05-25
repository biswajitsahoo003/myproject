package com.tcl.dias.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Port (Source/Destination)",
    "Destination IP Pool",
    "Type of Change",
    "ACL Name/Number",
    "Source IP Pool",
    "Interface Number",
    "IP Pool/Subnet Mask",
    "VRF Name (If Applicable)",
    "QOS Policy(If Applicable)",
    "BW to be Allocated per COS/Reservation IP",
    "Trunk Group",
    "CLI Change",
    "IP Address",
    "CPS Change",
    "Issue End Time",
    "Impact",
    "Issue Start Time",
    "Channel",
    "Category",
    "Netflow Server IP",
    "Version",
    "Port",
    "Source Interface",
    "IP Accounting Needed",
    "IP Helper Address (if applicable)",
    "Local DHCP Server (if applicable)",
    "LAN IP Pool/Subnet Mask",
    "Excluded Address (if applicable)",
    "DNS IP",
    "Contact",
    "Preshared Keys",
    "Phase1 Parameter",
    "Phase2 Parameter",
    "Source",
    "Transform Set",
    "Destination",
    "GRE/IPSEC",
    "ISKAMP policy",
    "Timing Change",
    "New Termination Number",
    "Percentage Wise Routing",
    "ICR Change Type",
    "ITFS/DID/LNS/UIFN",
    "Lan Subnets To Be Advertised",
    "Protocol Name",
    "AS Number",
    "New IP Address",
    "IP Change Type",
    "TCL Owned Pool",
    "Customer Owned IPv6 Pool",
    "Wave File Attached",
    "IVR Modificationr",
    "New IVR Prompt Change",
    "DTMF Change",
    "Source IP (Subnet)",
    "Direction",
    "Destination IP (Subnet)",
    "Type Of NAT",
    "AS Set (If Applicable)",
    "Public Pool/Subnet Mask",
    "Toll Free/LNS",
    "Outpulse Change",
    "New Outpulse or PSTN Number",
    "Existing Outpulse or PSTN Number",
    "Prefix/Suffix Change Details",
    "Change Type",
    "New Primary/Secondary Trunk Group Change",
    "New Circuit ID/Trunk Group",
    "Existing Primary/Secondary Trunk Group Change",
    "RTBH",
    "Community Based Routing/Policy Based Routing",
    "Route-Map",
    "Next Hop",
    "VRF Name",
    "Route Changes",
    "Load Balancing",
    " TG Sequence Change",
    "Routing Label Change",
    "Route Block",
    "SIP Request Details",
    "SNMP Server IP",
    "Community String",
    "VLan ID",
    "Customer owned AS Number",
    "BGP Policy Rejection Reason",
    "BGP Details",
    "BGP Policy Agree",
    "Route-Object",
    "Customer Reference Number"
})
public class AdditionalVariablesBean {

    @JsonProperty("Port (Source/Destination)")
    private String portSourceDestination;
    @JsonProperty("Destination IP Pool")
    private String destinationIPPool;
    @JsonProperty("Type of Change")
    private String typeOfChange;
    @JsonProperty("ACL Name/Number")
    private String aCLNameNumber;
    @JsonProperty("Source IP Pool")
    private String sourceIPPool;
    @JsonProperty("Interface Number")
    private String interfaceNumber;
    @JsonProperty("IP Pool/Subnet Mask")
    private String iPPoolSubnetMask;
    @JsonProperty("VRF Name (If Applicable)")
    private String vRFNameIfApplicable;
    @JsonProperty("QOS Policy(If Applicable)")
    private String qOSPolicyIfApplicable;
    @JsonProperty("BW to be Allocated per COS/Reservation IP")
    private String bWToBeAllocatedPerCOSReservationIP;
    @JsonProperty("Trunk Group")
    private String trunkGroup;
    @JsonProperty("CLI Change")
    private String cLIChange;
    @JsonProperty("IP Address")
    private String iPAddress;
    @JsonProperty("CPS Change")
    private String cPSChange;
    @JsonProperty("Issue End Time")
    private String issueEndTime;
    @JsonProperty("Impact")
    private String impact;
    @JsonProperty("Issue Start Time")
    private String issueStartTime;
    @JsonProperty("Channel")
    private String channel;
    @JsonProperty("Category")
    private String category;
    @JsonProperty("Netflow Server IP")
    private String netflowServerIP;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Port")
    private String port;
    @JsonProperty("Source Interface")
    private String sourceInterface;
    @JsonProperty("IP Accounting Needed")
    private String iPAccountingNeeded;
    @JsonProperty("IP Helper Address (if applicable)")
    private String iPHelperAddressIfApplicable;
    @JsonProperty("Local DHCP Server (if applicable)")
    private String localDHCPServerIfApplicable;
    @JsonProperty("LAN IP Pool/Subnet Mask")
    private String lANIPPoolSubnetMask;
    @JsonProperty("Excluded Address (if applicable)")
    private String excludedAddressIfApplicable;
    @JsonProperty("DNS IP")
    private String dNSIP;
    @JsonProperty("Contact")
    private String contact;
    @JsonProperty("Preshared Keys")
    private String presharedKeys;
    @JsonProperty("Phase1 Parameter")
    private String phase1Parameter;
    @JsonProperty("Phase2 Parameter")
    private String phase2Parameter;
    @JsonProperty("Source")
    private String source;
    @JsonProperty("Transform Set")
    private String transformSet;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("GRE/IPSEC")
    private String gREIPSEC;
    @JsonProperty("ISKAMP policy")
    private String iSKAMPPolicy;
    @JsonProperty("Timing Change")
    private String timingChange;
    @JsonProperty("New Termination Number")
    private String newTerminationNumber;
    @JsonProperty("Percentage Wise Routing")
    private String percentageWiseRouting;
    @JsonProperty("ICR Change Type")
    private String iCRChangeType;
    @JsonProperty("ITFS/DID/LNS/UIFN")
    private String iTFSDIDLNSUIFN;
    @JsonProperty("Lan Subnets To Be Advertised")
    private String lanSubnetsToBeAdvertised;
    @JsonProperty("Protocol Name")
    private String protocolName;
    @JsonProperty("AS Number")
    private String aSNumber;
    @JsonProperty("New IP Address")
    private String newIPAddress;
    @JsonProperty("IP Change Type")
    private String iPChangeType;
    @JsonProperty("TCL Owned Pool")
    private String tCLOwnedPool;
    @JsonProperty("Customer Owned IPv6 Pool")
    private String customerOwnedIPv6Pool;
    @JsonProperty("Wave File Attached")
    private String waveFileAttached;
    @JsonProperty("IVR Modificationr")
    private String iVRModificationr;
    @JsonProperty("New IVR Prompt Change")
    private String newIVRPromptChange;
    @JsonProperty("DTMF Change")
    private String dTMFChange;
    @JsonProperty("Source IP (Subnet)")
    private String sourceIPSubnet;
    @JsonProperty("Direction")
    private String direction;
    @JsonProperty("Destination IP (Subnet)")
    private String destinationIPSubnet;
    @JsonProperty("Type Of NAT")
    private String typeOfNAT;
    @JsonProperty("AS Set (If Applicable)")
    private String aSSetIfApplicable;
    @JsonProperty("Public Pool/Subnet Mask")
    private String publicPoolSubnetMask;
    @JsonProperty("Toll Free/LNS")
    private String tollFreeLNS;
    @JsonProperty("Outpulse Change")
    private String outpulseChange;
    @JsonProperty("New Outpulse or PSTN Number")
    private String newOutpulseOrPSTNNumber;
    @JsonProperty("Existing Outpulse or PSTN Number")
    private String existingOutpulseOrPSTNNumber;
    @JsonProperty("Prefix/Suffix Change Details")
    private String prefixSuffixChangeDetails;
    @JsonProperty("Change Type")
    private String changeType;
    @JsonProperty("New Primary/Secondary Trunk Group Change")
    private String newPrimarySecondaryTrunkGroupChange;
    @JsonProperty("New Circuit ID/Trunk Group")
    private String newCircuitIDTrunkGroup;
    @JsonProperty("Existing Primary/Secondary Trunk Group Change")
    private String existingPrimarySecondaryTrunkGroupChange;
    @JsonProperty("RTBH")
    private String rTBH;
    @JsonProperty("Community Based Routing/Policy Based Routing")
    private String communityBasedRoutingPolicyBasedRouting;
    @JsonProperty("Route-Map")
    private String routeMap;
    @JsonProperty("Next Hop")
    private String nextHop;
    @JsonProperty("VRF Name")
    private String vRFName;
    @JsonProperty("Route Changes")
    private String routeChanges;
    @JsonProperty("Load Balancing")
    private String loadBalancing;
    @JsonProperty(" TG Sequence Change")
    private String tGSequenceChange;
    @JsonProperty("Routing Label Change")
    private String routingLabelChange;
    @JsonProperty("Route Block")
    private String routeBlock;
    @JsonProperty("SIP Request Details")
    private String sIPRequestDetails;
    @JsonProperty("SNMP Server IP")
    private String sNMPServerIP;
    @JsonProperty("Community String")
    private String communityString;
    @JsonProperty("VLan ID")
    private String vLanID;
    @JsonProperty("Customer owned AS Number")
    private String customerOwnedASNumber;
    @JsonProperty("BGP Policy Rejection Reason")
    private String bGPPolicyRejectionReason;
    @JsonProperty("BGP Details")
    private String bGPDetails;
    @JsonProperty("BGP Policy Agree")
    private String bGPPolicyAgree;
    @JsonProperty("Route-Object")
    private String routeObject;
    @JsonProperty("Customer Reference Number")
    private String customerReferenceNumber;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Port (Source/Destination)")
    public String getPortSourceDestination() {
        return portSourceDestination;
    }

    @JsonProperty("Port (Source/Destination)")
    public void setPortSourceDestination(String portSourceDestination) {
        this.portSourceDestination = portSourceDestination;
    }

    @JsonProperty("Destination IP Pool")
    public String getDestinationIPPool() {
        return destinationIPPool;
    }

    @JsonProperty("Destination IP Pool")
    public void setDestinationIPPool(String destinationIPPool) {
        this.destinationIPPool = destinationIPPool;
    }

    @JsonProperty("Type of Change")
    public String getTypeOfChange() {
        return typeOfChange;
    }

    @JsonProperty("Type of Change")
    public void setTypeOfChange(String typeOfChange) {
        this.typeOfChange = typeOfChange;
    }

    @JsonProperty("ACL Name/Number")
    public String getACLNameNumber() {
        return aCLNameNumber;
    }

    @JsonProperty("ACL Name/Number")
    public void setACLNameNumber(String aCLNameNumber) {
        this.aCLNameNumber = aCLNameNumber;
    }

    @JsonProperty("Source IP Pool")
    public String getSourceIPPool() {
        return sourceIPPool;
    }

    @JsonProperty("Source IP Pool")
    public void setSourceIPPool(String sourceIPPool) {
        this.sourceIPPool = sourceIPPool;
    }

    @JsonProperty("Interface Number")
    public String getInterfaceNumber() {
        return interfaceNumber;
    }

    @JsonProperty("Interface Number")
    public void setInterfaceNumber(String interfaceNumber) {
        this.interfaceNumber = interfaceNumber;
    }

    @JsonProperty("IP Pool/Subnet Mask")
    public String getIPPoolSubnetMask() {
        return iPPoolSubnetMask;
    }

    @JsonProperty("IP Pool/Subnet Mask")
    public void setIPPoolSubnetMask(String iPPoolSubnetMask) {
        this.iPPoolSubnetMask = iPPoolSubnetMask;
    }

    @JsonProperty("VRF Name (If Applicable)")
    public String getVRFNameIfApplicable() {
        return vRFNameIfApplicable;
    }

    @JsonProperty("VRF Name (If Applicable)")
    public void setVRFNameIfApplicable(String vRFNameIfApplicable) {
        this.vRFNameIfApplicable = vRFNameIfApplicable;
    }

    @JsonProperty("QOS Policy(If Applicable)")
    public String getQOSPolicyIfApplicable() {
        return qOSPolicyIfApplicable;
    }

    @JsonProperty("QOS Policy(If Applicable)")
    public void setQOSPolicyIfApplicable(String qOSPolicyIfApplicable) {
        this.qOSPolicyIfApplicable = qOSPolicyIfApplicable;
    }

    @JsonProperty("BW to be Allocated per COS/Reservation IP")
    public String getBWToBeAllocatedPerCOSReservationIP() {
        return bWToBeAllocatedPerCOSReservationIP;
    }

    @JsonProperty("BW to be Allocated per COS/Reservation IP")
    public void setBWToBeAllocatedPerCOSReservationIP(String bWToBeAllocatedPerCOSReservationIP) {
        this.bWToBeAllocatedPerCOSReservationIP = bWToBeAllocatedPerCOSReservationIP;
    }

    @JsonProperty("Trunk Group")
    public String getTrunkGroup() {
        return trunkGroup;
    }

    @JsonProperty("Trunk Group")
    public void setTrunkGroup(String trunkGroup) {
        this.trunkGroup = trunkGroup;
    }

    @JsonProperty("CLI Change")
    public String getCLIChange() {
        return cLIChange;
    }

    @JsonProperty("CLI Change")
    public void setCLIChange(String cLIChange) {
        this.cLIChange = cLIChange;
    }

    @JsonProperty("IP Address")
    public String getIPAddress() {
        return iPAddress;
    }

    @JsonProperty("IP Address")
    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    @JsonProperty("CPS Change")
    public String getCPSChange() {
        return cPSChange;
    }

    @JsonProperty("CPS Change")
    public void setCPSChange(String cPSChange) {
        this.cPSChange = cPSChange;
    }

    @JsonProperty("Issue End Time")
    public String getIssueEndTime() {
        return issueEndTime;
    }

    @JsonProperty("Issue End Time")
    public void setIssueEndTime(String issueEndTime) {
        this.issueEndTime = issueEndTime;
    }

    @JsonProperty("Impact")
    public String getImpact() {
        return impact;
    }

    @JsonProperty("Impact")
    public void setImpact(String impact) {
        this.impact = impact;
    }

    @JsonProperty("Issue Start Time")
    public String getIssueStartTime() {
        return issueStartTime;
    }

    @JsonProperty("Issue Start Time")
    public void setIssueStartTime(String issueStartTime) {
        this.issueStartTime = issueStartTime;
    }

    @JsonProperty("Channel")
    public String getChannel() {
        return channel;
    }

    @JsonProperty("Channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @JsonProperty("Category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("Netflow Server IP")
    public String getNetflowServerIP() {
        return netflowServerIP;
    }

    @JsonProperty("Netflow Server IP")
    public void setNetflowServerIP(String netflowServerIP) {
        this.netflowServerIP = netflowServerIP;
    }

    @JsonProperty("Version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("Version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("Port")
    public String getPort() {
        return port;
    }

    @JsonProperty("Port")
    public void setPort(String port) {
        this.port = port;
    }

    @JsonProperty("Source Interface")
    public String getSourceInterface() {
        return sourceInterface;
    }

    @JsonProperty("Source Interface")
    public void setSourceInterface(String sourceInterface) {
        this.sourceInterface = sourceInterface;
    }

    @JsonProperty("IP Accounting Needed")
    public String getIPAccountingNeeded() {
        return iPAccountingNeeded;
    }

    @JsonProperty("IP Accounting Needed")
    public void setIPAccountingNeeded(String iPAccountingNeeded) {
        this.iPAccountingNeeded = iPAccountingNeeded;
    }

    @JsonProperty("IP Helper Address (if applicable)")
    public String getIPHelperAddressIfApplicable() {
        return iPHelperAddressIfApplicable;
    }

    @JsonProperty("IP Helper Address (if applicable)")
    public void setIPHelperAddressIfApplicable(String iPHelperAddressIfApplicable) {
        this.iPHelperAddressIfApplicable = iPHelperAddressIfApplicable;
    }

    @JsonProperty("Local DHCP Server (if applicable)")
    public String getLocalDHCPServerIfApplicable() {
        return localDHCPServerIfApplicable;
    }

    @JsonProperty("Local DHCP Server (if applicable)")
    public void setLocalDHCPServerIfApplicable(String localDHCPServerIfApplicable) {
        this.localDHCPServerIfApplicable = localDHCPServerIfApplicable;
    }

    @JsonProperty("LAN IP Pool/Subnet Mask")
    public String getLANIPPoolSubnetMask() {
        return lANIPPoolSubnetMask;
    }

    @JsonProperty("LAN IP Pool/Subnet Mask")
    public void setLANIPPoolSubnetMask(String lANIPPoolSubnetMask) {
        this.lANIPPoolSubnetMask = lANIPPoolSubnetMask;
    }

    @JsonProperty("Excluded Address (if applicable)")
    public String getExcludedAddressIfApplicable() {
        return excludedAddressIfApplicable;
    }

    @JsonProperty("Excluded Address (if applicable)")
    public void setExcludedAddressIfApplicable(String excludedAddressIfApplicable) {
        this.excludedAddressIfApplicable = excludedAddressIfApplicable;
    }

    @JsonProperty("DNS IP")
    public String getDNSIP() {
        return dNSIP;
    }

    @JsonProperty("DNS IP")
    public void setDNSIP(String dNSIP) {
        this.dNSIP = dNSIP;
    }

    @JsonProperty("Contact")
    public String getContact() {
        return contact;
    }

    @JsonProperty("Contact")
    public void setContact(String contact) {
        this.contact = contact;
    }

    @JsonProperty("Preshared Keys")
    public String getPresharedKeys() {
        return presharedKeys;
    }

    @JsonProperty("Preshared Keys")
    public void setPresharedKeys(String presharedKeys) {
        this.presharedKeys = presharedKeys;
    }

    @JsonProperty("Phase1 Parameter")
    public String getPhase1Parameter() {
        return phase1Parameter;
    }

    @JsonProperty("Phase1 Parameter")
    public void setPhase1Parameter(String phase1Parameter) {
        this.phase1Parameter = phase1Parameter;
    }

    @JsonProperty("Phase2 Parameter")
    public String getPhase2Parameter() {
        return phase2Parameter;
    }

    @JsonProperty("Phase2 Parameter")
    public void setPhase2Parameter(String phase2Parameter) {
        this.phase2Parameter = phase2Parameter;
    }

    @JsonProperty("Source")
    public String getSource() {
        return source;
    }

    @JsonProperty("Source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("Transform Set")
    public String getTransformSet() {
        return transformSet;
    }

    @JsonProperty("Transform Set")
    public void setTransformSet(String transformSet) {
        this.transformSet = transformSet;
    }

    @JsonProperty("Destination")
    public String getDestination() {
        return destination;
    }

    @JsonProperty("Destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonProperty("GRE/IPSEC")
    public String getGREIPSEC() {
        return gREIPSEC;
    }

    @JsonProperty("GRE/IPSEC")
    public void setGREIPSEC(String gREIPSEC) {
        this.gREIPSEC = gREIPSEC;
    }

    @JsonProperty("ISKAMP policy")
    public String getISKAMPPolicy() {
        return iSKAMPPolicy;
    }

    @JsonProperty("ISKAMP policy")
    public void setISKAMPPolicy(String iSKAMPPolicy) {
        this.iSKAMPPolicy = iSKAMPPolicy;
    }

    @JsonProperty("Timing Change")
    public String getTimingChange() {
        return timingChange;
    }

    @JsonProperty("Timing Change")
    public void setTimingChange(String timingChange) {
        this.timingChange = timingChange;
    }

    @JsonProperty("New Termination Number")
    public String getNewTerminationNumber() {
        return newTerminationNumber;
    }

    @JsonProperty("New Termination Number")
    public void setNewTerminationNumber(String newTerminationNumber) {
        this.newTerminationNumber = newTerminationNumber;
    }

    @JsonProperty("Percentage Wise Routing")
    public String getPercentageWiseRouting() {
        return percentageWiseRouting;
    }

    @JsonProperty("Percentage Wise Routing")
    public void setPercentageWiseRouting(String percentageWiseRouting) {
        this.percentageWiseRouting = percentageWiseRouting;
    }

    @JsonProperty("ICR Change Type")
    public String getICRChangeType() {
        return iCRChangeType;
    }

    @JsonProperty("ICR Change Type")
    public void setICRChangeType(String iCRChangeType) {
        this.iCRChangeType = iCRChangeType;
    }

    @JsonProperty("ITFS/DID/LNS/UIFN")
    public String getITFSDIDLNSUIFN() {
        return iTFSDIDLNSUIFN;
    }

    @JsonProperty("ITFS/DID/LNS/UIFN")
    public void setITFSDIDLNSUIFN(String iTFSDIDLNSUIFN) {
        this.iTFSDIDLNSUIFN = iTFSDIDLNSUIFN;
    }

    @JsonProperty("Lan Subnets To Be Advertised")
    public String getLanSubnetsToBeAdvertised() {
        return lanSubnetsToBeAdvertised;
    }

    @JsonProperty("Lan Subnets To Be Advertised")
    public void setLanSubnetsToBeAdvertised(String lanSubnetsToBeAdvertised) {
        this.lanSubnetsToBeAdvertised = lanSubnetsToBeAdvertised;
    }

    @JsonProperty("Protocol Name")
    public String getProtocolName() {
        return protocolName;
    }

    @JsonProperty("Protocol Name")
    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    @JsonProperty("AS Number")
    public String getASNumber() {
        return aSNumber;
    }

    @JsonProperty("AS Number")
    public void setASNumber(String aSNumber) {
        this.aSNumber = aSNumber;
    }

    @JsonProperty("New IP Address")
    public String getNewIPAddress() {
        return newIPAddress;
    }

    @JsonProperty("New IP Address")
    public void setNewIPAddress(String newIPAddress) {
        this.newIPAddress = newIPAddress;
    }

    @JsonProperty("IP Change Type")
    public String getIPChangeType() {
        return iPChangeType;
    }

    @JsonProperty("IP Change Type")
    public void setIPChangeType(String iPChangeType) {
        this.iPChangeType = iPChangeType;
    }

    @JsonProperty("TCL Owned Pool")
    public String getTCLOwnedPool() {
        return tCLOwnedPool;
    }

    @JsonProperty("TCL Owned Pool")
    public void setTCLOwnedPool(String tCLOwnedPool) {
        this.tCLOwnedPool = tCLOwnedPool;
    }

    @JsonProperty("Customer Owned IPv6 Pool")
    public String getCustomerOwnedIPv6Pool() {
        return customerOwnedIPv6Pool;
    }

    @JsonProperty("Customer Owned IPv6 Pool")
    public void setCustomerOwnedIPv6Pool(String customerOwnedIPv6Pool) {
        this.customerOwnedIPv6Pool = customerOwnedIPv6Pool;
    }

    @JsonProperty("Wave File Attached")
    public String getWaveFileAttached() {
        return waveFileAttached;
    }

    @JsonProperty("Wave File Attached")
    public void setWaveFileAttached(String waveFileAttached) {
        this.waveFileAttached = waveFileAttached;
    }

    @JsonProperty("IVR Modificationr")
    public String getIVRModificationr() {
        return iVRModificationr;
    }

    @JsonProperty("IVR Modificationr")
    public void setIVRModificationr(String iVRModificationr) {
        this.iVRModificationr = iVRModificationr;
    }

    @JsonProperty("New IVR Prompt Change")
    public String getNewIVRPromptChange() {
        return newIVRPromptChange;
    }

    @JsonProperty("New IVR Prompt Change")
    public void setNewIVRPromptChange(String newIVRPromptChange) {
        this.newIVRPromptChange = newIVRPromptChange;
    }

    @JsonProperty("DTMF Change")
    public String getDTMFChange() {
        return dTMFChange;
    }

    @JsonProperty("DTMF Change")
    public void setDTMFChange(String dTMFChange) {
        this.dTMFChange = dTMFChange;
    }

    @JsonProperty("Source IP (Subnet)")
    public String getSourceIPSubnet() {
        return sourceIPSubnet;
    }

    @JsonProperty("Source IP (Subnet)")
    public void setSourceIPSubnet(String sourceIPSubnet) {
        this.sourceIPSubnet = sourceIPSubnet;
    }

    @JsonProperty("Direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("Direction")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("Destination IP (Subnet)")
    public String getDestinationIPSubnet() {
        return destinationIPSubnet;
    }

    @JsonProperty("Destination IP (Subnet)")
    public void setDestinationIPSubnet(String destinationIPSubnet) {
        this.destinationIPSubnet = destinationIPSubnet;
    }

    @JsonProperty("Type Of NAT")
    public String getTypeOfNAT() {
        return typeOfNAT;
    }

    @JsonProperty("Type Of NAT")
    public void setTypeOfNAT(String typeOfNAT) {
        this.typeOfNAT = typeOfNAT;
    }

    @JsonProperty("AS Set (If Applicable)")
    public String getASSetIfApplicable() {
        return aSSetIfApplicable;
    }

    @JsonProperty("AS Set (If Applicable)")
    public void setASSetIfApplicable(String aSSetIfApplicable) {
        this.aSSetIfApplicable = aSSetIfApplicable;
    }

    @JsonProperty("Public Pool/Subnet Mask")
    public String getPublicPoolSubnetMask() {
        return publicPoolSubnetMask;
    }

    @JsonProperty("Public Pool/Subnet Mask")
    public void setPublicPoolSubnetMask(String publicPoolSubnetMask) {
        this.publicPoolSubnetMask = publicPoolSubnetMask;
    }

    @JsonProperty("Toll Free/LNS")
    public String getTollFreeLNS() {
        return tollFreeLNS;
    }

    @JsonProperty("Toll Free/LNS")
    public void setTollFreeLNS(String tollFreeLNS) {
        this.tollFreeLNS = tollFreeLNS;
    }

    @JsonProperty("Outpulse Change")
    public String getOutpulseChange() {
        return outpulseChange;
    }

    @JsonProperty("Outpulse Change")
    public void setOutpulseChange(String outpulseChange) {
        this.outpulseChange = outpulseChange;
    }

    @JsonProperty("New Outpulse or PSTN Number")
    public String getNewOutpulseOrPSTNNumber() {
        return newOutpulseOrPSTNNumber;
    }

    @JsonProperty("New Outpulse or PSTN Number")
    public void setNewOutpulseOrPSTNNumber(String newOutpulseOrPSTNNumber) {
        this.newOutpulseOrPSTNNumber = newOutpulseOrPSTNNumber;
    }

    @JsonProperty("Existing Outpulse or PSTN Number")
    public String getExistingOutpulseOrPSTNNumber() {
        return existingOutpulseOrPSTNNumber;
    }

    @JsonProperty("Existing Outpulse or PSTN Number")
    public void setExistingOutpulseOrPSTNNumber(String existingOutpulseOrPSTNNumber) {
        this.existingOutpulseOrPSTNNumber = existingOutpulseOrPSTNNumber;
    }

    @JsonProperty("Prefix/Suffix Change Details")
    public String getPrefixSuffixChangeDetails() {
        return prefixSuffixChangeDetails;
    }

    @JsonProperty("Prefix/Suffix Change Details")
    public void setPrefixSuffixChangeDetails(String prefixSuffixChangeDetails) {
        this.prefixSuffixChangeDetails = prefixSuffixChangeDetails;
    }

    @JsonProperty("Change Type")
    public String getChangeType() {
        return changeType;
    }

    @JsonProperty("Change Type")
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    @JsonProperty("New Primary/Secondary Trunk Group Change")
    public String getNewPrimarySecondaryTrunkGroupChange() {
        return newPrimarySecondaryTrunkGroupChange;
    }

    @JsonProperty("New Primary/Secondary Trunk Group Change")
    public void setNewPrimarySecondaryTrunkGroupChange(String newPrimarySecondaryTrunkGroupChange) {
        this.newPrimarySecondaryTrunkGroupChange = newPrimarySecondaryTrunkGroupChange;
    }

    @JsonProperty("New Circuit ID/Trunk Group")
    public String getNewCircuitIDTrunkGroup() {
        return newCircuitIDTrunkGroup;
    }

    @JsonProperty("New Circuit ID/Trunk Group")
    public void setNewCircuitIDTrunkGroup(String newCircuitIDTrunkGroup) {
        this.newCircuitIDTrunkGroup = newCircuitIDTrunkGroup;
    }

    @JsonProperty("Existing Primary/Secondary Trunk Group Change")
    public String getExistingPrimarySecondaryTrunkGroupChange() {
        return existingPrimarySecondaryTrunkGroupChange;
    }

    @JsonProperty("Existing Primary/Secondary Trunk Group Change")
    public void setExistingPrimarySecondaryTrunkGroupChange(String existingPrimarySecondaryTrunkGroupChange) {
        this.existingPrimarySecondaryTrunkGroupChange = existingPrimarySecondaryTrunkGroupChange;
    }

    @JsonProperty("RTBH")
    public String getRTBH() {
        return rTBH;
    }

    @JsonProperty("RTBH")
    public void setRTBH(String rTBH) {
        this.rTBH = rTBH;
    }

    @JsonProperty("Community Based Routing/Policy Based Routing")
    public String getCommunityBasedRoutingPolicyBasedRouting() {
        return communityBasedRoutingPolicyBasedRouting;
    }

    @JsonProperty("Community Based Routing/Policy Based Routing")
    public void setCommunityBasedRoutingPolicyBasedRouting(String communityBasedRoutingPolicyBasedRouting) {
        this.communityBasedRoutingPolicyBasedRouting = communityBasedRoutingPolicyBasedRouting;
    }

    @JsonProperty("Route-Map")
    public String getRouteMap() {
        return routeMap;
    }

    @JsonProperty("Route-Map")
    public void setRouteMap(String routeMap) {
        this.routeMap = routeMap;
    }

    @JsonProperty("Next Hop")
    public String getNextHop() {
        return nextHop;
    }

    @JsonProperty("Next Hop")
    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

    @JsonProperty("VRF Name")
    public String getVRFName() {
        return vRFName;
    }

    @JsonProperty("VRF Name")
    public void setVRFName(String vRFName) {
        this.vRFName = vRFName;
    }

    @JsonProperty("Route Changes")
    public String getRouteChanges() {
        return routeChanges;
    }

    @JsonProperty("Route Changes")
    public void setRouteChanges(String routeChanges) {
        this.routeChanges = routeChanges;
    }

    @JsonProperty("Load Balancing")
    public String getLoadBalancing() {
        return loadBalancing;
    }

    @JsonProperty("Load Balancing")
    public void setLoadBalancing(String loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    @JsonProperty(" TG Sequence Change")
    public String getTGSequenceChange() {
        return tGSequenceChange;
    }

    @JsonProperty(" TG Sequence Change")
    public void setTGSequenceChange(String tGSequenceChange) {
        this.tGSequenceChange = tGSequenceChange;
    }

    @JsonProperty("Routing Label Change")
    public String getRoutingLabelChange() {
        return routingLabelChange;
    }

    @JsonProperty("Routing Label Change")
    public void setRoutingLabelChange(String routingLabelChange) {
        this.routingLabelChange = routingLabelChange;
    }

    @JsonProperty("Route Block")
    public String getRouteBlock() {
        return routeBlock;
    }

    @JsonProperty("Route Block")
    public void setRouteBlock(String routeBlock) {
        this.routeBlock = routeBlock;
    }

    @JsonProperty("SIP Request Details")
    public String getSIPRequestDetails() {
        return sIPRequestDetails;
    }

    @JsonProperty("SIP Request Details")
    public void setSIPRequestDetails(String sIPRequestDetails) {
        this.sIPRequestDetails = sIPRequestDetails;
    }

    @JsonProperty("SNMP Server IP")
    public String getSNMPServerIP() {
        return sNMPServerIP;
    }

    @JsonProperty("SNMP Server IP")
    public void setSNMPServerIP(String sNMPServerIP) {
        this.sNMPServerIP = sNMPServerIP;
    }

    @JsonProperty("Community String")
    public String getCommunityString() {
        return communityString;
    }

    @JsonProperty("Community String")
    public void setCommunityString(String communityString) {
        this.communityString = communityString;
    }

    @JsonProperty("VLan ID")
    public String getVLanID() {
        return vLanID;
    }

    @JsonProperty("VLan ID")
    public void setVLanID(String vLanID) {
        this.vLanID = vLanID;
    }

    @JsonProperty("Customer owned AS Number")
    public String getCustomerOwnedASNumber() {
        return customerOwnedASNumber;
    }

    @JsonProperty("Customer owned AS Number")
    public void setCustomerOwnedASNumber(String customerOwnedASNumber) {
        this.customerOwnedASNumber = customerOwnedASNumber;
    }

    @JsonProperty("BGP Policy Rejection Reason")
    public String getBGPPolicyRejectionReason() {
        return bGPPolicyRejectionReason;
    }

    @JsonProperty("BGP Policy Rejection Reason")
    public void setBGPPolicyRejectionReason(String bGPPolicyRejectionReason) {
        this.bGPPolicyRejectionReason = bGPPolicyRejectionReason;
    }

    @JsonProperty("BGP Details")
    public String getBGPDetails() {
        return bGPDetails;
    }

    @JsonProperty("BGP Details")
    public void setBGPDetails(String bGPDetails) {
        this.bGPDetails = bGPDetails;
    }

    @JsonProperty("BGP Policy Agree")
    public String getBGPPolicyAgree() {
        return bGPPolicyAgree;
    }

    @JsonProperty("BGP Policy Agree")
    public void setBGPPolicyAgree(String bGPPolicyAgree) {
        this.bGPPolicyAgree = bGPPolicyAgree;
    }

    @JsonProperty("Route-Object")
    public String getRouteObject() {
        return routeObject;
    }

    @JsonProperty("Route-Object")
    public void setRouteObject(String routeObject) {
        this.routeObject = routeObject;
    }

    @JsonProperty("Customer Reference Number")
	public String getCustomerReferenceNumber() {
		return customerReferenceNumber;
	}

    @JsonProperty("Customer Reference Number")
	public void setCustomerReferenceNumber(String customerReferenceNumber) {
		this.customerReferenceNumber = customerReferenceNumber;
	}
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
