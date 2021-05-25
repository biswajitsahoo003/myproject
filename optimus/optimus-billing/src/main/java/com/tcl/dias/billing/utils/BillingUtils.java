package com.tcl.dias.billing.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.tcl.dias.billing.constants.ExceptionConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import sun.net.www.protocol.file.FileURLConnection;

/**
 * Utility class for billing and payment related functions. 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SuppressWarnings("restriction")
public class BillingUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingUtils.class);

	/**
	 * Establish connection with the downstream system and fetch the file form the given URL.
	 * 
	 * @param fileUrl
	 * @return {@InputStream}
	 */
	public static InputStream fetchFileFromClient(String fileUrl) {
		Client client = ClientBuilder.newClient();
		return client.target(fileUrl).request().get(InputStream.class);
	}

	/**
	 * Establish connection with the local system and fetch the file form the given URL.
	 * 
	 * @param fileUrl
	 * @return {@InputStream}
	 */
	private static InputStream fetchFile(String url) throws IOException {
		URL obj = new URL(url);
		FileURLConnection con = (FileURLConnection) obj.openConnection();
		return con.getInputStream();
	}
	
	/**
	 * Write the files to the given folderPath.
	 * 
	 * @param fileName
	 * @param fileUrl
	 * @param folderPath
	 * @throws IOException
	 */
	public static void writeFile(String fileName, String fileUrl, String folderPath) throws IOException {
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			bis = new BufferedInputStream(new URL(fileUrl).openStream());
			fos = new FileOutputStream(folderPath.concat(fileName));
			byte data[] = new byte[1024];
			int count;
			while ((count = bis.read(data, 0, 1024)) != -1) {
				fos.write(data, 0, count);
			}
		} finally {
			if (bis != null)
				bis.close();
			if (fos != null)
				fos.close();
		}
	}

	/**
	 * Convert from Json to class type given.
	 * 
	 * @param jsonStr
	 * @param valueType
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, TypeReference<T> valueType) {
		T object = null;
		try {
			object = new ObjectMapper().readValue(jsonStr, valueType);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return object;
	}

	/**
	 * Read files from the given "invoicesFilePath" and create a zip file in the "zipFilePath",
	 * Open a ZipOutputStream and return it.
	 * 
	 * @param invoicesFilePath
	 * @param zipFilePath
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	public static InputStream addFilesToZip(String invoicesFilePath, String zipFilePath)
			throws TclCommonException, IOException {
		InputStream is = null;
		
		Path zipPath = Files.createFile(Paths.get(zipFilePath));
		ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath));
		try {
			Path filePath = Paths.get(invoicesFilePath);
			Files.walk(filePath).filter(path -> !path.toFile().isDirectory()).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(filePath.relativize(path).toString());
				try {
					zos.putNextEntry(zipEntry);
					Files.copy(path, zos);
				} catch (IOException ex) {
					throw new TclCommonRuntimeException(ExceptionConstants.ERROR_ZIP_FOLDER, ex,
							ResponseResource.R_CODE_ERROR);
				}
			});
			is = fetchFile("file:///" + zipFilePath.replace("\\", "//"));
			
			// Remove the temp directory
			removeDirectory(invoicesFilePath);
		} finally {
			if(zos != null)
				zos.close();
		}
		return is;
	}

	/**
	 * To create directory in the given "folderPath".
	 * 
	 * @param folderPath
	 */
	public static void createDirectory(String folderPath) {
		File directory = new File(folderPath);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}
	
	/**
	 * To delete the directory in the give "path".
	 * 
	 * @param path
	 * @throws TclCommonException
	 */
	public static void removeDirectory(String path) throws TclCommonException {
		try {
			File file = new File(path);
			if(file.exists()){
				FileUtils.deleteDirectory(file);
			}
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.ERROR_DELETING_FOLDER);
		}
	}
	
	
	public static String getDate(Object obj) {
		try {
				Date date = new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(obj));
			    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");  
			    
			    return  dateFormat.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}  
		
		
		return null;
	}
	
	public static void validateSqlInjectionInput(String inputParam) throws TclCommonException {
		if (StringUtils.isNotBlank(inputParam)) {
			if (inputParam.toLowerCase().contains(" or ") || inputParam.toLowerCase().contains(" and ") || inputParam.contains("=") || inputParam.contains("*")
					|| inputParam.toLowerCase().contains("delete") || inputParam.toLowerCase().contains("update")
					|| inputParam.toLowerCase().contains("select") || inputParam.toLowerCase().contains("!")) {
				LOGGER.info("vulnarable input detected {}", inputParam);
				throw new TclCommonException("billing.validation.sql");
			} else {
				LOGGER.info("Input is safe {}", inputParam);
			}
		}
	}
	
}