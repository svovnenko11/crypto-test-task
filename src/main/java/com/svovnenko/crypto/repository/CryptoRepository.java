package com.svovnenko.crypto.repository;

import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.CryptoType;
import com.svovnenko.crypto.domain.NormalizedValue;
import com.svovnenko.crypto.repository.entities.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository with native queries
 */
@Repository
public interface CryptoRepository extends JpaRepository<CryptoEntity, Long> {

    List<CryptoEntity> findBySymbol(CryptoType symbol);

    @Query(value = "SELECT c.symbol, " +
            "(SELECT c1.price " +
            " FROM crypto_entity c1 " +
            " WHERE c1.symbol = c.symbol " +
            "   AND c1.timestamp = (SELECT MIN(c2.timestamp) FROM crypto_entity c2 WHERE c2.symbol = c.symbol AND c2.timestamp BETWEEN :startDate AND :endDate) LIMIT 1), " +
            "(SELECT c3.price " +
            " FROM crypto_entity c3 " +
            " WHERE c3.symbol = c.symbol " +
            "   AND c3.timestamp = (SELECT MAX(c4.timestamp) FROM crypto_entity c4 WHERE c4.symbol = c.symbol AND c4.timestamp BETWEEN :startDate AND :endDate) LIMIT 1), " +
            "MIN(c.price) AS min, " +
            "MAX(c.price) AS max " +
            "FROM crypto_entity c " +
            "WHERE c.symbol = :symbol AND c.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY c.symbol", nativeQuery = true)
    Optional<CryptoStatistic> findAggregatedValuesBySymbolAndDateRange(
            @Param("symbol") String symbol,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT c.symbol, " +
            "(SELECT c1.price FROM crypto_entity c1 WHERE c1.symbol = c.symbol AND c1.timestamp = (SELECT MIN(c2.timestamp) FROM crypto_entity c2 WHERE c2.symbol = c.symbol) LIMIT 1), " +
            "(SELECT c3.price FROM crypto_entity c3 WHERE c3.symbol = c.symbol AND c3.timestamp = (SELECT MAX(c4.timestamp) FROM crypto_entity c4 WHERE c4.symbol = c.symbol) LIMIT 1), " +
            "MIN(c.price), " +
            "MAX(c.price) " +
            "FROM crypto_entity c " +
            "WHERE c.symbol = :symbol " +
            "GROUP BY c.symbol", nativeQuery = true)
    Optional<CryptoStatistic> findAggregatedValuesBySymbol(@Param("symbol") String symbol);

    @Query(value = "SELECT c.symbol, " +
            "((MAX(c.price) - MIN(c.price)) / MIN(c.price)) as normalizedRange " +
            "FROM crypto_entity c " +
            "WHERE c.timestamp >= :fromDate AND c.timestamp <= :toDate " +
            "GROUP BY c.symbol " +
            "ORDER BY normalizedRange DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<NormalizedValue> findHighestNormalizedRange(@Param("fromDate") LocalDate fromDate,
                                                         @Param("toDate") LocalDate toDate);

    @Query(value = "SELECT c.symbol, " +
            "((MAX(c.price) - MIN(c.price)) / MIN(c.price)) as normalizedRange " +
            "FROM crypto_entity c " +
            "WHERE CAST(c.timestamp AS DATE) = :date " +
            "GROUP BY c.symbol " +
            "ORDER BY normalizedRange DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<NormalizedValue> findHighestNormalizedForDay(@Param("date") LocalDate date);

    @Query(value = "SELECT c.symbol, " +
            "((MAX(c.price) - MIN(c.price)) / MIN(c.price)) as normalizedRange " +
            "FROM crypto_entity c " +
            "GROUP BY c.symbol " +
            "ORDER BY normalizedRange DESC", nativeQuery = true)
    List<NormalizedValue> findAllNormalizedValues();

    @Query(value = "SELECT c.symbol, " +
            "((MAX(c.price) - MIN(c.price)) / MIN(c.price)) as normalizedRange " +
            "FROM crypto_entity c " +
            "WHERE c.timestamp >= :fromDate AND c.timestamp <= :toDate " +
            "GROUP BY c.symbol " +
            "ORDER BY normalizedRange DESC", nativeQuery = true)
    List<NormalizedValue> findAllNormalizedValues(@Param("fromDate") LocalDate fromDate,
                                                  @Param("toDate") LocalDate toDate);
}