package com.project.elisa.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Embeddable
//@Table(name="item",schema="project1")
@Table(name="item")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    @NotNull
    private String itemName;

    @NotNull
    private int qoh;

    @NotNull
    private int price;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user = null;
}
