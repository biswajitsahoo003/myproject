package com.tcl.dias.oms.partner.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareSessionBean;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.thirdpartysystem.relayware.RelayWareServiceSessionResponse;
import com.tcl.dias.oms.partner.thirdpartysystem.relayware.RelayWareServiceTrainingResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service layer related to all relay ware data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class RelayWareService {

    @Autowired
    RestClientService restClientService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${relayware.service.authenticate.url}")
    private String authenticateUrl;

    @Value("${relayware.service.training.url}")
    private String trainingUrl;

    @Value("${relayware.service.username}")
    private String username;

    @Value("${relayware.service.password}")
    private String password;

    private static final Logger LOGGER = LoggerFactory.getLogger(RelayWareService.class);

    /**
     * Method to get Session Id
     *
     * @return {@link RelayWareSessionBean}
     */
    public RelayWareSessionBean getSessionId() {
        RelayWareSessionBean relayWareSessionBean = new RelayWareSessionBean();
        relayWareSessionBean.setSessionId(createSession().getSESSIONID());
        return relayWareSessionBean;
    }

    /**
     * Method to create Session
     *
     * @return {@link RelayWareServiceSessionResponse}
     */
    private RelayWareServiceSessionResponse createSession() {
        RelayWareServiceSessionResponse relayWareServiceSessionResponse = new RelayWareServiceSessionResponse();
        try {
            MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
            formBody.add(PartnerConstants.RELAY_WARE_AUTHENTICATION_USERNAME, username);
            formBody.add(PartnerConstants.RELAY_WARE_AUTHENTICATION_PASSWORD, password);
            LOGGER.info("relay ware user name : {}", username);
            LOGGER.info("relay ware password: {}", password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            LOGGER.info("token request for relayware {}", formBody);
            RestResponse response = restClientService.postWithUrlEncodedRequest(authenticateUrl, formBody, requestHeaders);

            LOGGER.info("response status : {}", response.getStatus());
            if (response.getStatus() == Status.SUCCESS) {
                LOGGER.info("SessionID from relay ware {}", response);
                relayWareServiceSessionResponse = (RelayWareServiceSessionResponse) Utils.convertJsonToObject(response.getData(), RelayWareServiceSessionResponse.class);
            }
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_RELAY_WARE_SESSIONID, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return relayWareServiceSessionResponse;
    }

    /**
     * Method to get Relay Ware Trainings
     *
     * @return {@link List}
     */
    public List<RelayWareServiceTrainingResponse> getTrainings() {
        // TODO: Pass date as param for relay ware is yet to be confirmed
        //LocalDate.now()
        String sessionID = createSession().getSESSIONID();
        List<RelayWareServiceTrainingResponse> relayWareResponse = new ArrayList<>();
        try {
            if (Objects.nonNull(sessionID)) {
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.add(PartnerConstants.RELAY_WARE_SESSIONID_HEADER, sessionID);
                RestResponse response = restClientService.getWithQueryParam(trainingUrl, null, requestHeaders);
                if (response.getStatus().equals(Status.SUCCESS)) {
                    if (Objects.nonNull(response)) {
                        relayWareResponse = GscUtils.fromJson(response.getData(), new TypeReference<List<RelayWareServiceTrainingResponse>>() {
                        });
                    }
                } else {
                    throw new TclCommonRuntimeException(String.format("Error occurred while getting relay Ware Details: %s", response.getErrorMessage()));
                }
            }
        } catch (Exception e) {
            LOGGER.info("Error is relay ware training Response", e);
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_RELAY_WARE_TRAININGS, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return relayWareResponse;
    }


}
