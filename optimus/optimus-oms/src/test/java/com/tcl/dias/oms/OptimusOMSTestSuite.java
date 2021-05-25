package com.tcl.dias.oms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.oms.controller.DashBoardControllerTest;
import com.tcl.dias.oms.controller.FeasibilityEngineListnerTests;
import com.tcl.dias.oms.controller.IllOrderControllerTest;
import com.tcl.dias.oms.controller.IllQuotesControllerTest;
import com.tcl.dias.oms.controller.InternalStakeViewControllerTest;
import com.tcl.dias.oms.controller.OmsAttachmentListenerTest;
import com.tcl.dias.oms.controller.SlaControllerTest;
import com.tcl.dias.oms.controller.UserServiceControllerTest;
import com.tcl.dias.oms.gsc.common.GscPdfHelperTest;
import com.tcl.dias.oms.gsc.controller.GscDashboardControllerTest;
import com.tcl.dias.oms.gsc.controller.GscOrderControllerTest;
import com.tcl.dias.oms.gsc.controller.GscQuoteControllerTest;
import com.tcl.dias.oms.gsc.service.GscOrderDetailServiceTest;
import com.tcl.dias.oms.gsc.service.GscOrderServiceTest;
import com.tcl.dias.oms.gsc.service.GscQuoteAttributeServiceTest;
import com.tcl.dias.oms.gsc.service.GscQuoteServiceTest;
import com.tcl.dias.oms.gsc.util.GscComponentAttributeValuesHelperTest;
import com.tcl.dias.oms.service.IllSfdcTest;

/**
 * 
 * test suite for OptimusLocationTestSuite.java class.
 * 
 *
 * @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	
	IllOrderControllerTest.class ,
	IllQuotesControllerTest.class,
	OmsAttachmentListenerTest.class,
	UserServiceControllerTest.class,
	SlaControllerTest.class,
	IllSfdcTest.class,
	FeasibilityEngineListnerTests.class,
	DashBoardControllerTest.class,
	
	GscDashboardControllerTest.class,
	GscQuoteControllerTest.class,
	GscOrderDetailServiceTest.class,
	GscOrderServiceTest.class,
	GscQuoteAttributeServiceTest.class,
	GscQuoteServiceTest.class,
	GscComponentAttributeValuesHelperTest.class,
	GscOrderControllerTest.class,
	InternalStakeViewControllerTest.class,
	OmsAttachmentListenerTest.class,
	GscPdfHelperTest.class
	
})
public class OptimusOMSTestSuite {

}
