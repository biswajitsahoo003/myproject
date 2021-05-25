package com.tcl.dias.customer.service.v1;

import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.customer.entity.entities.LegalEntitySapCode;
import com.tcl.dias.customer.entity.repository.SupplierLegalEntityCompanyCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This file contains the Supplier Related Service
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
@Transactional
public class SupplierService {

    @Autowired
    SupplierLegalEntityCompanyCodeRepository supplierLegalEntityCompanyCodeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierService.class);

    public LeSapCodeResponse getSapCodeBasedOnSupplierLe(List<Integer> supplierLeId, String value, String supplierLeType) {
        LOGGER.info("Supplier ID :: {}", supplierLeId);
        LOGGER.info("Value :: {}", value);
        LOGGER.info("Supplier Le Type :: {}", supplierLeType);
        LeSapCodeResponse reponse = new LeSapCodeResponse();
        List<LeSapCodeBean> leSapCodes = new ArrayList<>();
        LeSapCodeBean leSapCodeBean = new LeSapCodeBean();
        if (supplierLeId != null && !supplierLeId.isEmpty()) {

            String type = "SAP Code";
            if (value != null) {
                type = value;
            }

            List<String> legalEntitySapCodes = supplierLegalEntityCompanyCodeRepository.findCompanyCodeBySupplierLeId(supplierLeId.get(0), type, supplierLeType);
            if(Objects.nonNull(legalEntitySapCodes)) {
                leSapCodeBean.setCodeValue(legalEntitySapCodes.get(0));
            }
            leSapCodes.add(leSapCodeBean);
            reponse.setLeSapCodes(leSapCodes);
        }
        LOGGER.info("LeSapCodeResponse :: {}", reponse.toString());
        return reponse;
    }

}
