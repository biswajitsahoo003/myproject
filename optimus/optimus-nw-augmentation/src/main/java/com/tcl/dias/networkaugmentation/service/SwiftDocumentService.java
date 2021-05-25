package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConditionalOnProperty(name = "document.service.provider", havingValue = "swift")
@Service
public class SwiftDocumentService implements IDocumentService{

    @Value("${swift.auth.url}")
    String authUrl;

    @Value("${swift.documentservice.url}")
    String documentSrvUrl;

    @Value("${swift.username}")
    String swiftUserName;

    @Value("${swift.auth.key}")
    String swiftAuthKey;

    @Autowired
    AttachmentFileStorageService fileStorageService;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Value("${temp.download.url.expiryWindow}")
    String tempDownloadUrlExpiryWindow;

    private String getAuthURL() {
        // return "http://inp44xdapp3221/auth";
        System.out.println("============= auth URL "+authUrl);
        return authUrl;
    }

    private String getDocumentServiceURL() {
        // return "http://inp44xdapp3221/swift/v1";
        System.out.println("============= document service URL "+documentSrvUrl);
        return documentSrvUrl;
    }

    private String getUserName() {
        // return "optnwachn1";
        System.out.println("============= Username "+swiftUserName);
        return swiftUserName;
    }

    private String getAuthKey() {
        // return "nyTwxCcqvjOCQW0ImqJA3QN05PGh8J5oLAKlsdrX";
        System.out.println("============= swiftAuthKey "+swiftAuthKey);
        return swiftAuthKey;
    }

    private RestTemplate getRestTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        return restTemplateBuilder.build();
    }

    private String getAuthToken() throws TclCommonRuntimeException {
        String userName = getUserName();
        String authKey = getAuthKey();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-User", getUserName().trim());
        httpHeaders.add("X-Auth-Key", getAuthKey());
        System.out.println("======Auth Token"+authKey);

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        //ResponseEntity<Void> responseEntity = restTemplate.getForEntity(getAuthURL(), Void.class);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(getAuthURL(),HttpMethod.GET, httpEntity, Void.class);
        HttpHeaders rHttpHeaders = responseEntity.getHeaders();

        List<String> authTokenList = rHttpHeaders.get("X-Auth-Token");
        String authToken = null;
        if(authTokenList != null && authTokenList.size() > 0) {
            authToken = authTokenList.get(0);
        }

        System.out.println("========= auth Token"+ authToken);
        return authToken;
    }

    @Override
    public void createFolder(String folderName) throws TclCommonRuntimeException {
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        String folderUrl = docSrvUrl + "/" + folderName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(folderUrl, HttpMethod.PUT, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Create Folder Failed");
        }
    }

    @Override
    public ArrayList<String> getFolderList() throws TclCommonRuntimeException {
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(docSrvUrl, HttpMethod.GET, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();

        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Get Folder List Failed");
        }
        System.out.println("========= Response "+responseEntity.getBody());
        String[] files = responseEntity.getBody().split("\n");
        ArrayList<String> stringArrayList = new ArrayList<String>(Arrays.asList(files));

        return stringArrayList;
    }

    @Override
    public ArrayList<String> getFileList(String folderName) throws TclCommonRuntimeException {
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        String folderUrl = docSrvUrl + "/" + folderName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(folderUrl, HttpMethod.GET, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Get File List Failed");
        }
        String[] files = responseEntity.getBody().split("\n");
        ArrayList<String> stringArrayList = new ArrayList<String>(Arrays.asList(files));
        return stringArrayList;
    }

    @Override
    public void uploadFile(String folderName, MultipartFile file) throws TclCommonRuntimeException, TclCommonException {
        TempUploadUrlInfo tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
                Long.parseLong(tempUploadUrlExpiryWindow), "nwa");
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        if(file.isEmpty()) return;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<byte[]> entity = null;
        try {
            entity = new HttpEntity<byte[]>(file.getBytes(), httpHeaders);
            System.out.println("============= File content" + new String(file.getBytes()) );

        } catch (IOException e) {
            throw new TclCommonRuntimeException("File Upload Failed: " + e.getMessage());
        }

        String fileName = file.getOriginalFilename();
        String fileUrl = tempUploadUrlInfo + "/" + folderName + "/" + fileName;
        System.out.println("============= File path" + fileUrl);
        ResponseEntity<String> responseEntity = restTemplate.exchange(fileUrl, HttpMethod.PUT, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("File Upload Failed");
        }

        return;
    }

    @Override
    public void uploadFiles(String folderName, MultipartFile[] files) throws TclCommonRuntimeException {
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        System.out.println(" File size is ......."+files.length);
        for(MultipartFile file : files) {
            if(file.isEmpty()) continue;


            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("X-Auth-Token", authToken);
            HttpEntity<byte[]> entity = null;
            try {
                entity = new HttpEntity<byte[]>(file.getBytes(), httpHeaders);
            } catch (IOException e) {
                throw new TclCommonRuntimeException("File Upload Failed: " + e.getMessage());
            }

            String fileName = file.getOriginalFilename();
            String fileUrl = docSrvUrl + "/" + folderName + "/" + fileName;
            System.out.println("========== File url "+fileUrl);
            ResponseEntity<String> responseEntity = restTemplate.exchange(fileUrl, HttpMethod.PUT, entity, String.class);
            HttpStatus status = responseEntity.getStatusCode();
            if(!status.is2xxSuccessful()) {
                throw new TclCommonRuntimeException("File Upload Failed");
            }
        }

        return;
    }

    @Override
    public byte[] downloadFile(String folderName, String fileName) throws TclCommonRuntimeException, TclCommonException {
//        String  containerName = getDocumentServiceURL();
        String tempUploadUrlInfo = fileStorageService.getTempDownloadUrl(
                fileName, Long.parseLong(tempDownloadUrlExpiryWindow),
                folderName,false);
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        String fileUrl = tempUploadUrlInfo+ "/" + folderName + "/" + fileName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(fileUrl, HttpMethod.GET, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("File Download Failed");
        }
        String data = responseEntity.getBody();

        return data.getBytes();
    }

    @Override
    public void deleteFolder(String folderName) throws TclCommonRuntimeException {
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        String folderUrl = docSrvUrl + "/" + folderName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(folderUrl, HttpMethod.DELETE, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("Remove Folder Failed");
        }
        return;
    }

    @Override
    public void deleteFile(String folderName, String fileName) throws TclCommonRuntimeException {
        String docSrvUrl = getDocumentServiceURL();
        RestTemplate restTemplate = getRestTemplate();
        String authToken = getAuthToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", authToken);
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        String fileUrl = docSrvUrl + "/" + folderName + "/" + fileName;
        ResponseEntity<String> responseEntity = restTemplate.exchange(fileUrl, HttpMethod.DELETE, entity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if(!status.is2xxSuccessful()) {
            throw new TclCommonRuntimeException("File Delete Failed");
        }

        return;
    }
}
