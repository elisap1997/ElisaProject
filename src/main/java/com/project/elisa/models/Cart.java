package com.project.elisa.models;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
//@Table(name="cart",schema="project1")
@Table(name="cart")
public class Cart implements Serializable {
    @Id
    @Column(name="CART_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartNum;
    private int numItemsInCart;
    private int userId;

}