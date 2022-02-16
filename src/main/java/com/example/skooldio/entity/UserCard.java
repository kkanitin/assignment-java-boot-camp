package com.example.skooldio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_card")
@Getter
@Setter
@NoArgsConstructor
public class UserCard {

    @Id
    @SequenceGenerator(name = "user_card_seq", allocationSize = 1, sequenceName = "user_card_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_card_seq")
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    private String cardType;
    private String cardNo;
    private String expireMonth;
    private String expireYear;
    private String ccvOrCvv;
}
