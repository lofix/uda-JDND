package com.example.demo.controller;

import com.example.demo.Utils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private static OrderController orderController;
    private static UserRepository userRepository = mock(UserRepository.class);
    private static OrderRepository orderRepository = mock(OrderRepository.class);
    private static String DESCRIPTION = "test description";
    private static String ITEM_NAME = "bread";
    private static String USER_NAME = "testuser";
    private static String PASSWORD = "password";

    @BeforeClass
    public static void setup() {
        orderController = new OrderController();
        Utils.injectObject(orderController, "userRepository", userRepository);
        Utils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit() {
        User user = Utils.createUser(1L, PASSWORD, USER_NAME);
        Item item = Utils.createItem(2L, ITEM_NAME, BigDecimal.TEN, DESCRIPTION);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = Utils.createCart(5L, items, BigDecimal.ONE, user);
        user.setCart(cart);
        UserOrder order = Utils.createOrder(4L, user, items, BigDecimal.ONE);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder result = response.getBody();
        assertEquals(user, result.getUser());
        assertEquals(items, result.getItems());
        assertEquals(BigDecimal.ONE, result.getTotal());
    }

    @Test
    public void getOrdersForUser() {
        User user = Utils.createUser(1L, PASSWORD, USER_NAME);
        Item item = Utils.createItem(2L, ITEM_NAME, BigDecimal.TEN, DESCRIPTION);
        List<Item> items = new ArrayList<>();
        items.add(item);

        UserOrder order = Utils.createOrder(4L, user, items, BigDecimal.ONE);
        List<UserOrder> listOrders = new ArrayList<>();
        listOrders.add(order);

        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(listOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(USER_NAME);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> result = response.getBody();
        UserOrder resultOrder = result.get(0);
        assertEquals(user, resultOrder.getUser());
        assertEquals(items, resultOrder.getItems());

    }
}
