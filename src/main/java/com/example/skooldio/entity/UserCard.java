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
    @GeneratedValue(
            generator = "usercard_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "usercard_seq", allocationSize = 1, sequenceName = "usercard_seq")
    @Column(name = "ID")
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String cardType;
    private String cardNo;
    private String expireMonth;
    private String expireYear;
    private String ccvOrCvv;
    private Integer priority;

//    public UserCard(User userId, String cardType, String cardNo, String expireMonth, String expireYear, String ccvOrCvv, int priority) {
//        this.userId = userId;
//        this.cardType = cardType;
//        this.cardNo = cardNo;
//        this.expireMonth = expireMonth;
//        this.expireYear = expireYear;
//        this.ccvOrCvv = ccvOrCvv;
//        this.priority = priority;
//    }
}
