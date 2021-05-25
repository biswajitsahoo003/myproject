package com.tcl.dias.wfe.integration;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.wfe.controller.v1.WorkFlowEngineController;
import com.tcl.dias.wfe.util.ObjectCreator;

/*import static org.junit.Assert.fail;
import org.junit.Before;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.tcl.dias.common.test.utils.TokenOperations;
*/

/**
 * 
 * @author Dinahar V
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class WorkFlowEngineControllerIntegrationTest {

	@Autowired
	private WorkFlowEngineController wfeController;

	@Autowired
	private ObjectCreator objectCreator;

}
