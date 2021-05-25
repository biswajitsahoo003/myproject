package com.tcl.dias.wfe.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.wfe.controller.v1.WorkFlowEngineController;
import com.tcl.dias.wfe.service.v1.WorkFlowEngineService;
import com.tcl.dias.wfe.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author Dinahar V
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkFlowEngineControllerTest {

	private static final Integer FAMILY_ID = 3;

	@MockBean
	private WorkFlowEngineService wfeService;

	@Autowired
	private WorkFlowEngineController wfeController;

	@Autowired
	private ObjectCreator objectCreator;
	
	
	@Autowired
	MQUtils mqUtils;
	
	@Test
	public void sendResponse() throws TclCommonException {
		/*mqUtils.send("rcode_test", "{\"input_data\":[\r\n" + 
				"	{\"Site_id\" : \"1\",\r\n" + 
				"	\"Latitude_final\" : \"28.549806\",\r\n" + 
				"                    \"Longitude_final\" : \"93.272333\",\r\n" + 
				"                    \"prospect_name\" : \"Kasturi & Sons Limited\",\r\n" + 
				"                    \"BW_mbps\" : \"2\",\r\n" + 
				"		    \"Burstable_BW\" : \"4\",\r\n" + 
				"                    \"resp_city\" : \"Ranchi\",\r\n" + 
				"                    \"resp_state\" : \"JHARKHAND\",                  \r\n" + 
				"                    \"Customer_Segment\" : \"Carriers\",\r\n" + 
				"                    \"Sales.Org\" : \"Service Provider\",\r\n" + 
				"                    \"Product.Name\" : \"Global VPN\",                    \r\n" + 
				"                    \"Feasibility.Response..Created.Date\" : \"2016-10-18\",\r\n" + 
				"                    \"local_loop_interface\" : \"FE\",\r\n" + 
				"                    \"last_mile_contract_term\" : \"2 Years\"}]}");*/
		
		mqUtils.send("rcode_test", "{\r\n" + 
				"	\"input_data\": [\r\n" + 
				"		{\r\n" + 
				"			\"Latitude_final\": \"28.549806\",\r\n" + 
				"			\"Longitude_final\": \"77.272333\",\r\n" + 
				"			\"prospect_name\": \"Aircel Limited\",\r\n" + 
				"			\"Burstable_BW\": \"50\",\r\n" + 
				"			\"BW_mbps\": \"50\",\r\n" + 
				"			\"resp_city\": \"Delhi\",\r\n" + 
				"			\"resp_state\": \"delhi\",\r\n" + 
				"			\"Customer_Segment\": \"Carriers\",\r\n" + 
				"			\"Sales.Org\": \"Service Provider\",\r\n" + 
				"			\"Product.Name\": \"Global VPN\",\r\n" + 
				"			\"Feasibility.Response..Created.Date\": \"2016-10-18\",\r\n" + 
				"			\"local_loop_interface\": \"FE\",\r\n" + 
				"			\"last_mile_contract_term\": \"2 Years\",\r\n" + 
				"			\"Site_id\":\"1\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"Latitude_final\": \"28.605333\",\r\n" + 
				"			\"Longitude_final\": \"77.354167\",\r\n" + 
				"			\"prospect_name\": \"Aircel Limited\",\r\n" + 
				"			\"Burstable_BW\": \"2\",\r\n" + 
				"			\"BW_mbps\": \"2\",\r\n" + 
				"			\"resp_city\": \"Noida\",\r\n" + 
				"			\"resp_state\": \"uttar pradesh\",\r\n" + 
				"			\"Customer_Segment\": \"Carriers\",\r\n" + 
				"			\"Sales.Org\": \"Service Provider\",\r\n" + 
				"			\"Product.Name\": \"Global VPN\",\r\n" + 
				"			\"Feasibility.Response..Created.Date\": \"2017-02-01\",\r\n" + 
				"			\"local_loop_interface\": \"100-Base-TX\",\r\n" + 
				"			\"last_mile_contract_term\": \"2 Years\",\r\n" + 
				"			\"Site_id\":\"2\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"Latitude_final\": \"15.353194\",\r\n" + 
				"			\"Longitude_final\": \"75.132806\",\r\n" + 
				"			\"prospect_name\": \"ICICI Group\",\r\n" + 
				"			\"Burstable_BW\": \"2\",\r\n" + 
				"			\"BW_mbps\": \"2\",\r\n" + 
				"			\"resp_city\": \"Hubli\",\r\n" + 
				"			\"resp_state\": \"karnataka\",\r\n" + 
				"			\"Customer_Segment\": \"Enterprise - Strategic\",\r\n" + 
				"			\"Sales.Org\": \"Enterprise\",\r\n" + 
				"			\"Product.Name\": \"Global VPN\",\r\n" + 
				"			\"Feasibility.Response..Created.Date\": \"2017-02-01\",\r\n" + 
				"			\"local_loop_interface\": \"FE\",\r\n" + 
				"			\"last_mile_contract_term\": \"1 Year\",\r\n" + 
				"			\"Site_id\":\"3\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"Latitude_final\": \"13.100333\",\r\n" + 
				"			\"Longitude_final\": \"80.280222\",\r\n" + 
				"			\"prospect_name\": \"Euronet Worldwide Inc\",\r\n" + 
				"			\"Burstable_BW\": \"2\",\r\n" + 
				"			\"BW_mbps\": \"2\",\r\n" + 
				"			\"resp_city\": \"Chennai\",\r\n" + 
				"			\"resp_state\": \"tamil nadu\",\r\n" + 
				"			\"Customer_Segment\": \"Enterprise-Partners Account\",\r\n" + 
				"			\"Sales.Org\": \"Enterprise\",\r\n" + 
				"			\"Product.Name\": \"Global VPN\",\r\n" + 
				"			\"Feasibility.Response..Created.Date\": \"2017-01-01\",\r\n" + 
				"			\"local_loop_interface\": \"100-Base-TX\",\r\n" + 
				"			\"last_mile_contract_term\": \"1 Year\",\r\n" + 
				"			\"Site_id\":\"4\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"Latitude_final\": \"25.311917\",\r\n" + 
				"			\"Longitude_final\": \"82.988083\",\r\n" + 
				"			\"prospect_name\": \"Sify Technologies Limited\",\r\n" + 
				"			\"Burstable_BW\": \"2\",\r\n" + 
				"			\"BW_mbps\": \"2\",\r\n" + 
				"			\"resp_city\": \"Varanasi\",\r\n" + 
				"			\"resp_state\": \"uttar pradesh\",\r\n" + 
				"			\"Customer_Segment\": \"Carriers\",\r\n" + 
				"			\"Sales.Org\": \"Service Provider\",\r\n" + 
				"			\"Product.Name\": \"Global VPN\",\r\n" + 
				"			\"Feasibility.Response..Created.Date\": \"2017-02-01\",\r\n" + 
				"			\"local_loop_interface\": \"100-Base-TX\",\r\n" + 
				"			\"last_mile_contract_term\": \"1 Year\",\r\n" + 
				"			\"Site_id\":\"5\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"Latitude_final\": \"18.562572200000002\",\r\n" + 
				"			\"Longitude_final\": \"73.883564699999994\",\r\n" + 
				"			\"prospect_name\": \"Aircel Limited\",\r\n" + 
				"			\"Burstable_BW\": \"2\",\r\n" + 
				"			\"BW_mbps\": \"2\",\r\n" + 
				"			\"resp_city\": \"pune\",\r\n" + 
				"			\"resp_state\": \"maharashtra\",\r\n" + 
				"			\"Customer_Segment\": \"Carriers\",\r\n" + 
				"			\"Sales.Org\": \"Service Provider\",\r\n" + 
				"			\"Product.Name\": \"NPL\",\r\n" + 
				"			\"Feasibility.Response..Created.Date\": \"2017-01-27\",\r\n" + 
				"			\"local_loop_interface\": \"FE\",\r\n" + 
				"			\"last_mile_contract_term\": \"1 Year\",\r\n" + 
				"			\"Site_id\":\"6\"\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
				"}");

	}

}
