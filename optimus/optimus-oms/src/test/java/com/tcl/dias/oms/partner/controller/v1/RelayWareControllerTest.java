package com.tcl.dias.oms.partner.controller.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareSessionBean;
import com.tcl.dias.oms.partner.service.v1.RelayWareService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class RelayWareControllerTest {

    @Mock
    RelayWareService relayWareService;

    @InjectMocks
    RelayWareController relayWareController;

    @Test
    public void testSessionIdNotNull() throws TclCommonException {

        RelayWareSessionBean relayWareSessionBean = new RelayWareSessionBean();
        relayWareSessionBean.setSessionId("ABCD");
        Mockito.doReturn(relayWareSessionBean).when(relayWareService).getSessionId();
        assertNotNull(relayWareController.getSessionId());

    }

    @Test
    public void testSessionId() throws TclCommonException {


        RelayWareSessionBean relayWareSessionBean = new RelayWareSessionBean();
        relayWareSessionBean.setSessionId("ABCD");
        ResponseResource<RelayWareSessionBean> var = relayWareController.getSessionId();
        Mockito.doReturn(relayWareSessionBean).when(relayWareService).getSessionId();
        assertEquals(var.getMessage(), relayWareController.getSessionId().getMessage());
        //assertThat(relayWareController.getSessionId().getData().getSessionId()).isEqualToComparingFieldByFieldRecursively(var);

    }

    @Test
    public void testSessionIdResponse() throws TclCommonException {


        RelayWareSessionBean relayWareSessionBean = new RelayWareSessionBean();
        relayWareSessionBean.setSessionId("ABCD");
        ResponseResource<RelayWareSessionBean> var = relayWareController.getSessionId();
        assertTrue(var != null && var.getStatus() == Status.SUCCESS);
        //assertThat(relayWareController.getSessionId().getData().getSessionId()).isEqualToComparingFieldByFieldRecursively(var);

    }

}
