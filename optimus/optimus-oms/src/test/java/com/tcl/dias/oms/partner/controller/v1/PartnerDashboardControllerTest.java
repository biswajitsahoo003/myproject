package com.tcl.dias.oms.partner.controller.v1;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import com.tcl.dias.oms.partner.beans.PartnerSfdcSalesResponse;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareTrainingBean;
import com.tcl.dias.oms.partner.service.v1.PartnerDashboardService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartnerDashboardControllerTest {
    @Autowired
    PartnerDashboardController partnerDashboardController;

    @MockBean
    PartnerDashboardService partnerDashboardService;

    @Test
    public void testGetSfdcSalesReport() throws Exception {
        //Mockito.when(partnerDashboardService.getSfdcSalesReport(Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(new PartnerSfdcSalesResponse);
        Mockito.when(partnerDashboardService.getSfdcSalesReport(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(new PartnerSfdcSalesResponse());
        ResponseResource<PartnerSfdcSalesResponse> response = partnerDashboardController.getSfdcSalesReport(1, "id", "class");
        assertTrue(response != null);
        // response.getData().setCurrency("USD");
        //System.out.println(response.getData().getCurrency());
    }

    @Test
    public void testGetProducts() throws Exception {
        Mockito.when(partnerDashboardService.getProducts()).thenReturn(new ArrayList<>());
        ResponseResource<List<String>> response = partnerDashboardController.getProducts();
        assertTrue(response != null);
        System.out.println(response.getStatus());
        System.out.println(response.getData().toString());
    }

    @Test
    public void testGetRelayWareTrainings() throws Exception {
        Mockito.when(partnerDashboardService.getRelayWareTrainings(Mockito.anyInt())).thenReturn(new RelayWareTrainingBean());
        ResponseResource<RelayWareTrainingBean> response = partnerDashboardController.getRelayWareTrainings(1);
        assertTrue(response != null);
        System.out.println(response.getData().getPartnerId());
    }


}
