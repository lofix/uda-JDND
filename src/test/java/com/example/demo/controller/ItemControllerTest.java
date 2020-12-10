package com.example.demo.controller;

import com.example.demo.Utils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTest {
    private static ItemController itemController;
    private static ItemRepository itemRepository = mock(ItemRepository.class);
    private static String DESCRIPTION = "test description";
    private static String ITEM_NAME = "bread";

    @BeforeClass
    public static void setup() {
        itemController = new ItemController();
        Utils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        Item item = Utils.createItem(2L, ITEM_NAME, BigDecimal.valueOf(1000.0), DESCRIPTION);
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        Item returnedItem = returnedItems.get(0);
        assertEquals(ITEM_NAME, returnedItem.getName());
        assertEquals(DESCRIPTION, returnedItem.getDescription());
    }

    @Test
    public void getItemById() {
        Item item = Utils.createItem(1L, ITEM_NAME, BigDecimal.valueOf(1000.0), DESCRIPTION);
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item returnedItem = response.getBody();
        assertEquals(ITEM_NAME, returnedItem.getName());
        assertEquals(DESCRIPTION, returnedItem.getDescription());
    }

    @Test
    public void getItemsByName() {
        Item item = Utils.createItem(1L, ITEM_NAME, BigDecimal.valueOf(1000.0), DESCRIPTION);
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findByName(ITEM_NAME)).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(ITEM_NAME);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        Item returnedItem = returnedItems.get(0);
        assertEquals(ITEM_NAME, returnedItem.getName());
        assertEquals(DESCRIPTION, returnedItem.getDescription());
    }
}
