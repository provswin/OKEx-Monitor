package cn.a4miles.okex_monitor.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.a4miles.okex_monitor.adapter.OKExpandableItemAdapter;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public class OKSymbolTickersLevelItem implements MultiItemEntity {
    private String symbol;

    private String date;

    private String sell;

    private String thisWeekBuy;

    private String thisWeekSpread;

    private String nextWeekBuy;

    private String nextWeekSpread;

    private String quarterBuy;

    private String quarterSpread;

    private int position;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getThisWeekBuy() {
        return thisWeekBuy;
    }

    public void setThisWeekBuy(String thisWeekBuy) {
        this.thisWeekBuy = thisWeekBuy;
    }

    public String getThisWeekSpread() {
        return thisWeekSpread;
    }

    public void setThisWeekSpread(String thisWeekSpread) {
        this.thisWeekSpread = thisWeekSpread;
    }

    public String getNextWeekBuy() {
        return nextWeekBuy;
    }

    public void setNextWeekBuy(String nextWeekBuy) {
        this.nextWeekBuy = nextWeekBuy;
    }

    public String getNextWeekSpread() {
        return nextWeekSpread;
    }

    public void setNextWeekSpread(String nextWeekSpread) {
        this.nextWeekSpread = nextWeekSpread;
    }

    public String getQuarterBuy() {
        return quarterBuy;
    }

    public void setQuarterBuy(String quarterBuy) {
        this.quarterBuy = quarterBuy;
    }

    public String getQuarterSpread() {
        return quarterSpread;
    }

    public void setQuarterSpread(String quarterSpread) {
        this.quarterSpread = quarterSpread;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "OKSymbolTickersLevelItem{" +
                "symbol='" + symbol + '\'' +
                ", date='" + date + '\'' +
                ", sell='" + sell + '\'' +
                ", thisWeekBuy='" + thisWeekBuy + '\'' +
                ", thisWeekSpread='" + thisWeekSpread + '\'' +
                ", nextWeekBuy='" + nextWeekBuy + '\'' +
                ", nextWeekSpread='" + nextWeekSpread + '\'' +
                ", quarterBuy='" + quarterBuy + '\'' +
                ", quarterSpread='" + quarterSpread + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public int getItemType() {
        return OKExpandableItemAdapter.TYPE_LEVEL_1;
    }
}
