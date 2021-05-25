package com.tcl.dias.l2oworkflowutils.service.v1;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttributeServiceTest {

    @Autowired
    private AttributeServiceImpl attributeServiceImpl;

    @Test
    public void shouldReturnAttributeVal() throws TclCommonException{

    	Map<String, Object> feAttributeValMap = attributeServiceImpl.getTaskAttributes("arrange-field-engineer-osp", 483);
    	System.out.println(feAttributeValMap);
    	Map<String, Object> attributeValMap = attributeServiceImpl.getTaskAttributes("lm-conduct-site-survey", 474);
    	Map<String, Object> clrAttributeValMap = attributeServiceImpl.getTaskAttributes("enrich-service-design", 474);
    	Map<String, Object> attachmentListMap = attributeServiceImpl.getTaskAttributes("validate-supporting-document", 474);
    	
    	
    	assertNotNull(feAttributeValMap);
        assertNotNull(attributeValMap);
        assertNotNull(clrAttributeValMap);
        assertNotNull(attachmentListMap);
    }

}