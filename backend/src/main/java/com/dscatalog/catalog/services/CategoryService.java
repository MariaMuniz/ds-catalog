package com.dscatalog.catalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.catalog.dto.CategoryDTO;
import com.dscatalog.catalog.entities.Category;
import com.dscatalog.catalog.repositories.CategoryRepository;
import com.dscatalog.catalog.services.exceptions.DatabaseException;
import com.dscatalog.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class CategoryService {
	@Autowired
	private CategoryRepository  repository;
	
@Transactional(readOnly=true)
	public List<CategoryDTO>findAll(){

		List<Category>list= repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
	}

@Transactional(readOnly=true)
public CategoryDTO findById(Long id) {
	Optional<Category> obj = repository.findById(id);
	Category entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not founld"));
	
	return new CategoryDTO(entity);
}

@Transactional(readOnly=true)
public CategoryDTO insert(CategoryDTO dto) {
Category entity = new Category();
entity.setName(dto.getName());
entity = repository.save(entity);
return new CategoryDTO(entity);

}



@Transactional
public CategoryDTO update(Long id, CategoryDTO dto) {
	try {
	Category entity = repository.getReferenceById(id);
entity.setName(dto.getName());
entity = repository.save(entity);
return  new CategoryDTO(entity);
	}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException("Id not fould");
	}
	
}

@Transactional(propagation = Propagation.SUPPORTS)
public void delete(Long id) {
	if (!repository.existsById(id)) {
		throw new ResourceNotFoundException("Recurso não encontrado");
	}
	try {
        	repository.deleteById(id);    		
	}
    	catch (DataIntegrityViolationException e) {
        	throw new DatabaseException("Falha de integridade referencial");
   	}
}


}
