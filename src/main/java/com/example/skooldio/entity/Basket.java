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
    @GeneratedValue(
            generator = "basket_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "basket_seq", allocationSize = 1, sequenceName = "basket_seq")
    @Column(name = "ID")
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;
    @Column(nullable = false)
    private int quantity;
}
