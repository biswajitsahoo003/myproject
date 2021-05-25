package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tcl.dias.networkaugment.entity.repository.GroupToHolidayTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.networkaugment.entity.entities.BusinessHourTemplate;
import com.tcl.dias.networkaugment.entity.entities.GroupToBusinessHourTemplate;
import com.tcl.dias.networkaugment.entity.entities.GroupToHolidayTemplate;
import com.tcl.dias.networkaugment.entity.entities.HolidayTemplate;
import com.tcl.dias.networkaugment.entity.repository.BusinessHourTemplateRepository;
import com.tcl.dias.networkaugment.entity.repository.GroupToBusinessHourTemplateRepository;
import com.tcl.dias.networkaugment.entity.repository.GroupToHolidayTemplateRepository;
import com.tcl.dias.networkaugment.entity.repository.HolidayTemplateRepository;

/**
 * This Class is used to calculate tat
 * @author prasath
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly=true,isolation=Isolation.READ_COMMITTED)
public class TATService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TATService.class);

    @Autowired
	GroupToHolidayTemplateRepository groupToHolidayTemplateRepository;

    @Autowired
    GroupToBusinessHourTemplateRepository groupToBusinessHourTemplateRepository;

    @Autowired
    HolidayTemplateRepository holidayTemplateRepository;

    @Autowired
    BusinessHourTemplateRepository businessHourTemplateRepository;

    public Timestamp calculateDueDate(int tat, String groupName, Timestamp taskCreateTime) {
        Timestamp operatingDate = taskCreateTime;
        try {
	        GroupToBusinessHourTemplate groupToBusinessHourTemplate =
	                groupToBusinessHourTemplateRepository.findFirstByGroupName(groupName);
	        int minutes = 60;
	        String businessHourTemplateCode ="9_am_to_5pm";
	        GroupToHolidayTemplate groupToHolidayTemplate = groupToHolidayTemplateRepository.findFirstByGroupName(groupName);
	        if (groupToHolidayTemplate != null) businessHourTemplateCode = groupToBusinessHourTemplate.getTemplateCode();
	        List<BusinessHourTemplate> businessHourTemplates = businessHourTemplateRepository.findAllByTemplateCode(businessHourTemplateCode);
	        List<HolidayTemplate> holidayTemplates = (groupToHolidayTemplate != null) ?
	                holidayTemplateRepository.findAllByTemplateCode(groupToHolidayTemplate.getTemplateCode()) : Arrays.asList();
	
	        Map<String, BusinessHourTemplate> businessHourTemplateMap = businessHourTemplates.stream().collect(Collectors.toMap(BusinessHourTemplate::getDay, Function.identity()));
	        List<Timestamp> holidayTemplateList = holidayTemplates.stream().map(HolidayTemplate::getDate).collect(Collectors.toList());
	
	        operatingDate = checkTaskCreatedBeforeBusinessHour(operatingDate, businessHourTemplateMap);
	        while (tat >= 1) {
	            String currentDay = operatingDate.toLocalDateTime().getDayOfWeek().name();
	            BusinessHourTemplate businessHourTemplate = businessHourTemplateMap.get(currentDay.toUpperCase());
	            if(isWorkingDay(operatingDate, businessHourTemplateMap, holidayTemplateList)) {
	                int subtractHours = subtractWorkingHours(operatingDate, businessHourTemplate);
	                int subtractMinutes = subtractHours * minutes;
	                if(tat >= subtractMinutes) {
	                    tat = tat - subtractMinutes;
	                    operatingDate = (tat > 1) ? getNextDay(operatingDate, businessHourTemplate) :
	                            setEndWorkingHour(operatingDate, businessHourTemplate);
	                } else {
	                    operatingDate = Timestamp.valueOf(operatingDate.toLocalDateTime().plusMinutes(tat));
	                    return operatingDate;
	                }
	            } else {
	                operatingDate = getNextDay(operatingDate, businessHourTemplate);
	            }
	        }
        }catch(Exception ee) {
        	ee.printStackTrace();
        }
        return operatingDate;
    }
    
    
 
    
    public static List<LocalDateTime> getDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
       return Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate)).collect(Collectors.toList());

    }

    private Timestamp setEndWorkingHour(Timestamp operatingDate, BusinessHourTemplate businessHourTemplate) {
        LocalDateTime endLocalDateTime = businessHourTemplate.getEndTime().toLocalDateTime();
        LocalDateTime localDateTime = operatingDate.toLocalDateTime().withHour(endLocalDateTime.getHour());
        localDateTime = localDateTime.withMinute(endLocalDateTime.getMinute());
        return Timestamp.valueOf(localDateTime);
    }

    private Timestamp checkTaskCreatedBeforeBusinessHour(Timestamp taskCreateTime,
                                                         Map<String, BusinessHourTemplate> businessHourTemplateMap) {
        LocalDateTime taskCreatedLocalTime = taskCreateTime.toLocalDateTime();
        BusinessHourTemplate businessHourTemplate = businessHourTemplateMap.get(taskCreatedLocalTime.getDayOfWeek().name());
        if(taskCreatedLocalTime!=null  && businessHourTemplate!=null) {
	        if(taskCreatedLocalTime.getHour() < businessHourTemplate.getStartTime().toLocalDateTime().getHour()){
	            taskCreatedLocalTime = taskCreatedLocalTime.withHour(getHour(businessHourTemplate.getStartTime())).withMinute(0);
	            return Timestamp.valueOf(taskCreatedLocalTime);
	        }
        }
        return taskCreateTime;
    }

    private int getHour(Timestamp time) {
        return time.toLocalDateTime().getHour();
    }

    private int subtractWorkingHours(Timestamp operatingDate, BusinessHourTemplate businessHourTemplate) {
        LocalDateTime localDateTime = operatingDate.toLocalDateTime().withHour(getHour(businessHourTemplate.getEndTime()));
        Timestamp workingEndDate = Timestamp.valueOf(localDateTime);
        if (getHour(operatingDate) < getHour(workingEndDate)) {
            return getHoursDifference(operatingDate, workingEndDate);
        }
        return 0;
    }
    
    

    private Timestamp getNextDay(Timestamp timestamp, BusinessHourTemplate businessHourTemplate) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        localDateTime = localDateTime.plusDays(1);
        localDateTime = localDateTime.withHour(getHour(businessHourTemplate.getStartTime()));
        return Timestamp.valueOf(localDateTime);
    }
    
    private int getHoursDifference(Timestamp startTimestamp, Timestamp endTimeStamp) {
    	
    	
    	 LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();
    	 LocalDateTime endLocalDateTime = endTimeStamp.toLocalDateTime();
    	 return endLocalDateTime.getHour() - startLocalDateTime.getHour();
    	}
    
  
	public int calculateDelay(String groupName, Timestamp plannedEndtime, Timestamp actualEndTime) {
		int delay = 0;
		try {
			GroupToBusinessHourTemplate groupToBusinessHourTemplate = groupToBusinessHourTemplateRepository
					.findFirstByGroupName(groupName);
			int minutes = 60;
			String businessHourTemplateCode = "9_am_to_5pm";
			GroupToHolidayTemplate groupToHolidayTemplate = groupToHolidayTemplateRepository
					.findFirstByGroupName(groupName);
			if (groupToHolidayTemplate != null)
				businessHourTemplateCode = groupToBusinessHourTemplate.getTemplateCode();
			List<BusinessHourTemplate> businessHourTemplates = businessHourTemplateRepository
					.findAllByTemplateCode(businessHourTemplateCode);
			List<HolidayTemplate> holidayTemplates = (groupToHolidayTemplate != null)
					? holidayTemplateRepository.findAllByTemplateCode(groupToHolidayTemplate.getTemplateCode())
					: Arrays.asList();

			Map<String, BusinessHourTemplate> businessHourTemplateMap = businessHourTemplates.stream()
					.collect(Collectors.toMap(BusinessHourTemplate::getDay, Function.identity()));
			List<Timestamp> holidayTemplateList = holidayTemplates.stream().map(HolidayTemplate::getDate)
					.collect(Collectors.toList());
			List<LocalDateTime> days = getDaysBetween(plannedEndtime.toLocalDateTime(),
					actualEndTime.toLocalDateTime());
			if (!days.isEmpty()) {
				days.remove(0);
				for (LocalDateTime day : days) {
					Timestamp dayTimestamp = Timestamp.valueOf(day);
					if (isWorkingDay(dayTimestamp, businessHourTemplateMap, holidayTemplateList)) {
						String currentDay = dayTimestamp.toLocalDateTime().getDayOfWeek().name();
						BusinessHourTemplate businessHourTemplate = businessHourTemplateMap
								.get(currentDay.toUpperCase());
						int workingHours = getHoursDifference(businessHourTemplate.getStartTime(),
								businessHourTemplate.getEndTime());
						delay += workingHours * minutes;
					}
				}
			}

			if (plannedEndtime.toLocalDateTime().toLocalDate().isEqual(actualEndTime.toLocalDateTime().toLocalDate())) {
				Timestamp operatingDate = checkTaskCreatedBeforeBusinessHour(plannedEndtime, businessHourTemplateMap);
				BusinessHourTemplate businessHourTemplate = businessHourTemplateMap
						.get(actualEndTime.toLocalDateTime().getDayOfWeek().name());
				if (actualEndTime.toLocalDateTime().getHour() < businessHourTemplate.getEndTime().toLocalDateTime()
						.getHour()) {
					if (isWorkingDay(operatingDate, businessHourTemplateMap, holidayTemplateList)
							&& getHour(operatingDate) < getHour(actualEndTime)) {
						int hours = getHoursDifference(operatingDate, actualEndTime);
						delay += hours * minutes;

					}
				} else {

					if (isWorkingDay(operatingDate, businessHourTemplateMap, holidayTemplateList)
							&& getHour(operatingDate) < getHour(businessHourTemplate.getEndTime())) {
						int hours = getHoursDifference(operatingDate, businessHourTemplate.getEndTime());
						delay += hours * minutes;

					}

				}

			} else {
				if (isWorkingDay(plannedEndtime, businessHourTemplateMap, holidayTemplateList)) {
					Timestamp operatingDate = checkTaskCreatedBeforeBusinessHour(plannedEndtime,
							businessHourTemplateMap);

					String currentDay = operatingDate.toLocalDateTime().getDayOfWeek().name();
					BusinessHourTemplate businessHourTemplate = businessHourTemplateMap.get(currentDay.toUpperCase());
					int hoursDifferenceStart = getHoursDifference(operatingDate, Timestamp.valueOf(
							operatingDate.toLocalDateTime().withHour(getHour(businessHourTemplate.getEndTime()))));
					delay += hoursDifferenceStart * minutes;
				}
				if (isWorkingDay(actualEndTime, businessHourTemplateMap, holidayTemplateList)) {
					Timestamp operatingDate = checkTaskCreatedBeforeBusinessHour(actualEndTime,
							businessHourTemplateMap);

					String currentDay = operatingDate.toLocalDateTime().getDayOfWeek().name();
					BusinessHourTemplate businessHourTemplate = businessHourTemplateMap.get(currentDay.toUpperCase());
					int hoursDifferenceEnd = getHoursDifference(Timestamp.valueOf(
							plannedEndtime.toLocalDateTime().withHour(getHour(businessHourTemplate.getStartTime()))),
							operatingDate);
					delay += hoursDifferenceEnd * minutes;
				}
			}

		} catch (Exception e) {
			LOGGER.error("error in calculationg delay hours{}", e);
		}
		return delay;
	}


    private Boolean isWorkingDay(Timestamp currentTimestamp, Map<String, BusinessHourTemplate> businessHourTemplates,
                                 List<Timestamp> holidayTemplates) {
        LocalDateTime currentLocalDateTime = currentTimestamp.toLocalDateTime();
        String currentDay = currentLocalDateTime.getDayOfWeek().name();
        BusinessHourTemplate businessHourTemplate = businessHourTemplates.get(currentDay.toUpperCase());
        boolean isNotHoliday = holidayTemplates.stream().noneMatch(h -> h.toLocalDateTime().toLocalDate()
                .equals(currentLocalDateTime.toLocalDate()));
        return businessHourTemplate.getIsWorkingDay().equals("Y") && isNotHoliday;
    }

  
}
