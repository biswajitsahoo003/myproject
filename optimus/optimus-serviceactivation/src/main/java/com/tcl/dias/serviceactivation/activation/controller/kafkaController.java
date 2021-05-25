package com.tcl.dias.serviceactivation.activation.controller;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.utils.KafkaProducer;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.integratemux.beans.NodeInfoRequestBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/v1/kafka")
public class kafkaController {

    @Value("${kafka.pushnodeinfo.topic}")
    String pushNodeInfoTopic;
    @Autowired
    NetworkInventoryRepository networkInventoryRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/nodeinfo")
    public ResponseEntity postMessageToKafka(@RequestBody NodeInfoRequestBean nodeInfoRequestBean) throws TclCommonException {
        kafkaProducer.sendMessage(pushNodeInfoTopic, Utils.convertObjectToJson(nodeInfoRequestBean));
        saveInNetworkInventory(nodeInfoRequestBean);
        return new ResponseEntity("Message posted successfully", HttpStatus.OK);
    }

    //TODO:: move this to different class
    @Transactional
    private void saveInNetworkInventory(NodeInfoRequestBean nodeInfoRequestBean) throws TclCommonException {
        NetworkInventory networkInventory = new NetworkInventory();
        networkInventory.setRequestId(nodeInfoRequestBean.getRequestid());
        networkInventory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        networkInventory.setResponse(Utils.convertObjectToJson(Utils.convertObjectToJson(nodeInfoRequestBean)));
        networkInventory.setType("MUX INTEGRATION - PUSH NODE INFO");
        networkInventoryRepository.save(networkInventory);
    }

}
