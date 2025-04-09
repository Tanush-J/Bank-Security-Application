package com.security.bank.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String status;
    private double balance;
    private float interestRate;
    @Enumerated(EnumType.STRING)
    private BranchType branch;
    private String proof;
    private Date openingDate;
    private Long accountNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private Nominee nominee;  //(One To One Mapping with Nominee entity)

    @OneToOne(cascade = CascadeType.ALL)
    private Card card; //(One To One Mapping with Card entity)
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user; //(Many To One Mapping with User entity)
}
