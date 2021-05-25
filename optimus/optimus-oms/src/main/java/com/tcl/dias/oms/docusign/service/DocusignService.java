package com.tcl.dias.oms.docusign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.pdf.service.IllQuotePdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class DocusignService extends DocuSignUtilService{
	
	@Autowired
	IllQuoteService illQuoteService;
	
	@Autowired
	NplQuoteService nplQuoteService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	IzoPcQuoteService izoPcQuoteService;

	@Autowired
	GscOrderService gscOrderService;
	
	@Autowired
	IllQuotePdfService illQuotePdfService;
	
	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;
	
	@Autowired
    IPCQuoteService ipcQuoteService;
	
	
	
	public void processForApproveQuotes(String orderRefUuid) throws TclCommonException {
		
		if (orderRefUuid.contains(CommonConstants.IAS)) {
			illQuoteService.approvedDocusignQuotes(orderRefUuid);
		} else if (orderRefUuid.contains(CommonConstants.NPL)) {
			nplQuoteService.approvedDocusignQuotes(orderRefUuid);
		} else if (orderRefUuid.contains(CommonConstants.NDE)) {
			nplQuoteService.approvedDocusignQuotes(orderRefUuid);
		}else if (orderRefUuid.contains(CommonConstants.GVPN)) {
			gvpnQuoteService.approvedDocusignQuotes(orderRefUuid);
		} else if (orderRefUuid.contains(CommonConstants.IZOPC)) {
			izoPcQuoteService.approvedDocusignQuotes(orderRefUuid);
		} else if (orderRefUuid.contains(CommonConstants.GSC)) {
			gscOrderService.approvedDocuSignQuotes(orderRefUuid);
		} else if(orderRefUuid.contains(IzosdwanCommonConstants.IZOSDWAN)) {
			izosdwanQuoteService.approvedDocusignQuotes(orderRefUuid);
		} else if (orderRefUuid.contains(CommonConstants.IPC)) {
            ipcQuoteService.approvedDocusignQuotes(orderRefUuid);
        }

	}
	

}
