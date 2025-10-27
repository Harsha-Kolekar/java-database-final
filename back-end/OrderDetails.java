package com.project.code.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class OrderDetails {

    // 1. Primary Key with auto increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Customer foreign key mapping
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private Customer customer;

    // 3. Store foreign key mapping
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonManagedReference
    private Store store;

    // 4. Total price of the order
    private Double totalPrice;

    // 5. Date & time when the order was placed
    private LocalDateTime date;

    // 6. One-to-many relationship with OrderItem
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    // 7. Default constructor
    public OrderDetails() {}

    // Parameterized constructor
    public OrderDetails(Customer customer, Store store, Double totalPrice, LocalDateTime date) {
        this.customer = customer;
        this.store = store;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // 9. Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
