package com.tcl.dias.serviceactivation;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.serviceactivation.datamigration.DataMigrationILL;
/**
 * This class has test controllers for data migration methods in Data Migration
 * class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DataMigrationIllTest {
	@Autowired
	DataMigrationILL dataMigration;

	public static final String ill_data = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"   <soapenv:Body>\r\n" + 
			"      <runJobReturn xsi:type=\"ns1:runJobReturn\" xmlns=\"http://talend.org\" xmlns:ns1=\"http://talend.org\">\r\n" + 
			"         <ns1:item xsi:type=\"ns1:ArrayOf_xsd_string\">\r\n" + 
			"            <ns1:item xsi:type=\"xsd:string\"><![CDATA[<Order xmlns:tmdm=\"http://www.talend.com/mdm\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <IAS>\r\n" + 
			"    <serviceId>2221169538</serviceId>\r\n" + 
			"    <ServiceBandwidth>\r\n" + 
			"      <rfsCharacteristic />\r\n" + 
			"      <bandwidthValue>2.0</bandwidthValue>\r\n" + 
			"      <unit>MBPS</unit>\r\n" + 
			"    </ServiceBandwidth>\r\n" + 
			"    <wanV4IPAddress>\r\n" + 
			"      <address>203.199.67.168/30</address>\r\n" + 
			"      <CustomerProvided>false</CustomerProvided>\r\n" + 
			"    </wanV4IPAddress>\r\n" + 
			"    <lanV4IPAddress>[LV4IPA_M125335_1]</lanV4IPAddress>\r\n" + 
			"    <lanV4IPAddress>[LV4IPA_M125335_2]</lanV4IPAddress>\r\n" + 
			"    <redundancyRole>SINGLE</redundancyRole>\r\n" + 
			"    <cpe>\r\n" + 
			"      <wanInterface>\r\n" + 
			"        <interface xsi:type=\"EthernetInterface\">\r\n" + 
			"          <name>DefaultForCPETabREADONLY</name>\r\n" + 
			"          <v4IpAddress>\r\n" + 
			"            <address>203.199.67.169/30</address>\r\n" + 
			"          </v4IpAddress>\r\n" + 
			"          <SecondaryV4IpAddress>\r\n" + 
			"            <address>121.244.65.234/29</address>\r\n" + 
			"            <CustomerProvided>false</CustomerProvided>\r\n" + 
			"          </SecondaryV4IpAddress>\r\n" + 
			"        </interface>\r\n" + 
			"      </wanInterface>\r\n" + 
			"    </cpe>\r\n" + 
			"    <ManagementType>UNMANAGED</ManagementType>\r\n" + 
			"    <isLoadBalancingRequired>false</isLoadBalancingRequired>\r\n" + 
			"    <isTriggerNCCM>false</isTriggerNCCM>\r\n" + 
			"    <ServiceState>ACTIVE</ServiceState>\r\n" + 
			"    <ServiceLink>\r\n" + 
			"      <serviceId>Single</serviceId>\r\n" + 
			"      <role>PRIMARY</role>\r\n" + 
			"    </ServiceLink>\r\n" + 
			"    <ServiceType>ILL</ServiceType>\r\n" + 
			"    <ComponentType>Port</ComponentType>\r\n" + 
			"    <ServiceSubType>PILL</ServiceSubType>\r\n" + 
			"    <ServiceSpecVersion>\r\n" + 
			"      <versionNo>1</versionNo>\r\n" + 
			"    </ServiceSpecVersion>\r\n" + 
			"    <ServiceInstanceVersion>\r\n" + 
			"      <versionNo>1</versionNo>\r\n" + 
			"    </ServiceInstanceVersion>\r\n" + 
			"    <ServiceInstanceID>M125544</ServiceInstanceID>\r\n" + 
			"    <vrf>\r\n" + 
			"      <name>NOT_APPLICABLE</name>\r\n" + 
			"    </vrf>\r\n" + 
			"    <MultiLinkQoS>\r\n" + 
			"      <cosType>6COS</cosType>\r\n" + 
			"      <cos>\r\n" + 
			"        <name>COS4</name>\r\n" + 
			"        <cosUpdateAction>COMPLETE</cosUpdateAction>\r\n" + 
			"        <classificationCriteria />\r\n" + 
			"        <Bandwidth>\r\n" + 
			"          <rfsCharacteristic />\r\n" + 
			"          <bandwidthValue>2.0</bandwidthValue>\r\n" + 
			"          <unit>MBPS</unit>\r\n" + 
			"        </Bandwidth>\r\n" + 
			"        <BandWidthPercentage>1</BandWidthPercentage>\r\n" + 
			"      </cos>\r\n" + 
			"      <QoSProfile>PREMIUM</QoSProfile>\r\n" + 
			"      <QosTrafficMode>UNICAST</QosTrafficMode>\r\n" + 
			"      <IsDefaultFC>true</IsDefaultFC>\r\n" + 
			"      <ALUSchedulerPolicy>\r\n" + 
			"        <isPreProvisioned>false</isPreProvisioned>\r\n" + 
			"        <TotalPIRBandwidth>\r\n" + 
			"          <rfsCharacteristic />\r\n" + 
			"        </TotalPIRBandwidth>\r\n" + 
			"        <TotalCIRBandwidth>\r\n" + 
			"          <rfsCharacteristic />\r\n" + 
			"        </TotalCIRBandwidth>\r\n" + 
			"      </ALUSchedulerPolicy>\r\n" + 
			"      <SAPEgressPolicy>\r\n" + 
			"        <isPreProvisioned>false</isPreProvisioned>\r\n" + 
			"      </SAPEgressPolicy>\r\n" + 
			"      <SAPIngressPolicy>\r\n" + 
			"        <isPreProvisioned>false</isPreProvisioned>\r\n" + 
			"      </SAPIngressPolicy>\r\n" + 
			"    </MultiLinkQoS>\r\n" + 
			"    <WanRoutingProtocol xsi:type=\"BGPRoutingProtocol\">\r\n" + 
			"      <id>BGP</id>\r\n" + 
			"      <remoteASNumber>\r\n" + 
			"        <number>AS55918</number>\r\n" + 
			"        <category>PUBLIC</category>\r\n" + 
			"        <isASNumberCustomerOwned>false</isASNumberCustomerOwned>\r\n" + 
			"      </remoteASNumber>\r\n" + 
			"      <bgpNeighbourLocalASNumber>\r\n" + 
			"        <category>PUBLIC</category>\r\n" + 
			"        <isASNumberCustomerOwned>true</isASNumberCustomerOwned>\r\n" + 
			"      </bgpNeighbourLocalASNumber>\r\n" + 
			"      <isOriginateDefaultEnabled>true</isOriginateDefaultEnabled>\r\n" + 
			"      <isASOverriden>FALSE</isASOverriden>\r\n" + 
			"      <isSOORequired>false</isSOORequired>\r\n" + 
			"      <inboundBGPv4PrefixesAllowed>\r\n" + 
			"        <prefixListEntry>\r\n" + 
			"          <networkPrefix>202.94.67.0/24</networkPrefix>\r\n" + 
			"          <subnetRangeMaximum>26</subnetRangeMaximum>\r\n" + 
			"          <operator>LE</operator>\r\n" + 
			"        </prefixListEntry>\r\n" + 
			"        <prefixListEntry>\r\n" + 
			"          <networkPrefix>202.94.67.0/24</networkPrefix>\r\n" + 
			"        </prefixListEntry>\r\n" + 
			"        <name>VISTAAR</name>\r\n" + 
			"      </inboundBGPv4PrefixesAllowed>\r\n" + 
			"      <isInboundBGPV4PrefixesRequired>true</isInboundBGPV4PrefixesRequired>\r\n" + 
			"      <isRoutemapEnabledInboundRoutemapV4>true</isRoutemapEnabledInboundRoutemapV4>\r\n" + 
			"      <BGPNeighbourInboundRoutemapV4>VISTAAR</BGPNeighbourInboundRoutemapV4>\r\n" + 
			"      <routesExchanged>\r\n" + 
			"        <routes>DEFAULTROUTE</routes>\r\n" + 
			"      </routesExchanged>\r\n" + 
			"      <isRedistributeConnectedEnabled>true</isRedistributeConnectedEnabled>\r\n" + 
			"      <isRedistributeStaticEnabled>false</isRedistributeStaticEnabled>\r\n" + 
			"      <isNeighbourShutdownRequired>false</isNeighbourShutdownRequired>\r\n" + 
			"      <neighbourCommunity>4755:22</neighbourCommunity>\r\n" + 
			"      <neighbourCommunity>4755:99</neighbourCommunity>\r\n" + 
			"      <neighbourCommunity>4755:222</neighbourCommunity>\r\n" + 
			"      <neighbourCommunity>4755:914</neighbourCommunity>\r\n" + 
			"      <BGPNeighborOn>WAN</BGPNeighborOn>\r\n" + 
			"      <attribute2>additive</attribute2>\r\n" + 
			"      <ALUBackupPath>NOT_APPLICABLE</ALUBackupPath>\r\n" + 
			"    </WanRoutingProtocol>\r\n" + 
			"    <PERouter>\r\n" + 
			"      <hostname>mu-lvsb-t1-IPrt21</hostname>\r\n" + 
			"      <v4ManagementIPAddress>\r\n" + 
			"        <address>192.168.165.16</address>\r\n" + 
			"      </v4ManagementIPAddress>\r\n" + 
			"      <make>CISCO IP</make>\r\n" + 
			"      <model>Cisco Router 10008</model>\r\n" + 
			"      <type>LEGACY</type>\r\n" + 
			"      <role>PE</role>\r\n" + 
			"      <wanInterface>\r\n" + 
			"        <interface xsi:type=\"SerialInterface\">\r\n" + 
			"          <name>Serial1/0/0.1/2/3/1:0</name>\r\n" + 
			"          <physicalPortName>SONET1/0/0</physicalPortName>\r\n" + 
			"          <v4IpAddress>\r\n" + 
			"            <address>203.199.67.170/30</address>\r\n" + 
			"          </v4IpAddress>\r\n" + 
			"          <SecondaryV4IpAddress>\r\n" + 
			"            <address>121.244.65.233/29</address>\r\n" + 
			"            <CustomerProvided>false</CustomerProvided>\r\n" + 
			"          </SecondaryV4IpAddress>\r\n" + 
			"          <crcSize>16</crcSize>\r\n" + 
			"        </interface>\r\n" + 
			"      </wanInterface>\r\n" + 
			"      <topologyInfo>\r\n" + 
			"        <uniSwitch>\r\n" + 
			"          <interface>\r\n" + 
			"            <ethernetInterface xsi:type=\"EthernetInterface\" />\r\n" + 
			"          </interface>\r\n" + 
			"        </uniSwitch>\r\n" + 
			"      </topologyInfo>\r\n" + 
			"      <attribute1>dataSaved</attribute1>\r\n" + 
			"    </PERouter>\r\n" + 
			"    <isServiceLocked>false</isServiceLocked>\r\n" + 
			"    <netpReferenceId>GUI-2017092690010</netpReferenceId>\r\n" + 
			"  </IAS>\r\n" + 
			"  <OrderInfo>\r\n" + 
			"    <ServiceID>2221169538</ServiceID>\r\n" + 
			"    <COPFID>71P100309</COPFID>\r\n" + 
			"    <OrderType>CHANGE_ORDER</OrderType>\r\n" + 
			"    <originatorDetails>\r\n" + 
			"      <name>bpm.wasadmin</name>\r\n" + 
			"      <origination_date>2017-10-09</origination_date>\r\n" + 
			"      <group>CMIP</group>\r\n" + 
			"    </originatorDetails>\r\n" + 
			"    <ServiceInstanceID>[M125544]</ServiceInstanceID>\r\n" + 
			"    <ConfigurationRequestId>BPM_M125544</ConfigurationRequestId>\r\n" + 
			"    <orderStatus>CLOSED</orderStatus>\r\n" + 
			"    <customer>\r\n" + 
			"      <Customer>\r\n" + 
			"        <CRNID>VV0360</CRNID>\r\n" + 
			"        <customerName>Vistaar Systems Pvt Ltd</customerName>\r\n" + 
			"        <customerType>Others</customerType>\r\n" + 
			"        <customerCategory>Channel</customerCategory>\r\n" + 
			"        <customerEmailID>VPILLAI@VISTAAR.COM</customerEmailID>\r\n" + 
			"        <customerAddress>\r\n" + 
			"          <location>MUMBAI</location>\r\n" + 
			"          <addressLine1>RAHEJA POINT ONE WING</addressLine1>\r\n" + 
			"          <addressLine2>J.NEHRU ROAD, VAKOLA</addressLine2>\r\n" + 
			"          <city>MUMBAI</city>\r\n" + 
			"          <state>Maharashtra</state>\r\n" + 
			"          <pincode>400055</pincode>\r\n" + 
			"          <country>India</country>\r\n" + 
			"        </customerAddress>\r\n" + 
			"      </Customer>\r\n" + 
			"    </customer>\r\n" + 
			"    <LMType>TTML</LMType>\r\n" + 
			"    <PMName>PRGMNAV</PMName>\r\n" + 
			"  </OrderInfo>\r\n" + 
			"</Order>]]></ns1:item>\r\n" + 
			"         </ns1:item>\r\n" + 
			"      </runJobReturn>\r\n" + 
			"   </soapenv:Body>\r\n" + 
			"</soapenv:Envelope>";

	/**
	 * Test method for migrateIAS in Data Migration
	 * 
	 * @author diksha garg
	 * @throws Exception 
	 */
	@Test
	public void testMigrateILL() throws Exception {
		
		JSONObject obj = XML.toJSONObject(ill_data);
		JSONObject envelope = (JSONObject) obj.get("soapenv:Envelope");
		JSONObject body = (JSONObject) envelope.get("soapenv:Body");
		JSONObject runJobReturn = (JSONObject) body.get("runJobReturn");
		JSONObject ns1Item = (JSONObject) runJobReturn.get("ns1:item");
		JSONObject item = (JSONObject) ns1Item.get("ns1:item");		
		String contentItem = (String) item.get("content");
		System.out.println(contentItem);
		JSONObject orderJson = XML.toJSONObject(contentItem);
		JSONObject order = orderJson.getJSONObject("Order");
		String response = dataMigration.migrateILL(orderJson,
				orderJson.getJSONObject("OrderInfo").get("ServiceID").toString(),null,null,null);
		System.out.println(response);

	}
}
