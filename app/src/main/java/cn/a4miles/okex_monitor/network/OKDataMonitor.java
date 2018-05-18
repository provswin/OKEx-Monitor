package cn.a4miles.okex_monitor.network;

import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import cn.a4miles.okex_monitor.util.OKDateUtil;
import cn.a4miles.okex_monitor.beans.OKDateTickers;
import cn.a4miles.okex_monitor.network.consts.OKFutureRestApi;
import cn.a4miles.okex_monitor.network.consts.OKStockRestApi;
import cn.a4miles.okex_monitor.network.impl.OKDataMonitorInterface;

/**
 * 负责监控数据与打印数据
 *
 * @author
 * @create 2018-05-15 上午9:56
 **/
public class OKDataMonitor {
    private OKDateTickers mThisWeekFutureTickers, mNextWeekFutureTickers, mQuarterFutureTickers;
    private OKDateTickers mSpotTickers;
    private Timer timer;
    private String mSymbol;
    private long mInterval;

    public OKDataMonitor(String symbol, long interval) {
        mSymbol = symbol;
        mInterval = interval;
    }

    public String getMonitorSymbol() {
        return mSymbol;
    }

    public void startMonitor(final OKDataMonitorInterface callback) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestTickers(mSymbol, callback);
            }
        }, 0, mInterval);
    }

    private void requestTickers(final String symbol, final OKDataMonitorInterface callback) {
        final OKFutureRestApi futureRestApi = new OKFutureRestApi();
        // 请求期货数据
        // 1. this_week
        // 2. next_week
        // 3. quarter
        // 4. spot
        // 5. calculate spread

        futureRestApi.getFutureTickers(symbol + "_usd", "this_week", new OKHttpManager.ResultCallback<OKDateTickers>() {
            @Override
            public void onError(Exception e) {
                System.out.println("request 【this_week】 error :" + OKDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
            }

            @Override
            public void onResponse(OKDateTickers futureTickers) {
                mThisWeekFutureTickers = futureTickers;
                futureRestApi.getFutureTickers(symbol + "_usd", "next_week", new OKHttpManager.ResultCallback<OKDateTickers>() {
                    @Override
                    public void onError(Exception e) {
                        System.out.println("request 【next_week】 error :" + OKDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                    }

                    @Override
                    public void onResponse(OKDateTickers futureTickers) {
                        mNextWeekFutureTickers = futureTickers;

                        futureRestApi.getFutureTickers(symbol + "_usd", "quarter", new OKHttpManager.ResultCallback<OKDateTickers>() {
                            @Override
                            public void onError(Exception e) {
                                System.out.println("request 【quarter】 error :" + OKDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                            }

                            @Override
                            public void onResponse(OKDateTickers futureTickers) {
                                mQuarterFutureTickers = futureTickers;
                                OKStockRestApi stockRestApi = new OKStockRestApi();
                                stockRestApi.getTickers(symbol + "_usdt", new OKHttpManager.ResultCallback<OKDateTickers>() {
                                    @Override
                                    public void onError(Exception e) {

                                    }

                                    @Override
                                    public void onResponse(OKDateTickers spotTickers) {
                                        mSpotTickers = spotTickers;

                                        callback.postResult(mThisWeekFutureTickers,
                                                mNextWeekFutureTickers, mQuarterFutureTickers,
                                                mSpotTickers, mSymbol);

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void stopMonitor() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
