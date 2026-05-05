package com.gmfs.demo.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;


@Entity
@Data
//    @Table
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private Integer company_id;
    private LocalDateTime record_date;


}


