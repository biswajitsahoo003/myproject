package com.tcl.dias.location.gvpn.service.v1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.tcl.dias.location.beans.MstCityBean;
import com.tcl.dias.location.beans.MstStateBean;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstState;

/**
 * This file contains the GvpnAbstractService.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class GvpnAbstractService {
	
	/**
	 * @author VIVEK KUMAR K
	 * constructMstStatesBean
	 * @param mstStates
	 */
	protected List<MstStateBean> constructMstStatesBean(List<MstState> mstStates) {
		List<MstStateBean> mstStateBeans=new ArrayList<>();
		if(mstStates!=null && !mstStates.isEmpty()) {
			mstStates.forEach( mstState ->{
				MstStateBean mstStateBean=new MstStateBean();
				mstStateBean.setId(mstState.getId());
				mstStateBean.setIsUnion(mstState.getIsUnion());
				mstStateBean.setName(mstState.getName());
				mstStateBean.setRegionName(mstState.getRegionName());
				mstStateBean.setStateCode(mstState.getStateCode());
				mstStateBeans.add(mstStateBean);
				
			});

		}
		mstStateBeans.sort(Comparator.comparing(MstStateBean::getName));
		
		return mstStateBeans;
		
	}
	
	
	/**
	 * @author VIVEK KUMAR K
	 * constructMstStatesBean
	 * @param mstStates
	 */
	protected List<MstCityBean> constructMstCityBean(List<MstCity> mstCity) {
		List<MstCityBean> mstCityBeans=new ArrayList<>();
		if(mstCity!=null && !mstCity.isEmpty()) {
			mstCity.forEach( mstState ->{
				MstCityBean mstStateBean=new MstCityBean();
				mstStateBean.setId(mstState.getId());
				mstStateBean.setName(mstState.getName());
				mstCityBeans.add(mstStateBean);
				
			});

		}
		mstCityBeans.sort(Comparator.comparing(MstCityBean::getName));

		return mstCityBeans;
		
	}
	
	/*
	 * constructMstCityBean based on zipCode
	 * @param mstStates
	 */
	protected MstCityBean constructMstCityBeanByZipcode(MstCity mstCity) {
		MstCityBean mstCityBeans=new MstCityBean();
		mstCityBeans.setId(mstCity.getId());
		mstCityBeans.setName(mstCity.getName());
		return mstCityBeans;
		
	}


	}
