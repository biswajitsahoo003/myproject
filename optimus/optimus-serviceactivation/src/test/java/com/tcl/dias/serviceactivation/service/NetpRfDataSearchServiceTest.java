package com.tcl.dias.serviceactivation.service;


import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetpRfDataSearchServiceTest {

    @Autowired
    NetpRfDataSearchService netpRfDataSearchService;

    @Test
    public void netpSearchRadwinNodeTest() throws TclCommonException {
        System.out.println(netpRfDataSearchService.netpRadwinSearchNodeResult("A", "B"));
    }

    @Test
    public void netpSearchCambiumNodeTest() throws TclCommonException {
        System.out.println(netpRfDataSearchService.netpCambiumSearchNodeResult("A", "B"));
    }
}
