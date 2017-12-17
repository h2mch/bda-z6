package ch.hslu.cas.bda.ingestion.exchangerate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class CoinbaseExchangeRate {

    @CsvBindByPosition(position = 0)
    private String timeStamp;
    @CsvBindByPosition(position = 1)
    private String open;
    @CsvBindByPosition(position = 2)
    private String high;
    @CsvBindByPosition(position = 3)
    private String low;
    @CsvBindByPosition(position = 4)
    private String close;
    @CsvBindByPosition(position = 5)
    private String volumeBTC;
    @CsvBindByPosition(position = 6)
    private String volumeDollar;
    @CsvBindByPosition(position = 7)
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
