package com.example.fintechapplication.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Setter
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_at")
    @CreationTimestamp
    private Date transactionAt = new Date();



    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

}
