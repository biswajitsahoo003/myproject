package com.tcl.dias.sap.controller;

import com.tcl.dias.sap.beans.GrnResponses;
import com.tcl.dias.sap.beans.GrnSapHanaResponse;
import com.tcl.dias.sap.beans.MinResponseBean;
import com.tcl.dias.sap.beans.PoStatusBean;
import com.tcl.dias.sap.beans.PoStatusResponseBean;
import com.tcl.dias.sap.service.SapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sap")
public class SapController {

    @Autowired
    SapService sapService;

    @PostMapping("/minstatus")
    public void getMinResponse(@RequestBody MinResponseBean minResponseBean) {
        sapService.sendMinStatus(minResponseBean);
    }


    @PostMapping("/postatus")
    public void getPOResponse(@RequestBody PoStatusResponseBean poStatusBean) {
        sapService.sendPoStatus(poStatusBean);
    }

    @PostMapping("/grnstatus")
    public void getGrnResponse(@RequestBody GrnSapHanaResponse grnResponses) {
        sapService.sendGrnStatus(grnResponses);
    }
}
