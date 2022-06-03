package com.project.elisa.dao;

import com.project.elisa.models.Cart;
import com.project.elisa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartDAO extends JpaRepository<Cart,Integer> {
    @Query("select c from Cart c where userId = ?1")
    public Cart getCartFromUserId(int userId);

    @Query("select c from Cart c where user=?1")
    Cart getCartFromUser(User user);
}
