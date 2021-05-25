package com.tcl.dias.networkaugmentation.beans;

import java.util.List;

/**
 * This file contains the Paging class for O2C Dashboard
 *
 *
 * @author Mayank Sharma
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PagedResult<T> {
    public static final long DEFAULT_OFFSET = 0;
    public static final int DEFAULT_MAX_NO_OF_ROWS = 100;
    private long totalItems;
    private int totalPages;
    private List<T> elements;
    private String billedNrc;
    private String billedCircuits;
    private String billedMrc;
    private String filteredMrc;
    private String filteredNrc;


    public PagedResult(List<T> elements, long totalItems, int totalPages, String billedNrc, String billedMrc, String billedCircuits, String filteredMrc, String filteredNrc) {
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.elements = elements;
        this.billedNrc = billedNrc;
        this.billedMrc = billedMrc;
        this.billedCircuits = billedCircuits;
        this.filteredMrc = filteredMrc;
        this.filteredNrc = filteredNrc;
    }

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

    public String getBilledNrc() {
        return billedNrc;
    }

    public void setBilledNrc(String billedNrc) {
        this.billedNrc = billedNrc;
    }

    public String getBilledCircuits() {
        return billedCircuits;
    }

    public void setBilledCircuits(String billedCircuits) {
        this.billedCircuits = billedCircuits;
    }

    public String getBilledMrc() {
        return billedMrc;
    }

    public void setBilledMrc(String billedMrc) {
        this.billedMrc = billedMrc;
    }

    public String getFilteredMrc() {
        return filteredMrc;
    }

    public void setFilteredMrc(String filteredMrc) {
        this.filteredMrc = filteredMrc;
    }

    public String getFilteredNrc() {
        return filteredNrc;
    }

    public void setFilteredNrc(String filteredNrc) {
        this.filteredNrc = filteredNrc;
    }
}
