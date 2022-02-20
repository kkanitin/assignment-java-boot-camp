package com.example.skooldio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product{

    @Id
    @GeneratedValue(
            generator = "product_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "product_seq", allocationSize = 1, sequenceName = "product_seq")
    @Column(name="ID")
    private long id;
    @Column(nullable = false)
    private String name;
    private Double priceBaht;
    private int quantity;
    private String detail;
}
