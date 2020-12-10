package com.example.demo.controller;

import com.example.demo.Utils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private static CartController cartController;
    private static UserRepository userRepository = mock(UserRepository.class);
    private static ItemRepository itemRepository = mock(ItemRepository.class);
    private static CartRepository cartRepository = mock(CartRepository.class);
    private static String USER_NAME = "testuser";
    private static String PASSWORD = "password";

    @BeforeClass
    public static void setup() {
        cartController = new CartController();
        Utils.injectObject(cartController, "userRepository", userRepository);
        Utils.injectObject(cartController, "cartRepository", cartRepository);
        Utils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart(){
        ModifyCartRequest req = Utils.createModifyRequest(2L, 2, USER_NAME);
        Item item = Utils.createItem(2L, "bread", BigDecimal.TEN, "nice bread");
        List<Item> items = new ArrayList<>();
        items.add(item);
        User user = Utils.createUser(1L, PASSWORD, USER_NAME);
        Cart cart = Utils.createCart(5L, items, BigDecimal.TEN, user);
        user.setCart(cart);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addToCart(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart result = response.getBody();
        assertEquals(items, result.getItems());
        assertEquals(BigDecimal.valueOf(30), result.getTotal());
    }

    @Test
    public void removeFromCart(){
        ModifyCartRequest req = Utils.createModifyRequest(2L, 2, USER_NAME);
        Item item = Utils.createItem(2L, "bread", BigDecimal.TEN, "nice bread");
        List<Item> items = new ArrayList<>();
        items.add(item);
        User user = Utils.createUser(1L, PASSWORD, USER_NAME);
        Cart cart = Utils.createCart(5L, items, BigDecimal.valueOf(500.0), user);
        user.setCart(cart);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.removeFromCart(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart result = response.getBody();
        assertEquals(items, result.getItems());
        assertEquals(BigDecimal.valueOf(480.0), result.getTotal());
    }
}
