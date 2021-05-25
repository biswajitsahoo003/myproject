package com.tcl.dias.wfe.feasibility.service;

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
import org.springframework.stereotype.Service;

@Service
public class DroolsService {


	@Bean(name = "policy")
	public KieSession kieSession() {
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		Resource resrc = ResourceFactory.newClassPathResource("feasiblityRank2.xls"); // Move it into property
		resrc.setResourceType(ResourceType.DTABLE);
		kieFileSystem.write(resrc);
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		KieModule kieModule = kieBuilder.getKieModule();

		KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
		return kieContainer.newKieSession();
	}
}
