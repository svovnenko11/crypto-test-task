package com.svovnenko.crypto.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * CryptoNormalizedRangeDtoDto
 */
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

@JsonTypeName("CryptoNormalizedRangeDto")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-28T14:03:30.736132300+01:00[Europe/Budapest]", comments = "Generator version: 7.17.0")
public class CryptoNormalizedRangeDtoDto {

  private @Nullable CryptoSymbolDto symbol;

  private @Nullable Double normalizedRange;

  public CryptoNormalizedRangeDtoDto symbol(@Nullable CryptoSymbolDto symbol) {
    this.symbol = symbol;
    return this;
  }

  /**
   * Get symbol
   * @return symbol
   */
  @Valid 
  @Schema(name = "symbol", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symbol")
  public @Nullable CryptoSymbolDto getSymbol() {
    return symbol;
  }

  public void setSymbol(@Nullable CryptoSymbolDto symbol) {
    this.symbol = symbol;
  }

  public CryptoNormalizedRangeDtoDto normalizedRange(@Nullable Double normalizedRange) {
    this.normalizedRange = normalizedRange;
    return this;
  }

  /**
   * Get normalizedRange
   * @return normalizedRange
   */
  
  @Schema(name = "normalizedRange", example = "0.15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("normalizedRange")
  public @Nullable Double getNormalizedRange() {
    return normalizedRange;
  }

  public void setNormalizedRange(@Nullable Double normalizedRange) {
    this.normalizedRange = normalizedRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CryptoNormalizedRangeDtoDto cryptoNormalizedRangeDto = (CryptoNormalizedRangeDtoDto) o;
    return Objects.equals(this.symbol, cryptoNormalizedRangeDto.symbol) &&
        Objects.equals(this.normalizedRange, cryptoNormalizedRangeDto.normalizedRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, normalizedRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CryptoNormalizedRangeDtoDto {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    normalizedRange: ").append(toIndentedString(normalizedRange)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

