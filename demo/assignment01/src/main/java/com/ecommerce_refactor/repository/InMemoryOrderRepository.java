package com.ecommerce_refactor.repository;

import com.ecommerce_refactor.model.Order;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    @Override
    public Order findById(String orderId) {
        return orders.stream().filter(order -> order.getOrderId().equals(orderId)).findFirst().orElse(null);
    }

    Map<Integer, String> hs = new Hashtable<>();
}
