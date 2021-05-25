package com.tcl.dias.oms.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.PartnerDocumentBean;

@Service
public class PartnerOpportunityObjectCreator {

    public PartnerDocumentBean createPartnerDocumentBean() {
        PartnerDocumentBean partnerDocumentBean = new PartnerDocumentBean();
        partnerDocumentBean.setName("testName");
        partnerDocumentBean.setId(23);
        return partnerDocumentBean;
    }

    public MultipartFile returnFile() {
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "testname";
            }

            @Override
            public String getOriginalFilename() {
                return "testfile";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
        return file;
    }
//public PartnerDocumentBean updateUploadObjectConfigurationDocument(int id,String path){
//    PartnerDocumentBean partnerDocumentBean = new PartnerDocumentBean();
//    partnerDocumentBean.setId(id);
//    partnerDocumentBean.setName(path);
//    return partnerDocumentBean;
//}
}
