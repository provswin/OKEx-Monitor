package cn.a4miles.okex_monitor.network.consts;

import cn.a4miles.okex_monitor.configs.OKConfig;
import cn.a4miles.okex_monitor.network.OKHttpManager;
import cn.a4miles.okex_monitor.network.OKHttpManager.ResultCallback;

/**
 * Future接口类
 *
 * @author
 * @create 2018-05-14 下午5:32
 **/
public class OKFutureRestApi {
    public void getFutureTickers(String symbol, String contractType, ResultCallback callback) {
        OKHttpManager.getAsyn(OKConfig.url_prex + OKFutureApiConst.FUTURE_TICKER_URL + "?symbol=" + symbol +
                "&contract_type=" + contractType, callback);
    }
}