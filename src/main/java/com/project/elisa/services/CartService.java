package com.project.elisa.services;

import com.project.elisa.models.Cart;
import com.project.elisa.models.User;

public interface CartService {
    public Cart getCart(int userId);
}
