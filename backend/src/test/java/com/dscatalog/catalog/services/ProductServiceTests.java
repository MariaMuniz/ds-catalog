package com.dscatalog.catalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dscatalog.catalog.repositories.ProductRepository;
import com.dscatalog.catalog.services.exceptions.DatabaseException;
import com.dscatalog.catalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId =1L;
		nonExistingId=2L;
		dependentId=3L;
		
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
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