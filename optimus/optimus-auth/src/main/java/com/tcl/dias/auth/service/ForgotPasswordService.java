package com.tcl.dias.auth.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.common.keycloack.bean.KeycloakUserResponseBean;
import com.tcl.dias.auth.beans.ForgotPasswordRequest;
import com.tcl.dias.auth.beans.ForgotPasswordResponse;
import com.tcl.dias.auth.beans.ResetPasswordRequest;
import com.tcl.dias.auth.constants.Constants;
import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.auth.constants.ForgotPasswordConstant;
import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;
import com.tcl.dias.auth.redis.service.ResetUserInfoService;
import com.tcl.dias.auth.redis.service.UserToResetTokenService;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.TokenGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ForgotPasswordService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ForgotPasswordService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordService.class);

	@Value("${app.host}")
	String domainUrl;

	@Value("${auth.reset.path}")
	String path;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${reset.expiry.time}")
	long resetTokenTimeOut;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.bcc}")
	String[] bcc;

	@Value("${notification.mail.template}")
	String resetTemplateId;

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	ResetUserInfoService resetUserInfoService;

	@Autowired
	UserToResetTokenService userToResetTokenService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MQUtils mqUtils;

	/**
	 * processForgotPassword- This method process the forgot password it gets the
	 * email as the input and generates an reset url The reset url will have an
	 * expiry and can be only accessed by token
	 * 
	 * @param forgotPasswordRequest
	 * @return ForgotPasswordResponse
	 * @throws TclCommonException
	 */
	public ForgotPasswordResponse processForgotPassword(ForgotPasswordRequest forgotPasswordRequest)
			throws TclCommonException {
		ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse(Status.SUCCESS.toString());
		try {
			if (validateUser(forgotPasswordRequest)) {
				String userId = forgotPasswordRequest.getEmailId();
				LOGGER.info("user email id : {}", userId);
				String userIdentifier = authenticationService.getUserDetailById(userId);
				if (StringUtils.isNotBlank(userIdentifier)) {
					String firstName = "User";
					com.tcl.dias.oms.entity.entities.User userEntity = userRepository.findByEmailIdAndStatus(userId, 1);
					if (userEntity != null) {
						firstName = userEntity.getFirstName();
					}
					String resetToken = TokenGenerator.create();
					persistResetToken(resetToken, userId);
					String resetUrl = constructResetUrl(resetToken);

					String notificationBody = constructMailNotificationObject(forgotPasswordRequest.getEmailId(),
							ForgotPasswordConstant.RESET_PASS_NOTIFICATION_SUBJECT, firstName, resetUrl,
							resetTemplateId);
					LOGGER.info("MDC Filter token value in before Queue call processForgotPassword {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mqUtils.send(notificationMailQueue, notificationBody);
				} else {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.FORGOT_PASWD_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return forgotPasswordResponse;
	}

	/**
	 * 
	 * This method will be used to reset the password , given the reset Token ,
	 * validation and reset password
	 * 
	 * @param resetPasswordRequest
	 * @param resetToken
	 * @return String
	 * @throws TclCommonException
	 */
	public String processResetPassword(ResetPasswordRequest resetPasswordRequest, String resetToken)
			throws TclCommonException {
		String status = Status.SUCCESS.toString();
		try {
			if (validateResetToken(resetPasswordRequest, resetToken)) {
				ResetUserInfoBean resetTokenUserInfo = resetUserInfoService.find(resetToken);
				if (resetTokenUserInfo != null) {
					String userId = resetTokenUserInfo.getUserId();
					status = authenticationService.resetPassword(userId, resetPasswordRequest.getPassword(),
							resetPasswordRequest.getPassword());
					resetUserInfoService.delete(resetToken);
					userToResetTokenService.delete(userId, resetToken);
				} else {
					throw new TclCommonException(ExceptionConstants.RESET_PASWD_TOKEN_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.RESET_PASWD_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
		return status;
	}

	/**
	 * This method is used for validating the resetToken
	 * 
	 * @param resetToken
	 * @return String
	 * @throws TclCommonException
	 */
	public String processValidateResetToken(String resetToken) throws TclCommonException {
		String status = Status.SUCCESS.toString();
		try {
			if (StringUtils.isNotEmpty(resetToken) && resetUserInfoService.find(resetToken) != null) {
				ResetUserInfoBean resetTokenUserInfo = resetUserInfoService.find(resetToken);
				if (resetTokenUserInfo == null) {
					throw new TclCommonException(ExceptionConstants.RESET_PASWD_TOKEN_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.RESET_PASWD_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
		return status;
	}

	/**
	 * This method is to check the user is valid or not
	 * 
	 * @param forgotPasswordRequest
	 * @return boolean
	 * @throws TclCommonException
	 */
	private boolean validateUser(ForgotPasswordRequest forgotPasswordRequest){
		boolean status = true;
		if (forgotPasswordRequest == null || StringUtils.isEmpty(forgotPasswordRequest.getEmailId())) {
			return false;
		}
		if (forgotPasswordRequest.getEmailId().toLowerCase().contains(Constants.TATA_COM_DOMAIN)) {
			return false;
		}
		return status;
	}

	/**
	 * This method is used to validate the token value
	 * 
	 * @param resetPasswordRequest
	 * @param resetToken
	 * @return boolean
	 */
	private boolean validateResetToken(ResetPasswordRequest resetPasswordRequest, String resetToken) {
		boolean status = true;
		if (StringUtils.isEmpty(resetToken) || resetPasswordRequest == null
				|| StringUtils.isEmpty(resetPasswordRequest.getPassword())) {
			return false;
		}
		return status;
	}

	/**
	 * This method is used to construct the url
	 * 
	 * @param resetToken
	 * @return String
	 */
	private String constructResetUrl(String resetToken) {
		StringBuilder resetUrl = new StringBuilder(domainUrl).append(path).append(CommonConstants.QUESTION_MARK)
				.append(ForgotPasswordConstant.TOKEN).append(CommonConstants.EQUAL).append(resetToken);
		return resetUrl.toString();
	}

	/**
	 * This method is used to save the user
	 * 
	 * @param resetToken
	 * @param userId
	 */
	private void persistResetToken(String resetToken, String userId) {
		Set<Object> userToResetTokenBean = userToResetTokenService.find(userId);
		if (userToResetTokenBean != null && !userToResetTokenBean.isEmpty()) {
			for (Object resetTok : userToResetTokenBean) {
				resetUserInfoService.delete((String) resetTok);
				userToResetTokenService.delete(userId, (String) resetTok);
			}
		}
		ResetUserInfoBean resetUserInfo = new ResetUserInfoBean();
		resetUserInfo.setResetToken(resetToken);
		resetUserInfo.setUserId(userId);
		resetUserInfo.setCreatedTime(new Timestamp(new Date().getTime()));
		resetUserInfoService.save(resetUserInfo, resetTokenTimeOut);
		userToResetTokenService.save(resetUserInfo, resetTokenTimeOut);
	}

	/**
	 * This method is used to construct the content for the mail to be sent to the
	 * user
	 * 
	 * @param toAddress
	 * @param subject
	 * @param message
	 * @return String
	 * @throws TclCommonException
	 */
	private String constructMailNotificationObject(String toAddress, String subject, String name, String resetUrl,
			String templateId) throws TclCommonException {
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("reseturl", resetUrl);
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		if (bcc.length > 0)
			mailNotificationRequest.setBcc(Arrays.asList(bcc));
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject(subject);
		mailNotificationRequest.setTemplateId(templateId);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(toAddress);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setVariable(map);
		return Utils.convertObjectToJson(mailNotificationRequest);
	}

}
