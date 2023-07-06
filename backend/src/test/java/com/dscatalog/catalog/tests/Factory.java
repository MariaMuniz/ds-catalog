package com.dscatalog.catalog.tests;

import java.time.Instant;

import com.dscatalog.catalog.dto.ProductDTO;
import com.dscatalog.catalog.entities.Category;
import com.dscatalog.catalog.entities.Product;

public class Factory {
	
	
	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone",8000.0, "https://img.com/img.png",Instant.parse("2023-06-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletronics"));
		return product;
	}
	
	
 public static ProductDTO createProductDTO() {
	 Product product =createProduct();
	 return new ProductDTO(product, product.getCategories());
	 
 }
}
