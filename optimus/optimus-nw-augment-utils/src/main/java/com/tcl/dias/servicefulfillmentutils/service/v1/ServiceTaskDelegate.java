package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceTaskDelegate implements JavaDelegate {

    @Autowired
    TaskRepository taskRepository;

    @Value(value = "${app.cramer.baseUrl}")
    private String cramerBaseUrl;



    @Override
    public void execute(DelegateExecution delegateExecution) {
        try{

            Task task = taskRepository.findByWfTaskId(delegateExecution.getId());
            String orderCode = task.getOrderCode();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> jsonNodeResp = restTemplate.getForEntity(String.format("%s/CreateObject?orderId=%s&orderType=EOR&technology=TF", cramerBaseUrl, orderCode), JsonNode.class);

            if(jsonNodeResp.getStatusCodeValue()==200) {
                JsonNode jsonNode = jsonNodeResp.getBody();
                System.out.println(" Cramer response " + jsonNode.toString());
                // TODO store the response from cramer.
                //TODO prepare for next task/activit.
            } else {
                throw new RuntimeException("Error in getting cramer data.");
            }

            delegateExecution.setVariable("isError", false);
        }catch (Exception ex) {
            ex.printStackTrace();
            Integer numRetries = 0;
            if(delegateExecution.getVariable("numRetries")!=null) {
                numRetries = (Integer) delegateExecution.getVariable("numRetries");
                numRetries++;
            }

            delegateExecution.setVariable("numRetries", numRetries);
            delegateExecution.setVariable("isError", true);
        }

    }
}
