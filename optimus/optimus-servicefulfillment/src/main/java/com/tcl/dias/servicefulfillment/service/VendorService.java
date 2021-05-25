package com.tcl.dias.servicefulfillment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.VendorResponseBean;
import com.tcl.dias.servicefulfillment.beans.VendorsBean;
import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;
import com.tcl.dias.servicefulfillment.entity.repository.MstStatusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstVendorsRepository;
import com.tcl.dias.servicefulfillment.specification.VendorSpecification;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * VendorService this class is used to get the vendor details
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional(readOnly=true,isolation=Isolation.READ_COMMITTED)
public class VendorService {
	
	@Autowired
	MstVendorsRepository mstVendorsRepository;

	@Autowired
	MstStatusRepository mstStatusRepository;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	RestClientService restClientService;
	
	/**
	 * @author vivek
	 * @param vendorName
	 * @param type
	 * @param city
	 * @param country
	 * @return
	 * @throws TclCommonException
	 * this is used to get the vendor details
	 */
	public List<VendorsBean> getVendorDetails(String vendorName, String type, String country,String state, String city)
			throws TclCommonException {
		
				

		List<MstVendor> vendors = mstVendorsRepository.findAll(
				VendorSpecification.getVendorFilter(vendorName, TaskStatusConstants.ACTIVE, country, type,state , city));

		if (!vendors.isEmpty()) {
			return vendors.stream().map(VendorsBean::new).collect(Collectors.toList());
		}
		return new ArrayList<VendorsBean>();
	}

	/**
	 * This method is used to get list of all vendors
	 *
	 * @return
	 */

	public List<VendorsBean> getAllVendors() {
		return mstVendorsRepository.findByMstStatus_code("ACTIVE").stream().map(VendorsBean::new)
				.collect(Collectors.toList());
	}
	 


	/**
	 * This method is used to add a new vendor
	 *
	 * @param vendorsBean
	 * @return
	 */

	@Transactional(readOnly = false)
	public VendorsBean addVendor(VendorsBean vendorsBean) {
		MstVendor mstVendor = new MstVendor();
		mstVendor.setName(vendorsBean.getName());
		mstVendor.setContactName(vendorsBean.getContactName());
		mstVendor.setEmail(vendorsBean.getEmail());
		mstVendor.setPhoneNumber(vendorsBean.getPhoneNumber());
		mstVendor.setType(vendorsBean.getType());
		mstVendor.setCircle(vendorsBean.getCircle());
		mstVendor.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		mstVendor.setCountry(vendorsBean.getCountry());
		mstVendorsRepository.save(mstVendor);
		return new VendorsBean();
	}

	/**
	 * This method is used to update Vendor details
	 *
	 * @param vendorsBean
	 * @return
	 * @throws TclCommonException
	 */

	@Transactional(readOnly = false)
	public VendorsBean updateVendor(VendorsBean vendorsBean) throws TclCommonException {
		Optional<MstVendor> vendor = mstVendorsRepository.findById(vendorsBean.getId());
		if(!vendor.isPresent()){
			throw new TclCommonException(ExceptionConstants.MST_VENDOR_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
		}
		MstVendor mstVendor = vendor.get();
		if(!mstVendor.getEmail().equalsIgnoreCase(vendorsBean.getEmail())){
			mstVendor.setEmail(vendorsBean.getEmail());
		}
		if(!mstVendor.getName().equalsIgnoreCase(vendorsBean.getName())){
			mstVendor.setName(vendorsBean.getName());
		}
		if(!mstVendor.getPhoneNumber().equalsIgnoreCase(vendorsBean.getPhoneNumber())){
			mstVendor.setPhoneNumber(vendorsBean.getPhoneNumber());
		}
		if(!mstVendor.getCircle().equalsIgnoreCase(vendorsBean.getCircle())){
			mstVendor.setCircle(vendorsBean.getCircle());
		}
		if(!mstVendor.getContactName().equalsIgnoreCase(vendorsBean.getContactName())){
			mstVendor.setContactName(vendorsBean.getContactName());
		}
		if(!mstVendor.getCountry().equalsIgnoreCase(vendorsBean.getCountry())){
			mstVendor.setCountry(vendorsBean.getCountry());
		}
		if(!mstVendor.getType().equalsIgnoreCase(vendorsBean.getType())){
			mstVendor.setType(vendorsBean.getType());
		}
		mstVendorsRepository.save(mstVendor);
		return new VendorsBean();
	}

	/**
	 * @author Mayank
	 * @param vendorId
	 * @return used to delete vendor
	 */
	@Transactional(readOnly = false)
	public VendorsBean deleteVendorById(Integer vendorId) {
		Optional<MstVendor> oMstVendor = mstVendorsRepository.findById(vendorId);
		if(oMstVendor.isPresent()){
			MstVendor mstVendor = oMstVendor.get();
			mstVendor.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INACTIVE));
			mstVendorsRepository.save(mstVendor);
		}
		return new VendorsBean();
	} 
	
	/**
	 * @author diksha garg
	 * @param type
	 * @param city
	 * @param state
	 * @return
	 * @throws TclCommonException
	 * this is used to get the vendor details from akana
	 */
	public VendorResponseBean searchVendorDetails(String type,String state, String city)
			throws TclCommonException {
		
		final String uri = "https://api-uat.tatacommunications.com/sb/vendorMaster/v0.1/searchVendor";

		String responseJsonCity = null;
		String responseJsonState = null;
		VendorResponseBean vendorResponseBeanCity = null;
		VendorResponseBean vendorResponseBeanState = null;

		Map<String,String> queryParams = new HashMap<>();
		queryParams.put("vendorType", type);
		if (city != null && !city.isEmpty()) {
			queryParams.put("region", city);
			
			RestResponse restResponse = restClientService.getWithQueryParam(uri, queryParams, new HttpHeaders());

			responseJsonCity = restResponse.getData();
			if (responseJsonCity!=null && !responseJsonCity.isEmpty()) {
				vendorResponseBeanCity = Utils.convertJsonToObject(responseJsonCity, VendorResponseBean.class);
			}
		}
		if (state != null && !state.isEmpty()) {
			queryParams.put("region", state);

			RestResponse restResponse = restClientService.getWithQueryParam(uri, queryParams, new HttpHeaders());

			responseJsonState = restResponse.getData();
			if (responseJsonState!=null && !responseJsonState.isEmpty()) {
				vendorResponseBeanState = Utils.convertJsonToObject(responseJsonState, VendorResponseBean.class);
			}
		}

		// concatenate state and city results if both entries present
		if (vendorResponseBeanCity != null && vendorResponseBeanCity.getVendorDetails()!=null && !vendorResponseBeanCity.getVendorDetails().isEmpty()
				&& vendorResponseBeanState != null && vendorResponseBeanState.getVendorDetails()!=null && !vendorResponseBeanState.getVendorDetails().isEmpty()) {
			vendorResponseBeanCity.getVendorDetails().addAll(vendorResponseBeanState.getVendorDetails());
		}

		// put state entries if city gives no result
		if ((vendorResponseBeanCity == null || vendorResponseBeanCity.getVendorDetails()==null || vendorResponseBeanCity.getVendorDetails().isEmpty())
				&& vendorResponseBeanState != null && vendorResponseBeanState.getVendorDetails()!=null && !vendorResponseBeanState.getVendorDetails().isEmpty()) {
			vendorResponseBeanCity = vendorResponseBeanState;
		}
		
		return vendorResponseBeanCity;
	}
}
