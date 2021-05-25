package com.tcl.dias.sfdc.factory;

import com.tcl.dias.sfdc.component.DealRegistrationMapper;
import com.tcl.dias.sfdc.component.SfdcBundleOpportunityMapper;
import com.tcl.dias.sfdc.component.SfdcDeleteProductMapper;
import com.tcl.dias.sfdc.component.SfdcFeasibilityMapper;
import com.tcl.dias.sfdc.component.SfdcOpportunityMapper;
import com.tcl.dias.sfdc.component.SfdcPartnerEntityContactMapper;
import com.tcl.dias.sfdc.component.SfdcPartnerEntityMapper;
import com.tcl.dias.sfdc.component.SfdcPartnerOpportunityMapper;
import com.tcl.dias.sfdc.component.SfdcProcessMapper;
import com.tcl.dias.sfdc.component.SfdcSalesFunnelMapper;
import com.tcl.dias.sfdc.component.SfdcSiteMapper;
import com.tcl.dias.sfdc.component.SfdcStagingMapper;
import com.tcl.dias.sfdc.component.SfdcUpdateProcessMapper;
import com.tcl.dias.sfdc.component.SfdcUpdateWaiverMapper;
import com.tcl.dias.sfdc.component.SfdcWaiverMapper;
import com.tcl.dias.sfdc.constants.MapperConstants;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This file contains the SfdcMapperFactory.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcMapperFactory {

	@Autowired
	private SfdcOpportunityMapper opportunityMapper;

	@Autowired
	private SfdcSiteMapper siteMapper;

	@Autowired
	private SfdcStagingMapper stagingMapper;

	@Autowired
	private SfdcProcessMapper processMapper;
	
	@Autowired
	private SfdcUpdateProcessMapper updateProcessMapper;
	
	@Autowired
	private SfdcDeleteProductMapper deleteProcessMapper;

	@Autowired
	private SfdcPartnerEntityMapper partnerEntityProcessMapper;

	@Autowired
	private SfdcSalesFunnelMapper salesFunnelMapper;
	
	@Autowired
	private SfdcFeasibilityMapper feasibilityMapper;

	@Autowired
	private SfdcPartnerOpportunityMapper partnerOpportunityMapper;

	@Autowired
	DealRegistrationMapper dealRegistrationMapper;
	
	@Autowired
	private SfdcBundleOpportunityMapper sfdcBundleMapper;

	@Autowired
	private SfdcWaiverMapper waiverMapper;

	@Autowired
	private SfdcUpdateWaiverMapper waiverUpdateMapper;
	
	@Autowired
	SfdcPartnerEntityContactMapper partnerEntityContactMapper;


	public ISfdcMapper getInstanceMapper(String type) {
		ISfdcMapper mapper = null;
		

		if (type.equalsIgnoreCase(MapperConstants.OPPORTUNITY_MAPPER.toString())) {
			mapper = opportunityMapper;
		}

		else if (type.equalsIgnoreCase(MapperConstants.SITE_MAPPER.toString())) {
			mapper = siteMapper;
		} else if (type.equalsIgnoreCase(MapperConstants.STAGING_MAPPER.toString())) {
			mapper = stagingMapper;
		}

		else if (type.equalsIgnoreCase(MapperConstants.PROCESS_MAPPER.toString())) {
			mapper = processMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.PRODUCT_UPDATE.toString())) {
			mapper = updateProcessMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.PRODUCT_DELETE.toString())) {
			mapper = deleteProcessMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.FEASIBILITY_MAPPER.toString())) {
			mapper = feasibilityMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.PARTNER_ENTITY.toString())) {
			mapper = partnerEntityProcessMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.SALES_FUNNEL.toString())) {
			mapper = salesFunnelMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.PARTNER_OPPOERTUNITY_MAPPER.toString())) {
			mapper = partnerOpportunityMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.DEAL_REGISTRATION.toString())) {
			mapper = dealRegistrationMapper;
		}
		else if(type.equalsIgnoreCase(MapperConstants.BUNDLE_OPPORTUNITY.toString())) {
			mapper=sfdcBundleMapper;
		}
		else if (type.equalsIgnoreCase(MapperConstants.PARTNER_ENTITY_CONTACT.toString())) {
			mapper = partnerEntityContactMapper;
		}
		else if(type.equalsIgnoreCase(MapperConstants.WAIVER_MAPPER.toString())) {
			mapper=waiverMapper;
		}
		else if(type.equalsIgnoreCase(MapperConstants.WAIVER_UPDATE_MAPPER.toString())) {
			mapper=waiverUpdateMapper;
		}

		return mapper;
	}

}
