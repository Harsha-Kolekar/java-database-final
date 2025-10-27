package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import com.project.code.DTO.PlaceOrderRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Method to process and save an order
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
        // 1. Retrieve or create Customer
        Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setName(placeOrderRequest.getCustomerName());
            customer.setEmail(placeOrderRequest.getCustomerEmail());
            customer.setPhone(placeOrderRequest.getCustomerPhone());
            customer = customerRepository.save(customer);
        }

        // 2. Retrieve Store
        Optional<Store> storeOptional = storeRepository.findById(placeOrderRequest.getStoreId());
        if (storeOptional.isEmpty()) {
            throw new RuntimeException("Store not found with ID: " + placeOrderRequest.getStoreId());
        }
        Store store = storeOptional.get();

        // 3. Create OrderDetails
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
        orderDetails.setDate(LocalDateTime.now());
        orderDetails = orderDetailsRepository.save(orderDetails);

        // 4. Create and save OrderItems
        for (PlaceOrderRequestDTO.ProductOrderDTO productOrder : placeOrderRequest.getProducts()) {
            // Get inventory record
            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(
                    productOrder.getProductId(), store.getId()
            );

            if (inventory == null) {
                throw new RuntimeException("Inventory not found for Product ID: " + productOrder.getProductId() +
                        " in Store ID: " + store.getId());
            }

            // Check stock availability
            if (inventory.getStockLevel() < productOrder.getQuantity()) {
                throw new RuntimeException("Insufficient stock for Product ID: " + productOrder.getProductId());
            }

            // Reduce stock
            inventory.setStockLevel(inventory.getStockLevel() - productOrder.getQuantity());
            inventoryRepository.save(inventory);

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orderDetails);
            orderItem.setProduct(inventory.getProduct());
            orderItem.setQuantity(productOrder.getQuantity());
            orderItem.setPrice(productOrder.getPrice());
            orderItemRepository.save(orderItem);
        }
    }
}
