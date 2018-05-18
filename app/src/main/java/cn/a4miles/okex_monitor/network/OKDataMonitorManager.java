package cn.a4miles.okex_monitor.network;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import cn.a4miles.okex_monitor.beans.OKDateTickers;
import cn.a4miles.okex_monitor.entity.OKSymbolTickersLevelItem;
import cn.a4miles.okex_monitor.network.impl.OKDataMonitorInterface;

public class OKDataMonitorManager extends Observable {

    private HashMap<String, OKDataMonitor> monitors = new HashMap<>();

    private HashMap<String, OKSymbolTickersLevelItem> datas = new HashMap<>();

    private List<String> mSymbols;

    private Map<String, Object> obs = new HashMap<String, Object>();

    private static volatile OKDataMonitorManager manager = null;

    public static OKDataMonitorManager getSingleton(List<String> symbols, long interval){
        if(manager == null){
            synchronized (OKDataMonitorManager.class){
                if(manager == null){
                    manager = new OKDataMonitorManager(symbols, interval);
                }
            }
        }
        return manager;
    }

    public OKSymbolTickersLevelItem getTicker(String symbol) {
        return datas.get(symbol);
    }

    private OKDataMonitorManager(List<String> symbols, long interval) {
        mSymbols = symbols;
        for (int i = 0; i < mSymbols.size(); i++) {
            String symbol = mSymbols.get(i);
            if (!TextUtils.isEmpty(symbol)) {
                OKDataMonitor monitor = new OKDataMonitor(symbol, interval);
                monitors.put(symbol, monitor);
            }
        }
    }

    public void startMonitor(String symbol) {
        if (TextUtils.isEmpty(symbol)) {
            throw new InvalidParameterException("Expect a symbol!");
        }

        OKDataMonitor monitor = monitors.get(symbol);
        if (monitor != null) {
            monitor.startMonitor(new OKDataMonitorInterface() {
                @Override
                public void postResult(OKDateTickers thisWeek, OKDateTickers nextWeek, OKDateTickers quarter, OKDateTickers spot, String symbol) {
                    BigDecimal thisWeekSpread = new BigDecimal(thisWeek.getOKTicker().getBuy
                            ()).divide(new BigDecimal(spot.getOKTicker
                            ().getSell()), 6, RoundingMode.HALF_UP);

                    BigDecimal nextWeekSpread = new BigDecimal(nextWeek.getOKTicker().getBuy
                            ()).divide(new BigDecimal(spot.getOKTicker
                            ().getSell()), 6, RoundingMode.HALF_UP);

                    BigDecimal quarterSpread = new BigDecimal(quarter.getOKTicker().getBuy
                            ()).divide(new BigDecimal(spot.getOKTicker
                            ().getSell()), 6, RoundingMode.HALF_UP);

                    OKSymbolTickersLevelItem resultItem = new OKSymbolTickersLevelItem();
                    resultItem.setSymbol(symbol);
                    resultItem.setSell(spot.getOKTicker().getSell());
                    resultItem.setDate(spot.getDate());
                    resultItem.setPosition(mSymbols.indexOf(symbol));
                    resultItem.setThisWeekBuy(thisWeek.getOKTicker().getBuy());
                    resultItem.setThisWeekSpread(thisWeekSpread.toString());
                    resultItem.setNextWeekBuy(nextWeek.getOKTicker().getBuy());
                    resultItem.setNextWeekSpread(nextWeekSpread.toString());
                    resultItem.setQuarterBuy(quarter.getOKTicker().getBuy());
                    resultItem.setQuarterSpread(quarterSpread.toString());
                    datas.put(symbol, resultItem);
                    doBusiness(resultItem, symbol);
                }
            });
        }
    }

    public void stopMonitor(String symbol) {
        if (TextUtils.isEmpty(symbol)) {
            throw new InvalidParameterException("Expect a symbol!");
        }

        OKDataMonitor monitor = monitors.get(symbol);
        if (monitor != null) {
            monitor.stopMonitor();
        }
    }

    public void stopAllMonitors() {
        for (String key : monitors.keySet()) {
            if (monitors.get(key) != null) {
                monitors.get(key).stopMonitor();
            }
        }
    }

    public void resumeAllMonitors() {
        for (String key : monitors.keySet()) {
            if (monitors.get(key) != null) {
                this.startMonitor(key);
            }
        }
    }

    public void doBusiness(OKSymbolTickersLevelItem item, String symbol) {
        // 设置修改状态
        super.setChanged();
        // 通知观察者
        this.notifyObservers(item, symbol);
    }

    public void notifyObservers(OKSymbolTickersLevelItem item, String symbol) {
        Iterator<String> it = obs.keySet().iterator();
        String key;
        while (it.hasNext()) {
            key = it.next();
            if (key.startsWith(symbol)) {
                OKDataObserver ob = (OKDataObserver) obs.get(key);
                ob.update(this, item, symbol);
            }
        }
    }

    public synchronized void addObserver(String name, Observer o) {
        obs.put(name, o);
    }

    public synchronized void updateObserver(String name, Observer o) {
        Iterator<String> it = obs.keySet().iterator();
        String key;
        while (it.hasNext()) {
            key = it.next();
            if (key.equals(name)) {
                obs.put(key, o);
                break;
            }
        }
    }

    public synchronized void deleteObserver(Observer o) {
        if (obs.values().contains(o)) {
            Iterator<String> it = obs.keySet().iterator();
            String key = null;
            while (it.hasNext()) {
                key = it.next();
                if (obs.get(key).equals(o)) {
                    obs.remove(key);
                    break;
                }
            }
        }
    }

    public synchronized void deleteObserverByKey(String prefix) {
        if (TextUtils.isEmpty(prefix)) {
            return;
        }

        List<Observer> observers = new ArrayList<>();

        Iterator<String> it = obs.keySet().iterator();
        String key;
        while (it.hasNext()) {
            key = it.next();
            if (key.startsWith(prefix)) {
                if ((Observer)obs.get(key) != null) {
                    observers.add((Observer) obs.get(key));
                }
            }
        }

        for (Observer ob :
                observers) {
            deleteObserver(ob);
        }
    }

    public synchronized OKSymbolTickersLevelItem getData(String symbol) {
        if (TextUtils.isEmpty(symbol)) {
            return null;
        }
        return datas.get(symbol);
    }
}
