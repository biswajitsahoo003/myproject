package com.tcl.dias.servicefulfillmentutils.service.v1;
/*
import com.tcl.dias.common.utils.RestClientService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CreateDeviceInCramerTask implements JavaDelegate {

    RestTemplate restTemplate = new RestTemplate();


    @Override
    public void execute(DelegateExecution delegateExecution) {
        try{

            Client client = ClientBuilder.newClient();
            Response cramerRes = client.target(cramerUrl)
                    .request()
                    .accept("application/json")
                    .post(Entity.json(cramerInputEntity));

            if(cramerRes.getStatus()>=200 && cramerRes.getStatus()<300) {
                //all good
            } else {
                throw new RuntimeException("what went wrong.");
            }
            delegateExecution.setVariable("isError", false);


        }catch (Exception ex) {
            delegateExecution.setVariable("isError", true);
            Integer numRetry = null;
            if(delegateExecution.getVariable("numRetry")!=null) {
                numRetry = (Integer) delegateExecution.getVariable("numRetry");
                numRetry++;
            } else {
                numRetry = 0;
            }
            delegateExecution.setVariable("numRetry", numRetry);
        }
    }

}*/
