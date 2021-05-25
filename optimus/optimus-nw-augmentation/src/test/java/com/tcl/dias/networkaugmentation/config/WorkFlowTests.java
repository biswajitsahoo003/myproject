package com.tcl.dias.networkaugmentation.config;

import com.tcl.dias.networkaugmentation.beans.NwaOrderDetailsBean;
import com.tcl.dias.networkaugmentation.beans.OrderInitiateResultBean;
import com.tcl.dias.networkaugmentation.service.NetworkAugmentationService;
import com.tcl.dias.networkaugmentation.service.OrderService;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeResponse;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.flowable.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class WorkFlowTests {

    @Autowired
    NetworkAugmentationService networkAugmentationService;


    @Autowired
    TaskService taskService;


    @Autowired
    OrderService orderService;

    @Autowired
    RuntimeService runtimeService;

    @Test
    public void testStageWorkflow() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("eorIpCard",true);
        runtimeService.startProcessInstanceByKey("eor_stage_workflow", vars);
    }


    @Test
    public void test_eor_tx_card_provisioning_V20() throws TclCommonException {
        String processToStart = "eor_tx_card_provisioning_V20";
        NwaOrderDetailsBean nwaOrderDetailsBean = new NwaOrderDetailsBean();
        //orderBean.setBtsOrderId();
        //nwaOrderDetailsBean.setOrderCode("EOR_5000696");
        nwaOrderDetailsBean.setOrderType("EORTXCARD");
        nwaOrderDetailsBean.setSubject("Equip Tx Card Prov Order Initiation");
        nwaOrderDetailsBean.setProjectType("EOR");
        nwaOrderDetailsBean.setPmName("Rajesh");
        nwaOrderDetailsBean.setPmContactEmail("gimec_nwa@legomail.com");
        nwaOrderDetailsBean.setOrderStatus(String.valueOf(1));
        nwaOrderDetailsBean.setOriginatorName("Rajesh");
        nwaOrderDetailsBean.setOriginatorGroupId("GIMEC_NWA");
        nwaOrderDetailsBean.setOriginatOrContactNumber("9845098450");

        Map<String, String> initiateProvisioningResp = networkAugmentationService.initiateProvisioning(processToStart, nwaOrderDetailsBean);;
        System.out.println("initiateProvisioningResp: "+initiateProvisioningResp);
        Integer taskId = Integer.valueOf(initiateProvisioningResp.get("TASK_ID"));
        String wfTaskId = initiateProvisioningResp.get("WF_TASK_ID");


        AssigneeRequest request = new AssigneeRequest();
        request.setTaskId(taskId);
        request.setAssigneeNameFrom("");
        request.setAssigneeNameTo("gimec_nwa@legomail.com");
        request.setWfTaskId(wfTaskId);
        request.setType("Claim");
        AssigneeResponse assignResp = taskService.updateAssignee(request);
        System.out.println("assignResp: "+assignResp);
        Map<String, Object> payLoad = new HashMap<>();
        OrderInitiateResultBean orderInitiateResultBean = orderService.orderInitate(payLoad);
        System.out.println("orderInitiateResultBean: "+orderInitiateResultBean);


        Map<String, Object> completeTaskPayload = new HashMap<>();
        String completeResp = taskService.manuallyCompleteTask(taskId, completeTaskPayload);
        System.out.println("Complete resp: "+completeResp);

        //api to find open tasks to clear or next task to complete.
    }

    @Test
    public void test_eor_equip_wireless_provisioning() throws TclCommonException {
        String processToStart = "eor_equip_wireless_provisioning";
        NwaOrderDetailsBean nwaOrderDetailsBean = new NwaOrderDetailsBean();
        //orderBean.setBtsOrderId();
        nwaOrderDetailsBean.setOrderCode("EOR_5000696");
        nwaOrderDetailsBean.setOrderType("EORTX");
        nwaOrderDetailsBean.setSubject("Equip Tx Prov Order Initiation");
        nwaOrderDetailsBean.setProjectType("EOR");
        nwaOrderDetailsBean.setPmName("Rajesh");
        nwaOrderDetailsBean.setPmContactEmail("gimec_nwa@legomail.com");
        nwaOrderDetailsBean.setOrderStatus(String.valueOf(1));
        nwaOrderDetailsBean.setOriginatorName("Rajesh");
        nwaOrderDetailsBean.setOriginatorGroupId("GIMEC_NWA");
        nwaOrderDetailsBean.setOriginatOrContactNumber("9845098450");

        Map<String, String> initiateProvisioningResp = networkAugmentationService.initiateProvisioning(processToStart, nwaOrderDetailsBean);;
        System.out.println("initiateProvisioningResp: "+initiateProvisioningResp);
        Integer taskId = Integer.valueOf(initiateProvisioningResp.get("taskId"));
        String wfTaskId = initiateProvisioningResp.get("wfTaskId");


        AssigneeRequest request = new AssigneeRequest();
        request.setTaskId(taskId);
        request.setAssigneeNameFrom("");
        request.setAssigneeNameTo("gimec_nwa@legomail.com");
        request.setWfTaskId(wfTaskId);
        request.setType("Claim");
        AssigneeResponse assignResp = taskService.updateAssignee(request);
        System.out.println("assignResp: "+assignResp);
        Map<String, Object> payLoad = new HashMap<>();
        OrderInitiateResultBean orderInitiateResultBean = orderService.orderInitate(payLoad);
        System.out.println("orderInitiateResultBean: "+orderInitiateResultBean);


        Map<String, Object> completeTaskPayload = new HashMap<>();
        String completeResp = taskService.manuallyCompleteTask(taskId, completeTaskPayload);
        System.out.println("Complete resp: "+completeResp);
    }

    @Test
    public void test_ip_card_removal() throws TclCommonException {
        String processToStart = "eor_ip_card_removal";
        NwaOrderDetailsBean nwaOrderDetailsBean = new NwaOrderDetailsBean();
        //orderBean.setBtsOrderId();
        nwaOrderDetailsBean.setOrderCode("EOR_0005000010");
        nwaOrderDetailsBean.setOrderType("EORIPCARDREM");
        nwaOrderDetailsBean.setSubject("Equip Ip Card Removal");
        nwaOrderDetailsBean.setProjectType("EOR");
        nwaOrderDetailsBean.setPmName("Rajesh");
        nwaOrderDetailsBean.setPmContactEmail("gimec_nwa@legomail.com");
        nwaOrderDetailsBean.setOrderStatus(String.valueOf(1));
        nwaOrderDetailsBean.setOriginatorName("Rajesh");
        nwaOrderDetailsBean.setOriginatorGroupId("GIMEC_NWA");
        nwaOrderDetailsBean.setOriginatOrContactNumber("9845098450");

        Map<String, String> initiateProvisioningResp = networkAugmentationService.initiateTermination(processToStart, nwaOrderDetailsBean);
        System.out.println("initiateProvisioningResp: "+initiateProvisioningResp);
        Integer taskId = Integer.valueOf(initiateProvisioningResp.get("TASK_ID"));
        String wfTaskId = initiateProvisioningResp.get("WF_TASK_ID");


        AssigneeRequest request = new AssigneeRequest();
        request.setTaskId(taskId);
        request.setAssigneeNameFrom("");
        request.setAssigneeNameTo("gimec_nwa@legomail.com");
        request.setWfTaskId(wfTaskId);
        request.setType("Claim");
        AssigneeResponse assignResp = taskService.updateAssignee(request);
        System.out.println("assignResp: "+assignResp);
        Map<String, Object> payLoad = new HashMap<>();
        OrderInitiateResultBean orderInitiateResultBean = orderService.orderInitate(payLoad);
        System.out.println("orderInitiateResultBean: "+orderInitiateResultBean);


        Map<String, Object> completeTaskPayload = new HashMap<>();
        String completeResp = taskService.manuallyCompleteTask(taskId, completeTaskPayload);
        System.out.println("Complete resp: "+completeResp);
    }

    @Test
    public void test_tx_card_removal() throws TclCommonException {
        String processToStart = "eor_equip_tx_card_removal";
        NwaOrderDetailsBean nwaOrderDetailsBean = new NwaOrderDetailsBean();
        //orderBean.setBtsOrderId();
        nwaOrderDetailsBean.setOrderCode("EOR_5000696");
        nwaOrderDetailsBean.setOrderType("EORTX");
        nwaOrderDetailsBean.setSubject("Equip Tx Prov Order Initiation");
        nwaOrderDetailsBean.setProjectType("EOR");
        nwaOrderDetailsBean.setPmName("Rajesh");
        nwaOrderDetailsBean.setPmContactEmail("gimec_nwa@legomail.com");
        nwaOrderDetailsBean.setOrderStatus(String.valueOf(1));
        nwaOrderDetailsBean.setOriginatorName("Rajesh");
        nwaOrderDetailsBean.setOriginatorGroupId("GIMEC_NWA");
        nwaOrderDetailsBean.setOriginatOrContactNumber("9845098450");

        Map<String, String> initiateProvisioningResp = networkAugmentationService.initiateTermination(processToStart, nwaOrderDetailsBean);
        System.out.println("initiateProvisioningResp: "+initiateProvisioningResp);
        Integer taskId = Integer.valueOf(initiateProvisioningResp.get("taskId"));
        String wfTaskId = initiateProvisioningResp.get("wfTaskId");


        AssigneeRequest request = new AssigneeRequest();
        request.setTaskId(taskId);
        request.setAssigneeNameFrom("");
        request.setAssigneeNameTo("gimec_nwa@legomail.com");
        request.setWfTaskId(wfTaskId);
        request.setType("Claim");
        AssigneeResponse assignResp = taskService.updateAssignee(request);
        System.out.println("assignResp: "+assignResp);
        Map<String, Object> payLoad = new HashMap<>();
        OrderInitiateResultBean orderInitiateResultBean = orderService.orderInitate(payLoad);
        System.out.println("orderInitiateResultBean: "+orderInitiateResultBean);


        Map<String, Object> completeTaskPayload = new HashMap<>();
        String completeResp = taskService.manuallyCompleteTask(taskId, completeTaskPayload);
        System.out.println("Complete resp: "+completeResp);
    }

    @Test
    public void test_eor_wireless_ip_pool_allocationl() throws TclCommonException {
        String processToStart = "eor_wireless_ip_pool_allocation";
        NwaOrderDetailsBean nwaOrderDetailsBean = new NwaOrderDetailsBean();
        //orderBean.setBtsOrderId();
        nwaOrderDetailsBean.setOrderCode("EOR_5000696");
        nwaOrderDetailsBean.setOrderType("EORTX");
        nwaOrderDetailsBean.setSubject("Equip Tx Prov Order Initiation");
        nwaOrderDetailsBean.setProjectType("EOR");
        nwaOrderDetailsBean.setPmName("Rajesh");
        nwaOrderDetailsBean.setPmContactEmail("gimec_nwa@legomail.com");
        nwaOrderDetailsBean.setOrderStatus(String.valueOf(1));
        nwaOrderDetailsBean.setOriginatorName("Rajesh");
        nwaOrderDetailsBean.setOriginatorGroupId("GIMEC_NWA");
        nwaOrderDetailsBean.setOriginatOrContactNumber("9845098450");

        Map<String, String> initiateProvisioningResp = networkAugmentationService.initiateProvisioning(processToStart, nwaOrderDetailsBean);
        System.out.println("initiateProvisioningResp: "+initiateProvisioningResp);
        Integer taskId = Integer.valueOf(initiateProvisioningResp.get("taskId"));
        String wfTaskId = initiateProvisioningResp.get("wfTaskId");


        AssigneeRequest request = new AssigneeRequest();
        request.setTaskId(taskId);
        request.setAssigneeNameFrom("");
        request.setAssigneeNameTo("gimec_nwa@legomail.com");
        request.setWfTaskId(wfTaskId);
        request.setType("Claim");
        AssigneeResponse assignResp = taskService.updateAssignee(request);
        System.out.println("assignResp: "+assignResp);
        Map<String, Object> payLoad = new HashMap<>();
        OrderInitiateResultBean orderInitiateResultBean = orderService.orderInitate(payLoad);
        System.out.println("orderInitiateResultBean: "+orderInitiateResultBean);


        Map<String, Object> completeTaskPayload = new HashMap<>();
        String completeResp = taskService.manuallyCompleteTask(taskId, completeTaskPayload);
        System.out.println("Complete resp: "+completeResp);
    }


}
