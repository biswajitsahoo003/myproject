package com.tcl.dias.oms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.OmsCustomerBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Partner;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service class for Oms Customer Listener
 *
 * @author SuruchiA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service

public class OmsCustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	/**
	 * Method to fetch customer details from Repository
	 * @param erfCustomerId
	 * @return
	 * @throws TclCommonException
	 */

	public List<OmsCustomerBean> getOmsCustomerDetails(List<Integer> erfCustomerIds) throws TclCommonException {

			if (Objects.isNull(erfCustomerIds)) 
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			
			List<Customer> customers = customerRepository.findByErfCusCustomerIdInAndStatus(erfCustomerIds, (byte)1);
			List<OmsCustomerBean> customerBeanList = new ArrayList<>();
			customers.forEach(customer -> {
				OmsCustomerBean omsCustomerBean = new OmsCustomerBean();
				omsCustomerBean.setCustomerId(customer.getId());
				omsCustomerBean.setCustomerCode(customer.getCustomerCode());
				omsCustomerBean.setCustomerEmail(customer.getCustomerEmailId());
				omsCustomerBean.setCustomerName(customer.getCustomerName());
				omsCustomerBean.setErfCusId(customer.getErfCusCustomerId());
				customerBeanList.add(omsCustomerBean);
			});
			return customerBeanList;
	}
}
