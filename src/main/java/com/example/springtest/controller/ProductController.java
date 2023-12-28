package com.example.springtest.controller;

import com.example.springtest.form.RecordsForm;
import com.example.springtest.model.Product;
import com.example.springtest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public void saveRecords(@RequestBody RecordsForm form){
        productService.saveProducts(form);
    }

    @GetMapping("/all")
    public List<Product> getProducts(){
        return productService.findAll();
    }


}
