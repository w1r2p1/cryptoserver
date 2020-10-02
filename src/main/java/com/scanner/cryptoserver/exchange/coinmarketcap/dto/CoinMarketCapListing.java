package com.scanner.cryptoserver.exchange.coinmarketcap.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class CoinMarketCapListing {
    private Map<Integer, CoinMarketCapData> data = new HashMap<>();

    /**
     * Convert a coin market cap data list to a coin market cap listing.
     * The purpose of this is to put the list of data in a Map for better lookup performance.
     *
     * @param dataList the list of coin market cap data - usually parsed from Json.
     * @return the listing with the map.
     */
    public CoinMarketCapListing convertToCoinMarketCapListing(List<CoinMarketCapData> dataList) {
        CoinMarketCapListing coinMarketCapListing = new CoinMarketCapListing();
        if (dataList == null || dataList.size() == 0) {
            return coinMarketCapListing;
        }
        Map<Integer, CoinMarketCapData> map = dataList.stream().collect(Collectors.toMap(CoinMarketCapData::getId, d -> d));
        coinMarketCapListing.setData(map);
        return coinMarketCapListing;
    }

    /**
     * Find the coin market cap data using the symbol (i.e. "BTC") or name (i.e. "Bitcoin"), if necessary.
     * There are sometimes multiple coins with the same symbol. In this case, the name will be used
     * to get the coin wanted among the duplicates.
     * The purpose of this method is to find the data when an id is not available.
     *
     * @param symbol the coin - such as "ETH" or "LTC"
     * @param name   the name of the coin, such as "Bitcoin" or "Ether"
     * @return return the coin market cap data, if found.
     */
    public Optional<CoinMarketCapData> findData(String symbol, String name) {
        if (data != null) {
            return data.values().stream()
                    .filter(d -> d.isCoin(symbol))
                    .filter(d -> name.isEmpty() || d.isCoin(name))
                    .findFirst();
        }
        return Optional.empty();
    }
}
