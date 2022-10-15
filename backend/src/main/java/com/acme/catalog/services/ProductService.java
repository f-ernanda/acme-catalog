package com.acme.catalog.services;

import com.acme.catalog.dto.ProductDTO;
import com.acme.catalog.entities.Product;
import com.acme.catalog.repositories.ProductRepository;
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
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> page = repository.findAll(pageRequest);
        return page.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optionalEntity = repository.findById(id);
        Product entity = optionalEntity.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        //entity.setName(productDTO.getName());
        Product savedEntity = repository.save(entity);
        return new ProductDTO(savedEntity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product entity = repository.getReferenceById(id);
            //entity.setName(productDTO.getName());
            Product savedEntity = repository.save(entity);
            return new ProductDTO(savedEntity);
        }
        catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
        catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Category cannot be deleted as it contains products"); //?
        }
    }
}
