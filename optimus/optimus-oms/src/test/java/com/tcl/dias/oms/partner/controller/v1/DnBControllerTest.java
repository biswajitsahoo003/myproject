package com.tcl.dias.oms.partner.controller.v1;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.PartnerEntityRequest;
import com.tcl.dias.oms.partner.beans.dnb.DnBRequestBean;
import com.tcl.dias.oms.partner.beans.dnb.DnbLeDetailsBean;
import com.tcl.dias.oms.partner.service.v1.DnBService;
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
public class DnBControllerTest {


    @Autowired
    PartnerObjectCreator partnerObjectCreator;

    @MockBean
    DnBService dnBService;

    @MockBean
    DnBRequestBean dnBRequestBean;

    @Test
    public void testGetOptimusAndDnbPartnerLeNames() {
        dnBRequestBean = partnerObjectCreator.createDnBRequestBean();
        Mockito.when(dnBService.compareOptimusAndDnBCustomerLeName(dnBRequestBean)).thenReturn(partnerObjectCreator.createDnBLeDetailsList());
        List<DnbLeDetailsBean> response = dnBService.compareOptimusAndDnBCustomerLeName(dnBRequestBean);
        ResponseResource<List<DnbLeDetailsBean>> responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertTrue(responseResource.getData().stream().findFirst().get().getDunsId() == 22);

    }


    @Test
    public void TestGetOptimusAndDnbPartnerLeNamesCheckStatus() {

        Mockito.when(dnBService.compareOptimusAndDnBCustomerLeName(partnerObjectCreator.createDnBRequestBean())).thenReturn(partnerObjectCreator.createDnBLeDetailsList());
        List<DnbLeDetailsBean> response = dnBService.compareOptimusAndDnBCustomerLeName(partnerObjectCreator.createDnBRequestBean());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertTrue(responseResource.getStatus() == Status.SUCCESS);
    }


    @Test
    public void TestGetDNBLegalEntityDetails() {
        Mockito.when(dnBService.getDnBLegalEntityDetails(Mockito.anyInt())).thenReturn(partnerObjectCreator.createPartnerEntityRequestObject());
        PartnerEntityRequest response = dnBService.getDnBLegalEntityDetails(1);
        ResponseResource<PartnerEntityRequest> responseResourcee = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertTrue(responseResourcee != null);
    }

    @Test
    public void TestGetDNBLegalEntityDetailsCheckStatus() {
        Mockito.when(dnBService.getDnBLegalEntityDetails(Mockito.anyInt())).thenReturn(partnerObjectCreator.createPartnerEntityRequestObject());
        PartnerEntityRequest response = dnBService.getDnBLegalEntityDetails(1);
        ResponseResource<PartnerEntityRequest> responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertTrue(responseResource.getStatus() == Status.SUCCESS);
    }


}
