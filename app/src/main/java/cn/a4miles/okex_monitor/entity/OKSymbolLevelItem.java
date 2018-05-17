package cn.a4miles.okex_monitor.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import cn.a4miles.okex_monitor.adapter.OKExpandableItemAdapter;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public class OKSymbolLevelItem extends AbstractExpandableItem<OKSymbolTickersLevelItem> implements MultiItemEntity {
    private String symbol;

    private int position;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "OKSymbolLevelItem{" +
                "symbol='" + symbol + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public int getItemType() {
        return OKExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
