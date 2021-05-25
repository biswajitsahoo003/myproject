package com.tcl.dias.location.service.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.redis.service.RedisCacheService;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;

/**
 * 
 * This file contains the LocationCacheService.java class.
 * This class provides functions to store and retrieve location data from redis/DB.
 *
 * @author Samuel S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class LocationCacheService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationCacheService.class);
	
	@Autowired
	private MstPincodeRespository mstPincodeRespository;

	@Autowired
	private MstCityRepository mstCityRepository;

	@Autowired
	private MstCountryRepository mstCountryRepository;

	@Autowired
	private MstStateRepository mstStateRepository;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	public MstCountry findCountryByName(String country) {
		MstCountry mstCountry = null;
		Object cacheObj = redisCacheService.getHash("mstCountryMap", country);
		if(cacheObj !=null) {			
			mstCountry = (MstCountry)cacheObj;
			LOGGER.info("return MstCountry from redis cache ({})",mstCountry.getName());
		}else {
			LOGGER.info("return MstCountry from DB");
			mstCountry = mstCountryRepository.findByName(country);	
			redisCacheService.putHash("mstCountryMap", country, mstCountry);			
		}
		return mstCountry;
	}
	
	public MstState findStateByNameAndMstCountry(String state, String country) {		
		MstState mstState = null;
		Object cacheObj = redisCacheService.getHash("mstStateCountryMap", state+country);
		
		if(cacheObj !=null) {	
			mstState = (MstState)cacheObj;			
			LOGGER.info("return MstState from redis cache ({})",mstState.getName());
		}else {
			LOGGER.info("return MstState from DB");
			List<MstState >mstStateList = mstStateRepository.findByNameAndMstCountry_Name(state, country);
			if(!mstStateList.isEmpty()) {
				mstState = mstStateList.get(0);
				redisCacheService.putHash("mstStateCountryMap", state+country, mstState);	
			}
		}
		return mstState;
	}
	
	public MstState findStateByName(String state) {		
		MstState mstState = null;
		Object cacheObj = redisCacheService.getHash("mstStateMap", state);
		
		if(cacheObj !=null) {	
			mstState = (MstState)cacheObj;		
			LOGGER.info("return MstState from redis cache ({})",mstState.getName());
		}else {
			LOGGER.info("return MstState from DB {}",state);
			mstState = mstStateRepository.findByName(state);	
			redisCacheService.putHash("mstStateMap", state, mstState);			
		}
		return mstState;
	}
	

	public MstCity findCityByNameAndMstState(String city, String state) {		
		MstCity mstCity = null;
		Object cacheObj = redisCacheService.getHash("mstCityStateMap", city+state);
		
		if(cacheObj !=null) {	
			mstCity = (MstCity)cacheObj;			
			LOGGER.info("return MstCity from redis cache ({})",mstCity.getName());
		}else {
			LOGGER.info("return MstCity from DB");
			List<MstCity> mstCityList = mstCityRepository.findByNameAndMstState_Name(city, state);
			if(!mstCityList.isEmpty()) {
				mstCity = mstCityList.get(0);
				redisCacheService.putHash("mstCityStateMap", city+state, mstCity);	
			}					
		}
		return mstCity;
	}
	
	@SuppressWarnings("unchecked")
	public List<MstPincode> findPinCode(String pinCode) {		
		List<MstPincode> mstPincode = null;
		Object cacheObj = redisCacheService.getHash("mstPinCodeMap", pinCode);
		
		if(cacheObj !=null) {	
			mstPincode = (List<MstPincode>)cacheObj;	
			LOGGER.info("return MstPincode from redis cache ({})",pinCode);
		}else {
			LOGGER.info("return pincode from DB");
			mstPincode = mstPincodeRespository.findByCode(pinCode);	
			redisCacheService.putHash("mstPinCodeMap", pinCode, mstPincode);			
		}
		return mstPincode;
	}
	

}
