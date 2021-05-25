package com.tcl.dias.oms.sfdc.core;

import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the OmsSfdcInputHandler.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface IOmsSfdcInputHandler {

	OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean) throws TclCommonException;
	
	FeasibilityRequestBean getFeasibilityBean(Integer id, FeasibilityRequestBean feasibilityBean) throws TclCommonException;

	ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException;

	ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException;

	SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
			SiteSolutionOpportunityBean solutionOpportunityBean) throws TclCommonException;

	UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe, UpdateOpportunityStage updateOpportunityStage)
			throws TclCommonException;
	
	void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,ProductSolution productSolution) throws TclCommonException;


}
