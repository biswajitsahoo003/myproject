package com.tcl.dias.rules.service;

import com.tcl.dias.common.teamsdr.beans.SolutionBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesRequest;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Common rules service
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class CommonRulesService {

    @Autowired
    @Qualifier(value = "common")
    private KieSession kSession;

    /**
     * To execute common rules
     *
     * @param request
     */
    public void executeRules(TeamsDRRulesRequest request){

        SolutionBean solutionBean = new SolutionBean();
        solutionBean.setOfferingName(request.getSolutions().get(0).getOfferingName());
        System.out.println(solutionBean.toString());

        kSession.insert(solutionBean);

        // fire all rules.
        kSession.fireAllRules();
        System.out.println("Exiting common rule service...");
    }
}
