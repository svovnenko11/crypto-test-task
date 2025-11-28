package com.svovnenko.crypto.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Supported crypto symbols
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-28T14:03:30.736132300+01:00[Europe/Budapest]", comments = "Generator version: 7.17.0")
public enum CryptoSymbolDto {
  
  BTC("BTC"),
  
  DOGE("DOGE"),
  
  ETH("ETH"),
  
  LTC("LTC"),
  
  XRP("XRP");

  private final String value;

  CryptoSymbolDto(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CryptoSymbolDto fromValue(String value) {
    for (CryptoSymbolDto b : CryptoSymbolDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

