package com.example.api.dto;

import java.util.List;

public class PagingCustomerDto {

    private CustomerDto filterCustomers;

    private List<CustomerDto> customers;

    private int pageNumber;

    private String sortBy;

    private int pageSize;

    private int numberOfPages;

    private int totalRecords;

    public CustomerDto getFilterCustomers() {
        return filterCustomers;
    }

    public void setFilterCustomers(CustomerDto filterCustomers) {
        this.filterCustomers = filterCustomers;
    }

    public List<CustomerDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDto> customers) {
        this.customers = customers;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
        int numberOfPages = (int) totalRecords / getPageSize();
        if ((totalRecords % getPageSize()) > 0) numberOfPages++;
        setNumberOfPages(numberOfPages);
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
