package com.svovnenko.crypto.service;

import com.svovnenko.crypto.domain.Crypto;
import com.svovnenko.crypto.domain.CryptoType;

import java.util.List;
import java.util.Map;

public interface CryptoValuesReader {

    Map<CryptoType, List<Crypto>> readCryptoValues();

}
