package com.tcl.dias.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.tcl.dias.entity.Student;

import org.springframework.amqp.rabbit.annotation.Queue;
@Component
public class MessageListner {

	 
    @RabbitListener(queuesToDeclare={ @Queue("${rabbitmq.custom.message}") })
    public void receiveMessage(String message) {
    	
    	System.out.println("RabbitMQ message--"+message);
    }
 

}
