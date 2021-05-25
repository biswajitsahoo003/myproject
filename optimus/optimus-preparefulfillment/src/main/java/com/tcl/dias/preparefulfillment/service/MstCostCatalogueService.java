package com.tcl.dias.preparefulfillment.service;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SfdcVendorC;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StgSfdcVendorRepository;

import java.text.DecimalFormat;

@Service
@Transactional
public class MstCostCatalogueService {


	private static final Logger LOGGER = LoggerFactory.getLogger(MstCostCatalogueService.class);

	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;
	
	@Autowired
	StgSfdcVendorRepository stgSfdcVendorRepository;

	public void readExcel(MultipartFile file) {
		try {
			LOGGER.info("Deleting mst Cost catalogue");
			mstCostCatalogueRepository.deleteAll();
			LOGGER.info("Deleted mst Cost catalogue");
			Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8");
			Sheet sheet = workbook.getSheetAt(0);
			String oem = null;
			String bom = null;
			String rmc = null;
			String smc = null;
			for (int i = 4; i < sheet.getLastRowNum(); i++) {
				MstCostCatalogue mstCostCatalogue = new MstCostCatalogue();

				if (sheet.getRow(i).getCell(2).getStringCellValue() != "") {
					oem = sheet.getRow(i).getCell(2).getStringCellValue();
				}
				mstCostCatalogue.setOem(oem);

				if (sheet.getRow(i).getCell(3).getStringCellValue() != "") {
					bom = sheet.getRow(i).getCell(3).getStringCellValue();
				}
				mstCostCatalogue.setBundledBom(bom);

				if (sheet.getRow(i).getCell(4) != null) {
					sheet.getRow(i).getCell(4).setCellType(CellType.STRING);
					if (sheet.getRow(i).getCell(4).getStringCellValue() != "") {
						rmc = sheet.getRow(i).getCell(4).getStringCellValue();
					}
					if (rmc != null && !rmc.contains("Not present")) {
						mstCostCatalogue.setRentalMaterialCode(rmc);
					}
					if("License".equalsIgnoreCase(sheet.getRow(i).getCell(21).getStringCellValue())) {
						mstCostCatalogue.setRentalMaterialCode(null);
					}
				}

				if (sheet.getRow(i).getCell(5) != null) {
					sheet.getRow(i).getCell(5).setCellType(CellType.STRING);
					if (sheet.getRow(i).getCell(5).getStringCellValue() != "") {
						smc = sheet.getRow(i).getCell(5).getStringCellValue();
					}
					if (smc != null && !smc.contains("Not present")) {
						mstCostCatalogue.setSaleMaterialCode(smc);
					}
					if("License".equalsIgnoreCase(sheet.getRow(i).getCell(21).getStringCellValue())) {
						mstCostCatalogue.setSaleMaterialCode(null);
					}
				}
				if (sheet.getRow(i).getCell(6) != null) {
					sheet.getRow(i).getCell(6).setCellType(CellType.STRING);
					if (StringUtils.isNotBlank(sheet.getRow(i).getCell(6).getStringCellValue()))
						mstCostCatalogue.setServiceNumber(sheet.getRow(i).getCell(6).getStringCellValue());
				}
				if (sheet.getRow(i).getCell(7) != null) {
					mstCostCatalogue.setShortText(sheet.getRow(i).getCell(7).getStringCellValue());
				}
				mstCostCatalogue.setProductCode(sheet.getRow(i).getCell(8).getStringCellValue());
				mstCostCatalogue.setDescription(sheet.getRow(i).getCell(9).getStringCellValue());
				mstCostCatalogue.setQuantity((int) sheet.getRow(i).getCell(10).getNumericCellValue());				
				mstCostCatalogue.setPerListPriceUsd(sheet.getRow(i).getCell(11).getNumericCellValue());
				DecimalFormat df = new DecimalFormat("#.##");
				mstCostCatalogue.setTotalListPriceUsd(sheet.getRow(i).getCell(12).getNumericCellValue());
				mstCostCatalogue.setProcurementDiscountPercentage(sheet.getRow(i).getCell(13).getNumericCellValue()*100);
				mstCostCatalogue.setTotalPriceUsd(Double.valueOf(df.format(sheet.getRow(i).getCell(14).getNumericCellValue())));
				mstCostCatalogue.setMarginThreePercentage(Double.valueOf(df.format(sheet.getRow(i).getCell(15).getNumericCellValue())));
				mstCostCatalogue.setTotalPriceMargin(Double.valueOf(df.format(sheet.getRow(i).getCell(16).getNumericCellValue())));
				mstCostCatalogue.setDdpCharge(Double.valueOf(df.format(sheet.getRow(i).getCell(17).getNumericCellValue())));
				mstCostCatalogue.setTotalPriceDdp(Double.valueOf(df.format(sheet.getRow(i).getCell(18).getNumericCellValue())));
				if (sheet.getRow(i).getCell(23) != null) {
					sheet.getRow(i).getCell(23).setCellType(CellType.STRING);
					if (StringUtils.isNotBlank(sheet.getRow(i).getCell(23).getStringCellValue()) && NumberUtils.isNumber(sheet.getRow(i).getCell(23).getStringCellValue())) {
						mstCostCatalogue
								.setIncrementalRate(Double.valueOf(sheet.getRow(i).getCell(23).getStringCellValue()));
					} else {
						mstCostCatalogue.setIncrementalRate(0.0);
					}
				}
				mstCostCatalogue.setHsnCode(String.valueOf((int) sheet.getRow(i).getCell(19).getNumericCellValue()));
				if (StringUtils.isNotBlank(sheet.getRow(i).getCell(21).getStringCellValue()) && !sheet.getRow(i).getCell(21).getStringCellValue().equalsIgnoreCase("-")) {
					mstCostCatalogue.setCategory(sheet.getRow(i).getCell(21).getStringCellValue());
				}
				if (sheet.getRow(i).getCell(25) != null) {
					sheet.getRow(i).getCell(25).setCellType(CellType.STRING);
					if(StringUtils.isNotBlank(sheet.getRow(i).getCell(25).getStringCellValue())) {
						mstCostCatalogue.setVendorCode(sheet.getRow(i).getCell(25).getStringCellValue());
						Stg0SfdcVendorC stgSfdcVendor=stgSfdcVendorRepository.findByVendorIdCAndCompanyCodeC(sheet.getRow(i).getCell(25).getStringCellValue(), "VSIN");
						if(stgSfdcVendor!=null){
							mstCostCatalogue.setVendorName(stgSfdcVendor.getName());
						}
					}
				}
				if (sheet.getRow(i).getCell(26) != null && StringUtils.isNotBlank(sheet.getRow(i).getCell(26).getStringCellValue())) {
					sheet.getRow(i).getCell(26).setCellType(CellType.STRING);
					mstCostCatalogue.setCurrency(sheet.getRow(i).getCell(26).getStringCellValue());
				}
				
				mstCostCatalogueRepository.save(mstCostCatalogue);

			}
			LOGGER.info("Loaded Mst cost Catalogue");
		} catch (Exception e) {
			LOGGER.error("Error in loading cost catalogue", e);
		}

	}

}
