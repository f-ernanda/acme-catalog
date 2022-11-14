package com.acme.catalog.services;

import com.acme.catalog.dto.ProductDTO;
import com.acme.catalog.entities.Product;
import com.acme.catalog.repositories.ProductRepository;
import com.acme.catalog.services.exceptions.ResourceNotFoundException;
import com.acme.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        //Mock IDs
        existingId = 1L;
        nonExistingId = 100L;

        //Mock product and DTO
        product = Factory.createProduct(existingId);
        productDTO = Factory.createProductDTO();

        //Mock page
        PageImpl<Product> page = new PageImpl<>(List.of(product));

        //Mock findAll
        when(productRepository.findAll((Pageable) any())).thenReturn(page);

        //Mock findById
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Mock save
        when(productRepository.save(any())).thenReturn(product);

        //Mock getReferenceById
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //Mock deleteById
        doNothing().when(productRepository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);
        assertNotNull(result);
    }

    @Test
    public void findByIdShouldReturnDTOWhenIdExists() {
        ProductDTO result = productService.findById(existingId);
        assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingId));
    }

    @Test
    public void updateShouldReturnDTOWhenIdExists() {
        ProductDTO result = productService.update(existingId, productDTO);
        assertNotNull(result);
    }

    @Test
    public void updateShouldThrowExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> productService.update(nonExistingId, productDTO));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> productService.delete(existingId));
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }
}
