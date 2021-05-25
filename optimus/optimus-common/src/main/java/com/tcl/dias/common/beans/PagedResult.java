package com.tcl.dias.common.beans;

import java.util.List;

/**
 * This file contains the PagingResults.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PagedResult<T> {
	public static final long DEFAULT_OFFSET = 0;
	public static final int DEFAULT_MAX_NO_OF_ROWS = 100;
	private long totalItems;
	private int totalPages;
	private List<T> elements;

	public PagedResult(List<T> elements, long totalItems, int totalPages) {
		this.elements = elements;
		this.totalPages = totalPages;
		this.totalItems = totalItems;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}

	public List<T> getElements() {
		return elements;
	}
}
