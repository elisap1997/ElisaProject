package com.project.elisa.services;

import com.project.elisa.dao.CartDAO;
import com.project.elisa.dao.ItemDAO;
import com.project.elisa.dao.OrderDAO;
import com.project.elisa.dao.UserDAO;
import com.project.elisa.exceptions.UserNotFoundException;
import com.project.elisa.models.Cart;
import com.project.elisa.models.Item;
import com.project.elisa.models.Order;
import com.project.elisa.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserDAO userDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    CartDAO cartDAO;

    @Autowired
    private HttpServletRequest req;

    @Override
    public User register(User user) {
        if(userDAO.findByUsername(user.getUsername())==null){
            Order o = new Order();
            Cart c = new Cart();
            o.setUserId(user.getUserId());
            c.setUserId(user.getUserId());
            userDAO.save(user);
            cartDAO.save(c);
            orderDAO.save(o);
            return user;
        }
        else{
            throw new RuntimeException("USERNAME EXISTS");
        }
    }

    @Override
    public User update(User user){
        if(!userDAO.existsById(user.getUserId())){
            throw new RuntimeException
                    ("CANNOT UPDATE, " +
                            "USER DOES NOT EXIST");
        }

        userDAO.save(user);
        HttpSession session = req.getSession(false);

        User sessionUser = (User) session.getAttribute("currentUser");
        if(sessionUser.getUserId() == user.getUserId()){
            session.setAttribute("currentUser",user);
        }
        return user;
    }

    @Override
    public User login(String username, String password) {
        User exists = userDAO.findByUsernameAndPassword(username,password)
                .orElseThrow(() -> new UserNotFoundException(String.format
                        ("username: %s does not exist",username)));

        HttpSession session = req.getSession();
        session.setAttribute("currentUser",exists);
        return exists;
    }

    @Override
    public void logout(){
        HttpSession session = req.getSession(false);
        if(session == null){
            return;
        }
        session.invalidate();
    }

    @Override
    public boolean deleteAccount(int userId) {
        User u = userDAO.findById(userId);
        List<Item> items = new ArrayList<>(u.getCartContents());
        if(!items.isEmpty()){
            for(Item i : items){
                i.setUser(null);
                i.setQoh(i.getQoh()+1);
                itemDAO.save(i);
            }
        }
        userDAO.deleteById(userId);
        return true;

    }

    @Override
    public String getUsersAndCarts() {
        String str = "";
        List<User> users = userDAO.findAll(Sort.by("userId"));
        for(User u : users){
            Order o = orderDAO.getOrderFromUserId(u.getUserId());
            str=str + u.getFirstName()+" "+u.getLastName()+"'s ("+u.getUsername()+") cart contains:\n";
            for(Item i : u.getCartContents()){
                str=str+i.getItemName()+" -- $"+i.getPrice()+"\n";
            }
            str=str+"total= $"+o.getTotalPrice()+"\n--------------------------------\n";
        }
        return str;
    }

    @Override
    public String getSingleUserAndCart(int userId){
        User user = userDAO.findById(userId);
        Order o = orderDAO.getOrderFromUserId(userId);
        String str = user.getFirstName()+" "+user.getLastName()+"'s ("+user.getUsername()+") cart contains:\n";
        for(Item i : user.getCartContents()){
            str=str+i.getItemName()+" -- $"+i.getPrice()+"\n";
        }
        str=str+"total= $"+o.getTotalPrice()+"\n--------------------------------\n";
        return str;
    }

    @Override
    public boolean isUserExists(int userId) {
        return userDAO.existsById(userId);
    }

    @Override
    @Transactional
    public boolean addItemToCart(User user, int itemId) {
        Item item = itemDAO.findById(itemId);
        Cart cart = cartDAO.getCartFromUserId(user.getUserId());
        if(item==null){
            return false;
        }
        else{
            user.getCartContents().add(item);
            item.setQoh(item.getQoh()-1);
            item.setUser(user);
            cart.setNumItemsInCart(cart.getNumItemsInCart()+1);
            Order order = orderDAO.getOrderFromUserId(user.getUserId());
            order.setTotalPrice(order.getTotalPrice()+item.getPrice());
            userDAO.save(user);
            itemDAO.save(item);
            cartDAO.save(cart);
            orderDAO.save(order);
            return true;
        }
    }

    @Override
    public User getUser(int userId){
        return userDAO.findById(userId);
    }

    @Override
    public int checkout(int userId){
        User u = userDAO.findById(userId);
        if(u.getCardNum() == null){
            return -1;
        }
        if(u.getCartContents().isEmpty()){
            return 0;
        }
        Cart c = cartDAO.getCartFromUserId(userId);
        Order o = orderDAO.getOrderFromUserId(userId);
        int total = o.getTotalPrice();
        List<Item> items = new ArrayList<>(u.getCartContents());
        for(Item i : items){
            u.getCartContents().remove(i);
            itemDAO.deleteById(i.getItemId());
        }
        c.setNumItemsInCart(0);
        o.setTotalPrice(0);
        userDAO.save(u);
        cartDAO.save(c);
        orderDAO.save(o);
        return total;
    }

    @Override
    public boolean emptyCart(int userId) {
        User u = userDAO.findById(userId);
        if(u.getCartContents().isEmpty()){
            return true;
        }
        Cart c = cartDAO.getCartFromUserId(userId);
        Order o = orderDAO.getOrderFromUserId(userId);
        List<Item> items = new ArrayList<>(u.getCartContents());
        for(Item i : items){
            u.getCartContents().remove(i);
            i.setUser(null);
            i.setQoh(i.getQoh()+1);
            itemDAO.save(i);
        }
        c.setNumItemsInCart(0);
        o.setTotalPrice(0);
        userDAO.save(u);
        cartDAO.save(c);
        orderDAO.save(o);
        return true;
    }
}