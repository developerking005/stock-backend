package com.library.seatmanager.Controller;

import com.library.seatmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService service;

    @GetMapping("/{symbol}")
    public Double getPrice(@PathVariable String symbol){
        return service.getPrice(symbol);
    }
}
