package com.tcl.dias.oms.partner.service.v1;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.oms.partner.beans.PartnerSfdcSalesResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartnerDashboardServiceTest {

    @Autowired
    PartnerDashboardService partnerDashboardService;


    @Test
    public void getSfdcSalesReport() {
        PartnerDashboardService service = mock(PartnerDashboardService.class);
        when(service.getSfdcSalesReport(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(new PartnerSfdcSalesResponse());
        PartnerSfdcSalesResponse response = partnerDashboardService.getSfdcSalesReport(1, "id", "class");
        assertTrue(response != null);
        System.out.println(response.getCurrency());

    }

    @Test
    public void getPartnerCUIDs() {
    }

    @Test
    public void getRelayWareTrainings() {
    }

    @Test
    public void getProducts() {
    }
}
