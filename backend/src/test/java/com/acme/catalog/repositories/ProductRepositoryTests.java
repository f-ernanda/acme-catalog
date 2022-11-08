package com.acme.catalog.repositories;

import com.acme.catalog.entities.Product;
import com.acme.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long totalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        totalProducts = 20L;
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
        Optional<Product> result = productRepository.findById(existingId);
        assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<Product> result = productRepository.findById(nonExistingId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = productRepository.save(Factory.createProduct());
        assertNotNull(product.getId());
        assertEquals(totalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> productRepository.deleteById(nonExistingId));
    }
}
