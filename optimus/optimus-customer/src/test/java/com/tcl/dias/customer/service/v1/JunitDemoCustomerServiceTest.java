package com.tcl.dias.customer.service.v1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JunitDemoCustomerServiceTest {
	
	@Autowired
	JunitDemoCustomerService junitDemoCustomerService;

	@Test
	public void testJunitDemo() {
		String response = junitDemoCustomerService.testJunitDemo();
		assertEquals("Hello World", response);
	}
	
	
}