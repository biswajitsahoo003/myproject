package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.DealRegistrationRequestBean;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.stereotype.Component;

/**
 * Mapper for Deal Registration Request Bean
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class DealRegistrationMapper implements ISfdcMapper {

    @Override
    public BaseBean transfortToSfdcRequest(String inputRequest) throws TclCommonException {
        DealRegistrationRequestBean dealRegistrationRequestBean = null;

        if (inputRequest != null) {
            dealRegistrationRequestBean = (DealRegistrationRequestBean) Utils.convertJsonToObject(inputRequest,
                    DealRegistrationRequestBean.class);
        }

        return dealRegistrationRequestBean;
    }

}
