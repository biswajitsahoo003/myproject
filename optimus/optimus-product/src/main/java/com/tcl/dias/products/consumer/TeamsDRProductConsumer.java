package com.tcl.dias.products.consumer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.teamsdr.service.v1.TeamsDRProductServiceMatrixService;

/**
 * Consumer/Listener class for Teams DR product microservice
 *
 * @author Srinivasa Raghavan
 */
@Service
public class TeamsDRProductConsumer {
    @Autowired
    TeamsDRProductServiceMatrixService teamsDRProductServiceMatrixService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRProductConsumer.class);
    /**
     * Queue for getting license details based on the list of countries and
     * aggreementType
     *
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.teamsdr.license.countries.details}") })
    public String getLicenseDetails(String request) {
        String response = "";
        try {
            LOGGER.info("Input Payload received for getting license details:{}", request);
            TeamsDRLicenseRequestBean requestBean = Utils.convertJsonToObject(request, TeamsDRLicenseRequestBean.class);
            response = Utils.convertObjectToJson(
                    teamsDRProductServiceMatrixService.getLicenseDetailsBasedOnCountries(requestBean));
        } catch (Exception e) {
            LOGGER.error("Error in getting License Details ", e);
        }
        return response;
    }

	/**
	 * Get teams DR services level charges from catalog
	 * 
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.teamsdr.service.level.charges}") })
	public String getServiceLevelCharges(String request) {
		String response = "";
		try {
			LOGGER.info("Input Payload received for getting service level charges:{}", request);
			response = Utils.convertObjectToJson(teamsDRProductServiceMatrixService.getServiceLevelCharges());
		} catch (Exception e) {
			LOGGER.error("Error in getting License Details ", e);
		}
		return response;
	}

	/**
	 * Get media gateway vendor name from catalog
	 * 
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${teamsdr.mg.get.vendor.name}") })
	public String getVendorName(String request) {
		String response = "";
		try {
			LOGGER.info("Input Payload received for getting service level charges:{}", request);
			Map<String, String> req = (Map<String, String>) Utils.convertJsonToObject(request, Map.class);
			response = Utils.convertObjectToJson(teamsDRProductServiceMatrixService.getVendorName(req));
		} catch (Exception e) {
			LOGGER.error("Error in getting License Details ", e);
		}
		return response;
	}

	/**
	 * Method to get hsnCodes.
	 * 
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.teamsdr.get.hsncodes}") })
	public String getHSNCodes(String request) {
		String response = "";
		try {
			response = Utils.convertObjectToJson(teamsDRProductServiceMatrixService.getHSNCodes());
			LOGGER.info("Response from queue :: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting hsn codes.", e);
		}
		return response;
	}
}