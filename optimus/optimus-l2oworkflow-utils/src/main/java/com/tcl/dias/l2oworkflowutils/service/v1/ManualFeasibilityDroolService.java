package com.tcl.dias.l2oworkflowutils.service.v1;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.kie.api.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Service related to all manual feasibility
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ManualFeasibilityDroolService {

    @Bean(name = "manualPolicy")
    public KieSession kieSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        Resource resource = ResourceFactory.newClassPathResource("feasibilityRank.xls");
        resource.setResourceType(ResourceType.DTABLE);
        kieFileSystem.write(resource);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer.newKieSession();
    }

}
