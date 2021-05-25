package com.tcl.dias.wfe.dao;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.wfe.service.v1.WorkFlowEngineService;
import com.tcl.dias.wfe.util.ObjectCreator;

/**
 * 
 * @author Dinahar V
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestWorkFlowEngineService {

	@Autowired
	private WorkFlowEngineService wfeService;

	@Autowired
	private ObjectCreator objectCreator;

}
