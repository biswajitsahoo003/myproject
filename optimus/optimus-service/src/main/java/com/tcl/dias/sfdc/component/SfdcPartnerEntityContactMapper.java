package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.sfdc.bean.PartnerEntityContactRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityAccountBean;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityBean;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityContact;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityContactBean;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityContactRequest;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * This file contains the SfdcPartnerEntityContactMapper.java class.
 *
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component
public class SfdcPartnerEntityContactMapper implements ISfdcMapper {
    @Override
    public BaseBean transfortToSfdcRequest(String inputRequest) throws TclCommonException {
        SfdcPartnerEntityContactRequest sfdcPartnerEntityBean = null;

        if (StringUtils.isNotBlank(inputRequest)) {
            PartnerEntityContactRequestBean partnerEntityBean = (PartnerEntityContactRequestBean) Utils.convertJsonToObject(inputRequest,
                    PartnerEntityContactRequestBean.class);
            sfdcPartnerEntityBean = constructPartnerEntityContactRequestBean(partnerEntityBean);
        }

        return sfdcPartnerEntityBean;
    }

    private SfdcPartnerEntityContactRequest constructPartnerEntityContactRequestBean(PartnerEntityContactRequestBean partnerEntityRequestBean){
        SfdcPartnerEntityContactRequest sfdcPartnerEntityContactRequest=new SfdcPartnerEntityContactRequest();
        SfdcPartnerEntityContactBean sfdcPartnerEntityContactBean=new SfdcPartnerEntityContactBean();
        SfdcPartnerEntityContact sfdcPartnerEntityContact=new SfdcPartnerEntityContact();

        sfdcPartnerEntityContactRequest.setSfdcPartnerEntityContactBean(sfdcPartnerEntityContactBean);
        sfdcPartnerEntityContactRequest.setReferenceId(partnerEntityRequestBean.getReferenceId());

        //setting mandatory fields of contact bean creation

        //sfdcPartnerEntityContactBean.setAccountName(partnerEntityRequestBean.getCustomerContactName());
        sfdcPartnerEntityContactBean.setRecordTypeName("Partner Contact");
        sfdcPartnerEntityContactBean.setAccountId(partnerEntityRequestBean.getAccountId18());
        sfdcPartnerEntityContactBean.setCity(partnerEntityRequestBean.getCity());
        sfdcPartnerEntityContactBean.setLegalEntity(partnerEntityRequestBean.getCustomerLegalEntityId());
        sfdcPartnerEntityContactBean.setSfdcPartnerEntityContact(sfdcPartnerEntityContact);


        //setting mandatory fields of contact creation
        sfdcPartnerEntityContact.setFirstName(partnerEntityRequestBean.getFirstName());
        sfdcPartnerEntityContact.setLastName(partnerEntityRequestBean.getLastName());
        sfdcPartnerEntityContact.setEmail(partnerEntityRequestBean.getCustomerContactEmail());
        sfdcPartnerEntityContact.setPhone(partnerEntityRequestBean.getCustomerContactNumber());
        sfdcPartnerEntityContact.setMailingStreet(partnerEntityRequestBean.getStreet());
        sfdcPartnerEntityContact.setAddressLine1(null);
        sfdcPartnerEntityContact.setMailingCity(partnerEntityRequestBean.getCity());
        sfdcPartnerEntityContact.setMailingState(partnerEntityRequestBean.getState());
        sfdcPartnerEntityContact.setMailingZipCode(partnerEntityRequestBean.getPostalCode());
        sfdcPartnerEntityContact.setZipPostalCode(partnerEntityRequestBean.getPostalCode());
        sfdcPartnerEntityContact.setMailingCountry(partnerEntityRequestBean.getCountry());
        sfdcPartnerEntityContact.setMobilePhone(partnerEntityRequestBean.getCustomerContactNumber());
        sfdcPartnerEntityContact.setActive("Yes");
        sfdcPartnerEntityContact.setRole("Other");
       return  sfdcPartnerEntityContactRequest;
    }
}
