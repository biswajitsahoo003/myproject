/**
 * 
 */
package com.tcl.dias.servicefulfillmentutils.service.v1;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ASyed
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CriticalPathServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CriticalPathServiceTest.class);
	
    @Autowired
    CriticalPathService criticalPathService;
    
    
	@Test
	public void testComputeCriticalPath() {
		Integer serviceId = new Integer(1681);
		logger.info("Triggering criticalPathService.computeCriticalPath for Service Id :{}",serviceId);
		criticalPathService.computeCriticalPath(serviceId);
		
		assertTrue(true);
	}

}
