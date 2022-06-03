package com.project.elisa.controller;

import com.project.elisa.annotation.Authorized;
import com.project.elisa.models.Role;
import com.project.elisa.models.User;
import com.project.elisa.services.AuthorizationService;
import com.project.elisa.services.ItemService;
import com.project.elisa.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired()
    User user;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;


    @Autowired
    private AuthorizationService authorizationService;

    boolean result;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        if(user == null){
            LOGGER.error("WARNING: USER IS NULL");
            return new ResponseEntity<>("USER NULL", HttpStatus.NO_CONTENT);
        }
        if(userService.isUserExists(user.getUserId())){
            LOGGER.warn("WARNING: USER EXISTS");
            return new ResponseEntity<>
                    ("WARNING: CANNOT SAVE USER: "
                            + user.getUserId() + ".", HttpStatus.CONFLICT);   //409
        }
        else{
            LOGGER.info("SUCCESS! "+user+" NOW IN USER TABLE");
            return ResponseEntity.accepted().body("SUCCESS! USER NO: "
                    +userService.register(user).toString());
        }
    }

    @PutMapping("/update")
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> updateUserInfo(@RequestBody User user){
        authorizationService.guardByUserId(user.getUserId());
        User result = userService.update(user);
        LOGGER.info("UPDATED "+user);
        return ResponseEntity.accepted().body("UPDATED USER: "+result.toString());
    }

    @PostMapping("/login/{user}/{pass}")
    public ResponseEntity<String> login(@PathVariable("user") String username, @PathVariable("pass") String password){
        User u = userService.login(username,password);
        LOGGER.info("LOGGED IN AS: "+user);
        return new ResponseEntity<>("Welcome " + u.getFirstName() + " " + u.getLastName(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        userService.logout();
        LOGGER.info("LOGGED OUT");
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/getusersandcarts")
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> getUsersAndCarts(){
        return ResponseEntity.ok(userService.getUsersAndCarts());
    }

    @GetMapping("/getmycart/{userid}")
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> getMyCart(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);

        return ResponseEntity.ok(userService.getSingleUserAndCart(userId));
    }

    @PutMapping("/addproducttocart/{userid}/{itemid}")
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> addProductToCart(@PathVariable("userid") int userId,@PathVariable("itemid") int itemId ){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        user = userService.getUser(userId);
        if(user==null){
            LOGGER.error("INVALID USERNAME OR PASSWORD");
            responseEntity = new ResponseEntity<>
                    ("INVALID USERNAME OR PASSWORD: TYPE AGAIN", HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            result = userService.addItemToCart(user,itemId);

            if(!result){
                LOGGER.warn("UNABLE TO ADD ITEM TO CART");
                responseEntity = new ResponseEntity<>
                        ("UNABLE TO ADD ITEM TO CART", HttpStatus.CONFLICT);
            }
            else{
                LOGGER.info("ADDED ITEM TO CART");
                responseEntity = new ResponseEntity<>
                        ("SUCCESS! ITEM ADDED", HttpStatus.OK);
            }
        }

        return responseEntity;
    }

    @DeleteMapping("/deleteuser/{userid}")
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> deleteUser(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        if(userService.isUserExists(userId)){
            result = userService.deleteAccount(userId);
            LOGGER.info("DELETED USER WITH userId: "+userId);
            responseEntity = new ResponseEntity<>
                    ("USER DELETED", HttpStatus.OK);
        }
        else{
            LOGGER.error("CANNOT DELETE USER");
            responseEntity = new ResponseEntity<>
                    ("UNABLE TO DELETE BECAUSE " + userId + " IS INVALID"
                            , HttpStatus.NOT_ACCEPTABLE);
        }

        return responseEntity;
    }

    @GetMapping("/getitemsinstock")
    public ResponseEntity<String> getItemsInStock(){
        String items = itemService.getAllInstockItems();
        if(Objects.equals(items, "")){
            LOGGER.warn("ITEM TABLE EMPTY");
            return new ResponseEntity<>
                    ("ITEMS OUT OF STOCK", HttpStatus.NO_CONTENT);
        }
        else{
            LOGGER.info("RETRIEVING ITEMS");
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
    }

    @PutMapping("/checkout/{userid}")
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> checkout(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        if(userService.isUserExists(userId)){
            int total = userService.checkout(userId);
            if(total>0){
                LOGGER.info("TRANSACTION COMPLETE");
                responseEntity = new ResponseEntity<>
                        ("TRANSACTION COMPLETE\n" +
                                "THIS IS YOUR TOTAL $" + total +
                                "\nTHANK YOU AND GOOD BYE"
                                , HttpStatus.OK);
            }
            else if(total == 0){
                LOGGER.warn("NOTHING IN CART");
                responseEntity = new ResponseEntity<>
                        ("NOTHING IN THE CART", HttpStatus.NO_CONTENT);
            }
            else{ //total=-1
                LOGGER.warn("NO CREDIT CARD");
                responseEntity = new ResponseEntity<>
                        ("UNABLE TO PROCESS " +
                                "COULD NOT FIND CREDIT CARD"
                                , HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            LOGGER.error("USER NON-EXISTENT");
            responseEntity = new ResponseEntity<>
                    ("CANNOT PROCESS USER "
                            + userId + " NOT IN EXISTENCE"
                            , HttpStatus.NO_CONTENT);
        }

        return responseEntity;
    }

    @PutMapping("/emptycart/{userid}")
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> emptyCart(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        if(userService.isUserExists(userId)){
            result= userService.emptyCart(userId);
            LOGGER.info("CART EMPTIED");
            responseEntity = new ResponseEntity<>
                    ("CART IS NOW EMPTY"
                            , HttpStatus.OK);
        }
        else{
            LOGGER.error("ERROR: "+userId+" NON-EXISTENT");
            responseEntity = new ResponseEntity<>
                    ("CANNOT PROCESS, userID "
                            + userId + " NON-EXISTENT"
                            , HttpStatus.NO_CONTENT);
        }
        return responseEntity;
    }
}