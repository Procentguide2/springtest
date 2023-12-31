package com.example.springtest.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "item_code")
    private Integer itemCode;

    @Column(name = "item_name", length = 100)
    private String itemName;

    @Column(name = "item_quantity")
    private Integer itemQuantity;

    @Column(name = "item_status", length = 100)
    private String itemStatus;

    public Product() {
    }

    public Product(LocalDate entryDate, Integer itemCode, String itemName, Integer itemQuantity, String itemStatus) {
        this.entryDate = entryDate;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemStatus = itemStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getItemCode() {
        return itemCode;
    }

    public void setItemCode(Integer itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

}