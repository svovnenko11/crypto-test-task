package com.svovnenko.crypto.service.impl;

import com.svovnenko.crypto.domain.Crypto;
import com.svovnenko.crypto.domain.CryptoType;
import com.svovnenko.crypto.repository.CryptoRepository;
import com.svovnenko.crypto.repository.entities.CryptoEntity;
import com.svovnenko.crypto.service.CryptoValuesReader;
import com.svovnenko.crypto.service.InitialLoader;
import com.svovnenko.crypto.service.mapper.CryptoMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CsvInitialLoaderImpl implements InitialLoader {

    @Autowired
    private CryptoValuesReader reader;

    @Autowired
    private CryptoRepository repository;

    @Autowired
    private CryptoMapper mapper;

    /**
     * For the test task using H2 in memory DB. And initialoaded it once on application start
     * Read all values from resources/static/prices and put into DB
     *
     * For the real world scenario need to know how much and how often new values will appear
     * 1) Initial load should be one time operation. Possibly with just insert into DB (easiest)
     *  or some dataflow for big numbers of data
     * 2) It is dynamic unlimited during the day - some messaging/listener should be introduced
     * 3) If it is once per day file with values are loaded - then some job.
     *  Which also can calculate most common API call calculation.
     *  For example - precalculate and store in db normalized range for all crypto. Or some basic statistic
     */
    @PostConstruct
    public void load() {
        Map<CryptoType, List<Crypto>> cryptoTypeListMap = reader.readCryptoValues();
        List<CryptoEntity> cryptoList = cryptoTypeListMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .map(mapper::toEntity)
                .toList();
        if (cryptoList.isEmpty()) {
            return;
        }
        repository.saveAllAndFlush(cryptoList);
    }
}
