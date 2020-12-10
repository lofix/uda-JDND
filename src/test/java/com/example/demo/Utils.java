package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

public class Utils {
    public static void injectObject(Object target,String field,Object toInject) {
        try {
            Field declaredField = target.getClass().getDeclaredField(field);
            boolean isSet = false;
            if (!declaredField.isAccessible()) {
                isSet = true;
                declaredField.setAccessible(true);
            }
            declaredField.set(target,toInject);
            if(isSet){declaredField.setAccessible(false);}

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User createUser(Long id, String pass, String username){
        User user = new User();
        user.setId(id);
        user.setPassword(pass);
        user.setUsername(username);
        return user;
    }

    public static Item createItem(Long id, String name, BigDecimal price, String description) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        return item;
    }

    public static Cart createCart(Long id, List<Item> items, BigDecimal price, User user) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(items);
        cart.setTotal(price);
        cart.setUser(user);
        return cart;
    }

    public static UserOrder createOrder(Long id, User user, List<Item> items, BigDecimal total) {
        UserOrder order = new UserOrder();
        order.setId(id);
        order.setUser(user);
        order.setItems(items);
        order.setTotal(total);
        return order;
    }

    public static ModifyCartRequest createModifyRequest(Long id, int quantity, String username) {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(id);
        req.setQuantity(quantity);
        req.setUsername(username);
        return req;
    }

    public static CreateUserRequest createCreateUserRequest(String password, String username) {
        CreateUserRequest req = new CreateUserRequest();
        req.setPassword(password);
        req.setConfirmPassword(password);
        req.setUsername(username);
        return req;
    }
}
