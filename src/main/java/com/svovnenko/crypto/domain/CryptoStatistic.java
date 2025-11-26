package com.svovnenko.crypto.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoStatistic {

    private CryptoType symbol;
    private BigDecimal oldestPrice;
    private BigDecimal newestPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    //For the easy mapping in Repository
    public CryptoStatistic(String symbol, BigDecimal oldest, BigDecimal newest, BigDecimal min, BigDecimal max) {
        this.symbol = CryptoType.valueOf(symbol);
        this.oldestPrice = oldest;
        this.newestPrice = newest;
        this.minPrice = min;
        this.maxPrice = max;
    }
}
