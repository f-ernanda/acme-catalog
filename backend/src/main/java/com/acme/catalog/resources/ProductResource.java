package com.acme.catalog.resources;

import com.acme.catalog.dto.ProductDTO;
import com.acme.catalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllPaged(Pageable pageable) {
        Page<ProductDTO> productsDTO = productService.findAllPaged(pageable);
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO productDTO = productService.findById(id);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.insert(productDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProductDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.update(id, productDTO);
        return ResponseEntity.ok(savedProductDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
