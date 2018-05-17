package cn.a4miles.okex_monitor.adapter;

import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import cn.a4miles.okex_monitor.R;
import cn.a4miles.okex_monitor.util.DensityUtils;
import cn.a4miles.okex_monitor.util.OKTimeUtil;
import cn.a4miles.okex_monitor.beans.OKDateTickers;
import cn.a4miles.okex_monitor.entity.OKSymbolLevelItem;
import cn.a4miles.okex_monitor.entity.OKSymbolTickersLevelItem;
import cn.a4miles.okex_monitor.network.OKDataMonitor;
import cn.a4miles.okex_monitor.network.impl.OKDataMonitorInterface;

/**
 * Created by huhuaxiang on 2018/5/15.
 */

public class OKExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    private SparseArray<OKDataMonitor> monitors = new SparseArray<>();
    private HashMap<String, OKSymbolTickersLevelItem> tickersMap = new HashMap<>();
    private long mInterval;
    private HashMap<String, CountDownTimer> timerMap = new HashMap<>();

    public OKExpandableItemAdapter(List<MultiItemEntity> data, long interval) {
        super(data);

        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);

        mInterval = interval;

        for (int i = 0; i < data.size(); i++) {
            monitors.put(i, new OKDataMonitor((((OKSymbolLevelItem) data.get(i))).getSymbol(), mInterval));
            tickersMap.put(((OKSymbolLevelItem) data.get(i)).getSymbol(), ((OKSymbolLevelItem)
                    data.get(i)).getSubItem(0));
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case TYPE_LEVEL_0:
                final OKSymbolLevelItem symbolLevelItem = (OKSymbolLevelItem) item;
                helper.setText(R.id.tvSymbol, ((OKSymbolLevelItem) item).getSymbol())
                        .setImageResource(R.id.iv, symbolLevelItem.isExpanded() ? R.mipmap.arrow_b : R.mipmap
                                .arrow_r);

                helper.setText(R.id.tvTimer, "" + mInterval / 1000);

                final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) helper.getView(R.id
                        .progressView).getLayoutParams();

                final int width = DensityUtils.getScreenWidth(mContext);

                // 倒计时10秒，1秒刷新一次数字
                // 配合网络请求时间
                final CountDownTimer timer = new CountDownTimer(mInterval + 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (timerMap.get(symbolLevelItem.getSymbol()).equals(this)) {
                            double percent = (millisUntilFinished) / 1000 * 0.1;
                            params.width = (int) (width * percent);
                            params.height = DensityUtils.dp2px(mContext, 80);
                            helper.getView(R.id.progressView).setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onFinish() {
                        // 结束后重新计时
                        if (timerMap.get(symbolLevelItem.getSymbol()).equals(this)) {
                            double percent = (mInterval) / 1000 * 0.1;
                            params.width = (int) (width * percent);
                            params.height = DensityUtils.dp2px(mContext, 80);
                            helper.getView(R.id.progressView).setLayoutParams(params);
                            start();
                        }
                    }
                };

                if (!timerMap.containsValue(timer)) {
                    timerMap.put(symbolLevelItem.getSymbol(), timer);
                }

                if (symbolLevelItem.isExpanded()) {
                    helper.setVisible(R.id.progressView, true);
                    timer.start();
                } else {
                    helper.setVisible(R.id.progressView, false);
                    timer.cancel();
                }

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (symbolLevelItem.isExpanded()) {
                            collapse(pos);
                            OKDataMonitor monitor = monitors.get(symbolLevelItem.getPosition());
                            monitor.stopMonitor();
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1: {
                final OKSymbolTickersLevelItem symbolTickersLevelItem =
                        (OKSymbolTickersLevelItem) item;

                // 找到当前二级菜单的父Item，并获取Symbol
                final OKSymbolLevelItem parentItem = (OKSymbolLevelItem) getData().get(getParentPosition
                        (symbolTickersLevelItem));
                final String parentSymbol = parentItem.getSymbol();

                if (!TextUtils.isEmpty(tickersMap.get(parentSymbol).getContractType())) {
                    // 如果已经有数据，则显示当前数据后再请求
                    setViewWithTickers(tickersMap.get(parentSymbol), helper);
                } else {
                    helper.setVisible(R.id.progressBar, true);
                    helper.setVisible(R.id.llContent, false);
                }

                requestTickers(helper, symbolTickersLevelItem, parentItem, parentSymbol);
            }
            break;
        }
    }

    private void requestTickers(final BaseViewHolder helper, final OKSymbolTickersLevelItem symbolTickersLevelItem, final OKSymbolLevelItem parentItem, final String parentSymbol) {
        OKDataMonitor monitor = monitors.get(symbolTickersLevelItem.getPosition());

        monitor.startMonitor(new OKDataMonitorInterface() {
            @Override
            public void postResult(OKDateTickers thisWeek, OKDateTickers nextWeek,
                                   OKDateTickers quarter, OKDateTickers spot, String
                                           symbol) {
                if (parentSymbol.equals(symbol)) {
                    helper.setVisible(R.id.progressBar, false);
                    helper.setVisible(R.id.llContent, true);

                    BigDecimal thisWeekSpread = new BigDecimal(thisWeek.getOKTicker
                            ().getSell()).divide(new BigDecimal(spot.getOKTicker().getBuy
                            ()), 6, RoundingMode.HALF_UP);

                    BigDecimal nextWeekSpread = new BigDecimal(nextWeek.getOKTicker
                            ().getSell()).divide(new BigDecimal(spot.getOKTicker().getBuy
                            ()), 6, RoundingMode.HALF_UP);

                    BigDecimal quarterSpread = new BigDecimal(quarter
                            .getOKTicker().getSell()).divide(new BigDecimal(spot
                            .getOKTicker().getBuy()), 6, RoundingMode.HALF_UP);

                    OKSymbolTickersLevelItem resultItem = new OKSymbolTickersLevelItem();
                    resultItem.setContractType(symbol);
                    resultItem.setBuy(spot.getOKTicker().getBuy());
                    resultItem.setDate(spot.getDate());
                    resultItem.setPosition(getParentPosition(symbolTickersLevelItem));
                    resultItem.setThisWeekSell(thisWeek.getOKTicker().getSell());
                    resultItem.setThisWeekSpread(thisWeekSpread.toString());
                    resultItem.setNextWeekSell(nextWeek.getOKTicker().getSell());
                    resultItem.setNextWeekSpread(nextWeekSpread.toString());
                    resultItem.setQuarterSell(quarter.getOKTicker().getSell());
                    resultItem.setQuarterSpread(quarterSpread.toString());
                    tickersMap.put(symbol, resultItem);

                    setViewWithTickers(resultItem, helper);
                }
            }
        });
    }

    private void setViewWithTickers(OKSymbolTickersLevelItem item, BaseViewHolder helper) {
        // This Week
        helper.setText(R.id.tvThisWeekTime, "[this week] trade signal " + OKTimeUtil.getStrDateFromLong
                (Long.valueOf(item.getDate()) * 1000, OKTimeUtil.PATTERN_YMDHMS_1));

        helper.setText(R.id.tvThisWeekFutureSell, "sell this week at " +
                item.getThisWeekSell());

        helper.setText(R.id.tvThisWeekSpotBuy, "buy spot at " +
                item.getBuy());

        helper.setText(R.id.tvThisWeekPercent, item.getThisWeekSpread());

        // Next Week
        helper.setText(R.id.tvNextWeekTime, "[next week] trade signal " + OKTimeUtil.getStrDateFromLong
                (Long.valueOf(item.getDate()) * 1000, OKTimeUtil
                        .PATTERN_YMDHMS_1));

        helper.setText(R.id.tvNextWeekFutureSell, "sell next week at " +
                item.getNextWeekSell());

        helper.setText(R.id.tvNextWeekSpotBuy, "buy spot at " +
                item.getBuy());

        helper.setText(R.id.tvNextWeekPercent, item.getNextWeekSpread());

        // Quarter
        helper.setText(R.id.tvQuarterTime, "[quarter] trade signal " +
                OKTimeUtil.getStrDateFromLong(Long.valueOf(item.getDate
                        ()) * 1000, OKTimeUtil.PATTERN_YMDHMS_1));

        helper.setText(R.id.tvQuarterFutureSell, "sell quarter at " +
                item.getQuarterSell());

        helper.setText(R.id.tvQuarterSpotBuy, "buy spot at " +
                item.getBuy());

        helper.setText(R.id.tvQuarterPercent, item.getQuarterSpread());

        setSpreadColor(new BigDecimal(item.getThisWeekSpread()), helper,
                R.id.tvThisWeekPercent);

        setSpreadColor(new BigDecimal(item.getNextWeekSpread()), helper,
                R.id.tvNextWeekPercent);

        setSpreadColor(new BigDecimal(item.getQuarterSpread()), helper, R
                .id.tvQuarterPercent);
    }

    private void setSpreadColor(BigDecimal spread, BaseViewHolder helper, @IdRes int viewId) {
        if (spread.compareTo(new BigDecimal("1")) == 0) {
            helper.setTextColor(viewId, mContext.getResources
                    ().getColor(android.R.color.black));
        } else if (spread.compareTo(new BigDecimal("1")) > 0) {
            helper.setTextColor(viewId, mContext.getResources
                    ().getColor(R.color.premiumColor));
        } else {
            helper.setTextColor(viewId, mContext.getResources
                    ().getColor(R.color.agioColor));
        }
    }
}
