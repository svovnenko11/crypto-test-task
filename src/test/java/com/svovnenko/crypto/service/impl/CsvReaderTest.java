package com.svovnenko.crypto.service.impl;

import com.svovnenko.crypto.domain.Crypto;
import com.svovnenko.crypto.domain.CryptoType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvReaderTest {

    private CsvReader csvReader;

    @BeforeEach
    void setUp() {
        csvReader = new CsvReader();
    }

    @Test
    void testReadCryptoValues_ReturnsMapWithData() {
        CsvReader csvReader = new CsvReader();
        Map<CryptoType, List<Crypto>> result = csvReader.readCryptoValues();

        assertFalse(result.isEmpty());
        assertTrue(result.containsKey(CryptoType.BTC));
        List<Crypto> cryptos = result.get(CryptoType.BTC);
        assertEquals(100, cryptos.size());
        assertEquals(new BigDecimal("46813.21"), cryptos.get(0).getPrice());
        assertEquals(Instant.ofEpochMilli(1641009600000L), cryptos.get(0).getTime());
    }

    @Test
    void testReadSingleCsv_ReturnsEmptyList_OnIOException() {
        // Pass a non-existent file
//        File file = new File("non_existent.csv");
//        List<Crypto> result = csvReader.readSingleCsv(file);
//        assertTrue(result.isEmpty());
        InputStream throwingStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated IO error");
            }
        };

        List<Crypto> result = csvReader.readSingleCsv(throwingStream);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseCsvLine_ReturnsEmptyOptional_OnMalformedLine() {
        String badLine = "not,a,number";
        Optional<Crypto> result = csvReader.parseCsvLine(badLine);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseCsvLine_ReturnsCrypto_OnValidLine() {
        String line = "1700000000000,BTC,42000.5";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isPresent());
        Crypto crypto = result.get();
        assertEquals(Instant.ofEpochMilli(1700000000000L), crypto.getTime());
        assertEquals(CryptoType.BTC, crypto.getSymbol());
        assertEquals(new BigDecimal("42000.5"), crypto.getPrice());
    }

    @Test
    void testParseCsvLine_ReturnsEmptyOptional_WhenColumnsAreMissing() {
        String line = "1700000000000,BTC";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseCsvLine_ReturnsEmptyOptional_WhenValuesAreNullOrEmpty() {
        // Empty price
        String line = "1700000000000,BTC,";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isEmpty());

        // Empty symbol
        String line2 = "1700000000000,,42000.5";
        Optional<Crypto> result2 = csvReader.parseCsvLine(line2);
        assertTrue(result2.isEmpty());
    }

    @Test
    void testParseCsvLine_ReturnsEmptyOptional_WhenSymbolIsInvalid() {
        String line = "1700000000000,INVALID,42000.5";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseCsvLine_ReturnsEmptyOptional_WhenPriceIsNotANumber() {
        String line = "1700000000000,BTC,not_a_number";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseCsvLine_ReturnsEmptyOptional_WhenTimestampIsNotANumber() {
        String line = "not_a_timestamp,BTC,42000.5";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseCsvLine_IgnoresExtraColumns() {
        String line = "1700000000000,BTC,42000.5,extra";
        Optional<Crypto> result = csvReader.parseCsvLine(line);
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("42000.5"), result.get().getPrice());
    }

}
