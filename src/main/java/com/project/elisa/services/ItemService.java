package com.project.elisa.services;

import com.project.elisa.models.Item;

import java.util.List;

public interface ItemService {
    public boolean addItem(Item item);
    public boolean deleteItem(int itemId);
    public boolean updateItem(Item item);
    public String getAllInstockItems();
    public boolean isItemExists(int itemId);
}