package com.dscatalog.catalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dscatalog.catalog.entities.Category;
import com.dscatalog.catalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository  repository;

	public List<Category>findAll(){
		return repository.findAll();
	}
}
