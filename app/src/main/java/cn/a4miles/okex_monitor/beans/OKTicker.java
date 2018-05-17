/**
 * Copyright 2018 bejson.com
 */
package cn.a4miles.okex_monitor.beans;

/**
 * Auto-generated: 2018-05-14 17:7:48
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OKTicker {

    private String high;
    private String vol;
    private String day_high;
    private String last;
    private String low;
    private String contract_id;
    private String buy;
    private String sell;
    private String coin_vol;
    private String day_low;
    private String unit_amount;

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getDay_high() {
        return day_high;
    }

    public void setDay_high(String day_high) {
        this.day_high = day_high;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getCoin_vol() {
        return coin_vol;
    }

    public void setCoin_vol(String coin_vol) {
        this.coin_vol = coin_vol;
    }

    public String getDay_low() {
        return day_low;
    }

    public void setDay_low(String day_low) {
        this.day_low = day_low;
    }

    public String getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(String unit_amount) {
        this.unit_amount = unit_amount;
    }

    @Override
    public String toString() {
        return "OKTicker{" +
                "high=" + high +
                ", vol=" + vol +
                ", day_high=" + day_high +
                ", last=" + last +
                ", low=" + low +
                ", contract_id=" + contract_id +
                ", buy=" + buy +
                ", sell=" + sell +
                ", coin_vol=" + coin_vol +
                ", day_low=" + day_low +
                ", unit_amount=" + unit_amount +
                '}';
    }
}