package cn.a4miles.okex_monitor.network.consts;

/**
 * 现货Api
 *
 * @author
 * @create 2018-05-14 下午5:50
 **/
public class OKStockApiConst {
    /**
     * 现货行情URL
     */
    public final static String TICKER_URL = "/api/v1/ticker.do";

    /**
     * 现货市场深度URL
     */
    public final static String DEPTH_URL = "/api/v1/depth.do";

    /**
     * 现货历史交易信息URL
     */
    public final static String TRADES_URL = "/api/v1/trades.do";

    /**
     * 现货获取用户信息URL
     */
    public final static String USERINFO_URL = "/api/v1/userinfo.do";

    /**
     * 现货 下单交易URL
     */
    public final static String TRADE_URL = "/api/v1/trade.do";

    /**
     * 现货 批量下单URL
     */
    public final static String BATCH_TRADE_URL = "/api/v1/batch_trade.do";

    /**
     * 现货 撤销订单URL
     */
    public final static String CANCEL_ORDER_URL = "/api/v1/cancel_order.do";

    /**
     * 现货 获取用户订单URL
     */
    public final static String ORDER_INFO_URL = "/api/v1/order_info.do";

    /**
     * 现货 批量获取用户订单URL
     */
    public final static String ORDERS_INFO_URL = "/api/v1/orders_info.do";

    /**
     * 现货 获取历史订单信息，只返回最近七天的信息URL
     */
    public final static String ORDER_HISTORY_URL = "/api/v1/order_history.do";
}
