package ch.hslu.cas.bda.ingestion.exchangerate;

import com.opencsv.bean.CsvBindByName;

public class CoinbaseExchangeRate {

    @CsvBindByName()
    private String timeStamp;
    @CsvBindByName
    private String open;
    @CsvBindByName
    private String high;
    @CsvBindByName
    private String low;
    @CsvBindByName
    private String close;
    @CsvBindByName(column = "Volume_(BTC)")
    private String volumeBTC;
    @CsvBindByName(column = "Volume_(Currency)")
    private String volumeDollar;
    @CsvBindByName(column = "Weighted_Price")
    private String weightedPrice;

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getVolumeBTC() {
        return volumeBTC;
    }

    public void setVolumeBTC(String volumeBTC) {
        this.volumeBTC = volumeBTC;
    }

    public String getVolumeDollar() {
        return volumeDollar;
    }

    public void setVolumeDollar(String volumeDollar) {
        this.volumeDollar = volumeDollar;
    }

    public String getWeightedPrice() {
        return weightedPrice;
    }

    public void setWeightedPrice(String weightedPrice) {
        this.weightedPrice = weightedPrice;
    }
}
