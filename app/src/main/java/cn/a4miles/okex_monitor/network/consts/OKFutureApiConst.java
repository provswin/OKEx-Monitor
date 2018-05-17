package cn.a4miles.okex_monitor.network.consts;

/**
 * 期货行情接口
 *
 * @author
 * @create 2018-05-14 下午5:37
 **/
public class OKFutureApiConst {
    /**
     * 期货行情URL
     */
    public final static String FUTURE_TICKER_URL = "/api/v1/future_ticker.do";
    /**
     * 期货指数查询URL
     */
    public final static String FUTURE_INDEX_URL = "/api/v1/future_index.do";

    /**
     * 期货交易记录查询URL
     */
    public final static String FUTURE_TRADES_URL = "/api/v1/future_trades.do";

    /**
     * 期货市场深度查询URL
     */
    public final static String FUTURE_DEPTH_URL = "/api/v1/future_depth.do";
    /**
     * 美元-人民币汇率查询URL
     */
    public final static String FUTURE_EXCHANGE_RATE_URL = "/api/v1/exchange_rate.do";

    /**
     * 期货取消订单URL
     */
    public final static String FUTURE_CANCEL_URL = "/api/v1/future_cancel.do";

    /**
     * 期货下单URL
     */
    public final static String FUTURE_TRADE_URL = "/api/v1/future_trade.do";

    /**
     * 期货账户信息URL
     */
    public final static String FUTURE_USERINFO_URL = "/api/v1/future_userinfo.do";

    /**
     * 逐仓期货账户信息URL
     */
    public final static String FUTURE_USERINFO_4FIX_URL = "/api/v1/future_userinfo_4fix.do";

    /**
     * 期货持仓查询URL
     */
    public final static String FUTURE_POSITION_URL = "/api/v1/future_position.do";

    /**
     * 期货逐仓持仓查询URL
     */
    public final static String FUTURE_POSITION_4FIX_URL = "/api/v1/future_position_4fix.do";

    /**
     * 用户期货订单信息查询URL
     */
    public final static String FUTURE_ORDER_INFO_URL = "/api/v1/future_order_info.do";
}
