package com.svovnenko.crypto.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CryptoNormalizedRangeListResponseDataDto
 */
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

@JsonTypeName("CryptoNormalizedRangeListResponse_data")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-28T14:03:30.736132300+01:00[Europe/Budapest]", comments = "Generator version: 7.17.0")
public class CryptoNormalizedRangeListResponseDataDto {

  @Valid
  private List<@Valid CryptoNormalizedRangeDtoDto> values = new ArrayList<>();

  public CryptoNormalizedRangeListResponseDataDto values(List<@Valid CryptoNormalizedRangeDtoDto> values) {
    this.values = values;
    return this;
  }

  public CryptoNormalizedRangeListResponseDataDto addValuesItem(CryptoNormalizedRangeDtoDto valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<>();
    }
    this.values.add(valuesItem);
    return this;
  }

  /**
   * Get values
   * @return values
   */
  @Valid 
  @Schema(name = "values", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("values")
  public List<@Valid CryptoNormalizedRangeDtoDto> getValues() {
    return values;
  }

  public void setValues(List<@Valid CryptoNormalizedRangeDtoDto> values) {
    this.values = values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CryptoNormalizedRangeListResponseDataDto cryptoNormalizedRangeListResponseData = (CryptoNormalizedRangeListResponseDataDto) o;
    return Objects.equals(this.values, cryptoNormalizedRangeListResponseData.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CryptoNormalizedRangeListResponseDataDto {\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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

