package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * This file contains the PagingResults.java class.
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class PagedResultWithTimestamp<T> {
    public static final long DEFAULT_OFFSET = 0;
    public static final int DEFAULT_MAX_NO_OF_ROWS = 100;
    private long totalItems;
    private int totalPages;
    private List<T> elements;
    private String timestamp;

    public PagedResultWithTimestamp(List<T> elements, long totalItems, int totalPages, String timestamp) {
        this.elements = elements;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

