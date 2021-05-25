package com.tcl.dias.serviceactivation;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.serviceactivation.activation.services.ProductActivationConfigurationService;
import com.tcl.dias.serviceactivation.activemq.creator.MessageCreator;

/**
 * 
 * test suite for serviceactivation application
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class OptimusServiceActivationTestSuite {

	@Autowired
	ProductActivationConfigurationService productActivationConfigurationService;
	
	
	@Autowired
	MessageCreator messageCreator;
	
	@Value("${activemq.netp.create.queue}")
	String netpCreateQueue;

	@Test
	@Transactional
	public void testname() throws Exception {
		
		String request="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
				"<soapenv:Body>\r\n" + 
				"<p:performIPServiceActivation xmlns:p=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\">\r\n" + 
				"    <orderDetails xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">\r\n" + 
				"        <service>\r\n" + 
				"            <iaService>\r\n" + 
				"                <serviceType>ILL</serviceType>\r\n" + 
				"                <serviceSubType>STDILL</serviceSubType>\r\n" + 
				"                <serviceBandwidth>\r\n" + 
				"                    <speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">2.0</speed>\r\n" + 
				"                    <unit xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">MBPS</unit>\r\n" + 
				"                </serviceBandwidth>\r\n" + 
				"                <burstableBandwidth>\r\n" + 
				"                    <speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">2.0</speed>\r\n" + 
				"                    <unit xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">MBPS</unit>\r\n" + 
				"                </burstableBandwidth>\r\n" + 
				"                <serviceLink/>\r\n" + 
				"                <isConfigManaged>true</isConfigManaged>\r\n" + 
				"                <wanV4Addresses/>\r\n" + 
				"                <wanV4Addresses/>\r\n" + 
				"                <Router>\r\n" + 
				"                    <hostName>AJI_hpe01</hostName>\r\n" + 
				"                    <type>MPLS</type>\r\n" + 
				"                    <role>PE</role>\r\n" + 
				"                    <v4ManagementIPAddress/>\r\n" + 
				"                    <v6ManagementIPAddress/>\r\n" + 
				"                    <wanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">\r\n" + 
				"                        <interface>\r\n" + 
				"                            <serialInterface/>\r\n" + 
				"                            <ethernetInterface>\r\n" + 
				"                                <name>Lag 1:1104.151</name>\r\n" + 
				"                                <physicalPortName>Lag 1</physicalPortName>\r\n" + 
				"                                <v4IpAddress>\r\n" + 
				"                                    <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">10.52.2.73</address>\r\n" + 
				"                                </v4IpAddress>\r\n" + 
				"                                <v6IpAddress/>\r\n" + 
				"                                <inboundAccessControlList>\r\n" + 
				"                                    <name>infra-acl-do-not-add-or-edit</name>\r\n" + 
				"                                </inboundAccessControlList>\r\n" + 
				"                                <outboundAccessControlList/>\r\n" + 
				"                                <inboundAccessControlListV6>\r\n" + 
				"                                    <name>infra-acl-do-not-add-or-edit</name>\r\n" + 
				"                                </inboundAccessControlListV6>\r\n" + 
				"                                <outboundAccessControlListV6/>\r\n" + 
				"                                <duplex>NOT_APPLICABLE</duplex>\r\n" + 
				"                                <speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">NOT APPLICABLE</speed>\r\n" + 
				"                                <isAutoNegotiation>NOT APPLICABLE</isAutoNegotiation>\r\n" + 
				"                                <vlan>151</vlan>\r\n" + 
				"                                <mode>ACCESS</mode>\r\n" + 
				"                                <encapsulation>QnQ</encapsulation>\r\n" + 
				"                                <svlan>1104</svlan>\r\n" + 
				"                                <bfdConfig/>\r\n" + 
				"                            </ethernetInterface>\r\n" + 
				"                            <channelizedSDHInterface/>\r\n" + 
				"                        </interface>\r\n" + 
				"                        <staticRoutes/>\r\n" + 
				"                    </wanInterface>\r\n" + 
				"                    <topologyInfo>\r\n" + 
				"                        <name>AJI_hsw01_Core01</name>\r\n" + 
				"                        <uNISwitch>\r\n" + 
				"                            <v4ManagementIPAddress>\r\n" + 
				"                                <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">AJI_LOCATION.420024F.AJI_hsw01</address>\r\n" + 
				"                            </v4ManagementIPAddress>\r\n" + 
				"                            <interface>\r\n" + 
				"                                <ethernetInterface>\r\n" + 
				"                                    <physicalPortName>Management-1</physicalPortName>\r\n" + 
				"                                    <duplex>NOT_APPLICABLE</duplex>\r\n" + 
				"                                    <speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">NOT APPLICABLE</speed>\r\n" + 
				"                                    <isAutoNegotiation>NOT APPLICABLE</isAutoNegotiation>\r\n" + 
				"                                    <vlan>151</vlan>\r\n" + 
				"                                    <mode>TRUNK</mode>\r\n" + 
				"                                    <mediaType>OPTICAL</mediaType>\r\n" + 
				"                                    <portType>GigabitEthernet</portType>\r\n" + 
				"                                    <handOff>NNI</handOff>\r\n" + 
				"                                </ethernetInterface>\r\n" + 
				"                            </interface>\r\n" + 
				"                        </uNISwitch>\r\n" + 
				"                    </topologyInfo>\r\n" + 
				"                    <routerTopologyInterface1>\r\n" + 
				"                        <name>1/1/1</name>\r\n" + 
				"                        <physicalPortName>1/1/1</physicalPortName>\r\n" + 
				"                    </routerTopologyInterface1>\r\n" + 
				"                    <routerTopologyInterface2>\r\n" + 
				"                        <name>Lag1</name>\r\n" + 
				"                        <physicalPortName>Lag1</physicalPortName>\r\n" + 
				"                    </routerTopologyInterface2>\r\n" + 
				"                </Router>\r\n" + 
				"                <cpe>\r\n" + 
				"                    <loopbackInterface>\r\n" + 
				"                        <name></name>\r\n" + 
				"                        <v4IpAddress/>\r\n" + 
				"                        <v6IpAddress>\r\n" + 
				"                            <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\"></address>\r\n" + 
				"                        </v6IpAddress>\r\n" + 
				"                    </loopbackInterface>\r\n" + 
				"                    <isCEACEConfigurable xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">false</isCEACEConfigurable>\r\n" + 
				"                    <lanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">\r\n" + 
				"                        <interface>\r\n" + 
				"                            <name>Lag 1:1104.151</name>\r\n" + 
				"                            <v4IpAddress>\r\n" + 
				"                                <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">10.52.2.74</address>\r\n" + 
				"                            </v4IpAddress>\r\n" + 
				"                            <v6IpAddress/>\r\n" + 
				"                            <vlan>151</vlan>\r\n" + 
				"                            <mode>Q-in-Q</mode>\r\n" + 
				"                            <mediaType></mediaType>\r\n" + 
				"                            <portType></portType>\r\n" + 
				"                        </interface>\r\n" + 
				"                        <routingProtocol/>\r\n" + 
				"                        <HSRPProtocol/>\r\n" + 
				"                    </lanInterface>\r\n" + 
				"                </cpe>\r\n" + 
				"                <qos/>\r\n" + 
				"                <solutionTable/>\r\n" + 
				"                <extendedLAN>\r\n" + 
				"                    <isEnabled xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">false</isEnabled>\r\n" + 
				"                    <numberOfMacAddresses>0</numberOfMacAddresses>\r\n" + 
				"                </extendedLAN>\r\n" + 
				"                <SAMCustomerDescription>INTERNET-VPN</SAMCustomerDescription>\r\n" + 
				"                <vrf>\r\n" + 
				"                    <name>Internet VPN</name>\r\n" + 
				"                    <maximumRoutes/>\r\n" + 
				"                </vrf>\r\n" + 
				"                <InternetGatewayv4IPAddress>\r\n" + 
				"                    <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">4.2.2.2</address>\r\n" + 
				"                </InternetGatewayv4IPAddress>\r\n" + 
				"                <InternetGatewayv6IPAddress/>\r\n" + 
				"                <NMSServerv4IPAddress/>\r\n" + 
				"            </iaService>\r\n" + 
				"        </service>\r\n" + 
				"        <serviceId>091CHEN0300Y6HNIMMG</serviceId>\r\n" + 
				"        <customerDetails>\r\n" + 
				"            <ALUCustomerID>0</ALUCustomerID>\r\n" + 
				"            <address/>\r\n" + 
				"        </customerDetails>\r\n" + 
				"        <orderCategory>CUSTOMER_ORDER</orderCategory>\r\n" + 
				"        <orderType>NEW</orderType>\r\n" + 
				"        <isDowntimeRequired>false</isDowntimeRequired>\r\n" + 
				"    </orderDetails>\r\n" + 
				"    <header xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">\r\n" + 
				"        <requestID xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">RID_091CHEN0300Y6HNIMMG_1562840909675_IASE2E</requestID>\r\n" + 
				"        <originatingSystem xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">OPTIMUS_UAT</originatingSystem>\r\n" + 
				"        <originationTime xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">2019-07-11T15:58:29.670+05:30</originationTime>\r\n" + 
				"        <authUser xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">tw_admin</authUser>\r\n" + 
				"    </header>\r\n" + 
				"</p:performIPServiceActivation>\r\n" + 
				" </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		String sRequest="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <p:performIPServiceActivation xmlns:p=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\">\r\n" + 
				"         <orderDetails xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">\r\n" + 
				"            <service>\r\n" + 
				"               <iaService>\r\n" + 
				"                  <serviceType>ILL</serviceType>\r\n" + 
				"                  <serviceSubType>STDILL</serviceSubType>\r\n" + 
				"                  <serviceBandwidth>\r\n" + 
				"                     <speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">20.0</speed>\r\n" + 
				"                     <unit xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">MBPS</unit>\r\n" + 
				"                  </serviceBandwidth>\r\n" + 
				"                  <redundancyRole>SINGLE</redundancyRole>\r\n" + 
				"                  <isConfigManaged>false</isConfigManaged>\r\n" + 
				"                  <wanV4Addresses>\r\n" + 
				"                     <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">113.13.223.36/30</address>\r\n" + 
				"                  </wanV4Addresses>\r\n" + 
				"                  <lanV4Addresses>\r\n" + 
				"                     <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">7.4.23.48/29</address>\r\n" + 
				"                  </lanV4Addresses>\r\n" + 
				"                  <wanV6Addresses>\r\n" + 
				"                     <address>2006:dffd::fffe:8080:0/126</address>\r\n" + 
				"                  </wanV6Addresses>\r\n" + 
				"                  <lanV6Addresses>\r\n" + 
				"                     <address>2006:dddd::fffe:9090:0/126</address>\r\n" + 
				"                  </lanV6Addresses>\r\n" + 
				"                  <Router>\r\n" + 
				"                     <hostName>TataNetAS-PE-105</hostName>\r\n" + 
				"                     <type>MPLS</type>\r\n" + 
				"                     <role>PE</role>\r\n" + 
				"                     <v4ManagementIPAddress>\r\n" + 
				"                        <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">100.64.0.105</address>\r\n" + 
				"                     </v4ManagementIPAddress>\r\n" + 
				"                     <wanInterface>\r\n" + 
				"                        <interface>\r\n" + 
				"                           <ethernetInterface>\r\n" + 
				"                              <name>Lag 105:1907.173</name>\r\n" + 
				"                              <physicalPortName>Lag 105</physicalPortName>\r\n" + 
				"                              <v4IpAddress>\r\n" + 
				"                                 <address>113.13.223.37/30</address>\r\n" + 
				"                              </v4IpAddress>\r\n" + 
				"                              <v6IpAddress>\r\n" + 
				"                                 <address>2006:dffd::fffe:8080:1/126</address>\r\n" + 
				"                              </v6IpAddress>\r\n" + 
				"                              <duplex>NOT_APPLICABLE</duplex>\r\n" + 
				"                              <speed>NOT_APPLICABLE</speed>\r\n" + 
				"                              <isAutoNegotiation>NOT_APPLICABLE</isAutoNegotiation>\r\n" + 
				"                              <vlan>173</vlan>\r\n" + 
				"                              <mode>ACCESS</mode>\r\n" + 
				"                              <encapsulation>QnQ</encapsulation>\r\n" + 
				"                              <svlan>1907</svlan>\r\n" + 
				"                           </ethernetInterface>\r\n" + 
				"                        </interface>\r\n" + 
				"                     </wanInterface>\r\n" + 
				"                     <isRouterBFDEnabled>false</isRouterBFDEnabled>\r\n" + 
				"                  </Router>\r\n" + 
				"                  <cpe>\r\n" + 
				"                     <isCEACEConfigurable xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">false</isCEACEConfigurable>\r\n" + 
				"                     <wanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">\r\n" + 
				"                        <interface>\r\n" + 
				"                           <ethernetInterface>\r\n" + 
				"                              <name>DefaultForCPETabREADONLY</name>\r\n" + 
				"                              <v4IpAddress>\r\n" + 
				"                                 <address>113.13.223.38/30</address>\r\n" + 
				"                              </v4IpAddress>\r\n" + 
				"                              <v6IpAddress>\r\n" + 
				"                                 <address>2006:dffd::fffe:8080:2/126</address>\r\n" + 
				"                              </v6IpAddress>\r\n" + 
				"                              <duplex>NOT_APPLICABLE</duplex>\r\n" + 
				"                              <speed>NOT_APPLICABLE</speed>\r\n" + 
				"                              <isAutoNegotiation>NOT_APPLICABLE</isAutoNegotiation>\r\n" + 
				"                              <mode>NOT_APPLICABLE</mode>\r\n" + 
				"                           </ethernetInterface>\r\n" + 
				"                        </interface>\r\n" + 
				"                     </wanInterface>\r\n" + 
				"                     <lanInterface xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">\r\n" + 
				"                        <interface>\r\n" + 
				"                           <v4IpAddress>\r\n" + 
				"                              <address>7.4.23.49/29</address>\r\n" + 
				"                           </v4IpAddress>\r\n" + 
				"                           <v6IpAddress>\r\n" + 
				"                              <address>2006:fddd::ffee:9090:1/126</address>\r\n" + 
				"                           </v6IpAddress>\r\n" + 
				"                           <duplex>NOT_APPLICABLE</duplex>\r\n" + 
				"                           <speed>NOT_APPLICABLE</speed>\r\n" + 
				"                           <isAutoNegotiation>NOT_APPLICABLE</isAutoNegotiation>\r\n" + 
				"                           <mode>NOT_APPLICABLE</mode>\r\n" + 
				"                        </interface>\r\n" + 
				"                     </lanInterface>\r\n" + 
				"                     <snmpServerCommunity xmlns:out2=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:out5=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out7=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:out6=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out8=\"http://www.tcl.com/2014/3/ipsvc/xsd\">t2c2l2com</snmpServerCommunity>\r\n" + 
				"                  </cpe>\r\n" + 
				"                  <qos>\r\n" + 
				"                     <cosType>6COS</cosType>\r\n" + 
				"                     <cos>\r\n" + 
				"                        <name>COS6</name>\r\n" + 
				"                        <classificationCriteria>\r\n" + 
				"                           <ipprecedent>\r\n" + 
				"                              <criteriaValue1 xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">1</criteriaValue1>\r\n" + 
				"                              <criteriaValue2 xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">2</criteriaValue2>\r\n" + 
				"                              <criteriaValue3 xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">3</criteriaValue3>\r\n" + 
				"                              <criteriaValue4 xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">4</criteriaValue4>\r\n" + 
				"                              <criteriaValue5 xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">5</criteriaValue5>\r\n" + 
				"                              <criteriaValue6 xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">6</criteriaValue6>\r\n" + 
				"                           </ipprecedent>\r\n" + 
				"                        </classificationCriteria>\r\n" + 
				"                        <bandwidth>\r\n" + 
				"                           <speed xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">3.072E7</speed>\r\n" + 
				"                           <unit xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">BPS</unit>\r\n" + 
				"                        </bandwidth>\r\n" + 
				"                        <cosUpdateAction>COMPLETE</cosUpdateAction>\r\n" + 
				"                        <bandwidthinKBPS>30720</bandwidthinKBPS>\r\n" + 
				"                        <PIRBandwidth>\r\n" + 
				"                           <speed>30720.0</speed>\r\n" + 
				"                           <unit>KBPS</unit>\r\n" + 
				"                        </PIRBandwidth>\r\n" + 
				"                        <isDefaultFC>true</isDefaultFC>\r\n" + 
				"                     </cos>\r\n" + 
				"                     <QoSProfile>PREMIUM</QoSProfile>\r\n" + 
				"                     <QoSTrafficMode>UNICAST</QoSTrafficMode>\r\n" + 
				"                     <ALUSchedulerPolicy>\r\n" + 
				"                        <name>ACE_30Mb</name>\r\n" + 
				"                        <totalPIRbandwidth>30720</totalPIRbandwidth>\r\n" + 
				"                        <totalCIRbandwidth>30720</totalCIRbandwidth>\r\n" + 
				"                        <isPreprovisioned>false</isPreprovisioned>\r\n" + 
				"                     </ALUSchedulerPolicy>\r\n" + 
				"                     <ALUIngressPolicy>\r\n" + 
				"                        <name>ACE_IN:COS6_30Mb</name>\r\n" + 
				"                        <isPreprovisioned>false</isPreprovisioned>\r\n" + 
				"                     </ALUIngressPolicy>\r\n" + 
				"                     <ALUEgressPolicy>\r\n" + 
				"                        <name>ACE_OUT:COS6_30Mb</name>\r\n" + 
				"                        <isPreprovisioned>false</isPreprovisioned>\r\n" + 
				"                     </ALUEgressPolicy>\r\n" + 
				"                  </qos>\r\n" + 
				"                  <solutionTable>\r\n" + 
				"                     <VPN xmlns:out2=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\">\r\n" + 
				"                        <vpnId xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">Internet_VPN</vpnId>\r\n" + 
				"                        <topology xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">HUBnSPOKE</topology>\r\n" + 
				"                        <leg xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">\r\n" + 
				"                           <SiteID>091CLSS050664183000</SiteID>\r\n" + 
				"						   <legServiceID>45</legServiceID>\r\n" + 
				"                           <role>SPOKE</role>\r\n" + 
				"                           <Interfacename>Lag 105:1807.173</Interfacename>\r\n" + 
				"                        </leg>\r\n" + 
				"                        <vpnType xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">CUSTOMER</vpnType>\r\n" + 
				"                     </VPN>\r\n" + 
				"                     <SolutionID xmlns:out2=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\">091CLSS050664183000</SolutionID>\r\n" + 
				"                  </solutionTable>\r\n" + 
				"				  <wanRoutingProtocol>\r\n" + 
				"                     <staticRoutingProtocol>\r\n" + 
				"                        <PEWANStaticRoutes xmlns:out3=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">\r\n" + 
				"                           <StaticRouteList>\r\n" + 
				"                              <ipSubnet>\r\n" + 
				"                                 <v4Address>\r\n" + 
				"                                    <address>10.90.80.96/29</address>\r\n" + 
				"                                 </v4Address>\r\n" + 
				"                              </ipSubnet>\r\n" + 
				"                              <nextHopIp>113.13.223.36</nextHopIp>\r\n" + 
				"                           </StaticRouteList>\r\n" + 
				"                        </PEWANStaticRoutes>\r\n" + 
				"                     </staticRoutingProtocol>\r\n" + 
				"                  </wanRoutingProtocol>\r\n" + 
				"                  <extendedLAN>\r\n" + 
				"                     <isEnabled xmlns:out=\"http://www.tcl.com/2011/11/ipsvc/xsd\">false</isEnabled>\r\n" + 
				"                  </extendedLAN>\r\n" + 
				"                  <isAPSEnabled>false</isAPSEnabled>\r\n" + 
				"                  <lastMile>\r\n" + 
				"                     <type>MAN</type>\r\n" + 
				"                  </lastMile>\r\n" + 
				"                  <InstanceID>SOAP_NETP_IAS_2016</InstanceID>\r\n" + 
				"                  <SAMCustomerDescription>INTERNET-VPN</SAMCustomerDescription>\r\n" + 
				"                  <CSSSAMID>4755</CSSSAMID>\r\n" + 
				"                  <ALUServiceName>INTERNET-VPN:INTERNET-VPN:S</ALUServiceName>\r\n" + 
				"                  <InternetGatewayv4IPAddress>\r\n" + 
				"                     <address xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">66.110.0.204</address>\r\n" + 
				"                  </InternetGatewayv4IPAddress>\r\n" + 
				"               </iaService>\r\n" + 
				"            </service>\r\n" + 
				"            <serviceId>091CLSS050664183000</serviceId>\r\n" + 
				"            <copfId>93364</copfId>\r\n" + 
				"            <customerDetails>\r\n" + 
				"               <ALUCustomerID>4755</ALUCustomerID>\r\n" + 
				"               <id>VT456655</id>\r\n" + 
				"               <name>Mozilla INC</name>\r\n" + 
				"               <type>Others</type>\r\n" + 
				"               <category>Enterprise</category>\r\n" + 
				"               <address>\r\n" + 
				"                  <location xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">Pune</location>\r\n" + 
				"                  <addressLine1 xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">Pune</addressLine1>\r\n" + 
				"                  <city xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">Pune</city>\r\n" + 
				"                  <state xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">Maharashtra</state>\r\n" + 
				"                  <pincode xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">145623</pincode>\r\n" + 
				"                  <country xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">India</country>\r\n" + 
				"               </address>\r\n" + 
				"               <emailID>vishay@gmail.com</emailID>\r\n" + 
				"            </customerDetails>\r\n" + 
				"            <orderCategory>CUSTOMER_ORDER</orderCategory>\r\n" + 
				"            <orderType>NEW</orderType>\r\n" + 
				"            <isDowntimeRequired>false</isDowntimeRequired>\r\n" + 
				"         </orderDetails>\r\n" + 
				"         <header xmlns:io5=\"http://www.w3.org/2005/08/addressing\" xmlns:out7=\"http://IPServicesLibrary/ipsvc/bo/_2013/_06\" xmlns:io4=\"http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0\" xmlns:out6=\"http://www.tcl.com/2011/11/ipsvc/xsd\" xmlns:out9=\"http://www.tcl.com/2014/4/ipsvc/xsd\" xmlns:io3=\"http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0\" xmlns:out8=\"http://www.tcl.com/2011/11/ace/common/xsd\" xmlns:io2=\"http://www.ibm.com/websphere/sibx/smo/v6.0.1\" xmlns:out3=\"http://www.tcl.com/2014/5/ipsvc/xsd\" xmlns:out11=\"http://www.tcl.com/2014/3/ipsvc/xsd\" xmlns:out12=\"http://IPServicesLibrary/ipsvc/bo/_2013/_01\" xmlns:out2=\"http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:out5=\"http://www.tcl.com/2014/2/ipsvc/xsd\" xmlns:out4=\"http://IPServicesLibrary/ipsvc/bo/_2011/_11\" xmlns:out10=\"wsdl.http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest\" xmlns:io=\"http://schemas.xmlsoap.org/ws/2006/08/addressing\" xmlns:io6=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xs4xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:out=\"http://IPServicesLibrary/ipsvc/bo/_2013/_02\">\r\n" + 
				"            <actionRequired xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">PE_PROV_CONFIG</actionRequired>\r\n" + 
				"            <requestID xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">RID_091CLSS050664183000_1410_1455273786106_IASE2E</requestID>\r\n" + 
				"            <originatingSystem xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">OPTIMUS</originatingSystem>\r\n" + 
				"            <originationTime xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">2016-02-12T10:43:06.774Z</originationTime>\r\n" + 
				"            <authUser xmlns:out=\"http://www.tcl.com/2011/11/ace/common/xsd\">tw_admin</authUser>\r\n" + 
				"         </header>\r\n" + 
				"      </p:performIPServiceActivation>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";

		//messageCreator.convertAndSend(request, netpCreateQueue);
		// productActivationConfigurationService.processRfConfigurationXml("091MUMB030032817839");
		productActivationConfigurationService.processIpConfigurationXml("091CHEN030041C5JT55","PE_PROV_CONFIG","TEST");
		//productActivationConfigurationService.processTxConfigurationXml("091CHEN0300C2I6BAXD","CONFIG");
System.out.println("");
	}

}
