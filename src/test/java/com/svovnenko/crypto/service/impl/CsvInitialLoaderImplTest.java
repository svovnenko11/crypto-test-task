package com.svovnenko.crypto.service.impl;

import com.svovnenko.crypto.domain.Crypto;
import com.svovnenko.crypto.domain.CryptoType;
import com.svovnenko.crypto.repository.CryptoRepository;
import com.svovnenko.crypto.repository.entities.CryptoEntity;
import com.svovnenko.crypto.service.CryptoValuesReader;
import com.svovnenko.crypto.service.mapper.CryptoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CsvInitialLoaderImplTest {

    @Mock
    private CryptoValuesReader reader;

    @Mock
    private CryptoRepository repository;

    @Mock
    private CryptoMapper mapper;

    @InjectMocks
    private CsvInitialLoaderImpl csvInitialLoader;

    public CsvInitialLoaderImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void load_WhenCryptoValuesAreRead_SavesCryptoEntitiesToRepository() {
        Instant time1 = Instant.now();
        Crypto crypto1 = Crypto.builder()
                .symbol(CryptoType.BTC)
                .time(time1)
                .price(BigDecimal.ONE).build();
        Instant time2 = Instant.now();
        Crypto crypto2 = Crypto.builder()
                .symbol(CryptoType.ETH)
                .time(time2)
                .price(BigDecimal.ONE).build();

        Map<CryptoType, List<Crypto>> cryptoTypeListMap = Map.of(
                CryptoType.BTC, List.of(crypto1),
                CryptoType.ETH, List.of(crypto2)

        );

        CryptoEntity entity1 = new CryptoEntity(null, time1, CryptoType.BTC, BigDecimal.ONE);
        CryptoEntity entity2 = new CryptoEntity(null, time1, CryptoType.ETH, BigDecimal.ONE);

        when(reader.readCryptoValues()).thenReturn(cryptoTypeListMap);
        when(mapper.toEntity(crypto1)).thenReturn(entity1);
        when(mapper.toEntity(crypto2)).thenReturn(entity2);

        csvInitialLoader.load();

        verify(repository, times(1)).saveAllAndFlush(argThat(argument -> {
            List<CryptoEntity> list = (List<CryptoEntity>) argument;
            return list.size() == 2 && list.containsAll(List.of(entity1, entity2));
        }));
    }

    @Test
    public void load_WhenNoCryptoValuesAreRead_DoesNotInteractWithRepository() {
        when(reader.readCryptoValues()).thenReturn(Map.of());

        csvInitialLoader.load();

        verify(repository, never()).saveAllAndFlush(anyList());
    }

}