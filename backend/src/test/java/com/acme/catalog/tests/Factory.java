package com.acme.catalog.tests;

import com.acme.catalog.dto.ProductDTO;
import com.acme.catalog.entities.Category;
import com.acme.catalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(null, "Bird Seed V4", "The best seed for your best bird", 40.6, "https://static.wikia.nocookie.net/looneytunes/images/e/e4/Bird_Seed_V4.png/revision/latest/scale-to-width-down/125?cb=20150116050327", Instant.parse("2020-07-13T20:50:00Z"));
        product.getCategories().add(new Category(1L, "Pet Shop"));
        return product;
    }

    public static Product createProduct(Long id) {
        Product product = new Product(id, "Bird Seed V4", "The best seed for your best bird", 40.6, "https://static.wikia.nocookie.net/looneytunes/images/e/e4/Bird_Seed_V4.png/revision/latest/scale-to-width-down/125?cb=20150116050327", Instant.parse("2020-07-13T20:50:00Z"));
        product.getCategories().add(new Category(1L, "Pet Shop"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
