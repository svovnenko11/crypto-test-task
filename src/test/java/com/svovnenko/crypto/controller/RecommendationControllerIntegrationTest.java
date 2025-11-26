package com.svovnenko.crypto.controller;

import com.svovnenko.crypto.domain.CryptoStatistic;
import com.svovnenko.crypto.domain.NormalizedValue;
import com.svovnenko.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class)
class RecommendationControllerIntegrationTest {

    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String START_DATE = "2023-01-01";
    public static final String END_DATE = "2023-12-31";
    public static final String BTC = "BTC";
    public static final String SYMBOL = "symbol";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CryptoRepository cryptoRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        reset(cryptoRepository);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    //------------------------------------------------------
    // Normalized list tests
    //------------------------------------------------------
    @Test
    public void normalizedListRangeGet_HappyPath_ReturnsOk() throws Exception {
        when(cryptoRepository.findAllNormalizedValues(any(), any()))
                .thenReturn(List.of(
                        new NormalizedValue(BTC, BigDecimal.ONE),
                        new NormalizedValue("DOGE", BigDecimal.TEN)));
        mockMvc.perform(get("/recommendation/normalized-range")
                        .param(FROM, START_DATE)
                        .param(TO, END_DATE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(containsString("{\"data\":" +
                                "{\"values\":[" +
                                "{\"normalizedRange\":1.0,\"symbol\":\"BTC\"}," +
                                "{\"normalizedRange\":10.0,\"symbol\":\"DOGE\"}]}}")));

        verify(cryptoRepository).findAllNormalizedValues(
                eq(LocalDate.of(2023, 1, 1)), eq(LocalDate.of(2023, 12, 31)));
        verify(cryptoRepository, never()).findAllNormalizedValues();
    }

    @Test
    public void normalizedList_HappyPath_ReturnsOk() throws Exception {
        when(cryptoRepository.findAllNormalizedValues())
                .thenReturn(List.of(
                        new NormalizedValue(BTC, BigDecimal.ONE),
                        new NormalizedValue("DOGE", BigDecimal.TEN)));
        mockMvc.perform(get("/recommendation/normalized-range"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(containsString("{\"data\":" +
                                "{\"values\":[" +
                                "{\"normalizedRange\":1.0,\"symbol\":\"BTC\"}," +
                                "{\"normalizedRange\":10.0,\"symbol\":\"DOGE\"}]}}")));

        verify(cryptoRepository, never()).findAllNormalizedValues(any(), any());
        verify(cryptoRepository).findAllNormalizedValues();
    }

    @Test
    public void normalizedList_IncorrectInput_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/normalized-range")
                        .param(FROM, "invalid date"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Invalid parameter 'from': invalid date ")));

        verify(cryptoRepository, never()).findAllNormalizedValues(any(), any());
        verify(cryptoRepository, never()).findAllNormalizedValues();
    }

    @Test
    public void normalizedList_FromIsAfterTo_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/normalized-range")
                        .param(FROM, END_DATE)
                        .param(TO, START_DATE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Incorrect dates. Range start (2023-12-31) is after range end (2023-01-01)")));

        verify(cryptoRepository, never()).findAllNormalizedValues(any(), any());
        verify(cryptoRepository, never()).findAllNormalizedValues();
    }

    @Test
    public void normalizedList_GeneralError_Returns503() throws Exception {
        when(cryptoRepository.findAllNormalizedValues()).thenThrow(new NullPointerException());
        mockMvc.perform(get("/recommendation/normalized-range")
                        .param(FROM, START_DATE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Something went wrong")));
    }

    //------------------------------------------------------
    // highestNormalized tests
    //------------------------------------------------------
    @Test
    public void highestNormalizedRangeGet_HappyPath_ReturnsOk() throws Exception {
        when(cryptoRepository.findHighestNormalizedRange(any(), any()))
                .thenReturn(Optional.of(new NormalizedValue(BTC, BigDecimal.ONE)));
        mockMvc.perform(get("/recommendation/highest-normalized-range")
                        .param(FROM, START_DATE)
                        .param(TO, END_DATE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(containsString("{\"data\":{\"normalizedRange\":1.0,\"symbol\":\"BTC\"}}")));


        verify(cryptoRepository).findHighestNormalizedRange(
                eq(LocalDate.of(2023, 1, 1)), eq(LocalDate.of(2023, 12, 31)));
        verify(cryptoRepository, never()).findHighestNormalizedForDay(any());
    }

    @Test
    public void highestNormalizedForDay_HappyPath_ReturnsOk() throws Exception {
        when(cryptoRepository.findHighestNormalizedForDay(any()))
                .thenReturn(Optional.of(new NormalizedValue(BTC, BigDecimal.ONE)));
        mockMvc.perform(get("/recommendation/highest-normalized-range")
                        .param(FROM, START_DATE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(containsString("{\"data\":{\"normalizedRange\":1.0,\"symbol\":\"BTC\"}}")));

        verify(cryptoRepository, never()).findHighestNormalizedRange(any(), any());
        verify(cryptoRepository).findHighestNormalizedForDay(eq(LocalDate.of(2023, 1, 1)));
    }

    @Test
    public void highestNormalizedForDay_IncorrectInput_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/highest-normalized-range")
                        .param(FROM, "invalid date"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Invalid parameter 'from': invalid date ")));

        verify(cryptoRepository, never()).findHighestNormalizedRange(any(), any());
        verify(cryptoRepository, never()).findHighestNormalizedForDay(any());
    }

    @Test
    public void highestNormalizedForDay_FromIsAfterTo_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/highest-normalized-range")
                        .param(FROM, END_DATE)
                        .param(TO, START_DATE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Incorrect dates. Range start (2023-12-31) is after range end (2023-01-01)")));

        verify(cryptoRepository, never()).findHighestNormalizedRange(any(), any());
        verify(cryptoRepository, never()).findHighestNormalizedForDay(any());
    }

    @Test
    public void highestNormalizedForDay_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/recommendation/highest-normalized-range")
                        .param(FROM, START_DATE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("No data found for this range")));

    }

    @Test
    public void highestNormalizedForDay_GeneralError_Returns503() throws Exception {
        when(cryptoRepository.findHighestNormalizedForDay(any())).thenThrow(new NullPointerException());
        mockMvc.perform(get("/recommendation/highest-normalized-range")
                        .param(FROM, START_DATE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Something went wrong")));

    }

    //------------------------------------------------------
    // statistic tests
    //------------------------------------------------------
    @Test
    public void statisticRangeGet_HappyPath_ReturnsOk() throws Exception {
        when(cryptoRepository.findAggregatedValuesBySymbolAndDateRange(any(), any(), any()))
                .thenReturn(Optional.of(
                        new CryptoStatistic(BTC, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)));
        mockMvc.perform(get("/recommendation/BTC/statistics")
                        .param(FROM, START_DATE)
                        .param(TO, END_DATE)
                        .param(SYMBOL, BTC))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(containsString("{\"data\":" +
                                "{\"maxPrice\":1.0," +
                                "\"minPrice\":1.0," +
                                "\"newestPrice\":1.0," +
                                "\"oldestPrice\":1.0," +
                                "\"symbol\":\"BTC\"}}")));


        verify(cryptoRepository).findAggregatedValuesBySymbolAndDateRange(eq(BTC),
                eq(LocalDate.of(2023, 1, 1)), eq(LocalDate.of(2023, 12, 31)));
        verify(cryptoRepository, never()).findAggregatedValuesBySymbol(any());
    }

    @Test
    public void statisticGet_HappyPath_ReturnsOk() throws Exception {
        when(cryptoRepository.findAggregatedValuesBySymbol(BTC))
                .thenReturn(Optional.of(
                        new CryptoStatistic(BTC, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)));
        mockMvc.perform(get("/recommendation/BTC/statistics")
                        .param(SYMBOL, BTC))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(containsString("{\"data\":" +
                                "{\"maxPrice\":1.0," +
                                "\"minPrice\":1.0," +
                                "\"newestPrice\":1.0," +
                                "\"oldestPrice\":1.0," +
                                "\"symbol\":\"BTC\"}}")));

        verify(cryptoRepository, never()).findAggregatedValuesBySymbolAndDateRange(any(),any(),any());
        verify(cryptoRepository).findAggregatedValuesBySymbol(BTC);
    }

    @Test
    public void statisticGet_IncorrectInput_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/BTC/statistics")
                        .param(SYMBOL, BTC)
                        .param(FROM, "invalid date"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Invalid parameter 'from': invalid date ")));

        verify(cryptoRepository, never()).findAggregatedValuesBySymbolAndDateRange(any(), any(), any());
        verify(cryptoRepository, never()).findAggregatedValuesBySymbol(any());
    }

    @Test
    public void statisticGet_IncorrectSymbolInput_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/invalid/statistics"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Invalid parameter 'symbol': invalid ")));

        verify(cryptoRepository, never()).findAggregatedValuesBySymbolAndDateRange(any(), any(), any());
        verify(cryptoRepository, never()).findAggregatedValuesBySymbol(any());
    }

    @Test
    public void statisticGet_FromIsAfterTo_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/recommendation/BTC/statistics")
                        .param(SYMBOL, BTC)
                        .param(FROM, END_DATE)
                        .param(TO, START_DATE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Incorrect dates. Range start (2023-12-31) is after range end (2023-01-01)")));

        verify(cryptoRepository, never()).findAggregatedValuesBySymbolAndDateRange(any(), any(), any());
        verify(cryptoRepository, never()).findAggregatedValuesBySymbol(any());
    }

    @Test
    public void statisticGet_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/recommendation/BTC/statistics")
                        .param(SYMBOL, BTC))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Crypto not found: BTC")));

    }
//
    @Test
    public void statisticGet_GeneralError_Returns503() throws Exception {
        when(cryptoRepository.findAggregatedValuesBySymbol(BTC)).thenThrow(new NullPointerException());
        mockMvc.perform(get("/recommendation/BTC/statistics")
                        .param(FROM, START_DATE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content()
                        .string(containsString("Something went wrong")));

    }
}
