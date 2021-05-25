package com.tcl.dias.serviceactivation.activation.factory.base;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.tcl.dias.serviceactivation.activation.netp.beans.ACEHeader;
import com.tcl.dias.serviceactivation.activation.netp.beans.ALUSchedulerPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.ASNumber;
import com.tcl.dias.serviceactivation.activation.netp.beans.AccessControListlEntry;
import com.tcl.dias.serviceactivation.activation.netp.beans.AccessControlList;
import com.tcl.dias.serviceactivation.activation.netp.beans.ActionRequired;
import com.tcl.dias.serviceactivation.activation.netp.beans.AddonFeatures;
import com.tcl.dias.serviceactivation.activation.netp.beans.Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.BFDConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.BGPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.Bandwidth;
import com.tcl.dias.serviceactivation.activation.netp.beans.BandwidthUnit;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumAAAConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumAntennaParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumLastmile;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumSite;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumVLANAttributes;
import com.tcl.dias.serviceactivation.activation.netp.beans.ChannelizedSDHInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.CoS;
import com.tcl.dias.serviceactivation.activation.netp.beans.Customer;
import com.tcl.dias.serviceactivation.activation.netp.beans.CustomerPremiseEquipment;
import com.tcl.dias.serviceactivation.activation.netp.beans.DataMDT;
import com.tcl.dias.serviceactivation.activation.netp.beans.DscpValue;
import com.tcl.dias.serviceactivation.activation.netp.beans.EthernetInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.ExtendedLAN;
import com.tcl.dias.serviceactivation.activation.netp.beans.GVPNService;
import com.tcl.dias.serviceactivation.activation.netp.beans.HBSConfigParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.HSRPProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.HSUConfigParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.IAService;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPAddress;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPAddressList;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPPrecedent;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPService.WanRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPSubnetADValueMap;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPSubnetADValueMap.IpSubnet;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV4Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV6Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.IZO;
import com.tcl.dias.serviceactivation.activation.netp.beans.Interface;
import com.tcl.dias.serviceactivation.activation.netp.beans.LANInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.LastMile;
import com.tcl.dias.serviceactivation.activation.netp.beans.Leg;
import com.tcl.dias.serviceactivation.activation.netp.beans.LoopbackInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.MACAddressVIDMap;
import com.tcl.dias.serviceactivation.activation.netp.beans.MVLANUntaggedMode;
import com.tcl.dias.serviceactivation.activation.netp.beans.MaximumRoutes;
import com.tcl.dias.serviceactivation.activation.netp.beans.NATMode;
import com.tcl.dias.serviceactivation.activation.netp.beans.OSPFRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderCategory;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo2;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderType2;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderType3;
import com.tcl.dias.serviceactivation.activation.netp.beans.PerformIPServiceActivation;
import com.tcl.dias.serviceactivation.activation.netp.beans.PerformTransmissionServiceActivation;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixList;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixListEntry;
import com.tcl.dias.serviceactivation.activation.netp.beans.Protocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.QoS;
import com.tcl.dias.serviceactivation.activation.netp.beans.RPAddress;
import com.tcl.dias.serviceactivation.activation.netp.beans.Radwin5KLastmile;
import com.tcl.dias.serviceactivation.activation.netp.beans.Router;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutesExchanged;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.SNMPParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.SerialInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.ServiceLink;
import com.tcl.dias.serviceactivation.activation.netp.beans.StaticRoutes;
import com.tcl.dias.serviceactivation.activation.netp.beans.StaticRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.Switch;
import com.tcl.dias.serviceactivation.activation.netp.beans.TopologyInfo;
import com.tcl.dias.serviceactivation.activation.netp.beans.TransmissionService;
import com.tcl.dias.serviceactivation.activation.netp.beans.VLANQOSProfile;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPN;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPNSolutionTable;
import com.tcl.dias.serviceactivation.activation.netp.beans.VirtualRouteForwardingServiceInstance;
import com.tcl.dias.serviceactivation.activation.netp.beans.WANInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.WimaxLastMile;
import com.tcl.dias.serviceactivation.activation.services.SatSocService;
import com.tcl.dias.serviceactivation.activation.utils.ActivationUtils;
import com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Broadcast;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.CienaParam;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.GetCLRInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.L2Params;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Protection;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.Attribute;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.VpnIllPortAttributes;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.AluSchedulerPolicy;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.HsrpVrrpProtocol;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.RouterUplinkport;
import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.TxConfiguration;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.VlanQosProfile;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.entities.WimaxLastmile;
import com.tcl.dias.serviceactivation.entity.repository.InterfaceProtocolMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.LmComponentRepository;
import com.tcl.dias.serviceactivation.entity.repository.NeighbourCommunityConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RegexAspathConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceCosCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.TxConfigurationRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;

