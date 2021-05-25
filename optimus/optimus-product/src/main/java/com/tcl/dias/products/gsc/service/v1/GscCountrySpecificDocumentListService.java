package com.tcl.dias.products.gsc.service.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentListener;
import com.tcl.dias.productcatelog.entity.entities.GscCountrySpecificDocumentList;
import com.tcl.dias.productcatelog.entity.repository.GscCountrySpecificDocumentListRepository;

/**
 * Country Specific Document List for GSC products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscCountrySpecificDocumentListService {

	@Autowired
	GscCountrySpecificDocumentListRepository gscCountrySpecificDocumentListRepository;

	private static GscCountrySpecificDocumentBean toDocumentBean(
			GscCountrySpecificDocumentList gscCountrySpecificDocument) {
		GscCountrySpecificDocumentBean bean = new GscCountrySpecificDocumentBean();
		bean.setCountryName(gscCountrySpecificDocument.getCountryName());
		bean.setDocumentName(gscCountrySpecificDocument.getDocumentName());
		bean.setProductName(gscCountrySpecificDocument.getProductName());
		bean.setCountryCode(gscCountrySpecificDocument.getCountryCode());
		bean.setCategory(gscCountrySpecificDocument.getCategory());
		bean.setTemplate(gscCountrySpecificDocument.getTemplate());
		bean.setuID(gscCountrySpecificDocument.getUID());
		bean.setRemarks(gscCountrySpecificDocument.getRemarks());
		bean.setType(gscCountrySpecificDocument.getType());
		bean.setCategory(gscCountrySpecificDocument.getCategory());
		return bean;
	}

	public String processDocumentName(GscCountrySpecificDocumentListener listener) {
		GscCountrySpecificDocumentBean gscCountrySpecificDocumentBean = listener.getGscCountrySpecificDocumentBean();
		return gscCountrySpecificDocumentListRepository
				.findByDocumentNameAndProductNameAndCountryName(gscCountrySpecificDocumentBean.getDocumentName(),
						gscCountrySpecificDocumentBean.getProductName(),
						gscCountrySpecificDocumentBean.getCountryName())
				.get().getUID();
	}

	public List<GscCountrySpecificDocumentBean> getDocumentsForProductAndCountry(String productName,
			String iso3CountryCode) {
		return gscCountrySpecificDocumentListRepository.findByProductNameAndCountryCode(productName, iso3CountryCode)
				.stream().map(GscCountrySpecificDocumentListService::toDocumentBean).collect(Collectors.toList());
	}
}
