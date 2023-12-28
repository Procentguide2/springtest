package com.example.springtest.service;

import com.example.springtest.dto.ProductDto;
import com.example.springtest.form.RecordsForm;
import com.example.springtest.model.Product;
import com.example.springtest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void saveProducts(RecordsForm form){
        List<ProductDto> products = form.getRecords();
        for (ProductDto productDto : products){
            Product product = new Product(productDto.getEntryDate(),
                    productDto.getItemCode(),
                    productDto.getItemName(),
                    productDto.getItemQuantity(),
                    productDto.getItemStatus());

            productRepository.save(product);
        }

    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

}
