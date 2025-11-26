package com.svovnenko.crypto.service.mapper;

import com.svovnenko.crypto.config.CommonMapperConfig;
import com.svovnenko.crypto.domain.Crypto;
import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.NormalizedValue;
import com.svovnenko.crypto.domain.dto.CryptoNormalizedRangeDtoDto;
import com.svovnenko.crypto.domain.dto.CryptoNormalizedRangeResponseDto;
import com.svovnenko.crypto.domain.dto.CryptoStatisticsResponseDto;
import com.svovnenko.crypto.repository.entities.CryptoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface CryptoMapper {

    @Mapping(target = "data", source = "statistic")
    CryptoStatisticsResponseDto toStatisticResponse(CryptoStatistic statistic);

    @Mapping(target = "data", source = "normalizedValue")
    CryptoNormalizedRangeResponseDto toNormalizedRangeResponse(NormalizedValue normalizedValue);

    CryptoNormalizedRangeDtoDto toNormalizedResponse(NormalizedValue normalizedValue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", source = "time")
    CryptoEntity toEntity(Crypto crypto);
}

