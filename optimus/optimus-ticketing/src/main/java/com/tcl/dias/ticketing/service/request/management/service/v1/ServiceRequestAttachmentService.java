package com.tcl.dias.ticketing.service.request.management.service.v1;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.constants.ExceptionConstants;
import com.tcl.dias.response.beans.AttachmentInfoBean;
import com.tcl.dias.ticketing.request.AttachmentRequest;
import com.tcl.dias.ticketing.response.AttachmentResponse;
import com.tcl.dias.ticketing.response.ErrorResponseDetails;
import com.tcl.dias.ticketing.service.category.service.v1.TicketingAbstractService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ServiceRequestAttachmentService.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceRequestAttachmentService extends TicketingAbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestAttachmentService.class);

	@Value("${service.request.management.url}")
	String serviceRequestManagementUrl;

	@Value("${app.id}")
	String appId;

	@Value("${secret.key}")
	String appSecret;

	@Autowired
	RestClientService restClientService;

	/**
	 * @author vivek getAttachment
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public AttachmentResponse getAttachment(String ticketId) throws TclCommonException {
		if (Objects.isNull(ticketId))
			throw new TclCommonException(ExceptionConstants.TICKETID_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		AttachmentResponse attachmentResponse = null;
		RestResponse response = restClientService.getWithQueryParam(
				getAttachmentUrl(serviceRequestManagementUrl, ticketId) + "/" + "attachments", null,
				getBasicAuth(appId, appSecret, getHeader(), null));
		LOGGER.info("getAttachment response  {}", response);
		if (response.getStatus() == Status.SUCCESS) {
			return (AttachmentResponse) Utils.convertJsonToObject(response.getData(), AttachmentResponse.class);
		} else {
			attachmentResponse = new AttachmentResponse();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			attachmentResponse.setStatus(errorDetails.getStatus());
			attachmentResponse.setMessage(errorDetails.getMessage());

		}

		return attachmentResponse;
	}

	/**
	 * getAttachmentDetails
	 * 
	 * @author vivek
	 * @param ticketId
	 * @param attachmentId
	 * @param httpResponse 
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 * @throws ParseException
	 */
	public AttachmentInfoBean getAttachmentDetails(String ticketId, String attachmentId,
			HttpServletResponse httpResponse) throws TclCommonException, IOException {
		AttachmentInfoBean attachmentInfoBean = null;
		if (Objects.isNull(ticketId))
			throw new TclCommonException(ExceptionConstants.TICKETID_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		if (Objects.isNull(attachmentId))
			throw new TclCommonException(ExceptionConstants.ATTACHMENTID_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		RestResponse response = restClientService.getWithQueryParam(
				getAttachmentDetailsUrl(serviceRequestManagementUrl, ticketId, attachmentId), null,
				getBasicAuth(appId, appSecret, getHeader(), null));
		LOGGER.info("getAttachment Details response  {}", response);
		if (response.getStatus() == Status.SUCCESS) {
			attachmentInfoBean = (AttachmentInfoBean) Utils.convertJsonToObject(response.getData(),
					AttachmentInfoBean.class);

			byte[] decodedByte = Base64.decodeBase64(attachmentInfoBean.getPayload());
			attachmentInfoBean.setImage(decodedByte);
			httpResponse.setContentType("application/octet-stream");
			httpResponse.setContentLength(decodedByte.length);
			httpResponse.setHeader("Content-disposition", "inline;filename="+attachmentInfoBean.getName());
			httpResponse.setHeader("Cache-Control", "cache");
			httpResponse.addHeader("Cache-Control", "must-revalidate");
			httpResponse.setHeader("Pragma", "public");
			ServletOutputStream out = httpResponse.getOutputStream();
			out.write(decodedByte);
			out.flush();

			return attachmentInfoBean;
		} else {

			attachmentInfoBean = new AttachmentInfoBean();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			attachmentInfoBean.setStatus(errorDetails.getStatus());
			attachmentInfoBean.setMessage(errorDetails.getMessage());

		}

		return attachmentInfoBean;
	}

	/**
	 * @author vivek updateAttachmentDetails
	 * @param request
	 * @param ticketId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws ParseException
	 */
	public AttachmentInfoBean updateAttachmentDetails(String name, String type, String ticketId, MultipartFile file)
			throws TclCommonException, IOException {
		AttachmentInfoBean attachmentInfoBean = null;
		AttachmentRequest attachmentRequest = new AttachmentRequest();
		if (Objects.isNull(ticketId) || (Objects.isNull(file)))
			throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		attachmentRequest.setName(name);
		attachmentRequest.setType(type);
		attachmentRequest.setPayload(Utils.encodeFileToBase64Binary(file));
		RestResponse response = restClientService.postWithClientRepo(
				getAttachmentUrl(serviceRequestManagementUrl, ticketId) + "/" + "attachments",
				Utils.convertObjectToJson(attachmentRequest), getBasicAuth(appId, appSecret, getHeader(), null));
		LOGGER.info("update updateAttachmentDetails response  {}", response);
		if (response.getStatus() == Status.SUCCESS) {
			return (AttachmentInfoBean) Utils.convertJsonToObject(response.getData(), AttachmentInfoBean.class);
		} else {
			attachmentInfoBean = new AttachmentInfoBean();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			attachmentInfoBean.setStatus(errorDetails.getStatus());
			attachmentInfoBean.setMessage(errorDetails.getMessage());

		}

		return attachmentInfoBean;

	}

}
