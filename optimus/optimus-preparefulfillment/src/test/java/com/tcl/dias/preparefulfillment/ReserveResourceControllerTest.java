package com.tcl.dias.preparefulfillment;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.RuntimeService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.servicefulfillment.beans.ServiceFulfillmentRequest;
//import com.tcl.dias.preparefulfillment.config.KeycloakInterceptor;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This test class has different dimensions of test cases
 * for reserve resource controller.
 *
 * @author arjayapa
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Sql(scripts={"classpath:data.sql"})
public class ReserveResourceControllerTest {

    @Value("${app.host}")
    private String host;

    @Value("${app.host.port}")
    private String port;

    @Value("${keycloak.auth-server-url}")
    private String authServer;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String resource;

    @Value("${preparefulfillment.keycloak.testUser}")
    private String keycloakTestUser;

    @Value("${preparefulfillment.keycloak.testPassword}")
    private String keycloakTestPassword;

    private ServiceFulfillmentRequest res;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    private ProcessL2OService processL2OService;

    @Before
    public void init() throws TclCommonException {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getRestTemplate().getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        //interceptors.add(new KeycloakInterceptor(authServer, realm, resource, keycloakTestUser, keycloakTestPassword));
        restTemplate.getRestTemplate().setInterceptors(interceptors);

    }

    @Test
    public void testReserveResource() throws TclCommonException, InterruptedException {
   
        processL2OService.processL2ODataToFlowable(576,null,false);
        Task task = taskRepo.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(576, "basic_enrichment");
        if (task !=null) {         
            runtimeService.trigger(task.getWfTaskId());            
        }
    }
}
