package com.example.skooldio.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
public class Address{

    @Id
    @GeneratedValue(
            generator = "address_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "address_seq", allocationSize = 1, sequenceName = "address_seq")
    @Column(name="ID")
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Column(nullable = false)
    private String houseNo;
    private String buildingName;
    private String floor;
    private String village;
    private String soi;
    private String road;
    private String khet;
    private String kwang;
    private String province;
    private Integer postCode;
    private Integer priority;
}
