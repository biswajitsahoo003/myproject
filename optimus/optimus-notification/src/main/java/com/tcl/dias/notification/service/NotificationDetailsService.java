package com.tcl.dias.notification.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.NotificationActionBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserNotificationSettingsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.notification.beans.NotificationActionDetailsBean;
import com.tcl.dias.notification.beans.ProductNotificationActionDetails;
import com.tcl.dias.notification.beans.UserNotificationSubscriptionDetailsBean;
import com.tcl.dias.notification.constants.ExceptionConstants;
import com.tcl.dias.notification.entity.entities.NotificationAction;
import com.tcl.dias.notification.entity.entities.UserNotificationSetting;
import com.tcl.dias.notification.entity.repository.NotificationActionRepository;
import com.tcl.dias.notification.entity.repository.UserNotificationSettingsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the NotificationDetailsService.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class NotificationDetailsService {

	@Autowired
	UserNotificationSettingsRepository userNotificationSettingsRepository;

	@Autowired
	NotificationActionRepository notificationActionRepository;

	/**
	 * 
	 * Save user Notification details for the user
	 * 
	 * @author ANANDHI VIJAY
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	public String constructUserNotificationSettingForUserAndSave(String userId) throws TclCommonException {
		List<UserNotificationSetting> userNotificationSettings = new ArrayList<>();
		if (userId == null) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			List<NotificationAction> notificationActions = notificationActionRepository.findAll();
			if (notificationActions != null && !notificationActions.isEmpty()) {
				notificationActions.stream().forEach(notificationAction -> userNotificationSettings
						.add(constructUserNotificationSettingsForUser(userId, notificationAction)));
			}
			if (!userNotificationSettings.isEmpty()) {
				userNotificationSettingsRepository.saveAll(userNotificationSettings);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private static NotificationAction convertNotificationActionBeanToNotificationAction(
			NotificationActionBean notificationActionBean) {
		NotificationAction notificationAction = new NotificationAction();
		notificationAction.setId(notificationActionBean.getActionId());
		notificationAction.setCode(notificationActionBean.getActionCode());
		notificationAction.setErfPrdCatalogProductName(notificationActionBean.getProductName());
		notificationAction.setIsActive(notificationActionBean.getIsActive());
		notificationAction.setName(notificationActionBean.getActionName());
		return notificationAction;
	}

	private static NotificationActionBean convertNotificationActionToNotificationActionBean(
			NotificationAction notificationAction, Boolean isSubscribed) {
		NotificationActionBean notificationActionBean = new NotificationActionBean();
		notificationActionBean.setActionId(notificationAction.getId());
		notificationActionBean.setActionName(notificationAction.getName());
		notificationActionBean.setActionCode(notificationAction.getCode());
		notificationActionBean.setProductName(notificationAction.getErfPrdCatalogProductName());
		notificationActionBean.setIsActive(notificationAction.getIsActive());
		notificationActionBean.setIsSubscribed(isSubscribed);
		return notificationActionBean;
	}

	private static UserNotificationSetting constructUserNotificationSettingsForUser(String userId,
			NotificationAction notificationAction) {
		UserNotificationSetting userNotificationSetting = new UserNotificationSetting();
		userNotificationSetting.setCreatedBy(userId);
		userNotificationSetting.setCreatedTime(new Timestamp(new Date().getTime()));
		userNotificationSetting.setNotificationAction(notificationAction);
		userNotificationSetting.setUserid(userId);
		userNotificationSetting.setIsActive(CommonConstants.ACTIVE);
		userNotificationSetting.setUpdatedTime(new Timestamp(new Date().getTime()));
		userNotificationSetting.setUpdatedBy(userId);
		userNotificationSetting.setIsNotificationEnabled(CommonConstants.ACTIVE);
		userNotificationSetting.setNotificationType("email");
		return userNotificationSetting;
	}

	/**
	 * @author ANANDHI VIJAY Get Notification Subscription details for the user
	 * @return
	 * @throws TclCommonException
	 */
	public List<NotificationActionBean> getNotificationSubscriptionDetailsByUserId(String userId)
			throws TclCommonException {
		List<NotificationActionBean> notificationActionBeans = new ArrayList<>();
		List<Integer> notificationActions = new ArrayList<>();
		if (userId == null) {
			throw new TclCommonException("Invalid input");
		}
		try {
			List<UserNotificationSetting> userNotificationSettings = userNotificationSettingsRepository
					.findByUserid(userId);
			if (userNotificationSettings == null || userNotificationSettings.isEmpty()) {
				constructUserNotificationSettingForUserAndSave(userId);
				userNotificationSettings = userNotificationSettingsRepository.findByUserid(userId);
			}
			userNotificationSettings.stream().forEach(userNotificationSetting -> {
				if (userNotificationSetting.getNotificationAction() != null
						&& userNotificationSetting.getNotificationAction().getId() != null) {
					notificationActions.add(userNotificationSetting.getNotificationAction().getId());
					notificationActionBeans.add(convertNotificationActionToNotificationActionBean(
							userNotificationSetting.getNotificationAction(),
							userNotificationSetting.getIsNotificationEnabled().equals(CommonConstants.ACTIVE)));
				}
			});
			mapUnsubscribedNotificationAction(notificationActionBeans, notificationActions);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return notificationActionBeans;
	}

	private void mapUnsubscribedNotificationAction(List<NotificationActionBean> notificationActionBeans,
			List<Integer> notificationActions) {
		List<NotificationAction> notificationActions2 = notificationActionRepository.findByIdNotIn(notificationActions);
		if (notificationActions2 != null && !notificationActions2.isEmpty()) {
			notificationActions2.stream().forEach(notificationAction -> notificationActionBeans
					.add(convertNotificationActionToNotificationActionBean(notificationAction, false)));
		}
	}

	/**
	 * @author ANANDHI VIJAY Update Notification Subscription details for the user
	 * @return
	 * @throws TclCommonException
	 */
	public String updateNotificationSubscriptionDetailsForTheUser(
			UserNotificationSettingsBean userNotificationSettingsBean) throws TclCommonException {
		if (userNotificationSettingsBean == null || userNotificationSettingsBean.getUserId() == null
				|| userNotificationSettingsBean.getNotificationActionDetails() == null) {
			throw new TclCommonException("Invalid Input");
		}
		try {
			List<UserNotificationSetting> userNotificationSettings = new ArrayList<>();
			userNotificationSettingsBean.getNotificationActionDetails().stream().forEach(notificationActionBean -> {
				if (notificationActionBean.getActionId() != null) {
					UserNotificationSetting userNotificationSetting = userNotificationSettingsRepository
							.findByUseridAndNotificationAction_Id(userNotificationSettingsBean.getUserId(),
									notificationActionBean.getActionId());
					userNotificationSettings.add(constructUserNotificationSettingsOnUpdate(
							userNotificationSettingsBean.getUserId(), notificationActionBean, userNotificationSetting));
				}
			});
			if (!userNotificationSettings.isEmpty()) {
				userNotificationSettingsRepository.saveAll(userNotificationSettings);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private UserNotificationSetting constructUserNotificationSettingsOnUpdate(String userId,
			NotificationActionBean notificationActionBean, UserNotificationSetting userNotificationSetting) {
		if (userNotificationSetting == null) {
			userNotificationSetting = new UserNotificationSetting();
			userNotificationSetting.setCreatedBy(userId);
			userNotificationSetting.setCreatedTime(new Timestamp(new Date().getTime()));
			userNotificationSetting.setUserid(userId);
			userNotificationSetting.setNotificationType("email");
			userNotificationSetting
					.setNotificationAction(convertNotificationActionBeanToNotificationAction(notificationActionBean));
		}
		userNotificationSetting
				.setIsActive(notificationActionBean.getIsSubscribed().equals(true) ? CommonConstants.ACTIVE
						: CommonConstants.INACTIVE);
		userNotificationSetting
				.setIsNotificationEnabled(notificationActionBean.getIsSubscribed().equals(true) ? CommonConstants.ACTIVE
						: CommonConstants.INACTIVE);
		userNotificationSetting.setUpdatedBy(userId);
		userNotificationSetting.setUpdatedTime(new Timestamp(new Date().getTime()));
		return userNotificationSetting;
	}

	/**
	 * 
	 * This method creates a new Notification Action
	 * 
	 * @author ANANDHI VIJAY
	 * 
	 * @param notificationActionBean
	 * @return
	 * @throws TclCommonException
	 */
	public String createAndReturnNotificationAction(NotificationActionBean notificationActionBean)
			throws TclCommonException {
		if (notificationActionBean == null || notificationActionBean.getActionName() == null
				|| notificationActionBean.getProductName() == null) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		NotificationAction notificationAction = notificationActionRepository
				.saveAndFlush(constructNotificationActionForAddNotificationAction(notificationActionBean));
		if (notificationAction.getId() != null) {
			return ResponseResource.RES_SUCCESS;
		}
		return ResponseResource.RES_FAILURE;
	}

	private static NotificationAction constructNotificationActionForAddNotificationAction(
			NotificationActionBean notificationActionBean) {
		NotificationAction notificationAction = new NotificationAction();
		notificationAction.setCode(Utils.generateUid());
		notificationAction.setErfPrdCatalogProductName(notificationActionBean.getProductName());
		notificationAction.setIsActive(CommonConstants.ACTIVE);
		notificationAction.setIsDefaultUserNotificationAction(CommonConstants.INACTIVE);
		notificationAction.setName(notificationActionBean.getActionName());
		return notificationAction;
	}

	/**
	 * @author ANANDHI VIJAY Get User Notification Subscription details
	 * @return
	 * @throws TclCommonException
	 */
	public UserNotificationSubscriptionDetailsBean getUserNotificationSubscriptionDetails() throws TclCommonException {
		UserNotificationSubscriptionDetailsBean userNotificationSubscriptionDetailsBean = new UserNotificationSubscriptionDetailsBean();
		List<UserNotificationSetting> userNotificationSettings = getUserNotificationSettingsForTheUser(
				CommonConstants.ACTIVE);
		if (userNotificationSettings == null || userNotificationSettings.isEmpty()) {
			constructUserNotificationSettingForUserAndSave(Utils.getSource());
			userNotificationSettings = getUserNotificationSettingsForTheUser(CommonConstants.ACTIVE);
		}
		if (userNotificationSettings != null) {
			List<NotificationAction> notificationActions = notificationActionRepository
					.findByIsActive(CommonConstants.ACTIVE);
			List<ProductNotificationActionDetails> productNotificationActionDetails = new ArrayList<>();
			productNotificationActionDetails.add(getProductNotificationActionByProductName(notificationActions,
					userNotificationSettings, CommonConstants.IAS));
			productNotificationActionDetails.add(getProductNotificationActionByProductName(notificationActions,
					userNotificationSettings, CommonConstants.GVPN));
			productNotificationActionDetails.add(getProductNotificationActionByProductName(notificationActions,
					userNotificationSettings, CommonConstants.NPL));
			productNotificationActionDetails.add(getProductNotificationActionByProductName(notificationActions,
					userNotificationSettings, CommonConstants.GSC));
			userNotificationSubscriptionDetailsBean
					.setProductNotificationActionDetails(productNotificationActionDetails);
		}
		return userNotificationSubscriptionDetailsBean;
	}

	private List<UserNotificationSetting> getUserNotificationSettingsForTheUser(Integer status) {
		String userName = Utils.getSource();
		return userNotificationSettingsRepository.findByUseridAndIsActive(userName, status);
	}

	private ProductNotificationActionDetails getProductNotificationActionByProductName(
			List<NotificationAction> notificationActions, List<UserNotificationSetting> userNotificationSettings,
			String productName) {
		ProductNotificationActionDetails productNotificationActionDetails = new ProductNotificationActionDetails();
		productNotificationActionDetails.setProductName(productName);
		List<NotificationAction> iasNotificationActionList = notificationActions.stream()
				.filter(notificationAction -> notificationAction.getErfPrdCatalogProductName().equals(productName))
				.collect(Collectors.toList());
		if (iasNotificationActionList != null) {
			List<NotificationActionDetailsBean> notificationActionDetailsBeans = new ArrayList<>();
			iasNotificationActionList.stream().forEach(notificationAction -> {
				notificationActionDetailsBeans
						.add(constructNotificationActionDetailsBean(notificationAction, userNotificationSettings));
			});
			productNotificationActionDetails.setNotificationActionDetails(notificationActionDetailsBeans);
		} else {
			productNotificationActionDetails.setNotificationActionDetails(new ArrayList<>());
		}
		return productNotificationActionDetails;
	}

	private NotificationActionDetailsBean constructNotificationActionDetailsBean(NotificationAction notificationAction,
			List<UserNotificationSetting> userNotificationSettings) {
		NotificationActionDetailsBean notificationActionDetailsBean = new NotificationActionDetailsBean();
		notificationActionDetailsBean.setActionId(notificationAction.getId());
		notificationActionDetailsBean.setActionName(notificationAction.getName());
		Optional<UserNotificationSetting> userNotificationSetting = userNotificationSettings.stream()
				.filter(settings -> settings.getNotificationAction().equals(notificationAction)).findFirst();
		if (userNotificationSetting.isPresent()) {
			notificationActionDetailsBean.setIsEnabled(
					userNotificationSetting.get().getIsNotificationEnabled().equals(CommonConstants.ACTIVE));
		} else {
			notificationActionDetailsBean.setIsEnabled(false);
		}
		return notificationActionDetailsBean;
	}

	/**
	 * 
	 * Update User Notification Subscription Details fro the user
	 * 
	 * @author ANANDHI VIJAY
	 * @param userNotificationSubscriptionDetailsBean
	 * @return
	 */
	public String updateUserNotificationSubscriptionDetails(
			UserNotificationSubscriptionDetailsBean userNotificationSubscriptionDetailsBean) {
		String response = null;
		if (userNotificationSubscriptionDetailsBean != null) {
			changeTheStatusOfTheUserNotificationSettings(Utils.getSource(), CommonConstants.INACTIVE);
			response = constructUserNotificationSettingsAndSaveForUser(Utils.getSource(),
					userNotificationSubscriptionDetailsBean);
			if (response != null && response.equals(ResponseResource.RES_SUCCESS)) {
				clearUserNotificationSettingsByStatus(CommonConstants.INACTIVE);
			}
		}
		return response;
	}

	private void changeTheStatusOfTheUserNotificationSettings(String userName, Integer status) {
		List<UserNotificationSetting> userNotificationSettings = userNotificationSettingsRepository
				.findByUserid(userName);
		List<UserNotificationSetting> userNotificationSettingsToDeactivate = new ArrayList<>();
		if (userNotificationSettings != null && !userNotificationSettings.isEmpty()) {
			userNotificationSettings.stream().forEach(userNotificationSetting -> {
				UserNotificationSetting settings = userNotificationSetting;
				settings.setIsActive(status);
				userNotificationSettingsToDeactivate.add(settings);
			});
		}
		if (!userNotificationSettingsToDeactivate.isEmpty()) {
			userNotificationSettingsRepository.saveAll(userNotificationSettingsToDeactivate);
		}
	}

	private String constructUserNotificationSettingsAndSaveForUser(String userName,
			UserNotificationSubscriptionDetailsBean userNotificationSubscriptionDetailsBean) {
		final List<UserNotificationSetting> userNotificationSettings = new ArrayList<>();
		userNotificationSubscriptionDetailsBean.getProductNotificationActionDetails()
				.forEach(productNotificationAction -> {
					productNotificationAction.getNotificationActionDetails().forEach(action -> {
						if (action.getActionId() != null) {
							Optional<NotificationAction> notificationAction = notificationActionRepository
									.findById(action.getActionId());
							if (notificationAction.isPresent() && action.getIsEnabled() != null) {
								userNotificationSettings.add(constructUserNotificationSettingsForUserOnUpdate(userName,
										notificationAction.get(),
										action.getIsEnabled().equals(true) ? CommonConstants.ACTIVE
												: CommonConstants.INACTIVE));
							}
						}
					});
				});
		if (!userNotificationSettings.isEmpty()
				&& userNotificationSettingsRepository.saveAll(userNotificationSettings) != null) {
			return ResponseResource.RES_SUCCESS;
		}
		return ResponseResource.RES_FAILURE;
	}

	private static UserNotificationSetting constructUserNotificationSettingsForUserOnUpdate(String userId,
			NotificationAction notificationAction, Integer status) {
		UserNotificationSetting userNotificationSetting = new UserNotificationSetting();
		userNotificationSetting.setCreatedBy(userId);
		userNotificationSetting.setCreatedTime(new Timestamp(new Date().getTime()));
		userNotificationSetting.setNotificationAction(notificationAction);
		userNotificationSetting.setUserid(userId);
		userNotificationSetting.setIsActive(CommonConstants.ACTIVE);
		userNotificationSetting.setUpdatedTime(new Timestamp(new Date().getTime()));
		userNotificationSetting.setUpdatedBy(userId);
		userNotificationSetting.setIsNotificationEnabled(status);
		userNotificationSetting.setNotificationType("email");
		return userNotificationSetting;
	}

	private void clearUserNotificationSettingsByStatus(Integer flag) {
		List<UserNotificationSetting> userNotificationSettings = userNotificationSettingsRepository
				.findByIsActive(flag);
		if (userNotificationSettings != null && !userNotificationSettings.isEmpty()) {
			userNotificationSettingsRepository.deleteAll(userNotificationSettings);
		}
	}
}
