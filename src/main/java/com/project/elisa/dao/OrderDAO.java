package com.project.elisa.dao;

import com.project.elisa.models.Order;
import com.project.elisa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDAO extends JpaRepository<Order,Integer> {
    @Query("select o from Order o where userId = ?1")
    public Order getOrderFromUserId(int userId);

    @Query("select o from Order o where user=?1")
    Order getOrderFromUser(User u);
}
