package com.svovnenko.crypto.service.impl;

import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.NormalizedValue;
import com.svovnenko.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationServiceImplTest {

    @Mock
    private CryptoRepository repository;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    public RecommendationServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for getFullStatisticByCrypto when both startDate and endDate are provided.
     */
    @Test
    public void testGetFullStatisticByCrypto_WithDateRange() {
        String symbol = "BTC";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        CryptoStatistic statistic = new CryptoStatistic(symbol,
                BigDecimal.valueOf(100), BigDecimal.valueOf(200), BigDecimal.valueOf(50), BigDecimal.valueOf(300));

        when(repository.findAggregatedValuesBySymbolAndDateRange(symbol, startDate, endDate))
                .thenReturn(Optional.of(statistic));

        Optional<CryptoStatistic> result = recommendationService.getFullStatisticByCrypto(symbol, startDate, endDate);

        assertTrue(result.isPresent());
        assertEquals(statistic, result.get());
        verify(repository).findAggregatedValuesBySymbolAndDateRange(any(), any(),any());
        verify(repository, never()).findAggregatedValuesBySymbol(symbol);
    }

    /**
     * Test for getFullStatisticByCrypto when startDate and endDate are null.
     */
    @Test
    public void testGetFullStatisticByCrypto_WithoutDateRange() {
        String symbol = "ETH";
        CryptoStatistic statistic = new CryptoStatistic(symbol, BigDecimal.valueOf(50), BigDecimal.valueOf(150), BigDecimal.valueOf(40), BigDecimal.valueOf(200));

        when(repository.findAggregatedValuesBySymbol(symbol)).thenReturn(Optional.of(statistic));

        Optional<CryptoStatistic> result = recommendationService.getFullStatisticByCrypto(symbol, null, null);

        assertTrue(result.isPresent());
        assertEquals(statistic, result.get());
        verify(repository, never()).findAggregatedValuesBySymbolAndDateRange(any(), any(),any());
        verify(repository).findAggregatedValuesBySymbol(symbol);
    }

    /**
     * Test for getFullStatisticByCrypto when no data is found in the repository.
     */
    @Test
    public void testGetFullStatisticByCrypto_NoDataFound() {
        String symbol = "XRP";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(repository.findAggregatedValuesBySymbolAndDateRange(symbol, startDate, endDate)).thenReturn(Optional.empty());

        Optional<CryptoStatistic> result = recommendationService.getFullStatisticByCrypto(symbol, startDate, endDate);

        assertTrue(result.isEmpty());
    }

    /**
     * Test for getHighestNormalizedValue for a single day.
     */
    @Test
    public void testGetHighestNormalizedValue_ForSingleDay() {
        LocalDate date = LocalDate.of(2023, 9, 15);
        NormalizedValue normalizedValue = new NormalizedValue("BTC", BigDecimal.valueOf(0.8));

        when(repository.findHighestNormalizedForDay(date)).thenReturn(Optional.of(normalizedValue));

        Optional<NormalizedValue> result = recommendationService.getHighestNormalizedValue(date, null);

        assertTrue(result.isPresent());
        assertEquals(normalizedValue, result.get());
        verify(repository).findHighestNormalizedForDay(date);
        verify(repository, never()).findHighestNormalizedRange(any(), any());
    }

    /**
     * Test for getHighestNormalizedValue for a date range.
     */
    @Test
    public void testGetHighestNormalizedValue_ForDateRange() {
        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 9, 30);
        NormalizedValue highestNormalized = new NormalizedValue("ETH", BigDecimal.valueOf(0.9));

        when(repository.findHighestNormalizedRange(startDate, endDate)).thenReturn(Optional.of(highestNormalized));

        Optional<NormalizedValue> result = recommendationService.getHighestNormalizedValue(startDate, endDate);

        assertTrue(result.isPresent());
        assertEquals(highestNormalized, result.get());
        verify(repository).findHighestNormalizedRange(startDate, endDate);
        verify(repository, never()).findHighestNormalizedForDay(any());
    }

    /**
     * Test for getNormalizedValues with a date range.
     */
    @Test
    public void testGetNormalizedValues_WithDateRange() {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 31);
        List<NormalizedValue> values = List.of(
                new NormalizedValue("BTC", BigDecimal.valueOf(0.5)),
                new NormalizedValue("ETH", BigDecimal.valueOf(0.7))
        );

        when(repository.findAllNormalizedValues(startDate, endDate)).thenReturn(values);

        List<NormalizedValue> result = recommendationService.getNormalizedValues(startDate, endDate);

        assertEquals(values, result);
        verify(repository).findAllNormalizedValues(startDate, endDate);
        verify(repository, never()).findAllNormalizedValues();
    }

    /**
     * Test for getNormalizedValues without a date range.
     */
    @Test
    public void testGetNormalizedValues_WithoutDateRange() {
        List<NormalizedValue> values = List.of(
                new NormalizedValue("BTC", BigDecimal.valueOf(0.8)),
                new NormalizedValue("ETH", BigDecimal.valueOf(0.6))
        );

        when(repository.findAllNormalizedValues()).thenReturn(values);

        List<NormalizedValue> result = recommendationService.getNormalizedValues(null, null);

        assertEquals(values, result);
        verify(repository).findAllNormalizedValues();
        verify(repository, never()).findAllNormalizedValues(any(), any());
    }

    /**
     * Test for getNormalizedValues with no data found.
     */
    @Test
    public void testGetNormalizedValues_NoDataFound() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        when(repository.findAllNormalizedValues(startDate, endDate)).thenReturn(List.of());

        List<NormalizedValue> result = recommendationService.getNormalizedValues(startDate, endDate);

        assertTrue(result.isEmpty());
        verify(repository).findAllNormalizedValues(startDate, endDate);
    }

    /**
     * Test for getHighestNormalizedValue when no data is found.
     */
    @Test
    public void testGetHighestNormalizedValue_NoDataFound() {
        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 9, 30);

        when(repository.findHighestNormalizedRange(startDate, endDate)).thenReturn(Optional.empty());

        Optional<NormalizedValue> result = recommendationService.getHighestNormalizedValue(startDate, endDate);

        assertTrue(result.isEmpty());
    }
}