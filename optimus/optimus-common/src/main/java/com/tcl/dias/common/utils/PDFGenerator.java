package com.tcl.dias.common.utils;

import java.io.ByteArrayOutputStream;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

/**
 * This file contains the PDFGenerator.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PDFGenerator {

	public static void createPdf(String str, ByteArrayOutputStream outByteStream) throws DocumentException {
		final ITextRenderer iTextRenderer = new ITextRenderer();
		iTextRenderer.setDocumentFromString(str);
		iTextRenderer.layout();
		iTextRenderer.createPDF(outByteStream);
	}
}
