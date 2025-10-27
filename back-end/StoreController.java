package com.project.code.Controller;

import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import com.project.code.DTO.PlaceOrderRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    // 1. Add a new store
    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> response = new HashMap<>();
        Store savedStore = storeRepository.save(store);
        response.put("message", "Store created successfully with ID: " + savedStore.getId());
        return response;
    }

    // 2. Validate if a store exists by ID
    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        return storeRepository.findById(storeId).isPresent();
    }

    // 3. Place an order for a store
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.saveOrder(placeOrderRequest);
            response.put("message", "Order placed successfully");
        } catch (Exception e) {
            response.put("Error", e.getMessage());
        }
        return response;
    }
}
