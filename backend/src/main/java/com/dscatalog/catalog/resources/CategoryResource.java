package com.dscatalog.catalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dscatalog.catalog.entities.Category;
import com.dscatalog.catalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	@Autowired
	public CategoryService service;
	@GetMapping
	public ResponseEntity<List<Category>>findAll(){
		
		List<Category>list= service.findAll();
		return ResponseEntity.ok().body(list);
	}
	

}
