package com.example.skooldio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "basket")
@Getter
@Setter
@NoArgsConstructor
public class Basket {

    @Id
    @SequenceGenerator(name = "basket_seq", allocationSize = 1, sequenceName = "basket_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basket_seq")
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
    private int quantity;
}
