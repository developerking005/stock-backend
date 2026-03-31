package com.library.seatmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private Double price;

    private LocalDateTime updatedAt;

    public Stock(){}

    public Stock(String symbol, Double price){
        this.symbol = symbol;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId(){ return id; }

    public String getSymbol(){ return symbol; }
    public void setSymbol(String symbol){ this.symbol = symbol; }

    public Double getPrice(){ return price; }
    public void setPrice(Double price){
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt(){ return updatedAt; }
}