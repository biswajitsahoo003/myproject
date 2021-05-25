package com.tcl.dias.location.gvpn.service.v1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.tcl.dias.location.entity.repository.MstCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.location.beans.MstCityBean;
import com.tcl.dias.location.beans.MstStateBean;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;

/**
 * This file contains the GvpnLocationService.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Transactional
@Service
public class GvpnLocationService extends GvpnAbstractService {

	@Autowired
	MstStateRepository mstStateRepository;

	@Autowired
	MstCityRepository mstCityRepository;
	
	@Autowired
	MstPincodeRespository mstPinCodeRepository;

	@Autowired
	MstCountryRepository mstCountryRepository;


	/**
	 * @author VIVEK KUMAR K getStateDetails
	 * @param country
	 * @return
	 */
	public List<MstStateBean> getStateDetails(String country) {
		List<MstState> mstStates = mstStateRepository.findByMstCountry_Name(country);
		return constructMstStatesBean(mstStates);
	}

	/**
	 * @author KAUSHIK SHANKAR getCountryDetails
	 * @param country
	 * @return
	 */
	public List<String> getCountryDetails() {
		List<String> mstCountry = mstCountryRepository.findDistinctCountries();
		return mstCountry;
	}

	/**
	 * @author VIVEK KUMAR K getStateDetails
	 * @param country
	 * @return
	 */
	public List<MstCityBean> getCityDetails(String city) {
		List<MstCity> mstCity = mstCityRepository.findByMstState_Name(city);
		return constructMstCityBean(mstCity);
	}
	
	/**
	 * getCityDetails based on zipcode 
	 * @param country
	 * @return
	 */
	public List<MstCityBean> getCityDetailsByZipCode(String zipcode,String State) {
		List<MstCityBean> CityList=null;
		List<MstPincode> pincode=mstPinCodeRepository.findByCode(zipcode);
		List<MstCity> mstCity = mstCityRepository.findByMstState_Name(State);
		List<MstCity> mstCityList=new ArrayList<>();
		if(!pincode.isEmpty() && !mstCity.isEmpty()) {
		  mstCity.forEach( mstCityDetail ->{		
			if(mstCityDetail.getId()==pincode.get(0).getMstCity().getId()) {
				mstCityList.add(mstCityDetail);
			}
			
		  	});
		  	if(!mstCityList.isEmpty()) {
		  		CityList= constructMstCityBean(mstCityList);
		  		return CityList;
		  	}
		  	else {
		  		return CityList;
		  	}
		}
		else {
			return CityList;
		}
		
//		List<MstPincode> pincode=mstPinCodeRepository.findByCode(zipcode);
//		Optional<MstCity> mstCity = mstCityRepository.findById(pincode.get(0).getMstCity().getId());
//		if(mstCity.isPresent()) {
//			return constructMstCityBeanByZipcode(mstCity.get());
//		}
//		else {
//		return null;
//		}
		
	}


}
