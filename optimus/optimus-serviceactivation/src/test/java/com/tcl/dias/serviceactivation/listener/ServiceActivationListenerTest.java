package com.tcl.dias.serviceactivation.listener;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceActivationListenerTest {

    @Autowired
    TaskService taskService;

    @Autowired
    MQUtils mqUtils;

    @Test
    public void test() throws TclCommonException {
        //CreatePlannedEventBean createPlannedEventBean = Utils.convertJsonToObject("", CreatePlannedEventBean.class);
        String response = (String) mqUtils.sendAndReceive("availableslots_queue", "{\"serviceId\": 1249,\"processInstanceId\":\"LHZC4#154a307d-076b-11ea-9037-0242ac110010\"}");
    }

}