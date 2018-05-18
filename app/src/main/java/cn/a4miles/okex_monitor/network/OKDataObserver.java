package cn.a4miles.okex_monitor.network;

import java.util.Observable;
import java.util.Observer;

import cn.a4miles.okex_monitor.entity.OKSymbolTickersLevelItem;

public abstract class OKDataObserver implements Observer{
    public abstract void update(Observable o, OKSymbolTickersLevelItem item, String symbol);

    @Override
    public void update(Observable o, Object arg) {

    }
}
