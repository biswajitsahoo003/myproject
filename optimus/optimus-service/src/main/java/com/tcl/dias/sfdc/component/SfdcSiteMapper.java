package com.tcl.dias.sfdc.component;

import org.springframework.stereotype.Component;

import com.tcl.dias.common.sfdc.bean.SiteOpportunityLocation;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcSiteLocation;
import com.tcl.dias.sfdc.bean.SfdcSiteSolutionBean;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the SfdcSiteMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component
public class SfdcSiteMapper implements ISfdcMapper {

	/**
	 * transfortToSfdcRequest
	 * 
	 * @param omsInput
	 * @return
	 */
	@Override

	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcSiteSolutionBean sfdSitebean = null;

		SiteSolutionOpportunityBean siteBean = (SiteSolutionOpportunityBean) Utils.convertJsonToObject(omsInput,
				SiteSolutionOpportunityBean.class);

		sfdSitebean = new SfdcSiteSolutionBean();
		sfdSitebean.setIsCancel(siteBean.getIsCancel());
		sfdSitebean.setProductSolutionCode(siteBean.getProductSolutionCode());
		sfdSitebean.setOpportunityID(siteBean.getOpportunityId());
		sfdSitebean.setProductServiceID(siteBean.getProductServiceId());
		sfdSitebean.setSourceSystem(siteBean.getSourceSystem());
		sfdSitebean.setSourceSytemTransactionID(siteBean.getSourceSytemTransactionId());
		for (SiteOpportunityLocation location : siteBean.getSiteOpportunityLocations()) {
			SfdcSiteLocation sfdcLocation = new SfdcSiteLocation();
			sfdcLocation.setCity(location.getLocation());
			sfdcLocation.setCountry(location.getCountry());
			sfdcLocation.setLocation(location.getLocation());
			sfdcLocation.setSiteLocationID(location.getSiteLocationID());
			sfdcLocation.setSiteMRC(String.valueOf(location.getSiteMRC()));
			sfdcLocation.setSiteNRC(String.valueOf(location.getSiteNRC()));
			sfdcLocation.setState(location.getState());
			//MACD COPFID implementation
			sfdcLocation.setCurrentCircuitServiceId(location.getCurrentCircuitServiceId());
			
			if (location.getLinkCode()!=null)
				sfdcLocation.setLinkType(location.getLinkCode());

			sfdSitebean.getSiteLocations().add(sfdcLocation);

		}

		return sfdSitebean;
	}

}
