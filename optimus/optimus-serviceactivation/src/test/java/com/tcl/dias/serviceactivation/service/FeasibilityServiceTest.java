package com.tcl.dias.serviceactivation.service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.feasibility.O2CFeasibilityRequest;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeasibilityServiceTest {

    @Autowired
    FeasibilityService feasibilityService;

    @Test
    public void getFeasibilityResponse() throws TclCommonException {
        O2CFeasibilityRequest o2CFeasibilityRequest = Utils.convertJsonToObject("{\n" +
                "  \"input_data\": [\n" +
                "    {\n" +
                "      \"BW_mbps\": 2,\n" +
                "                  \"sector_id\" : \"S01:TANQMD0912\",\n" +
                "                  \"bts_id\" : \"ABC\",\n" +
                "                  \"in_solution_type\" : \"UBR_PMP\",\n" +
                "                  \"bts_ip\" : \"1.11.11\",\n" +
                "      \"site_id\": \"primary_site11\",\n" +
                "      \"account_id_with_18_digit\": \"0012000000GSDzVAAX\",\n" +
                "      \"latitude_final\": 28.558312,\n" +
                "      \"longitude_final\": 77.094343\n" +
                "    }\n" +
                "  ]\n" +
                "}\n", O2CFeasibilityRequest.class);
        feasibilityService.getFeasibilityResponse(o2CFeasibilityRequest);
    }
}