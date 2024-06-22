package com.oss.model;

import jakarta.persistence.*;
@Table
@Entity
public class role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String role;
    @OneToOne(mappedBy = "role")
    private  user user;
}
