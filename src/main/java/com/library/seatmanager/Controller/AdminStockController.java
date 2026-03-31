package com.library.seatmanager.Controller;

import com.library.seatmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminStockController {

    @Autowired
    private final StockService service;

    @PostMapping("/stock")
    public ResponseEntity<?> updateStock(
            @RequestParam String symbol,
            @RequestParam Double price){

        return ResponseEntity.ok(
                service.updatePrice(symbol,price));
    }
}
