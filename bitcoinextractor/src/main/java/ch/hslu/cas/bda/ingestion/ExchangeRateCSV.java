package ch.hslu.cas.bda.ingestion;

import com.opencsv.bean.CsvBindByPosition;

public class ExchangeRateCSV {

    @CsvBindByPosition(position = 0)
    private String date_nr;
    @CsvBindByPosition(position = 1)
    private String xau;
    @CsvBindByPosition(position = 2)
    private String xag;
    @CsvBindByPosition(position = 3)
    private String xpt;
    @CsvBindByPosition(position = 4)
    private String cad;
    @CsvBindByPosition(position = 5)
    private String eur;
    @CsvBindByPosition(position = 6)
    private String jpy;
    @CsvBindByPosition(position = 7)
    private String gbp;
    @CsvBindByPosition(position = 8)
    private String chf;
    @CsvBindByPosition(position = 9)
    private String aud;
    @CsvBindByPosition(position = 10)
    private String hkd;
    @CsvBindByPosition(position = 11)
    private String nzd;
    @CsvBindByPosition(position = 12)
    private String krw;
    @CsvBindByPosition(position = 13)
    private String mxn;
    @CsvBindByPosition(position = 14)
    private String xau_prc;
    @CsvBindByPosition(position = 15)
    private String xag_prc;
    @CsvBindByPosition(position = 16)
    private String xpt_prc;
    @CsvBindByPosition(position = 17)
    private String cad_prc;
    @CsvBindByPosition(position = 18)
    private String eur_prc;
    @CsvBindByPosition(position = 19)
    private String jpy_prc;
    @CsvBindByPosition(position = 20)
    private String gbp_prc;
    @CsvBindByPosition(position = 21)
    private String chf_prc;
    @CsvBindByPosition(position = 22)
    private String aud_prc;
    @CsvBindByPosition(position = 23)
    private String hkd_prc;
    @CsvBindByPosition(position = 24)
    private String nzd_prc;
    @CsvBindByPosition(position = 25)
    private String krw_prc;
    @CsvBindByPosition(position = 26)
    private String mxn_prc;
    @CsvBindByPosition(position = 27)
    private String xau_20_volty;
    @CsvBindByPosition(position = 28)
    private String xag_20_volty;
    @CsvBindByPosition(position = 29)
    private String xpt_20_volty;
    @CsvBindByPosition(position = 30)
    private String cad_20_volty;
    @CsvBindByPosition(position = 31)
    private String eur_20_volty;
    @CsvBindByPosition(position = 32)
    private String jpy_20_volty;
    @CsvBindByPosition(position = 33)
    private String gbp_20_volty;
    @CsvBindByPosition(position = 34)
    private String chf_20_volty;
    @CsvBindByPosition(position = 35)
    private String aud_20_volty;
    @CsvBindByPosition(position = 36)
    private String hkd_20_volty;
    @CsvBindByPosition(position = 37)
    private String nzd_20_volty;
    @CsvBindByPosition(position = 38)
    private String krw_20_volty;
    @CsvBindByPosition(position = 39)
    private String mxn_20_volty;
    @CsvBindByPosition(position = 40)
    private String btc_close;
    @CsvBindByPosition(position = 41)
    private String btc_volume;
    @CsvBindByPosition(position = 42)
    private String btc_close_20_volty;
    @CsvBindByPosition(position = 43)
    private String btc_volume_20_volty;
    @CsvBindByPosition(position = 44)
    private String btc_1_volty;
    @CsvBindByPosition(position = 45)
    private String eth_close;
    @CsvBindByPosition(position = 46)
    private String eth_volume;
    @CsvBindByPosition(position = 47)
    private String eth_close_20_volty;
    @CsvBindByPosition(position = 48)
    private String eth_volume_20_volty;
    @CsvBindByPosition(position = 49)
    private String eth_1_volty;
    @CsvBindByPosition(position = 50)
    private String ltc_close;
    @CsvBindByPosition(position = 51)
    private String ltc_volume;
    @CsvBindByPosition(position = 52)
    private String ltc_close_20_volty;
    @CsvBindByPosition(position = 53)
    private String ltc_volume_20_volty;
    @CsvBindByPosition(position = 54)
    private String ltc_1_volty;
    @CsvBindByPosition(position = 55)
    private String btc_close_prc_nextday;
    @CsvBindByPosition(position = 56)
    private String btc_price_direction;
    @CsvBindByPosition(position = 57)
    private String btc_prc_bucket;
    @CsvBindByPosition(position = 58)
    private String CHN;
    @CsvBindByPosition(position = 59)
    private String RUS;
    @CsvBindByPosition(position = 60)
    private String KOR;
    @CsvBindByPosition(position = 61)
    private String NGA;
    @CsvBindByPosition(position = 62)
    private String AF;
    @CsvBindByPosition(position = 63)
    private String AN;
    @CsvBindByPosition(position = 64)
    private String AS;
    @CsvBindByPosition(position = 65)
    private String EU;
    @CsvBindByPosition(position = 66)
    private String NA;
    @CsvBindByPosition(position = 67)
    private String OC;
    @CsvBindByPosition(position = 68)
    private String SA;
    @CsvBindByPosition(position = 69)
    private String totalCount;
    @CsvBindByPosition(position = 70)
    private String gsAvg;
    @CsvBindByPosition(position = 71)
    private String nmSum;

