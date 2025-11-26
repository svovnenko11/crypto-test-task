package com.svovnenko.crypto.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NormalizedValue {

    private CryptoType symbol;
    private BigDecimal normalizedRange;

    //For the easy mapping in Repository
    public NormalizedValue(String symbol, BigDecimal normalizedRange) {
        this.symbol = CryptoType.valueOf(symbol);
        this.normalizedRange = normalizedRange;
    }
}
