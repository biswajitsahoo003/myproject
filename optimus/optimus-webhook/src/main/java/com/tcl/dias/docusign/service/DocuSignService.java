package com.tcl.dias.docusign.service;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

@Service
public class DocuSignService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocuSignService.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${mq.docusign.notification.queue}")
	String docusignNotificationQueue;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * processWebHook
	 */
	public void processWebHook(String responseBody) {
		LOGGER.info("Response Received {}", responseBody);
		LOGGER.info("Data received from DS Connect: {}", responseBody);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document xml = builder.parse(new InputSource(new StringReader(responseBody)));
			xml.getDocumentElement().normalize();
			LOGGER.info("Connect data parsed!");
			Element envelopeStatus = (Element) xml.getElementsByTagName("EnvelopeStatus").item(0);
			String envelopeId = envelopeStatus.getElementsByTagName("EnvelopeID").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("envelopeId= {}", envelopeId);
			String eStatus = envelopeStatus.getElementsByTagName("Status").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("envelopeStatus= {}", eStatus);
			String timeGenerated = envelopeStatus.getElementsByTagName("TimeGenerated").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("timeGenerated= {}", timeGenerated);
			Element documentStatuses = (Element) envelopeStatus.getElementsByTagName("DocumentStatuses").item(0);
			Element documentStatus = (Element) documentStatuses.getElementsByTagName("DocumentStatus").item(0);
			String documentId = documentStatus.getElementsByTagName("ID").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("documentId= {}", documentId);
			saveJdbcRequest(responseBody, envelopeId);
			JSONObject xmlJSONObj = XML.toJSONObject(responseBody);
			LOGGER.info("Json Obj for envelopeId {} = {}", envelopeId, xmlJSONObj);
			send(docusignNotificationQueue, xmlJSONObj.toString());
		} catch (Exception e) {
			LOGGER.error("Error in Response", e);
		}

	}

	/**
	 * This is used for asynchronous publish
	 * 
	 * @param queue
	 * @param request
	 */
	public void send(String queue, String request) {
		rabbitTemplate.convertAndSend(queue, request);
	}

	private Boolean saveJdbcRequest(String docusignRequest, String envelopeId) {
		try {
			String query = "insert into docusign_notification_hook (docusign_notification_request,status,created_time,envelope_id) values(?,?,?,?)";
			return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException {
					ps.setString(2, "INPROGRESS");
					ps.setString(1, docusignRequest);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					ps.setTimestamp(3, timestamp);
					ps.setString(4, envelopeId);

					return ps.execute();

				}
			});
		} catch (Exception e) {
			LOGGER.error("ERROR in saving the status", e);
		}
		return false;
	}
	
	public Boolean updateJdbcRequest(String status, String envelopeId,String mdcToken) {
		try {
			LOGGER.info("MDC-TOKEN {} :: Input for update request {},{}",mdcToken,status,envelopeId);
			String query = "update docusign_notification_hook set status=?,updated_time=?,mdc_token=? where envelope_id= ?";
			return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException {
					ps.setString(1, status);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					ps.setTimestamp(2, timestamp);
					ps.setString(3, mdcToken);
					ps.setString(4, envelopeId);
					return ps.execute();
				}
			});
		} catch (Exception e) {
			LOGGER.error("ERROR in updateJdbcRequest the status", e);
		}
		return false;
	}

}