    public String getDate_nr() {
        return date_nr;
    }

    public void setDate_nr(String date_nr) {
        this.date_nr = date_nr;
    }

    public String getXau() {
        return xau;
    }

    public void setXau(String xau) {
        this.xau = xau;
    }

    public String getXag() {
        return xag;
    }

    public void setXag(String xag) {
        this.xag = xag;
    }

    public String getXpt() {
        return xpt;
    }

    public void setXpt(String xpt) {
        this.xpt = xpt;
    }

    public String getCad() {
        return cad;
    }

    public void setCad(String cad) {
        this.cad = cad;
    }

    public String getEur() {
        return eur;
    }

    public void setEur(String eur) {
        this.eur = eur;
    }

    public String getJpy() {
        return jpy;
    }

    public void setJpy(String jpy) {
        this.jpy = jpy;
    }

    public String getGbp() {
        return gbp;
    }

    public void setGbp(String gbp) {
        this.gbp = gbp;
    }

    public String getChf() {
        return chf;
    }

    public void setChf(String chf) {
        this.chf = chf;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getHkd() {
        return hkd;
    }

    public void setHkd(String hkd) {
        this.hkd = hkd;
    }

    public String getNzd() {
        return nzd;
    }

    public void setNzd(String nzd) {
        this.nzd = nzd;
    }

    public String getKrw() {
        return krw;
    }

    public void setKrw(String krw) {
        this.krw = krw;
    }

    public String getMxn() {
        return mxn;
    }

    public void setMxn(String mxn) {
        this.mxn = mxn;
    }

    public String getXau_prc() {
        return xau_prc;
    }

    public void setXau_prc(String xau_prc) {
        this.xau_prc = xau_prc;
    }

    public String getXag_prc() {
        return xag_prc;
    }

    public void setXag_prc(String xag_prc) {
        this.xag_prc = xag_prc;
    }

    public String getXpt_prc() {
        return xpt_prc;
    }

    public void setXpt_prc(String xpt_prc) {
        this.xpt_prc = xpt_prc;
    }

    public String getCad_prc() {
        return cad_prc;
    }

    public void setCad_prc(String cad_prc) {
        this.cad_prc = cad_prc;
    }

    public String getEur_prc() {
        return eur_prc;
    }

    public void setEur_prc(String eur_prc) {
        this.eur_prc = eur_prc;
    }

    public String getJpy_prc() {
        return jpy_prc;
    }

    public void setJpy_prc(String jpy_prc) {
        this.jpy_prc = jpy_prc;
    }

    public String getGbp_prc() {
        return gbp_prc;
    }

    public void setGbp_prc(String gbp_prc) {
        this.gbp_prc = gbp_prc;
    }

    public String getChf_prc() {
        return chf_prc;
    }

    public void setChf_prc(String chf_prc) {
        this.chf_prc = chf_prc;
    }

    public String getAud_prc() {
        return aud_prc;
    }

    public void setAud_prc(String aud_prc) {
        this.aud_prc = aud_prc;
    }

    public String getHkd_prc() {
        return hkd_prc;
    }

    public void setHkd_prc(String hkd_prc) {
        this.hkd_prc = hkd_prc;
    }

    public String getNzd_prc() {
        return nzd_prc;
    }

    public void setNzd_prc(String nzd_prc) {
        this.nzd_prc = nzd_prc;
    }

    public String getKrw_prc() {
        return krw_prc;
    }

    public void setKrw_prc(String krw_prc) {
        this.krw_prc = krw_prc;
    }

    public String getMxn_prc() {
        return mxn_prc;
    }

    public void setMxn_prc(String mxn_prc) {
        this.mxn_prc = mxn_prc;
    }

    public String getXau_20_volty() {
        return xau_20_volty;
    }

    public void setXau_20_volty(String xau_20_volty) {
        this.xau_20_volty = xau_20_volty;
    }

    public String getXag_20_volty() {
        return xag_20_volty;
    }

    public void setXag_20_volty(String xag_20_volty) {
        this.xag_20_volty = xag_20_volty;
    }

    public String getXpt_20_volty() {
        return xpt_20_volty;
    }

    public void setXpt_20_volty(String xpt_20_volty) {
        this.xpt_20_volty = xpt_20_volty;
    }

    public String getCad_20_volty() {
        return cad_20_volty;
    }

    public void setCad_20_volty(String cad_20_volty) {
        this.cad_20_volty = cad_20_volty;
    }

    public String getEur_20_volty() {
        return eur_20_volty;
    }

    public void setEur_20_volty(String eur_20_volty) {
        this.eur_20_volty = eur_20_volty;
    }

    public String getJpy_20_volty() {
        return jpy_20_volty;
    }

    public void setJpy_20_volty(String jpy_20_volty) {
        this.jpy_20_volty = jpy_20_volty;
    }

    public String getGbp_20_volty() {
        return gbp_20_volty;
    }

    public void setGbp_20_volty(String gbp_20_volty) {
        this.gbp_20_volty = gbp_20_volty;
    }

    public String getChf_20_volty() {
        return chf_20_volty;
    }

    public void setChf_20_volty(String chf_20_volty) {
        this.chf_20_volty = chf_20_volty;
    }

    public String getAud_20_volty() {
        return aud_20_volty;
    }

    public void setAud_20_volty(String aud_20_volty) {
        this.aud_20_volty = aud_20_volty;
    }

    public String getHkd_20_volty() {
        return hkd_20_volty;
    }

    public void setHkd_20_volty(String hkd_20_volty) {
        this.hkd_20_volty = hkd_20_volty;
    }

    public String getNzd_20_volty() {
        return nzd_20_volty;
    }

    public void setNzd_20_volty(String nzd_20_volty) {
        this.nzd_20_volty = nzd_20_volty;
    }

    public String getKrw_20_volty() {
        return krw_20_volty;
    }

    public void setKrw_20_volty(String krw_20_volty) {
        this.krw_20_volty = krw_20_volty;
    }

    public String getMxn_20_volty() {
        return mxn_20_volty;
    }

    public void setMxn_20_volty(String mxn_20_volty) {
        this.mxn_20_volty = mxn_20_volty;
    }

    public String getBtc_close() {
        return btc_close;
    }

    public void setBtc_close(String btc_close) {
        this.btc_close = btc_close;
    }

    public String getBtc_volume() {
        return btc_volume;
    }

    public void setBtc_volume(String btc_volume) {
        this.btc_volume = btc_volume;
    }

    public String getBtc_close_20_volty() {
        return btc_close_20_volty;
    }

    public void setBtc_close_20_volty(String btc_close_20_volty) {
        this.btc_close_20_volty = btc_close_20_volty;
    }

    public String getBtc_volume_20_volty() {
        return btc_volume_20_volty;
    }

    public void setBtc_volume_20_volty(String btc_volume_20_volty) {
        this.btc_volume_20_volty = btc_volume_20_volty;
    }

    public String getBtc_1_volty() {
        return btc_1_volty;
    }

    public void setBtc_1_volty(String btc_1_volty) {
        this.btc_1_volty = btc_1_volty;
    }

    public String getEth_close() {
        return eth_close;
    }

    public void setEth_close(String eth_close) {
        this.eth_close = eth_close;
    }

    public String getEth_volume() {
        return eth_volume;
    }

    public void setEth_volume(String eth_volume) {
        this.eth_volume = eth_volume;
    }

    public String getEth_close_20_volty() {
        return eth_close_20_volty;
    }

    public void setEth_close_20_volty(String eth_close_20_volty) {
        this.eth_close_20_volty = eth_close_20_volty;
    }

    public String getEth_volume_20_volty() {
        return eth_volume_20_volty;
    }

    public void setEth_volume_20_volty(String eth_volume_20_volty) {
        this.eth_volume_20_volty = eth_volume_20_volty;
    }

    public String getEth_1_volty() {
        return eth_1_volty;
    }

    public void setEth_1_volty(String eth_1_volty) {
        this.eth_1_volty = eth_1_volty;
    }

    public String getLtc_close() {
        return ltc_close;
    }

    public void setLtc_close(String ltc_close) {
        this.ltc_close = ltc_close;
    }

    public String getLtc_volume() {
        return ltc_volume;
    }

    public void setLtc_volume(String ltc_volume) {
        this.ltc_volume = ltc_volume;
    }

    public String getLtc_close_20_volty() {
        return ltc_close_20_volty;
    }

    public void setLtc_close_20_volty(String ltc_close_20_volty) {
        this.ltc_close_20_volty = ltc_close_20_volty;
    }

    public String getLtc_volume_20_volty() {
        return ltc_volume_20_volty;
    }

    public void setLtc_volume_20_volty(String ltc_volume_20_volty) {
        this.ltc_volume_20_volty = ltc_volume_20_volty;
    }

    public String getLtc_1_volty() {
        return ltc_1_volty;
    }

    public void setLtc_1_volty(String ltc_1_volty) {
        this.ltc_1_volty = ltc_1_volty;
    }

    public String getBtc_close_prc_nextday() {
        return btc_close_prc_nextday;
    }

    public void setBtc_close_prc_nextday(String btc_close_prc_nextday) {
        this.btc_close_prc_nextday = btc_close_prc_nextday;
    }

    public String getBtc_price_direction() {
        return btc_price_direction;
    }

    public void setBtc_price_direction(String btc_price_direction) {
        this.btc_price_direction = btc_price_direction;
    }

    public String getBtc_prc_bucket() {
        return btc_prc_bucket;
    }

    public void setBtc_prc_bucket(String btc_prc_bucket) {
        this.btc_prc_bucket = btc_prc_bucket;
    }

    public String getCHN() {
        return CHN;
    }

    public void setCHN(String CHN) {
        this.CHN = CHN;
    }

    public String getRUS() {
        return RUS;
    }

    public void setRUS(String RUS) {
        this.RUS = RUS;
    }

    public String getKOR() {
        return KOR;
    }

    public void setKOR(String KOR) {
        this.KOR = KOR;
    }

    public String getNGA() {
        return NGA;
    }

    public void setNGA(String NGA) {
        this.NGA = NGA;
    }

    public String getAF() {
        return AF;
    }

    public void setAF(String AF) {
        this.AF = AF;
    }

    public String getAN() {
        return AN;
    }

    public void setAN(String AN) {
        this.AN = AN;
    }

    public String getAS() {
        return AS;
    }

    public void setAS(String AS) {
        this.AS = AS;
    }

    public String getEU() {
        return EU;
    }

    public void setEU(String EU) {
        this.EU = EU;
    }

    public String getNA() {
        return NA;
    }

    public void setNA(String NA) {
        this.NA = NA;
    }

    public String getOC() {
        return OC;
    }

    public void setOC(String OC) {
        this.OC = OC;
    }

    public String getSA() {
        return SA;
    }

    public void setSA(String SA) {
        this.SA = SA;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getGsAvg() {
        return gsAvg;
    }

    public void setGsAvg(String gsAvg) {
        this.gsAvg = gsAvg;
    }

    public String getNmSum() {
        return nmSum;
    }

    public void setNmSum(String nmSum) {
        this.nmSum = nmSum;
    }
}
