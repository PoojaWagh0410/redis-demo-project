package com.ttpl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Table(name = "address")
@Getter
@Setter
//@RedisHash("Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "zip_code")
    private Long zipCode;

    @Column(name = "state", length = 150)
    private String state;

}
