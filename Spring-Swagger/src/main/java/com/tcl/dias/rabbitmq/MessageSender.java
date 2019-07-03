package com.tcl.dias.rabbitmq;

import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tcl.dias.entity.Student;
@Component
public class MessageSender {

	@Value("${rabbitmq.custom.message}")
	String messageQueue;
	
	private final RabbitTemplate rabbitTemplate;

	@Autowired
	public MessageSender(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	//@Scheduled(fixedDelay = 3000L)
	public void sendMessage(Optional<Student> student) {
		rabbitTemplate.convertAndSend(messageQueue, student.get().getName()+"--"+student.get().getPassportNumber());
		
	}

}
