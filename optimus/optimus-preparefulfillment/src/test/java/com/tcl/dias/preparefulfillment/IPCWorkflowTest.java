package com.tcl.dias.preparefulfillment;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.preparefulfillment.servicefulfillment.service.ServiceFulfillmentService;
import com.tcl.dias.servicefulfillment.entity.entities.ScGstAddress;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IPCWorkflowTest {


	@Autowired
	org.flowable.engine.TaskService flowableTaskService;

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	ServiceFulfillmentService serviceFulfillmentService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Value("${rabbitmq.customer.all.secs.queue}")
	String customerAllSecsQueue;
	
	@Autowired
	MQUtils mqUtils;

	@Test
	public void testProcessData() throws Exception {		
		Map<String, Object> processVar = new HashMap<>();
		processVar.put(SC_ORDER_ID, 286);
		processVar.put(ORDER_CODE, "IAS170120QFDKTTV");
		processVar.put(ORDER_TYPE, "NEW");
		processVar.put(SERVICE_CODE, "091CHEN0300A0001974");
		processVar.put(SERVICE_ID, 578);
		processVar.put("isCPERequired",true);
		processVar.put("bandwidthAllocationReq",true);
		processVar.put("cpeType","Outright");
		processVar.put("isCPEArrangedByCustomer",false);
		processVar.put("vpnConnectionStatusReq",true);
		//processVar.put("billing_start_stage_ID",5);
		//runtimeService.startProcessInstanceByKey("ill-service-fulfilment-handover-workflow-test", processVar);	 
		runtimeService.startProcessInstanceByKey("billing_handover_workflow_products", processVar);
		//ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(728); 
		//ScGstAddress gstAddress = scServiceDetail.getScGstAddress();
		//System.out.println(gstAddress.getDistrict());
		//List<String> entitySapCodes = getAllSecsCode(124);
		//System.out.println(entitySapCodes);
		/*
		 * ScServiceAttribute attribute = new ScServiceAttribute();
		 * attribute.setAttributeName("secsCodes");
		 * attribute.setScServiceDetail(scServiceDetail);
		 * scServiceAttributeRepository.save(attribute);
		 */
		/*
		 * try {
		 * componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
		 * "secsCodes", Utils.convertObjectToJson(entitySapCodes)); } catch
		 * (TclCommonException e) { e.printStackTrace(); }
		 */
	}
	
	@Test
	public void manuallyCompleteTaskTest() throws Exception {
		Map<String, Object> processVar = new HashMap<>();
		//processVar.put("discountApprovalLevel", 3);
		taskService.manuallyCompleteTask(2522, processVar);
		//Scanner scanner = new Scanner(System.in);
		//String input= scanner.nextLine();
	}
	
	/*@Test
	public void getTasks() throws TclCommonException {
		List<AssignedGroupingBean> response = taskService.getTaskDetails("C1", null, null, null,
				null);
		System.out.print(response);
		
	}*/
	
	@SuppressWarnings("unchecked")
	public List<String> getAllSecsCode(int cusId) {
		String crnIds = null;
		ArrayList<String> listSapCode=null;
		try {
			crnIds = (String) mqUtils.sendAndReceive(customerAllSecsQueue, new Integer(cusId).toString());
			
			listSapCode= Utils.convertJsonToObject(crnIds, ArrayList.class);
		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return listSapCode;

	}
	
	
	
}
