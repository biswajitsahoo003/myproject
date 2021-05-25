package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.networkaugment.entity.repository.MstDocumentRepository;
import com.tcl.dias.networkaugment.entity.repository.MstSlotRepository;
import com.tcl.dias.servicefulfillmentutils.beans.MstAppointmentDocumentsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstSlotBean;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the document related service
 */
@Service
@Transactional(readOnly = true,isolation=Isolation.READ_COMMITTED)
public class DocumentService {

	@Autowired
	MstDocumentRepository mstDocumentRepository;
	@Autowired
	MstSlotRepository mstSlotRepository;


	/**
	 * @author vivek
	 * 
	 * used to return the master Document list
	 * @return
	 */
	public List<MstAppointmentDocumentsBean> getMasterDocuments() {

		return mstDocumentRepository.findAll().stream().map(MstAppointmentDocumentsBean::new)
				.collect(Collectors.toList());
	}

	/**
	 * @author vivek
	 * 
	 * used to get the master slots
	 * @return
	 */
	public List<MstSlotBean> getMasterSlots() {
		return mstSlotRepository.findAll().stream().map(MstSlotBean::new).collect(Collectors.toList());
	}

}
