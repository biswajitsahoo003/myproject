package com.tcl.dias.serviceactivation.service;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.feasibility.O2CFeasibilityRequest;
import com.tcl.dias.servicefulfillmentutils.beans.feasibility.O2CFeasibilityResponse;
import com.tcl.dias.servicefulfillmentutils.beans.feasibility.Results;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeasibilityService {

    @Autowired
    RestClientService restClientService;

    @Value("{o2c.feasibility.url}")
    String o2cFeasibilityUrl;

    public Map<String, String> getFeasibilityResponse(O2CFeasibilityRequest request) throws TclCommonException {
        RestResponse post = restClientService.post(o2cFeasibilityUrl, Utils.convertObjectToJson(request));
        if(post.getData()!=null && post.getStatus()== Status.SUCCESS) {
            O2CFeasibilityResponse o2CFeasibilityResponse = Utils.convertJsonToObject(post.getData(), O2CFeasibilityResponse.class);
             return o2CFeasibilityResponse.getResults()
                    .stream()
                    .collect(Collectors.toMap(Results::getBtsIp, Results::getNetworkFeasibilityCheck));
        }
        return null;
    }

    //TODO: Provide implementation
    public void triggerO2CFeasibility(String serviceCode){

    }
}
