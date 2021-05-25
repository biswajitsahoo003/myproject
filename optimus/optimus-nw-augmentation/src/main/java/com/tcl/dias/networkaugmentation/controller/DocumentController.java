package com.tcl.dias.networkaugmentation.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugment.entity.entities.Attachment;
import com.tcl.dias.networkaugmentation.service.IDocumentService;
import com.tcl.dias.networkaugmentation.service.NwaAttachmentService;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupBean;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/v1/document")
public class DocumentController {

    @Autowired
    IDocumentService documentService;

    @Autowired
    private NwaAttachmentService nwaAttachmentService;

    /**
     * this method is used to create folder
     * @param folderName this is the required folder name to be created
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.CREATE_FOLDER)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/createfolder/{folderName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String, Object>> createFolder(
            @PathVariable(value = "folderName") String folderName) {
        try {
            documentService.createFolder(folderName);
            HashMap<String, Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Folder Created Successfully!");
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            HashMap<String, Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Create Folder Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to get list of available folders
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.LIST_FOLDERS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/folders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String, Object>> getFolderList() {
        try {
            List<String> folders = documentService.getFolderList();
            HashMap<String, Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Folders List");
            jsonObject.put("folders", folders);
            System.out.println("==== arrayList size "+jsonObject);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            System.out.println("========= " +ex.getMessage());
            jsonObject.put("message", "Get Folders List Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<Map<String,Object>>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to get list of files available of given folder
     * @param folderName to get list of files under the given folder name
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.LIST_FILES)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/files/{folderName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String,Object>> getFileList(@PathVariable(value = "folderName") String folderName) {
        try {
            List<String> files = documentService.getFileList(folderName);
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Files List");
            jsonObject.put("files", files);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Get Files List Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to get list of files available of given folder
     * @param folderName to get list of files under the given folder name
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.UPLOAD_FILE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/uploadfile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String,Object>> uploadFile(@RequestParam(value = "folderName") String folderName,
                                                           @RequestParam("file") MultipartFile file) {
        try {
            documentService.uploadFile(folderName, file);
//            Attachment response = nwaAttachmentService.uploadAttachment(folderName,file,"","", taskId);
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
//            jsonObject.put("File Info ",response);
            jsonObject.put("message", "File Upload Successful");
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            System.out.println("=========== Exception "+ex.getMessage());
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Upload file Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to get list of files available of given folder
     * @param folderName to get list of files under the given folder name
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.UPLOAD_FILE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/uploadfiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String,Object>> uploadFiles(@RequestParam(value = "folderName") String folderName,
                                                            @RequestParam("files") MultipartFile[] files) {
        try {
            documentService.uploadFiles(folderName, files);
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "File Upload Successful");
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            System.out.println("=========== Exception "+ex.getMessage());
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Upload file Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to create folder
     * @param folderName this is the required folder name to be deleted
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.DELETE_FOLDER)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/deletefolder/{folderName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String,Object>> deleteFolder(
            @PathVariable(value = "folderName") String folderName) {
        try {
            documentService.deleteFolder(folderName);
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Folder Deleted Successfully!");
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Delete Folder Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to create folder
     * @param folderName this is the required folder name to be deleted
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.DELETE_FILE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/deletefile/{folderName}/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Map<String,Object>> deleteFile(
            @PathVariable(value = "folderName") String folderName, @PathVariable(value = "fileName") String fileName) {
        try {
            documentService.deleteFile(folderName, fileName);
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "File Deleted Successfully!");
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, jsonObject,
                    Status.SUCCESS);
        } catch (Exception ex) {
            HashMap<String,Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Delete File Failed");
            jsonObject.put("exception", ex.getMessage());
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, jsonObject,
                    Status.FAILURE);
        }
    }

    /**
     * this method is used to create folder
     * @param folderName this is the required folder name to be deleted
     * @return JSON Object
     * @author Prasad Munaga
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DocumentService.DOWNLOAD_FILE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/download/{folderName}/{fileName}", produces = MediaType.ALL_VALUE)
    public ResponseResource<byte[]> downloadFile(
            @PathVariable(value = "folderName") String folderName, @PathVariable(value = "fileName") String fileName) {
        try {
            byte[] data = documentService.downloadFile(folderName, fileName);
//            Optional<Attachment> response = nwaAttachmentService.getAttachmentDetails(attachmentId);
            /*
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "File Deleted Successfully!");
            jsonObject.put("base64data", Base64.getEncoder().encode(data));
            */
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
                    Status.SUCCESS);
        } catch (Exception ex) {
            HashMap<String, Object> jsonObject = new HashMap<String, Object>();
            jsonObject.put("message", "Delete File Failed");
            jsonObject.put("exception", ex.getMessage());
            String strData = jsonObject.toString();
            byte[] data = strData.getBytes();
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, data,
                    Status.FAILURE);
        }
    }
}
