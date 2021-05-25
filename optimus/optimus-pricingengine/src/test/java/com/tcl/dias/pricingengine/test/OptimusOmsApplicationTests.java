package com.tcl.dias.pricingengine.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.pricingengine.ipc.services.IpcPricingService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the OptimusOmsApplicationTests.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OptimusOmsApplicationTests {

	@Autowired
	IpcPricingService ipcPricingService;

	@Test
	public void contextLoads() throws TclCommonException {
		String a="{\r\n" + 
				"  \"quotes\": [\r\n" + 
				"    {\r\n" + 
				"      \"quoteId\": \"21330\",\r\n" + 
				"      \"term\": 12,\r\n" + 
				"     \"managementEnabled\": true,\r\n" + 
				"      \"cloudvm\": [\r\n" + 
				"        {\r\n" + 
				"          \"itemId\": \"1573\",\r\n" + 
				"          \"type\": \"new\",\r\n" + 
				"          \"region\": \"INDIA\",\r\n" + 
				"          \"count\": 1,\r\n" + 
				"          \"perGBAdditionalIOPSForSSD\": \"0\",\r\n" + 
				"          \"variant\": \"L.Nickel\",\r\n" + 
				"          \"vcpu\": \"1\",\r\n" + 
				"          \"vram\": \"1\",\r\n" + 
				"          \"rootStorage\": {\r\n" + 
				"            \"type\": \"SSD\",\r\n" + 
				"            \"size\": \"50\"\r\n" + 
				"          },\r\n" + 
				"          \"additionalStorages\": [\r\n" + 
				"            {\r\n" + 
				"              \"type\": \"SSD\",\r\n" + 
				"              \"size\": \"50\"\r\n" + 
				"            }\r\n" + 
				"          ],\r\n" + 
				"          \"os\": \"centos\",\r\n" + 
				"          \"hypervisor\": \"ESXI\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"          \"itemId\": \"1574\",\r\n" + 
				"          \"type\": \"new\",\r\n" + 
				"          \"region\": \"INDIA\",\r\n" + 
				"          \"count\": 1,\r\n" + 
				"          \"perGBAdditionalIOPSForSSD\": \"0\",\r\n" + 
				"          \"variant\": \"L.Bronze\",\r\n" + 
				"          \"vcpu\": \"2\",\r\n" + 
				"          \"vram\": \"2\",\r\n" + 
				"          \"rootStorage\": {\r\n" + 
				"            \"type\": \"SSD\",\r\n" + 
				"            \"size\": \"100\"\r\n" + 
				"          },\r\n" + 
				"          \"additionalStorages\": [\r\n" + 
				"            {\r\n" + 
				"              \"type\": \"SSD\",\r\n" + 
				"              \"size\": \"50\"\r\n" + 
				"            }\r\n" + 
				"          ],\r\n" + 
				"          \"os\": \"windows\",\r\n" + 
				"          \"hypervisor\": \"ESXI\"\r\n" + 
				"        }\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}\r\n" + 
				"";
		String response=ipcPricingService.processIpcPricing(a);
		System.out.println(response);
	}

}
