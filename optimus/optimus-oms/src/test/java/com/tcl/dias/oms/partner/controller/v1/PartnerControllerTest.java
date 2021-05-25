package com.tcl.dias.oms.partner.controller.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.utils.PartnerOpportunityObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartnerControllerTest {

    @MockBean
    PartnerService partnerService;


    @Autowired
    PartnerOpportunityObjectCreator partnerOpportunityObjectCreator;
//    @Test
//    public void createPartnerOpportunityTest(){
////        PartnerOpportunityBean response = Mockito.when(partnerService.createPartnerOpportunity())
//    }


    @Test
    public void TestUploadOpportunityFileStorageNotNull() throws TclCommonException {
        Mockito.when(partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile())).thenReturn(partnerOpportunityObjectCreator.createPartnerDocumentBean());
        PartnerDocumentBean response = partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertNotNull(responseResource);
    }

    @Test
    public void TestUploadOpportunityFileStorageStatus() throws TclCommonException {
        Mockito.when(partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile())).thenReturn(partnerOpportunityObjectCreator.createPartnerDocumentBean());
        PartnerDocumentBean response = partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertEquals(responseResource.getStatus(), Status.SUCCESS);
    }

    @Test
    public void TestUploadOpportunityObjectStorageNotNull() throws TclCommonException {
        Mockito.when(partnerService.uploadOpportunityObjectStorage(partnerOpportunityObjectCreator.returnFile())).thenReturn(partnerOpportunityObjectCreator.createPartnerDocumentBean());
        PartnerDocumentBean response = partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertNotNull(responseResource);
    }

    @Test
    public void TestUploadOpportunityObjectStorageStatus() throws TclCommonException {
        Mockito.when(partnerService.uploadOpportunityObjectStorage(partnerOpportunityObjectCreator.returnFile())).thenReturn(partnerOpportunityObjectCreator.createPartnerDocumentBean());
        PartnerDocumentBean response = partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertEquals(responseResource.getStatus(), Status.SUCCESS);
    }

    @Test
    public void TestUpdateUploadOpportunityObjectStorageNotNull() throws TclCommonException {
        Mockito.when(partnerService.updateUploadObjectConfigurationDocument("123", "testpath")).thenReturn(partnerOpportunityObjectCreator.createPartnerDocumentBean());
        PartnerDocumentBean response;
        response = partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertNotNull(responseResource);
    }

    @Test
    public void TestUpdateUploadOpportunityObjectStorageStatus() throws TclCommonException {
        Mockito.when(partnerService.updateUploadObjectConfigurationDocument("123", "testpath")).thenReturn(partnerOpportunityObjectCreator.createPartnerDocumentBean());
        PartnerDocumentBean response;
        response = partnerService.uploadOpportunityFileStorage(partnerOpportunityObjectCreator.returnFile());
        ResponseResource responseResource = new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
        assertEquals(responseResource.getStatus(), Status.SUCCESS);
    }


}
