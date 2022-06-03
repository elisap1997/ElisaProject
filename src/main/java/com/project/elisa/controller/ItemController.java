package com.project.elisa.controller;

import com.project.elisa.annotation.Authorized;
import com.project.elisa.models.Item;
import com.project.elisa.models.Role;
import com.project.elisa.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    @Autowired
    Item item;

    @Autowired
    ItemService itemService;
    boolean result;

    @PostMapping("/additem")
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> addItem(@RequestBody Item item) {
        ResponseEntity<String> responseEntity = null;

        if (itemService.isItemExists(item.getItemId())) {
            responseEntity = new ResponseEntity<String>
                    ("WARNING: CANNOT SAVE ITEM NO." + item.getItemId() + " , already exists", HttpStatus.CONFLICT);
        } else {
            result = itemService.addItem(item);
            if (result) {
                responseEntity = new ResponseEntity<String>
                        ("ITEM SAVED:" + item.getItemId(), HttpStatus.OK);        //200 means all good

            } else {
                responseEntity = new ResponseEntity<String>
                        ("WARNING: CANNOT USE NEGATIVE NUMBERS!", HttpStatus.NOT_ACCEPTABLE);        //406 is not good
            }
        }
        return responseEntity;
    }

    @DeleteMapping("/deleteitem/{itemid}")
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> deleteItem(@PathVariable("itemid") int itemId){
        ResponseEntity<String> responseEntity = null;
        if (!itemService.isItemExists(item.getItemId())) {
            responseEntity = new ResponseEntity<String>
                    ("WARNING: CANNOT DELETE" + item.getItemId() + " NOT IN EXISTENCE", HttpStatus.NO_CONTENT);
        }
        else{
            result = itemService.deleteItem(itemId);
            responseEntity = new ResponseEntity<String>
                    ("DELETED ITEM: " + item.getItemId(), HttpStatus.OK);
        }
        return responseEntity;
    }

    @PutMapping("/updateitem")
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> updateItem(@RequestBody Item item){
        ResponseEntity<String> responseEntity = null;
        if (!itemService.isItemExists(item.getItemId())) {
            responseEntity = new ResponseEntity<String>
                    ("WARNING: CANNOT UPDATE" + item.toString() + " IS NON-EXISTENT", HttpStatus.NO_CONTENT);
        }
        else{
            result = itemService.updateItem(item);
            if (result) {
                responseEntity = new ResponseEntity<String>
                        ("UPDATED ITEM NO: " + item.toString(), HttpStatus.OK);        //200 IS GOOD

            } else {
                responseEntity = new ResponseEntity<String>
                        ("WARNING: CANNOT USE NEGATIVE NUMBERS", HttpStatus.NOT_ACCEPTABLE);        //406 IS NOT
            }
        }
        return responseEntity;
    }
}