/**
 * This class is the parent most class with respect to Product and routerType.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public abstract class AbstractConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfiguration.class);

	@Autowired
	protected ServiceDetailRepository serviceDetailRepository;

	@Autowired
	protected IpAddressDetailRepository ipAddressDetailRepository;

	@Autowired
	protected InterfaceProtocolMappingRepository interfaceProtocolMappingRepository;

	@Autowired
	protected PrefixlistConfigRepository prefixListConfigRepository;

	@Autowired
	protected ServiceCosCriteriaRepository serviceCosCriteriaRepository;

	@Autowired
	protected PolicyCriteriaRepository policyCriteriaRepository;

	@Autowired
	protected NeighbourCommunityConfigRepository neighbourCommunityConfigRepository;

	@Autowired
	protected RegexAspathConfigRepository regexAspathConfigRepository;

	@Autowired
	protected TxConfigurationRepository txConfigurationRepository;

	@Autowired
	protected VpnSolutionRepository vpnSolutionRepository;
	
	@Autowired
	protected LmComponentRepository lmComponentRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	SatSocService satSocService;

	/**
	 * This method is the main method for generating the xml for Ip Config
	 * generateIPConfigXml
	 * 
	 * @param serviceId
	 * @return
	 */
	public String generateIPConfigXml(ServiceDetail serviceDetail, String actionMode, String requestId) {
		LOGGER.info("Inside AbstractConfiguration.generateIpConfigXml with input serviceId {}", serviceDetail.getId());
		String response = null;
		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		performIPServiceActivation
				.setHeader(constructAceHeader(serviceDetail.getServiceId(), orderDetail, actionMode, requestId));
		performIPServiceActivation.setOrderDetails(createOrderDetails(serviceDetail, orderDetail));
		response = jaxbObjectToXML(performIPServiceActivation);
		return mapXmlHeaders(response);
	}

	/**
	 * mapXmlHeaders
	 */
	private String mapXmlHeaders(String xml) {
		String header = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n";
		header = header + "<soapenv:Body>";
		String tailer = " </soapenv:Body>\r\n</soapenv:Envelope>";
		xml = header + xml + tailer;
		xml = xml.replace("<header>",
				"<header xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">");
		xml = xml.replace(
				"<ns2:performIPServiceActivation xmlns:ns2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\">",
				"<p:performIPServiceActivation xmlns:p=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\">");
		xml = xml.replace("<orderDetails>",
				"<orderDetails xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">");
		xml = xml.replace("<actionRequired>",
				"<actionRequired xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<requestID>", "<requestID xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<originatingSystem>",
				"<originatingSystem xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<originationTime>",
				"<originationTime xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<authUser>", "<authUser xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<location>", "<location xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<addressLine1>", "<addressLine1 xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<city>", "<city xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<state>", "<state xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<pincode>", "<pincode xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<country>", "<country xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");

		xml = xml.replaceAll("<speed>", "<speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replaceAll("<unit>", "<unit xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");

		xml = xml.replaceAll("<isCEACEConfigurable>",
				"<isCEACEConfigurable xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");
		xml = xml.replaceAll("<wanInterface>",
				"<wanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");
		xml = xml.replaceAll("<lanInterface>",
				"<lanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");
		xml = xml.replaceAll("<snmpServerCommunity>",
				"<snmpServerCommunity xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">");

		xml = xml.replaceAll("<VPN>",
				"<VPN xmlns:out2=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\">");
		xml = xml.replaceAll("<vpnId>", "<vpnId xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<topology>", "<topology xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<leg>", "<leg xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<vpnType>", "<vpnType xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<SolutionID>",
				"<SolutionID xmlns:out2=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\">");
		xml = xml.replaceAll("<PEWANStaticRoutes>",
				"<PEWANStaticRoutes xmlns:out3=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replaceAll("<isEnabled>", "<isEnabled xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">");
		xml = xml.replaceAll("<address>", "<address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">");
		xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
		xml = xml.replace("</ns2:performIPServiceActivation>", "</p:performIPServiceActivation>");
		return xml;
	}

	/**
	 * This method is the main method for generating the xml for RF Config
	 * generateRFConfigXml
	 * 
	 * @param serviceId
	 * @return
	 */
	public String generateRFConfigXml(ServiceDetail serviceDetail, String actionMode, String requestId) {
		LOGGER.info("Inside generateRFConfigXml with input serviceId {}", serviceDetail.getId());
		String response = null;
		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		performIPServiceActivation
				.setHeader(constructAceHeader(serviceDetail.getServiceId(), orderDetail, actionMode, requestId));
		performIPServiceActivation.setOrderDetails(createRfOrderDetails(serviceDetail, orderDetail));
		response = jaxbObjectToXML(performIPServiceActivation);
		return mapXmlHeaders(response);
	}

	public String generateTxConfigXml(ServiceDetail serviceDetail, String actionMode, String requestId, String txType) {
		LOGGER.info("Inside generateTxConfigXml with input serviceId {}", serviceDetail.getId());
		String response = "";
		PerformTransmissionServiceActivation performTransmissionServiceActivation = new PerformTransmissionServiceActivation();
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		ACEHeader header = constructAceHeader(serviceDetail.getServiceId(), orderDetail, actionMode, requestId);
		performTransmissionServiceActivation.setHeader(header);
		OrderInfo2 orderInfo2 = createTxOrderDetails(serviceDetail, orderDetail, txType);

		performTransmissionServiceActivation.setOrderDetails(orderInfo2);
		response = mapXmlHeaders(jaxbObjectToXML(performTransmissionServiceActivation));

		return response;
	}

	/**
	 * 
	 * jaxbObjectToXML - This method is used to marshell the object to XML
	 * 
	 * @param performIPServiceActivation
	 * @return
	 */
	private String jaxbObjectToXML(PerformIPServiceActivation performIPServiceActivation) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(PerformIPServiceActivation.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
			StringWriter sw = new StringWriter();
			m.marshal(performIPServiceActivation, sw);
			xmlString = sw.toString();
			LOGGER.trace("Unmarshelled XML {}", xmlString);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb marshalling ", e);
		}
		return xmlString;
	}

	private String jaxbObjectToXML(PerformTransmissionServiceActivation performTransmissionServiceActivation) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(PerformTransmissionServiceActivation.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
			StringWriter sw = new StringWriter();
			m.marshal(performTransmissionServiceActivation, sw);
			xmlString = sw.toString();
			LOGGER.trace("Unmarshelled XML {}", xmlString);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb marshalling ", e);
		}
		return xmlString;
	}

	private GetCLRInfoResponse jaxbXmlToGetCLRInfoResponse(String inputXml) {
		try {
			JAXBContext context = JAXBContext.newInstance(GetCLRInfoResponse.class);
			Unmarshaller um = context.createUnmarshaller();
			// um.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format
			// XML
			StringReader reader = new StringReader(inputXml);
			return (GetCLRInfoResponse) um.unmarshal(reader);
		} catch (Exception e) {
			LOGGER.error("Error in jaxb unmarshalling ", e);
		}
		return null;
	}

	/**
	 * generateRequestId
	 */
	public String generateRequestId(String serviceUuid) {
		return "RID_" + serviceUuid + "_" + Calendar.getInstance().getTimeInMillis() + "_IASE2E";

	}

	private ACEHeader constructAceHeader(String serviceUuid, OrderDetail orderDetail, String actionMode,
			String requestId) {
		ACEHeader aceHeader = new ACEHeader();
		try {
			aceHeader.setAuthUser("tw_admin");
			/*if (StringUtils.isNotBlank(orderDetail.getOriginatorName()))
				aceHeader.setOriginatingSystem(orderDetail.getOriginatorName());
			else*/
			aceHeader.setOriginatingSystem("OPTIMUS");
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			aceHeader.setOriginationTime(xgcal);
			if (requestId == null) {
				requestId = generateRequestId(serviceUuid);
			}
			aceHeader.setRequestID(requestId);
			aceHeader.getActionRequired()
					.add(actionMode != null ? ActionRequired.fromValue(actionMode) : ActionRequired.CONFIG);
			LOGGER.info("Request Id Generated is {}", aceHeader.getRequestID());
		} catch (Exception e) {
			LOGGER.error("Error in constructing Header ", e);
		}
		return aceHeader;
	}

	protected OrderInfo2 createTxOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail, String txType) {
		String orderType=OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);
		String orderCategory=OrderCategoryMapping.getOrderCategory(serviceDetail, orderDetail);


		OrderInfo2 orderInfo2 = new OrderInfo2();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			orderInfo2.setServiceId(serviceDetail.getServiceId());
			
			/*if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("shifting")){
				orderInfo2.setOrderType(OrderType3.fromValue("PARALLEL_UPGRADE"));
			}else if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("downgrade")){
				orderInfo2.setOrderType(OrderType3.fromValue("PARALLEL_DOWNGRADE"));
			}else if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("upgrade")){
				orderInfo2.setOrderType(OrderType3.fromValue("PARALLEL_UPGRADE"));
			}else*/ if (StringUtils.isNotBlank(orderType) && "NEW".equalsIgnoreCase(orderType)){
				LOGGER.info("AbstractConfig::NEW");
				orderInfo2.setOrderType(OrderType3.fromValue("NEW"));
			} else if("ADD_SITE".equals(orderCategory)){
				LOGGER.info("AbstractConfig::ADDSITE");
				orderInfo2.setOrderType(OrderType3.fromValue("NEW"));
			} else if("NPL".equalsIgnoreCase(serviceDetail.getServiceType()) && serviceDetail.getOrderSubCategory()!=null &&
					serviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
				LOGGER.info("AbstractConfig::NPL:PARALLEL");
				orderInfo2.setOrderType(OrderType3.fromValue("NEW"));
			}
			
				
			TxConfiguration txConfig = txConfigurationRepository
					.findFirstByServiceIdAndStatusOrderByIdDesc(serviceDetail.getServiceId(), "ISSUED");
			
			
			createTxServiceDetails(serviceDetail, txConfig,orderInfo2);
		
			if (StringUtils.isNotBlank(orderDetail.getCopfId()))
				orderInfo2.setCopfId(orderDetail.getCopfId());
			if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
				orderInfo2.setOldServiceId(serviceDetail.getOldServiceId());
			}
			/*String orderCategory = StringUtils.trimToEmpty(orderDetail.getOrderCategory());
			if (StringUtils.isBlank(orderCategory))
				orderCategory = "CUSTOMER_ORDER";*/
			orderInfo2.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
			if(serviceDetail.getServiceType() !=null && "NPL".equalsIgnoreCase(serviceDetail.getServiceType())) {			
				orderInfo2.setPortBandwidth((serviceDetail.getServiceBandwidth().intValue())+"M");
			}else {
				orderInfo2.setPortBandwidth("NA");
				if (serviceDetail.getServiceBandwidth() != null)
					orderInfo2.setServiceBandwidth(ActivationUtils.toString(serviceDetail.getServiceBandwidth()));
			}
			//orderInfo2.setCustomerDetails(createTxCustomerDetails(orderDetail));

			OrderInfo2 mplsOrderInfo = new OrderInfo2();
			BeanUtils.copyProperties(orderInfo2, mplsOrderInfo);

			if (txType.toLowerCase().contains("tx") || txType.toLowerCase().contains("sdh")
					|| txType.toLowerCase().contains("access")) {
				orderInfo2.setIsSDHConfigurable(true);
			} else if (txType.toLowerCase().contains("mpls")) {
				orderInfo2.setIsMPLSTPConfigurable(true);
			}
		}
		return orderInfo2;

	}

	/**
	 * 
	 * createInitiateOrder
	 * 
	 * @return
	 */
	protected OrderInfo3 createOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		LOGGER.info("Inside AbstractConfiguration.createOrderDetails with input serviceId {}", serviceDetail.getId());
		String orderType=OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);

		OrderInfo3 orderInfo3 = new OrderInfo3();
		if (serviceDetail.getServiceId() != null)
			orderInfo3.setServiceId(serviceDetail.getServiceId());
		if (serviceDetail != null)
			orderInfo3.setService(createServiceDetails(serviceDetail, orderDetail));
		if (orderDetail.getCopfId() != null)
			orderInfo3.setCopfId(orderDetail.getCopfId());
		if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
			orderInfo3.setOldServiceId(serviceDetail.getOldServiceId());
		}
		if(Objects.nonNull(serviceDetail.getMulticastings()) && !serviceDetail.getMulticastings().isEmpty() && "GVPN".equals(serviceDetail.getServiceType())){
			LOGGER.info("Current MultiCast exists");
			Optional<Multicasting> multiCastOptional=serviceDetail.getMulticastings().stream().findFirst();
			if(multiCastOptional.isPresent()){
				Multicasting multiCast=multiCastOptional.get();
				AddonFeatures addOnFeature = new AddonFeatures();
				com.tcl.dias.serviceactivation.activation.netp.beans.Multicasting netpMulticasting= new com.tcl.dias.serviceactivation.activation.netp.beans.Multicasting();
				netpMulticasting.setWanPIMMode(multiCast.getWanPimMode());
				netpMulticasting.setType(multiCast.getType());
				netpMulticasting.setRPLocation(multiCast.getRpLocation());
				netpMulticasting.setAutoDiscoveryOption(multiCast.getAutoDiscoveryOption());	
				//DataMDT
				DataMDT dataMDT = new DataMDT();
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(multiCast.getDataMdt());
				dataMDT.setIPAddress(ipv4Address);
				dataMDT.setThreshold(1);
				netpMulticasting.setDataMDT(dataMDT);
				//DefaultMDT
				IPV4Address ipv4DefaultAddress = new IPV4Address();
				ipv4DefaultAddress.setAddress(multiCast.getDefaultMdt());
				netpMulticasting.setDefaultMDT(ipv4DefaultAddress);
				//RpAddress
				List<RPAddress> rpAddressList = new ArrayList<>();
				RPAddress rpAddress = new RPAddress();
				IPV4Address ipAddress = new IPV4Address();
				ipAddress.setAddress(multiCast.getRpAddress());
				rpAddress.setIPAddress(ipAddress);
				rpAddressList.add(rpAddress);
				netpMulticasting.setRpAddress(rpAddressList);
				addOnFeature.setMulticasting(netpMulticasting);
				LOGGER.info("Set AddOnFeature");
				orderInfo3.setAddOns(addOnFeature);
			}
		}else if("IZOPC".equalsIgnoreCase(serviceDetail.getProductName())) {
				LOGGER.info("IZOPC NetpDetails for Service Id::{}",serviceDetail.getServiceId());
				AddonFeatures addOnFeature = new AddonFeatures();
				IZO izoDetails = new IZO();
				izoDetails.setIzoPrivate(true);
				ScServiceAttribute cloudTypeAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(serviceDetail.getScServiceDetailId(), "Cloud Type");
				if(cloudTypeAttribute!=null && cloudTypeAttribute.getAttributeValue()!=null) {
					LOGGER.info("IZOPC CloudType exists for Service Id::{}",serviceDetail.getServiceId());
					izoDetails.setCloudType(cloudTypeAttribute.getAttributeValue());
				}
				ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(serviceDetail.getScServiceDetailId(), "Cloud Provider");
				if(cloudProviderAttribute!=null) {
					LOGGER.info("IZOPC CloudProvider exists for Service Id::{}",serviceDetail.getServiceId());
					if("AWS Direct Connect".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())) {
						izoDetails.setCloudProvider("Amazon Web Services");
					}else if("Microsoft Azure Express Route".equalsIgnoreCase(cloudProviderAttribute.getAttributeValue())) {
						izoDetails.setCloudProvider("Microsoft Azure");
					}else {
						izoDetails.setCloudProvider(cloudProviderAttribute.getAttributeValue());
					}
				}
				addOnFeature.setIZODetails(izoDetails);
				LOGGER.info("Set AddOnFeature For IZOPC");
				orderInfo3.setAddOns(addOnFeature);
		}
		/*if (StringUtils.isNotBlank(orderDetail.getOrderCategory())){
			if("ADD_SITE".equals(orderDetail.getOrderCategory())){
				orderDetail.setOrderCategory("CUSTOMER_ORDER");
			}*/
			orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
		//}
		if (orderDetail.getAsdOpptyFlag() != null)
			orderInfo3.setOptyBidCategory(orderDetail.getOptyBidCategory());
		orderInfo3.setIsDowntimeRequired(false);
		orderInfo3.setCustomerDetails(createCustomerDetails(orderDetail, serviceDetail,false));
		/*if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("shifting")){
			orderInfo3.setOrderType(OrderType2.fromValue("PARALLEL_UPGRADE"));
			orderInfo3.setIsDowntimeRequired(true);
		}else if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("downgrade")){
			orderInfo3.setOrderType(OrderType2.fromValue("PARALLEL_DOWNGRADE"));
			orderInfo3.setIsDowntimeRequired(true);
		}else if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("upgrade")){
			orderInfo3.setOrderType(OrderType2.fromValue("PARALLEL_UPGRADE"));
			orderInfo3.setIsDowntimeRequired(true);
		}else*/ if (StringUtils.isNotBlank(orderType) && "NEW".equalsIgnoreCase(orderType)){
			orderInfo3.setOrderType(OrderType2.fromValue(orderType));
		} 
		return orderInfo3;

	}

	protected OrderInfo3 createRfOrderDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		
		String orderCategory=OrderCategoryMapping.getOrderCategory(serviceDetail, orderDetail);
		String orderType=OrderCategoryMapping.getOrderType(serviceDetail, orderDetail);

		OrderInfo3 orderInfo3 = new OrderInfo3();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			orderInfo3.setServiceId(serviceDetail.getServiceId());
			orderInfo3.setService(createRfServiceDetails(serviceDetail, orderDetail));
			if (StringUtils.isNotBlank(orderDetail.getCopfId()))
				orderInfo3.setCopfId(orderDetail.getCopfId());
			if (StringUtils.isNotBlank(serviceDetail.getOldServiceId())) {
				orderInfo3.setOldServiceId(serviceDetail.getOldServiceId());
			}
			//if (StringUtils.isNotBlank(orderDetail.getOrderCategory()))
			orderInfo3.setOrderCategory(OrderCategory.fromValue("CUSTOMER_ORDER"));
			orderInfo3.setIsDowntimeRequired(false);
			orderInfo3.setCustomerDetails(createCustomerDetails(orderDetail, serviceDetail,true));
			/*if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("shifting")){
				orderInfo3.setOrderType(OrderType2.fromValue("PARALLEL_UPGRADE"));
				orderInfo3.setIsDowntimeRequired(true);
			}else if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("downgrade")){
				orderInfo3.setOrderType(OrderType2.fromValue("PARALLEL_DOWNGRADE"));
				orderInfo3.setIsDowntimeRequired(true);
			}else if (Objects.nonNull(orderDetail.getOrderSubCategory()) && orderDetail.getOrderSubCategory().toLowerCase().contains("parallel") && orderDetail.getOrderSubCategory().toLowerCase().contains("upgrade")){
				orderInfo3.setOrderType(OrderType2.fromValue("PARALLEL_UPGRADE"));
				orderInfo3.setIsDowntimeRequired(true);
			}else */if (StringUtils.isNotBlank(orderType) && "NEW".equalsIgnoreCase(orderType)){
				orderInfo3.setOrderType(OrderType2.fromValue(orderType));
			} 
		}
		return orderInfo3;

	}

	protected TransmissionService createTxServiceDetails(ServiceDetail serviceDetail, TxConfiguration txConfig,OrderInfo2 orderInfo2) {
		TransmissionService txService = new TransmissionService();
		Customer customer = new Customer();
		if (StringUtils.isNotBlank(serviceDetail.getServiceId())) {
			String clrResponse = txConfig.getClrResponse();
			GetCLRInfoResponse getClr = jaxbXmlToGetCLRInfoResponse(clrResponse);
			if (getClr != null) {
				for (com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Worker cWorker : getClr.getResponse()
						.getService().getWorker()) {
					com.tcl.dias.serviceactivation.activation.netp.beans.Worker worker = new com.tcl.dias.serviceactivation.activation.netp.beans.Worker();
					BeanUtils.copyProperties(cWorker, worker);
					txService.getWorker().add(worker);
				}
				for (Protection cProtection : getClr.getResponse().getService().getProtections()) {
					com.tcl.dias.serviceactivation.activation.netp.beans.Protection protection = new com.tcl.dias.serviceactivation.activation.netp.beans.Protection();
					BeanUtils.copyProperties(cProtection, protection);
					txService.getProtections().add(protection);
				}
				if(getClr.getResponse().getService().getL2Params() !=null) {
					for (L2Params l2Params : getClr.getResponse().getService().getL2Params()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.L2Params l2Param = new com.tcl.dias.serviceactivation.activation.netp.beans.L2Params();
						BeanUtils.copyProperties(l2Params, l2Param);
						txService.getL2Params().add(l2Param);
					}
				}
				if(getClr.getResponse().getService().getCienaParam() !=null) {
					for (CienaParam cienaParams : getClr.getResponse().getService().getCienaParam()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.CienaParam cienaParam = new com.tcl.dias.serviceactivation.activation.netp.beans.CienaParam();
						BeanUtils.copyProperties(cienaParams, cienaParam);
						txService.getCienaParam().add(cienaParam);
					}
				}
				if(getClr.getResponse().getService().getTopologies() !=null) {
					for (com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Topology topologies : getClr.getResponse().getService().getTopologies()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.Topology topology = new com.tcl.dias.serviceactivation.activation.netp.beans.Topology();
						BeanUtils.copyProperties(topologies, topology);
						txService.getTopologies().add(topology);
					}
				}
				if(getClr.getResponse().getService().getBroadcast() !=null) {
					for (Broadcast broadcasts : getClr.getResponse().getService().getBroadcast()) {
						com.tcl.dias.serviceactivation.activation.netp.beans.Broadcast broadcast = new com.tcl.dias.serviceactivation.activation.netp.beans.Broadcast();
						BeanUtils.copyProperties(broadcasts, broadcast);
						txService.getBroadcast().add(broadcast);
					}
				}
				
				com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Customer getClrCustomer = getClr.getResponse().getCustomer();
				if(getClrCustomer!=null) {
					BeanUtils.copyProperties(getClrCustomer, customer);
				}
			}
			txService.setIsACEActionRequired(true);
			txService.setIsNOCActionRequired(false);
			txService.setVerifyAndActivate(true);
		}
		
		orderInfo2.setService(txService);
		orderInfo2.setCustomerDetails(customer);
		return txService;
	}

	/*protected Customer createTxCustomerDetails(OrderDetail orderDetail) {
		Customer customerDetail = new Customer();
		if (Objects.nonNull(orderDetail.getCustCuId()))
			customerDetail.setId(orderDetail.getCustCuId().toString());
		if (StringUtils.isNotBlank(orderDetail.getCustomerName()))
			customerDetail.setName(orderDetail.getCustomerName());
		if (StringUtils.isNotBlank(orderDetail.getCustomerType()))
			customerDetail.setType(orderDetail.getCustomerType());
		return customerDetail;
	}*/

	protected Customer createCustomerDetails(OrderDetail orderDetail, ServiceDetail serviceDetail,boolean rf) {
		Customer customerDetail = new Customer();
		if (Objects.nonNull(orderDetail.getCustCuId()))
			customerDetail.setId(orderDetail.getCustCuId().toString());
		if (StringUtils.isNotBlank(orderDetail.getCustomerName()))
			customerDetail.setName(orderDetail.getCustomerName());
		if (StringUtils.isNotBlank(orderDetail.getCustomerType()))
			customerDetail.setType(orderDetail.getCustomerType());
		if (StringUtils.isNotBlank(orderDetail.getCustomerCategory()))
			customerDetail.setCategory(orderDetail.getCustomerCategory());
		if (StringUtils.isNotBlank(orderDetail.getCustomerEmail()))
			customerDetail.setEmailID(orderDetail.getCustomerEmail());
		Address address = new Address();
		if (StringUtils.isNotBlank(serviceDetail.getAddressLine1()))
			address.setAddressLine1(serviceDetail.getAddressLine1());
		if (StringUtils.isNotBlank(serviceDetail.getAddressLine2()))
			address.setAddressLine2(serviceDetail.getAddressLine2());
		if (StringUtils.isNotBlank(serviceDetail.getCity()))
			address.setCity(serviceDetail.getCity());
		if (StringUtils.isNotBlank(serviceDetail.getState()))
			address.setState(serviceDetail.getState());
		if (StringUtils.isNotBlank(serviceDetail.getPincode()))
			address.setPincode(serviceDetail.getPincode());
		if (StringUtils.isNotBlank(serviceDetail.getCountry()))
			address.setCountry(serviceDetail.getCountry());
		if (StringUtils.isNotBlank(serviceDetail.getCity()))
			address.setLocation(serviceDetail.getCity());
		customerDetail.setAddress(address);
		if(!rf) {
		if (StringUtils.isNotBlank(orderDetail.getAluCustomerId()))
			customerDetail.setALUCustomerID(orderDetail.getAluCustomerId());// ALU SPEC
		}
		return customerDetail;
	}

	protected OrderInfo3.Service createRfServiceDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		OrderInfo3.Service service = new OrderInfo3.Service();
		Set<LmComponent> lmComponents = serviceDetail.getLmComponents();
		for (LmComponent lmComponent : lmComponents) {
			createRfCambium(serviceDetail, service, lmComponent);
			createRfRadwin(serviceDetail, service, lmComponent);
			createRfWimax(serviceDetail, service, lmComponent);
		}
		return service;
	}

	/**
	 * createRfRadwin
	 * 
	 * @param service
	 * @param lmComponent
	 */
	protected void createRfRadwin(ServiceDetail serviceDetail, OrderInfo3.Service service, LmComponent lmComponent) {
		for (RadwinLastmile radwinLastmile : lmComponent.getRadwinLastmiles()) {
			Radwin5KLastmile radwin5kLastMile = new Radwin5KLastmile();
			if("ECO INTERNET".equalsIgnoreCase(serviceDetail.getServiceSubtype())) {
				radwin5kLastMile.setServiceSubType("STDILL");
			}else {
				radwin5kLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			}
			radwin5kLastMile.setServiceType(serviceDetail.getServiceType());

			if (serviceDetail.getBurstableBw() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
					}
				}
				radwin5kLastMile.setPortSpeed(portBandwidth);
			} else {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
					}
				}
				radwin5kLastMile.setPortSpeed(portBandwidth);
			}
			if (StringUtils.isNotBlank(serviceDetail.getNetpRefid()))
				radwin5kLastMile.setInstanceID(serviceDetail.getNetpRefid());
			if (StringUtils.isNotBlank(radwinLastmile.getBtsIp()))
				radwin5kLastMile.setBTSIP(radwinLastmile.getBtsIp());
			if (StringUtils.isNotBlank(radwinLastmile.getBtsName()))
				radwin5kLastMile.setBSName(radwinLastmile.getBtsName());
			HSUConfigParameters hsuConfigParameters = new HSUConfigParameters();
			if (StringUtils.isNotBlank(radwinLastmile.getSiteName()))
				hsuConfigParameters.setName(radwinLastmile.getSiteName());
			if (StringUtils.isNotBlank(radwinLastmile.getSectorId()))
				hsuConfigParameters.setSectorID(radwinLastmile.getSectorId());
			if (StringUtils.isNotBlank(radwinLastmile.getFrequency()))
				hsuConfigParameters.setFrequency(radwinLastmile.getFrequency());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteLat()))
				hsuConfigParameters.setLatitude(radwinLastmile.getSiteLat().trim());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteLong()))
				hsuConfigParameters.setLongitude(radwinLastmile.getSiteLong().trim());
			if (StringUtils.isNotBlank(radwinLastmile.getSiteContact()))
				hsuConfigParameters.setSiteContact(radwinLastmile.getSiteContact());
			if (StringUtils.isNotBlank(radwinLastmile.getCustomerLocation()))
				hsuConfigParameters.setCustomerLocation(radwinLastmile.getCustomerLocation());
			if (StringUtils.isNotBlank(radwinLastmile.getHsuIp())) {
				IPAddress hscuMgmtIp = new IPAddress();
				hscuMgmtIp.setAddress(radwinLastmile.getHsuIp());
				hsuConfigParameters.setHsuMgmtIP(hscuMgmtIp);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getHsuSubnet())) {
				IPAddress hscuMgmtSubnet = new IPAddress();
				hscuMgmtSubnet.setAddress(radwinLastmile.getHsuSubnet());
				hsuConfigParameters.setHsuMgmtSubnet(hscuMgmtSubnet);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getGatewayIp())) {
				IPAddress hscuGatewayIp = new IPAddress();
				hscuGatewayIp.setAddress(radwinLastmile.getGatewayIp());
				hsuConfigParameters.setHsuGatewayIP(hscuGatewayIp);
			}
			if (StringUtils.isNotBlank(radwinLastmile.getHsuMacAddr()))
				hsuConfigParameters.setHsuMacAddress(radwinLastmile.getHsuMacAddr().replaceAll(" ", ":").replaceAll("-", ":"));
			if (StringUtils.isNotBlank(radwinLastmile.getMgmtVlanid()))
				hsuConfigParameters.setManagementVlanID(ActivationUtils.toInteger(radwinLastmile.getMgmtVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getNtpServerIp())) {
				IPAddress ntpServerIp = new IPAddress();
				ntpServerIp.setAddress(radwinLastmile.getNtpServerIp());
				hsuConfigParameters.setNtpServerIP(ntpServerIp);
			}
			if (radwinLastmile.getCustomerLocation() != null)
				hsuConfigParameters.setCustomerLocation(radwinLastmile.getCustomerLocation());
			if (radwinLastmile.getSiteName() != null)
				hsuConfigParameters.setName(radwinLastmile.getSiteName());
			if (StringUtils.isNotBlank(radwinLastmile.getNtpOffset()))
				hsuConfigParameters.setNtpOffset(ActivationUtils.toInteger(radwinLastmile.getNtpOffset()));
			Protocol protocol = new Protocol();
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolSnmp()))
				protocol.setSnmp(radwinLastmile.getProtocolSnmp());
	/*		if (StringUtils.isNotBlank(radwinLastmile.getProtocolWebinterface()))
				protocol.setWebAccess(radwinLastmile.getProtocolWebinterface());
			if (StringUtils.isNotBlank(radwinLastmile.getProtocolTelnet()))
				protocol.setTelnet(radwinLastmile.getProtocolTelnet());*/
			hsuConfigParameters.setProtocol(protocol);
			if (StringUtils.isNotBlank(radwinLastmile.getReqd_tx_power()))
				hsuConfigParameters.setRequiredTxPower(ActivationUtils.toInteger(radwinLastmile.getReqd_tx_power()));
			if (StringUtils.isNotBlank(radwinLastmile.getEthernet_Port_Config()))
				hsuConfigParameters.setEthernetPortConfiguration(radwinLastmile.getEthernet_Port_Config());
			radwin5kLastMile.setHSUConfigParameters(hsuConfigParameters);
			HBSConfigParameters hbsConfigParameters = new HBSConfigParameters();
			if (StringUtils.isNotBlank(radwinLastmile.getVlanMode()))
				hbsConfigParameters.setVlanMode((!radwinLastmile.getVlanMode().contains("NOT_APPLICABLE"))
						? radwinLastmile.getVlanMode().toLowerCase()
						: radwinLastmile.getVlanMode());
			if (StringUtils.isNotBlank(radwinLastmile.getDataVlanid()))
				hbsConfigParameters.setDataVlanID(ActivationUtils.toInteger(radwinLastmile.getDataVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getDataVlanPriority()))
				hbsConfigParameters
						.setDataVlanPriority(ActivationUtils.toInteger(radwinLastmile.getDataVlanPriority()));
			if (StringUtils.isNotBlank(radwinLastmile.getAllowedVlanid()))
				hbsConfigParameters.setAllowedVlanID(ActivationUtils.toInteger(radwinLastmile.getAllowedVlanid()));
			if (StringUtils.isNotBlank(radwinLastmile.getHsuEgressTraffic()))
				hbsConfigParameters.setHsuEgressTraffic(radwinLastmile.getHsuEgressTraffic().toUpperCase());
			if (StringUtils.isNotBlank(radwinLastmile.getHsuIngressTraffic()))
				hbsConfigParameters.setHsuIngressTraffic(radwinLastmile.getHsuIngressTraffic().toUpperCase());
			if (radwinLastmile.getMirUl() != null)
				hbsConfigParameters.setMirUL(Math.round(radwinLastmile.getMirUl()));
			if (radwinLastmile.getMirDl() != null)
				hbsConfigParameters.setMirDL(Math.round(radwinLastmile.getMirDl()));
			if (radwinLastmile.getUntagVlanId() != null)
				hbsConfigParameters.setUntagVlanID(ActivationUtils.getBooleanValue(radwinLastmile.getUntagVlanId()));
			radwin5kLastMile.setHBSConfigParameters(hbsConfigParameters);
			service.setRadwin5KLastmile(radwin5kLastMile);
		}
	}

	/**
	 * 
	 * createRfWimax
	 * 
	 * @param serviceDetail
	 * @param service
	 * @param lmComponent
	 */
	protected void createRfWimax(ServiceDetail serviceDetail, OrderInfo3.Service service, LmComponent lmComponent) {
		for (WimaxLastmile wimaxLastMile : lmComponent.getWimaxLastmiles()) {
			WimaxLastMile netWimaxLastMile = new WimaxLastMile();
			if("ECO INTERNET".equalsIgnoreCase(serviceDetail.getServiceSubtype())) {
				netWimaxLastMile.setServiceSubType("STDILL");
			}else {
				netWimaxLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			}
			netWimaxLastMile.setServiceType(serviceDetail.getServiceType());
			if (serviceDetail.getBurstableBw() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw() * 1024);
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getBurstableBw());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
					}
				}
				netWimaxLastMile.setPortSpeed(portBandwidth);
			} else {
				Bandwidth portBandwidth = new Bandwidth();
				if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit())) {
					if (ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit())
							.equals(BandwidthUnit.MBPS)) {
						portBandwidth.setSpeed(Math.abs(ActivationUtils.toFloat(serviceDetail.getServiceBandwidthUnit()) * 1024));
						portBandwidth.setUnit(BandwidthUnit.MBPS);
					} else {
						portBandwidth.setSpeed(serviceDetail.getServiceBandwidth());
						portBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
					}
				}
				netWimaxLastMile.setPortSpeed(portBandwidth);
			}

			if (StringUtils.isNotBlank(serviceDetail.getNetpRefid()))
				netWimaxLastMile.setInstanceID(serviceDetail.getNetpRefid());
			if (StringUtils.isNotBlank(wimaxLastMile.getBtsName()))
				netWimaxLastMile.setBSName(wimaxLastMile.getBtsName());
			if (StringUtils.isNotBlank(wimaxLastMile.getBtsIp()))
				netWimaxLastMile.setBTSIP(wimaxLastMile.getBtsIp());
			if (StringUtils.isNotBlank(wimaxLastMile.getUniqueName()))
				netWimaxLastMile.setUniqueName(wimaxLastMile.getUniqueName());
			if (StringUtils.isNotBlank(wimaxLastMile.getSumacAddress()))
				netWimaxLastMile.setSUMACAddress(wimaxLastMile.getSumacAddress());
			if (StringUtils.isNotBlank(wimaxLastMile.getSuMgmtIp())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(wimaxLastMile.getSuMgmtIp());
				netWimaxLastMile.setSUMgmtIP(ipv4Address);
			}
			if (StringUtils.isNotBlank(wimaxLastMile.getSuMgmtSubnet())) {
				IPV4Address ipv4SuMgSubAddress = new IPV4Address();
				ipv4SuMgSubAddress.setAddress(wimaxLastMile.getSuMgmtSubnet());
				netWimaxLastMile.setSUMgmtSubnet(ipv4SuMgSubAddress);
			}
			if (StringUtils.isNotBlank(wimaxLastMile.getGatewayMgmtIp())) {
				IPV4Address ipv4SuGateAddress = new IPV4Address();
				ipv4SuGateAddress.setAddress(wimaxLastMile.getGatewayMgmtIp());
				netWimaxLastMile.setGatewayMgmtIP(ipv4SuGateAddress);
			}
			if (StringUtils.isNotBlank(wimaxLastMile.getHomeRegion()))
				netWimaxLastMile.setHomeRegion(wimaxLastMile.getHomeRegion());
			if (StringUtils.isNotBlank(wimaxLastMile.getDescription1()))
				netWimaxLastMile.setDescription1(wimaxLastMile.getDescription1());
			if (StringUtils.isNotBlank(wimaxLastMile.getDescription2()))
				netWimaxLastMile.setDescription2(wimaxLastMile.getDescription2());

			String provisioningMode = wimaxLastMile.getProvisioningMode();
			WimaxLastMile.ProvisioningMode provMode = new WimaxLastMile.ProvisioningMode();
			MVLANUntaggedMode mvlanUntaggedMode = new MVLANUntaggedMode();
			if (wimaxLastMile.getSsvlanTagging() != null)
				mvlanUntaggedMode
						.setSSVLANTaggingEnabled(ActivationUtils.getBooleanValue(wimaxLastMile.getSsvlanTagging()));
			NATMode natMode = new NATMode();
			if (StringUtils.isNotBlank(wimaxLastMile.getCuststaticWanipGateway()))
				natMode.setStaticCustomerWANIPGateway(wimaxLastMile.getCuststaticWanipGateway());
			if (StringUtils.isNotBlank(wimaxLastMile.getCuststaticWanipMask()))
				natMode.setStaticCustomerWANIPMask(wimaxLastMile.getCuststaticWanipMask());
			if (StringUtils.isNotBlank(wimaxLastMile.getCuststaticWanip()))
				natMode.setStaticCustomerWANIP(wimaxLastMile.getCuststaticWanip());

			for (VlanQosProfile vlanQosProf : wimaxLastMile.getVlanQosProfiles()) {
				VLANQOSProfile vlanQos = new VLANQOSProfile();
				if (vlanQosProf.getVlanId() != null)
					vlanQos.setVlanID(vlanQosProf.getVlanId());
				if (StringUtils.isNotBlank(vlanQosProf.getUpstreamQosProfile()))
					vlanQos.setUpstreamQoSProfile(vlanQosProf.getUpstreamQosProfile());
				if (StringUtils.isNotBlank(vlanQosProf.getDownstreamQosProfile()))
					vlanQos.setDownstreamQoSProfile(vlanQosProf.getDownstreamQosProfile());
				mvlanUntaggedMode.getMvlanQoSProfile().add(vlanQos);
				natMode.setNatQoSProfile(vlanQos);

			}
			if (provisioningMode != null && provisioningMode.toLowerCase().contains("untagged")) {
				provMode.setMvlanUntaggedmode(mvlanUntaggedMode);
			} else {
				provMode.setNatmode(natMode);
			}
			netWimaxLastMile.setProvisioningMode(provMode);
			service.setWimaxLastmile(netWimaxLastMile);
		}
	}

	/**
	 * createRfCambium
	 * 
	 * @param serviceDetail
	 * @param service
	 * @param lmComponent
	 */
	protected void createRfCambium(ServiceDetail serviceDetail, OrderInfo3.Service service, LmComponent lmComponent) {
		for (com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile camLastMile : lmComponent
				.getCambiumLastmiles()) {
			// createRFProductConfig(serviceDetail, service);
			// TODO -> LM COMPON
			CambiumLastmile cambiumLastMile = new CambiumLastmile();
			if("ECO INTERNET".equalsIgnoreCase(serviceDetail.getServiceSubtype())) {
				cambiumLastMile.setServiceSubType("STDILL");
			}else {
				cambiumLastMile.setServiceSubType(serviceDetail.getServiceSubtype());
			}
			cambiumLastMile.setServiceType(serviceDetail.getServiceType());
			if (StringUtils.isNotBlank(serviceDetail.getNetpRefid()))
				cambiumLastMile.setInstanceID(serviceDetail.getNetpRefid());
			if (StringUtils.isNotBlank(camLastMile.getBsName()))
				cambiumLastMile.setBSName(camLastMile.getBsName());
			if (StringUtils.isNotBlank(camLastMile.getBsIp()))
				cambiumLastMile.setBTSIP(camLastMile.getBsIp());
			if (StringUtils.isNotBlank(serviceDetail.getServiceType()))
				cambiumLastMile.setServiceType(serviceDetail.getServiceType());

			if (camLastMile.getPortSpeed() != null) {
				Bandwidth portBandwidth = new Bandwidth();
				portBandwidth.setSpeed(camLastMile.getPortSpeed());
				if (StringUtils.isNotBlank(camLastMile.getPortSpeedUnit()))
					portBandwidth.setUnit(ActivationUtils.getBandwithUnit(camLastMile.getPortSpeedUnit()));
				cambiumLastMile.setPortSpeed(portBandwidth);
			}
			if (StringUtils.isNotBlank(camLastMile.getSuMacAddress()))
				cambiumLastMile.setSUMACAddress(camLastMile.getSuMacAddress());
			if (StringUtils.isNotBlank(camLastMile.getMgmtIpGateway())) {
				IPV4Address suGatewayIpv4 = new IPV4Address();
				suGatewayIpv4.setAddress(camLastMile.getMgmtIpGateway());
				cambiumLastMile.setGatewayMgmtIP(suGatewayIpv4);
			}
			if (StringUtils.isNotBlank(camLastMile.getMgmtIpForSsSu())) {
				IPV4Address suMgmtIpv4 = new IPV4Address();
				suMgmtIpv4.setAddress(camLastMile.getMgmtIpForSsSu());
				cambiumLastMile.setSUMgmtIP(suMgmtIpv4);
			}
			if (StringUtils.isNotBlank(camLastMile.getMgmtSubnetForSsSu())) {
				IPV4Address suMgtSubnetIpv4 = new IPV4Address();
				suMgtSubnetIpv4.setAddress(camLastMile.getMgmtSubnetForSsSu());
				cambiumLastMile.setSUMgmtSubnet(suMgtSubnetIpv4);
			}
			cambiumLastMile.setAPIVersion("PMP.SM(v13.1.3)");
			if (StringUtils.isNotBlank(camLastMile.getDhcpState()))
				cambiumLastMile.setDHCPState(camLastMile.getDhcpState().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getLinkSpeed()))
				cambiumLastMile.setLinkSpeed(camLastMile.getLinkSpeed().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getRegionCode()))
				cambiumLastMile.setRegionCode(camLastMile.getRegionCode());
			if (camLastMile.getWebpageAutoUpdate() != null)
				cambiumLastMile.setWebpageAutoUpdate(camLastMile.getWebpageAutoUpdate());
			if (StringUtils.isNotBlank(camLastMile.getCustomRadioFrequencyList()))
				cambiumLastMile.setRFScanList(camLastMile.getCustomRadioFrequencyList());
			if (StringUtils.isNotBlank(camLastMile.getBridgeEntryTimeout()))
				cambiumLastMile.setBridgeEntryTimeout(camLastMile.getBridgeEntryTimeout());
			if (StringUtils.isNotBlank(camLastMile.getDynamicRateAdapt())) {
				if (camLastMile.getDynamicRateAdapt().equalsIgnoreCase("1x"))
					cambiumLastMile.setDynamicRateAdapt("0");
				else
					cambiumLastMile.setDynamicRateAdapt("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getMulticastDestinationAddr()))
				cambiumLastMile.setMulticastDestAddress(camLastMile.getMulticastDestinationAddr().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getDeviceDefaultReset()))
				cambiumLastMile.setDeviceDefaultReset(camLastMile.getDeviceDefaultReset());
			if (StringUtils.isNotBlank(camLastMile.getPowerupmodeWithno802_3link())) {
				if (camLastMile.getPowerupmodeWithno802_3link().toLowerCase().contains("operational"))
					cambiumLastMile.setPowerupMode("0");
				else
					cambiumLastMile.setPowerupMode("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getFrameTimingPulseGated()))
				cambiumLastMile.setFrameTimingPulseGated(camLastMile.getFrameTimingPulseGated().toUpperCase());
			if (camLastMile.getColorCode1() != null)
				cambiumLastMile.setColourCode(camLastMile.getColorCode1());
			if (StringUtils.isNotBlank(camLastMile.getTransmitterOutputPower()))
				cambiumLastMile
						.setTransmitterOutputPower(ActivationUtils.toInteger(camLastMile.getTransmitterOutputPower()));
			if (StringUtils.isNotBlank(camLastMile.getLargevcDataq()))
				cambiumLastMile.setEnableLargeDataVCQ(camLastMile.getLargevcDataq().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getInstallationColorCode()))
				cambiumLastMile.setInstallationColourCode(camLastMile.getInstallationColorCode().toUpperCase());
			CambiumAAAConfig cambiumAAAConfig = new CambiumAAAConfig();
			if (StringUtils.isNotBlank(camLastMile.getEthernetAccess()))
				cambiumAAAConfig.setEthernetAccess(camLastMile.getEthernetAccess().toUpperCase());
			if (StringUtils.isNotBlank(camLastMile.getUserName()))
				cambiumAAAConfig.setUsername(camLastMile.getUserName());
			if (StringUtils.isNotBlank(camLastMile.getAllowlocalloginAfteraaareject())) {
				if (camLastMile.getAllowlocalloginAfteraaareject().toLowerCase().contains("enabled"))
					cambiumAAAConfig.setAllowLocalLoginAfterAAAReject("1");
				else
					cambiumAAAConfig.setAllowLocalLoginAfterAAAReject("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getHipriorityUplinkCir()))
				cambiumAAAConfig.setHighPriorityUplinkCIR(camLastMile.getHipriorityUplinkCir());
			if (StringUtils.isNotBlank(camLastMile.getAuthenticationKey()))
				cambiumAAAConfig.setAuthenticationKey(ActivationUtils.toInteger(camLastMile.getAuthenticationKey()));
			if (StringUtils.isNotBlank(camLastMile.getRealm()))
				cambiumAAAConfig.setRealm(ActivationUtils.toInteger(camLastMile.getRealm()));
			if (StringUtils.isNotBlank(camLastMile.getDeviceaccessTracking()))
				cambiumAAAConfig.setDeviceAccessTracking(camLastMile.getDeviceaccessTracking());
			if (StringUtils.isNotBlank(camLastMile.getLowpriorityDownlinkCir()))
				cambiumAAAConfig.setLowPriorityDownlinkCIR(camLastMile.getLowpriorityDownlinkCir());
			if (StringUtils.isNotBlank(camLastMile.getSelectKey())) {
				if (camLastMile.getSelectKey().toLowerCase().contains("default"))
					cambiumAAAConfig.setSelectKey("0");
				else
					cambiumAAAConfig.setSelectKey("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getEnforceAuthentication())) {
				if (camLastMile.getEnforceAuthentication().toLowerCase().contains("default"))
					cambiumAAAConfig.setEnforceAuth("0");
				else if(camLastMile.getEnforceAuthentication().toUpperCase().contains("AAA"))
					cambiumAAAConfig.setEnforceAuth("1");
				else
					cambiumAAAConfig.setEnforceAuth("2");
			}
			if (StringUtils.isNotBlank(camLastMile.getIdentity())) {
				cambiumAAAConfig.setIdentity(camLastMile.getIdentity());
			}else {
				cambiumAAAConfig.setIdentity(Objects.nonNull(camLastMile.getSuMacAddress()) && !camLastMile.getSuMacAddress().isEmpty()?camLastMile.getSuMacAddress().replaceAll(" ", "-"):camLastMile.getSuMacAddress());
			}
			if (StringUtils.isNotBlank(camLastMile.getDownlinkBurstAllocation()))
				cambiumAAAConfig.setDownlinkBurstAllocation(camLastMile.getDownlinkBurstAllocation());
			if (StringUtils.isNotBlank(camLastMile.getLowpriorityUplinkCir()))
				cambiumAAAConfig.setLowPriorityUplinkCIR(camLastMile.getLowpriorityUplinkCir());
			if (StringUtils.isNotBlank(camLastMile.getPassword()))
				cambiumAAAConfig.setPassword(camLastMile.getPassword());
			if (StringUtils.isNotBlank(camLastMile.getUplinkBurstAllocation()))
				cambiumAAAConfig.setUplinkBurstAllocation(camLastMile.getUplinkBurstAllocation());
			if (StringUtils.isNotBlank(camLastMile.getEnableBroadcastMulticastDatarate()))
				cambiumAAAConfig.setEnableBroadcastMulticastRate(camLastMile.getEnableBroadcastMulticastDatarate());
			if (StringUtils.isNotBlank(camLastMile.getPhase1())) {
				if (camLastMile.getPhase1().toUpperCase().contains("EAPTTLS"))
					cambiumAAAConfig.setPhase1("0");
				else
					cambiumAAAConfig.setPhase1("1");
			}
			if (StringUtils.isNotBlank(camLastMile.getServerCommonName()))
				cambiumAAAConfig.setServerCommonName(camLastMile.getServerCommonName());
			if (StringUtils.isNotBlank(camLastMile.getBwUplinkSustainedRate()))
				cambiumAAAConfig.setBandwidthUplinkSustainRate(camLastMile.getBwUplinkSustainedRate());
			if (StringUtils.isNotBlank(camLastMile.getHipriorityChannel()))
				cambiumAAAConfig.setHighPriorityChannel(camLastMile.getHipriorityChannel());
			if (StringUtils.isNotBlank(camLastMile.getPhase2())) {
				if (camLastMile.getPhase2().toUpperCase().contains("PAP"))
					cambiumAAAConfig.setPhase2("0");
				else if (camLastMile.getPhase2().toUpperCase().contains("CHAP"))
					cambiumAAAConfig.setPhase2("1");
				else
					cambiumAAAConfig.setPhase2("2");
			}
			if (StringUtils.isNotBlank(camLastMile.getNetworkAccessibility())) {
				if (camLastMile.getNetworkAccessibility().toLowerCase().contains("public"))
					cambiumAAAConfig.setNetworkAccessibility("1");
				else
					cambiumAAAConfig.setNetworkAccessibility("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getBwDownlinkSustainedRate()))
				cambiumAAAConfig.setBandwidthDownlinkSustainRate(camLastMile.getBwDownlinkSustainedRate());
			if (StringUtils.isNotBlank(camLastMile.getUseRealmStatus()))
				cambiumAAAConfig.setUseRealmStatus(camLastMile.getUseRealmStatus());
			if (StringUtils.isNotBlank(camLastMile.getUserAuthenticationMode())) {
				if (camLastMile.getUserAuthenticationMode().toLowerCase().contains("than"))
					cambiumAAAConfig.setUserAuthenticationMode("2");
				else if (camLastMile.getUserAuthenticationMode().toLowerCase().contains("local"))
					cambiumAAAConfig.setUserAuthenticationMode("1");
				else
					cambiumAAAConfig.setUserAuthenticationMode("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getHipriorityDownlinkCir()))
				cambiumAAAConfig.setHighPriorityDownlinkCIR(camLastMile.getHipriorityDownlinkCir());
			cambiumLastMile.setCambiumAAAConfig(cambiumAAAConfig);
			CambiumAntennaParameters cambiumAntennaParameters = new CambiumAntennaParameters();
			if (StringUtils.isNotBlank(camLastMile.getSmHeight())){
				Double smHeight = Double.valueOf(camLastMile.getSmHeight());
				Integer value = smHeight.intValue();
				cambiumAntennaParameters.setHeight(value.toString());
			}
			if (StringUtils.isNotBlank(camLastMile.getLatitudeSettings()))
				cambiumAntennaParameters.setLattitude(camLastMile.getLatitudeSettings().trim());
			if (StringUtils.isNotBlank(camLastMile.getLongitudeSettings()))
				cambiumAntennaParameters.setLongitude(camLastMile.getLongitudeSettings().trim());
			cambiumLastMile.setCambiumAntennaParameters(cambiumAntennaParameters);
			CambiumVLANAttributes cambiumVLANAttributes = new CambiumVLANAttributes();
			if (StringUtils.isNotBlank(camLastMile.getAllowFrametypes())) {
				if (camLastMile.getAllowFrametypes().toLowerCase().contains("untag"))
					cambiumVLANAttributes.setAllowFrameTypes("2");
				else if (camLastMile.getAllowFrametypes().toLowerCase().contains("tag"))
					cambiumVLANAttributes.setAllowFrameTypes("1");
				else
					cambiumVLANAttributes.setAllowFrameTypes("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getDynamicLearning()))
				cambiumVLANAttributes.setDynamicLearning(camLastMile.getDynamicLearning().toUpperCase());
			if (camLastMile.getVlanAgeingTimeout() != null)
				cambiumVLANAttributes.setVlanAgingTimeout(ActivationUtils.toString(camLastMile.getVlanAgeingTimeout()));
			if (StringUtils.isNotBlank(camLastMile.getMgmtVid()))
				cambiumVLANAttributes.setManagementVID(camLastMile.getMgmtVid());
			if (StringUtils.isNotBlank(camLastMile.getDefaultPortVid()))
				cambiumVLANAttributes.setDefaultPortVID(ActivationUtils.toInteger(camLastMile.getDefaultPortVid()));
			if (StringUtils.isNotBlank(camLastMile.getSmMgmtVidPassthrough()))
				cambiumVLANAttributes
						.setSSManagementVIDPassthrough(camLastMile.getSmMgmtVidPassthrough().toUpperCase());
			if (camLastMile.getProviderVid() != null)
				cambiumVLANAttributes.setProviderVID(ActivationUtils.toString(camLastMile.getProviderVid()));
			if (StringUtils.isNotBlank(camLastMile.getMappedMacAddress())) {
				MACAddressVIDMap macAddressVIDMap = new MACAddressVIDMap();
				macAddressVIDMap.setMACAddress(camLastMile.getMappedMacAddress());
				cambiumVLANAttributes.getMappedMACAddress().add(macAddressVIDMap);
			}
			if (StringUtils.isNotBlank(camLastMile.getMappedVid1()))
				cambiumVLANAttributes.setMappedVID1(camLastMile.getMappedVid1());
			if (camLastMile.getMappedVid2() != null)
				cambiumVLANAttributes.setMappedVID2(ActivationUtils.toString(camLastMile.getMappedVid2()));
			if (StringUtils.isNotBlank(camLastMile.getVlanPorttype())) {
				if (camLastMile.getVlanPorttype().toLowerCase().contains("qinq"))
					cambiumVLANAttributes.setVlanPortType("1");
				else
					cambiumVLANAttributes.setVlanPortType("0");
			}
			if (StringUtils.isNotBlank(camLastMile.getAcceptQinqFrames()))
				cambiumVLANAttributes.setAcceptQinQFrames(camLastMile.getAcceptQinqFrames().toUpperCase());
			if(camLastMile.getBsName().toLowerCase().contains("lab") || camLastMile.getBsName().toLowerCase().contains("test")){
				cambiumVLANAttributes.setManagementVID("3551");
				cambiumVLANAttributes.setDefaultPortVID(3551);
				cambiumVLANAttributes.setMappedVID1("3551");
				cambiumVLANAttributes.setMappedVID2("3551");
			}

			cambiumLastMile.setCambiumVLANAttributes(cambiumVLANAttributes);
			SNMPParameters snmpParameters = new SNMPParameters();

			IPAddressList ipSnmpTrapIpAddressList = new IPAddressList();
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp1())) {
				IPAddress snmpIp1 = new IPV4Address();
				snmpIp1.setAddress(camLastMile.getSnmpTrapIp1());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp1);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp2())) {
				IPAddress snmpIp2 = new IPV4Address();
				snmpIp2.setAddress(camLastMile.getSnmpTrapIp2());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp2);

			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp3())) {
				IPAddress snmpIp3 = new IPV4Address();
				snmpIp3.setAddress(camLastMile.getSnmpTrapIp3());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp3);
			}

			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp4())) {
				IPAddress snmpIp4 = new IPV4Address();
				snmpIp4.setAddress(camLastMile.getSnmpTrapIp4());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp4);
			}

			if (StringUtils.isNotBlank(camLastMile.getSnmpTrapIp5())) {
				IPAddress snmpIp5 = new IPV4Address();
				snmpIp5.setAddress(camLastMile.getSnmpTrapIp5());
				ipSnmpTrapIpAddressList.getIPAddress().add(snmpIp5);
			}

			snmpParameters.setSNMPTrapIPList(ipSnmpTrapIpAddressList);

			IPAddressList ipSnmpAccessAddressList = new IPAddressList();
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp1())) {
				IPAddress accessIp1 = new IPV4Address();
				accessIp1.setAddress(camLastMile.getSnmpAccessingIp1());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp1);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp2())) {
				IPAddress accessIp2 = new IPV4Address();
				accessIp2.setAddress(camLastMile.getSnmpAccessingIp2());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp2);
			}
			if (StringUtils.isNotBlank(camLastMile.getSnmpAccessingIp3())) {
				IPAddress accessIp3 = new IPV4Address();
				accessIp3.setAddress(camLastMile.getSnmpAccessingIp3());
				ipSnmpAccessAddressList.getIPAddress().add(accessIp3);
			}

			snmpParameters.setSNMPAccessingIPList(ipSnmpAccessAddressList);
			cambiumLastMile.setSNMPParameters(snmpParameters);
			if (StringUtils.isNotBlank(camLastMile.getSiteinfoViewabletoGuestusers()))
				cambiumLastMile.setViewableToGuestUsers(camLastMile.getSiteinfoViewabletoGuestusers().toUpperCase());
			CambiumSite cambiumSite = new CambiumSite();
			if (StringUtils.isNotBlank(camLastMile.getSiteContact()))
				cambiumSite.setContact(camLastMile.getSiteContact());
			if (StringUtils.isNotBlank(camLastMile.getSiteLocation()))
				cambiumSite.setLocation(camLastMile.getSiteLocation());
			if (StringUtils.isNotBlank(camLastMile.getSiteName()))
				cambiumSite.setName(camLastMile.getSiteName());
			cambiumLastMile.setCambiumSiteDetails(cambiumSite);
			if (StringUtils.isNotBlank(camLastMile.getDeviceType()))
				cambiumLastMile.setDeviceType(camLastMile.getDeviceType());

			if(camLastMile.getBsName().toLowerCase().contains("lab") || camLastMile.getBsName().toLowerCase().contains("test"))
				cambiumLastMile.setColourCode("252");
			service.setCambiumLastmile(cambiumLastMile);
		}
	}

	/**
	 * createRFProductConfig
	 * 
	 * @param serviceDetail
	 * @param service
	 */
	protected void createRFProductConfig(ServiceDetail serviceDetail, OrderInfo3.Service service) {
		IAService iasService = new IAService();
		if (serviceDetail.getServiceBandwidth() != null) {
			Bandwidth bandwidth = new Bandwidth();
			bandwidth.setSpeed(serviceDetail.getServiceBandwidth());
			if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit()))
				bandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
			iasService.setServiceBandwidth(bandwidth);
		}
		if (serviceDetail.getBurstableBw() != null) {
			Bandwidth burstableBandwidth = new Bandwidth();
			burstableBandwidth.setSpeed(serviceDetail.getBurstableBw());
			if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit()))
				burstableBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
			iasService.setBurstableBandwidth(burstableBandwidth);
		}
		service.setIaService(iasService);
	}

	protected OrderInfo3.Service createServiceDetails(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		LOGGER.info("Inside AbstractConfiguration.createServiceDetails with input serviceId {}", serviceDetail.getId());
		IAService iasService = new IAService();
		iasService.setIsIDCService(ActivationUtils.getBooleanValue(serviceDetail.getIsIdcService()));
		if (StringUtils.isNotBlank(serviceDetail.getServiceType()))
			iasService.setServiceType(serviceDetail.getServiceType());
		if (StringUtils.isNotBlank(serviceDetail.getServiceSubtype()))
			iasService.setServiceSubType(serviceDetail.getServiceSubtype());
		if (StringUtils.isNotBlank(serviceDetail.getAluSvcName()))
			iasService.setALUServiceName(serviceDetail.getAluSvcName());

		if (StringUtils.isNotBlank(serviceDetail.getUsageModel()))
			iasService.setUsageModel(serviceDetail.getUsageModel());
		
		if (StringUtils.isNotBlank(serviceDetail.getAluSvcid()))
			iasService.setALUServiceID(serviceDetail.getAluSvcid());
		
		if (StringUtils.isNotBlank(serviceDetail.getDataTransferCommit()))
			iasService.setDataTransferCommit(serviceDetail.getDataTransferCommit());
		
		if (StringUtils.isNotBlank(serviceDetail.getDataTransferCommitUnit()))
			iasService.setDataTransferCommitUnit(serviceDetail.getDataTransferCommitUnit());
		if (StringUtils.isNotBlank(serviceDetail.getDescription()))
			iasService.setDescriptionFreeText(serviceDetail.getDescription());

		if (serviceDetail.getCssSammgrId() != null)
			iasService.setCSSSAMID(ActivationUtils.toString(serviceDetail.getCssSammgrId()));
		if (StringUtils.isNotBlank(orderDetail.getSamCustomerDescription()))
			iasService.setSAMCustomerDescription(orderDetail.getSamCustomerDescription());// ALU SPEC
		if (StringUtils.isNotBlank(serviceDetail.getNetpRefid()))
			iasService.setInstanceID(serviceDetail.getNetpRefid());

		List<VpnSolution> vpnSolutions = vpnSolutionRepository
				.findByServiceDetail_IdAndEndDateIsNull(serviceDetail.getId());
		VPNSolutionTable solutionTable = new VPNSolutionTable();
		String currentLegId = null;
		for (VpnSolution vpnSolution : vpnSolutions) {
			if(vpnSolution.getVpnName()!=null) {
				if (StringUtils.isNotBlank(serviceDetail.getSolutionId()))
					solutionTable.setSolutionID(serviceDetail.getSolutionId());
				VPN vpn = new VPN();
				if (StringUtils.isNotBlank(vpnSolution.getVpnName()))
					
					if(vpnSolution.getVpnName().equalsIgnoreCase("INTERNET_VPN")) {
						vpn.setVpnId("Internet_VPN");

					}
					else {
					vpn.setVpnId(vpnSolution.getVpnName());
					}
				if (StringUtils.isNotBlank(vpnSolution.getVpnType()))
					vpn.setVpnType(vpnSolution.getVpnType());
				if (StringUtils.isNotBlank(vpnSolution.getVpnTopology()))
					vpn.setTopology(vpnSolution.getVpnTopology());
				Leg leg = new Leg();
				if (StringUtils.isNotBlank(vpnSolution.getVpnLegId())) {
					leg.setLegServiceID(vpnSolution.getVpnLegId());
					currentLegId = vpnSolution.getVpnLegId();
				}
				if (StringUtils.isNotBlank(vpnSolution.getLegRole()))
					leg.setRole(vpnSolution.getLegRole());
				if (StringUtils.isNotBlank(vpnSolution.getInterfaceName()))
					leg.setInterfacename(vpnSolution.getInterfaceName());
				if (StringUtils.isNotBlank(vpnSolution.getSiteId()))
					leg.setSiteID(vpnSolution.getSiteId());
				if (StringUtils.isNotBlank(vpnSolution.getInstanceId()))
					leg.setInstanceID(vpnSolution.getInstanceId());
				vpn.getLeg().add(leg);
				solutionTable.getVPN().add(vpn);
				iasService.setSolutionTable(solutionTable);
			}
		}

		if (serviceDetail.getVrfs() != null)
			iasService.setVrf(createVrfDetails(serviceDetail, null, currentLegId));
		if (!(serviceDetail.getRedundancyRole() != null
				&& serviceDetail.getRedundancyRole().toUpperCase().contains("SINGLE"))) {
			iasService.getServiceLink().add(createServiceLinks(serviceDetail));
		}
		if (StringUtils.isNotBlank(serviceDetail.getIntefaceDescSvctag()))
			iasService.setInterfaceDescriptionServiceTag(serviceDetail.getIntefaceDescSvctag());
		List<InterfaceProtocolMapping> interfaceProtocolMappings = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndRouterDetailNotNullAndEndDateIsNull(serviceDetail.getId());
		for (InterfaceProtocolMapping interfaceProtocolMapping : interfaceProtocolMappings) {
			if (interfaceProtocolMapping.getCpe() == null)// TODO
				iasService.getRouter().add(createPeRouterDetails(interfaceProtocolMapping, serviceDetail));
		}
		if (serviceDetail.getServiceBandwidth() != null) {
			Bandwidth bandwidth = new Bandwidth();
			bandwidth.setSpeed(serviceDetail.getServiceBandwidth());
			if (StringUtils.isNotBlank(serviceDetail.getServiceBandwidthUnit()))
				bandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getServiceBandwidthUnit()));
			iasService.setServiceBandwidth(bandwidth);
		}
		if (serviceDetail.getBurstableBw() != null) {
			Bandwidth burstableBandwidth = new Bandwidth();
			burstableBandwidth.setSpeed(serviceDetail.getBurstableBw());
			if (StringUtils.isNotBlank(serviceDetail.getBurstableBwUnit()))
				burstableBandwidth.setUnit(ActivationUtils.getBandwithUnit(serviceDetail.getBurstableBwUnit()));
			iasService.setBurstableBandwidth(burstableBandwidth);
		}
		if (StringUtils.isNotBlank(serviceDetail.getRedundancyRole())) {
			iasService.setRedundancyRole(serviceDetail.getRedundancyRole().toUpperCase());
		}
		if (serviceDetail.getMgmtType().toUpperCase().contains("UNMANAGED")) {
			iasService.setIsConfigManaged(false);

		} else {
			iasService.setIsConfigManaged(true);

		}
		IpAddressDetail ipAddressDetail = ipAddressDetailRepository
				.findByServiceDetailIdAndEndDateIsNull(serviceDetail.getId());
		if (StringUtils.isNotBlank(ipAddressDetail.getPingAddress1())) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(ipAddressDetail.getPingAddress1());
			iasService.setInternetGatewayv4IPAddress(ipv4Address);
		}
		if (StringUtils.isNotBlank(ipAddressDetail.getPingAddress2())) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(ipAddressDetail.getPingAddress2());
			iasService.setInternetGatewayv6IPAddress(ipv6Address);
		}
		if (StringUtils.isNotBlank(ipAddressDetail.getNmsServiceIpv4Address())) {
			IPV4Address nmsServerIpv4Address = new IPV4Address();
			nmsServerIpv4Address.setAddress(ipAddressDetail.getNmsServiceIpv4Address());
			iasService.setNMSServerv4IPAddress(nmsServerIpv4Address);
		}
		iasService.setExtendedLAN(createExtendedLanConfig(ipAddressDetail));
		Set<IpaddrWanv4Address> wanV4Addresses = ipAddressDetail.getIpaddrWanv4Addresses();
		for (IpaddrWanv4Address ipaddrWanv4Address : wanV4Addresses) {
			if (StringUtils.isNotBlank(ipaddrWanv4Address.getWanv4Address())) {
				IPV4Address wanV4Address = new IPV4Address();
				wanV4Address.setAddress(ipaddrWanv4Address.getWanv4Address());
				iasService.getWanV4Addresses().add(wanV4Address);
			}
		}

		Set<IpaddrWanv6Address> wanV6Addresses = ipAddressDetail.getIpaddrWanv6Addresses();
		for (IpaddrWanv6Address ipaddrWanv6Address : wanV6Addresses) {
			if (StringUtils.isNotBlank(ipaddrWanv6Address.getWanv6Address())) {
				IPV6Address wanIpv6Address = new IPV6Address();
				wanIpv6Address.setAddress(ipaddrWanv6Address.getWanv6Address());
				iasService.getWanV6Addresses().add(wanIpv6Address);
			}
		}

		Set<IpaddrLanv4Address> lanV4Addresses = ipAddressDetail.getIpaddrLanv4Addresses();
		for (IpaddrLanv4Address ipaddrLanv4Addres : lanV4Addresses) {
			if (StringUtils.isNotBlank(ipaddrLanv4Addres.getLanv4Address())) {
				IPV4Address lanIpv4Address = new IPV4Address();
				lanIpv4Address.setAddress(ipaddrLanv4Addres.getLanv4Address().trim());
				iasService.getLanV4Addresses().add(lanIpv4Address);
			}
		}

		Set<IpaddrLanv6Address> lanV6Addresses = ipAddressDetail.getIpaddrLanv6Addresses();
		for (IpaddrLanv6Address ipaddrLanv6Addres : lanV6Addresses) {
			if (StringUtils.isNotBlank(ipaddrLanv6Addres.getLanv6Address())) {
				IPV6Address lanIpv6Address = new IPV6Address();
				lanIpv6Address.setAddress(ipaddrLanv6Addres.getLanv6Address()!=null ?ipaddrLanv6Addres.getLanv6Address().trim():ipaddrLanv6Addres.getLanv6Address());
				iasService.getLanV6Addresses().add(lanIpv6Address);
			}
		}

		OrderInfo3.Service service = new OrderInfo3.Service();
		List<InterfaceProtocolMapping> bgInterfaceProtocolEntity = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndEndDateIsNull(serviceDetail.getId());
		for (InterfaceProtocolMapping interfaceProtocolMapping : bgInterfaceProtocolEntity) {
			if (interfaceProtocolMapping.getCpe() == null) {
				Bgp bgp = interfaceProtocolMapping.getBgp();
				Ospf ospf = interfaceProtocolMapping.getOspf();
				StaticProtocol staticProtocol = interfaceProtocolMapping.getStaticProtocol();
				iasService.setWanRoutingProtocol(createServiceWanRoutingProtocol(bgp, ospf, staticProtocol));
				if (bgp != null) {
					Set<WanStaticRoute> wanstaticRoutes = bgp.getWanStaticRoutes();
					iasService.setPEWANAdditionalStaticRoutes(createPeWanAdditionalStaticRoutesBgp(wanstaticRoutes));
					if (bgp.getRtbhOptions() != null) {
						iasService.setRTBHOption(bgp.getRtbhOptions());
						iasService.setIsRTBH(true);

					}
				}
			}
		}
		iasService.setQos(createServiceQos(serviceDetail));
		List<InterfaceProtocolMapping> cpeInterfaceProtocolEntity = interfaceProtocolMappingRepository
				.findByServiceDetailIdAndCpeNotNullAndEndDateIsNull(serviceDetail.getId());
		for (InterfaceProtocolMapping interfaceProtocolMapping : cpeInterfaceProtocolEntity) {
			if (interfaceProtocolMapping.getIscpeWanInterface() == 1) {// This is only for wan interface
				CustomerPremiseEquipment cpe = createCpeDetails(interfaceProtocolMapping);
				if (cpe != null)
					iasService.getCpe().add(cpe);
			}
		}
		if("ACTIVE".equalsIgnoreCase(serviceDetail.getServiceState()) && (cpeInterfaceProtocolEntity==null || cpeInterfaceProtocolEntity.isEmpty())) { //Applicable only for IP Blocking and Termination
			CustomerPremiseEquipment cpe = new CustomerPremiseEquipment();
			cpe.setIsCEACEConfigurable(false);
			cpe.setSnmpServerCommunity("t2c2l2com");
			Map<String,String> ipAddress=getTclWanDetailsFromReverseISC(serviceDetail.getServiceId());
			if(ipAddress.containsKey("customerEndLoopback") && ipAddress.get("customerEndLoopback")!=null) {
				Interface loopbackInterface = new Interface();
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ipAddress.get("customerEndLoopback"));
				loopbackInterface.setV4IpAddress(ipv4Address);
				cpe.setLoopbackInterface(loopbackInterface);
			}
			WANInterface wanInterface = new WANInterface();
			WANInterface.Interface wIntfce = new WANInterface.Interface();
			EthernetInterface etherInterface = new EthernetInterface();
			etherInterface.setMode("ACCESS");
			etherInterface.setDuplex(ActivationUtils.getDuplex("NOT_APPLICABLE"));
			etherInterface.setIsAutoNegotiation("NOT_APPLICABLE");
			etherInterface.setSpeed("NOT_APPLICABLE");
			if(ipAddress.containsKey("wanIpAddress") && ipAddress.get("wanIpAddress")!=null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ipAddress.get("wanIpAddress"));
				etherInterface.setV4IpAddress(ipv4Address);
			}
			wIntfce.setEthernetInterface(etherInterface);
			wanInterface.setInterface(wIntfce);
			cpe.setWanInterface(wanInterface);
			iasService.getCpe().add(cpe);
		}
		if (serviceDetail.getLastmileType() != null && !serviceDetail.getLastmileType().isEmpty()) {
			LOGGER.info("LastMileType exists");
				LastMile lastMile = new LastMile();
				lastMile.setType(serviceDetail.getLastmileType());
				if(Objects.nonNull(serviceDetail.getLastmileType()) && !serviceDetail.getLastmileType().isEmpty() && serviceDetail.getLastmileType().equalsIgnoreCase("OnnetRF")){
					LOGGER.info("OnnetRF as LastMileType");
					/*List<LmComponent> lmComponents=lmComponentRepository.findByServiceDetail_id(serviceDetail.getId());
					if(Objects.nonNull(lmComponents) && !lmComponents.isEmpty()){
						LOGGER.info("LM Component exists");
						lmComponents.stream().filter(lmComp -> Objects.nonNull(lmComp.getLmOnwlProvider()) 
								&& !lmComp.getLmOnwlProvider().isEmpty() && "Radwin".equalsIgnoreCase(lmComp.getLmOnwlProvider()) &&  "pmp".contains(lmComp.getLmOnwlProvider())).forEach(lmComp ->{
							LOGGER.info("OnnetRF as Radwin");
							lastMile.setType(lmComp.getLmOnwlProvider().toUpperCase());
						});						
					}else{
						LOGGER.info("OnnetRF not Radwin");
						lastMile.setType(serviceDetail.getLastmileType());
					}*/
					LOGGER.info("{}=>getLastmileProvider={}",serviceDetail.getServiceId(),serviceDetail.getLastmileProvider());
					if(Objects.nonNull(serviceDetail.getLastmileProvider()) && !serviceDetail.getLastmileProvider().isEmpty() && 
							serviceDetail.getLastmileProvider().toLowerCase().contains("radwin") && !serviceDetail.getLastmileProvider().toLowerCase().contains("pmp")){
						lastMile.setType("RADWIN");
					}
				}else{
					LOGGER.info("Not OnnetRF as LastMileType");
					lastMile.setType(serviceDetail.getLastmileType());
				}
				iasService.getLastMile().add(lastMile);
		}
		service.setIaService(iasService);
		return service;
	}

	protected VirtualRouteForwardingServiceInstance createVrfDetails(ServiceDetail serviceDetail,
			GVPNService gvpnService, String currentLegId) {
		VirtualRouteForwardingServiceInstance vrf = null;
		Set<Vrf> vrfs = serviceDetail.getVrfs();
		for (Vrf vr : vrfs) {
			if (gvpnService != null) {
				gvpnService.setIsVRFLiteEnabled(ActivationUtils.getBooleanValue(vr.getIsvrfLiteEnabled()));
				gvpnService.setCurrentCustomerSiteId(serviceDetail.getServiceId());
				if (currentLegId != null)
					gvpnService.setCurrentLegID(currentLegId);
			}
			vrf = new VirtualRouteForwardingServiceInstance();
			if (StringUtils.isNotBlank(vr.getVrfName())) {
				RoutingPolicy impRoutingPolicy = new RoutingPolicy();
				RoutingPolicy expRoutingPolicy = new RoutingPolicy();
				for (PolicyType policyType : vr.getPolicyTypes()) {
					if (policyType.getIsvprnImportpolicy() != null && policyType.getIsvprnImportpolicy() == 1) {
						impRoutingPolicy.setIsPreprovisioned(
								ActivationUtils.getBooleanValue(policyType.getIsvprnImportPreprovisioned()));
						impRoutingPolicy.setName(policyType.getIsvprnImportpolicyName());
						vrf.setALUVPRNImportPolicy(impRoutingPolicy);
					}

					if (policyType.getIsvprnExportpolicy() != null && policyType.getIsvprnExportpolicy() == 1) {
						expRoutingPolicy.setIsPreprovisioned(
								ActivationUtils.getBooleanValue(policyType.getIsvprnExportPreprovisioned()));
						expRoutingPolicy.setName(policyType.getIsvprnExportpolicyName());
						vrf.setALUVPRNExportPolicy(expRoutingPolicy);
					}
				}
			}
			MaximumRoutes maximumRoutes = new MaximumRoutes();
			if (StringUtils.isNotBlank(vr.getThreshold()) && StringUtils.isNotBlank(vr.getWarnOn()))
				maximumRoutes.setThresholdValue(ActivationUtils.toInteger(vr.getThreshold()));
			if (StringUtils.isNotBlank(vr.getMaxRoutesValue()) && StringUtils.isNotBlank(vr.getWarnOn()))
				maximumRoutes.setValue(ActivationUtils.toInteger(vr.getMaxRoutesValue()));
			if (StringUtils.isNotBlank(vr.getWarnOn()))
				maximumRoutes.setWarnOn(vr.getWarnOn().toUpperCase());

			if (StringUtils.isNotBlank(vr.getThreshold()) || StringUtils.isNotBlank(vr.getWarnOn())
					|| StringUtils.isNotBlank(vr.getMaxRoutesValue())) {
				vrf.setMaximumRoutes(maximumRoutes);
			}
		}
		return vrf;
	}

	protected ServiceLink createServiceLinks(ServiceDetail serviceDetail) {
		ServiceLink serviceLink = new ServiceLink();
		if (serviceDetail.getSvclinkRole() != null)
			serviceLink.setRole(serviceDetail.getSvclinkRole());
		if (serviceDetail.getSvclinkSrvid() != null)
			serviceLink.setServiceId(serviceDetail.getSvclinkSrvid());
		return serviceLink;

	}

	protected void createIpAddressDetails() {

	}

	protected void createWanV4Address() {

	}

	protected ExtendedLAN createExtendedLanConfig(IpAddressDetail ipAddressDetail) {
		ExtendedLAN extendedLAN = new ExtendedLAN();
		if (ipAddressDetail.getExtendedLanEnabled() != null)
			extendedLAN.setIsEnabled(ActivationUtils.getBooleanValue(ipAddressDetail.getExtendedLanEnabled()));
		if (ipAddressDetail.getNoMacAddress() != null)
			extendedLAN.setNumberOfMacAddresses(ipAddressDetail.getNoMacAddress());
		return extendedLAN;

	}

	protected void createLanIpAddressV4() {

	}

	protected void createLanIpAddressV6() {

	}

	protected Router createPeRouterDetails(InterfaceProtocolMapping interfaceProtocolMapping,
			ServiceDetail serviceDetail) {
		RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
		Router router = new Router();
		if (StringUtils.isNotBlank(routerDetail.getRouterHostname()))
			router.setHostName(routerDetail.getRouterHostname());
		if (StringUtils.isNotBlank(routerDetail.getRouterRole()))
			router.setRole(routerDetail.getRouterRole());
		if (StringUtils.isNotBlank(routerDetail.getIpv4MgmtAddress())) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(routerDetail.getIpv4MgmtAddress());
			router.setV4ManagementIPAddress(ipv4Address);
		}
		if (StringUtils.isNotBlank(routerDetail.getIpv6MgmtAddress())) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(routerDetail.getIpv6MgmtAddress());
			router.setV6ManagementIPAddress(ipv6Address);
		}
		if (StringUtils.isNotBlank(routerDetail.getRouterType()))
			router.setType(routerDetail.getRouterType());
		router.setWanInterface(createWanInterfaceEthernet(serviceDetail,interfaceProtocolMapping, router));
		if (serviceDetail != null) {
			Set<Topology> topologies = serviceDetail.getTopologies();
			for (Topology topology : topologies) {
				TopologyInfo topologyInfo = createTopologyInformation(topology);
				if (topologyInfo != null) {
					router.setTopologyInfo(topologyInfo);
					Set<RouterUplinkport> routerUplinkPorts = topology.getRouterUplinkports();
					for (RouterUplinkport routerUplinkPort : routerUplinkPorts) {
						router.setRouterTopologyInterface1(createRouterUplinkPorts1(routerUplinkPort));
						router.setRouterTopologyInterface2(createRouterUplinkPorts2(routerUplinkPort));
					}
				}
			}
		}
		return router;
	}

	protected WANInterface createWanInterfaceEthernet(ServiceDetail serviceDetail, InterfaceProtocolMapping interfaceProtocolMapping,
			Router router) {
		WANInterface wanInterface = new WANInterface();
		WANInterface.Interface interfce = new WANInterface.Interface();
		if (interfaceProtocolMapping.getEthernetInterface() != null)
			interfce.setEthernetInterface(
					createEthernetInterface(serviceDetail,interfaceProtocolMapping.getEthernetInterface(), router, wanInterface));
		if (interfaceProtocolMapping.getChannelizedE1serialInterface() != null)
			interfce.setSerialInterface(
					createWanInterfaceCE1Serial(interfaceProtocolMapping.getChannelizedE1serialInterface(), router));
		if (interfaceProtocolMapping.getChannelizedSdhInterface() != null)
			interfce.setChannelizedSDHInterface(
					createWanInterfaceCSdh(interfaceProtocolMapping.getChannelizedSdhInterface(), router));
		wanInterface.setInterface(interfce);
		// wanInterface.setStaticRoutes(createPeWanStaticRoutes(interfaceProtocolMapping.getStaticProtocol()));
		return wanInterface;
	}

	protected EthernetInterface createEthernetInterface(
			ServiceDetail serviceDetail, com.tcl.dias.serviceactivation.entity.entities.EthernetInterface ethernetInterfaceEntity, Router router,
			WANInterface wanInterface) {
		EthernetInterface ethernetInterface = null;
		if (ethernetInterfaceEntity != null) {
			ethernetInterface = new EthernetInterface();
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getInterfaceName()))
				ethernetInterface.setName(ethernetInterfaceEntity.getInterfaceName());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getPhysicalPort()))
				ethernetInterface.setPhysicalPortName(ethernetInterfaceEntity.getPhysicalPort());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ethernetInterfaceEntity.getModifiedIpv4Address());
				ethernetInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(ethernetInterfaceEntity.getModifiedIpv6Address());
				ethernetInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedSecondaryIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(ethernetInterfaceEntity.getModifiedSecondaryIpv4Address());
				wanInterface.setSecondaryV4WANIPAddress(ipv4Address);
				//ethernetInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getModifiedSecondaryIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(ethernetInterfaceEntity.getModifiedSecondaryIpv6Address());
				ethernetInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMediaType()))
				ethernetInterface.setMediaType(ethernetInterfaceEntity.getMediaType());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getPortType()) && !ethernetInterfaceEntity.getPortType().toUpperCase().contains("LAG") && !"IZOPC".equalsIgnoreCase(serviceDetail.getProductName()))
				ethernetInterface.setPortType(ethernetInterfaceEntity.getPortType().toUpperCase());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getEncapsulation()))
				ethernetInterface.setEncapsulation(ethernetInterfaceEntity.getEncapsulation());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMode()))
				ethernetInterface.setMode(ethernetInterfaceEntity.getMode().toUpperCase());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getQosLoopinPassthrough()))
				ethernetInterface.setQosLoopinPassthroughInterface(ethernetInterfaceEntity.getQosLoopinPassthrough());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getFraming()))
				ethernetInterface.setFraming(ethernetInterfaceEntity.getFraming());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getInnerVlan()))
				ethernetInterface.setVlan(ActivationUtils.toInteger(ethernetInterfaceEntity.getInnerVlan()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getSpeed()))
				ethernetInterface.setSpeed(ethernetInterfaceEntity.getSpeed());
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getDuplex()))
				ethernetInterface.setDuplex(ActivationUtils.getDuplex(ethernetInterfaceEntity.getDuplex()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getOuterVlan()))
				ethernetInterface.setSvlan(ActivationUtils.toInteger(ethernetInterfaceEntity.getOuterVlan()));
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getAutonegotiationEnabled()))
				ethernetInterface.setIsAutoNegotiation(ethernetInterfaceEntity.getAutonegotiationEnabled());
			if (ethernetInterfaceEntity.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIsbfdEnabled()));
			if (ethernetInterfaceEntity.getIsbfdEnabled() != null
					&& ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIsbfdEnabled()))
				ethernetInterface.setBfdConfig(createEthernetInterfaceHdlcConfig(ethernetInterfaceEntity));
			if (ethernetInterfaceEntity.getIshsrpEnabled() != null
					&& ActivationUtils.getBooleanValue(ethernetInterfaceEntity.getIshsrpEnabled())) {
				wanInterface.setHSRP(createCpeHrspProtocol(ethernetInterfaceEntity));
			}
			if (StringUtils.isNotBlank(ethernetInterfaceEntity.getMtu()))
				ethernetInterface.setMtu(Integer.valueOf(ethernetInterfaceEntity.getMtu()));
			AccessControlList inBoundAccessControlList = new AccessControlList();
			AccessControlList outBoundAccessControlList = new AccessControlList();
			AccessControlList inBoundAccessControlListV6 = new AccessControlList();
			AccessControlList outBoundAccessControlListV6 = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = ethernetInterfaceEntity.getAclPolicyCriterias();
			for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {

				if (aclPolicyCriteria.getEndDate() == null) {
					if (aclPolicyCriteria.getInboundIpv4AclName() != null) {// TODO
						inBoundAccessControlList.setName(aclPolicyCriteria.getInboundIpv4AclName());
						ethernetInterface.setInboundAccessControlList(inBoundAccessControlList);
					}
					if (aclPolicyCriteria.getInboundIpv6AclName() != null) {// TODO
						inBoundAccessControlListV6.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						ethernetInterface.setInboundAccessControlListV6(inBoundAccessControlListV6);
					}

					if (aclPolicyCriteria.getOutboundIpv4AclName() != null) {// TODO
						outBoundAccessControlList.setName(aclPolicyCriteria.getOutboundIpv4AclName());
						ethernetInterface.setOutboundAccessControlList(outBoundAccessControlList);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundAccessControlListV6.setName(aclPolicyCriteria.getInboundIpv6AclName());
						ethernetInterface.setOutboundAccessControlListV6(outBoundAccessControlListV6);
					}
				}
			}
		}
		return ethernetInterface;

	}

	protected BFDConfig createEthernetInterfaceHdlcConfig(
			com.tcl.dias.serviceactivation.entity.entities.EthernetInterface ethernetInterfaceEntity) {
		BFDConfig bfdConfig = new BFDConfig();
		if (ethernetInterfaceEntity.getBfdtransmitInterval() != null && StringUtils.isNumeric(ethernetInterfaceEntity.getBfdtransmitInterval()))
			bfdConfig.setTransmitInterval(ActivationUtils.toInteger(ethernetInterfaceEntity.getBfdtransmitInterval()));
		if (ethernetInterfaceEntity.getBfdreceiveInterval() != null)
			bfdConfig.setRecieveInterval(ActivationUtils.toInteger(ethernetInterfaceEntity.getBfdreceiveInterval()));
		if (ethernetInterfaceEntity.getBfdMultiplier() != null)
			bfdConfig.setMultiplier(ActivationUtils.toInteger(ethernetInterfaceEntity.getBfdMultiplier()));
		return bfdConfig;
	}

	protected SerialInterface createWanInterfaceCE1Serial(ChannelizedE1serialInterface channelizedE1serialInterface,
			Router router) {
		SerialInterface serialInterface = null;
		if (channelizedE1serialInterface != null) {
			serialInterface = new SerialInterface();
			if (channelizedE1serialInterface.getInterfaceName() != null)
				serialInterface.setName(channelizedE1serialInterface.getInterfaceName());
			if (channelizedE1serialInterface.getPhysicalPort() != null)
				serialInterface.setPhysicalPortName(channelizedE1serialInterface.getPhysicalPort());
			if (channelizedE1serialInterface.getModifiedIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1serialInterface.getModifiedIpv4Address());
				serialInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedE1serialInterface.getModifiedIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1serialInterface.getModifiedIpv6Address());
				serialInterface.setV6IpAddress(ipv6Address);
			}
			if (channelizedE1serialInterface.getModifiedSecondaryIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1serialInterface.getModifiedSecondaryIpv4Address());
				serialInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedE1serialInterface.getModifiedSecondaryIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1serialInterface.getModifiedSecondaryIpv6Address());
				serialInterface.setV6IpAddress(ipv6Address);
			}
			if (channelizedE1serialInterface.getChannelGroupNumber() != null)
				serialInterface.setChannelGroupNumber(channelizedE1serialInterface.getChannelGroupNumber());
			if (channelizedE1serialInterface.getDlciValue() != null)
				serialInterface.setDlciValue(ActivationUtils.toInteger(channelizedE1serialInterface.getDlciValue()));
			for (int i = Integer.valueOf(channelizedE1serialInterface.getFirsttimeSlot()); i <= Integer
					.valueOf(channelizedE1serialInterface.getLasttimeSlot()); i++) {
				serialInterface.getTimeslot().add(String.valueOf(i));
			}
			if (channelizedE1serialInterface.getIsframed() != null)
				serialInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsframed()));

			if (channelizedE1serialInterface.getEncapsulation() != null) {
				serialInterface.setEncapsulation(channelizedE1serialInterface.getEncapsulation().toUpperCase());
			}

			if (channelizedE1serialInterface.getMode() != null) {
				serialInterface.setMode(channelizedE1serialInterface.getMode().toUpperCase());
			}
			if (channelizedE1serialInterface.getIsframed() != null)
				serialInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsframed()));
			if (channelizedE1serialInterface.getFraming() != null) {
				serialInterface.setFraming(channelizedE1serialInterface.getFraming());
			}
			if (channelizedE1serialInterface.getIscrcforenabled() != null) {
				serialInterface.setCrcSize(channelizedE1serialInterface.getCrcsize());
			}
			if (channelizedE1serialInterface.getPortType() != null)
				serialInterface.setPortType(channelizedE1serialInterface.getPortType().toUpperCase());
			if (channelizedE1serialInterface.getIsbfdEnabled() != null)
				serialInterface.setBfdConfig(createCE1SerialHdlcConfig(channelizedE1serialInterface));
			if (channelizedE1serialInterface.getMtu() != null)
				serialInterface.setMtu(ActivationUtils.toInteger(channelizedE1serialInterface.getMtu()));

			if (channelizedE1serialInterface.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(channelizedE1serialInterface.getIsbfdEnabled()));
			if (channelizedE1serialInterface.getEncapsulation() != null)
				serialInterface.setEncapsulation(channelizedE1serialInterface.getEncapsulation());

			AccessControlList inBoundAccessControlList = new AccessControlList();
			AccessControlList outBoundAccessControlList = new AccessControlList();
			AccessControlList inBoundAccessControlListV6 = new AccessControlList();
			AccessControlList outBoundAccessControlListV6 = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = channelizedE1serialInterface.getAclPolicyCriterias();
			for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
				if (aclPolicyCriteria.getEndDate() == null) {
					if (aclPolicyCriteria.getInboundIpv4AclName() != null) {// TODO
						inBoundAccessControlList.setName(aclPolicyCriteria.getInboundIpv4AclName());
						serialInterface.setInboundAccessControlList(inBoundAccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv4AclName() != null) {// TODO
						outBoundAccessControlList.setName(aclPolicyCriteria.getOutboundIpv4AclName());
						serialInterface.setOutboundAccessControlList(outBoundAccessControlList);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundAccessControlListV6.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						serialInterface.setOutboundAccessControlListV6(outBoundAccessControlListV6);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						inBoundAccessControlListV6.setName(aclPolicyCriteria.getInboundIpv6AclName());
						serialInterface.setInboundAccessControlListV6(inBoundAccessControlListV6);
					}
				}
			}

		}
		return serialInterface;
	}

	protected BFDConfig createCE1SerialHdlcConfig(ChannelizedE1serialInterface channelizedE1serialInterface) {
		BFDConfig bfdConfig = null;
		if (channelizedE1serialInterface != null) {
			bfdConfig = new BFDConfig();
			if (channelizedE1serialInterface.getBfdtransmitInterval() != null)
				bfdConfig.setTransmitInterval(
						ActivationUtils.toInteger(channelizedE1serialInterface.getBfdtransmitInterval()));
			if (channelizedE1serialInterface.getBfdreceiveInterval() != null)
				bfdConfig.setRecieveInterval(
						ActivationUtils.toInteger(channelizedE1serialInterface.getBfdreceiveInterval()));
			if (channelizedE1serialInterface.getBfdMultiplier() != null)
				bfdConfig.setMultiplier(ActivationUtils.toInteger(channelizedE1serialInterface.getBfdMultiplier()));
		}
		return bfdConfig;
	}

	protected ChannelizedSDHInterface createWanInterfaceCSdh(ChannelizedSdhInterface channelizedSdhInterface,
			Router router) {
		ChannelizedSDHInterface channelizedSDHInterface = null;
		if (channelizedSdhInterface != null) {
			channelizedSDHInterface = new ChannelizedSDHInterface();
			if (channelizedSdhInterface.getInterfaceName() != null)
				channelizedSDHInterface.setName(channelizedSdhInterface.getInterfaceName());
			if (channelizedSdhInterface.getPhysicalPort() != null)
				channelizedSDHInterface.setPhysicalPortName(channelizedSdhInterface.getPhysicalPort());
			if (channelizedSdhInterface.getModifiedIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInterface.getModifiedIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedSdhInterface.getModifiedIipv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInterface.getModifiedIipv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}
			if (channelizedSdhInterface.getModifiedSecondaryIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInterface.getModifiedSecondaryIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			if (channelizedSdhInterface.getModifiedSecondaryIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInterface.getModifiedSecondaryIpv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(channelizedSdhInterface.getChannelGroupNumber()))
				channelizedSDHInterface.setChannelGroupNumber(channelizedSdhInterface.getChannelGroupNumber());
			if (StringUtils.isNotBlank(channelizedSdhInterface.getDlciValue()))
				channelizedSDHInterface.setDlciValue(ActivationUtils.toInteger(channelizedSdhInterface.getDlciValue()));

			for (int i = Integer.valueOf(channelizedSdhInterface.get_4Kfirsttime_slot()); i <= Integer
					.valueOf(channelizedSdhInterface.get_4klasttimeSlot()); i++) {
				channelizedSDHInterface.getSixtyFourKTimeslot().add(String.valueOf(i));
			}
			if (StringUtils.isNotBlank(channelizedSdhInterface.getEncapsulation())) {
				channelizedSDHInterface.setEncapsulation(channelizedSdhInterface.getEncapsulation().toUpperCase());
			}

			if (StringUtils.isNotBlank(channelizedSdhInterface.getMode())) {
				channelizedSDHInterface.setMode(channelizedSdhInterface.getMode().toUpperCase());
			}
			if (channelizedSdhInterface.getIsbfdEnabled() != null)
				router.setIsRouterBFDEnabled(
						ActivationUtils.getBooleanValue(channelizedSdhInterface.getIsbfdEnabled()));
			if (channelizedSdhInterface.getIsframed() != null)
				channelizedSDHInterface
						.setIsFramed(ActivationUtils.getBooleanValue(channelizedSdhInterface.getIsframed()));
			if (channelizedSdhInterface.getFraming() != null) {
				channelizedSDHInterface.setFraming(channelizedSdhInterface.getFraming());
			}
			if (channelizedSdhInterface.getPosframing() != null) {
				channelizedSDHInterface.setPosFraming(channelizedSdhInterface.getPosframing());
			}
			if (channelizedSdhInterface.getJ() != null)
				channelizedSDHInterface.setJ(ActivationUtils.toString(channelizedSdhInterface.getJ()));
			if (channelizedSdhInterface.getKlm() != null)
				channelizedSDHInterface.setKLM(channelizedSdhInterface.getKlm());
			if (channelizedSdhInterface.getPortType() != null)
				channelizedSDHInterface.setPortType(channelizedSdhInterface.getPortType().toUpperCase());
			if (channelizedSdhInterface.getIsbfdEnabled() != null)
				channelizedSDHInterface.setBfdConfig(createCsdhHdlcConfig(channelizedSdhInterface));
			AccessControlList inBoundAccessControlList = new AccessControlList();
			AccessControlList outBoundAccessControlList = new AccessControlList();
			AccessControlList inBoundAccessControlListV6 = new AccessControlList();
			AccessControlList outBoundAccessControlListV6 = new AccessControlList();
			Set<AclPolicyCriteria> aclPolicyCriterias = channelizedSdhInterface.getAclPolicyCriterias();
			for (AclPolicyCriteria aclPolicyCriteria : aclPolicyCriterias) {
				if (aclPolicyCriteria.getEndDate() == null) {
					if (aclPolicyCriteria.getInboundIpv4AclName() != null) {// TODO
						inBoundAccessControlList.setName(aclPolicyCriteria.getInboundIpv4AclName());
						channelizedSDHInterface.setInboundAccessControlList(inBoundAccessControlList);
					}
					if (aclPolicyCriteria.getOutboundIpv4AclName() != null) {// TODO
						outBoundAccessControlList.setName(aclPolicyCriteria.getOutboundIpv4AclName());
						channelizedSDHInterface.setOutboundAccessControlList(outBoundAccessControlList);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						outBoundAccessControlListV6.setName(aclPolicyCriteria.getOutboundIpv6AclName());
						channelizedSDHInterface.setOutboundAccessControlListV6(outBoundAccessControlListV6);
					}

					if (aclPolicyCriteria.getOutboundIpv6AclName() != null) {// TODO
						inBoundAccessControlListV6.setName(aclPolicyCriteria.getInboundIpv6AclName());
						channelizedSDHInterface.setInboundAccessControlListV6(inBoundAccessControlListV6);
					}
				}
			}

		}
		return channelizedSDHInterface;
	}

	protected BFDConfig createCsdhHdlcConfig(ChannelizedSdhInterface channelizedSdhInterface) {
		BFDConfig bfdConfig = new BFDConfig();
		if (StringUtils.isNotBlank(channelizedSdhInterface.getBfdtransmitInterval()))
			bfdConfig.setTransmitInterval(ActivationUtils.toInteger(channelizedSdhInterface.getBfdtransmitInterval()));
		if (StringUtils.isNotBlank(channelizedSdhInterface.getBfdreceiveInterval()))
			bfdConfig.setRecieveInterval(ActivationUtils.toInteger(channelizedSdhInterface.getBfdreceiveInterval()));
		if (StringUtils.isNotBlank(channelizedSdhInterface.getBfdMultiplier()))
			bfdConfig.setMultiplier(ActivationUtils.toInteger(channelizedSdhInterface.getBfdMultiplier()));
		return bfdConfig;
	}

	protected WanRoutingProtocol createServiceWanRoutingProtocol(Bgp bgp, Ospf ospf, StaticProtocol staticProtocol) {
		WanRoutingProtocol wanRoutingProtocol = new WanRoutingProtocol();
		if (bgp != null)
			wanRoutingProtocol.setBgpRoutingProtocol(createWanRoutingBgpRoutingProtocol(bgp));
		if (ospf != null)
			wanRoutingProtocol.setOspfRoutingProtocol(createOspfRoutingProtocol(ospf));
		if (staticProtocol != null)
			wanRoutingProtocol.setStaticRoutingProtocol(createPeWanStaticRoutes(staticProtocol));

		if (wanRoutingProtocol.getBgpRoutingProtocol() == null && wanRoutingProtocol.getOspfRoutingProtocol() == null
				&& wanRoutingProtocol.getStaticRoutingProtocol() == null) {
			return null;
		}
		return wanRoutingProtocol;
	}

	/**
	 * createOspfRoutingProtocol
	 */
	protected OSPFRoutingProtocol createOspfRoutingProtocol(Ospf ospf) {
		OSPFRoutingProtocol ospfRoutingProtocol = new OSPFRoutingProtocol();
		if (ospf != null) {
			if (StringUtils.isNotBlank(ospf.getProcessId()))
				ospfRoutingProtocol.setProcessId(ospf.getProcessId());
			if (StringUtils.isNotBlank(ospf.getDomainId()))
				ospfRoutingProtocol.setDomainId(ospf.getDomainId());
			if (StringUtils.isNotBlank(ospf.getAreaId()))
				ospfRoutingProtocol.setAreaId(ospf.getAreaId());
			if (ospf.getIsnetworkP2p() != null)
				ospfRoutingProtocol.setIsNetworkTypeP2P(ActivationUtils.getStrValue(ospf.getIsnetworkP2p()));
			if (ospf.getOspfNetworkType() != null)
				ospfRoutingProtocol.setNetworkType(ospf.getOspfNetworkType());
			if (ospf.getIsignoreipOspfMtu() != null)
				ospfRoutingProtocol.setIsIPOSPFMTUIgnore(ActivationUtils.getStrValue(ospf.getIsignoreipOspfMtu()));
			if (ospf.getIsauthenticationRequired() != null)
				ospfRoutingProtocol
						.setIsAuthenticationRequired(ActivationUtils.getStrValue(ospf.getIsauthenticationRequired()));
			if (StringUtils.isNotBlank(ospf.getAuthenticationMode()))
				ospfRoutingProtocol.setAuthenticationMode(ospf.getAuthenticationMode().toUpperCase());
			if (StringUtils.isNotBlank(ospf.getPassword()))
				ospfRoutingProtocol.setAuthenticationPassword(ospf.getPassword());
			if (ospf.getIsredistributeConnectedEnabled() != null)
				ospfRoutingProtocol.setIsRedistributeConnectedEnabled(
						ActivationUtils.getStrValue(ospf.getIsredistributeConnectedEnabled()));
			if (ospf.getIsredistributeStaticEnabled() != null)
				ospfRoutingProtocol.setIsRedistributeStaticEnabled(
						ActivationUtils.getStrValue(ospf.getIsredistributeStaticEnabled()));
			if (StringUtils.isNotBlank(ospf.getCost()))
				ospfRoutingProtocol.setOspfCost(ActivationUtils.toInteger(ospf.getCost()));
			if (StringUtils.isNotBlank(ospf.getRedistributeRoutermapname()))
				ospfRoutingProtocol.setRedistributionRoutemapName(ospf.getRedistributeRoutermapname());
			if (StringUtils.isNotBlank(ospf.getVersion()))
				ospfRoutingProtocol.setVersion(ospf.getVersion().toUpperCase());
			if (StringUtils.isNotBlank(ospf.getLocalPreference()))
				ospfRoutingProtocol.setLocalPreference(ospf.getLocalPreference());
			if (StringUtils.isNotBlank(ospf.getLocalPreferenceV6()))
				ospfRoutingProtocol.setLocalPreferenceV6(ospf.getLocalPreferenceV6());
			RoutingPolicy routingPol = new RoutingPolicy();
			if (StringUtils.isNotBlank(ospf.getRedistributeRoutermapname())) {
				routingPol.setName(ospf.getRedistributeRoutermapname());
				ospfRoutingProtocol.setOSPFNeighborOutboundRoutingPolicy(routingPol);
			}
			RoutingPolicy routingPolV6 = new RoutingPolicy();
			if (StringUtils.isNotBlank(ospf.getRedistributeRoutermapname())) {
				routingPolV6.setName(ospf.getRedistributeRoutermapname());
				ospfRoutingProtocol.setOSPFNeighborOutboundRoutingPolicyV6(routingPolV6);
			}
		}
		return ospfRoutingProtocol;
	}

	protected BGPRoutingProtocol createWanRoutingBgpRoutingProtocol(Bgp bgp) {
		BGPRoutingProtocol bgpRoutingProtocol = new BGPRoutingProtocol();
		ASNumber remoteAsNumber = new ASNumber();
		if (bgp.getRemoteAsNumber() != null) {
			remoteAsNumber.setNumber(bgp.getRemoteAsNumber() + "");
			bgpRoutingProtocol.setRemoteASNumber(remoteAsNumber);
		}
		if (bgp.getRemoteUpdateSourceInterface() != null || bgp.getRemoteUpdateSourceIpv4Address() != null
				|| bgp.getRemoteUpdateSourceIpv6Address() != null) {
			LoopbackInterface loopbackInterface = new LoopbackInterface();// TODO
			if (StringUtils.isNotBlank(bgp.getRemoteUpdateSourceInterface()))
				loopbackInterface.setName(bgp.getRemoteUpdateSourceInterface());
			if (bgp.getRemoteUpdateSourceIpv4Address() != null) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(bgp.getRemoteUpdateSourceIpv4Address());
				loopbackInterface.setV4IpAddress(ipv4Address);
			}
			if (bgp.getRemoteUpdateSourceIpv6Address() != null) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(bgp.getRemoteUpdateSourceIpv6Address());
				loopbackInterface.setV6IpAddress(ipv6Address);
			}
			bgpRoutingProtocol.setRemoteBGPNeighbourUpdateSource(loopbackInterface);// NEIBHOR ON -> WAN/LOOPBACK
		}
		ASNumber bgpNeighbourLocalAsNumber = new ASNumber();
		if (StringUtils.isNotBlank(bgp.getLocalAsNumber())) {
			bgpNeighbourLocalAsNumber.setNumber(bgp.getLocalAsNumber());
			bgpRoutingProtocol.setBgpNeighbourLocalASNumber(bgpNeighbourLocalAsNumber);
		}
		if (bgp.getLocalUpdateSourceInterface() != null || bgp.getLocalUpdateSourceIpv4Address() != null
				|| bgp.getLocalUpdateSourceIpv6Address() != null) {
			LoopbackInterface localBgpNeibourLoopbackInterface = new LoopbackInterface();
			localBgpNeibourLoopbackInterface.setName(bgp.getLocalUpdateSourceInterface());
			if (bgp.getLocalUpdateSourceIpv4Address() != null) {
				IPV4Address lbnipv4Address = new IPV4Address();
				lbnipv4Address.setAddress(bgp.getLocalUpdateSourceIpv4Address());
				localBgpNeibourLoopbackInterface.setV4IpAddress(lbnipv4Address);
			}
			if (bgp.getLocalUpdateSourceIpv6Address() != null) {
				IPV6Address lbnipv6Address = new IPV6Address();
				lbnipv6Address.setAddress(bgp.getLocalUpdateSourceIpv6Address());
				localBgpNeibourLoopbackInterface.setV6IpAddress(lbnipv6Address);
			}
			bgpRoutingProtocol.setLocalBGPNeighbourUpdateSource(localBgpNeibourLoopbackInterface);
		}
		if (bgp.getAsoOverride() != null)
			bgpRoutingProtocol.setIsASOverriden(ActivationUtils.getStrValue(bgp.getAsoOverride()));
		if (bgp.getIsebgpMultihopReqd() != null)
			bgpRoutingProtocol.setIsEBGPMultihopRequired(ActivationUtils.getStrValue(bgp.getIsebgpMultihopReqd()));
		bgpRoutingProtocol.setMaxPrefix(ActivationUtils.toInteger(bgp.getMaxPrefix()));
		if (bgp.getSplitHorizon() != null)
			bgpRoutingProtocol.setSplitHorizon(ActivationUtils.getBooleanValue(bgp.getSplitHorizon()));
		bgpRoutingProtocol.setMaxPrefixThreshold(ActivationUtils.toInteger(bgp.getMaxPrefixThreshold()));
		bgpRoutingProtocol.setIsRedistributeConnectedEnabled(
				ActivationUtils.getBooleanValue(bgp.getRedistributeConnectedEnabled()));
		bgpRoutingProtocol
				.setIsRedistributeStaticEnabled(ActivationUtils.getBooleanValue(bgp.getRedistributeStaticEnabled()));
		if (bgp.getSooRequired() != null)
			bgpRoutingProtocol.setIsSOORequired(ActivationUtils.getBooleanValue(bgp.getSooRequired()));
		if (bgp.getHelloTime() != null)
			bgpRoutingProtocol.setHelloTime(ActivationUtils.toInteger(bgp.getHelloTime()));
		if (bgp.getHoldTime() != null)
			bgpRoutingProtocol.setHoldTime(ActivationUtils.toInteger(bgp.getHoldTime()));

		if (bgp.getPassword() != null && !bgp.getPassword().isEmpty()) {
			bgpRoutingProtocol.setAuthenticationPassword(bgp.getPassword());
			bgpRoutingProtocol.setIsAuthenticationRequired(true);
			bgpRoutingProtocol.setAuthenticationMode("MD5");
		}
		if (bgp.getRoutesExchanged() != null) {
			RoutesExchanged routesExchanged = new RoutesExchanged();

			routesExchanged.setRoute(bgp.getRoutesExchanged().toUpperCase());
			bgpRoutingProtocol.setRoutesExchanged(routesExchanged);
		}
		if (bgp.getRemoteIpv4Address() != null) {
			IPV4Address remoteIpv4Address = new IPV4Address();
			remoteIpv4Address.setAddress(bgp.getRemoteIpv4Address());
			bgpRoutingProtocol.setRemoteIPV4Address(remoteIpv4Address);
		}
		if (bgp.getRemoteIpv6Address() != null) {
			IPV6Address remoteIpv6Address = new IPV6Address();
			remoteIpv6Address.setAddress(bgp.getRemoteIpv6Address());
			bgpRoutingProtocol.setRemoteIPV6Address(remoteIpv6Address);
		}
		if (bgp.getLocalPreference() != null && !bgp.getLocalPreference().isEmpty())
			bgpRoutingProtocol.setLocalPreference(bgp.getLocalPreference());
		if (bgp.getIsmultihopTtl() != null)
			bgpRoutingProtocol.setMultihopTTL(ActivationUtils.toInteger(bgp.getIsmultihopTtl()));
		if (bgp.getNeighbourshutdownRequired() != null)
			bgpRoutingProtocol.setIsNeighbourShutdownRequired(
					ActivationUtils.getBooleanValue(bgp.getNeighbourshutdownRequired()));
		return bgpRoutingProtocol;
	}

	protected void createArrayOfInboundPrefixes() {

	}

	protected StaticRoutes createPeWanAdditionalStaticRoutesBgp(Set<WanStaticRoute> wanstaticRoutes) {
		StaticRoutes staticRoutes = null;

		for (WanStaticRoute wanStaticRoute : wanstaticRoutes) {
			if (wanStaticRoute.getIsPewan() != null && ActivationUtils.getBooleanValue(wanStaticRoute.getIsPewan())) {
				staticRoutes = new StaticRoutes();
				IPSubnetADValueMap ipSubNetAdValue = new IPSubnetADValueMap();
				IpSubnet ipSubNet = new IpSubnet();
				if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(".")) {
					IPV4Address ipv4Address = new IPV4Address();
					ipv4Address.setAddress(wanStaticRoute.getIpsubnet());// TODO- Regular Expression
					ipSubNet.setV4Address(ipv4Address);
					ipSubNetAdValue.setIpSubnet(ipSubNet);
				}
				if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(":")) {
					IPV6Address ipv6Address = new IPV6Address();
					ipv6Address.setAddress(wanStaticRoute.getIpsubnet());// TODO
					ipSubNet.setV6Address(ipv6Address);
					ipSubNetAdValue.setIpSubnet(ipSubNet);
				}
				if (wanStaticRoute.getAdvalue() != null)
					ipSubNetAdValue.setAdValue(wanStaticRoute.getAdvalue());
				if (wanStaticRoute.getNextHopid() != null)
					ipSubNetAdValue.setNextHopIp(wanStaticRoute.getNextHopid());
				if (wanStaticRoute.getDescription() != null)
					ipSubNetAdValue.setDescription(wanStaticRoute.getDescription());
				if (wanStaticRoute.getGlobal() != null) {
					ipSubNetAdValue.setIsGlobal(wanStaticRoute.getGlobal() == 1 ? true : false);
				} // SET NULL if null in db
				if (wanStaticRoute.getPopCommunity() != null)
					staticRoutes.setPopCommunity(wanStaticRoute.getPopCommunity());
				if (wanStaticRoute.getRegionalCommunity() != null)
					staticRoutes.setRegionalCommunity(wanStaticRoute.getRegionalCommunity());
				if (wanStaticRoute.getServiceCommunity() != null)
					staticRoutes.setServiceCommunity(wanStaticRoute.getServiceCommunity());
				staticRoutes.getStaticRouteList().add(ipSubNetAdValue);
			}
		}
		return staticRoutes;
	}

	protected StaticRoutingProtocol createPeWanStaticRoutes(StaticProtocol staticProtocol) {
		StaticRoutingProtocol staticRoutingProtocol = null;
		if (staticProtocol != null) {
			staticRoutingProtocol = new StaticRoutingProtocol();
			for (WanStaticRoute wanStaticRoute : staticProtocol.getWanStaticRoutes()) {
				if (wanStaticRoute.getIsPewan() == 1 && wanStaticRoute.getIpsubnet() != null) {
					staticRoutingProtocol.setPEWANStaticRoutes(createWantStaticRoutes(staticRoutingProtocol,
							wanStaticRoute, staticRoutingProtocol.getPEWANStaticRoutes()));
				}
				if (wanStaticRoute.getIsCewan() == 1 && wanStaticRoute.getIpsubnet() != null) {
					staticRoutingProtocol
							.setCEWANStaticRoutes(createWantStaticRoutes(staticRoutingProtocol, wanStaticRoute,staticRoutingProtocol.getCEWANStaticRoutes()));
				}
			}
		}
		if (staticRoutingProtocol != null && staticRoutingProtocol.getPEWANStaticRoutes() == null
				&& staticRoutingProtocol.getCEWANStaticRoutes() == null) {
			return null;
		}
		return staticRoutingProtocol;

	}

	/**
	 * createWantStaticRoutes
	 * 
	 * @param staticRoutingProtocol
	 * @param wanStaticRoute
	 */
	private StaticRoutes createWantStaticRoutes(StaticRoutingProtocol staticRoutingProtocol,
			WanStaticRoute wanStaticRoute, StaticRoutes peStaticRoutes) {
		if (peStaticRoutes == null)
			peStaticRoutes = new StaticRoutes();
		IPSubnetADValueMap ipSubNetAdValue = new IPSubnetADValueMap();
		IpSubnet ipSubNet = new IpSubnet();
		if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(".")) {
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(wanStaticRoute.getIpsubnet());// Regex should be applies
			ipSubNet.setV4Address(ipv4Address);
		}
		if (wanStaticRoute.getIpsubnet() != null && wanStaticRoute.getIpsubnet().contains(":")) {
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(wanStaticRoute.getIpsubnet());
			ipSubNet.setV6Address(ipv6Address);// Regex should be applies
		}
		ipSubNetAdValue.setIpSubnet(ipSubNet);
		ipSubNetAdValue.setAdValue(wanStaticRoute.getAdvalue());
		ipSubNetAdValue.setNextHopIp(wanStaticRoute.getNextHopid());
		ipSubNetAdValue.setDescription(wanStaticRoute.getDescription());
		ipSubNetAdValue.setIsGlobal(wanStaticRoute.getGlobal()!=null &&  wanStaticRoute.getGlobal()== 1 ? true : false);
		peStaticRoutes.getStaticRouteList().add(ipSubNetAdValue);
		if (wanStaticRoute.getPopCommunity() != null)
			peStaticRoutes.setPopCommunity(wanStaticRoute.getPopCommunity());
		if (wanStaticRoute.getRegionalCommunity() != null)
			peStaticRoutes.setRegionalCommunity(wanStaticRoute.getRegionalCommunity());
		if (wanStaticRoute.getServiceCommunity() != null)
			peStaticRoutes.setServiceCommunity(wanStaticRoute.getServiceCommunity());
		return peStaticRoutes;
	}

	protected TopologyInfo createTopologyInformation(Topology topology) {
		TopologyInfo topologyInfo = new TopologyInfo();
		topologyInfo.setName(topology.getTopologyName());
		Switch switches = createUniSwitchDetails(topology);
		if (switches != null)
			topologyInfo.setUNISwitch(switches);
		else
			return null;
		return topologyInfo;

	}

	protected EthernetInterface createRouterUplinkPorts1(RouterUplinkport routerUplinkPort) {
		EthernetInterface ethernetInterface = new EthernetInterface();
		ethernetInterface.setPhysicalPortName(routerUplinkPort.getPhysicalPort1Name());
		ethernetInterface.setName(routerUplinkPort.getPhysicalPort1Name());
		return ethernetInterface;
	}

	protected EthernetInterface createRouterUplinkPorts2(RouterUplinkport routerUplinkPort) {
		EthernetInterface ethernetInterface = new EthernetInterface();
		ethernetInterface.setPhysicalPortName(routerUplinkPort.getPhysicalPort2Name());
		ethernetInterface.setName(routerUplinkPort.getPhysicalPort2Name());
		return ethernetInterface;
	}

	protected Switch createUniSwitchDetails(Topology topology) {
		Switch swith = null;
		Set<UniswitchDetail> uniSwitches = topology.getUniswitchDetails();
		for (UniswitchDetail uniswitchDetail : uniSwitches) {
			if (uniswitchDetail.getHostName() != null) {

				swith = new Switch();
				swith.setHostName(uniswitchDetail.getHostName());
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(uniswitchDetail.getMgmtIp());
				swith.setV4ManagementIPAddress(ipv4Address);
				Switch.Interface sInterface = new Switch.Interface();
				EthernetInterface etherInterface = new EthernetInterface();
				etherInterface.setPhysicalPortName(uniswitchDetail.getPhysicalPort());
				etherInterface.setSvlan(ActivationUtils.toInteger(uniswitchDetail.getOuterVlan()));
				etherInterface.setVlan(ActivationUtils.toInteger(uniswitchDetail.getInnerVlan()));
				if(uniswitchDetail.getPortType()!=null && !uniswitchDetail.getPortType().toUpperCase().contains("LAG")) {
				etherInterface.setPortType(uniswitchDetail.getPortType().toUpperCase());
				}
				if(uniswitchDetail.getMode()!=null) {
				etherInterface.setMode(uniswitchDetail.getMode().toUpperCase());
				}
				if (uniswitchDetail.getMaxMacLimit() != null)
					etherInterface.setMaxMacLimit(uniswitchDetail.getMaxMacLimit());
				etherInterface.setMediaType(uniswitchDetail.getMediaType());
				if (uniswitchDetail.getInterfaceName() != null)
					etherInterface.setName(uniswitchDetail.getInterfaceName());
				if (uniswitchDetail.getHandoff() != null)
					etherInterface.setHandOff(uniswitchDetail.getHandoff());
				etherInterface.setSpeed(uniswitchDetail.getSpeed());
				etherInterface.setIsAutoNegotiation(uniswitchDetail.getAutonegotiationEnabled());
				etherInterface.setDuplex(ActivationUtils.getDuplex(uniswitchDetail.getDuplex()));
				if(uniswitchDetail.getSyncVlanReqd()!=null){
					etherInterface.setSyncVLAN(uniswitchDetail.getSyncVlanReqd()==1?true:false);
				}
				sInterface.setEthernetInterface(etherInterface);
				if (!(uniswitchDetail.getHandoff() != null && uniswitchDetail.getHandoff().equals("NNI"))) {
					swith.setInterface(sInterface);
				} else {
					swith = null;
					break;
				}

			}
		}
		return swith;
	}

	protected QoS createServiceQos(ServiceDetail serviceDetail) {
		QoS qoS = new QoS();
		try {
		Set<ServiceQo> quoses = serviceDetail.getServiceQos().stream().filter(ser->ser.getEndDate()==null).collect(Collectors.toSet());
		
	List<AluSchedulerPolicy>	aluSchedulerPolicys=serviceDetail.getAluSchedulerPolicies().stream().filter(alu->alu.getEndDate()==null).collect(Collectors.toList());
		for (AluSchedulerPolicy aluSchedulerPolicy : aluSchedulerPolicys) {
			ALUSchedulerPolicy aluSch = new ALUSchedulerPolicy();
			aluSch.setName(aluSchedulerPolicy.getSchedulerPolicyName());
			aluSch.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSchedulerPolicyIspreprovisioned()));
			aluSch.setTotalCIRbandwidth(aluSchedulerPolicy.getTotalCirBw());
			aluSch.setTotalPIRbandwidth(aluSchedulerPolicy.getTotalPirBw());
			qoS.setALUSchedulerPolicy(aluSch);
			ALUSchedulerPolicy aLUEgressPolicy = new ALUSchedulerPolicy();
			aLUEgressPolicy.setName(aluSchedulerPolicy.getSapEgressPolicyname());
			aLUEgressPolicy.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSapEgressPreprovisioned()));
			qoS.setALUEgressPolicy(aLUEgressPolicy);
			ALUSchedulerPolicy aLUingressPolicy = new ALUSchedulerPolicy();
			aLUingressPolicy.setName(aluSchedulerPolicy.getSapIngressPolicyname());
			aLUingressPolicy.setIsPreprovisioned(
					ActivationUtils.getBooleanValue(aluSchedulerPolicy.getSapIngressPreprovisioned()));
			qoS.setALUIngressPolicy(aLUingressPolicy);
		}
		for (ServiceQo serviceQo : quoses) {
			qoS.setCosType(serviceQo.getCosType().toUpperCase());
			if (StringUtils.isNotBlank(serviceQo.getCosProfile()))
				qoS.setQoSProfile(serviceQo.getCosProfile().toUpperCase());
			if (StringUtils.isNotBlank(serviceQo.getQosTrafiicMode()))
				qoS.setQoSTrafficMode(serviceQo.getQosTrafiicMode().toUpperCase());
			if (serviceQo.getIsbandwidthApplicable() != null)
				qoS.setIsBandwidthApplicable(ActivationUtils.getStrValue(serviceQo.getIsbandwidthApplicable()));
			// Change the approach
			List<ServiceCosCriteria> serviceCosCriteriasv1 = serviceCosCriteriaRepository
					.findByServiceQo_ServiceQosIdAndEndDateIsNullOrderByCosNameAsc(serviceQo.getServiceQosId());// This
																												// will
																												// give
																												// the
																												// order
																												// of
																												// cos
			Map<String, CoS> cosMapper = new HashMap<>();
			for (ServiceCosCriteria serviceCosCriteria : serviceCosCriteriasv1) {// TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				CoS cos = null;
				CoS.ClassificationCriteria classCri = null;
				if (cosMapper.get(serviceCosCriteria.getCosName()) == null) {
					cos = new CoS();
					classCri = new CoS.ClassificationCriteria();// TODO
					cos.setClassificationCriteria(classCri);
					cosMapper.put(serviceCosCriteria.getCosName(), cos);
					classCri = cos.getClassificationCriteria();
				} else {
					cos = cosMapper.get(serviceCosCriteria.getCosName());
				}
				if(serviceCosCriteria.getBwBpsunit()!=null) {
					Bandwidth bw = new Bandwidth();
					bw.setSpeed(Math.abs(ActivationUtils.toFloat(serviceCosCriteria.getBwBpsunit())));
					bw.setUnit(BandwidthUnit.BPS);// Same Unit to Pir
					cos.setBandwidth(bw);
				}else if(serviceDetail.getServiceBandwidth()!=null){
					Bandwidth bw = new Bandwidth();
					bw.setSpeed(Math.abs(serviceDetail.getServiceBandwidth()*1000000));
					bw.setUnit(BandwidthUnit.BPS);// Same Unit to Pir
					cos.setBandwidth(bw);
				}
				if (StringUtils.isNotBlank(serviceQo.getPirBw())) {
					if (serviceCosCriteria.getCosName().equals("COS1")) {
						Bandwidth pirBw = new Bandwidth();
						pirBw.setSpeed((ActivationUtils.toFloat(serviceCosCriteria.getCosPercent()) / 100F)
								* ActivationUtils.toFloat(serviceQo.getPirBw()));
						pirBw.setUnit(ActivationUtils.getBandwithUnit(serviceQo.getPirBwUnit()));
						cos.setPIRBandwidth(pirBw);
					} else {
						Bandwidth pirBw = new Bandwidth();
						pirBw.setSpeed(Math.abs(ActivationUtils.toFloat(serviceQo.getPirBw())));
						pirBw.setUnit(ActivationUtils.getBandwithUnit(serviceQo.getPirBwUnit()));
						cos.setPIRBandwidth(pirBw);
					}
				}
				
				cos.setCosUpdateAction(serviceQo.getCosUpdateAction().toUpperCase());
				if (serviceQo.getPirBw() != null) {
					cos.setBandwidthinKBPS(String.valueOf(Math.round((ActivationUtils.toFloat(serviceCosCriteria.getBwBpsunit())/1000))));// Already rules engine in converting to kbps
				}
				// cos.setIsDefaultFC(serviceQo.getIsdefaultFc() == 1 ? true : false);

				if (serviceCosCriteria.getClassificationCriteria().equals("ipprecedence")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					classCri.setIpprecedent(new IPPrecedent());
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal1()) && !serviceCosCriteria.getIpprecedenceVal1().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue1(serviceCosCriteria.getIpprecedenceVal1());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal2()) && !serviceCosCriteria.getIpprecedenceVal2().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue2(serviceCosCriteria.getIpprecedenceVal2());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal3()) && !serviceCosCriteria.getIpprecedenceVal3().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue3(serviceCosCriteria.getIpprecedenceVal3());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal4()) && !serviceCosCriteria.getIpprecedenceVal4().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue4(serviceCosCriteria.getIpprecedenceVal4());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal5()) && !serviceCosCriteria.getIpprecedenceVal5().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue5(serviceCosCriteria.getIpprecedenceVal5());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal6()) && !serviceCosCriteria.getIpprecedenceVal6().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue6(serviceCosCriteria.getIpprecedenceVal6());
					}
					if(Objects.nonNull(serviceCosCriteria.getIpprecedenceVal7()) && !serviceCosCriteria.getIpprecedenceVal7().isEmpty()){
						classCri.getIpprecedent().setCriteriaValue7(serviceCosCriteria.getIpprecedenceVal7());
					}
				}
				if (serviceCosCriteria.getClassificationCriteria().equals("DSCP")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					classCri.setDscpValue(new DscpValue());
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal1()) && !serviceCosCriteria.getDhcpVal1().isEmpty()){
						classCri.getDscpValue().setCriteriaValue1(serviceCosCriteria.getDhcpVal1().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal2()) && !serviceCosCriteria.getDhcpVal2().isEmpty()){
						classCri.getDscpValue().setCriteriaValue2(serviceCosCriteria.getDhcpVal2().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal3()) && !serviceCosCriteria.getDhcpVal3().isEmpty()){
						classCri.getDscpValue().setCriteriaValue3(serviceCosCriteria.getDhcpVal3().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal4()) && !serviceCosCriteria.getDhcpVal4().isEmpty()){
						classCri.getDscpValue().setCriteriaValue4(serviceCosCriteria.getDhcpVal4().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal5()) && !serviceCosCriteria.getDhcpVal5().isEmpty()){
						classCri.getDscpValue().setCriteriaValue5(serviceCosCriteria.getDhcpVal5().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal6()) && !serviceCosCriteria.getDhcpVal6().isEmpty()){
						classCri.getDscpValue().setCriteriaValue6(serviceCosCriteria.getDhcpVal6().toLowerCase());
					}
					if(Objects.nonNull(serviceCosCriteria.getDhcpVal7()) && !serviceCosCriteria.getDhcpVal7().isEmpty()){
						classCri.getDscpValue().setCriteriaValue7(serviceCosCriteria.getDhcpVal7().toLowerCase());
					}
				}

				if (serviceCosCriteria.getClassificationCriteria().toUpperCase().equals("ANY")) {
					cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					if (classCri != null)
						classCri.setAny("ANY");
				}

				if (serviceCosCriteria.getClassificationCriteria().equals("IP Address")) {
					Set<AclPolicyCriteria> aclPolicies = serviceCosCriteria.getAclPolicyCriterias().stream().filter(acl->acl.getEndDate()==null).collect(Collectors.toSet());
					classCri.setAccessControlList(new AccessControlList());
					for (AclPolicyCriteria aclPolicy : aclPolicies) {
						if (aclPolicy.getEndDate() == null) {
							classCri.getAccessControlList().setName(aclPolicy.getIssvcQosCoscriteriaIpaddrAclName());
							AccessControListlEntry accessControListlEntry = new AccessControListlEntry();
							accessControListlEntry.setSeqNo(ActivationUtils.toInteger(aclPolicy.getSequence()));
							accessControListlEntry.setAction(Objects.nonNull(aclPolicy.getAction())?aclPolicy.getAction().toUpperCase():aclPolicy.getAction());
							AccessControListlEntry.SourceSubnet souceSubNetValue = new AccessControListlEntry.SourceSubnet();
							String sval = ActivationUtils.getStrValue(aclPolicy.getSourceAny());
							if (sval != null) {
								souceSubNetValue.setAny(sval.equals("TRUE") ? "ANY" : null);
							}
							if (aclPolicy.getSourceSubnet() != null) {
								if (aclPolicy.getSourceSubnet().contains(".")) {
									IPV4Address souipv4Address = new IPV4Address();
									souipv4Address.setAddress(aclPolicy.getSourceSubnet());
									souceSubNetValue.setV4Address(souipv4Address);
								} else if (aclPolicy.getSourceSubnet().contains(":")) {
									IPV6Address souipv6Address = new IPV6Address();
									souipv6Address.setAddress(aclPolicy.getSourceSubnet());
									souceSubNetValue.setV6Address(souipv6Address);
								}
							}
							accessControListlEntry.setSourceSubnet(souceSubNetValue);
							accessControListlEntry.setSourceCondition(aclPolicy.getSourceCondition());
							accessControListlEntry.setSourcePortNumber(aclPolicy.getSourcePortnumber());
							AccessControListlEntry.DestinationSubnet destinationSubNetValue = new AccessControListlEntry.DestinationSubnet();
							String dval = ActivationUtils.getStrValue(aclPolicy.getDestinationAny());
							if (dval != null) {
								destinationSubNetValue.setAny(dval.equals("TRUE") ? "ANY" : null);
							}

							if (aclPolicy.getDestinationSubnet() != null) {
								if (aclPolicy.getDestinationSubnet().contains(".")) {
									IPV4Address deipv4Address = new IPV4Address();
									deipv4Address.setAddress(aclPolicy.getDestinationSubnet());
									destinationSubNetValue.setV4Address(deipv4Address);
								} else if (aclPolicy.getDestinationSubnet().contains(":")) {
									IPV6Address deipv6Address = new IPV6Address();
									deipv6Address.setAddress(aclPolicy.getDestinationSubnet());
									destinationSubNetValue.setV6Address(deipv6Address);
								}
							}
							accessControListlEntry.setDestinationSubnet(destinationSubNetValue);
							if (aclPolicy.getSourceCondition() != null)
								accessControListlEntry.setSourceCondition(aclPolicy.getSourceCondition().toUpperCase());
							/*IPV4Address deipv4Address = new IPV4Address();
							deipv4Address.setAddress(aclPolicy.getDestinationSubnet());
							destinationSubNetValue.setV4Address(deipv4Address);
							accessControListlEntry.setDestinationSubnet(destinationSubNetValue);*/
							if (aclPolicy.getProtocol() != null && !aclPolicy.getProtocol().isEmpty()) {
								accessControListlEntry.setProtocol(aclPolicy.getProtocol().toUpperCase());
							} else {
								accessControListlEntry.setProtocol("IP");
							}
							accessControListlEntry.setDescription(aclPolicy.getDescription());
							if (aclPolicy.getDestinationPortnumber() != null
									&& !aclPolicy.getDestinationPortnumber().isEmpty()) {
								accessControListlEntry.setPortNumber(aclPolicy.getDestinationPortnumber());
							}
							if (aclPolicy.getDestinationCondition() != null
									&& !aclPolicy.getDestinationCondition().isEmpty()) {
								accessControListlEntry.setCondition(aclPolicy.getDestinationCondition().toUpperCase());
							}
							classCri.getAccessControlList().getAccessControlListEntry().add(accessControListlEntry);
						}
						cos.setName(serviceCosCriteria.getCosName().toUpperCase());
					}
				}
				qoS.getCos().add(cos);
			}

			if (!qoS.getCos().isEmpty()) {
				Integer lastRecord = qoS.getCos().size() - 1;
				CoS coS=qoS.getCos().get(lastRecord);
				coS.setIsDefaultFC(serviceQo.getIsdefaultFc() == 1 ? true : false);
				/*if (serviceQo.getPirBw() != null) {
					coS.setBandwidthinKBPS(String.valueOf(Math.round(ActivationUtils.toFloat(serviceQo.getPirBw()))));// Already rules engine in converting to kbps
				}*/
			}
		}
		}catch(Exception e) {
			LOGGER.error("Exception in AbstractConfiguration.createServiceQos::{}",e);
		}

		return qoS;
	}

	protected void createAluSchedulerPolicy() {

	}

	protected void createSapIngressPolicy() {

	}

	protected void createSapEngressPolicy() {

	}

	protected CustomerPremiseEquipment createCpeDetails(InterfaceProtocolMapping interfaceProtocolMapping) {
		Cpe cpe = interfaceProtocolMapping.getCpe();
		CustomerPremiseEquipment customerPremiseEquipment = new CustomerPremiseEquipment();
		customerPremiseEquipment.setHostName(cpe.getHostName());

		if (isLoopBackTag(cpe)) {
			Interface loopbackInterface = new Interface();
			if (cpe.getLoopbackInterfaceName() != null) {
				loopbackInterface.setName(cpe.getLoopbackInterfaceName());
			}
			if (StringUtils.isNotBlank(cpe.getMgmtLoopbackV4address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(cpe.getMgmtLoopbackV4address());
				loopbackInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(cpe.getMgmtLoopbackV6address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(cpe.getMgmtLoopbackV6address());
				loopbackInterface.setV6IpAddress(ipv6Address);
			}
			customerPremiseEquipment.setLoopbackInterface(loopbackInterface);
		}

		customerPremiseEquipment.setIsCEACEConfigurable(ActivationUtils.getBooleanValue(cpe.getIsaceconfigurable()));
		customerPremiseEquipment.setSnmpServerCommunity(cpe.getSnmpServerCommunity());
		if (interfaceProtocolMapping.getIscpeWanInterface() == 1)
			customerPremiseEquipment.setWanInterface(createCpeWanInterface(cpe, interfaceProtocolMapping));
		else
			return null;
		return customerPremiseEquipment;
	}

	protected boolean isLoopBackTag(Cpe cpe) {

		if (cpe.getLoopbackInterfaceName() != null || cpe.getMgmtLoopbackV4address() != null
				|| cpe.getMgmtLoopbackV6address() != null) {
			return true;
		}
		return false;
	}

	protected void createCpeInitConfigParam() {

	}

	protected WANInterface createCpeWanInterface(Cpe cpe, InterfaceProtocolMapping interfaceProtocolMapping) {
		WANInterface wanInterface = new WANInterface();
		WANInterface.Interface wIntfce = new WANInterface.Interface();
		EthernetInterface etherInterface = new EthernetInterface();
		com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter = interfaceProtocolMapping
				.getEthernetInterface();
		if (etherInter != null) {
			if (StringUtils.isNotBlank(etherInter.getInterfaceName()))
				etherInterface.setName(etherInter.getInterfaceName());
			if (StringUtils.isNotBlank(etherInter.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(etherInter.getModifiedIpv4Address());
				etherInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(etherInter.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(etherInter.getModifiedIpv6Address());
				etherInterface.setV6IpAddress(ipv6Address);
			}
			if (StringUtils.isNotBlank(etherInter.getInnerVlan()))
				etherInterface.setVlan(ActivationUtils.toInteger(etherInter.getInnerVlan()));
			if (StringUtils.isNotBlank(etherInter.getPortType()))
				etherInterface.setPortType(etherInter.getPortType().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getMode()))
				etherInterface.setMode(etherInter.getMode().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getMediaType()))
				etherInterface.setMediaType(etherInter.getMediaType().toUpperCase());
			if (StringUtils.isNotBlank(etherInter.getSpeed()))
				etherInterface.setSpeed(etherInter.getSpeed());
			if (StringUtils.isNotBlank(etherInter.getDuplex()))
				etherInterface.setDuplex(ActivationUtils.getDuplex(etherInter.getDuplex()));
			if (StringUtils.isNotBlank(etherInter.getAutonegotiationEnabled()))
				etherInterface.setIsAutoNegotiation(etherInter.getAutonegotiationEnabled());
			wIntfce.setEthernetInterface(etherInterface);
		}

		ChannelizedE1serialInterface channelizedE1Serial = interfaceProtocolMapping.getChannelizedE1serialInterface();
		SerialInterface serialInter = new SerialInterface();
		if (channelizedE1Serial != null) {
			if (StringUtils.isNotBlank(channelizedE1Serial.getInterfaceName()))
				serialInter.setName(channelizedE1Serial.getInterfaceName());
			if (StringUtils.isNotBlank(channelizedE1Serial.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedE1Serial.getModifiedIpv4Address());
				serialInter.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(channelizedE1Serial.getModifiedIpv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedE1Serial.getModifiedIpv6Address());
				serialInter.setV6IpAddress(ipv6Address);
			}
			wIntfce.setSerialInterface(serialInter);
		}

		ChannelizedSdhInterface channelizedSdhInfe = interfaceProtocolMapping.getChannelizedSdhInterface();
		ChannelizedSDHInterface channelizedSDHInterface = new ChannelizedSDHInterface();
		if (channelizedSdhInfe != null) {
			if (StringUtils.isNotBlank(channelizedSdhInfe.getInterfaceName()))
				channelizedSDHInterface.setName(channelizedSdhInfe.getInterfaceName());
			if (StringUtils.isNotBlank(channelizedSdhInfe.getModifiedIpv4Address())) {
				IPV4Address ipv4Address = new IPV4Address();
				ipv4Address.setAddress(channelizedSdhInfe.getModifiedIpv4Address());
				channelizedSDHInterface.setV4IpAddress(ipv4Address);
			}
			if (StringUtils.isNotBlank(channelizedSdhInfe.getModifiedIipv6Address())) {
				IPV6Address ipv6Address = new IPV6Address();
				ipv6Address.setAddress(channelizedSdhInfe.getModifiedIipv6Address());
				channelizedSDHInterface.setV6IpAddress(ipv6Address);
			}
			wIntfce.setChannelizedSDHInterface(channelizedSDHInterface);
		}

		wanInterface.setInterface(wIntfce);
		return wanInterface;
	}

	protected LANInterface createCpeLanInterface(Cpe cpe, InterfaceProtocolMapping interfaceProtocolMapping) {
		LANInterface lanInterface = new LANInterface();
		EthernetInterface etherInterface = new EthernetInterface();
		com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter = interfaceProtocolMapping
				.getEthernetInterface();
		etherInterface.setName(etherInter.getInterfaceName());
		IPV4Address ipv4Address = new IPV4Address();
		ipv4Address.setAddress(etherInter.getIpv4Address());
		etherInterface.setV4IpAddress(ipv4Address);
		IPV6Address ipv6Address = new IPV6Address();
		ipv6Address.setAddress(etherInter.getIpv6Address());
		etherInterface.setV6IpAddress(ipv6Address);
		etherInterface.setVlan(ActivationUtils.toInteger(etherInter.getInnerVlan()));
		etherInterface.setPortType(etherInter.getPortType().toUpperCase());
		etherInterface.setMode(etherInter.getMode().toUpperCase());
		etherInterface.setMediaType(etherInter.getMediaType().toUpperCase());
		etherInterface.setSpeed(etherInter.getSpeed());
		etherInterface.setDuplex(ActivationUtils.getDuplex(etherInter.getDuplex()));
		etherInterface.setIsAutoNegotiation(etherInter.getAutonegotiationEnabled());
		lanInterface.setInterface(etherInterface);
		lanInterface.setHSRPProtocol(createCpeHrspProtocol(etherInter));
		lanInterface.setRoutingProtocol(createCpeLanRoutingProtocol(interfaceProtocolMapping));
		return lanInterface;

	}

	protected HSRPProtocol createCpeHrspProtocol(
			com.tcl.dias.serviceactivation.entity.entities.EthernetInterface etherInter) {
		HSRPProtocol hsrpProtocol = null;
		Set<HsrpVrrpProtocol> hsrpProtocols = etherInter.getHsrpVrrpProtocols();// It is a array
		for (HsrpVrrpProtocol hsrpVrrpProtocol : hsrpProtocols) {
			hsrpProtocol = new HSRPProtocol();
			hsrpProtocol.setHelloValue(hsrpVrrpProtocol.getHelloValue());
			if (hsrpVrrpProtocol.getHoldValue() != null)
				hsrpProtocol.setHoldValue(hsrpVrrpProtocol.getHoldValue());
			if (hsrpVrrpProtocol.getTimerUnit() != null)
				hsrpProtocol.setTimerUnit(hsrpVrrpProtocol.getTimerUnit());
			if (hsrpVrrpProtocol.getVirtualIpv4Address() != null)
				hsrpProtocol.setVirtualIPAddress(hsrpVrrpProtocol.getVirtualIpv4Address());
			if (hsrpVrrpProtocol.getRole() != null)
				hsrpProtocol.setRole(hsrpVrrpProtocol.getRole().toUpperCase());
			if (hsrpVrrpProtocol.getVirtualIpv6Address() != null)
				hsrpProtocol.setVirtualIPv6Address(hsrpVrrpProtocol.getVirtualIpv6Address());
			if (hsrpVrrpProtocol.getVirtualIpv4Address() != null)
				hsrpProtocol.setVirtualIPAddress(hsrpVrrpProtocol.getVirtualIpv4Address());
			if (hsrpVrrpProtocol.getPriority() != null)
				hsrpProtocol.setPriority(ActivationUtils.toInteger(hsrpVrrpProtocol.getPriority()));
		}
		return hsrpProtocol;
	}

	protected LANInterface.RoutingProtocol createCpeLanRoutingProtocol(
			InterfaceProtocolMapping interfaceProtocolMapping) {
		LANInterface.RoutingProtocol lanRoutingProtocol = null;
		BGPRoutingProtocol bgpRoutingProtocol = null;
		Bgp bgp = interfaceProtocolMapping.getBgp();
		if (bgp != null) {
			lanRoutingProtocol = new LANInterface.RoutingProtocol();
			bgpRoutingProtocol = new BGPRoutingProtocol();
			if (bgp.getRemoteAsNumber() != null) {
				ASNumber asNumber = new ASNumber();
				asNumber.setNumber(ActivationUtils.toString(bgp.getRemoteAsNumber()));
				bgpRoutingProtocol.setRemoteASNumber(asNumber);
			}
			IPV4Address ipv4Address = new IPV4Address();
			ipv4Address.setAddress(bgp.getRemoteIpv4Address());
			bgpRoutingProtocol.setRemoteIPV4Address(ipv4Address);
			IPV6Address ipv6Address = new IPV6Address();
			ipv6Address.setAddress(bgp.getRemoteIpv6Address());
			bgpRoutingProtocol.setRemoteIPV6Address(ipv6Address);
			if (bgp.getLocalAsNumber() != null) {
				ASNumber bgpNeibourLocalAsNumber = new ASNumber();
				bgpNeibourLocalAsNumber.setNumber(bgp.getLocalAsNumber());
				bgpRoutingProtocol.setBgpNeighbourLocalASNumber(bgpNeibourLocalAsNumber);
			}
			if (bgp.getAsoOverride() != null)
				bgpRoutingProtocol.setIsASOverriden(ActivationUtils.getStrValue(bgp.getAsoOverride()));
			if (bgp.getIsebgpMultihopReqd() != null)
				bgpRoutingProtocol.setIsEBGPMultihopRequired(ActivationUtils.getStrValue(bgp.getIsebgpMultihopReqd()));
			if (bgp.getMaxPrefix() != null)
				bgpRoutingProtocol.setMaxPrefix(ActivationUtils.toInteger(bgp.getMaxPrefix()));
			if (bgp.getMaxPrefixThreshold() != null)
				bgpRoutingProtocol.setMaxPrefixThreshold(ActivationUtils.toInteger(bgp.getMaxPrefixThreshold()));
			if (bgp.getRedistributeStaticEnabled() != null)
				bgpRoutingProtocol.setIsRedistributeStaticEnabled(
						ActivationUtils.getBooleanValue(bgp.getRedistributeStaticEnabled()));
			if (bgp.getRedistributeConnectedEnabled() != null)
				bgpRoutingProtocol.setIsRedistributeConnectedEnabled(
						ActivationUtils.getBooleanValue(bgp.getRedistributeConnectedEnabled()));
			if (bgp.getHelloTime() != null)
				bgpRoutingProtocol.setHelloTime(ActivationUtils.toInteger(bgp.getHelloTime()));
			if (bgp.getHoldTime() != null)
				bgpRoutingProtocol.setHoldTime(ActivationUtils.toInteger(bgp.getHoldTime()));
			bgpRoutingProtocol.setIsOriginateDefaultEnabled(false);// TODO
			bgpRoutingProtocol
					.setIsAuthenticationRequired(ActivationUtils.getBooleanValue(bgp.getIsauthenticationRequired()));
			if (StringUtils.isNotBlank(bgp.getAuthenticationMode()))
				bgpRoutingProtocol.setAuthenticationMode(bgp.getAuthenticationMode().toUpperCase());
			if (StringUtils.isNotBlank(bgp.getPassword()))
				bgpRoutingProtocol.setAuthenticationPassword(bgp.getPassword());
			Set<PolicyType> policyTypes = bgp.getPolicyTypes();
			for (PolicyType policyType : policyTypes) {
				if (policyType.getInboundRoutingIpv4Policy() == 1) {
					PrefixList inboundV4PrefixList = new PrefixList();
					inboundV4PrefixList.setIsPreprovisioned(
							ActivationUtils.getBooleanValue(policyType.getInboundIpv4Preprovisioned()));
					inboundV4PrefixList.setName(policyType.getInboundIpv4PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry inBoundV4PrefixListEntry = new PrefixListEntry();
								inBoundV4PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix()!=null ?policyListConfig.getNetworkPrefix().trim():policyListConfig.getNetworkPrefix());
								if(policyListConfig.getAction()!=null) {
								inBoundV4PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								}else {
									inBoundV4PrefixListEntry.setOperator("exact");
								}
								inBoundV4PrefixListEntry.setSubnetRangeMaximum(
										ActivationUtils.toInteger(policyListConfig.getLeValue()));
								inBoundV4PrefixListEntry.setSubnetRangeMinimum(
										ActivationUtils.toInteger(policyListConfig.getGeValue()));
								inboundV4PrefixList.getPrefixListEntry().add(inBoundV4PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setInboundBGPv4PrefixesAllowed(inboundV4PrefixList);
				}

				if (policyType.getInboundRoutingIpv6Policy() == 1) {
					PrefixList inboundV6PrefixList = new PrefixList();
					inboundV6PrefixList.setIsPreprovisioned(
							ActivationUtils.getBooleanValue(policyType.getInboundIpv6Preprovisioned()));
					inboundV6PrefixList.setName(policyType.getInboundIpv6PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry inBoundV6PrefixListEntry = new PrefixListEntry();
								inBoundV6PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix()!=null?policyListConfig.getNetworkPrefix().trim():policyListConfig.getNetworkPrefix());
								if(policyListConfig.getAction()!=null) {
								inBoundV6PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								}else {
									inBoundV6PrefixListEntry.setOperator("exact");
								}
								inBoundV6PrefixListEntry.setSubnetRangeMaximum(
										ActivationUtils.toInteger(policyListConfig.getLeValue()));
								inBoundV6PrefixListEntry.setSubnetRangeMinimum(
										ActivationUtils.toInteger(policyListConfig.getGeValue()));
								inboundV6PrefixList.getPrefixListEntry().add(inBoundV6PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setInboundBGPv6PrefixesAllowed(inboundV6PrefixList);
				}

				if (policyType.getOutboundRoutingIpv6Policy() == 1) {
					PrefixList outboundV6PrefixList = new PrefixList();
					outboundV6PrefixList.setIsPreprovisioned(
							ActivationUtils.getBooleanValue(policyType.getOutboundIpv6Preprovisioned()));
					outboundV6PrefixList.setName(policyType.getOutboundIpv6PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry outBoundV6PrefixListEntry = new PrefixListEntry();
								outBoundV6PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix()!=null?policyListConfig.getNetworkPrefix().trim():policyListConfig.getNetworkPrefix());
								if(policyListConfig.getAction()!=null) {
								outBoundV6PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								}
								outBoundV6PrefixListEntry.setSubnetRangeMaximum(
										ActivationUtils.toInteger(policyListConfig.getLeValue()));
								outBoundV6PrefixListEntry.setSubnetRangeMinimum(
										ActivationUtils.toInteger(policyListConfig.getGeValue()));
								outboundV6PrefixList.getPrefixListEntry().add(outBoundV6PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setOutboundBGPv6PrefixesAllowed(outboundV6PrefixList);
				}

				if (policyType.getOutboundRoutingIpv4Policy() == 1) {
					PrefixList outboundV4PrefixList = new PrefixList();
					if (policyType.getOutboundIpv4Preprovisioned() != null)
						outboundV4PrefixList.setIsPreprovisioned(
								ActivationUtils.getBooleanValue(policyType.getOutboundIpv4Preprovisioned()));
					if (StringUtils.isNotBlank(policyType.getOutboundIpv6PolicyName()))
						outboundV4PrefixList.setName(policyType.getOutboundIpv6PolicyName());
					Set<PolicyTypeCriteriaMapping> prefixTypeMappingCriterias = policyType
							.getPolicyTypeCriteriaMappings();// TODO
					for (PolicyTypeCriteriaMapping prefixTypeMappingCriteria : prefixTypeMappingCriterias) {
						Integer policyCriteriaId = prefixTypeMappingCriteria.getPolicyCriteriaId();
						PolicyCriteria policyCreiteria = policyCriteriaRepository
								.findByPolicyCriteriaIdAndEndDateIsNull(policyCriteriaId);
						if (policyCreiteria != null) {
							Set<PrefixlistConfig> prefixListConfigs = policyCreiteria.getPrefixlistConfigs();
							for (PrefixlistConfig policyListConfig : prefixListConfigs) {
								PrefixListEntry outBoundV4PrefixListEntry = new PrefixListEntry();
								if (StringUtils.isNotBlank(policyListConfig.getNetworkPrefix()))
									outBoundV4PrefixListEntry.setNetworkPrefix(policyListConfig.getNetworkPrefix().trim());
								if (StringUtils.isNotBlank(policyListConfig.getAction()))
									outBoundV4PrefixListEntry.setOperator(policyListConfig.getAction().toLowerCase());
								if (StringUtils.isNotBlank(policyListConfig.getLeValue()))
									outBoundV4PrefixListEntry.setSubnetRangeMaximum(
											ActivationUtils.toInteger(policyListConfig.getLeValue()));
								if (StringUtils.isNotBlank(policyListConfig.getGeValue()))
									outBoundV4PrefixListEntry.setSubnetRangeMinimum(
											ActivationUtils.toInteger(policyListConfig.getGeValue()));
								outboundV4PrefixList.getPrefixListEntry().add(outBoundV4PrefixListEntry);
							}
						}
					}
					bgpRoutingProtocol.setOutboundBGPv4PrefixesAllowed(outboundV4PrefixList);
				}

			}
			lanRoutingProtocol.setBgpRoutingProtocol(bgpRoutingProtocol);
		}
		return lanRoutingProtocol;
	}

	protected void createAttributes() {

	}

	protected void createPeWanAdditionalStaticBgp() {

	}

	protected void createPeWanStaticRouter() {

	}

	protected void createRoutingProtocolBgp() {

	}

	public String generateDummyIPConfigXml(ServiceDetail serviceDetail, String actionType, String requestId) {
		return null;
	}

	public String generateDummyRFConfigXml(ServiceDetail serviceDetail, String actionType, String requestId) {
		return null;
	}
	
	public Map<String,String> getTclWanDetailsFromReverseISC(String serviceId) {
		LOGGER.info("getTclWanDetailsFromReverseISC invoked for Service Code:{}", serviceId);
		Map<String,String> atMap=new HashMap<>();
		VpnIllPortAttributes vpnIllPortAttributes = satSocService.getReverseIsc(serviceId, "inventory");
		if (Objects.nonNull(vpnIllPortAttributes) && !CollectionUtils.isEmpty(vpnIllPortAttributes.getAttribute())) {
			LOGGER.info("ReverseISCAttributes exists for Service Code:{}", serviceId);
			String ipAddress = "";
			List<Attribute> reverseISCAttributes = vpnIllPortAttributes.getAttribute().stream()
					.filter(reverseISCAttribute -> (("CustomerEndLoopback".equalsIgnoreCase(reverseISCAttribute.getKey())) ||
							("CustomerWAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())))).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(reverseISCAttributes)) {
				LOGGER.info("ReverseISCAttributes exists for Service Code:{}", serviceId);
				for (Attribute reverseISCAttribute : reverseISCAttributes) {
					ipAddress = reverseISCAttribute.getValue();
					LOGGER.info("IP Address from SatSoc Reverse for Service Code {} : {}", serviceId,
							ipAddress);
					if (ipAddress != null) {
						String wanIpAddressArr[] = ipAddress.split(",");
						if (wanIpAddressArr.length > 0) {
							if("CustomerWAN_IP".equalsIgnoreCase(reverseISCAttribute.getKey())){
								atMap.put("wanIpAddress",wanIpAddressArr[0].trim());
							}else if("CustomerEndLoopback".equalsIgnoreCase(reverseISCAttribute.getKey())){
								atMap.put("customerEndLoopback",wanIpAddressArr[0].trim());
							}
						}
					}
				}
			}
		}
		return atMap;
	}
	
	public String getIpAddress(String ipAddress) {
		if(ipAddress!=null && !ipAddress.isEmpty() && ipAddress.contains("::")){
			ipAddress= ipAddress.replace("::",":0:0:0:0:");
		}
		return ipAddress;
	}

}
