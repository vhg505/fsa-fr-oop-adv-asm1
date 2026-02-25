package com.ecommerce_refactor.repository;

import com.ecommerce_refactor.model.Order;

import java.util.ArrayList;
import java.util.List;

public interface OrderRepository {
    void save(Order order) ;
    List<Order> findAll();
    Order findById(String orderId) ;
}
