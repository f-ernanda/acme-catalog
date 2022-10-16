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
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> page = categoryRepository.findAll(pageRequest);
        return page.map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalEntity = categoryRepository.findById(id);
        Category categoryEntity = optionalEntity.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return new CategoryDTO(categoryEntity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category categoryEntity = new Category();
        categoryEntity.setName(categoryDTO.getName());
        Category savedCategoryEntity = categoryRepository.save(categoryEntity);
        return new CategoryDTO(savedCategoryEntity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category categoryEntity = categoryRepository.getReferenceById(id);
            categoryEntity.setName(categoryDTO.getName());
            Category savedCategoryEntity = categoryRepository.save(categoryEntity);
            return new CategoryDTO(savedCategoryEntity);
        }
        catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Category cannot be deleted as it contains products");
        }
    }
}
