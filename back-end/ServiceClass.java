package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public ServiceClass(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // 1. Validate Inventory: returns false if inventory already exists
    public boolean validateInventory(Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
        return existingInventory == null; // true if no inventory exists
    }

    // 2. Validate Product: returns false if product with same name exists
    public boolean validateProduct(Product product) {
        Product existingProduct = productRepository.findByName(product.getName());
        return existingProduct == null; // true if product does not exist
    }

    // 3. Validate Product ID: returns false if product does not exist
    public boolean validateProductId(long id) {
        Product product = productRepository.findById(id);
        return product != null; // true if product exists
    }

    // 4. Get Inventory record for given product and store
    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }
}
