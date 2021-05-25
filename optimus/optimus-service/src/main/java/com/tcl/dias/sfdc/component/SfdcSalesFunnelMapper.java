package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcSalesFunnelRequestBean;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.stereotype.Component;

/**
 * Mapper for SFDC sales Funnel Data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class SfdcSalesFunnelMapper implements ISfdcMapper {

    @Override
    public BaseBean transfortToSfdcRequest(String inputRequest) throws TclCommonException {
        SfdcSalesFunnelRequestBean salesFunnelRequestBean = null;

        if (inputRequest != null) {
             salesFunnelRequestBean = (SfdcSalesFunnelRequestBean) Utils.convertJsonToObject(inputRequest,
                    SfdcSalesFunnelRequestBean.class);
        }

        return salesFunnelRequestBean;
    }

}
