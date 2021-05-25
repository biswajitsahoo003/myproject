package com.tcl.dias.networkaugmentation.service;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface IDocumentService {
    public void createFolder(String folderName) throws TclCommonRuntimeException;
    public ArrayList<String> getFolderList() throws TclCommonRuntimeException;
    public ArrayList<String> getFileList(String folderName) throws TclCommonRuntimeException;
    public void uploadFile(String folderName, MultipartFile file) throws TclCommonRuntimeException, TclCommonException;
    public void uploadFiles(String folderName, MultipartFile[] files) throws TclCommonRuntimeException;
    public byte[] downloadFile(String folderName, String fileName) throws TclCommonRuntimeException, TclCommonException;
    public void deleteFolder(String folderName) throws TclCommonRuntimeException;
    public void deleteFile(String folderName, String fileName) throws TclCommonRuntimeException;
}
