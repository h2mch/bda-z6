package ch.hslu.cas.bda.ingestion.exchangerate;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class GdaxExchangeRate {

    @CsvBindByPosition(position = 0)
    private String date_nr;
    @CsvBindByPosition(position = 1)
    private String dailyValueXAU;
    @CsvBindByPosition(position = 2)
    private String dailyValueXAG;
    @CsvBindByPosition(position = 3)
    private String dailyValueXPT;
    @CsvBindByPosition(position = 4)
    private String dailyValueCAD;
    @CsvBindByPosition(position = 5)
    private String dailyValueEUR;
    @CsvBindByPosition(position = 6)
    private String dailyValueJPY;
    @CsvBindByPosition(position = 7)
    private String dailyValueGBP;
    @CsvBindByPosition(position = 8)
    private String dailyValueCHF;
    @CsvBindByPosition(position = 9)
    private String dailyValueAUD;
    @CsvBindByPosition(position = 10)
    private String dailyValueHKD;
    @CsvBindByPosition(position = 11)
    private String dailyValueNZD;
    @CsvBindByPosition(position = 12)
    private String dailyValueKRW;
    @CsvBindByPosition(position = 13)
    private String dailyValueMXN;
    @CsvBindByPosition(position = 14)
    private String diffToPreDayxau_prc;
    @CsvBindByPosition(position = 15)
    private String diffToPreDayxag_prc;
    @CsvBindByPosition(position = 16)
    private String diffToPreDayxpt_prc;
    @CsvBindByPosition(position = 17)
    private String diffToPreDaycad_prc;
    @CsvBindByPosition(position = 18)
    private String diffToPreDayeur_prc;
    @CsvBindByPosition(position = 19)
    private String diffToPreDayjpy_prc;
    @CsvBindByPosition(position = 20)
    private String diffToPreDaygbp_prc;
    @CsvBindByPosition(position = 21)
    private String diffToPreDaychf_prc;
    @CsvBindByPosition(position = 22)
    private String diffToPreDayaud_prc;
    @CsvBindByPosition(position = 23)
    private String diffToPreDayhkd_prc;
    @CsvBindByPosition(position = 24)
    private String diffToPreDaynzd_prc;
    @CsvBindByPosition(position = 25)
    private String diffToPreDaykrw_prc;
    @CsvBindByPosition(position = 26)
    private String diffToPreDaymxn_prc;
    @CsvBindByPosition(position = 27)
    private String dailyValuebtc_low;
    @CsvBindByPosition(position = 28)
    private String dailyValuebtc_high;
    @CsvBindByPosition(position = 29)
    private String dailyValuebtc_open;
    @CsvBindByPosition(position = 30)
    private String dailyValuebtc_close;
    @CsvBindByPosition(position = 31)
    private String dailyValuebtc_volume;
    @CsvBindByPosition(position = 32)
    private String diffToPreDaybtc_low_prc;
    @CsvBindByPosition(position = 33)
    private String diffToPreDaybtc_high_prc;
    @CsvBindByPosition(position = 34)
    private String diffToPreDaybtc_open_prc;
    @CsvBindByPosition(position = 35)
    private String diffToPreDaybtc_close_prc;
    @CsvBindByPosition(position = 36)
    private String diffToPreDaybtc_volume_prc;
    @CsvBindByPosition(position = 37)
    private String dailyValueeth_low;
    @CsvBindByPosition(position = 38)
    private String dailyValueeth_high;
    @CsvBindByPosition(position = 39)
    private String dailyValueeth_open;
    @CsvBindByPosition(position = 40)
    private String dailyValueeth_close;
    @CsvBindByPosition(position = 41)
    private String dailyValueeth_volume;
    @CsvBindByPosition(position = 42)
    private String diffToPreDayeth_low_prc;
    @CsvBindByPosition(position = 43)
    private String diffToPreDayeth_high_prc;
    @CsvBindByPosition(position = 44)
    private String diffToPreDayeth_open_prc;
    @CsvBindByPosition(position = 45)
    private String diffToPreDayeth_close_prc;
    @CsvBindByPosition(position = 46)
    private String diffToPreDayeth_volume_prc;
    @CsvBindByPosition(position = 47)
    private String dailyValueltc_low;
    @CsvBindByPosition(position = 48)
    private String dailyValueltc_high;
    @CsvBindByPosition(position = 49)
    private String dailyValueltc_open;
    @CsvBindByPosition(position = 50)
    private String dailyValueltc_close;
    @CsvBindByPosition(position = 51)
    private String dailyValueltc_volume;
    @CsvBindByPosition(position = 52)
    private String diffToPreDayltc_low_prc;
    @CsvBindByPosition(position = 53)
    private String diffToPreDayltc_high_prc;
    @CsvBindByPosition(position = 54)
    private String diffToPreDayltc_open_prc;
    @CsvBindByPosition(position = 55)
    private String diffToPreDayltc_close_prc;
    @CsvBindByPosition(position = 56)
    private String diffToPreDayltc_volume_prc;


    public String getDailyValueXAU() {
        return dailyValueXAU;
    }

    public void setDailyValueXAU(String dailyValueXAU) {
        this.dailyValueXAU = dailyValueXAU;
    }

    public String getDailyValueXAG() {
        return dailyValueXAG;
    }

    public void setDailyValueXAG(String dailyValueXAG) {
        this.dailyValueXAG = dailyValueXAG;
    }

    public String getDailyValueXPT() {
        return dailyValueXPT;
    }

    public void setDailyValueXPT(String dailyValueXPT) {
        this.dailyValueXPT = dailyValueXPT;
    }

    public String getDailyValueCAD() {
        return dailyValueCAD;
    }

    public void setDailyValueCAD(String dailyValueCAD) {
        this.dailyValueCAD = dailyValueCAD;
    }

    public String getDailyValueEUR() {
        return dailyValueEUR;
    }

    public void setDailyValueEUR(String dailyValueEUR) {
        this.dailyValueEUR = dailyValueEUR;
    }

    public String getDailyValueJPY() {
        return dailyValueJPY;
    }

    public void setDailyValueJPY(String dailyValueJPY) {
        this.dailyValueJPY = dailyValueJPY;
    }

    public String getDailyValueGBP() {
        return dailyValueGBP;
    }

    public void setDailyValueGBP(String dailyValueGBP) {
        this.dailyValueGBP = dailyValueGBP;
    }

    public String getDailyValueCHF() {
        return dailyValueCHF;
    }

    public void setDailyValueCHF(String dailyValueCHF) {
        this.dailyValueCHF = dailyValueCHF;
    }

    public String getDailyValueAUD() {
        return dailyValueAUD;
    }

    public void setDailyValueAUD(String dailyValueAUD) {
        this.dailyValueAUD = dailyValueAUD;
    }

    public String getDailyValueHKD() {
        return dailyValueHKD;
    }

    public void setDailyValueHKD(String dailyValueHKD) {
        this.dailyValueHKD = dailyValueHKD;
    }

    public String getDailyValueNZD() {
        return dailyValueNZD;
    }

    public void setDailyValueNZD(String dailyValueNZD) {
        this.dailyValueNZD = dailyValueNZD;
    }

    public String getDailyValueKRW() {
        return dailyValueKRW;
    }

    public void setDailyValueKRW(String dailyValueKRW) {
        this.dailyValueKRW = dailyValueKRW;
    }

    public String getDailyValueMXN() {
        return dailyValueMXN;
    }

    public void setDailyValueMXN(String dailyValueMXN) {
        this.dailyValueMXN = dailyValueMXN;
    }

    public String getDiffToPreDayxau_prc() {
        return diffToPreDayxau_prc;
    }

    public void setDiffToPreDayxau_prc(String diffToPreDayxau_prc) {
        this.diffToPreDayxau_prc = diffToPreDayxau_prc;
    }

    public String getDiffToPreDayxag_prc() {
        return diffToPreDayxag_prc;
    }

    public void setDiffToPreDayxag_prc(String diffToPreDayxag_prc) {
        this.diffToPreDayxag_prc = diffToPreDayxag_prc;
    }

    public String getDiffToPreDayxpt_prc() {
        return diffToPreDayxpt_prc;
    }

    public void setDiffToPreDayxpt_prc(String diffToPreDayxpt_prc) {
        this.diffToPreDayxpt_prc = diffToPreDayxpt_prc;
    }

    public String getDiffToPreDaycad_prc() {
        return diffToPreDaycad_prc;
    }

    public void setDiffToPreDaycad_prc(String diffToPreDaycad_prc) {
        this.diffToPreDaycad_prc = diffToPreDaycad_prc;
    }

    public String getDiffToPreDayeur_prc() {
        return diffToPreDayeur_prc;
    }

    public void setDiffToPreDayeur_prc(String diffToPreDayeur_prc) {
        this.diffToPreDayeur_prc = diffToPreDayeur_prc;
    }

    public String getDiffToPreDayjpy_prc() {
        return diffToPreDayjpy_prc;
    }

    public void setDiffToPreDayjpy_prc(String diffToPreDayjpy_prc) {
        this.diffToPreDayjpy_prc = diffToPreDayjpy_prc;
    }

    public String getDiffToPreDaygbp_prc() {
        return diffToPreDaygbp_prc;
    }

    public void setDiffToPreDaygbp_prc(String diffToPreDaygbp_prc) {
        this.diffToPreDaygbp_prc = diffToPreDaygbp_prc;
    }

    public String getDiffToPreDaychf_prc() {
        return diffToPreDaychf_prc;
    }

    public void setDiffToPreDaychf_prc(String diffToPreDaychf_prc) {
        this.diffToPreDaychf_prc = diffToPreDaychf_prc;
    }

    public String getDiffToPreDayaud_prc() {
        return diffToPreDayaud_prc;
    }

    public void setDiffToPreDayaud_prc(String diffToPreDayaud_prc) {
        this.diffToPreDayaud_prc = diffToPreDayaud_prc;
    }

    public String getDiffToPreDayhkd_prc() {
        return diffToPreDayhkd_prc;
    }

    public void setDiffToPreDayhkd_prc(String diffToPreDayhkd_prc) {
        this.diffToPreDayhkd_prc = diffToPreDayhkd_prc;
    }

    public String getDiffToPreDaynzd_prc() {
        return diffToPreDaynzd_prc;
    }

    public void setDiffToPreDaynzd_prc(String diffToPreDaynzd_prc) {
        this.diffToPreDaynzd_prc = diffToPreDaynzd_prc;
    }

    public String getDiffToPreDaykrw_prc() {
        return diffToPreDaykrw_prc;
    }

    public void setDiffToPreDaykrw_prc(String diffToPreDaykrw_prc) {
        this.diffToPreDaykrw_prc = diffToPreDaykrw_prc;
    }

    public String getDiffToPreDaymxn_prc() {
        return diffToPreDaymxn_prc;
    }

    public void setDiffToPreDaymxn_prc(String diffToPreDaymxn_prc) {
        this.diffToPreDaymxn_prc = diffToPreDaymxn_prc;
    }

    public String getDailyValuebtc_low() {
        return dailyValuebtc_low;
    }

    public void setDailyValuebtc_low(String dailyValuebtc_low) {
        this.dailyValuebtc_low = dailyValuebtc_low;
    }

    public String getDailyValuebtc_high() {
        return dailyValuebtc_high;
    }

    public void setDailyValuebtc_high(String dailyValuebtc_high) {
        this.dailyValuebtc_high = dailyValuebtc_high;
    }

    public String getDailyValuebtc_open() {
        return dailyValuebtc_open;
    }

    public void setDailyValuebtc_open(String dailyValuebtc_open) {
        this.dailyValuebtc_open = dailyValuebtc_open;
    }

    public String getDailyValuebtc_close() {
        return dailyValuebtc_close;
    }

    public void setDailyValuebtc_close(String dailyValuebtc_close) {
        this.dailyValuebtc_close = dailyValuebtc_close;
    }

    public String getDailyValuebtc_volume() {
        return dailyValuebtc_volume;
    }

    public void setDailyValuebtc_volume(String dailyValuebtc_volume) {
        this.dailyValuebtc_volume = dailyValuebtc_volume;
    }

    public String getDiffToPreDaybtc_low_prc() {
        return diffToPreDaybtc_low_prc;
    }

    public void setDiffToPreDaybtc_low_prc(String diffToPreDaybtc_low_prc) {
        this.diffToPreDaybtc_low_prc = diffToPreDaybtc_low_prc;
    }

    public String getDiffToPreDaybtc_high_prc() {
        return diffToPreDaybtc_high_prc;
    }

    public void setDiffToPreDaybtc_high_prc(String diffToPreDaybtc_high_prc) {
        this.diffToPreDaybtc_high_prc = diffToPreDaybtc_high_prc;
    }

    public String getDiffToPreDaybtc_open_prc() {
        return diffToPreDaybtc_open_prc;
    }

    public void setDiffToPreDaybtc_open_prc(String diffToPreDaybtc_open_prc) {
        this.diffToPreDaybtc_open_prc = diffToPreDaybtc_open_prc;
    }

    public String getDiffToPreDaybtc_close_prc() {
        return diffToPreDaybtc_close_prc;
    }

    public void setDiffToPreDaybtc_close_prc(String diffToPreDaybtc_close_prc) {
        this.diffToPreDaybtc_close_prc = diffToPreDaybtc_close_prc;
    }

    public String getDiffToPreDaybtc_volume_prc() {
        return diffToPreDaybtc_volume_prc;
    }

    public void setDiffToPreDaybtc_volume_prc(String diffToPreDaybtc_volume_prc) {
        this.diffToPreDaybtc_volume_prc = diffToPreDaybtc_volume_prc;
    }

    public String getDailyValueeth_low() {
        return dailyValueeth_low;
    }

    public void setDailyValueeth_low(String dailyValueeth_low) {
        this.dailyValueeth_low = dailyValueeth_low;
    }

    public String getDailyValueeth_high() {
        return dailyValueeth_high;
    }

    public void setDailyValueeth_high(String dailyValueeth_high) {
        this.dailyValueeth_high = dailyValueeth_high;
    }

    public String getDailyValueeth_open() {
        return dailyValueeth_open;
    }

    public void setDailyValueeth_open(String dailyValueeth_open) {
        this.dailyValueeth_open = dailyValueeth_open;
    }

    public String getDailyValueeth_close() {
        return dailyValueeth_close;
    }

    public void setDailyValueeth_close(String dailyValueeth_close) {
        this.dailyValueeth_close = dailyValueeth_close;
    }

    public String getDailyValueeth_volume() {
        return dailyValueeth_volume;
    }

    public void setDailyValueeth_volume(String dailyValueeth_volume) {
        this.dailyValueeth_volume = dailyValueeth_volume;
    }

    public String getDiffToPreDayeth_low_prc() {
        return diffToPreDayeth_low_prc;
    }

    public void setDiffToPreDayeth_low_prc(String diffToPreDayeth_low_prc) {
        this.diffToPreDayeth_low_prc = diffToPreDayeth_low_prc;
    }

    public String getDiffToPreDayeth_high_prc() {
        return diffToPreDayeth_high_prc;
    }

    public void setDiffToPreDayeth_high_prc(String diffToPreDayeth_high_prc) {
        this.diffToPreDayeth_high_prc = diffToPreDayeth_high_prc;
    }

    public String getDiffToPreDayeth_open_prc() {
        return diffToPreDayeth_open_prc;
    }

    public void setDiffToPreDayeth_open_prc(String diffToPreDayeth_open_prc) {
        this.diffToPreDayeth_open_prc = diffToPreDayeth_open_prc;
    }

    public String getDiffToPreDayeth_close_prc() {
        return diffToPreDayeth_close_prc;
    }

    public void setDiffToPreDayeth_close_prc(String diffToPreDayeth_close_prc) {
        this.diffToPreDayeth_close_prc = diffToPreDayeth_close_prc;
    }

    public String getDiffToPreDayeth_volume_prc() {
        return diffToPreDayeth_volume_prc;
    }

    public void setDiffToPreDayeth_volume_prc(String diffToPreDayeth_volume_prc) {
        this.diffToPreDayeth_volume_prc = diffToPreDayeth_volume_prc;
    }

    public String getDailyValueltc_low() {
        return dailyValueltc_low;
    }

    public void setDailyValueltc_low(String dailyValueltc_low) {
        this.dailyValueltc_low = dailyValueltc_low;
    }

    public String getDailyValueltc_high() {
        return dailyValueltc_high;
    }

    public void setDailyValueltc_high(String dailyValueltc_high) {
        this.dailyValueltc_high = dailyValueltc_high;
    }

    public String getDailyValueltc_open() {
        return dailyValueltc_open;
    }

    public void setDailyValueltc_open(String dailyValueltc_open) {
        this.dailyValueltc_open = dailyValueltc_open;
    }

    public String getDailyValueltc_close() {
        return dailyValueltc_close;
    }

    public void setDailyValueltc_close(String dailyValueltc_close) {
        this.dailyValueltc_close = dailyValueltc_close;
    }

    public String getDailyValueltc_volume() {
        return dailyValueltc_volume;
    }

    public void setDailyValueltc_volume(String dailyValueltc_volume) {
        this.dailyValueltc_volume = dailyValueltc_volume;
    }

    public String getDiffToPreDayltc_low_prc() {
        return diffToPreDayltc_low_prc;
    }

    public void setDiffToPreDayltc_low_prc(String diffToPreDayltc_low_prc) {
        this.diffToPreDayltc_low_prc = diffToPreDayltc_low_prc;
    }

    public String getDiffToPreDayltc_high_prc() {
        return diffToPreDayltc_high_prc;
    }

    public void setDiffToPreDayltc_high_prc(String diffToPreDayltc_high_prc) {
        this.diffToPreDayltc_high_prc = diffToPreDayltc_high_prc;
    }

    public String getDiffToPreDayltc_open_prc() {
        return diffToPreDayltc_open_prc;
    }

    public void setDiffToPreDayltc_open_prc(String diffToPreDayltc_open_prc) {
        this.diffToPreDayltc_open_prc = diffToPreDayltc_open_prc;
    }

    public String getDiffToPreDayltc_close_prc() {
        return diffToPreDayltc_close_prc;
    }

    public void setDiffToPreDayltc_close_prc(String diffToPreDayltc_close_prc) {
        this.diffToPreDayltc_close_prc = diffToPreDayltc_close_prc;
    }

    public String getDiffToPreDayltc_volume_prc() {
        return diffToPreDayltc_volume_prc;
    }

    public void setDiffToPreDayltc_volume_prc(String diffToPreDayltc_volume_prc) {
        this.diffToPreDayltc_volume_prc = diffToPreDayltc_volume_prc;
    }

    public String getDate_nr() {
        return date_nr;
    }

    public void setDate_nr(String date_nr) {
        this.date_nr = date_nr;
    }
}
