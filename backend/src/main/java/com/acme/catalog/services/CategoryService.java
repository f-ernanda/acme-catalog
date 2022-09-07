package com.acme.catalog.services;

import com.acme.catalog.entities.Category;
import com.acme.catalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }
}
