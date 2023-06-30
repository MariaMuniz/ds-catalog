package com.dscatalog.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dscatalog.catalog.entities.Product;
import com.dscatalog.catalog.tests.Factory;

@DataJpaTest
public class ProductRepositoriesTests {

	@Autowired
	private ProductRepository repository;
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
    void setUp() throws Exception {
		existingId = 1L;
		nonExistingId =1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersitiWithAutoicrementWhenIdNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts +1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
	
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void optionalNotEmpatylShouldObjectWhenIdExists() {
	
		repository.findById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void optionalEmpatylShouldObjectWhenIdNotExists() {
	
		repository.findById(nonExistingId);
		Optional<Product> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}
	
}
