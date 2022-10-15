package com.acme.catalog.services;

import com.acme.catalog.dto.CategoryDTO;
import com.acme.catalog.entities.Category;
import com.acme.catalog.repositories.CategoryRepository;
import com.acme.catalog.services.exceptions.DatabaseException;
import com.acme.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> page = repository.findAll(pageRequest);
        return page.map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalEntity = repository.findById(id);
        Category entity = optionalEntity.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category();
        entity.setName(categoryDTO.getName());
        Category savedEntity = repository.save(entity);
        return new CategoryDTO(savedEntity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(categoryDTO.getName());
            Category savedEntity = repository.save(entity);
            return new CategoryDTO(savedEntity);
        }
        catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Category cannot be deleted as it contains products");
        }
    }
}
