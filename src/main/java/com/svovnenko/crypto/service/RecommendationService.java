package com.svovnenko.crypto.service;

import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.NormalizedValue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface RecommendationService {

    Optional<CryptoStatistic> getFullStatisticByCrypto(String symbol, LocalDate startDate, LocalDate endDate);

    Optional<NormalizedValue> getHighestNormalizedValue(LocalDate startDate, LocalDate endDate);

    List<NormalizedValue> getNormalizedValues(LocalDate startDate, LocalDate endDate);
}
