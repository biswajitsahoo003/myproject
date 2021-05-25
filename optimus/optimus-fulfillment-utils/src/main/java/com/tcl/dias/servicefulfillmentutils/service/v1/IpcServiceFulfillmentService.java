package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ProductSolutionBean;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author Ramalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for the document related
 *            service
 */
@Service
public class IpcServiceFulfillmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpcServiceFulfillmentService.class);

	@Autowired
	ScProductDetailRepository scProductDetailRepository;

	@Autowired
	ScProductDetailAttributeRepository scProductDetailAttributeRepository;

	@Value("${rabbitmq.ipc.si.solutions.queue}")
	String ipcSiSolutionsQueue;

	@Autowired
	MQUtils mqUtils;

	@Transactional
	public String loadDataFromInvAndUpdateO2c( String serviceCode) throws TclCommonException {
		String ipcSiSolutionsQueueResponse = (String) mqUtils.sendAndReceive(ipcSiSolutionsQueue, serviceCode);
		LOGGER.info("IPC SI Solutions Queue Response in HandOver Note :: {}" , ipcSiSolutionsQueueResponse);
		ProductSolutionBean[] productSolutions = null;
		if(ipcSiSolutionsQueueResponse != null && !ipcSiSolutionsQueueResponse.isEmpty()) {
			productSolutions = Utils.convertJsonToObject(ipcSiSolutionsQueueResponse, ProductSolutionBean[].class);
		}
		if(productSolutions != null) {
			List<ProductSolutionBean> sortedProductSolutions = Arrays.asList(productSolutions);
			sortedProductSolutions.forEach(prod -> {
				prod.getCloudSolutions().forEach(cloudSol -> {
					cloudSol.getComponents().forEach(comp -> {
						comp.getAttributes().stream().filter(x -> IpcConstants.HOST_NAME.equals(x.getName())).forEach(attr -> {
							ScProductDetail scProductDetail = scProductDetailRepository.findFirstByCloudCodeOrderByIdDesc(cloudSol.getCloudCode());
							ScProductDetailAttribute scProductDetailAttribute = scProductDetailAttributeRepository.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(), IpcConstants.HOST_NAME, IpcConstants.IPC_COMMON);
							if(scProductDetailAttribute != null) {
								scProductDetailAttribute.setAttributeValue(attr.getValue());
								scProductDetailAttribute.setUpdatedBy("InvSystem");
								scProductDetailAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
							} else {
								scProductDetailAttribute = new ScProductDetailAttribute();
								scProductDetailAttribute.setScProductDetail(scProductDetail);
								scProductDetailAttribute.setCategory(IpcConstants.IPC_COMMON);
								scProductDetailAttribute.setAttributeName(IpcConstants.HOST_NAME);
								scProductDetailAttribute.setAttributeValue(attr.getValue());
								scProductDetailAttribute.setIsActive(IpcConstants.Y);
								scProductDetailAttribute.setCreatedBy("InvSystem");
								scProductDetailAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
								scProductDetailAttributeRepository.save(scProductDetailAttribute);
							}
						});
					});
				});
			});
		}
		return IpcConstants.SUCCESS;
	}

}