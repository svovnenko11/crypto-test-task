package com.svovnenko.crypto.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * CryptoStatisticsDtoDto
 */
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

@JsonTypeName("CryptoStatisticsDto")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-28T14:03:30.736132300+01:00[Europe/Budapest]", comments = "Generator version: 7.17.0")
public class CryptoStatisticsDtoDto {

  private @Nullable CryptoSymbolDto symbol;

  private @Nullable Double oldestPrice;

  private @Nullable Double newestPrice;

  private @Nullable Double minPrice;

  private @Nullable Double maxPrice;

  public CryptoStatisticsDtoDto symbol(@Nullable CryptoSymbolDto symbol) {
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

  public CryptoStatisticsDtoDto oldestPrice(@Nullable Double oldestPrice) {
    this.oldestPrice = oldestPrice;
    return this;
  }

  /**
   * Get oldestPrice
   * @return oldestPrice
   */
  
  @Schema(name = "oldestPrice", example = "46813.21", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("oldestPrice")
  public @Nullable Double getOldestPrice() {
    return oldestPrice;
  }

  public void setOldestPrice(@Nullable Double oldestPrice) {
    this.oldestPrice = oldestPrice;
  }

  public CryptoStatisticsDtoDto newestPrice(@Nullable Double newestPrice) {
    this.newestPrice = newestPrice;
    return this;
  }

  /**
   * Get newestPrice
   * @return newestPrice
   */
  
  @Schema(name = "newestPrice", example = "47200.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newestPrice")
  public @Nullable Double getNewestPrice() {
    return newestPrice;
  }

  public void setNewestPrice(@Nullable Double newestPrice) {
    this.newestPrice = newestPrice;
  }

  public CryptoStatisticsDtoDto minPrice(@Nullable Double minPrice) {
    this.minPrice = minPrice;
    return this;
  }

  /**
   * Get minPrice
   * @return minPrice
   */
  
  @Schema(name = "minPrice", example = "45000.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minPrice")
  public @Nullable Double getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(@Nullable Double minPrice) {
    this.minPrice = minPrice;
  }

  public CryptoStatisticsDtoDto maxPrice(@Nullable Double maxPrice) {
    this.maxPrice = maxPrice;
    return this;
  }

  /**
   * Get maxPrice
   * @return maxPrice
   */
  
  @Schema(name = "maxPrice", example = "48000.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxPrice")
  public @Nullable Double getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(@Nullable Double maxPrice) {
    this.maxPrice = maxPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CryptoStatisticsDtoDto cryptoStatisticsDto = (CryptoStatisticsDtoDto) o;
    return Objects.equals(this.symbol, cryptoStatisticsDto.symbol) &&
        Objects.equals(this.oldestPrice, cryptoStatisticsDto.oldestPrice) &&
        Objects.equals(this.newestPrice, cryptoStatisticsDto.newestPrice) &&
        Objects.equals(this.minPrice, cryptoStatisticsDto.minPrice) &&
        Objects.equals(this.maxPrice, cryptoStatisticsDto.maxPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, oldestPrice, newestPrice, minPrice, maxPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CryptoStatisticsDtoDto {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    oldestPrice: ").append(toIndentedString(oldestPrice)).append("\n");
    sb.append("    newestPrice: ").append(toIndentedString(newestPrice)).append("\n");
    sb.append("    minPrice: ").append(toIndentedString(minPrice)).append("\n");
    sb.append("    maxPrice: ").append(toIndentedString(maxPrice)).append("\n");
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

