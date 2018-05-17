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
public class OKDateTickers {

    private String date;
    private OKTicker ticker;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setOKTicker(OKTicker OKTicker) {
        this.ticker = OKTicker;
    }

    public OKTicker getOKTicker() {
        return ticker;
    }

    @Override
    public String toString() {
        return "OKDateTickers{" +
                "date='" + date + '\'' +
                ", OKTicker=" + ticker +
                '}';
    }
}