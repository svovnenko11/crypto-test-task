package com.svovnenko.crypto.service.impl;

import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.NormalizedValue;
import com.svovnenko.crypto.repository.CryptoRepository;
import com.svovnenko.crypto.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * For this task made all aggregation calculation on DB. Based on performance and data volume and structure can be changed
 * 1) Obvious soulution - add some indexes for subqueries and overall
 * 2) Split one query for several ones. Calculate min/max, calculate old/new
 * 3) Move some logic into application.
 * 4) Some combination of 1-3
 * 5) Job that will precalculate basic results and store them in DB. runs on schedule
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private CryptoRepository repository;

    /**
     * Calls repository to find all statistic based on crypto symbol
     * If data range is present - call method with date filtering
     * @param symbol String crypto representative. Initial validation was done in API call stage
     * @param startDate
     * @param endDate
     * @return Optional<CryptoStatistic>
     */
    @Override
    public Optional<CryptoStatistic> getFullStatisticByCrypto(String symbol,
                                                              LocalDate startDate,
                                                              LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return repository.findAggregatedValuesBySymbol(symbol);
        }
        return repository.findAggregatedValuesBySymbolAndDateRange(symbol, startDate, endDate);
    }

    /**
     * Calls repository to find highest normalized value
     * If endDate is present - call method with RANGE date filtering
     * If only startDate is present - means statistic will get only for this day
     * @param startDate
     * @param endDate
     * @return Optional<NormalizedValue>
     */
    @Override
    public Optional<NormalizedValue> getHighestNormalizedValue(LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            return repository.findHighestNormalizedForDay(startDate);
        }
        return repository.findHighestNormalizedRange(startDate, endDate);
    }

    /**
     * Calls repository to find all normalized values
     * If data range is present - call method with date filtering
     * @param startDate
     * @param endDate
     * @return List<NormalizedValue>
     */
    @Override
    public List<NormalizedValue> getNormalizedValues(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return repository.findAllNormalizedValues();
        }
        return repository.findAllNormalizedValues(startDate, endDate);
    }
}
