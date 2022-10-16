package com.acme.catalog.services;

import com.acme.catalog.dto.CategoryDTO;
import com.acme.catalog.dto.ProductDTO;
import com.acme.catalog.entities.Category;
import com.acme.catalog.entities.Product;
import com.acme.catalog.repositories.CategoryRepository;
import com.acme.catalog.repositories.ProductRepository;
import com.acme.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> page = productRepository.findAll(pageRequest);
        return page.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optionalEntity = productRepository.findById(id);
        Product productEntity = optionalEntity.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return new ProductDTO(productEntity, productEntity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product productEntity = new Product();
        copyDtoToEntity(productDTO, productEntity);
        Product savedProductEntity = productRepository.save(productEntity);
        return new ProductDTO(savedProductEntity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product productEntity = productRepository.getReferenceById(id);
            copyDtoToEntity(productDTO, productEntity);
            Product savedProductEntity = productRepository.save(productEntity);
            return new ProductDTO(savedProductEntity);
        }
        catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product productEntity) {
        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setImgUrl(productDTO.getImgUrl());
        productEntity.setMoment(productDTO.getMoment());

        productEntity.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            productEntity.getCategories().add(category);
        }
    }
}
