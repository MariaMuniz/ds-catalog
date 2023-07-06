package com.dscatalog.catalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dscatalog.catalog.dto.ProductDTO;
import com.dscatalog.catalog.entities.Product;
import com.dscatalog.catalog.services.ProductService;
import com.dscatalog.catalog.services.exceptions.DatabaseException;
import com.dscatalog.catalog.services.exceptions.ResourceNotFoundException;
import com.dscatalog.catalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper  objectMapper;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO>page;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	@BeforeEach
	void setUp() throws Exception{
		
		existingId =1L;
		nonExistingId=2L;
		dependentId= 3L;
		
		productDTO = Factory.createProductDTO();
		
		page = new PageImpl<>(List.of(productDTO));
		when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		when(service.findById(existingId)).thenReturn(productDTO);
	
		 when(service.insert(any(ProductDTO.class))).thenReturn(productDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.update(eq(existingId), any())).thenReturn(productDTO);
		when(service.update(eq(nonExistingId),any())).thenThrow(ResourceNotFoundException.class);

	
		///quando é void usar whendepois//
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void insertShouldReturnProductDTOCreated() throws Exception {
		String jsonBody =objectMapper.writeValueAsString(productDTO);
	    ResultActions result = mockMvc.perform(post("/products")
	    		.content(jsonBody)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON));

	    result.andExpect(status().isCreated());
	    result.andExpect(jsonPath("$.id").exists());
	    result.andExpect(jsonPath("$.name").exists());
	    result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
		result.andExpect(jsonPath("$.date").exists());
	}
	

	@Test
	public void deleteShouldNotFoundwhenIdDoesNotExists() throws Exception  {
		
		ResultActions result = 
				mockMvc.perform(delete("/products/{id}",nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldNoContentwhenIdExists() throws Exception  {
		
		ResultActions result = 
				mockMvc.perform(delete("/products/{id}",existingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}
	
	
	@Test
	public void updateShouldReturnProductDTOwhenIdExists() throws Exception  {
		
		String jsonBody =objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	
	}
	@Test
	public void updateShouldReturnNotFoundwhenIdDoesNotExists() throws Exception  {
		String jsonBody =objectMapper.writeValueAsString(productDTO);
			
			ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
			
			result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShoudldReturnProductWhenIdExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void findByIdShoudldReturnNotFoundWhenIdDesNotExist() throws Exception{
		
		ResultActions result = 
				mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
		
	}
}
