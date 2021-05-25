package com.tcl.dias.preparefulfillment.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.preparefulfillment.beans.VmiMasterBean;
import com.tcl.dias.servicefulfillment.entity.entities.MstVmi;
import com.tcl.dias.servicefulfillment.entity.entities.MstVmiTransaction;
import com.tcl.dias.servicefulfillment.entity.repository.MstVmiRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstVmiTransactionRepository;

/**
 * This file contains the VmiLoaderService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class VmiLoaderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VmiLoaderService.class);

	@Autowired
	MstVmiRepository mstVmiRepository;

	@Autowired
	MstVmiTransactionRepository mstVmiTransactionRepository;

	private static String SUM_OF_QTY = "Sum of QTY";
	private static String RENTAL_MAT_CODE = "Rental Mat. Code";
	private static String PROD_CODE = "Product Code";
	private static String SALE_MAT_CODE = "Sale Mat Code";
	private static String GRAND_TOTAL = "Grand Total";
	private static String DESCRIPTION = "Description";
	private static String QTY_LEFT = "Qty Left";
	private static String QTY_LEFT_STOCK="Qty left in Stock";
	private static String SHIPPED_DATE = "Shipped date from Cisco";
	private static String DATE_RECVD = "Date recvd in warehouse";
	private static String DATE_RECD="Date Recd in warehouse";

	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * This method is used to load the vmi data - it removes all the records and
	 * reinserts the records
	 * 
	 * @param file
	 * @return Boolean - whether its success or failure
	 */
	public Boolean processMasterVmi(MultipartFile file) {
		LOGGER.info("Receiving the input vmi file");
		Boolean response = true;
		try {
			List<VmiMasterBean> vmiMasterData = extractVmiData(file);
			if (vmiMasterData != null) {
				LOGGER.info("Deleting all the records in the table to insert the {} no of new records ",
						vmiMasterData.size());
				mstVmiTransactionRepository.deleteAll();
				mstVmiRepository.deleteAll();
				LOGGER.info("Deleted all the records in the table to insert the {} no of new records ",
						vmiMasterData.size());
				List<MstVmi> mstVmis = new ArrayList<>();
				for (VmiMasterBean vmiMasterBean : vmiMasterData) {
					if(vmiMasterBean.getRentalMaterialCode()!=null && vmiMasterBean.getRentalMaterialCode().equals("Grand Total")) {
						continue;
					}
					MstVmi mstVmi = new MstVmi();
					mstVmi.setDescription(vmiMasterBean.getDescription());
					mstVmi.setGrandTotal(vmiMasterBean.getGrandTotal());
					mstVmi.setProductCode(vmiMasterBean.getProductCode());
					mstVmi.setQtyLeft(vmiMasterBean.getQtyLeft());
					mstVmi.setRentalMaterialCode(vmiMasterBean.getRentalMaterialCode());
					mstVmi.setSaleMaterialCode(vmiMasterBean.getSaleMaterialCode());
					mstVmi.setSumOfQty(vmiMasterBean.getSumOfQty());
					mstVmi.setPoLandingDate(vmiMasterBean.getPoLandingDate());
					mstVmi.setPoNumber(vmiMasterBean.getPoNumber());
					mstVmi.setSoNumber(vmiMasterBean.getSoNumber());
					mstVmi.setShippedDateCisco(vmiMasterBean.getShippedDateCisco());
					mstVmi.setReceivedDateWarehouse(vmiMasterBean.getReceivedDateWarehouse());
					mstVmi.setVmiStatusDate(vmiMasterBean.getVmiStatusDate());
					if (vmiMasterBean.getQuantityMapper() != null) {
						List<MstVmiTransaction> mstVmiTran = new ArrayList<>();
						for (Entry<String, String> quantityMap : vmiMasterBean.getQuantityMapper().entrySet()) {
							MstVmiTransaction mstVmiTransaction = new MstVmiTransaction();
							mstVmiTransaction.setMstVmi(mstVmi);
							if (StringUtils.isNotBlank(quantityMap.getValue()))
								mstVmiTransaction.setQuantity(Integer.valueOf(quantityMap.getValue()));
							mstVmiTransaction.setSubPoNumber(quantityMap.getKey());
							mstVmiTran.add(mstVmiTransaction);
						}
						mstVmi.setMstVmiTransactions(mstVmiTran);
					}
					mstVmis.add(mstVmi);
				}
				mstVmiRepository.saveAll(mstVmis);
				LOGGER.info("Inserted all the records in the table with {} no of new records ", vmiMasterData.size());
			} else {
				response = false;
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing the vmi template ", e);
			response = false;
		}
		return response;
	}

	/**
	 * This method extracts the excel
	 * 
	 * @param file
	 * @return the
	 */
	public List<VmiMasterBean> extractVmiData( MultipartFile file) {
		List<VmiMasterBean> vmiMasterDatas = new ArrayList<>();
		Map<String, List<String>> rowMapper = new HashMap<>();
		Map<String, String> headerMapper = new HashMap<>();
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8")) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);

				for (int j = 1; j < 5; j++) {
					if (sheet.getRow(j) == null)
						continue;
					Iterator<Cell> cellItr = sheet.getRow(j).cellIterator();
					String rowName = null;
					String rowData = null;

					while (cellItr.hasNext()) {
						Cell cell = cellItr.next();
						String value = null;
						if (StringUtils.isNotBlank(rowName) && rowName.toLowerCase().contains("date")) {
							CellStyle a=cell.getCellStyle();
							if (!a.getDataFormatString().equals("General")) {
								Date date = cell.getDateCellValue();
								value = date != null ? formatter.format(date) : "";
							} else {
								if (cell.getStringCellValue().contains("/")) {
									SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");
									value = formatter.format(slashFormatter.parse(cell.getStringCellValue()));
								} else {
									String ap="st";
									if(cell.getStringCellValue().contains("th")) {
										ap="th";
									}else if(cell.getStringCellValue().contains("rd")) {
										ap="rd";
									}else if(cell.getStringCellValue().contains("nd")) {
										ap="nd";
									}
									SimpleDateFormat slashFormatter = new SimpleDateFormat("dd'"+ap+"' MMMM yyyy");
									value = formatter.format(slashFormatter.parse(cell.getStringCellValue()));
								}
							}
						} else {
							cell.setCellType(CellType.STRING);
							value = cell.getStringCellValue();
						}
						if (StringUtils.isBlank(rowName)) {
							rowName = value;
						} else if (StringUtils.isNotBlank(rowName)) {
							rowData = value;
							break;
						}
					}
					if (StringUtils.isNotBlank(rowName))
						headerMapper.put(rowName, rowData);
				}

				if (headerMapper.size() <= 3) {
					continue;
				}

				for (int j = 5; sheet.getRow(j)!=null; j++) {
					Iterator<Cell> cellItr = sheet.getRow(j).cellIterator();
					String rowName = null;
					List<String> rowData = new ArrayList<>();

					while (cellItr.hasNext()) {
						Cell cell = cellItr.next();
						String value = null;
						if (StringUtils.isNotBlank(rowName) && rowName.toLowerCase().contains("date")) {
							CellStyle a=cell.getCellStyle();
							if (!a.getDataFormatString().equals("General")) {
								Date date = cell.getDateCellValue();
								value = date != null ? formatter.format(date) : "";
							} else {
								cell.setCellType(CellType.STRING);
								if (StringUtils.isBlank(cell.getStringCellValue())) {
									value="";
								} else {
									if (cell.getStringCellValue().contains("/")) {
										SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");
										value = formatter.format(slashFormatter.parse(cell.getStringCellValue()));
									} else {
										String ap = "st";
										if (cell.getStringCellValue().contains("th")) {
											ap = "th";
										} else if (cell.getStringCellValue().contains("rd")) {
											ap = "rd";
										} else if (cell.getStringCellValue().contains("nd")) {
											ap = "nd";
										}
										SimpleDateFormat slashFormatter = new SimpleDateFormat(
												"dd'" + ap + "' MMMM yyyy");
										value = formatter.format(slashFormatter.parse(cell.getStringCellValue()));
									}
								}
							}
						} else {
							cell.setCellType(CellType.STRING);
							value = cell.getStringCellValue();
						}
						if (rowName == null) {
							rowName = value;
						} else {
							rowData.add(value);
						}
					}
					rowMapper.put(rowName, rowData);
				}
				break;
			}
		} catch (Exception e) {
			LOGGER.error("Error in extracting the data", e);
			return null;
		}
		int count = 0;
		for (Entry<String, List<String>> map : rowMapper.entrySet()) {
			if (count < map.getValue().size()) {
				count = map.getValue().size();
			}

		}
		for (int j = 0; j < count; j++) {
			VmiMasterBean vmiMasterBean = new VmiMasterBean();
			vmiMasterBean.setSoNumber(headerMapper.get("SO#"));
			vmiMasterBean.setPoNumber(headerMapper.get("PO#"));
			vmiMasterBean.setPoLandingDate(headerMapper.get("PO Loading Date"));
			vmiMasterBean.setVmiStatusDate(headerMapper.get("VMI Status Date"));
			vmiMasterDatas.add(vmiMasterBean);
			for (Entry<String, List<String>> rowMap : rowMapper.entrySet()) {
				String keyMap = rowMap.getKey();
				if (rowMap.getValue().size() <= j) {
					continue;
				}
				String value = rowMap.getValue().get(j);

				if (keyMap.equalsIgnoreCase(SUM_OF_QTY)) {
					if (StringUtils.isNotBlank(value))
						vmiMasterBean.setSumOfQty(Integer.valueOf(value));
				}else if (keyMap.equalsIgnoreCase(SHIPPED_DATE)) {
					if (StringUtils.isNotBlank(value))
						vmiMasterBean.setShippedDateCisco(value);
				}else if (keyMap.equalsIgnoreCase(DATE_RECVD) || keyMap.equalsIgnoreCase(DATE_RECD)) {
					if (StringUtils.isNotBlank(value))
						vmiMasterBean.setReceivedDateWarehouse(value);
				} else if (keyMap.equalsIgnoreCase(RENTAL_MAT_CODE)) {
					vmiMasterBean.setRentalMaterialCode(value);
				} else if (keyMap.equalsIgnoreCase(PROD_CODE)) {
					vmiMasterBean.setProductCode(value);
				} else if (keyMap.equalsIgnoreCase(SALE_MAT_CODE)) {
					vmiMasterBean.setSaleMaterialCode(value);
				} else if (keyMap.equalsIgnoreCase(GRAND_TOTAL)) {
					if (StringUtils.isNotBlank(value))
						vmiMasterBean.setGrandTotal(Integer.valueOf(value));
				} else if (keyMap.equalsIgnoreCase(DESCRIPTION)) {
					vmiMasterBean.setDescription(value);
				} else if (keyMap.equalsIgnoreCase(QTY_LEFT) || keyMap.equalsIgnoreCase(QTY_LEFT_STOCK)) {
					if (StringUtils.isNotBlank(value))
						vmiMasterBean.setQtyLeft(Integer.valueOf(value));
				} else {
					vmiMasterBean.getQuantityMapper().put(keyMap, value);
				}

			}
		}
		return vmiMasterDatas;
	}
	
}
