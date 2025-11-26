package com.svovnenko.crypto.controller;

import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.NormalizedValue;
import com.svovnenko.crypto.domain.dto.CryptoNormalizedRangeDtoDto;
import com.svovnenko.crypto.domain.dto.CryptoNormalizedRangeListResponseDataDto;
import com.svovnenko.crypto.domain.dto.CryptoNormalizedRangeListResponseDto;
import com.svovnenko.crypto.domain.dto.CryptoNormalizedRangeResponseDto;
import com.svovnenko.crypto.domain.dto.CryptoStatisticsResponseDto;
import com.svovnenko.crypto.domain.dto.CryptoSymbolDto;
import com.svovnenko.crypto.exception.CryptoNotFoundException;
import com.svovnenko.crypto.exception.WrongDatesOrderException;
import com.svovnenko.crypto.service.RecommendationService;
import com.svovnenko.crypto.service.mapper.CryptoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recommendation")
@Slf4j
public class RecommendationController implements CryptoApi {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private CryptoMapper mapper;

    /**
     * Get highestNormalizedRange
     * can be run for one day if only start date is present or range if end date is present
     * @param from Start date (yyyy-MM-dd) (required)
     * @param to End date (yyyy-MM-dd) (optional)
     * @throws CryptoNotFoundException if nothing is found
     * @return
     */
    @Override
    public ResponseEntity<CryptoNormalizedRangeResponseDto> highestNormalizedRangeGet(LocalDate from, LocalDate to) {
        validateDateOrder(from, to);
        NormalizedValue normalizedValue = recommendationService.getHighestNormalizedValue(from, to)
                .orElseThrow(() -> new CryptoNotFoundException("No data found for this range"));

        return ResponseEntity.ok(mapper.toNormalizedRangeResponse(normalizedValue));
    }

    /**
     * Get normalizedRange
     * can be run for all period of time or range if start date and end date are present
     * @param from Start date (yyyy-MM-dd) (optional)
     * @param to End date (yyyy-MM-dd) (optional)
     * @return
     */
    @Override
    public ResponseEntity<CryptoNormalizedRangeListResponseDto> normalizedRangeGet(LocalDate from, LocalDate to) {
        validateDateOrder(from, to);
        List<NormalizedValue> normalizedList = recommendationService.getNormalizedValues(from, to);

        return ResponseEntity.ok(prepareRangeResponse(normalizedList));
    }

    private CryptoNormalizedRangeListResponseDto prepareRangeResponse(List<NormalizedValue> normalizedList) {
        List<CryptoNormalizedRangeDtoDto> normalizedListResponse = normalizedList.stream()
                .map(mapper::toNormalizedResponse)
                .toList();
        var responseData = new CryptoNormalizedRangeListResponseDataDto()
                .values(normalizedListResponse);

        return new CryptoNormalizedRangeListResponseDto().data(responseData);
    }

    /**
     * Get symbolStatistics for provided crypto
     * can be run for all period of time or range if start date and end date are present
     * @param symbol Crypto symbol (required)
     * @param from Start date (yyyy-MM-dd) (optional)
     * @param to End date (yyyy-MM-dd) (optional)
     * @throws CryptoNotFoundException if crypto not found
     * @return
     */
    @Override
    public ResponseEntity<CryptoStatisticsResponseDto> symbolStatisticsGet(CryptoSymbolDto symbol,
                                                                           LocalDate from, LocalDate to) {
        validateDateOrder(from, to);

        String symbolString = symbol.getValue();
        CryptoStatistic statistic = recommendationService.getFullStatisticByCrypto(symbolString, from, to)
                .orElseThrow(() -> new CryptoNotFoundException("Crypto not found: " + symbolString));

        return ResponseEntity.ok(mapper.toStatisticResponse(statistic));
    }

    /**
     * Validate that start date is BEFORE end date
     * @throws WrongDatesOrderException
     */
    private void validateDateOrder(LocalDate from, LocalDate to){
        if (from == null || to == null) {
            return;
        }
        if (from.isAfter(to)) {
            String msg = String.format("Incorrect dates. Range start (%s) is after range end (%s)", from, to);
            throw new WrongDatesOrderException(msg);
        }
    }
}
