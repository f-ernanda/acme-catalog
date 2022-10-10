package com.acme.catalog.services;

import com.acme.catalog.dto.CategoryDTO;
import com.acme.catalog.entities.Category;
import com.acme.catalog.repositories.CategoryRepository;
import com.acme.catalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> entities = repository.findAll();
        return entities.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalEntity = repository.findById(id);
        Category entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Entity not found."));
        return new CategoryDTO(entity);
    }
}
