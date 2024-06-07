package com.example.fintechapplication.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "accounts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String accountName;


    @Column(name = "balance")
    private double balance = 0.;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt = new Date();


    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Transaction> transactions;

}
