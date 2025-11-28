package com.svovnenko.crypto.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * CryptoNormalizedRangeResponseDto
 */
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

@JsonTypeName("CryptoNormalizedRangeResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-28T14:03:30.736132300+01:00[Europe/Budapest]", comments = "Generator version: 7.17.0")
public class CryptoNormalizedRangeResponseDto {

  private @Nullable CryptoNormalizedRangeDtoDto data;

  public CryptoNormalizedRangeResponseDto data(@Nullable CryptoNormalizedRangeDtoDto data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public @Nullable CryptoNormalizedRangeDtoDto getData() {
    return data;
  }

  public void setData(@Nullable CryptoNormalizedRangeDtoDto data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CryptoNormalizedRangeResponseDto cryptoNormalizedRangeResponse = (CryptoNormalizedRangeResponseDto) o;
    return Objects.equals(this.data, cryptoNormalizedRangeResponse.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CryptoNormalizedRangeResponseDto {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

