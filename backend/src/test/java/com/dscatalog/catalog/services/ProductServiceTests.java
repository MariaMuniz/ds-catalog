package com.dscatalog.catalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dscatalog.catalog.dto.ProductDTO;
import com.dscatalog.catalog.entities.Product;
import com.dscatalog.catalog.repositories.ProductRepository;
import com.dscatalog.catalog.services.exceptions.DatabaseException;
import com.dscatalog.catalog.services.exceptions.ResourceNotFoundException;
import com.dscatalog.catalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product>page;
	private Product product;
	
	//private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId =1L;
		nonExistingId=2L;
		dependentId=3L;
		product =Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		

		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
	}
	
	
@Test
public void findByIdShouldProductDtoWhenIdExists() {
	
	//repository.findById(existingId);
	//Optional<ProductDTO> dto = repository.findById(existingId);
	//Assertions.assertTrue(dto.isPresent());

}
	
@Test
public void findAllPagedShoulReturnPage(){
	
	Pageable pageable =PageRequest.of(0, 10);
	Page<ProductDTO> result = service.findAllPaged(pageable);
	
	Assertions.assertNotNull(result);
	Mockito.verify(repository).findAll(pageable);
	
}
	
@Test
public void deleteShouldDoNothinghWhenIdExists() {
Assertions.assertDoesNotThrow(()->{
	service.delete(existingId);
});
}

@Test
public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

	Assertions.assertThrows(ResourceNotFoundException.class, () -> {
		service.delete(nonExistingId);
	});
}
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});

}
}
