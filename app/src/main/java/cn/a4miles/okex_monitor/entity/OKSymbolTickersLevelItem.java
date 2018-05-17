package cn.a4miles.okex_monitor.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.a4miles.okex_monitor.adapter.OKExpandableItemAdapter;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public class OKSymbolTickersLevelItem implements MultiItemEntity {
    private String contractType;

    private String date;

    private String buy;

    private String thisWeekSell;

    private String thisWeekSpread;

    private String nextWeekSell;

    private String nextWeekSpread;

    private String quarterSell;

    private String quarterSpread;

    private int position;

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getThisWeekSell() {
        return thisWeekSell;
    }

    public void setThisWeekSell(String thisWeekSell) {
        this.thisWeekSell = thisWeekSell;
    }

    public String getThisWeekSpread() {
        return thisWeekSpread;
    }

    public void setThisWeekSpread(String thisWeekSpread) {
        this.thisWeekSpread = thisWeekSpread;
    }

    public String getNextWeekSell() {
        return nextWeekSell;
    }

    public void setNextWeekSell(String nextWeekSell) {
        this.nextWeekSell = nextWeekSell;
    }

    public String getNextWeekSpread() {
        return nextWeekSpread;
    }

    public void setNextWeekSpread(String nextWeekSpread) {
        this.nextWeekSpread = nextWeekSpread;
    }

    public String getQuarterSell() {
        return quarterSell;
    }

    public void setQuarterSell(String quarterSell) {
        this.quarterSell = quarterSell;
    }

    public String getQuarterSpread() {
        return quarterSpread;
    }

    public void setQuarterSpread(String quarterSpread) {
        this.quarterSpread = quarterSpread;
    }

    @Override
    public String toString() {
        return "OKSymbolTickersLevelItem{" +
                "contractType='" + contractType + '\'' +
                ", date='" + date + '\'' +
                ", buy='" + buy + '\'' +
                ", thisWeekSell='" + thisWeekSell + '\'' +
                ", thisWeekSpread='" + thisWeekSpread + '\'' +
                ", nextWeekSell='" + nextWeekSell + '\'' +
                ", nextWeekSpread='" + nextWeekSpread + '\'' +
                ", quarterSell='" + quarterSell + '\'' +
                ", quarterSpread='" + quarterSpread + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public int getItemType() {
        return OKExpandableItemAdapter.TYPE_LEVEL_1;
    }
}
