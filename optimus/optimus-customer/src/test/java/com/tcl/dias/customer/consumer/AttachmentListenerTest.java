package com.tcl.dias.customer.consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class has the test case for the attachment queues
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttachmentListenerTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${file.download.queue}")
	private String queue;

	@Test
	public void testAttacmentIdPath() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object response = rabbitTemplate.convertSendAndReceive(queue, "38");
		//String path = mapper.readValue((String) response, String.class);
		assertNotNull(response);
	}

	@Test(expected = NullPointerException.class)
	public void testAttacmentIdPathFailure() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object response = rabbitTemplate.convertSendAndReceive(queue, (String) null);
		//String path = mapper.readValue((String) response, String.class);
		assertNull(response);
	}

	@Test
	public void testAttacmentIdPathFailure1() throws JsonParseException, JsonMappingException, IOException {
		Object response = rabbitTemplate.convertSendAndReceive(queue, "");
		assertNull(response);
	}

}
