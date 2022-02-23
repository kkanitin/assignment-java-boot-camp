package com.example.skooldio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(
            generator = "txn_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "txn_seq", allocationSize = 1, sequenceName = "txn_seq")
    @Column(name = "ID")
    private long id;
    private Integer groupNumber;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "user_card_id", nullable = false)
    private UserCard userCard;
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private Double amount;
    @Column(length = 30)
    private String status;
    @Column(nullable = false)
    private Integer quantity;
    private Date createdDate;
    private Date updatedDate;

    public Transaction(User user, UserCard userCard,
                       Address address, Product product,
                       Integer quantity, Double amount, String status,
                       Date createdDate) {
        this.user = user;
        this.userCard = userCard;
        this.address = address;
        this.product = product;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Transaction(User user, UserCard userCard,
                       Address address, Product product,
                       Integer quantity,
                       Double amount, String status) {
        this.user = user;
        this.userCard = userCard;
        this.address = address;
        this.product = product;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }
}
