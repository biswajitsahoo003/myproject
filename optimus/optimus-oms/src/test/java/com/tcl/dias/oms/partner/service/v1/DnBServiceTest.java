package com.tcl.dias.oms.partner.service.v1;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.oms.partner.beans.dnb.DnBRequestBean;
import com.tcl.dias.oms.partner.beans.dnb.DnbLeDetailsBean;
import com.tcl.dias.oms.utils.PartnerObjectCreator;

/**
 * This file contains test cases for DashboardService.java class.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DnBServiceTest {

    private static Logger logger = LoggerFactory.getLogger(DnBServiceTest.class);


    @Autowired
    PartnerObjectCreator partnerObjectCreator;

    @Autowired
    DnBService dnBService;


    @Test
    public void TestCompareOptimusAndDnBCustomerLeName() {
        DnBService dnBService = mock(DnBService.class);
        DnBRequestBean dnBRequestBean = partnerObjectCreator.createDnBRequestBean();
        when(dnBService.compareOptimusAndDnBCustomerLeName(dnBRequestBean)).thenCallRealMethod();
//        when(dnBService.getDnBAuthorizationToken()).thenReturn("testtoken");
        List<DnbLeDetailsBean> dnbLeDetailsBeanList = dnBService.compareOptimusAndDnBCustomerLeName(dnBRequestBean);
        Assert.assertTrue(dnbLeDetailsBeanList.size() == 0);

    }

    @Test
    public void testConstructLegalEntityList() {

    }


}
