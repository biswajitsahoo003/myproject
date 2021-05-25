package com.tcl.dias.preparefulfillment.service;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.servicefulfillment.entity.entities.MstBudgetMatrix;
import com.tcl.dias.servicefulfillment.entity.repository.MstBudgetMatrixRepository;

@Service
@Transactional
public class MstBudgetMatrixService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MstBudgetMatrixService.class);

	@Autowired
	MstBudgetMatrixRepository mstBudgetMatrixRepository;

	public void readExcel(MultipartFile file) {
		try {
			LOGGER.info("Deleting all the record in budget matrix");
			mstBudgetMatrixRepository.deleteAll();
			LOGGER.info("All the record in budget matrix were deleted");
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				MstBudgetMatrix mstBudgetMatrix = new MstBudgetMatrix();
				mstBudgetMatrix.setProductName(sheet.getRow(i).getCell(1).getStringCellValue());
				mstBudgetMatrix.setEntity(sheet.getRow(i).getCell(2).getStringCellValue());
				mstBudgetMatrix.setCpeType(sheet.getRow(i).getCell(3).getStringCellValue());
				mstBudgetMatrix.setExpenseCategory(sheet.getRow(i).getCell(4).getStringCellValue());
				mstBudgetMatrix.setTypeOfExpenses(sheet.getRow(i).getCell(5).getStringCellValue());
				if (sheet.getRow(i).getCell(6).getStringCellValue() != null
						&& !sheet.getRow(i).getCell(6).getStringCellValue().contains("NA")) {
					mstBudgetMatrix.setWbsLevel1(sheet.getRow(i).getCell(6).getStringCellValue());
				}
				if (sheet.getRow(i).getCell(7).getStringCellValue() != null
						&& !sheet.getRow(i).getCell(7).getStringCellValue().contains("NA")) {
					mstBudgetMatrix.setWbsLevel5(sheet.getRow(i).getCell(7).getStringCellValue());
				}
				if (sheet.getRow(i).getCell(8) != null) {
					sheet.getRow(i).getCell(8).setCellType(CellType.STRING);
					if (sheet.getRow(i).getCell(8).getStringCellValue() != null
							&& !sheet.getRow(i).getCell(8).getStringCellValue().contains("NA")) {
						mstBudgetMatrix.setGl(sheet.getRow(i).getCell(8).getStringCellValue());
					}
				}
				if (sheet.getRow(i).getCell(9).getStringCellValue() != null
						&& !sheet.getRow(i).getCell(9).getStringCellValue().contains("NA")) {
					mstBudgetMatrix.setCostCenter(sheet.getRow(i).getCell(9).getStringCellValue());
				}
				if (sheet.getRow(i).getCell(10) != null) {
					sheet.getRow(i).getCell(10).setCellType(CellType.STRING);
					if (sheet.getRow(i).getCell(10).getStringCellValue() != null
							&& !sheet.getRow(i).getCell(10).getStringCellValue().contains("NA")) {
						mstBudgetMatrix.setDemandId(sheet.getRow(i).getCell(10).getStringCellValue());
					}
				}
				mstBudgetMatrixRepository.save(mstBudgetMatrix);
			}
			LOGGER.info("Budget Matrix is loaded successfully");
		} catch (Exception e) {
			LOGGER.error("Error in reading excel ", e);
		}
	}
}
