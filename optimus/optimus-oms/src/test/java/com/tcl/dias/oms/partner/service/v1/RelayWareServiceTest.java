package com.tcl.dias.oms.partner.service.v1;


import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareSessionBean;
import com.tcl.dias.oms.partner.thirdpartysystem.relayware.RelayWareServiceSessionResponse;


@RunWith(MockitoJUnitRunner.class)

public class RelayWareServiceTest {
    @Mock
    RestClientService restClientService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    RelayWareServiceSessionResponse relayWareServiceSessionResponse;

    @InjectMocks
    RelayWareService relayWareService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(RelayWareService.class, "authenticateUrl", "foo");
    }

    @Test
    public void testGetSessionId() {
        RelayWareSessionBean relayWareSessionBean = new RelayWareSessionBean();
        relayWareSessionBean.setSessionId(relayWareServiceSessionResponse.getSESSIONID());
        assertNotNull(relayWareSessionBean);
    }

    public void testCreateSession() {
//        MultiValueMap<String, String> formBody = when(Mockito.mock(Matchers.anyList()));
//        RestResponse response = restClientService.postWithUrlEncodedRequest(authenticateUrl, formBody, requestHeaders);
//
//        relayWareServiceSessionResponse = (RelayWareServiceSessionResponse) Utils.convertJsonToObject(response.getData(), RelayWareServiceSessionResponse.class);

    }

}
