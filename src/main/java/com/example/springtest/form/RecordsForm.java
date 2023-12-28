package com.example.springtest.form;

import com.example.springtest.dto.ProductDto;

import java.util.List;

public class RecordsForm {

    private List<ProductDto> records;

    public RecordsForm() {
    }

    public RecordsForm(List<ProductDto> productList) {
        this.records = productList;
    }

    public List<ProductDto> getRecords() {
        return records;
    }

    public void setRecords(List<ProductDto> records) {
        this.records = records;
    }
}
