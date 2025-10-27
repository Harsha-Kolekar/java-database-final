package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 1. Find inventory by product ID and store ID
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.store.id = :storeId")
    Inventory findByProductIdAndStoreId(Long productId, Long storeId);

    // 2. Find all inventory records for a specific store
    List<Inventory> findByStore_Id(Long storeId);

    // 3. Delete all inventory records for a specific product
    @Modifying
    @Transactional
    @Query("DELETE FROM Inventory i WHERE i.product.id = :productId")
    void deleteByProductId(Long productId);
}
