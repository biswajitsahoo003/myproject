package com.tcl.dias.rules.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * To configure drools rule files
 *
 * @author Srinivasa Raghavan
 */
@Configuration
@ComponentScan("com.tcl.dias.rules.service")
public class RulesConfig {

    private static final String commonDrlFile = "Rules/common/CommonRules.drl";

    private static final String teamsDrDrlFile = "Rules/teamsdr/TeamsDRRules.drl";

    @Bean(name = "common")
    public KieSession commonKieSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        Resource resrc = ResourceFactory.newClassPathResource(commonDrlFile);
        resrc.setResourceType(ResourceType.DRL);
        kieFileSystem.write(resrc);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer.newKieSession();
    }

    @Bean(name = "teamsdr")
    public KieSession teamsDRKieSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        Resource resrc = ResourceFactory.newClassPathResource(teamsDrDrlFile);
        resrc.setResourceType(ResourceType.DRL);
        kieFileSystem.write(resrc);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer.newKieSession();
    }

}
