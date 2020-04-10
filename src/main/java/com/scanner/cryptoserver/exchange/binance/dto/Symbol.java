package com.scanner.cryptoserver.exchange.binance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scanner.cryptoserver.exchange.coinmarketcap.dto.CoinMarketCapMap;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Symbol {
    //the symbol of the coin, such as BTCUSDT, or LTCUSD
    private String symbol;
    //the base asset, such as BTC, or LTC
    private String baseAsset;
    //the market, or quote, of the asset such as USDT or USD
    private String quoteAsset;
    //status - whether the coin is trading, etc.
    private String status;
    //the market cap in $USD
    private Double marketCap = 0.0;

    /**
     * Add the market cap value from the coin market cap to the symbol.
     *
     * @param coinMarketCapInfo the coin market cap information which contains the symbol, which has the market cap value.
     */
    public void addMarketCap(CoinMarketCapMap coinMarketCapInfo) {
        //find the symbol (i.e. "BTC") in the coin market cap info, and get the market cap value from it and set it in the exchange symbol
        coinMarketCapInfo.getData()
                .stream()
                .filter(c -> c.getSymbol().equals(getBaseAsset()))
                .findFirst()
                .ifPresent(cap -> setMarketCap(cap.getMarketCap()));
    }
}
