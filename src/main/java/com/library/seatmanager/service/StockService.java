package com.library.seatmanager.service;

import com.library.seatmanager.entity.Stock;
import com.library.seatmanager.repo.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository repo;

    public Stock updatePrice(String symbol, Double price){

        Stock stock = repo.findBySymbol(symbol)
                .orElse(new Stock(symbol, price));

        stock.setPrice(price);
        return repo.save(stock);
    }

    public Double getPrice(String symbol){
        return repo.findBySymbol(symbol)
                .map(Stock::getPrice)
                .orElse(100.0);
    }
}
