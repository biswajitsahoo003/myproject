package com.tcl.dias.wfe.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.wfe.Application;
import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Value("${rabbitmq.feasibility.request}")
	private String requestQueue;
	@Autowired
	MQUtils mqUtil;

	@org.junit.Test
	public void test() throws TclCommonException {

		//String request = "{\"input_data\":[{\"bw_mbps\":2,\"customer_segment\":\"Enterprise-Direct\",\"sales_org\":\"Enterprise\",\"product_name\":\"Internet Access Service\",\"local_loop_interface\":\"GE\",\"last_mile_contract_term\":\"1 Year\",\"site_id\":\"2294_primary\",\"prospect_name\":\"Regus\",\"burstable_bw\":2,\"resp_city\":\"Tiruvallur\",\"account_id_with_18_digit\":\"0012000000BSomfAAD\",\"opportunity_term\":12,\"quotetype_quote\":\"New Order\",\"connection_type\":\"Standard\",\"sum_no_of_sites_uni_len\":1,\"cpe_variant\":\"None\",\"cpe_management_type\":\"unmanaged\",\"cpe_supply_type\":\"rental\",\"topology\":\"primary_active\",\"latitude_final\":13.0184111,\"longitude_final\":80.18774819999999,\"feasibility_response_created_date\":\"2018-08-01\",\"additional_ip_flag\":\"No\",\"ip_address_arrangement\":\"None\",\"ipv4_address_pool_size\":\"29\",\"ipv6_address_pool_size\":\"0\"}]}";
		String request = "{\"input_data\":[{\"bw_mbps\":20,\"customer_segment\":\"Enterprise-Direct\",\"sales_org\":\"Enterprise\",\"product_name\":\"Internet Access Service\",\"local_loop_interface\":\"FE\",\"last_mile_contract_term\":\"1 Year\",\"site_id\":\"3331_primary\",\"prospect_name\":\"Regus\",\"burstable_bw\":20,\"resp_city\":\"Tiruvallur\",\"account_id_with_18_digit\":\"0012000000BSomfAAD\",\"opportunity_term\":12,\"quotetype_quote\":\"New Order\",\"connection_type\":\"Standard\",\"sum_no_of_sites_uni_len\":1,\"cpe_variant\":\"None\",\"cpe_management_type\":\"unmanaged\",\"cpe_supply_type\":\"rental\",\"topology\":\"primary_active\",\"latitude_final\":13.0184111,\"longitude_final\":80.18774819999999,\"feasibility_response_created_date\":\"2018-08-08\",\"additional_ip_flag\":\"No\",\"ip_address_arrangement\":\"None\",\"ipv4_address_pool_size\":\"29\",\"ipv6_address_pool_size\":\"0\"}]}";

		mqUtil.send(requestQueue, request);
	}

	/*
	 * public static void main(String[] args) { new Test().test(); }
	 */

	/*
	 * @Value("${rabbitmq.feasibility.response}") private String responseQueue;
	 * 
	 * @Autowired MQUtils mqUtils;
	 * 
	 * @Autowired private RuntimeService runtimeService;
	 */

	// @EventListener
	private void processPostDeploy(PostDeployEvent event) {

		LOGGER.info("processPostDeploy::: Start");
		try {
			// Thread.sleep(5000);
			processFeasibiltyMessage(responseBody());
			Map<String, Object> variables = new HashMap<>();
			// variables.put("siteArray", siteArray);

			List<Map<String, Object>> r1Sites = new ArrayList<>();
			List<Map<String, Object>> r2Sites = new ArrayList<>();
			List<Map<String, Object>> r3Sites = new ArrayList<>();
			variables.put("r1site", r1Sites);
			variables.put("r2site", r2Sites);
			variables.put("r3site", r3Sites);

			// runtimeService.startProcessInstanceByKey("new_process_1", variables);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("processPostDeploy::: End");
	}

	// @RabbitListener(queues = "${rabbitmq.feasibility.request}")
	public void processFeasibiltyMessage(String responseBody) throws Exception {
		List<Map<String, Object>> response = null;
		try {
			LOGGER.info("Process Start::::::::::::");
			JSONParser jsonParser = new JSONParser();
			JSONObject top = (JSONObject) jsonParser.parse(responseBody);
			JSONArray arr = (JSONArray) top.get("input_data");
			List<Map<String, Object>> list = (List<Map<String, Object>>) arr;
			response = list;
			/** Start **/

			Map<String, Object> variables = new HashMap<>();
			variables.put("siteArray", response);

			List<Map<String, Object>> r1Sites = new ArrayList<>();
			List<Map<String, Object>> r2Sites = new ArrayList<>();
			List<Map<String, Object>> r3Sites = new ArrayList<>();

			Map<String, Map<String, Object>> feasibleSites = new HashMap<>();
			Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();

			variables.put("r1site", r1Sites);
			variables.put("r2site", r2Sites);
			variables.put("r3site", r3Sites);

			// ProcessInstance instance =
			// runtimeService.startProcessInstanceByKey("new_process_1", variables);
			// ProcessInstance instance =
			// runtimeService.startProcessInstanceByKey("new_process_New_Test", variables);
			/** END **/
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorMap = new HashMap<>();
			for (Map map : response) {
				errorMap.put((String) map.get(WFEConstants.SITE_ID), "Error_in_feasibility");
			}
			// mqUtils.send(responseQueue, Utils.convertObjectToJson(errorMap));
			LOGGER.info("Application Exception::::::::::::");
		}
		LOGGER.info("Process End::::::::::::");
	}

	private String responseBody() {
		return "{\"input_data\":[{\"bw_mbps\":2,\"customer_segment\":\"Enterprise-Direct\",\"sales_org\":\"Enterprise\",\"product_name\":\"Internet Access Service\",\"local_loop_interface\":\"GE\",\"last_mile_contract_term\":\"1 Year\",\"site_id\":\"2294_primary\",\"prospect_name\":\"Regus\",\"burstable_bw\":2,\"resp_city\":\"Tiruvallur\",\"account_id_with_18_digit\":\"0012000000BSomfAAD\",\"opportunity_term\":12,\"quotetype_quote\":\"New Order\",\"connection_type\":\"Standard\",\"sum_no_of_sites_uni_len\":1,\"cpe_variant\":\"None\",\"cpe_management_type\":\"unmanaged\",\"cpe_supply_type\":\"rental\",\"topology\":\"primary_active\",\"latitude_final\":13.0184111,\"longitude_final\":80.18774819999999,\"feasibility_response_created_date\":\"2018-08-01\",\"additional_ip_flag\":\"No\",\"ip_address_arrangement\":\"None\",\"ipv4_address_pool_size\":\"29\",\"ipv6_address_pool_size\":\"0\"}]}";
	}

}
