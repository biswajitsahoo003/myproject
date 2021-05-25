package com.tcl.dias.rules.service;

import com.tcl.dias.common.teamsdr.beans.SolutionBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesRequest;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesResponse;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesResponseWrapper;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tcl.dias.rules.constants.TeamsDRRulesConstants.GSIP;
import static com.tcl.dias.rules.constants.TeamsDRRulesConstants.KSESSION_RULES;
import static com.tcl.dias.rules.constants.TeamsDRRulesConstants.KSESSION_TEAMSDR_RULES;
import static com.tcl.dias.rules.constants.TeamsDRRulesConstants.MANAGED_PLAN;
import static com.tcl.dias.rules.constants.TeamsDRRulesConstants.MEDIA_GATEWAY;
import static com.tcl.dias.rules.constants.TeamsDRRulesConstants.PLAN;

/**
 * This file contains the rules business logic
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class TeamsDRRulesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRRulesService.class);

    @Autowired
    @Qualifier(value = "teamsdr")
    private KieSession kSession;

    /**
     * Method to fire all the rules.
     * @param request
     * @return
     */
    public TeamsDRRulesResponseWrapper segregate(TeamsDRRulesRequest request){

        // Inserting solutions in ksession.
        request.getSolutions().forEach(kSession::insert);

        Map<Integer, List<SolutionBean>> resultSet = new HashMap();

        kSession.setGlobal("ucaasProducts", resultSet);
        kSession.setGlobal("productSolutions", new ArrayList<SolutionBean>());
        kSession.setGlobal("productSolutions1", new ArrayList<SolutionBean>());
        kSession.setGlobal("productSolutions2", new ArrayList<SolutionBean>());

        // fire all rules.
        kSession.fireAllRules();

        List<TeamsDRRulesResponse> response = new ArrayList<>();

        for (Map.Entry<Integer, List<SolutionBean>> entry : resultSet.entrySet()) {
            TeamsDRRulesResponse rulesResponse = new TeamsDRRulesResponse();
            rulesResponse.setKey(entry.getKey());
            rulesResponse.setSolutionBeans(entry.getValue());
            for(SolutionBean prdSol : entry.getValue()){
                System.out.println(entry.getKey() + " | " + prdSol.getOfferingName() + " | " + prdSol.getCountry()  + " | " + prdSol.getLeId()
                        + " | " + prdSol.getQuoteToLeId());
            }
            response.add(rulesResponse);
        }

        TeamsDRRulesResponseWrapper responseWrapper = new TeamsDRRulesResponseWrapper();
        responseWrapper.setResponse(response);

        return responseWrapper;
    }

    /**
     * Method to segregate gsc services based on country
     * @param ucaasProducts
     * @param productSolution
     */
    public static void gscSegregation (Map<Integer,List<SolutionBean>> ucaasProducts,SolutionBean productSolution) {
        Integer found = 0;
        for (List<SolutionBean> value : ucaasProducts.values()) {
            SolutionBean prodSol = value.stream().findFirst().get();
            if(prodSol.getProductName().equals(GSIP)){
                if(prodSol.getCountry().equals(productSolution.getCountry())){
                    value.add(productSolution);
                    found = 1;
                    break;
                }
            }
        }

        if(found == 0){
            AtomicInteger max= new AtomicInteger();
            ucaasProducts.keySet().forEach(key-> {if (max.get()<key) max.set(key);});
            Integer key =  max.get()+1;
            List<SolutionBean> productSolutions = new ArrayList<>();
            productSolutions.add(productSolution);
            ucaasProducts.put(key,productSolutions);
        }
    }


    /**
     * Method to segregate based on le id.
     * @param ucaasProducts
     * @param productSolution
     */
    public static void leSegregation (Map<Integer,List<SolutionBean>> ucaasProducts,SolutionBean productSolution) {
        try {
            LOGGER.info("Inside le segregation :: {}", Utils.convertObjectToJson(productSolution));
        } catch (TclCommonException e) {
            e.printStackTrace();
        }
        Integer keyFlag = 0;
        if(productSolution.getProductName().equals(GSIP)){
            for (Map.Entry<Integer, List<SolutionBean>> entry : ucaasProducts.entrySet()) {
               if(!entry.getValue().isEmpty()){
				   SolutionBean prodSol = entry.getValue().stream().findFirst().get();
				   LOGGER.info("Offering name :: {}",prodSol.getOfferingName());
				   if(prodSol.getOfferingName().contains(PLAN) || prodSol.getOfferingName().equals(MEDIA_GATEWAY)){
					   if(productSolution.getLeId().equals(prodSol.getLeId())){
						   keyFlag = entry.getKey();
						   entry.getValue().add(productSolution);
						   break;
					   }
				   }
			   }
            }

            // To remove the solution if exists in another key pair.
            if(keyFlag != 0){
                for (Map.Entry<Integer, List<SolutionBean>> entry : ucaasProducts.entrySet()) {
                    for(SolutionBean prdSol : entry.getValue()){
                        if(!entry.getKey().equals(keyFlag) && (productSolution.getOfferingName().equals(prdSol.getOfferingName()) &&
                                productSolution.getProductName().equals(prdSol.getProductName()) &&
                                productSolution.getCountry().equals(prdSol.getCountry()) &&
                                productSolution.getLeId().equals(prdSol.getLeId()))){
                            List<SolutionBean> newSols = new ArrayList<>(entry.getValue());
                            newSols.remove(prdSol);
                            entry.setValue(newSols);
                        }
                    }
                }
            }
        }

    }
}