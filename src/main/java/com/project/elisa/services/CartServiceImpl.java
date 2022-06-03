package com.project.elisa.services;

import com.project.elisa.dao.CartDAO;
import com.project.elisa.models.Cart;
import com.project.elisa.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartDAO cartDAO;

    @Override
    public Cart getCart(int userId) {
        return cartDAO.getCartFromUserId(userId);
    }
}