package com.example.skooldio.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private User user;
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

    public Address(User user, String houseNo, String buildingName, String floor, String village, String soi, String road, String khet, String kwang, String province, Integer postCode, Integer priority) {
        this.user = user;
        this.houseNo = houseNo;
        this.buildingName = buildingName;
        this.floor = floor;
        this.village = village;
        this.soi = soi;
        this.road = road;
        this.khet = khet;
        this.kwang = kwang;
        this.province = province;
        this.postCode = postCode;
        this.priority = priority;
    }
}
