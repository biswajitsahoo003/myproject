package com.tcl.dias.notification.entity.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tcl.dias.notification.entity.entities.UserNotificationSetting;

@Repository
public interface UserNotificationSettingsRepository extends JpaRepository<UserNotificationSetting, Integer>{
	
	public List<UserNotificationSetting> findByUserid(String userId);
	
	@Transactional
	@Modifying
	public Integer deleteByUserid(String userId);
	
	public UserNotificationSetting findByUseridAndNotificationAction_Id(String userId,Integer actionId);
	
	public List<UserNotificationSetting> findByUseridAndIsActive(String userId,Integer flag);
	
	public List<UserNotificationSetting> findByIsActive(Integer flag);
}

