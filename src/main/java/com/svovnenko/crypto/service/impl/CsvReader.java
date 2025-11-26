package com.svovnenko.crypto.service.impl;

import com.svovnenko.crypto.domain.Crypto;
import com.svovnenko.crypto.domain.CryptoType;
import com.svovnenko.crypto.service.CryptoValuesReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CsvReader implements CryptoValuesReader {

    public static final String FILE_DELIMITER = "_";

    /**
     * Read all values from resources/static/prices and put into DB
     * In test task if for example DOGE value is present in BTC file still store it,
     * in real world should be business consultation what to do in such cases
     *
     * @return map with key = crypto symbol and list of all values
     */
    @Override
    public Map<CryptoType, List<Crypto>> readCryptoValues() {
        return readAllCsvFromResources();
    }

    /**
     * read all files ends with .csv and map result
     * In case of exception - log and return empty map
     */
    private Map<CryptoType, List<Crypto>> readAllCsvFromResources() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:static/prices/*.csv");

            return Arrays.stream(resources)
                    .map(resource -> {
                        try {
                            return Map.entry(
                                    extractCryptoTypeFromFile(resource.getFilename()),
                                    readSingleCsv(resource.getInputStream())
                            );
                        } catch (IOException e) {
                            log.error("Error reading CSV resource: {}", resource.getFilename(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(entry -> !entry.getValue().isEmpty())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            log.error("Error during reading prices from input CSV files", e);
        }
        return Collections.emptyMap();
    }

    /**
     * get crypto symbol from file name
     *
     * @return CryptoType enum
     */
    private CryptoType extractCryptoTypeFromFile(String filename) {
        return CryptoType.valueOf(filename.split(FILE_DELIMITER)[0].toUpperCase());
    }

    protected List<Crypto> readSingleCsv(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = reader.lines().toList();
            return lines.stream()
                    .skip(1)
                    .map(this::parseCsvLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (IOException | UncheckedIOException e) {
            log.error("Error during parsing prices from input CSV stream.", e);
            return new ArrayList<>();
        }
    }

    /**
     * Parse csv file by line
     * in case of issue - skip line
     * @param line
     */
    //todo add some mapper for possible file structure change
    protected Optional<Crypto> parseCsvLine(String line) {
        try {
            String[] values = line.split(",");
            Instant timestamp = Instant.ofEpochMilli(Long.parseLong(values[0]));
            CryptoType symbol = CryptoType.valueOf(Optional.ofNullable(values[1]).orElse("").toUpperCase());
            BigDecimal price = new BigDecimal(Optional.ofNullable(values[2]).orElse("0"));

            return Optional.of(new Crypto(timestamp, symbol, price));
        } catch (Exception e) {
            log.error("Error parsing line: {}. Skipping this entry.", line, e);
            return Optional.empty();
        }
    }
}
