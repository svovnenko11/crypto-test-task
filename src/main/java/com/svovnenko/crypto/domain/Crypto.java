package com.svovnenko.crypto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    private Instant time;
    private CryptoType symbol;
    private BigDecimal price;
}

