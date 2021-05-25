package com.tcl.dias.adobesign.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.adobesign.api.beans.AgreementInfo;
import com.tcl.dias.adobesign.api.beans.FileInfo;
import com.tcl.dias.adobesign.api.beans.MemberInfo;
import com.tcl.dias.adobesign.api.beans.ParticipantSetsInfo;
import com.tcl.dias.adobesign.api.service.AdobeApiService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the AdobeApiTestService.java class.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdobeApiTestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdobeApiTestService.class);

	@Autowired
	AdobeApiService adobeApiService;

	//@Test
	public void testTransientDocument() {
		try {
			File file = new File("D://test.pdf");
			byte[] bytes = Files.readAllBytes(file.toPath());
			String transientDocumentId = adobeApiService.processTransientDocument(bytes, file.getName());
			LOGGER.info("Transient Document Id Token {}", transientDocumentId);
		} catch (TclCommonException | IOException e) {
			LOGGER.error("Error in transientDocumentId", e);
		}
	}

	@Test
	public void testAgreement() {
		try {
			File file = new File("D://test.pdf");
			byte[] bytes = Files.readAllBytes(file.toPath());
			String transientDocumentId = adobeApiService.processTransientDocument(bytes, file.getName());
			LOGGER.info("Transient Document Id Token {}", transientDocumentId);
			AgreementInfo agreementInfo = new AgreementInfo();
			List<FileInfo> fileInfos=new ArrayList<>();
			FileInfo fileInfo=new FileInfo();
			fileInfos.add(fileInfo);
			agreementInfo.setFileInfos(fileInfos);
			fileInfo.setTransientDocumentId(transientDocumentId);
			agreementInfo.setName("Customer Email");
			List<ParticipantSetsInfo> participantSetsInfo = new ArrayList<>();
			ParticipantSetsInfo customer=new ParticipantSetsInfo();
			participantSetsInfo.add(customer);
			agreementInfo.setParticipantSetsInfo(participantSetsInfo);
			customer.setOrder(1);
			List<MemberInfo> memberInfos = new ArrayList<>();
			MemberInfo memberInfo=new MemberInfo();
			memberInfos.add(memberInfo);
			customer.setMemberInfos(memberInfos);
			customer.setRole("SIGNER");
			memberInfo.setEmail("rajmanojkr@gmail.com");
			agreementInfo.setSignatureType("ESIGN");
			agreementInfo.setState("IN_PROCESS");
			String id = adobeApiService.processAgreement(agreementInfo);
			LOGGER.info("Unique Id Token {}", id);
		} catch (TclCommonException | IOException e) {
			LOGGER.error("Error in transientDocumentId", e);
		}
	}
}
