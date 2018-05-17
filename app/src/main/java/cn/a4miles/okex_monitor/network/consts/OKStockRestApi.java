package cn.a4miles.okex_monitor.network.consts;


import cn.a4miles.okex_monitor.configs.OKConfig;
import cn.a4miles.okex_monitor.network.OKHttpManager;
import cn.a4miles.okex_monitor.network.OKHttpManager.ResultCallback;

/**
 * 现货Api
 *
 * @author
 * @create 2018-05-14 下午5:50
 **/
public class OKStockRestApi {
    public void getTickers(String symbol, ResultCallback callback) {
        OKHttpManager.getAsyn(OKConfig.url_prex + OKStockApiConst.TICKER_URL + "?symbol=" + symbol, callback);
    }
}
