package cn.a4miles.okex_monitor.network.impl;

import cn.a4miles.okex_monitor.beans.OKDateTickers;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public interface OKDataMonitorInterface {
    void postResult(OKDateTickers thisWeek, OKDateTickers nextWeek, OKDateTickers quarter,
                    OKDateTickers spot, String symbol);
}
