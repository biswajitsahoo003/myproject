package com.tcl.dias.audit.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.tcl.dias.audit.constants.ExceptionConstants;
import com.tcl.dias.audit.dto.FeedbackDto;
import com.tcl.dias.audit.entity.entities.Feedback;
import com.tcl.dias.audit.entity.repository.FeedbackRepository;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class FeedbackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackService.class);

	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	UserInfoUtils userinfoUtils;
	
	
	@Autowired
	NotificationService notificationService;
	
	

	@Transactional(readOnly = true)
	public PagedResult<Feedback> getAllFeedbackDetails(Integer page, Integer size, String searchText) {
		
		List<Feedback> feedbackBean = new ArrayList<>();
		PageRequest pageable = null;
		
		if (page != null && size != null) {
			pageable = PageRequest.of(page, size);
		}
		Page<Feedback> pagedFeedbacks = feedbackRepository.findAllByOrderByCreatedTimeDesc(pageable);
		List<Feedback> feedbacks = pagedFeedbacks.getContent();
		if (!feedbacks.isEmpty()) {
			feedbacks.forEach(feedback -> feedbackBean.add(constructFeedbackSummary(feedback)));
		}
		return new PagedResult<>(feedbackBean, pagedFeedbacks.getTotalElements(), pagedFeedbacks.getTotalPages());
	}
	
	
	public List<FeedbackDto> getFeedbackDetails() throws TclCommonException {
		List<FeedbackDto> feedbackDetails = new ArrayList<>();
		try {
			List<Feedback> feedbacks = feedbackRepository.findAll();
			
			feedbacks.stream().forEach(item -> {
				FeedbackDto fbDto = new FeedbackDto();
				fbDto.setId(item.getId());
				fbDto.setPageURL(item.getPageURL());
				fbDto.setComments(item.getComments());
				fbDto.setCreatedBy(item.getCreatedBy());
				fbDto.setCreatedTime(item.getCreatedTime().toString());
				fbDto.setStatus(item.getStatus()!=null?item.getStatus().toUpperCase():"OPEN");
				feedbackDetails.add(fbDto);
			});
//			feedbackDetails = feedbackRepository.findAll().stream().map(FeedbackDto::new).collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("ERROR - ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return feedbackDetails;
	}

	public void createFeedback(FeedbackDto feedbackDto) throws TclCommonException {
		if (!validateFeedbackRequest(feedbackDto))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);
		try {
			Feedback feedback = new Feedback();
			feedback.setPageURL(feedbackDto.getPageURL());
			feedback.setComments(feedbackDto.getComments());
			feedback.setCreatedBy(feedbackDto.getCreatedBy());
			feedback.setCreatedTime(new Timestamp((new java.util.Date()).getTime()));
			feedback.setStatus(feedbackDto.getStatus()!=null?feedbackDto.getStatus().toUpperCase():"OPEN");
			feedbackRepository.save(feedback);
			processFeedbackNotification(feedback);
		} catch (Exception e) {
			LOGGER.error("Error in feedback", e);
		}}
	private void processFeedbackNotification(Feedback feedback)  {

		UserInformation userinformation=userinfoUtils.getUserInformation();
		if(!ObjectUtils.isEmpty(userinformation)) {
		LOGGER.info("userInformation {}",userinformation);
		String name = userinformation.getFirstName() + " " + userinformation.getLastName();
		String emailId = Utils.getSource();
		String customerName = null;
		LOGGER.info("mailId{}", emailId);
		List<CustomerDetail> customerDetailList=userinformation.getCustomers();
		if(!CollectionUtils.isEmpty(customerDetailList) && customerDetailList.get(0)!=null){
			customerName=customerDetailList.get(0).getCustomerName()!=null?
					customerDetailList.get(0).getCustomerName():"Internal User";
		}
		
		notificationService.nofifyCustomerFeedback(name, emailId,customerName, feedback);
		}
		
	}

	private boolean validateFeedbackRequest(FeedbackDto feedbackDto) {
		if (feedbackDto == null || feedbackDto.getPageURL() == null || feedbackDto.getComments() == null
				|| feedbackDto.getCreatedBy() == null) {
			return false;
		}
		return true;
	}
	
	public Feedback constructFeedbackSummary(Feedback fb) {
		Feedback feedback = new Feedback();
		feedback.setId(fb.getId());
		feedback.setPageURL(fb.getPageURL());
		feedback.setComments(fb.getComments());
		feedback.setCreatedBy(fb.getCreatedBy());
		feedback.setCreatedTime(fb.getCreatedTime());
		feedback.setStatus(fb.getStatus()!=null?fb.getStatus().toUpperCase():"OPEN");
		return feedback;
	}


	public void updateFeedback(FeedbackDto feedbackDto) throws TclCommonException {

		if (!validateFeedbackUpdateRequest(feedbackDto))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);
		try {
			Feedback feedback = new Feedback();
			Optional<Feedback> feedbackdetails = feedbackRepository
					.findById(feedbackDto.getId());
			if(feedbackdetails.isPresent()) {
				feedback.setId(feedbackdetails.get().getId());
				feedback.setStatus(feedbackDto.getStatus()!=null?feedbackDto.getStatus().toUpperCase():"OPEN");
				feedback.setPageURL(feedbackdetails.get().getPageURL());
				feedback.setComments(feedbackdetails.get().getComments());
				feedback.setCreatedBy(feedbackdetails.get().getCreatedBy());
				feedback.setCreatedTime(new Timestamp((new java.util.Date()).getTime()));
				feedbackRepository.save(feedback);
			}
			else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
	}


	private boolean validateFeedbackUpdateRequest(FeedbackDto feedbackDto) {
		if (feedbackDto == null || feedbackDto.getId()==null || feedbackDto.getStatus() == null) {
			return false;
		}
		return true;
	}


	
	}
