package com.tcl.dias.oms.consumer;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.npl.service.v1.NplQuoteService;

@Service
@Transactional
public class NplQuoteListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NplQuoteListener.class);

	@Autowired
	NplQuoteService nplQuoteService;
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.fetch.mf.response.in.oms.mq}")})
    public Map<String,String> getSiteDetailForNplLinkId(String request) {
        try {
        Object obj = new JSONParser().parse(request); 
        JSONObject jo = (JSONObject) obj;
        String linkId=(String) jo.get("request");
        
        LOGGER.info("request="+linkId);
         return nplQuoteService.getSiteDetailForNplBasedOnLinkId(linkId);
        } catch (Exception e) {
            LOGGER.error("error in Site Detail for npl", e);
        }
        return new HashMap<>();
    }

}
